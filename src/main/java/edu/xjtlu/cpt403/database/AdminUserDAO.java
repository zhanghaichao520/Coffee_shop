package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class AdminUserDAO extends AbstractDataBase<AdminUser> {
    final String sheetName = "AdminUser";

    public static void main(String[] args) throws Exception {
//        AdminUserDAO dao = new AdminUserDAO();
//        List<AdminUser> result = dao.selectAll();
//        System.out.println(JSON.toJSONString(result));
//
//        System.out.println(dao.insert(new AdminUser(0, "haichao", "123456", "1828888888"), true));
//
//        result = dao.selectAll();
//        System.out.println(JSON.toJSONString(result));
        System.out.println(AdminUser.class.getSuperclass().getSimpleName());

        System.out.println(AdminUser.class.getSimpleName());

    }

    @Override
    public List<AdminUser> selectAll()  {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                AdminUser adminUser = new AdminUser();
                return convert(rowData, adminUser);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    boolean insert(AdminUser object, boolean idIncrAuto) throws Exception {
        if (idIncrAuto) {
             object.setId(selectAll().size() + 1);
        }
        try {
            ExcelUtils.insert(convert(object), path, sheetName);
        } catch (Exception e) {
            System.out.println("insert user error: " + e.getMessage());
            return false;
        }
        return true;
    }


}
