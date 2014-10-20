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
public class Movimento implements Serializable{

    private String cod_empresa;
    private String cod_funcionario;
    private String cod_evento;
    private String valor;
    private String cod_aula_tarefa;
    private String div_rh;

    public String printLineMovimento(){
        return cod_empresa+cod_funcionario+cod_evento+valor+"\n";
    }

    public String getCod_aula_tarefa() {
        return cod_aula_tarefa;
    }

    public void setCod_aula_tarefa(String cod_aula_tarefa) {
        this.cod_aula_tarefa = cod_aula_tarefa;
    }

    public String getCod_empresa() {
        return cod_empresa;
    }

    public void setCod_empresa(String cod_empresa) {
        this.cod_empresa = cod_empresa;
    }

    public String getCod_evento() {
        return cod_evento;
    }

    public void setCod_evento(String cod_evento) {
        this.cod_evento = cod_evento;
    }

    public String getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(String cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public String getDiv_rh() {
        return div_rh;
    }

    public void setDiv_rh(String div_rh) {
        this.div_rh = div_rh;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
