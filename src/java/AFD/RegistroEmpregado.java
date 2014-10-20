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
public class RegistroEmpregado extends Registro {

    private Integer nsr;
    private Date dataEHoraAlteracao;
    private char tipoDaOperacao;
    private String pis;
    private String nomeEmpregado;

    public Date getDataEHoraAlteracao() {
        return dataEHoraAlteracao;
    }

    public void setDataEHoraAlteracao(Date dataEHoraAlteracao) {
        this.dataEHoraAlteracao = dataEHoraAlteracao;
    }

    public String getNomeEmpregado() {
        return nomeEmpregado;
    }

    public void setNomeEmpregado(String nomeEmpregado) {
        this.nomeEmpregado = nomeEmpregado;
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

    public char getTipoDaOperacao() {
        return tipoDaOperacao;
    }

    public void setTipoDaOperacao(char tipoDaOperacao) {
        this.tipoDaOperacao = tipoDaOperacao;
    }

    
}
