package edu.xjtlu.cpt403.userinterface;


import static edu.xjtlu.cpt403.util.UserInterfaceUtils.showOptionsAndGetChoice;

public class AdminstrationUI {

    public static void run(String username) {
        System.out.println("=============================================================");
        System.out.println("Hi, " + username + " welcome to adminstration UI!");

        String[] options = {
                "Exit.",
                "Inventory management.",
                "Purchase of goods.",
                "Reservation room."
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
    }

    private static void bookRoom() {
        String[] options = {
                "Exit.",
                "New reservation.",
                "Query reservation.",
                "Cancel reservation."
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

    private static void purchaseGoods() {
        String[] options = {
                "Exit.",
                "Buy food.",
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

    private static void inventoryManagement() {
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
