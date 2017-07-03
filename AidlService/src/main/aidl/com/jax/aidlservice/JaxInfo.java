package com.jax.aidlservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 2017/7/3.
 */

public class JaxInfo implements Parcelable {

    private String name;
    private int age;
    private String sex;
    private String info;

    public JaxInfo(String name, int age, String sex, String info) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public JaxInfo() {
    }

    @Override
    public String toString() {
        return "JaxInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.sex);
        dest.writeString(this.info);
    }

    public void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.sex = in.readString();
        this.info = in.readString();
    }

    protected JaxInfo(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.sex = in.readString();
        this.info = in.readString();
    }

    public static final Creator<JaxInfo> CREATOR = new Creator<JaxInfo>() {
        @Override
        public JaxInfo createFromParcel(Parcel source) {
            return new JaxInfo(source);
        }

        @Override
        public JaxInfo[] newArray(int size) {
            return new JaxInfo[size];
        }
    };
}
