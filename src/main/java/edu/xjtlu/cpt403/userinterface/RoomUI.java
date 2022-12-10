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
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RoomUI {

    /**
     * 新增房间预定
     *  1. 先展示所有的房间列表
     *  2. 让用户输入需要预定的房间号
     *  3. 检测房间是否可以被预定， 如果被别人定了， 或者用户输入的房间号有问题，都不能预定
     *  4. 检测当前用户是不是管理员，或者当前用户有没有登录， 如果登陆了就使用当前的用户id预定房间， 否则提示输入用户id
     *  5. 输入的用户ID也是需要验证合法性的， 比如用户是否存在
     *  6. 执行预定房间的逻辑 （更新那个房间的预定时间和预定人ID）
     *
     * 注意： 有异常及时在这里处理掉，比如文字提示，让用户重试
     */
    public static void newReservation() {
        System.out.println("=============================================================");
        System.out.println("Let's start newReservation room");
        //  没有登录禁止查看
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return;
        }
        // 1. 先展示所有的房间列表
        queryReservation();

        // 2. 让用户输入需要预定的房间号,可以对用户的输入进行校验， 也可以不校验（第二个参数直接传入null）
        String roomNumber = UserInterfaceUtils.getStringInput("Please enter the room number you want to reserve", new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                // 校验参数逻辑， 比如输入不合法就返回false,或者抛异常
                if (!StringUtils.isNumeric(s)) {
                    throw new IllegalArgumentException("The room number must be number");
                }
                return true;
            }
        });

        // 3. 检测房间是否可以被预定， 如果被别人定了， 或者用户输入的房间号有问题，都不能预定
        Room room;
        try {
            //  获取到room的信息， 便于之后使用
            room = DataBaseManager.getRoomDAO().select(Integer.parseInt(roomNumber));
        } catch (Exception e) {
            // 异常提示
            System.out.println("room number invalid: " + e.getMessage());
            System.out.println("Please try agagin!");
            // 一定要加返回， 意味着抛弃此次尝试，
            return;
        }
        // 检测是否可被预定
        if (room == null || (!room.isAvailable())) {
            System.out.println("room number is unavailable! Please try another room");
            return;
        }

        // 4. 检测当前用户是不是管理员，或者当前用户有没有登录， 如果登陆了就使用当前的用户id预定房间， 否则提示输入用户id
        User currentUser = UserInterfaceUtils.getCurrentUser();
        int userId;
        // 用户是管理员， 则需要提示输入需要预定房间的用户名字
        if (currentUser instanceof AdminUser) {
            // 让用户输入 名字
            String userName = UserInterfaceUtils.getStringInput("Please enter the user name you want to reserve", s -> User.validateName(s));
            // 输入的用户ID也是需要验证合法性的， 比如用户是否存在     根据名字找到用户id
            try {
                userId = DataBaseManager.getCustomerDAO().findByName(userName).getId();
            } catch (Exception e) {
                // 异常提示
                System.out.println("username invalid: " + e.getMessage());
                System.out.println("Please try agagin!");
                // 一定要加返回， 意味着抛弃此次尝试，
                return;
            }
        } else {
            userId = currentUser.getId();
        }

        // 6. 执行预定房间的逻辑 （更新那个房间的预定时间和预定人ID）
        room.setBookDate(LocalDate.now().toString());
        room.setBookUserid(userId);
        try {
            DataBaseManager.getRoomDAO().update(room.getId(), room);
        } catch (Exception e) {
            // 异常提示
            System.out.println("newReservation failed: " + e.getMessage());
            System.out.println("Please try agagin!");
        } finally {
            System.out.println("newReservation room end, result: " + room.toString());
            System.out.println("=============================================================");
        }
    }


    public static void queryReservation() {
        System.out.println("=============================================================");
        System.out.println("Let's start queryReservation room");

        //  没有登录禁止查看
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return;
        }
        RoomDAO roomDAO = DataBaseManager.getRoomDAO();

        try {
            List<Room> roomList = roomDAO.selectAll();
            if (CollectionUtils.isEmpty(roomList)) {
                System.out.println("The room is empty! ");
                return;
            }
            // 排序
            Collections.sort(roomList);
            // 显示

            for (Room room : roomList) {
                // 可预定房间单独展示
                if (room.isAvailable()) {
                    System.out.println(room);
                    continue;
                }
                //  普通用户不能查看别人定了的房间，
                if ((UserInterfaceUtils.getCurrentUser() instanceof Customer)
                        && (room.getBookUserid() != UserInterfaceUtils.getCurrentUser().getId())){
                    continue;
                }

                System.out.println(room);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("queryReservation room end");
            System.out.println("=============================================================");
        }

    }

    /**
     * 1. 先展示所有的房间列表
     * 2. 管理员可以直接取消所有房间的预定
     * 3. 用户只可以取消自己的预定
     */
    public static void cancelReservation() {
        System.out.println("=============================================================");
        System.out.println("Let's start cancelReservation room");
        //  没有登录禁止查看
        if (UserInterfaceUtils.getCurrentUser() == null) {
            System.out.println("Please Login first!");
            return;
        }
        // 1. 先展示所有的被此用户预定的房间列表
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
        String roomNumber = UserInterfaceUtils.getStringInput("Please enter the room number you want to cancel", new Function<String, Boolean>() {
            @Override
            public Boolean apply(String s) {
                // 校验参数逻辑， 比如输入不合法就返回false,或者抛异常
                if (!StringUtils.isNumeric(s)) {
                    throw new IllegalArgumentException("The room number must be number");
                }
                return true;
            }
        });

        // 3. 检测房间输入的房间号是否合法
        Room room;
        try {
            // 获取到room的信息， 便于之后使用
            room = DataBaseManager.getRoomDAO().select(Integer.parseInt(roomNumber));
        } catch (Exception e) {
            // 异常提示
            System.out.println("room number invalid: " + e.getMessage());
            System.out.println("Please try agagin!");
            // 一定要加返回， 意味着抛弃此次尝试，
            return;
        }

        // 4. 检测当前用户是不是管理员，
        User currentUser = UserInterfaceUtils.getCurrentUser();

        // 用户登录了， 但不是管理员， 而且这个房间也不是他定的。
        if ((currentUser instanceof Customer) && currentUser.getId() != room.getBookUserid()){
            System.out.println("this room is booked by other user, so can't support cancel reservation!");
            return;
        }

        // 5. 取消预定， 直接把预定用户id更新为0就行
        room.setBookUserid(0);
        try {
            DataBaseManager.getRoomDAO().update(room.getId(), room);
        } catch (Exception e) {
            // 异常提示
            System.out.println("newReservation failed: " + e.getMessage());
            System.out.println("Please try again!");
        } finally {
            System.out.println("newReservation room end, result: " + room.toString());
            System.out.println("=============================================================");
        }

    }


}
