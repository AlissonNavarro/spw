package ConsultaPonto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author amsgama
 */
public class Jornada implements Serializable {

    private String nome;
    private Integer schClassID;
    private String legend;
    private String legendaJornada;
    private Timestamp inicioJornada;
    private Integer prazoMinutosAtraso;
    private Integer isEntradaObrigatoria;
    private Timestamp checkInLimiteAntecipada;
    private Timestamp checkInLimiteAtrasada;
    private Integer sday;
    private Timestamp terminioJornada;
    private Integer prazoMinutosAdiantado;
    private Integer isSaidaObrigatoria;
    private Timestamp checkOutLimiteAntecipada;
    private Timestamp checkOutLimiteAtrasada;
    private Integer eday;
    private Integer trabalhoMinutos;
    private float tipoJornada;
    private Integer cyle;
    private Integer units;
    private Date startdate;
    private Boolean isOvertime = false;
    private Boolean temVirada = false;
    private Boolean soEntrada = false;
    private boolean soSaida = false;
//Dados da jornada ficticia da virada da noite.
    private String inicioJornada_ = "24:00:00";
    private Integer prazoMinutosAtraso_ = 0;
    private Integer isEntradaObrigatoria_ = 0;
    private String checkInLimiteAntecipada_ = "24:00:00";
    private String checkInLimiteAtrasada_ = "24:00:00";
    private Integer sday_;
    private String terminioJornada_ = "24:00:00";
    private Integer prazoMinutosAdiantado_ = 0;
    private Integer isSaidaObrigatoria_ = 0;
    private String checkOutLimiteAntecipada_ = "24:00:00";
    private String checkOutLimiteAtrasada_ = "24:00:00";
    private Integer eday_;
    private Timestamp inicioJornadaL;
    private Timestamp terminioJornadaL;
    private Timestamp inicioDescanso1;
    private Timestamp fimDescanso1;
    private Boolean deduzirDescanco1;
    private Timestamp inicioDescanso2;
    private Timestamp fimDescanso2;
    private Boolean deduzirDescanco2;
    private Timestamp inicioIntrajornada;
    private Timestamp fimIntrajornada;
    private Boolean deduzirIntrajornada;
    private Boolean combinarEntrada; //Combina entrada da interjornada com saida do primeiro descanso
    private Boolean combinarSaida; //Combina saida da interjornada com entrada do segundo descanso
    private Integer toleranciaDescanso;
    private Boolean isDeslocamento;
    private Boolean folga;

    public Jornada(String nome, Integer schClassID, Timestamp inicioJornada, Timestamp terminioJornada, Integer prazoMinutosAtraso,
            Integer prazoMinutosAdiantado, Integer checkIn, Integer checkOut, Integer trabalhoMinutos,
            Timestamp checkInLimiteAntecipada, Timestamp checkInLimiteAtrasada, Timestamp checkOutLimiteAntecipada,
            Timestamp checkOutLimiteAtrasada, float tipoJornada, Integer sday, Integer eday, Integer cyle, Integer units,
            Date startdate, Timestamp inicioJornadaL, Timestamp terminioJornadaL, String legend,
            Timestamp inicioDescanso1, Timestamp fimDescanso1, Timestamp inicioDescanso2, Timestamp fimDescanso2,
            Integer toleranciaDescanso, Boolean deduzirDescanco1, Boolean deduzirDescanco2, Timestamp inicioIntrajornada,
            Timestamp fimIntrajornada, Boolean deduzirIntrajornada, Boolean combinarEntrada, Boolean combinarSaida, Boolean folga) {

        this.nome = nome;
        this.schClassID = schClassID;
        this.inicioJornada = inicioJornada;
        this.terminioJornada = terminioJornada;
        this.prazoMinutosAtraso = prazoMinutosAtraso;
        this.prazoMinutosAdiantado = prazoMinutosAdiantado;
        this.isEntradaObrigatoria = checkIn;
        this.isSaidaObrigatoria = checkOut;
        this.trabalhoMinutos = trabalhoMinutos;
        this.checkInLimiteAntecipada = checkInLimiteAntecipada;
        this.checkInLimiteAtrasada = checkInLimiteAtrasada;
        this.checkOutLimiteAntecipada = checkOutLimiteAntecipada;
        this.checkOutLimiteAtrasada = checkOutLimiteAtrasada;
        this.tipoJornada = tipoJornada;
        this.sday = sday;
        this.eday = eday;
        this.cyle = cyle;
        this.units = units;
        this.startdate = startdate;
        this.inicioJornadaL = inicioJornadaL;
        this.terminioJornadaL = terminioJornadaL;
        this.legend = legend;
        this.inicioDescanso1 = inicioDescanso1;
        this.fimDescanso1 = fimDescanso1;
        this.inicioDescanso2 = inicioDescanso2;
        this.fimDescanso2 = fimDescanso2;
        /* if (inicioDescanso1 == null && inicioDescanso2 == null) {
         this.inicioDescanso1 = new Timestamp(1);
         this.fimDescanso1 = new Timestamp(1);
         this.inicioDescanso2 = new Timestamp(1);
         this.fimDescanso2 = new Timestamp(1);
         } else if (inicioDescanso1 != null) {
         this.inicioDescanso1 = inicioDescanso1;
         this.fimDescanso1 = fimDescanso1;
         this.inicioDescanso2 = new Timestamp(1);
         this.fimDescanso2 = new Timestamp(1);
         if (inicioDescanso2 != null) {
         this.inicioDescanso2 = inicioDescanso2;
         this.fimDescanso2 = fimDescanso2;
         }
         }*/
        /*this.inicioIntrajornada = new Timestamp(1);
         this.fimIntrajornada = new Timestamp(1);
         if (inicioIntrajornada != null) {
         this.inicioIntrajornada = inicioIntrajornada;
         this.fimIntrajornada = fimIntrajornada;
         }*/
        this.inicioIntrajornada = inicioIntrajornada;
        this.fimIntrajornada = fimIntrajornada;
        this.combinarEntrada = combinarEntrada;
        this.combinarSaida = combinarSaida;
        this.toleranciaDescanso = toleranciaDescanso;
        this.deduzirDescanco1 = deduzirDescanco1;
        this.deduzirDescanco2 = deduzirDescanco2;
        this.deduzirIntrajornada = deduzirIntrajornada;
        this.folga = folga;
    }

