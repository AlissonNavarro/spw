/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class SituacaoPonto implements Serializable {

    private String horaPonto;
    private String local;
    private String situacao;
    private Ponto horaPontoObj;
    //Caso o ponto tenha sido abonado
    private String justificativaAbono;
    private Boolean isAbonado;
    private Boolean isPreAssinalado;
    private Boolean isDescanso;
    private List<SelectItem> liberacaoList;

    public SituacaoPonto(Ponto ponto, String local, String situacao_) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dataCorrente = sdf.format(ponto.getPonto().getTime());
        horaPonto = dataCorrente;
        this.local = local;
        situacao = situacao_;
        horaPontoObj = ponto;
        liberacaoList = new ArrayList<SelectItem>();
    }

    public void liberarHorario(ActionEvent evento) {
        String cod_funcionario = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_funcionario");
        String nome = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nome");
        Integer tipoRegistro = horaPontoObj.getTipoRegistro();
        SimpleDateFormat sdfDiaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String registro = sdfDiaHora.format(horaPontoObj.getPonto());
        Banco b = new Banco();
        Metodos.setLogInfo("Liberar horário - Horário: " + registro + " - Funcionário: " + cod_funcionario + " - " + nome);
        
      
        if (horaPontoObj.getIsAbonado()) {
            b.alterarRegistroPontoAbonado(cod_funcionario, registro, tipoRegistro);
        } else {
            b.alterarRegistroPonto(cod_funcionario, registro, tipoRegistro);
        }
        inserirBackupBanco();
        
    }
    
    public void inserirBackupBanco(){
        String codFuncionario = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_funcionario");
        String nomeFuncionario = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nome");
        SimpleDateFormat sdfDiaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String registroAlterado = sdfDiaHora.format(horaPontoObj.getPonto());
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        //usuarioBean.getLogin() + " " + usuarioBean.getUsuario().getNome()
        String codRequerente = usuarioBean.getLogin();
        String nomeRequerente = usuarioBean.getUsuario().getNome();
        Banco b = new Banco();
        String liberacao;
        Integer tipoRegistro = horaPontoObj.getTipoRegistro();
        if (tipoRegistro == 0){
            liberacao = "Sem alteração";
            
        } else if (tipoRegistro == 1) {
            liberacao = "Turno 1 - Entrada";
        } else if (tipoRegistro == 2) {
            liberacao = "Turno 1 - Saída";
        } else if (tipoRegistro == 3) {
            liberacao = "Turno 2 - Entrada";
        } else {
            liberacao = "Turno 2 - Saída";
        }
        b.insertLog(codFuncionario, nomeFuncionario, registroAlterado, codRequerente, nomeRequerente, situacao, liberacao);
        
        
    }

    public Boolean getIsDescanso() {
        return isDescanso;
    }

    public void setIsDescanso(Boolean isDescanso) {
        this.isDescanso = isDescanso;
    }

    public SituacaoPonto() {
    }

    public Boolean getIsAbonado() {
        return isAbonado;
    }

    public void setIsAbonado(Boolean isAbonado) {
        this.isAbonado = isAbonado;
    }

    public String getHoraPonto() {
        return horaPonto;
    }

    public void setHoraPonto(String horaPonto) {
        this.horaPonto = horaPonto;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Ponto getHoraPontoObj() {
        return horaPontoObj;
    }

    public void setHoraPontoObj(Ponto horaPontoObj) {
        this.horaPontoObj = horaPontoObj;
    }

    public String getJustificativaAbono() {
        return justificativaAbono;
    }

    public void setJustificativaAbono(String justificativaAbono) {
        this.justificativaAbono = justificativaAbono;
    }

    public Boolean getIsPreAssinalado() {
        return isPreAssinalado;
    }

    public void setIsPreAssinalado(Boolean isPreAssinalado) {
        this.isPreAssinalado = isPreAssinalado;
    }

    public List<SelectItem> getLiberacaoList() {
        return liberacaoList;
    }

    public void setLiberacaoList(List<SelectItem> liberacaoList) {
        this.liberacaoList = liberacaoList;
    }
}
