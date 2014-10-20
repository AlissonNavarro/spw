/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import java.io.Serializable;

import java.util.Date;

/**
 *
 * @author amsgama
 */

//teste2
public class Abono implements Serializable{

    private Date dataInicioAbono;
    private Date dataFimAbono;
    private Integer userId;
    private Integer cod;
    private Integer dateId;
    private String categoriaJustificativa;
    private String justificativa;
    private Integer diaAbono;


    public String getLegenda() {
        String cod_abono = cod.toString();
        String legenda = "(F" +cod_abono + " - " + categoriaJustificativa +")";
        return legenda;
    }
    public Date getDataFimAbono() {
        return dataFimAbono;
    }

    public void setDataFimAbono(Date dataFimAbono) {
        this.dataFimAbono = dataFimAbono;
    }

    public Date getDataInicioAbono() {
        return dataInicioAbono;
    }

    public void setDataInicioAbono(Date dataInicioAbono) {
        this.dataInicioAbono = dataInicioAbono;
    }

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDiaAbono() {
        return diaAbono;
    }

    public String getCategoriaJustificativa() {
        return categoriaJustificativa;
    }

    public void setCategoriaJustificativa(String categoriaJustificativa) {
        this.categoriaJustificativa = categoriaJustificativa;
    }
    
    public void setDiaAbono(Integer diaAbono) {
        this.diaAbono = diaAbono;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }
}
