package entidades;

import java.io.Serializable;

public class Justificativa implements Serializable {

    private int justificativaID;
    private String justificativaNome;
    private boolean soAdministrador;
    private boolean isTotal;
    private boolean isDescricaoObrigatoria;

    public boolean getIsDescricaoObrigatoria() {
        return isDescricaoObrigatoria;
    }

    public void setIsDescricaoObrigatoria(boolean isDescricaoObrigatoria) {
        this.isDescricaoObrigatoria = isDescricaoObrigatoria;
    }

    public int getJustificativaID() {
        return justificativaID;
    }

    public void setJustificativaID(int justificativaID) {
        this.justificativaID = justificativaID;
    }
    
    public String getJustificativaNome() {
        return justificativaNome;
    }

    public void setJustificativaNome(String justificativaNome) {
        this.justificativaNome = justificativaNome;
    }

    public boolean getSoAdministrador() {
        return soAdministrador;
    }

    public void setSoAdministrador(boolean soAdministrador) {
        this.soAdministrador = soAdministrador;
    }

    public boolean getIsTotal() {
        return isTotal;
    }

    public void setIsTotal(boolean isTotal) {
        this.isTotal = isTotal;
    }

}
