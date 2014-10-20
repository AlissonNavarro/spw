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
            <center>
                <h:form prependId="false">
                    <h:messages infoClass="info" />
                </h:form>
                <h:form id="formMain">
                    <rich:panel style="width:250px;top:50%;left:50%;position: absolute;margin-top:-125px; margin-left:-125px;" >
                        <f:facet name="header">
                            <h:outputText value="Nova Senha"></h:outputText>
                        </f:facet>
                        <h:panelGrid columns="2" >
                            <h:outputText value="Senha: "/>
                            <h:inputSecret redisplay="false"
                                           value="#{novoUsuarioBean.novoUsuario.senha}" maxlength="11" size="11" tabindex="1">
                            </h:inputSecret>
                            <h:outputText value="Confirme a senha:"/>
                            <h:inputSecret redisplay="false" id="btSenha"
                                           value="#{novoUsuarioBean.novoUsuario.confirmacaoSenha}"
                                            maxlength="11" size="11" tabindex="2" onkeypress = "entrar(event)">
                            </h:inputSecret>
                        </h:panelGrid>
                        <br/>
                        <h:panelGrid columns="3" >
                            <h:commandButton id="btVoltar" value="Voltar"  action="login" tabindex="4"/>
                            <rich:spacer width="20"/>
                            <h:commandButton id="btEntrar" type="submit" value="Confirmar" action="#{novoUsuarioBean.entrar}" tabindex="3"/>
                        </h:panelGrid>
                    </rich:panel>
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


