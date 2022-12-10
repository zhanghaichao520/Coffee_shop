package edu.xjtlu.cpt403.userinterface;


import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.entity.User;

import static edu.xjtlu.cpt403.util.UserInterfaceUtils.getStringInput;
import static edu.xjtlu.cpt403.util.UserInterfaceUtils.showOptionsAndGetChoice;

public class AdminstrationUI {

    /**
     * 登录流程
     */
    public static void login() throws Exception {
        String username = getStringInput("Please input the admin username", s -> User.validateName(s));
        String password = getStringInput("Please input the admin password", s -> User.validatePassword(s));

        try {
            AdminUser.validateLogin(username, password, DataBaseManager.getAdminUserDAO());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage() + ", Please try again.");
            login();
            return;
        }

        run(username);

    }

    public static void run(String username) throws Exception {
        System.out.println("=============================================================");
        System.out.println("Hi, " + username + " welcome to adminstration UI!");

        String[] options = {
                "Exit.",
                "Inventory management.",
                "Purchase of Goods.",
                "Reservation Room."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : inventoryManagement(); break;
                case 2 : purchaseGoods(); break;
                case 3 : bookRoom();
                break;
            }
        }
        while (choice != 0);

        // 退出的时候， 顺便把账号也退出登录
        CustomerUI.exitLogin();
    }

    private static void bookRoom() {
        String[] options = {
                "Exit.",
                "New Reservation.",
                "Query Reservation.",
                "Cancel Reservation."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : RoomUI.newReservation(); break;
                case 2 : RoomUI.queryReservation(); break;
                case 3 : RoomUI.cancelReservation(); break;
            }
        }
        while (choice != 0);
    }

    private static void purchaseGoods() throws Exception {
        String[] options = {
                "Exit.",
                "Buy Food.",
                "Buy Drink."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : FoodUI.buyFood(); break;
                case 2 : DrinkUI.buyDrink();
                break;
            }
        }
        while (choice != 0);
    }

    private static void inventoryManagement() throws Exception {
        String[] options = {
                "Exit.",
                "Query Food.",
                "Add Food.",
                "Update Food.",
                "Remove Food.",
                "Query Drink.",
                "Add Drink.",
                "Update Drink.",
                "Remove Drink."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : FoodUI.queryFood(); break;
                case 2 : FoodUI.addFood(); break;
                case 3 : FoodUI.updateFood(); break;
                case 4 : FoodUI.removeFood(); break;
                case 5 : DrinkUI.queryDrink(); break;
                case 6 : DrinkUI.addDrink(); break;
                case 7 : DrinkUI.updateDrink(); break;
                case 8 : DrinkUI.removeDrink(); break;
            }
        }
        while (choice != 0);
    }


}
