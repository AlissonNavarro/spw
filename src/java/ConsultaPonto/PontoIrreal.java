/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import RelatorioMensal.*;
import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class PontoIrreal implements Serializable {


    private String hora;
    private String tipo;
    private String motivo;

    public PontoIrreal(String hora, String tipo, String motivo) {
        this.hora = hora;
        this.tipo = tipo;
        this.motivo = motivo;
    }

    public PontoIrreal() {
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
