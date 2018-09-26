package com.github.zlsqldatabase;

import com.github.zlsqldatabase.Annotation.dbField;

public class User {
    @dbField(fieldName = "name", fieldLength = 30)
    private String name;

    @dbField(fieldName = "password", fieldLength = 20)
    private String password;

    private int age;
    private String school;

    public User() {
        super();
    }

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "name="+name+",password="+password+",age="+age+",school="+school;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
