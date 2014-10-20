/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RelatorioMensal;

import java.io.Serializable;

/**
 *
 * @author Alexandre
 */
public class RelatorioLotacao implements Serializable{

    private String cpf;
    private String nome;
    private String departamento;
    private String escala;

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String Departamento) {
        this.departamento = Departamento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEscala() {
        return escala;
    }

    public void setEscala(String escala) {
        this.escala = escala;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
