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
public class DetalheHoraExtra implements Serializable {

    private Integer dia;
    private String diaStr;
    private Float inicio;
    private Boolean isDiaInteiro;
    private Integer cod_tipo;
    private HashMap<Integer, String> diaIntDiaStrHashMap;

    public DetalheHoraExtra(Integer cod_tipo, Integer dia, Float inicio, Boolean isDiaInteiro) {
        diaIntDiaStrHashMap = new HashMap<Integer, String>();
        this.cod_tipo = cod_tipo;
        this.dia = dia;
        this.inicio = inicio;
        this.isDiaInteiro = isDiaInteiro;
        mapearDiaIntDiaString();
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getDiaStr() {
        diaStr = diaIntDiaStrHashMap.get(dia);
        return diaStr;
    }

    public void setDiaStr(String diaStr) {
        this.diaStr = diaStr;
    }

    public Float getInicio() {
        return inicio;
    }

    public void setInicio(Float inicio) {
        this.inicio = inicio;
    }
    
    public Boolean getIsDiaInteiro() {
        return isDiaInteiro;
    }

    public void setIsDiaInteiro(Boolean isDiaInteiro) {
        this.isDiaInteiro = isDiaInteiro;
    }

    public Integer getCod_tipo() {
        return cod_tipo;
    }

    public void setCod_tipo(Integer cod_tipo) {
        this.cod_tipo = cod_tipo;
    }

    private void mapearDiaIntDiaString(){
        diaIntDiaStrHashMap.put(1, "Segunda-feira");
        diaIntDiaStrHashMap.put(2, "Terça-feira");
        diaIntDiaStrHashMap.put(3, "Quarta-feira");
        diaIntDiaStrHashMap.put(4, "Quinta-feira");
        diaIntDiaStrHashMap.put(5, "Sexta-feira");
        diaIntDiaStrHashMap.put(6, "Sábado");
        diaIntDiaStrHashMap.put(7, "Domingo");
        diaIntDiaStrHashMap.put(8, "Feriado");
    }
}
