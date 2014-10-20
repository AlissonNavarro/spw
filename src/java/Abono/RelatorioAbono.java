/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Alexandre
 */
public class RelatorioAbono implements Serializable {

    private HashMap<String, Integer> qntPorCategorias;
    private Integer cod_funcionario;
    private String funcionario;
    private Integer totalAbonos;
    private String detalhesApresentacao;
    private List<funcionarioEDetalhes> qntPorCategoriaList;
    

    public RelatorioAbono(HashMap<String, Integer> qntPorCategorias, Integer cod_funcionario, String funcionario, Integer totalAbonos) {
        this.qntPorCategorias = qntPorCategorias;
        this.cod_funcionario = cod_funcionario;
        this.funcionario = funcionario;
        this.totalAbonos = totalAbonos;
    }

    

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public HashMap<String, Integer> getQntPorCategorias() {
        return qntPorCategorias;
    }

    public void setQntPorCategorias(HashMap<String, Integer> qntPorCategorias) {
        this.qntPorCategorias = qntPorCategorias;
    }

    public List<funcionarioEDetalhes> ordenarValores(List<funcionarioEDetalhes> inteiroEStringList) {
        Collections.sort(inteiroEStringList, new Comparator() {

            public int compare(Object o1, Object o2) {
                funcionarioEDetalhes p1 = (funcionarioEDetalhes) o1;
                funcionarioEDetalhes p2 = (funcionarioEDetalhes) o2;
                return p1.getDetalhes() < p2.getDetalhes()
                        ? +1 : (p1.getDetalhes() > p2.getDetalhes()
                        ? -1 : 0);
            }
        });
        return inteiroEStringList;
    }

    public Integer geraApresentacao() {
        
        String s = "Sem abonos solicitados";
        Integer total = 0;
        ArrayList<funcionarioEDetalhes> listaDeValores = new ArrayList<funcionarioEDetalhes>();
        Set<String> conjunto = qntPorCategorias.keySet();
        for (Iterator<String> it = conjunto.iterator(); it.hasNext();) {
            String categoria = it.next();
            Integer qnt = qntPorCategorias.get(categoria);
            total = total + qnt;
            listaDeValores.add(new funcionarioEDetalhes(qnt, categoria));
        }
        ArrayList<funcionarioEDetalhes> valoresOrdenados = new ArrayList<funcionarioEDetalhes>();
        List lista = ordenarValores(listaDeValores);
        valoresOrdenados.addAll(lista);
        for (int i = 0; i < listaDeValores.size(); i++) {
            funcionarioEDetalhes inteiroEString = listaDeValores.get(i);
            if(i==0){
                s=inteiroEString.getFuncionario()+": "+inteiroEString.getDetalhes();
            }else{
                s=s+", "+inteiroEString.getFuncionario()+": "+inteiroEString.getDetalhes();
            }


        }
        detalhesApresentacao = s;
        totalAbonos = total;

        return total;
    }

    public Integer calculaTotalApresentação(){
        Integer total = 0;
        Set<String> conjunto = qntPorCategorias.keySet();
        for (Iterator<String> it = conjunto.iterator(); it.hasNext();) {
            String categoria = it.next();
            Integer qnt = qntPorCategorias.get(categoria);
            total = total + qnt;
        }
        return total;
    }

    public String getDetalhesApresentacao() {
        return detalhesApresentacao;
    }

    public void setDetalhesApresentacao(String detalhesApresentacao) {
        this.detalhesApresentacao = detalhesApresentacao;
    }

    public Integer getTotalAbonos() {
        return totalAbonos;
    }

    public void setTotalAbonos(Integer totalAbonos) {
        this.totalAbonos = totalAbonos;
    }

    public List<funcionarioEDetalhes> getQntPorCategoriaList() {
        return qntPorCategoriaList;
    }

    public void setQntPorCategoriaList(List<funcionarioEDetalhes> qntPorCategoriaList) {
        this.qntPorCategoriaList = qntPorCategoriaList;
    }

    

    

    
/*
    public BeanComparator getOrdenaMedia() {
        return ordenaMedia;
    }

    public void setOrdenaMedia(BeanComparator ordenaMedia) {
        this.ordenaMedia = ordenaMedia;
    }
*/
    

    
}


