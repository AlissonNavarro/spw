/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import CadastroJustificativas.*;
import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

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

    public int addJornadaCadastro(JornadaCadastro jc) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select name from num_run "
                    + "where name = '" + jc.getNome() + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "insert into num_run(oldid, name,startdate,enddate, cyle, units ) values(?,?,?,?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, "-1");
                pstmt.setString(2, jc.getNome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dia = sdf.format(jc.getDataInicio());
                pstmt.setString(3, dia);
                String sDataFinal = "31/12/2200 00:00:00";
                pstmt.setString(4, sDataFinal);
                pstmt.setString(5, jc.getQuantidadeCiclos().toString());
                pstmt.setString(6, jc.getUnidadeCiclosInt().toString());



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

    public List<JornadaCadastro> consultaJornadas() {

        List<JornadaCadastro> jornadaCadastroList = new ArrayList<JornadaCadastro>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select n.*, u.name as nome from num_run n left join userinfo u "
                    + " on (n.responsavel = u.userid)"
                    + " ORDER BY n.name";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer num_runid = rs.getInt("num_runid");
                String name = rs.getString("name");
                Date startdate = rs.getDate("startdate");
                Integer cyle = rs.getInt("cyle");
                Integer units = rs.getInt("units");
                Boolean isRegular = rs.getBoolean("isRegular");
                String nomeResponsavel = rs.getString("nome");
                JornadaCadastro jc = new JornadaCadastro(num_runid, name, startdate, cyle, units);
                jc.setIsRegular(isRegular);
                jc.setNomeResponsavel(nomeResponsavel);
                jornadaCadastroList.add(jc);
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
        return jornadaCadastroList;
    }
    
    public boolean findCronogramas(Integer jornada_id){
        boolean flag = false;

        try {
            ResultSet rs;
            Statement stmt;

            String sql = "select * from user_of_run where num_of_run_id = "+jornada_id;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = true;
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
        return flag;
    }

    public boolean deleteJornada(Integer jornada_id) {

        boolean flag = true;
        PreparedStatement pstmt = null;

        try {
            c.setAutoCommit(false);
            String query = "delete from num_run_deil"
                    + " where num_runid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, jornada_id);

            pstmt.executeUpdate();
            query = "delete from num_run"
                    + " where num_runid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, jornada_id);

            pstmt.executeUpdate();
            
            c.commit();
        } catch (Exception e) {
            try {
                System.out.println(e.getMessage());
                flag = false;
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("deleteJornada 1: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public List<Justificativa> consultaJustificativas() {

        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select leaveid,leavename from LeaveClass ORDER BY leavename";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Justificativa justificativa = new Justificativa();
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
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

            sql = "select leaveid,leavename"
                    + " from LeaveClass "
                    + " where leaveid = ?";

            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, justificativa_id);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
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
            String query = "update LeaveClass SET leaveName = ? "
                    + "where leaveid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, justificativa.getJustificativaNome());
            pstmt.setInt(2, justificativa.getJustificativaID());

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
                query = "insert into LeaveClass (leavename)  VALUES (?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novaJustificativa.getJustificativaNome());

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

    public int editJornada(JornadaCadastro editJornadaCadastro) {

        int flag = 0;
        PreparedStatement pstmt = null;

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select name from num_run "
                    + "where name = '" + editJornadaCadastro.getNome() + "' and num_runid <> '" + editJornadaCadastro.getId().toString() + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "update num_run set oldid = ?, name = ?, startdate = ?, enddate = ?, cyle = ?, units = ?"
                        + " where num_runid = ?";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, "-1");
                pstmt.setString(2, editJornadaCadastro.getNome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dia = sdf.format(editJornadaCadastro.getDataInicio());
                pstmt.setString(3, dia);
                String sDataFinal = "31/12/2200 00:00:00";
                pstmt.setString(4, sDataFinal);
                pstmt.setString(5, editJornadaCadastro.getQuantidadeCiclos().toString());
                pstmt.setString(6, editJornadaCadastro.getUnidadeCiclosInt().toString());
                pstmt.setString(7, editJornadaCadastro.getId().toString());

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
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

    public List<SelectItem> consultaHorariosSelectItemList() {

        List<SelectItem> horariosSelectItemList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from schClass ORDER BY schName";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer schClassID = rs.getInt("schClassID");
                String schName = rs.getString("schName");
                horariosSelectItemList.add(new SelectItem(schClassID, schName));
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
        return horariosSelectItemList;
    }

    public List<Turno> consultaHorariosList() {
        List<Turno> turnoList = new ArrayList<Turno>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from schClass where active = 'true' ORDER BY schName";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer schClassID = rs.getInt("schClassID");
                String schName = rs.getString("schName");
                Timestamp entrada = rs.getTimestamp("StartTime");
                Timestamp inicioFaixaEntrada = rs.getTimestamp("CheckInTime1");
                Timestamp fimFaixaEntrada = rs.getTimestamp("CheckInTime2");
                Timestamp inicioFaixaSaida = rs.getTimestamp("CheckOutTime1");
                Timestamp fimFaixaSaida = rs.getTimestamp("CheckOutTime2");
                Timestamp saida = rs.getTimestamp("EndTime");
                Turno turno = new Turno(schClassID, schName, entrada, saida, saida.before(entrada), inicioFaixaEntrada, fimFaixaEntrada, inicioFaixaSaida, fimFaixaSaida);
                turnoList.add(turno);
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
        return turnoList;
    }

    public ArrayList<HorariosXdia> consultaHorariosXJornada(Integer jornada_ID) {
        ArrayList<HorariosXdia> horariosXjornada = new ArrayList<HorariosXdia>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;


            sql = "SELECT     nrd.NUM_RUNID, nrd.STARTTIME, nrd.ENDTIME, nrd.SDAYS, nrd.EDAYS, nrd.SCHCLASSID, nrd.OverTime, nr.UNITS, nr.CYLE, sc.*"
                    + "FROM         NUM_RUN AS nr LEFT JOIN NUM_RUN_DEIL AS nrd ON nr.NUM_RUNID = nrd.NUM_RUNID LEFT JOIN "
                    + "SchClass AS sc ON nrd.SCHCLASSID = sc.schClassid "
                    + "WHERE     (nr.NUM_RUNID = " + jornada_ID + ")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer dia = rs.getInt("sdays");
                Integer diaFim = rs.getInt("edays");

                Integer units = rs.getInt("units");
                Integer horarioId = rs.getInt("schClassID");
                Integer cyle = rs.getInt("cyle");
                Timestamp entrada = rs.getTimestamp("StartTime");
                Timestamp saida = rs.getTimestamp("EndTime");
                String schName = rs.getString("schName");
                Timestamp inicioFaixaEntrada = rs.getTimestamp("CheckInTime1");
                Timestamp fimFaixaEntrada = rs.getTimestamp("CheckInTime2");
                Timestamp inicioFaixaSaida = rs.getTimestamp("CheckOutTime1");
                Timestamp fimFaixaSaida = rs.getTimestamp("CheckOutTime2");

                HorariosXdia hXd = new HorariosXdia(dia, !diaFim.equals(dia), units, horarioId, cyle, entrada, saida, schName, inicioFaixaEntrada, fimFaixaEntrada, inicioFaixaSaida, fimFaixaSaida);

                horariosXjornada.add(hXd);
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


        return horariosXjornada;
    }

    Boolean salvarTurnos(List<DiaJornada> diasJornadaList, Integer jornada_id) {
        boolean flag = true;
        PreparedStatement pstmt = null;

        try {

            String query = "delete from num_run_deil"
                    + " where num_runid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, jornada_id);

            pstmt.executeUpdate();

            for (Integer i = 0; i < diasJornadaList.size(); i++) {
                DiaJornada diaJornada = diasJornadaList.get(i);
                ArrayList<HorariosXdia> horariosJornada = diaJornada.getHxd();
                for (Integer j = 0; j < horariosJornada.size(); j++) {
                    HorariosXdia horariosXdia = horariosJornada.get(j);
                    query = "insert into num_run_deil(NUM_RUNID, STARTTIME, ENDTIME, SDAYS, EDAYS, SCHCLASSID, OverTime) values(?,?,?,?,?,?,?)";

                    pstmt = c.prepareStatement(query);
                    pstmt.setString(1, jornada_id.toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String entrada = sdf.format(horariosXdia.getEntrada());
                    pstmt.setString(2, entrada);
                    String saida = sdf.format(horariosXdia.getSaida());
                    pstmt.setString(3, saida);

                    pstmt.setString(4, diaJornada.getDiaInt().toString());
                    Integer edays = diaJornada.getDiaInt();
                    if (horariosXdia.getEntrada().after(horariosXdia.getSaida()) || horariosXdia.getEntrada().equals(horariosXdia.getSaida())) {
                        edays++;
                    }
                    pstmt.setString(5, edays.toString());
                    pstmt.setString(6, horariosXdia.getHorarioId().toString());
                    pstmt.setString(7, "-1");

                    pstmt.executeUpdate();

                }
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

    public void updateJornada(Integer cod_jornada, Boolean isRegular, Integer responsavel) {

        PreparedStatement pstmt = null;

        try {
            String query = "update num_run SET isregular = ?, responsavel = ? "
                    + "where num_runid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setBoolean(1, isRegular);
            pstmt.setInt(2, responsavel);
            pstmt.setInt(3, cod_jornada);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                System.out.println("updateJornada 1"+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
