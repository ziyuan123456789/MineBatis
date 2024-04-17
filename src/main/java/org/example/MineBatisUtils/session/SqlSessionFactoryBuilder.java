package org.example.MineBatisUtils.session;

import org.dom4j.DocumentException;
import org.example.MineBatisUtils.configuration.Configuration;
import org.example.MineBatisUtils.configuration.XmlConfigBuilder;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author ziyuan
 * @since 2024.04
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream in) throws PropertyVetoException, DocumentException {
        Configuration configuration = new Configuration();
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(configuration);
        xmlConfigBuilder.parseMineBatisXmlConfig(in);
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
