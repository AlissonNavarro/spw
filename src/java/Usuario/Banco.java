package Usuario;

import ClockManager.ClockManagerBean;
import Metodos.Metodos;
import Perfil.Perfil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class Banco {

    Driver d;
    Connection c;
    private Boolean isAtivo = false;
    static Connection theConn;
    Boolean hasConnection = false;

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public void Conectar() throws SQLException {
        String url = Metodos.getUrlConexao();
        hasConnection = true;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            hasConnection = false;
            System.out.println("Usuário.Banco: Conectar(): " + cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
            isAtivo = true;
        } catch (SQLException ex) {
            hasConnection = false;
            System.out.println("Usuário.Banco: Banco(): " + ex);
            isAtivo = false;
        }
    }

    public Usuario getUsuarioByMatricula(String login) {

        Usuario usuario = new Usuario();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select u.*,d.deptid from"
                    + " USERINFO u,DEPARTMENTS d "
                    + " where u.userid = '" + login
                    + "' and u.defaultdeptid = d.deptid";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                String senha = rs.getString("senha");
                String permissao = rs.getString("permissao");
                Integer departamento = rs.getInt("deptid");
                Integer perfil = rs.getInt("perfil");
                Date dataContratacao = rs.getDate("hiredday");

                usuario.setLogin(userid);
                usuario.setNome(name);
                usuario.setSsn(ssn);
                usuario.setSenha(senha);
                usuario.setPermissao(permissao);
                usuario.setDepartamento(departamento);
                usuario.setCodPerfil(perfil);
                usuario.setDataContratacao(dataContratacao);
            }
            rs.close();
            stmt.close();

            sql = "select supdeptid from DEPARTMENTS "
                    + " where deptid = " + usuario.getPermissao();

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer supdeptid = rs.getInt("supdeptid");

                if (supdeptid == 0) {
                    usuario.setIsAcessoTotal(true);
                } else {
                    usuario.setIsAcessoTotal(false);
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
        return usuario;
    }

    public Boolean userADExists(String userAD) {
        Boolean exists = false;
        try {
            Conectar();
            ResultSet rs;
            Statement stmt;
            String sql = "select * from USERINFO u "
                    + " where u.ADUsername = '" + userAD;
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                exists = true;
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
        return exists;
    }

    public Usuario getUsuarioByUserAD(String userAD) {

        Usuario usuario = new Usuario();
        try {
            Conectar();
            ResultSet rs;
            Statement stmt;
            String sql = "select u.userid,u.ssn,u.senha,u.name,u.permissao,u.perfil,d.deptid,u.hiredday,u.adusername"
                    + " from USERINFO u,DEPARTMENTS d "
                    + " where u.ADUsername = '" + userAD + "' and "
                    + " u.defaultdeptid = d.deptid";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            Boolean flag = false;
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                String senha = rs.getString("senha");
                String permissao = rs.getString("permissao");
                Integer departamento = rs.getInt("deptid");
                Integer perfil = rs.getInt("perfil");
                Date dataContratacao = rs.getDate("hiredday");

                usuario.setLogin(userid);
                usuario.setNome(name);
                usuario.setSsn(ssn);
                usuario.setSenha(senha);
                usuario.setPermissao(permissao);
                usuario.setDepartamento(departamento);
                usuario.setCodPerfil(perfil);
                usuario.setDataContratacao(dataContratacao);
                flag = true;
            }
            if (flag) {
                sql = "select supdeptid from DEPARTMENTS "
                        + " where deptid = " + usuario.getPermissao();

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Integer supdeptid = rs.getInt("supdeptid");

                    if (supdeptid == 0) {
                        usuario.setIsAcessoTotal(true);
                    } else {
                        usuario.setIsAcessoTotal(false);
                    }
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
        return usuario;
    }

    public Boolean insertADUser(String cpf, String aduser) {
        Boolean ok = false;

        PreparedStatement pstmt = null;
        try {
            String query = "UPDATE userinfo SET adusername = ? WHERE ssn = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, aduser);
            pstmt.setString(2, cpf);
            pstmt.executeUpdate();

            pstmt.close();
            ok = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ok;
    }

    public Usuario getUsuarioByCPF(String login) {

        Usuario usuario = new Usuario();

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select u.userid,u.ssn,u.senha,u.name,u.permissao,u.perfil,d.deptid,u.hiredday, u.adusername"
                    + " from USERINFO u,DEPARTMENTS d "
                    + " where u.ssn = '" + login + "' and "
                    + " u.defaultdeptid = d.deptid";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            Boolean flag = false;
            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                String name = rs.getString("name");
                String ssn = rs.getString("ssn");
                String senha = rs.getString("senha");
                String permissao = rs.getString("permissao");
                Integer departamento = rs.getInt("deptid");
                Integer perfil = rs.getInt("perfil");
                Date dataContratacao = rs.getDate("hiredday");
                String adname = rs.getString("adusername");

                usuario.setLogin(userid);
                usuario.setNome(name);
                usuario.setSsn(ssn);
                usuario.setSenha(senha);
                usuario.setPermissao(permissao);
                usuario.setDepartamento(departamento);
                usuario.setCodPerfil(perfil);
                usuario.setDataContratacao(dataContratacao);
                usuario.setUserAD(adname);
                flag = true;
            }
            rs.close();
            stmt.close();

            if (flag) {
                sql = "select supdeptid from DEPARTMENTS "
                        + " where deptid = " + usuario.getPermissao();

                stmt = c.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Integer supdeptid = rs.getInt("supdeptid");

                    if (supdeptid == 0) {
                        usuario.setIsAcessoTotal(true);
                    } else {
                        usuario.setIsAcessoTotal(false);
                    }
                }
                rs.close();
                stmt.close();
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
        return usuario;
    }

    public List<SelectItem> getUsuarioVinculos(String login) {

        List<SelectItem> vinculosList = new ArrayList<SelectItem>();

        try {
            Conectar();
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select userid from USERINFO "
                    + " where ssn = '" + login + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                vinculosList.add(new SelectItem(userid, userid.toString()));
            }

            if ((vinculosList.size() < 2) || login.equals("000.000.000-00")) {
                vinculosList = new ArrayList<SelectItem>();
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
        return vinculosList;
    }

    public boolean isAdministrador(String login) {

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select u.permissao from USERINFO "
                    + " where u.userid = '" + login + "'";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String permissao = rs.getString("permissao");
                if (permissao.equals("1")) {
                    return true;
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
        return false;
    }

    public String getServidorAD() {
        String servidor = "";

        try {
            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select IpAD from config";
            if (c.isClosed()) {
                return servidor;
            }
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("IpAd");
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
        if (servidor == null) {
            servidor = "";
        }
        return servidor;
    }

    protected void atualizarUltimoLogin() {
        Date agora = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
        atualizarUltimoLogin(sdf.format(agora));
    }

    protected void atualizarUltimoLogin(String data) {
        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE config SET lastDate = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, Base64Crypt.encrypt(data));

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

    protected void atualizarSerial(String serial) {
        PreparedStatement pstmt = null;
        try {

            String query = "UPDATE config SET serial = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, serial);
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

    protected String consultarSerial() {
        PreparedStatement pstmt = null;
        try {

            String query = "select serial from config";

            pstmt = c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("serial");
            } else {
                return null;
            }
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
        return null;
    }

    protected Date getUltimoLogin() {
        PreparedStatement pstmt = null;
        try {

            String query = "select lastDate from config";
            Date d = null;
            pstmt = c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String cifra = rs.getString("lastDate");
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
                String tmp = Base64Crypt.decrypt(cifra);
                d = sdf.parse(tmp);
            }
            return d;

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
        return null;
    }
    
    public boolean consultaCnpjEmpregador(String cnpj) {
        PreparedStatement pstmt = null;
        String resultado = "";
        try {
            String querySelect = "select cnpj from empregador order by id";
            
            
            ResultSet rs;
            pstmt = c.prepareStatement(querySelect);
            rs = pstmt.executeQuery();
            cnpj = cnpj.replace(".", "").replace("/", "").replace("-", "");
            while (rs.next()) {
                resultado = rs.getString("Cnpj");
                resultado = resultado.replace(".", "").replace("/", "").replace("-", "");
                if (cnpj.equals(resultado)) {
                    return true;
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
        return false;
    }

    public boolean mudarSenha(String cpf, String senha) {

        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE userinfo SET senha = ? WHERE ssn = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setString(1, senha);
            pstmt.setString(2, cpf);
            int r = pstmt.executeUpdate();
            if (r == 1) {
                return true;
            }

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
        return false;

    }

    public int[] consultaSenhasRestricoes() {
        int[] restricoes = {0, 0, 0, 0};
        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from senhasRestricoes";
            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                restricoes[0] = rs.getInt("senhaSize");
                restricoes[1] = rs.getInt("maiusculasCount");
                restricoes[2] = rs.getInt("numerosCount");
                restricoes[3] = rs.getInt("simbolosCount");
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

        return restricoes;
    }

    public boolean alteraSenhasRestricoes(int size, int maiusc, int num, int simb) {
        PreparedStatement pstmt = null;
        try {
            String querySelect = "select * from senhasRestricoes";
            boolean empty = true;
            ResultSet rs;
            pstmt = c.prepareStatement(querySelect);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                empty = false;
            }
            rs.close();

            if (empty) {
                querySelect = "INSERT INTO senhasRestricoes (senhaSize, maiusculasCount, "
                        + "numerosCount, simbolosCount) VALUES (?,?,?,?)";

            } else {
                querySelect = "UPDATE senhasRestricoes SET senhaSize = ?, "
                        + "maiusculasCount = ?, numerosCount = ?, simbolosCount = ?";
            }

            pstmt = c.prepareStatement(querySelect);
            pstmt.setInt(1, size);
            pstmt.setInt(2, maiusc);
            pstmt.setInt(3, num);
            pstmt.setInt(4, simb);
            int r = pstmt.executeUpdate();
            if (r == 1) {
                return true;
            }

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
        return false;
    }

    public Perfil consultaPermissoesPerfil(Integer perfilSelecionado) {

        Perfil perfil = new Perfil();
        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select * from perfil "
                    + " where cod_perfil = '" + perfilSelecionado + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {

                Integer cod_perfil = rs.getInt("cod_perfil");
                String nome_perfil = rs.getString("nome_perfil");
                Boolean consInd = rs.getBoolean("consInd");
                Boolean freqnComEscala = rs.getBoolean("freqnComEscala");
                Boolean freqnSemEscala = rs.getBoolean("freqnSemEscala");
                Boolean horaExtra = rs.getBoolean("horaExtra");
                Boolean showResumo = rs.getBoolean("showresumo");
                boolean pesquisarData = rs.getBoolean("pesquisarData");
                Boolean horCronoJorn = rs.getBoolean("horCronoJorn");
                Boolean horarios = rs.getBoolean("horarios");
                Boolean cronogramas = rs.getBoolean("cronogramas");
                Boolean jornadas = rs.getBoolean("jornadas");
                Boolean relatorios = rs.getBoolean("relatorios");
                Boolean relatorioMensComEscala = rs.getBoolean("relatorioMensComEscala");
                Boolean relatorioMensSemEscala = rs.getBoolean("relatorioMensSemEscala");
                Boolean relatorioDeResumodeEscalas = rs.getBoolean("relatorioDeResumodeEscalas");
                Boolean relatorioDeResumodeFrequencias = rs.getBoolean("relatorioDeResumodeFrequencias");
                Boolean listaDePresenca = rs.getBoolean("listaDePresenca");
                Boolean consultaIrregulares = rs.getBoolean("consultaIrregulares");
                Boolean consultaHoraExtra = rs.getBoolean("consultaHoraExtra");
                Boolean presenca = rs.getBoolean("presenca");
                Boolean abonos = rs.getBoolean("abonos");
                Boolean solicitacao = rs.getBoolean("solicitacao");
                Boolean diasEmAberto = rs.getBoolean("diasEmAberto");
                Boolean abonoRapido = rs.getBoolean("abonoRapido");
                Boolean historicoAbono = rs.getBoolean("historicoAbono");
                Boolean cadastrosEConfiguracoes = rs.getBoolean("cadastrosEConfiguracoes");
                Boolean permissoes = rs.getBoolean("permissoes");
                Boolean funcionarios = rs.getBoolean("funcionarios");
                Boolean deptos = rs.getBoolean("deptos");
                Boolean cargos = rs.getBoolean("cargos");
                Boolean feriados = rs.getBoolean("feriados");
                Boolean justificativas = rs.getBoolean("justificativas");
                Boolean tituloDoRelatorio = rs.getBoolean("tituloDoRelatorio");
                Boolean horaExtraEGratificacoes = rs.getBoolean("horaExtraEGratificacoes");
                Boolean abonosEmMassa = rs.getBoolean("abonosEmMassa");
                Boolean exclusaoAbono = rs.getBoolean("exclusaoAbono");
                Boolean rankingAbono = rs.getBoolean("rankingAbono");
                Boolean afdt = rs.getBoolean("afdt");
                Boolean acjef = rs.getBoolean("acjef");
                Boolean espelhoDePonto = rs.getBoolean("espelhoDePonto");
                Boolean bancoDeDados = rs.getBoolean("bancoDeDados");
                Boolean logs = rs.getBoolean("logs");
                Boolean relatorioConfiguracao = rs.getBoolean("relatorioConfiguracao");
                Boolean categoriaAfastamento = rs.getBoolean("categoriaAfastamento");
                Boolean afastamento = rs.getBoolean("afastamento");
                Boolean manutencao = rs.getBoolean("manutencao");
                Boolean listaRelogios = rs.getBoolean("listaRelogios");
                Boolean downloadAfd = rs.getBoolean("downloadAfd");
                Boolean scanIps = rs.getBoolean("scanIps");
                Boolean empresas = rs.getBoolean("empresas");
                Boolean verbas = rs.getBoolean("verbas");
                Boolean relatorioCatracas = rs.getBoolean("relatorioCatracas");
                Boolean autenticaSerial = rs.getBoolean("autenticaSerial");
                //Boolean pagamento = rs.getBoolean("pagamento");

                perfil.setCod_perfil(cod_perfil);
                perfil.setNome_perfil(nome_perfil);
                perfil.setConsInd(consInd);
                perfil.setFreqnComEscala(freqnComEscala);
                perfil.setFreqnSemEscala(freqnSemEscala);
                perfil.setHoraExtra(horaExtra);
                perfil.setShowResumo(showResumo);
                perfil.setHorCronoJorn(horCronoJorn);
                perfil.setHorarios(horarios);
                perfil.setCronogramas(cronogramas);
                perfil.setJornadas(jornadas);
                perfil.setRelatorios(relatorios);
                perfil.setRelatorioMensComEscala(relatorioMensComEscala);
                perfil.setRelatorioMensSemEscala(relatorioMensSemEscala);
                perfil.setRelatorioDeResumodeEscalas(relatorioDeResumodeEscalas);
                perfil.setRelatorioDeResumodeFrequencias(relatorioDeResumodeFrequencias);
                perfil.setListaDePresenca(listaDePresenca);
                perfil.setPresenca(presenca);
                perfil.setConsultaHoraExtra(consultaHoraExtra);
                perfil.setConsultaIrregulares(consultaIrregulares);
                perfil.setAbonos(abonos);
                perfil.setSolicitacao(solicitacao);
                perfil.setDiasEmAberto(diasEmAberto);
                perfil.setAbonosRapidos(abonoRapido);
                perfil.setHistoricoAbono(historicoAbono);
                perfil.setCadastrosEConfiguracoes(cadastrosEConfiguracoes);
                perfil.setPermissoes(permissoes);
                perfil.setFuncionarios(funcionarios);
                perfil.setDeptos(deptos);
                perfil.setCargos(cargos);
                perfil.setFeriados(feriados);
                perfil.setJustificativas(justificativas);
                perfil.setTituloDoRelatorio(tituloDoRelatorio);
                perfil.setHoraExtraEGratificacoes(horaExtraEGratificacoes);
                perfil.setAbonosEmMassa(abonosEmMassa);
                perfil.setExclusaoAbono(exclusaoAbono);
                perfil.setRankingAbono(rankingAbono);
                perfil.setAfdt(afdt);
                perfil.setAcjef(acjef);
                perfil.setEspelhoDePonto(espelhoDePonto);
                perfil.setBancoDeDados(bancoDeDados);
                perfil.setRelatorioLogDeOperacoes(logs);
                perfil.setRelatorioConfiguracao(relatorioConfiguracao);
                perfil.setCategoriaAfastamento(categoriaAfastamento);
                perfil.setAfastamento(afastamento);
                perfil.setManutencao(manutencao);
                perfil.setDownloadAfd(downloadAfd);
                perfil.setScanIP(scanIps);
                perfil.setListaRelogios(listaRelogios);
                perfil.setEmpresas(empresas);
                perfil.setVerbas(verbas);
                perfil.setRelatorioCatracas(relatorioCatracas);
                perfil.setPesquisarData(pesquisarData);
                perfil.setAutenticaSerial(autenticaSerial);
                //perfil.setPagamento(pagamento);
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
        return perfil;
    }

    public void select() throws SQLException {
        ResultSet rs;
        Statement stmt;
        String sql;
        sql = "SELECT * FROM USER_SPEDAY WHERE STARTSPECDAY between #01/06/09 01:20:00# and  #33/06/09 15:50:00#";
        String sql2 = "select *  from USER_SPEDAY "
                + "WHERE STARTSPECDAY = '01/06/09 01:20:00'";

        stmt = c.createStatement();
        rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Integer userid = rs.getInt("userid");
            System.out.println(userid);
        }
        rs.close();
        stmt.close();

    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Connection c = new Banco().Conectarr();
        Statement stmt = c.createStatement();
        //       File file = new File("myimage.gif");
        //       FileInputStream fis = new FileInputStream(file);
        //       PreparedStatement ps =
//                conn.prepareStatement("insert into images values (?,?)");
//        ps.setString(1, file.getName());
//        ps.setBinaryStream(2, fis, (int) file.length());

    }

    public Connection Conectarr() throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:jtds:sqlserver://FS-DESEN-05:1904/lurdes;user=user_web;password=123@net");
            //         executeStatement(con);
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe);
        }
        return c;
    }
}
