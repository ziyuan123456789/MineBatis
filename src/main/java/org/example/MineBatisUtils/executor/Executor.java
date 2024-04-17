package org.example.MineBatisUtils.executor;

import org.example.MineBatisUtils.configuration.Configuration;
import org.example.MineBatisUtils.configuration.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author ziyuan
 * @since 2024.04
 */
//xxx:sql执行器
public interface Executor {
    //xxx:查询
    <T> T query(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception;

    <T> T query(Configuration configuration, MappedStatement mappedStatement, Method method, Object[] params) throws Exception;


    <T> T selectQuery(Configuration configuration, MappedStatement mappedStatement, Object[] params) throws Exception;
}
