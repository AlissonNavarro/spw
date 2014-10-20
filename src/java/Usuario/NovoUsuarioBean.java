/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import RelatorioMensal.UploadFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.OutputStream;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 *
 * @author Alexandre
 */
public class NovoUsuarioBean {

    private NovoUsuario novoUsuario;
    private UsuarioBean usuarioBean;
    //Restrições de senha
    //Por padrão tamanho 0 significa que não precisa da restrição
    private int needMaiusculas = 0;
    private int needSize = 6;
    private int needNumeros = 0;
    private int needSimbolos = 0;
    private static char[] specialCh = {'!', '@', '#', '$', '%', '&', '*'};

    public NovoUsuarioBean() {
        novoUsuario = new NovoUsuario();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        /*
         Banco banco = new Banco();
         int[] rest = banco.consultaSenhasRestricoes();
         needSize = rest[0];
         needMaiusculas = rest[1];
         needNumeros = rest[2];
         needSimbolos = rest[3];
         */
    }

    public String entrar() {
        String retorno = "";
        int senhaResultado = verificarSenha(novoUsuario.senha);
        if (senhaResultado == 0) {
            if (novoUsuario.senha.equals(novoUsuario.confirmacaoSenha)) {
                Banco banco = new Banco();
                DesEncrypter encrypter = new DesEncrypter("aabbccaa");
                String senhaEncryter = encrypter.encrypt(novoUsuario.senha);
                banco.mudarSenha(usuarioBean.getUsuario().getSsn(), senhaEncryter);
                FacesMessage msgErro = new FacesMessage("Senha cadastrada com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                retorno = "login";
            } else {
                FacesMessage msgErro = new FacesMessage("As senhas digitadas não conferem!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else if (senhaResultado == 4) {
            FacesMessage msgErro = new FacesMessage("A senha deve possuir " + needSize + " dígitos!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (senhaResultado == 3) {
            FacesMessage msgErro = new FacesMessage("A senha deve conter " + needSimbolos + " caracters especiais! (!, @, #, $, %, &, *)");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (senhaResultado == 2) {
            FacesMessage msgErro = new FacesMessage("A senha deve conter " + needNumeros + " números!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (senhaResultado == 1) {
            FacesMessage msgErro = new FacesMessage("A senha deve conter " + needMaiusculas + " letras maiúsculas!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        return retorno;
    }
    //Quando estiver ok = 0
    //Retrição de letras maiusculas = 1
    //Retrição de numeros = 2
    //Retrição de simbolos = 3
    //Retrição de tamanho = 4

    private int verificarSenha(String senha) {
        if (senha.length() < needSize) {
            return 4;
        }

        int countUppers = 0;
        for (int i = 0; i < senha.length(); i++) {
            if (Character.isUpperCase(senha.charAt(i))) {
                countUppers++;
            }
        }
        if (countUppers < needMaiusculas) {
            return 1;
        }

        int countNumbers = 0;
        for (int i = 0; i < senha.length(); i++) {
            if (Character.isDigit(senha.charAt(i))) {
                countNumbers++;
            }
        }
        if (countNumbers < needNumeros) {
            return 2;
        }

        int countSimbolo = 0;
        for (int i = 0; i < senha.length(); i++) {
            for (int j = 0; j < specialCh.length; j++) {
                if (senha.charAt(i) == specialCh[j]) {
                    countSimbolo++;
                }
            }
        }
        if (countSimbolo < needSimbolos) {
            return 3;
        }
        return 0;
    }

    public void salvarAlteracoesSenha() {
        Banco banco = new Banco();
        banco.alteraSenhasRestricoes(needSize, needMaiusculas, needNumeros, needSimbolos);
    }

    public int getNeedMaiusculas() {
        return needMaiusculas;
    }

    public void setNeedMaiusculas(int needMaiusculas) {
        this.needMaiusculas = needMaiusculas;
    }

    public int getNeedNumeros() {
        return needNumeros;
    }

    public void setNeedNumeros(int needNumeros) {
        this.needNumeros = needNumeros;
    }

    public int getNeedSimbolos() {
        return needSimbolos;
    }

    public void setNeedSimbolos(int needSimbolos) {
        this.needSimbolos = needSimbolos;
    }

    public int getNeedSize() {
        return needSize;
    }

    public void setNeedSize(int needSize) {
        this.needSize = needSize;
    }

    public NovoUsuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(NovoUsuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }
}
