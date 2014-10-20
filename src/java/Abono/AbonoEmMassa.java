/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class AbonoEmMassa implements Serializable {

    private Integer posicao;
    private Integer cod_funcionario;
    private String nomeFuncionario;
    private Date inicio;
    private Date fim;
    private String categoria;
    private Integer cod_categoria;
    private String descricaoCategoria;
    private Date dataAbonoEmMassa;
    private Boolean marked;

    public AbonoEmMassa(String nomeFuncionario, Date inicio, Date fim,Integer cod_categoria, String categoria, String descricaoCategoria, Date dataAbonoEmMassa) {
        this.nomeFuncionario = nomeFuncionario;
        this.inicio = inicio;
        this.fim = fim;
        this.cod_categoria = cod_categoria;
        this.categoria = categoria;
        this.descricaoCategoria = descricaoCategoria;
        this.dataAbonoEmMassa = dataAbonoEmMassa;
    }

    public AbonoEmMassa() {
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }

    public void setDescricaoCategoria(String descricaoCategoria) {
        this.descricaoCategoria = descricaoCategoria;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Date getDataAbonoEmMassa() {
        return dataAbonoEmMassa;
    }

    public void setDataAbonoEmMassa(Date dataAbonoEmMassa) {
        this.dataAbonoEmMassa = dataAbonoEmMassa;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public Integer getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(Integer cod_categoria) {
        this.cod_categoria = cod_categoria;
    }
}
