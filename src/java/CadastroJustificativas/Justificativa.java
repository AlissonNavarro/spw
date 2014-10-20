/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJustificativas;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class Justificativa implements Serializable {

    private Integer justificativaID;
    private String justificativaNome;
    private Boolean soAdministrador;
    private Boolean isTotal;
    private Boolean isDescricaoObrigatoria;

    public Boolean getIsDescricaoObrigatoria() {
        return isDescricaoObrigatoria;
    }

    public void setIsDescricaoObrigatoria(Boolean isDescricaoObrigatoria) {
        this.isDescricaoObrigatoria = isDescricaoObrigatoria;
    }

    public Integer getJustificativaID() {
        return justificativaID;
    }

    public void setJustificativaID(Integer justificativaID) {
        this.justificativaID = justificativaID;
    }
    
    public String getJustificativaNome() {
        return justificativaNome;
    }

    public void setJustificativaNome(String justificativaNome) {
        this.justificativaNome = justificativaNome;
    }

    public Boolean getSoAdministrador() {
        return soAdministrador;
    }

    public void setSoAdministrador(Boolean soAdministrador) {
        this.soAdministrador = soAdministrador;
    }

    public Boolean getIsTotal() {
        return isTotal;
    }

    public void setIsTotal(Boolean isTotal) {
        this.isTotal = isTotal;
    }

}
