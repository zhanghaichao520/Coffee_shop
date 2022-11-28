package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.entity.Drink;

import java.util.List;

public class DrinkDAO extends AbstractDataBase<Drink> {
    final String sheetName = Drink.class.getSimpleName();


    @Override
    public List<Drink> selectAll() throws Exception {
        return null;
    }

    @Override
    public boolean insert(Drink object, boolean idIncrAuto) throws Exception {
        return false;
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
        return null;
    }
}
