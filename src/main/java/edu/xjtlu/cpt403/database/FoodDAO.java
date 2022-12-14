package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.Food;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FoodDAO extends AbstractDataBase<Food> {
    final String sheetName = Food.class.getSimpleName();

    public static void main(String[] args) throws Exception {
//        FoodDAO dao = new FoodDAO();
//        // test select all
//        List<Food> result = dao.selectAll();
//        System.out.println(JSON.toJSONString(result));


        //test update
//        Food food = DataBaseManager.getFoodDAO().select(1);
//        food.setPrice(16.0);
//        dao.update(1,food);
//        System.out.println(JSON.toJSONString(result));

//        // test insert
//        System.out.println(dao.insert(new Food("cookie", 30, 0, 100, 1), true));
//        result = dao.selectAll();
//        System.out.println(JSON.toJSONString(result));
//
//        // test select
//        System.out.println(JSON.toJSONString(dao.select(1)));

        //test del
//        dao.delete(4);
//        result = dao.selectAll();
//        System.out.println(JSON.toJSONString(result));

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
    public int delete(int id) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<Integer> rowIndexList = new ArrayList<>(list.stream().filter(rowData -> {
            Food food = new Food();
            try {
                convert(rowData, food);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == food.getId() && id > 0;
        }).map(RowData::getRowIndex).collect(Collectors.toList()));

        // ????????????????????????????????????????????????????????? ????????????????????????????????? ?????????row index?????????
        rowIndexList.sort(Comparator.reverseOrder());
        if (CollectionUtils.isNotEmpty(rowIndexList)) {
            for (Integer rowIndex : rowIndexList) {
                ExcelUtils.delete(rowIndex, path, sheetName);
            }
        }
        return rowIndexList.size();
    }

    @Override
    public int update(int id, Food object) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<RowData> rowDataList = list.stream().filter(rowData -> {
            Food food = new Food();
            try {
                convert(rowData, food);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == food.getId() && id > 0;
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(rowDataList)) {
            return 0;
        }

        return update(rowDataList, convert(object));
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

    @Override
    protected String getSheetName() {
        return sheetName;
    }
}
