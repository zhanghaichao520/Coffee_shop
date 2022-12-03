package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;

import static edu.xjtlu.cpt403.util.UserInterfaceUtils.*;

public class CustomerUI {
    /**
     * 登录流程
     */
    public static void login() throws Exception {
        String username = getStringInput("Please input your username", s -> User.validateName(s));
        String password = getStringInput("Please input your password", s -> User.validatePassword(s));

        try {
            Customer.validateLogin(username, password, DataBaseManager.getCustomerDAO());
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + ", Please try again.");
            login();
            return;
        }

        run(username);

    }

    public static void run(String username) throws Exception {
        System.out.println("=============================================================");
        System.out.println("Hi, " + username + " welcome to our coffee shop !");

        String[] options = {
                "Exit.",
                "Become VIP.",
                "Buy Food.",
                "Buy Drink.",
                "Reservation room.",
                "Query My Reservation.",
                "Cancel Reservation."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : FoodUI.buyFood(); break;
                case 2 : FoodUI.buyFood(); break;
                case 3 : DrinkUI.buyDrink(); break;
                case 4 : RoomUI.newReservation(); break;
                case 5 : RoomUI.queryReservation(); break;
                case 6 : RoomUI.cancelReservation(); break;

            }
        }
        while (choice != 0);
    }

    /**
     * 注册新用户
     *  1. 输入信息 名字， 密码 ，重复输入密码
     */
    public static void regist() {

        // 核心 customer  插入数据
    }


    /**
     * 用户修改秘密
     *  输入原密码
     *  然后重复输入两次新密码， 更新数据库，然后提示修改成功
     */
    public static void changePassword() {
        // 核心 customer  修改密码字段
    }

    /**
     * 用户注销，修改用户状态（改状态有个好处就是可以让账号在冻结和恢复来回切换，但是就还得提供一下resume的方法， 比如积分可以不清空， 下次还能继续攒），
     * 或者简单粗暴的删除用户，下次让他重新注册
     *
     * 记得还要把所有关联预定房间也都取消掉
     */
    public static void cancellation() {
        // 核心 customer  删除记录
    }

    /**
     * 开通会员流程
     */
    public static void becomeVip() {
        // 核心 customer  修改VIP字段
    }


    /**
     * 退出登录
     */
    public static void exitLogin() {
        UserInterfaceUtils.setCurrentUser(getCurrentUser());
    }
}
