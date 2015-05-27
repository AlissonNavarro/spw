package Funcionario;

import comunicacao.AcessoBD;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;

public class Banco {

    List<Integer> deptList = new ArrayList<Integer>();
    AcessoBD con;

    public Banco() {
        con = new AcessoBD();
    }

    public int cadastrarNovoFuncionario(Funcionario func) {
        int flag = 0;
        try {
            int nextId = 0;
            ResultSet rs;
            String sqlSelect = "select max(userid) as u from USERINFO ";
            rs = con.executeQuery(sqlSelect);

            while (rs.next()) {
                nextId = rs.getInt("u");
            }
            nextId++;
            String matricula = "" + nextId;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String nasc = func.getDataNascimento() == null ? null : sdf.format(func.getDataNascimento());
            String contr = func.getDataContratação() == null ? null : sdf.format(func.getDataContratação());
            Integer dept = func.getCod_dept() == -1 ? 1 : func.getCod_dept();
            String carg = func.getCargo() == 0 ? null : func.getCargo().toString();

            String sql = "insert into USERINFO(name,ssn,pis,badgenumber,mat_emcs,gender,birthday,hiredday,defaultdeptid,"
                    + "cargo,cod_regime,ativo,holiday,userid,livreAcesso) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            con.prepareStatement(sql);
            con.pstmt.setString(1, func.getNome());
            con.pstmt.setString(2, func.getCpf());
            con.pstmt.setString(3, setSomenteNumerosPis(func.getPIS()));
            Administracao.Banco b = new Administracao.Banco();
            if (b.CheckWithPIN()) {
                con.pstmt.setString(4, func.getMatricula());
            } else {
                con.pstmt.setString(4, addLetraPin(func.getMatricula()));
            }
            con.pstmt.setInt(5, func.getMat_emcs());
            con.pstmt.setInt(6, func.getSexo());
            con.pstmt.setString(7, nasc);
            con.pstmt.setString(8, contr);
            con.pstmt.setInt(9, dept);
            con.pstmt.setString(10, carg);
            con.pstmt.setInt(11, func.getCod_regime());
            con.pstmt.setBoolean(12, func.getIsAtivo());
            con.pstmt.setInt(13, func.getSucetivelAFeriado() ? 1 : 0);
            con.pstmt.setString(14, matricula);
            con.pstmt.setBoolean(15, func.getLivreAcesso());
            if (con.executeUpdate() != 1) {
               flag = 1; 
            }

            /*            try {
             conSQLite = getSQLiteConnection();
             sql = "insert into employee(employeename, pis, employeereg, idcompany) values (?,?,?,?)";
             PreparedStatement pstmt = conSQLite.prepareStatement(sql);
             pstmt.setString(1, func.getNome());
             pstmt.setString(2, setSomenteNumerosPis(func.getPIS()));
             pstmt.setString(3, func.getCpf());
             pstmt.setInt(4, 1);
             pstmt.executeUpdate();

             } catch (Exception e) {
             System.out.println(e.getMessage());
             flag = 2;

             }*/
        } catch (Exception e) {
            System.out.println(e);
            flag = 1;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int cadastrarNovoFuncionarioItdentity(Funcionario func) {
        int flag = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String nasc = func.getDataNascimento() == null ? null : sdf.format(func.getDataNascimento());
            String contr = func.getDataContratação() == null ? null : sdf.format(func.getDataContratação());
            Integer dept = func.getCod_dept() == -1 ? 1 : func.getCod_dept();
            String carg = func.getCargo() == 0 ? null : func.getCargo().toString();

            String sql = "insert into USERINFO(name,ssn,pis,badgenumber,mat_emcs,gender,birthday,hiredday,defaultdeptid,"
                    + "cargo,cod_regime,ativo,holiday,livreAcesso) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            con.prepareStatement(sql);
            con.pstmt.setString(1, func.getNome());
            con.pstmt.setString(2, func.getCpf());
            con.pstmt.setString(3, setSomenteNumerosPis(func.getPIS()));
            Administracao.Banco b = new Administracao.Banco();
            if (b.CheckWithPIN()) {
                con.pstmt.setString(4, func.getMatricula());
            } else {
                con.pstmt.setString(4, addLetraPin(func.getMatricula()));
            }
            con.pstmt.setInt(5, func.getMat_emcs());
            con.pstmt.setInt(6, func.getSexo());
            con.pstmt.setString(7, nasc);
            con.pstmt.setString(8, contr);
            con.pstmt.setInt(9, dept);
            con.pstmt.setString(10, carg);
            con.pstmt.setInt(11, func.getCod_regime());
            con.pstmt.setBoolean(12, func.getIsAtivo());
            con.pstmt.setInt(13, func.getSucetivelAFeriado() ? 1 : 0);
            con.pstmt.setBoolean(14, func.getLivreAcesso());
            con.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 1;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<SelectItem> consultaFuncionarioTotal(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
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
                userList.add(new SelectItem(null, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }

            } else {
                sql = "select u.userid,u.name"
                        + " from  USERINFO u "
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY u.name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(null, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    userList.add(new SelectItem(userid, name));
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public Funcionario consultaDetalhesFuncionario(Integer cod_funcionario) {
        Funcionario func = new Funcionario();
        try {
            ResultSet rs;
            String sql;
            sql = "SELECT * FROM  USERINFO  WHERE (USERID = " + cod_funcionario + ")";
            rs = con.executeQuery(sql);

            while (rs.next()) {

                Integer funcionarioId = cod_funcionario;
                String matricula = removeLetraPin(rs.getString("BADGENUMBER"));
                String cpf = rs.getString("SSN");
                String PIS = rs.getString("PIS");
                String nome = rs.getString("NAME");
                Integer sexo = genderStringToInt(rs.getString("GENDER"));
                Integer cargo = rs.getInt("Cargo");
                Integer dept = rs.getInt("DEFAULTDEPTID");
                String cracha = rs.getString("CardNo");
                Date dataNascimento = rs.getDate("BIRTHDAY");
                Date dataContratação = rs.getDate("HIREDDAY");
                Boolean ativo = rs.getBoolean("ativo");
                Integer cod_regime = rs.getInt("cod_regime");
                Integer mat_emcs = rs.getInt("mat_emcs");
                Integer feriado = rs.getInt("holiday");
                Boolean sucetivelAFeriado = (feriado == 1);
                String ADUsername = rs.getString("adusername");
                Boolean livreAcesso = rs.getBoolean("livreAcesso");
                func = new Funcionario(funcionarioId, matricula, cpf, PIS, nome, sexo, cargo,
                        cracha, dataNascimento, dataContratação, sucetivelAFeriado, dept,
                        cod_regime, mat_emcs, ADUsername, livreAcesso);
                func.setIsAtivo(ativo);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return func;
    }

    public boolean salvarAlteracoes(Funcionario funcionario) {
        boolean flag = true;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String nasc = funcionario.getDataNascimento() == null ? null : sdf.format(funcionario.getDataNascimento());
        String contr = funcionario.getDataContratação() == null ? null : sdf.format(funcionario.getDataContratação());

        String carg = funcionario.getCargo() == 0 ? null : funcionario.getCargo().toString();

        String query = "update USERINFO set BADGENUMBER=?,"
                + " SSN=?, NAME=?, GENDER=?, Cargo=?, CardNo=?,"
                + " BIRTHDAY=?, HIREDDAY=?, PIS=?, HOLIDAY=?,"
                + " ATIVO=?, cod_regime=?,Mat_emcs=?, livreAcesso=?"
                + " where userid=? ";

        try {
            con.prepareStatement(query);
            Administracao.Banco b = new Administracao.Banco();
            if (b.CheckWithPIN()) {
                con.pstmt.setString(1, funcionario.getMatricula());
            } else {
                con.pstmt.setString(1, addLetraPin(funcionario.getMatricula()));
            }
            con.pstmt.setString(2, funcionario.getCpf());
            con.pstmt.setString(3, funcionario.getNome());
            con.pstmt.setInt(4, funcionario.getSexo());
            con.pstmt.setString(5, carg);
            con.pstmt.setString(6, funcionario.getCracha());
            con.pstmt.setString(7, nasc);
            con.pstmt.setString(8, contr);
            con.pstmt.setString(9, setSomenteNumerosPis(funcionario.getPIS()));
            Integer feriado = funcionario.getSucetivelAFeriado() ? 1 : 0;
            con.pstmt.setInt(10, feriado);
            con.pstmt.setBoolean(11, funcionario.getIsAtivo());
            con.pstmt.setInt(12, funcionario.getCod_regime());
            con.pstmt.setInt(13, funcionario.getMat_emcs());
            con.pstmt.setBoolean(14, funcionario.getLivreAcesso());
            con.pstmt.setInt(15, funcionario.getFuncionarioId());

            con.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            flag = false;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int salvarTransferencia(Integer departamentoDestino, Integer cod_funcionario) {
        int flag = 0;
        String query = "update USERINFO set DEFAULTDEPTID=? "
                + " where userid=? ";
        flag = departamentoDestino == 0 ? 0 : 1;
        try {
            if (flag == 1) {
                con.prepareStatement(query);
                con.pstmt.setInt(1, departamentoDestino);
                con.pstmt.setInt(2, cod_funcionario);
                con.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return flag;
    }
    
    public void zerarSenhaUsuario(int idUsuario) {
        try {
            String query = "UPDATE USERINFO SET senha = NULL WHERE userid = ?";
            con.prepareStatement(query);
            con.pstmt.setInt(1, idUsuario);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }    

    //retorna uma sequencia para o pin, buscando o maior pin do banco e somando mais 1.
    public String getNextPIN() {
        String str = null;
        try {
            ResultSet rs = con.executeQuery("select Max(Cast((replace(badgenumber,'A',''))as int)) 'Maior' from userinfo");
            if (rs.next()) {
                int y = rs.getInt("Maior");
                y++;
                str = "" + y;
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            con.Desconectar();
        }
        return str;
    }

    //adiciona uma letra para o pin.
    public String addLetraPin(String pin) {
        return pin + "A";
    }

    /**
     * remove qualquer letra do pin
     *
     */
    public String removeLetraPin(String pin) {
        //remove todas as letras
        return pin.replaceAll("\\D", "");
    }

    public List<SelectItem> consultaDepartamentoHierarquico(Integer codigo_funcionario, Integer departamento, Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        Boolean isAdministradorVisivel = true;

        try {
            //Selecionando os departamentos com permissão de visibilidade.
            String sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";

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
                    isAdministradorVisivel = new Boolean(false);
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
        }
        return saida;
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

    public Boolean isAdministradorVisivel(Integer codigo_funcionario, Integer departamento, Integer permissao) {

        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();
        Boolean isAdministradorVisivel = new Boolean(true);

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
                    isAdministradorVisivel = new Boolean(false);
                }
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return isAdministradorVisivel;
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
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public List<SelectItem> consultaCargosList() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            ResultSet rs;
            String sql;
            sql = "SELECT * FROM Cargo";

            rs = con.executeQuery(sql);
            saida.add(new SelectItem(null, "Não Especificado"));
            while (rs.next()) {
                Integer cod_cargo = rs.getInt("cod_cargo");
                String nome = rs.getString("nome");
                saida.add(new SelectItem(cod_cargo, nome));
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    private String genderIntToString(Integer gender) {
        String result = null;
        if (gender != null && gender != 0) {
            if (gender == 2) {
                result = "Feminino";
            } else {
                result = "Masculin";
            }
        }
        return result;
    }

    private Integer genderStringToInt(String gender) {
        Integer result = null;
        if (gender != null) {
            if (gender.equals("Feminino")) {
                result = 2;
            } else {
                result = 1;
            }
        }
        return result;
    }

    /*public Boolean updateRegime(Integer userid, Integer cod_regime) {
    
     
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
     
     c.close();
     }
     } catch (Exception e) {
     System.out.println("Erro:" + e);
     }
     }
     return flag;
     }*/
    public List<SelectItem> consultaRegimeList() {

        List<SelectItem> regimeSelectItemList = new ArrayList<SelectItem>();
        try {
            ResultSet rs;

            String sql;
            sql = "select * from Regime_HoraExtra ORDER BY nome";
            rs = con.executeQuery(sql);
            regimeSelectItemList.add(new SelectItem(0, "Escolha o regime"));
            while (rs.next()) {
                Integer cod_regime = rs.getInt("cod_regime");
                String nome = rs.getString("nome");
                SelectItem regimeSelectItem = new SelectItem(cod_regime, nome);
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
    /* public String consultaRegime(Integer cod_regime) {
     String nome = null;
     try {
     ResultSet rs;
     
    
     String sql;
    
    
     sql = "SELECT nome FROM Regime_HoraExtra WHERE (cod_regime = " + cod_regime + ")";
     
     rs = stmt.executeQuery(sql);
     while (rs.next()) {
     nome = rs.getString("nome");
     }
     rs.close();
     
     } catch (Exception e) {
     System.out.println(e.getMessage());
     }
     return nome;
     } */

    private static String setSomenteNumerosPis(String pis) {
        return pis.replace(".", "").replace("/", "");
    }

    public void removoLinkAD(String aduser) {

        try {
            String query = "UPDATE USERINFO SET adusername = NULL WHERE adusername = ?";
            con.prepareStatement(query);
            con.pstmt.setString(1, aduser);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public List<SelectItem> consultaFuncionario(Integer dep, Boolean incluirSubSetores) {

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
                        + " from USERINFO u"
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
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

    public void excluirFuncionario(Integer userid) {

        try {
            String query = "delete from USERINFO where userid = ?";
            con.prepareStatement(query);
            con.pstmt.setInt(1, userid);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public void prepararCapturaDigital(Integer userid) {
        finalizarCapturaDigital();
        try {
            String query = "insert into calldigit(func_id) values(?)";
            con.prepareStatement(query);
            con.pstmt.setInt(1, userid);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    public void finalizarCapturaDigital() {
        try {
            String query = "delete from calldigit";
            con.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
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
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return userList;
    }

    public List<SelectItem> getRegimeSelectItem() {

        List<SelectItem> regimeList = new ArrayList<SelectItem>();

        ResultSet rs;
        regimeList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from regime_HoraExtra";

            con.prepareStatement(query);

            rs = con.executeQuery();

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

    public List<SelectItem> getCargoSelectItem() {
        List<SelectItem> cargoList = new ArrayList<SelectItem>();

        ResultSet rs;
        cargoList.add(new SelectItem(-1, "TODOS"));
        try {
            String query = "select * from cargo";
            con.prepareStatement(query);
            rs = con.executeQuery();
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

    public HashMap<Integer, Integer> getcod_funcionarioRegime() {

        HashMap<Integer, Integer> cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();

        ResultSet rs;

        try {
            String query = "select userid,cod_regime from userinfo";
            con.prepareStatement(query);
            rs = con.executeQuery();

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                Integer cod_regime = rs.getInt("cod_regime");
                cod_funcionarioRegimeHashMap.put(userid, cod_regime);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioRegimeHashMap;

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
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return cod_funcionarioCargoHashMap;

    }

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
            con.Desconectar();
        }
        return saida;
    }

    public List<SelectItem> consultaFuncionario(Integer dep, String permissao, Boolean incluirSubSetores) {

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
                        + " where u.permissao != " + permissao
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");

                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct u.userid,u.name from"
                        + " USERINFO u"
                        + " where u.defaultdeptid = " + dep
                        + " and u.permissao != " + permissao
                        + " ORDER BY name asc";

                rs = con.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
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

    public String consultaCargo(Integer cargo) {
        String nome = null;
        try {
            ResultSet rs;

            String sql;

            sql = "SELECT     cod_cargo, nome FROM  Cargo WHERE     (cod_cargo = " + cargo + ")";

            rs = con.executeQuery(sql);
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return nome;
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
        } finally {
            con.Desconectar();
        }
        return idToSuperHash;
    }
}
