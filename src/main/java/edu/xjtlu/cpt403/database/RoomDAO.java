package edu.xjtlu.cpt403.database;

import com.alibaba.fastjson.JSON;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.entity.Room;
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

public class RoomDAO extends AbstractDataBase<Room> {
    final String sheetName = Room.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        RoomDAO dao = new RoomDAO();
        // query
        List<Room> result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // insert
        System.out.println(dao.insert(new Room(888, "room1", 100D, DateUtils.getDate(new Date()), 888,10), false));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // update
        System.out.println("update record count: " + dao.update(888, new Room(888, "room1", 100D, DateUtils.getDate(new Date()), 2,10)));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));

        // delete
        System.out.println("delete record count: " + dao.delete(888));

        // query
        result = dao.selectAll();
        System.out.println(JSON.toJSONString(result));
    }

    @Override
    public List<Room> selectAll() throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        return list.stream().map(rowData -> {
            try {
                Room room = new Room();
                return convert(rowData, room);
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
    public boolean insert(Room object, boolean idIncrAuto) throws Exception {
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
            Room room = new Room();
            try {
                convert(rowData, room);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == room.getId() && id > 0;
        }).map(RowData::getRowIndex).toList());

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
    public int update(int id, Room object) throws Exception {
        List<RowData> list = ExcelUtils.readAll(path, sheetName);
        List<RowData> rowDataList = list.stream().filter(rowData -> {
            Room room = new Room();
            try {
                convert(rowData, room);
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return id == room.getId() && id > 0;
        }).toList();

        if (CollectionUtils.isEmpty(rowDataList)) {
            return 0;
        }

        return update(rowDataList, convert(object));
    }

    @Override
    public Room select(int id) throws Exception {
        List<Room> roomList = selectAll();
        if (CollectionUtils.isEmpty(roomList)) {
            throw new Exception("our system roomList is empty! ");
        }

        for (Room room : roomList) {
            if (id == room.getId()) {
                return room;
            }
        }

        throw new Exception("room id = " + id + " not find in our system");
    }

    @Override
    protected String getSheetName() {
        return sheetName;
    }
}
