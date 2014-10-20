/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaPonto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class teste {

    String departamento;
    private List<SelectItem> departamentolistt;

    public teste() {
        vai();
        for (Iterator<SelectItem> it = departamentolistt.iterator(); it.hasNext();) {
            SelectItem selectItem = it.next();
            System.out.println(selectItem.getLabel());
        }
    }

    public void vai() {

        departamentolistt = new ArrayList<SelectItem>();
        departamentolistt.add(new SelectItem(-1, "Selecione o Departamento"));
        departamentolistt.add(new SelectItem(1, "SGN SOLUÇÕES"));
        departamentolistt.add(new SelectItem(2, " PROJETO BANESE CARD"));
        departamentolistt.add(new SelectItem(3, " BIOMETRIA E SUPORTE TECNICO"));
        departamentolistt.add(new SelectItem(6, "  DESENVOLVIMENTO"));
        departamentolistt.add(new SelectItem(4, " ADMINISTRATIVO"));
        departamentolistt.add(new SelectItem(5, " AFASTADOS OU DEMITIDOS"));
    }

    public static void main(String[] s) {
        teste teste = new teste();
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getDepartamentolistt() {
        return departamentolistt;
    }

    public void setDepartamentolistt(List<SelectItem> departamentolistt) {
        this.departamentolistt = departamentolistt;
    }
}
