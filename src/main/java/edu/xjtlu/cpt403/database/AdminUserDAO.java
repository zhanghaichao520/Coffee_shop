package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminUserDAO extends AbstractDataBase<AdminUser> {
    final String sheetName = AdminUser.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        AdminUserDAO dao = new AdminUserDAO();
        // query
        List<AdminUser> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // insert
        System.out.println(dao.insert(new AdminUser(888, "haichao", "123456", "1828888888"), false));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // update
        System.out.println("update record count: " + dao.update(888, new AdminUser(888, "zhanghaichao", "654321", "1828889999")));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // delete
        System.out.println("delete record count: " + dao.delete(888));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));
    }

    @Override
    public int delete(int id) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<Integer> rowIndexList = new ArrayList<>(list.stream().filter(rowData -> {
            AdminUser adminUser = new AdminUser();
            try {
                convert(rowData, adminUser);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == adminUser.getId() && id > 0;
        }).map(RowData::getRowIndex).toList());

        // 要从后往前删除，要不然前面的删了之后， 后面的记录就会顶上来， 到时候row index就变了
        rowIndexList.sort(Comparator.reverseOrder());
        if (CollectionUtils.isNotEmpty(rowIndexList)) {
            for (Integer rowIndex : rowIndexList) {
                ExcelUtils.delete(rowIndex, path, sheetName);
            }
        }
        return rowIndexList.size();
    }

    @Override
    public List<AdminUser> selectAll()  {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                AdminUser adminUser = new AdminUser();
                return convert(rowData, adminUser);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean insert(AdminUser object, boolean idIncrAuto) throws Exception {
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

    @Override
    public int update(int id, AdminUser object) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<RowData> rowDataList = list.stream().filter(rowData -> {
            AdminUser adminUser = new AdminUser();
            try {
                convert(rowData, adminUser);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == adminUser.getId() && id > 0;
        }).toList();

        if (CollectionUtils.isEmpty(rowDataList)) {
           return 0;
        }

        return update(rowDataList, convert(object));
    }

    @Override
    public AdminUser select(int id) throws Exception {
        List<AdminUser> adminUsers = selectAll();
        if (CollectionUtils.isEmpty(adminUsers)) {
            throw new Exception("our system adminUsers is empty! ");
        }

        for (AdminUser adminUser : adminUsers) {
            if (id == adminUser.getId()) {
                return adminUser;
            }
        }

        throw new Exception("adminUser id = " + id + " not find in our system");
    }

    @Override
    protected String getSheetName() {
        return sheetName;
    }


}
