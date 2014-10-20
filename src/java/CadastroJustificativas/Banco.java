/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJustificativas;

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

    public List<Justificativa> consultaJustificativas() {

        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from LeaveClass ORDER BY leavename";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Boolean soAdministrador = rs.getBoolean("administrador");
                Boolean total = rs.getBoolean("total");
                Justificativa justificativa = new Justificativa();
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
                justificativa.setSoAdministrador(soAdministrador);
                justificativa.setIsTotal(total);
                justificativaList.add(justificativa);
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
        return justificativaList;
    }

    public Justificativa consultaJustificativa(Integer justificativa_id) {

        Justificativa justificativa = new Justificativa();
        PreparedStatement pstmt = null;
        ResultSet rs;
        try {

            String sql;

            sql = "select * "
                    + " from LeaveClass "
                    + " where leaveid = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, justificativa_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Boolean soAdministrador = rs.getBoolean("administrador");
                Boolean total = rs.getBoolean("total");
                Boolean descricaoObrigatoria = rs.getBoolean("descricaoObrigatoria");
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
                justificativa.setIsTotal(total);
                justificativa.setSoAdministrador(soAdministrador);
                justificativa.setIsDescricaoObrigatoria(descricaoObrigatoria);
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
        return justificativa;
    }

    public void editarJustificativa(Justificativa justificativa) {

        PreparedStatement pstmt = null;

        try {
            String query = "update LeaveClass SET leaveName = ?, administrador = ?, total = ?, descricaoObrigatoria = ? "
                    + "where leaveid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, justificativa.getJustificativaNome());
            pstmt.setBoolean(2, justificativa.getSoAdministrador());
            pstmt.setBoolean(3, justificativa.getIsTotal());
            pstmt.setBoolean(4, justificativa.getIsDescricaoObrigatoria());
            pstmt.setInt(5, justificativa.getJustificativaID());
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

    public Boolean deletarJustificativa(Integer justificativaID) {

        Boolean flag = true;
        PreparedStatement pstmt = null;

        try {
            String query = "delete from LeaveClass"
                    + " where leaveid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, justificativaID);

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

    public Boolean inserirNovaJustificativa(Justificativa novaJustificativa) {

        Boolean flag = true;
        PreparedStatement pstmt = null;
        ResultSet rs;

        try {

            String query = "select leaveName from LeaveClass"
                    + " where leaveName =? ";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, novaJustificativa.getJustificativaNome());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                flag = false;
            }
            if (flag) {
                query = "insert into LeaveClass (leavename,administrador,total,descricaoObrigatoria)  VALUES (?,?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novaJustificativa.getJustificativaNome());
                pstmt.setBoolean(2, novaJustificativa.getSoAdministrador());
                pstmt.setBoolean(3, novaJustificativa.getIsTotal());
                pstmt.setBoolean(4, novaJustificativa.getIsDescricaoObrigatoria());

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
