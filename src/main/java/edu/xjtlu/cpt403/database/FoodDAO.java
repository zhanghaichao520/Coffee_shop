package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.entity.Food;

import java.util.List;

public class FoodDAO extends AbstractDataBase<Food> {
    final String sheetName = Food.class.getSimpleName();


    @Override
    public List<Food> selectAll() throws Exception {
        return null;
    }

    @Override
    public boolean insert(Food object, boolean idIncrAuto) throws Exception {
        return false;
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
        return null;
    }


}
