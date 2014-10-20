/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroHoraExtra;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class Justificativa implements Serializable{

    private Integer cod_justificativa;
    private Integer cod_regime;
    private String descricao;

    public Justificativa(Integer cod_justificativa, Integer cod_regime, String descricao) {
        this.cod_justificativa = cod_justificativa;
        this.cod_regime = cod_regime;
        this.descricao = descricao;
    }

    public Justificativa(Integer cod_justificativa, String descricao) {
        this.cod_justificativa = cod_justificativa;
        this.descricao = descricao;
    }

    public Justificativa() {
    }

    public Integer getCod_justificativa() {
        return cod_justificativa;
    }

    public void setCod_justificativa(Integer cod_justificativa) {
        this.cod_justificativa = cod_justificativa;
    }

    public Integer getCod_regime() {
        return cod_regime;
    }

    public void setCod_regime(Integer cod_regime) {
        this.cod_regime = cod_regime;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
