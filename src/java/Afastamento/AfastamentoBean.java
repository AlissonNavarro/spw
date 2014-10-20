/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Afastamento;

/**
 *
 * @author amsgama
 */
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class AfastamentoBean implements Serializable {

    private Integer departamento;
    private Integer cod_funcionario;
    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private List<SelectItem> categoriaAfastamentoList;
    private List<Afastamento> afastamentoList;
    private Boolean incluirSubSetores;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private Afastamento novoAfastamento;
    private Afastamento alterarAFastamento;
    private Afastamento alterarAFastamentoTemp;

    public AfastamentoBean() throws SQLException {

        afastamentoList = new ArrayList<Afastamento>();
        alterarAFastamento = new Afastamento();
        alterarAFastamentoTemp = new Afastamento();
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        objLocale = new Locale("pt", "BR");
        cod_funcionario = -1;

        novoAfastamento = new Afastamento();
        novoAfastamento.setDataInicio(getPrimeiroDiaMes());
        novoAfastamento.setDataFim(getHoje());

        Banco banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();

        consultaDepartamentos();
        inicializaAtributos();
        Locale.setDefault(new Locale("pt", "BR"));
    }

    private static Date getHoje() {
        return new Date();
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentolist = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void inicializaAtributos() {
        //      Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        //      departamentolist = new ArrayList<SelectItem>();
        //      departamentolist = banco.consultaDepartamentoOrdernado();
    }

    public void consultaFuncionario() throws SQLException {
        Banco banco = new Banco();
        cod_funcionario = -1;
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        if (!departamento.equals(-1)) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        }
        afastamentoList = new ArrayList<Afastamento>();
    }

    public void showNovoAfastamento() {
        Banco banco = new Banco();
        categoriaAfastamentoList = banco.consultaCategoriaAfastamento();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        novoAfastamento.setResponsavel(usuarioBean.getUsuario());
    }

    public void showAlterarAfastamento() {
        String posicao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posAfastamento");

        Integer posicaoInt = Integer.parseInt(posicao);
        Afastamento afastamento = afastamentoList.get(posicaoInt);
        alterarAFastamento.setDataInicio(afastamento.getDataInicio());
        alterarAFastamento.setDataFim(afastamento.getDataFim());
        alterarAFastamento.setFuncionario(afastamento.getFuncionario());
        alterarAFastamento.setCategoriaAfastamento(afastamento.getCategoriaAfastamento());
        alterarAFastamentoTemp.copy(alterarAFastamento);
      
    }

    public void consultaAfastamento() {
        afastamentoList = new ArrayList<Afastamento>();
        Banco banco = new Banco();
        Calendar c = new GregorianCalendar();
        c.setTime(dataFim);
        c.add(Calendar.DAY_OF_MONTH, 1);

        if (dataInicio.getTime() <= dataFim.getTime()) {
            if (cod_funcionario.equals(0)) {
                afastamentoList = banco.consultaAfastamentoByDepartameto(departamento, dataInicio, c.getTime());
            } else if (!cod_funcionario.equals(null)) {
                afastamentoList = banco.consultaAfastamentoByFuncionario(cod_funcionario, dataInicio, c.getTime());
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void addAfastamento() {
        Banco banco = new Banco();

        if (cod_funcionario.equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Selecione um funcionário!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (novoAfastamento.getCategoriaAfastamento().getCategoriaAfastamentoID() == -1) {
            FacesMessage msgErro = new FacesMessage("Selecione uma categoria de justificativa!!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (!(novoAfastamento.getDataInicio().getTime() <= novoAfastamento.getDataFim().getTime())) {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (cod_funcionario.equals(0)) {
            banco.insertAfastamento(funcionarioList, novoAfastamento);
            FacesMessage msgErro = new FacesMessage("Afastamento inserido com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
            Metodos.setLogInfo("Afastamento - Departamento: " + Metodos.buscaRotulo(departamento.toString(), departamentolist)
                    + " Data Inicial: " + sfData.format(novoAfastamento.getDataInicio())
                    + " Data Final: " + sfData.format(novoAfastamento.getDataFim())
                    + " Categoria: " + novoAfastamento.getCategoriaAfastamento().getDescCategoriaAfastamento()
                    + " Responsável: " + novoAfastamento.getResponsavel().getNome());
            consultaAfastamento();
        } else {
            Boolean flag = banco.insertAfastamento(cod_funcionario, novoAfastamento);
            if (flag) {
                SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
                Metodos.setLogInfo("Afastamento - Funcionário: " + Metodos.buscaRotulo(cod_funcionario.toString(), funcionarioList)
                        + " Data Inicial: " + sfData.format(novoAfastamento.getDataInicio())
                        + " Data Final: " + sfData.format(novoAfastamento.getDataFim())
                        + " Categoria: " + novoAfastamento.getCategoriaAfastamento().getDescCategoriaAfastamento()
                        + " Responsável: " + novoAfastamento.getResponsavel().getNome());
                FacesMessage msgErro = new FacesMessage("Afastamento inserido com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Erro ao inserir o afastamento!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            consultaAfastamento();
        }
    }

    public void alterarAfastamento() {
        Banco banco = new Banco();

        if (cod_funcionario.equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Selecione um funcionário!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (!(dataInicio.getTime() <= dataFim.getTime())) {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (alterarAFastamento.getCategoriaAfastamento().getCategoriaAfastamentoID() == -1) {
            FacesMessage msgErro = new FacesMessage("Selecione uma categoria");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            banco = new Banco();
            UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
            banco.alterarAfastamento(alterarAFastamentoTemp, alterarAFastamento, usuarioBean.getUsuario());
            SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
            Metodos.setLogInfo("Alterar afastamento - Funcionário: " + alterarAFastamento.getFuncionario().getNome()
                    + " Data Inicial: " + sfData.format(alterarAFastamento.getDataInicio())
                    + " Data Final: " + sfData.format(alterarAFastamento.getDataFim())
                    + " Categoria: " + alterarAFastamento.getCategoriaAfastamento().getDescCategoriaAfastamento());
            consultaAfastamento();
        }
    }

    public void excluirAfastamento() {
        String posicao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posAfastamento");
        Integer posicaoInt = Integer.parseInt(posicao);
        Afastamento afastamento = afastamentoList.get(posicaoInt);
        Banco banco = new Banco();
        SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Metodos.setLogInfo("Excluir afastamento em Massa - Funcionário: " + afastamento.getFuncionario().getNome()
                + " Data e Hora Inicial: " + sfData.format(afastamento.getDataInicio())
                + " Data e Hora Final: " + sfData.format(afastamento.getDataFim()) +
                " Justificativa: " + afastamento.getCategoriaAfastamento().getDescCategoriaAfastamento());
        banco.deleteAfastamento(afastamento);
        consultaAfastamento();
    }

    public void excluirTodosAfastamento() {
        Banco banco = new Banco();
        SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Metodos.setLogInfo("Excluir afastamento em Massa - Departamento : " + Metodos.buscaRotulo(departamento.toString(), departamentolist)
                + " Data: " + sfData.format(dataInicio)
                + " Data: " + sfData.format(dataFim));
        banco.deleteTodosAfastamento(afastamentoList);
        consultaAfastamento();
        FacesMessage msgErro = new FacesMessage("Afastamentos excluidos com sucesso!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    private static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataPrimeiroDiaStr = sdfHora.format(data.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        try {
            data = df.parse(dataPrimeiroDiaStr);
        } catch (ParseException ex) {
        }
        return data;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public Afastamento getAlterarAFastamento() {
        return alterarAFastamento;
    }

    public void setAlterarAFastamento(Afastamento alterarAFastamento) {
        this.alterarAFastamento = alterarAFastamento;
    }

    public List<SelectItem> getCategoriaAfastamentoList() {
        return categoriaAfastamentoList;
    }

    public void setCategoriaAfastamentoList(List<SelectItem> categoriaAfastamentoList) {
        this.categoriaAfastamentoList = categoriaAfastamentoList;
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

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Afastamento getNovoAfastamento() {
        return novoAfastamento;
    }

    public void setNovoAfastamento(Afastamento novoAfastamento) {
        this.novoAfastamento = novoAfastamento;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }
}
