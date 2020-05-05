package com.puxin.export;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.puxin.export.entry.MultiLineHeadExcelModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelTest5 {

    /**
     *怎么样，这些已经基本满足我们的日常需求了，
     * easyexcel不仅支持上述几种形式，还支持在一个sheet中添加多个表
     *
     * @throws IOException
     */

    public void writeWithMultiTable() throws IOException {
        try (OutputStream out = new FileOutputStream("D:\\1111\\withoutHead2.xlsx");) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("sheet1");

            // 数据全是List<String> 无模型映射关系
            Table table1 = new Table(1);
            List<List<String>> data1 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<String> item = new ArrayList<>();
                item.add("item0" + i);
                item.add("item1" + i);
                item.add("item2" + i);
                data1.add(item);
            }
            writer.write0(data1, sheet1, table1);

            // 模型上有表头的注解
            Table table2 = new Table(2);
            table2.setClazz(MultiLineHeadExcelModel.class);
            List<MultiLineHeadExcelModel> data2 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                MultiLineHeadExcelModel item = new MultiLineHeadExcelModel();
                item.setP1("p1" + i);
                item.setP2("p2" + i);
                item.setP3("p3" + i);
                item.setP4("p4" + i);
                item.setP5("p5" + i);
                item.setP6("p6" + i);
                item.setP7("p7" + i);
                item.setP8("p8" + i);
                item.setP9("p9" + i);
                data2.add(item);
            }
            writer.write(data2, sheet1, table2);

            // 模型上没有注解，表头数据动态传入,此情况下模型field顺序与excel现实顺序一致
            List<List<String>> head = new ArrayList<List<String>>();
            List<String> headCoulumn1 = new ArrayList<String>();
            List<String> headCoulumn2 = new ArrayList<String>();
            List<String> headCoulumn3 = new ArrayList<String>();
            headCoulumn1.add("第一列");
            headCoulumn2.add("第二列");
            headCoulumn3.add("第三列");
            head.add(headCoulumn1);
            head.add(headCoulumn2);
            head.add(headCoulumn3);
            Table table3 = new Table(3);
            table3.setHead(head);
            writer.write0(data1, sheet1, table3);

            writer.finish();
        }
    }

    public static void main(String[] args) {
        ExportExcelTest5 excelWriteTest = new ExportExcelTest5();
        try {
            excelWriteTest.writeWithMultiTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}