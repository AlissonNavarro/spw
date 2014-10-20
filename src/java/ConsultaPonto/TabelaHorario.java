/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ConsultaPonto;

import java.io.Serializable;

/**
 *
 * @author ppccardoso
 */

public class TabelaHorario implements Serializable {

        private String diaSemana;
        //String que representão marcações
        private String entrada1;
        private String saida1;
        private String entrada2;
        private String saida2;
        private String entrada3;
        private String faixa1Desc1Entrada;
        private String faixa1Desc1Saida;
        private String faixa1Desc2Entrada;
        private String faixa1Desc2Saida;
        private String faixa2Desc1Entrada;
        private String faixa2Desc1Saida;
        private String faixa2Desc2Entrada;
        private String faixa2Desc2Saida;
        private String interJornada1Entrada;
        private String interJornada1Saida;
        private String interJornada2Entrada;
        private String interJornada2Saida;
        //String que representão faixa de exibição da tela
        private String faixaEntrada1;
        private String faixaSaida1;
        private String faixaEntrada2;
        private String faixaSaida2;
        private String faixaEntrada3;
        private String faixa1Descanso1;
        private String faixa1Interjornada;
        private String faixa1Descanso2;
        private String faixa2Descanso1;
        private String faixa2Interjornada;
        private String faixa2Descanso2;

        public String getInterJornada1Entrada() {
            return interJornada1Entrada;
        }

        public void setInterJornada1Entrada(String interJornada1Entrada) {
            this.interJornada1Entrada = interJornada1Entrada;
        }

        public String getInterJornada1Saida() {
            return interJornada1Saida;
        }

        public void setInterJornada1Saida(String interJornada1Saida) {
            this.interJornada1Saida = interJornada1Saida;
        }

        public String getInterJornada2Entrada() {
            return interJornada2Entrada;
        }

        public void setInterJornada2Entrada(String interJornada2Entrada) {
            this.interJornada2Entrada = interJornada2Entrada;
        }

        public String getInterJornada2Saida() {
            return interJornada2Saida;
        }

        public void setInterJornada2Saida(String interJornada2Saida) {
            this.interJornada2Saida = interJornada2Saida;
        }

        public String getFaixa1Descanso1() {
            faixa1Descanso1 = "De " + faixa1Desc1Entrada + " às " + faixa1Desc1Saida;
            return faixa1Descanso1;
        }

        public void setFaixa1Descanso1(String faixa1Descanso1) {
            this.faixa1Descanso1 = faixa1Descanso1;
        }

        public String getFaixa1Interjornada() {
            faixa1Interjornada = "De " + interJornada1Entrada + " às " + interJornada1Saida;
            return faixa1Interjornada;
        }

        public void setFaixa1Interjornada(String faixa1Interjornada) {
            this.faixa1Interjornada = faixa1Interjornada;
        }

        public String getFaixa1Descanso2() {
            faixa1Descanso2 = "De " + faixa1Desc2Entrada + " às " + faixa1Desc2Saida;
            return faixa1Descanso2;
        }

        public void setFaixa1Descanso2(String faixa1Descanso2) {
            this.faixa1Descanso2 = faixa1Descanso2;
        }

        public String getFaixa2Descanso1() {
            faixa2Descanso1 = "De " + faixa2Desc1Entrada + " às " + faixa2Desc1Saida;
            return faixa2Descanso1;
        }

        public void setFaixa2Descanso1(String faixa2Descanso1) {
            this.faixa2Descanso1 = faixa2Descanso1;
        }

        public String getFaixa2Interjornada() {
            faixa2Interjornada = "De " + interJornada2Entrada + " às " + interJornada2Saida;
            return faixa2Interjornada;
        }

        public void setFaixa2Interjornada(String faixa2Interjornada) {
            this.faixa2Interjornada = faixa2Interjornada;
        }

        public String getFaixa2Descanso2() {
            faixa2Descanso2 = "De " + faixa2Desc2Entrada + " às " + faixa2Desc2Saida;
            return faixa2Descanso2;
        }

