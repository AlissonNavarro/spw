/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

/**
 *
 * @author Alexandre
 */
public class Faixa {

    public Margem entrada;
    public Boolean isEntradaObrigatoria;
    public Margem saida;

    public Margem getEntrada() {
        return entrada;
    }

    public void setEntrada(Margem entrada) {
        this.entrada = entrada;
    }

    public Margem getSaida() {
        return saida;
    }

    public void setSaida(Margem saida) {
        this.saida = saida;
    }

}
