/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class DescolamentoTemporario implements Serializable {

    private Integer userid;
    private Date dataInicio;
    private Date dataFim;
    private Boolean isDiaExtra;
    private Boolean folga;
    private Integer schClassId;

    public DescolamentoTemporario(Integer userid, Date dataInicio, Date dataFim, Boolean isDiaExtra, Boolean folga) {
        this.userid = userid;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.isDiaExtra = isDiaExtra;
        this.folga = folga;
    }

    public DescolamentoTemporario(Integer userid, Date dataInicio, Date dataFim, Boolean isDiaExtra, Boolean folga, Integer schClassId) {
        this.userid = userid;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.isDiaExtra = isDiaExtra;
        this.folga = folga;
        this.schClassId = schClassId;
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

    public Boolean getFolga() {
        return folga;
    }

    public void setFolga(Boolean folga) {
        this.folga = folga;
    }

    public Boolean getIsDiaExtra() {
        return isDiaExtra;
    }

    public void setIsDiaExtra(Boolean isDiaExtra) {
        this.isDiaExtra = isDiaExtra;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getSchClassId() {
        return schClassId;
    }

    public void setSchClassId(Integer schClassId) {
        this.schClassId = schClassId;
    }
    
    
}
