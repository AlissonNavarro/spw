/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RelatorioMensal;

import ConsultaPonto.ConsultaFrequenciaComEscalaBean;
import ConsultaPonto.DiaComEscala;
import ConsultaPonto.SituacaoPonto;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author amsgama
 */
public class AFDTBean implements Serializable {

    private Date dataInicio;
    private Date dataFim;
    private Locale objLocale;
    Integer index;
    private List<SelectItem> funcionarioList;
    private Integer cod_funcionario;
    private String departamentoSelecionado;
    private List<SelectItem> departamentosSelecItem;
    private Boolean isAdministradorVisivel;
    private Boolean incluirSubSetores;
    private List<SelectItem> regimeOpcaoFiltroFuncionarioList;
    private Integer regimeSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioRegimeHashMap;
    private Integer tipoGestorSelecionadoOpcaoFiltroFuncionario;
    private HashMap<Integer, Integer> cod_funcionarioGestorHashMap;
    private List<SelectItem> gestorFiltroFuncionarioList;

    public AFDTBean() {
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
         tipoGestorSelecionadoOpcaoFiltroFuncionario = -1;
        cod_funcionarioRegimeHashMap = new HashMap<Integer, Integer>();
        iniciarOpcoesFiltro();
    }

    private void iniciarOpcoesFiltro() {
        gestorFiltroFuncionarioList = getOpcaoFiltroGestor();
        Banco b = new Banco();
        regimeOpcaoFiltroFuncionarioList = b.getRegimeSelectItem();
        cod_funcionarioRegimeHashMap = b.getcod_funcionarioRegime();

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
                Boolean criterioGestor = isFuncionarioDentroCriterioGestor(funcionario_);

                if (criterioGestor && criterioRegime) {
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

    public void gerar() {
        Banco banco = new Banco();
        AFDTCabecalho cabecalho = banco.consultaAFDTCabecalho(dataInicio, dataFim);
        List<AFDTDetalhe> detalheList = getPontosApurados(dataInicio, dataFim);
        AFDTTrailer afdttrailer = trailer();
        AFDT afdt = new AFDT();
        afdt.setCabecalho(cabecalho);
        afdt.setDetalheList(detalheList);
        afdt.setTrailer(afdttrailer);

        gerarAFDT(afdt);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        Metodos.Metodos.setLogInfo("Gerar Relatorio AFDT - Inicio: " + sf.format(dataInicio) + " Fim: " + sf.format(dataFim));

        try {
            popUpMovimento();
        } catch (IOException ex) {
        } catch (Exception ex) {
        }
    }

    private List<AFDTDetalhe> getPontosApurados(Date dataInicio, Date dataFim) {
        ConsultaFrequenciaComEscalaBean c = new ConsultaFrequenciaComEscalaBean("");

        Date dataInicio_ = (Date) dataInicio.clone();
        Date dataFim_ = (Date) dataFim.clone();

        c.setDataInicio(dataInicio_);
        c.setDataFim(dataFim_);
        List<Funcionario> funcionarioList_ = new ArrayList<Funcionario>();
        Banco banco = new Banco();
        funcionarioList_ = banco.consultaFuncionarioDepartamento(funcionarioList);
        HashMap<Integer, Integer> numSensorNumRelogioRep = banco.consultaNumRep();
        List<AFDTDetalhe> afdtDetalheList_ = new ArrayList<AFDTDetalhe>();

        for (Iterator<Funcionario> it = funcionarioList_.iterator(); it.hasNext();) {

            List<AFDTDetalhe> afdtDetalheList = new ArrayList<AFDTDetalhe>();
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
                for (Iterator<DiaComEscala> it1 = diaComEscalaList.iterator(); it1.hasNext();) {
                    DiaComEscala diaComEscala = it1.next();
                    List<SituacaoPonto> situacaoPontoList = diaComEscala.getSituacaoPonto();
                    situacaoPontoList = ordenarSituacao(situacaoPontoList);
                    for (Iterator<SituacaoPonto> it2 = situacaoPontoList.iterator(); it2.hasNext();) {
                        SituacaoPonto situacaoPonto = it2.next();

                        String pis = somentoNumero(getPisOuMatricula(funcionario));
                        Integer numRep = numSensorNumRelogioRep.get(situacaoPonto.getHoraPontoObj().getSensor());

                        AFDTDetalhe afdtDetalhe = setDetalhe(pis, numRep, situacaoPonto);
                        afdtDetalheList.add(afdtDetalhe);
                    }
                }
                afdtDetalheList_.addAll(setnumJornada(afdtDetalheList));
            }
        }
        putCodDetalheAfdt(afdtDetalheList_);
        return afdtDetalheList_;
    }

    private void putCodDetalheAfdt(List<AFDTDetalhe> afdtDetalheList) {
        index = 2;
        for (Iterator<AFDTDetalhe> it = afdtDetalheList.iterator(); it.hasNext();) {
            AFDTDetalhe afdtDetalhe = it.next();
            afdtDetalhe.setCod(index.toString());
            index++;
        }
    }

    public AFDTDetalhe setDetalhe(String pis, Integer numRep, SituacaoPonto situacaoPonto) {
        AFDTDetalhe afdtDetalhe = new AFDTDetalhe();

        afdtDetalhe.setPis(pis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

        String data = sdf.format(situacaoPonto.getHoraPontoObj().getPonto().getTime());
        String hora = sdfHora.format(situacaoPonto.getHoraPontoObj().getPonto().getTime());
        afdtDetalhe.setDataMarcacao(data);
        afdtDetalhe.setHoraMarcacao(hora);
        String tipo = getTipomarcacao(situacaoPonto);
        afdtDetalhe.setTipoMarcacao(tipo);
        if (numRep != null) {
            afdtDetalhe.setNumRep(numRep.toString());
        }

        if (situacaoPonto.getIsAbonado()) {
            afdtDetalhe.setTipoRegistro("I");
        } else if (situacaoPonto.getIsPreAssinalado()) {
            afdtDetalhe.setTipoRegistro("P");
        } else {
            afdtDetalhe.setTipoRegistro("O");
        }

        afdtDetalhe.setMotivo(situacaoPonto.getHoraPontoObj().getJustificativa());

        return afdtDetalhe;
    }

    private List<AFDTDetalhe> setnumJornada(List<AFDTDetalhe> afdtDetalheList) {
        Integer cont = 1;
        String flag = "E";
        List<AFDTDetalhe> afdtDetalheList_ = new ArrayList<AFDTDetalhe>();
        AFDTDetalhe afdtTemp = new AFDTDetalhe();
        Boolean isUltimoEntrada = false;
        for (Iterator<AFDTDetalhe> it = afdtDetalheList.iterator(); it.hasNext();) {
            AFDTDetalhe aFDTDetalhe = it.next();

            if (aFDTDetalhe.getTipoMarcacao().equals("D")) {
            } else {

                if (aFDTDetalhe.getTipoMarcacao().equals("E") && flag.equals("S")) {
                    AFDTDetalhe aFDTDetalhe_ = new AFDTDetalhe();
                    aFDTDetalhe_ = AFDTDetalhe.copy(afdtTemp);
                    aFDTDetalhe_.setTipoMarcacao("S");
                    afdtDetalheList_.add(aFDTDetalhe_);
                    flag = "E";
                }
                isUltimoEntrada = false;
                if (aFDTDetalhe.getTipoMarcacao().equals("E") && !flag.equals("S")) {
                    aFDTDetalhe.setNumJornada(cont.toString());
                    afdtTemp = AFDTDetalhe.copy(aFDTDetalhe);
                    flag = "S";
                    isUltimoEntrada = true;
                }

                if (aFDTDetalhe.getTipoMarcacao().equals("S") && !flag.equals("S")) {
                    AFDTDetalhe aFDTDetalhe_ = new AFDTDetalhe();
                    aFDTDetalhe_ = AFDTDetalhe.copy(aFDTDetalhe);
                    aFDTDetalhe_.setTipoMarcacao("E");
                    aFDTDetalhe_.setNumJornada(cont.toString());
                    afdtDetalheList_.add(aFDTDetalhe_);
                    flag = "S";
                }

                if (aFDTDetalhe.getTipoMarcacao().equals("S") && flag.equals("S")) {
                    aFDTDetalhe.setNumJornada(cont.toString());
                    flag = "E";
                    cont++;
                }
            }

            afdtDetalheList_.add(aFDTDetalhe);
        }
        if (isUltimoEntrada) {
            AFDTDetalhe aFDTDetalhe_ = new AFDTDetalhe();
            aFDTDetalhe_ = AFDTDetalhe.copy(afdtTemp);
            aFDTDetalhe_.setTipoMarcacao("S");
            afdtDetalheList_.add(aFDTDetalhe_);
        }
        return afdtDetalheList_;
    }

    private void print2(List<AFDTDetalhe> afdtDetalheList) {
        try {
            FileOutputStream fos = null;
            File arquivo = new File("C:\\teste\\teste2.txt");
            // Cria o arquivo se este não existe ainda
            arquivo.delete();
            try {
                arquivo.createNewFile();
            } catch (IOException ex) {
                System.out.println("print2 1: "+ex);
                //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            fos = new FileOutputStream(arquivo);
            for (Iterator<AFDTDetalhe> it = afdtDetalheList.iterator(); it.hasNext();) {
                try {
                    AFDTDetalhe detalhe = it.next();
                    fos.write(((detalhe.getDataMarcacao()) + " * ").getBytes());
                    fos.write((detalhe.getHoraMarcacao() + " * ").getBytes());
                    fos.write((detalhe.getPis() + " * ").getBytes());
                    fos.write((detalhe.getNumRep() + " * ").getBytes());
                    fos.write((detalhe.getTipoMarcacao() + " * ").getBytes());
                    fos.write((detalhe.getNumJornada() + " * ").getBytes());
                    fos.write(((detalhe.getTipoRegistro() + " * ")).getBytes());
                    fos.write((detalhe.getMotivo() + " * ").getBytes());
                    fos.write(("\n").getBytes());

                } catch (IOException ex) {
                    System.out.println("print2 2: "+ex);
                    //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fos.close();
        } catch (IOException ex) {
            System.out.println("print2 3: "+ex);
            //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void print1(List<AFDTDetalhe> afdtDetalheList) {
        try {
            FileOutputStream fos = null;
            File arquivo = new File("C:\\teste\\teste1.txt");
            // Cria o arquivo se este não existe ainda
            arquivo.delete();
            try {
                arquivo.createNewFile();
            } catch (IOException ex) {
                System.out.println("print1 1: "+ex);
                //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            fos = new FileOutputStream(arquivo);
            for (Iterator<AFDTDetalhe> it = afdtDetalheList.iterator(); it.hasNext();) {
                try {
                    AFDTDetalhe detalhe = it.next();
                    fos.write(((detalhe.getDataMarcacao()) + " * ").getBytes());
                    fos.write((detalhe.getHoraMarcacao() + " * ").getBytes());
                    fos.write((detalhe.getPis() + " * ").getBytes());
                    fos.write((detalhe.getNumRep() + " * ").getBytes());
                    fos.write((detalhe.getTipoMarcacao() + " * ").getBytes());
                    fos.write((detalhe.getNumJornada() + " * ").getBytes());
                    fos.write(((detalhe.getTipoRegistro() + " * ")).getBytes());
                    fos.write((detalhe.getMotivo() + " * ").getBytes());
                    fos.write(("\n").getBytes());

                } catch (IOException ex) {
                    System.out.println("print1 2: "+ex);
                    //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fos.close();
        } catch (IOException ex) {
            System.out.println("print1 3: "+ex);
            //Logger.getLogger(AFDTBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String somentoNumero(String string) {
        String saida = string.replace(".", "").replace("/", "").replace("-", "");
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

    private String getTipomarcacao(SituacaoPonto situacaoPonto) {
        String saida = null;
        String tipo = situacaoPonto.getSituacao();
        if (tipo.contains("Entrada")) {
            saida = "E";
        } else if (tipo.contains("Saida")) {
            saida = "S";
        } else if (tipo.contains("Descartado")) {
            saida = "D";
        } else if (tipo.contains("Fora da faixa")) {
            saida = "D";
        }
        return saida;
    }

    private AFDTTrailer trailer() {
        AFDTTrailer afdtTrailer = new AFDTTrailer();
        afdtTrailer.setCod(index.toString());
        return afdtTrailer;
    }

    private void gerarAFDT(AFDT afdt) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc = (ServletContext) context.getExternalContext().getContext();
            String path = sc.getRealPath("relatorio/") + "\\";
            File arquivo = new File(path + "AFDT.txt");
            // Cria o arquivo se este não existe ainda
            arquivo.delete();
            arquivo.createNewFile();
            FileOutputStream fos = new FileOutputStream(arquivo);
            AFDTCabecalho cabecalho = afdt.getCabecalho();
            String cabecalhoformatado = cabecalho.gerarCabecalho();
            fos.write((cabecalhoformatado + "\n").getBytes());
            List<AFDTDetalhe> afdtDetalheList = afdt.getDetalheList();
            for (Iterator<AFDTDetalhe> it = afdtDetalheList.iterator(); it.hasNext();) {
                AFDTDetalhe afdtDetalhe = it.next();
                fos.write((afdtDetalhe.gerarDetalhe() + "\n").getBytes());
            }

            fos.write((afdt.getTrailer().gerarTrailer() + "\n").getBytes());
            fos.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public static void popUpMovimento() throws IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("relatorio/") + "\\";
        File file = new File(path + "AFDT.txt");
        byte[] teste = getBytesFromFile(file);
        download(teste, "AFDT.txt");
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

    public List<SituacaoPonto> ordenarSituacao(List<SituacaoPonto> situacaoList) {
        Collections.sort(situacaoList, new Comparator() {

            public int compare(Object o1, Object o2) {
                SituacaoPonto p1 = (SituacaoPonto) o1;
                SituacaoPonto p2 = (SituacaoPonto) o2;
                return p1.getHoraPontoObj().getPonto().getTime() < p2.getHoraPontoObj().getPonto().getTime()
                        ? -1 : (p1.getHoraPontoObj().getPonto().getTime() > p2.getHoraPontoObj().getPonto().getTime()
                        ? +1 : 0);
            }
        });
        return situacaoList;
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

    public Integer getCod_funcionario() {
        return cod_funcionario;
    }

    public void setCod_funcionario(Integer cod_funcionario) {
        this.cod_funcionario = cod_funcionario;
    }

    public HashMap<Integer, Integer> getCod_funcionarioRegimeHashMap() {
        return cod_funcionarioRegimeHashMap;
    }

    public void setCod_funcionarioRegimeHashMap(HashMap<Integer, Integer> cod_funcionarioRegimeHashMap) {
        this.cod_funcionarioRegimeHashMap = cod_funcionarioRegimeHashMap;
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
}
