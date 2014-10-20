/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroCronograma;

import ConsultaPonto.*;
import Metodos.Metodos;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author amsgama
 */
public class Impressao {

    private static Connection getConnection() throws
        ClassNotFoundException, SQLException {
        String driver = "net.sourceforge.jtds.jdbc.Driver";
        String url = Metodos.getUrlConexao();
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url);
        return con;
    }

    public static String geraRelatorioEscalaIndividual(Date inicio,Date fim,String horasPrevistas,
            RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioEscalaIndividual";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio_ = sdf.format(inicio);
        String fim_ = sdf.format(fim);
        parameters.put("DataInicial", inicio_);
        parameters.put("DataFinal", fim_);
        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("titulo", Metodos.getTitulo());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaEscalaIndividual.jasper", Metodos.getPath() + "Relatorio Escala Individual.pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "Relatorio Escala Individual.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "Relatorio Escala Individual.pdf");
        return null;
    }

    public static void popUpRelatorio() throws IOException, Exception {
        File file = new File(Metodos.getPath() + "FrequenciaMensal.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioMensal.pdf");
    }

    public static void popUpRelatorio(String relatorio) throws IOException, Exception {
        File file = new File(Metodos.getPath() + relatorio + ".pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, relatorio + ".pdf");
    }

    public static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static void download(byte[] arquivo, String nome) throws Exception {
        HttpServletResponse response = getServletResponse();
        response.setContentType(null);
        response.setContentLength(arquivo.length);
        response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + nome + "\"");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(arquivo, 0, arquivo.length);
        outputStream.flush();
        outputStream.close();
        getFacesContext().responseComplete();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static void main(String[] args) throws JRException, Exception {
    }
}

