package edu.xjtlu.cpt403;

import entity.Person;
import universitysystem.UniversityDatabase;

import java.util.ArrayList;
import java.util.Scanner;

public class CoffeeShopUI {


    static Scanner keyboard;

    public static void run(UniversityDatabase db) {
        keyboard = new Scanner(System.in);
        System.out.println("Welcome to the Our Coffee Shop!");
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
                case 1 -> db.view();
                case 2 -> db.view();
                case 3 -> {
                    ArrayList<Person> removed = db.updateAtEndOfYear();
                    for (Person p : removed) {
                        System.out.println("Person " + p + " removed from database.");
                    }
                }
            }
        }
        while (choice != 0);
        keyboard.close();
    }

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

}
