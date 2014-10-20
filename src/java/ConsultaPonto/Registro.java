/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;

/**
 *
 * @author Alexandre
 */
public class Registro implements Serializable{

    private String registro;
    private boolean isAbonado;
    private boolean isTotal;

    public Registro() {
        registro = "";
        isAbonado = false;
        isTotal = false;
    }

    public boolean getIsAbonado() {
        return isAbonado;
    }

    public void setIsAbonado(boolean isAbonado) {
        this.isAbonado = isAbonado;
    }

    public boolean getIsTotal() {
        return isTotal;
    }

    public void setIsTotal(boolean isTotal) {
        this.isTotal = isTotal;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
