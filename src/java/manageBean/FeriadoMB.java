package manageBean;

import entidades.Feriado;
import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeriadoMB implements Serializable {

    AcessoBD con;

    public FeriadoMB() {
        con = new AcessoBD();
    }

    public List<Feriado> consultaFeriados() {

        List<Feriado> feriadoList = new ArrayList<Feriado>();
        try {
            String sql = "select holidayid,holidayname,starttime,Oficial from holidays ORDER BY starttime";
            ResultSet rs = con.executeQuery(sql);
            Feriado feriado;

            while (rs.next()) {
                Integer id = rs.getInt("holidayid");
                String nome = rs.getString("holidayname");
                Date data = rs.getDate("startTime");
                Boolean isOficial = rs.getBoolean("Oficial");

                feriado = new Feriado(id, nome, data, isOficial);
                feriadoList.add(feriado);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("FeriadoMB consultaFeriados " + ex);
        } finally {
            con.Desconectar();
        }
        return feriadoList;
    }

    public ArrayList<Feriado> consultaFeriadosPorData(Date inicio, Date fim) {

        ArrayList<Feriado> feriadoList = new ArrayList<Feriado>();
        try {
            String sql = "select holidayid,holidayname,starttime,Oficial from holidays "
                    + "where starttime between ? and ? ORDER BY starttime";

            java.sql.Date i = new java.sql.Date(inicio.getTime());
            java.sql.Date f = new java.sql.Date(fim.getTime());

            if (con.prepareStatement(sql)) {
                con.pstmt.setDate(1, i);
                con.pstmt.setDate(2, f);
                ResultSet rs = con.executeQuery();
                Feriado feriado;

                while (rs.next()) {
                    Integer id = rs.getInt("holidayid");
                    String nome = rs.getString("holidayname");
                    Date data = rs.getDate("startTime");
                    Boolean isOficial = rs.getBoolean("Oficial");

                    feriado = new Feriado(id, nome, data, isOficial);
                    feriadoList.add(feriado);
                }
                rs.close();
            }
        } catch (Exception ex) {
            System.out.println("FeriadoMB consultaFeriadosPorData " + ex);
        } finally {
            con.Desconectar();
        }
        return feriadoList;
    }

    public Feriado consultaFeriado(int id) {

        Feriado feriado = new Feriado();
        try {
            String sql = "select holidayid,holidayname,starttime,Oficial from holidays "
                    + " where holidayid = " + id;
            ResultSet rs = con.executeQuery(sql);

            if (rs.next()) {
                Integer id_ = rs.getInt("holidayid");
                String nome = rs.getString("holidayname");
                Date data = rs.getDate("startTime");
                Boolean isOficial = rs.getBoolean("Oficial");
                feriado = new Feriado(id_, nome, data, isOficial);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("FeriadoMB consultaFeriado " + ex);
        } finally {
            con.Desconectar();
        }
        return feriado;
    }

    public int inserir(Feriado feriado) {
        int flag = 0;
        try {
            String sql = "insert into holidays(holidayname,starttime,Oficial) values(?,?,?)";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, feriado.getNome());
                java.sql.Date data_ = new java.sql.Date(feriado.getData().getTime());
                con.pstmt.setDate(2, data_);
                con.pstmt.setBoolean(3, feriado.getIsOficial());
                con.executeUpdate();
                flag = 1;
            }
        } catch (Exception ex) {
            System.out.println("FeriadoMB inserir " + ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int alterar(Feriado feriado) {
        int flag = 0;
        try {
            String sql = "update holidays set holidayname = ?,starttime = ?, Oficial = ?"
                    + " where holidayid = ?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setString(1, feriado.getNome());
                java.sql.Date data_ = new java.sql.Date(feriado.getData().getTime());
                con.pstmt.setDate(2, data_);
                con.pstmt.setBoolean(3, feriado.getIsOficial());
                con.pstmt.setInt(4, feriado.getId());
                con.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("FeriadoMB alterar "+ex);
            flag = 1;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean deletar(int feriadoId) {
        boolean flag = false;
        try {
            String sql = "delete from holidays where holidayid = "+feriadoId;
            flag = con.executeUpdate(sql) > 0;
        } catch (Exception ex) {
            System.out.println("FeriadoMB deletar "+ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }
}
