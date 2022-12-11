package edu.xjtlu.cpt403.userinterface;


import edu.xjtlu.cpt403.database.CustomerDAO;
import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.RoomDAO;
import edu.xjtlu.cpt403.entity.AdminUser;
import edu.xjtlu.cpt403.entity.Customer;
import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.entity.User;
import edu.xjtlu.cpt403.util.UserInterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RoomUI {

    /**
      Room reservation
     * 1. first display a list of all rooms
     * 2. ask the user to enter the room number to be booked
     * 3. check if the room can be booked, if someone else has booked it, or if the user has entered a wrong room number, it can't be booked
     * 4. check if the current user is an administrator or if the current user is logged in, if so use the current user id to book the room, otherwise prompt for the user id
     * If the user is logged in, the current user id is used to book the room.
     * 6. execute the room booking logic (update the booking time and booker ID for that room)
     *
     * Note: exceptions are handled here in a timely manner, e.g. a text message asking the user to retry

     Translated with www.DeepL.com/Translator (free version)
     */
    public static void newReservation() {
        System.out.println("=============================================================");
        System.out.println("Let's start reservation room");
        /** No access for non-logged-in users */
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return;
        }
        /** 1. Show all room lists first */
        List<Room> roomList = queryReservation(true, false);
        if (CollectionUtils.isEmpty(roomList)) {
            return;
        }

        /** 2. The user is asked to enter the number of the room to be reserved, which can be checked or not (the second parameter is passed directly as null) */
        String roomNumber = UserInterfaceUtils.getStringInput("Please enter the room number you want to reserve", new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                /** Check the parameter logic, e.g. return false if the input is not legal, or throw an exception */
                if (!StringUtils.isNumeric(s)) {
                    throw new IllegalArgumentException("The room number must be number");
                }
                return true;
            }
        });

        /** 3. check if the room can be booked, if someone else has booked it, or if the user has entered a wrong room number, it cannot be booked */
        Room room;
        try {
            /**  Get information about the room for later use */
            room = DataBaseManager.getRoomDAO().select(Integer.parseInt(roomNumber));
        } catch (Exception e) {
            /** Exception alerts */
            System.out.println("room number invalid: " + e.getMessage());
            System.out.println("Please try agagin!");
            /** Always add return, which means abandoning the attempt */
            return;
        }
        /** Check if it can be booked */
        if (room == null || (!room.isAvailable())) {
            System.out.println("room number is unavailable! Please try another room");
            return;
        }

        /** 4. check if the current user is an administrator or if the current user is logged in, if so use the current user id to book a room, otherwise prompt for the user id */
        User currentUser = UserInterfaceUtils.getCurrentUser();
        int userId;
        /** If the user is an administrator, you will be prompted to enter the name of the user who needs to book the room */
        if (currentUser instanceof AdminUser) {
            /** Let the user enter the name */
            String userName = UserInterfaceUtils.getStringInput("Please enter the user name you want to reserve", s -> User.validateName(s));
            /** The user ID entered also needs to be verified as legitimate, e.g. whether the user exists or not Find the user id by name */
            try {
                userId = DataBaseManager.getCustomerDAO().findByName(userName).getId();
            } catch (Exception e) {
                // 异常提示 Exception alerts
                System.out.println("username invalid: " + e.getMessage());
                System.out.println("Please try agagin!");
                // 一定要加返回， 意味着抛弃此次尝试，Be sure to add the return, which means abandoning the attempt.
                return;
            }
        } else {
            userId = currentUser.getId();
        }

        // 6. 执行预定房间的逻辑 （更新那个房间的预定时间和预定人ID） Execute the room booking logic (update the booking time and the booker ID for that room)
        room.setBookDate(LocalDate.now().toString());
        room.setBookUserid(userId);
        try {
            DataBaseManager.getRoomDAO().update(room.getId(), room);
        } catch (Exception e) {
            // 异常提示
            System.out.println("Room Reservation Failed: " + e.getMessage());
            System.out.println("Please try agagin!");
        } finally {
            System.out.println("Congratulations! Reservation success! Your reservation information is: " + room);
            System.out.println("=============================================================");
        }
    }


    public static List<Room> queryReservation(boolean displayAvailable, boolean displayReserved) {
        List<Room> showList = new ArrayList<>();

        System.out.println("=============================================================");
        System.out.println("Let's start queryReservation room");

        //  没有登录禁止查看 No login to prevent viewing
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return showList;
        }
        RoomDAO roomDAO = DataBaseManager.getRoomDAO();

        try {
            List<Room> roomList = roomDAO.selectAll();
            if (CollectionUtils.isEmpty(roomList)) {
                System.out.println("The room is empty! ");
                return showList;
            }
            // 排序
            Collections.sort(roomList);
            // 显示

            for (Room room : roomList) {
                // 可预定房间单独展示 Rooms can be booked for individual display
                if (room.isAvailable() && displayAvailable) {
                    showList.add(room);
                    continue;
                }

                if (!displayReserved) {
                    continue;
                }

                //  普通用户不能查看别人定了的房间，Regular users cannot view the rooms booked by others.
                if ((UserInterfaceUtils.getCurrentUser() instanceof Customer)
                        && (room.getBookUserid() != UserInterfaceUtils.getCurrentUser().getId())){
                    continue;
                }

                showList.add(room);
            }

            if (CollectionUtils.isEmpty(showList)) {
                System.out.println("No room information was found!");
            } else {
                for (Room room : showList) {
                    System.out.println(room);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("queryReservation room end");
            System.out.println("=============================================================");
        }

        return showList;
    }

    /**
     * 1. Show all room listings first
     * 2. the administrator can cancel all room bookings directly
     * 3. users can only cancel their own bookings
     */
    public static void cancelReservation() {
        System.out.println("=============================================================");
        System.out.println("Let's start cancelReservation room");
        //  No login to prevent viewing
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return;
        }
        // 1. Show first the list of all rooms booked by this user
        List<Room> reservedRoom = Room.getReservedRoomByUserId(UserInterfaceUtils.getCurrentUser().getId());
        if (CollectionUtils.isEmpty(reservedRoom)) {
            System.out.println("You have not reseved any room, so can not cancel reservation!");
            return;
        }

        System.out.println("Here is your reservation !");
        for (Room room : reservedRoom) {
            System.out.println(room);
        }

        // 2. 让用户输入需要取消预定的房间号,可以对用户的输入进行校验， 也可以不校验（第二个参数直接传入null）
        //2. let the user enter the number of the room to be cancelled, with or without checking the user's input (the second parameter can be passed as null)
        String roomNumber = UserInterfaceUtils.getStringInput("Please enter the room number you want to cancel", new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                // 校验参数逻辑， 比如输入不合法就返回false,或者抛异常
                // Check the parameter logic, e.g. return false if the input is not legal, or throw an exception
                if (!StringUtils.isNumeric(s)) {
                    throw new IllegalArgumentException("The room number must be number");
                }
                return true;
            }
        });

        // 3. 检测房间输入的房间号是否合法
        // 3. check if the room number entered in the room is legal
        Room room;
        try {
            // 获取到room的信息， 便于之后使用
            // Get information about the room for later use
            room = DataBaseManager.getRoomDAO().select(Integer.parseInt(roomNumber));
        } catch (Exception e) {
            // 异常提示 Exception alerts
            System.out.println("room number invalid: " + e.getMessage());
            System.out.println("Please try agagin!");
            // 一定要加返回， 意味着抛弃此次尝试
            // Be sure to add the return, which means abandoning the attempt.
            return;
        }

        // 4. 检测当前用户是不是管理员，
        // 4. to detect if the current user is an administrator.
        User currentUser = UserInterfaceUtils.getCurrentUser();

        // 用户登录了， 但不是管理员， 而且这个房间也不是他定的
        // The user is logged in, but not the administrator, and the room is not booked by him
        if ((currentUser instanceof Customer) && currentUser.getId() != room.getBookUserid()){
            System.out.println("this room is booked by other user, so can't support cancel reservation!");
            return;
        }

        // 5. 取消预定， 直接把预定用户id更新为0就行
        // 5. To cancel a booking, simply update the booking user id to 0
        room.setBookUserid(0);
        try {
            DataBaseManager.getRoomDAO().update(room.getId(), room);
        } catch (Exception e) {
            // 异常提示    Exception alerts
            System.out.println(room + "cancel reservation failed: " + e.getMessage());
            System.out.println("Please try again!");
        } finally {
            System.out.println("newReservation room end, result: " + room.toString());
            System.out.println("=============================================================");
        }

    }


}
