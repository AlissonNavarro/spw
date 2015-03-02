/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */

//testeo

public class AbonarBean implements Serializable {

	
    private String justificativa;
    private String detalheJustificativa;
    private String hora;
    private Integer codigo;
    private List<SolicitacaoAbono> solicitacaoAbonoList;
    private List<String> horaAbonoList;
    private List<SelectItem> horaSelectAbonoList;
    private List<SelectItem> justificativaList;
    private SolicitacaoAbono solicitacaoAbono;
    private UsuarioBean usuarioBean;
    private String inclusao;
    private String departamento;
    private String nome;
    private String data;
    private String pontosSolicitados;
    private String descricao;

    public AbonarBean() {

        String cod = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_solicitacao");
        codigo = Integer.parseInt(cod);

        Banco banco = new Banco();

        horaAbonoList = new ArrayList<String>();

        justificativaList = new ArrayList<SelectItem>();
        justificativaList = banco.consultaJustificativasAbono();

        banco = new Banco();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        solicitacaoAbonoList = new ArrayList<SolicitacaoAbono>();
        solicitacaoAbonoList = banco.consultaSolicitacoesAbono(usuarioBean.getUsuario());

        solicitacaoAbono = new SolicitacaoAbono();
        for (Iterator<SolicitacaoAbono> it = solicitacaoAbonoList.iterator(); it.hasNext();) {
            SolicitacaoAbono solicitacao = it.next();
            if (solicitacao.getCod().equals(codigo)) {
                solicitacaoAbono = solicitacao;
            }
        }

        horaSelectAbonoList = new ArrayList<SelectItem>();
        for (Iterator<SelectItem> it = solicitacaoAbono.getPontosSolicitadosList().iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            horaSelectAbonoList.add(selectItem);
        }

        nome = solicitacaoAbono.getNome();
        departamento = solicitacaoAbono.getDepartamento();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        data = (String) sdf.format(solicitacaoAbono.getData());
        descricao = solicitacaoAbono.getDescricao();

    }

    public String enviar() {

        Banco banco = new Banco();

        Integer justificaticaInt = Integer.parseInt(justificativa);

        if (justificaticaInt != -1) {
            Long registroLong;
            Long dataInicioLong = solicitacaoAbono.getData().getTime();
            boolean ok = false;            
            boolean isDescricaoObrigatoria = banco.isDescricaoObrigatoria(justificaticaInt);
            if (!(isDescricaoObrigatoria && detalheJustificativa.equals(""))) {
                if (horaAbonoList.isEmpty()) {
                    FacesMessage msgErro = new FacesMessage("Você deve marcar pelo menos um horário!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                }
                for (int i = 0; i < horaAbonoList.size(); i++) {
                    registroLong = horaToLong(horaAbonoList.get(i));
                    if (registroLong != null) {
                        if (!(isDescricaoObrigatoria && detalheJustificativa.equals(""))) {
                            ok = banco.abonar(solicitacaoAbono.getCodigoFuncionario(),
                                    new Date(dataInicioLong + registroLong),
                                    justificaticaInt, detalheJustificativa, usuarioBean.getUsuario().getLogin(),
                                    solicitacaoAbono.getCod());
                            if (ok) {
                                String justificativaStr = Metodos.buscaRotulo(justificativa.toString(), justificativaList);
                                Metodos.setLogInfo("Aprovar Abono - Funcionário: \"" + nome + "\" Justificativa: " + justificativaStr);
                            }
                        }
                    } else {
                        FacesMessage msgErro = new FacesMessage("Hora inválida!");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    }
                }
                if (!horaAbonoList.isEmpty() && ok) {
                    SolicitacaoAbonoBean solicitacaoAbonoBean = ((SolicitacaoAbonoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("solicitacaoAbonoBean"));
                    solicitacaoAbonoBean.atualizar();
                    return "navegarAbono";
                } else if (horaAbonoList.isEmpty()) {
                    FacesMessage msgErro = new FacesMessage("Você deve selecionar pelo menos uma solicitação de abono!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                } else {
                    FacesMessage msgErro = new FacesMessage("Erro ao inserir o abono!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                }
            } else {
                FacesMessage msgErro = new FacesMessage("A justificativa escolhida requer uma descrição!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                return "";
            }

        } else {
            FacesMessage msgErro = new FacesMessage("Selecione uma categoria!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return "";
        }
    }

    public void addRegistro() {
        if (horaToLong(hora) != null) {
            SelectItem selectItem = new SelectItem(hora, hora);
            horaSelectAbonoList.add(selectItem);
        }
    }

    private Long horaToLong(String hora) {
        Long saida = null;
        try {
            hora += ":00";
            Time time = Time.valueOf(hora);
            saida = time.getTime() - 10800000;
        } catch (Exception e) {
        }
        return saida;
    }

    private String horaSegundos(String hora) {
        return hora += ":00";
    }

    public List<String> getHoraAbonoList() {
        return horaAbonoList;
    }

    public void setHoraAbonoList(List<String> horaAbonoList) {
        this.horaAbonoList = horaAbonoList;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<SelectItem> getJustificativaList() {
        return justificativaList;
    }

    public void setJustificativaList(List<SelectItem> justificativaList) {
        this.justificativaList = justificativaList;
    }

    public List<SelectItem> getHoraSelectAbonoList() {
        return horaSelectAbonoList;
    }

    public void setHoraSelectAbonoList(List<SelectItem> horaSelectAbonoList) {
        this.horaSelectAbonoList = horaSelectAbonoList;
    }

    public String getDetalheJustificativa() {
        return detalheJustificativa;
    }

    public void setDetalheJustificativa(String detalheJustificativa) {
        this.detalheJustificativa = detalheJustificativa;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getInclusao() {
        return inclusao;
    }

    public void setInclusao(String inclusao) {
        this.inclusao = inclusao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPontosSolicitados() {
        return pontosSolicitados;
    }

    public void setPontosSolicitados(String pontosSolicitados) {
        this.pontosSolicitados = pontosSolicitados;
    }
}
