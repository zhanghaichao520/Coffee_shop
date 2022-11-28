package edu.xjtlu.cpt403;

import edu.xjtlu.cpt403.userinterface.CoffeeShopUI;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;

public class Application {
    public static void main(String[] args) {

        try {
            CoffeeShopUI.run();
        } finally {
            UserInterfaceUtils.close();
        }
    }
}