/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaJornada;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class JornadaFunc {

    String nome;
    String funcionario;
    List<Integer> dias;

    public JornadaFunc(String nome, String funcionario) {
        this.nome = nome;
        this.funcionario = funcionario;
        dias = new ArrayList<Integer>();
    }

    public List<Integer> getDias() {
        return dias;
    }

    public void setDias(List<Integer> dias) {
        this.dias = dias;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
