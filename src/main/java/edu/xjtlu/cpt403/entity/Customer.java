package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.enums.Loyalty;

public class Customer extends User {

    private Loyalty loyalty;


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


}
