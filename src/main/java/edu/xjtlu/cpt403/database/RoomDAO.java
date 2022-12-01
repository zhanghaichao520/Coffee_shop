package edu.xjtlu.cpt403.database;

import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.util.ExcelUtils;
import edu.xjtlu.cpt403.util.RowData;
import org.apache.commons.collections4.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class RoomDAO extends AbstractDataBase<Room> {
    final String sheetName = Room.class.getSimpleName();


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
    public boolean delete(Room object) throws Exception {
        return false;
    }

    @Override
    public boolean update(int id, Room object) throws Exception {
        return false;
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


}
