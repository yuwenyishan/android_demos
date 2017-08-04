package com.jaxdx.xmlparsedemo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author 陈双龙
 * @date 2017/8/4
 * @description dom解析xml帮助类
 */

class DomHelper {

    List<Person> getPerson(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        List<Person> persons = new ArrayList<>();

        //获取dom解析工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //获取dom解析器
        DocumentBuilder builder = factory.newDocumentBuilder();
        //讲解析树放入内存，通过返回Document来描述结果
        Document document = builder.parse(inputStream);
        //取得根元素 <persons>
        Element root = document.getDocumentElement();
        //取得所以person节点集合
        NodeList personNodes = root.getElementsByTagName("person");
        for (int i = 0; i < personNodes.getLength(); i++) {
            Person person = new Person();
            //取得person节点元素
            Element personElement = (Element) personNodes.item(i);
            //取得属性值并设置person id字段
            person.setId(Integer.parseInt(personElement.getAttribute("id")));
            //获取person子节点
            NodeList personChild = personElement.getChildNodes();
            for (int j = 0; j < personChild.getLength(); j++) {
                //判断当前节点类型是否是元素类型节点
                if (personChild.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) personChild.item(j);
                    switch (childElement.getNodeName()) {
                        case "name":
                            person.setName(childElement.getFirstChild().getNodeValue());
                            break;
                        case "age":
                            person.setAge(Short.valueOf(childElement.getFirstChild().getNodeValue()));
                            break;
                    }
                }
            }
            persons.add(person);
        }
        return persons;
    }
}
