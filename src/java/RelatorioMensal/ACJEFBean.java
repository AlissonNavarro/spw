/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.Jornada;
import Funcionario.Funcionario;
import Usuario.UsuarioBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author amsgama
 */
public class ACJEFBean implements Serializable {

    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    Integer index;
    List<HorarioContratual> horarioContratualList;
    private List<SelectItem> funcionarioList;
    private Integer cod_funcionario;
    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
    private Boolean isAdministradorVisivel;
    private Boolean incluirSubSetores;
    private Integer codigo_horario = 1;
    //Filtro por regime
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    //Filtro por gestor
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;
    //Filtro por cargo
    private Integer cargoSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioCargoHashMap;
    private List<SelectItem> cargoOpcaoFiltroFuncionarioList;

    public ACJEFBean() {
        dataInicio = getPrimeiroDiaMes();
        dataFim = getHoje();
        objLocale = new Locale("pt", "BR");
        departamentosSelecItem = new ArrayList<SelectItem>();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        inicializarAtributos();
        consultaDepartamento();
    }

    public void consultaDepartamento() {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        String permissao = usuarioBean.getUsuario().getPermissao();
        Banco banco = new Banco();
        departamentosSelecItem = new ArrayList<SelectItem>();
        Integer dept = usuarioBean.getUsuario().getDepartamento();
        Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
        departamentosSelecItem = banco.consultaDepartamentoHierarquico(codigo_funcionario, dept, Integer.parseInt(permissao));
        isAdministradorVisivel = banco.isAdministradorVisivel(codigo_funcionario, dept, Integer.parseInt(permissao));
    }

    public void gerar() {
        Banco banco = new Banco();

        ACJEFCabecalho cabecalho = banco.consultaACJEFCabecalho(dataInicio, dataFim);
        List<ACJEFDetalhe> detalheList = getDetalhe(dataInicio, dataFim);
        ACJEFTrailer acjeftrailer = trailer(detalheList);

        ACJEF acjef = new ACJEF();
        acjef.setCabecalho(cabecalho);
        acjef.setDetalheList(detalheList);
        acjef.setHorarioContratualList(horarioContratualList);
        acjef.setTrailer(acjeftrailer);

        gerarACJEF(acjef);

        try {
            popUpMovimento();
        } catch (IOException ex) {
        } catch (Exception ex) {
        }
    }

    private List<ACJEFDetalhe> getDetalhe(Date dataInicio, Date dataFim) {
        ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

        Date dataInicio_ = (Date) dataInicio.clone();
        Date dataFim_ = (Date) dataFim.clone();

        c.setDataInicio(dataInicio_);
        c.setDataFim(dataFim_);
        List<Funcionario> funcionarioList_ = new ArrayList<Funcionario>();
        Banco banco = new Banco();
        funcionarioList_ = banco.consultaFuncionarioDepartamento(funcionarioList);

        List<ACJEFDetalhe> acjefDetalheList_ = new ArrayList<ACJEFDetalhe>();
        horarioContratualList = new ArrayList<HorarioContratual>();

        for (Iterator<Funcionario> it = funcionarioList_.iterator(); it.hasNext();) {

            dataInicio_ = (Date) dataInicio.clone();
            dataFim_ = (Date) dataFim.clone();
            c = new ConsultaFrequenciaComEscalaBean("");
            c.setDataInicio(dataInicio_);
            c.setDataFim(dataFim_);

            Funcionario funcionario = it.next();
            c.setCod_funcionario(Integer.parseInt(funcionario.getMatricula()));
            c.consultaDiasSemMsgErro();
            List<DiaComEscala> diaComEscalaList = c.getDiasList();
            if (!diaComEscalaList.isEmpty()) {
                List<String> diasComHorasExtraList = c.getDiasComHorasExtraList();
                for (Iterator<DiaComEscala> it1 = diaComEscalaList.iterator(); it1.hasNext();) {
                    DiaComEscala diaComEscala = it1.next();
                    setDetalhe(funcionario, diaComEscala, diasComHorasExtraList, acjefDetalheList_);
                }
            }
        }
        numerarDetalhes(acjefDetalheList_);
        return acjefDetalheList_;
    }

