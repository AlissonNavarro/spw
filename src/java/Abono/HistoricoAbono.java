/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class HistoricoAbono implements Serializable {

    private Integer cod_registro_abono;
    private Integer cod_solicitacao;
    private Integer cod_funcionario;
    private Integer cod_responsavel;
    private String responsavel;
    private String funcionario;
    private String abono;
    private String categoriaJustificativa;
    private Date diaAbono;
    private String justificativa;
    private String solicitacao;
    private Boolean marked;
    private String status;
    private boolean negado;
    
    public HistoricoAbono() {
        marked = new Boolean(false);
    }

    public Integer getCod_registro_abono() {
        return cod_registro_abono;
    }

    public void setCod_registro_abono(Integer cod_registro_abono) {
        this.cod_registro_abono = cod_registro_abono;
    }

    public Integer getCod_solicitacao() {
        return cod_solicitacao;
    }

    public void setCod_solicitacao(Integer cod_solicitacao) {
        this.cod_solicitacao = cod_solicitacao;
    }

    public String getCategoriaJustificativa() {
        return categoriaJustificativa;
    }

    public void setCategoriaJustificativa(String categoriaJustificativa) {
        this.categoriaJustificativa = categoriaJustificativa;
    }

    public Date getDiaAbono() {
        return diaAbono;
    }

    public void setDiaAbono(Date diaAbono) {
        this.diaAbono = diaAbono;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getAbono() {
        return abono;
    }

    public void setAbono(String abono) {
        this.abono = abono;
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

    public String getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(String solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Integer getCod_responsavel() {
        return cod_responsavel;
    }

    public void setCod_responsavel(Integer cod_responsavel) {
        this.cod_responsavel = cod_responsavel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isNegado() {
        negado = false;
        if (getStatus().length() != 0) {
            if (getStatus().equals("Negado    ")) {
                negado = true;
            } else {
                negado = false;
            }
        }
        return negado;
    }

}
