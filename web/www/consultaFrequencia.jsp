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
        <title>Consulta Usuário</title>
    </head>
    <body>
        <f:view>

            <jsp:include page="_top.jsp"/>
            <h:form id="f_messagens" prependId="false">
                <center>
                    <h:messages infoClass="info" />
                </center>
            </h:form>
            <center>
                <h:form id="form">
                    <h:panelGrid columns="1" >
                        <center>
                            <rich:tabPanel switchType="client" width="965">
                                <rich:tab id="tab1" label="Frequência com escala">
                                    <br/>
                                    <center>
                                        <a4j:region id="region1">
                                            <h:panelGrid columns="8" >
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText  value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="#{consultaFrequenciaComEscalaBean.departamentoSrt}" />
                                                <rich:spacer width="30"/>
                                                <h:outputText value="#{consultaFrequenciaComEscalaBean.nome}" />
                                                <rich:spacer width="30"/>
                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaComEscalaBean.objLocale}" value="#{consultaFrequenciaComEscalaBean.dataInicio}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.consultaDias}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="30"/>

                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaComEscalaBean.objLocale}" value="#{consultaFrequenciaComEscalaBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.consultaDias}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="1"/>
                                            </h:panelGrid>
                                        </center>

                                        <h:panelGroup id="resumo">
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

                                            </center>
                                        </h:panelGroup>


                                        <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
                                        <rich:modalPanel id="panelStatus" autosized="true" >
                                            <h:panelGrid columns="3">
                                                <h:graphicImage url="../images/load.gif" />
                                                <rich:spacer width="8"/>
                                                <h:outputText value="  Carregando…" styleClass="label" />
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                        <br/>
                                        <h:panelGroup id="dias">
                                            <center>
                                                <h:panelGrid columns="1" columnClasses="gridContent"
                                                             rendered="#{not empty consultaFrequenciaComEscalaBean.diasList}">
                                                    <rich:panel id="panel">
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
                                                        <rich:dataGrid id="datagrid" value="#{consultaFrequenciaComEscalaBean.diasList}"
                                                                       var="dia" columns="5" elements="25">
                                                            <rich:panel styleClass="#{dia.colorDia}" rendered="#{dia.presente}"
                                                                        onmouseover="this.className='panelOver'"
                                                                        onmouseout="this.className='#{dia.colorDia}'" >
                                                                <a4j:support event="onclick" ajaxSingle="true"  action="#{consultaFrequenciaComEscalaBean.navegarDiaPonto}" >
                                                                    <a4j:actionparam name="diaParam" value="#{dia.diaString}"/>
                                                                </a4j:support>
                                                                <f:facet name="header">
                                                                    <h:panelGroup>
                                                                        <center>
                                                                            <h:outputText value="#{dia.diaString}"/>
                                                                            <h:graphicImage style="float:right;" value="../images/aguarda_abono.png"
                                                                                            rendered="#{dia.isPendente}"/>
                                                                        </center>
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
                                                                <%--   <h:outputText value="TRABALHADAS: " styleClass="label"/>
                                                                     <h:outputText value="#{dia.horasTrabalhadas}"/>
                                                                     <br/>
                                                                     <h:outputText value="SALDO: " styleClass="label"/>
                                                                     <h:outputText value="#{dia.saldoHoras}"/>
                                                                     <br/>
                                                                     <h:outputText value="ATRASO: " styleClass="label"/>
                                                                     <h:outputText value="#{dia.atraso}"/>--%>
                                                            </rich:panel>
                                                            <rich:panel styleClass="#{dia.colorDia}" rendered="#{!dia.presente}"
                                                                        onmouseover="this.className='panelOver'"
                                                                        onmouseout="this.className='#{dia.colorDia}'" >
                                                                <a4j:support event="onclick" ajaxSingle="true" rendered="#{dia.isAfastado == false}"
                                                                             action="#{consultaFrequenciaComEscalaBean.navegarDiaPonto}" >
                                                                    <a4j:actionparam name="diaParam" value="#{dia.diaString}"/>
                                                                </a4j:support>
                                                                <f:facet name="header">
                                                                    <h:panelGroup>
                                                                        <center>
                                                                            <h:outputText value="#{dia.diaString}"/>
                                                                            <h:graphicImage style="float:right;" value="../images/aguarda_abono.png"
                                                                                            rendered="#{dia.isPendente}"/>
                                                                        </center>
                                                                    </h:panelGroup>
                                                                </f:facet>
                                                                <br/>
                                                                <br/>
                                                                <h:outputLabel value="#{dia.justificativa}" styleClass="falta"/>
                                                                <br/>
                                                                <%--    <br/>
                                                                  <br/>
                                                                  <br/>
                                                                   <h:outputText value="SALDO: " styleClass="label" rendered="#{dia.justificativa == 'FALTA'
                                                                                                                                || dia.justificativa == 'FOLGA COMPENSATÓRIA'}"/>
                                                                   <h:outputText value="#{dia.saldoHoras}" rendered="#{dia.justificativa == 'FALTA'||
                                                                                          dia.justificativa == 'FOLGA COMPENSATÓRIA'}"/>--%>
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
                                                </h:panelGrid>
                                            </center>
                                        </h:panelGroup>
                                    </a4j:region>
                                </rich:tab>
                                <rich:tab id="tab2" label="Frequência sem escala">
                                    <br/>
                                    <center>
                                        <a4j:region id="region2">
                                            <h:panelGrid columns="8" >
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="30"/>
                                                <h:outputText value="#{consultaFrequenciaSemEscalaBean.departamentoSrt}" />
                                                <rich:spacer width="30"/>
                                                <h:outputText value="#{consultaFrequenciaSemEscalaBean.nome}" />
                                                <rich:spacer width="30"/>
                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaSemEscalaBean.objLocale}" value="#{consultaFrequenciaSemEscalaBean.dataInicio}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaSemEscalaBean.consultaDias}"
                                                                 reRender="diasT,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="30"/>
                                                <rich:calendar inputSize="8" locale="#{consultaFrequenciaSemEscalaBean.objLocale}" value="#{consultaFrequenciaSemEscalaBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{consultaFrequenciaSemEscalaBean.consultaDias}"
                                                                 reRender="diasT,f_messagens"/>
                                                </rich:calendar>
                                                <rich:spacer width="1"/>
                                            </h:panelGrid>
                                        </a4j:region>
                                        <a4j:status id="progressoEmAberto2"  for="region2" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
                                        <rich:modalPanel id="panelStatus2" autosized="true" >
                                            <h:panelGrid columns="3">
                                                <h:graphicImage url="../images/load.gif" />
                                                <rich:spacer width="8"/>
                                                <h:outputText value="  Carregando…" styleClass="label" />
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                    </center>
                                    <br/>
                                    <h:panelGroup id="diasT" >
                                        <center>
                                            <h:panelGrid columns="1" columnClasses="gridContent" rendered="#{not empty consultaFrequenciaSemEscalaBean.diasList}">
                                                <rich:panel id="panelT">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:column>
                                                                    <h:outputText  value="#{consultaFrequenciaSemEscalaBean.nome}"/>
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
                                                        <rich:panel>
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
                                                                styleClass="datatableTop"
                                                                width="100px">
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
                                        </h:panelGroup>
                                    </center>
                                </rich:tab>
                            </rich:tabPanel>
                        </center>
                    </h:panelGrid>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>