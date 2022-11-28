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
                case 1 -> inventoryManagement();
                case 2 -> purchaseGoods();
                case 3 -> bookRoom();
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
                case 1 -> RoomUI.newReservation();
                case 2 -> RoomUI.queryReservation();
                case 3 -> RoomUI.cancelReservation();
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
                case 1 -> FoodUI.buyFood();
                case 2 -> DrinkUI.buyDrink();
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
                case 1 -> FoodUI.queryFood();
                case 2 -> FoodUI.addFood();
                case 3 -> FoodUI.updateFood();
                case 4 -> FoodUI.removeFood();
                case 5 -> DrinkUI.queryDrink();
                case 6 -> DrinkUI.addDrink();
                case 7 -> DrinkUI.updateDrink();
                case 8 -> DrinkUI.removeDrink();
            }
        }
        while (choice != 0);
    }


}
