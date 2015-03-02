package ListaPresenca;

import ConsultaPonto.Jornada;
import Usuario.UsuarioBean;
import comunicacao.AcessoBD;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;

public class Banco {
    
    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public List<SelectItem> consultaDepartamento(Integer permissao, UsuarioBean usuarioBean) {

        List<SelectItem> departamentoList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;

        try {
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";           
            ResultSet rs = con.executeQuery(sql);

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

            sql = "select DEPTNAME,deptId from DEPARTMENTS ORDER BY DEPTNAME asc";
            
            rs = con.executeQuery(sql);
            if (usuarioBean.getUsuario().getIsAcessoTotal()) {
                departamentoList.add(new SelectItem(-1, "Todos os departamentos"));
            }


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
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return departamentoList;
    }

    public List<ListaPresenca> consultaDadosPonto(Integer cod_dept, Date data, Date hora) {
        List<ListaPresenca> listaPresencaList = new ArrayList<ListaPresenca>();

        try {
            ResultSet rs;
            String sql;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataLimite = sdf.format(hora);

            if (cod_dept == -1) {
                sql = "select u.userid,max(checktime)as checktime,max(u.name) as name,max(u.ssn) as cpf from"
                        + " checkinout c,departments d,userinfo u"
                        + " where d.deptid = u.defaultdeptid and"
                        + " c.userid = u.userid and "
                        + "c.checktime between '01/01/1980' and '" + dataLimite + "'"
                        + " group by u.userid " + " order by u.name ";
            } else {
                sql = "select u.userid,max(checktime)as checktime,max(u.name) as name,max(u.ssn) as cpf from"
                        + " checkinout c,departments d,userinfo u" + " where d.deptid = " + cod_dept + " and"
                        + " d.deptid = u.defaultdeptid and" + " c.userid = u.userid and c.checktime between '01/01/2005' and '"
                        + dataLimite + "'" + " group by u.userid " + " order by u.name ";
            }

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Timestamp checktime = rs.getTimestamp("checktime");
                String nome = rs.getString("name");
                String cpf = rs.getString("cpf");
                if (consultaNumJornadaCerta(userid, data) != -1) {
                    listaPresencaList.add(new ListaPresenca(userid, cpf, nome, new Date(checktime.getTime())));
                }
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return listaPresencaList;
    }

    public List<ListaPresenca> consultaDadosPontoTotal(Integer cod_dept,
            List<Integer> funcionarioList, Date data, Date hora, String filtro, Boolean incluirSubSetores) {

        List<ListaPresenca> listaPresencaList = new ArrayList<ListaPresenca>();


        try {
            ResultSet rs;
            String sql;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataLimite = sdf.format(hora);

            if (incluirSubSetores) {

                sql = "select u.userid,max(checktime)as checktime,max(u.name) as name," +
                        "max(u.ssn) as cpf,max(u.defaultdeptid) as deptid from"
                        + " checkinout c,departments d,userinfo u"
                        + " where u.name like '" + filtro + "%' and"
                        + " c.userid = u.userid and c.checktime between '01/10/2009' and '"
                        + dataLimite + "'" + " group by u.userid " + " order by name ";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    Timestamp checktime = rs.getTimestamp("checktime");
                    String nome = rs.getString("name");
                    String cpf = rs.getString("cpf");

                    if (funcionarioList.contains(userid)) {
                        listaPresencaList.add(new ListaPresenca(userid, cpf, nome, new Date(checktime.getTime())));
                    }
                }

            } else {

                sql = "select u.userid,max(checktime)as checktime,max(u.name) as name,max(u.ssn) as cpf from"
                        + " checkinout c,departments d,userinfo u"
                        + " where d.deptid = " + cod_dept + " and"
                        + " u.name like '" + filtro + "%' and"
                        + " d.deptid = u.defaultdeptid and"
                        + " c.userid = u.userid and c.checktime between '01/10/2009' and '"
                        + dataLimite + "'" + " group by u.userid " + " order by name ";

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    Timestamp checktime = rs.getTimestamp("checktime");
                    String nome = rs.getString("name");
                    String cpf = rs.getString("cpf");
                    if (funcionarioList.contains(userid)) {
                        listaPresencaList.add(new ListaPresenca(userid, cpf, nome, new Date(checktime.getTime())));
                    }
                }
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return listaPresencaList;
    }

    public List<Jornada> consultaRegraJornada(Integer userid, Integer eday, Date dataSelecionada) {

        Integer num_jornada = consultaNumJornadaCerta(userid, dataSelecionada);
        List<Jornada> jornadaList = new ArrayList<Jornada>();
        try {

            ResultSet rs;
            String sql;

            sql = "select s.*,nrd.sdays,nrd.edays,nr.startdate,nr.cyle,nr.units" + " from "
                    + "num_run_deil nrd,schclass s,num_run nr"
                    + " where nrd.num_runid = " + num_jornada + " and"
                    + " nrd.schclassid = s.schclassid and "
                    + " nr.num_runid = " + num_jornada;

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String schname = rs.getString("SCHNAME");
                Integer schClassID = rs.getInt("schClassID");
                String legend = rs.getString("legend");
                Timestamp inicioJornada = rs.getTimestamp("STARTTIME");
                Timestamp terminioJornada = rs.getTimestamp("ENDTIME");
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
                float tipoJornada = rs.getFloat("WorkDay");
                Integer cyle = rs.getInt("cyle");
                Integer units = rs.getInt("units");
                Date startdate = rs.getDate("startdate");
                Timestamp inicioDescanso1 = null;
                if(rs.getTimestamp("RESTINTIME1") != null){
                    inicioDescanso1 = rs.getTimestamp("RESTINTIME1");
                }
                Timestamp fimDescanso1 = null;
                if(rs.getTimestamp("RESTOUTTIME1") != null){
                    fimDescanso1 = rs.getTimestamp("RESTOUTTIME1");
                }
                Timestamp inicioIntrajornada = null;
                if(rs.getTimestamp("InterRestIn") != null){
                    inicioIntrajornada = rs.getTimestamp("InterRestIn");
                }
                Timestamp fimIntrajornada = null;
                if(rs.getTimestamp("InterRestOut") != null){
                    fimIntrajornada = rs.getTimestamp("InterRestOut");
                }
                Timestamp inicioDescanso2 = null;
                if(rs.getTimestamp("RESTINTIME2") != null){
                    inicioDescanso2 = rs.getTimestamp("RESTINTIME2");
                }
                Timestamp fimDescanso2 = null;
                if(rs.getTimestamp("RESTOUTTIME2") != null){
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
                        checkOutLimiteAtrasada, tipoJornada, sdays, edays, cyle, units, startdate, inicioJornada, terminioJornada, legend,
                        inicioDescanso1, fimDescanso1, inicioDescanso2, fimDescanso2, tolerancia, deduzirDescanco1, deduzirDescanco2, 
                        inicioIntrajornada, fimIntrajornada, deduzirIntrajornada, combinarEntrada, combinarSaida, false);
                jornadaList.add(jornada);
            }

            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return jornadaList;
    }

    public Integer consultaNumJornadaCerta(Integer userid, Date dataSelecionada) {

        Integer jornadaNumber = -1;
        try {
            // connection to an ACCESS MDB
            ResultSet rs;
            String sql;

            sql = "select num_of_run_id,startdate,enddate" + " from user_of_run ur" + "" + " where ur.userid = " + userid;
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer num_jornada = rs.getInt("num_of_run_id");
                Date inicioTimes = rs.getDate("startdate");
                Date fimTimes = rs.getDate("enddate");

                if ((inicioTimes.getTime() <= dataSelecionada.getTime()) && (fimTimes.getTime() >= dataSelecionada.getTime())) {
                    jornadaNumber = num_jornada;
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return jornadaNumber;
    }

    public String consultaDepartamentoo(Integer cod) {

        String departamento = "";
        try {
            ResultSet rs;
            String sql;
            sql = "select deptname from departments" + " where deptid = " + cod;
            rs = con.executeQuery(sql);

            while (rs.next()) {
                departamento = rs.getString("deptname");
            }

            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return departamento;
    }

    public void insertTeste(String srt) {
        try {
            String query = "insert into test(teste) values(?)";
            con.prepareStatement(query);
            con.pstmt.setString(1, srt);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cargoList;
    }
    
    public HashMap<Integer, Integer> getcod_funcionarioCargo() {
        HashMap<Integer, Integer> cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        
        ResultSet rs;
        //System.out.println("pesquisando getcod_funcionarioCargo");
        try {
            String query = "select userid,CARGO from userinfo";
            rs = con.executeQuery(query);
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cargo = rs.getInt("CARGO");
                cod_funcionarioCargoHashMap.put(userid, cargo);
                //System.out.println("funcCargo: " + userid + ", " + cargo);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioCargoHashMap;
    }

    public List<SelectItem> consultaFuncionario2(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
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

                sql = "select distinct u.userid,u.name,u.defaultdeptid"
                        + " from USERINFO u "
                        + " where u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep+ " and"
                        + " u.ativo = 'true'"
                        + " ORDER BY name asc";

                
                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    Integer userid = rs.getInt("userid");
                    String name = rs.getString("name");
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

    public void consultaTeste() {

        String departamento = "";
        try {
            ResultSet rs;            
            String sql = "select * from test";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                departamento = rs.getString("teste");
                System.out.println(departamento);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
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

    public void insertRelatorio(List<Relatorio> relatorio) { 
        String query = "insert into relatorioPresenca(cpf,nome,ultimoRegistro,situacao) values(?,?,?,?)";
        String queryDelete = "delete from relatorioPresenca";
        con.executeUpdate(queryDelete);
        con.prepareStatement(query);

        try {
            for (Iterator<Relatorio> it = relatorio.iterator(); it.hasNext();) {

                Relatorio relatorio1 = it.next();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String data = sdf.format(relatorio1.getUltimoRegistro());

                con.pstmt.setString(1, relatorio1.getCpf());
                con.pstmt.setString(2, relatorio1.getNome());
                con.pstmt.setString(3, data);
                con.pstmt.setString(4, relatorio1.getSituacao());
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
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

}
