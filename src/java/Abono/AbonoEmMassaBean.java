/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abono;

import Administracao.Banco;
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Alexandre
 */
public class AbonoEmMassaBean implements Serializable {

    private Integer departamento;
    private Integer cod_funcionario;
    private Integer cod_funcionario_alterado;
    private Integer cod_justificativa_selecionada;
    private List<SelectItem> departamentolist;
    private List<SelectItem> funcionarioList;
    private List<SelectItem> justificativasList;
    private List<AbonoEmMassa> abonoEmMassaList;
    private Boolean incluirSubSetores;
    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    private AbonoEmMassaADD novoAbonoEmMassa;
    private AbonoEmMassaADD alterarFastamento;

    public AbonoEmMassaBean() throws SQLException {

        abonoEmMassaList = new ArrayList<AbonoEmMassa>();
        alterarFastamento = new AbonoEmMassaADD();
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        objLocale = new Locale("pt", "BR");
        cod_funcionario = -1;

        novoAbonoEmMassa = new AbonoEmMassaADD();
        novoAbonoEmMassa.setDataInicio(getPrimeiroDiaMes());
        novoAbonoEmMassa.setDataFim(getHoje());

        Banco banco = new Banco();
        justificativasList = banco.consultaJustificativa();
        novoAbonoEmMassa.setJustificativasList(justificativasList);

        consultaDepartamentos();
        inicializaAtributos();
        Locale.setDefault(new Locale("pt", "BR"));
    }

