/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Usuario;

/**
 *
 * @author Alexandre
 */
public class NovoUsuario {

    String senha;
    String confirmacaoSenha;

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
