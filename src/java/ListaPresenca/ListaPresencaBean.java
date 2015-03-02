/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ListaPresenca;

import ConsultaPonto.Jornada;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author amsgama
 */
public class ListaPresencaBean implements Serializable{

    private List<SelectItem> departamentosSelecItem;
    private List<SelectItem> filtroSelecItem;
    private List<ListaPresenca> listaPresencaList;
    private Integer departamentoSelecionado;
    private Integer filtroSelecionado = 1;
    private Integer page;
    private Locale objLocale;
    private Date data;
    private String hora;
    private Date horaData;
    private UsuarioBean usuarioBean;

    public ListaPresencaBean() {
        departamentosSelecItem = new ArrayList<SelectItem>();
        objLocale = new Locale("pt", "BR");
        data = new Date();
        getHoraAtual();
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        consultaDepartamentos();
    }

        public ListaPresencaBean(String e) {

    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentosSelecItem = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void getHoraAtual() {
        Date data_ = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(data_);
        Integer horaIn = calendar.get(Calendar.HOUR_OF_DAY);
        String horaInSrt = "";
        if (horaIn < 10) {
            horaInSrt = "0" + horaIn;
        } else {
            horaInSrt = horaIn.toString();
        }
        String minutoInSrt = "";
        Integer minutosIn = calendar.get(Calendar.MINUTE);
        if (minutosIn < 10) {
            minutoInSrt = "0" + minutosIn;
        } else {
            minutoInSrt = minutosIn.toString();
        }
        hora = horaInSrt + ":" + minutoInSrt;
    }

    public void consultar() {

        if (validarHora(hora)) {
            Time time = Time.valueOf(hora + ":00");
            horaData = new Date();
            horaData.setTime(data.getTime() + time.getTime() - 10800000l);
            Banco banco = new Banco();

            listaPresencaList = banco.consultaDadosPonto(departamentoSelecionado, data, horaData);
            listaPresencaList = getJornada(listaPresencaList);

            List<ListaPresenca> listaPresencaListFiltro = new ArrayList<ListaPresenca>();

            for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {

                ListaPresenca listaPresenca = it.next();

                if (filtroSelecionado == 3) {
                    listaPresencaListFiltro.add(listaPresenca);
                }
                if (filtroSelecionado == 1) {
                    if (listaPresenca.getSituacao().equals("Presente")) {
                        listaPresencaListFiltro.add(listaPresenca);
                    }
                }
                if (filtroSelecionado == 2) {
                    if (listaPresenca.getSituacao().equals("Ausente") || listaPresenca.getSituacao().equals("Em espera")) {
                        listaPresencaListFiltro.add(listaPresenca);
                    }
                }
            }
            listaPresencaList = listaPresencaListFiltro;
        } else {
            FacesMessage msgErro = new FacesMessage("Hora Inválida.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            getHoraAtual();
        }
    }

    private Boolean validarHora(String hora_) {
        if ((hora_.length() == 5) && !hora_.contains("_")) {
            return true;
        } else {
            return false;
        }
    }

    public List<ListaPresenca> getJornada(List<ListaPresenca> listaPresencaList) {

        List<ListaPresenca> listaPresencaSaida = new ArrayList<ListaPresenca>();
        Jornada jornadaCerta = new Jornada();
        Banco banco = new Banco();

        GregorianCalendar calen = new GregorianCalendar();
        calen.setTime(data);

        int diaSemanaNumber = calen.get(Calendar.DAY_OF_WEEK) - 1;
        for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {

            ListaPresenca listaPresenca = it.next();
            List<Jornada> jornada = banco.consultaRegraJornada(listaPresenca.getUserid(), diaSemanaNumber, data);
            listaPresenca.setJornadaList(jornada);

            if (jornada.size() != 0) {

                jornadaCerta = getPontoAnterior(listaPresenca.getJornadaList());

                if (jornadaCerta.getIsCompleto()) {

                    Date chegadaAntecipada = timeToDate(jornadaCerta.getCheckInLimiteAntecipada().getTime());
                    Date chegadaAtrasado = timeToDate(jornadaCerta.getCheckInLimiteAtrasada().getTime());
                    Date saidaAdiantado = timeToDate(jornadaCerta.getCheckOutLimiteAntecipada().getTime());

                    if (listaPresenca.getUltimoRegistro().after(chegadaAntecipada) && listaPresenca.getUltimoRegistro().before(saidaAdiantado)) {
                        listaPresenca.setSituacao("Presente");
                    } else if (chegadaAntecipada.before(horaData) && chegadaAtrasado.after(horaData)) {
                        listaPresenca.setSituacao("Em espera");
                    } else {
                        listaPresenca.setSituacao("Ausente");
                    }

                    listaPresencaSaida.add(listaPresenca);
                } else {
                    listaPresenca.setSituacao("Jornada não iniciada");
                    listaPresencaSaida.add(listaPresenca);
                }
            }
        }
        return listaPresencaSaida;
    }

    public Date timeToDate(Long tempo) {

        Date dateCheckInLimiteAntecipada = data;
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String hora_ = sdfHora.format(tempo);
        Time time = Time.valueOf(hora_);
        Date newData = new Date();
        newData.setTime(dateCheckInLimiteAntecipada.getTime() + time.getTime() - 10800000l);
        return newData;
    }

    public Jornada getPontoAnterior(List<Jornada> jornadaList) {

        List<String> horasList = new ArrayList<String>();

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (jornada.getInicioJornada() != null) {
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                String hora_ = sdfHora.format(jornada.getCheckInLimiteAntecipada().getTime());
                horasList.add(hora_);
            }
        }

        List<Long> horasTimes = new ArrayList<Long>();
        for (Iterator<String> it = horasList.iterator(); it.hasNext();) {
            String _hora = it.next();
            Time time = Time.valueOf(_hora);
            horasTimes.add(time.getTime());
        }

        Time timeHora = Time.valueOf(hora + ":00");
        Long horaProcuradaLong = 0l;

        for (Iterator<Long> it = horasTimes.iterator(); it.hasNext();) {
            Long long1 = it.next();
            if (long1 <= timeHora.getTime()) {
                horaProcuradaLong = long1;
            }
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        String horaProcuradaString = sdfHora.format(horaProcuradaLong);

        Jornada jornada = new Jornada();

        jornada = getJornadaCerta(horaProcuradaString, jornadaList);

        return jornada;
    }

    private Jornada getJornadaCerta(String hora, List<Jornada> jornadaList) {
        Jornada jornadaCerta = new Jornada();

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {
            Jornada jornada = it.next();
            if (jornada.getInicioJornada() != null) {
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
                String hora_ = sdfHora.format(jornada.getCheckInLimiteAntecipada().getTime());
                if (hora_.equals(hora)) {
                    jornadaCerta = jornada;
                }
            }
        }
        return jornadaCerta;
    }

    public void voltar() {
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        //    FacesContext.getCurrentInstance().getExternalContext().redirect("/www/consultaFrequenciaAdmin.jsp?param=clear");

    }

    public void imprimir() throws JRException, Exception {

        List<Relatorio> relatorioList = new ArrayList<Relatorio>();
        for (Iterator<ListaPresenca> it = listaPresencaList.iterator(); it.hasNext();) {
            ListaPresenca listaPresenca = it.next();
            String cpf = listaPresenca.getCpf();
            String nome = listaPresenca.getNome();
            Date ultimoRegistro = listaPresenca.getUltimoRegistro();
            String situacao = listaPresenca.getSituacao();
            Relatorio relatorio = new Relatorio(cpf, nome, ultimoRegistro, situacao);
            relatorioList.add(relatorio);
        }
        Banco banco = new Banco();
        banco.insertRelatorio(relatorioList);
        banco = new Banco();
        String departamento = "";
        if (departamentoSelecionado == -1) {
            departamento = "Todos os departamentos";
        } else {
            departamento = banco.consultaDepartamentoo(departamentoSelecionado);
        }

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataSrt = sdfHora.format(data.getTime());
        String filtro = sdfHora.format(data.getTime());

        if (filtroSelecionado == 1) {
            filtro = "Todos os funcionários";
        } else if (filtroSelecionado == 2) {
            filtro = "Presentes";
        } else if (filtroSelecionado == 3) {
            filtro = "Ausentes";
        }

        Impressao.geraRelatorio(departamento, dataSrt, hora, filtro,0,0,0);
    }

    public List<ListaPresenca> getListaPresencaList() {
        return listaPresencaList;
    }

    public void setListaPresencaList(List<ListaPresenca> listaPresencaList) {
        this.listaPresencaList = listaPresencaList;
    }

    public Integer getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Integer departamentoSelecionado) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SelectItem> getFiltroSelecItem() {
        filtroSelecItem = new ArrayList<SelectItem>();        
        filtroSelecItem.add(new SelectItem(1, "Presentes"));
        filtroSelecItem.add(new SelectItem(2, "Ausentes"));
        filtroSelecItem.add(new SelectItem(3, "Todos"));
        return filtroSelecItem;
    }

    public void setFiltroSelecItem(List<SelectItem> filtroSelecItem) {
        this.filtroSelecItem = filtroSelecItem;
    }

    public Integer getFiltroSelecionado() {
        return filtroSelecionado;
    }

    public void setFiltroSelecionado(Integer filtroSelecionado) {
        this.filtroSelecionado = filtroSelecionado;
    }

    public static void main(String[] x){
        ListaPresencaBean list = new ListaPresencaBean("");
        System.out.print(list.validarHora("20:00"));
    }
}
