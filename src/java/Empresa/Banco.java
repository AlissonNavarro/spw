/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Empresa;

import Metodos.Metodos;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author prccardoso
 */
public class Banco implements Serializable {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
        }
    }
    
    public Boolean updateEmpresa(String razaoSocial, String cnpj, String endereco, Integer id) {

        Boolean flag = true;
        PreparedStatement pstmt = null;


        String query = "update empregador set CNPJ=?, RAZAO_SOCIAL=?, LOCAL=? WHERE ID=?";
                

        try {
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, cnpj);
            pstmt.setString(2, razaoSocial);
            pstmt.setString(3, endereco);
            pstmt.setInt(4, id);
  
            pstmt.executeUpdate();
        } catch (Exception e) {
            flag = false;
            System.out.println(e.getMessage());
            System.out.print(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public List<SelectItem> consultaEmpresaOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select id, razao_social from Empregador"
                    + " ORDER BY razao_social ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione a empresa"));

            while (rs.next()) {
                String id = rs.getString("id");
                String razao_social = rs.getString("razao_social");
                saida.add(new SelectItem(id, razao_social));
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
    
    public Empresa consultaDetalhesEmpresa(String item) {
        Empresa emp = new Empresa();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "SELECT * FROM  EMPREGADOR  WHERE (CNPJ = '" + item + "')";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);


            while (rs.next()) {

                String cnpj = rs.getString("cnpj");
                String razaoSocial = rs.getString("razao_social");
                String address = rs.getString("local");
                Integer cei = rs.getInt("cei");
                Integer id = rs.getInt("id");
                emp = new Empresa(cnpj, razaoSocial, address, cei, id);
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
        return emp;
    }
    
    public Boolean insertADIP(String ip){
        Boolean ok = false;
        
        PreparedStatement pstmt = null;
        try {
            String query = " UPDATE config SET IpAd = ?";            
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, ip);
            pstmt.executeUpdate();
                        
            pstmt.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ok;
    }
    
    public Boolean insertDigitIP(String ipSender, String ipCatcher){
        Boolean ok = false;
        
        PreparedStatement pstmt = null;
        try {
            String query = " UPDATE config SET ipDigitCatcher = ? , ipDigitSender = ?";            
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, ipCatcher);
            pstmt.setString(2, ipSender);
            pstmt.executeUpdate();
                        
            pstmt.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ok;
    }
    
    public String getServidorAD() {
        String servidor = "";

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select IpAD from config";
            if (c.isClosed()) {
                return servidor;
            }
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("IpAd");
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
        if (servidor == null) {
            servidor = "";
        }
        return servidor;
    }
    
    public String getServidorDigitSender() {
        String servidor = "";

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select ipDigitSender from config";
            if (c.isClosed()) {
                Conectar();
            }
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("ipDigitSender");
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
        if (servidor == null) {
            servidor = "";
        }
        return servidor;
    }
    
    public String getServidorDigitCatcher() {
        String servidor = "";

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select ipDigitCatcher from config";
            if (c.isClosed()) {
                Conectar();
            }
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("ipDigitCatcher");
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
        if (servidor == null) {
            servidor = "";
        }
        return servidor;
    }
    
}
