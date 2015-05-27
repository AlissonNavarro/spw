/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockManager;

import Machines.Machine;
import Machines.MachineBanco;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
//import servico.WebService1;
//import servico.WebService1Soap;

/**
 *
 * @author ppccardoso
 */
public class ClockManagerBean implements Serializable {

    
    private MachineBanco banco;
    //Variaveis do Bean
    private String abaCorrente;
    //Tabela de relógios
    private List<Machine> allClocks;
    private int qtdThreads = 320;
    private int timeout = 15000;
    //Detalhe dos Relógios
    private Machine clock;
    private int selectedClock;
    private Date dataLimite;
    //Variaveis do webService c#
    //private WebService1Soap soap;
    private Boolean webServup;
    private int DiasColetados;
    //Variaveis da interface
    private String ipInicio;
    private String ipFim;
    private String horas;
    private List<Rep> ips;
    private int page;
    //IDDATA
    private Boolean showFileBrowser = false;
    private String softName = "";
    private String fileWay = "C:/Arquivos de programas/ID DATA Tecnologia/ID REP Config 32 Bits/ID REP Config.exe";
    private String dbway;
    private String newFileWay = "";
    private int newValidWay = 0; // 0 = sem reação; 1 = reação negativa; 2 = reação positiva;
    //TRILOBIT
    private String IpDigitSender;
    private String FullNameIpSender;
    //ZK

    //--------------------------------------------------------------------------
    //Metodos genéricos
    //--------------------------------------------------------------------------
    public ClockManagerBean() {
        System.out.println("Contructor ClockManagerBean");
        prepareIdData();
        clock = new Machine();
        /*try {
         WebService1 w = new WebService1();
         soap = w.getWebService1Soap();
         webServup = true;
         System.out.println("webservice on");
         } catch (Exception ex) {
         webServup = false;
         System.out.println("webservice off");
         }
         if (((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean")).getPerfil().getListaRelogios()) {
         verifyClocks();
         }*/
        MachineBanco mb = new MachineBanco();
        allClocks = mb.consultaRelogios();
    }

