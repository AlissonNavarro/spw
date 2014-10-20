/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class RelatorioResumoFrequenciaEscolhaExcel implements Serializable {

    private Boolean nome;
    private Boolean cpf;
    private Boolean matricula;
    private Boolean departamento;
    private Boolean regime;
    private Boolean cargo;
    private Boolean dataAdmicao;
    private Boolean atrasos;
    private Boolean saldo;
    private Boolean faltas;
    private Boolean horaExtra;
    private Boolean totalQntAtraso16_59;
    private Boolean totalQntAtrasoMaiorUmaHora;
    private Boolean diasAtraso;
    private Boolean diasFalta;
    private Boolean qntDiasSemEscalasTrabalhados;
    private Boolean diasSemEscalasTrabalhados;
    private Boolean qntPontosForaDaFaixa;
    private Boolean diasPontoForaDaFaixa;
    private Boolean gratificacao;
    private Boolean feriadosTrabalhados;
    private Boolean totalHorasReceberAddNoturno;
    private Boolean qntDiasAddNoturno;
    private Boolean qntDSR;
    private Boolean gratificacaoDiaCritico;
    private Boolean horaPrevista;
    private Boolean horaTrabalhada;
    private Boolean todos;

    public Boolean getSaldo() {
        return saldo;
    }

    public void setSaldo(Boolean saldo) {
        this.saldo = saldo;
    }

    public Boolean getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(Boolean atrasos) {
        this.atrasos = atrasos;
    }

    public Boolean getCargo() {
        return cargo;
    }

    public void setCargo(Boolean cargo) {
        this.cargo = cargo;
    }

    public Boolean getCpf() {
        return cpf;
    }

    public void setCpf(Boolean cpf) {
        this.cpf = cpf;
    }

    public Boolean getDataAdmicao() {
        return dataAdmicao;
    }

    public void setDataAdmicao(Boolean dataAdmicao) {
        this.dataAdmicao = dataAdmicao;
    }

    public Boolean getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Boolean departamento) {
        this.departamento = departamento;
    }

    public Boolean getFaltas() {
        return faltas;
    }

    public void setFaltas(Boolean faltas) {
        this.faltas = faltas;
    }

    public Boolean getFeriadosTrabalhados() {
        return feriadosTrabalhados;
    }

    public void setFeriadosTrabalhados(Boolean feriadosTrabalhados) {
        this.feriadosTrabalhados = feriadosTrabalhados;
    }

    public Boolean getGratificacao() {
        return gratificacao;
    }

    public void setGratificacao(Boolean gratificacao) {
        this.gratificacao = gratificacao;
    }

    public Boolean getGratificacaoDiaCritico() {
        return gratificacaoDiaCritico;
    }

    public void setGratificacaoDiaCritico(Boolean gratificacaoDiaCritico) {
        this.gratificacaoDiaCritico = gratificacaoDiaCritico;
    }

    public Boolean getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(Boolean horaExtra) {
        this.horaExtra = horaExtra;
    }

    public Boolean getTotalQntAtraso16_59() {
        return totalQntAtraso16_59;
    }

    public void setTotalQntAtraso16_59(Boolean totalQntAtraso16_59) {
        this.totalQntAtraso16_59 = totalQntAtraso16_59;
    }

    public Boolean getTotalQntAtrasoMaiorUmaHora() {
        return totalQntAtrasoMaiorUmaHora;
    }

    public void setTotalQntAtrasoMaiorUmaHora(Boolean totalQntAtrasoMaiorUmaHora) {
        this.totalQntAtrasoMaiorUmaHora = totalQntAtrasoMaiorUmaHora;
    }

    public Boolean getHoraPrevista() {
        return horaPrevista;
    }

    public void setHoraPrevista(Boolean horaPrevista) {
        this.horaPrevista = horaPrevista;
    }

    public Boolean getHoraTrabalhada() {
        return horaTrabalhada;
    }

    public void setHoraTrabalhada(Boolean horaTrabalhada) {
        this.horaTrabalhada = horaTrabalhada;
    }

    public Boolean getMatricula() {
        return matricula;
    }

    public void setMatricula(Boolean matricula) {
        this.matricula = matricula;
    }

    public Boolean getNome() {
        return nome;
    }

    public void setNome(Boolean nome) {
        this.nome = nome;
    }

    public Boolean getQntDSR() {
        return qntDSR;
    }

    public void setQntDSR(Boolean qntDSR) {
        this.qntDSR = qntDSR;
    }

    public Boolean getQntDiasAddNoturno() {
        return qntDiasAddNoturno;
    }

    public void setQntDiasAddNoturno(Boolean qntDiasAddNoturno) {
        this.qntDiasAddNoturno = qntDiasAddNoturno;
    }

    public Boolean getTotalHorasReceberAddNoturno() {
        return totalHorasReceberAddNoturno;
    }

    public void setTotalHorasReceberAddNoturno(Boolean totalHorasReceberAddNoturno) {
        this.totalHorasReceberAddNoturno = totalHorasReceberAddNoturno;
    }

    public Boolean getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(Boolean diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public Boolean getDiasFalta() {
        return diasFalta;
    }

    public void setDiasFalta(Boolean diasFalta) {
        this.diasFalta = diasFalta;
    }

    public Boolean getRegime() {
        return regime;
    }

    public void setRegime(Boolean regime) {
        this.regime = regime;
    }

    public Boolean getDiasPontoForaDaFaixa() {
        return diasPontoForaDaFaixa;
    }

    public void setDiasPontoForaDaFaixa(Boolean diasPontoForaDaFaixa) {
        this.diasPontoForaDaFaixa = diasPontoForaDaFaixa;
    }

    public Boolean getDiasSemEscalasTrabalhados() {
        return diasSemEscalasTrabalhados;
    }

    public void setDiasSemEscalasTrabalhados(Boolean diasSemEscalasTrabalhados) {
        this.diasSemEscalasTrabalhados = diasSemEscalasTrabalhados;
    }

    public Boolean getQntDiasSemEscalasTrabalhados() {
        return qntDiasSemEscalasTrabalhados;
    }

    public void setQntDiasSemEscalasTrabalhados(Boolean qntDiasSemEscalasTrabalhados) {
        this.qntDiasSemEscalasTrabalhados = qntDiasSemEscalasTrabalhados;
    }

    public Boolean getQntPontosForaDaFaixa() {
        return qntPontosForaDaFaixa;
    }

    public void setQntPontosForaDaFaixa(Boolean qntPontosForaDaFaixa) {
        this.qntPontosForaDaFaixa = qntPontosForaDaFaixa;
    }

    /**
     * @return the todos
     */
    public Boolean getTodos() {
        return todos;
    }

    /**
     * @param todos the todos to set
     */
    public void setTodos(Boolean todos) {
        this.todos = todos;
    }
}
