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
<%@ page import="ConsultaPonto.ConsultaFrequenciaComEscalaBean" %>
<%@ page import="ConsultaPonto.ConsultaFrequenciaSemEscalaBean" %>
<%@ page import="ConsultaPonto.ConsultaFrequenciaHoraExtraBean" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%
            String param = request.getParameter("param");
            if (param != null) {
                ConsultaFrequenciaComEscalaBean consultaFrequenciaComEscalaBean = (ConsultaFrequenciaComEscalaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaFrequenciaComEscalaBean");
                ConsultaFrequenciaSemEscalaBean consultaFrequenciaSemEscalaBean = (ConsultaFrequenciaSemEscalaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaFrequenciaSemEscalaBean");
                ConsultaFrequenciaHoraExtraBean consultaFrequenciaHoraExtraBean = (ConsultaFrequenciaHoraExtraBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("consultaFrequenciaHoraExtraBean");
                if (param.equals("clear")) {
                    if (consultaFrequenciaComEscalaBean.getAbaCorrente().equals("tab1")) {
                        consultaFrequenciaComEscalaBean.construtor();
                        consultaFrequenciaComEscalaBean.consultaDias();
                    }
                    if (consultaFrequenciaComEscalaBean.getAbaCorrente().equals("tab2")) {
                        consultaFrequenciaSemEscalaBean.construtor();
                        consultaFrequenciaSemEscalaBean.consultaDias();
                    }
                    if (consultaFrequenciaComEscalaBean.getAbaCorrente().equals("tab3")) {
                        consultaFrequenciaHoraExtraBean.construtor();
                        consultaFrequenciaHoraExtraBean.consultaDias();
                    }
                }
            }
%>
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
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <title>Consulta Administrador</title>
        <script type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
        </script>
    </head>
    <body>
        <f:view >
            <div class = "branco" id = "carregando"></div>
            <div class = "branco" id = "escurecer"></div>
            <div class = "branco" id = "branco"></div>
            <jsp:include page="../www/_top.jsp"/>
            <h:form id="f_messagens" prependId="false">
                <center>
                    <h:messages infoClass="info" />
                </center>
            </h:form>
            <center>
                <h:form id="form">
                    <h:panelGrid columns="1" >
                        <center>
                            <rich:tabPanel switchType="client"  width="965" selectedTab="#{consultaFrequenciaComEscalaBean.abaCorrente}">
                                <rich:tab id="tab1" name="tab1" label="Frequência com escala"
                                          ignoreDupResponses="true" rendered="#{usuarioBean.perfil.freqnComEscala== true}">
                                    <a4j:support event="ontabenter" action="#{consultaFrequenciaComEscalaBean.setAba}" reRender="f_messagens">
                                        <a4j:actionparam name="tab" value="tab1"/>
                                    </a4j:support>
                                    <br/>                                    
                                    <a4j:region id="region1">
                                        <center>
                                            <h:panelGrid columns="8">
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="12"/>

                                                <h:selectOneMenu id="departManu" value="#{consultaFrequenciaComEscalaBean.departamento}">
                                                    <f:selectItems value="#{consultaFrequenciaComEscalaBean.departamentolist}"/>
                                                    <a4j:support event="onchange" action="#{consultaFrequenciaComEscalaBean.consultaFuncionario}"
                                                                 reRender="funcionario,dias,resumo,f_messagens"/>

                                                </h:selectOneMenu>
                                                <h:outputLink id="filtroFuncionarioComEscala" value="#">
                                                    <center>
                                                        <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                    </center>
                                                    <rich:componentControl for="filtroFuncionarioComEscalaModalPanel"
                                                                           attachTo="filtroFuncionarioComEscala" operation="show" event="onclick"/>
                                                </h:outputLink>

                                                <h:selectOneMenu id="funcionario" value="#{consultaFrequenciaComEscalaBean.cod_funcionario}">
                                                    <f:selectItems value="#{consultaFrequenciaComEscalaBean.funcionarioList}"/>
                                                    <a4j:support event="onchange"  action="#{consultaFrequenciaComEscalaBean.consultaDias}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </h:selectOneMenu>
                                                <rich:spacer width="10"/>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaComEscalaBean.objLocale}" value="#{consultaFrequenciaComEscalaBean.dataInicio}" >
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.consultaDias}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="10"/>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaComEscalaBean.objLocale}" value="#{consultaFrequenciaComEscalaBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.consultaDias}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </rich:calendar> 
                                                <h:panelGroup>
                                                    <rich:spacer width="15"/>                                                   
                                                    <h:commandButton image="../images/impressora.png" value="imprimir"
                                                                     action="#{consultaFrequenciaComEscalaBean.imprimirRelatorio}"/>
                                                </h:panelGroup>

                                                <h:panelGroup>
                                                    <h:selectBooleanCheckbox value="#{consultaFrequenciaComEscalaBean.incluirSubSetores}">
                                                        <a4j:support event="onchange" action="#{consultaFrequenciaComEscalaBean.consultaFuncionario}"
                                                                     reRender="funcionario"/>
                                                    </h:selectBooleanCheckbox>
                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                            <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
                                            <rich:modalPanel id="panelStatus" autosized="true" >
                                                <h:panelGrid columns="3">
                                                    <h:graphicImage url="../images/load.gif" />
                                                    <rich:spacer width="8"/>
                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                </h:panelGrid>
                                            </rich:modalPanel>
                                            <rich:modalPanel id="filtroFuncionarioComEscalaModalPanel"
                                                             width="750" height="350"  style="text-align:center;float:center;" >
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Filtrar funcionários"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                        <rich:componentControl for="filtroFuncionarioComEscalaModalPanel" attachTo="hidelink" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <fieldset class="demo_fieldset" >
                                                        <legend style="font-weight: bold;">Por Cargo</legend>
                                                        <h:panelGroup>
                                                            <h:selectOneMenu value="#{consultaFrequenciaComEscalaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                                <f:selectItems value="#{consultaFrequenciaComEscalaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                            </h:selectOneMenu>
                                                        </h:panelGroup>
                                                    </fieldset>
                                                    <br>
                                                    <fieldset class="demo_fieldset" >
                                                        <legend style="font-weight: bold;">Por Regime</legend>
                                                        <h:panelGroup>
                                                            <h:selectOneRadio
                                                                value="#{consultaFrequenciaComEscalaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                                <f:selectItems
                                                                    value="#{consultaFrequenciaComEscalaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                            </h:selectOneRadio>
                                                        </h:panelGroup>
                                                    </fieldset>
                                                    <br> 
                                                    <fieldset class="demo_fieldset" >
                                                        <legend style="font-weight: bold;">Por Gestor</legend>
                                                        <h:panelGroup>
                                                            <h:selectOneRadio
                                                                value="#{consultaFrequenciaComEscalaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                                <f:selectItems
                                                                    value="#{consultaFrequenciaComEscalaBean.gestorFiltroFuncionarioList}"/>
                                                            </h:selectOneRadio>
                                                        </h:panelGroup>
                                                    </fieldset>
                                                    <br> <br>
                                                    <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegime"
                                                                     action="#{consultaFrequenciaComEscalaBean.consultaFuncionario}">
                                                        <rich:componentControl for="filtroFuncionarioComEscalaModalPanel" attachTo="confirmarOpcaoFiltroRegime"
                                                                               operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </rich:modalPanel>
                                        </center>
                                        <h:panelGroup id="resumo" >
                                            <center>
                                                <rich:spacer height="8"/>
                                                <rich:dataTable id="resumoList"                                                          
                                                                border="0"
                                                                value="#{consultaFrequenciaComEscalaBean.resumoList}"
                                                                var="resumo"
                                                                rowClasses="zebra1,zebra2"
                                                                rendered="#{not empty consultaFrequenciaComEscalaBean.diasList}"
                                                                rows="1"
                                                                >
                                                    <f:facet name="header">
                                                        <h:outputText value="RESUMO DO PERÍODO"/>
                                                    </f:facet>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="H. CONTRATADAS" title="HORAS CONTRATADAS"/>
                                                        </f:facet>
                                                        <h:outputText  value="#{resumo.horasContratadas}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="H. TRABALHADAS" title="HORAS TRABALHADAS"/>
                                                        </f:facet>
                                                        <h:outputText value="#{resumo.horasTrabalhadas}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="SALDO"/>
                                                        </f:facet>
                                                        <h:outputText value=" #{resumo.saldo}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="D. CONTRATADOS" title="DIAS CONTRATADOS"/>
                                                        </f:facet>
                                                        <h:outputText value="#{resumo.diasContratados}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="D. TRABALHADOS" title="DIAS TRABALHADOS"/>
                                                        </f:facet>
                                                        <h:outputText value="#{resumo.diasTrabalhados}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="FALTA"/>
                                                        </f:facet>
                                                        <h:outputText value=" #{resumo.faltas}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="HORA EXTRA"/>
                                                        </f:facet>
                                                        <h:outputText value=" #{resumo.horaExtra}"/>
                                                    </rich:column>
                                                    <rich:column style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="ADICIONAL NOTURNO"/>
                                                        </f:facet>
                                                        <h:outputText value=" #{resumo.adicionalNoturno}"/>
                                                    </rich:column>
                                                </rich:dataTable>
                                                <%--  <h:panelGrid rendered="#{not empty consultaFrequenciaComEscalaBean.diasList}" columns="1">
                                                    <h:panelGrid columns="18" styleClass="resumo" >
                                                        <h:outputText value="Horas Contratadas: " styleClass="label"/>
                                                        <h:outputText value="#{consultaFrequenciaComEscalaBean.horasASeremTrabalhadasTotal}"/>
                                                        <rich:spacer width="15"/>
                                                        <h:outputText value="Horas Trabalhadas: " styleClass="label"/>
                                                        <h:outputText value="#{consultaFrequenciaComEscalaBean.horasTotal}"/>
                                                        <rich:spacer width="15"/>
                                                        <h:outputText value="Saldo: " styleClass="label"/>
                                                        <h:outputText value=" #{consultaFrequenciaComEscalaBean.saldoTotal}"/>
                                                        <rich:spacer width="15"/>
                                                        <h:outputText value="Dias Contratados: " styleClass="label"/>
                                                        <h:outputText value="#{consultaFrequenciaComEscalaBean.contDiasATrabalhar}"/>
                                                        <rich:spacer width="15"/>
                                                        <h:outputText value="Dias Trabalhados: " styleClass="label"/>
                                                        <h:outputText value="#{consultaFrequenciaComEscalaBean.contDiasTrabalhados}"/>
                                                        <rich:spacer width="15"/>
                                                        <h:outputText value="Faltas: " styleClass="label"/>
                                                        <h:outputText value=" #{consultaFrequenciaComEscalaBean.faltas}"/>
                                                    </h:panelGrid>
                                                    <h:panelGroup>
                                                        <center>
                                                            <h:panelGrid columns="16" styleClass="resumo">
                                                                <h:outputText value="Hora Extra: " styleClass="label"/>
                                                                <h:outputText value=" #{consultaFrequenciaComEscalaBean.horaExtra}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Adicional noturno: " styleClass="label"/>
                                                                <h:outputText value=" #{consultaFrequenciaComEscalaBean.adicionalNoturnoStr}"/>
                                                                <rich:spacer width="15"/>
                                                <%--     <h:panelGroup rendered="#{consultaFrequenciaComEscalaBean.gratificacaoStr != ' Sem gratificações'}">
                                                         <h:outputText value="Gratificação: " styleClass="label"/>
                                                         <h:outputText value=" #{consultaFrequenciaComEscalaBean.gratificacaoStr}"/>
                                                         <rich:spacer width="15"/>
                                                     </h:panelGroup>
                                                      <h:outputText value="Descontos de DSR: " styleClass="label"/>
                                                     <h:outputText value=" #{consultaFrequenciaComEscalaBean.qntDSR}"/>
                                                     <rich:spacer width="15"/>-%>
                                            </h:panelGrid>
                                        </center>
                                    </h:panelGroup>
                                </h:panelGrid>      --%>
                                            </center>
                                        </h:panelGroup>
                                        <h:panelGroup id="dias" >
                                            <center>
                                                <h:panelGrid columns="1" rendered="#{not empty consultaFrequenciaComEscalaBean.diasComRegistroSemEscalaList}" >
                                                    <rich:spacer height="8"/>
                                                    <rich:panel>
                                                        <f:facet name="header">
                                                            <h:panelGroup>
                                                                <center>
                                                                    <h:column>
                                                                        <h:outputText
                                                                            value="DIAS SEM ESCALA E COM REGISTRO DE PONTO"/>
                                                                    </h:column>
                                                                </center>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <center>
                                                            <rich:dataGrid
                                                                value="#{consultaFrequenciaComEscalaBean.diasComRegistroSemEscalaList}"
                                                                var="pontoAcessoT"
                                                                columns="#{consultaFrequenciaComEscalaBean.columNumberSemEscala}"
                                                                columnClasses="cellTop, cellTop, cellTop, cellTop, cellTop"
                                                                >
                                                                <rich:panel >
                                                                    <f:facet name="header" >
                                                                        <h:panelGroup>
                                                                            <center>
                                                                                <h:outputText value="#{pontoAcessoT.data}">
                                                                                    <f:convertDateTime pattern="EE dd/MM/yyyy"/>
                                                                                </h:outputText>
                                                                            </center>
                                                                        </h:panelGroup>
                                                                    </f:facet>
                                                                    <rich:dataTable
                                                                        value="#{pontoAcessoT.horasList}"
                                                                        var="horasList"
                                                                        rendered="#{not empty pontoAcessoT.horasList}"
                                                                        width="100px"
                                                                        styleClass="datatableTop">
                                                                        <f:facet name="header">
                                                                        </f:facet>
                                                                        <center>
                                                                            <h:column>
                                                                                <center>
                                                                                    <h:outputText value="#{horasList}" >
                                                                                        <f:convertDateTime pattern="HH:mm:ss" />
                                                                                    </h:outputText>
                                                                                </center>
                                                                            </h:column>
                                                                        </center>
                                                                    </rich:dataTable>
                                                                </rich:panel>
                                                            </rich:dataGrid>
                                                        </center>
                                                    </rich:panel>
                                                </h:panelGrid>
                                                <h:panelGrid columns="1" rendered="#{not empty consultaFrequenciaComEscalaBean.diasList}">
                                                    <rich:spacer height="8"/>
                                                    <center>
                                                        <rich:panel id="panel">
                                                            <center>
                                                                <f:facet name="header">
                                                                    <h:panelGroup>
                                                                        <center>
                                                                            <h:column>
                                                                                <h:outputText
                                                                                    value="#{consultaFrequenciaComEscalaBean.nome}"/>
                                                                            </h:column>
                                                                        </center>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                            </center>
                                                            <rich:dataGrid id="datagrid"
                                                                           value="#{consultaFrequenciaComEscalaBean.diasList}"
                                                                           var="dia"
                                                                           columns="#{consultaFrequenciaComEscalaBean.columNumberComEscala}" 
                                                                           columnClasses="cellTop, cellTop, cellTop, cellTop, cellTop"
                                                                           elements="25">
                                                                <rich:panel id="diaPanel"   styleClass="#{dia.colorDia}" rendered="#{dia.presente}"
                                                                            onmouseover="this.className='panelOver'"
                                                                            onmouseout="this.className='#{dia.colorDia}'" >
                                                                    <a4j:support  event="onclick" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.navegarDiaPonto}"  >
                                                                        <a4j:actionparam name="diaParam" value="#{dia.diaString}"/>
                                                                    </a4j:support>

                                                                    <f:facet name="header">
                                                                        <h:panelGroup>                                                                            
                                                                            <h:outputText  value="#{dia.diaString}"/>
                                                                            <h:outputText value=" (Feriado)" rendered="#{dia.isDiaCritico}"/>
                                                                            <h:graphicImage style="float:right;" value="../images/aguarda_abono.png" rendered="#{dia.isPendente}"
                                                                                            title="Aguardando analise da solicitação de abono"/>
                                                                        </h:panelGroup>
                                                                    </f:facet>
                                                                    <center>
                                                                        <table border="1">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>TURNO</th>
                                                                                    <th>ENTRADA</th>
                                                                                    <th>SAIDA</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td>1</td>
                                                                                    <td>
                                                                                        <h:outputText value="#{dia.entrada_1.registro}"/>
                                                                                    </td>
                                                                                    <td> <h:outputText value="#{dia.saida_1.registro}" /></td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>2</td>
                                                                                    <td> <h:outputText value="#{dia.entrada_2.registro}" /></td>
                                                                                    <td> <h:outputText value="#{dia.saida_2.registro}" /></td>
                                                                                </tr>
                                                                            </tbody>
                                                                        </table>
                                                                    </center>
                                                                    <br/>
                                                                    <h:outputText value="TRABALHADAS: " styleClass="label"/>
                                                                    <h:outputText value="#{dia.horasTrabalhadas}"/>
                                                                    <br/>
                                                                    <h:outputText value="SALDO: " styleClass="label"/>
                                                                    <h:outputText value="#{dia.saldoHoras}"/>
                                                                    <br/>
                                                                    <h:outputText value="ATRASO: " styleClass="label"/>
                                                                    <h:outputText value="#{dia.atraso}"/>                                                                    
                                                                </rich:panel>
                                                                <rich:panel id="diaPanelIrregular" styleClass="#{dia.colorDia}" rendered="#{!dia.presente}"
                                                                            onmouseover="this.className='panelOver'"
                                                                            onmouseout="this.className='#{dia.colorDia}'" >
                                                                    <a4j:support event="onclick" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.navegarDiaPonto}"
                                                                                 rendered="#{dia.isAfastado == false}">
                                                                        <a4j:actionparam name="diaParam" value="#{dia.diaString}"/>
                                                                    </a4j:support>
                                                                    <f:facet name="header">
                                                                        <h:panelGroup>
                                                                            <h:outputText  value="#{dia.diaString}"/>
                                                                            <h:graphicImage style="float:right;" value="../images/aguarda_abono.png" rendered="#{dia.isPendente}"
                                                                                            title="Aguardando analise da solicitação de abono"/>
                                                                        </h:panelGroup>
                                                                    </f:facet>
                                                                    <br/>
                                                                    <br/>
                                                                    <br/>
                                                                    <h:outputLabel value="#{dia.justificativa}" styleClass="falta"/>
                                                                    <br/>
                                                                    <br/>
                                                                    <br/>
                                                                    <h:outputText value="SALDO: " styleClass="label" rendered="#{dia.justificativa == 'FALTA'
                                                                                                                                 || dia.justificativa != 'FOLGA COMPENSATÓRIA'}"/>
                                                                    <h:outputText value="#{dia.saldoHoras}" rendered="#{dia.justificativa == 'FALTA'||
                                                                                           dia.justificativa != 'FOLGA COMPENSATÓRIA'}"/>
                                                                    <br/>
                                                                    <br/>
                                                                </rich:panel>
                                                                <f:facet name="footer">
                                                                    <rich:datascroller  id="datascroller" value="#{consultaFrequenciaComEscalaBean.page}"
                                                                                        page="#{consultaFrequenciaComEscalaBean.page}"
                                                                                        renderIfSinglePage="false">
                                                                    </rich:datascroller>
                                                                </f:facet>
                                                            </rich:dataGrid>
                                                        </rich:panel>
                                                    </center>
                                                </h:panelGrid>


                                            </h:panelGroup>
                                        </a4j:region>
                                    </center>
                                </rich:tab>
                                <rich:tab id="tab2" name="tab2" label="Frequência sem escala" ignoreDupResponses="true" rendered="#{usuarioBean.perfil.freqnSemEscala== true}">
                                    <a4j:support  event="ontabenter" action="#{consultaFrequenciaComEscalaBean.setAba}" reRender="f_messagens">>
                                        <a4j:actionparam name="tab" value="tab2"/>
                                    </a4j:support>
                                    <br/>
                                    <center>
                                        <a4j:region id="region2">
                                            <h:panelGrid columns="8" >
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="12"/>

                                                <h:selectOneMenu id="departManuT" value="#{consultaFrequenciaSemEscalaBean.departamento}">
                                                    <f:selectItems value="#{consultaFrequenciaSemEscalaBean.departamentolist}"/>
                                                    <a4j:support event="onchange" action="#{consultaFrequenciaSemEscalaBean.consultaFuncionario}"
                                                                 reRender="diasT,funcionarioT,f_messagens"/>
                                                </h:selectOneMenu>
                                                <rich:spacer width="10"/>

                                                <h:selectOneMenu id="funcionarioT" value="#{consultaFrequenciaSemEscalaBean.cod_funcionario}">
                                                    <f:selectItems value="#{consultaFrequenciaSemEscalaBean.funcionarioList}"/>
                                                    <a4j:support event="onchange"  action="#{consultaFrequenciaSemEscalaBean.consultaDias}"
                                                                 reRender="diasT,f_messagens"/>
                                                </h:selectOneMenu>

                                                <h:outputLink id="filtroFuncionarioSemEscala" value="#">
                                                    <center>
                                                        <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                    </center>
                                                    <rich:componentControl for="filtroFuncionarioSemEscalaModalPanel"
                                                                           attachTo="filtroFuncionarioSemEscala" operation="show" event="onclick"/>
                                                </h:outputLink>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaSemEscalaBean.objLocale}" value="#{consultaFrequenciaSemEscalaBean.dataInicio}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaSemEscalaBean.consultaDias}"
                                                                 reRender="diasT,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="1"/>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaSemEscalaBean.objLocale}" value="#{consultaFrequenciaSemEscalaBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaSemEscalaBean.consultaDias}"
                                                                 reRender="diasT,f_messagens"/>
                                                </rich:calendar>

                                                <h:panelGroup>
                                                    <rich:spacer width="15"/>
                                                    <h:commandButton image="../images/impressora.png" action="#{consultaFrequenciaSemEscalaBean.imprimir}"/>
                                                </h:panelGroup>

                                                <h:panelGroup>
                                                    <h:selectBooleanCheckbox value="#{consultaFrequenciaSemEscalaBean.incluirSubSetores}">
                                                        <a4j:support event="onchange" action="#{consultaFrequenciaSemEscalaBean.consultaFuncionario}" reRender="funcionarioT,diasT"/>
                                                    </h:selectBooleanCheckbox>
                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                        </a4j:region>
                                        <a4j:status id="progressoEmAberto"  for="region2" onstart="Richfaces.showModalPanel('panelStatus2');" onstop="#{rich:component('panelStatus2')}.hide()"/>
                                        <rich:modalPanel id="panelStatus2" autosized="true" >
                                            <h:panelGrid columns="3">
                                                <h:graphicImage url="../images/load.gif" />
                                                <rich:spacer width="8"/>
                                                <h:outputText value="  Carregando…" styleClass="label" />
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="filtroFuncionarioSemEscalaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Filtrar funcionários"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkSemEscala"/>
                                                    <rich:componentControl for="filtroFuncionarioSemEscalaModalPanel" attachTo="hidelinkSemEscala"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Cargo</legend>
                                                    <h:panelGroup>
                                                        <h:selectOneMenu value="#{consultaFrequenciaSemEscalaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                            <f:selectItems value="#{consultaFrequenciaSemEscalaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                        </h:selectOneMenu>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <br>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Regime</legend>
                                                    <h:panelGroup >
                                                        <h:selectOneRadio
                                                            value="#{consultaFrequenciaSemEscalaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                            <f:selectItems
                                                                value="#{consultaFrequenciaSemEscalaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                        </h:selectOneRadio>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <br>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Gestor</legend>
                                                    <h:panelGroup>
                                                        <h:selectOneRadio
                                                            value="#{consultaFrequenciaSemEscalaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                            <f:selectItems
                                                                value="#{consultaFrequenciaSemEscalaBean.gestorFiltroFuncionarioList}"/>
                                                        </h:selectOneRadio>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <br> <br>
                                                <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeSemEscala"
                                                                 action="#{consultaFrequenciaSemEscalaBean.consultaFuncionario}">
                                                    <rich:componentControl for="filtroFuncionarioSemEscalaModalPanel" attachTo="confirmarOpcaoFiltroRegimeSemEscala"
                                                                           operation="hide" event="onclick"/>
                                                </h:commandButton>
                                            </center>
                                        </rich:modalPanel>
                                    </center>
                                    <h:panelGroup id="diasT" >
                                        <center>
                                            <h:panelGrid columns="1" rendered="#{not empty consultaFrequenciaSemEscalaBean.diasList}" >                                               
                                                <rich:panel id="panelT">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:column>
                                                                    <h:outputText
                                                                        value="#{consultaFrequenciaSemEscalaBean.nome}"/>
                                                                </h:column>
                                                            </center>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <rich:dataGrid id="datagridT"
                                                                   value="#{consultaFrequenciaSemEscalaBean.diasList}"
                                                                   var="pontoAcessoT"
                                                                   columns="5"
                                                                   columnClasses="cellTop, cellTop, cellTop, cellTop, cellTop"
                                                                   >
                                                        <rich:panel >
                                                            <f:facet name="header" >
                                                                <h:panelGroup>
                                                                    <center>
                                                                        <h:outputText value="#{pontoAcessoT.data}">
                                                                            <f:convertDateTime pattern="EE dd/MM/yyyy"/>
                                                                        </h:outputText>
                                                                    </center>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <rich:dataTable
                                                                value="#{pontoAcessoT.horasList}"
                                                                var="horasList"
                                                                rendered="#{not empty pontoAcessoT.horasList}"
                                                                width="100px"
                                                                styleClass="datatableTop">
                                                                <f:facet name="header">
                                                                </f:facet>
                                                                <center>
                                                                    <h:column>
                                                                        <center>
                                                                            <h:outputText value="#{horasList}" >
                                                                                <f:convertDateTime pattern="HH:mm:ss" />
                                                                            </h:outputText>
                                                                        </center>
                                                                    </h:column>
                                                                </center>
                                                            </rich:dataTable>
                                                        </rich:panel>
                                                    </rich:dataGrid>
                                                </rich:panel>
                                            </h:panelGrid>
                                        </center>
                                    </h:panelGroup>
                                </rich:tab>
                                <rich:tab id="tab3" name="tab3" label="Hora Extra" ignoreDupResponses="true" rendered="#{usuarioBean.perfil.horaExtra== true}">
                                    <a4j:support  event="ontabenter" action="#{consultaFrequenciaComEscalaBean.setAba}" reRender="f_messagens">
                                        <a4j:actionparam name="tab" value="tab3"/>
                                    </a4j:support>
                                    <br/>
                                    <center>
                                        <a4j:region id="region3">
                                            <h:panelGrid columns="8" >
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="12"/>

                                                <h:selectOneMenu id="departManuH" value="#{consultaFrequenciaHoraExtraBean.departamento}">
                                                    <f:selectItems value="#{consultaFrequenciaHoraExtraBean.departamentolist}"/>
                                                    <a4j:support event="onchange" action="#{consultaFrequenciaHoraExtraBean.consultaFuncionario}"
                                                                 reRender="diasH,funcionarioH"/>
                                                </h:selectOneMenu>
                                                <rich:spacer width="12"/>

                                                <h:selectOneMenu id="funcionarioH" value="#{consultaFrequenciaHoraExtraBean.cod_funcionario}">
                                                    <f:selectItems value="#{consultaFrequenciaHoraExtraBean.funcionarioList}"/>
                                                    <a4j:support event="onchange"  action="#{consultaFrequenciaHoraExtraBean.consultaDias}"
                                                                 reRender="diasH,f_messagens"/>
                                                </h:selectOneMenu>

                                                <h:outputLink id="filtroFuncionarioHoraExtra" value="#">
                                                    <center>
                                                        <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                    </center>
                                                    <rich:componentControl for="filtroFuncionarioHoraExtraModalPanel"
                                                                           attachTo="filtroFuncionarioHoraExtra" operation="show" event="onclick"/>
                                                </h:outputLink>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaHoraExtraBean.objLocale}" value="#{consultaFrequenciaHoraExtraBean.dataInicio}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaHoraExtraBean.consultaDias}"
                                                                 reRender="diasH,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="12"/>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaHoraExtraBean.objLocale}" value="#{consultaFrequenciaHoraExtraBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaHoraExtraBean.consultaDias}"
                                                                 reRender="diasH,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="1"/>

                                                <h:panelGroup>
                                                    <h:selectBooleanCheckbox value="#{consultaFrequenciaHoraExtraBean.incluirSubSetores}">
                                                        <a4j:support event="onchange" action="#{consultaFrequenciaHoraExtraBean.consultaFuncionario}"
                                                                     reRender="diasH,funcionarioH"/>
                                                    </h:selectBooleanCheckbox>
                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                </h:panelGroup>
                                            </h:panelGrid>                                            
                                        </a4j:region>
                                        <a4j:status id="progressoEmAberto"  for="region3" onstart="Richfaces.showModalPanel('panelStatus3');" onstop="#{rich:component('panelStatus3')}.hide()"/>
                                        <rich:modalPanel id="panelStatus3" autosized="true" >
                                            <h:panelGrid columns="3">
                                                <h:graphicImage url="../images/load.gif" />
                                                <rich:spacer width="8"/>
                                                <h:outputText value="  Carregando…" styleClass="label" />
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="filtroFuncionarioHoraExtraModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Filtrar funcionários"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkHoraExtra"/>
                                                    <rich:componentControl for="filtroFuncionarioHoraExtraModalPanel" attachTo="hidelinkHoraExtra"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Cargo</legend>
                                                    <h:panelGroup>
                                                        <h:selectOneMenu value="#{consultaFrequenciaHoraExtraBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                            <f:selectItems value="#{consultaFrequenciaHoraExtraBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                        </h:selectOneMenu>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Regime</legend>
                                                    <h:panelGroup >
                                                        <h:selectOneRadio
                                                            value="#{consultaFrequenciaHoraExtraBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                            <f:selectItems
                                                                value="#{consultaFrequenciaHoraExtraBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                        </h:selectOneRadio>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <br>
                                                <fieldset class="demo_fieldset" >
                                                    <legend style="font-weight: bold;">Por Gestor</legend>
                                                    <h:panelGroup>
                                                        <h:selectOneRadio
                                                            value="#{consultaFrequenciaHoraExtraBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                            <f:selectItems
                                                                value="#{consultaFrequenciaHoraExtraBean.gestorFiltroFuncionarioList}"/>
                                                        </h:selectOneRadio>
                                                    </h:panelGroup>
                                                </fieldset>
                                                <br> <br>

                                                <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeHoraExtra"
                                                                 action="#{consultaFrequenciaHoraExtraBean.consultaFuncionario}">
                                                    <rich:componentControl for="filtroFuncionarioHoraExtraModalPanel" attachTo="confirmarOpcaoFiltroRegimeHoraExtra"
                                                                           operation="hide" event="onclick"/>
                                                </h:commandButton>
                                            </center>
                                        </rich:modalPanel>
                                    </center>
                                    <br/>
                                    <h:panelGroup id="diasH" >
                                        <center>
                                            <h:panelGrid columns="1" rendered="#{not empty consultaFrequenciaHoraExtraBean.diasList}" style="text-align:center;float:center;">
                                                <rich:panel id="panelH">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:column>
                                                                    <h:outputText
                                                                        value="#{consultaFrequenciaHoraExtraBean.nome}"/>
                                                                </h:column>
                                                            </center>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <rich:dataGrid id="datagridH"
                                                                   value="#{consultaFrequenciaHoraExtraBean.diasList}"
                                                                   var="diaH"
                                                                   columns="5"
                                                                   columnClasses="cellTop, cellTop, cellTop, cellTop, cellTop"
                                                                   >
                                                        <rich:panel onmouseover="this.style.backgroundColor='#fbfbc0'"
                                                                    onmouseout="this.style.backgroundColor='#FFFFFF'" >
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <center>
                                                                        <h:outputText value="#{diaH.data}">
                                                                            <f:convertDateTime pattern="EE dd/MM/yyyy"/>
                                                                        </h:outputText>
                                                                    </center>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <center>
                                                                <h:commandLink action="#{consultaFrequenciaHoraExtraBean.navegar2DiaHoraExtraBean}">
                                                                    <h:graphicImage value="#{diaH.imagem}" style="border:0"/>
                                                                    <f:param name="diaParam" value="#{diaH.dataString}"/>
                                                                    <f:param name="userid" value="#{consultaFrequenciaHoraExtraBean.cod_funcionario}"/>
                                                                    <f:param name="saldo" value="#{diaH.saldo}"/>
                                                                </h:commandLink>
                                                                <br/>
                                                                <h:outputText value="#{diaH.saldo}"/>
                                                            </center>
                                                        </rich:panel>
                                                    </rich:dataGrid>
                                                    <br>
                                                    <a4j:commandButton value="Liberar Todas" id = "horaemMassaButao" >
                                                        <rich:componentControl for="horaExtraEmMassaModalPanel" attachTo="horaemMassaButao" operation="show" event="onclick"/>
                                                    </a4j:commandButton>
                                                </rich:panel>

                                            </h:panelGrid>
                                            <rich:modalPanel id="horaExtraEmMassaModalPanel"
                                                             width="350" height="215"  style="text-align:center;float:center;" >
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Hora Extra"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkHoraExtraEmMassa"/>
                                                        <rich:componentControl for="horaExtraEmMassaModalPanel" attachTo="hidelinkHoraExtraEmMassa"
                                                                               operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:outputLabel value="Categoria:" styleClass="label"/>
                                                    <br/>
                                                    <h:selectOneMenu value="#{consultaFrequenciaHoraExtraBean.cod_categoria}">
                                                        <f:selectItems value="#{consultaFrequenciaHoraExtraBean.categoriaJustificativaList}"/>
                                                        <a4j:support event="onclick" reRender="info"/>
                                                    </h:selectOneMenu>
                                                    <br/>
                                                    <br/>
                                                    <h:panelGrid columns="1" style="text-align:center;">
                                                        <h:outputLabel value="Justificativa:" styleClass="label"/>
                                                        <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                          cols="40" rows="3" value="#{consultaFrequenciaHoraExtraBean.justificativa}" >
                                                        </h:inputTextarea>
                                                    </h:panelGrid>
                                                    <br/>
                                                    <h:panelGroup>
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:commandButton action="#{consultaFrequenciaHoraExtraBean.inserirHoraExtra}"
                                                                                 value="Confirmar" id="botaoAbonarHE">
                                                                    <rich:componentControl for="horaExtraEmMassaModalPanel" attachTo="botaoAbonarHE"
                                                                                           operation="hide" event="onclick"/>
                                                                </h:commandButton>
                                                            </center>
                                                        </h:panelGroup>
                                                    </h:panelGroup>
                                                </center>
                                            </rich:modalPanel>

                                        </center>
                                    </h:panelGroup>
                                </rich:tab>
                            </rich:tabPanel>
                        </center>
                    </h:panelGrid>
                </h:form>
                <jsp:include page="../www/_bot.jsp"/>
            </center>
        </f:view>
    </body>
</html>