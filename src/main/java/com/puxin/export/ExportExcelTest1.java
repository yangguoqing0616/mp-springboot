package com.puxin.export;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelTest1 {

    /**
     * 每行数据是List<String>无表头
     * 合并单元格 writer.merge();
     *
     * @throws IOException
     */

    public void writeWithoutHead() throws IOException {
        try (
            OutputStream out = new FileOutputStream("D:\\1111\\withoutHead.xlsx")) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, false);
            Sheet sheet1 = new Sheet(1, 0);
            sheet1.setSheetName("测试sheet1");
            List<List<String>> data = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                List<String> item = new ArrayList<>();
                item.add("item0" + i);
                item.add("item1" + i);
                item.add("item2" + i);
                data.add(item);
            }
            writer.write0(data, sheet1);
            writer.finish();
        }
    }

    public static void main(String[] args) {
        ExportExcelTest1 excelWriteTest = new ExportExcelTest1();
        try {
            excelWriteTest.writeWithoutHead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
