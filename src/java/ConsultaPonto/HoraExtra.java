/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alexandre
 */
public class HoraExtra  implements Serializable{

    private Date data;
    private String imagem;
    private String dataString;
    private String saldo;
    private Boolean hasHoraExtra;

    public HoraExtra(Date data) {
        this.data = data;     
    }

    public String getDataSrt() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataSrt = sdf.format(data.getTime());
        return dataSrt;
    }

    public HoraExtra() {      
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public Boolean getHasHoraExtra() {
        return hasHoraExtra;
    }

    public void setHasHoraExtra(Boolean hasHoraExtra) {
        this.hasHoraExtra = hasHoraExtra;
    }
}
