/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import ConsultaPonto.Jornada;
import Metodos.Metodos;
import Usuario.Usuario;
import com.sun.crypto.provider.RSACipher;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.model.SelectItem;

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
            System.out.println("Abono: Conectar: "+cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Abono: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alterarStatus(int cod_solicitacao) {
        try {

            Statement stmt = null;
            String sql = "UPDATE SolicitacaoAbono SET Status='Pendente',id_responsavel=NULL WHERE codigo = "+cod_solicitacao;
            stmt = c.createStatement();
            stmt.executeQuery(sql);
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
    }

    public List<SolicitacaoAbono> consultaSolicitacoesAbono(Usuario usuario, Integer departamentoSelecionado,
            java.util.Date dataInicio, java.util.Date dataFim, Boolean incluirSubSetores) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String inicioString = sdf.format(dataInicio);
        String fimString = sdf.format(dataFim.getTime() + 86399999);
        List<SolicitacaoAbono> solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;

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
                deptIDList.add(departamentoSelecionado);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(departamentoSelecionado, deptPermitidos, idToSuperHash);

                sql = "select sa.Codigo,d.deptID,d.DEPTNAME,u.USERID,u.name,sa.data,sa.entrada1,"
                        + "sa.saida1,sa.entrada2,sa.saida2,sa.descricao,sa.status,sa.inclusao,sa.resposta"
                        + " from SolicitacaoAbono sa,USERINFO u,DEPARTMENTS d"
                        + " where d.deptid = u.DEFAULTDEPTID" + " and"
                        + " sa.userid = u.userid" + " and"
                        + " sa.Status = 'Pendente' and"
                        + " u.permissao != " + usuario.getPermissao() + " and "
                        + " sa.data between '" + inicioString + "' and '" + fimString
                        + "' order by inclusao";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Integer codigo = rs.getInt("Codigo");
                    String departamento = rs.getString("DEPTNAME");
                    Integer deptID = rs.getInt("deptID");
                    Integer codigoFuncionario = rs.getInt("USERID");
                    String name = rs.getString("name");
                    Date data = rs.getDate("data");
                    String entrada1 = rs.getString("entrada1");
                    String saida1 = rs.getString("saida1");
                    String entrada2 = rs.getString("entrada2");
                    String saida2 = rs.getString("saida2");
                    String descricao = rs.getString("descricao");
                    String status = rs.getString("status");
                    Date inclusao = rs.getDate("Inclusao");
                    String resposta = rs.getString("Resposta");
                    if (deptIDList.contains(deptID)) {
                        solicitacaoAbonoList.add(new SolicitacaoAbono(codigo, departamento, codigoFuncionario, name, data, entrada1, saida1, entrada2, saida2, descricao, status, inclusao, resposta));
                    }
                }
            } else {
                sql = "select sa.Codigo,d.deptID,d.DEPTNAME,u.USERID,u.name,sa.data,sa.entrada1,sa.saida1,sa.entrada2,sa.saida2,sa.descricao,sa.status,sa.inclusao,sa.resposta"
                        + " from SolicitacaoAbono sa,USERINFO u,DEPARTMENTS d"
                        + " where d.deptid = u.DEFAULTDEPTID and "
                        + " d.deptid = " + departamentoSelecionado + " and "
                        + " sa.userid = u.userid" + " and"
                        + " sa.Status = 'Pendente' and"
                        + " u.permissao != " + usuario.getPermissao() + " and "
                        + " sa.data between '" + inicioString + "' and '" + fimString
                        + "' order by inclusao";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Integer codigo = rs.getInt("Codigo");
                    String departamento = rs.getString("DEPTNAME");
                    Integer codigoFuncionario = rs.getInt("USERID");
                    String name = rs.getString("name");
                    Date data = rs.getDate("data");
                    String entrada1 = rs.getString("entrada1");
                    String saida1 = rs.getString("saida1");
                    String entrada2 = rs.getString("entrada2");
                    String saida2 = rs.getString("saida2");
                    String descricao = rs.getString("descricao");
                    String status = rs.getString("status");
                    Date inclusao = rs.getDate("Inclusao");
                    String resposta = rs.getString("Resposta");

                    solicitacaoAbonoList.add(new SolicitacaoAbono(codigo, departamento, codigoFuncionario, name, data, entrada1, saida1, entrada2, saida2, descricao, status, inclusao, resposta));

                }
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("consultaSolicitacoesAbono: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return solicitacaoAbonoList;
    }

    public List<SolicitacaoAbono> consultaSolicitacoesAbono(Usuario usuario) {

        List<SolicitacaoAbono> solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        try {

            ResultSet rs = null;
            Statement stmt = null;
            String sql;

            //Selecionando os departamentos com permissão de visibilidade.
            sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                    + " ORDER BY SUPDEPTID asc";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            List<Integer> deptIDList = new ArrayList<Integer>();
            deptIDList.add(Integer.parseInt(usuario.getPermissao()));

            while (rs.next()) {
                Integer cod = rs.getInt("DEPTID");
                Integer supdeptid = rs.getInt("SUPDEPTID");
                idToSuperHash.put(cod, supdeptid);
            }
            rs.close();
            stmt.close();

            List<Integer> deptPermitidos = new ArrayList<Integer>();
            deptIDList = getDeptPermitidos(Integer.parseInt(usuario.getPermissao()), deptPermitidos, idToSuperHash);

            sql = "select sa.Codigo,d.deptID,d.DEPTNAME,u.USERID,u.name,sa.data,sa.entrada1,sa.saida1,sa.entrada2,sa.saida2,sa.descricao,sa.status,sa.inclusao,sa.resposta"
                    + " from SolicitacaoAbono sa,USERINFO u,DEPARTMENTS d"
                    + " where d.deptid = u.DEFAULTDEPTID" + " and"
                    + " sa.userid = u.userid" + " and"
                    + " u.permissao != " + usuario.getPermissao() +" and"
                    + " sa.Status = 'Pendente'"
                    + " order by inclusao";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer codigo = rs.getInt("Codigo");
                String departamento = rs.getString("DEPTNAME");
                Integer deptID = rs.getInt("deptID");
                Integer codigoFuncionario = rs.getInt("USERID");
                String name = rs.getString("name");
                Date data = rs.getDate("data");
                String entrada1 = rs.getString("entrada1");
                String saida1 = rs.getString("saida1");
                String entrada2 = rs.getString("entrada2");
                String saida2 = rs.getString("saida2");
                String descricao = rs.getString("descricao");
                String status = rs.getString("status");
                Date inclusao = rs.getDate("Inclusao");
                String resposta = rs.getString("Resposta");
                if (deptIDList.contains(deptID)) {
                    solicitacaoAbonoList.add(new SolicitacaoAbono(codigo, departamento, codigoFuncionario, name, data, entrada1, saida1, entrada2, saida2, descricao, status, inclusao, resposta));
                }
            }

        } catch (Exception e) {
            System.out.println("consultaSolicitacoesAbono: " + e.getMessage());
        } finally {
            try {
                if (c != null) {
                    c.close();
                }

            } catch (Exception e) {
            }
        }
        return solicitacaoAbonoList;
    }

    public Boolean consultaHorarioIrregularSolicitado(Integer userid, Long dia) {
        Boolean ok = false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String registroString = sdf.format(dia);
            ResultSet rs;
            Statement stmt;
            String sql;
            sql = " select *"
                    + " from  solicitacaoabono"
                    + " where userid = " + userid + " and"
                    + " convert(char(10),data,103) = convert(char(10),'" + registroString + "',103) and"
                    + " status = 'Pendente'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);


            while (rs.next()) {
                ok = true;
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ok;
    }
    
    public String getDescricaoSolicitacao(Integer userid, Long dia){
        String descricao = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String registroString = sdf.format(dia);
            ResultSet rs;
            Statement stmt;
            String sql;
            sql = " select Descricao"
                    + " from  solicitacaoabono"
                    + " where userid = " + userid + " and"
                    + " convert(char(10),data,103) = convert(char(10),'" + registroString + "',103) and"
                    + " status = 'Pendente'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);


            while (rs.next()) {
                descricao = rs.getString("Descricao");
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return descricao;
    }

    public String consultaDepartamentoNomeByUserid(Integer userid) {

        String nomeDept = "";

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select DEPTNAME"
                    + " from  DEPARTMENTS d, USERINFO u"
                    + " where u.defaultdeptid = d.deptid and "
                    + " userid = " + userid;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                nomeDept = rs.getString("DEPTNAME");
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nomeDept;
    }

    public List<SelectItem> consultaJustificativasAbono() {

        List<SelectItem> justificativaList = new ArrayList<SelectItem>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select l.leaveid,l.leavename from LEAVECLASS l"
                    + " where administrador = 0";

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

    public List<SelectItem> consultaJustificativasAbonoDescricaoNaoObrigatoria() {

        List<SelectItem> justificativaList = new ArrayList<SelectItem>();
        try {

            ResultSet rs = null;
            Statement stmt = null;

            String query = "select l.leaveid,l.leavename from LEAVECLASS l"
                    + " where administrador = 0 and descricaoObrigatoria = 0";

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

    /*public boolean abonar(Integer userid, Date inicio, Date fim,
            Integer dateid, String justificativa,
            Integer responsavel_id,
            Integer cod_solicitacao) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String inicioString = sdf.format(inicio);
            String fimString = sdf.format(fim);
            String dataAtual = sdf.format(new java.util.Date());

            String query = "INSERT INTO USER_SPEDAY values(?,?,?,?,?,?,?,?)";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, userid);
            pstmt.setString(2, inicioString);
            pstmt.setString(3, fimString);
            pstmt.setInt(4, dateid);
            pstmt.setString(5, justificativa);
            pstmt.setString(6, dataAtual);
            pstmt.setInt(7, responsavel_id);
            pstmt.setInt(8, cod_solicitacao);

            pstmt.executeUpdate();

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
    }*/

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

    public boolean abonar(Integer userid, java.util.Date registro,
            Integer dateid, String justificativa,
            Integer responsavel_id,
            Integer cod_solicitacao) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String registroString = sdf.format(registro);
            String dataAtual = sdf.format(new java.util.Date());

            String query = "INSERT INTO REGISTRO_ABONO values(?,?,?,?,?,?,?,?)";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, userid);
            pstmt.setString(2, registroString);
            pstmt.setInt(3, dateid);
            pstmt.setString(4, justificativa);
            pstmt.setString(5, dataAtual);
            pstmt.setInt(6, responsavel_id);
            pstmt.setInt(7, cod_solicitacao);
            pstmt.setNull(8, java.sql.Types.INTEGER);

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            ok = false;
        }
        if (ok) {
            updateSolicitacaoAbono(cod_solicitacao, ok, justificativa, responsavel_id);
        }
        return ok;
    }

    public boolean getDiasEmAbertoNegados(Integer userid, java.util.Date date) {
        boolean ok = false;
        try {
            ResultSet rs;
            Statement stmt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String data = sdf.format(date);
            String query = "SELECT * FROM SOLICITACAOABONO WHERE userid = " + userid + " AND convert(char(10),data,103) = convert(char(10),'" + data + "',103) AND Status = 'Negado'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                ok = true;
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ok;
    }

    public boolean getDiasEmAbertoAceitos(Integer userid, java.util.Date date) {
        boolean ok = false;
        try {
            ResultSet rs;
            Statement stmt;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String data = sdf.format(date);
            String query = "SELECT * FROM SOLICITACAOABONO WHERE userid = " + userid + " AND convert(char(10),data,103) = convert(char(10),'" + data + "',103) AND Status = 'Aceito'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                ok = true;
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ok;
    }

    public void updateSolicitacaoAbono(Integer cod_solicitacao, Boolean aceito, String resposta, Integer cod_responsavel) {

        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE SOLICITACAOABONO SET Status = ?, Resposta = ?, id_responsavel = ? WHERE Codigo = ?";

            pstmt = c.prepareStatement(query);
            if (aceito) {
                pstmt.setString(1, "Aceito");
            } else {
                pstmt.setString(1, "Negado");
            }

            pstmt.setString(2, resposta);

            pstmt.setInt(3, cod_responsavel);

            pstmt.setInt(4, cod_solicitacao);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateAceitarAbono(Integer userid, Integer responsavel, java.util.Date date, String justificativa, Integer dateid) {

        PreparedStatement pstmt = null;
        PreparedStatement pstmtIns = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String data = sdf.format(date);
            String dataAtual = sdf.format(new java.util.Date());
            String query = "UPDATE SOLICITACAOABONO SET Status = ?,  id_responsavel = ? WHERE userid = ? and convert(char(10),data,103) = convert(char(10),'" + data + "',103)";

            pstmt = c.prepareStatement(query);

            pstmt.setString(1, "Aceito");
            pstmt.setInt(2, responsavel);
            pstmt.setInt(3, userid);

            pstmt.executeUpdate();

            String queryIns = "INSERT INTO REGISTRO_ABONO (userid, checktime,dateid, yuanying,date,responsavel) values(?,?,?,?,?,?)";

            pstmtIns = c.prepareStatement(queryIns);
            pstmtIns.setInt(1, userid);
            pstmtIns.setString(2, data);
            pstmtIns.setInt(3, 0);
            pstmtIns.setString(4, null);
            pstmtIns.setString(5, dataAtual);
            pstmtIns.setInt(6, responsavel);

            pstmtIns.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateNegarAbono(Integer userid, Integer responsavel, java.util.Date date) {

        PreparedStatement pstmt = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String data = sdf.format(date);
            String query = "UPDATE SOLICITACAOABONO SET Status = ?,  id_responsavel = ? WHERE userid = ? and convert(char(10),data,103) = convert(char(10),'" + data + "',103)";

            pstmt = c.prepareStatement(query);

            pstmt.setString(1, "Negado");
            pstmt.setInt(2, responsavel);
            pstmt.setInt(3, userid);

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean abonarDiaEmAberto(Integer userid, java.util.Date registro,
            Integer dateid, String justificativa,
            Integer responsavel_id) {
        boolean ok = true;
        PreparedStatement pstmt = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String registroString = sdf.format(registro);
            String dataAtual = sdf.format(new java.util.Date());

            String query = "INSERT INTO REGISTRO_ABONO (userid, checktime,dateid, yuanying,date,responsavel) values(?,?,?,?,?,?)";

            pstmt = c.prepareStatement(query);
            pstmt.setInt(1, userid);
            pstmt.setString(2, registroString);
            pstmt.setInt(3, dateid);
            pstmt.setString(4, justificativa);
            pstmt.setString(5, dataAtual);
            pstmt.setInt(6, responsavel_id);

            pstmt.executeUpdate();

        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }

    public boolean abonarDiaEmAbertoEmMassa(List<DiaEmAberto> diasEmAbertoList, Integer responsavel_id) {
        boolean ok = true;

        PreparedStatement pstmt = null;
        PreparedStatement pstmtUpd = null;
        try {
            for (Iterator<DiaEmAberto> it = diasEmAbertoList.iterator(); it.hasNext();) {
                DiaEmAberto diaEmAberto = it.next();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String registroString = sdf.format(diaEmAberto.getData().getTime());
                String dataAtual = sdf.format(new java.util.Date());
                String query = "INSERT INTO REGISTRO_ABONO (userid, checktime,dateid, yuanying,date,responsavel) values(?,?,?,?,?,?)";

                String aux = "Abonar: ";
                pstmt = c.prepareStatement(query);
                pstmt.setInt(1, diaEmAberto.getCod_funcionario());
                aux = aux + diaEmAberto.getCod_funcionario();
                pstmt.setString(2, registroString);
                aux = aux + " " + registroString;
                pstmt.setString(3, diaEmAberto.getJustificativa());
                aux = aux + " " + diaEmAberto.getJustificativa();
                pstmt.setString(4, null);
                aux = aux + " null";
                pstmt.setString(5, dataAtual);
                aux = aux + " " + dataAtual;
                pstmt.setInt(6, responsavel_id);
                aux = aux + " " + responsavel_id;

                System.out.println();
                System.out.println(aux);
                pstmt.executeUpdate();

                String queryUpd = "UPDATE SOLICITACAOABONO SET status='Aceito' where userid = " + diaEmAberto.getCod_funcionario() + " and convert(char(10),data,103) = convert(char(10),'" + registroString + "',103)";
                pstmtUpd = c.prepareStatement(queryUpd);
                pstmtUpd.executeUpdate();
            }
        } catch (Exception e) {
            ok = false;
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    pstmtUpd.close();
                    c.close();
                }
            } catch (Exception e) {
                ok = false;
            }
        }
        return ok;
    }

    public List<Jornada> consultaRegraJornada(Integer userid, Integer num_jornada) {

        List<Jornada> jornadaList = new ArrayList<Jornada>();
        try {
            // connection to an ACCESS MDB
            ResultSet rs = null;
            Statement stmt = null;
            String sql = null;

            sql = "select s.*,nrd.sdays,nrd.edays,nr.startdate, nr.cyle,nr.units" + " from "
                    + "num_run_deil nrd,schclass s,num_run nr"
                    + " where nrd.num_runid = " + num_jornada + " and"
                    + " nrd.schclassid = s.schclassid and "
                    + " nr.num_runid = " + num_jornada;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

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
                Integer cyle = rs.getInt("cyle");
                Integer units = rs.getInt("units");
                Date startdate = rs.getDate("startdate");
                float tipoJornada = rs.getFloat("WorkDay");
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
        return jornadaList;
    }

    public Integer consultaNumJornadaCerta(Integer userid, String dataSelecionada) {

        Integer jornadaNumber = -1;
        try {
            // connection to an ACCESS MDB
            ResultSet rs;
            Statement stmt;
            String sql;

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = null;
            try {
                dt = df.parse(dataSelecionada);
            } catch (ParseException ex) {
                System.out.println(ex);
            }

            sql = "select num_of_run_id,startdate,enddate" + " from user_of_run ur" + " where ur.userid = " + userid;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer num_jornada = rs.getInt("num_of_run_id");
                Date inicioTimes = rs.getDate("startdate");
                Date fimTimes = rs.getDate("enddate");

                if ((inicioTimes.getTime() <= dt.getTime()) && (fimTimes.getTime() >= dt.getTime())) {
                    jornadaNumber = num_jornada;
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
        return jornadaNumber;
    }

    static String formateHora(String diaHora) {
        String[] hora = diaHora.split(" ");
        return hora[1];
    }

    public List<SelectItem> consultaFuncionarioGerente(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

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

                List<Integer> deptIDList = new ArrayList<Integer>();
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

                sql = "select distinct userid,name,defaultdeptid"
                        + " from USERINFO"
                        + " where permissao != 0"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-2, "Selecione o responsável"));
                userList.add(new SelectItem(-1, "TODOS OS RESPONSÁVEIS"));
                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    Integer dept = rs.getInt("defaultdeptid");
                    if (deptIDList.contains(dept)) {
                        userList.add(new SelectItem(userid, name));
                    }
                }
            } else {
                sql = "select distinct userid,name from"
                        + " USERINFO u"
                        + " where defaultdeptid = " + dep + " and "
                        + " permissao != 0"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-2, "Selecione o responsável"));
                userList.add(new SelectItem(-1, "TODOS OS RESPONSÁVEIS"));
                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
                    userList.add(new SelectItem(userid, name));
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
        return userList;
    }

    public List<HistoricoAbono> consultaHistorico(Integer cod_funcionario, Integer permissao, Date dataInicio, Date dataFim) {

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<HistoricoAbono> historicoAbonoList = new ArrayList<HistoricoAbono>();
        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Integer> deptIDList = new ArrayList<Integer>();
        try {
            //Buscar abonos no intervalo de tempo determinado de todos os administradores
            if (cod_funcionario.equals(-1)) {

                ResultSet rs = null;
                Statement stmt = null;

                String sql;

                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

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

               String query = "select t.*, ur.name as responsavel from ( "
                       + "select u.name, u.defaultdeptid, re.* "
                       + "from userinfo u, "
                       + "(select s.userid, s.data as checktime, NULL as leavename, s.resposta as yuanying, s.inclusao as date, s.id_responsavel as id_responsavel, s.codigo, s.descricao, NULL as cod_registro_abono, s.status "
                       + " from solicitacaoabono s where s.status<>'Aceito' "
                       + "union "
                       + "select r.userid, r.checktime, l.leavename, r.yuanying, r.date, r.responsavel as id_responsavel, r.cod_solicitacao as codigo, NULL as descricao, r.cod_registro_abono, 'Aceito' as status "
                       + "from registro_abono r left join leaveclass l on l.leaveid = r.dateid) re "
                       + "where u.userid = re.userid  and re.date between ? and ? "
                       + ") t left join USERINFO ur on ur.USERID = t.id_responsavel order by t.date";               
                       
                pstmt = c.prepareStatement(query);
                String dataInicioSrt = sdf.format(dataInicio);
                String dataFimSrt = sdf.format(dataFim.getTime() + 86399999);
                pstmt.setString(1, dataInicioSrt);
                pstmt.setString(2, dataFimSrt);

            } else {
                
                //consulta o departamento do usuario
                ResultSet rs = null;
                Statement stmt = null;

                String sql;

                sql = "select defaultdeptid from userinfo where userid="+cod_funcionario;
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                int cod_deptid = -1;
                
                if (rs.next()) {
                    cod_deptid = rs.getInt("DEFAULTDEPTID");
                }

                rs.close();
                stmt.close();


                //consulta os departamentos permitidos ao usuario
                rs = null;
                stmt = null;
                sql = "";

                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        +" where (deptid="+cod_deptid+" or supdeptid="+cod_deptid+")"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

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
                
                //Buscar abonos no intervalo de tempo determinado de um administrador específico
               String query = "select t.*, ur.name as responsavel from ( "
                       + "select u.name, u.defaultdeptid, re.* "
                       + "from userinfo u, "
                       + "(select s.userid, s.data as checktime, NULL as leavename, s.resposta as yuanying, s.inclusao as date, s.id_responsavel as id_responsavel, s.codigo, s.descricao, NULL as cod_registro_abono, s.status "
                       + " from solicitacaoabono s where s.status<>'Aceito' "
                       + "union "
                       + "select r.userid, r.checktime, l.leavename, r.yuanying, r.date, r.responsavel as id_responsavel, r.cod_solicitacao as codigo, NULL as descricao, r.cod_registro_abono, 'Aceito' as status "
                       + "from registro_abono r left join leaveclass l on l.leaveid = r.dateid) re "
                       + "where u.userid = re.userid and (re.id_responsavel is null or re.id_responsavel = ?) and re.date between ? and ? "
                       + ") t left join USERINFO ur on ur.USERID = t.id_responsavel order by t.date";
               
                pstmt = c.prepareStatement(query);
                String dataInicioSrt = sdf.format(dataInicio);
                String dataFimSrt = sdf.format(dataFim.getTime() + 86399999);
                pstmt.setInt(1, cod_funcionario);
                pstmt.setString(2, dataInicioSrt);
                pstmt.setString(3, dataFimSrt);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String funcionario = rs.getString("name");
                Integer departamento = rs.getInt("defaultdeptid");
                Timestamp checktime = rs.getTimestamp("checktime");
                String categoriaJustificativa = rs.getString("leavename");
                String justificativa = rs.getString("yuanying");
                Timestamp diaAbono = rs.getTimestamp("date");
                String responsavel = rs.getString("responsavel");
                String solicitacao = rs.getString("descricao");
                Integer cod_registro_abono = rs.getInt("cod_registro_abono");
                Integer cod_solicitacao = rs.getInt("codigo");
                String status = rs.getString("status");

                HistoricoAbono historicoAbono = new HistoricoAbono();
                historicoAbono.setCod_registro_abono(cod_registro_abono);
                historicoAbono.setCod_solicitacao(cod_solicitacao);
                historicoAbono.setFuncionario(funcionario);
                historicoAbono.setAbono(sdf.format(checktime));
                historicoAbono.setCategoriaJustificativa(categoriaJustificativa);
                historicoAbono.setJustificativa(justificativa);
                historicoAbono.setDiaAbono(diaAbono);
                historicoAbono.setResponsavel(responsavel);
                historicoAbono.setSolicitacao(solicitacao);
                historicoAbono.setStatus(status);
               // if (cod_funcionario.equals(-1)) {
                    if (deptIDList.contains(departamento)) {
                        historicoAbonoList.add(historicoAbono);
                    }
               // } else {
               //     historicoAbonoList.add(historicoAbono);
               // }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return historicoAbonoList;
    }
    
    

    public List<HistoricoAbono> consultaHistoricoDetalhado(Integer cod_funcionario, Boolean incluirSubSetores, Integer dept, Date dataInicio, Date dataFim) {

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<HistoricoAbono> historicoAbonoList = new ArrayList<HistoricoAbono>();
        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Integer> deptIDList = new ArrayList<Integer>();
        try {
            //Buscar abonos no intervalo de tempo determinado de todos os administradores



            ResultSet rs = null;
            Statement stmt = null;
            if (incluirSubSetores) {
                String sql;

                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                deptIDList.add(dept);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                stmt.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dept, deptPermitidos, idToSuperHash);
            } else {
                deptIDList.add(dept);
            }

            String query = "SELECT u.USERID, u.USERID, u.NAME, u.DEFAULTDEPTID, r.CHECKTIME, l.LeaveName, r.YUANYING, r.DATE, r.COD_REGISTRO_ABONO, sa.codigo, r.RESPONSAVEL,"
                    + " (SELECT NAME FROM USERINFO WHERE (USERID = r.RESPONSAVEL)) AS NomeResponsavel, sa.Descricao"
                    + " FROM SolicitacaoAbono AS sa RIGHT OUTER JOIN REGISTRO_ABONO AS r ON r.COD_SOLICITACAO = sa.codigo INNER JOIN "
                    + " USERINFO AS u ON r.USERID = u.USERID INNER JOIN LeaveClass AS l ON r.DATEID = l.LeaveId AND r.DATE BETWEEN ? and ?";

            pstmt = c.prepareStatement(query);
            String dataInicioSrt = sdf.format(dataInicio);
            String dataFimSrt = sdf.format(dataFim.getTime() + 86399999);
            pstmt.setString(1, dataInicioSrt);
            pstmt.setString(2, dataFimSrt);



            rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer codigo_funcionario = rs.getInt("userid");
                String funcionario = rs.getString("name");
                Integer departamento = rs.getInt("defaultdeptid");
                Timestamp checktime = rs.getTimestamp("checktime");
                String categoriaJustificativa = rs.getString("leavename");
                String justificativa = rs.getString("yuanying");
                Timestamp diaAbono = rs.getTimestamp("date");
                String responsavel = rs.getString("NomeResponsavel");
                Integer cod_responsavel = rs.getInt("responsavel");
                String solicitacao = rs.getString("descricao");
                Integer cod_registro_abono = rs.getInt("cod_registro_abono");
                Integer cod_solicitacao = rs.getInt("codigo");

                HistoricoAbono historicoAbono = new HistoricoAbono();
                historicoAbono.setCod_funcionario(codigo_funcionario);
                historicoAbono.setCod_registro_abono(cod_registro_abono);
                historicoAbono.setCod_solicitacao(cod_solicitacao);
                historicoAbono.setFuncionario(funcionario);
                historicoAbono.setAbono(sdf.format(checktime));
                historicoAbono.setCategoriaJustificativa(categoriaJustificativa);
                historicoAbono.setJustificativa(justificativa);
                historicoAbono.setDiaAbono(diaAbono);
                historicoAbono.setResponsavel(responsavel);
                historicoAbono.setCod_responsavel(cod_responsavel);
                historicoAbono.setSolicitacao(solicitacao);
                if (deptIDList.contains(departamento)) {
                    historicoAbonoList.add(historicoAbono);
                }

            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return historicoAbonoList;
    }

    public List<HistoricoAbono> consultaHistoricoPorPeriodo(Integer cod_funcionario, Date dataInicio, Date dataFim) {

        List<HistoricoAbono> historicoAbonoList = new ArrayList<HistoricoAbono>();
        PreparedStatement pstmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            //Buscar abonos no intervalo de tempo determinado de todos os administradores
            if (cod_funcionario == -1) {

                String query = "select u.name,s.startspecday,s.endspecday,l.leavename,s.yuanying,s.date,"
                        + "(select name from userinfo where userid = s.responsavel) as responsavel,sa.descricao"
                        + " from user_speday s, leaveClass l, userinfo u,solicitacaoAbono sa"
                        + " where u.userid = s.userid and"
                        + " s.cod_solicitacao = sa.codigo and "
                        + " l.leaveid = s.dateid and"
                        + " date between ? and ? "
                        + " order by date";

                pstmt = c.prepareStatement(query);
                String dataInicioSrt = sdf.format(dataInicio);
                String dataFimSrt = sdf.format(dataFim.getTime() + 86399999);
                pstmt.setString(1, dataInicioSrt);
                pstmt.setString(2, dataFimSrt);

            } else {
                //Buscar abonos no intervalo de tempo determinado de um administradores específico
                String query = "select u.name,s.startspecday,s.endspecday,l.leavename,s.yuanying,s.date,s.responsavel,sa.descricao"
                        + " from user_speday s, leaveClass l, userinfo u,solicitacaoAbono sa"
                        + " where s.responsavel = ? and"
                        + " u.userid = s.userid and"
                        + " s.cod_solicitacao = sa.codigo and "
                        + " l.leaveid = s.dateid and"
                        + " date between ? and ? "
                        + " order by date";

                pstmt = c.prepareStatement(query);
                String dataInicioSrt = sdf.format(dataInicio);
                String dataFimSrt = sdf.format(dataFim.getTime() + 86399999);
                pstmt.setInt(1, cod_funcionario);
                pstmt.setString(2, dataInicioSrt);
                pstmt.setString(3, dataFimSrt);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String funcionario = rs.getString("name");
                Timestamp periodoInicio = rs.getTimestamp("startspecday");
                Timestamp periodoFim = rs.getTimestamp("endspecday");
                String categoriaJustificativa = rs.getString("leavename");
                String justificativa = rs.getString("yuanying");
                Timestamp diaAbono = rs.getTimestamp("date");
                String responsavel = rs.getString("responsavel");
                String solicitacao = rs.getString("descricao");

                HistoricoAbono historicoAbono = new HistoricoAbono();
                historicoAbono.setFuncionario(funcionario);
                SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                if (periodoInicio.getTime() == periodoFim.getTime()) {
                    historicoAbono.setAbono(sdfHora.format(periodoInicio));
                } else {
                    historicoAbono.setAbono(sdfHora.format(periodoInicio) + " - " + sdfHora.format(periodoFim));
                }
                historicoAbono.setCategoriaJustificativa(categoriaJustificativa);
                historicoAbono.setJustificativa(justificativa);
                historicoAbono.setDiaAbono(diaAbono);
                historicoAbono.setResponsavel(responsavel);
                historicoAbono.setSolicitacao(solicitacao);
                historicoAbonoList.add(historicoAbono);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (c != null) {
                    pstmt.close();
                    c.close();
                }
            } catch (Exception e) {
            }
        }
        return historicoAbonoList;
    }

    public Boolean excluirAbono(List<HistoricoAbono> historicoAbonoList) {
        Boolean flag = true;
        PreparedStatement pstmt = null;
        try {
            for (Iterator<HistoricoAbono> it = historicoAbonoList.iterator(); it.hasNext();) {
                HistoricoAbono historicoAbono = it.next();
                String queryDelete = "delete from Registro_Abono "
                        + " where cod_registro_abono = " + historicoAbono.getCod_registro_abono();
                pstmt = c.prepareStatement(queryDelete);
                pstmt.executeUpdate();
                
                String query = "delete from solicitacaoAbono where codigo="+historicoAbono.getCod_solicitacao();
                pstmt = c.prepareStatement(query);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            flag = false;
            System.out.println("Abono: excluirAbono: "+ex);
        }
        return flag;
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

    public List<SelectItem> consultaDepartamentoHierarquico(Integer codigo_funcionario, Integer permissao) {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();

        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> idList = new ArrayList<Integer>();
        HashMap<Integer, String> idToNomeHash = new HashMap<Integer, String>();

        Boolean isAdministradorVisivel = true;

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

            idToSuperHash = new HashMap<Integer, Integer>();

            //Selecionando o departamento do administrador
            sql = "select u.DEFAULTDEPTID,d.deptname from USERINFO u, DEPARTMENTS d"
                    + " where u.userid = " + codigo_funcionario + " and "
                    + " u.DEFAULTDEPTID = d.deptid ";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            Integer dept = -1;
            String nome_dept = "";

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
                nome_dept = rs.getString("DEPTNAME");
            }
            rs.close();
            stmt.close();

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

                } else if (dept == deptID) {
                    isAdministradorVisivel = new Boolean(false);
                }
            }
            rs.close();
            stmt.close();

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
            System.out.println(e.getMessage());
        }
        return saida;
    }

    public List<SelectItem> consultaFuncionario(Integer dep, Boolean incluirSubSetores) {

        List<SelectItem> userList = new ArrayList<SelectItem>();
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();
        List<Integer> deptIDList;

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

                sql = "select distinct u.userid,u.name,u.defaultdeptid"
                        + " from USERINFO u"
                        + " ORDER BY name asc";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
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

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);
                userList.add(new SelectItem(-1, "Selecione um funcionário"));

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    String name = rs.getString("name");
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

    public List<Integer> consultaFuncionarioDepartamento(Integer dept, Integer permissao, String filtro, Boolean incluirSubsetores, int responsavel_ID) {

        List<Integer> matriculasList = new ArrayList<Integer>();
        List<Integer> deptIDList;
        HashMap<Integer, Integer> idToSuperHash = new HashMap<Integer, Integer>();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            if (incluirSubsetores) {

                //Selecionando os departamentos com permissão de visibilidade.
                sql = "select DEPTID,SUPDEPTID from DEPARTMENTS"
                        + " ORDER BY SUPDEPTID asc";
                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                deptIDList = new ArrayList<Integer>();
                deptIDList.add(dept);

                while (rs.next()) {
                    Integer cod = rs.getInt("DEPTID");
                    Integer supdeptid = rs.getInt("SUPDEPTID");
                    idToSuperHash.put(cod, supdeptid);
                }
                rs.close();
                stmt.close();

                List<Integer> deptPermitidos = new ArrayList<Integer>();
                deptIDList = getDeptPermitidos(dept, deptPermitidos, idToSuperHash);

                sql = " select distinct name, u.userid, defaultdeptid"
                        + " from userinfo u"
                        + " where  name like  '" + filtro + "%' and "
                        + " u.ativo = 'true' and"
                        + " u.USERID != " + responsavel_ID
                        + " order by name";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    Integer dep = rs.getInt("defaultdeptid");
                    if (deptIDList.contains(dep)) {
                        matriculasList.add(Integer.parseInt(userid));
                    }
                }
            } else {

                sql = " select  distinct name,u.userid"
                        + " from userinfo u"
                        + " where defaultdeptid = (" + dept + ")" + " and"
                        + " name like '" + filtro + "%' and "
                        + " u.USERID != "+responsavel_ID+" and "
                        + " u.ativo = 'true' "
                        + " order by name";

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String userid = rs.getString("userid");
                    matriculasList.add(Integer.parseInt(userid));
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return matriculasList;
    }

    public List<SelectItem> consultaFuncionarioProprioAdministrador(Integer codigo_usuario) {

        List<SelectItem> userList = new ArrayList<SelectItem>();

        try {
            ResultSet rs = null;
            Statement stmt = null;

            String sql;

            sql = "select name from USERINFO "
                    + " where userid = " + codigo_usuario;

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            userList.add(new SelectItem(-1, "Selecione um funcionário"));

            while (rs.next()) {
                String name = rs.getString("name");
                userList.add(new SelectItem(codigo_usuario, name));
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
        return userList;
    }

    public Boolean isAdministradorVisivel(Integer codigo_funcionario, Integer permissao) {

        List<SelectItem> deptOrdersList = new ArrayList<SelectItem>();
        Boolean isAdministradorVisivel = new Boolean(true);

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

            //Selecionando o departamento do administrador
            sql = "select DEFAULTDEPTID from USERINFO"
                    + " where userid = " + codigo_funcionario;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            Integer dept = -1;

            while (rs.next()) {
                dept = rs.getInt("DEFAULTDEPTID");
            }
            rs.close();
            stmt.close();

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
                } else if (dept == deptID) {
                    isAdministradorVisivel = new Boolean(false);
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
        return isAdministradorVisivel;
    }

    public String consultaDepartamentoNome(Integer deptID) {

        String nomeDept = "";

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = " select DEPTNAME"
                    + " from  DEPARTMENTS d"
                    + " where d.deptid = " + deptID;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                nomeDept = rs.getString("DEPTNAME");
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
        return nomeDept;
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
                    if (!deptList.contains(depFilho) ) {
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

    public static void main(String[] args) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String g = "08:00 - 12:00 - 13:00";
        String[] horasAbonoArray = g.split(" - ");
        System.out.print(g);
    }

    private static Long horaToLong(String hora) {
        Long saida = null;
        try {
            hora += ":00";
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }
        return saida;
    }

    public void fecharConexao() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println("Abono: fecharConexao: "+ex);
                //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
