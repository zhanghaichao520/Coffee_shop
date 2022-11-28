package edu.xjtlu.cpt403.entity;



import java.io.Serializable;
import java.util.Objects;


public abstract class User implements Comparable<User>, Serializable {
    private int id;
    private String name;

    private String passWord;

    private String phoneNumber;


    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(int id, String name, String passWord, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
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

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int compareTo(User o) {
        return name.compareTo(o.getName());
    }
}
