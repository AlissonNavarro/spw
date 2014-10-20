/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RelatorioMensal;

/**
 *
 * @author Alexandre
 */
public class RelatorioPortaria1510CabecalhoResumo {

    private String nome;
    private String cpf;
    private String matricula;
    private String departamento;

    public RelatorioPortaria1510CabecalhoResumo(String nome, String cpf, String matricula, String departamento) {
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.departamento = departamento;
    }

    public RelatorioPortaria1510CabecalhoResumo() {

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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
