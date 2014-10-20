/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHoraExtra;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Alexandre
 */
public class DetalheGratificacao implements Serializable {

    private Integer dia;
    private String diaStr;
    private String inicio;
    private String fim;
    private String nome;
    private Float valor;
    private String verba;
    private Integer cod_gratificacao;
    private HashMap<Integer, String> diaIntDiaStrHashMap;

    public DetalheGratificacao(Integer dia, String inicio, String fim, Integer cod_gratificacao) {
        diaIntDiaStrHashMap = new HashMap<Integer, String>();
        this.dia = dia;
        this.inicio = inicio;
        this.fim = fim;
        this.cod_gratificacao = cod_gratificacao;
        mapearDiaIntDiaString();
    }

    public DetalheGratificacao(String nome, Integer dia,Float valor,String verba, String inicio, String fim, Integer cod_gratificacao) {
        this.nome = nome;
        this.dia = dia;
        this.valor = valor;
        this.verba = verba;
        this.inicio = inicio;
        this.fim = fim;
        this.cod_gratificacao = cod_gratificacao;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public HashMap<Integer, String> getDiaIntDiaStrHashMap() {
        return diaIntDiaStrHashMap;
    }

    public void setDiaIntDiaStrHashMap(HashMap<Integer, String> diaIntDiaStrHashMap) {
        this.diaIntDiaStrHashMap = diaIntDiaStrHashMap;
    }

    public String getDiaStr() {
        diaStr = diaIntDiaStrHashMap.get(dia);
        return diaStr;
    }

    public void setDiaStr(String diaStr) {
        this.diaStr = diaStr;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public Integer getCod_gratificacao() {
        return cod_gratificacao;
    }

    public void setCod_gratificacao(Integer cod_gratificacao) {
        this.cod_gratificacao = cod_gratificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private void mapearDiaIntDiaString() {
        diaIntDiaStrHashMap.put(1, "Domingo");
        diaIntDiaStrHashMap.put(2, "Segunda-feira");
        diaIntDiaStrHashMap.put(3, "Terça-feira");
        diaIntDiaStrHashMap.put(4, "Quarta-feira");
        diaIntDiaStrHashMap.put(5, "Quinta-feira");
        diaIntDiaStrHashMap.put(6, "Sexta-feira");
        diaIntDiaStrHashMap.put(7, "Sábado");
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getVerba() {
        return verba;
    }

    public void setVerba(String verba) {
        this.verba = verba;
    }
}
