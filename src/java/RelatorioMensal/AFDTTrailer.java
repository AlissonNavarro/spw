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
public class AFDTTrailer implements Serializable{

    private String cod ;
    private final String TIPO_REGISTRO = "9";

    public String gerarTrailer() {
        String saida;
        saida = geraZeros(cod, 9);
        saida += TIPO_REGISTRO;
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


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

}
