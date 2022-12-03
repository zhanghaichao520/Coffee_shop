package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.SalesRecordDAO;

import java.sql.SQLOutput;

public class SalesRecordUI {

    /**
     * 新增销售记录查看功能，默认管理员权限
     *  1. 展示销售记录统计表
     *  2. 统计表按照<当日><本周><本月>按顺序展示三张表
     *  3. 统计表展示的数据：销售记录id，销售商品id，销售商品名称，商品销售数量，商品销售金额，商品销售时间
     *  4. 每个维度的统计表最后一行展示 销售金额的统计
     * 注意：
     */

     public static void SalesRecordView() {
         System.out.println("=============================================================");
         System.out.println("The following is the SalesRecord:");

         queryCurrentDay();


         queryCurrentWeek();


         queryCurrentMonth();



     }

    public static void queryCurrentDay() {
        System.out.println("CurrentDay:");
        SalesRecordDAO salesRecordDAO = DataBaseManager.getSalesRecordDAO();
    }

    public static void queryCurrentWeek() {
        System.out.println("CurrentWeek:");
    }

    public static void queryCurrentMonth() {
        System.out.println("CurrentMonth:");
    }


}
