/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import Metodos.Metodos;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class DiaHoraExtraBean implements Serializable {

    private DiaHoraExtra diaHoraExtra;
    private List<SelectItem> categoriaJustificativaList;
    private String nome;
    private String departamentoStr;
    private String dataSrt;
    private String horasTotal;
    private String horasASeremTrabalhadasTotal;
    private String saldoTotal;
    private Integer contDiasATrabalhar;
    private Integer contDiasTrabalhados;
    private Integer faltas;
    private Boolean isEditar;

    public DiaHoraExtraBean() {
        diaHoraExtra = new DiaHoraExtra();
        Banco banco = new Banco();
        dataSrt = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("diaParam");
        setDia(banco);
        setDetalheDia();
        categoriaJustificativaList = new ArrayList<SelectItem>();
        categoriaJustificativaList = banco.consultaCategoriaHoraExtra(diaHoraExtra.getUserid());
    }

    private void inserirHoraExtra() {
        if (categoriaJustificativaList.size() == 1) {
            FacesMessage msgErro = new FacesMessage("Funcionário não possui um regime cadastrado ou o regime não possui uma categoria de justificativa!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (diaHoraExtra.getCod_categoria().equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Escolha uma categoria de justificativa!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {            
            Banco banco = new Banco();
            Boolean sucesso = banco.insertHoraExtra(diaHoraExtra);
            if (sucesso) {
                Metodos.setLogInfo("Adicionar Hora Extra - Funcionario: \""+nome+"\" Data: "+dataSrt);
                FacesMessage msgErro = new FacesMessage("Hora extra inserida com sucesso.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Erro ao inserir hora extra.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

    private void editarHoraExtra() {
        if (diaHoraExtra.getCod_categoria().equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Escolha uma categoria de justificativa!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            Banco banco = new Banco();
            Boolean sucesso = banco.editarHoraExtra(diaHoraExtra);
            
   
            if (sucesso) {
                Metodos.setLogInfo("Editar Hora Extra - Funcionario: \""+nome+"\" Data: "+dataSrt);
                FacesMessage msgErro = new FacesMessage("Hora extra editada com sucesso.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Erro ao editar hora extra.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        }
    }

    private void setDia(Banco banco) throws NumberFormatException {
        String userid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userid");
        Date data = getStringToData(dataSrt);
        Integer cod_categoria = banco.consultaCodCategoriahoraExtra(Integer.parseInt(userid), data);
        String justificativa = banco.consultaJustificativaHoraExtra(Integer.parseInt(userid), data);
        diaHoraExtra.setCod_categoria(cod_categoria);
        diaHoraExtra.setJustificativa(justificativa);
        if (cod_categoria.equals(-1)) {
            isEditar = false;
        } else {
            isEditar = true;
        }
    }

    private void setDetalheDia() {
        ConsultaFrequenciaComEscalaBean c = getConsultaFrequenciaComEscalaBean();
        nome = c.getNome();
        departamentoStr = consultaNomeDepartamento(c.getCod_funcionario());
        horasTotal = c.getHorasTotal();
        horasASeremTrabalhadasTotal = c.getHorasASeremTrabalhadasTotal();
        saldoTotal = c.getSaldo2();
        //saldoTotal = c.getSaldoTotal();
        contDiasATrabalhar = c.getContDiasATrabalhar();
        contDiasTrabalhados = c.getContDiasTrabalhados();
        faltas = c.getFaltas();
    }

    private ConsultaFrequenciaComEscalaBean getConsultaFrequenciaComEscalaBean() throws NumberFormatException {
        dataSrt = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("diaParam");
        String useridParam = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userid");
        Date data = getStringToData(dataSrt);
        diaHoraExtra.setData(data);
        Integer userid = Integer.parseInt(useridParam);
        diaHoraExtra.setUserid(userid);
        ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");
        c.setDataInicio(data);
        c.setDataFim(data);
        c.setCod_funcionario(userid);
        c.consultaDiasSemMsgErro();
        return c;
    }

    public void enviar() {
        if (isEditar) {
            editarHoraExtra();
        } else {
            inserirHoraExtra();
        }
    }

    public String excluirHoraExtra(){
        Banco banco = new Banco();
        banco.excluirHoraExtra(diaHoraExtra);
        return "navegarInicio";
    }

    public String consultaNomeDepartamento(Integer userid) {
        Banco banco = new Banco();
        return banco.consultaDepartamentoNomeByUserid(userid);
    }

    public Date getStringToData(String dataStr) {
        java.util.Date dt = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dt = df.parse(dataStr);
        } catch (ParseException ex) {
        }
        return dt;
    }

    public List<SelectItem> getCategoriaJustificativaList() {
        return categoriaJustificativaList;
    }

    public void setCategoriaJustificativaList(List<SelectItem> categoriaJustificativaList) {
        this.categoriaJustificativaList = categoriaJustificativaList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getContDiasATrabalhar() {
        return contDiasATrabalhar;
    }

    public void setContDiasATrabalhar(Integer contDiasATrabalhar) {
        this.contDiasATrabalhar = contDiasATrabalhar;
    }

    public Integer getContDiasTrabalhados() {
        return contDiasTrabalhados;
    }

    public void setContDiasTrabalhados(Integer contDiasTrabalhados) {
        this.contDiasTrabalhados = contDiasTrabalhados;
    }

    public String getDepartamentoStr() {
        return departamentoStr;
    }

    public void setDepartamentoStr(String departamentoStr) {
        this.departamentoStr = departamentoStr;
    }

    public Integer getFaltas() {
        return faltas;
    }

    public void setFaltas(Integer faltas) {
        this.faltas = faltas;
    }

    public String getHorasASeremTrabalhadasTotal() {
        return horasASeremTrabalhadasTotal;
    }

    public void setHorasASeremTrabalhadasTotal(String horasASeremTrabalhadasTotal) {
        this.horasASeremTrabalhadasTotal = horasASeremTrabalhadasTotal;
    }

    public String getHorasTotal() {
        return horasTotal;
    }

    public void setHorasTotal(String horasTotal) {
        this.horasTotal = horasTotal;
    }

    public String getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(String saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public String getDataSrt() {
        return dataSrt;
    }

    public void setDataSrt(String dataSrt) {
        this.dataSrt = dataSrt;
    }

    public DiaHoraExtra getDiaHoraExtra() {
        return diaHoraExtra;
    }

    public void setDiaHoraExtra(DiaHoraExtra diaHoraExtra) {
        this.diaHoraExtra = diaHoraExtra;
    }

    public Boolean getIsEditar() {
        return isEditar;
    }

    public void setIsEditar(Boolean isEditar) {
        this.isEditar = isEditar;
    }
}
