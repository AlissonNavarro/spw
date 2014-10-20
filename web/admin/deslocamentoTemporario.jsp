<%--
    Document   : consultaFrequenciaAdmin
    Created on : 21/12/2009, 17:08:40
    Author     : Amvboas
--%>
<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<%@ page import="CadastroCronograma.CadastroCronogramaBean" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%
            String param = request.getParameter("param");
            if (param != null) {
                CadastroCronogramaBean cadastroCronogramaBean = (CadastroCronogramaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cadastroCronogramaBean");

                if (param.equals("atualizar")) {
                   cadastroCronogramaBean.consultaHorariosList();
                }
            }
%>

<%@ page import="javax.faces.context.FacesContext" %>
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
        <title>Deslocamento Temporário</title>
    </head>
    <body>
        <center>
            <f:view >
                <jsp:include page="../www/_top.jsp"/>

                <h:form id="f_messagens" prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="infoConflito"/>
                    </a4j:outputPanel>
                </h:form>
                <center>
                    <h:form id="form" >
                        <rich:tabPanel switchType="client" width="965"  >
                            <rich:tab label="Deslocamento Temporário" style="text-align:center;float:center"
                                      oncomplete="#{cadastroCronogramaBean.getAbaCorrente}">
                                <center>
                                    <br>
                                    <h:outputText value="#{cadastroCronogramaBean.tituloDeslocTemp}" styleClass="label" />
                                    <br>
                                    <h:panelGroup>
                                        <center>
                                            <h:panelGrid columns="1" width="100%">
                                                <br>
                                                <h:panelGrid columns="3" width="100%" columnClasses="colum30,colum05,colum70">
                                                    <rich:extendedDataTable
                                                        selectionMode= "none"
                                                        enableContextMenu="false"
                                                        id="horariosExtendListTemp"
                                                        value="#{cadastroCronogramaBean.horariosList}" var="turno"
                                                        height="400px">
                                                        <rich:column sortable="false" id="marcado0Temp" width="30px">
                                                            <f:facet name="header">
                                                            </f:facet>
                                                            <%--<h:outputText value="#{turno.marked}" /> --%>
                                                            <h:selectBooleanCheckbox
                                                                value="#{turno.marked}" >
                                                                <%--  <a4j:support action="#{cadastroCronogramaBean.changeVFturno}"  event="onclick">
                                                                      <f:param name="turno_id" value="#{turno.id}"/>
                                                                  </a4j:support>--%>
                                                            </h:selectBooleanCheckbox>
                                                        </rich:column>
                                                        <rich:column sortable="false" id="nomeHorarioTemp" width="115px">
                                                            <f:facet name="header">
                                                                <h:outputText value="Nome do Horário" />
                                                            </f:facet>
                                                            <center>
                                                                <h:outputText value="#{turno.nome}" />
                                                            </center>
                                                        </rich:column>
                                                        <rich:column sortable="false" id="entradaSaidaColunaTemp" width="115px">
                                                            <f:facet name="header">
                                                                <h:outputText value="Entrada - Saída" />
                                                            </f:facet>
                                                            <center>
                                                                <h:outputText value="#{turno.stringEntradaSaida}" />
                                                            </center>
                                                        </rich:column>
                                                    </rich:extendedDataTable>

                                                    <h:panelGrid columns="1" style="margin:0 0 0 18px">
                                                        <h:outputText value="Horário Extra"/>
                                                        <h:selectBooleanCheckbox value="#{cadastroCronogramaBean.isOverTime}"></h:selectBooleanCheckbox>

                                                        <a4j:commandButton image="../images/forward_32.png"
                                                                           action="#{cadastroCronogramaBean.adicionarTurnos}"
                                                                           reRender="horariosExtendListTemp,tabelaDeslocTemp,addTurnoPanelTempMessage"/>
                                                    </h:panelGrid>
                                                    <a4j:region id="region1">
                                                        <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
                                                        <rich:modalPanel id="panelStatus" autosized="true" >
                                                            <h:panelGrid columns="3">
                                                                <h:graphicImage url="../images/load.gif" />
                                                                <rich:spacer width="8"/>
                                                                <h:outputText value="  Carregando…" styleClass="label" />
                                                            </h:panelGrid>
                                                        </rich:modalPanel>
                                                        <rich:extendedDataTable
                                                            selectionMode= "none"
                                                            enableContextMenu="false"
                                                            id="tabelaDeslocTemp"
                                                            value="#{cadastroCronogramaBean.diasCronogramaList}" var="diaCronograma"
                                                            width="100%"
                                                            height="400px">
                                                            <rich:column sortable="false" id="marcado2Temp" width="32px">
                                                                <f:facet name="header">
                                                                </f:facet>
                                                                <h:selectBooleanCheckbox                                                               
                                                                    value="#{diaCronograma.marked}" >                                                               
                                                                </h:selectBooleanCheckbox>
                                                            </rich:column>
                                                            <rich:column sortable="false" id="diaDoCronogramaTemp"  style="text-align:center">
                                                                <f:facet name="header">
                                                                    <h:outputText value="Dia" />
                                                                </f:facet>
                                                                <h:outputText value="#{diaCronograma.diaString}" />
                                                            </rich:column>
                                                            <rich:column sortable="false" id="turnosAssociadosCronogramaTemp" width="42%" >
                                                                <f:facet name="header">
                                                                    <h:outputText value="Horários" />
                                                                </f:facet>
                                                                <h:outputText value="#{diaCronograma.horarioStrList.get(0)}" style="#{diaCronograma.cssHorario}" rendered="#{diaCronograma.horarioStrList.size() >= 1}"/>
                                                                <h:outputText value="#{diaCronograma.horarioStrList.get(1)}" style="#{diaCronograma.cssHorario}" rendered="#{diaCronograma.horarioStrList.size() >= 2}"/>
                                                            </rich:column>
                                                            <%--<rich:column sortable="false" width="60px" style="text-align:center">
                                                                <f:facet name="header">
                                                                    <h:outputText value="Dia Extra" />
                                                                </f:facet>
                                                                <h:selectBooleanCheckbox rendered="#{diaCronograma.horarios != ''}"
                                                                    value="#{diaCronograma.isOverTime}"/>
                                                            </rich:column>--%>
                                                            <rich:column sortable="false" width="60px" style="text-align:center">
                                                                <f:facet name="header">
                                                                    <h:outputText value="Folga C."/>
                                                                </f:facet>
                                                                <center>
                                                                    <a4j:commandButton id="BotaoFolgaCompensatoria" image="../images/casa_1.png"
                                                                                       action="#{diaCronograma.atribuiFolga}"
                                                                                       reRender="tabelaDeslocTemp"
                                                                                       rendered="#{diaCronograma.temHorario == true && !(diaCronograma.isFolga)}"
                                                                                       onclick="javascript:if (!confirm('Realmente deseja atribuir folga compensatória?')) return false;">
                                                                    </a4j:commandButton>
                                                                </center>
                                                            </rich:column>
                                                            <rich:column sortable="false" id="limparDiaCronogramaTemp" width="55px">
                                                                <f:facet name="header">
                                                                    <h:outputText value="Limpa H."/>
                                                                </f:facet>
                                                                <center>
                                                                    <a4j:commandButton id="limparBotaoCronogramaTemp" image="../images/espanador_22.png"
                                                                                       action="#{diaCronograma.limparTurno}"
                                                                                       reRender="tabelaDeslocTemp"
                                                                                       rendered="#{diaCronograma.temHorario == true && !(diaCronograma.isFolga) && !(diaCronograma.isDeslTemp)}"
                                                                                       onclick="javascript:if (!confirm('Realmente deseja limpar?')) return false;">
                                                                    </a4j:commandButton>
                                                                </center>
                                                            </rich:column>
                                                            <rich:column sortable="false" id="removeDeslocamentoTemp" width="55px">
                                                                <f:facet name="header">
                                                                    <h:outputText value="Desfazer"/>
                                                                </f:facet>
                                                                <center>
                                                                    <a4j:commandButton id="botaoRemoveDeslocamentoTemp" image="../images/delete.png"
                                                                                       action="#{diaCronograma.desfazHorario}"
                                                                                       reRender="tabelaDeslocTemp"
                                                                                       rendered="#{(diaCronograma.isDesfazer)}" 
                                                                                       onclick="javascript:if (!confirm('Deseja remover deslocamento temporario?')) return false;">
                                                                    </a4j:commandButton>
                                                                </center>
                                                            </rich:column>
                                                        </rich:extendedDataTable>   
                                                    </a4j:region>
                                                </h:panelGrid>
                                                <br/>
                                                <h:panelGroup>
                                                    <center>
                                                        <h:panelGrid columns="3" >
                                                            <h:panelGrid columns="1" style="text-align:center;float:center" >
                                                                <a4j:commandButton   value="Salvar" id="salvarAddTurnoTemp" image="../images/salvar_48.png"
                                                                                     action="#{cadastroCronogramaBean.salvarTurnos}" reRender="cronogramasGroupExterno">
                                                                    <rich:componentControl for="addTurnoPanelTemp" attachTo="salvarAddTurnoTemp" operation="hide" event="onclick"/>
                                                                </a4j:commandButton>
                                                                <h:outputText value="Salvar" styleClass="italico"/>
                                                            </h:panelGrid>

                                                            <rich:spacer id="espacoLimparSalvarTemp2" width="50px"/>

                                                            <h:panelGrid columns="1" style="text-align:center;float:center" >
                                                                <a4j:commandButton id="limparTodosBotaoTemp" image="../images/espanador_48.png"
                                                                                   action="#{cadastroCronogramaBean.limparIntervalo}"
                                                                                   reRender="tabelaDeslocTemp,tabelaHorariosIntervalo"
                                                                                   onclick="javascript:if (!confirm('Realmente deseja limpar?')) return false;">
                                                                </a4j:commandButton>
                                                                <h:outputText value=" Limpar Período" styleClass="italico"/>
                                                            </h:panelGrid>

                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                        </center>
                                    </h:panelGroup>
                                </center>
                            </rich:tab>
                        </rich:tabPanel>
                        <br>
                        <h:commandLink action="#{cadastroCronogramaBean.voltar}" styleClass="link">
                            <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                        </h:commandLink>
                    </h:form>
                </center>
                <jsp:include page="../www/_bot.jsp"/>
            </f:view>
        </center>
    </body>
</html>
