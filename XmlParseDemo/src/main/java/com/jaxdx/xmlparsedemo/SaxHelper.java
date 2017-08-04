package com.jaxdx.xmlparsedemo;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author 陈双龙
 * @date 2017/8/4
 * @description 使用sax解析xml内容
 */

class SaxHelper {

    List<Person> parsePersons(InputStream inputStream) throws ParserConfigurationException,
            SAXException, IOException {
        //得到SAX解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //得到SAX解析器
        SAXParser saxParser = factory.newSAXParser();
        //读取xml
        XMLReader reader = saxParser.getXMLReader();
        PersonParser parser = new PersonParser();
        reader.setContentHandler(parser);
        reader.parse(new InputSource(inputStream));
        return parser.getPersons();
    }

    private final class PersonParser extends DefaultHandler {

        private List<Person> persons;
        private String tag;//用于记录当前解析到哪个元素节点了
        private Person person;

        List<Person> getPersons() {
            return persons;
        }

        //最先执行此方法，所以在这里完成初始化
        @Override
        public void startDocument() throws SAXException {
            persons = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            //判断元素节点是否等于person
            if (localName.equals("person")) {
                person = new Person();
                //获取数据，参数为索引下标
                person.setId(Integer.parseInt(attributes.getValue(0)));
            }
            tag = localName;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equals("person")) {
                persons.add(person);
                person = null;
            }
            tag = null;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (tag != null) {
                //获取文本节点数据
                String data = new String(ch, start, length);
                if (tag.equals("name")) {
                    person.setName(data);
                } else if (tag.equals("age")) {
                    person.setAge(Short.valueOf(data));
                }
            }
        }
    }
}
