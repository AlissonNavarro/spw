/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author Alexandre
 */
public class DiaEmAberto {

    private Integer cod_funcionario;
    private String nomeFuncionario;
    private String departamento;
    private Date data;
    private String dataStr;
    private String pontosAbertos;
    private Boolean marked;
    private String justificativa;
    private Boolean hasJustificativaSelecionada;
    private Boolean isDeletavel;
    private String descricao;

    public DiaEmAberto(Integer cod_funcionario, String nomeFuncionario, String departamento, Date data, String pontosAbertos) {
        this.cod_funcionario = cod_funcionario;
        this.nomeFuncionario = nomeFuncionario;
        this.departamento = departamento;
        this.data = data;
        this.pontosAbertos = pontosAbertos;
        marked = false;
        hasJustificativaSelecionada = true;
        isDeletavel = false;
    }

    public DiaEmAberto() {
    }

    public void aceitar() {
        List<DiaEmAberto> diasemAbertoSelecionadosList = new ArrayList<DiaEmAberto>();
        String[] horasAbonoArray = pontosAbertos.split(" - ");
        for (int i = 0; i < horasAbonoArray.length; i++) {
            String horaAbono = horasAbonoArray[i];
            Long registroLong = horaToLong(horaAbono);
            Date date = zeraData(data);
            Long dataLong = date.getTime();
            DiaEmAberto diaEmAbertoCopy = new DiaEmAberto();
            diaEmAbertoCopy = copy();
            diaEmAbertoCopy.setData(new Date(registroLong + dataLong));
            diaEmAbertoCopy.setJustificativa("58");
            diasemAbertoSelecionadosList.add(diaEmAbertoCopy);

        }
        Banco b = new Banco();
        dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        Metodos.setLogInfo("Aceitar Abono - Funcionário: \"" + nomeFuncionario + "\" Data: " + dataStr);
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        b.abonarDiaEmAbertoEmMassa(diasemAbertoSelecionadosList, usuarioBean.getUsuario().getLogin());
        b.fecharConexao();
    }

    public void negar() {
        Banco b = new Banco();
        dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        Metodos.setLogInfo("Negar Abono - Funcionário: \"" + nomeFuncionario + "\" Data: " + dataStr);
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        b.updateNegarAbono(cod_funcionario, usuarioBean.getUsuario().getLogin(), data);
        b.fecharConexao();

    }

    public DiaEmAberto copy() {
        DiaEmAberto diaEmAberto = new DiaEmAberto();
        diaEmAberto.setCod_funcionario(cod_funcionario);
        diaEmAberto.setData(data);
        diaEmAberto.setDataStr(dataStr);
        diaEmAberto.setDepartamento(departamento);
        diaEmAberto.setHasJustificativaSelecionada(hasJustificativaSelecionada);
        diaEmAberto.setJustificativa(justificativa);
        diaEmAberto.setMarked(marked);
        diaEmAberto.setNomeFuncionario(nomeFuncionario);
        diaEmAberto.setPontosAbertos(pontosAbertos);
        diaEmAberto.setIsDeletavel(isDeletavel);
        return diaEmAberto;
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getPontosAbertos() {
        return pontosAbertos;
    }

    public void setPontosAbertos(String pontosAbertos) {
        this.pontosAbertos = pontosAbertos;
    }

    public String getDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataStr = sdf.format(data.getTime());
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Boolean getHasJustificativaSelecionada() {
        return hasJustificativaSelecionada;
    }

    public void setHasJustificativaSelecionada(Boolean hasJustificativaSelecionada) {
        this.hasJustificativaSelecionada = hasJustificativaSelecionada;
    }

    public void isMotivoDeselecionado() {
        System.out.println("justificativa: "+justificativa);
        if (justificativa == null || justificativa.equals("-1")) {
            hasJustificativaSelecionada = true;
            marked = false;
        } else {
            hasJustificativaSelecionada = false;
            marked = true;
        }
    }

    public void aceitarSemJustificativa() {
        marked = true;
    }

    /**
     * @return the isDeletavel
     */
    public Boolean getIsDeletavel() {
        return isDeletavel;
    }

    /**
     * @param isDeletavel the isDeletavel to set
     */
    public void setIsDeletavel(Boolean isDeletavel) {
        this.isDeletavel = isDeletavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    
    private Date zeraData(Date dataEntrada) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_ = sdf.format(dataEntrada.getTime());
        Date date = new Date();
        try {
            date = sdf.parse(data_);
        } catch (ParseException ex) {
            System.out.println("Abono: zeraData: "+ex);
            //Logger.getLogger(ConsultaDiaEmAbertoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
}
