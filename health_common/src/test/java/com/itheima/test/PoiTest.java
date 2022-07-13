package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PoiTest {

//    @Test
//    public void test1() throws Exception {
//        //读取文件
//        XSSFWorkbook  excel = new XSSFWorkbook(new FileInputStream(new File("d:\\poi.xlsx")));
//        //获取第一个sheet页
//        XSSFSheet sheet = excel.getSheetAt(0);
////        XSSFRow row1 = sheet.getRow(2);
//        //遍历sheet页
//        for (Row row : sheet) {
//            for (Cell cell : row) {
//                String s = String.valueOf(cell);
//                System.out.print(s + "\t");
//            }
//            System.out.println();
//        }
//
//        excel.close();
//    }
}
