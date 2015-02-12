package perfil2;

import java.io.Serializable;

public class Perfil implements Serializable {

    private int cod_perfil;
    private String nome_perfil;
    private boolean consInd;
    private boolean freqnComEscala;
    private boolean freqnSemEscala;
    private boolean horaExtra;
    private boolean showResumo;
    private boolean pesquisarData;

    private boolean horCronoJorn;
    private boolean horarios;
    private boolean cronogramas;
    private boolean jornadas;

    private boolean relatorios;
    private boolean relatorioMensComEscala;
    private boolean relatorioMensSemEscala;
    private boolean relatorioDeResumodeEscalas;
    private boolean relatorioLogDeOperacoes;
    private boolean relatorioDeResumodeFrequencias;
    private boolean relatorioCatracas;

    private boolean presenca;
    private boolean listaDePresenca;
    private boolean consultaIrregulares;
    private boolean consultaHoraExtra;

    private boolean abonos;
    private boolean solicitacao;
    private boolean diasEmAberto;
    private boolean abonosRapidos;
    private boolean historicoAbono;
    private boolean abonosEmMassa;
    private boolean exclusaoAbono;
    private boolean rankingAbono;

    private boolean cadastrosEConfiguracoes;
    private boolean permissoes;
    private boolean funcionarios;
    private boolean deptos;
    private boolean cargos;
    private boolean feriados;
    private boolean justificativas;
    private boolean tituloDoRelatorio;
    private boolean horaExtraEGratificacoes;

    private boolean afdt;
    private boolean acjef;
    private boolean espelhoDePonto;
    private boolean bancoDeDados;
    private boolean relatorioConfiguracao;
    private boolean categoriaAfastamento;
    private boolean afastamento;
    private boolean pagamento;

    private boolean manutencao;
    private boolean listaRelogios;
    private boolean downloadAfd;
    private boolean scanIP;
    private boolean relogioPonto;

    private boolean empresas;
    private boolean verbas;

    private boolean autenticaSerial;

    public Perfil() {
        this.cod_perfil = -1;
        this.nome_perfil = "";
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
        this.relogioPonto = false;
        this.consultaIrregulares = false;
        this.consultaHoraExtra = false;
        this.autenticaSerial = false;
    }

    public boolean getAbonos() {
        return abonos;
    }

    public void setAbonos(boolean abonos) {
        this.abonos = abonos;
    }

    public boolean getHorCronoJorn() {
        return horCronoJorn;
    }

    public void setHorCronoJorn(boolean horCronoJorn) {
        this.horCronoJorn = horCronoJorn;
    }

    public boolean getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(boolean solicitacao) {
        this.solicitacao = solicitacao;
    }

    public boolean getConsInd() {
        return consInd;
    }

    public void setConsInd(boolean consInd) {
        this.consInd = consInd;
    }

    public boolean getCronogramas() {
        return cronogramas;
    }

    public void setCronogramas(boolean cronogramas) {
        this.cronogramas = cronogramas;
    }

    public boolean getDiasEmAberto() {
        return diasEmAberto;
    }

    public void setDiasEmAberto(boolean diasEmAberto) {
        this.diasEmAberto = diasEmAberto;
    }

    public boolean getAbonosRapidos() {
        return abonosRapidos;
    }

    public void setAbonosRapidos(boolean abonosRapidos) {
        this.abonosRapidos = abonosRapidos;
    }

    public boolean getFreqnComEscala() {
        return freqnComEscala;
    }

    public void setFreqnComEscala(boolean freqnComEscala) {
        this.freqnComEscala = freqnComEscala;
    }

    public boolean getFreqnSemEscala() {
        return freqnSemEscala;
    }

    public void setFreqnSemEscala(boolean freqnSemEscala) {
        this.freqnSemEscala = freqnSemEscala;
    }

    public boolean getHistoricoAbono() {
        return historicoAbono;
    }

    public void setHistoricoAbono(boolean historicoAbono) {
        this.historicoAbono = historicoAbono;
    }

    public boolean getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(boolean horaExtra) {
        this.horaExtra = horaExtra;
    }

