package edu.xjtlu.cpt403.entity;

import java.io.Serializable;

public abstract class Product implements Comparable<Product>, Serializable {
    private String name;
    private double price;

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }
}
