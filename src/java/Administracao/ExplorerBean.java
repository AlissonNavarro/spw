/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

/**
 *
 * @author ancouto
 */
public class ExplorerBean implements Serializable {

    private Selection selectLog;
    private List<String> logList;
    private String pastaAtual;
    private String pastaPai;
    private String listBoxPasta;
    private String abaCorrente;
    private String subAbaCorrente;

    public ExplorerBean() {
        selectLog = new SimpleSelection();
        pastaAtual = Metodos.Metodos.getPath();
        listBoxPasta = "";
        abaCorrente = "tabPadroes";
        subAbaCorrente = "";
    }

    public List<String> getLogList() {
        File dir = new File(pastaAtual);
        File aux;
        logList = new ArrayList<String>();

        List<String> listaDiretorios = new ArrayList<String>();
        List<String> listaArquivos = new ArrayList<String>();

        if (dir.isDirectory()) {
            String arquivos[] = dir.list();
            if (dir.getParent() == null) {
                pastaPai = "";
            } else {
                pastaPai = dir.getParent();
                listaDiretorios.add("<dir> " + pastaPai);
            }
            for (int i = 0; i < arquivos.length; i++) {
                aux = new File(pastaAtual + arquivos[i]);
                if (aux.isDirectory()) {
                    listaDiretorios.add("<dir> " + arquivos[i]);
                } else {
                    listaArquivos.add(arquivos[i]);
                }
            }

            logList.addAll(listaDiretorios);
            logList.addAll(listaArquivos);
        }
        return logList;
    }

    public void setLogList(List<String> logList) {
        this.logList = logList;
    }

    public Selection getSelectLog() {
        return selectLog;
    }

    public void setSelectLog(Selection selectLog) {
        this.selectLog = selectLog;
    }

    public void abrirPasta() {
        String caminho = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("alvo");

        File fPastaAtual;
        File fPastaDestino;

        if (pastaPai.equals(caminho.substring(6))) {
            fPastaDestino = new File(caminho.substring(6) + "\\");
            this.pastaPai = fPastaDestino.getParent();
            this.pastaAtual = fPastaDestino.getAbsolutePath() + "\\";
        } else {
            fPastaAtual = new File(pastaAtual);
            fPastaDestino = new File(fPastaAtual.getAbsolutePath() + "\\" + caminho.substring(6));
            if (fPastaDestino.isDirectory()) {
                this.pastaPai = fPastaAtual.getAbsolutePath();
                this.pastaAtual = fPastaDestino.getAbsolutePath() + "\\";
            }
        }

    }

    public void abrirArquivo() {
        String caminho = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("alvo");

        File pastaAux = new File(pastaAtual);
        File aux = new File(pastaAux.getAbsolutePath() + "\\" + caminho);

        if (aux.isFile()) {

            HttpServletResponse r = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            r.setHeader("Content-Disposition", "attachment;filename=\"" + aux.getAbsolutePath() + "\"");
            File f = new File(aux.getAbsolutePath());
            r.setContentLength((int) f.length());
            r.setContentType("txt");

            try {
                FileInputStream in = new FileInputStream(f);
                OutputStream os = r.getOutputStream();
                byte[] buf = new byte[(int) f.length()];
                int c;
                while ((c = in.read(buf)) >= 0) {
                    os.write(buf, 0, c);
                }
                in.close();
                os.flush();
                os.close();
                FacesContext.getCurrentInstance().responseComplete();
                FacesContext.getCurrentInstance().renderResponse();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void deletarArquivo() {
        String caminho = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("alvo");

        File aux = new File(pastaAtual + caminho.substring(6));

        if (!caminho.substring(0, 6).equals("<dir> ")) {
            File f = new File(pastaAtual + caminho);
            f.delete();
        }
    }

    public String getListBoxPasta() {
        return listBoxPasta;
    }

    public void setListBoxPasta(String listBoxPasta) {
        if (listBoxPasta != null) {
            if (!this.listBoxPasta.equals(listBoxPasta)) {
                this.listBoxPasta = listBoxPasta;
                if (listBoxPasta.equals("T")) {
                    pastaAtual = Metodos.Metodos.getPath();
                }
                if (listBoxPasta.equals("C")) {
                    pastaAtual = "C:\\";
                }
                if (listBoxPasta.equals("D")) {
                    pastaAtual = "D:\\";
                }
                if (listBoxPasta.equals("E")) {
                    pastaAtual = "E:\\";
                }
                if (listBoxPasta.equals("F")) {
                    pastaAtual = "F:\\";
                }
            }
        }
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }

    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
    }
    
    public void setSubAba() {
        String subTab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subTab");
        subAbaCorrente = subTab;
    }   

    public String getSubAbaCorrente() {
        return subAbaCorrente;
    }

    public void setSubAbaCorrente(String subAbaCorrente) {
        this.subAbaCorrente = subAbaCorrente;
    }
    
    

}
