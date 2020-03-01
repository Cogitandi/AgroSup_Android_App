package com.example.entities;

public class Machine {

    private int id;
    private String name;
    private int width; // in cm

    public Machine(String name, int width) {
        this.name = name;
        this.width = width;
    }

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

    public int getWidth() {
        return width;
    }

    public String getWidthM() {
        float width = getWidth();
        float widthM = width / 100;
        String widthString = String.format("%.2f", widthM);
        return widthString;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
