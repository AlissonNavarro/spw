/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

/**
 *
 * @author Alexandre
 */
public class RelatorioPortaria1510CabecalhoResumo {

    private String userid;
    private String nome;
    private String cpf;
    private String matricula;
    private String departamento;
    private String cargo;
    private String regime;

    public RelatorioPortaria1510CabecalhoResumo(String userid, String nome, String cpf, String matricula, String departamento, String cargo, String regime) {
        this.userid = userid;
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.departamento = departamento;
        this.cargo = cargo;
        this.regime = regime;
    }

    public RelatorioPortaria1510CabecalhoResumo() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }
    
}
