/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class SolicitacaoAbonoBean implements Serializable {

    private Date dataInicio;
    private Date dataFim;
    private List<SolicitacaoAbono> solicitacaoAbonoList;
    private Locale objLocale;
    private Integer cod_solicitacao;
    private UsuarioBean usuarioBean;
    private String negadoDescricao;
    private List<SelectItem> departamentosSelecItem;
    private Integer departamentoSelecionado;
    private Boolean incluirSubSetores;
    private Boolean marked;
    private String varr;
    private HtmlSelectBooleanCheckbox la;
    private String abaCorrente;

    public SolicitacaoAbonoBean() {
        varr = "";
        solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        dataInicio = getPrimeiroDiaMes();
        dataFim = getLastDay();
        objLocale = new Locale("pt", "BR");
        consultaDepartamentos();
        abaCorrente = "tab1";
    }

    public void teste() {
        varr = varr + "0";
    }

    public String voltar() {
        return "navegarInicio";
    }
    /*
     public String negar() {
     Banco banco = new Banco();
     String dataStr = new SimpleDateFormat().format(data);
     Metodos.setLogInfo("Negar Abono - Funcionário: \""+nome+"\" Data: "+dataStr);

     UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
     banco.updateSolicitacaoAbono(cod, false, respostaJustificativa, usuarioBean.getUsuario().getLogin());
     return "solicitacaoAbonoBean";
     }
     */

    public void geraSolicitacaoAbonoList() {
        Banco banco = new Banco();
        solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        solicitacaoAbonoList = banco.consultaSolicitacoesAbono(usuarioBean.getUsuario(), departamentoSelecionado, dataInicio, dataFim, incluirSubSetores);
        if (solicitacaoAbonoList.isEmpty()) {
            FacesMessage msgErro = new FacesMessage("Nenhum abono solicitado!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        System.out.println("gerou");
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
            System.out.println("Abono: getPrimeiroDiaMes: " + ex);
            //Logger.getLogger(SolicitacaoAbonoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

    public static Date getLastDay() {
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getUltimoDiaMes());
        Date data = calendar.getTime();
        return data;
    }

    public static Date getUltimoDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date data = calendar.getTime();
        return data;
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void atualizar() {
        Banco banco = new Banco();
        solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        solicitacaoAbonoList = banco.consultaSolicitacoesAbono(usuarioBean.getUsuario(), departamentoSelecionado, dataInicio, dataFim, incluirSubSetores);
    }

    public List<SolicitacaoAbono> getSolicitacaoAbonoList() {
        return solicitacaoAbonoList;
    }

    public void setSolicitacaoAbonoList(List<SolicitacaoAbono> solicitacaoAbonoList) {
        this.solicitacaoAbonoList = solicitacaoAbonoList;
    }

    public Integer getCod_solicitacao() {
        return cod_solicitacao;
    }

    public void setCod_solicitacao(Integer cod_solicitacao) {
        this.cod_solicitacao = cod_solicitacao;
    }

    public String getNegadoDescricao() {
        return negadoDescricao;
    }

    public void setNegadoDescricao(String negadoDescricao) {
        this.negadoDescricao = negadoDescricao;
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

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public Integer getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Integer departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;
    }

    public String getVarr() {
        return varr;
    }

    public void setVarr(String varr) {
        this.varr = varr;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public HtmlSelectBooleanCheckbox getLa() {
        return la;
    }

    public void setLa(HtmlSelectBooleanCheckbox la) {
        this.la = la;
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }

    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        if (tab.equals("tab1")) {
            atualizar();
        }
        if (tab.equals("tab2")) {
            ConsultaDiaEmAbertoBean consultaDiaEmAbertoBean = (ConsultaDiaEmAbertoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaDiaEmAbertoBean");
            consultaDiaEmAbertoBean.consultarSemMSg();
        }
        abaCorrente = tab;
    }
}
