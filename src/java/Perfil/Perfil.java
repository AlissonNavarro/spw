/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Perfil;

import java.io.Serializable;

/**
 *
 * @author Amvboas
 */
public class Perfil implements Serializable {

    //   private Boolean teveAlteracao;
    private Integer cod_perfil;
    private String nome_perfil;
    private Boolean consInd;
    private Boolean freqnComEscala;
    private Boolean freqnSemEscala;
    private Boolean horaExtra;
    private Boolean showResumo;
    private boolean pesquisarData;

    private Boolean horCronoJorn;
    private Boolean horarios;
    private Boolean cronogramas;
    private Boolean jornadas;

    private Boolean relatorios;
    private Boolean relatorioMensComEscala;
    private Boolean relatorioMensSemEscala;
    private Boolean relatorioDeResumodeEscalas;
    private Boolean relatorioLogDeOperacoes;
    private Boolean relatorioDeResumodeFrequencias;
    private Boolean relatorioCatracas;

    private Boolean presenca;
    private Boolean listaDePresenca;
    private Boolean consultaIrregulares;
    private Boolean consultaHoraExtra;

    private Boolean abonos;
    private Boolean solicitacao;
    private Boolean diasEmAberto;
    private Boolean abonosRapidos;
    private Boolean historicoAbono;
    private Boolean abonosEmMassa;
    private Boolean exclusaoAbono;
    private Boolean rankingAbono;

    private Boolean cadastrosEConfiguracoes;
    private Boolean permissoes;
    private Boolean funcionarios;
    private Boolean deptos;
    private Boolean cargos;
    private Boolean feriados;
    private Boolean justificativas;
    private Boolean tituloDoRelatorio;
    private Boolean horaExtraEGratificacoes;

    private Boolean afdt;
    private Boolean acjef;
    private Boolean espelhoDePonto;
    private Boolean bancoDeDados;
    private Boolean relatorioConfiguracao;
    private Boolean categoriaAfastamento;
    private Boolean afastamento;
    private Boolean pagamento;

    private Boolean manutencao;
    private Boolean listaRelogios;
    private Boolean downloadAfd;
    private Boolean scanIP;

    private Boolean empresas;
    private Boolean verbas;
    
    private Boolean autenticaSerial;

    public Perfil() {
        setaTudoFalso();
    }

    public Boolean getAbonos() {
        return abonos;
    }

    public void setAbonos(Boolean abonos) {
        this.abonos = abonos;
    }

    public Boolean getHorCronoJorn() {
        return horCronoJorn;
    }

    public void setHorCronoJorn(Boolean horCronoJorn) {
        this.horCronoJorn = horCronoJorn;
    }

    public Boolean getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Boolean solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Boolean getConsInd() {
        return consInd;
    }

    public void setConsInd(Boolean consInd) {
        this.consInd = consInd;
    }

    public Boolean getCronogramas() {
        return cronogramas;
    }

    public void setCronogramas(Boolean cronogramas) {
        this.cronogramas = cronogramas;
    }

    public Boolean getDiasEmAberto() {
        return diasEmAberto;
    }

    public void setDiasEmAberto(Boolean diasEmAberto) {
        this.diasEmAberto = diasEmAberto;
    }

    public Boolean getAbonosRapidos() {
        return abonosRapidos;
    }

    public void setAbonosRapidos(Boolean abonosRapidos) {
        this.abonosRapidos = abonosRapidos;
    }

    public Boolean getFreqnComEscala() {
        return freqnComEscala;
    }

    public void setFreqnComEscala(Boolean freqnComEscala) {
        this.freqnComEscala = freqnComEscala;
    }

    public Boolean getFreqnSemEscala() {
        return freqnSemEscala;
    }

    public void setFreqnSemEscala(Boolean freqnSemEscala) {
        this.freqnSemEscala = freqnSemEscala;
    }

    public Boolean getHistoricoAbono() {
        return historicoAbono;
    }

    public void setHistoricoAbono(Boolean historicoAbono) {
        this.historicoAbono = historicoAbono;
    }

    public Boolean getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(Boolean horaExtra) {
        this.horaExtra = horaExtra;
    }

    public Boolean getShowResumo() {
        return showResumo;
    }

    public void setShowResumo(Boolean showResumo) {
        this.showResumo = showResumo;
    }

    public Boolean getPesquisarData() {
        return pesquisarData;
    }

    public void setPesquisarData(Boolean pesquisarData) {
        this.pesquisarData = pesquisarData;
    }

    public Boolean getHorarios() {
        return horarios;
    }

    public void setHorarios(Boolean horarios) {
        this.horarios = horarios;
    }

    public Boolean getJornadas() {
        return jornadas;
    }

    public void setJornadas(Boolean jornadas) {
        this.jornadas = jornadas;
    }

    public Boolean getListaDePresenca() {
        return listaDePresenca;
    }

