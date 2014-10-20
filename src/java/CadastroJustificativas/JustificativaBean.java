/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJustificativas;

import Metodos.Metodos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
public class JustificativaBean implements Serializable {

    private List<Justificativa> justificativa;
    private Justificativa editJustificativa;
    private Justificativa novaJustificativa;
    private String teste;

    public JustificativaBean() {
        editJustificativa = new Justificativa();
        novaJustificativa = new Justificativa();
        justificativa = new ArrayList<Justificativa>();
        Banco banco = new Banco();
        justificativa = banco.consultaJustificativas();
    }

    public void showInserir() {
        novaJustificativa = new Justificativa();
    }

    public void inserir() {
        Banco banco = new Banco();
        boolean flag = banco.inserirNovaJustificativa(novaJustificativa);
        if (flag) {
            //Metodos.setLogInfo("Adicionar Justificativa - Nome: "+novaJustificativa.getJustificativaNome()+" Exclusivo de Administrador: "+(novaJustificativa.getSoAdministrador()?"Sim":"Não")+" Abono Total: "+(novaJustificativa.getIsTotal()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Justificativa inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da justificativa já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativa = new ArrayList<Justificativa>();
        banco = new Banco();
        justificativa = banco.consultaJustificativas();
    }

    public void showEditar() {
        editJustificativa = new Justificativa();
        Banco banco = new Banco();
        String justificativa_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("justificativa_id");
        editJustificativa = banco.consultaJustificativa(Integer.parseInt(justificativa_id));
    }

    public void editar() {
        Banco banco = new Banco();
        if (null != editJustificativa.getJustificativaNome()) {
            banco.editarJustificativa(editJustificativa);
            //Metodos.setLogInfo("Editar Justificativa - Nome: "+editJustificativa.getJustificativaNome()+" Exclusivo de Administrador: "+(editJustificativa.getSoAdministrador()?"Sim":"Não")+" Abono Total: "+(editJustificativa.getIsTotal()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Justificativa alterada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da justificativa não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        justificativa = banco.consultaJustificativas();
    }

    public void deletar() {
        String justificativa_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("justificativa_id");

        Banco banco = new Banco();
        Justificativa jusDel = banco.consultaJustificativa(Integer.parseInt(justificativa_id));
        banco = new Banco();
        boolean flag = banco.deletarJustificativa(Integer.parseInt(justificativa_id));

        if (flag) {
            FacesMessage msgErro = new FacesMessage("Justificativa  deletada com sucesso.");
            //Metodos.setLogInfo("Excluir Justificativa - Nome: "+jusDel.getJustificativaNome());
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("A justificativa não pode ser deletado por está sendo utilizada em algum abono.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativa = new ArrayList<Justificativa>();
        banco = new Banco();
        justificativa = banco.consultaJustificativas();
    }

    public List<Justificativa> getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(List<Justificativa> justificativa) {
        this.justificativa = justificativa;
    }

    public Justificativa getNovaJustificativa() {
        return novaJustificativa;
    }

    public void setNovaJustificativa(Justificativa novaJustificativa) {
        this.novaJustificativa = novaJustificativa;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public Justificativa getEditJustificativa() {
        return editJustificativa;
    }

    public void setEditJustificativa(Justificativa editJustificativa) {
        this.editJustificativa = editJustificativa;
    }
}
