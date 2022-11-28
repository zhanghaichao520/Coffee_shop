package edu.xjtlu.cpt403.entity;



import edu.xjtlu.cpt403.database.AdminUserDAO;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;


public abstract class User implements Comparable<User>, Serializable {
    /** User ID*/
    private int id;

    /** User Name*/
    private String name;

    private String passWord;

    private String phoneNumber;


    public User() {
    }


    public User(int id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public User(int id, String name, String passWord, String phoneNumber) {
        validateName(name);
        validatePassword(passWord);
        this.id = id;
        this.name = name;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
    }

    public static boolean validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty.");
        }
        if (StringUtils.isNumeric(name)) {
            throw new IllegalArgumentException("name cannot be only numberic");
        }

        return true;
    }

    public static boolean validatePassword(String passWord) {
        if (passWord == null || passWord.isEmpty()) {
            throw new IllegalArgumentException("passWord cannot be empty.");
        }
        if (passWord.length() < 6) {
            throw new IllegalArgumentException("passWord length is too short, it must greater than 6!");
        }

        return true;
    }

    /**
     * login
     * @param name
     * @param passWord
     * @param adminUserDAO
     * @return
     */
    public static boolean validateLogin(String name, String passWord, AdminUserDAO adminUserDAO) {
        validateName(name);
        validatePassword(passWord);

        List<AdminUser> result = adminUserDAO.selectAll();
        AdminUser currentUser = null;
        for (AdminUser user : result) {
            if (user != null && name.equals(user.getName())) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) {
            throw new IllegalArgumentException("The admin user is not exist, please try another username!");
        }

        if (!passWord.equals(currentUser.getPassWord())) {
            throw new IllegalArgumentException("The admin user password is not right!");
        }

        // 把当前登录用户设置到全局变量，便于后续UI访问, 感觉这样的方式有一点TRICK， 但是也没想到有什么更好的办法
        UserInterfaceUtils.setCurrentUser(currentUser);
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int compareTo(User o) {
        return name.compareTo(o.getName());
    }
}
