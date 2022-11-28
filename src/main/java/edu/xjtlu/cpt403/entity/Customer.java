package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.enums.Loyalty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
@Data
@AllArgsConstructor
public class Customer extends User {

    private Loyalty loyalty;

    private String gender;

    private int isVip;

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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return isVip == customer.isVip && loyaltyCard == customer.loyaltyCard && loyalty == customer.loyalty && Objects.equals(gender, customer.gender) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loyalty, gender, isVip, loyaltyCard);
    }
}
