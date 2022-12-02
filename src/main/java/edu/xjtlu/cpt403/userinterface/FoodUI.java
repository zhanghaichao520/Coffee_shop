package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.RoomDAO;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.Food;
import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import edu.xjtlu.cpt403.database.FoodDAO;
import org.apache.commons.lang3.StringUtils;

import javax.jws.soap.SOAPBinding;
import javax.xml.bind.SchemaOutputResolver;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class FoodUI {
    /**
     ** 查询所有食物
     */
    public static void queryFood() {
        System.out.println("=============================================================");
        System.out.println("                     Food List");

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
            System.out.println("                  All Food lists are above!");
            System.out.println("=============================================================");
        }
    }

    /**
     * 购买食物
     *  考虑食物库存， 用户积分， 积分兑换等逻辑
     * 1.展示食物清单
     * 2.让用户选择需要购买得食物,并
     * 3.判断顾客是不是VIP，是的话两种选择，95折 or stamp+1
     * 4.更新食物库存和销量和更新顾客状态
     * 5.提示购买成功，可以溜了
     */
    public static void buyFood() throws Exception {
        User user = UserInterfaceUtils.getCurrentUser();
        int userID = user.getId();
        Customer customer = DataBaseManager.getCustomerDAO().select(userID);
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
            foodID = UserInterfaceUtils.getNumberInput();
            if (foodID == 0){
                CoffeeShopUI.run();
                break;
            }
            if (DataBaseManager.getFoodDAO().select(foodID) == null){
                System.out.println("Wrong foodID! The reason is that product does not exist or has been removed!");
                System.out.println("Please try to buy another goods!");
            }else {
                break;
            }
        }while (true);


        /**
         * step 3:
         * make sure users want to buy how many food
         * calculate the results and update the stockAvailable and sellAmount of food(foodID)
         */
        Food food;
        food = DataBaseManager.getFoodDAO().select(foodID);
        double price = food.getPrice();
        String foodName = food.getName();
        int stockAvailable = food.getStockAvailable();
        System.out.println("Now you have selected , " + foodName +
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
                        stockAvailable + " " + foodName + end +
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

        /**
         * step 5:
         * update the status of customer and food
         * give a message to tell customer that they finished their shopping
         */
        DataBaseManager.getCustomerDAO().update(userID,customer);
        updateFood(foodID,count);
        System.out.println("Payment completed!");
        System.out.println("Have a nice day and See you next time~");
    }

    /**
     * Insert a new food
     * if id == 0 means the system automatically sets the id value
     * if id == any postive number means we want to set a specific id value
     */
    public static void addFood() throws Exception {
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.println("Please input the details about food you want to add.");
        System.out.println("Do you want to set specific food id, if you want you can input it, or just input 0 and system will set id atuomatically");
        System.out.print("Input id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        name = UserInterfaceUtils.getStringInput("Please input the name of this food:",null);
        System.out.println("Please input the price of this food:");
        price = UserInterfaceUtils.getDoubleNumberInput(0.0,Double.MAX_VALUE);
        System.out.println("Please input the number of this food in stock:");
        stockAvailable = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        System.out.println("Please input the number of sales of this food:");
        sellAmount = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Food food = new Food(null,0,0,0,0);
        food.setName(name);
        food.setPrice(price);
        food.setStockAvailable(stockAvailable);
        food.setSellAmount(sellAmount);
        if (id == 0){
            DataBaseManager.getFoodDAO().insert(food,true);
        }else{
            food.setId(id);
            DataBaseManager.getFoodDAO().insert(food, false);
        }
    }

    /**
     * update all attributes: name, price, stockAvailable, sellAmount
     */
    public static void updateFood() throws Exception {
        int id;
        String name;
        double price;
        int stockAvailable;
        int sellAmount;
        System.out.print("Input the id of the food you want to update:");
        id = UserInterfaceUtils.getIntInput(1,Integer.MAX_VALUE);
        Food food = DataBaseManager.getFoodDAO().select(id);
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
        DataBaseManager.getFoodDAO().update(id,food);
    }

    /**
     * only update stockAvailble and sellAmount
     */
    public static void updateFood(int id, int count) throws Exception {
        Food food = DataBaseManager.getFoodDAO().select(id);
        int stockAvailble = food.getStockAvailable();
        int sellAmount = food.getSellAmount();
        food.setSellAmount(stockAvailble - count);
        food.setSellAmount(sellAmount + count);
        DataBaseManager.getFoodDAO().update(id,food);
    }

    /**
     * Delete food based on its id
     */
    public static void removeFood() throws Exception {
        int id;
        System.out.print("Please select the id of the food you want to remove, Input this id:");
        id = UserInterfaceUtils.getIntInput(0,Integer.MAX_VALUE);
        Food food = DataBaseManager.getFoodDAO().select(id);
        DataBaseManager.getFoodDAO().delete(food);
    }
}
