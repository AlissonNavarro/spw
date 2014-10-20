/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class RelatorioResumo implements Serializable {

    private Integer userid;
    private String matricula;
    private String nome;
    private String departamento;
    private String cpf;
    private String horaPrevista;
    private String horaTrabalhada;

    public RelatorioResumo(Integer userid, String nome, String departamento, String cpf) {
        this.userid = userid;
        this.nome = nome;
        this.departamento = departamento;
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return userid + "   " + nome + "   " + departamento + "   " + cpf + "   " + horaPrevista + "   " + horaTrabalhada + "\n";
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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

    public String getHoraPrevista() {
        return horaPrevista;
    }

    public void setHoraPrevista(String horaPrevista) {
        this.horaPrevista = horaPrevista;
    }

    public String getHoraTrabalhada() {
        return horaTrabalhada;
    }

    public void setHoraTrabalhada(String horaTrabalhada) {
        this.horaTrabalhada = horaTrabalhada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
