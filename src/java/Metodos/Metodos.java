package Metodos;

import Administracao.Conexao;
import ConsultaPonto.Jornada;
import Usuario.UsuarioBean;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Metodos {

    static Document doc = null;
    static String driver = "jdbc:jtds:sqlserver://";
    static boolean servidorAtivo = false;
    private static String version = "1.9.2";
    private static String urlCompleta = "";
    
    public static void carregarUrlCompleta() {
        urlCompleta = getUrlConexao();
    }
    
    public static String getUrlCompleta() {
        return urlCompleta;
    } 

    public static void setServidorAtivo() {
        try {
            servidorAtivo = InetAddress.getByName(getServerConexao()).isReachable(10000);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean getServidorAtivo() {
        return servidorAtivo;
    }

    public static List<Jornada> getJornadaDia(List<Jornada> jornadaList, Date date,
            HashMap<Integer, List<Integer>> diasHashMap) {

        List<Jornada> jornadaReturnList = new ArrayList<Jornada>();

        Jornada jornada_ = new Jornada();

        for (Iterator<Jornada> it = jornadaList.iterator(); it.hasNext();) {

            Jornada jornada = it.next();
            jornada_ = new Jornada();

            if (jornada.getUnits() == 0 || jornada.getUnits() == 2) {

                GregorianCalendar diaHorainicio = new GregorianCalendar();
                diaHorainicio.setTime(date);
                Integer dia = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

                Integer sdayDate = 0;
                Integer edayDate = 0;

                if (diasHashMap.containsKey(dia)) {
                    if (diasHashMap.get(dia).size() == 1) {
                        sdayDate = diasHashMap.get(dia).get(0);

                        if (jornada.getSday() != jornada.getEday()) {
                            jornada_.setTemVirada(true);

                            if (jornada.getSday() == sdayDate) {

                                jornada_.setNome(jornada.getNome());
                                jornada_.setSchClassID(jornada.getSchClassID());
                                jornada_.setLegend(jornada.getLegend());
                                jornada_.setInicioJornada(jornada.getInicioJornada());
                                jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                                jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                                jornada_.setPrazoMinutosAtraso(jornada.getPrazoMinutosAtraso());
                                jornada_.setIsEntradaObrigatoria(jornada.getIsEntradaObrigatoria());
                                jornada_.setCheckInLimiteAntecipada(jornada.getCheckInLimiteAntecipada());
                                jornada_.setCheckInLimiteAtrasada(jornada.getCheckInLimiteAtrasada());
                                jornada_.setSday(jornada.getSday());

                                jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                                jornada_.setTipoJornada(jornada.getTipoJornada());
                                jornada_.setCyle(jornada.getCyle());
                                jornada_.setUnits(jornada.getUnits());
                                jornada_.setEday_(jornada.getEday());
                                jornada_.setSoEntrada(true);
                                jornada_.setSoSaida(false);

                                jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                                jornada_.setFimDescanso1(jornada.getFimDescanso1());
                                jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                                jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                                jornada_.setFimDescanso2(jornada.getFimDescanso2());
                                jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                                jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                                jornada_.setIsOvertime(jornada.getIsOvertime());

                                jornadaReturnList.add(jornada_);
                            }

                            if (jornada.getEday() == sdayDate) {

                                jornada_.setNome(jornada.getNome());
                                jornada_.setSchClassID(jornada.getSchClassID());
                                jornada_.setLegend(jornada.getLegend());
                                jornada_.setTerminioJornada(jornada.getTerminioJornada());
                                jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                                jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                                jornada_.setPrazoMinutosAdiantado(jornada.getPrazoMinutosAdiantado());
                                jornada_.setIsSaidaObrigatoria(jornada.getIsSaidaObrigatoria());
                                jornada_.setCheckOutLimiteAntecipada(jornada.getCheckOutLimiteAntecipada());
                                jornada_.setCheckOutLimiteAtrasada(jornada.getCheckOutLimiteAtrasada());
                                jornada_.setEday(jornada.getEday());

                                jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                                jornada_.setTipoJornada(jornada.getTipoJornada());
                                jornada_.setCyle(jornada.getCyle());
                                jornada_.setUnits(jornada.getUnits());
                                jornada_.setSday_(jornada.getSday());
                                jornada_.setSoEntrada(false);
                                jornada_.setSoSaida(true);

                                jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                                jornada_.setFimDescanso1(jornada.getFimDescanso1());
                                jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                                jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                                jornada_.setFimDescanso2(jornada.getFimDescanso2());
                                jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                                jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                                jornada_.setIsOvertime(jornada.getIsOvertime());

                                jornadaReturnList.add(jornada_);
                            }

                        } else if (jornada.getSday() == sdayDate) {
                            jornadaReturnList.add(jornada);
                        }
                    } else if (diasHashMap.get(dia).size() == 2) {
                        sdayDate = diasHashMap.get(dia).get(1);
                        edayDate = diasHashMap.get(dia).get(0);

                        if (jornada.getSday() != jornada.getEday()) {
                            jornada_.setTemVirada(true);
                            if (jornada.getEday() == edayDate) {

                                jornada_.setNome(jornada.getNome());
                                jornada_.setSchClassID(jornada.getSchClassID());
                                jornada_.setLegend(jornada.getLegend());
                                jornada_.setTerminioJornada(jornada.getTerminioJornada());
                                jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                                jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                                jornada_.setPrazoMinutosAdiantado(jornada.getPrazoMinutosAdiantado());
                                jornada_.setIsSaidaObrigatoria(jornada.getIsSaidaObrigatoria());
                                jornada_.setCheckOutLimiteAntecipada(jornada.getCheckOutLimiteAntecipada());
                                jornada_.setCheckOutLimiteAtrasada(jornada.getCheckOutLimiteAtrasada());
                                jornada_.setEday(jornada.getEday());

                                jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                                jornada_.setTipoJornada(jornada.getTipoJornada());
                                jornada_.setCyle(jornada.getCyle());
                                jornada_.setUnits(jornada.getUnits());
                                jornada_.setSday_(jornada.getSday());
                                jornada_.setSoEntrada(false);
                                jornada_.setSoSaida(true);

                                jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                                jornada_.setFimDescanso1(jornada.getFimDescanso1());
                                jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                                jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                                jornada_.setFimDescanso2(jornada.getFimDescanso2());
                                jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                                jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                                jornada_.setIsOvertime(jornada.getIsOvertime());

                                jornadaReturnList.add(jornada_);

                            }
                            jornada_ = new Jornada();
                            jornada_.setTemVirada(true);
                            if (jornada.getSday() == sdayDate) {

                                jornada_.setNome(jornada.getNome());
                                jornada_.setSchClassID(jornada.getSchClassID());
                                jornada_.setLegend(jornada.getLegend());
                                jornada_.setInicioJornada(jornada.getInicioJornada());
                                jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                                jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                                jornada_.setPrazoMinutosAtraso(jornada.getPrazoMinutosAtraso());
                                jornada_.setIsEntradaObrigatoria(jornada.getIsEntradaObrigatoria());
                                jornada_.setCheckInLimiteAntecipada(jornada.getCheckInLimiteAntecipada());
                                jornada_.setCheckInLimiteAtrasada(jornada.getCheckInLimiteAtrasada());
                                jornada_.setSday(jornada.getSday());

                                jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                                jornada_.setTipoJornada(jornada.getTipoJornada());
                                jornada_.setCyle(jornada.getCyle());
                                jornada_.setUnits(jornada.getUnits());
                                jornada_.setEday_(jornada.getEday());
                                jornada_.setSoEntrada(true);
                                jornada_.setSoSaida(false);

                                jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                                jornada_.setFimDescanso1(jornada.getFimDescanso1());
                                jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                                jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                                jornada_.setFimDescanso2(jornada.getFimDescanso2());
                                jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                                jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                                jornada_.setIsOvertime(jornada.getIsOvertime());

                                jornadaReturnList.add(jornada_);
                            }
                        } else if (jornada.getSday() == sdayDate) {
                            jornadaReturnList.add(jornada);
                        }
                    }
                }
            }

            if (jornada.getUnits() == 1) {

                GregorianCalendar diaHorainicio = new GregorianCalendar();
                diaHorainicio.setTime(date);
                Integer dia = diaHorainicio.get(Calendar.DAY_OF_YEAR) + diaHorainicio.get(Calendar.YEAR) * 365;

                Integer sdayDate = getSday(diasHashMap, dia);

                if (jornada.getSday() != convertDay(jornada.getEday())) {
                    jornada_.setTemVirada(true);

                    if (jornada.getSday() == sdayDate) {

                        jornada_.setNome(jornada.getNome());
                        jornada_.setSchClassID(jornada.getSchClassID());
                        jornada_.setLegend(jornada.getLegend());
                        jornada_.setInicioJornada(jornada.getInicioJornada());
                        jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                        jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                        jornada_.setPrazoMinutosAtraso(jornada.getPrazoMinutosAtraso());
                        jornada_.setIsEntradaObrigatoria(jornada.getIsEntradaObrigatoria());
                        jornada_.setCheckInLimiteAntecipada(jornada.getCheckInLimiteAntecipada());
                        jornada_.setCheckInLimiteAtrasada(jornada.getCheckInLimiteAtrasada());
                        jornada_.setSday(jornada.getSday());

                        jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                        jornada_.setTipoJornada(jornada.getTipoJornada());
                        jornada_.setCyle(jornada.getCyle());
                        jornada_.setUnits(jornada.getUnits());
                        jornada_.setEday_(jornada.getEday());
                        jornada_.setSoEntrada(true);
                        jornada_.setSoSaida(false);

                        jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                        jornada_.setFimDescanso1(jornada.getFimDescanso1());
                        jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                        jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                        jornada_.setFimDescanso2(jornada.getFimDescanso2());
                        jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                        jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                        jornada_.setIsOvertime(jornada.getIsOvertime());

                        jornadaReturnList.add(jornada_);
                    }

                    if (convertDay(jornada.getEday()) == sdayDate) {

                        jornada_.setNome(jornada.getNome());
                        jornada_.setSchClassID(jornada.getSchClassID());
                        jornada_.setLegend(jornada.getLegend());
                        jornada_.setTerminioJornada(jornada.getTerminioJornada());
                        jornada_.setInicioJornadaL(jornada.getInicioJornadaL());
                        jornada_.setTerminioJornadaL(jornada.getTerminioJornadaL());
                        jornada_.setPrazoMinutosAdiantado(jornada.getPrazoMinutosAdiantado());
                        jornada_.setIsSaidaObrigatoria(jornada.getIsSaidaObrigatoria());
                        jornada_.setCheckOutLimiteAntecipada(jornada.getCheckOutLimiteAntecipada());
                        jornada_.setCheckOutLimiteAtrasada(jornada.getCheckOutLimiteAtrasada());
                        jornada_.setEday(jornada.getEday());

                        jornada_.setTrabalhoMinutos(jornada.getTrabalhoMinutos());
                        jornada_.setTipoJornada(jornada.getTipoJornada());
                        jornada_.setCyle(jornada.getCyle());
                        jornada_.setUnits(jornada.getUnits());
                        jornada_.setSday_(jornada.getSday());
                        jornada_.setSoEntrada(false);
                        jornada_.setSoSaida(true);

                        jornada_.setInicioDescanso1(jornada.getInicioDescanso1());
                        jornada_.setFimDescanso1(jornada.getFimDescanso1());
                        jornada_.setDeduzirDescanco1(jornada.getDeduzirDescanco1());
                        jornada_.setInicioDescanso2(jornada.getInicioDescanso2());
                        jornada_.setFimDescanso2(jornada.getFimDescanso2());
                        jornada_.setDeduzirDescanco2(jornada.getDeduzirDescanco2());

                        jornada_.setToleranciaDescanso(jornada.getToleranciaDescanso());
                        jornada_.setIsOvertime(jornada.getIsOvertime());

                        jornadaReturnList.add(jornada_);
                    }
                } else if (jornada.getSday() == sdayDate) {
                    jornadaReturnList.add(jornada);
                }
            }

        }
        return verificandoPossivelInversao(jornadaReturnList);
    }

    public static String getUrlConexao() {
        String urlConexao = driver + getServerConexao() + ":"
                + getPortConexao() + "/"
                + getDatabaseConexao()
                + ";user=" + getUserConexao()
                + ";password=" + getPasswordConexao();
        return urlConexao;
    }

    public static String getServerConexao() {
        String server = getTag("Server");
        return server;
    }

    public static String getPortConexao() {
        String port = getTag("Port");
        return port;
    }

    public static String getDatabaseConexao() {
        String database = getTag("Database");
        return database;
    }

    public static String getUserConexao() {
        String user = getTag("User");
        return user;
    }

    public static String getPasswordConexao() {
        String password = getTag("Password");
        return password;
    }

    public static Conexao getConexao() {
        Conexao c = new Conexao();
        return c;

    }

    public static String getTitulo() {
        Banco b = new Banco();
        String titulo = b.getTitulo();
        return titulo;
    }

    public static String getVersao() {
        return getVersion();
    }

    public static InputStream getLogo() {
        Banco b = new Banco();
        InputStream input = b.getLogo();
        return input;
    }

    public static String getSenhaAdministrador() {
        Banco banco = new Banco();
        String senha = null;
        if (getServidorAtivo()) {

            if (banco.hasConnection) {
                senha = banco.getSenhaAdministrador();
            }
        }
        if (senha == null) {
            //Pega a senha do config.xml em caso de banco vazio
            senha = getTag("SenhaAdministrador");
        }
        return senha;
    }

    public static void setServerConexao(String server) {
        editarTag("Server", server);
    }

    public static void setPortConexao(String port) {
        editarTag("Port", port);
    }

    public static void setDataBaseConexao(String database) {
        editarTag("Database", database);
    }

    public static void setUserConexao(String user) {
        editarTag("User", user);
    }

    public static void setPasswordConexao(String password) {
        editarTag("Password", password);
    }

    public static String carregar(String arquivo)
            throws FileNotFoundException, IOException {

        File file = new File(arquivo);

        if (!file.exists()) {
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        StringBuffer bufSaida = new StringBuffer();

        String linha;
        while ((linha = br.readLine()) != null) {
            bufSaida.append(linha + "\n");
        }
        br.close();
        return bufSaida.toString();
    }

    public static void remover(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                remover(files[i]);
            }
        }
        f.delete();
    }

    public static void criarPasta(String path) {
        File dir = new File(path);
        dir.mkdirs();
    }

    public static void salvar(String arquivo, String conteudo)
            throws IOException {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(arquivo));
            out.write(conteudo);
            out.close();
        } catch (IOException e) {
        }
    }

    public static String getPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("relatorio/") + "\\";
        return path;
    }

    public static void setLogPrint(String cpfrequerente, String nomerequerente, String cpfrequerido, String nomerequerido) {
        String msg = "Op: " + "Impressão - frequencia com escala. Requerido - " + cpfrequerido + " " + nomerequerido + ". Requerente - " + cpfrequerente + " " + nomerequerente + ".";
        Metodos.setLog("INFO", msg);
    }

    public static void setLogInfo(String operacao, String cpfrequerido, String nomerequerido) {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        String msg = "Op: " + operacao + ". Requerente - " + usuarioBean.getLogin() + " " + usuarioBean.getUsuario().getNome() + ". " + "Requerido - " + cpfrequerido + " " + nomerequerido + ".";
        Metodos.setLog("INFO", msg);
    }

    public static void setLogInfo(String operacao) {
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        String msg = "Op: " + operacao + ". Requerente - " + usuarioBean.getLogin() + " " + usuarioBean.getUsuario().getNome() + ".";
        Metodos.setLog("INFO", msg);
    }

    public static void setLog(String level, String msg) {

        Logger logger = Logger.getLogger("MyLogi");
        FileHandler fh;
        try {
            Date data = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
            String dateString = sf.format(data);
            fh = new FileHandler(getPathLogs() + dateString + ".log", true);
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.logp(Level.parse(level), "", "", msg);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static String buscaRotulo(String index, List<SelectItem> list) {
        int j = 0;
        String label = " ";
        while (j < list.size() && !list.get(j).getValue().toString().equals(index)) {
            j++;
        }
        if (j < list.size()) {
            label = list.get(j).getLabel();
        }
        return label;
    }

    public static String getPathText() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("resources/") + "\\";
        return path;
    }

    public static String getPathLogs() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("logs/") + "\\";
        return path;
    }

    public static String getPathImages() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext sc = (ServletContext) context.getExternalContext().getContext();
        String path = sc.getRealPath("images/") + "\\";
        return path;
    }

    public static String formatarDataSemHora(String data) {
        String resultado = "";
        Stack pilha = new Stack();
        for (int i = (data.length() - 1); i >= 0; i--) {
            if (data.charAt(i) == ' ') {
                break;
            }
            pilha.push(data.charAt(i));
        }
        int size = pilha.size();
        for (int i = 0; i < size; i++) {
            resultado += pilha.pop();
        }
        return resultado;
    }

    private static void carregarXML() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(new File(getPathText() + "config.xml"));
        } catch (SAXException e) {
            System.exit(1);
        } catch (ParserConfigurationException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void editarTag(String tag, String titulo) {
        carregarXML();
        Element dE = doc.getDocumentElement();
        NodeList nodeList = dE.getElementsByTagName(tag);
        Element element = (Element) nodeList.item(0);
        element.setTextContent(titulo);
        salvarAlteracoes();
    }

    private static String getTag(String tag) {
        String txt = null;
        try {
            carregarXML();
            Element dE = doc.getDocumentElement();
            NodeList nodeList = dE.getElementsByTagName(tag);
            Element element = (Element) nodeList.item(0);
            txt = element.getFirstChild().getNodeValue();
        } catch (NullPointerException ex) {
            return null;
        }
        return txt;
    }

    public static void setTitulo(String titulo) {
        Banco b = new Banco();
        String senha = b.getSenhaAdministrador();
        byte[] logo = b.getImageLogo();
        int tipoRelatorio = b.getTipoRelatorio();
        b.deleteAllConfig();
        b.inserirConfig(titulo, senha, tipoRelatorio, logo);
    }

    public static void setSenha(String senha) {
        Banco b = new Banco();
        b.setSenhaAdmin(senha);
    }

    public static void setURL(String url) {
        editarTag("Conexao", url);
    }

    private static void salvarAlteracoes() {
        DOMSource domSrc = new DOMSource(doc);
        StreamResult streamResult = null;
        try {
            streamResult = new StreamResult(new FileOutputStream(getPathText() + "config.xml"));
        } catch (FileNotFoundException ex) {
        }
        TransformerFactory transformerFac = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFac.newTransformer();
        } catch (TransformerConfigurationException ex) {
        }
        try {
            transformer.transform(domSrc, streamResult);
        } catch (TransformerException ex) {
        }
    }

    public static void download(String nome) throws Exception {
        File file = new File(getPath() + nome);
        byte[] arquivo = getBytesFromFile(file);
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

    private static byte[] getBytesFromFile(File file) throws IOException {
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

    private static HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Integer eday = 0;
        if ((eday % 8 == 0) && eday != 0) {
            eday = eday - 7;
        }
        System.out.print(eday);
    }

    private static Integer convertDay(Integer eday) {
        if ((eday % 8 == 0) && eday != 0) {
            eday = eday - 7;
        }
        return eday;
    }

    private static List<Jornada> verificandoPossivelInversao(List<Jornada> jornadaList) {
        List<Jornada> jornadaList_ = new ArrayList<Jornada>();
        if (jornadaList.size() == 2) {
            if (jornadaList.get(0).getSoEntrada() && jornadaList.get(1).getSoSaida()) {
                Jornada temp0 = jornadaList.get(0);
                Jornada temp1 = jornadaList.get(1);
                jornadaList_ = new ArrayList<Jornada>();
                jornadaList_.add(temp1);
                jornadaList_.add(temp0);
                return jornadaList_;
            } else {
                return jornadaList;
            }
        } else {
            return jornadaList;
        }
    }

    private static Integer getSday(HashMap<Integer, List<Integer>> diasHashMap, Integer dia) {
        if (diasHashMap.get(dia) == null) {
            if (diasHashMap.get(dia - 1) != null) {
                int saida = diasHashMap.get(dia - 1).get(0) + 1;
                return convertDay(saida);
            } else {
                if (diasHashMap.size() == 0) {
                    System.out.println("Usuario nao tem escala. falha em Metodos.getSday. msg 1");
                    return null;
                } else {
                    int aux = 1;
                    for (int x = dia; x > dia - 15; x--) {
                        if (diasHashMap.containsKey(x)) {
                            int saida = diasHashMap.get(x).get(0) + aux;
                            return convertDay(saida);
                        }
                        aux++;
                    }
                    System.out.println("Usuario nao tem escala. falha em Metodos.getSday. msg 2");
                    return null;
                }
            }
        }
        return diasHashMap.get(dia).get(0);
    }

    public static void oi(int x) {
        x += 8;
    }

    /**
     * @return the version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * @param aVersion the version to set
     */
    public static void setVersion(String aVersion) {
        version = aVersion;
    }

}
