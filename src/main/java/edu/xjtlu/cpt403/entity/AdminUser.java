package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.database.AdminUserDAO;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Objects;
public class AdminUser extends User{

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

    public AdminUser() {
    }

    public AdminUser(int id, String name) {
        super(id, name);
    }

    public AdminUser(int id, String name, String passWord, String phoneNumber) {
        super(id, name, passWord, phoneNumber);
    }
}
