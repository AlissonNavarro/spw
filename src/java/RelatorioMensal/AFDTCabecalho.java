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
public class AFDTCabecalho implements Serializable {

    private final String cod = "000000001";
    private final String TIPO_REGISTRO = "1";
    private Integer tipo_identificador;
    private String cnpj;
    private String cei;
    private String razao_social;
    private String dataInicial;
    private String dataFinal;
    private String dataGeracao;
    private String horaGeracao;

    public String gerarCabecalho() {
        String saida;
        saida = geraZeros(cod, 9);
        saida += TIPO_REGISTRO;
        saida += tipo_identificador;
        saida += geraEspaco(cnpj, 14);
        saida += geraEspaco(cei, 12);
        saida += geraEspaco(razao_social, 150);
        saida += dataInicial;
        saida += somentoNumero(dataFinal);
        saida += somentoNumero(dataGeracao);
        saida += somentoNumero(horaGeracao);
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

    private String somentoNumero(String string) {
        String saida = string.replace("/", "").replace(":", "");
        return saida;
    }

    public String getCei() {
        return cei;
    }

    public void setCei(String cei) {
        this.cei = cei;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(String dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getHoraGeracao() {
        return horaGeracao;
    }

    public void setHoraGeracao(String horaGeracao) {
        this.horaGeracao = horaGeracao;
    }

    public String getRazao_social() {
        return razao_social;
    }

    public void setRazao_social(String razao_social) {
        this.razao_social = razao_social;
    }

    public Integer getTipo_identificador() {
        return tipo_identificador;
    }

    public void setTipo_identificador(Integer tipo_identificador) {
        this.tipo_identificador = tipo_identificador;
    }
}
