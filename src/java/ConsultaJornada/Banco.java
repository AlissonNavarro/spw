package ConsultaJornada;

import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

public class Banco {

    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public List<SelectItem> consultaDepartamento(Integer permissao) {
        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS ORDER BY SUPDEPTID asc";
            rs = con.executeQuery(sql);
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
            sql = "select DEPTNAME,deptId from DEPARTMENTS ORDER BY DEPTNAME asc";
            rs = con.executeQuery(sql);

            departamentoList.add(new SelectItem(-1, "Selecione um departamento"));

            while (rs.next()) {
                String title = rs.getString("DEPTNAME");
                Integer deptId = rs.getInt("deptId");
                SelectItem selectItem = new SelectItem(deptId, title);
                if (title != null) {
                    if (deptIDList.contains(deptId)) {
                        departamentoList.add(selectItem);
                    }
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

    public String consultaNomeDepartamento(Integer cod) {
        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        String codigo = "";
        try {
            ResultSet rs;
            String sql = "select deptname from DEPARTMENTS "
                    + " where deptid = " + cod;
            rs = con.executeQuery(sql);
            departamentoList.add(new SelectItem(-1, "Selecione um departamento"));
            while (rs.next()) {
                codigo = rs.getString("DEPTNAME");
            }
            rs.close();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            con.Desconectar();
        }
        return codigo;
    }

    public List<JornadaExibicao> consultaJornadas(Integer cod_departamento, Date data) {

        List<JornadaFunc> jornadaList = new ArrayList<JornadaFunc>();
        List<JornadaExibicao> jornadaExibicaoList = new ArrayList<JornadaExibicao>();

        try {
            ResultSet rs;
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

            rs = con.executeQuery(sql);
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
            con.Desconectar();
        }
        return jornadaExibicaoList;
    }

    public boolean insertRelatorio(List<JornadaExibicao> jornadaExibicao) {
        boolean ok = true;
        String query = "insert into RelatorioEscala(nomeJornada,segunda,terca,quarta,quinta,sexta) values(?,?,?,?,?,?)";
        String queryDelete = "delete from RelatorioEscala";
        con.executeUpdate(queryDelete);

        try {
            for (Iterator<JornadaExibicao> it = jornadaExibicao.iterator(); it.hasNext();) {

                JornadaExibicao jornadaExibicao_ = it.next();
                con.prepareStatement(query);
                con.pstmt.setString(1, jornadaExibicao_.getNomeJornada());
                con.pstmt.setString(2, jornadaExibicao_.getSegunda());
                con.pstmt.setString(3, jornadaExibicao_.getTerca());
                con.pstmt.setString(4, jornadaExibicao_.getQuarta());
                con.pstmt.setString(5, jornadaExibicao_.getQuinta());
                con.pstmt.setString(6, jornadaExibicao_.getSexta());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.print(e.getMessage());
            ok = false;
        } finally {
            con.Desconectar();
        }
        return ok;
    }

}
