/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import java.io.Serializable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Amvboas
 */
public class Turno implements Serializable {

    //private Long rowid;
    public Boolean hasVirada;
    private Integer id;
    private Boolean marked;
    private String nome;
    private Timestamp entrada;
    private Timestamp saida;
    private Timestamp inicioFaixaEntrada;
    private Timestamp fimFaixaEntrada;
    private Timestamp inicioFaixaSaida;
    private Timestamp fimFaixaSaida;
    private String stringEntradaSaida;

    public Turno(int id, String Nome, Timestamp entrada, Timestamp saida, Boolean hasVirada, Timestamp inicioFaixaEntrada, Timestamp fimFaixaEntrada, Timestamp inicioFaixaSaida, Timestamp fimFaixaSaida) {
        this.id = id;
        this.marked = false;
        this.nome = Nome;
        this.entrada = entrada;
        this.saida = saida;
        this.hasVirada = hasVirada;
        this.inicioFaixaEntrada = inicioFaixaEntrada;
        this.fimFaixaEntrada = fimFaixaEntrada;
        this.inicioFaixaSaida = inicioFaixaSaida;
        this.fimFaixaSaida = fimFaixaSaida;
        constroiStringEntradaSaida();
    }

    public void change() {
        marked = !marked;

    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String Nome) {
        this.nome = Nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getEntrada() {
        return entrada;
    }

    public void setEntrada(Timestamp entrada) {
        this.entrada = entrada;
    }

    public Timestamp getSaida() {
        return saida;
    }

    public void setSaida(Timestamp saida) {
        this.saida = saida;
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

    public String getStringEntradaSaida() {
        return stringEntradaSaida;
    }

    public void setStringEntradaSaida(String stringEntradaSaida) {
        this.stringEntradaSaida = stringEntradaSaida;
    }

    private void constroiStringEntradaSaida() {

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        String in = sdfHora.format(entrada.getTime());
        String out = sdfHora.format(saida.getTime());
        stringEntradaSaida = "(" + in + " - " + out + ") ";

    }
}
