package org.example.MineBatisUtils.configuration;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ziyuan
 * @since 2024.04
 */
//xxx:dom4k解析xml文件
public class XmlMapperBuilder {
    private Configuration configuration;
    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }
    public void parse(InputStream inputStream) throws DocumentException {
        //xxx:解析xml文件
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Attribute namespace = rootElement.attribute("namespace");
        String namespaceValue = namespace.getValue();
        List<Element> selectNodes = rootElement.selectNodes("//select");
        List<Element> insertNodes = rootElement.selectNodes("//insert");
        List<Element> deleteNodes = rootElement.selectNodes("//delete");
        List<Element> updateNodes = rootElement.selectNodes("//update");
        List<Element> allList=new ArrayList<>();
        allList.addAll(selectNodes);
        allList.addAll(insertNodes);
        allList.addAll(deleteNodes);
        allList.addAll(updateNodes);
        for (Element element : allList) {
            String sqlCommandType = element.getName();
            MappedStatement mappedStatement = new MappedStatement();
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqltext = element.getTextTrim();
            String key = namespaceValue + "." + id;
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sqltext);
            mappedStatement.setSqlCommandType(sqlCommandType);
            configuration.getMappedStatementMap().put(key, mappedStatement);

        }
    }
}
