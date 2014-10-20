<%-- 
    Document   : erro
    Created on : Jan 4, 2010, 10:36:58 PM
    Author     : Alexandre
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<SCRIPT language="JavaScript" type="text/javascript">
    function limitText(limitField, limitNum) {
        if (limitField.value.length > limitNum) {
            limitField.value = limitField.value.substring(0, limitNum);
        }
    }
</SCRIPT>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <title>Abonar</title>
    </head>
    <body>
        <f:view>
            <jsp:include page="../www/_top.jsp"/>
            <center>
                <h:form prependId="false">
                    <a4j:outputPanel id="info" ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:form>
                    <a4j:keepAlive beanName="abonarBean" />
                    <rich:tabPanel switchType="client"  width="965" >
                        <rich:tab id="tab1" label="Abonar" ignoreDupResponses="true">
                            <center>
                                <h:panelGrid columns="1">
                                    <h:panelGroup>
                                        <center>
                                            <rich:panel>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="#{abonarBean.data}"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <h:panelGrid columns="1"  style="text-align:center;">
                                                    <h:panelGroup style="text-align:center;">
                                                        <center>
                                                            <h:panelGrid columns="6" style="text-align:center;">
                                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                                <h:outputText value="#{abonarBean.nome}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                                <h:outputText value="#{abonarBean.departamento}"/>
                                                                <rich:spacer width="15"/>
                                                            </h:panelGrid>
                                                            <br/>
                                                            <h:panelGrid columns="1" style="text-align:center;">
                                                                <h:outputText value="Solicitação " styleClass="label"/>
                                                                <h:outputText value="#{abonarBean.descricao}"/>
                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGroup>
                                                </h:panelGrid>

                                                <br/>
                                                <h:outputLabel value="Categorize o abono" styleClass="label"/>
                                                <br/>
                                                <h:selectOneMenu value="#{abonarBean.justificativa}">
                                                    <f:selectItems value="#{abonarBean.justificativaList}"/>
                                                    <a4j:support event="onclick" reRender="info"/>
                                                </h:selectOneMenu>
                                                <br/>
                                                <br/>
                                                <h:outputLabel value="Selecione os pontos que deseja abonar" styleClass="label"/>
                                                <h:panelGrid columns="3">
                                                    <h:selectManyCheckbox id ="manyMenuCheckBox" value="#{abonarBean.horaAbonoList}" >
                                                        <f:selectItems value="#{abonarBean.horaSelectAbonoList}"/>
                                                        <a4j:support event="onclick" reRender="info"/>
                                                    </h:selectManyCheckbox >
                                                    <%--
                                                    <h:outputLink  value="#" id="link">
                                                        <center>
                                                            <h:graphicImage  value="../images/adicionar.png" style="border:0"/>
                                                        </center>
                                                        <rich:componentControl for="panel" attachTo="link" operation="show" event="onclick"/>
                                                    </h:outputLink>--%>



                                                    <%--<rich:modalPanel id="panel" width="330" height="100" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:panelGroup>
                                                                <h:outputText value="Adicionar Registro"></h:outputText>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="controls">
                                                            <h:panelGroup>
                                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                                <rich:componentControl for="panel" attachTo="hidelink" operation="hide" event="onclick"/>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <center>
                                                            <h:outputText value="Adicione um registro: " styleClass="label"/>
                                                            <rich:spacer width="8"/>
                                                            <h:inputText id="hora" size="1" value="#{abonarBean.hora}"
                                                                         onkeyup="mascara_hora(this.id)">
                                                                <rich:jQuery selector="#hora" query="mask('99:99')" timing="onload"/>
                                                            </h:inputText>
                                                        </center>
                                                        <br/>
                                                        <h:commandButton value="Adicionar" id="adicionar" action="#{abonarBean.addRegistro}">
                                                            <rich:componentControl for="panel" attachTo="adicionar" operation="hide" event="onclick"/>
                                                            <a4j:support event="onclick" reRender="info"/>
                                                        </h:commandButton>
                                                    </rich:modalPanel> --%>


                                                </h:panelGrid>
                                                <h:panelGrid columns="1" style="text-align:center;">
                                                    <h:outputLabel value="Digite uma justificativa" styleClass="label"/>
                                                    <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                      cols="40" rows="3" value="#{abonarBean.detalheJustificativa}" >
                                                    </h:inputTextarea>
                                                </h:panelGrid>

                                                <br/>

                                                <h:panelGroup>
                                                    <center>
                                                        <h:commandButton action="#{abonarBean.enviar}" value="Confirmar">
                                                            <f:param  name="cod_solicitacao" value="#{abonarBean.codigo}"/>
                                                        </h:commandButton>
                                                    </center>
                                                </h:panelGroup>

                                            </rich:panel>
                                        </center>
                                    </h:panelGroup>
                                </h:panelGrid>
                            </center>
                        </rich:tab>
                    </rich:tabPanel>
                    <br>
                    <h:panelGroup>
                        <center>
                            <h:commandLink action="voltarAbono">
                                <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                <f:param  name="cod_solicitacao" value="#{abonarBean.codigo}"/>
                            </h:commandLink>
                        </center>
                    </h:panelGroup>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>

<SCRIPT language="JavaScript" type="text/javascript">
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
        }
    }
</SCRIPT>
