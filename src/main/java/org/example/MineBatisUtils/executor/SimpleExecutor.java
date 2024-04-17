package org.example.MineBatisUtils.executor;

import org.example.MineBatisUtils.GenericTokenParser;
import org.example.MineBatisUtils.ParameterMapping;
import org.example.MineBatisUtils.ParameterMappingTokenHandler;
import org.example.MineBatisUtils.configuration.BoundSql;
import org.example.MineBatisUtils.configuration.Configuration;
import org.example.MineBatisUtils.configuration.MappedStatement;
import org.example.MineBatisUtils.type.TypeHandler.TypeHandler;
import org.example.MineBatisUtils.type.TypeHandlerRegistry;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ziyuan
 * @since 2024.04
 */
//xxx:简单执行器
public class SimpleExecutor implements Executor {
    private TypeHandlerRegistry typeHandlerRegistry;

    private ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
    public SimpleExecutor(TypeHandlerRegistry typeHandlerRegistry){
        this.typeHandlerRegistry=typeHandlerRegistry;
    }

    @Override
    public <T> T query(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception {

        return null;
    }

    @Override
    public <T> T query(Configuration configuration, MappedStatement mappedStatement, Method method, Object[] args) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        String parameterType = mappedStatement.getResultType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        //xxx:把参数和参数名对应起来,放到map里
        Map<String, Object> paramValueMapping = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            paramValueMapping.put(parameter.getName(), args[i]);

        }
        String jdbcSql = boundSql.getJdbcsqlText();
        PreparedStatement statement = connection.prepareStatement(jdbcSql);
        //xxx:在替换#{}的同时,取出里面的参数名称封装为ParameterMapping,放到数组中,保存好顺序
        List<ParameterMapping> parameterMapping = parameterMappingTokenHandler.getParameterMapping();
        for (int i = 0; i < parameterMapping.size(); i++) {
            //xxx:拿到参数名字,根据名字找到对应的值,然后根据值的类型找到对应的处理器,进行setParameter
            String argName = parameterMapping.get(i).getProperty();
            Class<?> clazz = paramValueMapping.get(argName).getClass();
            //xxx:jdbc这个为什么不从0开始?反而从1开始?
            typeHandlerRegistry.getTypeHandlers().get(clazz).setParameter(statement, i + 1, paramValueMapping.get(argName));
        }
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        List<Object> returnList=new ArrayList<>();
        //xxx:先看看是不是一个List 如果是则取出泛型类型覆盖clazz,并且标记isList为true
        if(parameterTypeClass.isAssignableFrom(List.class)){
            Type returnType = method.getGenericReturnType();
            if(returnType instanceof ParameterizedType type){
                parameterTypeClass= (Class<?>) type.getActualTypeArguments()[0];
            }
        }
        Map<String, Method> fieldNames = new HashMap<>();
        PropertyDescriptor[] props = Introspector.getBeanInfo(parameterTypeClass).getPropertyDescriptors();
        for (PropertyDescriptor prop : props) {
            Method setter = prop.getWriteMethod();
            if (setter != null) {
                //xxx:无论怎么样全都改为小写,避免繁琐的大小写问题
                String fieldName = prop.getName().toLowerCase();
                fieldNames.put(fieldName, setter);
            }
        }
        //xxx:获取结果集的元数据,拿到字段名,然后根据字段名找到对应的setter方法,然后根据setter方法的参数类型找到对应的处理器,进行注入
        ResultSetMetaData metaData = statement.getMetaData();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            columnNames.add(metaData.getColumnName(i + 1).toLowerCase());
        }
        //xxx:调用无参构造器,构建一个实例
        while (resultSet.next()) {
            Object instance = parameterTypeClass.getDeclaredConstructor().newInstance();
            for (String columnName : columnNames) {
                Method setter = fieldNames.get(columnName);
                TypeHandler typeHandler = typeHandlerRegistry.getTypeHandlers().get(setter.getParameterTypes()[0]);
                setter.invoke(instance, typeHandler.getResult(resultSet, columnName));

            }
            returnList.add(instance);

        }
        parameterMappingTokenHandler.resetParameterMappings();
        connection.close();
        return (T) returnList;
    }


    @Override
    public <T> T selectQuery(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception {
        return null;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null) {
            return Class.forName(parameterType);
        }
        return null;
    }

    private BoundSql getBoundSql(String sql) {

        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String jdbcSql = genericTokenParser.parse(sql);
        return new BoundSql(sql, jdbcSql);
    }
}
