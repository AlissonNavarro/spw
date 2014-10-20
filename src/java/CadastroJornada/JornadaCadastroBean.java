/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CadastroJornada;

import AjustaHorarios.Horario;
import Metodos.Metodos;
import Usuario.UsuarioBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

/**
 *
 * @author Alexandre
 */
public class JornadaCadastroBean implements Serializable {

    private List<JornadaCadastro> jornadaCadastroList;
    private JornadaCadastro jornadaCadastro;
    private JornadaCadastro editJornadaCadastro;
    private JornadaCadastro novaJornadaCadastro;
    private Locale objLocale;
    private Selection selecionadoJornadaCadastro;
    private List<SelectItem> diaSemanaList;
    private String id_jornada;
    private List<Turno> horariosList;
    private HashMap<Integer, ArrayList<HorariosXdia>> horariosXjornadaCompleta;
    private List<DiaJornada> diasJornadaList;
    private String abaCorrente;
    private String editJornadaCadastroNome;
    //private ArrayList<Integer> turnosMarcados;
    //private ArrayList<Integer> diasMarcados;
//    private String infoMessage;

    public JornadaCadastroBean() {
        Banco banco = new Banco();
        jornadaCadastroList = new ArrayList<JornadaCadastro>();
        jornadaCadastroList = banco.consultaJornadas();
        jornadaCadastro = new JornadaCadastro();
        novaJornadaCadastro = new JornadaCadastro();
        selecionadoJornadaCadastro = new SimpleSelection();
        editJornadaCadastro = new JornadaCadastro();
        //turnosMarcados = new ArrayList<Integer>();
        // diasMarcados = new ArrayList<Integer>();
        carregarDiaSemana();
        objLocale = new Locale("pt", "BR");
        abaCorrente = "sub31";
        //   infoMessage = "";
    }

