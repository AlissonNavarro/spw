/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Usuario;

import Metodos.Metodos;
import Perfil.Perfil;
import com.sun.faces.vendor.Tomcat6InjectionProvider;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 *
 * @author amsgama
 */
public class UsuarioBean implements Serializable {

    private Usuario usuario;
    private String login;
    private String ADlogin;
    private String senha;
    private Boolean ehAdministrador;
    private Boolean ehSuperAdministrador;
    private Boolean isAtivo;
    private Boolean useAD = false;
    private Boolean hasAdinked;
    private List<SelectItem> vinculoList;
    private Integer cod_vinculo_;
    private Perfil perfil;
    private String cpfAD;
    private InetAddress addr;
    private String IpAD;
    private Boolean existsADServer;
    private String versao;
    private String serial;
    private String validade;

    private Boolean zerg;//Bug Time

    public UsuarioBean() {
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        usuario = new Usuario();
        hasAdinked = true;
        ehAdministrador = false;
        ehSuperAdministrador = false;
        existsADServer = false;
        vinculoList = new ArrayList<SelectItem>();
        verificarADServer();
        versao = Metodos.getVersao();
        zerg = false;
    }

    private void verificarADServer() {
        if (Metodos.getServidorAtivo()) {
            Banco banco = new Banco();
            try {
                if (banco.hasConnection) {
                    String ip = banco.getServidorAD();
                    if (ip.equals("") || ip.equals("000.000.000.000")) {
                        existsADServer = false;
                    } else {
                        addr = InetAddress.getByName(ip);
                        existsADServer = true;
                        useAD = true;
                    }
                }
            } catch (UnknownHostException uhe) {
                existsADServer = false;
            } catch (Exception e) {
                System.out.println("verificarADServer: " + e);
            }
        } else {
            existsADServer = false;
        }
    }

    public void openPortal() {
        try {
            URI uri = new URI("http://www.sgnsolucoes.com.br");
            Desktop.getDesktop().browse(uri);
        } catch (IOException ex) {
        } catch (URISyntaxException ex) {
        }
    }

