package beans;

import manageBean.VerbaMB;
import entidades.Verba;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class VerbaBean implements Serializable {

    private Verba verba;

    public VerbaBean() {
        VerbaMB b = new VerbaMB();
        verba = b.consultaVerba();
    }

    public void salvar() {
        VerbaMB b = new VerbaMB();        
        boolean sucesso = b.alterar(verba);
        if (sucesso) {
            FacesMessage msgErro = new FacesMessage("Verbas atualizadas com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Erro inesperado! Favor tentar novamente ou contactar o administrador");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        verba = b.consultaVerba();
    }

    public Verba getVerba() {
        return verba;
    }

    public void setVerba(Verba verba) {
        this.verba = verba;
    }

}
