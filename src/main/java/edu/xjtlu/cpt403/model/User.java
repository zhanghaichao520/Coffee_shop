package edu.xjtlu.cpt403.model;

import java.util.Objects;

public class User {
    private int idNumber;
    private String name;

    public User() {
    }

    public User(int idNumber, String name) {
        this.idNumber = idNumber;
        this.name = name;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idNumber == user.idNumber && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "idNumber=" + idNumber +
                ", name='" + name + '\'' +
                '}';
    }
}
