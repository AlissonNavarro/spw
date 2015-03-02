package comunicacao;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.Serializable;

public class AcessoBD implements Serializable {

    public Connection c;
    public PreparedStatement pstmt;
    private Statement stmt;
    private String url;

    public AcessoBD() {
        url = Metodos.getUrlCompleta();
        if (url.equals("")) {
            Metodos.carregarUrlCompleta();
            url = Metodos.getUrlCompleta();
        }
    }

    public boolean Conectar() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            return true;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("AcessoBD Conectar " + cnfe);
        } catch (SQLException ex) {
            System.out.println("AcessoBD Conectar " + ex);
        }
        return false;
    }

    public void Desconectar() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException ex) {
            System.out.println("AcessoBD Desconectar " + ex);
        }
    }

    public ResultSet executeQuery(String sql) {
        if (Conectar()) {
            try {
                stmt = c.createStatement();
                return stmt.executeQuery(sql);
            } catch (SQLException ex) {
                System.out.println("AcessoBD executeQuery param sql " + ex);
            }
        }
        return null;
    }

    public int executeUpdate(String sql) {
        if (Conectar()) {
            try {
                stmt = c.createStatement();
                return stmt.executeUpdate(sql);
            } catch (SQLException ex) {
                System.out.println("AcessoBD executeUpdate param sql " + ex);
            }
        }
        return -1;
    }

    public boolean prepareStatement(String sql) {
        if (Conectar()) {
            try {
                pstmt = c.prepareStatement(sql);
                return true;
            } catch (SQLException ex) {
                System.out.println("AcessoBD prepareStatement " + ex);
            }

        }
        return false;
    }

    public ResultSet executeQuery() {
        try {
            return pstmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println("AcessoBD executeQuery " + ex);
        }
        return null;
    }

    public int executeUpdate() {
        try {
            return pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("AcessoBD executeUpdate " + ex);
        }
        return -1;
    }

}
