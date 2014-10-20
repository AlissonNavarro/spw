/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CadastroCronograma;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Amvboas
 */
public class Cronograma implements Serializable{
    Integer id_usuario;
    Integer id_jornada;
    String nomeJornada;
    Date inicio;
    Date fim;

    public Cronograma(Integer id_usuario,  Integer id_jornada, String nomeJornada, Date inicio, Date fim) {
        this.id_usuario = id_usuario;
        this.id_jornada = id_jornada;
        this.nomeJornada = nomeJornada;
        this.inicio = inicio;
        this.fim = fim;
    }

    Cronograma() {
        this.id_usuario = null;

        this.id_jornada = null;
        this.nomeJornada = null;
        this.inicio = null;
        this.fim = null;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Integer getId_jornada() {
        return id_jornada;
    }

    public void setId_jornada(Integer id_jornada) {
        this.id_jornada = id_jornada;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public String getNomeJornada() {
        return nomeJornada;
    }

    public void setNomeJornada(String nomeJornada) {
        this.nomeJornada = nomeJornada;
    }

  



}