        public void setFaixa2Descanso2(String faixa2Descanso2) {
            this.faixa2Descanso2 = faixa2Descanso2;
        }

        public String getFaixa1Desc1Entrada() {
            return faixa1Desc1Entrada;
        }

        public void setFaixa1Desc1Entrada(String faixa1Desc1Entrada) {
            this.faixa1Desc1Entrada = faixa1Desc1Entrada;
        }

        public String getFaixa1Desc1Saida() {
            return faixa1Desc1Saida;
        }

        public void setFaixa1Desc1Saida(String faixa1Desc1Saida) {
            this.faixa1Desc1Saida = faixa1Desc1Saida;
        }

        public String getFaixa1Desc2Entrada() {
            return faixa1Desc2Entrada;
        }

        public void setFaixa1Desc2Entrada(String faixa1Desc2Entrada) {
            this.faixa1Desc2Entrada = faixa1Desc2Entrada;
        }

        public String getFaixa1Desc2Saida() {
            return faixa1Desc2Saida;
        }

        public void setFaixa1Desc2Saida(String faixa1Desc2Saida) {
            this.faixa1Desc2Saida = faixa1Desc2Saida;
        }

        public String getFaixa2Desc1Entrada() {
            return faixa2Desc1Entrada;
        }

        public void setFaixa2Desc1Entrada(String faixa2Desc1Entrada) {
            this.faixa2Desc1Entrada = faixa2Desc1Entrada;
        }

        public String getFaixa2Desc1Saida() {
            return faixa2Desc1Saida;
        }

        public void setFaixa2Desc1Saida(String faixa2Desc1Saida) {
            this.faixa2Desc1Saida = faixa2Desc1Saida;
        }

        public String getFaixa2Desc2Entrada() {
            return faixa2Desc2Entrada;
        }

        public void setFaixa2Desc2Entrada(String faixa2Desc2Entrada) {
            this.faixa2Desc2Entrada = faixa2Desc2Entrada;
        }

        public String getFaixa2Desc2Saida() {
            return faixa2Desc2Saida;
        }

        public void setFaixa2Desc2Saida(String faixa2Desc2Saida) {
            this.faixa2Desc2Saida = faixa2Desc2Saida;
        }

        public String getEntrada3() {
            return entrada3;
        }

        public void setEntrada3(String entrada3) {
            this.entrada3 = entrada3;
        }

        public String getFaixaEntrada3() {
            return faixaEntrada3;
        }

        public void setFaixaEntrada3(String faixaEntrada3) {
            this.faixaEntrada3 = faixaEntrada3;
        }

        public String getDiaSemana() {
            return diaSemana;
        }

        public void setDiaSemana(String diaSemana) {
            this.diaSemana = diaSemana;
        }

        public String getEntrada1() {
            return entrada1;
        }

        public void setEntrada1(String entrada1) {
            this.entrada1 = entrada1;
        }

        public String getEntrada2() {
            return entrada2;
        }

        public void setEntrada2(String entrada2) {
            this.entrada2 = entrada2;
        }

        public String getSaida1() {
            return saida1;
        }

        public void setSaida1(String saida1) {
            this.saida1 = saida1;
        }

        public String getSaida2() {
            return saida2;
        }

        public void setSaida2(String saida2) {
            this.saida2 = saida2;
        }

        public String getFaixaEntrada1() {
            return faixaEntrada1;
        }

        public void setFaixaEntrada1(String faixaEntrada1) {
            this.faixaEntrada1 = faixaEntrada1;
        }

        public String getFaixaEntrada2() {
            return faixaEntrada2;
        }

        public void setFaixaEntrada2(String faixaEntrada2) {
            this.faixaEntrada2 = faixaEntrada2;
        }

        public String getFaixaSaida1() {
            return faixaSaida1;
        }

        public void setFaixaSaida1(String faixaSaida1) {
            this.faixaSaida1 = faixaSaida1;
        }

        public String getFaixaSaida2() {
            return faixaSaida2;
        }

        public void setFaixaSaida2(String faixaSaida2) {
            this.faixaSaida2 = faixaSaida2;
        }
    }
