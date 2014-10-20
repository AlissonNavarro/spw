/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cadastroVerba;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class Verba implements Serializable{

    private Integer cod_verba;
    private String descrecao;

    public Verba(){
    }

    public Verba(Integer cod_verba){
        this.cod_verba = cod_verba;
    }

    public Integer getCod_verba() {
        return cod_verba;
    }

    public void setCod_verba(Integer cod_verba) {
        this.cod_verba = cod_verba;
    }

    public String getDescrecao() {
        return descrecao;
    }

    public void setDescrecao(String descrecao) {
        this.descrecao = descrecao;
    }
}
