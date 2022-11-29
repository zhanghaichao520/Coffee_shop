package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.entity.User;

import static edu.xjtlu.cpt403.util.UserInterfaceUtils.getStringInput;
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
                case 3 : administrationStart(); break;
            }
        }
        while (choice != 0);

    }

    private static void guestStart() {

    }

    private static void customerStart() {

    }

    private static void administrationStart() {
        String username = getStringInput("Please input the admin username", s -> User.validateName(s));
        String password = getStringInput("Please input the admin password", s -> User.validatePassword(s));

        try {
            User.validateLogin(username, password, DataBaseManager.getAdminUserDAO());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage() + ", Please try again.");
            administrationStart();
            return;
        }

        AdminstrationUI.run(username);

    }

}
