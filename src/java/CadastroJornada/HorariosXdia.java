/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Amvboas
 */
public class HorariosXdia implements Serializable {

    private Integer dia;
    private Integer units;
    private Integer horarioId;
    private Integer cyle;
    private Date entrada;
    private Date saida;
    private String schName;
    private Timestamp inicioFaixaEntrada;
    private Timestamp fimFaixaEntrada;
    private Timestamp inicioFaixaSaida;
    private Timestamp fimFaixaSaida;
    private Timestamp inicioPrimeiroDescanso;
    private Timestamp fimPrimeiroDescanso;
    private Timestamp inicioInterjornada;
    private Timestamp fimInterjornada;
    private Timestamp inicioSegundoDescanso;
    private Timestamp fimSegundoDescanso;


    public HorariosXdia(Integer dia, Boolean hasVirada, Integer units, Integer horarioId, 
            Integer cyle, Timestamp entrada, Timestamp saida, String schName, 
            Timestamp inicioFaixaEntrada, Timestamp fimFaixaEntrada, Timestamp inicioFaixaSaida, 
            Timestamp fimFaixaSaida) {
        this.dia = dia;
        this.units = units;
        this.horarioId = horarioId;
        this.cyle = cyle;
        this.entrada = entrada;
        this.saida = saida;
        this.schName = schName;
        this.inicioFaixaEntrada = inicioFaixaEntrada;
        this.fimFaixaEntrada = fimFaixaEntrada;
        this.inicioFaixaSaida = inicioFaixaSaida;
        this.fimFaixaSaida = fimFaixaSaida;

    }
    
    //Utilizado para a tabela quando for setar uma jornada em um cronograma
    public HorariosXdia(Integer dia, Boolean hasVirada, Integer units, Integer horarioId, 
            Integer cyle, Timestamp entrada, Timestamp saida, String schName, Timestamp inicioFaixaEntrada, 
            Timestamp fimFaixaEntrada, Timestamp inicioFaixaSaida, Timestamp fimFaixaSaida, 
            Timestamp inicioPrimeiroDescanso, Timestamp fimPrimeiroDescanso, Timestamp inicioSegundoDescanso, 
            Timestamp fimSegundoDescanso, Timestamp inicioInterjornada, Timestamp fimInterjornada) {
        this.dia = dia;
        this.units = units;
        this.horarioId = horarioId;
        this.cyle = cyle;
        this.entrada = entrada;
        this.saida = saida;
        this.schName = schName;
        this.inicioFaixaEntrada = inicioFaixaEntrada;
        this.fimFaixaEntrada = fimFaixaEntrada;
        this.inicioFaixaSaida = inicioFaixaSaida;
        this.fimFaixaSaida = fimFaixaSaida;
        this.inicioPrimeiroDescanso = inicioPrimeiroDescanso;
        this.fimPrimeiroDescanso = fimPrimeiroDescanso;
        this.inicioInterjornada = inicioInterjornada;
        this.fimInterjornada = fimInterjornada;
        this.inicioSegundoDescanso = inicioSegundoDescanso;
        this.fimSegundoDescanso = fimSegundoDescanso;

    }

    public HorariosXdia() {
    }

    public Integer getCyle() {
        return cyle;
    }

    public void setCyle(Integer cyle) {
        this.cyle = cyle;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public Integer getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Integer horarioId) {
        this.horarioId = horarioId;
    }

    public String getSchName() {
        return schName;
    }

    public void setSchName(String schName) {
        this.schName = schName;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Timestamp getFimFaixaEntrada() {
        return fimFaixaEntrada;
    }

    public void setFimFaixaEntrada(Timestamp fimFaixaEntrada) {
        this.fimFaixaEntrada = fimFaixaEntrada;
    }

    public Timestamp getFimFaixaSaida() {
        return fimFaixaSaida;
    }

    public void setFimFaixaSaida(Timestamp fimFaixaSaida) {
        this.fimFaixaSaida = fimFaixaSaida;
    }

    public Timestamp getInicioFaixaEntrada() {
        return inicioFaixaEntrada;
    }

    public void setInicioFaixaEntrada(Timestamp inicioFaixaEntrada) {
        this.inicioFaixaEntrada = inicioFaixaEntrada;
    }

    public Timestamp getInicioFaixaSaida() {
        return inicioFaixaSaida;
    }

    public void setInicioFaixaSaida(Timestamp inicioFaixaSaida) {
        this.inicioFaixaSaida = inicioFaixaSaida;
    }

    public Timestamp getFimPrimeiroDescanso() {
        return fimPrimeiroDescanso;
    }

    public void setFimPrimeiroDescanso(Timestamp fimPrimeiroDescanso) {
        this.fimPrimeiroDescanso = fimPrimeiroDescanso;
    }

    public Timestamp getFimSegundoDescanso() {
        return fimSegundoDescanso;
    }

    public void setFimSegundoDescanso(Timestamp fimSegundoDescanso) {
        this.fimSegundoDescanso = fimSegundoDescanso;
    }

    public Timestamp getInicioPrimeiroDescanso() {
        return inicioPrimeiroDescanso;
    }

    public void setInicioPrimeiroDescanso(Timestamp inicioPrimeiroDescanso) {
        this.inicioPrimeiroDescanso = inicioPrimeiroDescanso;
    }

    public Timestamp getInicioInterjornada() {
        return inicioInterjornada;
    }

    public void setInicioInterjornada(Timestamp inicioInterjornada) {
        this.inicioInterjornada = inicioInterjornada;
    }

    public Timestamp getFimInterjornada() {
        return fimInterjornada;
    }

    public void setFimInterjornada(Timestamp fimInterjornada) {
        this.fimInterjornada = fimInterjornada;
    }
    
    public Timestamp getInicioSegundoDescanso() {
        return inicioSegundoDescanso;
    }

    public void setInicioSegundoDescanso(Timestamp inicioSegundoDescanso) {
        this.inicioSegundoDescanso = inicioSegundoDescanso;
    }
}
