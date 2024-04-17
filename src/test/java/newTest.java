import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.Mapper.UserMapper;
import org.example.MineBatisUtils.Io.Resources;
import org.example.MineBatisUtils.session.SqlSession;
import org.example.MineBatisUtils.session.SqlSessionFactory;
import org.example.MineBatisUtils.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author ziyuan
 * @since 2024.04
 */
public class newTest {
    @Test
    public void test() throws Exception {
        //xxx:首先读取配置文件
        InputStream inputStream = Resources.getResourceAsSteam("minebatis-config.xml");
        //xxx:建造一个SqlSessionFactory出来,构建defaultSqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //xxx:确定工厂后生产session
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //xxx:SqlSession生产Jdk代理类
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(userMapper.getOneUser(1));
    }

    @Before
    public void before(){



    }

}