    public Jornada(String nome, Integer schClassID, Timestamp inicioJornada, Timestamp terminioJornada, Integer prazoMinutosAtraso,
            Integer prazoMinutosAdiantado, Integer checkIn, Integer checkOut, Integer trabalhoMinutos,
            Timestamp checkInLimiteAntecipada, Timestamp checkInLimiteAtrasada, Timestamp checkOutLimiteAntecipada,
            Timestamp checkOutLimiteAtrasada, float tipoJornada, Integer sday, Integer eday, Integer cyle, Integer units,
            Date startdate, Timestamp inicioJornadaL, Timestamp terminioJornadaL, String legend,
            Timestamp inicioDescanso1, Timestamp fimDescanso1, Timestamp inicioDescanso2, Timestamp fimDescanso2,
            Integer toleranciaDescanso, Boolean deduzirDescanco1, Boolean deduzirDescanco2, Boolean isDeslocamento, Timestamp inicioIntrajornada,
            Timestamp fimIntrajornada, Boolean deduzirIntrajornada, Boolean combinarEntrada, Boolean combinarSaida, Boolean folga) {

        this.nome = nome;
        this.schClassID = schClassID;
        this.inicioJornada = inicioJornada;
        this.terminioJornada = terminioJornada;
        this.prazoMinutosAtraso = prazoMinutosAtraso;
        this.prazoMinutosAdiantado = prazoMinutosAdiantado;
        this.isEntradaObrigatoria = checkIn;
        this.isSaidaObrigatoria = checkOut;
        this.trabalhoMinutos = trabalhoMinutos;
        this.checkInLimiteAntecipada = checkInLimiteAntecipada;
        this.checkInLimiteAtrasada = checkInLimiteAtrasada;
        this.checkOutLimiteAntecipada = checkOutLimiteAntecipada;
        this.checkOutLimiteAtrasada = checkOutLimiteAtrasada;
        this.tipoJornada = tipoJornada;
        this.sday = sday;
        this.eday = eday;
        this.cyle = cyle;
        this.units = units;
        this.startdate = startdate;
        this.inicioJornadaL = inicioJornadaL;
        this.terminioJornadaL = terminioJornadaL;
        this.legend = legend;
        this.inicioDescanso1 = inicioDescanso1;
        this.fimDescanso1 = fimDescanso1;
        this.inicioDescanso2 = inicioDescanso2;
        this.fimDescanso2 = fimDescanso2;
        if (inicioDescanso1 == null && inicioDescanso2 == null) {
            this.inicioDescanso1 = new Timestamp(1);
            this.fimDescanso1 = new Timestamp(1);
            this.inicioDescanso2 = new Timestamp(1);
            this.fimDescanso2 = new Timestamp(1);
        } else if (inicioDescanso1 != null) {
            this.inicioDescanso1 = inicioDescanso1;
            this.fimDescanso1 = fimDescanso1;
            this.inicioDescanso2 = new Timestamp(1);
            this.fimDescanso2 = new Timestamp(1);
            if (inicioDescanso2 != null) {
                this.inicioDescanso2 = inicioDescanso2;
                this.fimDescanso2 = fimDescanso2;
            }
        }
        this.inicioIntrajornada = new Timestamp(1);
        this.fimIntrajornada = new Timestamp(1);
        if (inicioIntrajornada != null) {
            this.inicioIntrajornada = inicioIntrajornada;
            this.fimIntrajornada = fimIntrajornada;
        }
        this.combinarEntrada = combinarEntrada;
        this.combinarSaida = combinarSaida;
        this.toleranciaDescanso = toleranciaDescanso;
        this.deduzirDescanco1 = deduzirDescanco1;
        this.deduzirDescanco2 = deduzirDescanco2;
        this.deduzirIntrajornada = deduzirIntrajornada;
        this.isDeslocamento = isDeslocamento;
        this.folga = folga;
    }

