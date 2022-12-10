package edu.xjtlu.cpt403.util;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description: Excel读写工具类
 * @Author: haichao.zhang22
 * @CreateDate: 2022/11/24 19:21
 */
public class ExcelUtils {

    public static void insert(RowData pd, String path, String sheets) throws IOException {
        XSSFWorkbook workbook = getExcelByPath(path);
        XSSFSheet sheet = workbook.getSheet(sheets);
        XSSFRow row = sheet.getRow(0);
        // 记录表头的序号
        Map<Integer, String> indexToField = new HashMap<>();
        for (int j = 0; j <row.getLastCellNum(); j++) {
            // record table head
            indexToField.put(j, row.getCell(j).getStringCellValue());
        }
        int cellNum = row.getLastCellNum();// 这个就是列数

        // 创建新的一行
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < cellNum; i++) {
            String field = indexToField.get(i);
            row.createCell(i).setCellValue(pd.getOrDefault(field,"null"));
        }
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();
    }


    public static void main(String[] args) throws Exception {
        String path = "src/main/resources/Database.xlsx";
        String sheetName = "AdminUser";
        List<RowData> list = readAll( path, sheetName);
        System.out.println(JSON.toJSONString(list));
        System.out.println(list.get(0).getRowIndex());
        System.out.println(JSON.toJSONString(list.get(0).getColNameToColIndex()));
        update(1,0, "1", path,sheetName);
    }

    public static void delete(int rowIndex, String path, String sheets) throws IOException {
        XSSFWorkbook workbook = getExcelByPath(path);
        XSSFSheet sheet = workbook.getSheet(sheets);
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum)
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);// 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
        if (rowIndex == lastRowNum) {
            XSSFRow removingRow = sheet.getRow(rowIndex);
            if (removingRow != null)
                sheet.removeRow(removingRow);
        }
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();
    }

    /**
     * @param rowNum 行数
     * @param colNum 列数
     * @param value
     * @param path
     * @param sheets
     * @throws IOException
     */
    public static void update(int rowNum, int colNum, String value, String path, String sheets) throws IOException {
        XSSFWorkbook workbook = getExcelByPath(path);
        XSSFSheet sheet = workbook.getSheet(sheets);
        XSSFRow row = sheet.getRow(rowNum);
        XSSFCell cell = row.getCell(colNum);
        if (cell == null) {
            row.createCell(colNum).setCellValue(value);
        } else {
            row.getCell(colNum).setCellValue(value);
        }
        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();
    }
    /**
     * 获取列数
     *
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static int getColNum(String path ,String sheetName) throws IOException {
        FileInputStream inp = new FileInputStream(path);
        XSSFWorkbook wb = new XSSFWorkbook(inp);
        XSSFSheet sheet = wb.getSheet(sheetName);
        //获得总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        return coloumNum;
    }

    public static List<RowData> readAll(String path, String sheets) {
        List<RowData> rs = new ArrayList<>();
        XSSFWorkbook workbook = getExcelByPath(path);
        XSSFSheet sheet = workbook.getSheet(sheets);

        Map<Integer, String> indexToName = new HashMap<>();
        Map<String, Integer> nameToIndex = new HashMap<>();

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            // init a blank row
            RowData data = new RowData();
            for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {
                // record table head
                if (i == 0) {
                    indexToName.put(j, sheet.getRow(i).getCell(j).getStringCellValue());
                    nameToIndex.put(sheet.getRow(i).getCell(j).getStringCellValue(), j);
                } else {
                    data.put(indexToName.getOrDefault(j, "UNKNOWN_FIELD"),  formatCell(sheet.getRow(i).getCell(j)));
                }
            }
            if (i != 0) {
                data.setRowIndex(sheet.getRow(i).getRowNum());
                data.setColNameToColIndex(nameToIndex);
                rs.add(data);
            }
        }

        return rs;
    }


    /**
     * 通过文件路劲获取excel文件
     *
     * @param path
     * @return
     */
    public static XSSFWorkbook getExcelByPath(String path) {
        try {

            byte[] buf = IOUtils.toByteArray(new FileInputStream(path));//execelIS为InputStream流
            //在需要用到InputStream的地方再封装成InputStream
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
            XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);
            return workbook;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过文件获取excel对象
     *
     * @param file
     * @return
     */
    public HSSFWorkbook getExcelByFile(File file) {
        try {
            POIFSFileSystem fspoi = new POIFSFileSystem(new FileInputStream(file.getPath()));
            HSSFWorkbook workbook = new HSSFWorkbook(fspoi);
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fileChooser() throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "csv");
        // 设置文件类型
        chooser.setFileFilter(filter);
        // 打开选择器面板
        int returnVal = chooser.showSaveDialog(new JPanel());
        // 保存文件从这里入手，输出的是文件名
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
        }
    }

    public static String formatCell(Cell cell) {
        String ret;
        switch (cell.getCellType()) {
            case STRING:
                ret = cell.getStringCellValue();
                break;
            case FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = formatCell(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    ret = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    ret = sdf.format(date);
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case BLANK:
                ret = "";
                break;
            case BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                ret = null;
                break;
            default:
                ret = null;
        }
        return ret; // 有必要自行trim
    }
}