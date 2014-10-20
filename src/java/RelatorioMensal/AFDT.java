/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RelatorioMensal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amsgama
 */
public class AFDT implements Serializable{

    AFDTCabecalho cabecalho;
    List<AFDTDetalhe> detalheList;
    AFDTTrailer trailer;

    public AFDT() {
        cabecalho = new AFDTCabecalho();
        detalheList = new ArrayList<AFDTDetalhe>();
        trailer = new AFDTTrailer();
    }

    public AFDTCabecalho getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(AFDTCabecalho cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<AFDTDetalhe> getDetalheList() {
        return detalheList;
    }

    public void setDetalheList(List<AFDTDetalhe> detalheList) {
        this.detalheList = detalheList;
    }

    public AFDTTrailer getTrailer() {
        return trailer;
    }

    public void setTrailer(AFDTTrailer trailer) {
        this.trailer = trailer;
    }

}
