package ConsultaPonto;

import Metodos.Metodos;
import Abono.Abono;
import Abono.SolicitacaoAbono;
import entidades.Afastamento;
import CadastroHoraExtra.DetalheGratificacao;
import CadastroHoraExtra.DetalheHoraExtra;
import CadastroHoraExtra.Gratificacao;
import CadastroHoraExtra.RegimeHoraExtra;
import CadastroHoraExtra.TipoHoraExtra;
import Funcionario.Funcionario;
import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.faces.model.SelectItem;

public class Banco {

    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public List<SolicitacaoAbono> consultaSolicitacaoAbonoFuncionario(Integer cod, String dat) {
        List<SolicitacaoAbono> solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        try {
            String query = "select * from SolicitacaoAbono sa"
                    + " where userid = " + cod + " and"
                    + " Data = '" + dat + "' "
                    + " order by inclusao";
            ResultSet rs = con.executeQuery(query);
            SolicitacaoAbono solicitacaoAbono = new SolicitacaoAbono();

            while (rs.next()) {
                solicitacaoAbono = new SolicitacaoAbono();

                Integer codigo = rs.getInt("Codigo");
                java.sql.Date data = rs.getDate("data");
                String entrada1 = rs.getString("entrada1");
                String saida1 = rs.getString("saida1");
                String entrada2 = rs.getString("entrada2");
                String saida2 = rs.getString("saida2");
                String descricao = rs.getString("descricao");
                String status = rs.getString("status");
                java.sql.Date inclusao = rs.getDate("Inclusao");
                String resposta = rs.getString("Resposta");

                solicitacaoAbono.setCod(codigo);
                solicitacaoAbono.setData(data);
                solicitacaoAbono.setEntrada1(entrada1);
                solicitacaoAbono.setEntrada2(entrada2);
                solicitacaoAbono.setSaida1(saida1);
                solicitacaoAbono.setSaida2(saida2);
                solicitacaoAbono.setDescricao(descricao);
                solicitacaoAbono.setStatus(status);
                solicitacaoAbono.setInclusao(inclusao);
                solicitacaoAbono.setRespostaJustificativa(resposta);
                solicitacaoAbonoList.add(solicitacaoAbono);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return solicitacaoAbonoList;
    }

    public boolean deleteSolicitacaoAbono(Integer cod_solicitacao) {
        boolean flag = false;
        String queryDelete = "delete from SolicitacaoAbono where codigo = " + cod_solicitacao;
        flag = con.executeUpdate(queryDelete) > 0;
        con.Desconectar();
        return flag;
    }

    public List<String> consultaChechInOut(Integer userid, String diaParam) {

        List<String> diaPontoList = new ArrayList<String>();
        try {
            String sql = "select c.checktime from" + " checkinout c,userinfo u" + " where " + " c.userid = u.userid and" + " " + "c.userid = " + userid.toString();
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dia = sdf.format(timestamp.getTime());

                if (Metodos.formatarDataSemHora(diaParam).equals(dia)) {
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                    String hora = sdf2.format(timestamp.getTime());
                    diaPontoList.add(hora);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diaPontoList;
    }

    public List<String> consultaDiasPontoBatido(Integer userid, Date inicio, Date fim) {

        List<String> diaPontoList = new ArrayList<String>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select checktime from checkinout "
                    + "where userid = " + userid + " and checktime between '" + inicioStr + "' and '" + fimStr + "'";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dia = sdf.format(timestamp.getTime());
                if (!diaPontoList.contains(dia)) {
                    diaPontoList.add(dia);
                }
            }

            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diaPontoList;
    }

    public List<Integer> consultaDiasNaoFalta(Integer userid, Date inicio, Date fim) {
        List<Integer> diaPontoList = new ArrayList<Integer>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select checktime from checkinout "
                    + "where userid = " + userid + " and checktime between '" + inicioStr + "' and '" + fimStr + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(timestamp.getTime());
                Integer diaInt = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
                if (!diaPontoList.contains(diaInt)) {
                    diaPontoList.add(diaInt);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diaPontoList;
    }

    public List<Integer> consultaDiasAbono(Integer userid, Date inicio, Date fim) {
        List<Integer> diaPontoList = new ArrayList<Integer>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select checktime "
                    + "from REGISTRO_ABONO "
                    + "where userid = " + userid + " and checktime between '" + inicioStr + "' and '" + fimStr + "'";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(timestamp.getTime());
                Integer diaInt = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
                if (!diaPontoList.contains(diaInt)) {
                    diaPontoList.add(diaInt);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diaPontoList;
    }

    public List<Integer> consultaDiasAbonoPeriodo(Integer userid, Date inicio, Date fim) {

        List<Integer> diaPontoList = new ArrayList<Integer>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select startspecday,endspecday from USER_SPEDAY "
                    + "where userid = " + userid + " and startspecday between '" + inicioStr + "' and '" + fimStr + "'";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp startspecday = rs.getTimestamp("startspecday");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(startspecday.getTime());
                Integer diaInt = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
                if (!diaPontoList.contains(diaInt)) {
                    diaPontoList.add(diaInt);
                }
                Timestamp endspecday = rs.getTimestamp("endspecday");
                dia = new GregorianCalendar();
                dia.setTimeInMillis(endspecday.getTime());
                diaInt = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
                if (!diaPontoList.contains(diaInt)) {
                    diaPontoList.add(diaInt);
                }

            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return diaPontoList;
    }

    public List<PeriodoJornada> consultaPeriodoJornada(int userid, Date inicio, Date fim) {

        List<PeriodoJornada> dataPeriodoJornadaList = new ArrayList<PeriodoJornada>();
        try {
            String sql = " select ur.num_of_run_id, ur.startdate, ur.enddate, nr.units, nr.cyle"
                    + " from user_of_run ur, num_run nr"
                    + " where ur.userid = " + userid + " and"
                    + "       ur.num_of_run_id = nr.num_runid"
                    + " ORDER BY ur.startdate ASC";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {

                PeriodoJornada periodoJornada = new PeriodoJornada();

                Date inicioTimes = rs.getDate("startdate");
                Date fimTimes = rs.getDate("enddate");

                periodoJornada.setNum_jornada(rs.getInt("num_of_run_id"));
                periodoJornada.setTipo(rs.getInt("units"));
                periodoJornada.setCiclo(rs.getInt("cyle"));
                periodoJornada.setInicioJornada(inicioTimes);

                if (inicioTimes.getTime() > fimTimes.getTime()) {
                    periodoJornada.setInvalido(true);
                } else {
                    periodoJornada.setInvalido(false);
                }

                if (inicioTimes.getTime() <= inicio.getTime()) {
                    periodoJornada.setInicio(inicio);
                } else {
                    periodoJornada.setInicio(inicioTimes);
                }

                if (fimTimes.getTime() >= fim.getTime()) {
                    periodoJornada.setFim(fim);
                } else {
                    periodoJornada.setFim(fimTimes);
                }

                if ((inicioTimes.getTime() <= fim.getTime() && fimTimes.getTime() >= inicio.getTime())) {
                    dataPeriodoJornadaList.add(periodoJornada);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return dataPeriodoJornadaList;
    }

    public List<Turnos> consultaTurnos(int num_runid) {

        List<Turnos> listaTurnos = new ArrayList<Turnos>();
        try {
            String sql = "select h.schclassid, h.starttime, h.endtime, t.sdays, t.edays, t.overtime "
                    + "from num_run_deil t, schclass h "
                    + "where t.schclassid = h.schclassid and t.num_runid = " + num_runid;
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Turnos t = new Turnos();

                t.setSchclassid(rs.getInt("schclassid"));
                t.setStartTime(rs.getDate("starttime"));
                t.setEndTime(rs.getDate("endtime"));
                t.setSdays(rs.getInt("sdays"));
                t.setEdays(rs.getInt("edays"));
                t.setOvertime(rs.getInt("overtime"));

                listaTurnos.add(t);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return listaTurnos;
    }

    public boolean feriadoInflui(Integer userid) {
        boolean feriadoInflui = true;
        try {
            String sql = " select holiday from userinfo where userid = " + userid;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer flag_holiday = rs.getInt("holiday");
                if (flag_holiday == 0) {
                    feriadoInflui = false;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return feriadoInflui;
    }

    public void fecharConexao() {
    }

    public List<Date> consultaInicioEFimContrato(Integer userid) {

        Date inicioTimes = new Date();
        Date fimTimes = new Date();
        List<Date> inicioeFimList = new ArrayList<Date>();
        try {
            String sql = " select min(startdate) as  startdate" + " from user_of_run ur"
                    + " where ur.userid = " + userid;
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                inicioTimes = rs.getDate("startdate");
                if (inicioTimes != null) {
                    inicioeFimList.add(inicioTimes);
                }
            }
            rs.close();

            sql = "select enddate from user_of_run"
                    + " where userid = " + userid
                    + " order by enddate desc";
            rs = con.executeQuery(sql);

            while (rs.next()) {
                fimTimes = rs.getDate("enddate");
                if (fimTimes != null) {
                    inicioeFimList.add(fimTimes);
                    break;
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return inicioeFimList;
    }

    public List<Ponto> consultaChechInOut(Integer userid, Date inicio, Date fim) {
        List<Ponto> pontos = new ArrayList<Ponto>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            GregorianCalendar dia = new GregorianCalendar();
            dia.setTime(fim);
            dia.add(Calendar.DAY_OF_MONTH, 2);
            Date fim_ = new Date(fim.getTime());
            fim_.setTime(dia.getTimeInMillis());
            String fimStr = sdfHora.format(fim_.getTime());

            String sql = "select u.name,c.checktime,c.sensorid,c.tipoRegistro, r.repAlias"
                    + " from CHECKINOUT c join USERINFO u on c.userid = u.userid left join Rep r on c.repid = r.repId"
                    + " where c.userid = " + userid + " and"
                    + " c.checktime between '" + inicioStr + "' and '" + fimStr + "'";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");
                Integer sensor = rs.getInt("sensorid");
                Integer tipoRegistro = rs.getInt("tipoRegistro");

                Ponto ponto = new Ponto();
                ponto.setPonto(timestamp);
                ponto.setSensor(sensor);
                ponto.setTipoRegistro(tipoRegistro);
                ponto.setLocal(rs.getString("repAlias"));

                pontos.add(ponto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return pontos;
    }

    public List<RegistroAdicionado> consultaRegistrosAdicionados(Integer userid, Date inicio, Date fim) {
        List<RegistroAdicionado> registroAdicionadoList = new ArrayList<RegistroAdicionado>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            GregorianCalendar dia = new GregorianCalendar();
            dia.setTime(fim);
            dia.add(Calendar.DAY_OF_MONTH, 2);
            Date fim_ = new Date(fim.getTime());
            fim_.setTime(dia.getTimeInMillis());
            String fimStr = sdfHora.format(fim_.getTime());

            String sql = " select u.name, r.checktime,r.tipoRegistro,l.leaveName,l.total,r.yuanying"
                    + " from REGISTRO_ABONO r, USERINFO u, LeaveClass l"
                    + " where r.userid = " + userid + " and"
                    + " u.userid = r.userid and "
                    + " l.leaveID = r.dateid and "
                    + " r.checktime" + " between '" + inicioStr + "' and '" + fimStr + "'";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {

                String userid_ = rs.getString("name");
                Timestamp checktime = rs.getTimestamp("checktime");
                String leaveName = rs.getString("leaveName");
                String yuanying = rs.getString("yuanying");
                Boolean total = rs.getBoolean("total");
                Integer tipoRegistro = rs.getInt("tipoRegistro");

                RegistroAdicionado registroAdicionado = new RegistroAdicionado();

                registroAdicionado.setNome(userid_);
                registroAdicionado.setCheckTime(checktime);
                registroAdicionado.setCategoriaJustificativa(leaveName);
                registroAdicionado.setJustificativa(yuanying);
                registroAdicionado.setIsTotal(total);
                registroAdicionado.setTipoRegistro(tipoRegistro);

                registroAdicionadoList.add(registroAdicionado);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return registroAdicionadoList;
    }

    public List<Gratificacao> consultaGratificacao(Integer userid) {
        List<Gratificacao> gratificacaoList = new ArrayList<Gratificacao>();
        try {
            String sql = " select g.cod_gratificacao,g.nome,g.valor,g.verba,d.diaSemana,d.inicio,d.fim "
                    + " from userInfo u,Gratificacao g,DetalheGratificacao d"
                    + " where u.userid = " + userid + " and"
                    + " u.cod_regime = g.cod_regime and "
                    + " g.cod_gratificacao = d.cod_gratificacao "
                    + " order by g.cod_gratificacao";
            ResultSet rs = con.executeQuery(sql);
            List<Integer> controleGratificacaoList = new ArrayList<Integer>();
            List<DetalheGratificacao> detalheGratificacaoList = new ArrayList<DetalheGratificacao>();
            while (rs.next()) {
                Integer cod_gratificacao = rs.getInt("cod_gratificacao");
                String nome = rs.getString("nome");
                Integer diaSemana = rs.getInt("diaSemana");
                Float valor = rs.getFloat("valor");
                String verba = rs.getString("verba");
                String inicio = rs.getString("inicio");
                String fim = rs.getString("fim");
                DetalheGratificacao detalheGratificacao = new DetalheGratificacao(nome, diaSemana, valor, verba, inicio, fim, cod_gratificacao);
                detalheGratificacaoList.add(detalheGratificacao);

                if (!controleGratificacaoList.contains(cod_gratificacao)) {
                    controleGratificacaoList.add(cod_gratificacao);
                }
            }

            for (Iterator<Integer> it = controleGratificacaoList.iterator(); it.hasNext();) {
                Integer cod_gratificacao = it.next();
                Gratificacao gratificacao = new Gratificacao();
                for (Iterator<DetalheGratificacao> it1 = detalheGratificacaoList.iterator(); it1.hasNext();) {
                    DetalheGratificacao detalheGratificacao = it1.next();
                    if (cod_gratificacao.equals(detalheGratificacao.getCod_gratificacao())) {
                        gratificacao.setNome(detalheGratificacao.getNome());
                        gratificacao.setCod_gratificacao(detalheGratificacao.getCod_gratificacao());
                        gratificacao.setValor(detalheGratificacao.getValor());
                        gratificacao.setVerba(detalheGratificacao.getVerba());
                        gratificacao.setDetalheGratificacaoList(detalheGratificacao);
                    }
                }
                gratificacaoList.add(gratificacao);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return gratificacaoList;
    }

    public List<Timestamp> consultaChechInOutTotal(Integer userid, Date inicio, Date fim) {
        List<Timestamp> getAllTimestamp = new ArrayList<Timestamp>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select checktime" + " from checkinout"
                    + " where userid = " + userid + " and"
                    + " checktime" + " between '" + inicioStr + "' and '" + fimStr + "'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");
                getAllTimestamp.add(timestamp);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return getAllTimestamp;
    }

    public List<Abono> consultaAbono(Integer userid, Date inicio, Date fim) {
        List<Abono> abonoList = new ArrayList<Abono>();
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 172799998);

            String sql = " select s.userid,s.STARTSPECDAY,s.ENDSPECDAY,s.YUANYING,s.DATEID, l.leavename"
                    + " from  USER_SPEDAY s,USERINFO u,LeaveClass l"
                    + " where s.userid = u.userid and"
                    + "       s.userid = " + userid.toString() + " and"
                    + "       s.dateid = " + "l.LeaveID" + " and"
                    + "       s.STARTSPECDAY" + " between '" + inicioStr + "' and '" + fimStr + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer userId = rs.getInt("userid");
                Timestamp inicioAbono = rs.getTimestamp("STARTSPECDAY");
                Timestamp fimAbono = rs.getTimestamp("ENDSPECDAY");
                Integer cod = rs.getInt("DATEID");
                String justificativa = rs.getString("YUANYING");
                String categoriaJustificativa = rs.getString("leavename");

                GregorianCalendar diaHora = new GregorianCalendar();
                diaHora.setTime(inicioAbono);
                int diaDoAno = diaHora.get(Calendar.DAY_OF_YEAR) + diaHora.get(Calendar.YEAR) * 365;

                Abono abono = new Abono();
                abono.setUserId(userId);
                abono.setDataInicioAbono(inicioAbono);
                abono.setDataFimAbono(fimAbono);
                abono.setCod(cod);
                abono.setJustificativa(justificativa);
                abono.setDiaAbono(diaDoAno);
                abono.setCategoriaJustificativa(categoriaJustificativa);
                abonoList.add(abono);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return abonoList;
    }

    public String consultaNome(Integer cod) {

        String nome = "Funcionario não encontrado";
        try {
            // connection to an ACCESS MDB

            String sql = "select u.name" + " from USERINFO u" + " where u.userid = " + cod;

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                nome = rs.getString("Name");
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nome;
    }

    public Funcionario consultaFuncionario(int cod) {
        Funcionario funcionario = new Funcionario();
        try {
            String sql = "select u.name, d.deptname, u.ssn, r.nome as regime "
                    + "from USERINFO u left join Regime_HoraExtra r on u.cod_regime = r.cod_regime, departments d"
                    + " where u.userid = ? and "
                    + " d.deptid = u.defaultdeptid";

            if (con.prepareStatement(sql)) {
                con.pstmt.setInt(1, cod);
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    funcionario.setNome(rs.getString("Name"));
                    funcionario.setDept(rs.getString("deptname"));
                    funcionario.setCpf(rs.getString("ssn"));
                    funcionario.setNome_regime(rs.getString("regime"));
                }
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("ConsultaPonto.Banco.consultaFuncionario" + e.getMessage());
        } finally {
            con.Desconectar();
        }
        return funcionario;
    }

    public Integer consultaNumJornadaCerta(Integer userid, String dataSelecionada) {

        Integer jornadaNumber = -1;
        try {
            // connection to an ACCESS MDB
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = null;
            try {
                dt = df.parse(dataSelecionada);
            } catch (ParseException ex) {
                System.out.println("ConsultaPonto: consultaNumJornadaCerta: " + ex);
                //Logger.getLogger(DetalheDiaBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            String sql = "select num_of_run_id,startdate,enddate"
                    + " from user_of_run ur"
                    + " where ur.userid = " + userid;

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer num_jornada = rs.getInt("num_of_run_id");
                Date inicioTimes = rs.getDate("startdate");
                Date fimTimes = rs.getDate("enddate");

                if ((inicioTimes.getTime() <= dt.getTime()) && (fimTimes.getTime() >= dt.getTime())) {
                    jornadaNumber = num_jornada;
                }
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return jornadaNumber;
    }

    public boolean solicitacaoAbono(Integer userid, String data, String entrada1, String saida1, String entrada2, String saida2, String descricao, Date inclusao) {
        boolean ok = true;

        if (entrada1 == null && saida1 == null && entrada2 == null && saida2 == null) {
            return false;
        }
        try {
            String query = "insert into SolicitacaoAbono(USERID,Data,Entrada1,Saida1,Entrada2,Saida2,Descricao,Status,Inclusao) values(?,?,?,?,?,?,?,?,?)";

            con.prepareStatement(query);
            con.pstmt.setInt(1, userid);
            con.pstmt.setString(2, data);
            con.pstmt.setString(3, entrada1);
            con.pstmt.setString(4, saida1);
            con.pstmt.setString(5, entrada2);
            con.pstmt.setString(6, saida2);
            con.pstmt.setString(7, descricao);
            con.pstmt.setString(8, "Pendente");
            Locale.setDefault(new Locale("pt", "BR"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
            con.pstmt.setString(9, sdf.format(new java.util.Date()));
            con.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());

            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean insertRelatorio(List<Relatorio> relatorio) {
        boolean ok = true;

        String query = "insert into relatorio(data,definicao,Entrada1,Saida1,Entrada2,Saida2,horasTrabalhadas,saldo,observacao) values(?,?,?,?,?,?,?,?,?)";
        String queryDelete = "delete from relatorio";
        con.executeUpdate(queryDelete);
        con.prepareStatement(query);

        try {
            for (Iterator<Relatorio> it = relatorio.iterator(); it.hasNext();) {
                Relatorio relatorio1 = it.next();

                con.pstmt.setString(1, relatorio1.getData());
                con.pstmt.setString(2, relatorio1.getDefinicao());
                con.pstmt.setString(3, relatorio1.getEntrada1());
                con.pstmt.setString(4, relatorio1.getSaida1());
                con.pstmt.setString(5, relatorio1.getEntrada2());
                con.pstmt.setString(6, relatorio1.getSaida2());
                con.pstmt.setString(7, relatorio1.getHorasTrabalhadas());
                con.pstmt.setString(8, relatorio1.getSaldo());
                con.pstmt.setString(9, relatorio1.getObservacao());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean insertRelatorioSemEscala(List<DiaSemEscala> pontoAcessoTotal) {
        boolean ok = true;

        String query = "insert into relatorio_sem_escala(data,pontos) values(?,?)";
        String queryDelete = "delete from relatorio_sem_escala";
        con.executeUpdate(queryDelete);
        con.prepareStatement(query);

        try {
            for (Iterator<DiaSemEscala> it = pontoAcessoTotal.iterator(); it.hasNext();) {
                DiaSemEscala pontoAcessoTotal1 = it.next();

                con.pstmt.setString(1, pontoAcessoTotal1.getDataSrt());
                con.pstmt.setString(2, pontoAcessoTotal1.getPontosBatidos());

                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public List<Jornada> consultaRegraJornada(Integer userid, Integer num_jornada) {

        List<Jornada> jornadaList = new ArrayList<Jornada>();
        try {
            String sql = "select s.*,nrd.sdays,nrd.edays,nr.startdate,nr.cyle,nr.units"
                    + " from " + "num_run_deil nrd,schclass s,num_run nr"
                    + " where nrd.num_runid = " + num_jornada + " and"
                    + " nrd.schclassid = s.schclassid and "
                    + " nr.num_runid = " + num_jornada + " order by nrd.SDAYS, nrd.STARTTIME";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String schname = rs.getString("SCHNAME");
                Integer schClassID = rs.getInt("schClassID");
                String legend = rs.getString("legend");
                Timestamp inicioJornada = rs.getTimestamp("STARTTIME");
                Timestamp terminioJornada = rs.getTimestamp("ENDTIME");
                Timestamp inicioJornadaL = rs.getTimestamp("STARTTIME");
                Timestamp terminioJornadaL = rs.getTimestamp("ENDTIME");
                Timestamp checkInLimiteAntecipada = rs.getTimestamp("CHECKINTIME1");
                Timestamp checkInLimiteAtrasada = rs.getTimestamp("CHECKINTIME2");
                Timestamp checkOutLimiteAntecipada = rs.getTimestamp("CHECKOUTTIME1");
                Timestamp checkOutLimiteAtrasada = rs.getTimestamp("CHECKOUTTIME2");
                Integer prazoMinutosAtraso = rs.getInt("LATEMINUTES");
                Integer prazoMinutosAdiantado = rs.getInt("EARLYMINUTES");
                Integer checkIn = rs.getInt("CHECKIN");
                Integer checkOut = rs.getInt("CHECKOUT");
                Integer trabalhoMinutos = rs.getInt("WorkMins");
                Integer sdays = rs.getInt("SDAYS");
                Integer edays = rs.getInt("EDAYS");
                Integer cyle = rs.getInt("cyle");
                Integer units = rs.getInt("units");
                Date startdate = rs.getDate("startdate");
                float tipoJornada = rs.getFloat("WorkDay");
                Timestamp inicioDescanso1 = null;
                if (rs.getTimestamp("RESTINTIME1") != null) {
                    inicioDescanso1 = rs.getTimestamp("RESTINTIME1");
                }
                Timestamp fimDescanso1 = null;
                if (rs.getTimestamp("RESTOUTTIME1") != null) {
                    fimDescanso1 = rs.getTimestamp("RESTOUTTIME1");
                }
                Timestamp inicioIntrajornada = null;
                if (rs.getTimestamp("InterRestIn") != null) {
                    inicioIntrajornada = rs.getTimestamp("InterRestIn");
                }
                Timestamp fimIntrajornada = null;
                if (rs.getTimestamp("InterRestOut") != null) {
                    fimIntrajornada = rs.getTimestamp("InterRestOut");
                }
                Timestamp inicioDescanso2 = null;
                if (rs.getTimestamp("RESTINTIME2") != null) {
                    inicioDescanso2 = rs.getTimestamp("RESTINTIME2");
                }
                Timestamp fimDescanso2 = null;
                if (rs.getTimestamp("RESTOUTTIME2") != null) {
                    fimDescanso2 = rs.getTimestamp("RESTOUTTIME2");
                }
                Integer tolerancia = rs.getInt("RestTolerance");
                Boolean deduzirDescanco1 = rs.getBoolean("ReduceRestTime1");
                Boolean deduzirDescanco2 = rs.getBoolean("ReduceRestTime2");
                Boolean deduzirIntrajornada = rs.getBoolean("ReduceInterRest");
                Boolean combinarEntrada = rs.getBoolean("CombineIn");
                Boolean combinarSaida = rs.getBoolean("CombineOut");

                Jornada jornada = new Jornada(schname, schClassID, inicioJornada, terminioJornada, prazoMinutosAtraso, prazoMinutosAdiantado,
                        checkIn, checkOut, trabalhoMinutos, checkInLimiteAntecipada, checkInLimiteAtrasada, checkOutLimiteAntecipada,
                        checkOutLimiteAtrasada, tipoJornada, sdays, edays, cyle, units, startdate, inicioJornadaL, terminioJornadaL, legend,
                        inicioDescanso1, fimDescanso1, inicioDescanso2, fimDescanso2, tolerancia, deduzirDescanco1, deduzirDescanco2,
                        inicioIntrajornada, fimIntrajornada, deduzirIntrajornada, combinarEntrada, combinarSaida, false);
                jornadaList.add(jornada);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (jornadaList.size() == 1 && jornadaList.get(0).getTemVirada() && jornadaList.get(0).getCyle() == 1) {
            Jornada jornada_ = new Jornada();
            jornada_ = jornadaList.get(0);
            jornadaList.add(jornada_);
        }
        return jornadaList;
    }

    public List<Integer> consultaDiasTrabalho(Integer userid, Integer num_jornada, Date dataInicio, Date dataFim, Date startDate,
            List<Integer> diasDeslocadosFolgas, List<Feriado> feriadosList) {

        List<Integer> diaList = new ArrayList<Integer>();
        HashMap<Integer, List<Integer>> diaSemanaHashMap = new HashMap<Integer, List<Integer>>();
        List<Integer> diaTrabalhoList = new ArrayList<Integer>();
        Map<Integer, Boolean> hasVirada = new HashMap<Integer, Boolean>();
        Integer cyle = 0;
        Integer primeiroSday = 0;

        try {

            String sql = "select nr.cyle,nr.units,nrd.sdays,nrd.edays from"
                    + " num_run_deil nrd, num_run nr"
                    + " where nrd.num_runid = " + num_jornada
                    + " and" + " nr.num_runid = " + num_jornada;

            ResultSet rs = con.executeQuery(sql);
            Integer units = 0;
            Boolean primeiroSdays = true;

            while (rs.next()) {
                units = rs.getInt("UNITS");
                Integer sdays = rs.getInt("SDAYS");
                Integer edays = rs.getInt("EDAYS");
                cyle = rs.getInt("cyle");
                Integer num_ciclo = 0;
                Integer sdays_ = 0;
                if (primeiroSdays) {
                    primeiroSday = sdays;
                    primeiroSdays = false;
                }

                if (sdays % 7 == 0 && sdays != 0) {
                    num_ciclo = ((sdays - 1) / 7);
                    sdays_ = 7;
                } else {
                    num_ciclo = sdays / 7;
                    sdays_ = sdays % 7;
                }

                List<Integer> listDias = new ArrayList<Integer>();
                if (diaSemanaHashMap.get(num_ciclo) == null) {
                    listDias.add(sdays_);
                } else {
                    listDias = diaSemanaHashMap.get(num_ciclo);
                    listDias.add(sdays_);
                }

                diaSemanaHashMap.put(num_ciclo, listDias);

                if (sdays.equals(edays)) {
                    hasVirada.put(sdays, false);
                } else {
                    hasVirada.put(sdays, true);
                }
            }
            rs.close();

            if (units == 0 || units == 2) {

                diaList = new ArrayList<Integer>();
                hasVirada = new HashMap<Integer, Boolean>();

                sql = "select nr.cyle, nrd.sdays,nrd.edays from"
                        + " num_run_deil nrd, num_run nr"
                        + " where nrd.num_runid = " + num_jornada + " and"
                        + " nr.num_runid = " + num_jornada;

                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer sdays = rs.getInt("SDAYS");
                    Integer edays = rs.getInt("EDAYS");
                    cyle = rs.getInt("cyle");
                    if (!diaList.contains(sdays)) {
                        diaList.add(sdays);
                    }
                    if (sdays.equals(edays)) {
                        hasVirada.put(sdays, false);
                    } else {
                        hasVirada.put(sdays, true);
                    }
                }

                rs.close();

            }

            GregorianCalendar diaHorainicio = new GregorianCalendar();
            diaHorainicio.setTime(dataInicio);
            Integer diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

            GregorianCalendar diaHoraFim = new GregorianCalendar();
            diaHoraFim.setTime(dataFim);
            int diaFim = diaHoraFim.get(Calendar.DAY_OF_YEAR) + diaHoraFim.get(Calendar.YEAR) * 365;

            if (units == 0) {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int i = 0;
                boolean naoPule = true;
                do {
                    if ((diaList.contains(i)) && diaInicio <= diaInicioStartDate
                            && !diasDeslocadosFolgas.contains(diaInicioStartDate)) {
                        if (!diaTrabalhoList.contains(diaInicioStartDate)) {
                            diaTrabalhoList.add(diaInicioStartDate);
                        }

                        if (containsFeriado(feriadosList, diaInicioStartDate)) {
                            naoPule = false;
                        }

                        if (hasVirada.get(i) && (!diasDeslocadosFolgas.contains(diaInicioStartDate + 1)) && naoPule) {
                            if (!diaTrabalhoList.contains(diaInicioStartDate + 1)) {
                                diaTrabalhoList.add(diaInicioStartDate + 1);
                            }
                        }
                        naoPule = true;
                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;

                    if (i == cyle - 1) {
                        i = 0;
                    } else {
                        i++;
                    }
                } while (diaInicioStartDate != diaFim + 1);

            } else if (units == 1) {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int diaSemana = diaStartDate.get(Calendar.DAY_OF_WEEK) - 1;

                Integer num_ciclo = 0;

                if (primeiroSday <= diaSemana) {
                    Set<Integer> conjuntoCiclos = diaSemanaHashMap.keySet();
                    num_ciclo = getMaxCiclos(conjuntoCiclos);
                }
                int i = diaSemana * (num_ciclo + 1);
                boolean naoPule = true;
                do {

                    if (diaSemana == 0) {
                        diaSemana = 7;
                    }

                    if (diaSemanaHashMap.get(num_ciclo) != null && diaSemanaHashMap.get(num_ciclo).contains(diaSemana) && diaInicio <= diaInicioStartDate
                            && !diasDeslocadosFolgas.contains(diaInicioStartDate)) {
                        if (!diaTrabalhoList.contains(diaInicioStartDate)) {
                            diaTrabalhoList.add(diaInicioStartDate);
                        }

                        if (containsFeriado(feriadosList, diaInicioStartDate)) {
                            naoPule = false;
                        }

                        if (hasVirada.get((num_ciclo * 7) + diaSemana) && (!diasDeslocadosFolgas.contains(diaInicioStartDate + 1)) && naoPule) {
                            if (!diaTrabalhoList.contains(diaInicioStartDate + 1)) {
                                diaTrabalhoList.add(diaInicioStartDate + 1);
                            }
                        }
                        naoPule = true;
                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                    diaSemana = diaStartDate.get(Calendar.DAY_OF_WEEK) - 1;

                    i++;

                    if (i % 7 == 0) {
                        num_ciclo = ((i - 1) / 7);
                    } else {
                        num_ciclo = i / 7;
                    }
                    if (i == cyle * 7) {
                        i = 0;
                    }

                } while (diaInicioStartDate != diaFim + 1);
            } else {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int i = 0;
                boolean naoPule = true;
                do {
                    if ((diaList.contains(i)) && diaInicio <= diaInicioStartDate
                            && !diasDeslocadosFolgas.contains(diaInicioStartDate)) {
                        if (!diaTrabalhoList.contains(diaInicioStartDate)) {
                            diaTrabalhoList.add(diaInicioStartDate);
                        }

                        if (containsFeriado(feriadosList, diaInicioStartDate)) {
                            naoPule = false;
                        }

                        if (hasVirada.get(i) && (!diasDeslocadosFolgas.contains(diaInicioStartDate + 1)) && naoPule) {
                            if (!diaTrabalhoList.contains(diaInicioStartDate + 1)) {
                                diaTrabalhoList.add(diaInicioStartDate + 1);
                            }
                        }
                        naoPule = true;
                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;

                    if (i == (cyle * 31) - 1) {
                        i = 0;
                    } else {
                        i++;
                    }
                } while (diaInicioStartDate != diaFim + 1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return diaTrabalhoList;
    }

    private Integer getMaxCiclos(Set<Integer> conjuntoCiclos) {
        int aux = -1;
        int saida = 0;
        for (Iterator<Integer> it = conjuntoCiclos.iterator(); it.hasNext();) {
            Integer ciclo = it.next();
            if (ciclo > aux) {
                saida = ciclo;
            }
        }
        return saida;
    }

    private Boolean containsFeriado(List<Feriado> feriadoList, Integer dia) {

        for (Iterator<Feriado> it = feriadoList.iterator(); it.hasNext();) {
            Feriado feriado = it.next();
            if (feriado.getDiaFeriado().equals(dia)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, List<Integer>> getDiaHashMap(Integer userid, Integer num_jornada, Date dataInicio, Date dataFim, Date startDate) {

        List<Integer> diaList = new ArrayList<Integer>();
        HashMap<Integer, List<Integer>> diaSemanaHashMap = new HashMap<Integer, List<Integer>>();
        HashMap<Integer, List<Integer>> diaTrabalhoList = new HashMap<Integer, List<Integer>>();
        Map<Integer, Boolean> hasVirada = new HashMap<Integer, Boolean>();
        Integer cyle = 0;
        Integer primeiroSday = 0;

        try {

            String sql = "select nr.cyle,nr.units,nrd.sdays,nrd.edays from"
                    + " num_run_deil nrd, num_run nr"
                    + " where nrd.num_runid = " + num_jornada
                    + " and" + " nr.num_runid = " + num_jornada;

            ResultSet rs = con.executeQuery(sql);
            Integer units = 0;

            while (rs.next()) {
                units = rs.getInt("UNITS");
                Integer sdays = rs.getInt("SDAYS");
                Integer edays = rs.getInt("EDAYS");
                cyle = rs.getInt("cyle");
                Integer num_ciclo = 0;
                Integer sdays_ = 0;

                if (sdays % 7 == 0 && sdays != 0) {
                    num_ciclo = ((sdays - 1) / 7);
                    sdays_ = 7;
                } else {
                    num_ciclo = sdays / 7;
                    sdays_ = sdays % 7;
                }

                List<Integer> listDias = new ArrayList<Integer>();
                if (diaSemanaHashMap.get(num_ciclo) == null) {
                    listDias.add(sdays_);
                } else {
                    listDias = diaSemanaHashMap.get(num_ciclo);
                    listDias.add(sdays_);
                }

                diaSemanaHashMap.put(num_ciclo, listDias);

                if (sdays == edays) {
                    hasVirada.put(sdays, false);
                } else {
                    hasVirada.put(sdays, true);
                }
            }
            rs.close();

            if (units == 0 || units == 2) {

                diaList = new ArrayList<Integer>();
                hasVirada = new HashMap<Integer, Boolean>();

                sql = "select nr.cyle, nrd.sdays,nrd.edays from"
                        + " num_run_deil nrd, num_run nr"
                        + " where nrd.num_runid = " + num_jornada + " and"
                        + " nr.num_runid = " + num_jornada;

                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer sdays = rs.getInt("SDAYS");
                    Integer edays = rs.getInt("EDAYS");
                    cyle = rs.getInt("cyle");
                    if (!diaList.contains(sdays)) {
                        diaList.add(sdays);
                    }
                    if (sdays == edays) {
                        hasVirada.put(sdays, false);
                    } else {
                        hasVirada.put(sdays, true);
                    }
                }

                rs.close();

            }

            GregorianCalendar diaHorainicio = new GregorianCalendar();
            diaHorainicio.setTime(dataInicio);
            Integer diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

            GregorianCalendar diaHoraFim = new GregorianCalendar();
            diaHoraFim.setTime(dataFim);
            int diaFim = diaHoraFim.get(Calendar.DAY_OF_YEAR) + diaHoraFim.get(Calendar.YEAR) * 365;

            if (units == 0) {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int i = 0;
                do {
                    if ((diaList.contains(i)) && diaInicio <= diaInicioStartDate) {
                        List<Integer> iList = new ArrayList<Integer>();
                        if (diaTrabalhoList.get(diaInicioStartDate) != null) {
                            iList = diaTrabalhoList.get(diaInicioStartDate);
                            if (!iList.contains(i)) {
                                iList.add(i);
                            }
                        } else {
                            iList.add(i);
                        }
                        diaTrabalhoList.put(diaInicioStartDate, iList);
                        if (hasVirada.get(i)) {
                            iList = new ArrayList<Integer>();
                            if (diaTrabalhoList.get(diaInicioStartDate + 1) != null) {
                                iList = diaTrabalhoList.get(diaInicioStartDate + 1);
                                if (!iList.contains(i + 1)) {
                                    iList.add(i + 1);
                                }
                            } else {
                                iList.add(i + 1);
                            }
                            diaTrabalhoList.put(diaInicioStartDate + 1, iList);
                        }
                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                    if (i == cyle - 1) {
                        i = 0;
                    } else {
                        i++;
                    }
                } while (diaInicioStartDate != diaFim + 1);

            } else if (units == 1) {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int diaSemana = diaStartDate.get(Calendar.DAY_OF_WEEK) - 1;

                Integer num_ciclo = 0;

                if (primeiroSday < diaSemana) {
                    Set<Integer> conjuntoCiclos = diaSemanaHashMap.keySet();
                    num_ciclo = getMaxCiclos(conjuntoCiclos);
                }
                int i = diaSemana * (num_ciclo + 1);

                do {

                    if (diaSemana == 0) {
                        diaSemana = 7;
                    }

                    if (diaSemanaHashMap.get(num_ciclo) != null
                            && diaSemanaHashMap.get(num_ciclo).contains(diaSemana)
                            && diaInicio <= diaInicioStartDate) {
                        List<Integer> iList = new ArrayList<Integer>();
                        if (diaTrabalhoList.get(diaInicioStartDate) != null) {
                            iList = diaTrabalhoList.get(diaInicioStartDate);
                            if (!iList.contains((num_ciclo) * 7 + diaSemana)) {
                                iList.add((num_ciclo) * 7 + diaSemana);
                            }
                        } else {
                            iList.add((num_ciclo) * 7 + diaSemana);
                        }
                        diaTrabalhoList.put(diaInicioStartDate, iList);

                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                    diaSemana = diaStartDate.get(Calendar.DAY_OF_WEEK) - 1;

                    i++;

                    if (i % 7 == 0) {
                        num_ciclo = ((i - 1) / 7);
                    } else {
                        num_ciclo = i / 7;
                    }
                    if (i == cyle * 7) {
                        i = 0;
                    }
                } while (diaInicioStartDate != diaFim + 1);
            } else {

                GregorianCalendar diaStartDate = new GregorianCalendar();
                diaStartDate.setTime(startDate);

                Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                int i = 0;
                do {
                    if ((diaList.contains(i)) && diaInicio <= diaInicioStartDate) {
                        List<Integer> iList = new ArrayList<Integer>();
                        if (diaTrabalhoList.get(diaInicioStartDate) != null) {
                            iList = diaTrabalhoList.get(diaInicioStartDate);
                            if (!iList.contains(i)) {
                                iList.add(i);
                            }
                        } else {
                            iList.add(i);
                        }
                        diaTrabalhoList.put(diaInicioStartDate, iList);
                        if (hasVirada.get(i)) {
                            iList = new ArrayList<Integer>();
                            if (diaTrabalhoList.get(diaInicioStartDate + 1) != null) {
                                iList = diaTrabalhoList.get(diaInicioStartDate + 1);
                                if (!iList.contains(i + 1)) {
                                    iList.add(i + 1);
                                }
                            } else {
                                iList.add(i + 1);
                            }
                            diaTrabalhoList.put(diaInicioStartDate + 1, iList);
                        }
                    }
                    diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
                    diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
                    if (i == (cyle * 31) - 1) {
                        i = 0;
                    } else {
                        i++;
                    }
                } while (diaInicioStartDate != diaFim + 1);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return diaTrabalhoList;
    }

    /*  public HashMap<Integer, Integer> getDiaHashMap(Integer userid, Integer num_jornada, Date dataInicio, Date dataFim, Date startDate) {
    
     List<Integer> diaSemanaList = new ArrayList<Integer>();
     HashMap<Integer, Integer> diaTrabalhoList = new HashMap<Integer, Integer>();
     Map<Integer, Boolean> hasVirada = new HashMap<Integer, Boolean>();
     Integer cyle = 0;
     try {
     // connection to an ACCESS MDB
     
     
     
    
     String sql = "select nr.units,nrd.sdays,nrd.edays from"
     + " num_run_deil nrd, num_run nr"
     + " where nrd.num_runid = " + num_jornada
     + " and" + " nr.num_runid = " + num_jornada;
    
     
     ResultSet rs = con.executeQuery(sql);
     Integer units = 0;
    
     while (rs.next()) {
     units = rs.getInt("UNITS");
     Integer sdays = rs.getInt("SDAYS");
     Integer edays = rs.getInt("EDAYS");
     if (!diaSemanaList.contains(sdays)) {
     diaSemanaList.add(sdays);
     }
     if (!diaSemanaList.contains(edays)) {
     diaSemanaList.add(edays);
     }
     }
     rs.close();
     
    
     if (units == 0) {
    
     diaSemanaList = new ArrayList<Integer>();
    
     String sql = "select nr.cyle, nrd.sdays,nrd.edays from"
     + " num_run_deil nrd, num_run nr"
     + " where nrd.num_runid = " + num_jornada + " and"
     + " nr.num_runid = " + num_jornada;
    
     
     ResultSet rs = con.executeQuery(sql);
    
     while (rs.next()) {
     Integer sdays = rs.getInt("SDAYS");
     Integer edays = rs.getInt("EDAYS");
     cyle = rs.getInt("cyle");
     if (!diaSemanaList.contains(sdays)) {
     diaSemanaList.add(sdays);
     }
     if (sdays == edays) {
     hasVirada.put(sdays, false);
     } else {
     hasVirada.put(sdays, true);
     }
     }
     }
    
     GregorianCalendar diaHorainicio = new GregorianCalendar();
     diaHorainicio.setTime(dataInicio);
     Integer diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;
    
     GregorianCalendar diaHoraFim = new GregorianCalendar();
     diaHoraFim.setTime(dataFim);
     int diaFim = diaHoraFim.get(Calendar.DAY_OF_YEAR) + diaHoraFim.get(Calendar.YEAR) * 365;
    
     int i = 0;
    
     if (units == 0) {
    
     GregorianCalendar diaStartDate = new GregorianCalendar();
     diaStartDate.setTime(startDate);
    
     Integer diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
    
     do {
     if ((diaSemanaList.contains(i)) && diaInicio <= diaInicioStartDate) {
     diaTrabalhoList.put(diaInicioStartDate, i);
     if (hasVirada.get(i)) {
     diaTrabalhoList.put(diaInicioStartDate + 1, i + 1);
     }
     }
     diaStartDate.add(Calendar.DAY_OF_MONTH, 1);
     diaInicioStartDate = diaStartDate.get(Calendar.DAY_OF_YEAR) + diaStartDate.get(Calendar.YEAR) * 365;
     if (i == cyle - 1) {
     i = 0;
     } else {
     i++;
     }
     } while (diaInicioStartDate != diaFim + 1);
    
     } else if (units == 1) {
    
     do {
     int diaSemana = diaHorainicio.get(Calendar.DAY_OF_WEEK) - 1;
    
     if (diaSemanaList.contains(diaSemana)) {
     diaTrabalhoList.put(diaInicio, diaSemana);
     }
     diaHorainicio.add(Calendar.DAY_OF_MONTH, 1);
     diaInicio = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;
    
     } while (diaInicio != diaFim + 1);
     }
    
     rs.close();
     
    
     } catch (Exception e) {    System.out.println(e.getMessage());
    
     }
     return diaTrabalhoList;
     }*/
    public List<Feriado> consultaFeriados(Date inicio, Date fim, List<Integer> diasDeslocadosAdicionais) {

        List<Feriado> feriadoList = new ArrayList<Feriado>();

        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar diaHorainicio = new GregorianCalendar();
            diaHorainicio.setTime(inicio);
            diaHorainicio.add(Calendar.DAY_OF_MONTH, -7);
            String inicioStr = sdfHora.format(diaHorainicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime());

            String sql = " SELECT h.HOLIDAYNAME,h.STARTTIME from"
                    + " HOLIDAYS h" + " where starttime between '" + inicioStr + "' and '" + fimSrt + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String nome = rs.getString("HOLIDAYNAME");
                Timestamp data = rs.getTimestamp("STARTTIME");

                GregorianCalendar diaHora = new GregorianCalendar();
                diaHora.setTime(data);

                int diadoAno = diaHora.get(Calendar.DAY_OF_YEAR) + diaHora.get(Calendar.YEAR) * 365;
                Feriado feriado = new Feriado(nome, diadoAno, new Date(data.getTime()));
                if (!diasDeslocadosAdicionais.contains(diadoAno)) {
                    feriadoList.add(feriado);
                }
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return feriadoList;
    }

    public List<Date> consultaDiaComHoraExtra(Integer userid, Date dia) {
        List<Date> date = new ArrayList<Date>();
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(dia.getTime());
            String sql = " select * from user_temp_sch u, horaextra h "
                    + " where u.userid = " + userid + " and u.userid = h.userid and "
                    + " u.overtime = 1 and "
                    + " convert(nvarchar(10), h.data, 103) = '" + dateStr + "' and "
                    + " (convert(nvarchar(10), u.cometime, 103) = convert(nvarchar(10), h.data, 103) or convert(nvarchar(10), u.leavetime, 103) = convert(nvarchar(10), h.data, 103))";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                date.add(rs.getTimestamp("cometime"));
                date.add(rs.getTimestamp("leavetime"));
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public List<Integer> consultaFeriadosCriticos(Date inicio, Date fim) {

        List<Integer> feriadoCriticoList = new ArrayList<Integer>();

        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime());

            String sql = " SELECT h.starttime,h.oficial from"
                    + " HOLIDAYS h" + " where oficial = 'true' and"
                    + " starttime between '" + inicioStr + "' and '" + fimSrt + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {

                Timestamp data = rs.getTimestamp("STARTTIME");
                GregorianCalendar diaHora = new GregorianCalendar();
                diaHora.setTime(data);

                int diadoAno = diaHora.get(Calendar.DAY_OF_YEAR) + diaHora.get(Calendar.YEAR) * 365;
                feriadoCriticoList.add(diadoAno);
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return feriadoCriticoList;
    }

    public List<String> consultaUsuarios() {
        List<String> userList = new ArrayList<String>();

        try {
            String sql = "select name from" + " userinfo ORDER BY  name asc";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                userList.add(rs.getString("name"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public List<SelectItem> consultaFuncionario(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;
        List<String> cpfDuplicados = getCPFsDuplicados();

        try {
            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";

                ResultSet rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dep);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dep, deptPermitidos, idToSuperHash);

                sql = "select distinct u.userid,u.name,u.defaultdeptid,u.badgenumber,u.ssn"
                        + " from USERINFO u"
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
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
                rs.close();
            } else {
                String sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY name asc";

                ResultSet rs = con.executeQuery(sql);
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
                rs.close();
            }

        } catch (Exception e) {
            System.out.println("Erro consulta funcionário" + e);
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    //Lista de pessoas com duplo vínculo
    private List<String> getCPFsDuplicados() {
        List<String> cpfList = new ArrayList<String>();
        try {
            String sql = "SELECT distinct ssn FROM userinfo u1"
                    + "	WHERE (SELECT count(*) FROM userinfo u2	WHERE u2.ssn = u1.ssn) > 1 order by ssn";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String cpf = rs.getString("ssn");
                cpfList.add(cpf);
            }
            rs.close();
        } catch (SQLException e) {
        } finally {
            con.Desconectar();
        }

        return cpfList;
    }

    public List<SelectItem> consultaFuncionario(Integer dep, String permissao, Boolean incluirSubSetores) {
        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;
        List<String> cpfDuplicados = getCPFsDuplicados();
        try {
            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";

                ResultSet rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dep);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dep, deptPermitidos, idToSuperHash);

                sql = "select distinct u.userid,u.name,u.defaultdeptid,u.badgenumber,u.ssn"
                        + " from USERINFO u "
                        + " where u.permissao != " + permissao
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");

                    if (deptIDList.contains(dept)) {
                        if (cpfDuplicados.contains(ssn)) {
                            name += " - " + badgenumber;
                        }
                        userList.add(new SelectItem(userid, name));
                    }
                }
                rs.close();
            } else {
                String sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep
                        + " and u.permissao != " + permissao
                        + " ORDER BY name asc";

                ResultSet rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");

                    if (cpfDuplicados.contains(ssn)) {
                        name += " - " + badgenumber;
                    }
                    userList.add(new SelectItem(userid, name));
                }
                rs.close();
            }
        } catch (Exception e) {
            System.out.println("Erro consulta funcionário" + e);
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public List<SelectItem> consultaFuncionarioProprioAdministrador(Integer codigo_usuario) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        try {
            String sql = "select name from USERINFO "
                    + " where userid = " + codigo_usuario;
            ResultSet rs = con.executeQuery(sql);

            userList.add(new SelectItem(-1, "Selecione um funcionário"));

            while (rs.next()) {
                String name = rs.getString("name");
                userList.add(new SelectItem(codigo_usuario, name));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public List<SelectItem> consultaFuncionarioTotal(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        List<Integer> deptIDList;

        try {
            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";

                ResultSet rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dep);

                HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dep, deptPermitidos, idToSuperHash);

                sql = "select u.userid,u.name,u.defaultdeptid"
                        + " from  USERINFO u "
                        + " ORDER BY u.name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }
                rs.close();
            } else {
                String sql = "select u.userid,u.name"
                        + " from  USERINFO u "
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY u.name asc";
                ResultSet rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    userList.add(new SelectItem(userid, name));
                }
                rs.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public boolean isAdministrador(Integer login) {
        try {
            String sql = "select permissao from" + " USERINFO " + " where userid = " + login;
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String permissao = rs.getString("permissao");
                if (permissao.equals("1")) {
                    return true;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return false;

    }

    public String consultaDepartamentoNome(Integer deptID) {
        String nomeDept = "";
        try {
            String sql = " select DEPTNAME"
                    + " from  DEPARTMENTS d"
                    + " where d.deptid = " + deptID;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nomeDept = rs.getString("DEPTNAME");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return nomeDept;
    }

    public String consultaDepartamentoNomeByUserid(Integer userid) {
        String nomeDept = "";
        try {
            String sql = " select DEPTNAME"
                    + " from  DEPARTMENTS d, USERINFO u"
                    + " where u.defaultdeptid = d.deptid and "
                    + " userid = " + userid;

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nomeDept = rs.getString("DEPTNAME");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return nomeDept;
    }

    public List<String> consultaInfoUsuario(Integer cod) {
        List<String> infoList = new ArrayList<String>();

        try {
            String sql = " select userid,name,ssn,badgenumber,deptname,c.nome as cargo, r.nome as regime"
                    + " from userinfo u left join cargo c on u.cargo =  c.cod_cargo"
                    + " left join regime_horaextra r on r.cod_regime = u.cod_regime, departments d"
                    + " where u.userid = " + cod + " and u.defaultdeptid = d.deptid ";

            ResultSet rs = con.executeQuery(sql);

            String userid = "";
            String name = "";
            String ssn = "";
            String lotacao = "";
            String cargo = "";
            String regime = "";

            while (rs.next()) {
                userid = rs.getString("userid");
                name = rs.getString("name");
                ssn = rs.getString("ssn");
                lotacao = rs.getString("deptname");
                cargo = rs.getString("cargo");
                regime = rs.getString("regime");
            }
            infoList.add(userid);
            infoList.add(name);

            if (ssn != null) {
                infoList.add(ssn);
            } else {
                infoList.add("");
            }
            if (lotacao != null) {
                infoList.add(lotacao);
            } else {
                infoList.add("");
            }

            if (cargo != null) {
                infoList.add(cargo);

            } else {
                infoList.add("");
            }

            if (regime != null) {
                infoList.add(regime);
            } else {
                infoList.add("");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return infoList;
    }

    public List<Integer> consultaFuncionarioDepartamento(String dept) {
        List<Integer> matriculasList = new ArrayList<Integer>();
        try {
            String subSelect;
            subSelect = "select DEPTID FROM DEPARTMENTS WHERE DEPTNAME = '" + dept + "'";
            String sql = "select u.userid from userinfo u,user_of_run r"
                    + " where defaultdeptid = (" + subSelect + ")"
                    + " and" + " r.userid = u.userid"
                    + " order by name";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String userid = rs.getString("userid");
                matriculasList.add(Integer.parseInt(userid));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return matriculasList;
    }

    public ArrayList<Integer> consultaCicloDias(Integer num) {
        ArrayList<Integer> daysList = new ArrayList<Integer>();
        try {
            String sql = "select sdays,edays from num_run_deil"
                    + " where num_runid = " + num;
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer sdays = rs.getInt("sdays");
                Integer edays = rs.getInt("edays");

                if (!daysList.contains(sdays)) {
                    daysList.add(sdays);
                }
                if (!daysList.contains(edays)) {
                    daysList.add(edays);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return daysList;
    }

    public HashMap<String, ArrayList<Jornada>> consultaDiasDeslocados(Integer cod, Date inicio, Date fim) {
        HashMap<String, ArrayList<Jornada>> diasComDeslocamento = new HashMap<String, ArrayList<Jornada>>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime() + 172799998);

            String sql = "select uts.cometime,uts.leavetime,uts.overtime,uts.folga,sc.*"
                    + " from user_temp_sch uts left join SchClass sc on uts.schclassid = sc.schclassid"
                    + " where uts.userid = " + cod + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimSrt + "' "
                    + " order by cometime";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Date cometime = rs.getDate("cometime");
                Date leavetime = rs.getDate("leavetime");
                Boolean overtime = rs.getInt("overtime") == 1 ? true : false;
                String schName = rs.getString("schName");
                Integer schClassID = -1;
                if (rs.getString("schClassID") != null) {
                    schClassID = rs.getInt("schClassID");
                }
                String legend = rs.getString("legend");
                Boolean folga = rs.getBoolean("folga");
                Timestamp startTime = rs.getTimestamp("startTime");
                Timestamp endTime = rs.getTimestamp("endTime");
                Integer lateMinutes = rs.getInt("lateMinutes");
                Integer earlyMinutes = rs.getInt("earlyMinutes");
                Integer checkIn = rs.getInt("CHECKIN");
                Integer checkOut = rs.getInt("CHECKOUT");
                Timestamp checkInTime1 = rs.getTimestamp("checkInTime1");
                Timestamp checkInTime2 = rs.getTimestamp("checkInTime2");
                Timestamp checkOutTime1 = rs.getTimestamp("checkOutTime1");
                Timestamp checkOutTime2 = rs.getTimestamp("checkOutTime2");
                Float workday = rs.getFloat("workday");
                Timestamp inicioDescanso1 = rs.getTimestamp("RESTINTIME1");
                Timestamp inicioDescanso2 = rs.getTimestamp("RESTINTIME2");
                Timestamp inicioIntrajornada = rs.getTimestamp("InterRestIn");
                Timestamp fimIntrajornada = rs.getTimestamp("InterRestOut");;
                Timestamp fimDescanso1 = rs.getTimestamp("RESTOUTTIME1");
                Timestamp fimDescanso2 = rs.getTimestamp("RESTOUTTIME2");
                Integer tolerancia = rs.getInt("RestTolerance");
                Boolean deduzirDescanco1 = rs.getBoolean("ReduceRestTime1");
                Boolean deduzirDescanco2 = rs.getBoolean("ReduceRestTime2");
                Boolean deduzirIntrajornada = rs.getBoolean("ReduceInterRest");
                Boolean combinarEntrada = rs.getBoolean("CombineIn");
                Boolean combinarSaida = rs.getBoolean("CombineOut");

                /*if (inicioDescanso1 == null && inicioDescanso2 == null) {
                 inicioDescanso1 = null;
                 fimDescanso1 = null;
                 inicioIntrajornada = null;
                 fimIntrajornada = null;
                 inicioDescanso2 = null;
                 fimDescanso2 = null;
                 } else if (inicioDescanso1 != null) {
                 inicioIntrajornada = null;
                 fimIntrajornada = null;
                 inicioDescanso2 = null;
                 fimDescanso2 = null;
                 }*/
                Jornada jornada = new Jornada();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String cometimeSrt = sdf.format(cometime.getTime());
                String leavetimeSrt = sdf.format(leavetime.getTime());

                if (cometimeSrt.equals(leavetimeSrt)) {
                    jornada.setTemVirada(false);
                    jornada.setNome(schName);
                    jornada.setSchClassID(schClassID);
                    jornada.setFloga(folga);
                    jornada.setLegend(legend);
                    jornada.setInicioJornada(startTime);
                    jornada.setTerminioJornada(endTime);
                    jornada.setInicioJornadaL(startTime);
                    jornada.setTerminioJornadaL(endTime);
                    jornada.setPrazoMinutosAtraso(lateMinutes);
                    jornada.setPrazoMinutosAdiantado(earlyMinutes);
                    jornada.setCheckInLimiteAntecipada(checkInTime1);
                    jornada.setCheckInLimiteAtrasada(checkInTime2);
                    jornada.setIsEntradaObrigatoria(checkIn);
                    jornada.setIsSaidaObrigatoria(checkOut);
                    jornada.setCheckOutLimiteAntecipada(checkOutTime1);
                    jornada.setCheckOutLimiteAtrasada(checkOutTime2);
                    jornada.setTipoJornada(workday);

                    jornada.setInicioDescanso1(inicioDescanso1);
                    jornada.setFimDescanso1(fimDescanso1);
                    jornada.setDeduzirDescanco1(deduzirDescanco1);
                    jornada.setInicioIntrajornada(inicioIntrajornada);
                    jornada.setFimIntrajornada(fimIntrajornada);
                    jornada.setDeduzirIntrajornada(deduzirIntrajornada);
                    jornada.setCombinarEntrada(combinarEntrada);
                    jornada.setCombinarSaida(combinarSaida);
                    jornada.setInicioDescanso2(inicioDescanso2);
                    jornada.setFimDescanso2(fimDescanso2);
                    jornada.setDeduzirDescanco2(deduzirDescanco2);
                    jornada.setToleranciaDescanso(tolerancia);

                    jornada.setIsOvertime(overtime);

                    ArrayList schList = new ArrayList();

                    if (diasComDeslocamento.get(cometimeSrt) != null) {
                        schList = diasComDeslocamento.get(cometimeSrt);
                    }
                    schList.add(jornada);
                    diasComDeslocamento.put(cometimeSrt, schList);

                } else {
                    jornada.setTemVirada(true);
                    jornada.setNome(schName);
                    jornada.setSchClassID(schClassID);
                    jornada.setFloga(folga);
                    jornada.setLegend(legend);
                    jornada.setInicioJornada(startTime);
                    jornada.setInicioJornadaL(startTime);
                    jornada.setTerminioJornadaL(endTime);
                    jornada.setPrazoMinutosAtraso(lateMinutes);
                    jornada.setPrazoMinutosAdiantado(earlyMinutes);
                    jornada.setCheckInLimiteAntecipada(checkInTime1);
                    jornada.setCheckInLimiteAtrasada(checkInTime2);
                    jornada.setIsEntradaObrigatoria(checkIn);
                    jornada.setIsSaidaObrigatoria(checkOut);
                    jornada.setCheckOutLimiteAntecipada(checkOutTime1);
                    jornada.setCheckOutLimiteAtrasada(checkOutTime2);
                    jornada.setTipoJornada(workday);
                    jornada.setSoEntrada(true);
                    jornada.setSoSaida(false);

                    jornada.setInicioDescanso1(inicioDescanso1);
                    jornada.setFimDescanso1(fimDescanso1);
                    jornada.setDeduzirDescanco1(deduzirDescanco1);
                    jornada.setInicioIntrajornada(inicioIntrajornada);
                    jornada.setFimIntrajornada(fimIntrajornada);
                    jornada.setDeduzirIntrajornada(deduzirIntrajornada);
                    jornada.setCombinarEntrada(combinarEntrada);
                    jornada.setCombinarSaida(combinarSaida);
                    jornada.setInicioDescanso2(inicioDescanso2);
                    jornada.setFimDescanso2(fimDescanso2);
                    jornada.setDeduzirDescanco2(deduzirDescanco2);
                    jornada.setToleranciaDescanso(tolerancia);

                    jornada.setIsOvertime(overtime);

                    ArrayList schComeList = new ArrayList();

                    if (diasComDeslocamento.get(cometimeSrt) != null) {
                        schComeList = diasComDeslocamento.get(cometimeSrt);
                    }
                    schComeList.add(jornada);
                    diasComDeslocamento.put(cometimeSrt, schComeList);

                    ArrayList schLeaveList = new ArrayList();
                    Jornada jornada2 = new Jornada();
                    jornada2.setTemVirada(true);
                    jornada2.setNome(schName);
                    jornada2.setFloga(folga);
                    jornada2.setSchClassID(schClassID);
                    jornada2.setLegend(legend);
                    jornada2.setInicioJornadaL(startTime);
                    jornada2.setTerminioJornadaL(endTime);
                    jornada2.setTerminioJornada(endTime);
                    jornada2.setPrazoMinutosAtraso(lateMinutes);
                    jornada2.setPrazoMinutosAdiantado(earlyMinutes);
                    jornada2.setCheckInLimiteAntecipada(checkInTime1);
                    jornada2.setCheckInLimiteAtrasada(checkInTime2);
                    jornada2.setIsEntradaObrigatoria(checkIn);
                    jornada2.setIsSaidaObrigatoria(checkOut);
                    jornada2.setCheckOutLimiteAntecipada(checkOutTime1);
                    jornada2.setCheckOutLimiteAtrasada(checkOutTime2);
                    jornada2.setTipoJornada(workday);
                    jornada2.setSoSaida(true);
                    jornada2.setSoEntrada(false);
                    jornada2.setIsOvertime(overtime);

                    jornada2.setInicioDescanso1(inicioDescanso1);
                    jornada2.setFimDescanso1(fimDescanso1);
                    jornada2.setDeduzirDescanco1(deduzirDescanco1);
                    jornada2.setInicioIntrajornada(inicioIntrajornada);
                    jornada2.setFimIntrajornada(fimIntrajornada);
                    jornada2.setDeduzirIntrajornada(deduzirIntrajornada);
                    jornada2.setCombinarEntrada(combinarEntrada);
                    jornada2.setCombinarSaida(combinarSaida);
                    jornada2.setInicioDescanso2(inicioDescanso2);
                    jornada2.setFimDescanso2(fimDescanso2);
                    jornada2.setDeduzirDescanco2(deduzirDescanco2);
                    jornada2.setToleranciaDescanso(tolerancia);

                    jornada2.setIsOvertime(overtime);

                    if (diasComDeslocamento.get(leavetimeSrt) != null) {
                        schLeaveList = diasComDeslocamento.get(leavetimeSrt);
                    }
                    schLeaveList.add(jornada2);
                    diasComDeslocamento.put(leavetimeSrt, schLeaveList);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diasComDeslocamento;
    }

    public ArrayList<DescolamentoTemporario> consultaHorarioRemover(Integer cod, Date inicio, Date fim) {
        ArrayList<DescolamentoTemporario> listaDeslocamentoTemporario = new ArrayList<DescolamentoTemporario>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime() + 172799998);

            String sql = "select uts.*"
                    + " from user_temp_sch uts"
                    + " where uts.userid = " + cod + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimSrt + "' "
                    + "and schclassid = -1"
                    + " order by cometime";

            ResultSet rs = con.executeQuery(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Date cometime = rs.getDate("cometime");
                Date leavetime = rs.getDate("leavetime");
                Boolean isDiaExtra = rs.getBoolean("overtime");
                Boolean folga = rs.getBoolean("folga");
                Integer schClassId = rs.getInt("schClassId");
                DescolamentoTemporario deslTemp = new DescolamentoTemporario(userid, cometime, leavetime, isDiaExtra, folga, schClassId);
                String data = sdf.format(cometime.getTime());
                listaDeslocamentoTemporario.add(deslTemp);

            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return listaDeslocamentoTemporario;
    }

    public ArrayList<DescolamentoTemporario> consultaDeslocamentoTemp(Integer cod, Date inicio, Date fim) {
        ArrayList<DescolamentoTemporario> listaDeslocamentoTemporario = new ArrayList<DescolamentoTemporario>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime() + 172799998);

            String sql = "select uts.*"
                    + " from user_temp_sch uts"
                    + " where uts.userid = " + cod + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimSrt + "' "
                    + " and schclassid <> -1"
                    + " order by cometime";

            ResultSet rs = con.executeQuery(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Date cometime = rs.getDate("cometime");
                Date leavetime = rs.getDate("leavetime");
                Boolean isDiaExtra = rs.getBoolean("overtime");
                Boolean folga = rs.getBoolean("folga");
                Integer schClassId = rs.getInt("schClassId");
                DescolamentoTemporario deslTemp = new DescolamentoTemporario(userid, cometime, leavetime, isDiaExtra, folga, schClassId);
                String data = sdf.format(cometime.getTime());
                listaDeslocamentoTemporario.add(deslTemp);

            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return listaDeslocamentoTemporario;
    }

    public HashMap<String, DescolamentoTemporario> consultaDiasDeslocamentoTemp(int cod, Date inicio, Date fim) {
        HashMap<String, DescolamentoTemporario> diasDeslocamentoTempHashMap = new HashMap<String, DescolamentoTemporario>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimSrt = sdfHora.format(fim.getTime() + 172799998);

            String sql = "select uts.*"
                    + " from user_temp_sch uts"
                    + " where uts.userid = " + cod + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimSrt + "' "
                    + " order by cometime";
            ResultSet rs = con.executeQuery(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                DescolamentoTemporario deslTemp = new DescolamentoTemporario(rs.getInt("userid"), rs.getDate("cometime"), rs.getDate("leavetime"), rs.getBoolean("overtime"), rs.getBoolean("folga"), rs.getInt("schClassId"));
                String data = sdf.format(rs.getDate("cometime").getTime());
                if (diasDeslocamentoTempHashMap.get(data) == null) {
                    diasDeslocamentoTempHashMap.put(data, deslTemp);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diasDeslocamentoTempHashMap;
    }

    public List<Afastamento> consultaAfastamento(Integer cod_funcionario, Date inicio, Date fim) {
        List<Afastamento> afastamentoList = new ArrayList<Afastamento>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime());

            String sql = "SELECT u.name,u.userid,u.defaultdeptid, a.*, ca.*, "
                    + "(select name from userinfo where userid = a.cod_responsavel) as responsavel"
                    + " FROM Afastamento a, USERINFO u, CategoriaAfastamento ca"
                    + " where u.userid = a.userid and "
                    + " u.userid = " + cod_funcionario + "and "
                    + " ca.cod_categoria = a.cod_categoria and "
                    + " ((a.inicioAfastamento >= '" + inicioStr + "' and a.inicioAfastamento <= '" + fimStr + "') or "
                    + " (a.fimAfastamento >= '" + inicioStr + "' and a.fimAfastamento <= '" + fimStr + "') or "
                    + " (a.inicioAfastamento <= '" + inicioStr + "' and a.fimAfastamento >= '" + fimStr + "')) "
                    + " order by name";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                //    Integer userid = rs.getInt("userid");
                Date inicioAfastamento = rs.getDate("inicioAfastamento");
                Date fimAfastamento = rs.getDate("fimAfastamento");
                Integer codCatAfastamento = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String legendaAfastamento = rs.getString("legendaAfastamento");

                Afastamento afastamento = new Afastamento();
                afastamento.setDataInicio(inicioAfastamento);
                afastamento.setDataFim(fimAfastamento);
                afastamento.setCodCategoriaAfastamento(codCatAfastamento);
                afastamentoList.add(afastamento);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return afastamentoList;
    }

    public Date consultaStartDate(Integer num, Date inicioPeriodo, Date dataInicio) {
        Date startdate = new Date();
        try {
            String sql = "select startdate,units,cyle from num_run"
                    + " where num_runid = " + num;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                //startdate = rs.getDate("startdate");
                startdate = inicioPeriodo;
                Integer unit = rs.getInt("units");
                Integer cyle = rs.getInt("cyle");
                //startdate = geraDiaInicialDeApuracao(startdate, inicioPeriodo, unit, cyle);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return startdate;
    }

    public Date geraDiaInicialDeApuracao(Date dataJornada, Date dataUsuario, Integer unidade, Integer ciclos) {

        if (unidade == 1) {
            ciclos = ciclos * 7;
        }
        if (unidade == 2) {
            ciclos = ciclos * 31;
        }

        GregorianCalendar calendarJornada = new GregorianCalendar();

        calendarJornada.setTime(dataJornada);
        long jor = calendarJornada.getTimeInMillis();

        GregorianCalendar calendarUsuario = new GregorianCalendar();
        calendarUsuario.setTime(dataUsuario);
        long user = calendarUsuario.getTimeInMillis();
        Date dateInicial = null;
        if (jor < user) {
            long diferenca = Math.abs(jor - user) / 86400000;
            long deslocamento = (diferenca % ciclos) - ciclos;
            long diaapurado = user + (deslocamento * 86400000);
            GregorianCalendar apurador = new GregorianCalendar();
            apurador.setTimeInMillis(diaapurado);
            dateInicial = apurador.getTime();
        } else {
            long diferenca = Math.abs(jor - user) / 86400000;
            long deslocamento = (diferenca % ciclos);
            long diaapurado = user - (deslocamento * 86400000);
            GregorianCalendar apurador = new GregorianCalendar();
            apurador.setTimeInMillis(diaapurado);
            dateInicial = apurador.getTime();
        }
        return dateInicial;

    }

    static String formateHora(String diaHora) {
        String[] hora = diaHora.split(" ");
        return hora[1];
    }

    public List<Integer> consultaDiasDeslocadosSemTrabalho(Integer userID, Date inicio, Date fim) {

        List<Integer> semTrabalhoList = new ArrayList<Integer>();
        List<Integer> deslocadosList = new ArrayList<Integer>();
        List<Integer> returnList = new ArrayList<Integer>();

        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 172799998);

            String sql = " select cometime,leavetime"
                    + " from user_temp_sch "
                    + " where userid = " + userID + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimStr + "' and"
                    + " schclassid = -1";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Date inicioDate = rs.getDate("cometime");
                Date fimDate = rs.getDate("leavetime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(inicioDate.getTime());
                Integer diaInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!semTrabalhoList.contains(diaInicio)) {
                    semTrabalhoList.add(diaInicio);
                }
                dia.setTimeInMillis(fimDate.getTime());
                Integer fimInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!semTrabalhoList.contains(fimInicio)) {
                    semTrabalhoList.add(fimInicio);
                }
            }

            rs.close();
            sql = " select cometime,leavetime"
                    + " from user_temp_sch "
                    + " where userid = " + userID + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimStr + "' and"
                    + " schclassid != -1";

            rs = con.executeQuery(sql);

            while (rs.next()) {
                Date inicioDate = rs.getDate("cometime");
                Date fimDate = rs.getDate("leavetime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(inicioDate.getTime());
                Integer diaInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!deslocadosList.contains(diaInicio)) {
                    deslocadosList.add(diaInicio);
                }

                dia.setTimeInMillis(fimDate.getTime());
                Integer fimInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!deslocadosList.contains(fimInicio)) {
                    deslocadosList.add(fimInicio);
                }
            }

            rs.close();

            for (Iterator<Integer> it = semTrabalhoList.iterator(); it.hasNext();) {
                Integer folga = it.next();

                if (!deslocadosList.contains(folga)) {
                    returnList.add(folga);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return returnList;
    }

    public List<Integer> consultaDiasDeslocadosAdicionais(Integer userID, Date inicio, Date fim) {
        List<Integer> diasDeslocado = new ArrayList<Integer>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = " select cometime,leavetime"
                    + " from user_temp_sch "
                    + " where userid = " + userID + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimStr + "' and"
                    + " schclassid != -1";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Date inicioDate = rs.getDate("cometime");
                Date fimDate = rs.getDate("leavetime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(inicioDate.getTime());
                Integer diaInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!diasDeslocado.contains(diaInicio)) {
                    diasDeslocado.add(diaInicio);
                }

                dia.setTimeInMillis(fimDate.getTime());
                Integer fimInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!diasDeslocado.contains(fimInicio)) {
                    diasDeslocado.add(fimInicio);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diasDeslocado;
    }

    public List<TipoHoraExtra> consultaTipoHoraExtra(int userID) {
        List<TipoHoraExtra> tipoHoraExtraList = new ArrayList<>();
        List<DetalheHoraExtra> detalheHoraExtraList = new ArrayList<>();
        try {
            String sql = "select th.*,dh. * "
                    + " from userinfo u, tipo_HoraExtra th, detalhe_HoraExtra dh"
                    + " where u.userid = " + userID + " and "
                    + " u.cod_regime = th.cod_regime and"
                    + " th.cod_regime = u.cod_regime and "
                    + " dh.cod_tipo = th.cod_tipo"
                    + " order by th.cod_tipo";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                TipoHoraExtra tipoHoraExtra = new TipoHoraExtra(rs.getInt("cod_regime"), rs.getInt("cod_tipo"), rs.getString("nome"), rs.getBoolean("padrao"));
                tipoHoraExtra.setValor(rs.getFloat("valor"));
                tipoHoraExtra.setVerba(rs.getString("verba"));
                DetalheHoraExtra detalhaHoraExtra = new DetalheHoraExtra(rs.getInt("cod_tipo"), rs.getInt("dia"), rs.getFloat("inicio"), rs.getBoolean("dia_inteiro"));
                detalheHoraExtraList.add(detalhaHoraExtra);

                if (detalheHoraExtraList.size() == 8) {
                    tipoHoraExtra.setDetalheHoraExtra(detalheHoraExtraList);
                    tipoHoraExtraList.add(tipoHoraExtra);
                    detalheHoraExtraList = new ArrayList<>();
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return tipoHoraExtraList;
    }

    public List<SelectItem> consultaCategoriaHoraExtra(Integer userid) {
        List<SelectItem> categoriaHoraExtraSelectItem = new ArrayList<SelectItem>();
        try {
            String sql = "select * from categoriaHoraExtra "
                    + " where cod_regime = (select cod_regime from userinfo where userid = " + userid + " )";

            ResultSet rs = con.executeQuery(sql);

            categoriaHoraExtraSelectItem.add(new SelectItem(-1, "Escolha a categoria"));
            while (rs.next()) {
                Integer cod = rs.getInt("cod");
                String descricao = rs.getString("descricao");
                categoriaHoraExtraSelectItem.add(new SelectItem(cod, descricao));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return categoriaHoraExtraSelectItem;
    }

    public Integer consultaCodCategoriahoraExtra(Integer userid, Date data) {
        Integer cod_categoria = -1;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataStr = sdf.format(data.getTime());
            String sql = "select cod_categoria from HoraExtra "
                    + " where userid = " + userid + " and "
                    + " data = '" + dataStr + "'";

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                cod_categoria = rs.getInt("cod_categoria");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_categoria;
    }

    public boolean insertRelatorioPortaria1510(List<HorarioContratual> horarioContratualList, List<RelatorioPortaria1510> relatorioPortaria1510) {
        boolean ok = true;

        String query1 = "insert into RelatorioHorarioContratual values(?,?,?,?,?,?,?,?,?)";
        String queryDelete1 = "truncate table RelatorioHorarioContratual";
        String query2 = "insert into RelatorioPortaria1510Resumo(dia,marcacoes,entrada1,entrada2,saida1,"
                + "saida2,horasDia,saldo,ch, EntradaCatraca, SaidaCatraca) values(?,?,?,?,?,?,?,?,?,?,?)";
        String queryDelete2 = "truncate table RelatorioPortaria1510Resumo";
        con.executeUpdate(queryDelete1);
        con.executeUpdate(queryDelete2);
        con.prepareStatement(query1);

        try {
            for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
                HorarioContratual horarioContratual = it.next();

                con.pstmt.setString(1, horarioContratual.getCod_horario());
                con.pstmt.setString(2, horarioContratual.getEntrada1());
                con.pstmt.setString(3, horarioContratual.getSaida1());
                con.pstmt.setString(4, horarioContratual.getEntrada2());
                con.pstmt.setString(5, horarioContratual.getSaida2());
                con.pstmt.setString(6, horarioContratual.getEntradaD1());
                con.pstmt.setString(7, horarioContratual.getSaidaD1());
                con.pstmt.setString(8, horarioContratual.getEntradaD2());
                con.pstmt.setString(9, horarioContratual.getSaidaD2());

                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        }
        con.prepareStatement(query2);

        try {
            for (Iterator<RelatorioPortaria1510> it = relatorioPortaria1510.iterator(); it.hasNext();) {
                RelatorioPortaria1510 relatorioPortaria1510_ = it.next();

                con.pstmt.setString(1, relatorioPortaria1510_.getDia());
                con.pstmt.setString(2, relatorioPortaria1510_.getMarcacoesRegistradas());
                con.pstmt.setString(3, relatorioPortaria1510_.getEntrada1());
                con.pstmt.setString(4, relatorioPortaria1510_.getEntrada2());
                con.pstmt.setString(5, relatorioPortaria1510_.getSaida1());
                con.pstmt.setString(6, relatorioPortaria1510_.getSaida2());
                con.pstmt.setString(7, relatorioPortaria1510_.getHorasDias());
                con.pstmt.setString(8, relatorioPortaria1510_.getSaldo());
                con.pstmt.setString(9, relatorioPortaria1510_.getCh());
                if (relatorioPortaria1510_.getEntradasCatraca() != null) {
                    con.pstmt.setString(10, relatorioPortaria1510_.getEntradasCatraca());
                } else {
                    con.pstmt.setString(10, "");
                }
                if (relatorioPortaria1510_.getSaidasCatraca() != null) {
                    con.pstmt.setString(11, relatorioPortaria1510_.getSaidasCatraca());
                } else {
                    con.pstmt.setString(11, "");
                }
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        Boolean flag2 = insertRelatorioPortaria1510Detalhe(relatorioPortaria1510);
        if (flag2.equals(false)) {
            ok = false;
        }
        return ok;
    }

    public boolean insertRelatorioPortaria1510Detalhe(List<RelatorioPortaria1510> relatorioPortaria1510List) {
        boolean ok = true;

        String query = "insert into RelatorioPortaria1510Detalhe(cod_dia,horario,ocorrencia,motivo) values(?,?,?,?)";
        String queryDelete = "truncate table RelatorioPortaria1510Detalhe";
        con.executeUpdate(queryDelete);
        con.prepareStatement(query);

        try {
            Integer i = 1;
            for (Iterator<RelatorioPortaria1510> it = relatorioPortaria1510List.iterator(); it.hasNext();) {
                RelatorioPortaria1510 relatorioPortaria1510 = it.next();

                List<PontoIrreal> pontosIrreaisList = relatorioPortaria1510.getPontoIrrealList();

                for (Iterator<PontoIrreal> it1 = pontosIrreaisList.iterator(); it1.hasNext();) {
                    PontoIrreal pontoIrreal = it1.next();
                    con.pstmt.setInt(1, i);
                    con.pstmt.setString(2, pontoIrreal.getHora());
                    con.pstmt.setString(3, pontoIrreal.getTipo());
                    con.pstmt.setString(4, pontoIrreal.getMotivo());
                    con.executeUpdate();
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ok = false;
            try {
                con.c.rollback();
            } catch (SQLException ex) {
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public String consultaJustificativaHoraExtra(Integer userid, Date data) {
        String justificativa = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataStr = sdf.format(data.getTime());
            String sql = "select justificativa from HoraExtra "
                    + " where userid = " + userid + " and "
                    + " data = '" + dataStr + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                justificativa = rs.getString("justificativa");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return justificativa;
    }

    public Integer consultaTipoRelatorio() {
        Integer tipoRelatorio = new Integer(0);
        try {
            String sql = " select tipoRelatorio from config";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                tipoRelatorio = rs.getInt("tipoRelatorio");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return tipoRelatorio;
    }

    public Integer consultaDias(Integer numrun_id) {
        Integer maximo = 0;
        try {
            String sql = "select max(edays) as maximo from"
                    + " num_run_deil"
                    + " where num_runid = " + numrun_id;

            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                maximo = rs.getInt("maximo");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return maximo;
    }

    public List<String> consultaDiasHoraExtra(Integer userid, Date inicio, Date fim) {

        List<String> diasHoraExtraList = new ArrayList<String>();
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = "select data"
                    + " from  HoraExtra "
                    + " where userid = " + userid + " and "
                    + " data between '" + inicioStr + "' and '" + fimStr + "'";

            ResultSet rs = con.executeQuery(sql);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            while (rs.next()) {
                Date date = rs.getDate("data");
                diasHoraExtraList.add(sdf.format(date.getTime()));
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diasHoraExtraList;
    }

    public boolean insertHoraExtra(DiaHoraExtra diaHoraExtra) {
        boolean ok = true;

        String query = "insert into HoraExtra(userid,data,cod_categoria,justificativa) values(?,?,?,?)";

        try {
            con.prepareStatement(query);

            con.pstmt.setInt(1, diaHoraExtra.getUserid());
            con.pstmt.setDate(2, new java.sql.Date(diaHoraExtra.getData().getTime()));
            con.pstmt.setInt(3, diaHoraExtra.getCod_categoria());
            con.pstmt.setString(4, diaHoraExtra.getJustificativa());

            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public void alterarRegistroPonto(String cod_funcionario, String registro, Integer tipoRegistro) {

        String query = "update checkinout set tipoRegistro=? "
                + " where userid=? and checktime=?";
        tipoRegistro = tipoRegistro == 0 ? null : tipoRegistro;
        try {
            con.prepareStatement(query);
            if (tipoRegistro == null) {
                con.pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                con.pstmt.setInt(1, tipoRegistro);
            }
            con.pstmt.setString(2, cod_funcionario);
            con.pstmt.setString(3, registro);

            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public void alterarRegistroPontoAbonado(String cod_funcionario, String registro, Integer tipoRegistro) {

        String query = "update REGISTRO_ABONO set tipoRegistro=? "
                + " where userid=? and checktime=?";
        tipoRegistro = tipoRegistro == 0 ? null : tipoRegistro;
        try {
            con.prepareStatement(query);
            if (tipoRegistro == null) {
                con.pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                con.pstmt.setInt(1, tipoRegistro);
            }
            con.pstmt.setString(2, cod_funcionario);
            con.pstmt.setString(3, registro);

            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public boolean insertHoraExtraEmMassa(Integer cod_funcionario, Date dataInicio, Date dataFim, List<DiaHoraExtra> diaHoraExtraList) {
        boolean ok = true;

        String query = "insert into HoraExtra(userid,data,cod_categoria,justificativa) values(?,?,?,?)";
        String queryDelete = "delete from HoraExtra where userid = ? and data between ? and ?";

        try {
            con.prepareStatement(queryDelete);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicioStr = sdf.format(dataInicio.getTime());
            String dataFimStr = sdf.format(dataFim.getTime());
            con.pstmt.setInt(1, cod_funcionario);
            con.pstmt.setString(2, dataInicioStr);
            con.pstmt.setString(3, dataFimStr);
            con.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("ConsultaPonto: insertHoraExtraEmMassa 1: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            con.prepareStatement(query);
            con.c.setAutoCommit(false);
            for (Iterator<DiaHoraExtra> it = diaHoraExtraList.iterator(); it.hasNext();) {
                DiaHoraExtra diaHoraExtra = it.next();
                con.pstmt.setInt(1, diaHoraExtra.getUserid());
                con.pstmt.setDate(2, new java.sql.Date(diaHoraExtra.getData().getTime()));
                con.pstmt.setInt(3, diaHoraExtra.getCod_categoria());
                con.pstmt.setString(4, diaHoraExtra.getJustificativa());
                con.executeUpdate();
            }
            con.c.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                con.c.rollback();
            } catch (SQLException ex) {
                System.out.println("ConsultaPonto: insertHoraExtraEmMassa 2: " + ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean editarHoraExtra(DiaHoraExtra diaHoraExtra) {
        boolean ok = true;
        String query = "update HoraExtra set cod_categoria=?, justificativa=? "
                + " where userid=? and data=?";
        try {
            con.prepareStatement(query);
            con.pstmt.setInt(1, diaHoraExtra.getCod_categoria());
            con.pstmt.setString(2, diaHoraExtra.getJustificativa());
            con.pstmt.setInt(3, diaHoraExtra.getUserid());
            con.pstmt.setDate(4, new java.sql.Date(diaHoraExtra.getData().getTime()));
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean excluirHoraExtra(DiaHoraExtra diaHoraExtra) {
        boolean ok = true;
        String query = "delete from HoraExtra "
                + " where userid=? and data=?";
        try {
            con.prepareStatement(query);
            con.pstmt.setInt(1, diaHoraExtra.getUserid());
            con.pstmt.setDate(2, new java.sql.Date(diaHoraExtra.getData().getTime()));
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public List<Date> getExtraAntesDeDeslocamento(Integer userid, Date dia) {
        List<Date> data = new ArrayList<Date>();
        try {
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(dia.getTime());

            String sql = " select * from user_temp_sch "
                    + " where userid = " + userid + " and schclassid != -1 and overtime = 0 and "
                    + " convert(nvarchar(10), cometime, 103) = '" + dateStr + "' and "
                    + " CONVERT(nvarchar(10),  cometime, 103) != CONVERT(nvarchar(10), leavetime, 103) and "
                    + " CONVERT(nvarchar(10),  cometime, 103) in (select CONVERT(nvarchar(10), s1.leavetime, 103) "
                    + " from user_temp_sch s1 where userid = " + userid + " and schclassid != -1 and overtime = 1 ) ";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                data.add(rs.getTimestamp("cometime"));
                data.add(rs.getTimestamp("leavetime"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return data;
    }

    public List<Date> getExtraDepoisDeDeslocamento(Integer userid, Date dia) {

        List<Date> data = new ArrayList<Date>();
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(dia.getTime());

            String sql = " select * from user_temp_sch "
                    + " where userid = " + userid + " and schclassid != -1 and overtime = 0 and "
                    + " convert(nvarchar(10), leavetime, 103) = '" + dateStr + "' and "
                    + " CONVERT(nvarchar(10),  cometime, 103) != CONVERT(nvarchar(10), leavetime, 103) and "
                    + " CONVERT(nvarchar(10),  leavetime, 103) in (select CONVERT(nvarchar(10), s1.cometime, 103) "
                    + " from user_temp_sch s1 where userid = " + userid + " and schclassid != -1 and overtime = 1 ) ";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                data.add(rs.getTimestamp("cometime"));
                data.add(rs.getTimestamp("leavetime"));
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public Boolean consultaExtraAntesDeDeslocamento(Integer userid, Date dia) {

        Boolean contemExtra = false;
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(dia.getTime());

            String sql = " select * from user_temp_sch "
                    + " where userid = " + userid + " and schclassid != -1 and overtime = 0 and "
                    + " convert(nvarchar(10), leavetime, 103) = '" + dateStr + "' and "
                    + " CONVERT(nvarchar(10),  cometime, 103) != CONVERT(nvarchar(10), leavetime, 103) and "
                    + " CONVERT(nvarchar(10),  cometime, 103) in (select CONVERT(nvarchar(10), s1.leavetime, 103) "
                    + " from user_temp_sch s1 where userid = " + userid + " and schclassid != -1 and overtime = 1 ) ";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                contemExtra = true;
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return contemExtra;
    }

    public Boolean consultaExtraDepoisDeDeslocamento(Integer userid, Date dia) {

        Boolean contemExtra = false;
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(dia.getTime());

            String sql = " select * from user_temp_sch "
                    + " where userid = " + userid + " and schclassid != -1 and overtime = 0 and "
                    + " convert(nvarchar(10), cometime, 103) = '" + dateStr + "' and "
                    + " CONVERT(nvarchar(10),  cometime, 103) != CONVERT(nvarchar(10), leavetime, 103) and "
                    + " CONVERT(nvarchar(10),  leavetime, 103) in (select CONVERT(nvarchar(10), s1.cometime, 103) "
                    + " from user_temp_sch s1 where userid = " + userid + " and schclassid != -1 and overtime = 1 )";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                contemExtra = true;
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return contemExtra;
    }

    //função que veriica os dias deslocados que possuem hora extra
    public Long consultaDeslocadosHoraContratada(Integer userID, Date date) {

        Long miliseconds = 0L;;

        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = sdfHora.format(date.getTime());

            String sql = " select cometime,leavetime from user_temp_sch "
                    + " where userid = " + userID + " and schclassid != -1 and overtime = 0 "
                    + " and convert(nvarchar(10), leavetime, 103) = '" + dateStr + "'  "
                    + " and CONVERT(nvarchar(10), leavetime, 103)  in  (select CONVERT(nvarchar(10), s1.cometime, 103) "
                    + " from user_temp_sch s1 where userid = " + userID + " and schclassid != -1 and overtime = 1 ) "
                    + " union"
                    + " select cometime,leavetime from user_temp_sch "
                    + " where userid = " + userID + " and schclassid != -1 and overtime = 0 "
                    + " and convert(nvarchar(10), cometime, 103) = '" + dateStr + "'  "
                    + " and CONVERT(nvarchar(10), cometime, 103)  in  (select CONVERT(nvarchar(10), s1.leavetime, 103) "
                    + " from user_temp_sch s1 where userid = " + userID + " and schclassid != -1 and overtime = 1 ) ";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp inicioDate = rs.getTimestamp("cometime");
                Timestamp fimDate = rs.getTimestamp("leavetime");

                GregorianCalendar diaInicio = new GregorianCalendar();
                GregorianCalendar diaFim = new GregorianCalendar();

                diaInicio.setTimeInMillis(inicioDate.getTime());
                diaFim.setTimeInMillis(fimDate.getTime());

                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(diaFim.getTimeInMillis() - diaInicio.getTimeInMillis());

                miliseconds = dia.getTimeInMillis();
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return miliseconds;

    }

    public List<Integer> consultaDiasDeslocadosHoraExtra(int userID, Date inicio, Date fim) {
        List<Integer> diasDeslocadoHoraExtra = new ArrayList<Integer>();
        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = " select cometime,leavetime"
                    + " from user_temp_sch "
                    + " where userid = " + userID + " and "
                    + " cometime between '" + inicioStr + "' and '" + fimStr + "' and"
                    + " schclassid != -1 and "
                    + " overtime = 1";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Date inicioDate = rs.getDate("cometime");
                Date fimDate = rs.getDate("leavetime");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(inicioDate.getTime());
                int diaInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;
                if (!diasDeslocadoHoraExtra.contains(diaInicio)) {

                    diasDeslocadoHoraExtra.add(diaInicio);
                }

                dia.setTimeInMillis(fimDate.getTime());
                int fimInicio = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!diasDeslocadoHoraExtra.contains(fimInicio)) {

                    diasDeslocadoHoraExtra.add(fimInicio);
                }
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return diasDeslocadoHoraExtra;
    }

    public boolean isFeriadoCritico(Integer cod_funcionario) {
        boolean isFeriadoCritico = false;
        try {
            String sql = " select r.feriadoCritico"
                    + " from Regime_HoraExtra r,userinfo u "
                    + " where u.userid = " + cod_funcionario + " and"
                    + " u.cod_regime = r.cod_regime";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                isFeriadoCritico = feriadoCritico;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return isFeriadoCritico;
    }

    public RegimeHoraExtra getRegimeHoraExtra(Integer cod_funcionario) {

        RegimeHoraExtra regimeHoraExtra = new RegimeHoraExtra();
        try {
            String sql = " select r.*"
                    + " from Regime_HoraExtra r,userinfo u "
                    + " where u.userid = " + cod_funcionario + " and"
                    + " u.cod_regime = r.cod_regime";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer cod_regime = rs.getInt("cod_regime");
                regimeHoraExtra.setCod(cod_regime);
                String nome = rs.getString("nome");
                regimeHoraExtra.setNome(nome);
                Integer modoTolerancia = rs.getInt("modoTolerancia");
                regimeHoraExtra.setModoTolerancia(modoTolerancia);
                Integer tolerancia = rs.getInt("tolerancia");
                regimeHoraExtra.setTolerancia(tolerancia);
                Integer limiteEntrada = rs.getInt("limiteEntrada");
                limiteEntrada = (limiteEntrada == null) ? 0 : limiteEntrada;
                regimeHoraExtra.setLimiteEntrada(limiteEntrada);
                Boolean feriadoCritico = rs.getBoolean("feriadoCritico");
                regimeHoraExtra.setFeriadoCritico(feriadoCritico);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return regimeHoraExtra;
    }

    public List<SelectItem> getRegimeSelectItem() {
        List<SelectItem> regimeList = new ArrayList<SelectItem>();
        regimeList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from regime_HoraExtra";
            ResultSet rs = con.executeQuery(query);

            while (rs.next()) {
                Integer cod_regime = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                regimeList.add(new SelectItem(cod_regime, nome));
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return regimeList;
    }

    public List<SelectItem> getCargoSelectItem() {
        List<SelectItem> cargoList = new ArrayList<SelectItem>();
        cargoList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from cargo";
            ResultSet rs = con.executeQuery(query);
            while (rs.next()) {
                Integer cargo = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                cargoList.add(new SelectItem(cargo, nome));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cargoList;
    }

    public HashMap<Integer, Integer> getcod_funcionarioRegime() {
        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        try {
            String query = "select userid,cod_regime from userinfo";
            ResultSet rs = con.executeQuery(query);
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cod_regime = rs.getInt("cod_regime");
                cod_funcionarioRegimeHashMap.put(userid, cod_regime);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioRegimeHashMap;
    }

    public HashMap<Integer, Integer> getcod_funcionarioCargo() {
        HashMap<Integer, Integer> cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        //System.out.println("pesquisando getcod_funcionarioCargo");
        try {
            String query = "select userid,CARGO from userinfo";
            con.prepareStatement(query);
            ResultSet rs = con.pstmt.executeQuery();
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cargo = rs.getInt("CARGO");
                cod_funcionarioCargoHashMap.put(userid, cargo);
                //System.out.println("funcCargo: " + userid + ", " + cargo);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioCargoHashMap;

    }

    public HashMap<Integer, Integer> getcod_funcionarioSubordinacaoDepartamento(Integer dept) {
        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> idToSuperHash = getHierarquiaDepartamentos();
        try {
            String query = "select userid,defaultdeptid,permissao from userinfo ";
            ResultSet rs = con.executeQuery(query);

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
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioRegimeHashMap;

    }

    public HashMap<Integer, Integer> getHierarquiaDepartamentos() {
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return idToSuperHash;
    }

    public List<Integer> consultaDiasPendentes(Integer userID, Date inicio, Date fim) {

        List<Integer> diasPendente = new ArrayList<Integer>();

        try {

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            String sql = " select data"
                    + " from SolicitacaoAbono "
                    + " where userid = " + userID + " and "
                    + " status = 'Pendente' and "
                    + " data between '" + inicioStr + "' and '" + fimStr + "'";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Date dataPendente = rs.getDate("data");
                GregorianCalendar dia = new GregorianCalendar();
                dia.setTimeInMillis(dataPendente.getTime());
                Integer diaEra = dia.get(Calendar.DAY_OF_YEAR) + dia.get(Calendar.YEAR) * 365;

                if (!diasPendente.contains(diaEra)) {
                    diasPendente.add(diaEra);
                }
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return diasPendente;
    }

    public RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo(Integer cod_funcionario) {
        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo = new RelatorioPortaria1510CabecalhoResumo();
        try {
            String sql = " select userid,name,ssn,badgenumber,mat_emcs,deptname,c.nome as cargo, r.nome as regime"
                    + " from userinfo u left join cargo c on u.cargo =  c.cod_cargo"
                    + " left join regime_horaextra r on u.cod_regime = r.cod_regime, departments d"
                    + " where u.userid = " + cod_funcionario + " and u.defaultdeptid = d.deptid ";

            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                String userid = rs.getString("userid");
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                String badgenumber = "";
                if (rs.getString("mat_emcs") == null || rs.getString("mat_emcs").equals("0")) {
                    badgenumber = rs.getString("badgenumber");
                } else {
                    badgenumber = rs.getString("mat_emcs");
                }
                String deptname = rs.getString("deptname");
                String cargo = rs.getString("cargo");
                if (cargo == null) {
                    cargo = "";
                }
                String regime = rs.getString("regime");
                relatorioPortaria1510CabecalhoResumo = new RelatorioPortaria1510CabecalhoResumo(userid, name, ssn, badgenumber, deptname, cargo, regime);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return relatorioPortaria1510CabecalhoResumo;
    }
    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public List<SelectItem> consultaDepartamentoHierarquico(Integer codigo_funcionario, Integer departamento, Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        Boolean isAdministradorVisivel = true;

        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";

            ResultSet rs = con.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(permissao, deptPermitidos, idToSuperHash);

            idToSuperHash = new HashMap<Integer, Integer>();

            //Selecionando o departamento do administrador
            sql = "select u.DEFAULTDEPTID,d.deptname from USERINFO u, DEPARTMENTS d"
                    + " where u.userid = " + codigo_funcionario + " and "
                    + " u.DEFAULTDEPTID = d.deptid ";

            rs = con.executeQuery(sql);

            Integer dept = -1;
            String nome_dept = "";

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
                nome_dept = rs.getString("DEPTNAME");
            }
            rs.close();

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            rs = con.executeQuery(sql);

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
                    isAdministradorVisivel = false;
                }
            }
            rs.close();

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

    public Boolean isAdministradorVisivel(Integer codigo_funcionario, Integer departamento, Integer permissao) {

        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();
        Boolean isAdministradorVisivel = true;

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";

            ResultSet rs = con.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(permissao, deptPermitidos, idToSuperHash);

            //Selecionando o departamento do administrador
            sql = "select DEFAULTDEPTID from USERINFO"
                    + " where userid = " + codigo_funcionario;

            rs = con.executeQuery(sql);

            Integer dept = -1;

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
            }
            rs.close();

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            rs = con.executeQuery(sql);

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
                    isAdministradorVisivel = false;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return isAdministradorVisivel;
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

    public List<SelectItem> consultaDepartamentoHierarquico(Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";

            ResultSet rs = con.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(permissao, deptPermitidos, idToSuperHash);

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer deptID = rs.getInt("DEPTID");
                String nome = rs.getString("DEPTNAME");
                Integer supdeptid = rs.getInt("supdeptid");
                if (deptIDList.contains(deptID)) {
                    deptOrdersList.add(new SelectItem(deptID, supdeptid.toString()));
                    idToSuperHash.put(deptID, supdeptid);
                    idList.add(deptID);
                    idToNomeHash.put(deptID, nome);
                }
            }
            rs.close();
            Integer raiz = Integer.parseInt(deptOrdersList.get(0).getValue().toString());
            List<Integer> deptSrtList = ordenarDepts(raiz, deptOrdersList);
            saida.add(new SelectItem("-1", "Selecione um departamento"));
            for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
                Integer id_dept = it.next();
                Integer espaces = getEspace(id_dept, idList, idToSuperHash);
                saida.add(new SelectItem(id_dept, espaces(espaces) + idToNomeHash.get(id_dept), "", false, false));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public boolean insertLog(String codFuncionario, String nomeFuncionario, String registroAlterado, String codRequerente, String nomeRequerente, String situacao, String liberacao) {
        boolean ok = true;
        try {
            String query = "insert into LogdeLiberacao(Usuario_ID, Usuario_Nome, Registro_Alterado, Requerente_ID, Requerente_Nome, Data_Alteracao, Situacao, Liberacao) values(?,?,?,?,?,?,?,?)";

            con.prepareStatement(query);
            con.pstmt.setString(1, codFuncionario);
            con.pstmt.setString(2, nomeFuncionario);
            con.pstmt.setString(3, registroAlterado);
            con.pstmt.setString(4, codRequerente);
            con.pstmt.setString(5, nomeRequerente);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

            Locale.setDefault(new Locale("pt", "BR")); //<-- Necessario?

            con.pstmt.setString(6, sdf.format(new java.util.Date()));
            con.pstmt.setString(7, situacao);
            con.pstmt.setString(8, liberacao);
            con.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

}
