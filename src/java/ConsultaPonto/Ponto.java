/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author amsgama
 */
public class Ponto implements Serializable {

    private Timestamp ponto;
    private Integer sensor;
    private Boolean isPreAssinalado;
    private Boolean isAbonado;
    private String justificativa;
    private Integer tipoRegistro;
    private String local;

    public Ponto() {
        isPreAssinalado = false;
        isAbonado = false;
        tipoRegistro = 0;
    }

    public Boolean getIsPreAssinalado() {
        return isPreAssinalado;
    }

    public void setIsPreAssinalado(Boolean isPreAssinalado) {
        this.isPreAssinalado = isPreAssinalado;
    }

    public Timestamp getPonto() {
        return ponto;
    }

    public void setPonto(Timestamp ponto) {
        this.ponto = ponto;
    }

    public Integer getSensor() {
        return sensor;
    }

    public void setSensor(Integer sensor) {
        this.sensor = sensor;
    }

    public Boolean getIsAbonado() {
        return isAbonado;
    }

    public void setIsAbonado(Boolean isAbonado) {
        this.isAbonado = isAbonado;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Integer getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Integer tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
    
}
