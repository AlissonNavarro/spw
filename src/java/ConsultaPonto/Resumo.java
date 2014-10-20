/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class Resumo implements Serializable{

    private String horasContratadas;
    private String horasTrabalhadas;
    private String saldo;
    private Integer diasContratados;
    private Integer diasTrabalhados;
    private Integer faltas;
    private String horaExtra;
    private String adicionalNoturno;

    public String getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(String adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public Integer getDiasContratados() {
        return diasContratados;
    }

    public void setDiasContratados(Integer diasContratados) {
        this.diasContratados = diasContratados;
    }

    public Integer getDiasTrabalhados() {
        return diasTrabalhados;
    }

    public void setDiasTrabalhados(Integer diasTrabalhados) {
        this.diasTrabalhados = diasTrabalhados;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public String getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(String horaExtra) {
        this.horaExtra = horaExtra;
    }

    public String getHorasContratadas() {
        return horasContratadas;
    }

    public void setHorasContratadas(String horasContratadas) {
        this.horasContratadas = horasContratadas;
    }

    public String getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(String horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}
