/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class JornadaCadastro implements Serializable {

    private Integer id;
    private String nome;
    private Date dataInicio;
    private Integer quantidadeCiclos;
    private Integer unidadeCiclos;
    private Boolean isRegular;
    private String nomeResponsavel;
    private String css;

    public JornadaCadastro() {
        quantidadeCiclos = new Integer(1);
        unidadeCiclos = new Integer(1);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JornadaCadastro(Integer id, String nome, Date dataInicio, Integer quantidadeCiclos, Integer unidadeCiclos) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.quantidadeCiclos = quantidadeCiclos;
        this.unidadeCiclos = unidadeCiclos;

    }

    public Boolean estaPreenchidoCorretamente() {
        if (!nome.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void addNovaJornada() {
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidadeCiclos() {
        return quantidadeCiclos;
    }

    public void setQuantidadeCiclos(Integer quantidadeCiclos) {
        this.quantidadeCiclos = quantidadeCiclos;
    }

    public String getUnidadeCiclos() {
        if (unidadeCiclos != null) {
            if (unidadeCiclos == 0) {
                return "Dia";
            } else if (unidadeCiclos == 1) {
                return "Semana";
            } else {
                return "Mes";
            }
        } else {
            return null;
        }
    }

    public Integer getUnidadeCiclosInt() {
        return unidadeCiclos;
    }

    public void setUnidadeCiclos(String unidadeCiclosLabel) {

        if (unidadeCiclosLabel.equals("Dia")) {
            unidadeCiclos = 0;
        } else if (unidadeCiclosLabel.equals("Semana")) {
            unidadeCiclos = 1;
        } else {
            unidadeCiclos = 2;
        }
    }

    public void setUnidadeCiclos(Integer unidadeCiclos) {
        this.unidadeCiclos = unidadeCiclos;
    }

    public Boolean getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(Boolean isRegular) {
        this.isRegular = isRegular;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCss() {
        if (isRegular){
            css="";
        }else{
            css="color: red";
        }
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

}
