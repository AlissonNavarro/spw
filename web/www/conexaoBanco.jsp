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
        <title>SGN - Soluções em Gestão De Negócios</title>
    </head>
    <body>
        <f:view>
            <jsp:include page="_top.jsp"/>
            <a4j:keepAlive beanName="conexaoBean" />
            <center>
                <h:form prependId="false">
                    <h:messages infoClass="info" />
                </h:form>
                <h:form id="formMain">
                    <br/>
                    <center>
                        <h:panelGrid columnClasses="gridContent">
                            <br><br><br>
                            <rich:panel style="text-align: center;">
                                <center>
                                    <f:facet name="header">
                                        <h:panelGroup>
                                            <h:outputText value="CONEXÃO COM O BANCO DE DADOS"/>
                                        </h:panelGroup>
                                    </f:facet>
                                    <h:panelGrid columns="3" style="text-align:center;float:center;">
                                        <h:outputText value="Servidor: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText id="servidor"  value="#{conexaoBean.conexao.server}" size="20"/>

                                        <h:outputText value="Porta: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText id="porta"  value="#{conexaoBean.conexao.port}" size="20"/>

                                        <h:outputText value="Banco de Dados: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText id="database"  value="#{conexaoBean.conexao.database}" size="20"/>


                                        <h:outputText value="Usuário: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputText id="usuario"  value="#{conexaoBean.conexao.usuario}" size="20"/>

                                        <h:outputText value="Senha: " styleClass="label" style="float:right"/>
                                        <rich:spacer width="10"/>
                                        <h:inputSecret id="senha" redisplay="true" value="#{conexaoBean.conexao.senha}" size="20"/>
                                    </h:panelGrid>
                                </center>
                                <center>
                                    <h:panelGrid columns="1">
                                        <h:commandLink type="submit" action="#{conexaoBean.salvar}">
                                            <h:graphicImage  value="../images/conectar.png" style="border:0"/>
                                        </h:commandLink>
                                        <h:outputLabel value="Conectar" styleClass="labelRight"/>
                                    </h:panelGrid>
                                </center>
                            </rich:panel>
                        </h:panelGrid>
                        <h:panelGroup>
                            <center>
                                <h:commandLink action="login">
                                    <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                </h:commandLink>
                            </center>
                        </h:panelGroup>
                    </center>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>

<SCRIPT>
    function entrar(e)
    {
        if (e.keyCode == 13)
        {
            document.getElementById("formMain:btEntrar").click();
        }
    }
</SCRIPT>


