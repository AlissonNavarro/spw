/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

//teste3
public class Main {

    public static void main(String[] args) throws IOException {
        // import org.apache.poi.ss.usermodel.*;

        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();

        // create a new sheet
        Sheet s = wb.createSheet("planilha 1");
        s.setColumnWidth(1,19*256);

        Row r = s.createRow(0);
        r = s.createRow(1);
        r = s.createRow(2);
        r = s.createRow(3);        

        Cell c;

        c = s.getRow(0).createCell(0);
        c.setCellValue(createHelper.createRichTextString("Teste1"));

        c = s.getRow(1).createCell(0);
        c.setCellValue(createHelper.createRichTextString("11"));
        c = s.getRow(2).createCell(0);
        c.setCellValue(createHelper.createRichTextString("12"));
        c = s.getRow(3).createCell(0);
        c.setCellValue(createHelper.createRichTextString("13"));
        
        
        c = s.getRow(0).createCell(1);
        c.setCellValue(createHelper.createRichTextString("Teste2"));
        c = s.getRow(1).createCell(1);
        c.setCellValue(createHelper.createRichTextString("21"));
        c = s.getRow(2).createCell(1);
        c.setCellValue(createHelper.createRichTextString("22"));
        c = s.getRow(3).createCell(1);
        c.setCellValue(createHelper.createRichTextString("23"));        

        // Save
        String filename = "C:\\testeee.xls";
        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);

        out.close();
    }
}
