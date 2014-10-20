/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

/**
 *
 * @author amsgama
 */
public class ACJEFDetalhe {

    private String cod;
    private final String TIPO_REGISTRO = "3";
    private String pis;
    private String dataInicioJornada;
    private String horaEntrada;
    private String cod_horario;
    private String horasDiurnasNaoExtraordinarias;
    private String horasNoturnasNaoExtraordinarias;
    private String horasExtra1;
    private String percentualHE1;
    private String modalidadeHE1;
    private String horasExtra2;
    private String percentualHE2;
    private String modalidadeHE2;
    private String horasExtra3;
    private String percentualHE3;
    private String modalidadeHE3;
    private String horasExtra4;
    private String percentualHE4;
    private String modalidadeHE4;
    private String faltaAtraso;
    private String sinalHora;
    private String saldoHoras;

    public String gerarDetalhe() {
        String saida;
        saida = geraZeros(cod, 9);
        saida += TIPO_REGISTRO;
        saida += geraZeros(pis, 12);
        saida += somentoNumero(dataInicioJornada);
        saida += somentoNumero(horaEntrada);
        saida += geraEspaco(cod_horario, 4);
        saida += geraEspaco(horasDiurnasNaoExtraordinarias, 4);
        saida += geraEspaco(horasNoturnasNaoExtraordinarias, 4);
        saida += geraEspaco(horasExtra1, 4);
        saida += geraEspaco(percentualHE1, 4);
        saida += geraEspaco(modalidadeHE1, 1);
        saida += geraEspaco(horasExtra2, 4);
        saida += geraEspaco(percentualHE2, 4);
        saida += geraEspaco(modalidadeHE2, 1);
        saida += geraEspaco(horasExtra3, 4);
        saida += geraEspaco(percentualHE3, 4);
        saida += geraEspaco(modalidadeHE3, 1);
        saida += geraEspaco(horasExtra4, 4);
        saida += geraEspaco(percentualHE4, 4);
        saida += geraEspaco(modalidadeHE4, 1);
        saida += somentoNumero(faltaAtraso);
        saida += geraEspaco(sinalHora, 1);
        saida += somentoNumero(saldoHoras);

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

    public String getDataInicioJornada() {
        return dataInicioJornada;
    }

    public void setDataInicioJornada(String dataInicioJornada) {
        this.dataInicioJornada = dataInicioJornada;
    }

    public String getFaltaAtraso() {
        return faltaAtraso;
    }

    public void setFaltaAtraso(String faltaAtraso) {
        this.faltaAtraso = faltaAtraso;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHorasDiurnasNaoExtraordinarias() {
        return horasDiurnasNaoExtraordinarias;
    }

    public void setHorasDiurnasNaoExtraordinarias(String horasDiurnasNaoExtraordinarias) {
        this.horasDiurnasNaoExtraordinarias = horasDiurnasNaoExtraordinarias;
    }

    public String getHorasExtra1() {
        return horasExtra1;
    }

    public void setHorasExtra1(String horasExtra1) {
        this.horasExtra1 = horasExtra1;
    }

    public String getHorasExtra2() {
        return horasExtra2;
    }

    public void setHorasExtra2(String horasExtra2) {
        this.horasExtra2 = horasExtra2;
    }

    public String getHorasExtra3() {
        return horasExtra3;
    }

    public void setHorasExtra3(String horasExtra3) {
        this.horasExtra3 = horasExtra3;
    }

    public String getHorasExtra4() {
        return horasExtra4;
    }

    public void setHorasExtra4(String horasExtra4) {
        this.horasExtra4 = horasExtra4;
    }

    public String getHorasNoturnasNaoExtraordinarias() {
        return horasNoturnasNaoExtraordinarias;
    }

    public void setHorasNoturnasNaoExtraordinarias(String horasNoturnasNaoExtraordinarias) {
        this.horasNoturnasNaoExtraordinarias = horasNoturnasNaoExtraordinarias;
    }

    public String getModalidadeHE1() {
        return modalidadeHE1;
    }

    public void setModalidadeHE1(String modalidadeHE1) {
        this.modalidadeHE1 = modalidadeHE1;
    }

    public String getModalidadeHE2() {
        return modalidadeHE2;
    }

    public void setModalidadeHE2(String modalidadeHE2) {
        this.modalidadeHE2 = modalidadeHE2;
    }

    public String getModalidadeHE3() {
        return modalidadeHE3;
    }

    public void setModalidadeHE3(String modalidadeHE3) {
        this.modalidadeHE3 = modalidadeHE3;
    }

    public String getModalidadeHE4() {
        return modalidadeHE4;
    }

    public void setModalidadeHE4(String modalidadeHE4) {
        this.modalidadeHE4 = modalidadeHE4;
    }

    public String getPercentualHE1() {
        return percentualHE1;
    }

    public void setPercentualHE1(String percentualHE1) {
        this.percentualHE1 = percentualHE1;
    }

    public String getPercentualHE2() {
        return percentualHE2;
    }

    public void setPercentualHE2(String percentualHE2) {
        this.percentualHE2 = percentualHE2;
    }

    public String getPercentualHE3() {
        return percentualHE3;
    }

    public void setPercentualHE3(String percentualHE3) {
        this.percentualHE3 = percentualHE3;
    }

    public String getPercentualHE4() {
        return percentualHE4;
    }

    public void setPercentualHE4(String percentualHE4) {
        this.percentualHE4 = percentualHE4;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getSaldoHoras() {
        return saldoHoras;
    }

    public void setSaldoHoras(String saldoHoras) {
        this.saldoHoras = saldoHoras;
    }

    public String getSinalHora() {
        return sinalHora;
    }

    public void setSinalHora(String sinalHora) {
        this.sinalHora = sinalHora;
    }
}
