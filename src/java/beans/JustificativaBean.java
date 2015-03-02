package beans;

import manageBean.JustificativaMB;
import entidades.Justificativa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class JustificativaBean implements Serializable {

    private List<Justificativa> justificativa;
    private Justificativa editJustificativa;
    private Justificativa novaJustificativa;

    public JustificativaBean() {
        editJustificativa = new Justificativa();
        novaJustificativa = new Justificativa();
        justificativa = new ArrayList<Justificativa>();
        JustificativaMB banco = new JustificativaMB();
        justificativa = banco.consultaJustificativas();
    }

    public void showInserir() {
        novaJustificativa = new Justificativa();
    }

    public void inserir() {
        JustificativaMB banco = new JustificativaMB();
        boolean flag = banco.inserir(novaJustificativa);
        if (flag) {
            //Metodos.setLogInfo("Adicionar Justificativa - Nome: "+novaJustificativa.getJustificativaNome()+" Exclusivo de Administrador: "+(novaJustificativa.getSoAdministrador()?"Sim":"Não")+" Abono Total: "+(novaJustificativa.getIsTotal()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Justificativa inserida com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da justificativa já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativa = banco.consultaJustificativas();
    }

    public void showEditar() {
        editJustificativa = new Justificativa();
        JustificativaMB banco = new JustificativaMB();
        String justificativa_id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("justificativa_id");
        editJustificativa = banco.consultaJustificativa(Integer.parseInt(justificativa_id));
    }

    public void editar() {
        JustificativaMB banco = new JustificativaMB();
        if (null != editJustificativa.getJustificativaNome()) {
            banco.alterar(editJustificativa);
            //Metodos.setLogInfo("Editar Justificativa - Nome: "+editJustificativa.getJustificativaNome()+" Exclusivo de Administrador: "+(editJustificativa.getSoAdministrador()?"Sim":"Não")+" Abono Total: "+(editJustificativa.getIsTotal()?"Sim":"Não"));
            FacesMessage msgErro = new FacesMessage("Justificativa alterada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("O nome da justificativa não pode estar vazio!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        justificativa = banco.consultaJustificativas();
    }

    public void deletar() {
        String justificativaId = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("justificativa_id");
        JustificativaMB banco = new JustificativaMB();
        boolean flag = banco.excluir(Integer.parseInt(justificativaId));
        if (flag) {
            FacesMessage msgErro = new FacesMessage("Justificativa  deletada com sucesso.");
            //Metodos.setLogInfo("Excluir Justificativa - Nome: "+jusDel.getJustificativaNome());
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("A justificativa não pode ser deletado por está sendo utilizada em algum abono.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
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

    public Justificativa getEditJustificativa() {
        return editJustificativa;
    }

    public void setEditJustificativa(Justificativa editJustificativa) {
        this.editJustificativa = editJustificativa;
    }
}
