package org.example.MineBatisUtils.session;

import java.sql.Connection;

/**
 * @author ziyuan
 * @since 2024.04
 */
public interface SqlSessionFactory {
    SqlSession openSession();
    SqlSession openSession(Connection connection);
}
