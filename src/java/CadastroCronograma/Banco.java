/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroCronograma;

import CadastroJornada.HorariosXdia;
import CadastroJornada.Turno;
import ConsultaPonto.DescolamentoTemporario;
import ConsultaPonto.Escala;
import Funcionario.Funcionario;
import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;

/**
 *
 * @author Amvboas
 */
class Banco {

    Driver d;
    Connection c;
    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("CadastroCronograma: Conectar: " + cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
        }
    }

    private Integer getEspace(Integer depId, List<Integer> idList, HashMap<Integer, Integer> idToSuperHash) {

        Integer espaco = 0;
        Integer superId = idToSuperHash.get(depId);

        if (idList.contains(superId)) {
            espaco++;
            return espaco += getEspace(superId, idList, idToSuperHash);
        } else {
            return espaco;
        }
    }

    private static String espaces(Integer qnt) {

        String nbsp = "";
        String nbsp_ = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

        for (int i = 1; i
                <= qnt; i++) {
            nbsp += nbsp_;
        }
        return nbsp;
    }

    private List<Integer> getDeptPermitidos(Integer raiz, List<Integer> deptPermitidos, HashMap<Integer, Integer> idToSuperHash) {

        deptPermitidos.add(raiz);

        while (getFilhos(raiz, deptPermitidos, idToSuperHash) != null) {

            Integer filho = getFilhos(raiz, deptPermitidos, idToSuperHash);

            if (filho != null) {
                getDeptPermitidos(filho, deptPermitidos, idToSuperHash);
            }
        }
        return deptPermitidos;
    }

    private Integer getFilhos(Integer raiz, List<Integer> deptPermitidos, HashMap<Integer, Integer> idToSuperHash) {
        Set<Integer> chaves = idToSuperHash.keySet();

        for (Iterator<Integer> iterator = chaves.iterator(); iterator.hasNext();) {
            Integer chave = iterator.next();
            if (raiz.equals(idToSuperHash.get(chave)) && !deptPermitidos.contains(chave)) {
                return chave;
            }
        }
        return null;
    }

    private List<Integer> ordenarDepts(Integer raiz, List<SelectItem> list) {

        while (!list.isEmpty()) {

            if (!deptList.contains(raiz)) {
                deptList.add(raiz);
            }

            Integer depFilho = getFilho(raiz, list);

            if (depFilho != null) {
                List<SelectItem> filhosList = getTodosFilhosList(depFilho, list);

                if (filhosList.isEmpty()) {
                    if (!deptList.contains(depFilho)) {
                        deptList.add(depFilho);
                    }
                } else {
                    ordenarDepts(depFilho, list);
                }
            } else {
                list = remover(raiz, list);
                break;
            }
        }
        return deptList;
    }

    private List<SelectItem> remover(Integer raiz, List<SelectItem> list) {
        List<SelectItem> listSaida = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = list.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();

            if (!raiz.equals(selectItem.getValue())) {
                listSaida.add(selectItem);
            }
        }
        return listSaida;
    }

    private List<SelectItem> getTodosFilhosList(Integer dep, List<SelectItem> fila) {

        List<SelectItem> list = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = fila.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            Integer super_dept = Integer.parseInt(selectItem.getLabel());

            if (super_dept.equals((dep))) {
                list.add(selectItem);
            }
        }
        return list;
    }

    private Integer getFilho(Integer dep, List<SelectItem> fila) {

        Integer saida = null;

        for (Iterator<SelectItem> it = fila.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            Integer super_dept = Integer.parseInt(selectItem.getLabel());
            Integer id_dept = Integer.parseInt(selectItem.getValue().toString());

            if (super_dept.equals(dep)) {
                saida = id_dept;
                fila.remove(selectItem);
                break;
            }
        }
        return saida;
    }

    public String getNomeFuncionario(int cod_funcionario) {
        String retorno = "";
        ResultSet rs = null;
        Statement stmt = null;

        String sql = "select name from userinfo where userid = " + cod_funcionario;

        try {
            Conectar();
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                retorno = rs.getString("name");
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
        return retorno;
    }

    public List<SelectItem> consultaFuncionarioTotal(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        List<Integer> deptIDList;

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dep);

                HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                stmt.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dep, deptPermitidos, idToSuperHash);

                sql = "select u.userid,u.name,u.defaultdeptid"
                        + " from  USERINFO u "
                        + " ORDER BY u.name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }

            } else {
                sql = "select u.userid,u.name"
                        + " from  USERINFO u "
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY u.name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    userList.add(new SelectItem(userid, name));
                }
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
        return userList;
    }

    public List<Cronograma> consultaCronogramas(Integer cod_funcionario) {
        List<Cronograma> cronogramasList = new ArrayList<Cronograma>();
        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "SELECT uor.USERID, uor.NUM_OF_RUN_ID, uor.STARTDATE, uor.ENDDATE, nr.NAME "
                    + "FROM USER_OF_RUN AS uor LEFT OUTER JOIN "
                    + "NUM_RUN AS nr ON nr.NUM_RUNID = uor.NUM_OF_RUN_ID "
                    + "WHERE (uor.USERID = " + cod_funcionario + ")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("USERID");
                Integer jornadaid = rs.getInt("NUM_OF_RUN_ID");
                Date inicio = rs.getDate("STARTDATE");
                Date fim = rs.getDate("ENDDATE");
                String nomeJornada = rs.getString("NAME");

                Cronograma cronograma = new Cronograma(userid, jornadaid, nomeJornada, inicio, fim);
                cronogramasList.add(cronograma);
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

        return cronogramasList;
    }

    ArrayList<Horario> consultaHorariosDia(Integer cod_funcionario, Escala escala) {
        ArrayList<Horario> horariosList = new ArrayList<Horario>();

        try {
            ResultSet rs = null;
            Statement stmt = null;
            for (int i = 0; i < escala.getSchClassIDList().size(); i++) {

                Integer horarioID = escala.getSchClassIDList().get(i);
                String sql;

                sql = "SELECT schClassid, schName, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend"
                        + " FROM         SchClass WHERE     (schClassid = " + horarioID + ") and active = 'true'";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    //
                    String schName = rs.getString("schName");
                    Date StartTime = rs.getTimestamp("StartTime");
                    Date EndTime = rs.getTimestamp("EndTime");
                    Integer CheckIn = rs.getInt("CheckIn");
                    Integer CheckOut = rs.getInt("CheckOut");
                    Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                    Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                    Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                    Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                    String Legend = rs.getString("Legend");

                    Boolean overTime = getOverTime(cod_funcionario, escala.getDia(), horarioID);

                    Horario horario = new Horario(schName, horarioID, StartTime, EndTime,
                            CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend, overTime);

                    horario.setFazParteEscala(true);

                    //Testando se o horário é temporário
                    DescolamentoTemporario deslTemp = escala.getDeslT();
                    horariosList.add(horario);

                }
                //public Horario(Integer horarioId, Date entrada, Date saida, Integer CheckIn, Integer CheckOut, Date inicioFaixaEntrada, Date fimFaixaEntrada, Date inicioFaixaSaida, Date fimFaixaSaida, String Legend)

            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return horariosList;
    }

    public ArrayList<Horario> consultaHorariosDiaPorSchClassId(Integer cod_funcionario, int horarioID) {

        ArrayList<Horario> horariosList = new ArrayList<Horario>();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            sql = "SELECT schClassid, schName, StartTime, EndTime, CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend"
                    + " FROM         SchClass WHERE     (schClassid = " + horarioID + ") and active = 'true'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //
                String schName = rs.getString("schName");
                Date StartTime = rs.getTimestamp("StartTime");
                Date EndTime = rs.getTimestamp("EndTime");
                Integer CheckIn = rs.getInt("CheckIn");
                Integer CheckOut = rs.getInt("CheckOut");
                Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                String Legend = rs.getString("Legend");
                Boolean overTime = false;

                Horario horario = new Horario(schName, horarioID, StartTime, EndTime,
                        CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend, overTime);
                horariosList.add(horario);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
        return horariosList;
    }

    public ArrayList<Horario> consultaHorariosPorFuncionario(Integer cod_funcionario, Date dia) {

        ArrayList<Horario> horariosList = new ArrayList<Horario>();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(dia.getTime());

            //sql = "select * from SCHCLASS s, (select nr.SCHCLASSID from user_of_run u, num_run n, NUM_RUN_DEIL nr "
            //      + "where u.NUM_OF_RUN_ID = n.NUM_RUNID and nr.NUM_RUNID = n.NUM_RUNID and USERID="+cod_funcionario+") t where s.SCHCLASSID = t.SCHCLASSID";
            sql = "select * from SCHCLASS s, (select nr.SCHCLASSID, nr.SDAYS, nr.EDAYS "
                    + " from user_of_run u, num_run n, NUM_RUN_DEIL nr where u.NUM_OF_RUN_ID = n.NUM_RUNID and 	nr.NUM_RUNID = n.NUM_RUNID"
                    + " and u.USERID=" + cod_funcionario + " and '" + sdf.format(dia) + " 00:00:00' between u.STARTDATE and u.ENDDATE) t where s.SCHCLASSID = t.SCHCLASSID and t.sdays = " + (gc.get(Calendar.DAY_OF_WEEK) - 1);
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //
                String schName = rs.getString("schName");
                Integer schClassid = rs.getInt("schClassid");
                Date StartTime = rs.getTimestamp("StartTime");
                Date EndTime = rs.getTimestamp("EndTime");
                Integer CheckIn = rs.getInt("CheckIn");
                Integer CheckOut = rs.getInt("CheckOut");
                Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                String Legend = rs.getString("Legend");
                Boolean overTime = false;

                Horario horario = new Horario(schName, schClassid, StartTime, EndTime,
                        CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend, overTime);
                horariosList.add(horario);
            }

            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return horariosList;
    }

    public ArrayList<Horario> consultaHorariosFuncionarioPorID(Integer cod_funcionario, List<Integer> schclass) {

        ArrayList<Horario> horariosList = new ArrayList<Horario>();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            for (int x = 0; x < schclass.size(); x++) {

                sql = "select * from SCHCLASS s, (select nr.SCHCLASSID, nr.SDAYS, nr.EDAYS "
                        + " from user_of_run u, num_run n, NUM_RUN_DEIL nr where u.NUM_OF_RUN_ID = n.NUM_RUNID and 	nr.NUM_RUNID = n.NUM_RUNID"
                        + " and u.USERID=" + cod_funcionario + ") t where s.SCHCLASSID = t.SCHCLASSID and s.schclassid = " + schclass.get(x).intValue();
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    //
                    String schName = rs.getString("schName");
                    Integer schClassid = rs.getInt("schClassid");
                    Date StartTime = rs.getTimestamp("StartTime");
                    Date EndTime = rs.getTimestamp("EndTime");
                    Integer CheckIn = rs.getInt("CheckIn");
                    Integer CheckOut = rs.getInt("CheckOut");
                    Date CheckInTime1 = rs.getTimestamp("CheckInTime1");
                    Date CheckInTime2 = rs.getTimestamp("CheckInTime2");
                    Date CheckOutTime1 = rs.getTimestamp("CheckOutTime1");
                    Date CheckOutTime2 = rs.getTimestamp("CheckOutTime2");
                    String Legend = rs.getString("Legend");
                    Boolean overTime = false;

                    Horario horario = new Horario(schName, schClassid, StartTime, EndTime,
                            CheckIn, CheckOut, CheckInTime1, CheckInTime2, CheckOutTime1, CheckOutTime2, Legend, overTime);
                    horariosList.add(horario);
                }
            }

            rs.close();
            stmt.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return horariosList;
    }

    public Boolean getOverTime(Integer cod_funcionario, Date dia, Integer horarioID) {

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String diaStr = sdfHora.format(dia.getTime());
        Boolean overTime = false;
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String sql = "select * from user_temp_sch where userid = " + cod_funcionario + " and "
                    + "convert(nvarchar(10), cometime, 103) = '" + diaStr + "' and schclassid = " + horarioID;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                overTime = (rs.getInt("overtime") == 1) ? true : false;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return overTime;
    }

    public void fechaConexao() {
        try {
            if (c != null) {
                c.close();
            }

        } catch (Exception e) {
        }
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

    boolean salvarTurnos(List<DiaCronograma> diasCronogramaList, Integer cod_funcionario, Date dataInicio, Date dataFim) {
        boolean flag = true;
        PreparedStatement pstmt = null;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdfHora.format(dataInicio.getTime());
        String fimStr = sdfHora.format(dataFim.getTime() + 86399999);
        String query;
        try {

            for (Integer i = 0; i < diasCronogramaList.size(); i++) {
                DiaCronograma diaCronograma = diasCronogramaList.get(i);
                ArrayList<Horario> horariosList = diaCronograma.getHorariosList();

                query = "delete from user_temp_sch where userid=? and cometime between ? and ?";
                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_funcionario);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                pstmt.setString(2, sdf.format(diaCronograma.getDia()) + " 00:00:00");
                pstmt.setString(3, sdf.format(diaCronograma.getDia()) + " 23:59:59");
                pstmt.executeUpdate();

                if (diaCronograma.isRetirarHorarioDoDia() && diaCronograma.getAcao() != 3) {
                    query = "insert into USER_TEMP_SCH(USERID, COMETIME, LEAVETIME, OVERTIME, TYPE, FLAG, SCHCLASSID, FOLGA) values(?,?,?,?,?,?,?,?)";
                    pstmt = c.prepareStatement(query);
                    pstmt.setInt(1, cod_funcionario);
                    //    Integer desloc = diaCronograma.getOrdem_dia() * 86400000;

                    Date data = new Date(43200000 + diaCronograma.getDia().getTime());
                    String dataStr = sdfHora.format(data);
                    pstmt.setString(2, dataStr);
                    pstmt.setString(3, dataStr);
                    pstmt.setInt(4, 0);
                    pstmt.setInt(5, 0);
                    pstmt.setInt(6, 1);
                    pstmt.setString(7, "-1");
                    pstmt.setBoolean(8, diaCronograma.getIsFolga());
                    pstmt.executeUpdate();
                }

                if (diaCronograma.getAcao() == 1 || diaCronograma.getAcao() == 4) {

                    for (Integer j = 0; j < horariosList.size(); j++) {

                        Horario horario = horariosList.get(j);

                        //verifica se o horario nao é temporario ou se o horario é um folga
                        //sendo folga cadastra, caso nao, so cadastrar se nao fizer parte da escala
                        if (!horario.isFazParteEscala() || diaCronograma.getAcao() == 1) {
                            query = "insert into USER_TEMP_SCH(USERID, COMETIME, LEAVETIME, OVERTIME, TYPE, FLAG, SCHCLASSID, FOLGA) values(?,?,?,?,?,?,?,?)";
                            pstmt = c.prepareStatement(query);
                            pstmt.setInt(1, cod_funcionario);

                            SimpleDateFormat sdfSODia = new SimpleDateFormat("dd/MM/yyyy");
                            String dia = sdfSODia.format(diaCronograma.getDia().getTime());
                            SimpleDateFormat sdfSOHora = new SimpleDateFormat("HH:mm:ss");
                            String entradaHora = sdfSOHora.format(horario.getEntrada());
                            String saidaHora = sdfSOHora.format(horario.getSaida());
                            pstmt.setString(2, dia + " " + entradaHora);

                            if (horario.getEntrada().after(horario.getSaida()) || horario.getEntrada().equals(horario.getSaida())) {
                                GregorianCalendar g = new GregorianCalendar();
                                g.setTime(diaCronograma.getDia());
                                g.add(Calendar.DAY_OF_MONTH, 1);
                                dia = sdfSODia.format(g.getTime().getTime());
                            }
                            pstmt.setString(3, dia + " " + saidaHora);
                            Integer over = (horario.isIsOverTime()) ? 1 : 0;
                            pstmt.setInt(4, over);
                            pstmt.setInt(5, 0);
                            pstmt.setInt(6, 1);
                            pstmt.setInt(7, horario.getHorarioId());
                            pstmt.setBoolean(8, diaCronograma.getIsFolga());
                            pstmt.executeUpdate();
                        }
                    }

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

    boolean excluirDeslocTempTurnos(List<DiaCronograma> diasCronogramaList, Integer cod_funcionario, Date dataInicio, Date dataFim, Boolean overtime) {
        boolean flag = true;
        PreparedStatement pstmt = null;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdfHora.format(dataInicio.getTime());
        String fimStr = sdfHora.format(dataFim.getTime() + 86399999);

        try {

            String query = "DELETE FROM USER_TEMP_SCH "
                    + "WHERE USERID = ? AND COMETIME BETWEEN ? AND ? ";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_funcionario);
            pstmt.setString(2, inicioStr);
            pstmt.setString(3, fimStr);

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

    private Long horaToLong(String hora) {
        Long saida = null;
        try {
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }
        return saida;
    }

    public List<SelectItem> geraJornadasList() {
        List<SelectItem> jornadasList = new ArrayList<SelectItem>();
        List<Integer> deptIDList;

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            sql = "SELECT NUM_RUNID, NAME "
                    + "FROM NUM_RUN ORDER BY NAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            jornadasList.add(new SelectItem(0, "Selecione uma Jornada"));

            while (rs.next()) {
                Integer userid = rs.getInt("NUM_RUNID");
                String name = rs.getString("NAME");
                jornadasList.add(new SelectItem(userid, name));
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
        return jornadasList;
    }

    public Boolean addJornada(Integer cod_funcionario, Integer jornadaSelecionada, Date dataInicioJornada, Date dataFimJornada) {
        boolean flag = true;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String inicioStr = sdfHora.format(dataInicioJornada.getTime());
        String fimStr = sdfHora.format(dataFimJornada.getTime());
        PreparedStatement pstmt = null;

        try {

            String query = "insert into USER_OF_RUN(USERID, NUM_OF_RUN_ID, STARTDATE, ENDDATE,"
                    + " ISNOTOF_RUN, ORDER_RUN) values(?,?,?,?,?,?)";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_funcionario);
            pstmt.setInt(2, jornadaSelecionada);
            pstmt.setString(3, inicioStr);
            pstmt.setString(4, fimStr);
            pstmt.setInt(5, 0);
            pstmt.setInt(6, 0);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.getMessage();
            flag = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
            return flag;
        }

    }

    public Boolean inserirJornadasGrupo(List<Jornada> jornadaList) {
        boolean flag = true;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        PreparedStatement pstmt = null;
        try {
            String queryInsert;
            String queryUpdate;
            String queryDelete;
            c.setAutoCommit(false);
            queryInsert = "insert into USER_OF_RUN(USERID, NUM_OF_RUN_ID, STARTDATE, ENDDATE,"
                    + " ISNOTOF_RUN, ORDER_RUN) values(?,?,?,?,?,?)";

            queryUpdate = "update USER_OF_RUN set NUM_OF_RUN_ID=?,STARTDATE=?,ENDDATE=? "
                    + " where userid=? and STARTDATE=?";

            queryDelete = "delete from USER_OF_RUN"
                    + " where (USERID = ?) and  (NUM_OF_RUN_ID = ?) and (STARTDATE = ?)";
            for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
                Jornada jornada = it.next();

                if (jornada.getFlag().equals(1)) {
                    pstmt = c.prepareStatement(queryInsert);
                    pstmt.setInt(1, jornada.getCod_funcionario());
                    pstmt.setInt(2, jornada.getCod_jornada());
                    pstmt.setString(3, sdfHora.format(jornada.getInicioNovo()));
                    pstmt.setString(4, sdfHora.format(jornada.getFimNovo()));
                    pstmt.setInt(5, 0);
                    pstmt.setInt(6, 0);
                    pstmt.executeUpdate();
                } else if (jornada.getFlag().equals(2)) {
                    pstmt = c.prepareStatement(queryUpdate);

                    pstmt.setInt(1, jornada.getCod_jornada());
                    pstmt.setString(2, sdfHora.format(jornada.getInicioNovo()));
                    pstmt.setString(3, sdfHora.format(jornada.getFimNovo()));
                    pstmt.setInt(4, jornada.getCod_funcionario());
                    pstmt.setString(5, sdfHora.format(jornada.getInicioAntigo()));
                    pstmt.executeUpdate();
                } else if (jornada.getFlag().equals(3)) {
                    pstmt = c.prepareStatement(queryDelete);
                    pstmt.setInt(1, jornada.getCod_funcionario());
                    pstmt.setInt(2, jornada.getCod_jornada());
                    pstmt.setString(3, sdfHora.format(jornada.getInicioAntigo()));
                    pstmt.executeUpdate();
                }

            }
            c.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = false;
            c.rollback();
        } finally {
            return flag;
        }

    }

    public List<Jornada> getJornadas(List<SelectItem> funcionarioList) {
        List<Jornada> jornadaList = new ArrayList<Jornada>();
        Statement pstmt = null;
        ResultSet rs = null;

        try {

            String query = "select ur.*,u.userid as useridUserInfo, u.badgenumber,u.name as nomeFuncionario,nr.name as nomeJornada from USER_OF_RUN ur, userinfo u, num_run nr "
                    + "where u.userid = ur.userid and nr.num_runid = ur.num_of_run_id order by u.name,ur.userid,ur.startdate";

            pstmt = c.createStatement();
            rs = pstmt.executeQuery(query);

            while (rs.next()) {
                Integer cod_funcionario = rs.getInt("useridUserInfo");
                if (containsFuncionario(funcionarioList, cod_funcionario.toString())) {
                    Integer cod_jornada_ = rs.getInt("num_of_run_id");
                    Date inicio = rs.getDate("startdate");
                    Date fim = rs.getDate("enddate");
                    String matricula = rs.getString("badgenumber");
                    String nomeFuncionario = rs.getString("nomeFuncionario");
                    String nomeJornada = rs.getString("nomeJornada");
                    Jornada jornada_ = new Jornada(cod_funcionario, cod_jornada_, inicio, fim, 2);
                    jornada_.setMatricula(matricula);
                    jornada_.setNomeFuncionario(nomeFuncionario);
                    jornada_.setNomeJornada(nomeJornada);
                    jornadaList.add(jornada_);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return jornadaList;
        }
    }

    public List<Funcionario> getFuncionarios() {
        List<Funcionario> funcionarioList = new ArrayList<Funcionario>();
        Statement pstmt = null;
        ResultSet rs = null;

        try {

            String query = "select name,userid,badgenumber from userinfo ";

            pstmt = c.createStatement();
            rs = pstmt.executeQuery(query);

            while (rs.next()) {
                Integer cod_funcionario = rs.getInt("userid");
                String nome = rs.getString("name");
                Funcionario f = new Funcionario();
                f.setNome(nome);
                f.setFuncionarioId(cod_funcionario);
                funcionarioList.add(f);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return funcionarioList;
    }

    public Boolean deletarJornada(String cod_funcionario, String cod_jornada, String dataInicio) {
        PreparedStatement pstmt = null;
        Boolean flag = true;
        try {

            String query = "delete from user_of_run"
                    + " where (USERID = ?) and  (NUM_OF_RUN_ID = ?) and (STARTDATE = ? )";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, cod_funcionario);
            pstmt.setString(2, cod_jornada);
            pstmt.setString(3, dataInicio);

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

    public Boolean deletarTodasJornadas(List<Jornada> jornadaList) {
        PreparedStatement pstmt = null;
        Boolean flag = true;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");

        try {
            c.setAutoCommit(false);
            for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
                Jornada jornada = it.next();

                String query = "delete from user_of_run"
                        + " where (USERID = ?) and  (NUM_OF_RUN_ID = ?) and (STARTDATE = ? )";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, jornada.getCod_funcionario());
                pstmt.setInt(2, jornada.getCod_jornada());
                pstmt.setString(3, sdfHora.format(jornada.getInicioAntigo()));

                pstmt.executeUpdate();

            }
            c.commit();
        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("CadastroCronograma: deletarTodasJornadas: " + ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private Boolean containsFuncionario(List<SelectItem> funcionarioList, String funcionario) {
        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            if (selectItem.getValue().equals(funcionario)) {
                return true;
            }
        }
        return false;
    }

    boolean deleteCronograma(Cronograma cronograma) {
        boolean flag = true;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdfHora.format(cronograma.getInicio().getTime());
        String fimStr = sdfHora.format(cronograma.getFim().getTime());
        PreparedStatement pstmt = null;

        try {

            String query = "delete from user_of_run"
                    + " where (USERID = ?) and  (NUM_OF_RUN_ID = ?) and (STARTDATE = ? ) and (ENDDATE= ? )";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cronograma.id_usuario);
            pstmt.setInt(2, cronograma.id_jornada);
            pstmt.setString(3, inicioStr);
            pstmt.setString(4, fimStr);

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

    public List<SelectItem> consultaDepartamentoHierarquico(Integer codigo_funcionario, Integer departamento, Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        Boolean isAdministradorVisivel = true;

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            stmt.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(permissao, deptPermitidos, idToSuperHash);

            idToSuperHash = new HashMap<Integer, Integer>();

            //Selecionando o departamento do administrador
            sql = "select u.DEFAULTDEPTID,d.deptname from USERINFO u, DEPARTMENTS d"
                    + " where u.userid = " + codigo_funcionario + " and "
                    + " u.DEFAULTDEPTID = d.deptid ";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            Integer dept = -1;
            String nome_dept = "";

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
                nome_dept = rs.getString("DEPTNAME");
            }
            rs.close();
            stmt.close();

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer deptID = rs.getInt("DEPTID");
                String nome = rs.getString("DEPTNAME");
                Integer supdeptid = rs.getInt("supdeptid");

                if (deptIDList.contains(deptID)) {
                    deptOrdersList.add(new SelectItem(deptID, supdeptid.toString()));
                    idToSuperHash.put(deptID, supdeptid);
                    idList.add(deptID);
                    idToNomeHash.put(deptID, nome);

                } else if (dept == deptID) {
                    isAdministradorVisivel = new Boolean(false);
                }
            }
            rs.close();
            stmt.close();

            List<Integer> deptSrtList = ordenarDepts(permissao, deptOrdersList);
            saida.add(new SelectItem("-1", "Selecione um departamento"));

            if (!isAdministradorVisivel) {
                saida.add(new SelectItem(dept, nome_dept));
            }

            for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
                Integer id_dept = it.next();
                Integer espaces = getEspace(id_dept, idList, idToSuperHash);
                saida.add(new SelectItem(id_dept, espaces(espaces) + idToNomeHash.get(id_dept), "", false, false));
                //        System.out.print(espaces(espaces) + idToNomeHash.get(id_dept));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return saida;
    }

    public Boolean isAdministradorVisivel(Integer codigo_funcionario, Integer departamento, Integer permissao) {

        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();
        Boolean isAdministradorVisivel = new Boolean(true);

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            stmt.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(permissao, deptPermitidos, idToSuperHash);

            //Selecionando o departamento do administrador
            sql = "select DEFAULTDEPTID from USERINFO"
                    + " where userid = " + codigo_funcionario;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            Integer dept = -1;

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
            }
            rs.close();
            stmt.close();

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer deptID = rs.getInt("DEPTID");
                String nome = rs.getString("DEPTNAME");
                Integer supdeptid = rs.getInt("supdeptid");

                if (deptIDList.contains(deptID)) {
                    deptOrdersList.add(new SelectItem(deptID, supdeptid.toString()));
                    idToSuperHash.put(deptID, supdeptid);
                    idList.add(deptID);
                    idToNomeHash.put(deptID, nome);
                } else if (dept == deptID) {
                    isAdministradorVisivel = new Boolean(false);
                }
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
        return isAdministradorVisivel;
    }

    public List<SelectItem> consultaFuncionario(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;
        List<String> cpfDuplicados = getCPFsDuplicados();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dep);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                stmt.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dep, deptPermitidos, idToSuperHash);

                sql = "select distinct u.userid,u.name,u.defaultdeptid,u.badgenumber,u.ssn"
                        + " from USERINFO u"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        if (cpfDuplicados.contains(ssn)) {
                            name += " - " + badgenumber;
                        }
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    String name = rs.getString("name");
                    if (cpfDuplicados.contains(ssn)) {
                        name += " - " + badgenumber;
                    }
                    userList.add(new SelectItem(userid, name));
                }
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("Erro consulta funcionário" + e);
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return userList;
    }

    //Lista de pessoas com duplo vínculo
    private List<String> getCPFsDuplicados() {

        List<String> cpfList = new ArrayList<String>();
        try {
            ResultSet rs = null;
            Statement stmt = null;
            String sql = "SELECT distinct ssn FROM userinfo u1"
                    + "	WHERE (SELECT count(*) FROM userinfo u2	WHERE u2.ssn = u1.ssn) > 1 order by ssn";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String cpf = rs.getString("ssn");
                cpfList.add(cpf);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cpfList;
    }

    boolean inserirRelatorioIndividual(List<DiaCronograma> diasCronogramaList) {
        boolean flag = true;
        PreparedStatement pstmt = null;
        String queryDelete = "delete from RelatorioEscalaIndividual";

        try {
            pstmt = c.prepareStatement(queryDelete);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
        }
        try {
            for (Integer i = 0; i < diasCronogramaList.size(); i++) {
                DiaCronograma diaCronograma = diasCronogramaList.get(i);
                String query = "insert into RelatorioEscalaIndividual(dia,horario) values(?,?)";
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, diaCronograma.getDiaString());
                pstmt.setString(2, diaCronograma.getHorarios());
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

    public List<SelectItem> consultaFuncionarioProprioAdministrador(Integer codigo_usuario) {

        List<SelectItem> userList = new ArrayList<SelectItem>();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            sql = "select name from USERINFO "
                    + " where userid = " + codigo_usuario;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            userList.add(new SelectItem(-1, "Selecione um funcionário"));

            while (rs.next()) {
                String name = rs.getString("name");
                userList.add(new SelectItem(codigo_usuario, name));
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
        return userList;
    }

    public List<SelectItem> getRegimeSelectItem() {

        List<SelectItem> regimeList = new ArrayList<SelectItem>();
        PreparedStatement pstmt = null;
        ResultSet rs;
        regimeList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from regime_HoraExtra";

            pstmt = c.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer cod_regime = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                regimeList.add(new SelectItem(cod_regime, nome));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return regimeList;

    }
    
    public List<SelectItem> getCargoSelectItem() {
        List<SelectItem> cargoList = new ArrayList<SelectItem>();
        PreparedStatement pstmt = null;
        ResultSet rs;
        cargoList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from cargo";
            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer cargo = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                cargoList.add(new SelectItem(cargo, nome));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return cargoList;
    }

    public HashMap<Integer, Integer> getcod_funcionarioRegime() {

        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        PreparedStatement pstmt = null;
        ResultSet rs;

        try {
            String query = "select userid,cod_regime from userinfo";

            pstmt = c.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cod_regime = rs.getInt("cod_regime");
                cod_funcionarioRegimeHashMap.put(userid, cod_regime);
            }

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
        return cod_funcionarioRegimeHashMap;

    }
    
    public HashMap<Integer, Integer> getcod_funcionarioCargo() {
        HashMap<Integer, Integer> cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        PreparedStatement pstmt = null;
        ResultSet rs;
        //System.out.println("pesquisando getcod_funcionarioCargo");
        try {
            if (c.isClosed()) {
                Conectar();
            }
            String query = "select userid,CARGO from userinfo";
            pstmt = c.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cargo = rs.getInt("CARGO");
                cod_funcionarioCargoHashMap.put(userid, cargo);
                //System.out.println("funcCargo: " + userid + ", " + cargo);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return cod_funcionarioCargoHashMap;

    }

    public HashMap<Integer, Integer> getcod_funcionarioSubordinacaoDepartamento(Integer dept) {

        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        PreparedStatement pstmt = null;
        ResultSet rs;

        HashMap<Integer, Integer> idToSuperHash = getHierarquiaDepartamentos();

        try {
            String query = "select userid,defaultdeptid,permissao from userinfo ";

            pstmt = c.prepareStatement(query);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer dept_ = rs.getInt("defaultdeptid");
                Integer permissao = rs.getInt("permissao");
                if (permissao != 0) {
                    Integer deptPai = idToSuperHash.get(dept_);
                    if (dept.equals(deptPai)) {
                        cod_funcionarioRegimeHashMap.put(userid, 1);
                    } else {
                        cod_funcionarioRegimeHashMap.put(userid, 0);
                    }
                } else {
                    cod_funcionarioRegimeHashMap.put(userid, -1);
                }
            }

        } catch (Exception e) {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ex) {
                }
            }
        }
        return cod_funcionarioRegimeHashMap;

    }

    public HashMap<Integer, Integer> getHierarquiaDepartamentos() {

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
        }
        return idToSuperHash;
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

                Timestamp inicioPrimeiroDescanso = rs.getTimestamp("restInTime1");
                Timestamp fimPrimeiroDescanso = rs.getTimestamp("restOutTime1");
                Timestamp inicioInterJornada = rs.getTimestamp("InterRestIn");
                Timestamp fimInterJornada = rs.getTimestamp("InterRestOut");
                Timestamp inicioSegundoDescanso = rs.getTimestamp("restInTime2");
                Timestamp fimSegundoDescanso = rs.getTimestamp("restOutTime2");

                HorariosXdia hXd = new HorariosXdia(dia, !diaFim.equals(dia), units, horarioId, cyle, entrada, saida, schName,
                        inicioFaixaEntrada, fimFaixaEntrada, inicioFaixaSaida, fimFaixaSaida,
                        inicioPrimeiroDescanso, fimPrimeiroDescanso, inicioSegundoDescanso,
                        fimSegundoDescanso, inicioInterJornada, fimInterJornada);

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
}
