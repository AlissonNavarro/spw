/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AjustaHorarios;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Amvboas
 */
public class Horario implements Serializable {

    private Integer horarioId;
    private String nome;
    private Date entrada;
    private Date saida;
    private Integer CheckIn;
    private Integer CheckOut;
    private Date inicioFaixaEntrada;
    private Date fimFaixaEntrada;
    private Date inicioFaixaSaida;
    private Date fimFaixaSaida;
    private String Legend;

    public Horario(String name, Integer horarioId, Date entrada, Date saida, Integer CheckIn, Integer CheckOut, Date inicioFaixaEntrada, Date fimFaixaEntrada, Date inicioFaixaSaida, Date fimFaixaSaida, String Legend) {
        this.nome = name;
        this.horarioId = horarioId;
        this.entrada = entrada;
        this.saida = saida;
        this.CheckIn = CheckIn;
        this.CheckOut = CheckOut;
        this.inicioFaixaEntrada = inicioFaixaEntrada;
        this.fimFaixaEntrada = fimFaixaEntrada;
        this.inicioFaixaSaida = inicioFaixaSaida;
        this.fimFaixaSaida = fimFaixaSaida;
        this.Legend = Legend;
    }

    public Horario(String name, Integer horarioId, Date entrada, Date saida, String Legend) {
        this.nome = name;
        this.horarioId = horarioId;
        this.entrada = entrada;
        this.saida = saida;
        this.CheckIn = 1;
        this.CheckOut = 1;
        this.inicioFaixaEntrada = geraInicioFaixa(entrada);
        this.fimFaixaEntrada = geraFimFaixa(entrada);
        this.inicioFaixaSaida = geraInicioFaixa(saida);
        this.fimFaixaSaida = geraFimFaixa(saida);
        this.Legend = Legend;
    }



    public Date geraInicioFaixa(Date entrada){
        Long ifaixa = entrada.getTime();
        ifaixa=ifaixa-900000;
        Date difaixa = new Date(ifaixa);
        return difaixa;
    }
    
    public Date geraFimFaixa(Date saida){
        Long sfaixa = saida.getTime();
        sfaixa=sfaixa+900000;
        Date dsfaixa = new Date(sfaixa);
        return dsfaixa;
    }
    
    

    public Horario() {
        this.nome = "";
        this.horarioId = -1;
        this.entrada = null;
        this.saida = null;
        this.CheckIn = null;
        this.CheckOut = null;
        this.inicioFaixaEntrada = null;
        this.fimFaixaEntrada = null;
        this.inicioFaixaSaida = null;
        this.fimFaixaSaida = null;
        this.Legend = "null";
    }

    public Integer getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(Integer CheckIn) {
        this.CheckIn = CheckIn;
    }

    public Integer getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(Integer CheckOut) {
        this.CheckOut = CheckOut;
    }

    public String getLegend() {
        return Legend;
    }

    public void setLegend(String Legend) {
        this.Legend = Legend;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getFimFaixaEntrada() {
        return fimFaixaEntrada;
    }

    public void setFimFaixaEntrada(Date fimFaixaEntrada) {
        this.fimFaixaEntrada = fimFaixaEntrada;
    }

    public Date getFimFaixaSaida() {
        return fimFaixaSaida;
    }

    public void setFimFaixaSaida(Date fimFaixaSaida) {
        this.fimFaixaSaida = fimFaixaSaida;
    }

    public Integer getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Integer horarioId) {
        this.horarioId = horarioId;
    }

    public Date getInicioFaixaEntrada() {
        return inicioFaixaEntrada;
    }

    public void setInicioFaixaEntrada(Date inicioFaixaEntrada) {
        this.inicioFaixaEntrada = inicioFaixaEntrada;
    }

    public Date getInicioFaixaSaida() {
        return inicioFaixaSaida;
    }

    public void setInicioFaixaSaida(Date inicioFaixaSaida) {
        this.inicioFaixaSaida = inicioFaixaSaida;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
