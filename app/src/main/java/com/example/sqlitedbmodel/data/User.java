package com.example.sqlitedbmodel.data;

public class User {
    private long id;
    private String name;
    private String Age;

    public User(long id, String name, String age) {
        this.id = id;
        this.name = name;
        Age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return Age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        Age = age;
    }
}
