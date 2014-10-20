package AjustaHorarios;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Main {

    private List<Horario> horariosOriginais;
    private List<Integer> listaDasChaves;
    private List<Horario> horariosIdeais;
    private HashMap<Integer, Float> mapaDasDiferencas;
    private HashMap<Integer, Float> mapaDasDiferencasDasCargas;
    private HashMap<Integer, Integer> mapaDaQuantidadeJornada;
    private HashMap<Integer, Integer> mapaDaQuantidadeDesloc;
    private HashMap<Integer, Horario> mapaDeTransicao;

    public void buscaOriginais() throws SQLException {
        horariosOriginais = new ArrayList<Horario>();
        Bank banco = new Bank();
        horariosOriginais = banco.consultaHorariosDia();
        buscaQuantidade();
    }

    public void buscaQuantidade() {
        mapaDaQuantidadeDesloc =  new HashMap<Integer, Integer>();
        mapaDaQuantidadeJornada = new HashMap<Integer, Integer>();
        Bank banco = new Bank();
        mapaDaQuantidadeJornada = banco.consultaQuantidadeJornada(horariosOriginais);
        mapaDaQuantidadeDesloc = banco.consultaQuantidadeDesloc(horariosOriginais);
        try {
                if (banco.c != null) {
                    banco.c.close();
                }

            } catch (Exception e) {
                System.out.println("Erro na busca de quantidade");

            }
    }


    public void substituiHorarios() {
        Bank banco = new Bank();
        banco.substituiHorarios(mapaDeTransicao, listaDasChaves);
    }

    public void imprimeMapa() {

        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horario = horariosOriginais.get(i);
            Horario horarioIdeal = mapaDeTransicao.get(horario.getHorarioId());
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            System.out.println(horario.getNome() + "#" + sf.format(horario.getEntrada()) + " às " + sf.format(horario.getSaida()) + "#" + horarioIdeal.getNome() + "#" + sf.format(horarioIdeal.getEntrada()) + " às " + sf.format(horarioIdeal.getSaida()));
        }
    }

    public void imprimeMapaDasDiferencas() {
        System.out.println("Horario antigo#Periodo antigo#Horario novo#Periodo novo#Diferenca em horas");
        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horario = horariosOriginais.get(i);
            Horario horarioIdeal = mapaDeTransicao.get(horario.getHorarioId());
            Float diferenca = mapaDasDiferencas.get(horario.getHorarioId());
            Float diferencaCarga = mapaDasDiferencasDasCargas.get(horario.getHorarioId());
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            System.out.println(horario.getNome() + "#" + sf.format(horario.getEntrada()) + " às " + sf.format(horario.getSaida()) + "#" + horarioIdeal.getNome() + "#" + sf.format(horarioIdeal.getEntrada()) + " às " + sf.format(horarioIdeal.getSaida()) + "#" + (diferenca / 3600000)+ "#" + (diferencaCarga / 3600000));

        }
    }
    public void imprimeHorariosPorPessoas(){
        System.out.println("Horario antigo#Periodo antigo#Utilização nas Jornadas#Utilização nos Deslocamentos#Horario novo#Periodo novo#Diferenca em horas");
        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horario = horariosOriginais.get(i);
            Horario horarioIdeal = mapaDeTransicao.get(horario.getHorarioId());
            Integer qntJor = mapaDaQuantidadeJornada.get(horario.getHorarioId());
            Integer qntDes = mapaDaQuantidadeDesloc.get(horario.getHorarioId());
            Float diferenca = mapaDasDiferencas.get(horario.getHorarioId());
            Float diferencaCarga = mapaDasDiferencasDasCargas.get(horario.getHorarioId());
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            System.out.println(horario.getNome() + "#" + sf.format(horario.getEntrada()) + " às " + sf.format(horario.getSaida()) + "#" + qntJor + "#"+ qntDes + "#" + horarioIdeal.getNome() + "#" + sf.format(horarioIdeal.getEntrada()) + " às " + sf.format(horarioIdeal.getSaida()) + "#" + (diferenca / 3600000)+ "#" + (diferencaCarga / 3600000));

        }
    }

    public void geraIdeais() throws ParseException {
        /*FO - Folga
        
        M - Manha (07:00 - 13:00)
        T - Tarde (13:00 - 19:00)
        N - Noite (19:00 - 07:00)
        E - Especial ( Horário diferente do padrão. Exemplo: 06:30 - 12:30)
        P - Plantao (07:00 - 07:00)
        D - Dobra (07:00 - 19:00)
         *
         */
        horariosIdeais = new ArrayList<Horario>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String nome = "Manhã";
        Integer id = 1;
        Date entrada = df.parse("30/12/1899 07:00:00");
        Date saida = df.parse("30/12/1899 13:00:00");
        String legenda = "M";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Tarde";
        id = 2;
        entrada = df.parse("30/12/1899 13:00:00");
        saida = df.parse("30/12/1899 19:00:00");
        legenda = "T";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Noite";
        id = 3;
        entrada = df.parse("30/12/1899 19:00:00");
        saida = df.parse("30/12/1899 07:00:00");
        legenda = "N";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Plantão";
        id = 4;
        entrada = df.parse("30/12/1899 07:00:00");
        saida = df.parse("30/12/1899 06:59:00");
        legenda = "P";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Dobra";
        id = 5;
        entrada = df.parse("30/12/1899 07:00:00");
        saida = df.parse("30/12/1899 19:00:00");
        legenda = "D";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Horario Especial 01";
        id = 6;
        entrada = df.parse("30/12/1899 06:00:00");
        saida = df.parse("30/12/1899 12:00:00");
        legenda = "HE1";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Horario Especial 02";
        id = 7;
        entrada = df.parse("30/12/1899 06:30:00");
        saida = df.parse("30/12/1899 12:30:00");
        legenda = "HE2";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Horario Especial 03";
        id = 8;
        entrada = df.parse("30/12/1899 07:30:00");
        saida = df.parse("30/12/1899 12:30:00");
        legenda = "HE3";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Manhã 1";
        id = 9;
        entrada = df.parse("30/12/1899 08:00:00");
        saida = df.parse("30/12/1899 12:00:00");
        legenda = "M1";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Tarde 1";
        id = 10;
        entrada = df.parse("30/12/1899 13:00:00");
        saida = df.parse("30/12/1899 17:00:00");
        legenda = "T1";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
        nome = "Tarde 2";
        id = 11;
        entrada = df.parse("30/12/1899 14:00:00");
        saida = df.parse("30/12/1899 18:00:00");
        legenda = "T2";
        horariosIdeais.add(new Horario(nome, id, entrada, saida, legenda));
    }

    public int adicionaHorariosIdeais() {
        Bank b = new Bank();
        Integer maior = b.consultaMaiorId();
        horariosIdeais = b.adicionaHorarios(horariosIdeais);
        return maior;
    }

    public float comparaHorarios(Horario horarioOriginal, Horario horarioIdeal) {

        long l = 0;
        long d = 0;
        //     SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        // 12horas: 43200000
        // 1 hora: 3600000
        //      System.out.println("Orig:\n"+horarioOriginal.getNome()+"\n"+sf.format(horarioOriginal.getEntrada())+"\n"+sf.format(horarioOriginal.getSaida())+"\n");
        //      System.out.println("Ideal:\n"+horarioIdeal.getNome()+"\n"+sf.format(horarioIdeal.getEntrada())+"\n"+sf.format(horarioIdeal.getSaida())+"\n");

        d = Math.abs(horarioOriginal.getEntrada().getTime() - horarioIdeal.getEntrada().getTime());
        //     System.out.println("Dif1: "+d);
        l += (d < 43200000) ? d : 43200000 - (d - 43200000);
        //     System.out.println("l1: "+l);
        d = Math.abs(horarioOriginal.getSaida().getTime() - horarioIdeal.getSaida().getTime());
        //    System.out.println("Dif2: "+d);
        l += (d < 43200000) ? d : 43200000 - (d - 43200000);
        //    System.out.println("l2: "+l);
        return l;
    }
    public float comparaCargaHorarios(Horario horarioOriginal, Horario horarioIdeal) throws ParseException {

        long l = 0;
        long d = 0;
        long cOriginal = 0;
        long cIdeal = 0;
        //     SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        // 12horas: 43200000
        // 1 hora: 3600000
        //      System.out.println("Orig:\n"+horarioOriginal.getNome()+"\n"+sf.format(horarioOriginal.getEntrada())+"\n"+sf.format(horarioOriginal.getSaida())+"\n");
        //      System.out.println("Ideal:\n"+horarioIdeal.getNome()+"\n"+sf.format(horarioIdeal.getEntrada())+"\n"+sf.format(horarioIdeal.getSaida())+"\n");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date meiaNoite30 = df.parse("30/12/1899 00:00:00");
        Date meiaNoite31 = df.parse("31/12/1899 00:00:00");
        //Date saida = df.parse("30/12/1899 13:00:00");

        cOriginal =  horarioOriginal.getSaida().getTime() - horarioOriginal.getEntrada().getTime();
        if (cOriginal<0){
            cOriginal = (meiaNoite31.getTime()- horarioOriginal.getEntrada().getTime())+ (horarioOriginal.getSaida().getTime()- meiaNoite30.getTime());
        }
        cIdeal =  horarioIdeal.getSaida().getTime() - horarioIdeal.getEntrada().getTime();
        if (cIdeal<0){
            cIdeal = (meiaNoite31.getTime()- horarioIdeal.getEntrada().getTime())+ (horarioIdeal.getSaida().getTime()- meiaNoite30.getTime());
        }
        //     System.out.println("l1: "+l);
        d = Math.abs(cIdeal - cOriginal);
        //    System.out.println("Dif2: "+d);
        return d;
    }

    public void criaMapa() throws ParseException {

        mapaDeTransicao = new HashMap<Integer, Horario>();
        mapaDasDiferencas = new HashMap<Integer, Float>();
        mapaDasDiferencasDasCargas = new HashMap<Integer, Float>();
        listaDasChaves = new ArrayList<Integer>();

        for (int i = 0; i < horariosOriginais.size(); i++) {
            Horario horarioOrig = horariosOriginais.get(i);
            float min = 46800000;
            float cargaMin =12;
            int indexMin = 0;


            for (int j = 0; j < horariosIdeais.size(); j++) {
                Horario horarioIdeal = horariosIdeais.get(j);
                float f = comparaHorarios(horarioOrig, horarioIdeal);
                float d = comparaCargaHorarios(horarioOrig, horarioIdeal);
                if (f < min) {
                    min = f;
                    cargaMin = d;
                    indexMin = j;
                } else if (f == min){
                    if(d<cargaMin){
                        min = f;
                        cargaMin = d;
                        indexMin = j;
                    }
                }
            }
            listaDasChaves.add(horarioOrig.getHorarioId());
            mapaDeTransicao.put(horarioOrig.getHorarioId(), horariosIdeais.get(indexMin));
            mapaDasDiferencas.put(horarioOrig.getHorarioId(), min);
            mapaDasDiferencasDasCargas.put(horarioOrig.getHorarioId(), cargaMin);
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, ParseException {

        Main m = new Main();
        m.buscaOriginais();
        m.geraIdeais();
        m.adicionaHorariosIdeais();
        m.criaMapa();
        m.imprimeMapa();
        m.substituiHorarios();
        //Pronto Aki
        System.out.println("______________________________________________________________________");
        System.out.println("______________________________________________________________________");
        System.out.println("______________________________________________________________________");
        m.imprimeHorariosPorPessoas();
        //     m.buscaOriginais();
        //     m.criaMapa();
        //     m.imprimeMapa();
        //int x = 2;

    }
}
