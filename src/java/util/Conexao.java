package util;

import Metodos.Metodos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private Connection c;
    private Statement st;
    private PreparedStatement pst;

    public void Conectar() {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("util.Conexao Conectar(): " + ex);
        }
    }

    public void Desconectar() {
        try {
            c.close();
        } catch (SQLException ex) {
            System.out.println("util.Conexao Desconectar(): " + ex);
        }
    }

}
