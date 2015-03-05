package Usuario;

import comunicacao.AcessoBD;
import entidades.Perfil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.faces.model.SelectItem;

public class Banco {

    AcessoBD con;
    static Connection theConn;
    Boolean hasConnection = false;

    public Banco() {
        con = new AcessoBD();
    }
    
    public boolean Conectar() {
        return con.Conectar();
    }

    public Usuario getUsuarioByMatricula(String login) {
        Usuario usuario = new Usuario();
        try {
            ResultSet rs;
            String sql = "select u.*,d.deptid from"
                    + " USERINFO u,DEPARTMENTS d "
                    + " where u.userid = '" + login
                    + "' and u.defaultdeptid = d.deptid"; 
            rs = con.executeQuery(sql);
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
            sql = "select supdeptid from DEPARTMENTS "
                    + " where deptid = " + usuario.getPermissao();
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer supdeptid = rs.getInt("supdeptid");

                if (supdeptid == 0) {
                    usuario.setIsAcessoTotal(true);
                } else {
                    usuario.setIsAcessoTotal(false);
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return usuario;
    }

    public Boolean userADExists(String userAD) {
        Boolean exists = false;
        try {
            ResultSet rs;
            String sql = "select * from USERINFO u "
                    + " where u.ADUsername = '" + userAD;
            
            rs = con.executeQuery(sql);
            while (rs.next()) {
                exists = true;
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return exists;
    }

    public Usuario getUsuarioByUserAD(String userAD) {

        Usuario usuario = new Usuario();
        try {
            ResultSet rs;
            String sql = "select u.userid,u.ssn,u.senha,u.name,u.permissao,u.perfil,d.deptid,u.hiredday,u.adusername"
                    + " from USERINFO u,DEPARTMENTS d "
                    + " where u.ADUsername = '" + userAD + "' and "
                    + " u.defaultdeptid = d.deptid";

            
            rs = con.executeQuery(sql);
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

                
                rs = con.executeQuery(sql);

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
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return usuario;
    }

    public Boolean insertADUser(String cpf, String aduser) {
        Boolean ok = false;

        PreparedStatement pstmt;
        try {
            String query = "UPDATE userinfo SET adusername = ? WHERE ssn = ?";

            con.prepareStatement(query);
            con.pstmt.setString(1, aduser);
            con.pstmt.setString(2, cpf);
            con.executeUpdate();
            ok = true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return ok;
    }

    public Usuario getUsuarioByCPF(String login) {
        Usuario usuario = new Usuario();
        try {
            ResultSet rs;
            String sql;

            sql = "select u.userid,u.ssn,u.senha,u.name,u.permissao,u.perfil,d.deptid,u.hiredday, u.adusername"
                    + " from USERINFO u,DEPARTMENTS d "
                    + " where u.ssn = '" + login + "' and "
                    + " u.defaultdeptid = d.deptid";            
            rs = con.executeQuery(sql);
            Boolean flag = false;
            while (rs.next()) {
                usuario.setLogin(rs.getInt("userid"));
                usuario.setNome(rs.getString("name"));
                usuario.setSsn(rs.getString("ssn"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPermissao(rs.getString("permissao"));
                usuario.setDepartamento(rs.getInt("deptid"));
                usuario.setCodPerfil(rs.getInt("perfil"));
                usuario.setDataContratacao(rs.getDate("hiredday"));
                usuario.setUserAD(rs.getString("adusername"));
                flag = true;
            }
            rs.close();
            

            if (flag) {
                sql = "select supdeptid from DEPARTMENTS "
                        + " where deptid = " + usuario.getPermissao();

                
                rs = con.executeQuery(sql);

                while (rs.next()) {
                    Integer supdeptid = rs.getInt("supdeptid");

                    if (supdeptid == 0) {
                        usuario.setIsAcessoTotal(true);
                    } else {
                        usuario.setIsAcessoTotal(false);
                    }
                }
                rs.close();
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return usuario;
    }

    public List<SelectItem> getUsuarioVinculos(String login) {
        List<SelectItem> vinculosList = new ArrayList<>();
        try {
            ResultSet rs;
            
            String sql;

            sql = "select userid from USERINFO "
                    + " where ssn = '" + login + "'";

            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                Integer userid = rs.getInt("userid");
                vinculosList.add(new SelectItem(userid, userid.toString()));
            }

            if ((vinculosList.size() < 2) || login.equals("000.000.000-00")) {
                vinculosList = new ArrayList<>();
            }
            rs.close();
            

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return vinculosList;
    }

    public boolean isAdministrador(String login) {

        try {
            ResultSet rs;
            String sql;

            sql = "select u.permissao from USERINFO "
                    + " where u.userid = '" + login + "'";
            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                String permissao = rs.getString("permissao");
                if (permissao.equals("1")) {
                    return true;
                }
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            con.Desconectar();
        }
        return false;
    }

    public String getServidorAD() {
        String servidor = "";

        try {
            ResultSet rs;
            String sql;

            sql = "select IpAD from config";            
            rs = con.executeQuery(sql);

            while (rs.next()) {
                servidor = rs.getString("IpAd");
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
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
        try {
            String query = "UPDATE config SET lastDate = ?";
            con.prepareStatement(query);
            con.pstmt.setString(1, Base64Crypt.encrypt(data));
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    protected void atualizarSerial(String serial) {
        try {
            String query = "UPDATE config SET serial = ?";
            con.prepareStatement(query);
            con.pstmt.setString(1, serial);
            con.executeUpdate();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
    }

    protected String consultarSerial() {
        try {

            String query = "select serial from config";
            ResultSet rs = con.executeQuery(query);
            if (rs.next()) {
                return rs.getString("serial");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        } finally {
            con.Desconectar();
        }
        return null;
    }

    protected Date getUltimoLogin() {
        PreparedStatement pstmt = null;
        try {

            String query = "select lastDate from config";
            Date d = null;
            ResultSet rs = con.executeQuery(query);
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
            con.Desconectar();
        }
        return null;
    }
    
    public boolean consultaCnpjEmpregador(String cnpj) {
        String resultado = "";
        try {
            String querySelect = "select cnpj from empregador order by id";
            ResultSet rs = con.executeQuery(querySelect);
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
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return false;
    }

    public boolean mudarSenha(String cpf, String senha) {
        try {
            String query = "UPDATE userinfo SET senha = ? WHERE ssn = ?";

            con.prepareStatement(query);
            con.pstmt.setString(1, senha);
            con.pstmt.setString(2, cpf);
            int r = con.executeUpdate();
            if (r == 1) {
                return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.Desconectar();
        }
        return false;

    }

    public int[] consultaSenhasRestricoes() {
        int[] restricoes = {0, 0, 0, 0};
        try {
            ResultSet rs;
            String sql = "select * from senhasRestricoes";
            
            rs = con.executeQuery(sql);

            if (rs.next()) {
                restricoes[0] = rs.getInt("senhaSize");
                restricoes[1] = rs.getInt("maiusculasCount");
                restricoes[2] = rs.getInt("numerosCount");
                restricoes[3] = rs.getInt("simbolosCount");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);

        } finally {
            con.Desconectar();
        }

        return restricoes;
    }

    public boolean alteraSenhasRestricoes(int size, int maiusc, int num, int simb) {
        PreparedStatement pstmt = null;
        try {
            String querySelect = "select * from senhasRestricoes";
            boolean empty = true;
            ResultSet rs = con.executeQuery(querySelect);
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

            con.prepareStatement(querySelect);
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
            con.Desconectar();
        }
        return false;
    }

    public Perfil consultaPermissoesPerfil(Integer perfilSelecionado) {
        Perfil perfil = new Perfil();
        try {
            ResultSet rs;
            String sql;

            sql = "select * from perfil "
                    + " where cod_perfil = '" + perfilSelecionado + "'";
            rs = con.executeQuery(sql);

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
                Boolean relogioPonto = rs.getBoolean("relogioPonto");
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
                perfil.setRelogioPonto(relogioPonto);
                perfil.setEmpresas(empresas);
                perfil.setVerbas(verbas);
                perfil.setRelatorioCatracas(relatorioCatracas);
                perfil.setPesquisarData(pesquisarData);
                perfil.setAutenticaSerial(autenticaSerial);
                //perfil.setPagamento(pagamento);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);

        } finally {
            con.Desconectar();
        }
        return perfil;
    }

    public void select() throws Exception {
        ResultSet rs;
        String sql;
        sql = "SELECT * FROM USER_SPEDAY WHERE STARTSPECDAY between #01/06/09 01:20:00# and  #33/06/09 15:50:00#";
        String sql2 = "select *  from USER_SPEDAY "
                + "WHERE STARTSPECDAY = '01/06/09 01:20:00'";
 
        rs = con.executeQuery(sql);
        while (rs.next()) {
            Integer userid = rs.getInt("userid");
            System.out.println(userid);
        }
        rs.close();
        
    }
}
