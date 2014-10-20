/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroDeCategoriaAfastamento;

import Metodos.Metodos;
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
 * @author amsgama
 */
public class Banco {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
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

    public List<CategoriaAfastamento> consultaCategoriaAfastamento() {

        List<CategoriaAfastamento> categoriaAfastamentoList = new ArrayList<CategoriaAfastamento>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from categoriaAfastamento"
                    + " ORDER BY descAfastamento";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String legendaAfastamento = rs.getString("legendaAfastamento");
                CategoriaAfastamento ca = new CategoriaAfastamento(cod_categoria, descAfastamento, legendaAfastamento);
                categoriaAfastamentoList.add(ca);
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
        return categoriaAfastamentoList;
    }

    public CategoriaAfastamento consultaCategoriaAfastamento(Integer categoriaAfastamento_id) {

        CategoriaAfastamento ca = new CategoriaAfastamento();
        PreparedStatement pstmt = null;
        ResultSet rs;
        try {

            String sql;

            sql = "select * from CategoriaAfastamento "
                    + " where cod_categoria = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, categoriaAfastamento_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String legendaAfastamento = rs.getString("legendaAfastamento");
                ca = new CategoriaAfastamento(cod_categoria, descAfastamento, legendaAfastamento);
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
        return ca;
    }

    public void editarCategoriaAfastamento(CategoriaAfastamento categoriaAfastamento) {

        PreparedStatement pstmt = null;

        try {
            String query = "update categoriaAfastamento SET descAfastamento = ?, legendaAfastamento = ?"
                    + "where cod_categoria = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, categoriaAfastamento.getDescCategoriaAfastamento());
            pstmt.setString(2, categoriaAfastamento.getLegenda());
            pstmt.setInt(3, categoriaAfastamento.getCategoriaAfastamentoID());

            pstmt.executeUpdate();

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
    }

    public Boolean deletarCategoriaAfastamento(Integer cod_categoria) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {
            String query = "delete from categoriaAfastamento"
                    + " where cod_categoria = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_categoria);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
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

    public Boolean inserirNovaCategoria(CategoriaAfastamento novaCategoria) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        ResultSet rs;

        try {

            String query = "select descAfastamento from CategoriaAfastamento"
                    + " where descAfastamento = ? ";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, novaCategoria.getDescCategoriaAfastamento());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                flag = false;
            }
            if (flag) {
                query = "insert into CategoriaAfastamento (descAfastamento,legendaAfastamento) VALUES (?,?)";
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novaCategoria.getDescCategoriaAfastamento());
                pstmt.setString(2, novaCategoria.getLegenda());
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
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
}
