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
public class RegistroIdentificacaoEmpresa extends Registro {

    private Integer nsr;
    private Date dataEHoraGravacao;
    private Boolean isCnpj;
    private Long identificador;
    private Integer CEI;
    private String nome;
    private String local;

    public Integer getCEI() {
        return CEI;
    }

    public void setCEI(Integer CEI) {
        this.CEI = CEI;
    }

    public Date getDataEHoraGravacao() {
        return dataEHoraGravacao;
    }

    public void setDataEHoraGravacao(Date dataEHoraGravacao) {
        this.dataEHoraGravacao = dataEHoraGravacao;
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
    }

    public Boolean getIsCnpj() {
        return isCnpj;
    }

    public void setIsCnpj(Boolean isCnpj) {
        this.isCnpj = isCnpj;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNsr() {
        return nsr;
    }

    public void setNsr(Integer nsr) {
        this.nsr = nsr;
    }






}
