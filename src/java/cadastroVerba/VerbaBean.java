/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastroVerba;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
public class VerbaBean implements Serializable {

    private Verba empresa;
    private Verba adicionalNoturno;
    private Verba atrasos;
    private Verba atrasos_menor_uma_hora;
    private Verba atrasos_maior_uma_hora;
    private Verba feriadoCritico;
    private Verba dsr;
    private Verba faltas;
    

    public VerbaBean() {
        Banco b = new Banco();
        List<Integer> verbasList = b.getVerbas();
        if (!verbasList.isEmpty()) {
            empresa = new Verba(verbasList.get(0));
            adicionalNoturno = new Verba(verbasList.get(1));
            atrasos = new Verba(verbasList.get(2));
            atrasos_maior_uma_hora = new Verba(verbasList.get(3));
            atrasos_menor_uma_hora = new Verba(verbasList.get(4));
            feriadoCritico = new Verba(verbasList.get(5));
            dsr = new Verba(verbasList.get(6));
            faltas = new Verba(verbasList.get(7));
            
        }
    }

    public void salvar() {
        Banco b = new Banco();
        List<Integer> verbaList = new ArrayList<Integer>();
        verbaList.add(empresa.getCod_verba());
        verbaList.add(adicionalNoturno.getCod_verba());
        verbaList.add(atrasos.getCod_verba());
        verbaList.add(atrasos_maior_uma_hora.getCod_verba());
        verbaList.add(atrasos_menor_uma_hora.getCod_verba());
        verbaList.add(feriadoCritico.getCod_verba());
        verbaList.add(dsr.getCod_verba());
        verbaList.add(faltas.getCod_verba());
        
        Boolean sucesso = b.updateVerbas(verbaList);
        if (sucesso) {
            FacesMessage msgErro = new FacesMessage("Verbas atualizadas com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado! Favor tentar novamente ou contactar o administrador");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public Verba getAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(Verba adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public Verba getAtrasos() {
        return atrasos;
    }

    public void setAtrasos(Verba atrasos) {
        this.atrasos = atrasos;
    }

    public Verba getAtrasos_maior_uma_hora() {
        return atrasos_maior_uma_hora;
    }

    public void setAtrasos_maior_uma_hora(Verba atrasos_maior_uma_hora) {
        this.atrasos_maior_uma_hora = atrasos_maior_uma_hora;
    }

    public Verba getAtrasos_menor_uma_hora() {
        return atrasos_menor_uma_hora;
    }

    public void setAtrasos_menor_uma_hora(Verba atrasos_menor_uma_hora) {
        this.atrasos_menor_uma_hora = atrasos_menor_uma_hora;
    }

    public Verba getDsr() {
        return dsr;
    }

    public void setDsr(Verba dsr) {
        this.dsr = dsr;
    }

    public Verba getFaltas() {
        return faltas;
    }

    public void setFaltas(Verba faltas) {
        this.faltas = faltas;
    }

    public Verba getFeriadoCritico() {
        return feriadoCritico;
    }

    public void setFeriadoCritico(Verba feriadoCritico) {
        this.feriadoCritico = feriadoCritico;
    }

   public Verba getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Verba empresa) {
        this.empresa = empresa;
    }
}