    public void setListaDePresenca(Boolean listaDePresenca) {
        this.listaDePresenca = listaDePresenca;
    }

    public Boolean getPagamento() {
        return pagamento;
    }

    public void setPagamento(Boolean pagamento) {
        this.pagamento = pagamento;
    }

    public Boolean getRelatorioDeResumodeEscalas() {
        return relatorioDeResumodeEscalas;
    }

    public void setRelatorioDeResumodeEscalas(Boolean relatorioDeResumodeEscalas) {
        this.relatorioDeResumodeEscalas = relatorioDeResumodeEscalas;
    }

    public Boolean getRelatorioDeResumodeFrequencias() {
        return relatorioDeResumodeFrequencias;
    }

    public void setRelatorioDeResumodeFrequencias(Boolean relatorioDeResumodeFrequencias) {
        this.relatorioDeResumodeFrequencias = relatorioDeResumodeFrequencias;
    }

    public Boolean getRelatorioMensComEscala() {
        return relatorioMensComEscala;
    }

    public void setRelatorioMensComEscala(Boolean relatorioMensComEscala) {
        this.relatorioMensComEscala = relatorioMensComEscala;
    }

    public Boolean getRelatorioMensSemEscala() {
        return relatorioMensSemEscala;
    }

    public void setRelatorioMensSemEscala(Boolean relatorioMensSemEscala) {
        this.relatorioMensSemEscala = relatorioMensSemEscala;
    }

