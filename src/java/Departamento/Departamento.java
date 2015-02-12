package Departamento;

import java.io.Serializable;

public class Departamento implements Serializable {

    private Integer id;
    private String nome;
    private Integer superDeptoId;

    public Departamento() {
        id = 0;
        nome = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSuperDeptoId() {
        return superDeptoId;
    }

    public void setSuperDeptoId(Integer superDeptoId) {
        this.superDeptoId = superDeptoId;
    }

}
