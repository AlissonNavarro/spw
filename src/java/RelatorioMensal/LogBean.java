/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amsgama
 */
public class LogBean {

    private Date dataLog;
    private Locale objLocale;

    public LogBean() {
        dataLog = new Date();
        objLocale = new Locale("pt", "BR");
        Locale.setDefault(new Locale("pt", "BR"));
    }

    public void downloadLog() {
        if (dataLog != null) {
            String log = convertDateLog(dataLog);
            try {
                Boolean existe = Impressao.popUpLog(log);


                if (!existe) {
                    FacesMessage msgErro = new FacesMessage("Não existe log para a data selecionada!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }else{
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    Metodos.Metodos.setLogInfo("Download de log de operações - Dia: "+sf.format(dataLog));
                }
            } catch (IOException ex) {
                System.out.println("RelatorioMensal: downloadLog 1: "+ex);
                //Logger.getLogger(LogBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.out.println("RelatorioMensal: downloadLog 2: "+ex);
                //Logger.getLogger(LogBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione uma data para visualizar o log!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    private String convertDateLog(Date data) {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("yyyy.MM.dd");
        String retorno = sdfSemana.format(data.getTime());       
        return retorno;
    }

    public Date getDataLog() {
        return dataLog;
    }

    public void setDataLog(Date dataLog) {
        this.dataLog = dataLog;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }
}
