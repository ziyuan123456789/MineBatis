package org.example.MineBatisUtils.session;

import lombok.extern.slf4j.Slf4j;
import org.example.MineBatisUtils.configuration.Configuration;
import org.example.MineBatisUtils.configuration.MappedStatement;
import org.example.MineBatisUtils.executor.Executor;
import org.example.MineBatisUtils.executor.SimpleExecutor;
import org.example.MineBatisUtils.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Slf4j
//xxx:如何水代码行数?
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Executor executor ;

    public DefaultSqlSession(Configuration configuration, TypeHandlerRegistry typeHandlerRegistry) {
        this.configuration = configuration;
        executor=new SimpleExecutor(typeHandlerRegistry);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) throws Exception {
        Object proxyInstance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    String className = method.getDeclaringClass().getName();
                    String statementId = className + "." + methodName;
                    Type genericReturnType = method.getGenericReturnType();
                    MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                    if (mappedStatement == null) {
                        throw new IllegalStateException("没检测到标签" + statementId);
                    }
//                        String sql = mappedStatement.getSql();
//                        //xxx:把参数和参数名对应起来,放到map里
//                        Map<String, Object> paramValueMapping = new HashMap<>();
//                        Parameter[] parameters = method.getParameters();
//                        for (int i = 0; i < parameters.length; i++) {
//                            Parameter parameter = parameters[i];
//                            paramValueMapping.put(parameter.getName(), args[i]);
//
//                        }
//                        //xxx:构建解析器,把mybatis#{}风格转化为jdbc ?风格
//                        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
//                        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
//                        String jdbcSql = genericTokenParser.parse(sql);
//                        Connection connection = configuration.getDataSource().getConnection();
//                        PreparedStatement statement = connection.prepareStatement(jdbcSql);
//                        //xxx:在替换#{}的同时,取出里面的参数名称封装为ParameterMapping,放到数组中,保存好顺序
//                        List<ParameterMapping> parameterMapping = parameterMappingTokenHandler.getParameterMapping();
//                        for (int i = 0; i < parameterMapping.size(); i++) {
//                            //xxx:拿到参数名字,根据名字找到对应的值,然后根据值的类型找到对应的处理器,进行setParameter
//                            String argName = parameterMapping.get(i).getProperty();
//                            Class<?> clazz = paramValueMapping.get(argName).getClass();
//                            //xxx:jdbc这个为什么不从0开始?反而从1开始?
//                            typeHandlerRegistry.getTypeHandlers().get(clazz).setParameter(statement, i + 1, paramValueMapping.get(argName));
//                        }

                    String sqlCommandType = mappedStatement.getSqlCommandType();
                    switch (sqlCommandType) {
                        case "insert":
                            return insert(statementId, method, args);
                        case "delete":
                            return delete(statementId, method, args);
                        case "update":
                            return update(statementId, method, args);
                        case "select":
                            if (genericReturnType instanceof ParameterizedType) {
                                return selectList(statementId, method, args);
                            } else {
                                return selectOne(statementId, method, args);
                            }
                        default:
                            throw new UnsupportedOperationException("你写的什么?" + sqlCommandType);
                    }
                }
            }
        );
        return (T) proxyInstance;
    }


    @Override
    public <T> List<T> selectList(String statementId, Method method, Object[] args) throws Exception {
        List returnList;
        try {
            MappedStatement ms = this.configuration.getMappedStatement(statementId);
            returnList = this.executor.query(configuration,ms,method,args);
        } catch (Exception e) {
            log.error("sql执行失败");
            throw e;
        }
        return returnList;
    }

    @Override
    public <T> T selectOne(String statementId, Method method, Object[] args) throws Exception {
        List<T> list = this.selectList(statementId, method,args);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new RuntimeException("不唯一" + list.size());
        } else {
            return null;
        }
    }

    @Override
    public <T> T insert(String statementId, Method method, Object[] args) throws Exception {
        return update(statementId, method, args);
    }

    @Override
    public <T> T update(String statementId, Method method, Object[] args) throws Exception {
        return null;
    }

    @Override
    public <T> T delete(String statementId, Method method, Object[] args) throws Exception {
        return update(statementId, method, args);
    }
}
