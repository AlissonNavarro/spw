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
public class Cabecalho extends Registro {

    private boolean isCnpj;
    private long identificador;
    private Integer CEI;
    private String nome;
    private Integer numeroREP;
    private Date dataInicial;
    private Date dataFinal;
    private Date dataEHoraGeracaoArquivo;

    public Integer getCEI() {
        return CEI;
    }

    public void setCEI(Integer CEI) {
        this.CEI = CEI;
    }

    public long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(long identificador) {
        this.identificador = identificador;
    }

    public boolean getIsCnpj() {
        return isCnpj;
    }

    public void setIsCnpj(boolean isCnpj) {
        this.isCnpj = isCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNumeroREP() {
        return numeroREP;
    }

    public void setNumeroREP(Integer numeroREP) {
        this.numeroREP = numeroREP;
    }

    public Date getDataEHoraGeracaoArquivo() {
        return dataEHoraGeracaoArquivo;
    }

    public void setDataEHoraGeracaoArquivo(Date dataEHoraGeracaoArquivo) {
        this.dataEHoraGeracaoArquivo = dataEHoraGeracaoArquivo;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    


    

}

