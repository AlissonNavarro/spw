/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Metodos.Metodos;
import Usuario.DesEncrypter;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 *
 * @author Alexandre
 */
public class ConfiguracoesBean implements Serializable {

    private String titulo;
    private String senhaAtual;
    private String senhaNova;
    private String senhaNovaConfirmacao;
    private String abaCorrente;
    
    //Configurações do sistema
    private boolean checkWithPIN;

    public ConfiguracoesBean() {
        titulo = Metodos.getTitulo();
        Banco b = new Banco();
        checkWithPIN = b.CheckWithPIN();
        b = null;
        //abaCorrente = "tabUsuarios1";
        System.out.println("aba: "+abaCorrente);
        
    }

    public void CorrigePIN() {
        Banco b = new Banco();
        b.CorrigePIN(checkWithPIN);
    }
    
    public void SaveDefinitions(){
        Banco b = new Banco();
        b.SaveConfig(checkWithPIN);
    }
    
    public void editarTitulo() {
        FacesMessage msgErro = new FacesMessage("Título alterado com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
      
        Metodos.setTitulo(titulo);
        //Metodos.setLogInfo("Alteração Título Relatórios - Título: "+titulo);
        senhaAtual = "";
    }

    public void alterarSenha() {

        DesEncrypter encrypter = new DesEncrypter("aabbccab");
        String senha = Metodos.getSenhaAdministrador();
        String senhaDesencriptada = encrypter.decrypt(senha);
        if (senhaAtual.equals(senhaDesencriptada)) {
            if (senhaNova.equals(senhaNovaConfirmacao)) {
                if (!senhaNova.equals("")) {
                    String senhaAtualEncriptada = encrypter.encrypt(senhaNova);
                    Metodos.setSenha(senhaAtualEncriptada);
                    FacesMessage msgErro = new FacesMessage("Senha alterada com sucesso!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    FacesMessage msgErro = new FacesMessage("A senha não pode está vazia!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            } else {
                FacesMessage msgErro = new FacesMessage("Confirmação de senha inválida. Tente novamente!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Senha atual inválida!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getSenhaNovaConfirmacao() {
        return senhaNovaConfirmacao;
    }

    public void setSenhaNovaConfirmacao(String senhaNovaConfirmacao) {
        this.senhaNovaConfirmacao = senhaNovaConfirmacao;
    }

    public boolean getCheckWithPIN() {
        return checkWithPIN;
    }

    public void setCheckWithPIN(boolean checkWithPIN) {
        this.checkWithPIN = checkWithPIN;
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }
    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
        System.out.println("aba: "+abaCorrente);
    }
}
