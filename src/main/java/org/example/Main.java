package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.Mapper.UserMapper;
import org.example.MineBatisUtils.Io.Resources;
import org.example.MineBatisUtils.MapperJdkProxyFactory;
import org.example.MineBatisUtils.session.SqlSession;
import org.example.MineBatisUtils.session.SqlSessionFactory;
import org.example.MineBatisUtils.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        //xxx:收了神通吧c3p0,别污染控制台了
        Logger.getLogger("com.mchange.v2").setLevel(Level.OFF);
        InputStream inputStream = Resources.getResourceAsSteam("minebatis-config.xml");
        //xxx:建造一个SqlSessionFactory出来,构建defaultSqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //xxx:确定工厂后生产session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //xxx:SqlSession生产Jdk代理类
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            log.info(userMapper.getOneUser(1).toString());
//            log.info(userMapper.getAllUser(0).toString());
        } catch (Exception e) {
            log.error("执行失败", e);
        }

//        UserMapper userMapper=new MapperJdkProxyFactory().getMapper(UserMapper.class);
//        log.info(userMapper.getAllUser(1).toString());

    }
}