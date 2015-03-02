package entidades;

import java.io.Serializable;

public class Departamento implements Serializable {

    private int id;
    private String nome;
    private int superDeptoId;

    public Departamento() {
        id = 0;
        nome = "";
        superDeptoId = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSuperDeptoId() {
        return superDeptoId;
    }

    public void setSuperDeptoId(int superDeptoId) {
        this.superDeptoId = superDeptoId;
    }

}
