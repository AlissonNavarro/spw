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
        <title>Jornada</title>
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
                    <h:form id="form">
                        <rich:tabPanel switchType="client" width="965">
                            <rich:tab label="Turnos da Jornadas" style="text-align:center;float:center">
                                <center>
                                    <br>
                                    <h:outputText value="#{jornadaCadastroBean.editJornadaCadastroNome}" styleClass="label"/>
                                    <br>
                                    <h:panelGroup>
                                        <center>
                                            <h:panelGrid columns="1" width="80%">
                                                <br>
                                                <h:panelGrid columns="3" width="100%" columnClasses="colum30_,colum05_,colum70_">
                                                    <rich:extendedDataTable
                                                        selectionMode= "none"
                                                        enableContextMenu="false"
                                                        id="horariosExtendList"
                                                        value="#{jornadaCadastroBean.horariosList}" var="turno"
                                                        height="400px" >
                                                        <rich:column sortable="false" id="marcado0" width="30px" >
                                                            <f:facet name="header">
                                                            </f:facet>

                                                            <h:selectBooleanCheckbox
                                                                value="#{turno.marked}" >

                                                            </h:selectBooleanCheckbox>
                                                            <%--<a4j:support action="#{jornadaCadastroBean.changeVFturno}"  event="onclick">
                                                                    <f:param name="turno_id" value="#{turno.id}"/>
                                                                </a4j:support>--%>
                                                        </rich:column>
                                                        <rich:column sortable="false" id="nomeHorario" >
                                                            <f:facet name="header">
                                                                <h:outputText value="Nome do Horário" />
                                                            </f:facet>
                                                            <h:outputText value="#{turno.nome}" />
                                                        </rich:column>
                                                        <rich:column sortable="false" id="entradaSaidaColuna"  >
                                                            <f:facet name="header">
                                                                <h:outputText value="Entrada - Saída" />
                                                            </f:facet>
                                                            <h:outputText value="#{turno.stringEntradaSaida}" />
                                                        </rich:column>
                                                    </rich:extendedDataTable>

                                                    <a4j:commandButton image="../images/forward_32.png"
                                                                       action="#{jornadaCadastroBean.adicionarTurnos}"
                                                                       reRender="horariosExtendList,diasExtendList"/>

                                                    <rich:extendedDataTable
                                                        selectionMode= "none"
                                                        enableContextMenu="false"
                                                        id="diasExtendList"
                                                        value="#{jornadaCadastroBean.diasJornadaList}" var="diasJornada"
                                                        height="400px">
                                                        <rich:column sortable="false" id="marcado1" width="30px">
                                                            <f:facet name="header">
                                                            </f:facet>
                                                            <h:selectBooleanCheckbox
                                                                title="Marked"
                                                                value="#{diasJornada.marked}" >
                                                                <%--<a4j:support action="#{jornadaCadastroBean.changeVFdia}"  event="onclick">
                                                                    <f:param name="diaJornada_id" value="#{diasJornada.diaInt}"/>
                                                                </a4j:support>--%>
                                                            </h:selectBooleanCheckbox>
                                                        </rich:column>
                                                        <rich:column sortable="false" id="diaDaJornada" style="text-align:center" >
                                                            <f:facet name="header">
                                                                <h:outputText value="Dia" />
                                                            </f:facet>
                                                            <h:outputText value="#{diasJornada.diaString}" />
                                                        </rich:column>
                                                        <rich:column sortable="false" id="turnosAssociados" width="50%" >
                                                            <f:facet name="header">
                                                                <h:outputText value="Horários" />
                                                            </f:facet>
                                                            <h:outputText value="#{diasJornada.horarios}" />
                                                        </rich:column>
                                                        <rich:column sortable="false" id="limpar" width="30px" >
                                                            <a4j:commandButton id="limparBotao" image="../images/espanador_22.png"
                                                                               action="#{diasJornada.limparTurno}"
                                                                               reRender="diasExtendList"
                                                                               onclick="javascript:if (!confirm('Realmente deseja limpar?')) return false;">
                                                            </a4j:commandButton>
                                                        </rich:column>
                                                    </rich:extendedDataTable>
                                                </h:panelGrid>
                                                <br/>
                                                <h:panelGroup>
                                                    <center>
                                                        <h:panelGrid columns="3" >
                                                            <h:commandButton  value="Salvar" id="salvarAddTurno" image="../images/salvar_48.png"
                                                                              onclick="javascript:if (!confirm('Realmente deseja salvar?')) return false;"
                                                                              action="#{jornadaCadastroBean.salvarTurnos}">
                                                                <rich:componentControl for="addTurnoPanel" attachTo="salvarAddTurno" operation="hide" event="onclick" />
                                                            </h:commandButton>
                                                            <rich:spacer id="espacoLimparSalvar" width="50px"/>
                                                            <a4j:commandButton id="limparTodosBotao" image="../images/espanador_48.png"
                                                                               action="#{jornadaCadastroBean.limparJornada}"
                                                                               reRender="diasExtendList"
                                                                               onclick="javascript:if (!confirm('Realmente deseja limpar?')) return false;">
                                                            </a4j:commandButton>

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
                        <h:commandLink action="#{jornadaCadastroBean.voltar}" styleClass="link">
                            <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                        </h:commandLink>
                    </h:form>
                </center>
                <jsp:include page="../www/_bot.jsp"/>
            </f:view>
        </center>
    </body>
</html>
