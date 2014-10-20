/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroCronograma;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class Jornada implements Serializable{

    private Integer cod_funcionario;
    private String matricula;
    private String nomeFuncionario;
    private String nomeJornada;
    private Integer cod_jornada;
    private Date inicioNovo;
    private Date fimNovo;
    private Date inicioAntigo;
    private Date fimAntigo;
    //flag = 1, caso inserção de jornada; flag = 2, caso update de jornada; flag = 3, caso deleção de jornada
    private Integer flag;

    public Jornada(Integer cod_funcionario, Integer cod_jornada, Date inicioAntigo, Date fimAntigo, Integer flag) {
        this.cod_funcionario = cod_funcionario;
        this.cod_jornada = cod_jornada;
        this.inicioAntigo = inicioAntigo;
        this.fimAntigo = fimAntigo;
        this.inicioNovo = inicioAntigo;
        this.fimNovo = fimAntigo;
        this.flag = flag;
    }

    public Jornada(Integer cod_funcionario, Integer cod_jornada, Date inicioNovo, Date fimNovo, Date inicioAntigo, Date fimAntigo, Integer flag) {
        this.cod_funcionario = cod_funcionario;
        this.cod_jornada = cod_jornada;
        this.inicioNovo = inicioNovo;
        this.fimNovo = fimNovo;
        this.inicioAntigo = inicioAntigo;
        this.fimAntigo = fimAntigo;
        this.flag = flag;
    }

    public Jornada() {
    }

    
    public boolean equal(Jornada jornada) {
           
            if (cod_funcionario.equals(jornada.getCod_funcionario()) &&
                    (inicioNovo.equals(jornada.getInicioNovo()))) {
                return true;
            } else {
                return false;
            } 
    }

    public boolean equal2(Jornada jornada) {

            if (cod_funcionario.equals(jornada.getCod_funcionario()) &&
                    (((inicioNovo.equals(jornada.getInicioNovo())||fimNovo.equals(jornada.getFimNovo()))&&
                    cod_jornada.equals(jornada.getCod_jornada())&&flag.equals(jornada.getFlag())))) {
                return true;
            } else {
                return false;
            }
    }

    public void setDadosJornada(Jornada jornada) {
        cod_funcionario = jornada.getCod_funcionario();
        nomeFuncionario = jornada.getNomeFuncionario();
        nomeJornada = jornada.getNomeJornada();
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Integer getCod_jornada() {
        return cod_jornada;
    }

    public void setCod_jornada(Integer cod_jornada) {
        this.cod_jornada = cod_jornada;
    }

    public Date getFimNovo() {
        return fimNovo;
    }

    public void setFimNovo(Date fimNovo) {
        this.fimNovo = fimNovo;
    }

    public Date getInicioNovo() {
        return inicioNovo;
    }

    public void setInicioNovo(Date inicioNovo) {
        this.inicioNovo = inicioNovo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getFimAntigo() {
        return fimAntigo;
    }

    public void setFimAntigo(Date fimAntigo) {
        this.fimAntigo = fimAntigo;
    }

    public Date getInicioAntigo() {
        return inicioAntigo;
    }

    public void setInicioAntigo(Date inicioAntigo) {
        this.inicioAntigo = inicioAntigo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getNomeJornada() {
        return nomeJornada;
    }

    public void setNomeJornada(String nomeJornada) {
        this.nomeJornada = nomeJornada;
    }
}
