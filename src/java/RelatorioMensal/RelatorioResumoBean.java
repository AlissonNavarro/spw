/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author amsgama
 */

public class RelatorioResumoBean implements Serializable {

    private Date dataInicio;
    private Date dataFim;

    public RelatorioResumoBean() {
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
    }

    public void consultar() {

        ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

        Date dataInicio_ = (Date) dataInicio.clone();
        Date dataFim_ = (Date) dataFim.clone();

        c.setDataInicio(dataInicio_);
        c.setDataFim(dataFim_);

        Banco banco = new Banco();
        List<RelatorioResumo> relatorioResumoList = new ArrayList<RelatorioResumo>();
        relatorioResumoList = banco.consultaFuncionarioOrderByDeptUser();

        for (Iterator<RelatorioResumo> it = relatorioResumoList.iterator(); it.hasNext();) {
            dataInicio_ = (Date) dataInicio.clone();
            dataFim_ = (Date) dataFim.clone();
            c = new ConsultaFrequenciaComEscalaBean("");
            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);
            RelatorioResumo relatorioResumo = it.next();
            c.setCod_funcionario(relatorioResumo.getUserid());
            c.consultaDiasSemMsgErro();
            if (!c.getDiasList().isEmpty()) {
                String previstas = c.getHorasASeremTrabalhadasTotal();
                String trabalhadas = c.getHorasTotal();
                relatorioResumo.setHoraPrevista(previstas);
                relatorioResumo.setHoraTrabalhada(trabalhadas);
            } else {
                relatorioResumo.setHoraPrevista("Sem Escala");
                relatorioResumo.setHoraTrabalhada("Sem Escala");
            }
        }
        gravarArquivo(relatorioResumoList);
        banco.fecharConexao();
    }

    private void gravarArquivo(List<RelatorioResumo> relatorioResumoList) {
        FileOutputStream fos = null;
        try {
            File arquivo;
            arquivo = new File("D:\\Agosto-HUSE.txt");
            fos = new FileOutputStream(arquivo);
            for (Iterator<RelatorioResumo> it = relatorioResumoList.iterator(); it.hasNext();) {
                RelatorioResumo relatorioResumo = it.next();
                try {
                    // Gravando no arquivo
                    String texto = relatorioResumo.toString();
                    fos.write((texto + "\n").getBytes());

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            fos.close();
        } catch (IOException ex) {
            System.out.println("RelatorioMensal: gravarArquivo 1: "+ex);
            //Logger.getLogger(RelatorioResumoBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                System.out.println("RelatorioMensal: gravarArquivo 2: "+ex);
                //Logger.getLogger(RelatorioResumoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Date getPrimeiroDiaMes() {
        GregorianCalendar g = new GregorianCalendar(2010, 7, 1);

        Date data = g.getTime();

        return data;
    }

    private static Date getHoje() {
        GregorianCalendar g = new GregorianCalendar(2010, 7, 31);

        Date data = g.getTime();

        return data;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public static void main(String[] args) {
        GregorianCalendar g = new GregorianCalendar();
        g.set(2011, 1, 10);
        System.out.print(g.getTime());
    }
}
