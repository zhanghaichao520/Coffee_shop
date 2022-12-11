package edu.xjtlu.cpt403.userinterface;


import edu.xjtlu.cpt403.database.CustomerDAO;
import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

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
                "Reserve Room.",
                "Query My Reservation.",
                "Query All Available Room.",
                "Cancel Reservation.",
                "Change Password.",
                "Cancel Account."
        };
        int choice;
        do {
            choice = showOptionsAndGetChoice(options, 0);
            switch (choice) {
                case 1 : becomeVip(); break;
                case 2 : FoodUI.buyFood(); break;
                case 3 : DrinkUI.buyDrink(); break;
                case 4 : RoomUI.newReservation(); break;
                case 5 : RoomUI.queryReservation(false, true); break;
                case 6 : RoomUI.queryReservation(true, false); break;
                case 7 : RoomUI.cancelReservation(); break;
                case 8 : changePassword(); break;
                case 9 : cancellation(); break;

            }
        }
        while (choice != 0);
    }

    /**
     * 注册新用户
     * 输入信息 名字， 密码 ，重复输入密码
     *
     *  1. 创建新Customer object
     *  2. 用户输入用户名
     *  3. 用户设置性别（男，女，保密）
     *  4. 用户输入手机号
     *  5. 用户设置密码
     *  6. 重复输入密码
     *  7. 检查两次输入是否一致
     *  8. 确认无误后，数据库写入新用户信息，注册成功
     *
     */
    public static void register(){

        Customer newCustomer = new Customer();
        System.out.println("=============================================================");
        System.out.println("Regist to be our guest");

        String username = getStringInput("Set your username: ", s -> User.validateName(s));
        newCustomer.setName(username);
        String [] genderOptions = {
                "MALE",
                "FEMALE",
                "SECRET"
        };
        int choice;
        do{
            choice =showOptionsAndGetChoice(genderOptions, 0);
            switch (choice) {
                case 1 : newCustomer.setGender(genderOptions[1]); break;
                case 2 : newCustomer.setGender(genderOptions[2]);  break;
                case 3 : newCustomer.setGender(genderOptions[3]);  break;
            }
        } while (choice == 0);

        String phoneNumeber = getStringInput("Please input your phone number: ", s -> User.validatePhoneNumber(s));
        newCustomer.setPhoneNumber(phoneNumeber);

        String password = getStringInput("Set your password: ", s -> User.validatePassword(s));

        String password2 = getStringInput("Please re-input your password: ", s -> User.validatePassword(s));

        while (password.equals(password2) == false){
            System.out.println("Please make sure the twice input are the same!");
            password = getStringInput("Set your password: ", s -> User.validatePassword(s));
            password2 = getStringInput("Please re-input your password: ", s -> User.validatePassword(s));
        }
        newCustomer.setPassWord(password);
        try {
            DataBaseManager.getCustomerDAO().insert(newCustomer,true);
        }
        catch (Exception e) {
            System.out.println("Fail in register." + e.getMessage());
            System.out.println("Please try again.");
            return;
        }
        System.out.println("Register successfully!");

        // 核心 customer  插入数据
    }


    /**
     * 用户修改密码
     * 输入原密码，然后重复输入两次新密码， 更新数据库，然后提示修改成功
     *
     *  1. 用户输入旧密码
     *  2. 验证用户是否正确输入或用户是否存在
     *  3. 验证通过后，用户输入新密码
     *  4. 数据库写入新用户信息，密码修改成功
     *
     */
    public static void changePassword(){
        //若未登录，提醒登录
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            try {
                login();
            } catch (Exception e) {
                System.out.println("Fail in log in." + e.getMessage());
                System.out.println("Please try again.");
                return;
            }
        }

        System.out.println("=============================================================");
        System.out.println("Reset your password");

        //获取已登陆用户的信息
        User currentUser = UserInterfaceUtils.getCurrentUser();

        //改密码操作
        String oldPassword = getStringInput("Please type in your old password:", s -> User.validatePassword(s));

        try {
            Customer.validateLogin(currentUser.getName(), oldPassword, DataBaseManager.getCustomerDAO());

        } catch (Exception ex) {
            System.out.println(ex.getMessage() + ", Please try again.");
            changePassword();
            return;
        }
        Customer customer = null;
        if (currentUser instanceof Customer) {
            customer = (Customer) currentUser;
        }
        //输入新密码
        String newPassword = getStringInput("Please type in your new password:", s -> User.validatePassword(s));
        currentUser.setPassWord(newPassword);
        try {
            DataBaseManager.getCustomerDAO().update(currentUser.getId(), customer);
        }
        catch (Exception e) {
            System.out.println("Fail in change password." + e.getMessage());
            System.out.println("Please try again.");
            return;
        }
        System.out.println("Password successfully changed.");
        // 核心 customer  修改密码字段
    }

    /**
     * 用户注销，修改用户状态（改状态有个好处就是可以让账号在冻结和恢复来回切换，但是就还得提供一下resume的方法， 比如积分可以不清空， 下次还能继续攒），
     * 或者简单粗暴的删除用户，下次让他重新注册，记得还要把所有关联预定房间也都取消掉
     * 1. 给用户展示已预订房间的信息，询问用户是否确认注销
     * 2. 用户确认后，取消用户预订的房间，从数据库中删除用户的信息
     * 3. 若用户选择不注销则直接退出
     */
    public static void cancellation(){
        //若未登录，提醒登录
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            try {
                login();
            } catch (Exception e) {
                System.out.println("Fail in log in." + e.getMessage());
                System.out.println("Please try again.");
                return;
            }
        }

        //获取已登陆用户的信息
        User currentUser = UserInterfaceUtils.getCurrentUser();

        System.out.println("=============================================================");

        System.out.println("Do you confirm to cancel all of your user information? (All the rooms have been registered will be cancelled)");

        //获取用户已预订房间信息
        List<Room> reservedRoom = Room.getReservedRoomByUserId(currentUser.getId());

        //展示用户已预订房间信息以辅助用户决定是否注销用户（已预订房间的用户注销的可能性偏小）
        if (CollectionUtils.isEmpty(reservedRoom)) {
            System.out.println("You have not book any room yet.");
        }
        else{
            System.out.println("There are the rooms you have been booking for:");
            for (Room room : reservedRoom) {
                System.out.println(room);
            }
        }

        //用户作出选择
        String [] confirmOptions = {
                "Yes, I want to cancel my information.",
                "No, I do not want to cancel."
        };
        int choice;
        do{
            choice =showOptionsAndGetChoice(confirmOptions, 0);
            switch (choice) {
                case 1 : break;
                case 2 : return;
            }
        } while (choice != 0);

        //注销操作
        //取消客户预订
        if (!CollectionUtils.isEmpty(reservedRoom)) {
            for (Room room : reservedRoom) {
                room.setBookUserid(0); //取消预定， 直接把预定用户id更新为0就行
            }
        }

        //删除用户
        Customer customer = null;
        if (currentUser instanceof Customer) {
            customer = (Customer) currentUser;
        }
        try {
            DataBaseManager.getCustomerDAO().delete(customer.getId());
        }
        catch (Exception e) {
            System.out.println("Fail in cancellation." + e.getMessage());
            System.out.println("Please try again.");
            return;
        }
        exitLogin();
        System.out.println("User has been cancelled successfully.");
        //没搞懂冻结的机制，如果是改变用户状态的话，那么不仅是customer object要多加一个属性，登陆选项等等也要改，而且从哪个接口解冻呢
        //而且冻结的话，占内存，如果要做些附加功能，可以考虑一下长期未登录用户的自动注销

        // 核心 customer  删除记录
    }


    /**
     * 开通会员流程
     */
    public static void becomeVip(){
        //若未登录，提醒登录
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            try {
                login();
            } catch (Exception e) {
                System.out.println("Fail in log in." + e.getMessage());
                System.out.println("Please try again.");
                return;
            }
        }

        //获取已登陆用户的信息
        User currentUser = UserInterfaceUtils.getCurrentUser();

        System.out.println("=============================================================");
        System.out.println("Become our VIP");

        // Check if the customer is already a VIP
        Customer customer = null;
        if (currentUser instanceof Customer) {
            customer = (Customer) currentUser;
        }

        if(customer.getIsVip() == 1){
            System.out.println("You are already our VIP!");
            return;
        }
        //询问用户是否确认开通VIP
        System.out.println("A VIP can choose to have 5% off discount/ get a stamp on loyalty card on each time of purchase.");
        System.out.println("Only $1688 per year, come to be our VIP for unique discount!");
        String [] confirmOptions = {
                "Yes, I want to be VIP.",
                "No, I do not want to be VIP."
        };
        int choice;
        do{
            choice =showOptionsAndGetChoice(confirmOptions, 0);
            switch (choice) {
                case 1 : break;
                case 2 : return;
            }
        } while (choice != 0);

        //若用户选择开通VIP，则在数据库中将用户VIP状态更改

        customer.setIsVip(1);
        try {
            CustomerDAO dao = new CustomerDAO();
            dao.update(currentUser.getId(), customer);
        }
        catch (Exception e) {
            System.out.println("Fail in activate VIP status." + e.getMessage());
            System.out.println("Please try again.");
            return;
        }
        System.out.println("Congratulations! You are our VIP now.");
        // 核心 customer  修改VIP字段
    }


    /**
     * 退出登录
     */
    public static void exitLogin() {
        UserInterfaceUtils.setCurrentUser(getCurrentUser());
    }
}