    public String getLegendaNumero() {

        String schClassIDStr = schClassID.toString();

        SimpleDateFormat sdfSemana = new SimpleDateFormat("HH:mm");

        String entrada = sdfSemana.format(inicioJornadaL.getTime());
        String saida = sdfSemana.format(terminioJornadaL.getTime());
        String legenda;

        legenda = "(" + schClassIDStr + " - " + entrada + " as " + saida + ")";

        return legenda;
    }

    public String getLegendaSigla() {

        if (inicioJornadaL == null || terminioJornadaL == null) {
            System.out.print("dd");
        }

        SimpleDateFormat sdfSemana = new SimpleDateFormat("HH:mm");
        String entrada = sdfSemana.format(inicioJornadaL.getTime());
        String saida = sdfSemana.format(terminioJornadaL.getTime());
        String legenda = "";
        if (legend != null && !legend.equals("")) {
            legenda = "(" + legend + " - " + entrada + " as " + saida + ")";
        }
        return legenda;
    }

    public Jornada() {
    }

    public Integer getIsEntradaObrigatoria() {
        return isEntradaObrigatoria;
    }

    public void setIsEntradaObrigatoria(Integer isEntradaObrigatoria) {
        this.isEntradaObrigatoria = isEntradaObrigatoria;
    }

    public Integer getIsEntradaObrigatoria_() {
        return isEntradaObrigatoria_;
    }

    public void setIsEntradaObrigatoria_(Integer isEntradaObrigatoria_) {
        this.isEntradaObrigatoria_ = isEntradaObrigatoria_;
    }

    public Integer getIsSaidaObrigatoria() {
        return isSaidaObrigatoria;
    }

    public void setIsSaidaObrigatoria(Integer isSaidaObrigatoria) {
        this.isSaidaObrigatoria = isSaidaObrigatoria;
    }

    public Integer getIsSaidaObrigatoria_() {
        return isSaidaObrigatoria_;
    }

    public void setIsSaidaObrigatoria_(Integer isSaidaObrigatoria_) {
        this.isSaidaObrigatoria_ = isSaidaObrigatoria_;
    }

    public void setCheckOut(Integer checkOut) {
        this.isSaidaObrigatoria = checkOut;
    }

    public Integer getPrazoMinutosAdiantado() {
        return prazoMinutosAdiantado;
    }

    public void setPrazoMinutosAdiantado(Integer prazoMinutosAdiantado) {
        this.prazoMinutosAdiantado = prazoMinutosAdiantado;
    }

    public Integer getPrazoMinutosAtraso() {
        return prazoMinutosAtraso;
    }

    public void setPrazoMinutosAtraso(Integer prazoMinutosAtraso) {
        this.prazoMinutosAtraso = prazoMinutosAtraso;
    }

