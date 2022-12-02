package edu.xjtlu.cpt403.entity;

public class Drink extends Product {
    private String name;

    private double price;

    private int id;

    private int stockAvailable;

    private int sellAmount;

    public Drink(String name, double price, int id, int stockAvailable, int sellAmount) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.stockAvailable = stockAvailable;
        this.sellAmount = sellAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public int getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(int sellAmount) {
        this.sellAmount = sellAmount;
    }

}
