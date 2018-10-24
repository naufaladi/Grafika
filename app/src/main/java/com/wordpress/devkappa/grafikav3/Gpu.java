package com.wordpress.devkappa.grafikav3;

public class Gpu {

    String model, type, priceRange;
    int bench;
    double value, price;

    public Gpu(String model, double price, double value, int bench, String type, String priceRange) {
        this.model = model;
        this.price = price;
        this.value = value;
        this.bench = bench;
        this.type = type;
        this.priceRange = priceRange;
    }

    public Gpu(){}

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public double getValue() {
        return value;
    }

    public int getBench() {
        return bench;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setBench(int bench) {
        this.bench = bench;
    }
}
