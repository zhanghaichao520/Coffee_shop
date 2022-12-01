package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.Drink;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class DrinkDAO extends AbstractDataBase<Drink> {
    final String sheetName = Drink.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        DrinkDAO dao = new DrinkDAO();
        // test select all
        List<Drink> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test insert
        System.out.println(dao.insert(new Drink("binghongcha", 30, 0, 100, 1), true));
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test select
        System.out.println(JSON.toJSONString(dao.select(1)));
    }

    @Override
    public List<Drink> selectAll() throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                Drink drink = new Drink();
                return convert(rowData, drink);
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
    public boolean insert(Drink object, boolean idIncrAuto) throws Exception {
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
    public boolean delete(Drink object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, Drink object) throws Exception {
        return false;
    }

    @Override
    public Drink select(int id) throws Exception {
        List<Drink> drinkList = selectAll();
        if (CollectionUtils.isEmpty(drinkList)) {
            throw new Exception("our system drinkList is empty! ");
        }

        for (Drink drink : drinkList) {
            if (id == drink.getId()) {
                return drink;
            }
        }

        throw new Exception("drink id = " + id + " not find in our system");
    }
}
