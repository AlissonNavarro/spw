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
        <title>Gerenciador de Relógios</title>
        <script type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
        </script>
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" src="../resources/jquery.maskedinput-1.2.2.js">
        </script>
    </head>
    <body>
        <center>
            <f:view >
                <center> 
                    <jsp:include page="../www/_top.jsp"/>

                    <h:form id="f_messagens" prependId="false">
                        <a4j:outputPanel ajaxRendered="true">
                            <rich:messages infoClass="info"/>
                        </a4j:outputPanel>
                    </h:form>

                    <style>
                        .pbody{
                            width:200px;
                        }
                    </style>
                    <rich:tabPanel switchType="client" width="965" selectedTab="#{clockManagerBean.abaCorrente}">
                        <rich:tab id="tab1" label="Lista de Relógios" rendered="#{usuarioBean.perfil.listaRelogios == true}">
                            <a4j:support event="ontabenter" action="#{clockManagerBean.setAba()}">
                                <a4j:actionparam name="tab" value="tab1"/>
                            </a4j:support>
                            <center>
                                <a4j:outputPanel id="repPanel">
                                    <f:facet name="header">
                                        <center>
                                            <h:outputText value="Relógios"></h:outputText>
                                            </center>
                                    </f:facet>
                                    <h:form>
                                        <h:panelGrid columns="2" columnClasses="gridContent">
                                            <a4j:commandButton id="updateClocksAjax" value="Verificar!" reRender="repPanel" action="#{clockManagerBean.verifyClocks}"/>
                                        </h:panelGrid>
                                        <rich:dataGrid id="repGrid" value="#{clockManagerBean.allClocks}" var="clock" columns="4" 
                                                       elements="16" width="600px" >
                                            <rich:panel bodyClass="pbody">
                                                <f:facet name="header">
                                                    <h:outputText value="#{clock.type.repMarca}"></h:outputText>
                                                </f:facet>
                                                <h:panelGrid columns="2">
                                                    <h:outputText value="Alias:" styleClass="label"></h:outputText>
                                                    <a4j:commandLink value="#{clock.repAlias}" id="aliasClock" reRender="clockDetails"
                                                                     oncomplete="Richfaces.showModalPanel('clockDetails')" action="#{clockManagerBean.findClock}">
                                                        <a4j:actionparam name="clock" value="#{clock.repId}" assignTo="#{clockManagerBean.selectedClock}"/>
                                                    </a4j:commandLink>
                                                    <h:outputText value="Ip:" styleClass="label"></h:outputText>
                                                    <h:outputText value="#{clock.repIp}" />
                                                    <h:outputText value="Porta:" styleClass="label"></h:outputText>
                                                    <h:outputText value="#{clock.repPort}" />
                                                    <h:outputText value="Status:" styleClass="label"></h:outputText>
                                                    <h:outputText value="?" rendered="#{clock.online == null}" />
                                                    <h:outputText value="Online" rendered="#{clock.online == true}" />
                                                    <h:outputText value="Offline" rendered="#{clock.online == false}" />
                                                </h:panelGrid>
                                            </rich:panel>
                                            <f:facet name="footer">
                                                <rich:datascroller></rich:datascroller>
                                            </f:facet>
                                        </rich:dataGrid>
                                    </h:form>

                                    <%--<rich:spacer width="10" rendered="#{not empty empresaBean.ipDigitSender}"/>
                                    <h:panelGrid columns="1" style="text-align:center;float:center" rendered="#{not empty empresaBean.ipDigitSender}">    
                                        <center>
                                            <h:outputLink id="sender" value="#{empresaBean.fullNameIpSender}" 
                                                          target="_blank" style="float:center" >
                                                <h:graphicImage value="../images/pontoClock.png" style="border:0"/>
                                            </h:outputLink>
                                            <h:outputText value="Enviar Usuários aos Relógios" styleClass="label"/>
                                        </center>
                                    </h:panelGrid>--%>
                                </a4j:outputPanel>
                            </center>
                        </rich:tab>
                        <rich:tab id="tab2" label="Coleta pelo AFD" rendered="#{usuarioBean.perfil.downloadAfd == true}">
                            <a4j:support event="ontabenter" action="#{clockManagerBean.setAba()}" >
                                <a4j:actionparam name="tab" value="tab2"/>
                            </a4j:support>
                            <center>
                                <h:panelGrid columns="1" >
                                    <rich:panel style="text-align: center;">
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Anexar o arquivo AFD"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <h:form id="formAFD">
                                            <center>
                                            <%--
                                            <h:outputLabel value="Data mínima das marcações"/>
                                            <rich:calendar inputSize="8" locale="pt/BR" value="#{clockManagerBean.dataLimite}" />
                                            --%>
                                            <rich:fileUpload fileUploadListener="#{clockManagerBean.abreAFD}" 
                                                             id="upload" acceptedTypes="txt" maxFilesQuantity="1"
                                                             listHeight="60" listWidth="250" 
                                                             cancelEntryControlLabel="Cancelar"
                                                             clearAllControlLabel="Limpar"
                                                             addControlLabel="Adicionar">
                                                <a4j:support event="onuploadcomplete" reRender="formAFD"/>
                                            </rich:fileUpload>
                                            <h:outputText style="font-color:red; font-size:9px" value="OBS: Anexar o arquivo .txt gerado pelo relógio na porta USB fiscal"/>
                                            <br>
                                            <h:outputText style="font-color:red; font-size:9px" value="Exemplo: AFD00022000760001483.txt"/>
                                            </center>
                                            <%--
                                            <h:panelGrid columns="5">
                                                <h:panelGrid columns="1">
                                                    <h:outputLink value="#" id="pathFinder">
                                                        <h:graphicImage value="/images/iddata.png"/>
                                                        <rich:componentControl for="browserFile" attachTo="pathFinder" operation="show" event="onclick"/>
                                                        <a4j:support action="#{clockManagerBean.callSoft}"
                                                                     event="onclick">
                                                        </a4j:support>
                                                    </h:outputLink>
                                                    <h:outputText value="IDData" styleClass="label"/>
                                                </h:panelGrid>
                                                <rich:spacer width="10"/>
                                                <h:panelGrid columns="1">
                                                    <h:outputLink id="senderlink" value="#{empresaBean.fullNameIpSender}" 
                                                                  target="_blank" style="float:center" >
                                                        <h:graphicImage value="/images/trilobit.png"/>
                                                    </h:outputLink>
                                                    <h:outputText value="Trilobit" styleClass="label"/>

                                                </h:panelGrid>
                                                <rich:spacer width="10"/>
                                                <h:panelGrid columns="1">
                                                    <h:graphicImage value="/images/zk.png"/>
                                                    <h:outputText value="ZK" styleClass="label"/>
                                                </h:panelGrid>
                                            </h:panelGrid>
                                            --%>
                                        </h:form>
                                    </rich:panel>
                                </h:panelGrid>
                            </center>
                        </rich:tab>
                        <rich:tab id="tab3" label="Scan de Ip's" rendered="#{usuarioBean.perfil.scanIP == true}">
                            <a4j:support event="ontabenter" action="#{clockManagerBean.setAba()}">
                                <a4j:actionparam name="tab" value="tab3"/>
                            </a4j:support>
                            <center>
                                <h:form id="fScan">
                                    <h:panelGrid columns="5" id="pgScan">
                                        <h:outputLabel value="IP Inicio:" for="iIpInicio" styleClass="label"/>
                                        <rich:spacer width="12"/>
                                        <h:outputLabel value="IP Fim:" for="iIpFim" styleClass="label"/>
                                        <rich:spacer width="12"/>
                                        <rich:spacer width="12"/>
                                        <h:inputText id="iIpInicio" value="#{clockManagerBean.ipInicio}" maxlength="15"/>
                                        <rich:spacer width="12"/>
                                        <h:inputText id="iIpFim" value="#{clockManagerBean.ipFim}" maxlength="15"/>
                                        <rich:spacer width="12"/>
                                        <h:commandButton value="Scan" action="#{clockManagerBean.verifyIps}" />
                                    </h:panelGrid>
                                    <h:panelGrid columns="1" id="pgScanResult">
                                        <rich:dataTable id="ipsList" value="#{clockManagerBean.ips}" var="linha" rows="20">
                                            <rich:column style="text-align:center">
                                                <f:facet name="header">
                                                    <h:outputText value="IP"/>
                                                </f:facet>
                                                <h:outputText value="#{linha.ip}"/>
                                            </rich:column>
                                            <rich:column style="text-align:center">
                                                <f:facet name="header">
                                                    <h:outputText value="ATIVO"/>
                                                </f:facet>
                                                <h:outputText value="#{linha.resultado}"/>
                                            </rich:column>
                                            <rich:column style="text-align:center">
                                                <f:facet name="header">
                                                    <h:outputText value="RESULTADO"/>
                                                </f:facet>
                                                <h:outputText value="#{linha.comentario}"/>
                                            </rich:column>                        
                                        </rich:dataTable>
                                        <rich:datascroller  id="ipsListScroller"
                                                            for="ipsList"
                                                            page="#{clockManagerBean.page}"
                                                            ajaxSingle="true"
                                                            renderIfSinglePage="false">
                                        </rich:datascroller>
                                    </h:panelGrid>
                                </h:form>
                            </center>
                        </rich:tab>
                        <rich:tab id="tab4" label="Relógios de Ponto" rendered="#{usuarioBean.perfil.relogioPonto == true}">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tab4"/>
                            </a4j:support>                            
                            <h:form id="formRelogios">
                                <center>

                                    <rich:panel id="panelRelogios" style="text-align: center;">
                                        <center>
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Gerenciar Relógios de Ponto"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid id="macGrid" columns="3" style="text-align:center;float:center;">
                                                    <h:outputText value="Alias: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="machineAlias"  value="#{machineBean.machine.repAlias}" size="20"/>

                                                    <h:outputText value="Ip: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="machineIp"  value="#{machineBean.machine.repIp}" size="20"/>

                                                    <h:outputText value="Porta: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="machinepPort"  value="#{machineBean.machine.repPort}" size="20"/>

                                                    <h:outputText value="Marca: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:selectOneMenu id="machineMarkBox" value="#{machineBean.machine.repType}">
                                                        <f:selectItems value="#{machineBean.machineTypeList}"/>
                                                    </h:selectOneMenu>

                                                    <h:outputText value="NFR: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.nfrOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.nfrOn}"/>
                                                    <h:inputText id="machinenfr"  value="#{machineBean.machine.nfr}" size="20" rendered="#{machineBean.machine.type.nfrOn}"/>

                                                    <h:outputText value="Modelo: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.modelOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.modelOn}"/>
                                                    <h:inputText id="machinemodel"  value="#{machineBean.machine.model}" size="20" rendered="#{machineBean.machine.type.modelOn}"/>

                                                    <h:outputText value="Local: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.localOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.localOn}"/>
                                                    <h:inputText id="machinelocal"  value="#{machineBean.machine.local}" size="20" rendered="#{machineBean.machine.type.localOn}"/>

                                                    <h:outputText value="Compania: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.idCompanyOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.idCompanyOn}"/>
                                                    <h:inputText id="machinecompany"  value="#{machineBean.machine.idCompany}" size="20" rendered="#{machineBean.machine.type.idCompanyOn}"/>

                                                    <h:outputText value="Tipo de Conexão: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.commTypeOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.commTypeOn}"/>
                                                    <h:inputText id="machinecommtype"  value="#{machineBean.machine.commType}" size="20" rendered="#{machineBean.machine.type.commTypeOn}"/>

                                                    <h:outputText value="Módulo Biométrico: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.biometricModuleOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.biometricModuleOn}"/>
                                                    <h:inputText id="machinebiomodule"  value="#{machineBean.machine.biometricModule}" size="20" rendered="#{machineBean.machine.type.biometricModuleOn}"/>

                                                    <h:outputText value="Hora da última busca: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.dateLastNSROn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.dateLastNSROn}"/>
                                                    <h:inputText id="machinedatelastnsr"  value="#{machineBean.machine.dateLastNSR}" size="20" rendered="#{machineBean.machine.type.dateLastNSROn}"/>

                                                    <h:outputText value="Último dado: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.lastNSROn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.lastNSROn}"/>
                                                    <h:inputText id="machinelastnsr"  value="#{machineBean.machine.lastNSR}" size="20" rendered="#{machineBean.machine.type.lastNSROn}"/>

                                                    <h:outputText value="Caminho do arquivo de coleta: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.pathCollectFileOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.pathCollectFileOn}"/>
                                                    <h:inputText id="machinepathfile"  value="#{machineBean.machine.pathCollectFile}" size="20" rendered="#{machineBean.machine.type.pathCollectFileOn}"/>

                                                    <h:outputText value="NSR: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.nsrOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.nsrOn}"/>
                                                    <h:inputText id="machinensr"  value="#{machineBean.machine.nsr}" size="20" rendered="#{machineBean.machine.type.nsrOn}"/>

                                                    <h:outputText value="Ip do Roteador: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.ipRoaterOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.ipRoaterOn}"/>
                                                    <h:inputText id="machineiproater"  value="#{machineBean.machine.ipRoater}" size="20" rendered="#{machineBean.machine.type.ipRoaterOn}"/>

                                                    <h:outputText value="Mascara: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.maskOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.maskOn}"/>
                                                    <h:inputText id="machinemask"  value="#{machineBean.machine.mask}" size="20" rendered="#{machineBean.machine.type.maskOn}"/>

                                                    <h:outputText value="Senha: " styleClass="label" style="float:right" rendered="#{machineBean.machine.type.senhaOn}"/>
                                                    <rich:spacer width="10" rendered="#{machineBean.machine.type.senhaOn}"/>
                                                    <h:inputText id="machinesenha"  value="#{machineBean.machine.senha}" size="20" rendered="#{machineBean.machine.type.senhaOn}"/>

                                                    <h:outputText value="Ativo: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:selectBooleanCheckbox id="machineEnable" value="#{machineBean.machine.repAtivo}"/>
                                                </h:panelGrid>
                                            </center>

                                            <center>
                                                <h:panelGrid columns="5">

                                                    <h:panelGrid columns="1">
                                                        <center>
                                                            <h:commandLink type="submit" action="#{machineBean.adicionar}">
                                                                <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                <a4j:support event="onClick" reRender="panelRelogios"/>
                                                            </h:commandLink>
                                                            <h:outputLabel value="Novo" styleClass="labelRight"/>
                                                        </center>
                                                    </h:panelGrid>

                                                    <rich:spacer width="10"/>
                                                    <h:panelGrid columns="1">
                                                        <center>
                                                            <h:commandLink type="submit" action="#{machineBean.editar}">
                                                                <h:graphicImage  value="../images/edit.gif" style="border:0"/>
                                                                <a4j:support event="onClick" reRender="panelRelogios"/>
                                                            </h:commandLink>
                                                            <h:outputLabel value="Editar" styleClass="labelRight"/>
                                                        </center>
                                                    </h:panelGrid>

                                                    <rich:spacer width="10"/>
                                                    <h:panelGrid columns="1">
                                                        <center>
                                                            <h:commandLink type="submit" action="#{machineBean.excluir}">
                                                                <h:graphicImage  value="../images/delete_24.png" style="border:0"/>
                                                                <a4j:support event="onClick" reRender="panelRelogios"/>
                                                            </h:commandLink>
                                                            <h:outputLabel value="Excluir" styleClass="labelRight"/>
                                                        </center>
                                                    </h:panelGrid>

                                                </h:panelGrid>
                                            </center>

                                            <rich:extendedDataTable id="relogioList"
                                                                    selection="#{machineBean.selectMac}" enableContextMenu="false"
                                                                    value="#{machineBean.machineList}" var="machineRow"
                                                                    rowClasses="zebra1,zebra2"
                                                                    rows="19" width="54%">
                                                <f:facet name="header">
                                                    <h:outputText value="Lista de Relógios"/>
                                                </f:facet>
                                                <a4j:support event="onRowClick" action="#{machineBean.showEditarRelogio}"
                                                             reRender="panelRelogios">
                                                    <f:param name="idMac" value="#{machineRow.repId}"/>
                                                </a4j:support>

                                                <rich:column sortBy="#{machineRow.repAlias}" filterBy="#{machineRow.repAlias}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="Nome"/>
                                                    </f:facet>
                                                    <h:outputText value="#{machineRow.repAlias}" />
                                                </rich:column>

                                                <rich:column sortBy="#{machineRow.repIp}" filterBy="#{machineRow.repIp}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="IP"/>
                                                    </f:facet>
                                                    <h:outputText value="#{machineRow.repIp}" />
                                                </rich:column>

                                                <rich:column sortBy="#{machineRow.repPort}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="Porta Serial"/>
                                                    </f:facet>
                                                    <h:outputText value="#{machineRow.repPort}" />
                                                </rich:column>

                                                <rich:column sortBy="#{machineRow.type.repMarca}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="Marca"/>
                                                    </f:facet>
                                                    <h:outputText value="#{machineRow.type.repMarca}" />
                                                </rich:column>

                                                <rich:column sortBy="#{machineRow.repAtivo}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="ativo"/>
                                                    </f:facet>
                                                    <h:outputText value="Ativo" rendered="#{machineRow.repAtivo}" />
                                                    <h:outputText value="Inativo" rendered="#{not machineRow.repAtivo}" />
                                                </rich:column>

                                            </rich:extendedDataTable>
                                            <rich:datascroller  id="datascrollers1"
                                                                for="relogioList"
                                                                page="#{machineBean.page}"
                                                                ajaxSingle="true"
                                                                renderIfSinglePage="false">
                                            </rich:datascroller>
                                        </center>
                                    </rich:panel>
                                </center>
                            </h:form>
                        </rich:tab>                    
                    </rich:tabPanel>

                    <rich:modalPanel id="clockDetails" width="550" autosized="true" styleClass="center" >
                        <f:facet name="header">
                            <h:panelGroup>
                                <h:outputText value="Detalhes do Relógio #{clockManagerBean.clock.repAlias}"></h:outputText>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="controls">
                            <h:panelGroup>
                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hideDetails"/>
                                <rich:componentControl for="clockDetails" attachTo="hideDetails" operation="hide" event="onclick"/>
                            </h:panelGroup>
                        </f:facet>
                        <center>
                            <h:form id="details_messagens" prependId="false">
                                <a4j:outputPanel ajaxRendered="true">
                                    <rich:messages infoClass="info"/>
                                </a4j:outputPanel>
                            </h:form>
                            <rich:panel bodyClass="pbody">
                                <h:form>
                                    <h:panelGrid id="clockDetailsGrid" columns="1" style="text-align:center">
                                        <h:panelGrid columns="2">
                                            <h:panelGrid columns="2">
                                                <h:outputText value="Alias:" styleClass="label"></h:outputText>
                                                <h:inputText value="#{clockManagerBean.clock.repAlias}"/>
                                                <h:outputText value="Ip:" styleClass="label"></h:outputText>
                                                <h:inputText value="#{clockManagerBean.clock.repIp}"/>
                                                <h:outputText value="Marca:" styleClass="label"></h:outputText>
                                                <h:outputText value="#{clockManagerBean.clock.type.repMarca}"/>
                                                <h:outputText value="Status:" styleClass="label"></h:outputText>
                                                <a4j:outputPanel id="onlinePanel">
                                                    <h:outputText value="?" rendered="#{clock.online == null}" />
                                                    <h:outputText value="Online" rendered="#{clock.online == true}" />
                                                    <h:outputText value="Offline" rendered="#{clock.online == false}" />
                                                </a4j:outputPanel>
                                            </h:panelGrid>
                                            <h:panelGrid columns="1">
                                                <h:graphicImage value="/images/iddata.png" rendered="#{clockManagerBean.clock.repType == 1}"/>
                                                <h:graphicImage value="/images/trilobit.png" rendered="#{clockManagerBean.clock.repType == 2}"/>
                                                <h:graphicImage value="/images/zk.png" rendered="#{clockManagerBean.clock.repType == 3}"/>
                                            </h:panelGrid>
                                        </h:panelGrid>
                                    </h:panelGrid>
                                    <a4j:commandButton id="saveClock" value="Salvar" reRender="repPanel" action="#{clockManagerBean.renameRep}">
                                        <rich:componentControl for="clockDetails" attachTo="saveClock" operation="hide" event="onclick"/>
                                    </a4j:commandButton>
                                </h:form>
                            </rich:panel>


                            <rich:panel bodyClass="pbody">
                                <h:form>
                                    <a4j:commandButton value="Verificar!" reRender="onlinePanel, infoPanel" action="#{clockManagerBean.verifySingleClock}"/>
                                </h:form>
                                <%--a4j:outputPanel id="infoPanel">
                                    <h:panelGrid id="clockGrid" columns="1">
                                        <h:panelGrid id="clockGrid1" columns="1" style="text-align:center">
                                            <h:panelGrid id="qtdUserGrid" columns="2" rendered="#{clockManagerBean.clock.qtdUsers != 0}">
                                                <h:outputText value="Nº de Usuários: "/>
                                                <rich:progressBar maxValue="#{clockManagerBean.clock.maxUsers}" value="#{clockManagerBean.clock.qtdUsers}">
                                                    <h:outputLabel value="Quantidade de Usuários #{clockManagerBean.clock.qtdUsers}/#{clockManagerBean.clock.maxUsers}"/>
                                                </rich:progressBar>
                                            </h:panelGrid>
                                            <h:panelGrid id="qtdDigitGrid" columns="2" rendered="#{clockManagerBean.clock.qtdDigitais != 0}">
                                                <h:outputText value="Nº de Digitais: "/>
                                                <rich:progressBar maxValue="#{clockManagerBean.clock.maxDigitais}" value="#{clockManagerBean.clock.qtdDigitais}">
                                                    <h:outputLabel value="Quantidade de Digitais: #{clockManagerBean.clock.qtdDigitais}/#{clockManagerBean.clock.maxDigitais}"/>
                                                </rich:progressBar>
                                            </h:panelGrid>
                                        </h:panelGrid>

                                        <h:panelGrid id="clockGrid2" columns="1" style="text-align:center">
                                            <h:panelGrid id="bobinaGrid" columns="2" rendered="#{clockManagerBean.clock.qtdBobina != 0}">
                                                <h:outputText value="Uso da bobina: "/>
                                                <h:outputText value="#{clockManagerBean.clock.qtdBobina}%"/>
                                            </h:panelGrid>

                                            <h:panelGrid id="memoriaGrid" columns="2" rendered="#{clockManagerBean.clock.qtdMemory != 0}">
                                                <h:outputText value="Uso de memória: "/>
                                                <h:outputText value="#{clockManagerBean.clock.qtdMemory}%"/>
                                            </h:panelGrid>

                                            <h:panelGrid id="bateriaGrid" columns="2" rendered="#{clockManagerBean.clock.qtdBatery != 0}">
                                                <h:outputText value="Uso de bateria: "/>
                                                <h:outputText value="#{clockManagerBean.clock.qtdBatery}%"/>
                                            </h:panelGrid>
                                        </h:panelGrid>

                                        <h:form id="AtualizaDadosRep">
                                            <h:panelGrid id="Buttons" columns="3" style="text-align:center">
                                                <h:outputText value="Horas do relógios: "/>
                                                <h:outputText id="clockTime" value="#{clockManagerBean.horas}"/>
                                                <h:commandButton value="Ajustar" id="adjustClock"/>

                                                <h:outputText value="Coletar manualmente os últimos "/>
                                                <h:panelGrid id="dias" columns="2" style="text-align:center">
                                                    <h:inputText value="#{clockManagerBean.diasColetados}" size="1" maxlength="2"/>
                                                    <h:outputText value="dias"/>
                                                </h:panelGrid>
                                                <a4j:commandButton value="Coletar" id="colectChecks" reRender="onlinePanel, infoPanel" action="#{clockManagerBean.coletar}"/>
                                            </h:panelGrid>
                                        </h:form>
                                    </h:panelGrid>
                                </a4j:outputPanel--%>
                            </rich:panel>

                        </center>
                    </rich:modalPanel>

                    <rich:modalPanel rendered="#{clockManagerBean.showFileBrowser}" id="browserFile" width="440" height="440" styleClass="center">
                        <f:facet name="header">
                            <h:panelGroup>
                                <h:outputText value="Selecione" ></h:outputText>
                            </h:panelGroup>
                        </f:facet>
                        <f:facet name="controls">
                            <h:panelGroup>
                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                <rich:componentControl for="browserFile" attachTo="hidelink" operation="hide" event="onclick"/>
                            </h:panelGroup>
                        </f:facet>
                        <center> 
                            <h:panelGrid id="virifyIDDATAgrid" columns="1" style="text-align:center">
                                <h:outputText style="color: red" value="Executavel não encontrado! " styleClass="label"/>
                                <br/>
                                <h:outputText id="way" value="#{clockManagerBean.fileWay}" styleClass="label"/>
                                <br/>
                                <h:outputText value="Selecione o local do arquivo .exe do relógio! " styleClass="label"/>
                                <br/>
                                <h:outputText style="color: red" value="Caminho invalido! " styleClass="label" rendered="#{clockManagerBean.newValidWay == 1}"/>
                                <h:outputText style="color: green" value="Caminho validado! " styleClass="label" rendered="#{clockManagerBean.newValidWay == 2}"/>
                                <h:inputText value="#{clockManagerBean.newFileWay}" id="newfileway" size="70">
                                    <a4j:support event="onblur" reRender="addJornadaGrid"
                                                 requestDelay="1100" action="#{clockManagerBean.verify}"/>
                                </h:inputText>
                            </h:panelGrid>
                            <center>
                                <h:commandButton value="Salvar" id="addWay" action="#{clockManagerBean.submitWay}">
                                    <rich:componentControl for="browserFile" attachTo="addWay" operation="hide" event="onclick"/>
                                </h:commandButton>
                            </center>
                        </center>
                    </rich:modalPanel>
                    <jsp:include page="../www/_bot.jsp"/>
                </center> 
            </f:view>
        </center>
    </body>
</html>
