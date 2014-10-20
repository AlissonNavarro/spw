/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class Escala implements Serializable{

    private Integer diaMes;
    private Date dia;
    private List<Integer> schClassIDList;
    private DescolamentoTemporario deslT;
    
    public Escala () {
        
    }

    public Escala(Integer diaMes,List<Integer> schClassIDList, Date dia) {
        this.diaMes = diaMes;
        this.schClassIDList = schClassIDList;
        this.dia = dia;
        this.deslT = null;
    }

    public Integer getDiaMes() {
        return diaMes;
    }

    public void setDiaMes(Integer diaMes) {
        this.diaMes = diaMes;
    }

    public List<Integer> getSchClassIDList() {
        return schClassIDList;
    }

    public void setSchClassIDList(List<Integer> schClassIDList) {
        this.schClassIDList = schClassIDList;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public DescolamentoTemporario getDeslT() {
        return deslT;
    }

    public void setDeslT(DescolamentoTemporario deslT) {
        this.deslT = deslT;
    }
}