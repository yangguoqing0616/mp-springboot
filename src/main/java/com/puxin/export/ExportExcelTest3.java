package com.puxin.export;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.puxin.export.entry.ExcelPropertyIndexModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportExcelTest3 {

    /**
     * 除了上面添加表头的方式，我们还可以使用实体类，
     * 为其添加com.alibaba.excel.annotation.ExcelProperty注解来生成表头，
     * 实体类数据作为Excel数据
     *
     * @throws IOException
     */

    public void writeWithHead() throws IOException {
        try (OutputStream out = new FileOutputStream("D:\\1111\\withoutHead.xlsx")) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0, ExcelPropertyIndexModel.class);
            sheet1.setSheetName("sheet12112");
            List<ExcelPropertyIndexModel> data = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                ExcelPropertyIndexModel item = new ExcelPropertyIndexModel();
                item.setName("name" + i);
                item.setAge("age" + i);
                item.setEmail("email" + i);
                item.setAddress("address" + i);
                item.setSax("sax" + i);
                item.setHeigh("heigh" + i);
                item.setLast("last" + i);
                data.add(item);
            }
            writer.write(data, sheet1);
            writer.finish();
        }
    }

    public static void main(String[] args) {
        ExportExcelTest3 excelWriteTest = new ExportExcelTest3();
        try {
            excelWriteTest.writeWithHead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
