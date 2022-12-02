package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.RoomDAO;
import edu.xjtlu.cpt403.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room implements Comparable<Room>, Serializable {
    private int id;
    private String name;
    private double price;

    private String bookDate;
    private int bookUserid;
    private int maxCapacity;

    public Room() {
    }

    public Room(int id, String name, double price, String bookDate, int bookUserid, int maxCapacity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.bookDate = bookDate;
        this.bookUserid = bookUserid;
        this.maxCapacity = maxCapacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public int getBookUserid() {
        return bookUserid;
    }

    public void setBookUserid(int bookUserid) {
        this.bookUserid = bookUserid;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", bookDate='" + bookDate + '\'' +
                ", bookUserid=" + bookUserid +
                ", maxCapacity=" + maxCapacity +
                ", status=" + (isAvailable()  ? "Available" : "Reserved"  ) +
                '}';
    }

    public static List<Room> getReservedRoomByUserId(int userId) {
        List<Room> result = new ArrayList<>();
        try {
            List<Room> roomList = DataBaseManager.getRoomDAO().selectAll();
            if (CollectionUtils.isEmpty(roomList)) {
                return result;
            }
            for (Room room : roomList) {
                if(room.isAvailable()) {
                    continue;
                }
                if (userId == room.getBookUserid()) {
                    result.add(room);
                }
            }
        } catch (Exception e) {
            return result;
        }

        return result;
    }

    /**
     * 检测房间是否可以被预定
     * @return
     */
    public boolean isAvailable() {

        try {
            // 检测数据库是否存在该房间
            if (DataBaseManager.getRoomDAO().select(id) == null) {
                return false;
            }

            // 检测预定的用户是否存在
            if (DataBaseManager.getCustomerDAO().select(id) == null) {
                return false;
            }

            // 如果预定的日期是今天， 那么也是 unavailable
            if (bookDate.equals(DateUtils.getDate(new Date()))) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public int compareTo(Room o) {
        return Integer.compare(id, o.getId());
    }
}
