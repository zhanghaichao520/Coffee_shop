package edu.xjtlu.cpt403.entity;


import edu.xjtlu.cpt403.database.CustomerDAO;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;

import java.util.List;
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

    /**
     * login
     * @param name
     * @param passWord
     * @param customerDAO
     * @return
     */
    public static boolean validateLogin(String name, String passWord, CustomerDAO customerDAO) throws Exception {
        validateName(name);
        validatePassword(passWord);

        List<Customer> result = customerDAO.selectAll();
        Customer currentUser = null;
        for (Customer user : result) {
            if (user != null && name.equals(user.getName())) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) {
            throw new IllegalArgumentException("The customer user is not exist, please try another username!");
        }

        if (!passWord.equals(currentUser.getPassWord())) {
            throw new IllegalArgumentException("The customer user password is not right!");
        }

        // 把当前登录用户设置到全局变量，便于后续UI访问, 感觉这样的方式有一点TRICK， 但是也没想到有什么更好的办法
        UserInterfaceUtils.setCurrentUser(currentUser);
        return true;
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
