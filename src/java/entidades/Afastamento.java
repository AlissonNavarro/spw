package entidades;

import java.io.Serializable;
import java.util.Date;
import Funcionario.Funcionario;

public class Afastamento implements Serializable {

    private int id;
    private int codFuncionario;
    private Funcionario funcionario;
    private int codCategoriaAfastamento;
    private CategoriaAfastamento categoriaAfastamento;
    private Date dataInicio;
    private Date dataFim;
    private int responsavel;

    public Afastamento() {
    }

    public Afastamento(int id, int codFuncionario, int codCategoriaAfastamento, Date dataInicio, Date dataFim, int responsavel) {
        this.id = id;
        this.codFuncionario = codFuncionario;
        this.codCategoriaAfastamento = codCategoriaAfastamento;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.responsavel = responsavel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public int getCodCategoriaAfastamento() {
        return codCategoriaAfastamento;
    }

    public void setCodCategoriaAfastamento(int codCategoriaAfastamento) {
        this.codCategoriaAfastamento = codCategoriaAfastamento;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public int getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(int responsavel) {
        this.responsavel = responsavel;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public CategoriaAfastamento getCategoriaAfastamento() {
        return categoriaAfastamento;
    }

    public void setCategoriaAfastamento(CategoriaAfastamento categoriaAfastamento) {
        this.categoriaAfastamento = categoriaAfastamento;
    }
    
    
    
}
