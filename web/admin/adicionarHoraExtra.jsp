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

<script>
    function limitText(limitField, limitNum) {
        if (limitField.value.length > limitNum) {
            limitField.value = limitField.value.substring(0, limitNum);
        }
    }
</script>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script>
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
        </script>
        <title>Hora Extra</title>
    </head>
    <body>
        <f:view>
            <jsp:include page="../www/_top.jsp"/>
            <center>
                <h:form prependId="false">
                    <a4j:keepAlive beanName="diaHoraExtraBean"/>
                    <a4j:outputPanel id="info" ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:form>                  
                    <rich:tabPanel switchType="client"  width="965" >
                        <rich:tab id="tab1" name="tab1" label="Hora Extra" ignoreDupResponses="true">
                            <center>
                                <br/>  
                                <h:outputText value="Data: " styleClass="labelMaior"/>
                                <h:outputText value="#{diaHoraExtraBean.dataSrt}" styleClass="labelMaior"/>
                                <br/>                                
                                <h:panelGrid columns="1">
                                    <h:panelGroup>
                                        <center>
                                            <br/>
                                            <h:panelGrid columns="6" styleClass="resumoDiaHoraExtra" >
                                                <h:outputText value="Funcionário: " styleClass="labelDireita"/>
                                                <h:outputText value="#{diaHoraExtraBean.nome}"/>
                                                <rich:spacer width="15"/>
                                                <h:outputText value="Departamento: " styleClass="labelDireita"/>
                                                <h:outputText value="#{diaHoraExtraBean.departamentoStr}"/>
                                                <rich:spacer width="15"/>
                                                <h:outputText value="Horas Contratadas: " styleClass="labelDireita"/>
                                                <h:outputText value="#{diaHoraExtraBean.horasASeremTrabalhadasTotal}"/>
                                                <rich:spacer width="15"/>
                                                <h:outputText value="Horas Trabalhadas: " styleClass="labelDireita"/>
                                                <h:outputText value="#{diaHoraExtraBean.horasTotal}"/>
                                                <rich:spacer width="15"/>
                                                <h:outputText value="Saldo: " styleClass="labelDireita"/>
                                                <h:outputText value=" #{diaHoraExtraBean.saldoTotal}"/>
                                                <rich:spacer width="15"/>
                                            </h:panelGrid>
                                            <br/>
                                        </center>
                                    </h:panelGroup>
                                    <rich:spacer width="37"/>                                   
                                    <h:panelGroup>
                                        <center>
                                            <h:outputLabel value="Categoria:" styleClass="label"/>
                                            <br/>
                                            <h:selectOneMenu value="#{diaHoraExtraBean.diaHoraExtra.cod_categoria}">
                                                <f:selectItems value="#{diaHoraExtraBean.categoriaJustificativaList}"/>
                                                <a4j:support event="onclick" reRender="info"/>
                                            </h:selectOneMenu>
                                            <br/>
                                            <br/>
                                            <h:panelGrid columns="1" style="text-align:center;">
                                                <h:outputLabel value="Justificativa:" styleClass="label"/>
                                                <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                  cols="40" rows="3" value="#{diaHoraExtraBean.diaHoraExtra.justificativa}" >
                                                </h:inputTextarea>
                                            </h:panelGrid>
                                            <br/>
                                            <h:panelGroup rendered="#{diaHoraExtraBean.isEditar}">
                                                <center>
                                                    <h:commandLink action="#{diaHoraExtraBean.excluirHoraExtra}"
                                                                   onclick="javascript:if (!confirm('Tem certeza que deseja excluir a hora extra?')) return false;">
                                                        <h:graphicImage  value="../images/fechar.gif" style="border:0"/>
                                                    </h:commandLink>
                                                    <br/>
                                                    <h:outputLabel value="Cancelar Hora Extra" styleClass="label"/>
                                                </center>
                                            </h:panelGroup>
                                        </center>
                                    </h:panelGroup>
                                </h:panelGrid>
                                             <br><br>
                                <h:panelGroup>                                
                                    <h:panelGroup>
                                        <center>
                                            <h:commandButton action="#{diaHoraExtraBean.enviar}" value="Confirmar"/>
                                        </center>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </center>
                        </rich:tab>
                    </rich:tabPanel>
                    <br>
                    <h:panelGroup>
                        <center>
                            <h:commandLink action="navegarInicio" >
                                <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                            </h:commandLink>
                        </center>
                    </h:panelGroup>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>