    public boolean getShowResumo() {
        return showResumo;
    }

    public void setShowResumo(boolean showResumo) {
        this.showResumo = showResumo;
    }

    public boolean getPesquisarData() {
        return pesquisarData;
    }

    public void setPesquisarData(boolean pesquisarData) {
        this.pesquisarData = pesquisarData;
    }

    public boolean getHorarios() {
        return horarios;
    }

    public void setHorarios(boolean horarios) {
        this.horarios = horarios;
    }

    public boolean getJornadas() {
        return jornadas;
    }

    public void setJornadas(boolean jornadas) {
        this.jornadas = jornadas;
    }

    public boolean getListaDePresenca() {
        return listaDePresenca;
    }

    public void setListaDePresenca(boolean listaDePresenca) {
        this.listaDePresenca = listaDePresenca;
    }

    public boolean getPagamento() {
        return pagamento;
    }

    public void setPagamento(boolean pagamento) {
        this.pagamento = pagamento;
    }

    public boolean getRelatorioDeResumodeEscalas() {
        return relatorioDeResumodeEscalas;
    }

    public void setRelatorioDeResumodeEscalas(boolean relatorioDeResumodeEscalas) {
        this.relatorioDeResumodeEscalas = relatorioDeResumodeEscalas;
    }

    public boolean getRelatorioDeResumodeFrequencias() {
        return relatorioDeResumodeFrequencias;
    }

    public void setRelatorioDeResumodeFrequencias(boolean relatorioDeResumodeFrequencias) {
        this.relatorioDeResumodeFrequencias = relatorioDeResumodeFrequencias;
    }

    public boolean getRelatorioMensComEscala() {
        return relatorioMensComEscala;
    }

    public void setRelatorioMensComEscala(boolean relatorioMensComEscala) {
        this.relatorioMensComEscala = relatorioMensComEscala;
    }

    public boolean getRelatorioMensSemEscala() {
        return relatorioMensSemEscala;
    }

    public void setRelatorioMensSemEscala(boolean relatorioMensSemEscala) {
        this.relatorioMensSemEscala = relatorioMensSemEscala;
    }

    public boolean getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(boolean relatorios) {
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

    public boolean getAbonosEmMassa() {
        return abonosEmMassa;
    }

    public void setAbonosEmMassa(boolean abonosEmMassa) {
        this.abonosEmMassa = abonosEmMassa;
    }

    public boolean getPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }

    public boolean getCargos() {
        return cargos;
    }

    public void setCargos(boolean cargos) {
        this.cargos = cargos;
    }

    public boolean getDeptos() {
        return deptos;
    }

    public void setDeptos(boolean deptos) {
        this.deptos = deptos;
    }

    public boolean getExclusaoAbono() {
        return exclusaoAbono;
    }

    public void setExclusaoAbono(boolean exclusaoAbono) {
        this.exclusaoAbono = exclusaoAbono;
    }

    public boolean getFeriados() {
        return feriados;
    }

    public void setFeriados(boolean feriados) {
        this.feriados = feriados;
    }

    public boolean isFreqnComEscala() {
        return freqnComEscala;
    }

    public boolean isFreqnSemEscala() {
        return freqnSemEscala;
    }

    public boolean getCadastrosEConfiguracoes() {
        return cadastrosEConfiguracoes;
    }

    public void setCadastrosEConfiguracoes(boolean cadastrosEConfiguracoes) {
        this.cadastrosEConfiguracoes = cadastrosEConfiguracoes;
    }

    public boolean getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(boolean funcionarios) {
        this.funcionarios = funcionarios;
    }

    public boolean getHoraExtraEGratificacoes() {
        return horaExtraEGratificacoes;
    }

    public void setHoraExtraEGratificacoes(boolean horaExtraEGratificacoes) {
        this.horaExtraEGratificacoes = horaExtraEGratificacoes;
    }

    public boolean getJustificativas() {
        return justificativas;
    }

    public boolean getConsultaIrregulares() {
        return consultaIrregulares;
    }

