package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.Drink;
import edu.xjtlu.cpt403.entity.Food;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class FoodDAO extends AbstractDataBase<Food> {
    final String sheetName = Food.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        FoodDAO dao = new FoodDAO();
        // test select all
        List<Food> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test insert
        System.out.println(dao.insert(new Food("cookie", 30, 0, 100, 1), true));
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // test select
        System.out.println(JSON.toJSONString(dao.select(1)));
    }
    @Override
    public List<Food> selectAll() throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                Food food = new Food();
                return convert(rowData, food);
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
    public boolean insert(Food object, boolean idIncrAuto) throws Exception {
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
    public boolean delete(Food object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, Food object) throws Exception {
        return false;
    }

    @Override
    public Food select(int id) throws Exception {
        List<Food> foodList = selectAll();
        if (CollectionUtils.isEmpty(foodList)) {
            throw new Exception("our system foodList is empty! ");
        }

        for (Food food : foodList) {
            if (id == food.getId()) {
                return food;
            }
        }

        throw new Exception("food id = " + id + " not find in our system");
    }


}
