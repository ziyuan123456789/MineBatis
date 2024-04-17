package org.example.MineBatisUtils.session;

import lombok.Data;
import org.example.MineBatisUtils.configuration.Configuration;
import org.example.MineBatisUtils.type.BaseTypeHandlerRegistry;
import org.example.MineBatisUtils.type.TypeHandler.Impl.IntHandler;
import org.example.MineBatisUtils.type.TypeHandler.Impl.IntegerHandler;
import org.example.MineBatisUtils.type.TypeHandler.Impl.LocalDateTimeHandler;
import org.example.MineBatisUtils.type.TypeHandler.Impl.StringHandler;
import org.example.MineBatisUtils.type.TypeHandlerRegistry;

import java.sql.Connection;
import java.time.LocalDateTime;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Data
//xxx:这个是默认工厂,直接硬编码注册TypeHandler,再另一个实现类由AutumnIoc接手,自动扫描注册
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    private TypeHandlerRegistry typeHandlerRegistry;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
        typeHandlerRegistry = new BaseTypeHandlerRegistry();
        typeHandlerRegistry.register(int.class, new IntHandler());
        typeHandlerRegistry.register(String.class, new StringHandler());
        typeHandlerRegistry.register(Integer.class, new IntegerHandler());
        typeHandlerRegistry.register(LocalDateTime.class, new LocalDateTimeHandler());
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration,typeHandlerRegistry);
    }

    //xxx:给你一个切换Connection的机会
    @Override
    public SqlSession openSession(Connection connection) {
        return null;
    }
}
