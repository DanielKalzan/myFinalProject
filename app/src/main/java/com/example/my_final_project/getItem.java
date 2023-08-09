package com.example.my_final_project;

public class getItem {

    private int id;
    private String name;
    int Unit;
    private int price;
    private byte[] image;

    public getItem(int id, String name, int price, int unit, byte[] image) {
        this.Unit = unit;
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        Unit = unit;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}
