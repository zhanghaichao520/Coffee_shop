package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDAO extends AbstractDataBase<Customer> {
    final String sheetName = Customer.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        CustomerDAO dao = new CustomerDAO();
        // test select all
        List<Customer> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test insert
        System.out.println(dao.insert(new Customer(888, "wyw", "123456", "1828888888", "unknown", 1, 9), false));
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test select
        System.out.println(JSON.toJSONString(dao.select(888)));

        // update
        System.out.println("update record count: " + dao.update(888, new Customer(888, "wyw", "654321", "1828889999", "boy", 0, 9)));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // delete
//        System.out.println("delete record count: " + dao.delete(888));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

    }

    @Override
    public List<Customer> selectAll() throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                Customer customer = new Customer();
                return convert(rowData, customer);
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
    public boolean insert(Customer object, boolean idIncrAuto) throws Exception {
        if (idIncrAuto) {
            object.setId(selectAll().size() + 1);
        }
        try {
            ExcelUtils.insert(convert(object), path, sheetName);
        } catch (Exception e) {
            System.out.println("insert customer error: " + e.getMessage());
            return false;
        }
        return true;
    }


    @Override
    public int delete(int id) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<Integer> rowIndexList = new ArrayList<>(list.stream().filter(rowData -> {
            Customer customer = new Customer();
            try {
                convert(rowData, customer);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == customer.getId() && id > 0;
        }).map(RowData::getRowIndex).collect(Collectors.toList()));

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
    public int update(int id, Customer object) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<RowData> rowDataList = list.stream().filter(rowData -> {
            Customer customer = new Customer();
            try {
                convert(rowData, customer);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == customer.getId() && id > 0;
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(rowDataList)) {
            return 0;
        }

        return update(rowDataList, convert(object));
    }

    public boolean userExist(int id) {
        try {
            Customer c = select(id);
            return c != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Customer select(int id) throws Exception {
        List<Customer> customers = selectAll();
        if (CollectionUtils.isEmpty(customers)) {
            throw new Exception("our system customer is empty! ");
        }

        for (Customer customer : customers) {
            if (id == customer.getId()) {
                return customer;
            }
        }

        throw new Exception("user id = " + id + " not find in our system");
    }

    public Customer findByName(String name) throws Exception {
        User.validateName(name);

        List<Customer> customers = selectAll();
        if (CollectionUtils.isEmpty(customers)) {
            throw new Exception("our system customer is empty! ");
        }

        for (Customer customer : customers) {
            if (name.equals(customer.getName())) {
                return customer;
            }
        }

        throw new Exception("username = " + name + " not find in our system");
    }

    @Override
    protected String getSheetName() {
        return sheetName;
    }
}
