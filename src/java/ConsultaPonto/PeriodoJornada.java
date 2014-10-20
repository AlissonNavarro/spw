/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class PeriodoJornada {

    private Date inicio;
    private Date fim;
    private Date inicioJornada;
    private Integer num_jornada;
    private Integer tipo;
    private Integer ciclo;
    private Boolean invalido;

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Integer getNum_jornada() {
        return num_jornada;
    }

    public void setNum_jornada(Integer num_jornada) {
        this.num_jornada = num_jornada;
    }

    public String toSring(){
        return num_jornada+" "+inicio+" "+fim;
    }

    public Date getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(Date inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public Boolean getInvalido() {
        return invalido;
    }

    public void setInvalido(Boolean invalido) {
        this.invalido = invalido;
    }
}
