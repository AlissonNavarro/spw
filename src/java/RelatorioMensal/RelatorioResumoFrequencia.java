/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author amsgama
 */
public class RelatorioResumoFrequencia implements Serializable {

    private Integer userid;
    private String nome;
    private String cargo;
    private String departamento;
    private String cpf;
    private String matricula;
    private String regime;
    private String saldo;
    private String horaPrevista;
    private String horaTrabalhada;
    private String dataAdmicao;
    private String faltas;
    private String atrasos;
    private Integer qntDiasAddNoturno;
    private Integer qntDSR;
    private String totalHorasReceberAddNoturno;
    private String feriadosTrabalhados;
    private List<String> horaExtra;
    private String gratificacao;
    private String gratificacaoDiaCritico;
    private Integer totalQntAtraso16_59;
    private Integer totalQntAtrasoMaiorUmaHora;
    private String diasAtraso;
    private String diasFalta;
    private Integer qntDiasSemEscalasTrabalhados;
    private String diasSemEscalasTrabalhados;
    private Integer qntPontosForaDaFaixa;
    private String diasPontoForaDaFaixa;


    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getHoraPrevista() {
        return horaPrevista;
    }

    public void setHoraPrevista(String horaPrevista) {
        this.horaPrevista = horaPrevista;
    }

    public String getHoraTrabalhada() {
        return horaTrabalhada;
    }

    public void setHoraTrabalhada(String horaTrabalhada) {
        this.horaTrabalhada = horaTrabalhada;
    }

    public String getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(String atrasos) {
        this.atrasos = atrasos;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDataAdmicao() {
        return dataAdmicao;
    }

    public void setDataAdmicao(String dataAdmicao) {
        this.dataAdmicao = dataAdmicao;
    }

    public String getFaltas() {
        return faltas;
    }

    public void setFaltas(String faltas) {
        this.faltas = faltas;
    }

    public String getFeriadosTrabalhados() {
        return feriadosTrabalhados;
    }

    public void setFeriadosTrabalhados(String feriadosTrabalhados) {
        this.feriadosTrabalhados = feriadosTrabalhados;
    }

    public List<String> getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(List<String> horaExtra) {
        this.horaExtra = horaExtra;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQntDiasAddNoturno() {
        return qntDiasAddNoturno;
    }

    public void setQntDiasAddNoturno(Integer qntDiasAddNoturno) {
        this.qntDiasAddNoturno = qntDiasAddNoturno;
    }

    public String getTotalHorasReceberAddNoturno() {
        return totalHorasReceberAddNoturno;
    }

    public void setTotalHorasReceberAddNoturno(String totalHorasReceberAddNoturno) {
        this.totalHorasReceberAddNoturno = totalHorasReceberAddNoturno;
    }

    public Integer getQntDSR() {
        return qntDSR;
    }

    public void setQntDSR(Integer qntDSR) {
        this.qntDSR = qntDSR;
    }

    public String getGratificacao() {
        return gratificacao;
    }

    public void setGratificacao(String gratificacao) {
        this.gratificacao = gratificacao;
    }

    public String getGratificacaoDiaCritico() {
        return gratificacaoDiaCritico;
    }

    public void setGratificacaoDiaCritico(String gratificacaoDiaCritico) {
        this.gratificacaoDiaCritico = gratificacaoDiaCritico;
    }

    public Integer getTotalQntAtraso16_59() {
        return totalQntAtraso16_59;
    }

    public void setTotalQntAtraso16_59(Integer totalQntAtraso16_59) {
        this.totalQntAtraso16_59 = totalQntAtraso16_59;
    }

    public Integer getTotalQntAtrasoMaiorUmaHora() {
        return totalQntAtrasoMaiorUmaHora;
    }

    public void setTotalQntAtrasoMaiorUmaHora(Integer totalQntAtrasoMaiorUmaHora) {
        this.totalQntAtrasoMaiorUmaHora = totalQntAtrasoMaiorUmaHora;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getDiasFalta() {
        return diasFalta;
    }

    public void setDiasFalta(String diasFalta) {
        this.diasFalta = diasFalta;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getDiasPontoForaDaFaixa() {
        return diasPontoForaDaFaixa;
    }

    public void setDiasPontoForaDaFaixa(String diasPontoForaDaFaixa) {
        this.diasPontoForaDaFaixa = diasPontoForaDaFaixa;
    }

    public String getDiasSemEscalasTrabalhados() {
        return diasSemEscalasTrabalhados;
    }

    public void setDiasSemEscalasTrabalhados(String diasSemEscalasTrabalhados) {
        this.diasSemEscalasTrabalhados = diasSemEscalasTrabalhados;
    }

    public Integer getQntDiasSemEscalasTrabalhados() {
        return qntDiasSemEscalasTrabalhados;
    }

    public void setQntDiasSemEscalasTrabalhados(Integer qntDiasSemEscalasTrabalhados) {
        this.qntDiasSemEscalasTrabalhados = qntDiasSemEscalasTrabalhados;
    }

    public Integer getQntPontosForaDaFaixa() {
        return qntPontosForaDaFaixa;
    }

    public void setQntPontosForaDaFaixa(Integer qntPontosForaDaFaixa) {
        this.qntPontosForaDaFaixa = qntPontosForaDaFaixa;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    
}
