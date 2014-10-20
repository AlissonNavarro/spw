/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;


import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author amsgama
 */
public class SituacaoPontoLiberacao extends SituacaoPonto{

    private List<SelectItem> turnoLiberado;

    public List<SelectItem> getTurnoLiberado() {
        return turnoLiberado;
    }

    public void setTurnoLiberado(List<SelectItem> turnoLiberado) {
        this.turnoLiberado = turnoLiberado;
    }
}
