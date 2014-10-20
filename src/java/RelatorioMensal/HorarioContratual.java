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
public class HorarioContratual implements Serializable {

    private String cod;
    private final String TIPO_REGISTRO = "2";
    private String cod_horario;
    private String entrada1;
    private String saida1;
    private String entrada2;
    private String saida2;
    private String entradaD1;
    private String saidaD1;
    private String entradaD2;
    private String saidaD2;

    public String gerarHoraContratual() {
        String saida;
        saida = geraZeros(cod_horario, 9);
        saida += TIPO_REGISTRO;
        saida += geraEspaco(cod_horario, 4);
        saida += somentoNumero(entrada1);
        saida += somentoNumero(saida1);
        saida += somentoNumero(entrada2);
        saida += somentoNumero(saida2);
        saida += somentoNumero(entradaD1);
        saida += somentoNumero(saidaD1);
        saida += somentoNumero(entradaD2);
        saida += somentoNumero(saidaD2);

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

    public HorarioContratual() {
        entrada1 = "";
        saida1 = "";
        entrada2 = "";
        saida2 = "";
        entradaD1 = "";
        saidaD1 = "";
        entradaD2 = "";
        saidaD2 = "";
    }

    public Boolean isEqual(HorarioContratual horarioContratual){
        return (entrada1.equals(horarioContratual.getEntrada1())&&
                saida1.equals(horarioContratual.getSaida1())&&
                entrada2.equals(horarioContratual.getEntrada2())&&
                saida2.equals(horarioContratual.getSaida2())&&
                entradaD1.equals(horarioContratual.getEntradaD1())&&
                saidaD1.equals(horarioContratual.getSaidaD1())&&
                entradaD2.equals(horarioContratual.getEntradaD2())&&
                saidaD2.equals(horarioContratual.getSaidaD2()));
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCod_horario() {
        return cod_horario;
    }

    public void setCod_horario(String cod_horario) {
        this.cod_horario = cod_horario;
    }

    public String getEntrada1() {
        return entrada1;
    }

    public void setEntrada1(String entrada1) {
        this.entrada1 = entrada1;
    }

    public String getEntrada2() {
        return entrada2;
    }

    public void setEntrada2(String entrada2) {
        this.entrada2 = entrada2;
    }

    public String getSaida1() {
        return saida1;
    }

    public void setSaida1(String saida1) {
        this.saida1 = saida1;
    }

    public String getSaida2() {
        return saida2;
    }

    public void setSaida2(String saida2) {
        this.saida2 = saida2;
    }

    public String getEntradaD1() {
        return entradaD1;
    }

    public void setEntradaD1(String entradaD1) {
        this.entradaD1 = entradaD1;
    }

    public String getEntradaD2() {
        return entradaD2;
    }

    public void setEntradaD2(String entradaD2) {
        this.entradaD2 = entradaD2;
    }

    public String getSaidaD1() {
        return saidaD1;
    }

    public void setSaidaD1(String saidaD1) {
        this.saidaD1 = saidaD1;
    }

    public String getSaidaD2() {
        return saidaD2;
    }

    public void setSaidaD2(String saidaD2) {
        this.saidaD2 = saidaD2;
    }


}
