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
    private int num_jornada;
    private int tipo;
    private int ciclo;
    private boolean invalido;

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

    public int getNum_jornada() {
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public boolean getInvalido() {
        return invalido;
    }

    public void setInvalido(boolean invalido) {
        this.invalido = invalido;
    }
}