<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);


    String param = request.getParameter("param");
    if (param != null) {
        if (param.equals("clear")) {
            session.invalidate();
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
        <link href="/css/cssLayout.css" rel="stylesheet" type="text/css">
        <link href="/css/default.css" rel="stylesheet" type="text/css">
        <link href="/css/cssLayout.css" rel="stylesheet" type="text/css">
        <link href="./css/default.css" rel="stylesheet" type="text/css">
        <link href="./css/cssLayout.css" rel="stylesheet" type="text/css">
        <link href="../css/default.css" rel="stylesheet" type="text/css">
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" src="../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" src="/resources/jquery.maskedinput-1.2.2.js">
        </script>
        <SCRIPT language="JavaScript" type="text/javascript">
            function putFocusCPF()
            {
                document.getElementById("form:cpf").focus();
            }


            function returnNumber(e){
                var tecla=(window.event)?event.keyCode:e.which;
                if((tecla > 47 && tecla < 58)) return true;
                else{
                    if ((tecla != 0)&&(tecla !=8)) return false;
                    else return true;
                }
            }
        </script>

        <title>SGN - Soluções em Gestão De Negócios</title>
    </head>
    <body id="bg_body" onload="putFocusCPF()" >
        <f:view>
            <center>
                <h:form id="mensagens" prependId="false">
                    <h:messages infoClass="info" />
                </h:form>
                <h:form id="form" >
                    <br><br><br><br><br><br><br><br><br>
                    <h:panelGrid columns="1">
                        <rich:panel style="text-align: center;">
                            <f:facet name="header">
                                <h:panelGroup>
                                    <h:outputText value="BEM-VINDO"/>
                                </h:panelGroup>
                            </f:facet>
                            <h:graphicImage value="/images/login.gif" rendered="#{not fileUploadBean.logoExiste}"/>
                            <a4j:mediaOutput element="img" mimeType="#{fileUploadBean.file.mime}" rendered="#{fileUploadBean.logoExiste}"
                                             createContent="#{fileUploadBean.paint}" value="1"
                                             style="width:180px; height:100px;" cacheable="false">
                                <f:param value="#{fileUploadBean.timeStamp}" name="time"/>
                            </a4j:mediaOutput>
                            <br>
                            <center>                           

                                <h:panelGrid id="login">
                                    <center>
                                        <h:panelGrid columns="2" style="text-align: center;float:center;" rendered="#{usuarioBean.existsADServer}">
                                            <h:outputText value="Logon pelo Active Directory " styleClass="label" style="float:right;"/>
                                            <h:selectBooleanCheckbox value="#{usuarioBean.useAD}">
                                                <a4j:support event="onchange" reRender="login"/>
                                            </h:selectBooleanCheckbox>
                                        </h:panelGrid>



                                        <h:panelGrid columns="3" style="text-align: center;float:center;">
                                            <h:outputText value="USUÁRIO: " styleClass="label" style="float:right;" rendered="#{usuarioBean.useAD}"/>
                                            <h:outputText value="CPF: " styleClass="label" style="float:right;" rendered="#{not usuarioBean.useAD}"/>
                                            <rich:spacer width="5"/>
                                            <h:inputText id="aduser" size="20" value="#{usuarioBean.ADlogin}" rendered="#{usuarioBean.useAD}">
                                                <a4j:support event="onblur" action="#{usuarioBean.verificarVinculosAD}"
                                                             reRender="vinculo"/>
                                            </h:inputText>

                                            <h:inputText id="cpf" size="12" value="#{usuarioBean.login}" rendered="#{not usuarioBean.useAD}">
                                                <a4j:support event="onblur" action="#{usuarioBean.verificarVinculos}"
                                                             reRender="vinculo"/>
                                                <rich:jQuery selector="#cpf" query="mask('999.999.999-99')" timing="onload"/>
                                            </h:inputText>

                                            <h:outputText value="SENHA:" styleClass="label" style="float:right;"/>
                                            <rich:spacer width="5"/>
                                            <h:inputSecret redisplay="false" rendered="#{usuarioBean.useAD}"
                                                           value="#{usuarioBean.senha}" maxlength="20" size="20">
                                            </h:inputSecret>
                                            <h:inputSecret redisplay="false" rendered="#{not usuarioBean.useAD}"
                                                           value="#{usuarioBean.senha}" maxlength="11" size="12">
                                            </h:inputSecret>
                                        </h:panelGrid>
                                        <h:panelGroup id="vinculo" style="text-align: center;float:center;">
                                            <h:panelGrid columns="3" rendered="#{not empty usuarioBean.vinculoList}"
                                                         style="text-align: center;float:center;">
                                                <h:outputText value="VÍNCULO: " styleClass="label" style="float:right;"/>
                                                <rich:spacer width="5"/>
                                                <h:selectOneMenu  value="#{usuarioBean.cod_vinculo_}">
                                                    <f:selectItems value="#{usuarioBean.vinculoList}"/>
                                                </h:selectOneMenu>
                                            </h:panelGrid>
                                        </h:panelGroup>
                                        <h:outputLink  value="#" id="link2" rendered="#{not usuarioBean.hasAdinked}">
                                            <center>
                                                Ligar usuário AD ao Ponto Web
                                            </center>
                                            <rich:componentControl for="ADpanel" attachTo="link2" operation="show" event="onclick"/>
                                        </h:outputLink>
                                    </center>                                   
                                </h:panelGrid>
                            </center>
                            <br>
                            <h:commandButton type="submit" value="Entrar" action="#{usuarioBean.entrar}">
                                <a4j:support action="onclick" reRender="adicionarADGrid"/>
                            </h:commandButton>              
                            <br>
                            <br>
                            <rich:modalPanel id="panel" width="330" height="190">
                                <f:facet name="header">
                                    <h:panelGroup>
                                        <h:outputText value="Mudança de Senha"></h:outputText>
                                    </h:panelGroup>
                                </f:facet>
                                <f:facet name="controls">
                                    <h:panelGroup>
                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                        <rich:componentControl for="panel" attachTo="hidelink" operation="hide" event="onclick"/>
                                    </h:panelGroup>
                                </f:facet>
                                <center>
                                    <h:panelGrid  columns="3" id="alterarID">
                                        <h:outputText value="CPF: " style="float:right;" styleClass="label" />
                                        <rich:spacer width="5"/>
                                        <h:inputText id="cpfM" size="11" value="#{alterarSenhaBean.alterarSenha.login}">                                                
                                            <rich:jQuery selector="#cpfM" query="mask('999.999.999-99')" timing="onload"/>
                                        </h:inputText>
                                        <h:outputText value="SENHA ANTIGA: " style="float:right;" styleClass="label"/>
                                        <rich:spacer width="5"/>
                                        <h:inputSecret redisplay="false"
                                                       value="#{alterarSenhaBean.alterarSenha.senhaAntiga}" maxlength="11" size="11">
                                        </h:inputSecret>
                                        <h:outputText value="NOVA SENHA: " style="float:right;" styleClass="label"/>
                                        <rich:spacer width="5"/>
                                        <h:inputSecret redisplay="false"
                                                       value="#{alterarSenhaBean.alterarSenha.senha}" maxlength="11" size="11">
                                        </h:inputSecret>
                                        <h:outputText value="CONFIRME NOVA SENHA: " style="float:right;" styleClass="label"/>
                                        <rich:spacer width="5"/>
                                        <h:inputSecret redisplay="false"
                                                       value="#{alterarSenhaBean.alterarSenha.confirmacaoSenha}" maxlength="11" size="11">
                                        </h:inputSecret> 
                                    </h:panelGrid>
                                    <br>
                                    <h:panelGrid columns="1" >
                                        <h:commandButton value="Confirmar" id="enviar" action="#{alterarSenhaBean.entrar}">
                                            <rich:componentControl for="panel" attachTo="enviar" operation="hide" event="onclick"/>
                                        </h:commandButton>
                                    </h:panelGrid>
                                </center>
                            </rich:modalPanel>
                            <h:outputLink  value="#" id="link" rendered="#{not usuarioBean.useAD}">
                                Alterar senha
                                <rich:componentControl for="panel" attachTo="link" operation="show" event="onclick"/>
                            </h:outputLink> 
                            <b>                               
                                <br><h:outputText value="#{usuarioBean.versao}" /> 
                            </b>
                        </rich:panel>
                    </h:panelGrid>
                </h:form>

                <rich:modalPanel id="ADpanel" width="330" height="190">
                    <f:facet name="header">
                        <h:panelGroup>
                            <h:outputText value="Active Directory"></h:outputText>
                        </h:panelGroup>
                    </f:facet>
                    <f:facet name="controls">
                        <h:panelGroup>
                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkADPanel"/>
                            <rich:componentControl for="panel" attachTo="hidelinkADPanel" operation="hide" event="onclick"/>
                        </h:panelGroup>
                    </f:facet>
                    <center>
                        <h:form id="form1">
                            <h:panelGrid  columns="3" id="adicionarADGrid">
                                <h:outputText value="CPF: " style="float:right;" styleClass="label" />
                                <rich:spacer width="5"/>
                                <h:inputText id="cpfMAD" size="11" value="#{usuarioBean.cpfAD}">                                                
                                    <rich:jQuery selector="#cpfMAD" query="mask('999.999.999-99')" timing="onload"/>
                                </h:inputText>

                                <h:outputText value="User AD: " style="float:right;" styleClass="label" />
                                <rich:spacer width="5"/>
                                <h:inputText id="aduserAD" size="20" value="#{usuarioBean.ADlogin}"/>
                            </h:panelGrid>
                            <br>
                            <h:panelGrid columns="1" >
                                <h:commandButton value="Confirmar" id="enviarLinkAD" action="#{usuarioBean.linkToAD}">
                                    <rich:componentControl for="ADpanel" attachTo="enviarLinkAD" operation="hide" event="onclick"/>
                                </h:commandButton>
                            </h:panelGrid>
                        </h:form>
                    </center>
                </rich:modalPanel>
            </center>
        </f:view>
    </body>
</html>
