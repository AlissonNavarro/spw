/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Departamento;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author amvboas
 */
class Banco implements Serializable {

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
            System.out.println(cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
        }
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
//            saida.add(new SelectItem(0, "Selecione o departamento"));

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

    public List<SelectItem> consultaDepartamentoDestinoOrdernado() {
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
            saida.add(new SelectItem(0, "NENHUM"));

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

    private static String espaces(Integer qnt) {

        String nbsp = "";
        String nbsp_ = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

        for (int i = 1; i
                <= qnt; i++) {
            nbsp += nbsp_;
        }
        return nbsp;
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

    public Integer salvarNovoDepartamento(String novoDepartamentoNome, Integer departamentoDestino) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select deptName from Departments "
                    + " where deptName = '" + novoDepartamentoNome + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "insert into Departments(DEPTNAME, SUPDEPTID, igroup, InheritParentSch, InheritDeptSch, InheritDeptSchClass, AutoSchPlan, InLate, OutEarly, InheritDeptRule, "
                        + "MinAutoSchInterval, RegisterOT, DefaultSchId, ATT, Holiday, OverTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novoDepartamentoNome);
                pstmt.setInt(2, departamentoDestino);
                pstmt.setInt(3, 0);
                pstmt.setInt(4, 1);
                pstmt.setInt(5, 1);
                pstmt.setInt(6, 1);
                pstmt.setInt(7, 1);
                pstmt.setInt(8, 1);
                pstmt.setInt(9, 1);
                pstmt.setInt(10, 1);
                pstmt.setInt(11, 24);
                pstmt.setInt(12, 1);
                pstmt.setInt(13, 1);
                pstmt.setInt(14, 1);
                pstmt.setInt(15, 1);
                pstmt.setInt(16, 1);

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

    public Integer salvarEditDepartamento(Departamento depto) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select deptName from Departments "
                    + " WHERE     (DEPTID <> " + depto.getId() + ") AND (DEPTNAME = '" + depto.getNomeDepartamento() + "') ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "UPDATE DEPARTMENTS SET DEPTNAME = ?, SUPDEPTID = ? WHERE DEPTID = ?";
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, depto.getNomeDepartamento());
                pstmt.setInt(2, depto.getSuperDeptoId());
                pstmt.setInt(3, depto.getId());

                pstmt.executeUpdate();
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

    Boolean temFilhos(Integer departamentoSelecionado) {
        Boolean temFilhos = false;
        try {
            ResultSet rs;
            Statement stmt;


            String sql;

            sql = "SELECT     DEPTID, SUPDEPTID"
                    + " FROM         DEPARTMENTS"
                    + " WHERE     (SUPDEPTID = " + departamentoSelecionado + ")";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                temFilhos = true;
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return temFilhos;
    }

    Boolean temFuncionariosAlocados(Integer departamentoSelecionado) {
        Boolean temFuncionariosAlocados = false;
        try {
            ResultSet rs;
            Statement stmt;


            String sql;

            sql = "SELECT     defaultdeptid"
                    + " FROM         Userinfo"
                    + " WHERE     (defaultdeptid = " + departamentoSelecionado + ")";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                temFuncionariosAlocados = true;
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return temFuncionariosAlocados;
    }

    Integer consultaDeptoPai(Integer departamentoSelecionado) {
        Integer supdeptid = -1;
        try {
            ResultSet rs;
            Statement stmt;


            String sql;

            sql = "SELECT     DEPTID, SUPDEPTID"
                    + " FROM         DEPARTMENTS"
                    + " WHERE     (DEPTID = " + departamentoSelecionado + ")";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                supdeptid = rs.getInt("SUPDEPTID");

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
        return supdeptid;
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
                Banco b = new Banco();
                filhos.addAll(b.getTodosOsDescendentes(deptid));
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
        return filhos;
    }

    public Boolean excluirDepartamento(Integer departamentoSelecionado) {
        Boolean flag = false;
        PreparedStatement pstmt = null;

        String querySelect = "select * from userinfo where defaultdeptid = "+departamentoSelecionado;

        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(querySelect);

            while (rs.next()) {
                flag = true;
                return flag;
            }
            rs.close();
            stmt.close();
            if (!flag) {
                
                String queryDelete = "delete from DEPARTMENTS "
                        + " where DEPTID = " + departamentoSelecionado;
                try {
                    pstmt = c.prepareStatement(queryDelete);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    flag = true;
                } finally {
                    try {
                        if (c != null) {
                            c.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (SQLException e) {
            flag = true;
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
}
