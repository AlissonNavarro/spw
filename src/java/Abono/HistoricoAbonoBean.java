/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 *
 */
public class HistoricoAbonoBean implements Serializable {

    private List<HistoricoAbono> historicoAbonoList;
    private List<HistoricoAbono> historicoAbonoPorPeriodoList;
    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private Integer departamento;
    private Integer cod_funcionario;
    private Date dataInicio;
    private Date dataFim;
    private UsuarioBean usuarioBean;
    private Boolean incluirSubSetores;
    private Locale objLocale;
    private Boolean isTodosFuncionarios;
    private Boolean isRelatorioSemFuncionario;

    public HistoricoAbonoBean() {
        Banco banco = new Banco();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Integer permissao;
        if (usuarioBean.getEhSuperAdministrador()) {
            permissao = 1;
        } else {
            permissao = Integer.parseInt(usuarioBean.getUsuario().getPermissao());
        }
        departamentolist = new ArrayList<SelectItem>();
        departamentolist = banco.consultaDepartamentoHierarquico(permissao);
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-2, "Selecione o responsável"));
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
    }

    public void consultaFuncionario() {
        cod_funcionario = -2;
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-2, "Selecione o responsável"));
        if (!departamento.equals(-1)) {
            Banco banco = new Banco();
            funcionarioList = new ArrayList<SelectItem>();
            funcionarioList = banco.consultaFuncionarioGerente(departamento, incluirSubSetores);
        }
    }

    public void consultaHistorico() {
        isRelatorioSemFuncionario = false;
        historicoAbonoList = new ArrayList<HistoricoAbono>();
        if (isConsultaValida()) {
            Banco banco = new Banco();
            java.sql.Date dataInicio_ = new java.sql.Date(dataInicio.getTime());
            java.sql.Date dataFim_ = new java.sql.Date(dataFim.getTime());
            historicoAbonoList = banco.consultaHistorico(cod_funcionario, departamento, dataInicio_, dataFim_);
            if (historicoAbonoList.isEmpty()) {
                isRelatorioSemFuncionario = true;
            }
            if (cod_funcionario == -1) {
                isTodosFuncionarios = true;
            } else {
                isTodosFuncionarios = false;
            }
            consultaHistoricoPorIntervalo();
        } else {
            if (!isDataValida()) {
                FacesMessage msgErro = new FacesMessage("Selecione uma data válida!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else if (!isFuncionarioValido()) {
                FacesMessage msgErro = new FacesMessage("Selecione um responsável!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

    public void alterarStatus() {
        int cod_solicitacao = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posRegistro"));
        Banco b = new Banco();
        b.alterarStatus(cod_solicitacao);      
        consultaHistorico();
    }
    
    public void excluirAbonos() {
        Banco banco = new Banco();
        List<HistoricoAbono> historicoAbonoSelecionados = new ArrayList<HistoricoAbono>();
        for (Iterator<HistoricoAbono> it = historicoAbonoList.iterator(); it.hasNext();) {
            HistoricoAbono historicoAbono = it.next();
            if (historicoAbono.getMarked()) {
                historicoAbonoSelecionados.add(historicoAbono);
            }
        }
        if (!historicoAbonoSelecionados.isEmpty()) {
            banco = new Banco();
            Boolean flag = banco.excluirAbono(historicoAbonoSelecionados);
            if (flag) {
                for (int i = 0; i < historicoAbonoSelecionados.size(); i++) {
                    HistoricoAbono historicoAbono = historicoAbonoSelecionados.get(i);
                    String diaAbonStr = historicoAbono.getAbono();
                        Metodos.setLogInfo("Exclusão Abono - Data e Hora: " + diaAbonStr + " Funcionário: " + historicoAbono.getFuncionario());
                }

                FacesMessage msgErro = new FacesMessage("Abonos deletados com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Nenhum registro foi selecionado para deleção!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        historicoAbonoList = new ArrayList<HistoricoAbono>();
        java.sql.Date dataInicio_ = new java.sql.Date(dataInicio.getTime());
        java.sql.Date dataFim_ = new java.sql.Date(dataFim.getTime());
        historicoAbonoList = banco.consultaHistorico(cod_funcionario, departamento, dataInicio_, dataFim_);
    }

    public void consultaHistoricoPorIntervalo() {

        Banco banco = new Banco();
        historicoAbonoPorPeriodoList = new ArrayList<HistoricoAbono>();
        java.sql.Date dataInicio_ = new java.sql.Date(dataInicio.getTime());
        java.sql.Date dataFim_ = new java.sql.Date(dataFim.getTime());
        historicoAbonoPorPeriodoList = banco.consultaHistoricoPorPeriodo(cod_funcionario, dataInicio_, dataFim_);
        if (historicoAbonoList.isEmpty()) {
            if (isRelatorioSemFuncionario) {
                FacesMessage msgErro = new FacesMessage("Nenhum registro foi abonado nesse intervalo de datas!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
        if (cod_funcionario == -1) {
            isTodosFuncionarios = true;
        } else {
            isTodosFuncionarios = false;
        }
    }

    private Boolean isConsultaValida() {
        if ((null != dataInicio) && (null != dataFim) && (null != cod_funcionario) && (-2 != cod_funcionario)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isDataValida() {
        if ((null != dataInicio) && (null != dataFim)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isFuncionarioValido() {
        if ((null != cod_funcionario) && (-2 != cod_funcionario)) {
            return true;
        } else {
            return false;
        }
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

    private static Date getHoje() {
        return new Date();
    }

    public List<HistoricoAbono> getHistoricoAbonoList() {
        return historicoAbonoList;
    }

    public void setHistoricoAbonoList(List<HistoricoAbono> historicoAbonoList) {
        this.historicoAbonoList = historicoAbonoList;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
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

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;
    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;
    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Boolean getIsTodosFuncionarios() {
        return isTodosFuncionarios;
    }

    public void setIsTodosFuncionarios(Boolean isTodosFuncionarios) {
        this.isTodosFuncionarios = isTodosFuncionarios;
    }

    public List<HistoricoAbono> getHistoricoAbonoPorPeriodoList() {
        return historicoAbonoPorPeriodoList;
    }

    public void setHistoricoAbonoPorPeriodoList(List<HistoricoAbono> historicoAbonoPorPeriodoList) {
        this.historicoAbonoPorPeriodoList = historicoAbonoPorPeriodoList;
    }

    public Boolean getIsRelatorioSemFuncionario() {
        return isRelatorioSemFuncionario;
    }

    public void setIsRelatorioSemFuncionario(Boolean isRelatorioSemFuncionario) {
        this.isRelatorioSemFuncionario = isRelatorioSemFuncionario;
    }
}
