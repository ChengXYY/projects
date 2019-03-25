package com.my.formtool.model;


public class ChartData {
    private String name;

    private Integer count = 0;

    public String getName(){return this.name;}

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void addOne(){this.count++;}
}
