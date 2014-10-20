/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroDeCategoriaAfastamento;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class CategoriaAfastamento implements Serializable {

    private Integer categoriaAfastamentoID;
    private String descCategoriaAfastamento;
    private String legenda;

    public CategoriaAfastamento(Integer categoriaAfastamentoID, String descCategoriaAfastamento, String legenda) {
        this.categoriaAfastamentoID = categoriaAfastamentoID;
        this.descCategoriaAfastamento = descCategoriaAfastamento;
        this.legenda = legenda;
    }

    public CategoriaAfastamento() {
    }

    public Integer getCategoriaAfastamentoID() {
        return categoriaAfastamentoID;
    }

    public void setCategoriaAfastamentoID(Integer categoriaAfastamentoID) {
        this.categoriaAfastamentoID = categoriaAfastamentoID;
    }

    public String getDescCategoriaAfastamento() {
        return descCategoriaAfastamento;
    }

    public void setDescCategoriaAfastamento(String descCategoriaAfastamento) {
        this.descCategoriaAfastamento = descCategoriaAfastamento;
    }

    public String getLegenda() {
        if (legenda == null) {
            return "X";
        }
        return legenda;
    }

    public String getSignificadoLegenda() {
        String saida = "";
        if (legenda != null) {
             saida = "(" + legenda + " - " + descCategoriaAfastamento + ")";
        }
        return saida;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }
}
