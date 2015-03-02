package AFD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AFDBean {

    public static Boolean isMain = false;
    private ArrayList<Registro> registroList;

    public ArrayList<Registro> getRegistroList() {
        return registroList;
    }

    public void setRegistroList(ArrayList<Registro> registroList) {
        this.registroList = registroList;
    }

    public void carregaRegistrosList(String arquivo) {
        registroList = new ArrayList<Registro>();
        try {
            ArrayList<String> registrosStrList = geraLinhasList(arquivo);
            for (int i = 0; i < registrosStrList.size(); i++) {
                String registrosStr = registrosStrList.get(i);
                Registro r = new Registro();
                String campo = registrosStr.substring(0, 9);
                if (campo.equals("000000000")) {//Cabeçalho
                    r = new Cabecalho();
                    Cabecalho registro = new Cabecalho();
                    campo = registrosStr.substring(9, 10);
                    registro.setTipodoregistro(Integer.parseInt(campo));
                    campo = registrosStr.substring(10, 11);
                    registro.setIsCnpj(campo.equals("1"));
                    campo = registrosStr.substring(11, 25);
                    registro.setIdentificador(Long.parseLong(campo));
                    campo = registrosStr.substring(25, 37);
                    registro.setCEI(Integer.parseInt(campo));
                    campo = registrosStr.substring(37, 187);
                    registro.setNome(campo);
                    campo = registrosStr.substring(187, 204);
                    registro.setNumeroREP(Integer.parseInt(campo));
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                    campo = registrosStr.substring(204, 212);
                    registro.setDataInicial(sdf.parse(campo));
                    campo = registrosStr.substring(212, 220);
                    registro.setDataFinal(sdf.parse(campo));
                    campo = registrosStr.substring(220, 232);
                    sdf = new SimpleDateFormat("ddMMyyyyHHmm");
                    registro.setDataEHoraGeracaoArquivo(sdf.parse(campo));
                    r = registro;

                    //            System.out.println("Adiciona Registro - Cabeçalho");

                } else if (campo.equals("999999999")) { //Trailer
                    Trailer registro = new Trailer();
                    campo = registrosStr.substring(9, 18);
                    registro.setQntRegistrosTipo2(Integer.parseInt(campo));
                    campo = registrosStr.substring(18, 27);
                    registro.setQntRegistrosTipo3(Integer.parseInt(campo));
                    campo = registrosStr.substring(27, 36);
                    registro.setQntRegistrosTipo4(Integer.parseInt(campo));
                    campo = registrosStr.substring(36, 45);
                    registro.setQntRegistrosTipo5(Integer.parseInt(campo));
                    campo = registrosStr.substring(45, 46);
                    registro.setTipodoregistro(Integer.parseInt(campo));
                    r = registro;
                    //        System.out.println("Adiciona Registro - Trailer");
                } else { // tipo 2,3,4 ou 5
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
                    Integer nsr = Integer.parseInt(campo);
                    campo = registrosStr.substring(9, 10);
                    Integer tipoRegistro = Integer.parseInt(campo);
                    switch (tipoRegistro) {
                        case 2:
                            RegistroIdentificacaoEmpresa rie = new RegistroIdentificacaoEmpresa();
                            rie.setNsr(nsr);
                            rie.setTipodoregistro(tipoRegistro);
                            campo = registrosStr.substring(10, 22);
                            rie.setDataEHoraGravacao(sdf.parse(campo));
                            campo = registrosStr.substring(22, 23);
                            rie.setIsCnpj(campo.equals("1"));
                            campo = registrosStr.substring(23, 37);
                            rie.setIdentificador(Long.parseLong(campo));
                            campo = registrosStr.substring(37, 49);
                            rie.setCEI(Integer.parseInt(campo));
                            campo = registrosStr.substring(50, 199);
                            rie.setNome(campo);
                            campo = registrosStr.substring(199, 299);
                            rie.setLocal(campo);
                            r = rie;
                            //        System.out.println("Adiciona Registro - NSR: " + nsr + " Tipo: 2 - Identificacao Empresa");
                            break;
                        case 3:
                            RegistroMarcacaoDePonto rmp = new RegistroMarcacaoDePonto();
                            rmp.setNsr(nsr);
                            rmp.setTipodoregistro(tipoRegistro);
                            campo = registrosStr.substring(10, 22);
                            rmp.setDataEHoraMarcacao(sdf.parse(campo));
                            campo = registrosStr.substring(22, 34);
                            rmp.setPis(campo);
                            registroList.add(rmp);
                            r = rmp;
                            //     System.out.println("Adiciona Registro - NSR: " + nsr + " Tipo: 3 - Marcacao De Ponto");
                            break;
                        case 4:
                            RegistroAjusteRelogio rar = new RegistroAjusteRelogio();
                            rar.setNsr(nsr);
                            rar.setTipodoregistro(tipoRegistro);
                            campo = registrosStr.substring(10, 22);
                            rar.setDataEHoraAntesAjuste(sdf.parse(campo));
                            campo = registrosStr.substring(22, 34);
                            rar.setDataEHoraAjustada(sdf.parse(campo));
                            registroList.add(rar);
                            r = rar;
                            //       System.out.println("Adiciona Registro - NSR: " + nsr + " Tipo: 4 - Ajuste Relogio");
                            break;
                        case 5:
                            RegistroEmpregado re = new RegistroEmpregado();
                            re.setNsr(nsr);
                            re.setTipodoregistro(tipoRegistro);
                            campo = registrosStr.substring(10, 22);
                            re.setDataEHoraAlteracao(sdf.parse(campo));
                            campo = registrosStr.substring(22, 23);
                            re.setTipoDaOperacao(campo.charAt(0));
                            campo = registrosStr.substring(23, 35);
                            re.setPis(campo);
                            campo = registrosStr.substring(35, 87);
                            re.setNomeEmpregado(campo);
                            r = re;
                            //       System.out.println("Adiciona Registro - NSR: " + nsr + " Tipo: 5 - Registro Empregado");
                            break;
                    }
                }
                registroList.add(r);
            }
        } catch (ParseException ex) {
            System.out.println("carregaRegistrosList: ParseException "+ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.out.println("carregaRegistrosList: FileNotFoundException "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("carregaRegistrosList: IOException "+ex.getMessage());
        }

    }

    public void gravaRegistros(Date datainicial, Date datafinal) {
        Banco banco = new Banco(isMain);
        for (int i = 0; i < registroList.size(); i++) {
            Registro registro = registroList.get(i);
            Integer tipoDoRegistro = registro.getTipodoregistro();
            switch (tipoDoRegistro) {
                case 1:
                    //Nada
                    break;
                case 2:
                    //Nada
                    break;
                case 3:
                    RegistroMarcacaoDePonto rmp = (RegistroMarcacaoDePonto) registro;
                    imprimeRegistro(registro);
                    if (rmp.getDataEHoraMarcacao().after(datainicial) && rmp.getDataEHoraMarcacao().before(datafinal)) {
                        banco.atualizaMarcacoes((RegistroMarcacaoDePonto) registro);
                    }
                    break;
                case 4:

                    break;
                case 5:
                    RegistroEmpregado re = (RegistroEmpregado) registro;
                    imprimeRegistro(registro);
                    if (re.getDataEHoraAlteracao().after(datainicial) && re.getDataEHoraAlteracao().before(datafinal)) {
                        banco.atualizaUsuarios((RegistroEmpregado) registro);
                    }
                    break;
                case 9:
                    break;
            }
        }
    }

    public void imprimeRegistrosList() {
        for (int i = 0; i < registroList.size(); i++) {
            Registro registro = registroList.get(i);
            imprimeRegistro(registro);
        }
    }

    public void imprimeRegistro(Registro registro) {
        Integer tipoDoRegistro = registro.getTipodoregistro();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        switch (tipoDoRegistro) {
            case 1:
                Cabecalho cabecalho = (Cabecalho) registro;
                System.out.println("");
                System.out.println("Registro tipo 1 - Cabeçalho");
                System.out.println("Nome: " + (cabecalho.getNome()));
                System.out.println((cabecalho.getIsCnpj() ? "CNPJ" : "CPF") + ": " + (cabecalho.getIdentificador()));
                System.out.println("CEI: " + (cabecalho.getCEI()));
                System.out.println("Numero Rep: " + cabecalho.getNumeroREP());
                sdf = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println("Data Inicial:" + sdf.format(cabecalho.getDataInicial()));
                System.out.println("Data Final:" + sdf.format(cabecalho.getDataFinal()));
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.println("Arquivo Gerado em: " + sdf.format(cabecalho.getDataFinal()));
                break;
            case 2:
                RegistroIdentificacaoEmpresa rie = (RegistroIdentificacaoEmpresa) registro;
                System.out.println("");
                System.out.println("Registro tipo 2 - Identificação da Empresa");
                System.out.println("NSR: " + rie.getNsr());
                System.out.println("Nome: " + rie.getNome());
                System.out.println((rie.getIsCnpj() ? "CNPJ" : "CPF") + ": " + (rie.getIdentificador()));
                System.out.println("Local: " + rie.getLocal());
                System.out.println("CEI: " + rie.getCEI());
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.println("Gravado em: " + sdf.format(rie.getDataEHoraGravacao()));
                break;
            case 3:
                RegistroMarcacaoDePonto rmp = (RegistroMarcacaoDePonto) registro;
                System.out.println("");
                System.out.println("Registro tipo 3 - Marcação de Ponto");
                System.out.println("NSR: " + rmp.getNsr());
                System.out.println("PIS: " + rmp.getPis());
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.println("Marcação Realizada em: " + sdf.format(rmp.getDataEHoraMarcacao()));
                break;
            case 4:
                RegistroAjusteRelogio rar = (RegistroAjusteRelogio) registro;
                System.out.println("");
                System.out.println("Registro tipo 4 - Ajuste Relógio");
                System.out.println("NSR: " + rar.getNsr());
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.println("Data e Hora antes do ajuste: " + sdf.format(rar.getDataEHoraAntesAjuste()));
                System.out.println("Data e Hora ajustada: " + sdf.format(rar.getDataEHoraAjustada()));
                break;
            case 5:
                RegistroEmpregado re = (RegistroEmpregado) registro;
                System.out.println("");
                System.out.println("Registro tipo 5 - Identificação de Funcionário");
                System.out.println("NSR: " + re.getNsr());
                char op = re.getTipoDaOperacao();
                String operacao = "Inválida";
                switch (op) {
                    case 'I':
                        operacao = "Inclusão";
                        break;
                    case 'A':
                        operacao = "Alteração";
                        break;
                    case 'E':
                        operacao = "Exclusão";
                        break;
                }
                System.out.println("Tipo da Operação: " + operacao);
                System.out.println("Nome: " + re.getNomeEmpregado());
                System.out.println("PIS: " + re.getPis());
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.println("Operação realizada em: " + sdf.format(re.getDataEHoraAlteracao()));


                break;
            case 9:

                Trailer trailer = (Trailer) registro;
                System.out.println("");
                System.out.println("Registro tipo 9 - Trailer");
                System.out.println("Quantidade de Registros do Tipo 2: " + trailer.getQntRegistrosTipo2());
                System.out.println("Quantidade de Registros do Tipo 3: " + trailer.getQntRegistrosTipo3());
                System.out.println("Quantidade de Registros do Tipo 4: " + trailer.getQntRegistrosTipo4());
                System.out.println("Quantidade de Registros do Tipo 5: " + trailer.getQntRegistrosTipo5());
                break;
        }

    }

    public static ArrayList<String> geraLinhasList(String arquivo)
            throws FileNotFoundException, IOException {

        File file = new File(arquivo);
        ArrayList<String> registros = new ArrayList<String>();

        if (!file.exists()) {
            System.out.print(file.getPath());
            return null;
        }

        BufferedReader br = new BufferedReader(new FileReader(arquivo));
        String linha;
        while ((linha = br.readLine()) != null) {
            registros.add(linha);
        }
        br.close();
        return registros;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date inicio = sdf.parse("01/01/2010");
        Date fim = sdf.parse("01/10/2011");
        AFDBean afdBean = new AFDBean();
        AFDBean.isMain = true;
        afdBean.carregaRegistrosList("AFD00000000000000000.txt");
        afdBean.imprimeRegistrosList();
        afdBean.gravaRegistros(inicio, fim);
        System.out.print("");
    }
}
