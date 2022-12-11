package edu.xjtlu.cpt403.userinterface;

import edu.xjtlu.cpt403.database.DataBaseManager;
import edu.xjtlu.cpt403.database.SalesRecordDAO;
import edu.xjtlu.cpt403.entity.Room;
import edu.xjtlu.cpt403.entity.SalesRecord;
import edu.xjtlu.cpt403.util.DateUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesRecordUI {


    /**
     * 新增销售记录查看功能，默认管理员权限
     *  1. 展示销售记录统计表
     *  2. 统计表分为<当日><本周><本月>三种类型，管理员可以选择查看其中任意一种
     *  3. 统计表展示的数据：销售记录id，销售商品id，销售商品名称，商品销售数量，商品销售金额，商品销售时间
     *  4. 每个维度的统计表之前显示金额统计与顾客统计
     */

     public static void salesRecordView(int nDays) {
         List<SalesRecord> salesRecordList;
         try {
             salesRecordList = DataBaseManager.getSalesRecordDAO().selectAll();
         } catch (Exception e) {
             System.out.println("query sales records exception : " + e.getMessage());
             return;
         }

         // n天前的时间戳
         long nDaysBeforeTimeStamp = System.currentTimeMillis() - nDays*24*60*60*1000L;

         List<SalesRecord> statisticResult = new ArrayList<>();

         for (SalesRecord salesRecord : salesRecordList) {
             long payTimeStamp = DateUtils.parseDate(salesRecord.getPayTime(), DateUtils.DATE_FORMAT_DAY).getTime();
             //取出销售记录表里面payTime=当前日期的数据
             if (payTimeStamp >= nDaysBeforeTimeStamp) {
                 statisticResult.add(salesRecord);
             }
         }
         //统计指定时段内的收入和访客量
         double totalIncome = 0D;
         Set<Integer> userIdSet = new HashSet<>();
         for(int i = 0; i < statisticResult.size(); i ++) {
             totalIncome += statisticResult.get(i).getPayCost();
             userIdSet.add(statisticResult.get(i).getUserid());
         }
         System.out.println("=======================================================");
         System.out.println("Number of Vistors in the period:"+userIdSet.size()+"\n"+"Income in the period:"+totalIncome+" (RMB)");
         System.out.println("————————SalsRecord————————");

         if(statisticResult.size()==0){
             System.out.println("Sorry! There is no SalesRecord!");
         }else {
             for (int i = 0; i < statisticResult.size(); i++) {
                 System.out.println(statisticResult.get(i));
             }
         }

         System.out.println("=============================================================");
         System.out.println("The following is the SalesRecord:");
     }
}
