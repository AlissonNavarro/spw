/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Afastamento;

import CadastroDeCategoriaAfastamento.CategoriaAfastamento;
import Funcionario.Funcionario;
import Usuario.Usuario;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class Afastamento implements Serializable {

    private Integer index;
    private Funcionario funcionario;
    private CategoriaAfastamento categoriaAfastamento;
    private Date dataInicio;
    private Date dataFim;
    private Usuario responsavel;

    public Afastamento() {
        categoriaAfastamento = new CategoriaAfastamento();
    }

    public void copy(Afastamento afastamento_) {
        this.setFuncionario(afastamento_.getFuncionario());
        this.setCategoriaAfastamento(afastamento_.getCategoriaAfastamento());
        this.setDataInicio(afastamento_.getDataInicio());
        this.setDataFim(afastamento_.getDataFim());
        this.setIndex(afastamento_.getIndex());
        this.setResponsavel(afastamento_.getResponsavel());
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public CategoriaAfastamento getCategoriaAfastamento() {
        return categoriaAfastamento;
    }

    public void setCategoriaAfastamento(CategoriaAfastamento categoriaAfastamento) {
        this.categoriaAfastamento = categoriaAfastamento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }
}
