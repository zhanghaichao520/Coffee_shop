package edu.xjtlu.cpt403.entity;

public class Food extends Product{
    public Food() {
    }

    public Food(String name, double price, int id, int stockAvailable, int sellAmount) {
        super(name, price, id, stockAvailable, sellAmount);
    }
}
