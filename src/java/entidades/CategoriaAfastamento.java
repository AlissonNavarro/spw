package entidades;

import java.io.Serializable;

public class CategoriaAfastamento implements Serializable {

    private int id;
    private String descricao;
    private String legenda;

    public CategoriaAfastamento() {
    }

    public CategoriaAfastamento(int id, String descricao, String legenda) {
        this.id = id;
        this.descricao = descricao;
        this.legenda = legenda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
            saida = "(" + legenda + " - " + descricao + ")";
        }
        return saida;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }
}
