/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Metodos.Metodos;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class MergePDF {

    public static void main(String[] args) {
        try {
            List<InputStream> pdfs = new ArrayList<InputStream>();

            pdfs.add(new FileInputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_1.pdf"));
            pdfs.add(new FileInputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_2.pdf"));
            pdfs.add(new FileInputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_3.pdf"));
            pdfs.add(new FileInputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_4.pdf"));
            pdfs.add(new FileInputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_5.pdf"));

            OutputStream output = new FileOutputStream("C:/Documents and Settings/amsgama/Meus documentos/Downloads/RelatorioMensal_jan_huse.pdf");
            MergePDF.concatPDFs2(pdfs, output, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void gerarPDFUnico(List<Integer> matriculas, String relatorio) throws FileNotFoundException {
        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (Iterator<Integer> it = matriculas.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            pdfs.add(new FileInputStream(Metodos.getPath() + "temp/" + matricula + ".pdf"));
        }
        OutputStream output = new FileOutputStream(Metodos.getPath() + relatorio + ".pdf");
        MergePDF.concatPDFs(pdfs, output, true);
    }

    public static void gerarPDFUnicoHorizontal(List<Integer> matriculas, String relatorio) throws FileNotFoundException {
        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (Iterator<Integer> it = matriculas.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            pdfs.add(new FileInputStream(Metodos.getPath() + "temp/" + matricula + ".pdf"));
        }
        OutputStream output = new FileOutputStream(Metodos.getPath() + relatorio + ".pdf");
        MergePDF.concatPDFsHorizontal(pdfs, output, true);
    }

    public static void gerarPDFUnicoHorizontalComListaDeFuncinarioSemEscala(List<Integer> matriculas,
            String relatorio, Boolean hasFuncinarioSemEscala) throws FileNotFoundException, IOException {
        
        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (Iterator<Integer> it = matriculas.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            pdfs.add(new FileInputStream(Metodos.getPath() + "temp/" + matricula + ".pdf"));
        }
        if (hasFuncinarioSemEscala) {
            pdfs.add(new FileInputStream(Metodos.getPath() + "temp/listaFuncionarioSemEscala.pdf"));
        }
        OutputStream output = new FileOutputStream(Metodos.getPath() + relatorio + ".pdf");
        MergePDF.concatPDFsHorizontal(pdfs, output, true);
        output.flush();
        output.close();
    }

    public static void gerarPDFUnicoHorizontalComListaDeFuncinarioSemRegistro(List<Integer> matriculas,
            String relatorio, Boolean hasFuncinarioSemEscala) throws FileNotFoundException, IOException {
        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (Iterator<Integer> it = matriculas.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            pdfs.add(new FileInputStream(Metodos.getPath() + "tempSE/" + matricula + ".pdf"));
        }
        if (hasFuncinarioSemEscala) {
            pdfs.add(new FileInputStream(Metodos.getPath() + "temp/RelatorioFuncionarioSemRegistro.pdf"));
        }
        OutputStream output = new FileOutputStream(Metodos.getPath() + relatorio + ".pdf");
        MergePDF.concatPDFs(pdfs, output, true);
        output.flush();
        output.close();
    }

    public static void gerarPDFUnicoDepartamento(List<Integer> matriculas) throws FileNotFoundException, IOException {
        List<InputStream> pdfs = new ArrayList<InputStream>();
        for (Iterator<Integer> it = matriculas.iterator(); it.hasNext();) {
            Integer matricula = it.next();
            pdfs.add(new FileInputStream(Metodos.getPath() + "tempSE/" + matricula + ".pdf"));
        }
        OutputStream output = new FileOutputStream(Metodos.getPath() + "FrequenciaMensal.pdf");
        MergePDF.concatPDFs(pdfs, output, true);
        output.flush();
        output.close();
    }

    private static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

        Document document = new Document(PageSize.A4.rotate());
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 520, 5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void concatPDFsHorizontal(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

        Document document = new Document();
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 520, 5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void concatPDFs2(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

        Document document = new Document(PageSize.A4);
        try {
            List<InputStream> pdfs = streamOfPDFFiles;
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = iteratorPDFs.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);

                    // Code for pagination.
                    if (paginate) {
                        cb.beginText();
                        cb.setFontAndSize(bf, 9);
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "", 520, 5, 0);
                        cb.endText();
                    }
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
