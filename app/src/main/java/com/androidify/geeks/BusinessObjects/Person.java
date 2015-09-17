package com.androidify.geeks.BusinessObjects;

/**
 * Created by b_ashish on 17-Sep-15.
 */
public class Person {

    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
