/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import Metodos.Metodos;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Permissao implements Serializable {

    private Integer userid;
    private String nomeRegime;
    private String badgerNumber;
    private String cpf;
    private String nome;
    private String departamento;
    private Integer dept_permissao;
    private Integer cod_regime;
    private Integer perfil;
    /*
    public Permissao(Integer userid, String badgerNumber, String cpf, String nome, String departamento, String dept_permissao, Integer cod_regime) {
    this.userid = userid;
    this.badgerNumber = badgerNumber;
    this.cpf = cpf;
    this.nome = nome;
    this.departamento = departamento;
    this.dept_permissao = dept_permissao;
    this.cod_regime = cod_regime;
    }
     *
     */

    public Permissao(Integer userid, String badgerNumber, String cpf, String nome, String departamento, Integer dept_permissao, Integer cod_regime, Integer perfil, String nomeRegime) {
        this.userid = userid;
        this.badgerNumber = badgerNumber;
        this.cpf = cpf;
        this.nome = nome;
        this.departamento = departamento;
        this.dept_permissao = dept_permissao;
        this.cod_regime = cod_regime;
        this.perfil = perfil;
        this.nomeRegime = nomeRegime;
    }

    public void atualizarPermissao() {
        Banco banco = new Banco();
        String valor = dept_permissao.toString();
        String permissao = banco.consultaPermissao(dept_permissao);
        banco.updatePermissao(userid, valor);
        FacesMessage msgErro = new FacesMessage("Permissão alterada com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
        permissao = (permissao == null)? "Sem Perfil" : permissao;
        Metodos.setLogInfo("Alteração de Permissão - Funcionário: " + getNome() + " Permissão: " + permissao);
    }

    public void atualizarPerfil() {
        Banco banco = new Banco();
        String perfilStr = banco.consultaPerfil(perfil);
        String valor = perfil.toString();
        banco.updatePerfil(userid, valor);
        FacesMessage msgErro = new FacesMessage("Perfil alterado com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
        perfilStr = (perfilStr == null)? "Sem Perfil" : perfilStr;
        Metodos.setLogInfo("Alteração de Perfil - Funcionário: " + getNome() + " Perfil: " + perfilStr);
    }

    public void atualizarRegime() {
        Banco banco = new Banco();
        String regime = banco.consultaRegime(cod_regime);
        Boolean teste = banco.updateRegime(userid, cod_regime);
        if (teste) {
            FacesMessage msgErro = new FacesMessage("Regime alterado com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            regime = regime == null ? "Sem regime" : regime;
            Metodos.setLogInfo("Alteração de Regime - Funcionário: " + getNome() + " Regime: " + regime);
        }
    }

    public void zerarSenha() {
        Banco banco = new Banco();
        banco.zerarSenhaUsuario(userid);
        FacesMessage msgErro = new FacesMessage("Senha formatada com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public String getBadgerNumber() {
        return badgerNumber;
    }

    public void setBadgerNumber(String badgerNumber) {
        this.badgerNumber = badgerNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getDept_permissao() {
        return dept_permissao;
    }

    public void setDept_permissao(Integer dept_permissao) {
        this.dept_permissao = dept_permissao;
    }

    public Integer getCod_regime() {
        return cod_regime;
    }

    public void setCod_regime(Integer cod_regime) {
        this.cod_regime = cod_regime;
    }

    public Integer getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }

    public String getNomeRegime() {
        return nomeRegime;
    }

    public void setNomeRegime(String nomeRegime) {
        this.nomeRegime = nomeRegime;
    }
}
