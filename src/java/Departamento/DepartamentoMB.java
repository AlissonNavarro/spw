package Departamento;

import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

public class DepartamentoMB implements Serializable {

    // Departamento Hieraquico
    List<Integer> deptList = new ArrayList<Integer>();
    AcessoBD con;

    public DepartamentoMB() {
        con = new AcessoBD();
    }

    public List<SelectItem> consultaDepartamentoOrdernado() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        try {
            String sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";
            ResultSet rs = con.executeQuery(sql);
            //saida.add(new SelectItem(-1, "Selecione o departamento"));

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
        } catch (Exception ex) {
            System.out.println("DepartamentoMB consultaDepartamentoOrdernado " + ex);
        } finally {
            con.Desconectar();
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
            String sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            ResultSet rs = con.executeQuery(sql);
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

            Integer raiz = Integer.parseInt(deptOrdersList.get(0).getValue().toString());
            List<Integer> deptSrtList = ordenarDepts(raiz, deptOrdersList);
            for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
                Integer id_dept = it.next();
                Integer espaces = getEspace(id_dept, idList, idToSuperHash);
                saida.add(new SelectItem(id_dept, espaces(espaces) + idToNomeHash.get(id_dept), "", false, false));
            }

        } catch (Exception ex) {
            System.out.println("DepartamentoMB consultaDepartamentoDestinoOrdernado " + ex);
        } finally {
            con.Desconectar();
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
        try {
            String sql = "select deptName from Departments "
                    + " where deptName = '" + novoDepartamentoNome + "'";
            try (ResultSet rs = con.executeQuery(sql)) {
                if (rs.next()) {
                    flag = 1;
                }
            }

            if (flag == 0) {
                String query = "insert into Departments(DEPTNAME, SUPDEPTID, igroup, InheritParentSch, InheritDeptSch, InheritDeptSchClass, AutoSchPlan, InLate, OutEarly, InheritDeptRule, "
                        + "MinAutoSchInterval, RegisterOT, DefaultSchId, ATT, Holiday, OverTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, novoDepartamentoNome);
                    con.pstmt.setInt(2, departamentoDestino);
                    con.pstmt.setInt(3, 0);
                    con.pstmt.setInt(4, 1);
                    con.pstmt.setInt(5, 1);
                    con.pstmt.setInt(6, 1);
                    con.pstmt.setInt(7, 1);
                    con.pstmt.setInt(8, 1);
                    con.pstmt.setInt(9, 1);
                    con.pstmt.setInt(10, 1);
                    con.pstmt.setInt(11, 24);
                    con.pstmt.setInt(12, 1);
                    con.pstmt.setInt(13, 1);
                    con.pstmt.setInt(14, 1);
                    con.pstmt.setInt(15, 1);
                    con.pstmt.setInt(16, 1);
                    con.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("DepartamentoMB salvarNovoDepartamento " + ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Integer salvarEditDepartamento(Departamento depto) {
        int flag = 0;
        try {
            String sql = "select deptName from Departments "
                    + " WHERE     (DEPTID <> " + depto.getId() + ") AND (DEPTNAME = '" + depto.getNome() + "') ";
            try (ResultSet rs = con.executeQuery(sql)) {
                while (rs.next()) {
                    flag = 1;
                }
            }

            if (flag == 0) {
                String query = "UPDATE DEPARTMENTS SET DEPTNAME = ?, SUPDEPTID = ? WHERE DEPTID = ?";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, depto.getNome());
                    con.pstmt.setInt(2, depto.getSuperDeptoId());
                    con.pstmt.setInt(3, depto.getId());
                    con.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Departamento salvarEditDepartamento " + ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public Boolean temFilhos(Integer departamentoSelecionado) {
        Boolean temFilhos = false;
        try {
            String sql = "SELECT DEPTID, SUPDEPTID FROM DEPARTMENTS"
                    + " WHERE (SUPDEPTID = " + departamentoSelecionado + ")";
            try (ResultSet rs = con.executeQuery(sql)) {
                if (rs.next()) {
                    temFilhos = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Departamento temFilhos " + ex);
        }
        return temFilhos;
    }

    public Boolean temFuncionariosAlocados(Integer departamentoSelecionado) {
        Boolean temFuncionariosAlocados = false;
        try {
            String sql = "SELECT defaultdeptid FROM Userinfo"
                    + " WHERE (defaultdeptid = " + departamentoSelecionado + ")";
            try (ResultSet rs = con.executeQuery(sql)) {
                if (rs.next()) {
                    temFuncionariosAlocados = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Departamento temFuncionariosAlocados " + ex);
        }
        return temFuncionariosAlocados;
    }

    public Integer consultaDeptoPai(Integer departamentoSelecionado) {
        Integer supdeptid = -1;
        try {
            String sql = "SELECT DEPTID, SUPDEPTID FROM DEPARTMENTS"
                    + " WHERE (DEPTID = " + departamentoSelecionado + ")";
            ResultSet rs = con.executeQuery(sql);
            while (rs.next()) {
                supdeptid = rs.getInt("SUPDEPTID");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Departamento consultaDeptoPai " + ex);
        } finally {
            con.Desconectar();
        }
        return supdeptid;
    }

    public ArrayList<Integer> getTodosOsDescendentes(Integer departamentoSelecionado) {
        ArrayList<Integer> filhos = new ArrayList<Integer>();
        try {
            String sql = "SELECT DEPTID, SUPDEPTID FROM DEPARTMENTS"
                    + " WHERE (SUPDEPTID = " + departamentoSelecionado + ")";
            ResultSet rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer deptid = rs.getInt("DEPTID");
                filhos.add(deptid);
                DepartamentoMB b = new DepartamentoMB();
                filhos.addAll(b.getTodosOsDescendentes(deptid));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Departamento getTodosOsDescendentes " + ex);
        } finally {
            con.Desconectar();
        }
        return filhos;
    }

    public boolean excluirDepartamento(Integer departamentoSelecionado) {
        String sql = "select * from userinfo where defaultdeptid = " + departamentoSelecionado;
        try {
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                return false;
            }
            rs.close();
            sql = "delete from DEPARTMENTS where DEPTID = " + departamentoSelecionado;
            con.executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            System.out.println("DeparamentoMB excluirDepartamento " + ex);
        } finally {
            con.Desconectar();
        }
        return false;
    }
}
