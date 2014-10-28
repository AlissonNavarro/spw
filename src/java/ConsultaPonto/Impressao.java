/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

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
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
    /* Gera Relatorio e visualiza-o */

    public static String geraRelatorio(String inicio, String fim, Integer matricula, String nome, String cpf,
            String lotacao, String cargo, String regime, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, String administradorNome, Integer faltas, String adicionalNoturno,
            String horaExtra, String gratificacao) throws JRException, Exception {
        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorio";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("inicio", inicio);
        parameters.put("fim", fim);
        parameters.put("matricula", matricula);
        parameters.put("nome", nome);
        parameters.put("cpf", cpf);
        parameters.put("lotacao", lotacao);
        parameters.put("cargo", cargo);
        parameters.put("regime", regime);
        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("horasTrabalhadas", horasTrabalhadas);
        parameters.put("saldoHoras", saldoHoras);
        parameters.put("diasContratados", diasContratados);
        parameters.put("diasComparecidos", diasComparecidos);
        parameters.put("administradorNome", administradorNome);
        parameters.put("faltas", faltas);
        parameters.put("titulo", Metodos.getTitulo());
        parameters.put("adicionalNoturno", adicionalNoturno);
        parameters.put("horaExtra", horaExtra);
        if (gratificacao.equals(" Sem gratificações")) {
            gratificacao = "";
        } else {
            gratificacao = "Gratificação:               " + gratificacao;
        }
        parameters.put("gratificacao", gratificacao);

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioPonto.jasper", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatorioPonto.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioMensal.pdf");
        return null;
    }

    public static String geraRelatorioDepartamento(String inicio, String fim, Integer matricula, String nome, String cpf,
            String lotacao, String cargo, String regime, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, String administrador, Integer faltas, String adicionalNoturno,
            String horaExtra, String gratificacao) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorio";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("inicio", inicio);
        parameters.put("fim", fim);
        parameters.put("matricula", matricula);
        parameters.put("nome", nome);
        parameters.put("cpf", cpf);
        parameters.put("lotacao", lotacao);
        parameters.put("cargo", cargo);
        parameters.put("regime", regime);
        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("horasTrabalhadas", horasTrabalhadas);
        parameters.put("saldoHoras", saldoHoras);
        parameters.put("diasContratados", diasContratados);
        parameters.put("diasComparecidos", diasComparecidos);
        parameters.put("administradorNome", administrador);
        parameters.put("faltas", faltas);
        parameters.put("titulo", Metodos.getTitulo());
        parameters.put("adicionalNoturno", adicionalNoturno);
        parameters.put("horaExtra", horaExtra);
        if (gratificacao.equals(" Sem gratificações")) {
            gratificacao = "";
        } else {
            gratificacao = "Gratificação: " + gratificacao;
        }
        parameters.put("gratificacao", gratificacao);

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioPonto.jasper",
                Metodos.getPath() + "temp/" + matricula + ".pdf", parameters, jrRS);

        return null;
    }

    public static String geraRelatorioSemEscala(String inicio, String fim, Integer matricula, String nome, String cpf,
            String lotacao, String cargo, String regime, String administradorNome) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorio_sem_escala";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("inicio", inicio);
        parameters.put("fim", fim);
        parameters.put("matricula", matricula);
        parameters.put("nome", nome);
        parameters.put("cpf", cpf);
        parameters.put("lotacao", lotacao);
        parameters.put("cargo", cargo);
        parameters.put("regime", regime);
        parameters.put("administradorNome", administradorNome);
        parameters.put("titulo", Metodos.getTitulo());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioPontoSEscalada.jasper",
                Metodos.getPath() + "RelatorioPontoSEscalada" + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatorioPontoSEscalada.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioMensal.pdf");
        return null;
    }

    public static String geraRelatorioSemEscalaDepartamento(String inicio, String fim, Integer matricula, String nome, String cpf,
            String lotacao, String cargo, String regime, String administradorNome) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorio_sem_escala";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("inicio", inicio);
        parameters.put("fim", fim);
        parameters.put("matricula", matricula);
        parameters.put("nome", nome);
        parameters.put("cpf", cpf);
        parameters.put("lotacao", lotacao);
        parameters.put("cargo", cargo);
        parameters.put("regime", regime);
        parameters.put("administradorNome", administradorNome);
        parameters.put("titulo", Metodos.getTitulo());

        //verifica se diretorio onde sera gerando o relatorio existe, caso nao cria
        File diretorio = new File(Metodos.getPath() + "tempSE/");
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        
        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioPontoSEscalada.jasper",
                Metodos.getPath() + "tempSE/" + matricula + ".pdf", parameters, jrRS);

        return null;
    }

    public static String geraRelatorioPortaria1510Resumo(RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo,
            Date dataInicio, Date dataFim, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, String administradorNome, Integer faltas, String adicionalNoturno,
            String horaExtra, String gratificacao) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510Resumo";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("cargo", relatorioPortaria1510CabecalhoResumo.getCargo());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);

        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("horasTrabalhadas", horasTrabalhadas);
        parameters.put("saldoHoras", saldoHoras);
        parameters.put("diasContratados", diasContratados);
        parameters.put("diasComparecidos", diasComparecidos);
        parameters.put("administradorNome", administradorNome);
        parameters.put("faltas", faltas);
        parameters.put("adicionalNoturno", adicionalNoturno);
        parameters.put("horaExtra", horaExtra);
        parameters.put("gratificacao", gratificacao);
        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("titulo", Metodos.getTitulo());
        InputStream is = Metodos.getLogo();
        if (is != null) {
            parameters.put("logo", Metodos.getLogo());
        }

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510Resumo.jasper", Metodos.getPath() + "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf");
        return null;
    }

    public static String geraRelatorioPortaria1510ResumoSemSaldo(RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo,
            Date dataInicio, Date dataFim, String dataContratacao) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510Resumo";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("dataContratacao", dataContratacao);
        parameters.put("cargo", relatorioPortaria1510CabecalhoResumo.getCargo());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);
        InputStream is = Metodos.getLogo();
        if (is != null) {
            parameters.put("logo", Metodos.getLogo());
        }



        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("titulo", Metodos.getTitulo());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510ResumoSemSaldo.jasper",
                Metodos.getPath() + "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "Relatorio " + relatorioPortaria1510CabecalhoResumo.getNome() + ".pdf");
        return null;
    }

    public static void geraRelatorioPortaria1510ResumoPorDepartamento(RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo,
            Date dataInicio, Date dataFim, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, String administradorNome, Integer faltas, String adicionalNoturno,
            String horaExtra, String gratificacao) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510Resumo";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("cargo", relatorioPortaria1510CabecalhoResumo.getCargo());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);

        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("horasTrabalhadas", horasTrabalhadas);
        parameters.put("saldoHoras", saldoHoras);
        parameters.put("diasContratados", diasContratados);
        parameters.put("diasComparecidos", diasComparecidos);
        parameters.put("administradorNome", administradorNome);
        parameters.put("faltas", faltas);
        parameters.put("adicionalNoturno", adicionalNoturno);
        parameters.put("horaExtra", horaExtra);
        parameters.put("gratificacao", gratificacao);

        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("titulo", Metodos.getTitulo());
        InputStream is = Metodos.getLogo();
        if (is != null) {
            parameters.put("logo", Metodos.getLogo());
        }

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510Resumo.jasper", Metodos.getPath() + "temp/"
                + relatorioPortaria1510CabecalhoResumo.getUserid() + ".pdf", parameters, jrRS);

    }

    public static void geraRelatorioCatracaPorDepartamento(RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo,
            Date dataInicio, Date dataFim, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, String administradorNome, Integer faltas, String adicionalNoturno,
            String horaExtra, String gratificacao) throws JRException, Exception {


        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510Resumo";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("cargo", relatorioPortaria1510CabecalhoResumo.getCargo());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);

        parameters.put("horasPrevistas", horasPrevistas);
        parameters.put("horasTrabalhadas", horasTrabalhadas);
        parameters.put("saldoHoras", saldoHoras);
        parameters.put("diasContratados", diasContratados);
        parameters.put("diasComparecidos", diasComparecidos);
        parameters.put("administradorNome", administradorNome);
        parameters.put("faltas", faltas);
        parameters.put("adicionalNoturno", adicionalNoturno);
        parameters.put("horaExtra", horaExtra);
        parameters.put("gratificacao", gratificacao);

        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("titulo", Metodos.getTitulo());
        InputStream is = Metodos.getLogo();
        if (is != null) {
            parameters.put("logo", Metodos.getLogo());
        }

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaCatraca.jasper", Metodos.getPath() + "temp/"
                + relatorioPortaria1510CabecalhoResumo.getUserid() + ".pdf", parameters, jrRS);
    }

    public static void geraRelatorioSemSaldoPortaria1510ResumoPorDepartamento(RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo,
            Date dataInicio, Date dataFim, String dataContratacao) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510Resumo";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("matricula", relatorioPortaria1510CabecalhoResumo.getMatricula());
        parameters.put("Empregado", relatorioPortaria1510CabecalhoResumo.getNome());
        parameters.put("cpf", relatorioPortaria1510CabecalhoResumo.getCpf());
        parameters.put("lotacao", relatorioPortaria1510CabecalhoResumo.getDepartamento());
        parameters.put("cargo", relatorioPortaria1510CabecalhoResumo.getCargo());
        parameters.put("regime", relatorioPortaria1510CabecalhoResumo.getRegime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);
        parameters.put("dataContratacao", dataContratacao);
        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("titulo", Metodos.getTitulo());
        InputStream is = Metodos.getLogo();
        if (is != null) {
            parameters.put("logo", Metodos.getLogo());
        }

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510ResumoSemSaldo.jasper", Metodos.getPath() + "temp/"
                + relatorioPortaria1510CabecalhoResumo.getUserid() + ".pdf", parameters, jrRS);

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
        String a = "entrada 1";
        String b = "entrada";
        System.out.print(a.contains(b));


    }
}
