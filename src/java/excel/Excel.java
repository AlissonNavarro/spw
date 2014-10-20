/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import Metodos.Metodos;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author amsgama
 */
public class Excel {

    private static CellStyle style;
    private static CellStyle estiloCabecalho;
    private static CellStyle estiloComum;


    private static void startStyle(Workbook wb) {
        Font font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // Fonts are set into a style so create a new one to use.
        style = wb.createCellStyle();
        style.setWrapText(true);
        estiloCabecalho = wb.createCellStyle();
        estiloComum = wb.createCellStyle();
        
        style.setFont(font);
    }

    private static void startStyleCabecalho(Workbook wb) {
        Font font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // Fonts are set into a style so create a new one to use.
        estiloCabecalho = wb.createCellStyle();
        estiloCabecalho.setWrapText(true);
        estiloCabecalho.setBorderTop(CellStyle.BORDER_MEDIUM);
        estiloCabecalho.setBorderBottom(CellStyle.BORDER_MEDIUM);
        estiloCabecalho.setBorderLeft(CellStyle.BORDER_MEDIUM);
        estiloCabecalho.setBorderRight(CellStyle.BORDER_MEDIUM);
        estiloCabecalho.setFont(font);
        estiloCabecalho.setAlignment(CellStyle.ALIGN_CENTER);
    }
    private static void startStyleComum(Workbook wb) {
        // Fonts are set into a style so create a new one to use.
        estiloComum = wb.createCellStyle();
        estiloComum.setWrapText(true);
        estiloComum.setBorderTop((short) 1);
        estiloComum.setBorderBottom((short) 1);
        estiloComum.setBorderLeft((short) 1);
        estiloComum.setBorderRight((short) 1);
    //    estiloComum.setAlignment(CellStyle.ALIGN_CENTER);
        estiloComum.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    }


    private static void autoSizeSheet(Sheet s, Integer num_colunas){
        for (int i = 0; i < num_colunas; i++) {
            s.autoSizeColumn((short) i);

        }
    }

    private static void setColumnWidth(Sheet s, List<String> cabecalho, List<Linha> linhaList) {

        Map<Integer, Integer> columnWidthMap = new HashMap<Integer, Integer>();
        int i = 0;

        for (Iterator<String> it = cabecalho.iterator(); it.hasNext();) {
            String cabecalho_ = it.next();
            columnWidthMap.put(i, cabecalho_.length());
            i++;
        }

        for (Iterator<Linha> it = linhaList.iterator(); it.hasNext();) {
            Linha linha = it.next();
            int j = 0;
            for (Iterator<String> it1 = linha.getConteudoCelulasList().iterator(); it1.hasNext();) {
                String coluna = it1.next();
                if (coluna != null && columnWidthMap.get(j) < coluna.length()) {
                    columnWidthMap.put(j, coluna.length());
                }
                j++;
            }
        }

        for (Iterator<Integer> it = columnWidthMap.keySet().iterator(); it.hasNext();) {
            Integer coluna = it.next();
            s.setColumnWidth(coluna, (columnWidthMap.get(coluna)+5) * 256);
        }
    }

