<%--
    Document   : relatorioMensal
    Created on : Feb 1, 2010, 9:59:08 PM
    Author     : Alexandre
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML Basic 1.0//EN" "http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd">

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


<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="/ponto/resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" language=javascript>
            function TrocaList()
            {
                var element = document.getElementById("formulario:manyBox");
                var i;
                for (i = 0; i < element.options.length ; i++)
                {
                    element.options[i].selected = true;
                }
            }
        </script>
        <title>SGN - Soluções em Gestão De Negócios</title>
    </head>
    <body onload="TrocaList()">
        <f:view >
            <jsp:include page="_top.jsp"/>
            <a4j:keepAlive beanName="teste"/>
            <center>
                <h:form id="f_messagens" prependId="false">
                    <center>
                        <h:messages infoClass="info"/>
                    </center>
                </h:form>
                <h:form id="formulario">
                    <br/>
                    <h:panelGrid id="group">
                        <h:selectManyListbox  size="10" id="manyBox" value="#{teste.values}" >
                            <f:selectItems
                                value="#{teste.list}"/>
                        </h:selectManyListbox>

                        <h:outputLabel for="address">
                            <h:outputText id="addressLabel"
                                          value="#{teste.saida}"/>
                        </h:outputLabel>


                    </h:panelGrid>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>

