/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Abono.AbonoEmMassaADD;
import Abono.AbonoEmMassa;
import Metodos.Metodos;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import org.apache.poi.hssf.record.ContinueRecord;

/**
 *
 * @author amsgama
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexandre
 */
public class Banco {

    Driver d;
    Connection c;
    Boolean hasConnection = false;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        hasConnection = true;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            hasConnection = false;
            System.out.println("Administracao: Conectar: " + cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            hasConnection = false;
            System.out.println("Administracao: Banco 1: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SaveConfig(Boolean saveWithPIN){
        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE config SET checkWithPIN = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setBoolean(1, saveWithPIN);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public void CorrigePIN(boolean checkWithPIN) {
        try {
            ResultSet rs = null;
            Statement stmt = null;
            Statement stmt2 = null;
            

            String query = "select userid,badgenumber from userinfo order by userid";
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            
            int userid;
            String badgenumber;
            while(rs.next()) {
                stmt2 = c.createStatement();
                badgenumber = rs.getString("badgenumber");
                userid = rs.getInt("userid");
                if (checkWithPIN) {
                    badgenumber = badgenumber.replaceAll("\\D", "");
                } else {
                    badgenumber = badgenumber.replaceAll("\\D", "");
                    badgenumber = badgenumber + "A";
                }
                try {
                    query = "update userinfo set badgenumber='"+badgenumber+"' where userid="+userid;
                    stmt2.executeUpdate(query);
                } catch(Exception ex) {
                    System.out.println("Administracao.Banco.CorrigePIN() " + ex.getMessage());
                    query = "delete from USERINFO where USERID="+userid;
                    stmt2.executeUpdate(query);
                    System.out.println("Registro duplicado, deletado userid="+userid);   
                }
                query="";
                userid = -1;
                badgenumber = "";
            }
        } catch (SQLException ex) {
            System.out.println("Administracao.Banco.CorrigePIN() " + ex.getMessage());
        }
        try {
            c.close();
        } catch (SQLException ex) {
            System.out.println("Administracao.Banco.CorrigePIN() " + ex.getMessage());
        }
    }
    
    public boolean CheckWithPIN() {
        boolean result = false;
        try {
            ResultSet rs = null;
            Statement stmt = null;

            String query = "select checkWithPIN from config";
            
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                result = rs.getBoolean("checkWithPIN");
            }
        } catch (SQLException ex) {
            System.out.println("Administracao.Banco.CheckWithPIN() " + ex.getMessage());
        }            
        return result;

    }

    public List<Permissao> usuarioPermissao(String filtro, List<Integer> departamentolistDosFuncionarios) {

        List<Permissao> permissaoList = new ArrayList<Permissao>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select u.userid,u.badgenumber,u.ssn,u.name,u.cod_regime,u.permissao,u.perfil,u.DEFAULTDEPTID, d.deptname,  r.nome as nome_regime "
                    + "             from userinfo as u inner join departments as d on (u.defaultdeptid = d.deptid) "
                    + "             left join Regime_HoraExtra as r on (u.cod_regime = r.cod_regime)"
                    + "             where  u.name like  '" + filtro + "%'"
                    + "		    order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(query);

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
        return permissaoList;
    }

    public List<Permissao> usuarioPermissaoSuperAdmin(String filtro) {

        List<Permissao> permissaoList = new ArrayList<Permissao>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select u.userid,u.badgenumber,u.ssn,u.name,u.cod_regime,u.permissao,u.perfil,u.DEFAULTDEPTID, d.deptname,  r.nome as nome_regime "
                    + "             from userinfo as u inner join departments as d on (u.defaultdeptid = d.deptid) "
                    + "             left join Regime_HoraExtra as r on (u.cod_regime = r.cod_regime)"
                    + "             where  u.name like  '" + filtro + "%'"
                    + "		    order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(query);

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
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return permissaoList;
    }

    public List<SelectItem> consultaDepartamento() {

        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select DEPTID,DEPTNAME from DEPARTMENTS ORDER BY DEPTNAME asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
        return departamentoList;
    }

    public void updatePermissao(Integer userid, String valor) {

        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE USERINFO SET Permissao = ? WHERE userid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, valor);
            pstmt.setInt(2, userid);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public Boolean updateRegime(Integer userid, Integer cod_regime) {

        PreparedStatement pstmt = null;
        Boolean flag = true;
        try {

            String query = "UPDATE USERINFO SET cod_regime = ? WHERE userid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_regime);
            pstmt.setInt(2, userid);

            pstmt.executeUpdate();
        } catch (Exception e) {
            flag = false;
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
        return flag;
    }

    public void zerarSenhaUsuario(int idUsuario) {
        PreparedStatement pstmt = null;
        try {
            String query = "UPDATE USERINFO SET senha = NULL WHERE userid = ?";
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, idUsuario);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public boolean isDescricaoObrigatoria(Integer cod_justificativa) {
        boolean retorno = false;
        PreparedStatement pstmt = null;
        ResultSet rs;
        try {

            String query = "select descricaoObrigatoria from leaveClass"
                    + " where leaveid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, cod_justificativa);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                retorno = rs.getBoolean("descricaoObrigatoria");
            }

            pstmt.close();
        } catch (Exception e) {
            retorno = false;
            System.out.println(e.getMessage());
        }

        return retorno;
    }

    public List<SelectItem> consultaRegime() {

        List<SelectItem> regimeSelectItemList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from Regime_HoraExtra ORDER BY nome";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            regimeSelectItemList.add(new SelectItem(0, "Escolha o regime"));
            while (rs.next()) {
                Integer cod = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                SelectItem regimeSelectItem = new SelectItem(cod, nome);
                regimeSelectItemList.add(regimeSelectItem);
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
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

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
            stmt.close();

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
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
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
            Statement stmt;

            String sql;

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
            stmt.close();

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
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
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
                        + " from USERINFO u "
                        + " where ativo = 'true'"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
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
            } else {
                sql = "select distinct u.userid,u.name,u.badgenumber,u.ssn from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep + " and "
                        + " ativo = 'true'"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
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

    public List<SelectItem> consultaJustificativa() {

        List<SelectItem> justificativaList = new ArrayList<SelectItem>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select l.leaveid,l.leavename from LEAVECLASS l";


            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            justificativaList.add(new SelectItem(-1, "Selecione a Justificativa"));

            while (rs.next()) {
                Integer cod = rs.getInt("leaveid");
                String nome = rs.getString("leavename");
                justificativaList.add(new SelectItem(cod, nome));
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
        return justificativaList;
    }

    public boolean insertAbonoEmMassa(Integer cod_funcionario, AbonoEmMassaADD novoAbonoEmMassa, Integer cod_responsavel) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {

            SimpleDateFormat sdfDiaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");

            String fimDia = somarStrDias(sdfDia.format(novoAbonoEmMassa.getDataFim()));

            String correnteDia = sdfDia.format(novoAbonoEmMassa.getDataInicio());
            do {
                String dataAtual = sdfDiaHora.format(new java.util.Date());

                String query = "INSERT INTO USER_SPEDAY(userid,startspecday,endspecday,dateid,yuanying,date,RESPONSAVEL) values(?,?,?,?,?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_funcionario);
                pstmt.setString(2, correnteDia + " " + novoAbonoEmMassa.getHoraInicio() + ":00");
                pstmt.setString(3, correnteDia + " " + novoAbonoEmMassa.getHoraFim() + ":00");
                pstmt.setInt(4, novoAbonoEmMassa.getCod_justificativa());
                pstmt.setString(5, novoAbonoEmMassa.getDescricaoJustificativa());
                pstmt.setString(6, dataAtual);
                pstmt.setInt(7, cod_responsavel);

                correnteDia = somarStrDias(correnteDia);
                try {
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    System.out.println("Erro1:" + e);
                }
            } while (!correnteDia.equals(fimDia));

        } catch (SQLException e) {
            ok = false;
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                ok = false;
                System.out.println("Erro:" + e);
            }
        }
        return ok;
    }

    public boolean insertAbonoEmMassaEmMassa(List<SelectItem> funcionarioList, AbonoEmMassaADD novoAbonoEmMassa, Integer cod_responsavel) {
        boolean ok = true;
        PreparedStatement pstmt = null;
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

                        pstmt = c.prepareStatement(query);
                        pstmt.setInt(1, cod_funcionario);
                        pstmt.setString(2, correnteDia + " " + novoAbonoEmMassa.getHoraInicio() + ":00");
                        pstmt.setString(3, correnteDia + " " + novoAbonoEmMassa.getHoraFim() + ":59");
                        pstmt.setInt(4, novoAbonoEmMassa.getCod_justificativa());
                        pstmt.setString(5, novoAbonoEmMassa.getDescricaoJustificativa());
                        pstmt.setString(6, dataAtual);
                        pstmt.setInt(7, cod_responsavel);

                        correnteDia = somarStrDias(correnteDia);

                        try {
                            pstmt.executeUpdate();
                        } catch (Exception e) {
                            System.out.println("Erro2:" + e);
                        }
                    } while (!correnteDia.equals(fimDia));
                }
            }

        } catch (Exception e) {
            ok = false;
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                ok = false;
                System.out.println("Erro:" + e);
            }
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