    private ACJEFDetalhe setDetalhe(Funcionario funcionario, DiaComEscala diaComEscala,
            List<String> diasComHorasExtraList, List<ACJEFDetalhe> acjefDetalheList_) {
        String pis = somentoNumero(getPisOuMatricula(funcionario));
        ACJEFDetalhe acjefDetalhe = setDetalhe(funcionario.getMatricula(), pis, diaComEscala, diasComHorasExtraList);

        HorarioContratual horarioContratual = new HorarioContratual();
        horarioContratual = getHorarioContratual(diaComEscala);
        Boolean contemHorario = containsHorarioContratual(horarioContratual, horarioContratualList);

        if (!contemHorario) {
            horarioContratual.setCod_horario(geraZeros(codigo_horario.toString(), 4));
            horarioContratualList.add(horarioContratual);
            codigo_horario++;
        }
        String cod_horario = procuraHorarioContratual(horarioContratual, horarioContratualList);
        acjefDetalhe.setCod_horario(cod_horario);
        acjefDetalheList_.add(acjefDetalhe);

        return acjefDetalhe;
    }

    private Boolean containsHorarioContratual(HorarioContratual horarioContratual, List<HorarioContratual> horarioContratualList) {

        for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.getEntrada1().equals(horarioContratual1.getEntrada1())
                    && horarioContratual.getSaida1().equals(horarioContratual1.getSaida1())
                    && horarioContratual.getEntrada2().equals(horarioContratual1.getEntrada2())
                    && horarioContratual.getSaida2().equals(horarioContratual1.getSaida2())) {
                return true;
            }
        }
        return false;
    }

    private String procuraHorarioContratual(HorarioContratual horarioContratual, List<HorarioContratual> horarioContratualList) {

        for (Iterator<HorarioContratual> it = horarioContratualList.iterator(); it.hasNext();) {
            HorarioContratual horarioContratual1 = it.next();
            if (horarioContratual.getEntrada1().equals(horarioContratual1.getEntrada1())
                    && horarioContratual.getSaida1().equals(horarioContratual1.getSaida1())
                    && horarioContratual.getEntrada2().equals(horarioContratual1.getEntrada2())
                    && horarioContratual.getSaida2().equals(horarioContratual1.getSaida2())) {
                return horarioContratual1.getCod_horario();
            }
        }
        return null;
    }

    private HorarioContratual getHorarioContratual(DiaComEscala diaComEscala) {
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        HorarioContratual horarioContratual = new HorarioContratual();
        List<Jornada> jornadaDiaList = diaComEscala.getJornadaList();
        if (jornadaDiaList.size() == 2) {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            horarioContratual.setEntrada2(sdfHora.format(jornadaDiaList.get(1).getInicioJornadaL()));
            horarioContratual.setSaida2(sdfHora.format(jornadaDiaList.get(1).getTerminioJornadaL()));

        } else {
            horarioContratual.setEntrada1(sdfHora.format(jornadaDiaList.get(0).getInicioJornadaL()));
            horarioContratual.setSaida1(sdfHora.format(jornadaDiaList.get(0).getTerminioJornadaL()));
            if(jornadaDiaList.get(0).getInicioDescanso1() != null)
                horarioContratual.setEntradaD1(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso1()));
            if(jornadaDiaList.get(0).getFimDescanso1() != null)
                horarioContratual.setSaidaD1(sdfHora.format(jornadaDiaList.get(0).getFimDescanso1()));
            if(jornadaDiaList.get(0).getInicioDescanso2() != null)
                horarioContratual.setEntradaD2(sdfHora.format(jornadaDiaList.get(0).getInicioDescanso2()));
            if(jornadaDiaList.get(0).getFimDescanso2() != null)
                horarioContratual.setSaidaD2(sdfHora.format(jornadaDiaList.get(0).getFimDescanso2()));
        }
        return horarioContratual;
    }

    public ACJEFDetalhe setDetalhe(String userid, String pis, DiaComEscala diaComEscala, List<String> diasComHorasExtraList) {
        ACJEFDetalhe acjefDetalhe = new ACJEFDetalhe();

        acjefDetalhe.setPis(pis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

        String data = sdf.format(diaComEscala.getDia());
        String hora = sdfHora.format(diaComEscala.getJornadaList().get(0).getInicioJornadaL());
        acjefDetalhe.setDataInicioJornada(data);
        acjefDetalhe.setHoraEntrada(hora);

        String horaDiurna = diaComEscala.getHorasDiurnasTrabalhadasNaoExtraordinarias();
        acjefDetalhe.setHorasDiurnasNaoExtraordinarias(horaDiurna.replace("h", "").replace("m", "").replace(":", ""));
        String horaNoturna = diaComEscala.getAdicionalNoturnoRelogio();
        acjefDetalhe.setHorasNoturnasNaoExtraordinarias(horaNoturna.replace("h", "").replace("m", "").replace(":", ""));
        acjefDetalhe.setFaltaAtraso("0000");

        if (hasDiaHoraExtra(diaComEscala.getDia(), diasComHorasExtraList)) {
            acjefDetalhe.setSinalHora("0");
            acjefDetalhe.setSaldoHoras("0000");
            setHoraExtra(userid, acjefDetalhe, diaComEscala);

        } else {
            String saldoACompensar = converteHoraPortaria(diaComEscala.getSaldoHoras());
            if (saldoACompensar.equals("0000")) {
                acjefDetalhe.setSinalHora("0");
            } else {
                acjefDetalhe.setSinalHora(sinalHoraACompensar(diaComEscala.getSaldoHoras()));
            }
            acjefDetalhe.setSaldoHoras(saldoACompensar);
        }
        return acjefDetalhe;
    }

    private String sinalHoraACompensar(String horaCompensar) {
        if (horaCompensar.startsWith("-")) {
            return "2";
        } else {
            return "1";
        }
    }

    private void gerarACJEF(ACJEF acjef) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String path = sc.getRealPath("relatorio/") + "\\";
            File arquivo = new File(path + "ACJEF.txt");
            // Cria o arquivo se este não existe ainda
            arquivo.delete();
            arquivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(arquivo);

            ACJEFCabecalho cabecalho = acjef.getCabecalho();
            String cabecalhoformatado = cabecalho.gerarCabecalho();
            fos.write((cabecalhoformatado + "\n").getBytes());

            List<HorarioContratual> acjefHorarioContratualList = acjef.getHorarioContratualList();
            for (Iterator<HorarioContratual> it = acjefHorarioContratualList.iterator(); it.hasNext();) {
                HorarioContratual horarioContratual = it.next();
                fos.write((horarioContratual.gerarHoraContratual() + "\n").getBytes());
            }

            List<ACJEFDetalhe> acjefDetalheList = acjef.getDetalheList();
            for (Iterator<ACJEFDetalhe> it = acjefDetalheList.iterator(); it.hasNext();) {
                ACJEFDetalhe acjefDetalhe = it.next();
                fos.write((acjefDetalhe.gerarDetalhe() + "\n").getBytes());
            }

            fos.write((acjef.getTrailer().gerarTrailer() + "\n").getBytes());
            fos.close();

            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            Metodos.Metodos.setLogInfo("Gerar Relatorio ACJEF - Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));


        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void consultaFuncionario() throws SQLException {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        Banco banco = new Banco();
        funcionarioList = new ArrayList<SelectItem>();
        funcionarioList.add(new SelectItem(-1, "Selecione o funcionario"));
        if (Integer.parseInt(departamentoSelecionado) != usuarioBean.getUsuario().getDepartamento() || isAdministradorVisivel) {
            funcionarioList = banco.consultaFuncionario(Integer.parseInt(departamentoSelecionado), incluirSubSetores);
        } else {
            Integer codigo_funcionario = usuarioBean.getUsuario().getLogin();
            funcionarioList = banco.consultaFuncionarioProprioAdministrador(codigo_funcionario);
        }

        funcionarioList = filtrarFuncionario();

    }

    private void inicializarAtributos() {
        regimeSelecionadoOpcaoFiltroFuncionario = -1;
        cargoSelecionadoOpcaoFiltroFuncionario = -1;
        tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        cod_funcionarioCargoHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
    }

    private void iniciarOpcoesFiltro() {
        gestorFiltroFuncionarioList = getOpcaoFiltroGestor();
        Banco b = new Banco();
        regimeOpcaoFiltroFuncionarioList = b.getRegimeSelectItem();
        cargoOpcaoFiltroFuncionarioList = b.getCargoSelectItem();
        cod_funcionarioRegimeHashMap = b.getcod_funcionarioRegime();
        cod_funcionarioCargoHashMap = b.getcod_funcionarioCargo();

    }

    private List<SelectItem> getOpcaoFiltroGestor() {
        List<SelectItem> tipoFiltroGestorList = new ArrayList<SelectItem>();
        tipoFiltroGestorList.add(new SelectItem(-1, "TODOS"));
        tipoFiltroGestorList.add(new SelectItem(0, "GESTORES SUBORDINADOS AO DEPARTAMENTO"));
        tipoFiltroGestorList.add(new SelectItem(1, "GESTORES SUBORDINADOS DIRETAMENTE AO DEPARTAMENTO"));

        return tipoFiltroGestorList;
    }

    private List<SelectItem> filtrarFuncionario() {
        Banco b = new Banco();
        cod_funcionarioGestorHashMap = b.getcod_funcionarioSubordinacaoDepartamento(Integer.parseInt(departamentoSelecionado));
        List<SelectItem> funcionarioList_ = new ArrayList<SelectItem>();

        for (Iterator<SelectItem> it = funcionarioList.iterator(); it.hasNext();) {
            SelectItem funcionario_ = it.next();
            if (!funcionario_.getValue().toString().equals("-1")&&!funcionario_.getValue().toString().equals("0")) {
                Boolean criterioRegime = isFuncionarioDentroCriterioRegime(funcionario_);
                Boolean criterioCargo = isFuncionarioDentroCriterioCargo(funcionario_);
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario_);

                if (criterioGestor && criterioRegime && criterioCargo) {
                    funcionarioList_.add(funcionario_);
                }
            } else {
                funcionarioList_.add(funcionario_);
            }
        }
        return funcionarioList_;
    }

    private Boolean isFuncionarioDentroCriterioRegime(SelectItem funcionarioSelectItem) {
        Boolean criterioRegime = false;
        Integer regime = cod_funcionarioRegimeHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        criterioRegime = (regimeSelecionadoOpcaoFiltroFuncionario == -1) || regime.equals(regimeSelecionadoOpcaoFiltroFuncionario);

        return criterioRegime;
    }
    
    private Boolean isFuncionarioDentroCriterioCargo(SelectItem funcionarioSelectItem) {
        Boolean criterioCargo = false;
        Integer cargo = cod_funcionarioCargoHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        criterioCargo = (cargoSelecionadoOpcaoFiltroFuncionario == -1) || cargo.equals(cargoSelecionadoOpcaoFiltroFuncionario);
        return criterioCargo;
    }

    private Boolean isFuncionarioDentroCriterioGestor(SelectItem funcionarioSelectItem) {
        Integer tipoGestor = cod_funcionarioGestorHashMap.get(Integer.parseInt(funcionarioSelectItem.getValue().toString()));
        Boolean criterioGestor = false;
        if (tipoGestorSelecionadoOpcaoFiltroFuncionario == -1) {
            criterioGestor = true;
        } else if (tipoGestorSelecionadoOpcaoFiltroFuncionario == 0) {
            if (tipoGestor >= 0) {
                criterioGestor = true;
            }
        } else {
            if (tipoGestor == 1) {
                criterioGestor = true;
            }
        }
        return criterioGestor;
    }

    private void setHoraExtra(String userid, ACJEFDetalhe acjefDetalhe, DiaComEscala diaComEscala) {
        for (Iterator it = diaComEscala.getTipo_valorHorasExtra().keySet().iterator(); it.hasNext();) {
            String nome = (String) it.next();
            Integer valor = (Integer) diaComEscala.getTipo_valorHorasExtra().get(nome);
            if (valor > 0) {
                if (diaComEscala.getSaldoDiurno() > 0) {
                    acjefDetalhe.setHorasExtra1(transformaMinutosEmHora(diaComEscala.getSaldoDiurno()));
                    acjefDetalhe.setModalidadeHE1("D");
                    Banco banco = new Banco();
                    String valor_ = banco.consultaNomePercentagemHoraExtra(userid, nome);
                    valor_ = valor_.replace(",", "").replace(".", "") + "00";
                    acjefDetalhe.setPercentualHE1(valor_);
                }
                if (diaComEscala.getSaldoNoturno() > 0) {
                    acjefDetalhe.setHorasExtra1(transformaMinutosEmHora(diaComEscala.getSaldoNoturno()));
                    acjefDetalhe.setModalidadeHE1("N");
                    Banco banco = new Banco();
                    String valor_ = banco.consultaNomePercentagemHoraExtra(userid, nome);
                    valor_ = valor_.replace(",", "").replace(".", "") + "00";
                    acjefDetalhe.setPercentualHE1(valor_);
                }
            }
        }
    }

    private String somentoNumero(String string) {
        String saida = string.replace(".", "").replace("/", "").replace("-", "");
        return saida;
    }

    private String converteHoraPortaria(String string) {
        String saida = string.replace(":", "").replace("-", "").replace(" ", "").replace("h", "").replace("m", "");
        return saida;
    }

    private Boolean hasDiaHoraExtra(Date data_, List<String> diasComHorasExtraList) {
        SimpleDateFormat sdfSemana = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdfSemana.format(data_.getTime());
        if (diasComHorasExtraList.contains(data)) {
            return true;
        } else {
            return false;
        }

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

    private void numerarDetalhes(List<ACJEFDetalhe> acjefDetalheList_) {
        Integer i = 0;
        if (horarioContratualList.size() > 0) {
            i = Integer.parseInt(horarioContratualList.get(horarioContratualList.size() - 1).getCod_horario()) + 1;
        }
        for (Iterator<ACJEFDetalhe> it = acjefDetalheList_.iterator(); it.hasNext();) {
            ACJEFDetalhe aCJEFDetalhe = it.next();
            aCJEFDetalhe.setCod(i.toString());
            i++;
        }
    }

    private ACJEFTrailer trailer(List<ACJEFDetalhe> detalheList) {
        ACJEFTrailer acjefTrailer = new ACJEFTrailer();
        if (detalheList.size() > 0) {
            Integer cod = Integer.parseInt(detalheList.get(detalheList.size() - 1).getCod()) + 1;
            acjefTrailer.setCod(cod.toString());
        } else {
            acjefTrailer.setCod("2");
        }
        return acjefTrailer;
    }

    private String geraZeros(String cod, Integer tamanho) {

        String saida = cod;
        Integer tamanhoEntranda = 0;

        if (saida != null) {
            tamanhoEntranda = cod.length();
        } else {
            saida = "";
        }

        Integer espacos = tamanho - tamanhoEntranda;

        for (int i = 0; i < espacos; i++) {
            saida = "0" + saida;
        }
        return saida;

    }

    private String getPisOuMatricula(Funcionario funcionario) {
        String pisOuMatricula = "";
        if (funcionario.getPIS() == null || funcionario.getMatricula().equals("")) {
            pisOuMatricula = funcionario.getMatricula();
        } else {
            pisOuMatricula = funcionario.getPIS();
        }
        return pisOuMatricula;
    }

    private static Date getHoje() {
        return new Date();
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

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    private String transformaMinutosEmHora(Integer tempoemminutos_) {

        Long tempoemminutos = Long.parseLong(tempoemminutos_.toString());

        Long horas = tempoemminutos / 60;
        Long minutos = tempoemminutos % 60;

        String horaSrt = "";
        String minutosSrt = "";

        if (horas < 10) {
            horaSrt = "0" + horas;
        } else {
            horaSrt = horas.toString();
        }

        if (minutos < 10) {
            minutosSrt = "0" + minutos;
        } else {
            minutosSrt = minutos.toString();
        }

        return horaSrt + minutosSrt;
    }

    public static void popUpMovimento() throws IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("relatorio/") + "\\";
        File file = new File(path + "ACJEF.txt");
        byte[] teste = getBytesFromFile(file);
        download(teste, "ACJEF.txt");
    }

    public static void download(byte[] arquivo, String nome) throws Exception {
        HttpServletResponse response = getServletResponse();
        response.setContentType(null);
        response.setContentLength(arquivo.length);
        response.addHeader("Content-Disposition", "attachment; filename=" + "\"" + nome + "\"");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(arquivo, 0, arquivo.length);
        outputStream.flush();
        outputStream.close();
        getFacesContext().responseComplete();
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
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

    public Boolean getIsAdministradorVisivel() {
        return isAdministradorVisivel;
    }

    public void setIsAdministradorVisivel(Boolean isAdministradorVisivel) {
        this.isAdministradorVisivel = isAdministradorVisivel;
    }

    public List<SelectItem> getRegimeOpcaoFiltroFuncionarioList() {
        return regimeOpcaoFiltroFuncionarioList;
    }

    public void setRegimeOpcaoFiltroFuncionarioList(List<SelectItem> regimeOpcaoFiltroFuncionarioList) {
        this.regimeOpcaoFiltroFuncionarioList = regimeOpcaoFiltroFuncionarioList;
    }

    public Integer getRegimeSelecionadoOpcaoFiltroFuncionario() {
        return regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public void setRegimeSelecionadoOpcaoFiltroFuncionario(Integer regimeSelecionadoOpcaoFiltroFuncionario) {
        this.regimeSelecionadoOpcaoFiltroFuncionario = regimeSelecionadoOpcaoFiltroFuncionario;
    }

    public HashMap<Integer, Integer> getCod_funcionarioRegimeHashMap() {
        return cod_funcionarioRegimeHashMap;
    }

    public void setCod_funcionarioRegimeHashMap(HashMap<Integer, Integer> cod_funcionarioRegimeHashMap) {
        this.cod_funcionarioRegimeHashMap = cod_funcionarioRegimeHashMap;
    }

    public HashMap<Integer, Integer> getCod_funcionarioGestorHashMap() {
        return cod_funcionarioGestorHashMap;
    }

    public void setCod_funcionarioGestorHashMap(HashMap<Integer, Integer> cod_funcionarioGestorHashMap) {
        this.cod_funcionarioGestorHashMap = cod_funcionarioGestorHashMap;
    }

    public List<SelectItem> getGestorFiltroFuncionarioList() {
        return gestorFiltroFuncionarioList;
    }

    public void setGestorFiltroFuncionarioList(List<SelectItem> gestorFiltroFuncionarioList) {
        this.gestorFiltroFuncionarioList = gestorFiltroFuncionarioList;
    }

    public Integer getTipoGestorSelecionadoOpcaoFiltroFuncionario() {
        return tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public void setTipoGestorSelecionadoOpcaoFiltroFuncionario(Integer tipoGestorSelecionadoOpcaoFiltroFuncionario) {
        this.tipoGestorSelecionadoOpcaoFiltroFuncionario = tipoGestorSelecionadoOpcaoFiltroFuncionario;
    }

    public Integer getCargoSelecionadoOpcaoFiltroFuncionario() {
        return cargoSelecionadoOpcaoFiltroFuncionario;
    }

    public void setCargoSelecionadoOpcaoFiltroFuncionario(Integer cargoSelecionadoOpcaoFiltroFuncionario) {
        this.cargoSelecionadoOpcaoFiltroFuncionario = cargoSelecionadoOpcaoFiltroFuncionario;
    }

    public HashMap<Integer, Integer> getCod_funcionarioCargoHashMap() {
        return cod_funcionarioCargoHashMap;
    }

    public void setCod_funcionarioCargoHashMap(HashMap<Integer, Integer> cod_funcionarioCargoHashMap) {
        this.cod_funcionarioCargoHashMap = cod_funcionarioCargoHashMap;
    }

    public List<SelectItem> getCargoOpcaoFiltroFuncionarioList() {
        return cargoOpcaoFiltroFuncionarioList;
    }

    public void setCargoOpcaoFiltroFuncionarioList(List<SelectItem> cargoOpcaoFiltroFuncionarioList) {
        this.cargoOpcaoFiltroFuncionarioList = cargoOpcaoFiltroFuncionarioList;
    }
    
}
