/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Afastamento;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class CategoriaAfastamento implements Serializable{

    private Integer cod_afastamento;
    private String desc_Afastamento;

    public Integer getCod_afastamento() {
        return cod_afastamento;
    }

    public void setCod_afastamento(Integer cod_afastamento) {
        this.cod_afastamento = cod_afastamento;
    }

    public String getDesc_Afastamento() {
        return desc_Afastamento;
    }

    public void setDesc_Afastamento(String desc_Afastamento) {
        this.desc_Afastamento = desc_Afastamento;
    }

}
