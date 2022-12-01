package edu.xjtlu.cpt403.entity;

public class Drink extends Product {
    public Drink() {
    }

    public Drink(String name, double price, int id, int stockAvailable, int sellAmount) {
        super(name, price, id, stockAvailable, sellAmount);
    }
}
