package edu.xjtlu.cpt403.entity;


import java.util.Objects;

public class Customer extends User {


    /** Customer Gender*/
    private String gender;

    /** User is Vip or not,
     * value=1,user is vip.
     * value=0,user is not vip.*/
    private int isVip;

    /** record number of user's purchases
     *  one purchase can add one point
     *  ten points can exchange product
     * */
    private int loyaltyCard;


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

    public Customer() {
    }


    public Customer(int id, String name, String passWord, String phoneNumber, String gender, int isVip, int loyaltyCard) {
        super(id, name, passWord, phoneNumber);
        this.gender = gender;
        this.isVip = isVip;
        this.loyaltyCard = loyaltyCard;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", gender='" + gender + '\'' +
                ", isVip=" + isVip +
                ", loyaltyCard=" + loyaltyCard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;

        return isVip == customer.isVip && loyaltyCard == customer.loyaltyCard  && Objects.equals(gender, customer.gender) ;

    }

    @Override
    public int hashCode() {


        return Objects.hash(super.hashCode(), gender, isVip, loyaltyCard);
    }
}
