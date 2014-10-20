/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

/**
 *
 * @author Alexandre
 */
public class Margem {

    private String margemInferior; 
    private String margemSuperior;

    public String getMargemEntrada() {
        return margemInferior;
    }

    public void setMargemEntrada(String margemEntrada) {
        this.margemInferior = margemEntrada;
    }

    public String getMargemSaida() {
        return margemSuperior;
    }

    public void setMargemSaida(String margemSaida) {
        this.margemSuperior = margemSaida;
    }

}
