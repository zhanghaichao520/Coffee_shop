package edu.xjtlu.cpt403.entity;

import edu.xjtlu.cpt403.database.RoomDAO;
import edu.xjtlu.cpt403.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

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
                ", status=" + (bookUserid != 0 && bookDate .equals(DateUtils.getDate(new Date()))  ? "Reserved" : "Available" ) +
                '}';
    }


    /**
     * 检测房间是否可以被预定
     * @param id
     * @return
     */
    public static boolean checkReservation(int id, RoomDAO roomDAO) throws Exception {
        // todo: 检测数据库是否存在
//        if (roomDAO.select(id) == null) {
//            throw new IllegalArgumentException("room not exist");
//        }
        if (id == 0) {
            throw new IllegalArgumentException("room not exist");
        }

        // todo:  检测数据库是否被别人定了

        return true;
    }

    @Override
    public int compareTo(Room o) {
        return Integer.compare(id, o.getId());
    }
}
