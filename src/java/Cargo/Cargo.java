package Cargo;

import java.io.Serializable;

public class Cargo implements Serializable{

    private String nomeCargo;
    private Integer id;
    
    public Cargo() {
        id=0;
        nomeCargo="";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

}
