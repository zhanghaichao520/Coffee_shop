package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.entity.SalesRecord;
import edu.xjtlu.cpt403.entity.SalesRecord;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class SalesRecordDAO extends AbstractDataBase<SalesRecord> {
    final String sheetName = SalesRecord.class.getSimpleName();


    @Override
    public List<SalesRecord> selectAll() throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                SalesRecord salesrecord = new SalesRecord();
                return convert(rowData, salesrecord);
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
    public boolean insert(SalesRecord object, boolean idIncrAuto) throws Exception {
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
    public boolean delete(SalesRecord object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, SalesRecord object) throws Exception {
        return false;
    }

    @Override
    public SalesRecord select(int id) throws Exception {
        List<SalesRecord> salesRecordList = selectAll();
        if (CollectionUtils.isEmpty(salesRecordList)) {
            throw new Exception("our system roomList is empty! ");
        }

        for (SalesRecord salesrecord : salesRecordList) {
            if (id == salesrecord.getId()) {
                return salesrecord;
            }
        }

        throw new Exception("salesRecord id = " + id + " not find in our system");
    }


}