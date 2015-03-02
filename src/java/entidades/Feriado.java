package entidades;

import java.io.Serializable;
import java.util.Date;

public class Feriado implements Serializable {

    private int id;
    private String nome;
    private Date data;
    private boolean isOficial;

    public Feriado() {
        id = 0;
        nome = "";
        data = new Date();
        isOficial = false;
    }

    public Feriado(int id,String nome, Date data, boolean isOficial) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.isOficial = isOficial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean getIsOficial() {
        return isOficial;
    }

    public void setIsOficial(boolean isOficial) {
        this.isOficial = isOficial;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
