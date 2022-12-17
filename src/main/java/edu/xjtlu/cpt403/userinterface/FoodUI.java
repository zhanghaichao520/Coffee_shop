package edu.xjtlu.cpt403.userinterface;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.RoomDAO;
import edu.xjtlu.cpt403.database.SalesRecordDAO;
import edu.xjtlu.cpt403.entity.*;
import edu.xjtlu.cpt403.util.DateUtils;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import edu.xjtlu.cpt403.database.FoodDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class FoodUI {
    /**
     ** show the list of food in this shop
     */
    public static void queryFood() {
        System.out.println("                     Food List");
        System.out.println("=============================================================");
        FoodDAO foodDAO = DataBaseManager.getFoodDAO();

        try {
            List<Food> foodList = foodDAO.selectAll();
            if (CollectionUtils.isEmpty(foodList)) {
                System.out.println("The food is empty! ");
                return;
            }
            // 排序
            Collections.sort(foodList);
            // 显示

            for (Food food : foodList) {
                System.out.println(food.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("=============================================================");
            System.out.println("                  All Food lists are above!");
        }
    }

    /**
     * 1.show food list
     * 2.get foodID
     * 3.make sure users want to buy how many food
     * 4.determine the identity of user and if he/she is customer, we should know if the customer is VIP
     * 5.update the status of (customer and) drink
     * 5.feedback
     */
    public static void buyFood() {
        User user = UserInterfaceUtils.getCurrentUser();
        /**
         * step 1:
         * show our food lists for users to tell what they can buy
         */
        queryFood();

        /**
         * stpe2: Get foodID
         * if foodID = 0 means users input exit, so in this situation restart project
         * if foodID != 0, check whether this food exist or not
         */
        int foodID;
        do{
            System.out.println("Now you should input the id of the food you want to buy!");
            foodID = UserInterfaceUtils.getNumberInput();
            if (foodID == 0){
                try {
                    CoffeeShopUI.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                if (DataBaseManager.getFoodDAO().select(foodID) == null){
                    System.out.println("Wrong foodID! The reason is that product does not exist or has been removed!");
                    System.out.println("Please try to buy another goods!");
                }else {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }while (true);


        /**
         * step 3:
         * make sure users want to buy how many food
         * calculate the results and update the stockAvailable and sellAmount of food(foodID)
         */
        Food food = null;
        double price;
        String foodName;
        int stockAvailable;
        int count;
        double totalPrice;
        try {
            food = DataBaseManager.getFoodDAO().select(foodID);
        } catch (Exception e) {
            System.out.println("Failed to get the food: "+e.getMessage());
        }
        price = food.getPrice();
        foodName = food.getName();
        stockAvailable = food.getStockAvailable();
        System.out.println("Now you have selected , " + foodName +
                "and its unit price is "+ price+". How many copies would you like to buy?");
        while (true){
            count = UserInterfaceUtils.getNumberInput();
            String end = "";
            if (count > 1){
                end = "s";
            }
            if(count > stockAvailable){
                System.out.println("You can only buy "+
                        stockAvailable + " " + foodName + end +
                        ", because there are only"+stockAvailable+" in stock, " +
                        "please re-enter the purchase number:");
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
         * determin the identity of the user
         * if user is admin or guest, just update the stock and sellAmount
         * ---------------------------------------------------------------
         * if user is customer
         * determine whether the customer is VIP
         * if yes, there are two options, 5% discount or stamp+1
         * if not, only stamp+1
         * Then update the status of customer and food
         */
        if(user instanceof Customer){
            int userID = user.getId();
            Customer customer = null;
            try {
                customer = DataBaseManager.getCustomerDAO().select(userID);
            } catch (Exception e) {
                System.out.println("Failed to get the information of the customer: "+e.getMessage());
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
                        System.out.println(e.getMessage());
                    }
                    int stockDrink = freeDrink.getStockAvailable();
                    int sellAmountDrink = freeDrink.getSellAmount();
                    try {
                        DataBaseManager.getDrinkDAO().update(666,new Drink("Secret",0.0,666,stockDrink-1,sellAmountDrink + 1));
                    } catch (Exception e) {
                        System.out.println("Failed to update the free drink: "+e.getMessage());
                    }
                }
            }
            if (isVip == 1){
                System.out.println("Hello " +customer.getName()+ " ! Now You should pay "+totalPrice+
                        "RMB to buy "+foodName+".");
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
            try {
                DataBaseManager.getCustomerDAO().update(userID,customer);
            } catch (Exception e) {
                System.out.println("Failed to update the information of the customer: "+e.getMessage());
            }
            updateFood(foodID,count);
        }else if (user instanceof  AdminUser){
            updateFood(foodID,count);
        }else{
            updateFood(foodID,count);
        }
        System.out.println("payment completed!");
        System.out.println("Have a nice day and see you next time~");
        System.out.println("==================================================");

        SalesRecord salesRecord = new SalesRecord();
        salesRecord.setProductID(foodID);
        salesRecord.setProductName(foodName);
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
     * Insert a new food
     * 1.get the details of new food
     * if id == 0 means the system automatically sets the id value
     * if id == any postive number means we want to set a specific id value
     * 2. add it
     * 3. feedback
     */
    public static void addFood(){
        queryFood();
        int id = 0;
        String name = null;
        double price;
        int stockAvailable;
        int sellAmount = 0;
        List<Food> foodList = null;
        boolean sign = true;
        boolean sign1 = true;
        try {
            foodList = DataBaseManager.getFoodDAO().selectAll();
        } catch (Exception e) {
            System.out.println("Failed to get the foodList: "+e.getMessage());
        }
        System.out.println("Please input the details about food you want to add.");
        while (sign){

            System.out.println("Do you want to set specific food id, you can input it if you want, or just input 0 and system will set id atuomatically");
            System.out.print("Input id:");
            id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
            if (id == 0){
                break;
            }
            int i = 0;
            for (Food food:foodList){
                if (id == food.getId()){
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
            name = UserInterfaceUtils.getStringInput("Please input the name of this food:",null);
            int j = 0;
            for (Food food:foodList){
                if (name.equals(food.getName())){
                    System.out.println(name+" already exists, please input a new name!");
                    j = 1;
                    break;
                }
            }
            if (j == 0){
                break;
            }
        }
        System.out.println("Please input the price of this food:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please input the number of this food in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Food food = new Food(null,0,0,0,0);
        food.setName(name);
        food.setPrice(price);
        food.setStockAvailable(stockAvailable);
        food.setSellAmount(sellAmount);
        try{
            if (id == 0){
                DataBaseManager.getFoodDAO().insert(food,true);
            }else{
                food.setId(id);
                DataBaseManager.getFoodDAO().insert(food, false);
            }
            System.out.println(name + " was added successfully!");
        }catch (Exception e) {
            System.out.println("Failed to add new food: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryFood();

    }

    /**
     * user:admin
      1.determine which one we want to update
     * 2.update all attributes: name, price, stockAvailable, sellAmount
     * 3.feedback
     */
    public static void updateFood(){
        queryFood();
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.print("Input the id of the food you want to update:");
        id = UserInterfaceUtils.getIntInput(1,Integer.MAX_VALUE);
        Food food = null;
        try {
            food = DataBaseManager.getFoodDAO().select(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name = UserInterfaceUtils.getStringInput("Please update the name of this food:",null);
        System.out.println("Please update the price of this food:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please update the number of this food in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        System.out.println("Please update the number of sales of this food:");
        sellAmount = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        food.setName(name);
        food.setPrice(price);
        food.setStockAvailable(stockAvailable);
        food.setSellAmount(sellAmount);
        try {
            DataBaseManager.getFoodDAO().update(id,food);
            System.out.println(name + " was updated successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add new food: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryFood();
    }

    /**
     * when user buy drink, we use this method to update the part of the attributes of this drink
     * only update stockAvailble and sellAmount
     */
    public static void updateFood(int id, int count){
        Food food = null;
        try {
            food = DataBaseManager.getFoodDAO().select(id);
        } catch (Exception e) {
            System.out.println("Failed to get the food: "+e.getMessage());
        }
        int stockAvailble = food.getStockAvailable();
        int sellAmount = food.getSellAmount();
        food.setStockAvailable(stockAvailble - count);
        food.setSellAmount(sellAmount + count);
        try {
            DataBaseManager.getFoodDAO().update(id,food);
        } catch (Exception e) {
            System.out.println("Failed to update the food: "+e.getMessage());
            System.out.println("Please try again");
        }

    }

    /**
     * user:admin
     * Delete food based on its id
     */
    public static void removeFood(){
        queryFood();
        int id;
        System.out.print("Please select the id of the food you want to remove, Input this id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Food food = null;
        try {
            food = DataBaseManager.getFoodDAO().select(id);
        } catch (Exception e) {
            System.out.println("Failed to get food: "+e.getMessage());
        }
        try {
            DataBaseManager.getFoodDAO().delete(id);
            System.out.println(food.getName() + " was removed successfully!");
        } catch (Exception e) {
            System.out.println("Failed to remove the food: "+e.getMessage());
            System.out.println("Please try again");
        }
        queryFood();
    }
}
