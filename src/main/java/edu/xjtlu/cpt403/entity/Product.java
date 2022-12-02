package edu.xjtlu.cpt403.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class Product implements Comparable<Product>, Serializable {
    private String name;
    private double price;

    private int id;

    private int stockAvailable;

    private int sellAmount;

    public Product() {
    }

    public Product(String name, double price, int id, int stockAvailable, int sellAmount) {
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

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", id=" + id +
                ", stockAvailable=" + stockAvailable +
                ", sellAmount=" + sellAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && id == product.id && stockAvailable == product.stockAvailable && sellAmount == product.sellAmount && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, id, stockAvailable, sellAmount);
    }
}
