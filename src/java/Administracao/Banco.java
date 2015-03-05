package Administracao;

import Abono.AbonoEmMassaADD;
import Abono.AbonoEmMassa;
import comunicacao.AcessoBD;
import java.sql.Timestamp;
import java.sql.ResultSet;
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
import java.util.Set;
import javax.faces.model.SelectItem;

public class Banco {

    AcessoBD con;
    Boolean hasConnection = false;

    public Banco() {
        con = new AcessoBD();
    }

    public void SaveConfig(boolean saveWithPIN) {
        try {
            String sql = "UPDATE config SET checkWithPIN = ?";
            if (con.prepareStatement(sql)) {
                con.pstmt.setBoolean(1, saveWithPIN);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public void CorrigePIN(boolean checkWithPIN) {
        try {
            String query = "select userid,badgenumber from userinfo order by userid";
            ResultSet rs = con.executeQuery(query);

            int userid;
            String badgenumber;
            while (rs.next()) {
                badgenumber = rs.getString("badgenumber");
                userid = rs.getInt("userid");
                if (checkWithPIN) {
                    badgenumber = badgenumber.replaceAll("\\D", "");
                } else {
                    badgenumber = badgenumber.replaceAll("\\D", "");
                    badgenumber = badgenumber + "A";
                }
                try {
                    query = "update userinfo set badgenumber='" + badgenumber + "' where userid=" + userid;
                    if (con.prepareStatement(query)) {
                        con.executeUpdate();
                    }
                } catch (Exception ex) {
                    System.out.println("Administracao.Banco.CorrigePIN() " + ex.getMessage());
                    query = "delete from USERINFO where USERID=" + userid;
                    if (con.prepareStatement(query)) {
                        con.executeUpdate();
                    }
                    System.out.println("Registro duplicado, deletado userid=" + userid);
                }
                query = "";
                userid = -1;
                badgenumber = "";
            }
        } catch (Exception ex) {
            System.out.println("Administracao.Banco.CorrigePIN() " + ex.getMessage());
        } finally {
            con.Desconectar();
        }
    }

    public boolean CheckWithPIN() {
        boolean result = false;
        try {
            String sql = "select checkWithPIN from config";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                result = rs.getBoolean("checkWithPIN");
            }
        } catch (Exception ex) {
            System.out.println("Administracao.Banco.CheckWithPIN() " + ex.getMessage());
        }
        return result;
    }

    public List<Permissao> usuarioPermissao(String filtro, List<Integer> departamentolistDosFuncionarios) {
        List<Permissao> permissaoList = new ArrayList<Permissao>();
        try {
            String sql = "select u.userid,u.badgenumber,u.ssn,u.name,u.cod_regime,u.permissao,u.perfil,u.DEFAULTDEPTID, d.deptname,  r.nome as nome_regime "
                    + "             from userinfo as u inner join departments as d on (u.defaultdeptid = d.deptid) "
                    + "             left join Regime_HoraExtra as r on (u.cod_regime = r.cod_regime)"
                    + "             where  u.name like  '" + filtro + "%'"
                    + "		    order by name";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer codigo = rs.getInt("userid");
                String badgenumber = rs.getString("badgenumber");
                String ssn = rs.getString("ssn");
                String name = rs.getString("name");
                String nomeRegime = rs.getString("nome_regime");
                String deptname = rs.getString("deptname");
                Integer permissao = rs.getInt("permissao");
                Integer cod_regime = rs.getInt("cod_regime");
                Integer perfil = rs.getInt("perfil");
                Integer deptid = rs.getInt("DEFAULTDEPTID");
                if (departamentolistDosFuncionarios.contains(deptid)) {
                    permissaoList.add(new Permissao(codigo, badgenumber, ssn, name, deptname, permissao, cod_regime, perfil, nomeRegime));
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return permissaoList;
    }

    public List<Permissao> usuarioPermissaoSuperAdmin(String filtro) {
        List<Permissao> permissaoList = new ArrayList<Permissao>();
        try {
            String query = "select u.userid,u.badgenumber,u.ssn,u.name,u.cod_regime,u.permissao,u.perfil,u.DEFAULTDEPTID, d.deptname,  r.nome as nome_regime "
                    + "             from userinfo as u inner join departments as d on (u.defaultdeptid = d.deptid) "
                    + "             left join Regime_HoraExtra as r on (u.cod_regime = r.cod_regime)"
                    + "             where  u.name like  '" + filtro + "%'"
                    + "		    order by name";
            ResultSet rs = con.executeQuery(query);
            while (rs.next()) {
                Integer codigo = rs.getInt("userid");
                String badgenumber = rs.getString("badgenumber");
                String ssn = rs.getString("ssn");
                String name = rs.getString("name");
                String nomeRegime = rs.getString("nome_regime");
                String deptname = rs.getString("deptname");
                Integer permissao = rs.getInt("permissao");
                Integer cod_regime = rs.getInt("cod_regime");
                Integer perfil = rs.getInt("perfil");
                permissaoList.add(new Permissao(codigo, badgenumber, ssn, name, deptname, permissao, cod_regime, perfil, nomeRegime));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return permissaoList;
    }

    public List<SelectItem> consultaDepartamento() {

        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        try {
            String sql = "select DEPTID,DEPTNAME from DEPARTMENTS ORDER BY DEPTNAME asc";
            ResultSet rs = con.executeQuery(sql);
            departamentoList.add(new SelectItem("0", "Usuario Comum"));
            while (rs.next()) {
                Integer deptid = rs.getInt("DEPTID");
                String nome = rs.getString("DEPTNAME");
                SelectItem selectItem = new SelectItem(deptid, nome);
                if (nome != null) {
                    departamentoList.add(selectItem);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return departamentoList;
    }

    public void updatePermissao(int userid, String valor) {
        try {
            String query = "UPDATE USERINFO SET Permissao = ? WHERE userid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, valor);
                con.pstmt.setInt(2, userid);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public boolean updateRegime(int userid, int cod_regime) {
        boolean flag = false;
        try {
            String query = "UPDATE USERINFO SET cod_regime = ? WHERE userid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, cod_regime);
                con.pstmt.setInt(2, userid);
                con.executeUpdate();
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public void zerarSenhaUsuario(int idUsuario) {
        try {
            String query = "UPDATE USERINFO SET senha = NULL WHERE userid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, idUsuario);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public boolean isDescricaoObrigatoria(int codJustificativa) {
        boolean retorno = false;
        try {
            String query = "select descricaoObrigatoria from leaveClass where leaveid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, codJustificativa);
                ResultSet rs = con.executeQuery();
                if (rs.next()) {
                    retorno = rs.getBoolean("descricaoObrigatoria");
                }
                rs.close();
            }
        } catch (Exception e) {
            retorno = false;
            System.out.println(e);
        }
        return retorno;
    }

    public List<SelectItem> consultaRegime() {
        List<SelectItem> regimeSelectItemList = new ArrayList<SelectItem>();
        try {
            String sql = "select * from Regime_HoraExtra ORDER BY nome";
            ResultSet rs = con.executeQuery(sql);
            regimeSelectItemList.add(new SelectItem(0, "Escolha o regime"));
            while (rs.next()) {
                Integer cod = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                SelectItem regimeSelectItem = new SelectItem(cod, nome);
                regimeSelectItemList.add(regimeSelectItem);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return regimeSelectItemList;
    }

    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

    public List<SelectItem> consultaDepartamentoHierarquico() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            String sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS ORDER BY SUPDEPTID asc,DEPTNAME";
            ResultSet rs = con.executeQuery(sql);
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
            saida.add(new SelectItem("0", "Usuário Comum"));
            for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
                Integer id_dept = it.next();
                Integer espaces = getEspace(id_dept, idList, idToSuperHash);
                saida.add(new SelectItem(id_dept, espaces(espaces) + idToNomeHash.get(id_dept), "", false, false));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public List<SelectItem> consultaDepartamentoOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            String sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
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
            System.out.println(e.getMessage());
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
            String sql = "";
            ResultSet rs;
            if (incluirSubSetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS ORDER BY SUPDEPTID asc";
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
                        + " where ativo = 'true'"
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
                        if (userList.size() == 1) {
                            userList.add(new SelectItem(0, "TODOS OS FUNCIONÁRIOS"));
                        }
                        if (cpfDuplicados.contains(ssn)) {
                            name += " - " + badgenumber;
                        }
                        userList.add(new SelectItem(userid, name));
                    }
                }
                rs.close();
            } else {
                sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep + " and "
                        + " ativo = 'true'"
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    if (userList.size() == 1) {
                        userList.add(new SelectItem(0, "TODOS OS FUNCIONÁRIOS"));
                    }
                    Integer userid = rs.getInt("userid");
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
            System.out.println(e.getMessage());
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cpfList;
    }

    public List<SelectItem> consultaJustificativa() {
        List<SelectItem> justificativaList = new ArrayList<SelectItem>();
        try {
            String query = "select l.leaveid,l.leavename from LEAVECLASS l";
            ResultSet rs = con.executeQuery(query);
            justificativaList.add(new SelectItem(-1, "Selecione a Justificativa"));

            while (rs.next()) {
                Integer cod = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                justificativaList.add(new SelectItem(cod, nome));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return justificativaList;
    }

    public boolean insertAbonoEmMassa(Integer cod_funcionario, AbonoEmMassaADD novoAbonoEmMassa, Integer cod_responsavel) {
        boolean ok = true;
        try {
            SimpleDateFormat sdfDiaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");

            String fimDia = somarStrDias(sdfDia.format(novoAbonoEmMassa.getDataFim()));

            String correnteDia = sdfDia.format(novoAbonoEmMassa.getDataInicio());
            do {
                String dataAtual = sdfDiaHora.format(new java.util.Date());

                String query = "INSERT INTO USER_SPEDAY(userid,startspecday,endspecday,dateid,yuanying,date,RESPONSAVEL) values(?,?,?,?,?,?,?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setInt(1, cod_funcionario);
                    con.pstmt.setString(2, correnteDia + " " + novoAbonoEmMassa.getHoraInicio() + ":00");
                    con.pstmt.setString(3, correnteDia + " " + novoAbonoEmMassa.getHoraFim() + ":00");
                    con.pstmt.setInt(4, novoAbonoEmMassa.getCod_justificativa());
                    con.pstmt.setString(5, novoAbonoEmMassa.getDescricaoJustificativa());
                    con.pstmt.setString(6, dataAtual);
                    con.pstmt.setInt(7, cod_responsavel);
                    correnteDia = somarStrDias(correnteDia);
                    try {
                        con.executeUpdate();
                    } catch (Exception ex) {

                    }
                }
            } while (!correnteDia.equals(fimDia));
        } catch (Exception e) {
            ok = false;
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean insertAbonoEmMassaEmMassa(List<SelectItem> funcionarioList, AbonoEmMassaADD novoAbonoEmMassa, Integer cod_responsavel) {
        boolean ok = true;
        try {

            SimpleDateFormat sdfDiaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");

            for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {

                String fimDia = somarStrDias(sdfDia.format(novoAbonoEmMassa.getDataFim()));
                String correnteDia = sdfDia.format(novoAbonoEmMassa.getDataInicio());

                SelectItem selectItem = it.next();
                Integer cod_funcionario = (Integer) selectItem.getValue();
                if (!(cod_funcionario.equals(0) || cod_funcionario.equals(-1))) {
                    do {
                        String dataAtual = sdfDiaHora.format(new java.util.Date());

                        String query = "INSERT INTO USER_SPEDAY(userid,startspecday,endspecday,dateid,yuanying,date,RESPONSAVEL) values(?,?,?,?,?,?,?)";

                        if (con.prepareStatement(query)) {
                            con.pstmt.setInt(1, cod_funcionario);
                            con.pstmt.setString(2, correnteDia + " " + novoAbonoEmMassa.getHoraInicio() + ":00");
                            con.pstmt.setString(3, correnteDia + " " + novoAbonoEmMassa.getHoraFim() + ":59");
                            con.pstmt.setInt(4, novoAbonoEmMassa.getCod_justificativa());
                            con.pstmt.setString(5, novoAbonoEmMassa.getDescricaoJustificativa());
                            con.pstmt.setString(6, dataAtual);
                            con.pstmt.setInt(7, cod_responsavel);
                            correnteDia = somarStrDias(correnteDia);
                            try {
                                con.executeUpdate();
                            } catch (Exception e) {
                                System.out.println("Erro2:" + e);
                            }
                        }
                    } while (!correnteDia.equals(fimDia));
                }
            }

        } catch (Exception e) {
            ok = false;
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    private String somarStrDias(String data) {
        java.util.Date dt = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dt = df.parse(data);
        } catch (ParseException ex) {
        }
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(dt);
        g.add(Calendar.DAY_OF_MONTH, 1);
        Date dateAux = (Date) g.getTime();
        String dataSaida = sdf.format(dateAux);
        return dataSaida;
    }

    public List<AbonoEmMassa> consultaAbonoEmMassaByDepartameto(int deptID, Date inicio, Date fim) {

        List<AbonoEmMassa> abonoEmMassaList = new ArrayList<AbonoEmMassa>();
        List<Integer> deptIDList;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdf.format(inicio);
            String fimStr = sdf.format(fim);

            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS ORDER BY SUPDEPTID asc";
            ResultSet rs = con.executeQuery(sql);

            deptIDList = new ArrayList<Integer>();
            deptIDList.add(deptID);

            HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(deptID, deptPermitidos, idToSuperHash);

            sql = "SELECT u.name,u.userid,u.defaultdeptid,l.leavename, us.*"
                    + " FROM USER_SPEDAY us,USERINFO u,leaveClass l"
                    + " where u.userid = us.userid and "
                    + " l.leaveid = us.dateid and "
                    + " us.STARTSPECDAY" + " >= '" + inicioStr + "' and " + " us.STARTSPECDAY < '" + fimStr + "'"
                    + " order by name";

            rs = con.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                String nome = rs.getString("name");
                Integer userid = rs.getInt("userid");
                Integer dept = rs.getInt("defaultdeptid");
                Timestamp startspecday = rs.getTimestamp("startspecday");
                Timestamp endspecday = rs.getTimestamp("endspecday");
                Integer cod_categoria = rs.getInt("dateid");
                String categoria = rs.getString("leavename");
                String descricaoCategoria = rs.getString("YUANYING");
                Timestamp dataAbonoEmMassa = rs.getTimestamp("date");

                if (deptIDList.contains(dept)) {
                    AbonoEmMassa abonoEmMassa = new AbonoEmMassa(nome, startspecday, endspecday, cod_categoria, categoria, descricaoCategoria, dataAbonoEmMassa);
                    abonoEmMassa.setPosicao(i);
                    abonoEmMassa.setCod_funcionario(userid);
                    i++;
                    abonoEmMassaList.add(abonoEmMassa);
                }
            }

            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return abonoEmMassaList;
    }

    public List<AbonoEmMassa> consultaAbonoEmMassaByFuncionario(int cod_funcionario, Date inicio, Date fim) {

        List<AbonoEmMassa> abonoEmMassaList = new ArrayList<AbonoEmMassa>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdf.format(inicio);
            String fimStr = sdf.format(fim);
            String sql = "SELECT u.name,u.userid,u.defaultdeptid,l.leavename, us.*"
                    + " FROM USER_SPEDAY us,USERINFO u,leaveClass l"
                    + " where u.userid = us.userid and"
                    + " us.userid = " + cod_funcionario + " and "
                    + " l.leaveid = us.dateid and "
                    + " us.STARTSPECDAY" + " between '" + inicioStr + "' and '" + fimStr + "'"
                    + " order by name";

            ResultSet rs = con.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                String nome = rs.getString("name");
                Integer userid = rs.getInt("userid");
                Timestamp startspecday = rs.getTimestamp("startspecday");
                Timestamp endspecday = rs.getTimestamp("endspecday");
                Integer cod_categoria = rs.getInt("dateid");
                String categoria = rs.getString("leavename");
                String descricaoCategoria = rs.getString("YUANYING");
                Timestamp dataAbonoEmMassa = rs.getTimestamp("date");

                AbonoEmMassa abonoEmMassa = new AbonoEmMassa(nome, startspecday, endspecday, cod_categoria, categoria, descricaoCategoria, dataAbonoEmMassa);
                abonoEmMassa.setPosicao(i);
                abonoEmMassa.setCod_funcionario(userid);
                i++;
                abonoEmMassaList.add(abonoEmMassa);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return abonoEmMassaList;
    }

    public boolean deleteAbonoEmMassa(AbonoEmMassa abonoEmMassa) {
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdf.format(abonoEmMassa.getInicio());

        String queryDelete = "delete from USER_SPEDAY "
                + " where userid = " + abonoEmMassa.getCod_funcionario()
                + " and startspecday = '" + inicioStr
                + "' and dateid = " + abonoEmMassa.getCod_categoria();
        con.executeUpdate(queryDelete);
        return flag;
    }

    public void deleteTodosAbonoEmMassa(List<AbonoEmMassa> abonoEmMassaList) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            con.c.setAutoCommit(false);
        } catch (Exception ex) {
            System.out.println("Administracao: deleteTodosAbonoEmMassa 1: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (Iterator<AbonoEmMassa> it = abonoEmMassaList.iterator(); it.hasNext();) {
                AbonoEmMassa abonoEmMassa = it.next();

                String inicioStr = sdf.format(abonoEmMassa.getInicio());

                String queryDelete = "delete from USER_SPEDAY "
                        + " where userid = " + abonoEmMassa.getCod_funcionario()
                        + " and startspecday = '" + inicioStr
                        + "' and dateid = " + abonoEmMassa.getCod_categoria();

                con.executeUpdate(queryDelete);
            }
            con.c.commit();
        } catch (Exception ex) {
            System.out.println("Administracao: deleteTodosAbonoEmMassa 2: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.c.setAutoCommit(true);
            } catch (Exception ex) {
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
            con.Desconectar();
        }
    }

    public boolean alterarAbonoEmMassa(int cod_funcionario, int cod_justificativa_selecionada,
            AbonoEmMassaADD abonoEmMassa, int cod_responsavel) {
        boolean ok = true;
        String query = "update USER_SPEDAY set dateid=?, yuanying=? "
                + " where userid=? and startspecday=? and dateid=? and RESPONSAVEL=?";
        try {
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, abonoEmMassa.getCod_justificativa());
                con.pstmt.setString(2, abonoEmMassa.getDescricaoJustificativa());
                con.pstmt.setInt(3, cod_funcionario);
                SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
                con.pstmt.setString(4, sdfHora.format(abonoEmMassa.getDataInicio()));
                con.pstmt.setInt(5, cod_justificativa_selecionada);
                con.pstmt.setInt(6, cod_responsavel);
                con.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
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
/*
    public List<SelectItem> consultaPerfis() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            String sql = "select cod_perfil, nome_perfil from Perfil ORDER BY nome_perfil ";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer perfilID = rs.getInt("cod_perfil");
                String nome = rs.getString("nome_perfil");
                saida.add(new SelectItem(perfilID, nome));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return saida;
    }
*/
    public void updatePerfil(Integer userid, String valor) {
        try {
            String query = "UPDATE USERINFO SET Perfil = ? WHERE userid = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, valor);
                con.pstmt.setInt(2, userid);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public List<SelectItem> consultaDepartamentoHierarquico(Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS ORDER BY SUPDEPTID asc";
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

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS ORDER BY SUPDEPTID asc,DEPTNAME";
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

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public ArrayList<Integer> getTodosOsDescendentes(Integer departamentoSelecionado) {
        ArrayList<Integer> filhos = new ArrayList<Integer>();
        try {
            String sql = "SELECT DEPTID, SUPDEPTID FROM DEPARTMENTS"
                    + " WHERE     (SUPDEPTID = " + departamentoSelecionado + ")";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer deptid = rs.getInt("DEPTID");
                filhos.add(deptid);
                filhos.addAll(getTodosOsDescendentes(deptid));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return filhos;
    }

    public String consultaRegime(Integer cod_regime) {
        String nome = null;
        try {
            String sql = "SELECT nome FROM Regime_HoraExtra WHERE (cod_regime = " + cod_regime + ")";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return nome;
    }

    public String consultaPerfil(Integer cod_perfil) {
        String nome = null;
        try {
            String sql = "SELECT nome_perfil FROM Perfil WHERE (cod_perfil = " + cod_perfil + ")";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome_perfil");
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return nome;
    }

    public String consultaPermissao(Integer permissao) {
        String nome = null;
        try {
            String sql = "SELECT DEPTNAME FROM DEPARTMENTS WHERE (DEPTID = " + permissao + ")";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                nome = rs.getString("DEPTNAME");
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return nome;
    }

    public boolean addConexao(Conexao conn) {
        boolean ok = false;
        try {
            String query = "INSERT INTO databanks (server, port, banco, login, senha, alias) VALUES (?,?,?,?,?,?) ";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, conn.getServer());
                con.pstmt.setString(2, conn.getPort());
                con.pstmt.setString(3, conn.getDatabase());
                con.pstmt.setString(4, conn.getUsuario());
                con.pstmt.setString(5, conn.getSenha());
                con.pstmt.setString(6, conn.getDesc());
                con.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean editConexao(Conexao conn) {
        boolean ok = false;
        try {

            String query = "UPDATE databanks SET server = ?, port = ?, banco = ?, login = ?, senha = ?, alias = ? WHERE conectionId = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setString(1, conn.getServer());
                con.pstmt.setString(2, conn.getPort());
                con.pstmt.setString(3, conn.getDatabase());
                con.pstmt.setString(4, conn.getUsuario());
                con.pstmt.setString(5, conn.getSenha());
                con.pstmt.setString(6, conn.getDesc());
                con.pstmt.setInt(7, conn.getConnid());
                con.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public boolean deleteConexao(Conexao conn) {
        boolean ok = false;
        try {
            String query = "DELETE FROM databanks WHERE conectionId = ?";
            if (con.prepareStatement(query)) {
                con.pstmt.setInt(1, conn.getConnid());
                con.executeUpdate();
                ok = true;
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public List<Conexao> consultaConexoes() {
        List<Conexao> saida = new ArrayList<Conexao>();
        try {
            String sql = " select * from databanks order by alias";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer connid = rs.getInt("conectionId");
                String server = rs.getString("server");
                String port = rs.getString("port");
                String database = rs.getString("banco");
                String usuario = rs.getString("login");
                String senha = rs.getString("senha");
                String desc = rs.getString("alias");
                saida.add(new Conexao(connid, server, port, database, usuario, senha, desc));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public Conexao consultaConexao(int connId) {
        Conexao conn = new Conexao();
        conn.CleanConexao();
        try {
            String sql = "select * from dbo.DataBanks where conectionId = " + connId;
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer connid = rs.getInt("conectionId");
                String server = rs.getString("server");
                String port = rs.getString("port");
                String database = rs.getString("banco");
                String usuario = rs.getString("login");
                String senha = rs.getString("senha");
                String desc = rs.getString("alias");
                conn = (new Conexao(connid, server, port, database, usuario, senha, desc));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return conn;
    }

    public Conexao consultaConexaoByDesc(String descricao) {
        Conexao conn = new Conexao();
        conn.CleanConexao();
        try {
            String sql = "select * from dbo.DataBanks where alias like '%" + descricao + "%'";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                Integer connid = rs.getInt("conectionId");
                String server = rs.getString("server");
                String port = rs.getString("port");
                String database = rs.getString("banco");
                String usuario = rs.getString("login");
                String senha = rs.getString("senha");
                String desc = rs.getString("alias");
                conn = (new Conexao(connid, server, port, database, usuario, senha, desc));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return conn;
    }

}
