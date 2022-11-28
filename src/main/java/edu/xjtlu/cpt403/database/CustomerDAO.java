package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDAO extends AbstractDataBase<Customer> {
    final String sheetName = Customer.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        CustomerDAO dao = new CustomerDAO();
        List<Customer> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        System.out.println(dao.insert(new Customer(0, "haichao", "123456", "1828888888", "unknown", 1, 9), true));
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
    public boolean delete(Customer object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, Customer object) throws Exception {
        return false;
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


}
