package edu.xjtlu.cpt403.entity;

import java.util.Objects;

public class AdminUser extends User{

    /** AdminUser account password */
    private String passWord;

    /** AdminUser phone number*/
    private String phoneNumber;

    public AdminUser() {
    }

    public AdminUser(int id, String name) {
        super(id, name);
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
    public String toString() {
        return "AdminUser{" +
                "passWord='" + passWord + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AdminUser adminUser = (AdminUser) o;
        return Objects.equals(passWord, adminUser.passWord) && Objects.equals(phoneNumber, adminUser.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passWord, phoneNumber);
    }
}