    public static void gerarPlanilha(String fileName, List<String> cabecalho, List<Linha> linhaList) {
        try {
            Workbook wb = new HSSFWorkbook();
            CreationHelper ch = wb.getCreationHelper();
            //Planilha
            Sheet s = wb.createSheet("planilha 1");
            startStyle(wb);
            startStyleCabecalho(wb);
            startStyleComum(wb);
//            setColumnWidth(s, cabecalho, linhaList);

            peencherLinhaCabecalho(cabecalho, s, ch);
            for (int i = 0; i < linhaList.size(); i++) {
                Linha linha = linhaList.get(i);
                peencherLinha(linha.getConteudoCelulasList(), i + 1, s, ch);
            }
            autoSizeSheet(s, cabecalho.size());
            gravar(Metodos.getPath() + fileName, wb);
        } catch (FileNotFoundException ex) {
            System.out.println("excel: gerarPlanilha 1: "+ex);
            //Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("excel: gerarPlanilha 1: "+ex);
            //Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void gerarPlanilhaDownload(String fileName, List<String> cabecalho, List<Linha> linhaList) {
        try {
            Workbook wb = new HSSFWorkbook();
            CreationHelper ch = wb.getCreationHelper();
            //Planilha
            Sheet s = wb.createSheet("planilha 1");
            startStyle(wb);
         //   setColumnWidth(s, cabecalho, linhaList);

            peencherLinhaCabecalho(cabecalho, s, ch);
            for (int i = 0; i < linhaList.size(); i++) {
                Linha linha = linhaList.get(i);
                peencherLinha(linha.getConteudoCelulasList(), i + 1, s, ch);
            }
            autoSizeSheet(s, cabecalho.size());
            gravar(Metodos.getPath() + fileName, wb);
            Metodos.download(fileName);
        } catch (Exception ex) {
            System.out.println("excel: gerarPlanilhaDownload: "+ex);
            //Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void gerarPlanilhaDownload(String fileName, List<String> cabecalho, List<Linha> linhaList, List<MergeRegion> MergeRegionList) {
        try {

        // Fonts are set into a style so create a new one to use.
        
            Workbook wb = new HSSFWorkbook();
            CreationHelper ch = wb.getCreationHelper();

            //Planilha
            Sheet s = wb.createSheet("planilha 1");
            startStyle(wb);
            startStyleCabecalho(wb);
            startStyleComum(wb);
            peencherLinhaCabecalho(cabecalho, s, ch,estiloCabecalho);
            for (int i = 0; i < linhaList.size(); i++) {
                Linha linha = linhaList.get(i);
                peencherLinha(linha.getConteudoCelulasList(), i + 1, s, ch,estiloComum);
            }

            for (int i = 0; i < MergeRegionList.size(); i++) {
                MergeRegion mergeRegion = MergeRegionList.get(i);
                s.addMergedRegion(new CellRangeAddress(mergeRegion.getFirstRow(), mergeRegion.getLastRow(), mergeRegion.getFirstColunm(), mergeRegion.getLastColumn()));
            }
            autoSizeSheet(s, cabecalho.size());
            gravar(Metodos.getPath() + fileName, wb);
            Metodos.download(fileName);
        } catch (Exception ex) {
            System.out.println("excel: gerarPlanilhaDownload: "+ex);
            //Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void peencherLinha(List<String> linha, Integer posicao, Sheet s, CreationHelper ch, CellStyle estilo) {
        Row r = s.createRow(posicao);
        for (int i = 0; i < linha.size(); i++) {
            String celula = linha.get(i);
            Cell c = r.createCell(i);
            c.setCellValue(ch.createRichTextString(celula));
            c.setCellStyle(estilo);
        }
    }

    private static void peencherLinha(List<String> linha, Integer posicao, Sheet s, CreationHelper ch) {
        Row r = s.createRow(posicao);
        for (int i = 0; i < linha.size(); i++) {
            String celula = linha.get(i);
            Cell c = r.createCell(i);
            c.setCellValue(ch.createRichTextString(celula));
            
        }
    }

    private static void peencherLinhaCabecalho(List<String> linha, Sheet s, CreationHelper ch) {
        Row r = s.createRow(0);
        for (int i = 0; i < linha.size(); i++) {
            String celula = linha.get(i);
            Cell c = r.createCell(i);
            c.setCellValue(ch.createRichTextString(celula));
            c.setCellStyle(style);
        }
    }

    private static void peencherLinhaCabecalho(List<String> linha, Sheet s, CreationHelper ch, CellStyle estilo) {
        Row r = s.createRow(0);
        for (int i = 0; i < linha.size(); i++) {
            String celula = linha.get(i);
            Cell c = r.createCell(i);
            c.setCellValue(ch.createRichTextString(celula));
            c.setCellStyle(estilo);
        }
    }

    private static void gravar(String pathAndFileName, Workbook wb) throws FileNotFoundException, IOException {
        String filename = pathAndFileName;
        FileOutputStream out = out = new FileOutputStream(filename);
        wb.write(out);
    }

    public static void main(String[] args) {

        Linha c1 = new Linha();

        List<String> cabecalho = new ArrayList<String>();
        cabecalho.add("coluna1");
        cabecalho.add("coluna2");
        cabecalho.add("coluna3");

        List<String> c1List = new ArrayList<String>();
        c1List.add("11");
        c1List.add("12");
        c1List.add("13");
        c1.setConteudoCelulasList(c1List);

        Linha c2 = new Linha();

        List<String> c2List = new ArrayList<String>();
        c2List.add("*21");
        c2List.add("*22");
        c2List.add("*23");
        c2.setConteudoCelulasList(c2List);

        List<Linha> linhasList = new ArrayList<Linha>();
        linhasList.add(c1);
        linhasList.add(c2);

        Excel.gerarPlanilha("c:\\testandoFinal.xls", cabecalho, linhasList);
    }
}
