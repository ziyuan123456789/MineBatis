package org.example.MineBatisUtils;

import lombok.extern.slf4j.Slf4j;
import org.example.MineBatisUtils.TypeHandler.Impl.IntegerHandler;
import org.example.MineBatisUtils.TypeHandler.Impl.LocalDateTimeHandler;
import org.example.MineBatisUtils.TypeHandler.Impl.StringHandler;
import org.example.MineBatisUtils.TypeHandler.TypeHandler;
import org.example.OrmAnnotations.MySelect;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Slf4j
public class MapperJdkProxyFactory {

    private static Map<Class<?>, TypeHandler> handlerMap = new HashMap<>();

    static {
        handlerMap.put(int.class, new IntegerHandler());
        handlerMap.put(String.class, new StringHandler());
        handlerMap.put(Integer.class, new IntegerHandler());
        handlerMap.put(LocalDateTime.class, new LocalDateTimeHandler());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getMapper(Class<T> mapper) {
        Object proxyInstance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }
                Connection connection = getConnection();
                MySelect annotation = method.getAnnotation(MySelect.class);
                String sql = annotation.value();
                if (sql == null || sql.isEmpty()) {
                    log.warn("sql为空");
                    throw new IllegalStateException("sql为空");
                }
                Map<String, Object> paramValueMapping = new HashMap<>();
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    paramValueMapping.put(parameter.getName(), args[i]);

                }
                ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
                GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
                String jdbcSql = genericTokenParser.parse(sql);
                PreparedStatement statement = connection.prepareStatement(jdbcSql);
                List<ParameterMapping> parameterMapping = parameterMappingTokenHandler.getParameterMapping();
                for (int i = 0; i < parameterMapping.size(); i++) {
                    String argName = parameterMapping.get(i).getProperty();
                    Class<?> clazz = paramValueMapping.get(argName).getClass();
                    handlerMap.get(clazz).setParameter(statement, i + 1, paramValueMapping.get(argName));
                }
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                Class<?> clazz = method.getReturnType();
                Map<String, Method> fieldNames = new HashMap<>();
                PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
                for (PropertyDescriptor prop : props) {
                    Method setter = prop.getWriteMethod();
                    if (setter != null) {
                        String fieldName = prop.getName().toLowerCase();
                        fieldNames.put(fieldName, setter);
                    }
                }

                ResultSetMetaData metaData = statement.getMetaData();
                List<String> columnNames = new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    columnNames.add(metaData.getColumnName(i + 1).toLowerCase());
                }
                while (resultSet.next()) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    for (String columnName : columnNames) {
                        Method setter = fieldNames.get(columnName);
                        TypeHandler typeHandler = handlerMap.get(setter.getParameterTypes()[0]);
                        setter.invoke(instance, typeHandler.getResult(resultSet, columnName));

                    }
                    return instance;
                }


                connection.close();
                return null;
            }

            private static Connection getConnection() throws SQLException {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
                return connection;
            }
        });
        return (T) proxyInstance;
    }
}
