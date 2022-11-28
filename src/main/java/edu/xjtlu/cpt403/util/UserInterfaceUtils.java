package edu.xjtlu.cpt403.util;

import edu.xjtlu.cpt403.entity.User;

import java.util.Scanner;
import java.util.function.Function;

public class UserInterfaceUtils {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserInterfaceUtils.currentUser = currentUser;
    }

    static Scanner keyboard = new Scanner(System.in);;
    //shows the user a list of options from an array, and gets their preferred choice
    //offset = 0 if you want to start numbering options at 0, 1 if you want to start at 1
    public static int showOptionsAndGetChoice(String[] options, int offset) {
        System.out.println("Please choose one of the following options.");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + offset) + ". " + options[i]);
        }
        return getIntInput(offset, options.length - 1 + offset);
    }

    //gets a number between lower and upper from the user
    public static int getIntInput(int lower, int upper) {
        String input = keyboard.nextLine().strip();
        int number = 0;
        try {
            number = Integer.parseInt(input);
        }
        catch (NumberFormatException ex) {
            System.out.println("Error. Please input a number between " + lower + " and " + upper + ".");
            return getIntInput(lower, upper);
        }
        if (number < lower || number > upper) {
            System.out.println("Error. Please input a number between " + lower + " and " + upper + ".");
            return getIntInput(lower, upper);
        }
        return number;
    }

    public static String getStringInput(String hint, Function<String, Boolean> validateFunction) {
        System.out.println(hint);
        String name = keyboard.nextLine();
        try {
            if (validateFunction != null) {
                if (!validateFunction.apply(name)) {
                   throw new IllegalArgumentException();
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Invalid string input, " + ex.getMessage());
            System.out.println("Please try again.");
            return getStringInput(hint, validateFunction);
        }
        return name;
    }

    public static void close() {
        keyboard.close();
    }

}
