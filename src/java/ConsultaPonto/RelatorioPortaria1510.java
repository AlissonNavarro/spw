/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class RelatorioPortaria1510 implements Serializable {

    private Date data; // Apenas para identificar
    private String dia;
    private String horasDias;
    private String saldo;
    private String marcacoesRegistradas;
    private String entrada1;
    private String entrada2;
    private String entrada3;
    private String saida1;
    private String saida2;
    private String saida3;
    private String ch;
    private List<PontoIrreal> pontoIrrealList;
    private String entradasCatraca;
    private String saidasCatraca;
    

    public RelatorioPortaria1510() {
        pontoIrrealList = new ArrayList<PontoIrreal>();
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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

    public String getEntrada3() {
        return entrada3;
    }

    public void setEntrada3(String entrada3) {
        this.entrada3 = entrada3;
    }

    public String getMarcacoesRegistradas() {
        return marcacoesRegistradas;
    }

    public void setMarcacoesRegistradas(String marcacoesRegistradas) {
        this.marcacoesRegistradas = marcacoesRegistradas;
    }

    public List<PontoIrreal> getPontoIrrealList() {
        return pontoIrrealList;
    }

    public void setPontoIrrealList(List<PontoIrreal> pontoIrrealList) {
        this.pontoIrrealList = pontoIrrealList;
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

    public String getSaida3() {
        return saida3;
    }

    public void setSaida3(String saida3) {
        this.saida3 = saida3;
    }

    public String getHorasDias() {
        return horasDias;
    }

    public void setHorasDias(String horasDias) {
        this.horasDias = horasDias;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getEntradasCatraca() {
        return entradasCatraca;
    }

    public void setEntradasCatraca(String entradasCatraca) {
        this.entradasCatraca = entradasCatraca;
    }

    public String getSaidasCatraca() {
        return saidasCatraca;
    }

    public void setSaidasCatraca(String saidasCatraca) {
        this.saidasCatraca = saidasCatraca;
    }
}