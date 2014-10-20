/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alexandre
 */
public class AlterarSenhaBean {

    private AlterarSenha alterarSenha;

    public AlterarSenhaBean() {
        alterarSenha = new AlterarSenha();
    }

    public void entrar() {
        Banco banco = new Banco();

        if (!alterarSenha.getLogin().equals("") && alterarSenha.getLogin() != null) {

            Usuario usuario;

            usuario = banco.getUsuarioByCPF(alterarSenha.getLogin());

            if (usuario.getLogin() != null) {
                DesEncrypter crypter = new DesEncrypter("aabbccaa");
                if (usuario.getSenha() != null) {
                    String senhaEncrypt = crypter.encrypt(alterarSenha.getSenhaAntiga());
                    if (usuario.getSenha().equals(senhaEncrypt)) {
                        if ((alterarSenha.getSenha().equals(alterarSenha.getConfirmacaoSenha()))) {
                            int[] senhaRestricoes = banco.consultaSenhasRestricoes();
                            
                            if (alterarSenha.getSenha().length() >= 6) { //   if (alterarSenha.getSenha().length() >= senhaRestricoes[0]) {
                                banco = new Banco();
                                DesEncrypter encrypter = new DesEncrypter("aabbccaa");
                                String senhaEncryter = encrypter.encrypt(alterarSenha.getSenha());
                                if (banco.mudarSenha(usuario.getSsn(), senhaEncryter)) {
                                    FacesMessage msgErro = new FacesMessage("Senha alterada com sucesso!");
                                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                                } else {
                                    FacesMessage msgErro = new FacesMessage("Falha em cadastrar a senha.");
                                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                                }
                            } else {
                                FacesMessage msgErro = new FacesMessage("A senha deve possuir no mínimo "+6+" dígitos!");//FacesMessage msgErro = new FacesMessage("A senha deve possuir no mínimo "+senhaRestricoes[0]+" dígitos!");
                                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                            }
                        } else {
                            FacesMessage msgErro = new FacesMessage("Erro ao confirmar senha!");
                            FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        }
                    } else {
                        FacesMessage msgErro = new FacesMessage("Senha Antiga Inválida");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    }
                } else {
                    FacesMessage msgErro = new FacesMessage("Senha antiga não cadastrada");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }

            } else {
                FacesMessage msgErro = new FacesMessage("Usuário Inválido");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Usuário Inválido");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public AlterarSenha getAlterarSenha() {
        return alterarSenha;
    }

    public void setAlterarSenha(AlterarSenha alterarSenha) {
        this.alterarSenha = alterarSenha;
    }
}