    public void addNovaJornada() {
        Banco banco = new Banco();
        Metodos.setLogInfo("Adicionar Jornada - " + novaJornadaCadastro.getNome());
        if (novaJornadaCadastro.estaPreenchidoCorretamente()) {
            int flag = banco.addJornadaCadastro(novaJornadaCadastro);

            if (flag == 0) {
                FacesMessage msgErro = new FacesMessage("Jornada adicionada com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 1) {
                FacesMessage msgErro = new FacesMessage("O nome da jornada já existe!");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }

            if (flag == 2) {
                FacesMessage msgErro = new FacesMessage("Dados Inválidos");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            }
            banco = new Banco();
            jornadaCadastroList = new ArrayList<JornadaCadastro>();
            jornadaCadastroList = banco.consultaJornadas();
        } else {
            FacesMessage msgErro = new FacesMessage("O nome do horário deve ser preenchido!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        novaJornadaCadastro = new JornadaCadastro();
    }

    public void showEditarJornada() {
        editJornadaCadastro = new JornadaCadastro();
        id_jornada = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id_jornada");
        Integer id_jornadaInt = Integer.parseInt(id_jornada);
        for (Iterator<JornadaCadastro> it = jornadaCadastroList.iterator(); it.hasNext();) {
            JornadaCadastro jornadaCadastro_ = it.next();
            if (id_jornadaInt.equals(jornadaCadastro_.getId())) {
                editJornadaCadastro = jornadaCadastro_;
                editJornadaCadastroNome = editJornadaCadastro.getNome();
            }
        }
    }

    /*  public void changeVFturno() {
     String turno_idStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("turno_id");
     Integer turno_id = Integer.parseInt(turno_idStr);
    
     for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
     Turno turno = it.next();
     if (turno.getId().equals(turno_id)) {
     //     turno.change();
    
     if (turnosMarcados.contains(turno_id)) {
     turnosMarcados.remove(turno_id);
     } else {
     turnosMarcados.add(turno_id);
     }
     }
     }
     }*/

    /* public void changeVFdia() {
     String diaJornada_idStr = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("diaJornada_id");
     Integer diaJornada_id = Integer.parseInt(diaJornada_idStr);
    
     for (Iterator<DiaJornada> it = diasJornadaList.iterator(); it.hasNext();) {
     DiaJornada diaJornada = it.next();
     if (diaJornada.getDiaInt().equals(diaJornada_id)) {
     //        diaJornada.change();
     if (diasMarcados.contains(diaJornada_id)) {
     diasMarcados.remove(diaJornada_id);
     } else {
     diasMarcados.add(diaJornada_id);
     }
     }
     }
     }*/
    public void editarJornadaCadastro() {
        Banco banco = new Banco();
        Metodos.setLogInfo("Editar Jornada - " + editJornadaCadastro.getNome());
        int flag = banco.editJornada(editJornadaCadastro);
        if (flag == 0) {
            FacesMessage msgErro = new FacesMessage("Jornada atualizada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 1) {
            FacesMessage msgErro = new FacesMessage("O nome da jornada já existe!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        if (flag == 2) {
            FacesMessage msgErro = new FacesMessage("Dados Inválidos");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }
        banco = new Banco();
        jornadaCadastroList = banco.consultaJornadas();
        novaJornadaCadastro = new JornadaCadastro();
        //      selecionadoJornadaCadastro = new SimpleSelection();
        //       id_jornada = null;
    }

    public void deletarJornada() {

        Banco banco = new Banco();

        JornadaCadastro jornadaCadastroSelecionada = new JornadaCadastro();
        Integer index = null;
        for (Iterator<Object> it = selecionadoJornadaCadastro.getKeys(); it.hasNext();) {
            index = (Integer) it.next();
        }

        if (index == null) {
            FacesMessage msgErro = new FacesMessage("Nenhuma linha selecionada para remoção!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            if (banco.findCronogramas(jornadaCadastroSelecionada.getId())) {
                FacesMessage msgErro = new FacesMessage("A Jornada não pode ser excluida pois está sendo utilizada.");
                FacesContext.getCurrentInstance().addMessage(null, msgErro);
            } else {
                jornadaCadastroSelecionada = jornadaCadastroList.get(index);
                Metodos.setLogInfo("Deletar Jornada - " + jornadaCadastroSelecionada.getNome());

                boolean flag = banco.deleteJornada(jornadaCadastroSelecionada.getId());

                if (!flag) {
                    FacesMessage msgErro = new FacesMessage("A Jornada não pode ser excluida pois está sendo utilizada por algum funcionário.");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                } else {
                    FacesMessage msgErro = new FacesMessage("Jornada excluida com sucesso!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }

                banco = new Banco();
                jornadaCadastroList = banco.consultaJornadas();
                selecionadoJornadaCadastro = new SimpleSelection();
                id_jornada = null;
            }
        }
    }

    private void carregarDiaSemana() {
        diaSemanaList = new ArrayList<SelectItem>();
        diaSemanaList.add(new SelectItem(0, "Dia"));
        diaSemanaList.add(new SelectItem(1, "Semana"));
        diaSemanaList.add(new SelectItem(2, "Mês"));
    }

    public void consultaHorariosList() {
        horariosList = new ArrayList<Turno>();
        Banco banco = new Banco();
        horariosList = banco.consultaHorariosList();
    }

    public String showHorariosXJornada() {
        consultaHorariosList();
        //      turnosMarcados.clear();
        //    diasMarcados.clear();
        //       infoMessage = "";
        montaJornadas();
        //Pesquisa se Existe virada
        return "navegarJornada";
    }

    public void montaJornadas() {
        ArrayList<HorariosXdia> horariosXdias = new ArrayList<HorariosXdia>();

        horariosXdias = consultaHorariosXJornada();

        horariosXjornadaCompleta = new HashMap<Integer, ArrayList<HorariosXdia>>();
        diasJornadaList = new ArrayList<DiaJornada>();

        //Gera o número de dias
        Integer unidade = horariosXdias.get(0).getUnits();
        Integer nroCiclos = horariosXdias.get(0).getCyle();
        Integer deslocI = 0;
        if (horariosXdias.get(0).getSaida() == null) {
            horariosXdias.remove(0);
        }

        if (unidade == 0) {
            for (int i = 0; i <= nroCiclos; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }

        } else if (unidade == 2) {
            for (int i = 0; i <= nroCiclos * 31; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }
        } else {
            for (int i = 1; i <= nroCiclos * 7; i++) {
                horariosXjornadaCompleta.put(i, new ArrayList<HorariosXdia>());
            }
            deslocI = 1;
        }
        //
        //Coloca os Horários nos dias

        for (int i = 0; i < horariosXdias.size(); i++) {
            HorariosXdia horariosXdia = horariosXdias.get(i);
            Integer dia = horariosXdia.getDia();
            ArrayList<HorariosXdia> hXd_aux = horariosXjornadaCompleta.get(dia);
            hXd_aux.add(horariosXdia);
            horariosXjornadaCompleta.put(dia, hXd_aux);
        }

        //Monta tabela de Apresentação
        int qntCiclos = 0;
        if (unidade == 0) {
            qntCiclos = nroCiclos;
        } else if (unidade == 2) {
            qntCiclos = nroCiclos * 31;
        } else {
            qntCiclos = nroCiclos * 7;
        }
        for (int i = 0 + deslocI; i < qntCiclos + deslocI; i++) {
            ArrayList<HorariosXdia> hXd_aux = horariosXjornadaCompleta.get(i);
            DiaJornada dj_aux = new DiaJornada(hXd_aux, i, unidade);
            diasJornadaList.add(dj_aux);
        }
    }

    public void adicionarTurnos() {
        ArrayList<Turno> turnosList = new ArrayList<Turno>();

        // teste de validação 1 : colocar virada anterior a um dia que já contem 2 marcados
        int maiorDosProximos = 0;
        int turnosMarcados = 0;
        int diasMarcados = 0;

        Integer turnoVirada = 0;

        for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
            Turno turno = it.next();
            if (turno.getMarked()) {
                turnosMarcados++;
                if (turnoVirada == 0) {
                    turnoVirada = (turno.getSaida().before(turno.getEntrada())) ? 1 : 0;
                }
            }
        }

        for (int i = 0; i < diasJornadaList.size() - 1; i++) {
            if (diasJornadaList.get(i).getMarked()) {
                DiaJornada dj = diasJornadaList.get(i + 1);
                if (maiorDosProximos < dj.getHxd().size()) {
                    maiorDosProximos = dj.getHxd().size();
                }
            }
        }
        // teste de validação 2 : dia com mais de 2 marcados ou com uma virada no dia anterior
        int viradaAnterior = 0;
        for (int i = 1; i < diasJornadaList.size(); i++) {
            if (diasJornadaList.get(i).getMarked()) {
                DiaJornada dj = diasJornadaList.get(i - 1);
                if (viradaAnterior == 0) {
                    viradaAnterior = (dj.getHasVirada()) ? 1 : 0;
                }
            }
        }

        int maior = 0;


        for (int i = 0; i < diasJornadaList.size(); i++) {
            if (diasJornadaList.get(i).getMarked()) {

                DiaJornada dj = diasJornadaList.get(i);
                if (dj.getHxd().size() + turnosMarcados >= maior) {
                    maior = dj.getHxd().size() + turnosMarcados;
                }
            }
        }
        if (viradaAnterior + maior > 2 || maiorDosProximos + turnoVirada > 2) {
            FacesMessage msgErro = new FacesMessage("Um dia não pode conter mais de dois horários!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);

        } else {
            // Testa se existem horarios em conflito
            SimpleDateFormat sfHora = new SimpleDateFormat("HH:mm");
            String horario1 = "";
            String iniciofaixaEntrada1 = "";
            String fimfaixaEntrada1 = "";
            String iniciofaixaSaida1 = "";
            String fimfaixaSaida1 = "";
            String horario2 = "";
            String iniciofaixaEntrada2 = "";
            String fimfaixaEntrada2 = "";
            String iniciofaixaSaida2 = "";
            String fimfaixaSaida2 = "";

            Boolean conflito = false;
            ArrayList<Turno> aSeremAdicionados = new ArrayList<Turno>();
            ArrayList<Turno> aSeremAdicionadosComVirada = new ArrayList<Turno>();
            ArrayList<HorariosXdia> horariosJaUtilizados = new ArrayList<HorariosXdia>();

            //teste 1: conflito entre dois turnos a serem adicionados
            for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
                Turno turno = it.next();
                if (turno.getMarked()) {

                    if (turno.hasVirada) {
                        aSeremAdicionadosComVirada.add(turno);
                    } else {
                        aSeremAdicionados.add(turno);
                    }
                }
            }
            for (int i = 0; i < aSeremAdicionados.size() - 1; i++) {
                Turno turno = aSeremAdicionados.get(i);
                for (int j = i + 1; j < aSeremAdicionados.size(); j++) {
                    Turno turno1 = aSeremAdicionados.get(j);
                    if (turno1.getInicioFaixaEntrada().before(turno.getInicioFaixaEntrada())) {
                        if (turno1.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                            horario1 = turno1.getNome();
                            fimfaixaSaida1 = sfHora.format(new Date(turno1.getFimFaixaSaida().getTime()));
                            horario2 = turno.getNome();
                            iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                            conflito = true;
                        }
                    } else {
                        if (turno1.getInicioFaixaEntrada().before(turno.getFimFaixaSaida())) {
                            horario1 = turno1.getNome();
                            iniciofaixaEntrada1 = sfHora.format(new Date(turno1.getInicioFaixaEntrada().getTime()));
                            horario2 = turno.getNome();
                            fimfaixaSaida2 = sfHora.format(new Date(turno.getFimFaixaSaida().getTime()));
                            conflito = true;
                        }
                    }
                }
            }
            for (int i = 0; i < aSeremAdicionados.size(); i++) {
                Turno turno = aSeremAdicionados.get(i);
                for (int j = 0; j < aSeremAdicionadosComVirada.size(); j++) {
                    Turno turnoComVirada = aSeremAdicionadosComVirada.get(j);
                    if (turno.getInicioFaixaEntrada().after(turnoComVirada.getInicioFaixaEntrada())) {
                        horario1 = turno.getNome();
                        iniciofaixaEntrada1 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));

                        horario2 = turnoComVirada.getNome();
                        iniciofaixaEntrada2 = sfHora.format(new Date(turnoComVirada.getInicioFaixaEntrada().getTime()));

                        conflito = true;
                    }
                }
            }
            for (int i = 0; i < aSeremAdicionadosComVirada.size(); i++) {
                Turno turnoComVirada = aSeremAdicionadosComVirada.get(i);
                for (int j = 0; j < diasJornadaList.size() - 1; j++) {
                    DiaJornada dj1 = diasJornadaList.get(j);
                    DiaJornada dj2 = diasJornadaList.get(j + 1);
                    if (dj1.getMarked() && dj2.getMarked()) {
                        for (int k = 0; k < dj2.getHxd().size(); k++) {
                            HorariosXdia horariosXdia = dj2.getHxd().get(k);
                            if (turnoComVirada.getFimFaixaSaida().after(horariosXdia.getInicioFaixaEntrada())) {
                                horario1 = turnoComVirada.getNome();
                                horario2 = horariosXdia.getSchName();
                                fimfaixaSaida1 = sfHora.format(new Date(turnoComVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(horariosXdia.getInicioFaixaEntrada().getTime()));
                                conflito = true;
                            }

                        }
                    }
                }
            }
            //Teste 2: Se existe conflito entre os horarios a serem adicionados e os que já estão sendo utilizados
            for (int i = 0; i < diasJornadaList.size(); i++) {
                DiaJornada dj = diasJornadaList.get(i);
                if (dj.getMarked()) {
                    for (int j = 0; j < dj.getHxd().size(); j++) {
                        HorariosXdia horarioDia = dj.getHxd().get(j);
                        horariosJaUtilizados.add(horarioDia);
                    }
                }
            }
            for (int i = 0; i < aSeremAdicionados.size(); i++) {
                Turno turno = aSeremAdicionados.get(i);
                for (int j = 0; j < horariosJaUtilizados.size(); j++) {
                    HorariosXdia horariosXdia = horariosJaUtilizados.get(j);
                    if (horariosXdia.getInicioFaixaEntrada().before(turno.getInicioFaixaEntrada())) {
                        if (horariosXdia.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                            horario1 = horariosXdia.getSchName();
                            horario2 = turno.getNome();
                            fimfaixaSaida1 = sfHora.format(new Date(horariosXdia.getFimFaixaSaida().getTime()));
                            iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                            conflito = true;
                        }
                    } else {
                        if (horariosXdia.getInicioFaixaEntrada().before(turno.getFimFaixaSaida())) {
                            horario1 = horariosXdia.getSchName();
                            horario2 = turno.getNome();
                            iniciofaixaEntrada1 = sfHora.format(new Date(horariosXdia.getInicioFaixaEntrada().getTime()));
                            fimfaixaSaida2 = sfHora.format(new Date(turno.getFimFaixaSaida().getTime()));
                            conflito = true;
                        }
                    }
                }
            }
            for (int i = 0; i < aSeremAdicionadosComVirada.size(); i++) {
                Turno turno = aSeremAdicionadosComVirada.get(i);
                for (int j = 0; j < horariosJaUtilizados.size(); j++) {
                    HorariosXdia horariosXdia = horariosJaUtilizados.get(j);
                    if (horariosXdia.getInicioFaixaEntrada().after(turno.getInicioFaixaEntrada())) {
                        horario1 = horariosXdia.getSchName();
                        horario2 = turno.getNome();
                        iniciofaixaEntrada1 = sfHora.format(new Date(horariosXdia.getInicioFaixaEntrada().getTime()));
                        iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                        conflito = true;
                    }
                }
            }
            //Teste 3: testa se existem horarios que chocam com a virada do dia anterior
            for (int i = 1; i < diasJornadaList.size(); i++) {
                if (diasJornadaList.get(i).getMarked()) {
                    DiaJornada dj = diasJornadaList.get(i - 1);
                    if (dj.getHasVirada()) {
                        HorariosXdia horarioVirada = new HorariosXdia();
                        for (int j = 0; j < dj.getHxd().size(); j++) {
                            if (dj.getHxd().get(j).getEntrada().after(dj.getHxd().get(j).getSaida())) {
                                horarioVirada = dj.getHxd().get(j);
                            }
                        }
                        for (int j = 0; j < aSeremAdicionados.size(); j++) {
                            Turno turno = aSeremAdicionados.get(j);
                            if (horarioVirada.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                                horario1 = horarioVirada.getSchName();
                                horario2 = turno.getNome();
                                fimfaixaSaida1 = sfHora.format(new Date(horarioVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                                conflito = true;
                            }
                        }
                        for (int j = 0; j < aSeremAdicionadosComVirada.size(); j++) {
                            Turno turno = aSeremAdicionadosComVirada.get(j);
                            if (horarioVirada.getFimFaixaSaida().after(turno.getInicioFaixaEntrada())) {
                                horario1 = horarioVirada.getSchName();
                                horario2 = turno.getNome();

                                fimfaixaSaida1 = sfHora.format(new Date(horarioVirada.getFimFaixaSaida().getTime()));
                                iniciofaixaEntrada2 = sfHora.format(new Date(turno.getInicioFaixaEntrada().getTime()));
                                conflito = true;
                            }
                        }
                    }
                }
            }
            //Teste4: testa se existem horarios a serem adicionados com virada e que chocam com horarios já existentes no próximo dia
            for (int i = 0; i < diasJornadaList.size() - 1; i++) {
                if (diasJornadaList.get(i).getMarked()) {
                    DiaJornada dj = diasJornadaList.get(i + 1);
                    for (int j = 0; j < aSeremAdicionadosComVirada.size(); j++) {
                        Turno turnoComVirada = aSeremAdicionadosComVirada.get(j);
                        ArrayList<HorariosXdia> HorariosDiaSeguinte = dj.getHxd();
                        for (int k = 0; k < HorariosDiaSeguinte.size(); k++) {
                            HorariosXdia horariosXdia = HorariosDiaSeguinte.get(k);
                            if (turnoComVirada.getFimFaixaSaida().after(horariosXdia.getInicioFaixaEntrada())) {
                                horario1 = horariosXdia.getSchName();
                                horario2 = turnoComVirada.getNome();

                                iniciofaixaEntrada1 = sfHora.format(new Date(horariosXdia.getInicioFaixaEntrada().getTime()));
                                fimfaixaSaida2 = sfHora.format(new Date(turnoComVirada.getFimFaixaSaida().getTime()));
                                conflito = true;
                            }
                        }
                    }
                }
            }
            if (conflito) {
                String ie1 = (iniciofaixaEntrada1.equals("") ? "" : "Inicio da faixa de entrada do horário (" + horario1 + "): " + iniciofaixaEntrada1);
                String fe1 = (fimfaixaEntrada1.equals("") ? "" : "Fim da faixa de entrada do horário (" + horario1 + "): " + fimfaixaEntrada1);
                String is1 = (iniciofaixaSaida1.equals("") ? "" : "Inicio da faixa de saida do horário (" + horario1 + "): " + iniciofaixaSaida1);
                String fs1 = (fimfaixaSaida1.equals("") ? "" : "Fim da faixa de saida do horário (" + horario1 + "): " + fimfaixaSaida1);
                String ie2 = (iniciofaixaEntrada2.equals("") ? "" : "Inicio da faixa de entrada do horário (" + horario2 + "): " + iniciofaixaEntrada2);
                String fe2 = (fimfaixaEntrada2.equals("") ? "" : "Fim da faixa de entrada do horário (" + horario2 + "): " + fimfaixaEntrada2);
                String is2 = (iniciofaixaSaida2.equals("") ? "" : "Inicio da faixa de saida do horário (" + horario2 + "): " + iniciofaixaSaida2);
                String fs2 = (fimfaixaSaida2.equals("") ? "" : "Fim da faixa de saida do horário (" + horario2 + "): " + fimfaixaSaida2);


                FacesMessage msgErro = new FacesMessage("Existe conflito entre os horários: (" + horario1 + ") e (" + horario2 + ")  Verifique se existe conflito nas faixas de entrada e saída");
                /*FacesMessage msgErro = new FacesMessage("Existe conflito entre os horários: (" + horario1 + ") e (" + horario2 + ") ----------------------------------------------------------------------------------------------------------------------------------------------------------- " + ie1+fe1+is1+fs1+ " ----------------------------------------------------------------------------------------------------------------------------------------------------------- " +ie2+fe2+is2+fs2);*/
                FacesContext.getCurrentInstance().addMessage(null, msgErro);

            } else {

                //monta selecionados
                //infoMessage = "";
                for (Iterator<Turno> it = horariosList.iterator(); it.hasNext();) {
                    Turno turno = it.next();
                    if (turno.getMarked()) {
                        turnosList.add(turno);
                    }
                }
                
                for (int i = 0; i < diasJornadaList.size(); i++) {

                    DiaJornada dj = diasJornadaList.get(i);
                    if (dj.getMarked()) {
                        ArrayList<HorariosXdia> hXd = dj.getHxd();
                        for (int j = 0; j < turnosList.size(); j++) {
                            Turno t = turnosList.get(j);
                            //public HorariosXdia(Integer dia, Boolean temVirada, Integer units, Integer horarioId, Integer cyle, Timestamp entrada, Timestamp saida, String schName) {
                            HorariosXdia hd = new HorariosXdia(dj.getDiaInt(), t.hasVirada, editJornadaCadastro.getUnidadeCiclosInt(), t.getId(), editJornadaCadastro.getQuantidadeCiclos(), t.getEntrada(), t.getSaida(), t.getNome(), t.getInicioFaixaEntrada(), t.getFimFaixaEntrada(), t.getInicioFaixaSaida(), t.getFimFaixaSaida());
                            hXd.add(hd);
                        }
                        hXd = ordenaHorarios(hXd);
                        dj.setHxd(hXd);
                        dj.initStringHorarios();
                        dj.setHasVirada(dj.calculaVirada());
                    }
                }

                ArrayList<Horario> horariosTodosList = getTodosHorarios(diasJornadaList);

                if (checkIrregularidade(horariosTodosList)) {
                    FacesMessage msgErro = new FacesMessage("Atenção! Existem dias na jornada que não possuem o descanso mínimo de 11 horas!");
                    FacesContext.getCurrentInstance().addMessage(null, msgErro);
                }

            }
        }

    }

    private ArrayList<Horario> getTodosHorarios(List<DiaJornada> diasJornadaList) {
        ArrayList<Horario> horariosTodosList = new ArrayList<Horario>();
        for (int i = 0; i < diasJornadaList.size(); i++) {
            DiaJornada dj = diasJornadaList.get(i);
            ArrayList<HorariosXdia> horariosXDiaList = dj.getHxd();
            for (int j = 0; j < horariosXDiaList.size(); j++) {
                HorariosXdia horario = horariosXDiaList.get(j);
                Date entrada = horario.getEntrada();
                Date saida = horario.getSaida();
                Horario h = new Horario();
                if (entrada.before(saida)) {
                    long tentrada = entrada.getTime() + (86400000 * i);
                    long tsaida = saida.getTime() + (86400000 * i);
                    h.setEntrada(new Date(tentrada));
                    h.setSaida(new Date(tsaida));
                } else {
                    long tentrada = entrada.getTime() + (86400000 * i);
                    long tsaida = saida.getTime() + (86400000 * (i + 1));
                    h.setEntrada(new Date(tentrada));
                    h.setSaida(new Date(tsaida));
                }
                horariosTodosList.add(h);
            }
        }
        Collections.sort(horariosTodosList, new HorarioComparator());
        return horariosTodosList;
    }

    private Boolean checkIrregularidade(ArrayList<Horario> horariosTodosList) {

        boolean ultimaPausaFoiMenosDe11Horas = false;
        boolean alerta11horas = false;
        Date inicioDescanso = new Date();
        Date fimDescanso = new Date();
        for (int i = 0; i < horariosTodosList.size() - 1; i++) {
            //  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            //     System.out.println("Descanso "+i+":");

            inicioDescanso = horariosTodosList.get(i).getSaida();
            fimDescanso = horariosTodosList.get(i + 1).getEntrada();
            //   System.out.println(sdf.format(inicioDescanso)+" às "+sdf.format(fimDescanso));
            if (fimDescanso.getTime() - inicioDescanso.getTime() < 39600000) {
                //  System.out.println("menos de 11 horas");
                if (ultimaPausaFoiMenosDe11Horas) {
                    //  System.out.println("denovo");
                    alerta11horas = true;
                } else {
                    ultimaPausaFoiMenosDe11Horas = true;
                }
            } else {
                //  System.out.println("maaais de 11");
                ultimaPausaFoiMenosDe11Horas = false;
            }
        }
        return alerta11horas;
    }

    public class HorarioComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Horario h1 = ((Horario) o1);
            Horario h2 = ((Horario) o2);
            if (h1.getEntrada().after(h2.getEntrada())) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public String voltar() {
        Banco banco = new Banco();
        jornadaCadastroList = banco.consultaJornadas();
        selecionadoJornadaCadastro = new SimpleSelection();
        id_jornada = null;
        return "navegarEscala";
    }

    public ArrayList<HorariosXdia> ordenaHorarios(ArrayList<HorariosXdia> hxd) {
        if (hxd.size() > 1) {
            int h0 = hxd.get(0).getEntrada().getHours();
            int m0 = hxd.get(0).getEntrada().getMinutes();
            int h1 = hxd.get(1).getEntrada().getHours();
            int m1 = hxd.get(1).getEntrada().getMinutes();
            if ( h1 < h0 || (h1 == h0 && m1 < m0) ) {
                HorariosXdia hd0 = hxd.get(0);
                HorariosXdia hd1 = hxd.get(1);
                hxd.clear();
                hxd.add(hd1);
                hxd.add(hd0);
            }
        }
        return hxd;
    }

    public void limparJornada() {
        for (int i = 0; i < diasJornadaList.size(); i++) {
            DiaJornada diaJornada = diasJornadaList.get(i);
            diaJornada.limparTurno();

        }
    }

    public void salvarTurnos() {
        Banco banco = new Banco();
        Metodos.setLogInfo("Alterar Turnos Jornada - " + editJornadaCadastro.getNome());
        ArrayList<Horario> horariosTodosList = getTodosHorarios(diasJornadaList);
        Boolean isIrregular = checkIrregularidade(horariosTodosList);
        UsuarioBean usuarioBean = ((UsuarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioBean"));
        banco.updateJornada(editJornadaCadastro.getId(), !isIrregular, usuarioBean.getUsuario().getLogin());

        boolean flag = banco.salvarTurnos(diasJornadaList, editJornadaCadastro.getId());


        if (!flag) {
            FacesMessage msgErro = new FacesMessage("A jornada não pode ser salva!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        } else {
            FacesMessage msgErro = new FacesMessage("Jornada editada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msgErro);
        }

        banco = new Banco();
        novaJornadaCadastro = new JornadaCadastro();
//        editJornadaCadastro = new JornadaCadastro();
        jornadaCadastroList = banco.consultaJornadas();
        selecionadoJornadaCadastro = new SimpleSelection();
        id_jornada = null;



    }

    public ArrayList<HorariosXdia> consultaHorariosXJornada() {
        ArrayList<HorariosXdia> horariosXjornada = new ArrayList<HorariosXdia>();
        Banco banco = new Banco();
        horariosXjornada = banco.consultaHorariosXJornada(editJornadaCadastro.getId());
        return horariosXjornada;
    }

    public String getAbaCorrente() {
        return abaCorrente;
    }

    public void setAbaCorrente(String abaCorrente) {
        this.abaCorrente = abaCorrente;
    }

    public void setAba() {
        String tab = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tab");
        abaCorrente = tab;
    }

    public JornadaCadastro getEditJornadaCadastro() {
        return editJornadaCadastro;
    }

    public void setEditJornadaCadastro(JornadaCadastro editJornadaCadastro) {
        this.editJornadaCadastro = editJornadaCadastro;
    }

    public JornadaCadastro getJornadaCadastro() {
        return jornadaCadastro;
    }

    public void setJornadaCadastro(JornadaCadastro jornadaCadastro) {
        this.jornadaCadastro = jornadaCadastro;
    }

    public JornadaCadastro getNovaJornadaCadastro() {
        return novaJornadaCadastro;
    }

    public void setNovaJornadaCadastro(JornadaCadastro novaJornadaCadastro) {
        this.novaJornadaCadastro = novaJornadaCadastro;
    }

    public Selection getSelecionadoJornadaCadastro() {
        return selecionadoJornadaCadastro;
    }

    public void setSelecionadoJornadaCadastro(Selection selecionadoJornadaCadastro) {
        this.selecionadoJornadaCadastro = selecionadoJornadaCadastro;
    }

    public void showAdicionarNovaJornada() {
        novaJornadaCadastro = new JornadaCadastro();
    }

    public List<JornadaCadastro> getJornadaCadastroList() {
        return jornadaCadastroList;
    }

    public void setJornadaCadastroList(List<JornadaCadastro> jornadaCadastroList) {
        this.jornadaCadastroList = jornadaCadastroList;
    }

    public Locale getObjLocale() {
        return objLocale;
    }

    public void setObjLocale(Locale objLocale) {
        this.objLocale = objLocale;
    }

    public List<SelectItem> getDiaSemanaList() {
        return diaSemanaList;
    }

    public void setDiaSemanaList(List<SelectItem> diaSemanaList) {
        this.diaSemanaList = diaSemanaList;
    }

    public String getId_jornada() {
        return id_jornada;
    }

    public void setId_jornada(String id_jornada) {
        this.id_jornada = id_jornada;
    }

    public List<Turno> getHorariosList() {
        return horariosList;
    }

    public void setHorariosList(List<Turno> horariosList) {
        this.horariosList = horariosList;

    }

    public List<DiaJornada> getDiasJornadaList() {
        return diasJornadaList;
    }

    public void setDiasJornadaList(List<DiaJornada> diasJornadaList) {
        this.diasJornadaList = diasJornadaList;
    }

    public String getEditJornadaCadastroNome() {
        return editJornadaCadastroNome;
    }

    public void setEditJornadaCadastroNome(String editJornadaCadastroNome) {
        this.editJornadaCadastroNome = editJornadaCadastroNome;
    }
}
