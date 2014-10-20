/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author amsgama
 */
public class RegistroAdicionado implements Serializable{

    private String nome;
    private Timestamp checkTime;   
    private String responsavel;
    private Integer cod_solicitacao;
    private String categoriaJustificativa;
    private String justificativa;
    private Timestamp diaAbono;
    private Boolean isTotal;
    private Integer tipoRegistro;

    public RegistroAdicionado(String nome, Timestamp checkTime, String responsavel, Integer cod_solicitacao,
            String categoriaJustificativa, String justificativa, Timestamp diaAbono, Boolean isTotal, Integer tipoRegistro) {
        this.nome = nome;
        this.checkTime = checkTime;
        this.responsavel = responsavel;
        this.cod_solicitacao = cod_solicitacao;
        this.categoriaJustificativa = categoriaJustificativa;
        this.justificativa = justificativa;
        this.diaAbono = diaAbono;
        this.isTotal = isTotal;
        this.tipoRegistro = tipoRegistro;
    }

    public RegistroAdicionado() {
        tipoRegistro = 0;
    }

    public String getCategoriaJustificativa() {
        return categoriaJustificativa;
    }

    public void setCategoriaJustificativa(String categoriaJustificativa) {
        this.categoriaJustificativa = categoriaJustificativa;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCod_solicitacao() {
        return cod_solicitacao;
    }

    public void setCod_solicitacao(Integer cod_solicitacao) {
        this.cod_solicitacao = cod_solicitacao;
    }

    public Timestamp getDiaAbono() {
        return diaAbono;
    }

    public void setDiaAbono(Timestamp diaAbono) {
        this.diaAbono = diaAbono;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getIsTotal() {
        return isTotal;
    }

    public void setIsTotal(Boolean isTotal) {
        this.isTotal = isTotal;
    }

    public Integer getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Integer tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}
