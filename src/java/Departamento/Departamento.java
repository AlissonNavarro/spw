/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Departamento;

import java.io.Serializable;

/**
 *
 * @author amvboas
 */
public class Departamento implements Serializable{

    private String nomeDepartamento;
    private Integer id;
    private Integer superDeptoId;

    public Departamento() {
        id=0;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeDepartamento() {
        return nomeDepartamento;
    }

    public void setNomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
    }

    public Integer getSuperDeptoId() {
        return superDeptoId;
    }

    public void setSuperDeptoId(Integer superDeptoId) {
        this.superDeptoId = superDeptoId;
    }

    


}
