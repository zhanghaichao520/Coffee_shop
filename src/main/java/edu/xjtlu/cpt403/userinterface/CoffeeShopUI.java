package edu.xjtlu.cpt403.userinterface;

import static edu.xjtlu.cpt403.util.UserInterfaceUtils.showOptionsAndGetChoice;

public class CoffeeShopUI {

    public static void run() {

        System.out.println("Welcome to Our Coffee Shop!");
        String[] options = {
                "Exit.",
                "Guest.",
                "Customer.",
                "Administration."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : guestStart(); break;
                case 2 : customerStart(); break;
                case 3 : AdminstrationUI.login(); break;
            }
        }
        while (choice != 0);

    }

    private static void guestStart() {
        String[] options = {
                "Exit.",
                "Buy Food.",
                "Buy Drink.",
                "Register Account."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : FoodUI.buyFood(); break;
                case 2 : DrinkUI.buyDrink(); break;
                case 3 : CustomerUI.regist(); break;
            }
        }
        while (choice != 0);

    }

    private static void customerStart() {
        String[] options = {
                "Exit.",
                "Login.",
                "Register Account.",
                "change password.",
                "Cancellation Account."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : CustomerUI.login(); break;
                case 2 : CustomerUI.regist(); break;
                case 3 : CustomerUI.changePassword(); break;
                case 4 : CustomerUI.cancellation(); break;
            }
        }
        while (choice != 0);

        // 退出的时候， 顺便把账号也退出登录
        CustomerUI.exitLogin();
    }


}
