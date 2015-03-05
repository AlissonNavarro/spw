package Metodos;

import comunicacao.AcessoBD;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Banco {

    Boolean hasConnection = false;
    AcessoBD con;
    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public Banco() {
        con = new AcessoBD();
    }

    public String getTitulo() {
        String titulo = "";
        ResultSet rs;
        String query = "select titulo from config";
        try {
            rs = con.executeQuery(query);
            if (rs.next()) {
                titulo = rs.getString("titulo");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
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
            rs = con.executeQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
                if (fileBytes != null) {
                    logoInputStream = new ByteArrayInputStream(fileBytes);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return logoInputStream;
    }

    public byte[] getImageLogo() {
        ResultSet rs;
        String query;
        byte[] fileBytes = null;
        try {
            query = "select logo from config";
            rs = con.executeQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return fileBytes;
    }

    public String getSenhaAdministrador() {
        //Necessario que a senhaAdmin inicialize com null, pois é uma flag de condição usada no return 
        String senhaAdmin = null;

        ResultSet rs;
        String query = "select senhaAdmin from config";
        try {
            rs = con.executeQuery(query);
            if (rs.next()) {
                senhaAdmin = rs.getString("senhaAdmin");
            }
        }catch (Exception e) {
            System.out.println(e);
        }finally {
            con.Desconectar();
        }
        return senhaAdmin;
    }

    public void setTitulo(String titulo) {

        String query = "update config set titulo=? ";

        try {
            con.prepareStatement(query);
            con.pstmt.setString(1, titulo);
            con.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
    }

    public void setSenhaAdmin(String senhaAdmin) {
        String query = "update config set senhaAdmin = ? ";
        try {
            con.prepareStatement(query);
            con.pstmt.setString(1, senhaAdmin);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
    }

    public void inserirConfig(String titulo, String senhaAdmin, int tipoRelatorio, byte[] image) {

        String query = "insert into config(titulo , senhaAdmin  "
                + ", tipoRelatorio , logo) values (?, ? ,? , ?)";

        try {
            con.prepareStatement(query);
            con.pstmt.setString(1, titulo);
            con.pstmt.setString(2, senhaAdmin);
            con.pstmt.setInt(3, tipoRelatorio);
            con.pstmt.setBytes(4, image);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.print(e);
        } finally {
            con.Desconectar();
        }
    }

    public void deleteAllConfig() {

        String query = "delete from Config";

        try {
            con.executeUpdate(query);
        } catch (Exception e) {
            System.out.print(e);
        } finally {
            con.Desconectar();
        }
    }

    public int getTipoRelatorio() {
        int tipoRelatorio = -1;

        ResultSet rs;
        String query = "select tipoRelatorio from config";

        try {
            rs = con.executeQuery(query);
            if (rs.next()) {
                tipoRelatorio = rs.getInt("tipoRelatorio");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return tipoRelatorio;
    }
}
