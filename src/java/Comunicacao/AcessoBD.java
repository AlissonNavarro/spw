package Comunicacao;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AcessoBD {

    private Connection c;
    private Statement stmt;
    public PreparedStatement pstmt;
    private ResultSet rs;

    public AcessoBD() {
    }

    public boolean Conectar() {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            return true;
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        } catch (SQLException ex) {
            System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
        }
        return false;
    }

    public void Desconectar() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException ex) {
            System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
        }
    }

    public ResultSet executeQuery(String sql) {
        if (Conectar()) {
            try {
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                return rs;
            } catch (SQLException ex) {
                System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
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
                System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
            }
        }
        return -1;
    }

    public boolean prepareStatement(String query) {
        if (Conectar()) {
            try {
                pstmt = c.prepareStatement(query);
                return true;
            } catch (SQLException ex) {
                System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
            }
        }
        return false;
    }

    public int executeUpdate() {
        if (Conectar()) {
            try {
                return pstmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println(AcessoBD.class.getName() + " " + ex.getMessage());
            }
        }
        return -1;
    }

}