    public void consultaDepartamentos() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        if (!usuarioBean.getUsuario().getPermissao().equals("")) {
            departamentolist = banco.consultaDepartamentoHierarquico(Integer.parseInt(usuarioBean.getUsuario().getPermissao()));
        }
    }

    public void inicializaAtributos() {
        //      Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(null, "Selecione o funcionario"));
        //      departamentolist = new ArrayList<SelectItem>();
        //      departamentolist = banco.consultaDepartamentoOrdernado();
    }

    public void consultaFuncionario() throws SQLException {
        Banco banco = new Banco();
        cod_funcionario = -1;
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        if (!departamento.equals(-1)) {
            funcionarioList = banco.consultaFuncionario(departamento, incluirSubSetores);
        }
        abonoEmMassaList = new ArrayList<AbonoEmMassa>();
    }

    public void showNovoAbonoEmMassa() {
        novoAbonoEmMassa.setCod_justificativa(-1);
        novoAbonoEmMassa.setDescricaoJustificativa(null);
    }

    public void showAlterarAbonoEmMassa() {
        String posicao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posAbonoEmMassa");
        cod_funcionario_alterado = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_funcionario"));

        Integer posicaoInt = Integer.parseInt(posicao);
        AbonoEmMassa abonoEmMassa = abonoEmMassaList.get(posicaoInt);
        cod_justificativa_selecionada = abonoEmMassa.getCod_categoria();

        alterarFastamento.setDataInicio(abonoEmMassa.getInicio());
        alterarFastamento.setDataFim(abonoEmMassa.getFim());

        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String[] dataHoraInicio = sdfHora.format(abonoEmMassa.getInicio().getTime()).split(" ");
        String[] dataHoraFim = sdfHora.format(abonoEmMassa.getFim().getTime()).split(" ");

        alterarFastamento.setHoraInicio(dataHoraInicio[1]);
        alterarFastamento.setHoraFim(dataHoraFim[1]);
        alterarFastamento.setNomeFuncionario(abonoEmMassa.getNomeFuncionario());
        alterarFastamento.setJustificativasList(justificativasList);
        alterarFastamento.setCod_justificativa(abonoEmMassa.getCod_categoria());
        alterarFastamento.setDescricaoJustificativa(abonoEmMassa.getDescricaoCategoria());
    }

    public void consultaAbonoEmMassa() {
        abonoEmMassaList = new ArrayList<AbonoEmMassa>();
        Banco banco = new Banco();
        Calendar c = new GregorianCalendar();
        c.setTime(dataFim);
        c.add(Calendar.DAY_OF_MONTH, 1);

        if (dataInicio.getTime() <= dataFim.getTime()) {
            if (cod_funcionario.equals(0)) {
                abonoEmMassaList = banco.consultaAbonoEmMassaByDepartameto(departamento, dataInicio, c.getTime());
            } else if (!cod_funcionario.equals(null)) {
                abonoEmMassaList = banco.consultaAbonoEmMassaByFuncionario(cod_funcionario, dataInicio, c.getTime());
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    public void addAbonoEmMassa() {
        Banco banco = new Banco();
        UsuarioBean usuarioBean;
        usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        boolean isDescricaoObrigatoria = banco.isDescricaoObrigatoria(novoAbonoEmMassa.getCod_justificativa());
        if (cod_funcionario.equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Selecione um funcionário!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (novoAbonoEmMassa.getCod_justificativa() == -1) {
            FacesMessage msgErro = new FacesMessage("Selecione uma justificativa!!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (!(novoAbonoEmMassa.getDataInicio().getTime() <= novoAbonoEmMassa.getDataFim().getTime())) {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (isDescricaoObrigatoria && novoAbonoEmMassa.getDescricaoJustificativa().equals("")) {
            FacesMessage msgErro = new FacesMessage("A justificativa escolhida requer uma descrição!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (cod_funcionario.equals(0)) {
            banco.insertAbonoEmMassaEmMassa(funcionarioList, novoAbonoEmMassa, usuarioBean.getUsuario().getLogin());
            FacesMessage msgErro = new FacesMessage("Abono inserido com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
            Metodos.setLogInfo("Abono em Massa - Departamento: " + Metodos.buscaRotulo(departamento.toString(), departamentolist)
                    + " Data e Hora Inicial: " + sfData.format(novoAbonoEmMassa.getDataInicio()) + " " + novoAbonoEmMassa.getHoraInicio()
                    + " Data e Hora Final: " + sfData.format(novoAbonoEmMassa.getDataFim()) + " " + novoAbonoEmMassa.getHoraFim()
                    + " Justificativa: " + Metodos.buscaRotulo(novoAbonoEmMassa.getCod_justificativa().toString(), novoAbonoEmMassa.getJustificativasList()));
            consultaAbonoEmMassa();
        } else {
            //achar
            Boolean flag = banco.insertAbonoEmMassa(cod_funcionario, novoAbonoEmMassa, usuarioBean.getUsuario().getLogin());
            if (flag) {
                SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
                Metodos.setLogInfo("Abono em Massa - Funcionário: "
                        + Metodos.buscaRotulo(cod_funcionario.toString(), funcionarioList)
                        + " Data e Hora Inicial: " + sfData.format(novoAbonoEmMassa.getDataInicio()) + " "
                        + novoAbonoEmMassa.getHoraInicio() + " Data e Hora Final: "
                        + sfData.format(novoAbonoEmMassa.getDataFim()) + " " + novoAbonoEmMassa.getHoraFim()
                        + " Justificativa: "
                        + Metodos.buscaRotulo(novoAbonoEmMassa.getCod_justificativa().toString(), novoAbonoEmMassa.getJustificativasList()));
                FacesMessage msgErro = new FacesMessage("Abono inserido com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                FacesMessage msgErro = new FacesMessage("Erro ao inserir o abono!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            consultaAbonoEmMassa();
        }
    }

    public void alterarAbonoEmMassa() {
        Banco banco = new Banco();
        boolean isDescricaoObrigatoria = banco.isDescricaoObrigatoria(alterarFastamento.getCod_justificativa());
        if (cod_funcionario.equals(-1)) {
            FacesMessage msgErro = new FacesMessage("Selecione um funcionário!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (!(dataInicio.getTime() <= dataFim.getTime())) {
            FacesMessage msgErro = new FacesMessage("Intervalo de datas inválido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (cod_justificativa_selecionada == -1) {
            FacesMessage msgErro = new FacesMessage("Selecione uma justificativa!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else if (isDescricaoObrigatoria && alterarFastamento.getDescricaoJustificativa().equals("")) {
            FacesMessage msgErro = new FacesMessage("A justificativa escolhida requer uma descrição!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            banco = new Banco();
            UsuarioBean usuarioBean;
            usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
            banco.alterarAbonoEmMassa(cod_funcionario_alterado, cod_justificativa_selecionada, alterarFastamento, usuarioBean.getUsuario().getLogin());
            SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy");
            Metodos.setLogInfo("Alterar abono em Massa - Funcionário: " + alterarFastamento.getNomeFuncionario()
                    + " Data e Hora Inicial: " + sfData.format(alterarFastamento.getDataInicio()) + " " + alterarFastamento.getHoraInicio()
                    + " Data e Hora Final: " + sfData.format(alterarFastamento.getDataFim()) + " " + alterarFastamento.getHoraFim()
                    + " Justificativa: " + Metodos.buscaRotulo(alterarFastamento.getCod_justificativa().toString(), alterarFastamento.getJustificativasList()));
            consultaAbonoEmMassa();
        }
    }

    public void excluirAbonoEmMassa() {
        String posicao = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("posAbonoEmMassa");
        Integer posicaoInt = Integer.parseInt(posicao);
        AbonoEmMassa abonoEmMassa = abonoEmMassaList.get(posicaoInt);
        Banco banco = new Banco();
        SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Metodos.setLogInfo("Excluir abono em Massa - Funcionário: " + abonoEmMassa.getNomeFuncionario()
                + " Data e Hora Inicial: " + sfData.format(abonoEmMassa.getInicio())
                + " Data e Hora Final: " + sfData.format(abonoEmMassa.getFim()) + " Justificativa: " + abonoEmMassa.getCategoria());
        banco.deleteAbonoEmMassa(abonoEmMassa);
        consultaAbonoEmMassa();
    }

    public synchronized void excluirTodosAbonoEmMassa() {
        Banco banco = new Banco();

        SimpleDateFormat sfData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
/*        Metodos.setLogInfo("Excluir abono em Massa - Departamento : " + departamentolist.get(departamento).getLabel()
                + " Data e Hora Inicial: " + sfData.format(abonoEmMassaList.get(0).getInicio())
                + " Data e Hora Final: " + sfData.format(abonoEmMassaList.get(0).getFim())
                + " Justificativa: " + abonoEmMassaList.get(0).getCategoria());*/

        banco.deleteTodosAbonoEmMassa(abonoEmMassaList);
        consultaAbonoEmMassa();
    }

    private static Date getPrimeiroDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date data = calendar.getTime();
        SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yyyy");
        String dataPrimeiroDiaStr = sdfHora.format(data.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        try {
            data = df.parse(dataPrimeiroDiaStr);


        } catch (ParseException ex) {
        }
        return data;


    }

    private static Date getHoje() {
        return new Date();
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;


    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;


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

    public Integer getDepartamento() {
        return departamento;


    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;


    }

    public List<SelectItem> getDepartamentolist() {
        return departamentolist;


    }

    public void setDepartamentolist(List<SelectItem> departamentolist) {
        this.departamentolist = departamentolist;


    }

    public List<SelectItem> getFuncionarioList() {
        return funcionarioList;


    }

    public void setFuncionarioList(List<SelectItem> funcionarioList) {
        this.funcionarioList = funcionarioList;


    }

    public Boolean getIncluirSubSetores() {
        return incluirSubSetores;


    }

    public void setIncluirSubSetores(Boolean incluirSubSetores) {
        this.incluirSubSetores = incluirSubSetores;


    }

    public Locale getObjLocale() {
        return objLocale;


    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;


    }

    public List<AbonoEmMassa> getAbonoEmMassaList() {
        return abonoEmMassaList;


    }

    public void setAbonoEmMassaList(List<AbonoEmMassa> abonoEmMassaList) {
        this.abonoEmMassaList = abonoEmMassaList;


    }

    public AbonoEmMassaADD getNovoAbonoEmMassa() {
        return novoAbonoEmMassa;


    }

    public void setNovoAbonoEmMassa(AbonoEmMassaADD novoAbonoEmMassa) {
        this.novoAbonoEmMassa = novoAbonoEmMassa;


    }

    public AbonoEmMassaADD getAlterarFastamento() {
        return alterarFastamento;


    }

    public void setAlterarFastamento(AbonoEmMassaADD alterarFastamento) {
        this.alterarFastamento = alterarFastamento;


    }

    public List<SelectItem> getJustificativasList() {
        return justificativasList;


    }

    public void setJustificativasList(List<SelectItem> justificativasList) {
        this.justificativasList = justificativasList;

    }
}
