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
public class DiaHoraExtra implements Serializable {

    private Integer userid;
    private Date data;
    private Integer cod_categoria;
    private String justificativa;

    public Integer getCod_categoria() {
        return cod_categoria;
    }

    public void setCod_categoria(Integer cod_categoria) {
        this.cod_categoria = cod_categoria;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
