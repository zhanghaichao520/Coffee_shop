package edu.xjtlu.cpt403.util;

import edu.xjtlu.cpt403.entity.User;
import org.apache.commons.lang3.StringUtils;

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
        String input = keyboard.nextLine().trim();
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

    public static double getDoubleNumberInput(double lower,double upper){
        String input = keyboard.nextLine().trim();
        double number = 0.0;
        try {
            number = Double.parseDouble(input);
        }
        catch (NumberFormatException ex) {
            System.out.println("Error. Please input a number between " + lower + " and " + upper + ".");
            return getDoubleNumberInput(lower, upper);
        }
        if (number < lower || number > upper) {
            System.out.println("Error. Please input a number between " + lower + " and " + upper + ".");
            return getDoubleNumberInput(lower, upper);
        }
        return number;
    }

    /**
     * 获取一个数字得方法
     * 这个方法不能输入0和负数，只能输入大于0的数
     * 如果输入exit的话，number返回0
     */
    public static int getNumberInput(){
        Boolean temp = true;
        int number = 0;
        System.out.println("Please input the number and it can not be zero!");
        do{
            String input = keyboard.nextLine();
            if (StringUtils.isNumeric(input)){
                number = Integer.parseInt(input);
                if (number == 0){
                    System.out.println("You can not input zero, please input the correct number.");
                    System.out.println("Please input a number,try again:");
                }else if (number > 0){
                    temp = false;
                }else{
                    System.out.println("Please enter a positive number");
                    System.out.println("Please input a number,try again:");
                }
            }else if(input == "exit"){
                number = 0;
                break;
            }else{
                System.out.println("You should input a number, try again please! You can input exit if you do not want to buy anything.");
                System.out.println("Please input a number,try again:");
            }
        }while(temp);

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
