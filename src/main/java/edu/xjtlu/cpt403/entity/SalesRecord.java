package edu.xjtlu.cpt403.entity;

public class SalesRecord {

    /** 购买记录ID */
    private int id;
    /** 购买的商品ID */
    private int productID;
    /** 购买的商品名称 */
    private String productName;
    /** 购买的商品数量 */
    private int buyNumber;
    /** 购买顾客的ID */
    private int userid;
    /** 购买花费的金额 */
    private double payCost;
    /** 购买时间(日期) */
    private String payTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getBuyNumber() {
        return buyNumber;
    }

    public void setBuyNumber(int buyNumber) {
        this.buyNumber = buyNumber;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getPayCost() {
        return payCost;
    }

    public void setPayCost(double payCost) {
        this.payCost = payCost;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return "SalesRecord{" +
                "id=" + id +
                ", productID=" + productID +
                ", productName='" + productName + '\'' +
                ", buyNumber=" + buyNumber +
                ", userid=" + userid +
                ", payCost=" + payCost +
                ", payTime='" + payTime + '\'' +
                '}';
    }
}
