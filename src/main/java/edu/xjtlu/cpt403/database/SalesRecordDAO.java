package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.entity.SalesRecord;

import java.util.List;

public class SalesRecordDAO extends AbstractDataBase<SalesRecord>{
    @Override
    public List<SalesRecord> selectAll() throws Exception {
        return null;
    }

    @Override
    public boolean insert(SalesRecord object, boolean idIncrAuto) throws Exception {
        return false;
    }

    @Override
    public boolean delete(SalesRecord object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, SalesRecord object) throws Exception {
        return false;
    }

    @Override
    public SalesRecord select(int id) throws Exception {
        return null;
    }
}