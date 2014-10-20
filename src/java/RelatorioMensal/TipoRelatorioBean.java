/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class TipoRelatorioBean {

    private List<SelectItem> tipoRelatorioList;
    private Integer tipoRelatorio;

    public TipoRelatorioBean() {
        Banco banco = new Banco();
        tipoRelatorio = banco.consultaTipoRelatorio();
        tipoRelatorioList = new ArrayList<SelectItem>();
        tipoRelatorioList.add(new SelectItem(1, "Relatório Administrativo com saldo"));
        tipoRelatorioList.add(new SelectItem(2, "Relatório Administrativo sem saldo"));
        tipoRelatorioList.add(new SelectItem(3, "Relatório Portaria 1510"));
    }

    public void alterarTipoRelatorio(){
        Banco banco = new Banco();
        banco.alterarTipoRelatorio(tipoRelatorio);
    }

    public List<SelectItem> getTipoRelatorioList() {
        return tipoRelatorioList;
    }

    public void setTipoRelatorioList(List<SelectItem> tipoRelatorioList) {
        this.tipoRelatorioList = tipoRelatorioList;
    }

    public Integer getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(Integer tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }
}
