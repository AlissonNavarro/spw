package CadastroJornada;

import entidades.Justificativa;
import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;

public class Banco {

    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public int addJornadaCadastro(JornadaCadastro jc) {
        int flag = 0;
        try {
            ResultSet rs;
            String sql;
            sql = "select name from num_run where name = '" + jc.getNome() + "'";
            rs = con.executeQuery(sql);
            while (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "insert into num_run(oldid, name,startdate,enddate, cyle, units ) values(?,?,?,?,?,?)";

                con.prepareStatement(query);
                con.pstmt.setString(1, "-1");
                con.pstmt.setString(2, jc.getNome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dia = sdf.format(jc.getDataInicio());
                con.pstmt.setString(3, dia);
                String sDataFinal = "31/12/2200 00:00:00";
                con.pstmt.setString(4, sDataFinal);
                con.pstmt.setString(5, jc.getQuantidadeCiclos().toString());
                con.pstmt.setString(6, jc.getUnidadeCiclosInt().toString());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<JornadaCadastro> consultaJornadas() {
        List<JornadaCadastro> jornadaCadastroList = new ArrayList<JornadaCadastro>();
        try {
            ResultSet rs;
            String sql;

            sql = "select n.*, u.name as nome from num_run n left join userinfo u "
                    + " on (n.responsavel = u.userid)"
                    + " ORDER BY n.name";

            rs = con.executeQuery(sql);

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

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return jornadaCadastroList;
    }

    public boolean findCronogramas(Integer jornada_id) {
        boolean flag = false;
        try {
            ResultSet rs;
            String sql = "select * from user_of_run where num_of_run_id = " + jornada_id;

            rs = con.executeQuery(sql);

            while (rs.next()) {
                flag = true;
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean deleteJornada(Integer jornada_id) {
        boolean flag = true;
        try {
            String query = "delete from num_run_deil where num_runid = ?";

            con.prepareStatement(query);
            con.pstmt.setInt(1, jornada_id);
            con.executeUpdate();
            query = "delete from num_run where num_runid = ?";

            con.prepareStatement(query);
            con.pstmt.setInt(1, jornada_id);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<Justificativa> consultaJustificativas() {
        List<Justificativa> justificativaList = new ArrayList<Justificativa>();
        try {
            String sql = "select leaveid,leavename from LeaveClass ORDER BY leavename";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                Justificativa justificativa = new Justificativa();
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
                justificativaList.add(justificativa);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return justificativaList;
    }

    public Justificativa consultaJustificativa(Integer justificativa_id) {
        Justificativa justificativa = new Justificativa();
        ResultSet rs;
        try {
            String sql = "select leaveid,leavename from LeaveClass where leaveid = ?";
            con.prepareStatement(sql);
            con.pstmt.setInt(1, justificativa_id);
            rs = con.pstmt.executeQuery();

            while (rs.next()) {
                Integer leaveid = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                justificativa.setJustificativaID(leaveid);
                justificativa.setJustificativaNome(nome);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return justificativa;
    }

    public void editarJustificativa(Justificativa justificativa) {
        try {
            String query = "update LeaveClass SET leaveName = ? "
                    + "where leaveid = ?";
            con.prepareStatement(query);
            con.pstmt.setString(1, justificativa.getJustificativaNome());
            con.pstmt.setInt(2, justificativa.getJustificativaID());
            con.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
    }

    public boolean deletarJustificativa(Integer justificativaID) {
        boolean flag = true;
        try {
            String query = "delete from LeaveClass where leaveid = ?";
            con.prepareStatement(query);
            con.pstmt.setInt(1, justificativaID);
            con.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean inserirNovaJustificativa(Justificativa novaJustificativa) {
        boolean flag = true;
        ResultSet rs;
        try {
            String query = "select leaveName from LeaveClass where leaveName =? ";
            con.prepareStatement(query);
            con.pstmt.setString(1, novaJustificativa.getJustificativaNome());
            rs = con.pstmt.executeQuery();

            if (rs.next()) {
                flag = false;
            }
            if (flag) {
                query = "insert into LeaveClass (leavename)  VALUES (?)";
                con.prepareStatement(query);
                con.pstmt.setString(1, novaJustificativa.getJustificativaNome());
                con.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int editJornada(JornadaCadastro editJornadaCadastro) {
        int flag = 0;
        try {
            ResultSet rs;
            String sql;
            sql = "select name from num_run "
                    + "where name = '" + editJornadaCadastro.getNome() + "' and num_runid <> '" + editJornadaCadastro.getId().toString() + "'";
            rs = con.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "update num_run set oldid = ?, name = ?, startdate = ?, enddate = ?, cyle = ?, units = ?"
                        + " where num_runid = ?";

                con.prepareStatement(query);
                con.pstmt.setString(1, "-1");
                con.pstmt.setString(2, editJornadaCadastro.getNome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dia = sdf.format(editJornadaCadastro.getDataInicio());
                con.pstmt.setString(3, dia);
                String sDataFinal = "31/12/2200 00:00:00";
                con.pstmt.setString(4, sDataFinal);
                con.pstmt.setString(5, editJornadaCadastro.getQuantidadeCiclos().toString());
                con.pstmt.setString(6, editJornadaCadastro.getUnidadeCiclosInt().toString());
                con.pstmt.setString(7, editJornadaCadastro.getId().toString());
                con.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<SelectItem> consultaHorariosSelectItemList() {

        List<SelectItem> horariosSelectItemList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            String sql;
            sql = "select * from schClass ORDER BY schName";
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer schClassID = rs.getInt("schClassID");
                String schName = rs.getString("schName");
                horariosSelectItemList.add(new SelectItem(schClassID, schName));
            }
            rs.close();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return horariosSelectItemList;
    }

    public List<Turno> consultaHorariosList() {
        List<Turno> turnoList = new ArrayList<Turno>();
        try {
            ResultSet rs;
            String sql;
            sql = "select * from schClass where active = 'true' ORDER BY schName";
            rs = con.executeQuery(sql);

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

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return turnoList;
    }

    public ArrayList<HorariosXdia> consultaHorariosXJornada(Integer jornada_ID) {
        ArrayList<HorariosXdia> horariosXjornada = new ArrayList<HorariosXdia>();
        try {
            ResultSet rs;
            String sql;
            sql = "SELECT     nrd.NUM_RUNID, nrd.STARTTIME, nrd.ENDTIME, nrd.SDAYS, nrd.EDAYS, nrd.SCHCLASSID, nrd.OverTime, nr.UNITS, nr.CYLE, sc.*"
                    + "FROM         NUM_RUN AS nr LEFT JOIN NUM_RUN_DEIL AS nrd ON nr.NUM_RUNID = nrd.NUM_RUNID LEFT JOIN "
                    + "SchClass AS sc ON nrd.SCHCLASSID = sc.schClassid "
                    + "WHERE     (nr.NUM_RUNID = " + jornada_ID + ")";

            rs = con.executeQuery(sql);

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
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return horariosXjornada;
    }

    public boolean salvarTurnos(List<DiaJornada> diasJornadaList, Integer jornada_id) {
        boolean flag = true;
        try {
            String query = "delete from num_run_deil where num_runid = ?";
            con.prepareStatement(query);
            con.pstmt.setInt(1, jornada_id);
            con.executeUpdate();

            for (Integer i = 0; i < diasJornadaList.size(); i++) {
                DiaJornada diaJornada = diasJornadaList.get(i);
                ArrayList<HorariosXdia> horariosJornada = diaJornada.getHxd();
                for (Integer j = 0; j < horariosJornada.size(); j++) {
                    HorariosXdia horariosXdia = horariosJornada.get(j);
                    query = "insert into num_run_deil(NUM_RUNID, STARTTIME, ENDTIME, SDAYS, EDAYS, SCHCLASSID, OverTime) values(?,?,?,?,?,?,?)";

                    con.prepareStatement(query);
                    con.pstmt.setString(1, jornada_id.toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String entrada = sdf.format(horariosXdia.getEntrada());
                    con.pstmt.setString(2, entrada);
                    String saida = sdf.format(horariosXdia.getSaida());
                    con.pstmt.setString(3, saida);

                    con.pstmt.setString(4, diaJornada.getDiaInt().toString());
                    Integer edays = diaJornada.getDiaInt();
                    if (horariosXdia.getEntrada().after(horariosXdia.getSaida()) || horariosXdia.getEntrada().equals(horariosXdia.getSaida())) {
                        edays++;
                    }
                    con.pstmt.setString(5, edays.toString());
                    con.pstmt.setString(6, horariosXdia.getHorarioId().toString());
                    con.pstmt.setString(7, "-1");

                    con.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public void updateJornada(Integer cod_jornada, Boolean isRegular, Integer responsavel) {
        try {
            String query = "update num_run SET isregular = ?, responsavel = ? "
                    + "where num_runid = ?";

            con.prepareStatement(query);
            con.pstmt.setBoolean(1, isRegular);
            con.pstmt.setInt(2, responsavel);
            con.pstmt.setInt(3, cod_jornada);
            con.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
    }
}