    public void setConsultaIrregulares(boolean consultaIrregulares) {
        this.consultaIrregulares = consultaIrregulares;
    }

    public boolean getConsultaHoraExtra() {
        return consultaHoraExtra;
    }

    public void setConsultaHoraExtra(boolean consultaHoraExtra) {
        this.consultaHoraExtra = consultaHoraExtra;
    }

    public void setJustificativas(boolean justificativas) {
        this.justificativas = justificativas;
    }

    public boolean getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(boolean permissoes) {
        this.permissoes = permissoes;
    }

    public boolean getRankingAbono() {
        return rankingAbono;
    }

    public void setRankingAbono(boolean rankingAbono) {
        this.rankingAbono = rankingAbono;
    }

    public boolean getTituloDoRelatorio() {
        return tituloDoRelatorio;
    }

    public void setTituloDoRelatorio(boolean tituloDoRelatorio) {
        this.tituloDoRelatorio = tituloDoRelatorio;
    }

    public boolean getAcjef() {
        return acjef;
    }

    public void setAcjef(boolean acjef) {
        this.acjef = acjef;
    }

    public boolean getAfdt() {
        return afdt;
    }

    public void setAfdt(boolean afdt) {
        this.afdt = afdt;
    }

    public boolean getBancoDeDados() {
        return bancoDeDados;
    }

    public void setBancoDeDados(boolean bancoDeDados) {
        this.bancoDeDados = bancoDeDados;
    }

    public boolean getEspelhoDePonto() {
        return espelhoDePonto;
    }

    public void setEspelhoDePonto(boolean espelhoDePonto) {
        this.espelhoDePonto = espelhoDePonto;
    }

    public boolean getRelatorioLogDeOperacoes() {
        return relatorioLogDeOperacoes;
    }

    public void setRelatorioLogDeOperacoes(boolean relatorioLogDeOperacoes) {
        this.relatorioLogDeOperacoes = relatorioLogDeOperacoes;
    }

    public boolean getRelatorioConfiguracao() {
        return relatorioConfiguracao;
    }

    public void setRelatorioConfiguracao(boolean relatorioConfiguracao) {
        this.relatorioConfiguracao = relatorioConfiguracao;
    }

    public boolean getRelatorioCatracas() {
        return relatorioCatracas;
    }

    public void setRelatorioCatracas(boolean relatorioCatracas) {
        this.relatorioCatracas = relatorioCatracas;
    }

    public boolean getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(boolean afastamento) {
        this.afastamento = afastamento;
    }

    public boolean getCategoriaAfastamento() {
        return categoriaAfastamento;
    }

    public void setCategoriaAfastamento(boolean categoriaAfastamento) {
        this.categoriaAfastamento = categoriaAfastamento;
    }

    public boolean getManutencao() {
        return manutencao;
    }

    public void setManutencao(boolean manutencao) {
        this.manutencao = manutencao;
    }

    public boolean getListaRelogios() {
        return listaRelogios;
    }

    public void setListaRelogios(boolean listaRelogios) {
        this.listaRelogios = listaRelogios;
    }

    public boolean getDownloadAfd() {
        return downloadAfd;
    }

    public void setDownloadAfd(boolean downloadAfd) {
        this.downloadAfd = downloadAfd;
    }

    public boolean getScanIP() {
        return scanIP;
    }

    public void setScanIP(boolean scanIP) {
        this.scanIP = scanIP;
    }

    public boolean getEmpresas() {
        return empresas;
    }

    public void setEmpresas(boolean empresas) {
        this.empresas = empresas;
    }

    public boolean getVerbas() {
        return verbas;
    }

    public void setVerbas(boolean verbas) {
        this.verbas = verbas;
    }

    public boolean getAutenticaSerial() {
        return autenticaSerial;
    }

    public void setAutenticaSerial(boolean autenticaSerial) {
        this.autenticaSerial = autenticaSerial;
    }

    public boolean getRelogioPonto() {
        return relogioPonto;
    }

    public void setRelogioPonto(boolean relogioPonto) {
        this.relogioPonto = relogioPonto;
    }

}
