/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class SolicitacaoAbono implements Serializable {

    private Integer cod;
    private String departamento;
    private Integer codigoFuncionario;
    private String nome;
    private Date data;
    private String entrada1 = null;
    private String saida1 = null;
    private String entrada2 = null;
    private String saida2 = null;
    private String descricao;
    private String pontosSolicitados;
    private List<SelectItem> pontosSolicitadosList;
    private String justificativa;
    private Date inclusao;
    private String status;
    private Boolean isPendente;
    private String respostaJustificativa;
    private String dataStr;

    public SolicitacaoAbono(Integer cod, String departamento, Integer codigoFuncionario, String nome, Date data, String entrada1,
            String saida1, String entrada2, String saida2, String descricao, String status, Date inclusao, String respostaJustificativa) {
        this.cod = cod;
        this.departamento = departamento;
        this.codigoFuncionario = codigoFuncionario;
        this.nome = nome;
        this.data = data;
        this.entrada1 = entrada1;
        this.saida1 = saida1;
        this.entrada2 = entrada2;
        this.saida2 = saida2;
        this.descricao = descricao;
        this.status = status;
        this.inclusao = inclusao;
        this.respostaJustificativa = respostaJustificativa;
    }

    public String negar() {
        Banco banco = new Banco();
        dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        Metodos.setLogInfo("Negar Abono - Funcionário: \""+nome+"\" Data: "+dataStr);

        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        banco.updateSolicitacaoAbono(cod, false, respostaJustificativa, usuarioBean.getUsuario().getLogin());
        banco.fecharConexao();
        return "solicitacaoAbonoBean";
    }

    public SolicitacaoAbono() {
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(String entrada1) {
        this.entrada1 = entrada1;
    }

    public String getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(String entrada2) {
        this.entrada2 = entrada2;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(Integer codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public String getSaida1() {
        return saida1;
    }

    public void setSaida1(String saida1) {
        this.saida1 = saida1;
    }

    public String getSaida2() {
        return saida2;
    }

    public void setSaida2(String saida2) {
        this.saida2 = saida2;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Date getInclusao() {
        return inclusao;
    }

    public void setInclusao(Date inclusao) {
        this.inclusao = inclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPontosSolicitados() {
        String pontos = "";

        if (entrada1 != null) {
            String[] ponto = entrada1.split(" ");
            pontos += ponto[1].substring(0, ponto[1].length() - 5) + " ";
        }
        if (saida1 != null) {
            String[] ponto = saida1.split(" ");
            pontos += ponto[1].substring(0, ponto[1].length() - 5) + " ";
        }
        if (entrada2 != null) {
            String[] ponto = entrada2.split(" ");
            pontos += ponto[1].substring(0, ponto[1].length() - 5) + " ";
        }
        if (saida2 != null) {
            String[] ponto = saida2.split(" ");
            pontos += ponto[1].substring(0, ponto[1].length() - 5);
        }
        if (pontos.endsWith(" ")) {
            pontos = pontos.substring(0, pontos.length() - 1);
        }
        return pontos.replace(" ", ", ");
    }

    public void setPontosSolicitados(String pontosSolicitados) {
        this.pontosSolicitados = pontosSolicitados;
    }

    public List<SelectItem> getPontosSolicitadosList() {

        pontosSolicitadosList = new ArrayList<SelectItem>();

        if (entrada1 != null) {
            String[] ponto = entrada1.split(" ");
            pontosSolicitadosList.add(new SelectItem(ponto[1].substring(0, ponto[1].length() - 5), ponto[1].substring(0, ponto[1].length() - 5)));
        }
        if (saida1 != null) {
            String[] ponto = saida1.split(" ");
            pontosSolicitadosList.add(new SelectItem(ponto[1].substring(0, ponto[1].length() - 5), ponto[1].substring(0, ponto[1].length() - 5)));
        }
        if (entrada2 != null) {
            String[] ponto = entrada2.split(" ");
            pontosSolicitadosList.add(new SelectItem(ponto[1].substring(0, ponto[1].length() - 5), ponto[1].substring(0, ponto[1].length() - 5)));
        }
        if (saida2 != null) {
            String[] ponto = saida2.split(" ");
            pontosSolicitadosList.add(new SelectItem(ponto[1].substring(0, ponto[1].length() - 5), ponto[1].substring(0, ponto[1].length() - 5)));
        }

        return pontosSolicitadosList;
    }

    public void setPontosSolicitadosList(List<SelectItem> pontosSolicitadosList) {
        this.pontosSolicitadosList = pontosSolicitadosList;
    }

    public String getRespostaJustificativa() {
        return respostaJustificativa;
    }

    public void setRespostaJustificativa(String respostaJustificativa) {
        this.respostaJustificativa = respostaJustificativa;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataStr = sdf.format(data.getTime());
        return dataStr;
    }

    public Boolean getIsPendente() {
        if (status.replace(" ", "").equals("Pendente")) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsPendente(Boolean isPendente) {
        this.isPendente = isPendente;
    }
}
