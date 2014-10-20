/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Perfil;

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

import java.util.List;
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
        String url = Metodos.getUrlConexao();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Perfil: Conectar: "+cnfe);
        }
    }

    public Banco() {
        try {
            Conectar();
        } catch (SQLException ex) {
            System.out.println("Perfil: Banco: "+ex);
            //Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Integer adicionarPerfil(String novoPerfil) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome_perfil from perfil "
                    + " where nome_perfil = '" + novoPerfil + "'";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "insert into Perfil(nome_perfil) values(?)";

                pstmt = c.prepareStatement(query);
                pstmt.setString(1, novoPerfil);
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

    public List<SelectItem> consultaPerfis() {
        List<SelectItem> saida = new ArrayList<SelectItem>();

        try {
            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select cod_perfil, nome_perfil from Perfil"
                    + " ORDER BY nome_perfil ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione o perfil"));

            while (rs.next()) {
                Integer perfilID = rs.getInt("cod_perfil");
                String nome = rs.getString("nome_perfil");
                saida.add(new SelectItem(perfilID, nome));
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
        return saida;
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
                Boolean pesquisarData = rs.getBoolean("pesquisarData");
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
                Boolean presenca = rs.getBoolean("presenca");
                Boolean consultaIrregulares = rs.getBoolean("consultaIrregulares");
                Boolean consultaHoraExtra = rs.getBoolean("consultaHoraExtra");
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
                boolean autenticaSerial = rs.getBoolean("autenticaSerial");
                //Boolean pagamento = rs.getBoolean("pagamento");

                perfil.setCod_perfil(cod_perfil);
                perfil.setNome_perfil(nome_perfil);
                perfil.setConsInd(consInd);
                perfil.setFreqnComEscala(freqnComEscala);
                perfil.setFreqnSemEscala(freqnSemEscala);
                perfil.setHoraExtra(horaExtra);
                perfil.setShowResumo(showResumo);
                perfil.setPesquisarData(pesquisarData);
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
                perfil.setScanIP(scanIps);
                perfil.setDownloadAfd(downloadAfd);
                perfil.setListaRelogios(listaRelogios);
                perfil.setEmpresas(empresas);
                perfil.setVerbas(verbas);
                perfil.setRelatorioCatracas(relatorioCatracas);
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

    Integer salvarAlteracoesPerfil(Perfil perfilEdit) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {
            String query = "UPDATE Perfil SET cadastrosEConfiguracoes = ?, permissoes = ?, funcionarios = ?, deptos = ?, cargos = ?, feriados = ?, "
                    + "justificativas = ?, tituloDoRelatorio = ?, horaExtraEGratificacoes = ?, consInd = ?, freqnComEscala = ?, freqnSemEscala = ?, "
                    + "horaExtra = ?, horCronoJorn = ?, horarios = ?, cronogramas = ?, jornadas = ?, relatorios = ?, relatorioMensComEscala = ?, "
                    + "relatorioMensSemEscala = ?, relatorioDeResumoDeEscalas = ?, relatorioDeResumoDeFrequencias = ?, listaDePresenca = ?, "
                    + "abonos = ?, solicitacao = ?, diasEmAberto = ?,abonoRapido = ?, historicoAbono = ?, abonosEmMassa = ?, exclusaoAbono = ?, rankingAbono = ?, "
                    + "afdt = ?, acjef = ?, espelhoDePonto = ?, bancoDeDados = ?, logs = ?,"
                    + " relatorioConfiguracao = ?, categoriaAfastamento = ?, afastamento = ?, manutencao = ?, "
                    + "empresas = ?, verbas = ?, showresumo = ?, relatorioCatracas = ?, pesquisarData = ?, "
                    + "listaRelogios = ?, downloadAfd = ?, scanIps = ?, presenca = ?, consultaIrregulares = ?, consultaHoraExtra = ?, autenticaSerial = ? "
                    + "WHERE cod_perfil = ?";

            pstmt = c.prepareStatement(query);
            pstmt.setBoolean(1, perfilEdit.getCadastrosEConfiguracoes());
            pstmt.setBoolean(2, perfilEdit.getPermissoes());
            pstmt.setBoolean(3, perfilEdit.getFuncionarios());
            pstmt.setBoolean(4, perfilEdit.getDeptos());
            pstmt.setBoolean(5, perfilEdit.getCargos());
            pstmt.setBoolean(6, perfilEdit.getFeriados());
            pstmt.setBoolean(7, perfilEdit.getJustificativas());
            pstmt.setBoolean(8, perfilEdit.getTituloDoRelatorio());
            pstmt.setBoolean(9, perfilEdit.getHoraExtraEGratificacoes());


            pstmt.setBoolean(10, perfilEdit.getConsInd());
            pstmt.setBoolean(11, perfilEdit.getFreqnComEscala());
            pstmt.setBoolean(12, perfilEdit.getFreqnSemEscala());
            pstmt.setBoolean(13, perfilEdit.getHoraExtra());

            pstmt.setBoolean(14, perfilEdit.getHorCronoJorn());
            pstmt.setBoolean(15, perfilEdit.getHorarios());
            pstmt.setBoolean(16, perfilEdit.getCronogramas());
            pstmt.setBoolean(17, perfilEdit.getJornadas());

            pstmt.setBoolean(18, perfilEdit.getRelatorios());
            pstmt.setBoolean(19, perfilEdit.getRelatorioMensComEscala());
            pstmt.setBoolean(20, perfilEdit.getRelatorioMensSemEscala());
            pstmt.setBoolean(21, perfilEdit.getRelatorioDeResumodeEscalas());
            pstmt.setBoolean(22, perfilEdit.getRelatorioDeResumodeFrequencias());
            pstmt.setBoolean(23, perfilEdit.getListaDePresenca());

            pstmt.setBoolean(24, perfilEdit.getAbonos());
            pstmt.setBoolean(25, perfilEdit.getSolicitacao());
            pstmt.setBoolean(26, perfilEdit.getDiasEmAberto());
            pstmt.setBoolean(27, perfilEdit.getAbonosRapidos());
            pstmt.setBoolean(28, perfilEdit.getHistoricoAbono());
            pstmt.setBoolean(29, perfilEdit.getAbonosEmMassa());
            pstmt.setBoolean(30, perfilEdit.getExclusaoAbono());
            pstmt.setBoolean(31, perfilEdit.getRankingAbono());

            pstmt.setBoolean(32, perfilEdit.getAfdt());
            pstmt.setBoolean(33, perfilEdit.getAcjef());

            pstmt.setBoolean(34, perfilEdit.getEspelhoDePonto());
            pstmt.setBoolean(35, perfilEdit.getBancoDeDados());
            pstmt.setBoolean(36, perfilEdit.getRelatorioLogDeOperacoes());
            pstmt.setBoolean(37, perfilEdit.getRelatorioConfiguracao());
            pstmt.setBoolean(38, perfilEdit.getCategoriaAfastamento());
            pstmt.setBoolean(39, perfilEdit.getAfastamento());
            pstmt.setBoolean(40, perfilEdit.getManutencao());
            pstmt.setBoolean(41, perfilEdit.getEmpresas());
            pstmt.setBoolean(42, perfilEdit.getVerbas());
            pstmt.setBoolean(43, perfilEdit.getShowResumo());
            pstmt.setBoolean(44, perfilEdit.getRelatorioCatracas());
            pstmt.setBoolean(45, perfilEdit.getPesquisarData());
            pstmt.setBoolean(46, perfilEdit.getListaRelogios());
            pstmt.setBoolean(47, perfilEdit.getDownloadAfd());
            pstmt.setBoolean(48, perfilEdit.getScanIP());
            
            pstmt.setBoolean(49, perfilEdit.getPresenca());
            pstmt.setBoolean(50, perfilEdit.getConsultaIrregulares());
            pstmt.setBoolean(51, perfilEdit.getConsultaHoraExtra());
            
            pstmt.setBoolean(52, perfilEdit.getAutenticaSerial());

            pstmt.setInt(53, perfilEdit.getCod_perfil());
            

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            flag = 1;
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

    public int salvarEditPerfil(Perfil perfilEdit) {
        int flag = 0;
        PreparedStatement pstmt = null;

        try {

            ResultSet rs;
            Statement stmt;

            String sql;

            sql = "select nome_perfil from Perfil "
                    + " WHERE     (cod_perfil <> " + perfilEdit.getCod_perfil()
                    + ") AND (nome_perfil = '" + perfilEdit.getNome_perfil() + "') ";

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                flag = 1;
            }
            rs.close();
            stmt.close();

            if (flag == 0) {
                String query = "UPDATE Perfil SET nome_perfil = ? WHERE cod_perfil = ?";
                pstmt = c.prepareStatement(query);
                pstmt.setString(1, perfilEdit.getNome_perfil());

                pstmt.setInt(2, perfilEdit.getCod_perfil());

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

    Boolean excluirPerfil(Integer perfilSelecionado) {
        Boolean flag = false;
        PreparedStatement pstmt = null;

        String queryDelete = "delete from Perfil "
                + " where cod_perfil = " + perfilSelecionado;
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
        return flag;
    }
}
