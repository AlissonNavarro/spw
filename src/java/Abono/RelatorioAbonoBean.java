package Abono;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Aloisio
 *
 */
public class RelatorioAbonoBean implements Serializable {

    private List<RelatorioAbono> relatorioAbonoList;
    private List<SelectItem> departamentolist;
    private Integer departamento;
    
    private Date dataInicio;
    private Date dataFim;
    private UsuarioBean usuarioBean;
    private Boolean incluirSubSetores;
    private Locale objLocale;

    public RelatorioAbonoBean() {
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
        objLocale = new Locale("pt", "BR");
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        consultaRelatorio();


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

    public void consultaRelatorio() {

        List<HistoricoAbono> historicoAbonoList = new ArrayList<HistoricoAbono>();
        if (isConsultaValida()) {
            Banco banco = new Banco();
            java.sql.Date dataInicio_ = new java.sql.Date(dataInicio.getTime());
            java.sql.Date dataFim_ = new java.sql.Date(dataFim.getTime());
            historicoAbonoList = banco.consultaHistoricoDetalhado(-1,incluirSubSetores, departamento, dataInicio_, dataFim_);
            HashMap relatorioMap = new HashMap<Integer,RelatorioAbono>();
            for (int i = 0; i < historicoAbonoList.size(); i++) {
                HistoricoAbono historicoAbono = historicoAbonoList.get(i);

            }

        } else {

                FacesMessage msgErro = new FacesMessage("Selecione uma data válida!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);

        }


    }
    /*
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
     * /*
     */

    private Boolean isConsultaValida() {
        if ((null != dataInicio) && (null != dataFim)) {
            return true;
        } else {
            return false;
        }
    }

    private static Date getHoje() {
        return new Date();
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

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public List<RelatorioAbono> getRelatorioAbonoList() {
        return relatorioAbonoList;
    }

    public void setRelatorioAbonoList(List<RelatorioAbono> relatorioAbonoList) {
        this.relatorioAbonoList = relatorioAbonoList;
    }

  
    
}

