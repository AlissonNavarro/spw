/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Metodos;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amvboas
 */
public class Banco {

    Driver d;
    Connection c;
    Boolean hasConnection = false;
    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        hasConnection = true;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            hasConnection = false;
            System.out.println("Metodos: Conectar: " + cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            hasConnection = false;
            System.out.println("Metodos: Banco: " + ex);
        }
    }

    public String getTitulo() {
        String titulo = "";
        PreparedStatement pstmt = null;
        ResultSet rs;
        String query = "select titulo from config";

        try {

            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                titulo = rs.getString("titulo");
            }
            pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return titulo;
    }

    public InputStream getLogo() {
        InputStream logoInputStream = null;
        ResultSet rs;
        String query = "select logo from config";
        byte[] fileBytes = null;
        try {
            query = "select logo from config";
            Statement state = c.createStatement();
            rs = state.executeQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
                if (fileBytes != null) {
                    logoInputStream = new ByteArrayInputStream(fileBytes);
                }
            }

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

        return logoInputStream;
    }


     public byte[] getImageLogo() {

        ResultSet rs;
        String query = "select logo from config";
        byte[] fileBytes = null;
        try {
            query = "select logo from config";
            Statement state = c.createStatement();
            rs = state.executeQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
            }

        } catch (Exception e) {

        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
            return fileBytes;
        }
    }

    public String getSenhaAdministrador() {
        //Necessario que a senhaAdmin inicialize com null, pois é uma flag de condição usada no return 
        String senhaAdmin = null;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String query = "select senhaAdmin from config";

        try {

            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                senhaAdmin = rs.getString("senhaAdmin");
            }
            pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return senhaAdmin;
    }

    public void setTitulo(String titulo) {

        PreparedStatement pstmt = null;
        String query = "update config set titulo=? ";

        try {
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, titulo);
            pstmt.executeUpdate();

        } catch (Exception e) {
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
    }

    public void setSenhaAdmin(String senhaAdmin) {

        PreparedStatement pstmt = null;
        String query = "update config set senhaAdmin = ? ";

        try {
            pstmt = c.prepareStatement(query);
            pstmt.setString(1, senhaAdmin);
            pstmt.executeUpdate();

        } catch (Exception e) {
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
    }


    public void inserirConfig(String titulo, String senhaAdmin
                ,int tipoRelatorio ,byte[] image){

            PreparedStatement pstmt = null;
            String query = "insert into config(titulo , senhaAdmin  "
                    + ", tipoRelatorio , logo) values (?, ? ,? , ?)";

            try {
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, titulo);
                pstmt.setString(2, senhaAdmin);
                pstmt.setInt(3, tipoRelatorio);
                pstmt.setBytes(4, image);
                pstmt.executeUpdate();
            } catch (Exception e) {

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
    }


    public void deleteAllConfig(){

            PreparedStatement pstmt = null;
            String query = "delete from Config";

            try {
                pstmt = c.prepareStatement(query);
                pstmt.executeUpdate();
            } catch (Exception e) {

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
    }

   public int getTipoRelatorio() {
        int tipoRelatorio = -1;
        PreparedStatement pstmt = null;
        ResultSet rs;
        String query = "select tipoRelatorio from config";

        try {

            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                tipoRelatorio = rs.getInt("tipoRelatorio");
            }
            pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return tipoRelatorio;
    }

    public static void main(String[] args) {
        byte[] b = null;
        //    InputStream i = new
    }
}
