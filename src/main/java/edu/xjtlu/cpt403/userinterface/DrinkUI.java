package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.DrinkDAO;
import edu.xjtlu.cpt403.database.FoodDAO;
import edu.xjtlu.cpt403.entity.*;
import edu.xjtlu.cpt403.util.DateUtils;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DrinkUI {

    /**
     * show the list of drink
     */
    public static void queryDrink() {
        System.out.println("                     Drink List");
        System.out.println("=============================================================");
        DrinkDAO drinkDAO = DataBaseManager.getDrinkDAO();

        try {
            List<Drink> drinkList = drinkDAO.selectAll();
            if (CollectionUtils.isEmpty(drinkList)) {
                System.out.println("The drink is empty! ");
                return;
            }
            // 排序
            Collections.sort(drinkList);
            // 显示

            for (Drink drink : drinkList) {
                System.out.println(drink.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("=============================================================");
            System.out.println("                  All drink lists are above!");
        }
    }


    /**
     * 1.show our drink lists for users to tell what they can buy
     * 2.Get drinkID
     * 3.make sure users want to buy how many drink
     * 4.Determine the identity of user and if he/she is customer, we should know if the customer is VIP
     * 5.update the status of (customer and) drink
     * 6.feedback
     */
    public static void buyDrink(){
        User user = UserInterfaceUtils.getCurrentUser();

        /**
         * step 1:
         * show our drink lists for users to tell what they can buy
         */
        queryDrink();

        /**
         * stpe2: Get drinkID
         * if drinkID = 0 means users input exit, so in this situation restart project
         * if drinkID != 0, check whether this drink exist or not
         */
        int drinkID;
        do{
            System.out.println("Now you should input the id of the drink you want to buy!");
            drinkID = UserInterfaceUtils.getNumberInput();
            if (drinkID == 0){
                try {
                    CoffeeShopUI.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                if (DataBaseManager.getDrinkDAO().select(drinkID) == null){
                    System.out.println("Wrong drinkID! The reason is that product does not exist or has been removed!");
                    System.out.println("Please try to buy another goods!");
                }else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while (true);


        /**
         * step 3:
         * make sure users want to buy how many drink
         * calculate the results and update the stockAvailable and sellAmount of drink(drinkID)
         */
        Drink drink = null;
        try {
            drink = DataBaseManager.getDrinkDAO().select(drinkID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double price = drink.getPrice();
        String drinkName = drink.getName();
        int stockAvailable = drink.getStockAvailable();
        System.out.println("Now you have selected , " + drinkName +
                "and its unit price is "+ price+". How many copies would you like to buy?");
        int count;
        double totalPrice;
        while (true){
            count = UserInterfaceUtils.getNumberInput();
            String end = "";
            if (count > 1){
                end = "s";
            }
            if(count > stockAvailable){
                System.out.println("You can only buy "+
                        stockAvailable+" " + drinkName + end +
                        ", because there are only"+stockAvailable+" in stock, " +
                        "please re-enter the purchase number:");
                continue;
            }else {
                break;
            }
        }
        if (count == 0){
            try {
                CoffeeShopUI.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        totalPrice = price * count;

        /**
         * step 4:
         *Determine whether the customer is VIP
         * if yes, there are two options, 5% discount or stamp+1
         * if not, only stamp+1
         */
        if (user instanceof Customer){
            int userID = user.getId();
            Customer customer = null;
            try {
                customer = DataBaseManager.getCustomerDAO().select(userID);
            } catch (Exception e) {
                System.out.println("Faild to get the information of customer: "+e.getMessage());
            }
            int isVip = customer.getIsVip();
            int loyaltyCardNumber = customer.getLoyaltyCard();
            if (loyaltyCardNumber == 10){
                System.out.println("Now the quantity of your loyaltyCard is 10, and you can exchange a bottle of drink for free");
                System.out.println("Would you like to exchange?");
                System.out.println("Input 1 to exchange, 2 not to exchange");
                System.out.println("Input Please:");
                int exchangeOrnot = UserInterfaceUtils.getIntInput(1,2);
                // stamp == 0 and the number of free drink -1 in stock
                if (exchangeOrnot == 1){
                    loyaltyCardNumber = 0;
                    customer.setLoyaltyCard(loyaltyCardNumber);
                    Drink freeDrink = null;
                    try {
                        freeDrink = DataBaseManager.getDrinkDAO().select(666);
                    } catch (Exception e) {
                        System.out.println("Failed to get the free drink: "+e.getMessage());
                    }
                    int stockDrink = freeDrink.getStockAvailable();
                    int sellAmountDrink = freeDrink.getSellAmount();
                    try {
                        DataBaseManager.getDrinkDAO().update(666,new Drink("Secret",0.0,666,stockDrink-1,sellAmountDrink + 1));
                    } catch (Exception e) {
                        System.out.println("Faild to update the details of free drink: "+e.getMessage());
                    }
                }
            }
            if (isVip == 1){
                System.out.println("Hello " +customer.getName()+ " ! Now You should pay "+totalPrice+
                        "RMB to buy "+drinkName+".");
                System.out.println("You can add one to your loyaltyCard quantity, " +
                        "or take 5% off the total price. Which one do you want to select, " +
                        "type 1 to select the former, type 2 to select the latter." +
                        " Input please: ");
                int choice = UserInterfaceUtils.getIntInput(1,2);
                if (choice == 1){
                    if (loyaltyCardNumber < 10){
                        customer.setLoyaltyCard(loyaltyCardNumber+1);
                    }
                }else {
                    totalPrice = 0.95 * totalPrice;
                }
            }else{
                if (loyaltyCardNumber < 10){
                    customer.setLoyaltyCard(loyaltyCardNumber+1);
                }
            }

            /**
             * step 5:
             * update the status of customer and drink
             * give a message to tell customer that they finished their shopping
             */
            try {
                DataBaseManager.getCustomerDAO().update(userID,customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateDrink(drinkID,count);
        }else  if (user instanceof AdminUser){
            updateDrink(drinkID,count);
        }else {
            updateDrink(drinkID,count);
        }
        System.out.println("Payment completed!");
        System.out.println("Have a nice day and See you next time~");
        System.out.println("==================================================");

        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setProductID(drinkID);
        salesRecord.setProductName(drinkName);
        salesRecord.setBuyNumber(count);
        if(user == null) {
            salesRecord.setUserid(-2);
        } else if (user instanceof AdminUser) {
            salesRecord.setUserid(-1);
        } else {
            salesRecord.setUserid(user.getId());
        }
        salesRecord.setPayCost(totalPrice);
        salesRecord.setPayTime(DateUtils.getDate(new Date()));
        try{
            DataBaseManager.getSalesRecordDAO().insert(salesRecord,true);
        }catch (Exception e) {
            System.out.println("Failed to add new SalesRecord: "+e.getMessage());
            System.out.println("Please try again");
        }
    }


    /**
     * 1.get the details of new drink
     * 2.add it
     * 3.feedback
     */
    public static void addDrink(){
        queryDrink();
        int id = 0;
        String name = null;
        double price;
        int stockAvailable;
        int sellAmount = 0;
        boolean sign = true;
        boolean sign1 = true;
        List<Drink> drinkList = null;
        try {
            drinkList = DataBaseManager.getDrinkDAO().selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Please input the details about drink you want to add.");
        while (sign){

            System.out.println("Do you want to set specific drink id, you can input it if you want , or just input 0 and system will set id atuomatically");
            System.out.print("Input id:");
            id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
            if (id == 0){
                break;
            }
            int i = 0;
            for (Drink drink:drinkList){
                if (id == drink.getId()){
                    System.out.println("id = "+id+" already exists, please input a new id!");
                    i = 1;
                    break;
                }
            }
            if (i == 0){
                break;
            }
        }
        while (sign1){
            name = UserInterfaceUtils.getStringInput("Please input the name of this drink:",null);
            int j = 0;
            for (Drink drink:drinkList){
                if (name.equals(drink.getName())){
                    System.out.println(name+" already exists, please input a new name!");
                    j = 1;
                    break;
                }
            }
            if (j == 0){
                break;
            }
        }

        System.out.println("Please input the price of this drink:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please input the number of this food in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Drink drink = new Drink(null,0,0,0,0);
        drink.setName(name);
        drink.setPrice(price);
        drink.setStockAvailable(stockAvailable);
        drink.setSellAmount(sellAmount);
        try{
            if (id == 0){
                DataBaseManager.getDrinkDAO().insert(drink,true);
            }else{
                drink.setId(id);
                DataBaseManager.getDrinkDAO().insert(drink, false);
            }
            System.out.println(name + " was added successfully!");
        }catch (Exception e) {
            System.out.println("Failed to add new drink: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryDrink();

    }

    /**
     * user:admin
     * 1.determine which one we want to update
     * 2.update all attributes of this drink
     * 3.feedback
     */
    public static void updateDrink(){
        queryDrink();
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.print("Input the id of the drink you want to update:");
        id = UserInterfaceUtils.getIntInput(1,Integer.MAX_VALUE);
        Drink drink = null;
        try {
            drink = DataBaseManager.getDrinkDAO().select(id);
        } catch (Exception e) {
            System.out.println("Failed to get drink: "+e.getMessage());
        }
        name = UserInterfaceUtils.getStringInput("Please update the name of this drink:",null);
        System.out.println("Please update the price of this drink:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please update the number of this drink in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        System.out.println("Please update the number of sales of this drink:");
        sellAmount = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        drink.setName(name);
        drink.setPrice(price);
        drink.setStockAvailable(stockAvailable);
        drink.setSellAmount(sellAmount);
        try {
            DataBaseManager.getDrinkDAO().update(id,drink);
            System.out.println(name + " was updated successfully!");
        } catch (Exception e) {
            System.out.println("Failed to update drink: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryDrink();

    }

    /**
     * when user buy drink, we use this method to update the part of the attributes of this drink
     * only update stockAvailble and sellAmount
     */
    public static void updateDrink(int id, int count){
        Drink drink = null;
        try {
            drink = DataBaseManager.getDrinkDAO().select(id);
        } catch (Exception e) {
            System.out.println("Failed to get drink: "+e.getMessage());
        }
        int stockAvailble = drink.getStockAvailable();
        int sellAmount = drink.getSellAmount();
        drink.setStockAvailable(stockAvailble - count);
        drink.setSellAmount(sellAmount + count);
        try {
            DataBaseManager.getDrinkDAO().update(id,drink);
        } catch (Exception e) {
            System.out.println("Failed to update drink: "+e.getMessage());
            System.out.println("Please try again");
        }
    }

    /**
     * user:admin
     * get the id of the drink
     * find this drink by its id
     * delete this drink
     * feedback
     */
    public static void removeDrink(){
        queryDrink();
        int id;
        System.out.print("Please select the id of the drink you want to remove, Input this id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Drink drink = null;
        try {
            drink = DataBaseManager.getDrinkDAO().select(id);
        } catch (Exception e) {
            System.out.println("Failed to get drink: "+e.getMessage());
        }
        try {
            DataBaseManager.getDrinkDAO().delete(id);
            System.out.println(drink.getName() + " was removed successfully!");
        } catch (Exception e) {
            System.out.println("Failed to remove drink: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryDrink();
    }
}
