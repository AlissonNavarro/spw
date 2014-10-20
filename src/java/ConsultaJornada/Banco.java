/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaJornada;

/**
 *
 * @author Alexandre
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Metodos.Metodos;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class Banco {

    Driver d;
    Connection c;

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("ConsultaJornada: Banco 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<SelectItem> consultaDepartamento(Integer permissao) {
        List<SelectItem> departamentoList = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

             sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(permissao);
            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                if (deptIDList.contains(supdeptid)) {
                    deptIDList.add(cod);
                }
            }
            rs.close();
            stmt.close();

            sql = "select DEPTNAME,deptId from DEPARTMENTS ORDER BY DEPTNAME asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            departamentoList.add(new SelectItem(-1, "Selecione um departamento"));

            while (rs.next()) {
                String title = rs.getString("DEPTNAME");
                Integer deptId = rs.getInt("deptId");
                SelectItem selectItem = new SelectItem(deptId, title);
                if (title != null) {
                     if (deptIDList.contains(deptId))
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

    public String consultaNomeDepartamento(Integer cod) {
        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        String codigo = "";
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select deptname from DEPARTMENTS "
                    + " where deptid = " + cod;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            departamentoList.add(new SelectItem(-1, "Selecione um departamento"));

            while (rs.next()) {
                codigo = rs.getString("DEPTNAME");
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
        return codigo;
    }

    public List<JornadaExibicao> consultaJornadas(Integer cod_departamento, Date data) {

        List<JornadaFunc> jornadaList = new ArrayList<JornadaFunc>();
        List<JornadaExibicao> jornadaExibicaoList = new ArrayList<JornadaExibicao>();

        try {
            ResultSet rs;
            Statement stmt;

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dataSrt = df.format(data);

            String sql = "select  u.name as nome,NR.NAME as jornada,n.sdays from"
                    + " user_of_run ur, num_run nr,userinfo u,num_run_deil n"
                    + " where  UR.NUM_OF_RUN_ID = nr.num_runid and"
                    + " u.userid = ur.userid and"
                    + " n.num_runid = nr.num_runid and"
                    + " u.defaultdeptid = " + cod_departamento + " and"
                    + " '" + dataSrt + "' between ur.startdate and ur.enddate"
                    + " order by ur.num_of_run_id,u.name";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                String nome = rs.getString("nome");
                String nomeJornada = rs.getString("jornada");
                Integer sdays = rs.getInt("sdays");

                if (jornadaList.size() != 0 && jornadaList.get(i - 1).getFuncionario().equals(nome)
                        && !jornadaList.get(i - 1).getDias().contains(sdays)) {
                    jornadaList.get(i - 1).getDias().add(sdays);
                } else if (jornadaList.size() == 0 || !jornadaList.get(i - 1).getFuncionario().equals(nome)) {
                    jornadaList.add(new JornadaFunc(nomeJornada, nome));
                    i++;
                }
            }

            rs.close();
            stmt.close();

            i = -1;

            for (Iterator<JornadaFunc> it = jornadaList.iterator(); it.hasNext();) {

                JornadaFunc jornadaFunc = it.next();

                if (jornadaExibicaoList.size() == 0 || !jornadaExibicaoList.get(i).getNomeJornada().equals(jornadaFunc.getNome())) {
                    jornadaExibicaoList.add(new JornadaExibicao(jornadaFunc.getNome()));
                    i++;
                }
                if (jornadaExibicaoList.get(i).getNomeJornada().equals(jornadaFunc.getNome())) {

                    if (jornadaFunc.getDias().contains(1)) {
                        jornadaExibicaoList.get(i).getFuncSegunda().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(2)) {
                        jornadaExibicaoList.get(i).getFuncTerca().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(3)) {
                        jornadaExibicaoList.get(i).getFuncQuarta().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(4)) {
                        jornadaExibicaoList.get(i).getFuncQuinta().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(5)) {
                        jornadaExibicaoList.get(i).getFuncSexta().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(6)) {
                        jornadaExibicaoList.get(i).getFuncSabado().add(jornadaFunc.getFuncionario() + "\n");
                    }

                    if (jornadaFunc.getDias().contains(7)) {
                        jornadaExibicaoList.get(i).getFuncDomingo().add(jornadaFunc.getFuncionario() + "\n");
                    }
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
        return jornadaExibicaoList;
    }

    public boolean insertRelatorio(List<JornadaExibicao> jornadaExibicao) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        String query = "insert into RelatorioEscala(nomeJornada,segunda,terca,quarta,quinta,sexta) values(?,?,?,?,?,?)";
        String queryDelete = "delete from RelatorioEscala";

        try {
            pstmt = c.prepareStatement(queryDelete);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("ConsultaJornada: insertRelatorio 1: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            pstmt = c.prepareStatement(query);
        } catch (SQLException ex) {
            System.out.println("ConsultaJornada: insertRelatorio 2: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (Iterator<JornadaExibicao> it = jornadaExibicao.iterator(); it.hasNext();) {

                JornadaExibicao jornadaExibicao_ = it.next();

                pstmt.setString(1, jornadaExibicao_.getNomeJornada());
                pstmt.setString(2, jornadaExibicao_.getSegunda());
                pstmt.setString(3, jornadaExibicao_.getTerca());
                pstmt.setString(4, jornadaExibicao_.getQuarta());
                pstmt.setString(5, jornadaExibicao_.getQuinta());
                pstmt.setString(6, jornadaExibicao_.getSexta());
                pstmt.executeUpdate();
            }
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

    public void closeConection() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("ConsultaJornada: closeConection 1: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {

 
       
    }
}
