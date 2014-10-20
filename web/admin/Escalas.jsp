<%-- 
    Document   : Escalas
    Created on : Feb 1, 2010, 9:24:59 PM
    Author     : Alexandre
--%>

<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <title>SGN - Soluções em Gestão De Negócios</title>
    </head>
    <body>
        <f:view>
            <jsp:include page="../www/_top.jsp"/>
            <center>
                <h:form id="f_messagens" prependId="false">
                    <center>
                        <h:messages infoClass="info" />
                    </center>
                </h:form>
                <h:form>
                    <h:panelGrid columns="1" >
                        <rich:tabPanel switchType="client" width="965">
                            <rich:tab id="tab1" label="Escalas">
                                <br/>
                                <center>
                                    <h:panelGrid columns="7" >
                                        <h:outputText value="Departamento: " styleClass="label"/>
                                        <rich:spacer width="20"/>
                                        <h:outputText value="Mês: " styleClass="label"/>
                                        <rich:spacer width="20"/>
                                        <h:outputText value="Ano:" styleClass="label"/>
                                        <rich:spacer width="20"/>
                                        <rich:spacer width="20"/>

                                        <h:selectOneMenu id="deparId" value="#{jornadaExibicaoBean.departamentoSelecionado}"  >
                                            <f:selectItems value="#{jornadaExibicaoBean.departamentosSelecItem}"/>
                                        </h:selectOneMenu>
                                        <rich:spacer width="20"/>

                                        <h:selectOneMenu id="mes" value="#{jornadaExibicaoBean.mesSelecionado}"  >
                                            <f:selectItems value="#{jornadaExibicaoBean.mesSelecItem}"/>
                                        </h:selectOneMenu>
                                        <rich:spacer width="20"/>

                                        <h:inputText id="hora" size="5" value="#{jornadaExibicaoBean.ano}">
                                            <rich:jQuery selector="#hora" query="mask('9999')" timing="onload"/>
                                        </h:inputText>
                                        <rich:spacer width="20"/>

                                        <h:commandButton id="buttonId" value="Consultar" action="#{jornadaExibicaoBean.getDiaConsulta}"/>
                                    </h:panelGrid>
                                </center>
                            </rich:tab>
                        </rich:tabPanel>
                    </h:panelGrid>   
                </h:form>
                <jsp:include page="../www/_bot.jsp"/>
            </center>
        </f:view>
    </body>
</html>
