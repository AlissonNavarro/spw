/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroFeriado;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class Feriado implements Serializable {

    private Integer id;
    private String nome;
    private Date data;
    private Boolean isOficial;

    public Feriado() {
    }

    public Feriado(Integer id,String nome, Date data, Boolean isOficial) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.isOficial = isOficial;
    }

    public Boolean estaPreenchidoCorretamento() {
        if (!nome.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getIsOficial() {
        return isOficial;
    }

    public void setIsOficial(Boolean isOficial) {
        this.isOficial = isOficial;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
