/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.io.Serializable;

/**
 *
 * @author amsgama
 */
public class AFDTDetalhe implements Serializable {

    private String cod;
    private final String TIPO_REGISTRO = "2";
    private String dataMarcacao;
    private String horaMarcacao;
    private String pis;
    private String numRep;
    private String tipoMarcacao;
    private String numJornada;
    private String tipoRegistro;
    private String motivo;

    public String gerarDetalhe() {
        String saida;
        saida = geraZeros(cod, 9);
        saida += TIPO_REGISTRO;
        saida += somentoNumero(dataMarcacao);
        saida += somentoNumero(horaMarcacao);
        saida += geraZeros(pis, 12);
        saida += geraEspaco(numRep, 17);
        saida += tipoMarcacao;
        saida += geraZeros(numJornada, 2);
        saida += tipoRegistro;
        saida += geraEspaco(motivo, 100);
        return saida;
    }

    private String somentoNumero(String string) {
        String saida = string.replace("/", "").replace(":", "");
        return saida;
    }

    private String geraEspaco(String string, Integer tamanho) {

        String saida = string;

        Integer tamanhoEntranda = 0;
        if (saida != null) {
            tamanhoEntranda = string.length();
        } else {
            saida = "";
        }

        Integer espacos = tamanho - tamanhoEntranda;

        for (int i = 0; i < espacos; i++) {
            saida += " ";
        }
        return saida;

    }

    private String geraZeros(String cod, Integer tamanho) {

        String saida = cod;
        Integer tamanhoEntranda = 0;

       if (saida != null) {
            tamanhoEntranda = cod.length();
        } else {
            saida = "";
        }

        Integer espacos = tamanho - tamanhoEntranda;

        for (int i = 0; i < espacos; i++) {
            saida = "0" + saida;
        }
        return saida;

    }

    public static AFDTDetalhe copy(AFDTDetalhe detalhe) {
        AFDTDetalhe afdtDetalhe = new AFDTDetalhe();

        afdtDetalhe.setDataMarcacao(detalhe.getDataMarcacao());
        afdtDetalhe.setHoraMarcacao(detalhe.getHoraMarcacao());
        afdtDetalhe.setMotivo(detalhe.getMotivo());        
        afdtDetalhe.setNumJornada(detalhe.getNumJornada());
        afdtDetalhe.setNumRep(detalhe.getNumRep());
        afdtDetalhe.setPis(detalhe.getPis());
        afdtDetalhe.setTipoMarcacao(detalhe.getTipoMarcacao());
        afdtDetalhe.setTipoRegistro(detalhe.getTipoRegistro());

        return afdtDetalhe;

    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDataMarcacao() {
        return dataMarcacao;
    }

    public void setDataMarcacao(String dataMarcacao) {
        this.dataMarcacao = dataMarcacao;
    }

    public String getHoraMarcacao() {
        return horaMarcacao;
    }

    public void setHoraMarcacao(String horaMarcacao) {
        this.horaMarcacao = horaMarcacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNumJornada() {
        return numJornada;
    }

    public void setNumJornada(String numJornada) {
        this.numJornada = numJornada;
    }

    public String getNumRep() {
        return numRep;
    }

    public void setNumRep(String numRep) {
        this.numRep = numRep;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getTipoMarcacao() {
        return tipoMarcacao;
    }

    public void setTipoMarcacao(String tipoMarcacao) {
        this.tipoMarcacao = tipoMarcacao;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}