    public float getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(float tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTrabalhoMinutos() {
        return trabalhoMinutos;
    }

    public void setTrabalhoMinutos(Integer trabalhoMinutos) {
        this.trabalhoMinutos = trabalhoMinutos;
    }

    public Integer getSday() {
        return sday;
    }

    public void setSday(Integer sday) {
        this.sday = sday;
    }

    public Integer getEday() {
        return eday;
    }

    public void setEday(Integer eday) {
        this.eday = eday;
    }

    public Timestamp getCheckInLimiteAntecipada() {
        return checkInLimiteAntecipada;
    }

    public void setCheckInLimiteAntecipada(Timestamp checkInLimiteAntecipada) {
        this.checkInLimiteAntecipada = checkInLimiteAntecipada;
    }

    public Timestamp getCheckInLimiteAtrasada() {
        return checkInLimiteAtrasada;
    }

    public void setCheckInLimiteAtrasada(Timestamp checkInLimiteAtrasada) {
        this.checkInLimiteAtrasada = checkInLimiteAtrasada;
    }

    public Timestamp getCheckOutLimiteAntecipada() {
        return checkOutLimiteAntecipada;
    }

    public void setCheckOutLimiteAntecipada(Timestamp checkOutLimiteAntecipada) {
        this.checkOutLimiteAntecipada = checkOutLimiteAntecipada;
    }

    public Timestamp getCheckOutLimiteAtrasada() {
        return checkOutLimiteAtrasada;
    }

    public void setCheckOutLimiteAtrasada(Timestamp checkOutLimiteAtrasada) {
        this.checkOutLimiteAtrasada = checkOutLimiteAtrasada;
    }

    public Timestamp getInicioJornada() {
        return inicioJornada;
    }

    public void setInicioJornada(Timestamp inicioJornada) {
        this.inicioJornada = inicioJornada;
    }

    public Timestamp getTerminioJornada() {
        return terminioJornada;
    }

    public void setTerminioJornada(Timestamp terminioJornada) {
        this.terminioJornada = terminioJornada;
    }

    public Integer getCyle() {
        return cyle;
    }

    public void setCyle(Integer cyle) {
        this.cyle = cyle;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }//

    public Boolean getSoEntrada() {
        return soEntrada;
    }

    public void setSoEntrada(Boolean soEntrada) {
        this.soEntrada = soEntrada;
    }

    public Boolean getTemVirada() {
        return temVirada;
    }

    public void setTemVirada(Boolean temVirada) {
        this.temVirada = temVirada;
    }

    public Boolean getIsCompleto() {
        if ((inicioJornada != null) && (checkInLimiteAntecipada != null) && (checkInLimiteAtrasada != null)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isJornadaNoturna() {
        if (terminioJornadaL == null || inicioJornadaL == null) {
            return false;
        } else if (terminioJornadaL.getTime() <= inicioJornadaL.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public String getCheckInLimiteAntecipada_() {
        return checkInLimiteAntecipada_;
    }

    public void setCheckInLimiteAntecipada_(String checkInLimiteAntecipada_) {
        this.checkInLimiteAntecipada_ = checkInLimiteAntecipada_;
    }

    public String getCheckInLimiteAtrasada_() {
        return checkInLimiteAtrasada_;
    }

    public void setCheckInLimiteAtrasada_(String checkInLimiteAtrasada_) {
        this.checkInLimiteAtrasada_ = checkInLimiteAtrasada_;
    }

    public String getCheckOutLimiteAntecipada_() {
        return checkOutLimiteAntecipada_;
    }

    public void setCheckOutLimiteAntecipada_(String checkOutLimiteAntecipada_) {
        this.checkOutLimiteAntecipada_ = checkOutLimiteAntecipada_;
    }

    public String getCheckOutLimiteAtrasada_() {
        return checkOutLimiteAtrasada_;
    }

    public void setCheckOutLimiteAtrasada_(String checkOutLimiteAtrasada_) {
        this.checkOutLimiteAtrasada_ = checkOutLimiteAtrasada_;
    }

    public Integer getEday_() {
        return eday_;
    }

    public void setEday_(Integer eday_) {
        this.eday_ = eday_;
    }

    public String getInicioJornada_() {
        return inicioJornada_;
    }

    public void setInicioJornada_(String inicioJornada_) {
        this.inicioJornada_ = inicioJornada_;
    }

    public Integer getPrazoMinutosAdiantado_() {
        return prazoMinutosAdiantado_;
    }

    public void setPrazoMinutosAdiantado_(Integer prazoMinutosAdiantado_) {
        this.prazoMinutosAdiantado_ = prazoMinutosAdiantado_;
    }

    public Integer getPrazoMinutosAtraso_() {
        return prazoMinutosAtraso_;
    }

    public void setPrazoMinutosAtraso_(Integer prazoMinutosAtraso_) {
        this.prazoMinutosAtraso_ = prazoMinutosAtraso_;
    }

    public Integer getSday_() {
        return sday_;
    }

    public void setSday_(Integer sday_) {
        this.sday_ = sday_;
    }

    public String getTerminioJornada_() {
        return terminioJornada_;
    }

    public void setTerminioJornada_(String terminioJornada_) {
        this.terminioJornada_ = terminioJornada_;
    }

    public boolean getSoSaida() {
        return soSaida;
    }

    public void setSoSaida(boolean soSaida) {
        this.soSaida = soSaida;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Integer getSchClassID() {
        return schClassID;
    }

    public void setSchClassID(Integer schClassID) {
        this.schClassID = schClassID;
    }

    public Timestamp getInicioJornadaL() {
        return inicioJornadaL;
    }

    public void setInicioJornadaL(Timestamp inicioJornadaL) {
        this.inicioJornadaL = inicioJornadaL;
    }

    public Timestamp getTerminioJornadaL() {
        return terminioJornadaL;
    }

    public void setTerminioJornadaL(Timestamp terminioJornadaL) {
        this.terminioJornadaL = terminioJornadaL;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getLegendaJornada() {
        return legendaJornada;
    }

    public void setLegendaJornada(String legendaJornada) {
        this.legendaJornada = legendaJornada;
    }

    public Timestamp getFimDescanso1() {
        return fimDescanso1;
    }

    public void setFimDescanso1(Timestamp fimDescanso1) {
        this.fimDescanso1 = fimDescanso1;
    }

    public Timestamp getFimDescanso2() {
        return fimDescanso2;
    }

    public void setFimDescanso2(Timestamp fimDescanso2) {
        this.fimDescanso2 = fimDescanso2;
    }

    public Timestamp getInicioDescanso1() {
        return inicioDescanso1;
    }

    public void setInicioDescanso1(Timestamp inicioDescanso1) {
        this.inicioDescanso1 = inicioDescanso1;
    }

    public Timestamp getInicioDescanso2() {
        return inicioDescanso2;
    }

    public void setInicioDescanso2(Timestamp inicioDescanso2) {
        this.inicioDescanso2 = inicioDescanso2;
    }

    public Boolean getIsOvertime() {
        return isOvertime;
    }

    public void setIsOvertime(Boolean isOvertime) {
        this.isOvertime = isOvertime;
    }

    public Integer getToleranciaDescanso() {
        return toleranciaDescanso;
    }

    public void setToleranciaDescanso(Integer toleranciaDescanso) {
        this.toleranciaDescanso = toleranciaDescanso;
    }

    public Boolean getDeduzirDescanco1() {
        return deduzirDescanco1;
    }

    public void setDeduzirDescanco1(Boolean deduzirDescanco1) {
        this.deduzirDescanco1 = deduzirDescanco1;
    }

    public Boolean getDeduzirDescanco2() {
        return deduzirDescanco2;
    }

    public void setDeduzirDescanco2(Boolean deduzirDescanco2) {
        this.deduzirDescanco2 = deduzirDescanco2;
    }

    public Boolean isIsDeslocamento() {
        return isDeslocamento;
    }

    public void setIsDeslocamento(Boolean isDeslocamento) {
        this.isDeslocamento = isDeslocamento;
    }

    public Timestamp getInicioIntrajornada() {
        return inicioIntrajornada;
    }

    public void setInicioIntrajornada(Timestamp inicioIntrajornada) {
        this.inicioIntrajornada = inicioIntrajornada;
    }

    public Timestamp getFimIntrajornada() {
        return fimIntrajornada;
    }

    public void setFimIntrajornada(Timestamp fimIntrajornada) {
        this.fimIntrajornada = fimIntrajornada;
    }

    public Boolean getDeduzirIntrajornada() {
        return deduzirIntrajornada;
    }

    public void setDeduzirIntrajornada(Boolean deduzirIntrajornada) {
        this.deduzirIntrajornada = deduzirIntrajornada;
    }

    public Boolean getCombinarEntrada() {
        return combinarEntrada;
    }

    public void setCombinarEntrada(Boolean combinarEntrada) {
        this.combinarEntrada = combinarEntrada;
    }

    public Boolean getCombinarSaida() {
        return combinarSaida;
    }

    public void setCombinarSaida(Boolean combinarSaida) {
        this.combinarSaida = combinarSaida;
    }

    public Boolean isFolga() {
        return folga;
    }

    public void setFloga(Boolean folga) {
        this.folga = folga;
    }

}