    public String entrar() {

        //Inicia procedimento de entrada
        checkIsAtivo();
        String navegar = "";
        boolean hasSenha = true;
        ehAdministrador = false;
        ehSuperAdministrador = false;

        Usuario userTeste = new Usuario();
        Banco banco = new Banco();

        if (useAD) {
            userTeste = banco.getUsuarioByMatricula(cod_vinculo_.toString());
        } else {
            userTeste = banco.getUsuarioByCPF(login);
        }

        if (!(userTeste.getLogin() == null || login.equals("000.000.000-00"))) {
            int tmp = validarSerial();
            banco = new Banco();
            Perfil perfil = banco.consultaPermissoesPerfil(userTeste.getCodPerfil());

            if (tmp == 0) {
                banco = new Banco();
                banco.atualizarUltimoLogin();
            }
            if (tmp == 51 || tmp == -51 || tmp == 50) {
                if (zerg) {
                    ZergRush();
                    return "";
                } else {
                    if (!perfil.getAutenticaSerial()) {
                        FacesMessage msgErro = new FacesMessage("Tempo de licença expirou, contate o adminitrador.");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        return "";
                    } else {
                        return "validar";
                    }
                }
            }
            if (tmp == 52) {
                if (zerg) {
                    ZergRush();
                    return "";
                } else {
                    if (!perfil.getAutenticaSerial()) {
                        FacesMessage msgErro = new FacesMessage("Horário do servidor inválidado, contate o adminitrador.");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        return "";
                    } else {
                        FacesMessage msgErro = new FacesMessage("Horário do servidor inválidado, por favor corrija a hora ou contate o fornecedor.");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        return "validar";
                    }
                }
            }
            if (tmp <= 30 && tmp > 0) {
                if (zerg) {
                } else {
                    FacesMessage msgErro = new FacesMessage("Faltam " + tmp + " dias para o termino da licença.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            }
            if (tmp == 1) {
                if (zerg) {
                } else {
                    FacesMessage msgErro = new FacesMessage("Falta " + tmp + " dia para o termino da licença.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }
            }
        }

        if (!useAD) {
            banco = new Banco();
            if (isAtivo) {
                DesEncrypter encrypterSenhaAdmin = new DesEncrypter("aabbccaa");
                String senhaAdminEncryter = encrypterSenhaAdmin.encrypt(senha);
                String senha_ = Metodos.getSenhaAdministrador();
                if (login.equals("000.000.000-00") && senhaAdminEncryter.equals(senha_)) {
                    ehAdministrador = true;
                    ehSuperAdministrador = true;
                    return "administrador";
                }
            } else {
                ehAdministrador = true;
                ehSuperAdministrador = true;
                return "navegarConexaoBanco";
            }

            if (!vinculoList.isEmpty()) {
                userTeste = banco.getUsuarioByMatricula(cod_vinculo_.toString());
            } else {
                userTeste = banco.getUsuarioByCPF(login);
            }

        } else {
            banco = new Banco();
            Boolean logged = false;
            verificarADServer();
            logged = activeDirectoryLogin(ADlogin, senha, addr.getHostName());
            if (!logged) {
                return navegar;
            }
            userTeste = banco.getUsuarioByUserAD(ADlogin);
            if (userTeste.getNome().equals("")) {
                cpfAD = "";
                hasAdinked = false;
                FacesMessage msgErro = new FacesMessage("Registro de Active Direcotory não está ligado a nenhum usuário");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                return navegar;
            }
        }

        if (userTeste.getLogin()
                != null) {
            if (userTeste.getSenha() == null) {
                hasSenha = false;
            }
            Boolean correctSenha = false;
            if (!useAD) {
                DesEncrypter encrypter = new DesEncrypter("aabbccaa");
                String senhaEncryter = encrypter.encrypt(senha);
                if (senhaEncryter.equals(userTeste.getSenha())) {
                    correctSenha = true;
                }
            } else {
                correctSenha = true;
            }
            if (correctSenha) {
                usuario.setLogin(userTeste.getLogin());
                usuario.setNome(userTeste.getNome());
                usuario.setPermissao(userTeste.getPermissao());
                usuario.setDepartamento(userTeste.getDepartamento());
                usuario.setIsAcessoTotal(userTeste.getIsAcessoTotal());
                usuario.setPrimeiroNome(userTeste.getPrimeiroNome());
                usuario.setCodPerfil(userTeste.getCodPerfil());
                usuario.setDataContratacao(userTeste.getDataContratacao());
                if (!usuario.getPermissao().equals("0")) {
                    navegar = navegarAdmin();
                    //Metodos.setLogInfo("Entrar no sistema - Permissão: Administrador");
                    ehAdministrador = true;

                } else if (usuario.getPermissao().equals("0")) {
                    // Metodos.setLogInfo("Entrar no sistema - Permissão: Comum");
                    navegar = "consultaFrequencia";
                }
            } else if (!hasSenha) {
                usuario.setLogin(userTeste.getLogin());
                usuario.setSsn(userTeste.getSsn());
                navegar = "novoUsuario";
            } else {
                FacesMessage msgErro = new FacesMessage("Senha inválida!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
        } else {
            FacesMessage msgErro = new FacesMessage("Usuário não cadastrado");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        return navegar;
    }

    public String linkToAD() {
        Boolean flag = false;
        Banco banco = new Banco();
        Usuario usuarioTemp = banco.getUsuarioByCPF(cpfAD);
        if (usuarioTemp.getLogin() == null) {
            FacesMessage msgErro = new FacesMessage("CPF não está cadastrado no sistema de ponto, procure o gestor do sistema");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (usuarioTemp.getUserAD() != null) {
                FacesMessage msgErro = new FacesMessage("Este CPF está ligado a " + usuarioTemp.getUserAD() + ", Em caso de duvidas procure o gestor de sua área!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                if (!cpfAD.equals("")) {
                    banco = new Banco();
                    flag = banco.insertADUser(cpfAD, ADlogin);
                }

                if (flag) {
                    hasAdinked = true;
                    FacesMessage msgErro = new FacesMessage("Usuário ligado com sucesso!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    FacesMessage msgErro = new FacesMessage("Erro ao ligar o usuário AD!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }

            }
        }
        return "";
    }

    private Boolean activeDirectoryLogin(String ADlogin, String senha, String servidor) {
        Boolean logged = false;
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "LDAP://" + servidor + ":389");
            env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
            env.put(Context.SECURITY_PRINCIPAL, ADlogin);
            env.put(Context.SECURITY_CREDENTIALS, senha);
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            logged = true;
        } catch (Exception e) {
            FacesMessage msgErro = new FacesMessage("Login ou senha do Active Directory invalidos!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        return logged;
    }

    public String navegarAdmin() {
        Banco b = new Banco();
        perfil = b.consultaPermissoesPerfil(usuario.getCodPerfil());
        if (perfil.getConsInd()) {
            return "consultaFrequenciaAdmin";
        } else if (perfil.getHorCronoJorn()) {
            return "navegarEscala";
        } else if (perfil.getRelatorios()) {
            return "navegarRelatorioMensal";
        } else if (perfil.getListaDePresenca()) {
            return "navegarPresenca";
        } else if (perfil.getAbonos()) {
            return "navegarAbono";
        } else if (perfil.getCadastrosEConfiguracoes()) {
            return "navegarConfiguracoes";
        } else if (perfil.getManutencao()) {
            return "navegarCadastroDeFuncionarios";
        }
        FacesMessage msgErro = new FacesMessage("Colaborador não possui previlégios!");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
        return "consultaFrequencia";

    }

    public void verificarVinculosAD() {
        checkIsAtivo();
        if (isAtivo) {
            Banco banco = new Banco();
            Usuario userTeste = new Usuario();
            try {
                userTeste = banco.getUsuarioByUserAD(ADlogin);
                vinculoList = banco.getUsuarioVinculos(userTeste.getSsn());
            } catch (Exception e) {
            }
        }
    }

    public void verificarVinculos() {
        checkIsAtivo();
        if (isAtivo) {
            Banco banco = new Banco();
            try {
                vinculoList = banco.getUsuarioVinculos(login);
            } catch (Exception e) {
            }
        }
    }

    public String alterarSenha() {
        Banco banco = new Banco();
        Usuario userTeste = banco.getUsuarioByCPF(login);
        if (userTeste.getLogin() != null) {
            return "#";
        } else {
            FacesMessage msgErro = new FacesMessage("Usuário não cadastrado");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return null;
        }
    }

    private void checkIsAtivo() {
        if (Metodos.getServidorAtivo()) {
            Banco banco = new Banco();
            isAtivo = banco.getIsAtivo();
        } else {
            isAtivo = false;
        }
    }

    public String verificarSerial() {
        if (serial == null) {
            FacesMessage msgErro = new FacesMessage("Favor preencher o campo.");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
            return "";
        } else {
            String aux;
            try {
                aux = Base64Crypt.decrypt(serial);
                String tipo = aux.substring(0, 1);
                String iniData = aux.substring(1, 9);
                String endData = aux.substring(9, 17);
                String cnpj = aux.substring(17);

                if (!tipo.equals("1")) {
                    FacesMessage msgErro = new FacesMessage("Serial incorreto. 1");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                    return "";
                }

                Banco banco = new Banco();
                if (!cnpj.equals("00000000000000")) { //Força um bug
                    if (!banco.consultaCnpjEmpregador(cnpj)) {
                        FacesMessage msgErro = new FacesMessage("Serial incorreto. 2");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        return "";
                    }
                }

                if (endData.equals("00000000")) {
                    setValidade("ilimitado");
                    banco = new Banco();
                    banco.atualizarSerial(serial);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                    Date dataFinal = sdf.parse(endData);
                    Date dataAtual = new Date();

                    if (dataAtual.before(dataFinal) || (cnpj.equals("00000000000000"))) {
                        Date dataInicial = sdf.parse(iniData);

                        if (dataAtual.before(dataInicial)) {
                            iniData = iniData + "0000";
                            banco = new Banco();
                            banco.atualizarUltimoLogin(iniData);
                        } else {
                            String tmpDataInicial = sdf.format(dataAtual) + "0000";
                            banco = new Banco();
                            banco.atualizarUltimoLogin(tmpDataInicial);
                        }

                        banco = new Banco();
                        banco.atualizarSerial(serial);
                        setValidade(endData.substring(0, 2) + "/" + endData.substring(2, 4) + "/" + endData.substring(4, endData.length()));
                    } else {
                        FacesMessage msgErro = new FacesMessage("Serial expirado. 3");
                        FacesContext.getCurrentInstance().addMessage(null, msgErro);
                        return "";
                    }
                }

            } catch (Exception ex) {
                FacesMessage msgErro = new FacesMessage("Serial incorreto. 4");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
                return "";
            }
        }
        FacesMessage msgErro = new FacesMessage("Autenticado com sucesso.");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
        return "";
    }

    private int validarSerial() {
        //busca serial no config
        Banco b = new Banco();
        String tmp = b.consultarSerial();
        //tirar a criptografia do serial
        String aux = null;
        if (tmp != null) {
            aux = Base64Crypt.decrypt(tmp);
        }
        
        String cnpj = "";
        String endDate = "";
        zerg = false;
        //if (!tmp.equals("")) {
        if (aux != null) {
            //dividir string do serial
            cnpj = aux.substring(17);
            endDate = aux.substring(9, 17);
        }

        if (cnpj.equals("00000000000000")) {
            zerg = true;
        }
        if (tmp == null) {
            return 50; //"validar"
        } else {
            try {
                setValidade(endDate.substring(0, 2) + "/" + endDate.substring(2, 4) + "/" + endDate.substring(4, endDate.length()));

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                Date dataAtual = new Date();
                Date dataChave = sdf.parse(endDate);

                //validar a data da string do serial com as conexoes do login
                b = new Banco();
                Date ultimoLogin = b.getUltimoLogin();
                if (!zerg) {
                    if (ultimoLogin == null) {
                        return 51; //Coluna de último login foi apagada
                    } else {
                        if (dataAtual.before(ultimoLogin)) {
                            return 52; //"corregirHorarioServidor"
                        }
                    }
                }
                //validar a data da string com a data do servidor
                if (dataAtual.after(dataChave)) {
                    return 50; //"validar"
                }
                long diff = dataChave.getTime() - dataAtual.getTime();
                diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (diff < 30) {
                    return (int) diff;
                }
            } catch (Exception ex) {
                return -51; //erro
            }
            return 0;
        }
    }

    //BUG TIME
    private void ZergRush() {
        FacesMessage msgErro = new FacesMessage("java.net.SocketException: Software caused connection abort: socket write error.");
        FacesContext.getCurrentInstance().addMessage(null, msgErro);
    }

    public String sair() throws IOException {
        //Contexto da Aplicação
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
        //FacesContext.getCurrentInstance().getExternalContext().redirect("/www/consultaFrequenciaAdmin.jsp?param=clear");
        return "login";
    }

    public Boolean getExistsADServer() {
        return existsADServer;
    }

    public void setExistsADServer(Boolean existsADServer) {
        this.existsADServer = existsADServer;
    }

    public String getCpfAD() {
        return cpfAD;
    }

    public void setCpfAD(String cpfAD) {
        this.cpfAD = cpfAD;
    }

    public List<SelectItem> getVinculoList() {
        return vinculoList;
    }

    public void setVinculoList(List<SelectItem> vinculoList) {
        this.vinculoList = vinculoList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getHasAdinked() {
        return hasAdinked;
    }

    public void setHasAdinked(Boolean hasAdinked) {
        this.hasAdinked = hasAdinked;
    }

    public String getADlogin() {
        return ADlogin;
    }

    public void setADlogin(String ADlogin) {
        this.ADlogin = ADlogin;
    }

    public Boolean getUseAD() {
        return useAD;
    }

    public void setUseAD(Boolean useAD) {
        this.useAD = useAD;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getEhAdministrador() {
        return ehAdministrador;
    }

    public void setEhAdministrador(Boolean ehAdministrador) {
        this.ehAdministrador = ehAdministrador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public Boolean getEhSuperAdministrador() {
        return ehSuperAdministrador;
    }

    public void setEhSuperAdministrador(Boolean ehSuperAdministrador) {
        this.ehSuperAdministrador = ehSuperAdministrador;
    }

    public Integer getCod_vinculo_() {
        return cod_vinculo_;
    }

    public void setCod_vinculo_(Integer cod_vinculo_) {
        this.cod_vinculo_ = cod_vinculo_;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

}
