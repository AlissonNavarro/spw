/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

/**
 *
 * @author Alexandre
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Formatacao {

    public Date primeiroDiaMes(Integer Ano, Integer mes) {
        Calendar cal = new GregorianCalendar(Ano, mes - 1, 1);
        int dia = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        String srt = (dia < 10 ? "0" + dia : "" + dia) + "/" + (mes < 10 ? "0" + mes : "" + mes) + "/" + Ano.toString();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df.parse(srt);
        } catch (ParseException ex) {
        }
        return dt;
    }

    public Date ultimoDiaMes(Integer Ano, Integer mes) {
        Calendar cal = new GregorianCalendar(Ano, mes - 1, 1);
        int dia = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String srt = (dia < 10 ? "0" + dia : "" + dia) + "/" + (mes < 10 ? "0" + mes : "" + mes) + "/" + Ano.toString();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dt = null;
        try {
            dt = df.parse(srt);
        } catch (ParseException ex) {
        }

        return dt;
    }

    public static void main(String[] args) {
        Formatacao f = new Formatacao();
        System.out.println(f.primeiroDiaMes(2010, 1));
        System.out.println(f.ultimoDiaMes(2010, 1));
    }
}
