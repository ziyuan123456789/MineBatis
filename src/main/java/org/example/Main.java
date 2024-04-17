package org.example;

import com.mchange.v2.c3p0.impl.NewPooledConnection;
import lombok.extern.slf4j.Slf4j;
import org.example.Mapper.UserMapper;
import org.example.MineBatisUtils.Io.Resources;
import org.example.MineBatisUtils.MapperJdkProxyFactory;
import org.example.MineBatisUtils.ParameterMapping;
import org.example.MineBatisUtils.configuration.BoundSql;
import org.example.MineBatisUtils.session.SqlSession;
import org.example.MineBatisUtils.session.SqlSessionFactory;
import org.example.MineBatisUtils.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = Resources.getResourceAsSteam("minebatis-config.xml");
        //xxx:建造一个SqlSessionFactory出来,构建defaultSqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //xxx:确定工厂后生产session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //xxx:SqlSession生产Jdk代理类
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            log.info(userMapper.getOneUser(1,"1").toString());
            log.info(userMapper.getAllUser(0).toString());
        } catch (Exception e) {
            log.error("执行失败", e);
        }

        UserMapper userMapper=new MapperJdkProxyFactory().getMapper(UserMapper.class);
        log.info(userMapper.getAllUser(1).toString());

    }
}