/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class Feriado implements Serializable{

    private String nome;
    private Integer diaFeriado;
    private Date dia;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Feriado(String nome, Integer diaFeriado, Date dia) {
        this.nome = nome;
        this.diaFeriado = diaFeriado;
        this.dia = dia;
    }   

    public Integer getDiaFeriado() {
        return diaFeriado;
    }

    public void setDiaFeriado(Integer diaFeriado) {
        this.diaFeriado = diaFeriado;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }
}
