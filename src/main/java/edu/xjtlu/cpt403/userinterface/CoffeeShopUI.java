package edu.xjtlu.cpt403.userinterface;

import static edu.xjtlu.cpt403.util.UserInterfaceUtils.showOptionsAndGetChoice;

public class CoffeeShopUI {

    public static void run() throws Exception {

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

    private static void guestStart() throws Exception {
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
                case 3 : CustomerUI.register(); break;
            }
        }
        while (choice != 0);

    }

    private static void customerStart() throws Exception {
        String[] options = {
                "Exit.",
                "Login.",
                "Register Account.",
                "Change Password.",
                "Cancellation Account."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {

                case 1 :
                    try {
                        CustomerUI.login();
                    } catch (Exception e) {
                        System.out.println(e.getMessage() + ", Please try again.");
                        return;
                    }
                    break;
                case 2 : CustomerUI.register(); break;
                case 3 : CustomerUI.changePassword(); break;
                case 4 : CustomerUI.cancellation(); break;
            }
        }
        while (choice != 0);

        // 退出的时候， 顺便把账号也退出登录
        CustomerUI.exitLogin();
    }


}
