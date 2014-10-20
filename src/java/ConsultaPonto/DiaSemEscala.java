/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class DiaSemEscala implements Serializable{

    public Date data;
    public List<Date> horasList;

    public DiaSemEscala(Date data, List<Date> horasList) {
        this.data = data;
        this.horasList = horasList;
    }

    public String getPontosBatidos() {
        String pontosBatidos = "";
        int i = 0;
        for (Iterator<Date> it = horasList.iterator(); it.hasNext();) {
            Date dataItem = it.next();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String dataSrt = sdf.format(dataItem.getTime());
            i++;
            if (i % 19 == 0) {
                pontosBatidos += "\n";
            }
            if (it.hasNext()) {
                pontosBatidos += dataSrt + " - ";
            } else {
                pontosBatidos += dataSrt;
            }
        }
        return pontosBatidos;
    }

    public String getDataSrt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataSrt = sdf.format(data.getTime());
        return dataSrt;
    }

    public DiaSemEscala() {
        horasList = new ArrayList<Date>();
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public List<Date> getHorasList() {
        return horasList;
    }

    public void setHorasList(Date horasList) {
        this.horasList.add(horasList);
    }

    public void setHorasList(List<Date> horasList) {
        this.horasList = horasList;
    }
}
