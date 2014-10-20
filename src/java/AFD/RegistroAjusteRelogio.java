/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package AFD;

import java.util.Date;

/**
 *
 * @author amvboas
 */
public class RegistroAjusteRelogio extends Registro{

    private Integer nsr;
    private Date dataEHoraAntesAjuste;
    private Date dataEHoraAjustada;

    public Date getDataEHoraAjustada() {
        return dataEHoraAjustada;
    }

    public void setDataEHoraAjustada(Date dataEHoraAjustada) {
        this.dataEHoraAjustada = dataEHoraAjustada;
    }

    public Date getDataEHoraAntesAjuste() {
        return dataEHoraAntesAjuste;
    }

    public void setDataEHoraAntesAjuste(Date dataEHoraAntesAjuste) {
        this.dataEHoraAntesAjuste = dataEHoraAntesAjuste;
    }

    public Integer getNsr() {
        return nsr;
    }

    public void setNsr(Integer nsr) {
        this.nsr = nsr;
    }

    

}
