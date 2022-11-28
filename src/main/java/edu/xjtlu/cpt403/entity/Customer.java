package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.enums.Loyalty;

import java.util.Objects;

public class Customer extends User {

    private Loyalty loyalty;

    private String gender;

    private int isVip;

    private int loyaltyCard;

    private String passWord;

    private String phoneNumber;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getLoyaltyCard() {
        return loyaltyCard;
    }

    public void setLoyaltyCard(int loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
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

    public Customer(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

    public Customer(int idNumber, String name, Loyalty loyalty) {
        super(idNumber, name);
        this.loyalty = loyalty;
    }

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "loyalty=" + loyalty +
                ", gender='" + gender + '\'' +
                ", isVip=" + isVip +
                ", loyaltyCard=" + loyaltyCard +
                ", passWord='" + passWord + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return isVip == customer.isVip && loyaltyCard == customer.loyaltyCard && loyalty == customer.loyalty && Objects.equals(gender, customer.gender) && Objects.equals(passWord, customer.passWord) && Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loyalty, gender, isVip, loyaltyCard, passWord, phoneNumber);
    }
}
