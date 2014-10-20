/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cargo;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author amvboas
 */
class Banco implements Serializable {

    Driver d;
    Connection c;
    // Cargo Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

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

    public List<SelectItem> consultaCargoOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select cod_cargo,nome from Cargo"
                    + " ORDER BY nome ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione o cargo"));

            while (rs.next()) {
                Integer cargoID = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                saida.add(new SelectItem(cargoID, nome));
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

    

    public Integer salvarNovoCargo(String novoCargoNome) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Cargo "
                    + " where nome = '" + novoCargoNome + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "insert into Cargo(Nome) values(?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novoCargoNome);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            try {
                if (c != null) {
                    if (flag != 1) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }

    public Integer salvarEditCargo(Cargo cargo) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome from Cargo "
                    + " WHERE     (cod_cargo <> " + cargo.getId() + ") AND (nome = '" + cargo.getNomeCargo() + "') ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "UPDATE Cargo SET nome = ? WHERE cod_cargo = ?";
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, cargo.getNomeCargo());
                
                pstmt.setInt(2, cargo.getId());

                pstmt.executeUpdate();
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            try {
                if (c != null) {
                    if (flag != 1) {
                        pstmt.close();
                    }
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }


    public Boolean excluirCargo(Integer cargoSelecionado) {
        Boolean flag = false;
        PreparedStatement pstmt = null;

        String queryDelete = "delete from Cargo "
                + " where cod_cargo = " + cargoSelecionado;
        try {
            pstmt = c.prepareStatement(queryDelete);
            pstmt.executeUpdate();
        } catch (SQLException e) {
           flag = true;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return flag;
    }
}
