package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.DrinkDAO;
import edu.xjtlu.cpt403.database.FoodDAO;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.Drink;
import edu.xjtlu.cpt403.entity.Food;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DrinkUI {

    /**
     * 查询所有饮料
     */
    public static void queryDrink() {
        System.out.println("=============================================================");
        System.out.println("                     Drink List");

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
            System.out.println("                  All drink lists are above!");
            System.out.println("=============================================================");
        }
    }

    /**
     * 购买饮料
     *  考虑饮料库存， 用户积分， 积分兑换等逻辑
     */
    public static void buyDrink() throws Exception {
        User user = UserInterfaceUtils.getCurrentUser();
        int userID = user.getId();
        Customer customer = DataBaseManager.getCustomerDAO().select(userID);
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
            drinkID = UserInterfaceUtils.getNumberInput();
            if (drinkID == 0){
                CoffeeShopUI.run();
                break;
            }
            if (DataBaseManager.getDrinkDAO().select(drinkID) == null){
                System.out.println("Wrong drinkID! The reason is that product does not exist or has been removed!");
                System.out.println("Please try to buy another goods!");
            }else {
                break;
            }
        }while (true);


        /**
         * step 3:
         * make sure users want to buy how many drink
         * calculate the results and update the stockAvailable and sellAmount of drink(drinkID)
         */
        Drink drink;
        drink = DataBaseManager.getDrinkDAO().select(drinkID);
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
            CoffeeShopUI.run();
            return;
        }

        /**
         * step 4:
         *Determine whether the customer is VIP
         * if yes, there are two options, 5% discount or stamp+1
         * if not, only stamp+1
         */
        int isVip = customer.getIsVip();
        totalPrice = price * count;
        int loyaltyCardNumber = customer.getLoyaltyCard();
        if (loyaltyCardNumber == 10){
            System.out.println("Now the quantity of your loyaltyCard is 10, and you can exchange a bottle of drink for free");
            System.out.println("Would you like to exchange?");
            System.out.println("Input 1 to exchange, 2 not to exchange");
            System.out.println("Input Please:");
            int exchangeOrnot = UserInterfaceUtils.getIntInput(1,2);
            if (exchangeOrnot == 1){
                loyaltyCardNumber = 0;
                customer.setLoyaltyCard(loyaltyCardNumber);
                // todo:免费饮料的数量减一
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
        DataBaseManager.getCustomerDAO().update(userID,customer);
        updateDrink(drinkID,count);
        System.out.println("Payment completed!");
        System.out.println("Have a nice day and See you next time~");
    }


    /**
     * 新增，也就是上架商品 根据商品属性录入名字，库存，价格等等
     */
    public static void addDrink() throws Exception {
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.println("Please input the details about drink you want to add.");
        System.out.println("Do you want to set specific drink id, if you want you can input it, or just input 0 and system will set id atuomatically");
        System.out.print("Input id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        name = UserInterfaceUtils.getStringInput("Please input the name of this drink:",null);
        System.out.println("Please input the price of this drink:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please input the number of this food in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        System.out.println("Please input the number of sales of this drink:");
        sellAmount = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Drink drink = new Drink(null,0,0,0,0);
        drink.setName(name);
        drink.setPrice(price);
        drink.setStockAvailable(stockAvailable);
        drink.setSellAmount(sellAmount);
        if (id == 0){
            DataBaseManager.getDrinkDAO().insert(drink,true);
        }else{
            drink.setId(id);
            DataBaseManager.getDrinkDAO().insert(drink, false);
        }

    }

    /**
     * 更新， 考虑修改价格， 库存等
     */
    public static void updateDrink() throws Exception {
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.print("Input the id of the drink you want to update:");
        id = UserInterfaceUtils.getIntInput(1,Integer.MAX_VALUE);
        Drink drink = DataBaseManager.getDrinkDAO().select(id);
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
        DataBaseManager.getDrinkDAO().update(id,drink);
    }

    /**
     * only update stockAvailble and sellAmount
     */
    public static void updateDrink(int id, int count) throws Exception {
        Drink drink = DataBaseManager.getDrinkDAO().select(id);
        int stockAvailble = drink.getStockAvailable();
        int sellAmount = drink.getSellAmount();
        drink.setSellAmount(stockAvailble - count);
        drink.setSellAmount(sellAmount + count);
        DataBaseManager.getDrinkDAO().update(id,drink);
    }

    /**
     * 删除， 也就是商品下架，根据商品名字或者id操作
     */
    public static void removeDrink() throws Exception {
        int id;
        System.out.print("Please select the id of the drink you want to remove, Input this id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Drink drink = DataBaseManager.getDrinkDAO().select(id);
        DataBaseManager.getDrinkDAO().delete(drink);

    }
}
