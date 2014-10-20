/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Metodos.Metodos;
import ConsultaPonto.*;
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
    /* Gera Relatorio e visualiza-o */

    public static String geraRelatorioDepartamentoo(String inicio, String fim, Integer matricula, String nome, String cpf,
            String lotacao, String cargo, String regime, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
            String diasContratados, String diasComparecidos, Integer faltas) throws JRException, Exception {

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
        parameters.put("administradorNome", "");
        parameters.put("faltas", faltas);
        parameters.put("versao", Metodos.getVersao());

        //   FacesContext context = FacesContext.getCurrentInstance();
        //     ServletContext sc = (ServletContext) context.getExternalContext().getContext();

        //     String path = sc.getRealPath("web/relatorio/RelatorioPonto.pdf");
        //      System.out.println(path);

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
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioPontoSEscalada.jasper",
                Metodos.getPath() + "RelatorioPontoSEscalada" + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatorioPontoSEscalada.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioMensal.pdf");
        return null;
    }

    public static String geraRelatorioLotacao(String departamento, String escala, Date data, Integer totalFuncionarios) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorio_lotacao";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataStr = sdf.format(data);

        parameters.put("data", dataStr);
        parameters.put("departamento", departamento);
        parameters.put("escala", escala);
        parameters.put("totalFuncionarios", totalFuncionarios);
        parameters.put("titulo", Metodos.getTitulo());
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioLotacao.jasper", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatorioPonto.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioLotacao.pdf");
        return null;
    }

    public static String geraRelatorioPortaria1510(String empregador, String endereco, String empregado,
            String Admissao, Date dataInicio, Date dataFim) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("Empregador", empregador);
        parameters.put("Endereco", endereco);
        parameters.put("Empregado", empregado);
        parameters.put("Admissao", Admissao);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);
        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510.jasper", Metodos.getPath() + "RelatoriaPortaria1510" + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatoriaPortaria1510.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatoriaPortaria1510.pdf");
        return null;
    }

    public static String geraRelatorioPortaria1510PorDepartamento(String empregador, String endereco, String empregado,
            String Admissao, Date dataInicio, Date dataFim, Integer userid) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioPortaria1510";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

        parameters.put("Empregador", empregador);
        parameters.put("Endereco", endereco);
        parameters.put("Empregado", empregado);
        parameters.put("Admissao", Admissao);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String inicio = sdf.format(dataInicio);
        String fim = sdf.format(dataFim);
        parameters.put("DataInicial", inicio);
        parameters.put("DataFinal", fim);
        String url = Metodos.getUrlConexao();
        Connection conexao = java.sql.DriverManager.getConnection(url);
        parameters.put("conexao", conexao);
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510.jasper", Metodos.getPath() + "temp/"
                + userid + ".pdf", parameters, jrRS);

        return null;
    }

    public static void geraRelatorioListaFuncionarioSemEscala(Integer totalFuncionarios) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from RelatorioFuncionarioSemEscala";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

         parameters.put("titulo", Metodos.getTitulo());
         parameters.put("totalFuncionario", totalFuncionarios);
         parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() +
                "RelatorioFuncionarioSemEscala.jasper", Metodos.getPath() + "temp/"
                +"listaFuncionarioSemEscala.pdf", parameters, jrRS);
    }

    public static void geraRelatorioListaFuncionarioSemRegistrodePonto(Integer totalFuncionarios) throws JRException, Exception {

        Connection con = getConnection();
        Statement stm = con.createStatement();
        //Foi usado a mesma tabela que o RelatorioFuncionarioSemEscala, por ser os mesmo campos
        String query = "select * from RelatorioFuncionarioSemEscala";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();

         parameters.put("titulo", Metodos.getTitulo());
         parameters.put("totalFuncionario", totalFuncionarios);
         parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() +
                "RelatorioFuncionarioSemRegistro.jasper", Metodos.getPath() + "temp/"
                +"RelatorioFuncionarioSemRegistro.pdf", parameters, jrRS);
    }

    public static String geraRelatorioPortaria1510Resumo(String matricula, String nome, String cpf,
            String lotacao, String regime, Date dataInicio, Date dataFim, String horasPrevistas, String horasTrabalhadas, String saldoHoras,
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

        parameters.put("matricula", matricula);
        parameters.put("Empregado", nome);
        parameters.put("cpf", cpf);
        parameters.put("lotacao", lotacao);
        parameters.put("regime", regime);
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
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatoriaPortaria1510Resumo.jasper", Metodos.getPath() + "Relatorio " + nome + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "Relatorio " + nome + ".pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "Relatorio " + nome + ".pdf");
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

    public static Boolean popUpLog(String log) throws IOException, Exception {
        File file = new File(Metodos.getPathLogs() + log + ".log");
        if (file.exists()) {
            byte[] teste = getBytesFromFile(file);
            download(teste, log + ".log");
            return true;
        } else {
            return false;
        }
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

    public static String geraRelatorioResumoEscala(List<String> diasMesList, List<String> diasSemanaList, String horaAno,
            String mesAno, String dept, String legendaSigla, String legendaNumero, String legendaAfastamento)
            throws ClassNotFoundException, SQLException, JRException, IOException, Exception {
        Connection con = getConnection();
        Statement stm = con.createStatement();
        String query = "select * from relatorioResumoEscala";
        ResultSet rs = stm.executeQuery(query);
        /* implementação da interface JRDataSource para DataSource ResultSet */
        JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
        /* HashMap de parametros utilizados no relatório. Sempre instanciados */
        Map parameters = new HashMap();
        parameters.put("diasMesList", diasMesList);
        parameters.put("semanasDoMesList", diasSemanaList);
        parameters.put("horaAno", horaAno);
        parameters.put("mesAno", mesAno);
        parameters.put("dept", dept);
        parameters.put("titulo", Metodos.getTitulo());
        parameters.put("legendaSigla", legendaSigla);
        parameters.put("legendaNumero", legendaNumero);
        parameters.put("legendaAfastamento", legendaAfastamento);
        parameters.put("versao", Metodos.getVersao());

        JasperRunManager.runReportToPdfFile(Metodos.getPath() + "RelatorioResumoEscala.jasper",
                Metodos.getPath() + "RelatorioResumoEscala" + ".pdf", parameters, jrRS);

        File file = new File(Metodos.getPath() + "RelatorioResumoEscala.pdf");
        byte[] teste = getBytesFromFile(file);
        download(teste, "RelatorioResumoEscala.pdf");
        return null;
    }

    public static void main(String[] args) throws JRException, Exception {
        File file = new File("d");

    }
}

