package com.jaxdx.xmlparsedemo;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈双龙
 * @date 2017/8/4
 * @description 使用pull解析xml
 */

class PullHelper {

    List<Person> parsePersons(InputStream inputStream) throws XmlPullParserException, IOException {
        List<Person> persons = new ArrayList<>();
        Person person = null;
        //得到pull解析器
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        //产生事件
        int eventType = parser.getEventType();
        //如果不是文档结束事件就循环推进
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT://开始文档事件
                    break;
                case XmlPullParser.START_TAG://开始元素事件
                    //获取解析器当前指向元素的名称
                    String name = parser.getName();
                    if (name.equals("person")) {
                        person = new Person();
                        person.setId(Integer.parseInt(parser.getAttributeValue(0)));
                    }
                    if (person != null) {
                        if (name.equals("name")) {
                            //获取解析器当前指向元素的下一个文本节点值
                            person.setName(parser.nextText());
                        }

                        if (name.equals("age")) {
                            person.setAge(Short.parseShort(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG://结束元素事件
                    //判断是不是person结束事件
                    if (parser.getName().equals("person")) {
                        persons.add(person);
                        person = null;
                    }
                    break;
            }
            //进入下一个元素并触发相应事件
            eventType = parser.next();
        }
        return persons;
    }
}
