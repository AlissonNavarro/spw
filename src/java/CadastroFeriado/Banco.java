/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroFeriado;

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
import java.util.Date;
import java.util.List;

/**
 *
 * @author amsgama
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

    public List<Feriado> consultaFeriados() {

        List<Feriado> feriadoList = new ArrayList<Feriado>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select holidayid,holidayname,starttime,Oficial from holidays ORDER BY starttime";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer id = rs.getInt("holidayid");
                String nome = rs.getString("holidayname");
                Date data = rs.getDate("startTime");
                Boolean isOficial = rs.getBoolean("Oficial");

                Feriado feriado = new Feriado(id, nome, data, isOficial);

                feriadoList.add(feriado);
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
        return feriadoList;
    }
    
    public ArrayList<Feriado> consultaFeriadosPorData(Date inicio, Date fim) {

        ArrayList<Feriado> feriadoList = new ArrayList<Feriado>();
        try {
            ResultSet rs;
            PreparedStatement pstmt;

            String sql;
            
            java.sql.Date i = new java.sql.Date(inicio.getTime());
            java.sql.Date f = new java.sql.Date(fim.getTime());
            
            sql = "select holidayid,holidayname,starttime,Oficial from holidays where starttime between ? and ? ORDER BY starttime";
            pstmt = c.prepareStatement(sql);
            pstmt.setDate(1, i);
            pstmt.setDate(2, f);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("holidayid");
                String nome = rs.getString("holidayname");
                Date data = rs.getDate("startTime");
                Boolean isOficial = rs.getBoolean("Oficial");

                Feriado feriado = new Feriado(id, nome, data, isOficial);

                feriadoList.add(feriado);
            }
            rs.close();
            pstmt.close();
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
        return feriadoList;
    }    

    public Feriado consultaFeriado(Integer id) {

        Feriado feriado = new Feriado();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select holidayid,holidayname,starttime,Oficial from holidays "
                    + " where holidayid = " + id;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer id_ = rs.getInt("holidayid");
                String nome = rs.getString("holidayname");
                Date data = rs.getDate("startTime");
                Boolean isOficial = rs.getBoolean("Oficial");

                feriado = new Feriado(id, nome, data, isOficial);

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
        return feriado;
    }

    public int addFeriado(Feriado feriado) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {            

            if (flag == 0) {
                String query = "insert into holidays(holidayname,starttime,Oficial) values(?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, feriado.getNome());
                java.sql.Date data_ = new java.sql.Date(feriado.getData().getTime());
                pstmt.setDate(2, data_);
                pstmt.setBoolean(3, feriado.getIsOficial());

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

    public int editFeriado(Feriado feriado) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            String query = "update holidays set holidayname = ?,starttime = ?, Oficial = ?"
                    + " where holidayid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, feriado.getNome());
            java.sql.Date data_ =  new java.sql.Date(feriado.getData().getTime());
            pstmt.setDate(2, data_);
            pstmt.setBoolean(3, feriado.getIsOficial());
            pstmt.setInt(4, feriado.getId());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 1;
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

    public boolean deleteFeriado(Integer feriado_id) {

        boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            String query = "delete from holidays"
                    + " where holidayid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, feriado_id);

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
}
