/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Administracao.Banco;
import Administracao.Conexao;
import ConsultaPonto.DiaComEscala;
import Metodos.Metodos;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *
 * @author ppccardoso
 */
public class CatracaBanco {

    private Boolean isAtivo = false;
    static String driver = "jdbc:jtds:sqlserver://";
    Connection c;
    Connection wspconn;

    public CatracaBanco() {
        try {
            Conectar();
            Conectar2();
            isAtivo = true;
        } catch (SQLException ex) {
            isAtivo = false;
        }
    }

    public void Conectar2() throws SQLException {
        String url = getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            wspconn = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Usuário: CatracaBanco: " + cnfe);
        }
    }

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Administracao: Conectar: " + cnfe);
        }
    }

    public static String getUrlConexao() {
        Banco banco = new Banco();
        Conexao conn = new Conexao();
        conn.CleanConexao();
        conn = banco.consultaConexaoByDesc("catraca");
        String urlConexao = driver + conn.getServer() + ":"
                + conn.getPort() + "/"
                + conn.getDatabase()
                + ";user=" + conn.getUsuario()
                + ";password=" + conn.getSenha();
        return urlConexao;
    }

    public String getCpf(int cod_func) {
        String saida = "";
        try {
            if (c.isClosed()) {
                Conectar();
            }

            ResultSet rs;
            Statement stmt;

            String sql = "select ssn from userinfo where userid = " + cod_func;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                saida = rs.getString("ssn");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return saida;
    }

    public String getEntradas(DiaComEscala diaComEscala, int cod_func) {
        String saida = "";
        String cpf = getCpf(cod_func);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diaComEscala.getDia());
        DecimalFormat formatter = new DecimalFormat("#00.###");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String data = "" + year + formatter.format(month) + formatter.format(day);
        try {
            if (wspconn.isClosed()) {
                Conectar2();
            }
            ResultSet rs;
            Statement stmt;

            String sql = "select l.* from LogAcesso l, Pessoas p"
                    + " where msgNumero = 16 and areNumero = 2 and l.lacData = " + data
                    + " and l.PIN = p.pin and p.pesCPF like '%" + cpf + "%' "
                    + " order by l.lacdata, l.lachora";

            stmt = wspconn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (!saida.equals("")) {
                    saida = saida + ", ";
                }
                String horarioBruto = rs.getString("lachora");
                String horarioRefinado = horarioBruto.substring(0, 2) + ":" + horarioBruto.substring(2, 4);
                saida = saida + horarioRefinado;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (wspconn != null) {
                    wspconn.close();
                }
            } catch (Exception e) {
            }
        }
        return saida;
    }

    public String getSaidas(DiaComEscala diaComEscala, int cod_func) {
        String saida = "";
        String cpf = getCpf(cod_func);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diaComEscala.getDia());
        DecimalFormat formatter = new DecimalFormat("#00.###");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String data = "" + year + formatter.format(month) + formatter.format(day);
        try {
            if (wspconn.isClosed()) {
                Conectar2();
            }
            ResultSet rs;
            Statement stmt;

            String sql = "select l.* from LogAcesso l, Pessoas p"
                    + " where msgNumero = 16 and areNumero = 1 and l.lacData = " + data
                    + " and l.PIN = p.pin and p.pesCPF like '%" + cpf + "%' "
                    + " order by l.lacdata, l.lachora";

            stmt = wspconn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (!saida.equals("")) {
                    saida = saida + ", ";
                }
                String horarioBruto = rs.getString("lachora");
                String horarioRefinado = horarioBruto.substring(0, 2) + ":" + horarioBruto.substring(2, 4);
                saida = saida + horarioRefinado;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (wspconn != null) {
                    wspconn.close();
                }
            } catch (Exception e) {
            }
        }
        return saida;
    }
}
