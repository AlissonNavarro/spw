/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Alisson
 */
public class testeCiclo {

    public static void main(String[] args) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(sdf.parse("01/10/2013"));
            int inicio = gc.get(Calendar.DAY_OF_YEAR) + gc.get(Calendar.YEAR) * 365;
            gc.setTime(sdf.parse("17/09/2014"));
            int fim = gc.get(Calendar.DAY_OF_YEAR) + gc.get(Calendar.YEAR) * 365;
            int cont = 0;
            for (int dia = inicio; dia <= fim; dia++) {
                if (cont == 0) {
                    System.out.println(dia);
                }
                cont++;
                if (cont == 3) {
                    cont = 0;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
