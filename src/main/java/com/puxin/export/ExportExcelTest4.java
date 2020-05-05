package com.puxin.export;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.puxin.export.entry.MultiLineHeadExcelModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelTest4 {

    /**
     * 如果单行表头表头还不满足需求，
     * 没关系，还可以使用多行复杂的表头
     *
     * @throws IOException
     */

    public void writeWithHead() throws IOException {
        try (
            OutputStream out = new FileOutputStream("D:\\1111\\withoutHead2.xlsx")) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0, MultiLineHeadExcelModel.class);
            sheet1.setSheetName("sheet1");
            List<MultiLineHeadExcelModel> data = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
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
                data.add(item);
            }
            writer.write(data, sheet1);
            writer.finish();
        }
    }

    public static void main(String[] args) {
        ExportExcelTest4 excelWriteTest = new ExportExcelTest4();
        try {
            excelWriteTest.writeWithHead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}