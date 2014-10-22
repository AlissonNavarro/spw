/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Afastamento;

import Administracao.*;
import CadastroDeCategoriaAfastamento.CategoriaAfastamento;

import Funcionario.Funcionario;
import Metodos.Metodos;
import Usuario.Usuario;
import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;

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
public class Banco implements Serializable {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Afastamento: Conectar: "+cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Afastamento: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public List<SelectItem> consultaCategoriaAfastamento() {

        List<SelectItem> categoriaAfastamentoList = new ArrayList<SelectItem>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select * from CATEGORIAAFASTAMENTO";


            stmt = c.createStatement();
            rs = stmt.executeQuery(query);
            categoriaAfastamentoList.add(new SelectItem(-1, "Selecione a categoria"));

            while (rs.next()) {
                Integer cod_afastamento = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");

                categoriaAfastamentoList.add(new SelectItem(cod_afastamento, descAfastamento));
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
        return categoriaAfastamentoList;
    }

    public boolean insertAfastamento(List<SelectItem> funcionarioList, Afastamento afastamento) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {


            SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
            c.setAutoCommit(false);
            for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {

                String dataInicio = (sdfDia.format(afastamento.getDataInicio()));
                String dataFim = (sdfDia.format(afastamento.getDataFim()));

                SelectItem selectItem = it.next();
                Integer cod_funcionario = (Integer) selectItem.getValue();
                if (!(cod_funcionario.equals(0) || cod_funcionario.equals(-1))) {

                    String query = "INSERT INTO AFASTAMENTO(userid,cod_categoria,inicioAfastamento," +
                            "fimAfastamento,cod_responsavel) values(?,?,?,?,?)";

                    pstmt = c.prepareStatement(query);
                    pstmt.setInt(1, cod_funcionario);
                    pstmt.setInt(2, afastamento.getCategoriaAfastamento().getCategoriaAfastamentoID());
                    pstmt.setString(3, dataInicio);
                    pstmt.setString(4, dataFim);
                    pstmt.setInt(5, new Integer(afastamento.getResponsavel().getLogin().toString()));

                    pstmt.executeUpdate();
                }
            }
            c.commit();

        } catch (Exception e) {
            try {
                c.rollback();
            } catch (SQLException ex) {
                System.out.println("Afastamento: insertAfastamento: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public boolean insertAfastamento(Integer cod_funcionario, Afastamento afastamento) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {

            SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");

            String dataInicio = (sdfDia.format(afastamento.getDataInicio()));
            String dataFim = (sdfDia.format(afastamento.getDataFim()));

            if (!(cod_funcionario.equals(0) || cod_funcionario.equals(-1))) {

                String query = "INSERT INTO AFASTAMENTO(userid,cod_categoria,inicioAfastamento," +
                        "fimAfastamento,cod_responsavel) values(?,?,?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, cod_funcionario);
                pstmt.setInt(2, afastamento.getCategoriaAfastamento().getCategoriaAfastamentoID());
                pstmt.setString(3, dataInicio);
                pstmt.setString(4, dataFim);
                pstmt.setInt(5, new Integer(afastamento.getResponsavel().getLogin().toString()));

                pstmt.executeUpdate();


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

    public void deleteTodosAfastamento(List<Afastamento> afastamentoList) {

        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            c.setAutoCommit(false);
        } catch (SQLException ex) {
            System.out.println("Afastamento: deleteTodosAfastamento 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (Iterator<Afastamento> it = afastamentoList.iterator(); it.hasNext();) {
                Afastamento afastamento = it.next();

                String inicioStr = sdf.format(afastamento.getDataInicio());

                String queryDelete = "delete from Afastamento "
                        + " where userid = " + afastamento.getFuncionario().getFuncionarioId()
                        + " and inicioAfastamento = '" + inicioStr + "'";

                pstmt = c.prepareStatement(queryDelete);
                pstmt.executeUpdate();
                pstmt.close();
            }
            c.commit();
        } catch (SQLException ex) {
            System.out.println("Afastamento: deleteTodosAfastament 2: "+ex);
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

    public Boolean deleteAfastamento(Afastamento afastamento) {
        Boolean flag = true;
        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioStr = sdf.format(afastamento.getDataInicio());

        String queryDelete = "delete from AFASTAMENTO "
                + " where userid = " + afastamento.getFuncionario().getFuncionarioId()
                + " and inicioAfastamento = '" + inicioStr + "'";

        try {
            pstmt = c.prepareStatement(queryDelete);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            flag = false;
            System.out.println("Afastamento: deleteAfastamento 2: "+ex);
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

    public List<Afastamento> consultaAfastamentoByDepartameto(Integer deptID, Date inicio, Date fim) {

        List<Afastamento> afastamentoList = new ArrayList<Afastamento>();
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

            sql = "SELECT u.name,u.userid,u.defaultdeptid, a.*, ca.*, "
                    + "(select name from userinfo where userid = a.cod_responsavel) as responsavel"
                    + " FROM Afastamento a, USERINFO u, CategoriaAfastamento ca"
                    + " where u.userid = a.userid and "
                    + " ca.cod_categoria = a.cod_categoria and "
                    + " ((a.inicioAfastamento" + " >= '" + inicioStr + "' and " + " a.inicioAfastamento <= '" + fimStr + "') or "
                    + " (a.fimAfastamento" + " >= '" + inicioStr + "' and " + " a.fimAfastamento <= '" + fimStr + "'))"
                    + " order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            int i = 0;
            while (rs.next()) {

                Integer dept = rs.getInt("defaultdeptid");
                String nome = rs.getString("name");
                Integer userid = rs.getInt("userid");
                Timestamp inicioAfastamento = rs.getTimestamp("inicioAfastamento");
                Timestamp fimAfastamento = rs.getTimestamp("fimAfastamento");
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String responsavel = rs.getString("responsavel");

                if (deptIDList.contains(dept)) {
                    Afastamento afastamento = new Afastamento();
                    Funcionario funcionario = new Funcionario();
                    funcionario.setFuncionarioId(userid);
                    funcionario.setNome(nome);
                    afastamento.setFuncionario(funcionario);
                    afastamento.setDataInicio(inicioAfastamento);
                    afastamento.setDataFim(fimAfastamento);
                    CategoriaAfastamento categoriaAfastamento = new CategoriaAfastamento();
                    categoriaAfastamento.setCategoriaAfastamentoID(cod_categoria);
                    categoriaAfastamento.setDescCategoriaAfastamento(descAfastamento);
                    afastamento.setCategoriaAfastamento(categoriaAfastamento);
                    Usuario responsavel_ = new Usuario();
                    responsavel_.setNome(responsavel);
                    afastamento.setResponsavel(responsavel_);

                    afastamento.setIndex(i);
                    afastamentoList.add(afastamento);
                    i++;
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
        return afastamentoList;
    }

    public boolean alterarAfastamento(Afastamento afastamentoAntigo, Afastamento afastamentoNovo, Usuario responsavel) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");

        String query = "update AFASTAMENTO set cod_categoria=?,inicioAfastamento=?, fimAfastamento=?, cod_responsavel = ? "
                + " where userid=? and inicioAfastamento=?";
        try {
            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, afastamentoNovo.getCategoriaAfastamento().getCategoriaAfastamentoID());
            pstmt.setString(2, sdfHora.format(afastamentoNovo.getDataInicio()));
            pstmt.setString(3, sdfHora.format(afastamentoNovo.getDataFim()));
            pstmt.setInt(4, responsavel.getLogin());
            pstmt.setInt(5, afastamentoNovo.getFuncionario().getFuncionarioId());
            pstmt.setString(6, sdfHora.format(afastamentoAntigo.getDataInicio()));

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

    public List<Afastamento> consultaAfastamentoByFuncionario(Integer cod_funcionario, Date inicio, Date fim) {

        List<Afastamento> afastamentoList = new ArrayList<Afastamento>();

        try {
            ResultSet rs;
            Statement stmt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioStr = sdf.format(inicio);
            String fimStr = sdf.format(fim);
            String sql;

            sql = "SELECT u.name,u.userid,u.defaultdeptid, a.*, ca.*, "
                    + "(select name from userinfo where userid = a.cod_responsavel) as responsavel"
                    + " FROM Afastamento a, USERINFO u, CategoriaAfastamento ca"
                    + " where u.userid = a.userid and "
                    + " u.userid = " + cod_funcionario + "and "
                    + " ca.cod_categoria = a.cod_categoria and "
                    + " ((a.inicioAfastamento >= '" + inicioStr + "' and a.inicioAfastamento <= '" + fimStr + "') or "
                    + " (a.fimAfastamento >= '" + inicioStr + "' and a.fimAfastamento <= '" + fimStr + "') or "
                    + " (a.inicioAfastamento <= '"+ inicioStr + "' and a.fimAfastamento >= '"+ fimStr + "')) "
                    + " order by name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            int i = 0;
            while (rs.next()) {

                String nome = rs.getString("name");
                Integer userid = rs.getInt("userid");
                Timestamp inicioAfastamento = rs.getTimestamp("inicioAfastamento");
                Timestamp fimAfastamento = rs.getTimestamp("fimAfastamento");
                Integer cod_categoria = rs.getInt("cod_categoria");
                String descAfastamento = rs.getString("descAfastamento");
                String responsavel = rs.getString("responsavel");
                Afastamento afastamento = new Afastamento();
                Funcionario funcionario = new Funcionario();
                funcionario.setFuncionarioId(userid);
                funcionario.setNome(nome);
                afastamento.setFuncionario(funcionario);
                afastamento.setDataInicio(inicioAfastamento);
                afastamento.setDataFim(fimAfastamento);
                CategoriaAfastamento categoriaAfastamento = new CategoriaAfastamento();
                categoriaAfastamento.setCategoriaAfastamentoID(cod_categoria);
                categoriaAfastamento.setDescCategoriaAfastamento(descAfastamento);
                afastamento.setCategoriaAfastamento(categoriaAfastamento);
                Usuario responsavel_ = new Usuario();
                responsavel_.setNome(responsavel);
                afastamento.setResponsavel(responsavel_);
                afastamento.setIndex(i);
                afastamentoList.add(afastamento);
                i++;
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
        return afastamentoList;
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
    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();

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

    public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("Afastamento: fecharConexao: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
