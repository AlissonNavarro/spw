/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class AbonoEmMassaADD implements Serializable {

    private Date dataInicio;
    private String horaInicio;
    private Date dataFim;
    private String horaFim;
    private Integer cod_justificativa;
    private List<SelectItem> justificativasList;
    private String descricaoJustificativa;
    private String  nomeFuncionario;

    public AbonoEmMassaADD(){
        justificativasList = new ArrayList<SelectItem>();
        descricaoJustificativa = new String();
        horaInicio = "00:00";
        horaFim = "23:59";
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

    public String getDescricaoJustificativa() {
        return descricaoJustificativa;
    }

    public void setDescricaoJustificativa(String descricaoJustificativa) {
        this.descricaoJustificativa = descricaoJustificativa;
    }

    public List<SelectItem> getJustificativasList() {
        return justificativasList;
    }

    public void setJustificativasList(List<SelectItem> justificativasList) {
        this.justificativasList = justificativasList;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getCod_justificativa() {
        return cod_justificativa;
    }

    public void setCod_justificativa(Integer cod_justificativa) {
        this.cod_justificativa = cod_justificativa;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    
    
}
