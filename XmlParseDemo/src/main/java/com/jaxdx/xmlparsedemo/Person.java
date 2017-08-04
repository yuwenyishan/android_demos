package com.jaxdx.xmlparsedemo;

/**
 * @author 陈双龙
 * @date 2017/8/4
 * @description person xml 对应的解析类
 */

class Person {

    private int id;
    private String name;
    private short age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    void setAge(short age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
