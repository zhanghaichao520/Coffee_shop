package edu.xjtlu.cpt403.database;

public class DataBaseManager {
    private static AdminUserDAO adminUserDAO = new AdminUserDAO();
    private static CustomerDAO customerDAO = new CustomerDAO();
    private static DrinkDAO drinkDAO = new DrinkDAO();
    private static FoodDAO foodDAO = new FoodDAO();
    private static RoomDAO roomDAO = new RoomDAO();


    public static AdminUserDAO getAdminUserDAO() {
        return adminUserDAO;
    }

    public static CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public static DrinkDAO getDrinkDAO() {
        return drinkDAO;
    }

    public static FoodDAO getFoodDAO() {
        return foodDAO;
    }

    public static RoomDAO getRoomDAO() {
        return roomDAO;
    }
}
