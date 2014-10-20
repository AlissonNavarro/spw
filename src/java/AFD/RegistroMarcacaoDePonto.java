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
public class RegistroMarcacaoDePonto extends Registro{

    private Integer nsr;
    private Integer nroRelogio;
    private Date dataEHoraMarcacao;
    private String pis;

    public Integer getNroRelogio() {
        return nroRelogio;
    }

    public void setNroRelogio(Integer nroRelogio) {
        this.nroRelogio = nroRelogio;
    }

    

    public Date getDataEHoraMarcacao() {
        return dataEHoraMarcacao;
    }

    public void setDataEHoraMarcacao(Date dataEHoraMarcacao) {
        this.dataEHoraMarcacao = dataEHoraMarcacao;
    }

    public Integer getNsr() {
        return nsr;
    }

    public void setNsr(Integer nsr) {
        this.nsr = nsr;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    

    

    

}