    public Boolean getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Boolean relatorios) {
        this.relatorios = relatorios;
    }

    public Integer getCod_perfil() {
        return cod_perfil;
    }

    public void setCod_perfil(Integer cod_perfil) {
        this.cod_perfil = cod_perfil;
    }

    public String getNome_perfil() {
        return nome_perfil;
    }

    public void setNome_perfil(String nome_perfil) {
        this.nome_perfil = nome_perfil;
    }

    public Boolean getAbonosEmMassa() {
        return abonosEmMassa;
    }

    public void setAbonosEmMassa(Boolean abonosEmMassa) {
        this.abonosEmMassa = abonosEmMassa;
    }

    public Boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(Boolean presenca) {
        this.presenca = presenca;
    }

    public Boolean getCargos() {
        return cargos;
    }

    public void setCargos(Boolean cargos) {
        this.cargos = cargos;
    }

    public Boolean getDeptos() {
        return deptos;
    }

    public void setDeptos(Boolean deptos) {
        this.deptos = deptos;
    }

    public Boolean getExclusaoAbono() {
        return exclusaoAbono;
    }

    public void setExclusaoAbono(Boolean exclusaoAbono) {
        this.exclusaoAbono = exclusaoAbono;
    }

    public Boolean getFeriados() {
        return feriados;
    }

    public void setFeriados(Boolean feriados) {
        this.feriados = feriados;
    }

    public boolean isFreqnComEscala() {
        return freqnComEscala;
    }

    public void setFreqnComEscala(boolean freqnComEscala) {
        this.freqnComEscala = freqnComEscala;
    }

    public boolean isFreqnSemEscala() {
        return freqnSemEscala;
    }

    public void setFreqnSemEscala(boolean freqnSemEscala) {
        this.freqnSemEscala = freqnSemEscala;
    }

    public Boolean getCadastrosEConfiguracoes() {
        return cadastrosEConfiguracoes;
    }

    public void setCadastrosEConfiguracoes(Boolean cadastrosEConfiguracoes) {
        this.cadastrosEConfiguracoes = cadastrosEConfiguracoes;
    }

    public Boolean getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Boolean funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Boolean getHoraExtraEGratificacoes() {
        return horaExtraEGratificacoes;
    }

    public void setHoraExtraEGratificacoes(Boolean horaExtraEGratificacoes) {
        this.horaExtraEGratificacoes = horaExtraEGratificacoes;
    }

    public Boolean getJustificativas() {
        return justificativas;
    }

    public Boolean getConsultaIrregulares() {
        return consultaIrregulares;
    }

    public void setConsultaIrregulares(Boolean consultaIrregulares) {
        this.consultaIrregulares = consultaIrregulares;
    }

    public Boolean getConsultaHoraExtra() {
        return consultaHoraExtra;
    }

    public void setConsultaHoraExtra(Boolean consultaHoraExtra) {
        this.consultaHoraExtra = consultaHoraExtra;
    }

    public void setJustificativas(Boolean justificativas) {
        this.justificativas = justificativas;
    }

    public Boolean getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Boolean permissoes) {
        this.permissoes = permissoes;
    }

    public Boolean getRankingAbono() {
        return rankingAbono;
    }

    public void setRankingAbono(Boolean rankingAbono) {
        this.rankingAbono = rankingAbono;
    }

    public Boolean getTituloDoRelatorio() {
        return tituloDoRelatorio;
    }

    public void setTituloDoRelatorio(Boolean tituloDoRelatorio) {
        this.tituloDoRelatorio = tituloDoRelatorio;
    }

    public Boolean getAcjef() {
        return acjef;
    }

    public void setAcjef(Boolean acjef) {
        this.acjef = acjef;
    }

    public Boolean getAfdt() {
        return afdt;
    }

    public void setAfdt(Boolean afdt) {
        this.afdt = afdt;
    }

    public Boolean getBancoDeDados() {
        return bancoDeDados;
    }

    public void setBancoDeDados(Boolean bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    public Boolean getEspelhoDePonto() {
        return espelhoDePonto;
    }

    public void setEspelhoDePonto(Boolean espelhoDePonto) {
        this.espelhoDePonto = espelhoDePonto;
    }

    public Boolean getRelatorioLogDeOperacoes() {
        return relatorioLogDeOperacoes;
    }

    public void setRelatorioLogDeOperacoes(Boolean relatorioLogDeOperacoes) {
        this.relatorioLogDeOperacoes = relatorioLogDeOperacoes;
    }

    public Boolean getRelatorioConfiguracao() {
        return relatorioConfiguracao;
    }

    public void setRelatorioConfiguracao(Boolean relatorioConfiguracao) {
        this.relatorioConfiguracao = relatorioConfiguracao;
    }

    public Boolean getRelatorioCatracas() {
        return relatorioCatracas;
    }

    public void setRelatorioCatracas(Boolean relatorioCatracas) {
        this.relatorioCatracas = relatorioCatracas;
    }

    public Boolean getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Boolean afastamento) {
        this.afastamento = afastamento;
    }

    public Boolean getCategoriaAfastamento() {
        return categoriaAfastamento;
    }

    public void setCategoriaAfastamento(Boolean categoriaAfastamento) {
        this.categoriaAfastamento = categoriaAfastamento;
    }

    public Boolean getManutencao() {
        return manutencao;
    }

    public void setManutencao(Boolean manutencao) {
        this.manutencao = manutencao;
    }

    public Boolean getListaRelogios() {
        return listaRelogios;
    }

    public void setListaRelogios(Boolean listaRelogios) {
        this.listaRelogios = listaRelogios;
    }

    public Boolean getDownloadAfd() {
        return downloadAfd;
    }

    public void setDownloadAfd(Boolean downloadAfd) {
        this.downloadAfd = downloadAfd;
    }

    public Boolean getScanIP() {
        return scanIP;
    }

    public void setScanIP(Boolean scanIP) {
        this.scanIP = scanIP;
    }

    public Boolean getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Boolean empresas) {
        this.empresas = empresas;
    }

    public Boolean getVerbas() {
        return verbas;
    }

    public void setVerbas(Boolean verbas) {
        this.verbas = verbas;
    }

    public Boolean getAutenticaSerial() {
        return autenticaSerial;
    }

    public void setAutenticaSerial(Boolean autenticaSerial) {
        this.autenticaSerial = autenticaSerial;
    }

    void setaTudoFalso() {

        this.consInd = false;
        this.freqnComEscala = false;
        this.freqnSemEscala = false;
        this.horaExtra = false;
        this.showResumo = false;
        this.pesquisarData = false;
        this.horCronoJorn = false;
        this.horarios = false;
        this.cronogramas = false;
        this.jornadas = false;
        this.relatorios = false;
        this.relatorioMensComEscala = false;
        this.relatorioMensSemEscala = false;
        this.relatorioDeResumodeEscalas = false;
        this.relatorioDeResumodeFrequencias = false;
        this.relatorioCatracas = false;
        this.listaDePresenca = false;
        this.abonos = false;
        this.solicitacao = false;
        this.diasEmAberto = false;
        this.abonosRapidos = false;
        this.historicoAbono = false;
        this.cadastrosEConfiguracoes = false;
        this.permissoes = false;
        this.funcionarios = false;
        this.deptos = false;
        this.cargos = false;
        this.feriados = false;
        this.justificativas = false;
        this.tituloDoRelatorio = false;
        this.horaExtraEGratificacoes = false;
        this.abonosEmMassa = false;
        this.exclusaoAbono = false;
        this.rankingAbono = false;
        this.afdt = false;
        this.acjef = false;
        this.espelhoDePonto = false;
        this.bancoDeDados = false;
        this.relatorioLogDeOperacoes = false;
        this.relatorioConfiguracao = false;
        this.categoriaAfastamento = false;
        this.afastamento = false;
        this.pagamento = false;
        this.manutencao = false;
        this.downloadAfd = false;
        this.scanIP = false;
        this.listaRelogios = false;
        this.empresas = false;
        this.verbas = false;
        this.presenca = false;
        this.consultaIrregulares = false;
        this.consultaHoraExtra = false;
        this.autenticaSerial = false;
    }


   // public boolean get() {
    //      throw new UnsupportedOperationException("Not yet implemented");
    //   }
}
