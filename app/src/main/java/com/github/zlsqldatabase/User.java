package com.github.zlsqldatabase;

import com.github.zlsqldatabase.Annotation.dbField;

public class User {
    @dbField(fieldName = "name", fieldLength = 30)
    public String name;

    @dbField(fieldName = "password", fieldLength = 20)
    public String password;

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
        return "name="+name+",password="+password;
    }
}
