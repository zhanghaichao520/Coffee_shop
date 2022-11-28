package edu.xjtlu.cpt403.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;
@Data
public class AdminUser extends User{

    public AdminUser() {
    }

    public AdminUser(int id, String name) {
        super(id, name);
    }

    public AdminUser(int id, String name, String passWord, String phoneNumber) {
        super(id, name, passWord, phoneNumber);
    }
}
