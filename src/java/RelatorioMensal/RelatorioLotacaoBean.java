/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import Usuario.UsuarioBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Alexandre
 */
public class RelatorioLotacaoBean implements Serializable {

    private Integer departamentoSelecionado;
    private Integer escalaSelecionada;
    private List<SelectItem> departamentosSelecItem;
    private List<SelectItem> escalasSelecItem;
    private List<RelatorioLotacao> relatorioLotacaoList;
    private Date data;
    private Locale objLocale;
    private Boolean incluirSubSetores;
    private Integer totalFuncionarios;

    public RelatorioLotacaoBean() {
        departamentosSelecItem = new ArrayList<SelectItem>();
        escalasSelecItem = new ArrayList<SelectItem>();
        relatorioLotacaoList = new ArrayList<RelatorioLotacao>();
        objLocale = new Locale("pt", "BR");
        data = new Date();
        consultaDepartamentos();
        consultaEscalas();
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
    }

    public void consultaEscalas() {
        Banco banco = new Banco();
        escalasSelecItem = banco.consultaEscalas();       
    }

    public void consultaFuncionarios() {
        relatorioLotacaoList = new ArrayList<RelatorioLotacao>();
        if (departamentoSelecionado != -1) {
            Banco banco = new Banco();
            relatorioLotacaoList = banco.consultaFuncionarios(departamentoSelecionado, escalaSelecionada, incluirSubSetores, data);
            totalFuncionarios = relatorioLotacaoList.size();
            if (relatorioLotacaoList.isEmpty()) {
                FacesMessage msgErro = new FacesMessage("Não existem funcionários alocados nesta consulta!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

    public void imprimir() throws JRException, Exception {

        if (departamentoSelecionado != -1 && !relatorioLotacaoList.isEmpty()) {
            Banco banco = new Banco();
            banco.insertRelatorioLotacao(relatorioLotacaoList);
            banco = new Banco();
            String[] dept_e_escala = banco.getNomeDepartamento_E_Escala(departamentoSelecionado, escalaSelecionada, incluirSubSetores);
            Impressao.geraRelatorioLotacao(dept_e_escala[0],dept_e_escala[1],data , totalFuncionarios);
        } else {
            FacesMessage msgErro = new FacesMessage("Entrada de dados inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        return data;
    }

    public static Date getUltimoDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date data = calendar.getTime();
        return data;
    }

    public void imprimirDepartamento() {
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Integer departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public Integer getEscalaSelecionada() {
        return escalaSelecionada;
    }

    public void setEscalaSelecionada(Integer escalaSelecionada) {
        this.escalaSelecionada = escalaSelecionada;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public List<SelectItem> getEscalasSelecItem() {
        return escalasSelecItem;
    }

    public void setEscalasSelecItem(List<SelectItem> escalasSelecItem) {
        this.escalasSelecItem = escalasSelecItem;
    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;
    }

    public List<RelatorioLotacao> getRelatorioLotacaoList() {
        return relatorioLotacaoList;
    }

    public void setRelatorioLotacaoList(List<RelatorioLotacao> relatorioLotacaoList) {
        this.relatorioLotacaoList = relatorioLotacaoList;
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

    public Integer getTotalFuncionarios() {
        return totalFuncionarios;
    }

    public void setTotalFuncionarios(Integer totalFuncionarios) {
        this.totalFuncionarios = totalFuncionarios;
    }
}
