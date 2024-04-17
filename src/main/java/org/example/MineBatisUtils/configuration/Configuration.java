package org.example.MineBatisUtils.configuration;

import lombok.Data;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ziyuan
 * @since 2024.04
 */
@Data
//xxx:sql配置封装类,使用Dom4j进行解析
public class Configuration {
    private DataSource dataSource;
    //xxx:封装成map ,因为一个xml里的sql可能有多个
    private Map<String, MappedStatement> mappedStatementMap = new ConcurrentHashMap<>();

    public MappedStatement getMappedStatement(String id) {
        return this.mappedStatementMap.get(id);
    }
}
