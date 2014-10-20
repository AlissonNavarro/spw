/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

import java.util.Date;

/**
 *
 * @author ancouto
 */
public class Turnos {
    private int schclassid;
    private Date startTime;
    private Date endTime;
    private int sdays;
    private int edays;
    private int overtime;
    
    public Turnos() {
        
    }
    
    public Turnos(int schclassid, Date startTime, Date endTime, int sdays, int edays, int overtime) {
        this.schclassid = schclassid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sdays = sdays;
        this.edays = edays;
        this.overtime = overtime;
    }

    public int getSchclassid() {
        return schclassid;
    }

    public void setSchclassid(int schclassid) {
        this.schclassid = schclassid;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getSdays() {
        return sdays;
    }

    public void setSdays(int sdays) {
        this.sdays = sdays;
    }

    public int getEdays() {
        return edays;
    }

    public void setEdays(int edays) {
        this.edays = edays;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }
  
}
