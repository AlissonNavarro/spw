/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compactador {


    public static File compactar(String fileToZip, String place) {
        File fileCompactaded = null;
        File zipFileToZip_ = new File(fileToZip);  
        String zipFileName  = zipFileToZip_.getParent().concat("\\backup_").concat(place+"_").concat(dateToString()).concat(".zip");
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            // Ajusta modo de compressão
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            FileInputStream in = new FileInputStream(fileToZip);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(zipFileToZip_.getName()));
            // Transfer bytes from the current file to the ZIP file            
            int len;
            byte[] buffer = new byte[18024];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            // Close the current entry
            out.closeEntry();
            // Close the current file input stream
            in.close();
            // Close the ZipOutPutStream
            out.close();

            fileCompactaded = new File(zipFileName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return fileCompactaded;
    }

    private static String dateToString() {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("ddMMyyHHmmss");
        String saida = sdfSemana.format(new Date().getTime());
        return saida;
    }
}
