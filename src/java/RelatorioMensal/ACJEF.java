/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RelatorioMensal;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class ACJEF implements Serializable {

    private ACJEFCabecalho cabecalho;
    private List<ACJEFDetalhe> detalheList;
    private List<HorarioContratual> horarioContratualList;
    private ACJEFTrailer trailer;

    public ACJEFCabecalho getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(ACJEFCabecalho cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<ACJEFDetalhe> getDetalheList() {
        return detalheList;
    }

    public void setDetalheList(List<ACJEFDetalhe> detalheList) {
        this.detalheList = detalheList;
    }

    public ACJEFTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(ACJEFTrailer trailer) {
        this.trailer = trailer;
    }

    public List<HorarioContratual> getHorarioContratualList() {
        return horarioContratualList;
    }

    public void setHorarioContratualList(List<HorarioContratual> horarioContratualList) {
        this.horarioContratualList = horarioContratualList;
    }
}
