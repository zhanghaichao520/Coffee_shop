package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.entity.SalesRecord;
import edu.xjtlu.cpt403.entity.SalesRecord;
import edu.xjtlu.cpt403.util.DateUtils;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SalesRecordDAO extends AbstractDataBase<SalesRecord> {
    final String sheetName = SalesRecord.class.getSimpleName();


    public static void main(String[] args) throws Exception {
        SalesRecordDAO dao = new SalesRecordDAO();
        // query
        List<SalesRecord> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // insert
        System.out.println(dao.insert(new SalesRecord(1,1,"Mocca",1,1,30, DateUtils.getDate(new Date())),false));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // update
        System.out.println("update record count: " + dao.update(1,new SalesRecord(1,1,"Mocca",1,1,30,DateUtils.getDate(new Date()))));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // delete
        System.out.println("delete record count: " + dao.delete(1));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));
    }

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
        }).collect(Collectors.collect(Collectors.toList()));
    }


    @Override
    public boolean insert(SalesRecord object, boolean idIncrAuto) throws Exception {
        if (idIncrAuto) {
            object.setId(selectAll().size() + 1);
        }
        try {
            ExcelUtils.insert(convert(object), path, sheetName);
        } catch (Exception e) {
            System.out.println("insert purchase error: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int delete(int id) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<Integer> rowIndexList = new ArrayList<>(list.stream().filter(rowData -> {
            SalesRecord salesrecord = new SalesRecord();
            try {
                convert(rowData, salesrecord);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == salesrecord.getId() && id > 0;
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
    public int update(int id, SalesRecord object) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<RowData> rowDataList = list.stream().filter(rowData -> {
            SalesRecord salesrecord = new SalesRecord();
            try {
                convert(rowData, salesrecord);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == salesrecord.getId() && id > 0;
        }).toList();

        if (CollectionUtils.isEmpty(rowDataList)) {
            return 0;
        }

        return update(rowDataList, convert(object));
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

    public List<SalesRecord> findByUserId(int userId) {
        List<SalesRecord> result = new ArrayList<>();
        try {
            List<SalesRecord> salesRecordList = selectAll();
            for (SalesRecord salesrecord : salesRecordList) {
                if (userId == salesrecord.getUserid()) {
                    result.add(salesrecord);
                }
            }
        } catch (Exception e) {
            System.out.println("read sale recorde exception: " + e.getMessage());
        }
        return result;
    }

    @Override
    protected String getSheetName() { return sheetName;}
}

