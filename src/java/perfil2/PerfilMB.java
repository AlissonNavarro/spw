package perfil2;

import comunicacao.AcessoBD;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

public class PerfilMB implements Serializable {

    private AcessoBD con;

    public PerfilMB() {
        con = new AcessoBD();
    }

    public int adicionarPerfil(String novoPerfil) {
        int flag = 0;
        try {
            String sql = "select nome_perfil from perfil where nome_perfil = '" + novoPerfil + "'";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                flag = 1;
            }
            rs.close();
            if (flag == 0) {
                String query = "insert into Perfil(nome_perfil) values(?)";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, novoPerfil);
                    con.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("PerfilMB adicionarPerfil " + ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public List<SelectItem> consultaPerfis() {
        List<SelectItem> saida = new ArrayList<SelectItem>();
        try {
            String sql = "select cod_perfil, nome_perfil from Perfil ORDER BY nome_perfil ";
            ResultSet rs = con.executeQuery(sql);
            saida.add(new SelectItem(-1, "Selecione o perfil"));

            while (rs.next()) {
                Integer perfilID = rs.getInt("cod_perfil");
                String nome = rs.getString("nome_perfil");
                saida.add(new SelectItem(perfilID, nome));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("PerfilMB consultaPerfis " + ex);
        } finally {
            con.Desconectar();
        }
        return saida;
    }

    public Perfil consultaPermissoesPerfil(int perfilSelecionado) {
        Perfil perfil = new Perfil();
        try {
            String sql = "select * from perfil where cod_perfil = '" + perfilSelecionado + "'";
            ResultSet rs = con.executeQuery(sql);

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
                perfil.setRelogioPonto(relogioPonto);
                perfil.setEmpresas(empresas);
                perfil.setVerbas(verbas);
                perfil.setRelatorioCatracas(relatorioCatracas);
                perfil.setAutenticaSerial(autenticaSerial);
                //perfil.setPagamento(pagamento);
            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("PerfilMB consultaPermissoesPerfil " + ex);
        } finally {
            con.Desconectar();
        }
        return perfil;
    }

    public int salvarAlteracoesPerfil(Perfil perfilEdit) {
        int flag = 0;
        try {
            String query = "UPDATE Perfil SET cadastrosEConfiguracoes = ?, permissoes = ?, funcionarios = ?, deptos = ?, cargos = ?, feriados = ?, "
                    + "justificativas = ?, tituloDoRelatorio = ?, horaExtraEGratificacoes = ?, consInd = ?, freqnComEscala = ?, freqnSemEscala = ?, "
                    + "horaExtra = ?, horCronoJorn = ?, horarios = ?, cronogramas = ?, jornadas = ?, relatorios = ?, relatorioMensComEscala = ?, "
                    + "relatorioMensSemEscala = ?, relatorioDeResumoDeEscalas = ?, relatorioDeResumoDeFrequencias = ?, listaDePresenca = ?, "
                    + "abonos = ?, solicitacao = ?, diasEmAberto = ?,abonoRapido = ?, historicoAbono = ?, abonosEmMassa = ?, exclusaoAbono = ?, rankingAbono = ?, "
                    + "afdt = ?, acjef = ?, espelhoDePonto = ?, bancoDeDados = ?, logs = ?,"
                    + " relatorioConfiguracao = ?, categoriaAfastamento = ?, afastamento = ?, manutencao = ?, "
                    + "empresas = ?, verbas = ?, showresumo = ?, relatorioCatracas = ?, pesquisarData = ?, "
                    + "listaRelogios = ?, downloadAfd = ?, scanIps = ?, relogioPonto = ?, presenca = ?, consultaIrregulares = ?, consultaHoraExtra = ?, autenticaSerial = ? "
                    + "WHERE cod_perfil = ?";

            if (con.prepareStatement(query)) {
                con.pstmt.setBoolean(1, perfilEdit.getCadastrosEConfiguracoes());
                con.pstmt.setBoolean(2, perfilEdit.getPermissoes());
                con.pstmt.setBoolean(3, perfilEdit.getFuncionarios());
                con.pstmt.setBoolean(4, perfilEdit.getDeptos());
                con.pstmt.setBoolean(5, perfilEdit.getCargos());
                con.pstmt.setBoolean(6, perfilEdit.getFeriados());
                con.pstmt.setBoolean(7, perfilEdit.getJustificativas());
                con.pstmt.setBoolean(8, perfilEdit.getTituloDoRelatorio());
                con.pstmt.setBoolean(9, perfilEdit.getHoraExtraEGratificacoes());

                con.pstmt.setBoolean(10, perfilEdit.getConsInd());
                con.pstmt.setBoolean(11, perfilEdit.getFreqnComEscala());
                con.pstmt.setBoolean(12, perfilEdit.getFreqnSemEscala());
                con.pstmt.setBoolean(13, perfilEdit.getHoraExtra());

                con.pstmt.setBoolean(14, perfilEdit.getHorCronoJorn());
                con.pstmt.setBoolean(15, perfilEdit.getHorarios());
                con.pstmt.setBoolean(16, perfilEdit.getCronogramas());
                con.pstmt.setBoolean(17, perfilEdit.getJornadas());

                con.pstmt.setBoolean(18, perfilEdit.getRelatorios());
                con.pstmt.setBoolean(19, perfilEdit.getRelatorioMensComEscala());
                con.pstmt.setBoolean(20, perfilEdit.getRelatorioMensSemEscala());
                con.pstmt.setBoolean(21, perfilEdit.getRelatorioDeResumodeEscalas());
                con.pstmt.setBoolean(22, perfilEdit.getRelatorioDeResumodeFrequencias());
                con.pstmt.setBoolean(23, perfilEdit.getListaDePresenca());

                con.pstmt.setBoolean(24, perfilEdit.getAbonos());
                con.pstmt.setBoolean(25, perfilEdit.getSolicitacao());
                con.pstmt.setBoolean(26, perfilEdit.getDiasEmAberto());
                con.pstmt.setBoolean(27, perfilEdit.getAbonosRapidos());
                con.pstmt.setBoolean(28, perfilEdit.getHistoricoAbono());
                con.pstmt.setBoolean(29, perfilEdit.getAbonosEmMassa());
                con.pstmt.setBoolean(30, perfilEdit.getExclusaoAbono());
                con.pstmt.setBoolean(31, perfilEdit.getRankingAbono());

                con.pstmt.setBoolean(32, perfilEdit.getAfdt());
                con.pstmt.setBoolean(33, perfilEdit.getAcjef());

                con.pstmt.setBoolean(34, perfilEdit.getEspelhoDePonto());
                con.pstmt.setBoolean(35, perfilEdit.getBancoDeDados());
                con.pstmt.setBoolean(36, perfilEdit.getRelatorioLogDeOperacoes());
                con.pstmt.setBoolean(37, perfilEdit.getRelatorioConfiguracao());
                con.pstmt.setBoolean(38, perfilEdit.getCategoriaAfastamento());
                con.pstmt.setBoolean(39, perfilEdit.getAfastamento());
                con.pstmt.setBoolean(40, perfilEdit.getManutencao());
                con.pstmt.setBoolean(41, perfilEdit.getEmpresas());
                con.pstmt.setBoolean(42, perfilEdit.getVerbas());
                con.pstmt.setBoolean(43, perfilEdit.getShowResumo());
                con.pstmt.setBoolean(44, perfilEdit.getRelatorioCatracas());
                con.pstmt.setBoolean(45, perfilEdit.getPesquisarData());
                con.pstmt.setBoolean(46, perfilEdit.getListaRelogios());
                con.pstmt.setBoolean(47, perfilEdit.getDownloadAfd());
                con.pstmt.setBoolean(48, perfilEdit.getScanIP());
                con.pstmt.setBoolean(49, perfilEdit.getRelogioPonto());

                con.pstmt.setBoolean(50, perfilEdit.getPresenca());
                con.pstmt.setBoolean(51, perfilEdit.getConsultaIrregulares());
                con.pstmt.setBoolean(52, perfilEdit.getConsultaHoraExtra());

                con.pstmt.setBoolean(53, perfilEdit.getAutenticaSerial());

                con.pstmt.setInt(54, perfilEdit.getCod_perfil());

                con.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("PerfilMB salvarAlteracoesPerfil " + ex);
            flag = 1;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public int salvarEditPerfil(Perfil perfilEdit) {
        int flag = 0;
        try {
            String sql = "select nome_perfil from Perfil "
                    + " WHERE     (cod_perfil <> " + perfilEdit.getCod_perfil()
                    + ") AND (nome_perfil = '" + perfilEdit.getNome_perfil() + "') ";
            ResultSet rs = con.executeQuery(sql);
            if (rs.next()) {
                flag = 1;
            }
            rs.close();

            if (flag == 0) {
                String query = "UPDATE Perfil SET nome_perfil = ? WHERE cod_perfil = ?";
                if (con.prepareStatement(query)) {
                    con.pstmt.setString(1, perfilEdit.getNome_perfil());
                    con.pstmt.setInt(2, perfilEdit.getCod_perfil());
                    con.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("PerfilMB salvarEditPerfil "+ex);
            flag = 2;
        } finally {
            con.Desconectar();
        }
        return flag;
    }

    public boolean excluirPerfil(int perfilSelecionado) {
        String queryDelete = "delete from Perfil where cod_perfil = " + perfilSelecionado;
        return con.executeUpdate(queryDelete) > 0;
    }
}
