/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Teste;

/**
 *
 * @author Alexandre
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
//import java.util.logging.Logger;
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
        String url = "jdbc:jtds:sqlserver://FS-DESEN-05:1904/mhf;user=user_web;password=123@net";
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
            System.out.println("Teste: Banco 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SelectItem> consultaEscalas() {
        List<SelectItem> escalaList = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            Map<Integer, List<Integer>> matriculaSchClassIDList = new HashMap<Integer, List<Integer>>();
            List<Integer> funcionariosList = new ArrayList<Integer>();

            sql = "select distinct ur.userid,nrd.schclassid from user_of_run ur, num_run_deil nrd "
                    + "where ur.num_of_run_id = nrd.num_runid "
                    + "order by userid";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer schclassid = rs.getInt("schclassid");

                funcionariosList.add(userid);
                List<Integer> schClassIDList = new ArrayList<Integer>();
                schClassIDList = matriculaSchClassIDList.get(userid);
                if (schClassIDList == null) {
                    schClassIDList = new ArrayList<Integer>();
                    schClassIDList.add(schclassid);
                } else {
                    if (!schClassIDList.contains(schclassid)) {
                        schClassIDList.add(schclassid);
                    }
                }
                matriculaSchClassIDList.put(userid, schClassIDList);
            }

            rs.close();
            stmt.close();

            sql = "select distinct ur.userid,uts.schclassid from user_of_run ur, user_temp_sch uts "
                    + "where ur.userid = uts.userid";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer schclassid = rs.getInt("schclassid");

                funcionariosList.add(userid);
                List<Integer> schClassIDList = new ArrayList<Integer>();
                schClassIDList = matriculaSchClassIDList.get(userid);

                if (schClassIDList == null) {
                    schClassIDList = new ArrayList<Integer>();
                    if (!schclassid.equals(-1)) {
                        schClassIDList.add(schclassid);
                    }
                } else {
                    if (!schClassIDList.contains(schclassid) && (!schclassid.equals(-1))) {
                        schClassIDList.add(schclassid);
                    }
                }
                matriculaSchClassIDList.put(userid, schClassIDList);
            }

            rs.close();
            stmt.close();

            Map<Integer, List<Integer>> deptFuncionariosList = new HashMap<Integer, List<Integer>>();
            sql = "select u.userid,d.deptid from userinfo u, departments d "
                    + "where u.defaultdeptid = d.deptid "
                    + "order by deptid";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer dept = rs.getInt("deptid");
                List<Integer> funcionariosList_ = new ArrayList<Integer>();
                funcionariosList_ = deptFuncionariosList.get(dept);

                if (funcionariosList_ == null) {
                    funcionariosList_ = new ArrayList<Integer>();
                    if (funcionariosList.contains(userid)) {
                        funcionariosList_.add(userid);
                    }
                } else {
                    if (!funcionariosList_.contains(userid)) {
                        if (funcionariosList.contains(userid)) {
                            funcionariosList_.add(userid);
                        }
                    }
                }
                if (!funcionariosList_.isEmpty()) {
                    deptFuncionariosList.put(dept, funcionariosList_);
                }
            }

            rs.close();
            stmt.close();

            sql = "select DEPTID,DEPTNAME,supdeptid from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc,DEPTNAME";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

            while (rs.next()) {
                Integer deptID = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("supdeptid");
                deptOrdersList.add(new SelectItem(deptID, supdeptid.toString()));
            }
            rs.close();
            stmt.close();



            Map<Integer, String> deptIDNomeMap = getMapeamentoDeptIDNomeMap();
            Map<Integer, String> schClassIDNameMap = getMapeamentoSchClassIDNameMap();
            Map<Integer, List<Integer>> deptSchClassIDMap = new HashMap<Integer, List<Integer>>();
            deptSchClassIDMap = getDeptSchClassIDMap(matriculaSchClassIDList, deptFuncionariosList);
            print(deptIDNomeMap, schClassIDNameMap, deptSchClassIDMap, deptOrdersList);

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
        return escalaList;
    }

    private Map<Integer, List<Integer>> getDeptSchClassIDMap(Map<Integer, List<Integer>> matriculaSchClassIDList,
            Map<Integer, List<Integer>> deptFuncionariosList) {

        Map<Integer, List<Integer>> deptSchClassIDMap = new HashMap<Integer, List<Integer>>();

        Iterator iterator = deptFuncionariosList.keySet().iterator();
        while (iterator.hasNext()) {
            Integer dept = (Integer) iterator.next();
            List<Integer> funcionarioPorDepartamentList = deptFuncionariosList.get(dept);
            for (Iterator<Integer> it = funcionarioPorDepartamentList.iterator(); it.hasNext();) {
                Integer funcionario = it.next();

                List<Integer> schClassIDList = new ArrayList<Integer>();
                schClassIDList = matriculaSchClassIDList.get(funcionario);

                if (deptSchClassIDMap.get(dept) == null) {
                    deptSchClassIDMap.put(dept, schClassIDList);
                } else {
                    List<Integer> schClassIDMapList = deptSchClassIDMap.get(dept);
                    schClassIDMapList.addAll(schClassIDList);
                    deptSchClassIDMap.put(dept, schClassIDMapList);
                }
            }
        }
        return deptSchClassIDMap;
    }

    private Map<Integer, String> getMapeamentoDeptIDNomeMap() {

        Map<Integer, String> deptIDNomeMap = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select deptid,deptname from departments";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Integer userid = rs.getInt("deptid");
                String deptname = rs.getString("deptname");
                deptIDNomeMap.put(userid, deptname);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Teste: getMapeamentoDeptIDNomeMap 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }

        return deptIDNomeMap;
    }

    private Map<Integer, String> getMapeamentoSchClassIDNameMap() {

        Map<Integer, String> schClassIDNameMap = new HashMap<Integer, String>();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select * from schclass";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            SimpleDateFormat sdfSemana = new SimpleDateFormat("HH:mm");
            while (rs.next()) {
                Integer userid = rs.getInt("schclassid");
                String deptname = rs.getString("schName");
                Timestamp inicio = rs.getTimestamp("StartTime");
                Timestamp fim = rs.getTimestamp("endtime");
                String inicioStr = sdfSemana.format(inicio.getTime());
                String fimStr = sdfSemana.format(fim.getTime());
                String horario = "("+deptname + " = " + inicioStr + " as " + fimStr+")";
                schClassIDNameMap.put(userid, horario);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Teste: getMapeamentoSchClassIDNameMap 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }

        return schClassIDNameMap;
    }

    private void print(Map<Integer, String> deptIDNomeMap, Map<Integer, String> schClassIDNameMap, Map<Integer, List<Integer>> deptSchClassIDMap, List<SelectItem> deptOrdersList) {

        List<Integer> deptSrtList = ordenarDepts(1, deptOrdersList);

        for (Iterator<Integer> it = deptSrtList.iterator(); it.hasNext();) {
            Integer dept = it.next();
            String deptStr = deptIDNomeMap.get(dept);
            if (deptStr != null) {
                List<Integer> schClassIDList = deptSchClassIDMap.get(dept);
                if (schClassIDList != null) {
                    printLinha(deptStr, schClassIDList, schClassIDNameMap);
                }
            }
        }
    }

    private String printLinha(String deptStr, List<Integer> schClassIDList, Map<Integer, String> schClassIDNameMap) {
        String saida = deptStr + " -> ";
        for (Iterator<Integer> it = schClassIDList.iterator(); it.hasNext();) {
            Integer schClassID = it.next();
            saida += schClassIDNameMap.get(schClassID) + ", ";
        }
        Integer x = saida.length()-2;
        System.out.println(saida.subSequence(0,x));
        System.out.println("");
        return saida;
    }
    List<Integer> deptList = new ArrayList<Integer>();

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

    public static void main(String[] args) throws ParseException {
        Banco b = new Banco();
        b.consultaEscalas();
    }
}
