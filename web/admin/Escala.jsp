<%-- 
    Document   : consultaFrequenciaAdmin
    Created on : 21/12/2009, 17:08:40
    Author     : amsgama
--%>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<%@page import="javax.faces.context.FacesContext" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" >
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" >
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" >
        <script type="text/javascript" src="../javaScript/jquery.js"></script>
        <title>Consulta Administrador</title>
        <script language="JavaScript" type="text/javascript">
            var $j = $.noConflict();

            function showDuty24() {
                $j(".duty24").show();
                $j(".duty12").show();
                $j(".dutyCallCenter").hide();
                $j(".type").hide();
            }

            function showDuty12() {
                $j(".duty12").show();
                $j(".duty24").hide();
                $j(".dutyCallCenter").hide();
                $j(".type").hide();
            }

            function showDutyCallCenter() {
                $j(".duty12").show();
                $j(".duty24").show();
                $j(".dutyCallCenter").show();
                $j(".type").hide();
            }

            function hideDuty() {
                $j(".duty12").hide();
                $j(".duty24").hide();
                $j(".dutyCallCenter").hide();
                $j(".type").hide();
            }


            /*$(document).ready(function() {
             $(".combinarEntrada").change(function() {
             alert( "Handler for .change() called." );
             //var saida = $("#editSaiDesc1");
             //alert("valor of " + saida.id + ": " + saida.value);
             });
             });*/

            /*function myFunction() {
             alert(startou);
             var x = $("#editSaiDesc1").;
             var text = "";
             for (var i = 0; i < x.length; i++) {
             text += "(" + x.elements[i].id + "-" + x.elements[i].value + "), ";
             alert("(" + x.elements[i].id + "-" + x.elements[i].value + "), ");
             }
             //var saida = $("#editSaiDesc1");
             //alert("valor of " + saida.id + ": " + saida.value);
             //alert(text);
             }*/

            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }

            function mascara_hora(id) {
                var myhora = '';
                myhora = myhora + document.getElementById(id).value;
                if (myhora.length == 2) {
                    myhora = myhora + ':';
                    document.getElementById(id).value = myhora;
                }
                if (myhora.length == 5) {
                    verifica_hora(id);
                }
            }

            function verifica_hora(id) {
                hrs = (document.getElementById(id).value.substring(0, 2));
                min = (document.getElementById(id).value.substring(3, 5));
                situacao = "";
                // verifica data e hora
                if ((hrs < 00) || (hrs > 23) || (min < 00) || (min > 59)) {
                    situacao = "falsa";
                }

                if (document.getElementById(id).value == "") {
                    situacao = "falsa";
                }

                if (situacao == "falsa") {
                    alert("Hora inválida!");
                    document.getElementById(id).focus();
                    document.getElementById(id).value = "";
                }
            }

            function mascara_horaBlur(id) {
                var myhora = '';
                myhora = myhora + document.getElementById(id).value;
                if (myhora.length == 2) {
                    myhora = myhora + ':';
                    document.getElementById(id).value = myhora;
                }

                if (myhora.length < 5 && myhora.length > 0) {
                    alert("Hora inválida!");
                    document.getElementById(id).focus();
                    document.getElementById(id).value = "";
                }

            }
        </SCRIPT>
        <style>
            .scrollAdd{
                overflow: auto;
                height:100%;
            }
        </style>
    </head>
    <body>
        <center>
            <f:view >
                <jsp:include page="../www/_top.jsp"/>
                <a4j:keepAlive beanName="horarioBean"/>
                <a4j:keepAlive beanName="afastamentoBean"/>

                <h:form id="f_messagens" prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:panelGrid columns="1">
                    <center>
                        <rich:tabPanel switchType="client"  width="965" selectedTab="#{jornadaCadastroBean.abaCorrente}" >
                            <rich:tab id="sub31" label="Horários" rendered="#{usuarioBean.perfil.horarios== true}">
                                <a4j:support event="ontabenter" action="#{jornadaCadastroBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="sub31"/>
                                </a4j:support>
                                <br>
                                <h:form id="formHorarioList">
                                    <center>            
                                        <a4j:commandLink id="link" action="#{horarioBean.showAdicionarNovoHorario}" 
                                                         oncomplete="javascript:#{rich:component('addmodalPanel')}.show();"
                                                         reRender="paneladdHorario, horarioPanel">
                                            <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                        </a4j:commandLink>
                                        <br>
                                        Adicionar
                                    </center>
                                    <br>


                                    <br>
                                    <center>
                                        <h:panelGrid id="panelGridHorario" columns="1">
                                            <rich:dataTable id="horarioList"
                                                            value="#{horarioBean.horarioList}"
                                                            var="linha"
                                                            rowClasses="zebra1,zebra2">
                                                <f:facet name="header">
                                                    <h:outputText value="LISTA DE HORÁRIOS"/>
                                                </f:facet>
                                                <rich:column sortBy="#{linha.horario_id}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="COD"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.horario_id}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.nome}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.nome}"/>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="LEGENDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.legenda}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.entrada}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="ENTRADA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.entrada}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.inicioFaixaEntrada}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="INÍCIO ENTRADA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.inicioFaixaEntrada}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>                                                    
                                                </rich:column>
                                                <rich:column sortBy="#{linha.fimFaixaEntrada}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="FIM ENTRADA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.fimFaixaEntrada}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.saida}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="SAIDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.saida}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.inicioFaixaSaida}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="INÍCIO SAÍDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.inicioFaixaSaida}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.fimFaixaSaida}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="FIM SAÍDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.fimFaixaSaida}">
                                                        <f:convertDateTime type="date" pattern="HH:mm"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.horarioDescanso}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="DESCANSO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.horarioDescanso}"/>
                                                </rich:column>
                                                <rich:column>
                                                    <f:facet name="header">
                                                        <h:outputText value="EDITAR"/>
                                                    </f:facet>
                                                    <center>
                                                        <h:outputLink  value="#" id="link">
                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                            <rich:componentControl for="editmodalPanel" attachTo="link" operation="show" event="onclick"/>
                                                            <a4j:support action="#{horarioBean.showEditarNovoHorario}"
                                                                         event="onclick"
                                                                         reRender="paneleditHorario">
                                                                <f:param name="horario_id_editar" value="#{linha.horario_id}"/>
                                                            </a4j:support>
                                                        </h:outputLink>
                                                    </center>
                                                </rich:column>
                                                <rich:column>
                                                    <f:facet name="header">
                                                        <h:outputText value="DELETAR"/>
                                                    </f:facet>
                                                    <center>
                                                        <a4j:commandButton id="btDeleteHorario"
                                                                           value="Deletar"
                                                                           image="../images/delete.png"
                                                                           reRender="panelGridHorario"
                                                                           ajaxSingle="true"
                                                                           action="#{horarioBean.deletarLinha}">
                                                            <f:param name="horario_id_delete" value="#{linha.horario_id}"/>
                                                        </a4j:commandButton>
                                                    </center>
                                                </rich:column>
                                            </rich:dataTable>
                                            <rich:datascroller
                                                for="horarioList"
                                                ajaxSingle="true"
                                                renderIfSinglePage="false">
                                            </rich:datascroller>
                                        </h:panelGrid>
                                    </center>
                                </h:form>

                            </rich:tab>
                            <rich:tab id="tab7" label="Jornadas" rendered="#{usuarioBean.perfil.jornadas== true}">
                                <a4j:support event="ontabenter" action="#{jornadaCadastroBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab7"/>
                                </a4j:support>
                                <h:form id="formJornadasList">

                                    <br>
                                    <center>
                                        <a4j:region id="regionJornada">
                                            <br>

                                            <a4j:status id="progressoEmAberto"  for="regionJornada"
                                                        onstart="Richfaces.showModalPanel('panelJornadaStatus');"
                                                        onstop="#{rich:component('panelJornadaStatus')}.hide()"/>
                                            <rich:modalPanel id="panelJornadaStatus" autosized="true" >
                                                <h:panelGrid columns="3">
                                                    <h:graphicImage url="../images/load.gif"/>
                                                    <rich:spacer width="8"/>
                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                </h:panelGrid>
                                            </rich:modalPanel>

                                            <h:panelGroup>
                                                <rich:extendedDataTable
                                                    selection="#{jornadaCadastroBean.selecionadoJornadaCadastro}"
                                                    enableContextMenu="false"
                                                    id="jornadaslist"
                                                    value="#{jornadaCadastroBean.jornadaCadastroList}" var="jornadaCadastro"
                                                    height="400px" width="70%">
                                                    <a4j:support event="onRowClick" action="#{jornadaCadastroBean.showEditarJornada}"
                                                                 reRender="editJornadaOutputPanel,addTurnoOutputPanel">
                                                        <f:param name="id_jornada" value="#{jornadaCadastro.id}"/>
                                                    </a4j:support>
                                                    <rich:column sortable="false" id="col_1" width="44%">
                                                        <f:facet name="header">
                                                            <h:outputText value="Nome da Jornada" />
                                                        </f:facet>
                                                        <h:outputText value="#{jornadaCadastro.nome}" style="#{jornadaCadastro.css}"/>
                                                    </rich:column>
                                                    <rich:column sortable="false" id="col_2" width="10%">
                                                        <f:facet name="header">
                                                            <h:outputText value="Início" id="datainicio"/>
                                                        </f:facet>
                                                        <h:outputText value="#{jornadaCadastro.dataInicio}" style="#{jornadaCadastro.css}">
                                                            <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                        </h:outputText>
                                                    </rich:column>
                                                    <rich:column sortable="false" width="8%" >
                                                        <f:facet name="header">
                                                            <h:outputText value="Ciclo" id="unidadeCiclos"/>
                                                        </f:facet>
                                                        <h:outputText value="#{jornadaCadastro.unidadeCiclos}" style="#{jornadaCadastro.css}"/>
                                                    </rich:column>
                                                    <rich:column sortable="false" width="8%">
                                                        <f:facet name="header">
                                                            <h:outputText value="Quant." id="quantidadeCiclos"/>
                                                        </f:facet>
                                                        <h:outputText value="#{jornadaCadastro.quantidadeCiclos}" style="#{jornadaCadastro.css}"/>
                                                    </rich:column>
                                                    <rich:column sortable="false" width="30%">
                                                        <f:facet name="header">
                                                            <h:outputText value="Responsável" />
                                                        </f:facet>
                                                        <h:outputText value="#{jornadaCadastro.nomeResponsavel}" style="#{jornadaCadastro.css}"/>
                                                    </rich:column>
                                                </rich:extendedDataTable>
                                                <h:outputText value="* Jornadas em vermelho indicam escalas com menos de 11 horas de intervalo" />
                                                <br>

                                                <h:panelGrid columns="4">
                                                    <h:panelGrid columns="1" style="text-align:center;float:center" >
                                                        <center>
                                                            <h:outputLink  value="#" id="linkAddJornada" style="float:center">
                                                                <h:graphicImage value="../images/add2.png" style="border:0"  />
                                                                <rich:componentControl for="addJornada" attachTo="linkAddJornada" operation="show" event="onclick"/>
                                                                <a4j:support action="#{jornadaCadastroBean.showAdicionarNovaJornada}"
                                                                             event="onclick">
                                                                </a4j:support>
                                                            </h:outputLink>
                                                            <h:outputText value="Jornada" styleClass="label" />
                                                        </center>
                                                    </h:panelGrid>

                                                    <h:panelGrid columns="1" style="text-align:center;float:center">
                                                        <a4j:commandButton image="../images/delete_24.png" action="#{jornadaCadastroBean.deletarJornada}" reRender="jornadaslist,editJornadaOutputPanel,editJornadaOutputPanel,addTurnoOutputPanel"
                                                                           onclick="javascript:if (!confirm('Deseja remover jornada?')) return false;"/>
                                                        <h:outputText value="Remover" styleClass="label"/>
                                                    </h:panelGrid>

                                                    <a4j:outputPanel id="editJornadaOutputPanel">
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     rendered="#{jornadaCadastroBean.id_jornada != null}">
                                                            <center>
                                                                <h:outputLink  value="#" id="linkEditJornada" style="float:center">
                                                                    <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                    <rich:componentControl for="editJornada" attachTo="linkEditJornada" operation="show" event="onclick"/>
                                                                    <a4j:support event="onclick" reRender="editJornadaPanelGrid"/>
                                                                </h:outputLink>
                                                                <h:outputText value="Editar" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     rendered="#{jornadaCadastroBean.id_jornada == null}">
                                                            <center>
                                                                <h:graphicImage value="../images/edit_dent_fosco.png" style="border:0"/>
                                                                <h:outputText value="Editar" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </a4j:outputPanel>

                                                    <a4j:outputPanel id="addTurnoOutputPanel">
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     rendered="#{jornadaCadastroBean.id_jornada != null}">
                                                            <center>
                                                                <%--<h:outputLink  value="#" id="linkAddTurno" style="float:center">
                                                                <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                <rich:componentControl for="addTurnoPanel" attachTo="linkAddTurno" operation="show" event="onclick"/>
                                                                <a4j:support event="onclick" reRender="horariosExtendList,diasExtendList,addTurnoPanelMessage" action="#{jornadaCadastroBean.showHorariosXJornada}"/>

                                                </h:outputLink>--%>
                                                                <h:commandLink styleClass="link" action="#{jornadaCadastroBean.showHorariosXJornada}">
                                                                    <a4j:support immediate="true" event="onclick" reRender="addJornadaModalPanel, diasExtendPanelGroup"/>
                                                                    <h:graphicImage  value="../images/add_verde_24.png" style="border:0"/>
                                                                </h:commandLink>

                                                                <h:outputText value="Turno" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     rendered="#{jornadaCadastroBean.id_jornada == null}">
                                                            <center>
                                                                <h:graphicImage value="../images/add_verde_24_fosco.png" style="border:0"/>
                                                                <h:outputText value="Turno" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </a4j:outputPanel>
                                                </h:panelGrid>
                                            </h:panelGroup>
                                        </a4j:region>
                                    </center>
                                </h:form>
                            </rich:tab>

                            <rich:tab id="tab9" label="Cronogramas" rendered="#{usuarioBean.perfil.cronogramas== true}">
                                <a4j:support event="ontabenter" action="#{jornadaCadastroBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab9"/>
                                </a4j:support>
                                <br>
                                <center>
                                    <rich:tabPanel switchType="client"  width="965">
                                        <rich:tab label="Individual">
                                            <center>
                                                <h:form id="formIndividualCronogramas">
                                                    <a4j:region id="CronogramasRegion">
                                                        <h:panelGrid columns="4" >
                                                            <h:outputText value="Departamento: " styleClass="label"/>
                                                            <rich:spacer width="12"/>
                                                            <h:outputText value="Funcionário: " styleClass="label"/>
                                                            <rich:spacer width="12"/>

                                                            <h:selectOneMenu id="departManuTAdmin" value="#{cadastroCronogramaBean.departamento}">
                                                                <f:selectItems value="#{cadastroCronogramaBean.departamentolist}"/>
                                                                <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionario}"
                                                                             reRender="funcionarioTAdmin,cronogramasGroupExterno, datasPanel"/>
                                                            </h:selectOneMenu>
                                                            <rich:spacer width="10"/>

                                                            <h:selectOneMenu id="funcionarioTAdmin" value="#{cadastroCronogramaBean.cod_funcionario}">
                                                                <f:selectItems value="#{cadastroCronogramaBean.funcionarioList}"/>
                                                                <a4j:support event="onchange"  action="#{cadastroCronogramaBean.consultaCronogramas}"
                                                                             reRender="formIndividualCronogramas, CronogramasRegion, cronogramasGroupExterno, 
                                                                             tabelaHorariosIntervalo, cronogramasList, datasPanel"/>
                                                            </h:selectOneMenu>

                                                            <h:outputLink id="filtroFuncionarioCronograma" value="#">
                                                                <center>
                                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                                </center>
                                                                <rich:componentControl for="filtroFuncionarioCronogramaModalPanel"
                                                                                       attachTo="filtroFuncionarioCronograma" operation="show" event="onclick"/>
                                                            </h:outputLink>

                                                            <h:panelGroup>
                                                                <h:selectBooleanCheckbox value="#{cadastroCronogramaBean.incluirSubSetores}">
                                                                    <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionario}"
                                                                                 reRender="funcionarioTAdmin"/>
                                                                </h:selectBooleanCheckbox>
                                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                            </h:panelGroup>

                                                            <a4j:status id="progressoEmAberto"  for="CronogramasRegion" onstart="Richfaces.showModalPanel('CronogramasRegionPanelStatus');"
                                                                        onstop="#{rich:component('CronogramasRegionPanelStatus')}.hide()"/>
                                                            <rich:modalPanel id="CronogramasRegionPanelStatus" autosized="true" >
                                                                <h:panelGrid columns="3">
                                                                    <h:graphicImage url="../images/load.gif" />
                                                                    <rich:spacer width="8"/>
                                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                                </h:panelGrid>
                                                            </rich:modalPanel>
                                                            <rich:modalPanel id="filtroFuncionarioCronogramaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                                                <f:facet name="header">
                                                                    <h:panelGroup>
                                                                        <h:outputText value="Filtrar funcionários"/>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                                <f:facet name="controls">
                                                                    <h:panelGroup>
                                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkCronograma"/>
                                                                        <rich:componentControl for="filtroFuncionarioCronogramaModalPanel" attachTo="hidelinkCronograma" operation="hide" event="onclick"/>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                                <center>
                                                                    <fieldset class="demo_fieldset" >
                                                                        <legend style="font-weight: bold;">Por Cargo</legend>
                                                                        <h:panelGroup>
                                                                            <h:selectOneMenu value="#{cadastroCronogramaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                                                <f:selectItems value="#{cadastroCronogramaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                                            </h:selectOneMenu>
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br>
                                                                    <fieldset>
                                                                        <legend style="font-weight: bold;">Por Regime</legend>
                                                                        <h:panelGroup >
                                                                            <h:selectOneRadio
                                                                                value="#{cadastroCronogramaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                                                <f:selectItems
                                                                                    value="#{cadastroCronogramaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                                            </h:selectOneRadio>
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br>
                                                                    <fieldset>
                                                                        <legend style="font-weight: bold;">Por Gestor</legend>
                                                                        <h:panelGroup>
                                                                            <h:selectOneRadio
                                                                                value="#{cadastroCronogramaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                                                <f:selectItems
                                                                                    value="#{cadastroCronogramaBean.gestorFiltroFuncionarioList}"/>
                                                                            </h:selectOneRadio> 
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br> <br>
                                                                    <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeCronograma"
                                                                                     action="#{cadastroCronogramaBean.consultaFuncionario}">
                                                                        <rich:componentControl for="filtroFuncionarioCronogramaModalPanel" attachTo="confirmarOpcaoFiltroRegimeCronograma"
                                                                                               operation="hide" event="onclick"/>
                                                                    </h:commandButton>
                                                                </center>
                                                            </rich:modalPanel>
                                                        </h:panelGrid>
                                                        <br>
                                                        <h:panelGroup id="cronogramasGroupExterno">
                                                            <rich:extendedDataTable
                                                                selection="#{cadastroCronogramaBean.cronogramaSelecionado}"
                                                                enableContextMenu="false"
                                                                rendered="#{not empty cadastroCronogramaBean.cronogramasList}"
                                                                id="cronogramasList"
                                                                value="#{cadastroCronogramaBean.cronogramasList}" var="cronograma"
                                                                width="350px"
                                                                height="100px">
                                                                <a4j:support event="onRowClick"  reRender="removeCronogramaOutputPanel, editCronogramaOutputPanel"/>
                                                                <f:facet name="header">
                                                                    <h:outputText value="Histórico de Jornadas" />
                                                                </f:facet> 
                                                                <rich:column sortable="false" id="nomeJornada" width="40%" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Nome da Jornada"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{cronograma.nomeJornada}" />
                                                                </rich:column>

                                                                <rich:column sortable="false" id="DataInicio" width="30%" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Início" />
                                                                    </f:facet>
                                                                    <h:outputText value="#{cronograma.inicio}" >
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                                <rich:column sortable="false" id="DataFim" width="30%" style="text-align:center">

                                                                    <f:facet name="header">
                                                                        <h:outputText value="Fim" />
                                                                    </f:facet>
                                                                    <h:outputText value="#{cronograma.fim}" >
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                            </rich:extendedDataTable>
                                                            <br>
                                                            <center>
                                                                <h:panelGrid columns="5" rendered="#{cadastroCronogramaBean.cod_funcionario != null
                                                                                                     &&cadastroCronogramaBean.cod_funcionario != -1}" >
                                                                    <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                 >
                                                                        <h:outputLink  value="#" id="linkAddJornadaUsuario" style="float:center" >
                                                                            <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                            <rich:componentControl for="addJornadaModalPanel" attachTo="linkAddJornadaUsuario" operation="show" event="onclick"/>
                                                                            <a4j:support event="onclick" action="#{cadastroCronogramaBean.showAdicionarJornada}" reRender="addJornadaGrid, JornadasNovasList, diasExtendPanelGroup"/>
                                                                        </h:outputLink>
                                                                        <h:outputText value= "Adicionar Jornada" styleClass="label"/>
                                                                    </h:panelGrid>
                                                                    <rich:spacer width="15"/>
                                                                    <a4j:outputPanel id="editCronogramaOutputPanel" >
                                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                     rendered="#{cadastroCronogramaBean.cronogramaSelecionado != null}">
                                                                            <h:outputLink  value="#" id="linkEditJornadaUsuario" style="float:center" >
                                                                                <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                                <rich:componentControl for="editJornadaPanel" attachTo="linkEditJornadaUsuario" operation="show" event="onclick"/>
                                                                                <a4j:support event="onclick" action="#{cadastroCronogramaBean.showEditarJornada}" reRender="editJornadaGrid"/>
                                                                            </h:outputLink>
                                                                            <h:outputText value= "Editar Jornada" styleClass="label"/>
                                                                        </h:panelGrid>
                                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                     rendered="#{cadastroCronogramaBean.cronogramaSelecionado == null}">
                                                                            <h:graphicImage value="../images/edit_dent_fosco.png" style="border:0"/>
                                                                            <h:outputText value="Editar Jornada" styleClass="label"/>
                                                                        </h:panelGrid>
                                                                    </a4j:outputPanel>
                                                                    <rich:spacer width="15"/>

                                                                    <a4j:outputPanel id="removeCronogramaOutputPanel" >
                                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                     rendered="#{cadastroCronogramaBean.cronogramaSelecionado != null}">
                                                                            <h:commandButton  value="Excluir" id="excluirJornada" image="../images/delete_24.png"
                                                                                              onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                              action="#{cadastroCronogramaBean.excluirJornada}"/>
                                                                            <a4j:support event="onclick"  action="#{cadastroCronogramaBean.geraDiasCronogramaList}"
                                                                                         reRender="tabelaHorariosIntervaloGrid,tabelaHorariosIntervalo"/>
                                                                            <h:outputText value="Excluir Jornada" styleClass="label"/>
                                                                        </h:panelGrid>
                                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                     rendered="#{cadastroCronogramaBean.cronogramaSelecionado == null}">
                                                                            <h:graphicImage value="../images/delete_transp_24.png" style="border:0"/>
                                                                            <a4j:support event="onclick"  action="#{cadastroCronogramaBean.geraDiasCronogramaList}"
                                                                                         reRender="tabelaHorariosIntervaloGrid,tabelaHorariosIntervalo"/>
                                                                            <h:outputText value="Excluir Jornada" styleClass="label"/>
                                                                        </h:panelGrid>
                                                                    </a4j:outputPanel>
                                                                </h:panelGrid>
                                                                <br>

                                                                <br>
                                                                <h:panelGrid id= "datasPanel" columns="8"
                                                                             rendered="#{cadastroCronogramaBean.cod_funcionario != null && cadastroCronogramaBean.cod_funcionario != -1}" >
                                                                    <h:outputText value="Data Inicial: " styleClass="label"/>
                                                                    <rich:spacer width="2"/>
                                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataInicio}">
                                                                        <a4j:support event="onchanged"   action="#{cadastroCronogramaBean.geraDiasCronogramaList}"
                                                                                     reRender="tabelaHorariosIntervaloGrid,tabelaHorariosIntervalo"/>
                                                                    </rich:calendar>
                                                                    <rich:spacer width="12"/>
                                                                    <h:outputText value="Data Final:" styleClass="label"/>
                                                                    <rich:spacer width="2"/>
                                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataFim}">
                                                                        <a4j:support event="onchanged"  action="#{cadastroCronogramaBean.geraDiasCronogramaList}"
                                                                                     reRender="tabelaHorariosIntervaloGrid,tabelaHorariosIntervalo"/>
                                                                    </rich:calendar>
                                                                    <h:panelGroup>
                                                                        <rich:spacer width="100"/>
                                                                        <h:commandButton  value="Imprimir"  image="../images/impressora.png"
                                                                                          action="#{cadastroCronogramaBean.imprimir}"/>
                                                                    </h:panelGroup>
                                                                </h:panelGrid>
                                                                <h:panelGroup id="tabelaHorariosIntervaloGrid"
                                                                              rendered="#{cadastroCronogramaBean.cod_funcionario != null&&cadastroCronogramaBean.cod_funcionario != -1}">
                                                                    <rich:extendedDataTable
                                                                        selectionMode= "none"
                                                                        enableContextMenu="false"
                                                                        id="tabelaHorariosIntervalo"
                                                                        value="#{cadastroCronogramaBean.diasCronogramaList}" var="diaCronograma"
                                                                        width="50%"
                                                                        height="650px">

                                                                        <rich:column sortable="false" id="diaDoCronograma" width="15%" style="text-align:center">

                                                                            <f:facet name="header">
                                                                                <h:outputText value="Dia" />
                                                                            </f:facet>
                                                                            <h:outputText value="#{diaCronograma.diaString}" />
                                                                        </rich:column>
                                                                        <rich:column sortable="false" id="turnosAssociadosCronograma" width="85%"
                                                                                     style="text-align:center;float:center">
                                                                            <f:facet name="header">
                                                                                <h:outputText value="Horários" />
                                                                            </f:facet>
                                                                            <h:outputText value="#{diaCronograma.horarioStrList.get(0)}" style="#{diaCronograma.cssHorario}" rendered="#{diaCronograma.horarioStrList.size() >= 1}"/>
                                                                            <h:outputText value="#{diaCronograma.horarioStrList.get(1)}" style="#{diaCronograma.cssHorario}" rendered="#{diaCronograma.horarioStrList.size() >= 2}"/>
                                                                        </rich:column>

                                                                    </rich:extendedDataTable>
                                                                    <h:outputText style="color: red" value="* Horários em vermelho representam dias com deslocamento temporário" />
                                                                    <br>
                                                                    <h:outputText style="color: blue" value="* Horários em azul representam dias com folga compensatória" />
                                                                    <br>
                                                                    <h:outputText style="color: #B0B0B0" value="* Horários em cinza representam dias sem horários que foram removidos" />                                                                    
                                                                    <br>
                                                                    <h:outputText style="color: green" value="* Horários como '[08:00]' representam que há hora extra" />
                                                                    <br><br>
                                                                    <h:panelGrid columns="3" style="text-align:center;float:center"
                                                                                 rendered="#{not empty cadastroCronogramaBean.diasCronogramaList}">
                                                                        <h:panelGroup>
                                                                            <%--<h:outputLink  value="#" id="linkAddTurnoTemp" style="float:center" >
                                                                                <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                                <rich:componentControl for="addTurnoPanelTemp" attachTo="linkAddTurnoTemp" operation="show" event="onclick"/>
                                                                                <a4j:support event="onclick" reRender="horariosExtendListTemp,tabelaDeslocTemp"/>
                                                                            </h:outputLink>--%>
                                                                            <h:commandLink styleClass="link" action="navegarDeslocamentoTemporario">
                                                                                <h:graphicImage  value="../images/add_verde_24.png" style="border:0"/>
                                                                            </h:commandLink>
                                                                            <br>
                                                                            <h:outputText value= "Adicionar deslocamentos temporários" styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <rich:spacer width="15"/>
                                                                        <h:panelGroup>
                                                                            <h:commandButton  value="Excluir" id="excluirTurnoTemp" image="../images/delete_24.png"
                                                                                              onclick="javascript:if (!confirm('Realmente deseja excluir os deslocamentos temporários do período?')) return false;"
                                                                                              action="#{cadastroCronogramaBean.excluirDeslocTempTurnos}"/>
                                                                            <br>
                                                                            <h:outputText value="Excluir deslocamentos temporários" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                    </h:panelGrid>
                                                                </h:panelGroup>
                                                            </center>  
                                                        </h:panelGroup>
                                                    </a4j:region>
                                                </h:form>
                                            </center>
                                        </rich:tab>

                                        <rich:tab label="Por Grupo" ontabenter="#{cadastroCronogramaBean.resetPage()}" reRender="CronogramasGrupoRegion">
                                            <center>
                                                <a4j:region id="CronogramasGrupoRegion">
                                                    <h:panelGrid columns="1" >
                                                        <h:form id="formCronogramaGrupo">
                                                            <h:outputText value="Departamento: " styleClass="label"/>
                                                            <rich:spacer width="12"/>                                                            
                                                            <h:panelGroup>
                                                                <h:panelGrid columns="3" >
                                                                    <h:selectOneMenu id="departManuTAdminGrupo" value="#{cadastroCronogramaBean.departamento}">
                                                                        <f:selectItems value="#{cadastroCronogramaBean.departamentolist}"/>
                                                                        <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionarioGrupo}"
                                                                                     reRender="adicionarJornadaGrupoButton,jornadasPanelGroup"/>
                                                                    </h:selectOneMenu>
                                                                    <rich:spacer width="10"/>

                                                                    <h:outputLink id="filtroFuncionarioCronogramaGrupo" value="#">
                                                                        <center>
                                                                            <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                                        </center>
                                                                        <rich:componentControl for="filtroFuncionarioCronogramaGrupoModalPanel"
                                                                                               attachTo="filtroFuncionarioCronogramaGrupo" operation="show" event="onclick"/>
                                                                    </h:outputLink>
                                                                </h:panelGrid>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <h:selectBooleanCheckbox value="#{cadastroCronogramaBean.incluirSubSetores}">
                                                                    <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionarioGrupo}"
                                                                                 reRender="adicionarJornadaGrupoButton,jornadasPanelGroup"/>
                                                                </h:selectBooleanCheckbox>
                                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                            </h:panelGroup>

                                                            <a4j:status id="progressoEmAbertoGrupo"
                                                                        for="CronogramasGrupoRegion" onstart="Richfaces.showModalPanel('CronogramasGrupoRegionPanelStatus');"
                                                                        onstop="#{rich:component('CronogramasGrupoRegionPanelStatus')}.hide()"/>

                                                            <h:panelGrid id= "addJornadaGrupoDatasPanel" columns="3" style="text-align:center">
                                                                <h:panelGroup style="text-align:center" >
                                                                    <h:outputText value="Data Inicial " styleClass="label"/><br>
                                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataInicioJornada}"/>
                                                                </h:panelGroup>
                                                                <rich:spacer width="20"/>
                                                                <h:panelGroup style="text-align:center" >
                                                                    <h:outputText value="Data Final" styleClass="label"/><br>
                                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataFimJornada}"/>
                                                                </h:panelGroup>
                                                            </h:panelGrid>
                                                            <br>

                                                            <rich:modalPanel id="CronogramasGrupoRegionPanelStatus" autosized="true" >
                                                                <h:panelGrid columns="3">
                                                                    <h:graphicImage url="../images/load.gif" />
                                                                    <rich:spacer width="8"/>
                                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                                </h:panelGrid>
                                                            </rich:modalPanel>
                                                            <rich:modalPanel id="filtroFuncionarioCronogramaGrupoModalPanel" width="750"
                                                                             height="250"  style="text-align:center;float:center;" >
                                                                <f:facet name="header">
                                                                    <h:panelGroup>
                                                                        <h:outputText value="Filtrar funcionários"></h:outputText>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                                <f:facet name="controls">
                                                                    <h:panelGroup>
                                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkCronogramaGrupo"/>
                                                                        <rich:componentControl for="filtroFuncionarioCronogramaGrupoModalPanel" attachTo="hidelinkCronogramaGrupo" operation="hide" event="onclick"/>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                                <center>
                                                                    <fieldset class="demo_fieldset" >
                                                                        <legend style="font-weight: bold;">Por Cargo</legend>
                                                                        <h:panelGroup>
                                                                            <h:selectOneMenu value="#{cadastroCronogramaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                                                <f:selectItems value="#{cadastroCronogramaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                                            </h:selectOneMenu>
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br>
                                                                    <!--<fieldset class="demo_fieldset" >-->
                                                                    <fieldset>
                                                                        <legend style="font-weight: bold;">Por Regime</legend>
                                                                        <h:panelGroup >
                                                                            <h:selectOneRadio
                                                                                value="#{cadastroCronogramaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                                                <f:selectItems
                                                                                    value="#{cadastroCronogramaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                                            </h:selectOneRadio>
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br>
                                                                    <!--<fieldset class="demo_fieldset" >-->
                                                                    <fieldset>
                                                                        <legend style="font-weight: bold;">Por Gestor</legend>
                                                                        <h:panelGroup>
                                                                            <h:selectOneRadio
                                                                                value="#{cadastroCronogramaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                                                <f:selectItems
                                                                                    value="#{cadastroCronogramaBean.gestorFiltroFuncionarioList}"/>
                                                                            </h:selectOneRadio>
                                                                        </h:panelGroup>
                                                                    </fieldset>
                                                                    <br> <br>
                                                                    <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeCronogramaGrupo"
                                                                                     action="#{cadastroCronogramaBean.consultaFuncionarioGrupo}">
                                                                        <rich:componentControl for="filtroFuncionarioCronogramaGrupoModalPanel" attachTo="confirmarOpcaoFiltroRegimeCronogramaGrupo"
                                                                                               operation="hide" event="onclick"/>
                                                                    </h:commandButton>
                                                                </center>
                                                            </rich:modalPanel>
                                                        </h:form>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:form id="formAddJornadaGrupo">
                                                        <h:panelGrid id="adicionarJornadaGrupoButton" columns="3" >

                                                            <h:panelGrid columns="1"
                                                                         style="text-align:center;float:center"
                                                                         rendered="#{cadastroCronogramaBean.departamento != -1}">
                                                                <h:outputLink  value="#" id="linkAddJornadaGrupoUsuario" style="float:center" >
                                                                    <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                    <rich:componentControl for="addJornadaGrupoPanel" attachTo="linkAddJornadaGrupoUsuario" operation="show" event="onclick"/>
                                                                    <a4j:support event="onclick" action="#{cadastroCronogramaBean.showAdicionarJornada}"/>
                                                                </h:outputLink>
                                                                <h:outputText value= "Adicionar Jornada" styleClass="label"/>
                                                            </h:panelGrid>
                                                            <rich:spacer width="10"/>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center"  rendered="#{not empty cadastroCronogramaBean.jornadaList}">
                                                                <h:commandButton  value="Excluir" image="../images/delete_24.png"

                                                                                  onclick="javascript:if (!confirm('Realmente deseja excluir as jornadas deste departamento?')) return false;"
                                                                                  action="#{cadastroCronogramaBean.deletarTodasJornadas}"/>
                                                                <h:outputText value="Excluir Jornada" styleClass="label"/>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center" rendered="#{empty cadastroCronogramaBean.jornadaList}">
                                                                <h:commandButton  value="Excluir" image="../images/delete_transp_24.png"

                                                                                  onclick="javascript:if (!confirm('Realmente deseja excluir as jornadas deste departamento?')) return false;"
                                                                                  action="#{cadastroCronogramaBean.deletarTodasJornadas}"/>
                                                                <h:outputText value="Excluir Jornada" styleClass="label"/>
                                                            </h:panelGrid>
                                                        </h:panelGrid >

                                                        <rich:modalPanel id="addJornadaGrupoPanel" width="300" height="230" autosized="true" styleClass="center" >
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <h:outputText value="Adicionar Jornada"></h:outputText>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <f:facet name="controls">
                                                                <h:panelGroup>
                                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkJornadaGrupoTemp"/>
                                                                    <rich:componentControl for="addJornadaGrupoPanel" attachTo="hidelinkJornadaGrupoTemp" operation="hide" event="onclick"/>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <center>
                                                                <h:panelGrid id="addJornadaGrupoGrid" columns="1" style="text-align:center">
                                                                    <center>
                                                                        <h:outputText value="Escolha a jornada a ser adicionada " styleClass="label"/>
                                                                        <h:panelGroup style="text-align:center" >
                                                                            <h:selectOneMenu  value="#{cadastroCronogramaBean.jornadaSelecionada}" >
                                                                                <f:selectItems value="#{cadastroCronogramaBean.jornadasList}"/>
                                                                                <%-- <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionario}" reRender="funcionarioTAdmin"/>--%>
                                                                            </h:selectOneMenu>
                                                                        </h:panelGroup>
                                                                        <br>


                                                                        <h:commandButton   value="Salvar" id="salvarAddJornadaGrupoTemp" image="../images/salvar_48.png"
                                                                                           action="#{cadastroCronogramaBean.salvarNovaJornadaGroup}">
                                                                            <rich:componentControl for="addJornadaGrupoPanel" attachTo="salvarAddJornadaGrupoTemp" operation="hide"
                                                                                                   event="onclick"/>
                                                                        </h:commandButton>
                                                                        <h:outputText value=" Salvar" styleClass="italico"/>
                                                                    </center>
                                                                </h:panelGrid>
                                                            </center>
                                                        </rich:modalPanel>
                                                    </h:form>
                                                    <rich:spacer width="15"/>
                                                    <br> <br>
                                                    <h:form id="formJornadasPanel">
                                                        <h:panelGroup id="jornadasPanelGroup">
                                                            <rich:dataTable
                                                                id ="jornadasDataTable"
                                                                value="#{cadastroCronogramaBean.jornadaList}" var="jornada"
                                                                rowClasses="zebra1,zebra2"
                                                                rows="25"
                                                                rendered="#{not empty cadastroCronogramaBean.jornadaList}">
                                                                <f:facet name="header">
                                                                    <h:outputText value="LISTA DE JORNADAS"/>
                                                                </f:facet>                                                                
                                                                <rich:column  style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Matrícula"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{jornada.matricula}" />
                                                                </rich:column >
                                                                <rich:column sortBy="#{jornada.nomeFuncionario}" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Nome"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{jornada.nomeFuncionario}" />
                                                                </rich:column >                                                               
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Jornada"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{jornada.nomeJornada}" />
                                                                </rich:column>
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Início"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{jornada.inicioNovo}" >
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Fim"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{jornada.fimNovo}" >
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="Excluir"/>
                                                                    </f:facet>
                                                                    <center>
                                                                        <a4j:commandButton image="../images/delete.png"
                                                                                           onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                           action="#{cadastroCronogramaBean.deletarJornada}"
                                                                                           reRender="jornadasPanelGroup">
                                                                            <f:param name="cod_funcionario" value="#{jornada.cod_funcionario}"/>
                                                                            <f:param name="cod_jornada" value="#{jornada.cod_jornada}"/>
                                                                            <f:param name="dataInicio" value="#{jornada.inicioNovo}}"/>
                                                                        </a4j:commandButton>
                                                                    </center>
                                                                </rich:column>
                                                            </rich:dataTable>
                                                            <rich:datascroller
                                                                for="jornadasDataTable"
                                                                ajaxSingle="true"
                                                                renderIfSinglePage="false">
                                                            </rich:datascroller>
                                                        </h:panelGroup>
                                                    </h:form>
                                                </a4j:region>
                                            </center>
                                        </rich:tab>
                                    </rich:tabPanel>
                                </rich:tab>

                                <rich:tab id="sub71" label="Afastamento" style="text-align:center;float:center" rendered="#{usuarioBean.perfil.afastamento == true}">
                                    <a4j:support event="ontabenter" action="#{jornadaCadastroBean.setAba}" reRender="f_messagens">
                                        <a4j:actionparam name="tab" value="sub71"/>
                                    </a4j:support>
                                    <center>
                                        <br>
                                        <h:panelGrid columnClasses="gridContent" style="text-align:center;float:center">
                                            <center>
                                                <a4j:region id="afastamentoRegion">
                                                    <h:form id="formAfastamento">
                                                        <center>
                                                            <h:panelGrid id="Entradas" columns="8" style="text-align:center;float:center">
                                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                                <rich:spacer width="12"/>
                                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                                <rich:spacer width="12"/>
                                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                                <rich:spacer width="12"/>
                                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                                <rich:spacer width="12"/>

                                                                <h:selectOneMenu  value="#{afastamentoBean.departamento}">
                                                                    <f:selectItems value="#{afastamentoBean.departamentolist}"/>
                                                                    <a4j:support event="onchange" action="#{afastamentoBean.consultaFuncionario}"
                                                                                 reRender="funcionario,afastamentoGroup,butaoAfastamento,funcionarioAf"/>

                                                                </h:selectOneMenu>
                                                                <rich:spacer width="10"/>

                                                                <h:selectOneMenu id="funcionarioAf" value="#{afastamentoBean.cod_funcionario}">
                                                                    <f:selectItems value="#{afastamentoBean.funcionarioList}"/>
                                                                    <a4j:support event="onchange"  action="#{afastamentoBean.consultaAfastamento}"
                                                                                 reRender="afastamentoGroup,butaoAfastamento"/>
                                                                </h:selectOneMenu>
                                                                <rich:spacer  width="10"/>

                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}" value="#{afastamentoBean.dataInicio}" >
                                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{afastamentoBean.consultaAfastamento}"
                                                                                 reRender="afastamentoGroup,butaoAfastamento"/>
                                                                </rich:calendar>
                                                                <rich:spacer width="10"/>

                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}" value="#{afastamentoBean.dataFim}">
                                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{afastamentoBean.consultaAfastamento}"
                                                                                 reRender="afastamentoGroup,butaoAfastamento"/>
                                                                </rich:calendar>
                                                                <rich:spacer width="15"/>

                                                                <h:panelGroup>
                                                                    <h:selectBooleanCheckbox value="#{afastamentoBean.incluirSubSetores}">
                                                                        <a4j:support event="onchange" action="#{afastamentoBean.consultaFuncionario}"
                                                                                     reRender="funcionarioAf,afastamentoGroup,butaoAfastamento"/>
                                                                    </h:selectBooleanCheckbox>
                                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                                </h:panelGroup>
                                                            </h:panelGrid>
                                                            <a4j:status id="progressoEmAberto"  for="afastamentoRegion"
                                                                        onstart="Richfaces.showModalPanel('panelAfastamentStatus');"
                                                                        onstop="#{rich:component('panelAfastamentStatus')}.hide()"/>
                                                            <rich:modalPanel id="panelAfastamentStatus" autosized="true" >
                                                                <h:panelGrid columns="3">
                                                                    <h:graphicImage url="../images/load.gif" />
                                                                    <rich:spacer width="8"/>
                                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                                </h:panelGrid>
                                                            </rich:modalPanel>
                                                        </center>


                                                        <h:panelGroup id="butaoAfastamento" style="text-align:center;float:center" >
                                                            <center>
                                                                <h:panelGrid columns="3" style="text-align:center;float:center"
                                                                             rendered="#{afastamentoBean.cod_funcionario != -1}">
                                                                    <h:panelGrid>
                                                                        <center>
                                                                            <h:outputLink  value="#" id="linkAdiconarAfastamento">
                                                                                <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                                <rich:componentControl for="panelAdicionarAfastamento" attachTo="linkAdiconarAfastamento"
                                                                                                       operation="show" event="onclick"/>
                                                                                <a4j:support event="onclick" reRender="gridNovoAfastamentoModalPanel"
                                                                                             action="#{afastamentoBean.showNovoAfastamento}" />
                                                                            </h:outputLink>
                                                                            Adicionar
                                                                        </center>
                                                                    </h:panelGrid>
                                                                    <rich:spacer width="25"/>
                                                                    <h:panelGrid  rendered="#{not empty afastamentoBean.afastamentoList}">
                                                                        <center>
                                                                            <h:commandButton   id="linkExcluirAfastamento" image="../images/fechar.gif"  action="#{afastamentoBean.excluirTodosAfastamento}">
                                                                                <a4j:support event="onclick"
                                                                                             onsubmit="javascript:if (!confirm('Deseja excluir todos esses afastamentos?')) return false;"/>
                                                                            </h:commandButton>
                                                                            Excluir Todos
                                                                        </center>
                                                                    </h:panelGrid>
                                                                </h:panelGrid>
                                                            </center>
                                                        </h:panelGroup>
                                                    </h:form>
                                                </a4j:region>

                                                <h:panelGroup id="afastamentoGroup" style="text-align:center;float:center">
                                                    <h:form id="formAfastamentogrupo">
                                                        <center>
                                                            <rich:dataTable id="listaDeAfastamento"
                                                                            value="#{afastamentoBean.afastamentoList}" var="afastamento"
                                                                            rowClasses="zebra1,zebra2"
                                                                            rows="25"
                                                                            rendered="#{not empty afastamentoBean.afastamentoList}">
                                                                <f:facet name="header">
                                                                    <h:outputText value="LISTA DE FUNCIONÁRIOS"/>
                                                                </f:facet>
                                                                <rich:column sortBy="#{afastamento.funcionario.nome}" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="NOME"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{afastamento.funcionario.nome}" />
                                                                </rich:column>
                                                                <rich:column sortBy="#{afastamento.dataInicio}" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="INÍCIO INTERVALO"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{afastamento.dataInicio}" >
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                                <rich:column sortBy="#{afastamento.dataFim}" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="FIM INTERVALO"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{afastamento.dataFim}">
                                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                                    </h:outputText>
                                                                </rich:column>
                                                                <rich:column sortBy="#{afastamento.categoriaAfastamento.descricao}" style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="CATEGORIA"/>
                                                                    </f:facet>
                                                                    <h:outputText value="#{afastamento.categoriaAfastamento.descricao}"/>
                                                                </rich:column>
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="ALTERAR"/>
                                                                    </f:facet>
                                                                    <center>
                                                                        <h:outputLink  value="#" id="btAlterarAfastamento">
                                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                            <rich:componentControl for="paneAlterarAfastamento"
                                                                                                   attachTo="btAlterarAfastamento" operation="show" event="onclick"/>
                                                                            <a4j:support action="#{afastamentoBean.showAlterarAfastamento}"
                                                                                         event="onclick"
                                                                                         reRender="gridAlterarAfastamentoModalPanel">
                                                                                <f:param name="posAfastamento" value="#{afastamento.id}"/>
                                                                            </a4j:support>

                                                                        </h:outputLink>
                                                                    </center>
                                                                </rich:column>
                                                                <rich:column style="text-align:center">
                                                                    <f:facet name="header">
                                                                        <h:outputText value="EXCLUIR"/>
                                                                    </f:facet>
                                                                    <a4j:commandButton id="btExcluirAfastamento"
                                                                                       value="Deletar"
                                                                                       image="../images/delete.png"
                                                                                       reRender="afastamentoGroup"
                                                                                       ajaxSingle="true"
                                                                                       action="#{afastamentoBean.excluirAfastamento}"
                                                                                       onclick="javascript:if (!confirm('Deseja excluir esse afastamento?')) return false;">
                                                                        <f:param name="posAfastamento" value="#{afastamento.id}"/>
                                                                    </a4j:commandButton>
                                                                </rich:column>
                                                            </rich:dataTable>
                                                        </center>
                                                        <rich:datascroller  id="datascrollerAfastamento"
                                                                            for="listaDeAfastamento"
                                                                            renderIfSinglePage="false">
                                                        </rich:datascroller>

                                                        <rich:modalPanel id="panelAdicionarAfastamento" width="270" height="170" autosized="true">
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <h:outputText value="Afastamento"></h:outputText>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <f:facet name="controls">
                                                                <h:panelGroup>
                                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAfastamento"/>
                                                                    <rich:componentControl for="panelAdicionarAfastamento" attachTo="hidelinkAfastamento" operation="hide" event="onclick"/>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <h:panelGrid id="gridNovoAfastamentoModalPanel" columns="1" style="text-align:center;float:center">
                                                                <h:panelGroup>
                                                                    <center>
                                                                        <h:panelGrid  columns="4"  style="text-align:center;float:center">
                                                                            <center>
                                                                                <h:outputText value="Data Inicial " styleClass="label"/>
                                                                                <rich:spacer width="25"/>
                                                                                <h:outputText value="Data Final" styleClass="label"/>
                                                                                <rich:spacer width="15"/>                                                                               

                                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}"
                                                                                               value="#{afastamentoBean.novoAfastamento.dataInicio}"/>
                                                                                <rich:spacer width="25"/>
                                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}"
                                                                                               value="#{afastamentoBean.novoAfastamento.dataFim}"/>
                                                                            </center>
                                                                        </h:panelGrid>

                                                                        <br>
                                                                        <h:panelGrid style="text-align:center;float:center">
                                                                            <h:outputText value="Categoria do Afastamento" styleClass="label"/>
                                                                            <h:selectOneMenu  value="#{afastamentoBean.novoAfastamento.codCategoriaAfastamento}">
                                                                                <f:selectItems value="#{afastamentoBean.categoriaAfastamentoList}"/>
                                                                            </h:selectOneMenu>
                                                                        </h:panelGrid>

                                                                        <br>
                                                                        <h:commandButton value="Confirmar" action="#{afastamentoBean.addAfastamento}"
                                                                                         id="confirmarNovoAfastamento" >
                                                                            <rich:componentControl for="panelAdicionarAfastamento"
                                                                                                   attachTo="confirmarNovoAfastamento" operation="hide" event="onclick"/>
                                                                        </h:commandButton>

                                                                    </center>
                                                                </h:panelGroup>
                                                            </h:panelGrid>
                                                        </rich:modalPanel>
                                                    </h:form>
                                                    <h:form id="formAlterarAfastametno">
                                                        <rich:modalPanel id="paneAlterarAfastamento" width="270" height="170" autosized="true">
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <h:outputText value="Alterar afastamento"></h:outputText>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <f:facet name="controls">
                                                                <h:panelGroup>
                                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAlterarAfastamento"/>
                                                                    <rich:componentControl for="paneAlterarAfastamento" attachTo="hidelinkAlterarAfastamento" operation="hide" event="onclick"/>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <h:panelGrid id="gridAlterarAfastamentoModalPanel" columns="1" style="text-align:center;float:center">
                                                                <h:panelGroup>
                                                                    <center>
                                                                        <h:panelGrid  columns="4"  style="text-align:center;float:center">
                                                                            <center>
                                                                                <h:outputText value="Data Inicial " styleClass="label"/>
                                                                                <rich:spacer width="25"/>
                                                                                <h:outputText value="Data Final" styleClass="label"/>
                                                                                <rich:spacer width="15"/>

                                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}"
                                                                                               value="#{afastamentoBean.alterarAFastamento.dataInicio}"/>
                                                                                <rich:spacer width="25"/>
                                                                                <rich:calendar inputSize="8" locale="#{afastamentoBean.objLocale}"
                                                                                               value="#{afastamentoBean.alterarAFastamento.dataFim}"/>
                                                                            </center>
                                                                        </h:panelGrid>

                                                                        <br>
                                                                        <h:panelGrid style="text-align:center;float:center">
                                                                            <h:outputText value="Categoria do Afastamento" styleClass="label"/>
                                                                            <h:selectOneMenu  value="#{afastamentoBean.alterarAFastamento.codCategoriaAfastamento}">
                                                                                <f:selectItems value="#{afastamentoBean.categoriaAfastamentoList}"/>
                                                                            </h:selectOneMenu>
                                                                        </h:panelGrid>

                                                                        <br>
                                                                        <h:commandButton value="Confirmar" action="#{afastamentoBean.alterarAfastamento}"
                                                                                         id="confirmarAlterarAfastamento" >
                                                                            <rich:componentControl for="paneAlterarAfastamento"
                                                                                                   attachTo="confirmarAlterarAfastamento" operation="hide" event="onclick"/>
                                                                        </h:commandButton>

                                                                    </center>
                                                                </h:panelGroup>
                                                            </h:panelGrid>
                                                        </rich:modalPanel>
                                                    </h:form>
                                                </h:panelGroup>
                                            </center>
                                        </h:panelGrid>
                                    </center>
                                </rich:tab>
                            </rich:tabPanel>                                
                        </center>
                    </h:panelGrid>

                    <a4j:form id="formNovoHorario">
                        <h:panelGroup>
                            <rich:modalPanel id="addmodalPanel" width="550" height="580" styleClass="center">
                                <div class="scrollAdd">
                                    <f:facet name="header">
                                        <h:panelGroup>
                                            <h:outputText value="Novo Horário"></h:outputText>
                                        </h:panelGroup>
                                    </f:facet>
                                    <f:facet name="controls">
                                        <h:panelGroup>
                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                            <rich:componentControl for="addmodalPanel" 
                                                                   attachTo="hidelink" 
                                                                   operation="hide" 
                                                                   event="onclick"/>
                                        </h:panelGroup>
                                    </f:facet>
                                    <center>

                                        <h:panelGrid id="paneladdHorario" columns="1">
                                            <h:panelGrid  columns="4">
                                                <h:outputText value="Nome do turno: " styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="Nome" size="20" value="#{horarioBean.novoHorario.nome}" maxlength="20" style="float:left" required="true">
                                                    <rich:ajaxValidator event="onblur"/>
                                                </h:inputText>
                                                <rich:message for="Nome" style="color:blue;float:left" />

                                                <h:outputText value="Legenda: " styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="legenda" size="2" value="#{horarioBean.novoHorario.legenda}" maxlength="3" style="float:left"/>
                                                <rich:message for="legenda" style="color:blue"/>

                                                <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NHEntrada" size="2" value="#{horarioBean.novoHorario.entrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"> <%--onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NHEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />
                                                </h:inputText>
                                                <rich:message for="NHEntrada" style="color:blue"/>

                                                <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NHSaida" size="2" value="#{horarioBean.novoHorario.saida}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"> <%--onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NHSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />
                                                </h:inputText>
                                                <rich:message for="NHSaida" style="color:blue"/>

                                                <h:outputText value="Início da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NIFEntrada" size="2" value="#{horarioBean.novoHorario.inicioFaixaEntrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"> <%-- onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NIFEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="NIFEntrada" style="color:blue"/>

                                                <h:outputText value="Fim da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NFFEntrada" size="2" value="#{horarioBean.novoHorario.fimFaixaEntrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"> <%-- onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NFFEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="NFFEntrada" style="color:blue"/>

                                                <h:outputText value="Início da Faixa de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NIFSaida" size="2" value="#{horarioBean.novoHorario.inicioFaixaSaida}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"> <%-- onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NIFSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="NIFSaida" style="color:blue"/>

                                                <h:outputText value="Fim da Faixa de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="NFFSaida" size="2" value="#{horarioBean.novoHorario.fimFaixaSaida}"  maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left"><%-- onblur="mascara_horaBlur(this.id)">--%>
                                                    <rich:jQuery selector="#NFFSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="NFFSaida" style="color:blue"/>
                                            </h:panelGrid>
                                            <br>
                                            <h:panelGrid columns="5">
                                                <h:selectBooleanCheckbox  value="#{horarioBean.novoHorario.isEntradaObrigatoria}"/>
                                                <h:outputText value="Entrada Obrigatória" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="15"/>
                                                <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.isSaidaObrigatoria}"/>
                                                <h:outputText value="Saída Obrigatória" styleClass="labelRight" style="float:right"/>
                                            </h:panelGrid>
                                            <a4j:outputPanel id="horarioPanel" ajaxRendered="true">
                                                <a4j:commandLink value="Expediente normal" action="#{horarioBean.setRestType('Expediente')}" ajaxSingle="true"
                                                                 rendered="#{horarioBean.novoHorario.type == 'Expediente' or horarioBean.novoHorario.type == null}"
                                                                 reRender="horarioPanel" styleClass="type"/>
                                                <a4j:commandLink value="Um Descanso" action="#{horarioBean.setRestType('Plantonista12')}" ajaxSingle="true"
                                                                 rendered="#{horarioBean.novoHorario.type == 'Plantonista12' or horarioBean.novoHorario.type == null}"
                                                                 reRender="horarioPanel" styleClass="type"/>
                                                <a4j:commandLink value="Dois Descansos" action="#{horarioBean.setRestType('Plantonista24')}" ajaxSingle="true"
                                                                 rendered="#{horarioBean.novoHorario.type == 'Plantonista24' or horarioBean.novoHorario.type == null}"
                                                                 reRender="horarioPanel" styleClass="type"/>
                                                <a4j:commandLink value="Call Center" action="#{horarioBean.setRestType('CallCenter')}" ajaxSingle="true"
                                                                 rendered="#{horarioBean.novoHorario.type == 'CallCenter' or horarioBean.novoHorario.type == null}"
                                                                 reRender="horarioPanel" styleClass="type"/>
                                                <br/>
                                                <h:panelGrid styleClass="duty12" rendered="#{horarioBean.novoHorario.type == 'Plantonista12' or 
                                                                                             horarioBean.novoHorario.type == 'Plantonista24' or 
                                                                                             horarioBean.novoHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">

                                                             <h:panelGrid columns="2">
                                                                 <h:outputText value="Tempo de Tolerância:" styleClass="label"/>
                                                                 <rich:inputNumberSpinner  value="#{horarioBean.novoHorario.toleranciaDesc}"
                                                                                           inputSize="2" maxValue="60" minValue="0"
                                                                                           id="addTempoToleranciaInputNumberSpinnerID">
                                                                 </rich:inputNumberSpinner>
                                                             </h:panelGrid>

                                                             <h:outputText value="Horário de Descanso 1" styleClass="label" style="margin:10px 0 10px 61px;display:block;text-aling:right"/>

                                                             <h:panelGrid  columns="3" >                                                        
                                                                 <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:inputText id="NHEntrada3" size="2" value="#{horarioBean.novoHorario.entradaDescanso1}" maxlength="20"
                                                                              onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)">
                                                                     <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                 </h:inputText>

                                                                 <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:panelGrid columns="1">
                                                                     <h:inputText id="NHSaida3" size="2" value="#{horarioBean.novoHorario.saidaDescanso1}" maxlength="20"
                                                                                  onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.novoHorario.combinarEntrada}">
                                                                         <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                     </h:inputText>
                                                                     <h:outputText value="#{horarioBean.novoHorario.saidaDescanso1}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.novoHorario.combinarEntrada}"/>
                                                                 </h:panelGrid>

                                                                 <h:outputText value="Deduzir Primeiro Descanso:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.deduzirDescanso1}">
                                                                     <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                 </h:selectBooleanCheckbox>
                                                             </h:panelGrid>
                                                </h:panelGrid>

                                                <h:panelGrid styleClass="dutyCallCenter" rendered="#{horarioBean.novoHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">
                                                    <h:outputText value="Horário de Interjornada" styleClass="label" style="margin:10px 0 10px 61px;display:block"/>
                                                    <h:panelGrid  columns="3" >                                                                  
                                                        <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="IEntrada" size="2" value="#{horarioBean.novoHorario.entradaIntrajornada}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.novoHorario.combinarEntrada}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.novoHorario.entradaIntrajornada}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.novoHorario.combinarEntrada}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="ISaida" size="2" value="#{horarioBean.novoHorario.saidaIntrajornada}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.novoHorario.combinarSaida}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.novoHorario.saidaIntrajornada}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.novoHorario.combinarSaida}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Deduzir da Interjornada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.deduzirIntrajornada}">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                        <h:outputText value="Combinar Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.combinarEntrada}">
                                                            <a4j:support event="onchange" ajaxSingle="true" reRender="horarioPanel" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                        <h:outputText value="Combinar Saida:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.combinarSaida}">
                                                            <a4j:support event="onchange" ajaxSingle="true" reRender="horarioPanel" status="progressoEmAberto"/>

                                                        </h:selectBooleanCheckbox>

                                                    </h:panelGrid>
                                                </h:panelGrid>

                                                <h:panelGrid styleClass="duty24" rendered="#{horarioBean.novoHorario.type == 'Plantonista24' ||
                                                                                             horarioBean.novoHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">
                                                    <h:outputText value="Horário de Descanso 2" styleClass="label" style="margin:10px 0 10px 61px;display:block"/>
                                                    <h:panelGrid  columns="3" >                                                                  
                                                        <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="NHEntrada2" size="2" value="#{horarioBean.novoHorario.entradaDescanso2}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.novoHorario.combinarSaida}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.novoHorario.entradaDescanso2}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.novoHorario.combinarSaida}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:inputText id="NHSaida2" size="2" value="#{horarioBean.novoHorario.saidaDescanso2}" maxlength="20"
                                                                     onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:inputText>

                                                        <h:outputText value="Deduzir Segundo Descanso:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.novoHorario.deduzirDescanso2}">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                    </h:panelGrid>
                                                </h:panelGrid>
                                            </a4j:outputPanel>
                                        </h:panelGrid>

                                        <br>
                                        <center>
                                            <h:commandButton value="Salvar"  id="addID" action="#{horarioBean.addNovoHorario}">
                                                <rich:componentControl for="addmodalPanel" attachTo="addID" operation="hide" event="onclick"/>
                                            </h:commandButton>

                                        </center>
                                    </center>
                                </div>
                            </rich:modalPanel>
                        </h:panelGroup>
                    </a4j:form>

                    <h:form id="formEditHorario">
                        <h:panelGroup>
                            <rich:modalPanel id="editmodalPanel" width="550" height="600" styleClass="center">
                                <div id="divMajestoso" class="scrollAdd">
                                    <center>
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Editar Horário" ></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>

                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="edithidelink"/>
                                                <rich:componentControl for="editmodalPanel" attachTo="edithidelink" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <h:panelGrid id="paneleditHorario" columns="1" >
                                            <h:panelGrid  columns="4">
                                                <h:outputText value="Nome do turno: " styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNome" size="20" value=  "#{horarioBean.editHorario.nome}" maxlength="20" style="float:left" required="true">
                                                    <rich:ajaxValidator event="onblur"/>
                                                </h:inputText>
                                                <rich:message for="editNome" style="color:blue;float:left" />

                                                <h:outputText value="Legenda: " styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editLegenda" size="2" value="#{horarioBean.editHorario.legenda}" maxlength="3" style="float:left"/>
                                                <rich:message for="editLegenda" style="color:blue"/>

                                                <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNHEntrada" size="2" value="#{horarioBean.editHorario.entrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNHEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />
                                                </h:inputText>
                                                <rich:message for="editNHEntrada" style="color:blue"/>

                                                <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNHSaida" size="2" value="#{horarioBean.editHorario.saida}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNHSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />
                                                </h:inputText>
                                                <rich:message for="editNHSaida" style="color:blue"/>

                                                <h:outputText value="Início da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNIFEntrada" size="2" value="#{horarioBean.editHorario.inicioFaixaEntrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNIFEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="editNIFEntrada" style="color:blue"/>

                                                <h:outputText value="Fim da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNFFEntrada" size="2" value="#{horarioBean.editHorario.fimFaixaEntrada}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNFFEntrada" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="editNFFEntrada" style="color:blue"/>

                                                <h:outputText value="Início da Faixa de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNIFSaida" size="2" value="#{horarioBean.editHorario.inicioFaixaSaida}" maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNIFSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="editNIFSaida" style="color:blue"/>

                                                <h:outputText value="Fim da Faixa de Saída:" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="5"/>
                                                <h:inputText id="editNFFSaida" size="2" value="#{horarioBean.editHorario.fimFaixaSaida}"  maxlength="5" required="true"
                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                    <rich:jQuery selector="#editNFFSaida" query="mask('99:99')" timing="onload"/>
                                                    <f:convertDateTime pattern="HH:mm"/>
                                                    <rich:ajaxValidator event="onblur" />                                                                    
                                                </h:inputText>
                                                <rich:message for="editNFFSaida" style="color:blue"/>
                                            </h:panelGrid>                                                    
                                            <br>
                                            <h:panelGrid columns="5" >
                                                <h:selectBooleanCheckbox value="#{horarioBean.editHorario.isEntradaObrigatoria}"/>
                                                <h:outputText value="Entrada Obrigatória" styleClass="labelRight" style="float:right"/>
                                                <rich:spacer width="15"/>
                                                <h:selectBooleanCheckbox value="#{horarioBean.editHorario.isSaidaObrigatoria}"/>
                                                <h:outputText value="Saída Obrigatória" styleClass="labelRight" style="float:right"/>
                                            </h:panelGrid>

                                            <h:outputLabel value="Expediente normal" styleClass="type" rendered="#{horarioBean.editHorario.type == 'Expediente'}" style="margin:10px 0 5px 5px" onclick="hideDuty();$(this).show()"/>
                                            <h:outputLabel value="Um Descanso" styleClass="type" rendered="#{horarioBean.editHorario.type == 'Plantonista12'}" onclick="showDuty12();$(this).show()"/>                                                                 
                                            <h:outputLabel value="Dois Descansos" styleClass="type" rendered="#{horarioBean.editHorario.type == 'Plantonista24'}" onclick="showDuty24();$(this).show()"/>      
                                            <h:outputLabel value="Call Center" styleClass="type" rendered="#{horarioBean.editHorario.type == 'CallCenter'}" onclick="showDutyCallCenter();$(this).show()"/>

                                            <a4j:outputPanel id="horarioEditPanel">
                                                <h:panelGrid styleClass="duty12" rendered="#{horarioBean.editHorario.type == 'Plantonista12' || 
                                                                                             horarioBean.editHorario.type == 'Plantonista24' || 
                                                                                             horarioBean.editHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">

                                                             <h:outputText value="Tempo de Tolerância:" styleClass="label"/>
                                                             <rich:inputNumberSpinner  value="#{horarioBean.editHorario.toleranciaDesc}"
                                                                                       inputSize="3" maxValue="100" minValue="0"
                                                                                       id="editTempoToleranciaInputNumberSpinnerID">
                                                                 <a4j:support ajaxSingle="true" event="onchange"/>
                                                             </rich:inputNumberSpinner>

                                                             <h:outputText value="Horário de Descanso 1" styleClass="label" style="margin:10px 0 10px 61px;display:block"/>
                                                             <h:panelGrid  columns="3" >                                                        
                                                                 <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:inputText id="editEntDesc1" size="2" value="#{horarioBean.editHorario.entradaDescanso1}" maxlength="20"
                                                                              onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)">
                                                                     <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                 </h:inputText>

                                                                 <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:panelGrid columns="1">
                                                                     <h:inputText id="editSaiDesc1" size="2" value="#{horarioBean.editHorario.saidaDescanso1}" maxlength="20" 
                                                                                  onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.editHorario.combinarEntrada}">
                                                                         <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                     </h:inputText>
                                                                     <h:outputText value="#{horarioBean.editHorario.saidaDescanso1}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.editHorario.combinarEntrada}"/>
                                                                 </h:panelGrid>

                                                                 <h:outputText value="Deduzir Primeiro Descanso:" styleClass="labelRight" style="float:right"/>
                                                                 <rich:spacer width="5"/>
                                                                 <h:selectBooleanCheckbox value="#{horarioBean.editHorario.deduzirDescanso1}">
                                                                     <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                                 </h:selectBooleanCheckbox>

                                                             </h:panelGrid>
                                                </h:panelGrid>

                                                <h:panelGrid styleClass="dutyCallCenter" rendered="#{horarioBean.editHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">
                                                    <h:outputText value="Horário de Interjornada" styleClass="label" style="margin:10px 0 10px 61px;display:block"/>
                                                    <h:panelGrid  columns="3" >                                                                  
                                                        <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="editEntInter" size="2" value="#{horarioBean.editHorario.entradaIntrajornada}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.editHorario.combinarEntrada}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.editHorario.entradaIntrajornada}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.editHorario.combinarEntrada}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="editSaiInter" size="2" value="#{horarioBean.editHorario.saidaIntrajornada}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.editHorario.combinarSaida}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.editHorario.saidaIntrajornada}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.editHorario.combinarSaida}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Deduzir da Interjornada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.editHorario.deduzirIntrajornada}">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                        <h:outputText value="Combinar Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox id="checkEditCombinarEntrada" value="#{horarioBean.editHorario.combinarEntrada}">
                                                            <a4j:support event="onchange" ajaxSingle="true" reRender="horarioEditPanel" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                        <h:outputText value="Combinar Saida:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox id="checkEditCombinarSaida" value="#{horarioBean.editHorario.combinarSaida}">
                                                            <a4j:support event="onchange" ajaxSingle="true" reRender="horarioEditPanel" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                    </h:panelGrid>
                                                </h:panelGrid>

                                                <h:panelGrid styleClass="duty24" rendered="#{horarioBean.editHorario.type == 'Plantonista24' ||
                                                                                             horarioBean.editHorario.type == 'CallCenter'}" style="margin:0 0 0 33px;">
                                                    <h:outputText value="Horário de Descanso 2" styleClass="label" style="margin:10px 0 10px 61px;display:block"/>
                                                    <h:panelGrid  columns="3" >                                                           
                                                        <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:panelGrid columns="1">
                                                            <h:inputText id="editEntDesc2" size="2" value="#{horarioBean.editHorario.entradaDescanso2}" maxlength="20"
                                                                         onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)" rendered="#{!horarioBean.editHorario.combinarSaida}">
                                                                <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                            </h:inputText>
                                                            <h:outputText value="#{horarioBean.editHorario.entradaDescanso2}" styleClass="labelRight" style="float:right" rendered="#{horarioBean.editHorario.combinarSaida}"/>
                                                        </h:panelGrid>

                                                        <h:outputText value="Horário de Saída:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:inputText id="editSaiDesc2" size="2" value="#{horarioBean.editHorario.saidaDescanso2}" maxlength="20"
                                                                     onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:inputText>

                                                        <h:outputText value="Deduzir Segundo Descanso:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox value="#{horarioBean.editHorario.deduzirDescanso2}">
                                                            <a4j:support ajaxSingle="true" event="onchange" status="progressoEmAberto"/>
                                                        </h:selectBooleanCheckbox>

                                                    </h:panelGrid>
                                                </h:panelGrid>   
                                            </a4j:outputPanel>
                                        </h:panelGrid>
                                        <br>
                                        <center>
                                            <h:commandButton value="Editar" id="editarID" action="#{horarioBean.editarHorario}">
                                                <rich:componentControl for="editmodalPanel"
                                                                       attachTo="editarID"
                                                                       operation="hide"
                                                                       event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </center>
                                </div>
                            </rich:modalPanel>

                        </h:panelGroup>
                    </h:form>

                    <h:form id="formNovaJornada">
                        <rich:modalPanel id="addJornada" width="300" height="170" autosized="true" styleClass="center">
                            <f:facet name="header">
                                <h:panelGroup>
                                    <h:outputText value="Nova Jornada"></h:outputText>
                                </h:panelGroup>
                            </f:facet>
                            <f:facet name="controls">
                                <h:panelGroup>
                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink2"/>
                                    <rich:componentControl for="addJornada" attachTo="hidelink2" operation="hide" event="onclick"/>
                                </h:panelGroup>
                            </f:facet>
                            <center>
                                <center>
                                    <h:panelGrid id="novaJornadadePanelGrid" columns="3">
                                        <h:outputText value="Nome: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText id="momeJornada"  value="#{jornadaCadastroBean.novaJornadaCadastro.nome}" size="20"/>

                                        <h:outputText value="Data de Início: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <rich:calendar inputSize="16" locale="#{jornadaCadastroBean.objLocale}" value="#{jornadaCadastroBean.novaJornadaCadastro.dataInicio}"/>

                                        <h:outputText value="Tipo do Ciclo: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <rich:comboBox defaultLabel="Escolha o tipo" value="#{jornadaCadastroBean.novaJornadaCadastro.unidadeCiclos}" width="127">
                                            <f:selectItem itemValue="Dia"  />
                                            <f:selectItem itemValue="Semana"  />
                                            <f:selectItem itemValue="Mês"  />
                                        </rich:comboBox>

                                        <h:outputText value="Número de Ciclos: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>

                                        <rich:inputNumberSpinner id="spinnerADD" value="#{jornadaCadastroBean.novaJornadaCadastro.quantidadeCiclos}"
                                                                 inputSize="16" maxValue="99" minValue="1"/>
                                    </h:panelGrid>
                                    <br>
                                    <h:commandButton value="Salvar" id="addJornadaSalvar" action="#{jornadaCadastroBean.addNovaJornada}">
                                        <rich:componentControl for="addJornada" attachTo="addJornadaSalvar" operation="hide" event="onclick"/>
                                    </h:commandButton>
                                </center>
                            </center>
                        </rich:modalPanel>
                    </h:form>                

                    <h:form id="formEditJornada">
                        <rich:modalPanel id="editJornada" width="300" height="170" autosized="true" styleClass="center">

                            <f:facet name="header">
                                <h:panelGroup>
                                    <h:outputText value="Editar Jornada"></h:outputText>
                                </h:panelGroup>
                            </f:facet>
                            <f:facet name="controls">
                                <h:panelGroup>
                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink3"/>
                                    <rich:componentControl for="editJornada" attachTo="hidelink3" operation="hide" event="onclick"/>
                                </h:panelGroup>
                            </f:facet>
                            <center>
                                <center>
                                    <h:panelGrid id="editJornadaPanelGrid" columns="3" >
                                        <h:outputText value="Nome: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText  value="#{jornadaCadastroBean.editJornadaCadastro.nome}" size="20"/>

                                        <h:outputText value="Data de Início: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <rich:calendar inputSize="16" locale="#{jornadaCadastroBean.objLocale}" value="#{jornadaCadastroBean.editJornadaCadastro.dataInicio}" />

                                        <h:outputText value="Tipo do Ciclo: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <rich:comboBox id="tipoCicloID" defaultLabel="Escolha o tipo" value="#{jornadaCadastroBean.editJornadaCadastro.unidadeCiclos}" width="127">
                                            <f:selectItem itemValue="Dia"  />
                                            <f:selectItem itemValue="Semana"  />
                                            <f:selectItem itemValue="Mês"  />
                                        </rich:comboBox>

                                        <h:outputText value="Número de Ciclos: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <rich:inputNumberSpinner id="numCicloID" value="#{jornadaCadastroBean.editJornadaCadastro.quantidadeCiclos}"
                                                                 inputSize="16" maxValue="99" minValue="1"/>
                                    </h:panelGrid>
                                    <br>
                                    <h:commandButton  value="Salvar" id="editJornadaSalvar" action="#{jornadaCadastroBean.editarJornadaCadastro}">
                                        <rich:componentControl for="editJornada" attachTo="editJornadaSalvar" operation="hide" event="onclick"/>
                                    </h:commandButton>

                                </center>
                            </center>
                        </rich:modalPanel>
                    </h:form>                                                  

                    <rich:modalPanel id="addJornadaModalPanel" width="550" height="530" autosized="true" styleClass="center" >

                        <f:facet name="header">
                            <h:panelGroup>
                                <h:outputText value="Adicionar Jornada ao Cronograma"></h:outputText>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="controls">
                            <h:panelGroup>
                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink9Temp"/>
                                <rich:componentControl for="addJornadaModalPanel" attachTo="hidelink9Temp" operation="hide" event="onclick"/>

                            </h:panelGroup>
                        </f:facet>
                        <div class="scrollAdd">
                            <a4j:outputPanel id="addJornadaCronoAjaxPanel">
                                <h:form id="addJornadaCronoForm">
                                    <center>              
                                        <h:panelGrid id="addJornadaCronoGrid" columns="1" style="text-align:center;width:550px">
                                            <center>
                                                <h:outputText value="Escolha a jornada a ser adicionada " styleClass="label"/>
                                                <h:panelGroup style="text-align:center" >
                                                    <h:selectOneMenu id="JornadasNovasList" value="#{cadastroCronogramaBean.jornadaSelecionadas}" >
                                                        <f:selectItems value="#{cadastroCronogramaBean.jornadasList}"/>
                                                        <a4j:support event="onchange" action="#{cadastroCronogramaBean.showDiasJornada}" reRender="diasExtendAddJornada"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                                <br/>
                                                <h:panelGroup id="diasExtendAddJornada">

                                                    <rich:extendedDataTable
                                                        rendered= "#{cadastroCronogramaBean.showDiasJornada}"
                                                        selectionMode= "none"
                                                        enableContextMenu="false"
                                                        id="diasExtendList"
                                                        value="#{cadastroCronogramaBean.diasJornadaList}" var="diasJornada"
                                                        height="186px">
                                                        <rich:column sortable="false" id="diaDaJornada" style="text-align:center" width="20%">
                                                            <f:facet name="header">
                                                                <h:outputText value="Dia" />
                                                            </f:facet>
                                                            <h:outputText value="#{diasJornada.diaString}" />
                                                        </rich:column>
                                                        <rich:column sortable="false" id="turnosAssociados" width="35%" >
                                                            <f:facet name="header">
                                                                <h:outputText value="Horários" />
                                                            </f:facet>
                                                            <h:outputText value="#{diasJornada.horarios}" />
                                                        </rich:column>
                                                        <rich:column sortable="false" id="descansos" width="48%" >
                                                            <f:facet name="header">
                                                                <h:outputText value="Descansos" />
                                                            </f:facet>
                                                            <h:outputText value="#{diasJornada.descansos}" />
                                                        </rich:column>
                                                    </rich:extendedDataTable>
                                                </h:panelGroup>
                                                <br/>
                                                <h:panelGrid columns="3" style="text-align:center;width:550px">
                                                    <h:panelGroup>
                                                        <h:outputText value="Data Inicial " styleClass="label"/><br>
                                                        <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataInicioNewJornada}"/>
                                                    </h:panelGroup>
                                                    <h:panelGroup>
                                                        <h:outputText value="Data Final" styleClass="label"/><br>
                                                        <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataFimNewJornada}"/>
                                                    </h:panelGroup>
                                                </h:panelGrid>
                                                <br/>
                                                <h:commandButton   value="Salvar" id="salvarAddJornadaTemp" image="../images/salvar_48.png"
                                                                   action="#{cadastroCronogramaBean.salvarNovaJornada}">
                                                    <rich:componentControl for="addJornadaModalPanel" attachTo="salvarAddJornadaTemp" operation="hide"
                                                                           event="onclick"/>
                                                </h:commandButton>
                                                <h:outputText value=" Salvar" styleClass="italico"/>
                                            </center>
                                        </h:panelGrid>
                                    </center>
                                </h:form>
                            </a4j:outputPanel>
                        </div>
                    </rich:modalPanel>

                    <h:form id="formEditJornadaAoCronograma">
                        <rich:modalPanel id="editJornadaPanel" width="550" height="530" autosized="true" styleClass="center" >
                            <f:facet name="header">
                                <h:panelGroup>
                                    <h:outputText value="Editar Jornada do Cronograma"></h:outputText>
                                </h:panelGroup>
                            </f:facet>
                            <f:facet name="controls">
                                <h:panelGroup>
                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink45Temp"/>
                                    <rich:componentControl for="editJornadaPanel" attachTo="hidelink45Temp" operation="hide" event="onclick"/>

                                </h:panelGroup>
                            </f:facet>
                            <div class="scrollAdd">
                                <center>
                                    <h:panelGrid id="editJornadaGrid" columns="1" style="text-align:center;width:550px">
                                        <center>
                                            <h:outputText value="Escolher outra jornada: " styleClass="label"/>
                                            <h:panelGroup style="text-align:center" >
                                                <h:selectOneMenu id="JornadasEditList"  value="#{cadastroCronogramaBean.jornadaEditSelecionada}" >
                                                    <f:selectItems value="#{cadastroCronogramaBean.jornadasList}"/>
                                                    <a4j:support event="onchange" action="#{cadastroCronogramaBean.showDiasJornada}" reRender="diasExtendEditJornada"/>
                                                    <%-- <a4j:support event="onchange" action="#{cadastroCronogramaBean.consultaFuncionario}" reRender="funcionarioTAdmin"/>--%>
                                                </h:selectOneMenu>
                                            </h:panelGroup>
                                            <br>
                                            <h:panelGroup id="diasExtendEditJornada">

                                                <rich:extendedDataTable
                                                    rendered= "#{cadastroCronogramaBean.showDiasJornada}"
                                                    selectionMode= "none"
                                                    enableContextMenu="false"
                                                    id="diasExtendAddJornadaList"
                                                    value="#{cadastroCronogramaBean.diasJornadaList}" var="diasJornada"
                                                    height="186px">
                                                    <rich:column sortable="false" style="text-align:center" width="20%">
                                                        <f:facet name="header">
                                                            <h:outputText value="Dia" />
                                                        </f:facet>
                                                        <h:outputText value="#{diasJornada.diaString}" />
                                                    </rich:column>
                                                    <rich:column sortable="false" width="35%" >
                                                        <f:facet name="header">
                                                            <h:outputText value="Horários" />
                                                        </f:facet>
                                                        <h:outputText value="#{diasJornada.horarios}" />
                                                    </rich:column>
                                                    <rich:column sortable="false" width="48%" >
                                                        <f:facet name="header">
                                                            <h:outputText value="Descansos" />
                                                        </f:facet>
                                                        <h:outputText value="#{diasJornada.descansos}" />
                                                    </rich:column>
                                                </rich:extendedDataTable>
                                            </h:panelGroup>
                                            <br>
                                            <h:panelGrid columns="3" style="text-align:center;width:550px">
                                                <h:panelGroup >
                                                    <h:outputText value="Data Inicial " styleClass="label"/><br>
                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataInicioEditJornada}"/>
                                                </h:panelGroup>
                                                <rich:spacer width="20"/>
                                                <h:panelGroup >
                                                    <h:outputText value="Data Final" styleClass="label"/><br>
                                                    <rich:calendar inputSize="8" locale="#{cadastroCronogramaBean.objLocale}" value="#{cadastroCronogramaBean.dataFimEditJornada}"/>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                            <br>
                                            <h:commandButton   value="Salvar" id="salvarEditJornadaTemp" image="../images/salvar_48.png"
                                                               action="#{cadastroCronogramaBean.salvarEditarJornada}">
                                                <rich:componentControl for="editJornadaPanel" attachTo="salvarEditJornadaTemp" operation="hide"
                                                                       event="onclick"/>
                                            </h:commandButton>
                                            <h:outputText value=" Salvar" styleClass="italico"/>
                                        </center>
                                    </h:panelGrid>
                                </center>
                            </div>
                        </rich:modalPanel>
                    </h:form>
                    <jsp:include page="../www/_bot.jsp"/>

                </f:view>
            </center>
    </body>
</html>