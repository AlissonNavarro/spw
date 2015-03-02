package RelatorioMensal;

import Funcionario.Funcionario;
import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.model.SelectItem;

public class Banco implements Serializable {

    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public boolean insertRelatorio(List<Relatorio> relatorio) {
        boolean ok = true;
        String query = "insert into relatorio(data,definicao,Entrada1,Saida1,Entrada2,Saida2,horasTrabalhadas,saldo,observacao) values(?,?,?,?,?,?,?,?,?)";
        String queryDelete = "delete from relatorio";
        con.executeUpdate(queryDelete);
        try {
            con.prepareStatement(query);
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
            System.out.print(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean insertRelatorioLotacao(List<RelatorioLotacao> relatorioLotacao) {
        boolean ok = true;

        String query = "insert into relatorio_lotacao values(?,?,?,?)";
        String queryDelete = "delete from relatorio_lotacao";
        con.executeUpdate(queryDelete);
        try {
            con.prepareStatement(query);
            for (Iterator<RelatorioLotacao> it = relatorioLotacao.iterator(); it.hasNext();) {
                RelatorioLotacao relatorioLotacao_ = it.next();
                con.pstmt.setString(1, relatorioLotacao_.getCpf());
                con.pstmt.setString(2, relatorioLotacao_.getNome());
                con.pstmt.setString(3, relatorioLotacao_.getDepartamento());
                con.pstmt.setString(4, relatorioLotacao_.getEscala());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.print(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean insertRelatorioPortaria1510(List<HorarioContratual> horarioContratualList, List<RelatorioPortaria1510> relatorioPortaria1510) {
        boolean ok = true;

        String query1 = "insert into RelatorioHorarioContratual values(?,?,?,?,?)";
        String queryDelete1 = "truncate table RelatorioHorarioContratual";
        String query2 = "insert into RelatorioPortaria1510(dia,marcacoes,entrada1,entrada2,entrada3,saida1,saida2,saida3,ch) values(?,?,?,?,?,?,?,?,?)";
        String queryDelete2 = "truncate table RelatorioPortaria1510";
        con.executeUpdate(queryDelete1);
        con.executeUpdate(queryDelete2);

        try {
            con.prepareStatement(query1);
            for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
                HorarioContratual horarioContratual = it.next();
                con.pstmt.setString(1, horarioContratual.getCod_horario());
                con.pstmt.setString(2, horarioContratual.getEntrada1());
                con.pstmt.setString(3, horarioContratual.getSaida1());
                con.pstmt.setString(4, horarioContratual.getEntrada2());
                con.pstmt.setString(5, horarioContratual.getSaida2());
                con.executeUpdate();
            }

            con.prepareStatement(query2);
            for (Iterator<RelatorioPortaria1510> it = relatorioPortaria1510.iterator(); it.hasNext();) {
                RelatorioPortaria1510 relatorioPortaria1510_ = it.next();
                con.pstmt.setString(1, relatorioPortaria1510_.getDia());
                con.pstmt.setString(2, relatorioPortaria1510_.getMarcacoesRegistradas());
                con.pstmt.setString(3, relatorioPortaria1510_.getEntrada1());
                con.pstmt.setString(4, relatorioPortaria1510_.getEntrada2());
                con.pstmt.setString(5, relatorioPortaria1510_.getEntrada3());
                con.pstmt.setString(6, relatorioPortaria1510_.getSaida1());
                con.pstmt.setString(7, relatorioPortaria1510_.getSaida2());
                con.pstmt.setString(8, relatorioPortaria1510_.getSaida3());
                con.pstmt.setString(9, relatorioPortaria1510_.getCh());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.print(e);
            ok = false;
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
            try {
                con.c.rollback();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public List<Timestamp> consultaChechInOutTotal(Integer userid, Date inicio, Date fim) {
        List<Timestamp> getAllTimestamp = new ArrayList<Timestamp>();
        try {
            ResultSet rs;
            String sql;

            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            String inicioStr = sdfHora.format(inicio.getTime());
            String fimStr = sdfHora.format(fim.getTime() + 86399999);

            sql = "select checktime" + " from checkinout"
                    + " where userid = " + userid + " and"
                    + " checktime" + " between '" + inicioStr + "' and '" + fimStr + "'";
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("checktime");
                getAllTimestamp.add(timestamp);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return getAllTimestamp;
    }

    public boolean insertRelatorioFuncionarioSemEscala(List<Funcionario> funcionarioSemEscalaList) {
        boolean ok = true;

        String query = "insert into RelatorioFuncionarioSemEscala(ssn,nome,departamento) values(?,?,?)";
        String queryDelete = "delete from RelatorioFuncionarioSemEscala";
        con.executeUpdate(queryDelete);
        con.prepareStatement(query);

        try {
            for (Iterator<Funcionario> it = funcionarioSemEscalaList.iterator(); it.hasNext();) {
                Funcionario funcionario = it.next();
                con.pstmt.setString(1, funcionario.getCpf());
                con.pstmt.setString(2, funcionario.getNome());
                con.pstmt.setString(3, funcionario.getDept());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.print(e);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public HashMap<Integer, String> consultaSchClassID_LegendaMap() {
        String queryLegenda = "select schClassid,legend from schClass";
        ResultSet rs;
        HashMap<Integer, String> schClassIDLegendaMap = new HashMap<Integer, String>();
        try {
            rs = con.executeQuery(queryLegenda);
            while (rs.next()) {
                Integer schClassid = rs.getInt("schClassid");
                String legend = rs.getString("legend");
                schClassIDLegendaMap.put(schClassid, legend);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.print(ex);
        } finally {
            con.Desconectar();
        }
        return schClassIDLegendaMap;
    }

    public boolean insertRelatorioResumoEscala(List<RelatorioResumoEscala> relatorioResumoEscala) {

        HashMap<Integer, String> schClassID_LegendaMap = consultaSchClassID_LegendaMap();
        boolean ok = true;

        String query = "insert into relatorioResumoEscala"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String queryDelete = "delete from relatorioResumoEscala";
        con.executeUpdate(queryDelete);

        con.prepareStatement(query);

        try {
            for (Iterator<RelatorioResumoEscala> it = relatorioResumoEscala.iterator(); it.hasNext();) {
                RelatorioResumoEscala relatorioResumoEscala_ = it.next();

                con.pstmt.setString(1, relatorioResumoEscala_.getNome());
                List<String> schClassIDListStr = new ArrayList<String>();
                relatorioResumoEscala_.setSchClassID_LegendaList(schClassID_LegendaMap);
                if (relatorioResumoEscala_.getHasEscala()) {
                    schClassIDListStr = relatorioResumoEscala_.getEscalasStr2();
                } else {
                    schClassIDListStr = relatorioResumoEscala_.getSemEscalasStr();
                }
                con.pstmt.setString(2, schClassIDListStr.get(0));
                con.pstmt.setString(3, schClassIDListStr.get(1));
                con.pstmt.setString(4, schClassIDListStr.get(2));
                con.pstmt.setString(5, schClassIDListStr.get(3));
                con.pstmt.setString(6, schClassIDListStr.get(4));
                con.pstmt.setString(7, schClassIDListStr.get(5));
                con.pstmt.setString(8, schClassIDListStr.get(6));
                con.pstmt.setString(9, schClassIDListStr.get(7));
                con.pstmt.setString(10, schClassIDListStr.get(8));
                con.pstmt.setString(11, schClassIDListStr.get(9));
                con.pstmt.setString(12, schClassIDListStr.get(10));
                con.pstmt.setString(13, schClassIDListStr.get(11));
                con.pstmt.setString(14, schClassIDListStr.get(12));
                con.pstmt.setString(15, schClassIDListStr.get(13));
                con.pstmt.setString(16, schClassIDListStr.get(14));
                con.pstmt.setString(17, schClassIDListStr.get(15));
                con.pstmt.setString(18, schClassIDListStr.get(16));
                con.pstmt.setString(19, schClassIDListStr.get(17));
                con.pstmt.setString(20, schClassIDListStr.get(18));
                con.pstmt.setString(21, schClassIDListStr.get(19));
                con.pstmt.setString(22, schClassIDListStr.get(20));
                con.pstmt.setString(23, schClassIDListStr.get(21));
                con.pstmt.setString(24, schClassIDListStr.get(22));
                con.pstmt.setString(25, schClassIDListStr.get(23));
                con.pstmt.setString(26, schClassIDListStr.get(24));
                con.pstmt.setString(27, schClassIDListStr.get(25));
                con.pstmt.setString(28, schClassIDListStr.get(26));
                con.pstmt.setString(29, schClassIDListStr.get(27));
                con.pstmt.setString(30, schClassIDListStr.get(28));
                con.pstmt.setString(31, schClassIDListStr.get(29));
                con.pstmt.setString(32, schClassIDListStr.get(30));

                con.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.print(ex);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public List<String> consultaInfoUsuario(Integer cod) {
        List<String> infoList = new ArrayList<String>();

        try {
            ResultSet rs;
            String sql;

            sql = " select userid,name,ssn,badgenumber,deptname,c.nome as cargo"
                    + " from userinfo u left join cargo c on u.cargo =  c.cod_cargo, departments d"
                    + " where u.userid = " + cod + " and u.defaultdeptid = d.deptid ";
            rs = con.executeQuery(sql);
            
            String userid = "";
            String name = "";
            String ssn = "";
            String lotacao = "";
            String cargo = "";

            while (rs.next()) {
                userid = rs.getString("userid");
                name = rs.getString("name");
                ssn = rs.getString("ssn");
                lotacao = rs.getString("deptname");
                cargo = rs.getString("cargo");
            }
            infoList.add(userid);
            infoList.add(name);
            infoList.add(ssn);
            infoList.add(lotacao);
            infoList.add(cargo);
            rs.close();
            
        } catch (Exception e) {
        } finally {
            con.Desconectar();
        }
        return infoList;
    }

    public Integer consultaTipoRelatorio() {
        Integer tipoRelatorio = 0;

        try {
            ResultSet rs;
            String sql;
            sql = " select tipoRelatorio from config";
            rs = con.executeQuery(sql);

            while (rs.next()) {
                tipoRelatorio = rs.getInt("tipoRelatorio");
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return tipoRelatorio;
    }

    public void alterarTipoRelatorio(Integer tipoRelatorio) {
        String query = "update config set tipoRelatorio=?";
        try {
            con.prepareStatement(query);
            con.pstmt.setInt(1, tipoRelatorio);
            con.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        } finally {
            con.Desconectar();
        }
    }

    public byte[] getImageLogo() {
        ResultSet rs;
        String query = "select logo from config";
        byte[] fileBytes = null;
        try {
            query = "select logo from config";
            rs = con.executeQuery(query);
            if (rs.next()) {
                fileBytes = rs.getBytes(1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return fileBytes;
    }

    public void insertImage(byte[] image) {
        String query;
        try {
            query = ("update config set logo=? ");
            con.prepareStatement(query);

            // Method used to insert a stream of bytes
            con.pstmt.setBytes(1, image);
            con.executeUpdate();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
    }

    public void deleteImage() {
        String query;
        try {
            query = ("update config set logo=? ");
            con.prepareStatement(query);
            // Method used to insert a stream of bytes
            con.pstmt.setBytes(1, null);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
    }

    public Funcionario consultaFuncionario(Integer cod) {
        Funcionario funcionario = new Funcionario();
        try {
            ResultSet rs;
            String sql;

            sql = "select userid,name,HIREDDAY,ssn,badgenumber,deptname,c.nome as cargo,r.nome as regime, u.mat_emcs "
                    + "from (userinfo u left join cargo c on u.cargo =  c.cod_cargo) "
                    + "left join  regime_horaextra r on r.cod_regime = u.cod_regime,departments d "
                    + "where u.userid = " + cod + " and u.defaultdeptid = d.deptid";            
            rs = con.executeQuery(sql);

            String name = "";
            String ssn = "";
            String cargo = "";
            String dept = "";
            String badgenumber = "";
            String regime = "";
            Integer mat_emcs = 0;
            Date contratacaoDate = null;

            while (rs.next()) {
                name = rs.getString("name");
                badgenumber = rs.getString("badgenumber");
                ssn = rs.getString("ssn");
                dept = rs.getString("deptname");
                cargo = rs.getString("cargo");
                contratacaoDate = rs.getDate("HIREDDAY");
                regime = rs.getString("regime");
                mat_emcs = rs.getInt("mat_emcs");
            }

            funcionario.setNome(name);
            funcionario.setCargoStr(cargo);
            funcionario.setCpf(ssn);
            funcionario.setDept(dept);
            funcionario.setMatricula(badgenumber);
            funcionario.setDataContratação(contratacaoDate);
            regime = regime == null ? "" : regime;
            funcionario.setNome_regime(regime);
            funcionario.setMat_emcs(mat_emcs);

            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return funcionario;
    }

    public List<Integer> consultaFuncionarioDepartamento(Integer dept, Boolean incluirSubsetores) {

        List<Integer> matriculasList = new ArrayList<Integer>();
        List<Integer> deptIDList;
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        try {
            ResultSet rs;            
            String sql;

            if (incluirSubsetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dept);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dept, deptPermitidos, idToSuperHash);

                sql = " select distinct name, u.userid, defaultdeptid"
                        + " from userinfo u"
                        + " where ssn != '999.999.999-99' and"
                        + " u.ativo = 'true'"
                        + " order by name";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    Integer dep = rs.getInt("defaultdeptid");
                    if (deptIDList.contains(dep)) {
                        matriculasList.add(Integer.parseInt(userid));
                    }
                }
            } else {

                sql = " select  distinct name,u.userid"
                        + " from userinfo u"
                        + " where defaultdeptid = (" + dept + ")" + " and"
                        + " u.ativo = 'true' and "
                        + " ssn != '999.999.999-99'"
                        + " order by name";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    matriculasList.add(Integer.parseInt(userid));
                }
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return matriculasList;
    }

    public List<RelatorioResumo> consultaFuncionarioOrderByDeptUser() {

        List<RelatorioResumo> relatorioResumoList = new ArrayList<RelatorioResumo>();

        try {
            ResultSet rs;
            
            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "SELECT D.deptname, U.name, U.userid, U.ssn "
                    + " FROM USERINFO U "
                    + "	LEFT JOIN DEPARTMENTS D ON (U.defaultdeptid = D.deptid)"
                    + " ORDER BY D.deptname, U.name";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                String deptname = rs.getString("deptname");
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                RelatorioResumo r = new RelatorioResumo(userid, name, deptname, ssn);
                relatorioResumoList.add(r);
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return relatorioResumoList;
    }

    public List<Funcionario> consultaFuncionarioDepartamento() {

        List<Funcionario> funcionarioList = new ArrayList<Funcionario>();

        try {
            ResultSet rs;
            
            String sql;

            sql = " select userid,pis"
                    + " from userinfo u"
                    + " where u.ativo = 'true' "
                    + " order by name";

            
            rs = con.executeQuery(sql);

            while (rs.next()) {

                Funcionario funcionario = new Funcionario();
                String userid = rs.getString("userid");
                String pis = rs.getString("pis");
                funcionario.setMatricula(userid);
                funcionario.setPIS(pis);
                funcionarioList.add(funcionario);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return funcionarioList;
    }

    public List<Funcionario> consultaFuncionarioDepartamento(List<SelectItem> funcionarioList_) {

        List<Funcionario> funcionarioList = new ArrayList<Funcionario>();

        try {
            ResultSet rs;
            
            String sql;

            sql = " select userid,pis"
                    + " from userinfo u"
                    + " where u.ativo = 'true' "
                    + " order by name";

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String userid = rs.getString("userid");
                if (constainsFuncionario(userid, funcionarioList_)) {
                    Funcionario funcionario = new Funcionario();
                    String pis = rs.getString("pis");
                    funcionario.setMatricula(userid);
                    funcionario.setPIS(pis);
                    funcionarioList.add(funcionario);
                }
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return funcionarioList;
    }

    private Boolean constainsFuncionario(String userid, List<SelectItem> funcionarioList_) {

        for (Iterator<SelectItem> it = funcionarioList_.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            Integer cod_funcionario = (Integer) selectItem.getValue();
            if (cod_funcionario.toString().equals(userid)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<Integer, Integer> consultaNumRep() {

        HashMap<Integer, Integer> numREPMap = new HashMap<Integer, Integer>();

        try {
            ResultSet rs;
            
            String sql;

            sql = " select COD_EQUIPAMENTO,NUM_REP"
                    + " from numRep";

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer cod_equipamento = rs.getInt("COD_EQUIPAMENTO");
                Integer num_equip = rs.getInt("NUM_REP");

                numREPMap.put(cod_equipamento, num_equip);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return numREPMap;
    }

    public List<SelectItem> consultaFuncionarioTotal(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        List<Integer> deptIDList;

        try {
            ResultSet rs;
            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

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
                boolean flag = true;
                while (rs.next()) {

                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        if (flag) {
                            userList.add(new SelectItem(0, "Todos os funcionários"));
                            flag = false;
                        }
                        userList.add(new SelectItem(userid, name));
                    }
                }

            } else {
                sql = "select u.userid,u.name"
                        + " from  USERINFO u "
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY u.name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));
                boolean flag = true;
                while (rs.next()) {
                    if (flag) {
                        userList.add(new SelectItem(0, "Todos os funcionários"));
                        flag = false;
                    }
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    userList.add(new SelectItem(userid, name));
                }
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return userList;
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

    public String[] getNomeDepartamento_E_Escala(Integer cod_departamento, Integer cod_escala, Boolean incluirSubsetores) {

        String[] departamento_e_escala = new String[2];

        try {
            ResultSet rs;
            
            String sql;

            sql = " select deptName"
                    + " from departments"
                    + " where deptID = " + cod_departamento;

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String deptName = rs.getString("deptName");
                departamento_e_escala[0] = deptName;
                if (incluirSubsetores) {
                    departamento_e_escala[0] += " (e sub-departamentos)";
                }
            }

            rs.close();
            

            sql = " select name"
                    + " from num_run"
                    + " where num_runid = " + cod_escala;

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                departamento_e_escala[1] = name;
            }

            if (cod_escala == -1) {
                departamento_e_escala[1] = "Todas as escalas";
            }

            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return departamento_e_escala;
    }

    public List<Integer> consultaFuncionarioDepartamentoSemEscala(Integer cod_dept, Boolean incluirSubsetores) {

        List<Integer> matriculasList = new ArrayList<Integer>();
        List<Integer> deptIDList;
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        try {

            ResultSet rs;
            
            String sql;

            if (incluirSubsetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(cod_dept);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(cod_dept, deptPermitidos, idToSuperHash);

                sql = " select u.userid,u.defaultdeptid"
                        + " from userinfo u "
                        + " where ssn != '999.999.999-99'"
                        + " order by name";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    Integer dep = rs.getInt("defaultdeptid");
                    if (deptIDList.contains(dep)) {
                        matriculasList.add(Integer.parseInt(userid));
                    }
                }
            } else {

                sql = " select u.userid"
                        + " from userinfo u"
                        + " where defaultdeptid = (" + cod_dept + ") and"
                        + " ssn != '999.999.999-99'"
                        + " order by name";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    matriculasList.add(Integer.parseInt(userid));
                }
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return matriculasList;
    }

    public List<SelectItem> consultaEscalas() {
        List<SelectItem> escalaList = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            
            String sql;

            sql = "select num_runid,name from NUM_RUN ORDER BY name";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer num_runid = rs.getInt("num_runid");
                String name = rs.getString("name");
                SelectItem selectItem = new SelectItem(num_runid, name);
                escalaList.add(selectItem);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return escalaList;
    }

    public List<RelatorioLotacao> consultaFuncionarios(Integer dept, Integer cod_escala, Boolean incluirSubsetores, Date dataFim) {

        List<RelatorioLotacao> relatorioLotacaoList = new ArrayList<RelatorioLotacao>();
        List<Integer> deptIDList;
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");

        String fimStr = sdfHora.format(dataFim.getTime());

        try {
            ResultSet rs = null;
            
            String sql;

            if (incluirSubsetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dept);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dept, deptPermitidos, idToSuperHash);

                if (cod_escala == -1) {

                    sql = " select distinct u.ssn as cpf,u.name as nome, d.deptname as departamento,d.deptid as cod_departamento,nr.name as escala"
                            + " from userinfo u,user_of_run ur, departments d, num_run nr"
                            + " where ur.userid = u.userid and "
                            + "       u.defaultdeptid = d.deptid  and"
                            + "       ur.num_of_run_id = nr.num_runid and "
                            + "       '" + fimStr + "' between ur.startdate and ur.enddate and "
                            + "       u.ssn != '999.999.999-99' "
                            + "       order by u.name";

                    
                    rs = con.executeQuery(sql);

                    while (rs.next()) {
                        String cpf = rs.getString("cpf");
                        String nome = rs.getString("nome");
                        String departamento = rs.getString("departamento");
                        Integer cod_departamento = rs.getInt("cod_departamento");
                        String escala = rs.getString("escala");
                        if (deptIDList.contains(cod_departamento)) {
                            RelatorioLotacao relatorioLotacao = new RelatorioLotacao();
                            relatorioLotacao.setCpf(cpf);
                            relatorioLotacao.setNome(nome);
                            relatorioLotacao.setDepartamento(departamento);
                            relatorioLotacao.setEscala(escala);
                            relatorioLotacaoList.add(relatorioLotacao);
                        }
                    }
                } else {
                    sql = " select distinct u.ssn as cpf,u.name as nome, d.deptname as departamento,d.deptid as cod_departamento,nr.name as escala"
                            + " from userinfo u,user_of_run ur, departments d, num_run nr"
                            + " where ur.userid = u.userid and "
                            + "       u.defaultdeptid = d.deptid  and"
                            + "       ur.num_of_run_id = nr.num_runid and "
                            + "       ur.num_of_run_id = " + cod_escala + " and "
                            + "       '" + fimStr + "' between ur.startdate and ur.enddate and "
                            + "       u.ssn != '999.999.999-99' "
                            + "       order by u.name";

                    
                    rs = con.executeQuery(sql);

                    while (rs.next()) {
                        String cpf = rs.getString("cpf");
                        String nome = rs.getString("nome");
                        String departamento = rs.getString("departamento");
                        Integer cod_departamento = rs.getInt("cod_departamento");
                        String escala = rs.getString("escala");
                        if (deptIDList.contains(cod_departamento)) {
                            RelatorioLotacao relatorioLotacao = new RelatorioLotacao();
                            relatorioLotacao.setCpf(cpf);
                            relatorioLotacao.setNome(nome);
                            relatorioLotacao.setDepartamento(departamento);
                            relatorioLotacao.setEscala(escala);
                            relatorioLotacaoList.add(relatorioLotacao);
                        }
                    }
                }
            } else {
                if (cod_escala == -1) {

                    sql = " select distinct u.ssn as cpf,u.name as nome, d.deptname as departamento,d.deptid as cod_departamento,nr.name as escala"
                            + " from userinfo u,user_of_run ur, departments d, num_run nr"
                            + " where ur.userid = u.userid and "
                            + "       u.defaultdeptid = d.deptid  and"
                            + "       u.defaultdeptid = " + dept + "  and"
                            + "       ur.num_of_run_id = nr.num_runid and "
                            + "       '" + fimStr + "' between ur.startdate and ur.enddate and "
                            + "       u.ssn != '999.999.999-99' "
                            + "       order by u.name";

                    
                    rs = con.executeQuery(sql);

                    while (rs.next()) {
                        String cpf = rs.getString("cpf");
                        String nome = rs.getString("nome");
                        String departamento = rs.getString("departamento");
                        Integer cod_departamento = rs.getInt("cod_departamento");
                        String escala = rs.getString("escala");

                        RelatorioLotacao relatorioLotacao = new RelatorioLotacao();
                        relatorioLotacao.setCpf(cpf);
                        relatorioLotacao.setNome(nome);
                        relatorioLotacao.setDepartamento(departamento);
                        relatorioLotacao.setEscala(escala);
                        relatorioLotacaoList.add(relatorioLotacao);
                    }

                } else {
                    sql = " select distinct u.ssn as cpf,u.name as nome, d.deptname as departamento,d.deptid as cod_departamento,nr.name as escala"
                            + " from userinfo u,user_of_run ur, departments d, num_run nr"
                            + " where ur.userid = u.userid and "
                            + "       u.defaultdeptid = d.deptid  and"
                            + "       u.defaultdeptid = " + dept + "  and"
                            + "       ur.num_of_run_id = nr.num_runid and "
                            + "       ur.num_of_run_id = " + cod_escala + " and "
                            + "       '" + fimStr + "' between ur.startdate and ur.enddate and "
                            + "       u.ssn != '999.999.999-99' "
                            + "       order by u.name";

                    
                    rs = con.executeQuery(sql);

                    while (rs.next()) {
                        String cpf = rs.getString("cpf");
                        String nome = rs.getString("nome");
                        String departamento = rs.getString("departamento");
                        Integer cod_departamento = rs.getInt("cod_departamento");
                        String escala = rs.getString("escala");

                        RelatorioLotacao relatorioLotacao = new RelatorioLotacao();
                        relatorioLotacao.setCpf(cpf);
                        relatorioLotacao.setNome(nome);
                        relatorioLotacao.setDepartamento(departamento);
                        relatorioLotacao.setEscala(escala);
                        relatorioLotacaoList.add(relatorioLotacao);
                    }

                }
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return relatorioLotacaoList;
    }

    public AFDTCabecalho consultaAFDTCabecalho(Date dataInicial, Date dataFim) {

        AFDTCabecalho cabecalho = new AFDTCabecalho();

        try {
            ResultSet rs;
            
            String sql;

            sql = "select * from Empregador";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String cnpj = rs.getString("CNPJ");
                String cei = rs.getString("CEI");
                String razao_social = rs.getString("RAZAO_SOCIAL");
                String cnpj_ = somentoNumero(cnpj);
                cabecalho.setCnpj(cnpj_);

                if (cabecalho.getCnpj().length() == 14) {
                    cabecalho.setTipo_identificador(1);
                } else {
                    cabecalho.setTipo_identificador(2);
                }

                cabecalho.setCei(cei);
                cabecalho.setRazao_social(razao_social);

            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicialStr = somentoNumero(sdf.format(dataInicial.getTime()));
            String dataFinalStr = somentoNumero(sdf.format(dataFim.getTime()));
            String dataAtual = somentoNumero(sdf.format(new Date()));

            cabecalho.setDataInicial(dataInicialStr);
            cabecalho.setDataFinal(dataFinalStr);
            cabecalho.setDataGeracao(dataAtual);
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            String hora = sdfHora.format(new Date());
            cabecalho.setHoraGeracao(hora);

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cabecalho;
    }

    public ACJEFCabecalho consultaACJEFCabecalho(Date dataInicial, Date dataFim) {

        ACJEFCabecalho cabecalho = new ACJEFCabecalho();

        try {
            ResultSet rs;
            
            String sql;

            sql = "select * from Empregador";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String cnpj = rs.getString("CNPJ");
                String cei = rs.getString("CEI");
                String razao_social = rs.getString("RAZAO_SOCIAL");
                String cnpj_ = somentoNumero(cnpj);
                cabecalho.setCnpj(cnpj_);

                if (cabecalho.getCnpj().length() == 14) {
                    cabecalho.setTipo_identificador(1);
                } else {
                    cabecalho.setTipo_identificador(2);
                }

                cabecalho.setCei(cei);
                cabecalho.setRazao_social(razao_social);

            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataInicialStr = somentoNumero(sdf.format(dataInicial.getTime()));
            String dataFinalStr = somentoNumero(sdf.format(dataFim.getTime()));
            String dataAtual = somentoNumero(sdf.format(new Date()));

            cabecalho.setDataInicial(dataInicialStr);
            cabecalho.setDataFinal(dataFinalStr);
            cabecalho.setDataGeracao(dataAtual);
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            String hora = sdfHora.format(new Date());
            cabecalho.setHoraGeracao(hora);

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cabecalho;
    }

    public String consultaNomePercentagemHoraExtra(String cod_funcionario, String nome) {

        String valor = "00";
        try {
            ResultSet rs;
            
            String sql;

            sql = "select t.valor from userinfo u ,regime_horaextra r,tipo_horaextra t "
                    + "where u.userid = '" + cod_funcionario + "' and u.cod_regime = r.cod_regime and "
                    + "r.cod_regime = t.cod_regime and t.nome like '" + nome + "'";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                valor = rs.getString("valor");
            }

            if (valor == null) {
                valor = "00";
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return valor;
    }

    /**
     * public AFDTCabecalho consultaHorarioContratuais() {
     *
     * AFDTCabecalho cabecalho = new AFDTCabecalho();
     *
     * try { ResultSet rs;  String sql;
     *
     * sql = "select * from num_run_deil";  rs =
     * stmt.executeQuery(sql);
     *
     * while (rs.next()) { String entrada = rs.getString("STARTTIME"); String
     * saida = rs.getString("ENDTIME"); String sday = rs.getString("SDAYS");
     * String eday = rs.getString("SDAYS"); Integer schClassid =
     * rs.getInt("schclassid");
     *
     * HorarioContratual hc = new HorarioContratual(); hc.setEntrada1(entrada);
     * hc.setSaida1(saida);
     *
     *
     *
     * SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm"); String hora =
     * sdfHora.format(new Date()); cabecalho.setHoraGeracao(hora); }
     *
     * rs.close();  } catch (Exception e) {
     *
     * } finally { try { if (c != null) { c.close(); }
     *
     * } catch (Exception e) { } } return cabecalho; }
     */
    public List<SelectItem> consultaDepartamentoOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            

            String sql;

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            
            rs = con.executeQuery(sql);
            saida.add(new SelectItem(0, "Selecione o departamento"));

            while (rs.next()) {
                Integer deptID = rs.getInt("DEPTID");
                String nome = rs.getString("DEPTNAME");
                Integer supdeptid = rs.getInt("supdeptid");

                deptOrdersList.add(new SelectItem(deptID, supdeptid.toString()));
                idToSuperHash.put(deptID, supdeptid);
                idList.add(deptID);
                idToNomeHash.put(deptID, nome);
            }
            rs.close();
            

            Integer raiz = Integer.parseInt(deptOrdersList.get(0).getValue().toString());
            List<Integer> deptSrtList = ordenarDepts(raiz, deptOrdersList);
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

    public List<SelectItem> consultaFuncionario(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;
        List<String> cpfDuplicados = getCPFsDuplicados();

        try {
            ResultSet rs = null;
            

            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

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
                        + " where u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));
                boolean flag = true;

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        if (flag) {
                            userList.add(new SelectItem(0, "TODOS OS FUNCIONÁRIOS"));
                            flag = false;
                        }
                        if (cpfDuplicados.contains(ssn)) {
                            name += " - " + badgenumber;
                        }
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep + " and "
                        + " u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));
                userList.add(new SelectItem(0, "TODOS OS FUNCIONÁRIOS"));

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String name = rs.getString("name");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    if (cpfDuplicados.contains(ssn)) {
                        name += " - " + badgenumber;
                    }
                    userList.add(new SelectItem(userid, name));
                }
            }

            rs.close();
            

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
            ResultSet rs = null;
            
            String sql = "SELECT distinct ssn FROM userinfo u1"
                    + "	WHERE (SELECT count(*) FROM userinfo u2	WHERE u2.ssn = u1.ssn) > 1 order by ssn";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String cpf = rs.getString("ssn");
                cpfList.add(cpf);
            }
            rs.close();
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cpfList;
    }

    public List<SelectItem> consultaFuncionario2(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;
        List<String> cpfDuplicados = getCPFsDuplicados();

        try {
            ResultSet rs = null;
            

            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

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
                        + " where ssn != '999.999.999-99' and "
                        + " u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
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
                        + " where u.defaultdeptid = " + dep + " and "
                        + " u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
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
            

        } catch (Exception e) {
            System.out.println("Erro consulta funcionário" + e);

        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public Map<Integer, Funcionario> consultaDadosFuncionario(Integer dep, Boolean incluirSubSetores) {

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        Map<Integer, Funcionario> matricFuncionarioHashMap = new HashMap<Integer, Funcionario>();
        List<Integer> deptIDList;

        try {
            ResultSet rs = null;
            

            String sql;

            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                
                rs = con.executeQuery(sql);

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

                sql = "select distinct u.userid,u.name,u.defaultdeptid,d.deptname,u.badgenumber,u.ssn,u.hiredday"
                        + " from USERINFO u, DEPARTMENTS d"
                        + " where u.ativo = 'true'"
                        + " and u.defaultdeptid = d.deptid"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    String name = rs.getString("name");
                    String deptname = rs.getString("deptname");
                    Date hiredday = rs.getDate("hiredday");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        Funcionario f = new Funcionario();
                        f.setFuncionarioId(userid);
                        f.setMatricula(badgenumber);
                        f.setCpf(ssn);
                        f.setNome(name);
                        f.setDept(deptname);
                        f.setDataContratação(hiredday);
                        matricFuncionarioHashMap.put(userid, f);
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name,u.badgenumber,d.deptname,u.ssn,u.hiredday from"
                        + " USERINFO u, DEPARTMENTS d"
                        + " where u.defaultdeptid = " + dep + " and "
                        + " u.ativo = 'true'"
                        + " and u.defaultdeptid = d.deptid"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String badgenumber = rs.getString("badgenumber");
                    String ssn = rs.getString("ssn");
                    String name = rs.getString("name");
                    String deptname = rs.getString("deptname");
                    Date hiredday = rs.getDate("hiredday");

                    Funcionario f = new Funcionario();
                    f.setFuncionarioId(userid);
                    f.setMatricula(badgenumber);
                    f.setCpf(ssn);
                    f.setNome(name);
                    f.setDept(deptname);
                    f.setDataContratação(hiredday);
                    matricFuncionarioHashMap.put(userid, f);
                }
            }

            rs.close();
            

        } catch (Exception e) {
            System.out.println("Erro consulta funcionário" + e);

        } finally {
            con.Desconectar();
        }
        return matricFuncionarioHashMap;
    }

    private String somentoNumero(String string) {
        String saida = string.replace(".", "").replace("/", "").replace("-", "");
        return saida;
    }

    public RelatorioPortaria1510Cabecalho consultaRelatorioPortaria1510Cabecalho(Integer cod_funcionario, Integer razaoSocial) {
        RelatorioPortaria1510Cabecalho relatorioPortaria1510Cabecalho = new RelatorioPortaria1510Cabecalho();

        try {
            ResultSet rs = null;
            

            String sql;

            if (razaoSocial == 0) {
                sql = "select id, RAZAO_SOCIAL,LOCAL from Empregador";
            } else {
                sql = "select id, RAZAO_SOCIAL,LOCAL from Empregador where id = " + razaoSocial;
            }

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String razao_social = rs.getString("RAZAO_SOCIAL");
                String local = rs.getString("LOCAL");
                relatorioPortaria1510Cabecalho.setEmpregador(razao_social);
                relatorioPortaria1510Cabecalho.setEndereco(local);
            }
            rs.close();
            

            sql = "select NAME,HIREDDAY from userinfo"
                    + " where userid = " + cod_funcionario;

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("NAME");
                String hiredday = rs.getString("HIREDDAY");
                String contratacao = convertDate(hiredday);
                relatorioPortaria1510Cabecalho.setEmpregado(name);
                relatorioPortaria1510Cabecalho.setAdmissao(contratacao);
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println("Erro consulta empregador" + e);
        } finally {
            con.Desconectar();
        }
        return relatorioPortaria1510Cabecalho;
    }

    public RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo(Integer cod_funcionario) {

        RelatorioPortaria1510CabecalhoResumo relatorioPortaria1510CabecalhoResumo = new RelatorioPortaria1510CabecalhoResumo();

        try {
            ResultSet rs = null;
            

            String sql;

            sql = "select name,ssn,badgenumber,deptname from userinfo u,departments d"
                    + " where u.userid = " + cod_funcionario
                    + " and u.defaultdeptid = d.deptid";

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                String badgenumber = rs.getString("badgenumber");
                String deptname = rs.getString("deptname");
                relatorioPortaria1510CabecalhoResumo = new RelatorioPortaria1510CabecalhoResumo(name, ssn, badgenumber, deptname);
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return relatorioPortaria1510CabecalhoResumo;
    }

    public List<SelectItem> getRegimeSelectItem() {

        List<SelectItem> regimeList = new ArrayList<SelectItem>();

        ResultSet rs;
        regimeList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from regime_HoraExtra";
            rs = con.executeQuery(query);

            while (rs.next()) {
                Integer cod_regime = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                regimeList.add(new SelectItem(cod_regime, nome));
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return regimeList;
    }

    public HashMap<Integer, Integer> getcod_funcionarioRegime() {

        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();

        ResultSet rs;

        try {
            String query = "select userid,cod_regime from userinfo";
            rs = con.executeQuery(query);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cod_regime = rs.getInt("cod_regime");
                cod_funcionarioRegimeHashMap.put(userid, cod_regime);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioRegimeHashMap;

    }

    public List<SelectItem> getCargoSelectItem() {
        List<SelectItem> cargoList = new ArrayList<SelectItem>();

        ResultSet rs;
        cargoList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from cargo";
            rs = con.executeQuery(query);
            while (rs.next()) {
                Integer cargo = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                cargoList.add(new SelectItem(cargo, nome));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cargoList;
    }

    public HashMap<Integer, Integer> getcod_funcionarioCargo() {
        HashMap<Integer, Integer> cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();

        ResultSet rs;
        try {
            String query = "select userid,CARGO from userinfo";
            rs = con.executeQuery(query);
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cargo = rs.getInt("CARGO");
                cod_funcionarioCargoHashMap.put(userid, cargo);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioCargoHashMap;

    }

    private String convertDate(String data) {
        if (data != null) {
            String diaDeContratacaooArray[] = data.split(" ");
            String diaDeContratacao = diaDeContratacaooArray[0] == null ? "" : diaDeContratacaooArray[0];
            String[] dataArray = diaDeContratacao.split("-");
            String ano = dataArray[0];
            String mes = dataArray[1];
            String dia = dataArray[2];
            return dia + "/" + mes + "/" + ano;
        } else {
            return "";
        }
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
            ResultSet rs;
            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            
            rs = con.executeQuery(sql);

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
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public List<SelectItem> consultaDepartamentoHierarquico(Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        List<Integer> deptIDList;

        try {
            ResultSet rs;
            

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            
            rs = con.executeQuery(sql);

            deptIDList = new ArrayList<Integer>();
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
            

            List<Integer> deptSrtList = ordenarDepts(permissao, deptOrdersList);
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

    public Boolean isAdministradorVisivel(Integer codigo_funcionario, Integer departamento, Integer permissao) {

        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();
        Boolean isAdministradorVisivel = true;

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            
            rs = con.executeQuery(sql);

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
            System.out.println(e);
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

        for (int i = 1; i <= qnt; i++) {
            nbsp += nbsp_;
        }

        return nbsp;
    }

    public List<SelectItem> consultaFuncionarioProprioAdministrador(Integer codigo_usuario) {

        List<SelectItem> userList = new ArrayList<SelectItem>();

        try {
            ResultSet rs = null;
            String sql;

            sql = "select name from USERINFO "
                    + " where userid = " + codigo_usuario;

            
            rs = con.executeQuery(sql);

            userList.add(new SelectItem(-1, "Selecione um funcionário"));

            while (rs.next()) {
                String name = rs.getString("name");
                userList.add(new SelectItem(codigo_usuario, name));
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public HashMap<Integer, Integer> getcod_funcionarioSubordinacaoDepartamento(Integer dept) {

        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();

        ResultSet rs;

        HashMap<Integer, Integer> idToSuperHash = getHierarquiaDepartamentos();

        try {
            String query = "select userid,defaultdeptid,permissao from userinfo ";
            rs = con.executeQuery(query);

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
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioRegimeHashMap;

    }

    public HashMap<Integer, Integer> getHierarquiaDepartamentos() {

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        try {
            ResultSet rs;
            

            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return idToSuperHash;
    }

    public List<Integer> getVerbas() {

        List<Integer> verbasList = new ArrayList<Integer>();

        try {

            String query = "Select * from verba";

            con.prepareStatement(query);

            ResultSet rs = con.executeQuery();

            while (rs.next()) {

                Integer empresa = rs.getInt("empresa");
                Integer adicionalNoturno = rs.getInt("adicionalNoturno");
                Integer atrasos = rs.getInt("atrasos");
                Integer atrasosMenorHora = rs.getInt("atrasosMenorHora");
                Integer atrasosMaiorHora = rs.getInt("atrasosMaiorHora");
                Integer FeriadoCritico = rs.getInt("FeriadoCritico");
                Integer DSR = rs.getInt("DSR");
                Integer Faltas = rs.getInt("Faltas");

                verbasList.add(empresa);
                verbasList.add(adicionalNoturno);
                verbasList.add(atrasos);
                verbasList.add(atrasosMenorHora);
                verbasList.add(atrasosMaiorHora);
                verbasList.add(FeriadoCritico);
                verbasList.add(DSR);
                verbasList.add(Faltas);

            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return verbasList;
    }
}
