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
    public int delete(int id) throws Exception {
        return 0;
    }

    @Override
    public int update(int id, SalesRecord object) throws Exception {
        return 0;
    }


    @Override
    public SalesRecord select(int id) throws Exception {
        return null;
    }

    @Override
    protected String getSheetName() {
        return null;
    }
}
