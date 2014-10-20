/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConsultaJornada;

import Usuario.UsuarioBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Alexandre
 */
public class JornadaExibicaoBean {

    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
    private String ano;
    private List<SelectItem> mesSelecItem;
    private String mesSelecionado;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;

    public JornadaExibicaoBean() {
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        consultaDepartamentos();
        carregarMeses();
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = new Date();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        ano = year.toString();
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
         UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        departamentosSelecItem = banco.consultaDepartamento(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
    }

    private void carregarMeses() {
        mesSelecItem = new ArrayList<SelectItem>();
        mesSelecItem.add(new SelectItem(-1, "Selecione o mês"));
        mesSelecItem.add(new SelectItem(1, "Janeiro"));
        mesSelecItem.add(new SelectItem(2, "Fevereiro"));
        mesSelecItem.add(new SelectItem(3, "Março"));
        mesSelecItem.add(new SelectItem(4, "Abril"));
        mesSelecItem.add(new SelectItem(5, "Maio"));
        mesSelecItem.add(new SelectItem(6, "Junho"));
        mesSelecItem.add(new SelectItem(7, "Julho"));
        mesSelecItem.add(new SelectItem(8, "Agosto"));
        mesSelecItem.add(new SelectItem(9, "Setembro"));
        mesSelecItem.add(new SelectItem(10, "Outubro"));
        mesSelecItem.add(new SelectItem(11, "Novembro"));
        mesSelecItem.add(new SelectItem(12, "Dezembro"));
    }

    public void getDiaConsulta() throws JRException, Exception {
        Date data = new Date();
        if (!ano.equals("-1") && !mesSelecionado.equals("-1") && !departamentoSelecionado.equals("-1")) {
            data = DiasMes.primeiroDiaMes(Integer.parseInt(ano), Integer.parseInt(mesSelecionado));
            Banco banco = new Banco();
            List<JornadaExibicao> jornadaExibicaoList = banco.consultaJornadas(Integer.parseInt(departamentoSelecionado), data);
            imprimir(jornadaExibicaoList);
        } else {
            FacesMessage msgErro = new FacesMessage("Selecione o departamento, o mês e o ano.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void imprimir(List<JornadaExibicao> jornadaExibicaoList) throws JRException, Exception {

        Banco banco = new Banco();
        banco.insertRelatorio(jornadaExibicaoList);

        mesIntToString();
        banco = new Banco();
        String departamento = banco.consultaNomeDepartamento(Integer.parseInt(departamentoSelecionado));
        Impressao.geraRelatorio(departamento,mesSelecionado + "/" + ano);
    }

    private String mesIntToString() {

        switch (Integer.parseInt(mesSelecionado)) {
            case 1:
                mesSelecionado = "Janeiro";
                break;
            case 2:
                mesSelecionado = "Fevereiro";
                break;
            case 3:
                mesSelecionado = "Março";
                break;
            case 4:
                mesSelecionado = "Abril";
                break;
            case 5:
                mesSelecionado = "Maio";
                break;
            case 6:
                mesSelecionado = "Junho";
                break;
            case 7:
                mesSelecionado = "Julho";
                break;
            case 8:
                mesSelecionado = "Agosto";
                break;
            case 9:
                mesSelecionado = "Setembro";
                break;
            case 10:
                mesSelecionado = "Outubro";
                break;
            case 11:
                mesSelecionado = "Novembro";
                break;
            case 12:
                mesSelecionado = "Dezembro";
                break;
            default:
        }
        return mesSelecionado;
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

    public String getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(String departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public List<SelectItem> getDepartamentosSelecItem() {
        return departamentosSelecItem;
    }

    public void setDepartamentosSelecItem(List<SelectItem> departamentosSelecItem) {
        this.departamentosSelecItem = departamentosSelecItem;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public List<SelectItem> getMesSelecItem() {
        return mesSelecItem;
    }

    public void setMesSelecItem(List<SelectItem> mesSelecItem) {
        this.mesSelecItem = mesSelecItem;
    }

    public String getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(String mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
