package Administracao;

import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class PreparaBancoVazioBean implements Serializable {

    public void preparar() throws SQLException, ClassNotFoundException {
        Boolean flag = false;
        PreparaBanco banco = new PreparaBanco();
        String msg = "Foram preenchidas as tabelas de: ";
        String msgErro = "Ocorram erros nas tabelas: ";
        flag = banco.prepareVerba();
        if (flag) {
            msg += "Verba; ";
        } else {
            msgErro += "Verba; ";
        }
        flag = banco.prepareRegime();
        if (flag) {
            msg += "Regime; ";
        } else {
            msgErro += "Regime; ";
        }
        flag = banco.prepareTipoHoraExtra();
        if (flag) {
            msg += "TipoHoraExtra; ";
        } else {
            msgErro += "TipoHoraExtra; ";
        }
        flag = banco.preparePerfil();
        if (flag) {
            msg += "Perfil; ";
        } else {
            msgErro += "Perfil; ";
        }
        flag = banco.prepareCargo();
        if (flag) {
            msg += "Cargo; ";
        } else {
            msgErro += "Cargo; ";
        }
        /*flag = banco.prepareDepartamento();
        if (flag) {
            msg += "Departaments; ";
        } else {
            msgErro += "Departaments; ";
        }*/
        flag = banco.prepareusuario();
        if (flag) {
            msg += "Usuário; ";
        } else {
            msgErro += "Usuário; ";
        }

        flag = banco.preparaRepTipo();
        if (flag) {
            msg += "RepTipo; ";
        } else {
            msgErro += "RepTipo; ";
        }
        
        flag = banco.prepareConfig();
        if (flag) {
            msg += "Config; ";
        } else {
            msgErro += "Config; ";
        }

        FacesMessage mensagem = new FacesMessage("Banco preparado para uso! " + msg + " " + msgErro);
        FacesContext.getCurrentInstance().addMessage(null, mensagem);
    }
}