    //Guarda no Bean a última aba aberta
    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
    }

    public void verifySingleClock() {
        try {
            InetAddress iad = InetAddress.getByName(clock.getRepIp());

            if (iad.isReachable(timeout)) {
                clock.setOnline(true);
            } else {
                clock.setOnline(false);
            }
            System.out.println("clock: " + clock.getOnline().toString());
            colectData();
        } catch (Exception e) {
            System.out.println("Verify Single Clock: Error: " + e.getMessage());
        }
    }

    //Altera somente o alias e ip do rep
    public void renameRep() {
        MachineBanco mb = new MachineBanco();
        mb.editRelogio(clock);
        allClocks = mb.consultaRelogios();

        FacesMessage msgErro = new FacesMessage("Relógio alterado! nome: " + clock.getRepAlias() + " ip: " + clock.getRepIp());
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    //Coleta os dads do relógio pesquisado
    private void colectData() {
        if (webServup) {
            clock.setQtdBatery(70);
            clock.setQtdBobina(0);
            //clock.setQtdUsers(soap.totalFuncionarios(clock.getRepIp(), clock.getRepPort(), clock.getRepType()));
            //clock.setQtdDigitais(soap.totalDigitais(clock.getRepIp(), clock.getRepPort(), clock.getRepType()));

            clock.setMaxUsers(1000);
            clock.setMaxDigitais(800);
        } else {
            FacesMessage msgErro = new FacesMessage("Falha ao acessar relógio");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
    }

    //Verifica todos os relógios
    public void verifyClocks() {
        MachineBanco mb = new MachineBanco();
        allClocks = mb.consultaRelogios();

        ExecutorService es = Executors.newFixedThreadPool(qtdThreads);
        final List<Future<Rep>> futures = new ArrayList<Future<Rep>>();
        for (int i = 0; i < allClocks.size(); i++) {
            futures.add(ipsAtivos(es, allClocks.get(i).getRepIp(), timeout, i));
        }

        for (final Future<Rep> f : futures) {
            try {
                if (f.get().resultado) {
                    allClocks.get(f.get().i).setOnline(true);
                } else {
                    allClocks.get(f.get().i).setOnline(false);
                }
            } catch (InterruptedException ex) {
            } catch (ExecutionException ex) {
            } catch (Exception ex) {
            }
        }

        es.shutdown();
        futures.clear();

        FacesMessage msgErro = new FacesMessage(allClocks.size() + " relógios Verificados");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    //Verifica, através do ping, os ip's ativos de uma faixa
    public void verifyIps() {

        String aux = "";
        int[] ip1 = new int[4];
        ip1[0] = -1;
        ip1[1] = -1;
        ip1[2] = -1;
        ip1[3] = -1;
        boolean errado = false;

        ipInicio = ipInicio + ".";

        for (int i = 0; i < ipInicio.length(); i++) {
            if (!ipInicio.substring(i, i + 1).equals(".")) {
                if (Character.isDigit(ipInicio.charAt(i))) {
                    aux = aux + ipInicio.substring(i, i + 1);
                } else {
                    errado = true;
                }
            } else {
                if (ip1[0] == -1) {
                    ip1[0] = Integer.parseInt(aux);
                    if (ip1[0] > 255 || ip1[0] < 0) {
                        errado = true;
                    }
                } else if (ip1[1] == -1) {
                    ip1[1] = Integer.parseInt(aux);
                    if (ip1[1] > 255 || ip1[1] < 0) {
                        errado = true;
                    }
                } else if (ip1[2] == -1) {
                    ip1[2] = Integer.parseInt(aux);
                    if (ip1[2] > 255 || ip1[2] < 0) {
                        errado = true;
                    }
                } else if (ip1[3] == -1) {
                    ip1[3] = Integer.parseInt(aux);
                    if (ip1[3] > 255 || ip1[3] < 0) {
                        errado = true;
                    }
                } else {
                    errado = true;
                }
                aux = "";
            }
        }

        aux = "";
        int[] ip2 = new int[4];
        ip2[0] = -1;
        ip2[1] = -1;
        ip2[2] = -1;
        ip2[3] = -1;

        ipFim = ipFim + ".";

        for (int i = 0; i < ipFim.length(); i++) {
            if (!ipFim.substring(i, i + 1).equals(".")) {
                if (Character.isDigit(ipFim.charAt(i))) {
                    aux = aux + ipFim.substring(i, i + 1);
                } else {
                    errado = true;
                }
            } else {
                if (ip2[0] == -1) {
                    ip2[0] = Integer.parseInt(aux);
                    if (ip2[0] > 255 || ip2[0] < 0) {
                        errado = true;
                    }
                } else if (ip2[1] == -1) {
                    ip2[1] = Integer.parseInt(aux);
                    if (ip2[1] > 255 || ip2[1] < 0) {
                        errado = true;
                    }
                } else if (ip2[2] == -1) {
                    ip2[2] = Integer.parseInt(aux);
                    if (ip2[2] > 255 || ip2[2] < 0) {
                        errado = true;
                    }
                } else if (ip2[3] == -1) {
                    ip2[3] = Integer.parseInt(aux);
                    if (ip2[3] > 255 || ip2[3] < 0) {
                        errado = true;
                    }
                } else {
                    errado = true;
                }
                aux = "";
            }
        }

        if (errado) {
            FacesMessage msgErro = new FacesMessage("IP invalido");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            ExecutorService es = Executors.newFixedThreadPool(qtdThreads - 160);
            final List<Future<Rep>> futures = new ArrayList<Future<Rep>>();
            for (int c1 = ip1[0]; c1 <= ip2[0]; c1++) {
                int z1;
                if (c1 != ip2[0]) {
                    z1 = 255;
                } else {
                    z1 = ip2[1];
                }
                for (int c2 = ip1[1]; c2 <= z1; c2++) {
                    int z2;
                    if (c2 != ip2[1]) {
                        z2 = 255;
                    } else {
                        z2 = ip2[2];
                    }
                    for (int c3 = ip1[2]; c3 <= z2; c3++) {
                        int z3;
                        if (c3 != ip2[2]) {
                            z3 = 255;
                        } else {
                            z3 = ip2[3];
                        }
                        for (int c4 = ip1[3]; c4 <= z3; c4++) {
                            futures.add(ipsAtivos(es, c1 + "." + c2 + "." + c3 + "." + c4, 10000, c4));
                        }
                    }
                }
            }

            ips = new ArrayList<Rep>();
            //System.out.println();
            Rep r;

            for (final Future<Rep> f : futures) {
                try {
                    r = new Rep();
                    r.ip = f.get().ip;
                    r.resultado = f.get().resultado;
                    //System.out.print(f.get().ip + " " + f.get().resultado);

                    if (f.get().resultado) {
                        InetAddress iad = InetAddress.getByName(f.get().ip);
                        try {
                            Socket s = new Socket(iad, 7000);
                            if (s.isConnected()) {
                                r.comentario = "Relógio";
                                //System.out.print(" Relógio talvez");
                                s.close();
                            }
                        } catch (Exception ex) {
                            r.comentario = "Não é relógio";
                            //System.out.print(" Não é relógio");
                        }

                    } else {
                        //System.out.print(" ?");
                    }
                    //System.out.println();
                    ips.add(r);
                } catch (InterruptedException ex) {
                } catch (ExecutionException ex) {
                } catch (Exception ex) {
                }
            }

            es.shutdown();
            futures.clear();
        }
        ipInicio = ipInicio.substring(0, ipInicio.length() - 1);
        ipFim = ipFim.substring(0, ipFim.length() - 1);
    }

    //realiza a coleta manual direto dos relógios
    public void coletar() {
        FacesMessage msgErro = new FacesMessage("Coleta de " + DiasColetados + " dias do relógio " + clock.getRepAlias() + " efetuado!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    //Recebe arquivo AFD
    public void abreAFD(UploadEvent event) {
        try {
            UploadItem item = event.getUploadItem();
            File arquivo = item.getFile();

            FileReader fr = new FileReader(arquivo);
            BufferedReader bf = new BufferedReader(fr);
            String rep = "";

            banco = new MachineBanco();
            int repid = 0;

            String linha;
            int qtdLines = 0;
            int qtdLog1 = 0;
            int qtdLog3 = 0;
            int qtdLog5 = 0;
            int qtdLogOutros = 0;
            int lastNSR = 0;
            String tipoRegistro = "";
            banco.criaDepartments();
            while ((linha = bf.readLine()) != null) {
                tipoRegistro = Character.toString(linha.charAt(9));
                if (tipoRegistro.equals("1")) {
                    rep = linha.substring(187, 204);
                    repid = banco.consultaRelogioIdByNFR(rep);
                    qtdLog1++;
                } else
                if (tipoRegistro.equals("3")) {
                    lastNSR = saveLog3(repid, linha);
                    qtdLog3++;
                } else
                if (tipoRegistro.equals("5")) {
                    lastNSR = saveLog5(repid, linha);
                    qtdLog5++;
                } else {
                    qtdLogOutros++;
                }                         
                qtdLines++;
            }
            System.out.println("Resultado");
            System.out.println("tipo 1: " + qtdLog1);
            System.out.println("tipo 3: " + qtdLog3);
            System.out.println("tipo 5: " + qtdLog5);
            System.out.println("outros: " + qtdLogOutros);
            System.out.println("Total : " + (qtdLog1 + qtdLog3 + qtdLog5 + qtdLogOutros));
            System.out.println("linhas: " + qtdLines);
            if (lastNSR != 0 && repid != 0) {
                banco.updateLastNSR(repid, lastNSR);
            }
            //System.out.println("Documento possui: " + qtdLines + " linhas, " + qtdLog3 + " do tipo Log3");
            FacesMessage msgErro = new FacesMessage("Finalizado! Foram processadas: " + qtdLines + " linhas, sendo destas " + qtdLog3 + " marcações");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            bf.close();
        } catch (Exception e) {
            System.out.println("erro: " + e.getMessage());
        } finally {
            banco.Desonectar();
        }
    }

    private int saveLog3(int rep, String linha) {
        linha = linha.replace(" ", "");
        linha = linha.replace(":", "");
        linha = linha.replace("/", "");
        String NSR = linha.substring(0, 9);
        //String reg = linha.substring(9, 10);
        String dia = linha.substring(10, 12);
        String mes = linha.substring(12, 14);
        String ano = linha.substring(14, 18);
        String hora = linha.substring(18, 20);
        String minuto = linha.substring(20, 22);
        String pis = linha.substring(22);
        //System.out.println("NSR: " + NSR + " - Reg: " + reg + " - Data: " + dia + "/" + mes + "/" + ano + " - Hora: " + hora + ":" + minuto + " - PIS: " + pis);

        String dateStr = ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":00";
        //System.out.println("data: "+dateStr);
        Timestamp time = Timestamp.valueOf(dateStr);
        if (dataLimite == null || time.after(dataLimite)) {
            banco.cadastraCheckInOut(pis, time, rep);
        }
        return Integer.valueOf(NSR);
    }

    private int saveLog5(int rep, String linha) {
        String NSR = linha.substring(0, 9);
        //String reg = linha.substring(9, 10);
        /*String dia = linha.substring(10, 12);
        String mes = linha.substring(12, 14);
        String ano = linha.substring(14, 18);
        String hora = linha.substring(18, 20);
        String minuto = linha.substring(20, 22);*/
        String status = linha.substring(22, 23);
        String pis = linha.substring(23, 35);
        String nome = linha.substring(35);
        //System.out.println("NSR: " + NSR + " - Reg: " + reg + " - Data: " + dia + "/" + mes + "/" + ano + " - Hora: " + hora + ":" + minuto + " - PIS: " + pis);

        //String dateStr = ano + "-" + mes + "-" + dia + " " + hora + ":" + minuto + ":00";
        //System.out.println("data: "+dateStr);
        //Timestamp time = Timestamp.valueOf(dateStr);
        //if (dataLimite == null || time.after(dataLimite)) {
            banco.cadastraUserRepToBanco(status, pis, nome, rep);
        //}
        return Integer.valueOf(NSR);
    }
    
    private Future<Rep> ipsAtivos(final ExecutorService es, final String ip, final int timeout, final int indice) {
        return es.submit(new Callable<Rep>() {
            @Override
            public Rep call() {
                Rep r = new Rep();
                r.ip = ip;
                try {
                    InetAddress iad = InetAddress.getByName(ip);
                    if (iad.isReachable(timeout)) {
                        r.resultado = true;
                        r.i = indice;
                        return r;
                    }
                } catch (Exception ex) {
                    System.out.println(ip + " " + ex.getMessage());
                }
                r.i = indice;
                return r;
            }
        });
    }

    public void findClock() {
        MachineBanco mb = new MachineBanco();
        clock = mb.consultaRelogio(selectedClock);
        DiasColetados = 7;
        verifySingleClock();
    }

    //--------------------------------------------------------------------------
    //Metodos para o IDDATA
    //--------------------------------------------------------------------------
    public void prepareIdData() {
        //Recuperação do aminho para o software IDData
        File idRep = new File(fileWay);
        if (!idRep.exists()) {
            //Comandos que tentarão corrigir o path para o executavel
            String idDataWay = System.getProperty("java.home");
            String filePath;
            String dbpath;
            Boolean javaFound = false;
            while (!javaFound) {
                idDataWay = idDataWay.substring(0, idDataWay.lastIndexOf(File.separator));
                if (idDataWay.endsWith("Java")) {
                    javaFound = true;
                    idDataWay = idDataWay.substring(0, idDataWay.lastIndexOf(File.separator));
                }
            }
            idDataWay = idDataWay + File.separator + "ID DATA Tecnologia" + File.separator + "ID REP Config 32 Bits";
            filePath = idDataWay + File.separator + "ID REP Config.exe";

            if (!System.getProperty("os.arch").equals("x86") && !new File(filePath).exists()) {
                idDataWay = filePath + File.separator + "ID DATA" + File.separator + "ID REP Config";
                filePath = idDataWay + File.separator + "ID REP Config.exe";
            }
            dbpath = filePath.substring(0, idDataWay.lastIndexOf(File.separator));
            dbpath = dbpath + File.separator + "Data" + File.separator + "IDREP.s3db";

            idRep = new File(filePath);
            if (!idRep.exists()) {
                showFileBrowser = true;
            } else {
                fileWay = filePath;
                dbway = dbpath;
                showFileBrowser = false;
            }
        } else {
            String dbaux = idRep.getPath();
            dbway = dbaux.substring(0, dbaux.lastIndexOf(File.separator));
            dbway = dbway + File.separator + "Data" + File.separator + "IDREP.s3db";
            showFileBrowser = false;
        }

    }

    //Cadastra os funcionarios, marcações e empresas do relogio
    public void callSoft() throws IOException {
        try {
            File idRep = new File(fileWay);
            if (idRep.exists()) {
                Process proc = Runtime.getRuntime().exec(fileWay);
                proc.waitFor();
                ClockManagerBanco b = new ClockManagerBanco();
                b.cadastrarFuncionarios();
                b.cadastrarMarcacoes();
                b.cadastrarEmpregadores();
                System.out.println("Arquivo encontrado");
                showFileBrowser = false;
            } else {
                showFileBrowser = true;
                System.out.println("Arquivo não encontrado");
            }
        } catch (Exception e) {
        }
    }

    public void verify() {
        int valid = 0;
        File file = new File(newFileWay);
        if (file.exists()) {
            valid = 2;
        } else {
            valid = 1;
        }
        newValidWay = valid;
    }

    public void submitWay() {
        newFileWay = newFileWay.replace("/", File.separator);
        fileWay = newFileWay;
        dbway = newFileWay.substring(0, newFileWay.lastIndexOf(File.separator));
        dbway = dbway + File.separator + "Data" + File.separator + "IDREP.s3db";
    }

    //--------------------------------------------------------------------------
    //Metodos para o TRILOBIT
    //--------------------------------------------------------------------------
    public void setDigitSender() {
        ClockManagerBanco banco = new ClockManagerBanco();
        IpDigitSender = banco.getServidorDigitSender();
        if (IpDigitSender == null || IpDigitSender.equals("")) {
            IpDigitSender = "";
        } else {
            FullNameIpSender = "http://" + IpDigitSender + "/sgnclockmanager/SendUsers.aspx";
        }
    }

    //--------------------------------------------------------------------------
    //Metodos para o ZK
    //--------------------------------------------------------------------------
    public String testarZk() {

        /*if (webServup && clock.getRepId() != null) {
         String retorno = soap.lerEmpregadosZK(clock.getRepIp(), clock.getRepPort());
         System.out.println("retorno: " + retorno);
         } else {
         FacesMessage msgErro = new FacesMessage("WebService desligado ou desinstalado!");
         FacesContext.getCurrentInstance().addMessage(null, msgErro);
         }*/
        return "";
    }

    //--------------------------------------------------------------------------
    //GETTERS and SETTERS
    //--------------------------------------------------------------------------
    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }

    public List<Machine> getAllClocks() {
        return allClocks;
    }

    public void setAllClocks(List<Machine> allClocks) {
        this.allClocks = allClocks;
    }

    public Boolean getShowFileBrowser() {
        return showFileBrowser;
    }

    public void setShowFileBrowser(Boolean showFileBrowser) {
        this.showFileBrowser = showFileBrowser;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getFileWay() {
        return fileWay;
    }

    public void setFileWay(String fileWay) {
        this.fileWay = fileWay;
    }

    public String getDbway() {
        return dbway;
    }

    public void setDbway(String dbway) {
        this.dbway = dbway;
    }

    public String getNewFileWay() {
        return newFileWay;
    }

    public void setNewFileWay(String newFileWay) {
        this.newFileWay = newFileWay;
    }

    public int getNewValidWay() {
        return newValidWay;
    }

    public void setNewValidWay(int newValidWay) {
        this.newValidWay = newValidWay;
    }

    public String getIpDigitSender() {
        return IpDigitSender;
    }

    public void setIpDigitSender(String IpDigitSender) {
        this.IpDigitSender = IpDigitSender;
    }

    public String getFullNameIpSender() {
        return FullNameIpSender;
    }

    public void setFullNameIpSender(String FullNameIpSender) {
        this.FullNameIpSender = FullNameIpSender;
    }

    public int getSelectedClock() {
        return selectedClock;
    }

    public void setSelectedClock(int selectedClock) {
        this.selectedClock = selectedClock;
    }

    public Machine getClock() {
        return clock;
    }

    public void setClock(Machine clock) {
        this.clock = clock;
    }

    public int getDiasColetados() {
        return DiasColetados;
    }

    public void setDiasColetados(int DiasColetados) {
        this.DiasColetados = DiasColetados;
    }

    public Date getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(Date dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getIpInicio() {
        return ipInicio;
    }

    public void setIpInicio(String ipInicio) {
        this.ipInicio = ipInicio;
    }

    public String getIpFim() {
        return ipFim;
    }

    public void setIpFim(String ipFim) {
        this.ipFim = ipFim;
    }

    public List<Rep> getIps() {
        return ips;
    }

    public void setIps(List<Rep> ips) {
        this.ips = ips;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
