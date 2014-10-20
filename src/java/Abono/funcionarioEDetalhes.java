/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Abono;

import java.io.Serializable;

/**
 *
 * @author Amvboas
 */
public class funcionarioEDetalhes implements Serializable {

        public Integer detalhes;
        public String funcionario;

    public funcionarioEDetalhes(Integer detalhes, String funcionario) {
        this.detalhes = detalhes;
        this.funcionario = funcionario;
    }

    public Integer getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(Integer detalhes) {
        this.detalhes = detalhes;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

        

        
    }