    public List<AbonoEmMassa> consultaAbonoEmMassaByDepartameto(Integer deptID, Date inicio, Date fim) {

        List<AbonoEmMassa> abonoEmMassaList = new ArrayList<AbonoEmMassa>();
        List<Integer> deptIDList;

        try {
            ResultSet rs;
            Statement stmt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdf.format(inicio);
            String fimStr = sdf.format(fim);

            String sql;

            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            deptIDList = new ArrayList<Integer>();
            deptIDList.add(deptID);

            HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            stmt.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(deptID, deptPermitidos, idToSuperHash);

            sql = "SELECT u.name,u.userid,u.defaultdeptid,l.leavename, us.*"
                    + " FROM USER_SPEDAY us,USERINFO u,leaveClass l"
                    + " where u.userid = us.userid and "
                    + " l.leaveid = us.dateid and "
                    + " us.STARTSPECDAY" + " >= '" + inicioStr + "' and " + " us.STARTSPECDAY < '" + fimStr + "'"
                    + " order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
        return abonoEmMassaList;
    }

    public List<AbonoEmMassa> consultaAbonoEmMassaByFuncionario(Integer cod_funcionario, Date inicio, Date fim) {

        List<AbonoEmMassa> abonoEmMassaList = new ArrayList<AbonoEmMassa>();

        try {
            ResultSet rs;
            Statement stmt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdf.format(inicio);
            String fimStr = sdf.format(fim);
            String sql;

            sql = "SELECT u.name,u.userid,u.defaultdeptid,l.leavename, us.*"
                    + " FROM USER_SPEDAY us,USERINFO u,leaveClass l"
                    + " where u.userid = us.userid and"
                    + " us.userid = " + cod_funcionario + " and "
                    + " l.leaveid = us.dateid and "
                    + " us.STARTSPECDAY" + " between '" + inicioStr + "' and '" + fimStr + "'"
                    + " order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
        return abonoEmMassaList;
    }

    public Boolean deleteAbonoEmMassa(AbonoEmMassa abonoEmMassa) {
        Boolean flag = true;
        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdf.format(abonoEmMassa.getInicio());

        String queryDelete = "delete from USER_SPEDAY "
                + " where userid = " + abonoEmMassa.getCod_funcionario()
                + " and startspecday = '" + inicioStr
                + "' and dateid = " + abonoEmMassa.getCod_categoria();
        try {
            pstmt = c.prepareStatement(queryDelete);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            flag = false;
            System.out.println("Administracao: deleteAbonoEmMassa: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
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

    public void deleteTodosAbonoEmMassa(List<AbonoEmMassa> abonoEmMassaList) {

        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            c.setAutoCommit(false);
        } catch (SQLException ex) {
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

                pstmt = c.prepareStatement(queryDelete);
                pstmt.executeUpdate();
                pstmt.close();
            }
            c.commit();
        } catch (SQLException ex) {
            System.out.println("Administracao: deleteTodosAbonoEmMassa 2: " + ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean alterarAbonoEmMassa(Integer cod_funcionario, Integer cod_justificativa_selecionada,
            AbonoEmMassaADD abonoEmMassa, Integer cod_responsavel) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        String query = "update USER_SPEDAY set dateid=?, yuanying=? "
                + " where userid=? and startspecday=? and dateid=? and RESPONSAVEL=?";
        try {
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, abonoEmMassa.getCod_justificativa());
            pstmt.setString(2, abonoEmMassa.getDescricaoJustificativa());
            pstmt.setInt(3, cod_funcionario);
            SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
            pstmt.setString(4, sdfHora.format(abonoEmMassa.getDataInicio()));
            pstmt.setInt(5, cod_justificativa_selecionada);
            pstmt.setInt(6, cod_responsavel);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.print(e.getMessage());
            ok = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
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

    public static void main(String[] args) {
        String saldo = " 12";
        System.out.println(saldo.subSequence(0, 1));
        System.out.print(saldo.subSequence(0, 1).equals("-"));
    }

    public List<SelectItem> consultaPerfis() {
        List<SelectItem> saida = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select cod_perfil, nome_perfil from Perfil"
                    + " ORDER BY nome_perfil ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);


            while (rs.next()) {
                Integer perfilID = rs.getInt("cod_perfil");
                String nome = rs.getString("nome_perfil");
                saida.add(new SelectItem(perfilID, nome));
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
        return saida;
    }

    void updatePerfil(Integer userid, String valor) {
        PreparedStatement pstmt = null;

        try {

            String query = "UPDATE USERINFO SET Perfil = ? WHERE userid = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, valor);
            pstmt.setInt(2, userid);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
    }

    public List<SelectItem> consultaDepartamentoHierarquico(Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

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
                }
            }
            rs.close();
            stmt.close();

            Integer raiz = Integer.parseInt(deptOrdersList.get(0).getValue().toString());
            List<Integer> deptSrtList = ordenarDepts(raiz, deptOrdersList);
            saida.add(new SelectItem("-1", "Selecione um departamento"));
            for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
                Integer id_dept = it.next();
                Integer espaces = getEspace(id_dept, idList, idToSuperHash);
                saida.add(new SelectItem(id_dept, espaces(espaces) + idToNomeHash.get(id_dept), "", false, false));
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
        return saida;
    }

    public ArrayList<Integer> getTodosOsDescendentes(Integer departamentoSelecionado) {
        ArrayList<Integer> filhos = new ArrayList<Integer>();
        try {
            ResultSet rs;
            Statement stmt;


            String sql;

            sql = "SELECT     DEPTID, SUPDEPTID"
                    + " FROM         DEPARTMENTS"
                    + " WHERE     (SUPDEPTID = " + departamentoSelecionado + ")";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer deptid = rs.getInt("DEPTID");
                filhos.add(deptid);
                filhos.addAll(getTodosOsDescendentes(deptid));
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return filhos;
    }

    public String consultaRegime(Integer cod_regime) {
        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;

            String sql;


            sql = "SELECT nome FROM Regime_HoraExtra WHERE (cod_regime = " + cod_regime + ")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nome;
    }

    public String consultaPerfil(Integer cod_perfil) {
        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;

            String sql;


            sql = "SELECT nome_perfil FROM Perfil WHERE (cod_perfil = " + cod_perfil + ")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome_perfil");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nome;
    }

    public String consultaPermissao(Integer permissao) {
        String nome = null;
        try {
            ResultSet rs;
            Statement stmt;
            String sql;
            sql = "SELECT DEPTNAME FROM DEPARTMENTS WHERE (DEPTID = " + permissao + ")";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("DEPTNAME");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nome;
    }

    public Boolean addConexao(Conexao conn) {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {

            String query = "INSERT INTO databanks (server, port, banco, login, senha, alias) VALUES (?,?,?,?,?,?) ";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, conn.getServer());
            pstmt.setString(2, conn.getPort());
            pstmt.setString(3, conn.getDatabase());
            pstmt.setString(4, conn.getUsuario());
            pstmt.setString(5, conn.getSenha());
            pstmt.setString(6, conn.getDesc());


            pstmt.executeUpdate();
            ok = true;
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
        return ok;
    }

    public Boolean editConexao(Conexao conn) {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE databanks SET server = ?, port = ?, banco = ?, login = ?, senha = ?, alias = ? WHERE conectionId = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, conn.getServer());
            pstmt.setString(2, conn.getPort());
            pstmt.setString(3, conn.getDatabase());
            pstmt.setString(4, conn.getUsuario());
            pstmt.setString(5, conn.getSenha());
            pstmt.setString(6, conn.getDesc());
            pstmt.setInt(7, conn.getConnid());

            pstmt.executeUpdate();
            ok = true;
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
        return ok;
    }

    public Boolean deleteConexao(Conexao conn) {
        Boolean ok = false;
        PreparedStatement pstmt = null;
        try {

            String query = "DELETE FROM databanks WHERE conectionId = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, conn.getConnid());

            pstmt.executeUpdate();
            ok = true;
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
                System.out.println("Erro:" + e);
            }
        }
        return ok;
    }

    public List<Conexao> consultaConexoes() {
        List<Conexao> saida = new ArrayList<Conexao>();
        try {
            ResultSet rs;
            Statement stmt;

            String sql = " select * from databanks order by alias";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

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
        return saida;
    }

    public Conexao consultaConexao(int connId) {
        String nome = null;
        Conexao conn = new Conexao();
        conn.CleanConexao();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;


            sql = "select * from dbo.DataBanks where conectionId = " + connId;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Conexao consultaConexaoByDesc(String descricao) {
        Conexao conn = new Conexao();
        conn.CleanConexao();
        try {
            ResultSet rs;
            Statement stmt;

            String sql;


            sql = "select * from dbo.DataBanks where alias like '%" + descricao + "%'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
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
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("Administracao: fecharConexao: " + ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
