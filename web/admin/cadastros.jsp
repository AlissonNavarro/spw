<%--
    Document   : relatorioMensal
    Created on : Feb 1, 2010, 9:59:08 PM
    Author     : Alexandre
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

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
                    <a4j:keepAlive beanName="horarioBean"/>
                    <a4j:keepAlive beanName="justificativaBean"/>
                    <a4j:keepAlive beanName="feriadoBean"/>
                    <h:panelGrid columns="1" >
                        <rich:tabPanel switchType="client" width="965">
                            <rich:tab id="tab1" label="Setores" disabled="true">                                
                            </rich:tab>
                            <rich:tab id="tab2" label="Horários" >
                                <br/>
                                <center>                                    
                                    <h:panelGrid id="panelGridHorario" columns="1">
                                        <rich:dataTable id="horarioList"
                                                        value="#{horarioBean.horarioList}"
                                                        var="linha"
                                                        rows="15"
                                                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Nome" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Entrada" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Início da Entrada" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Fim da Entrada" />
                                                    </h:column>                                                   
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Saída" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Início da Saída" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Fim da Saída" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Tolerância Entrada" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Tolerância Saída" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Editar" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Deletar" />
                                                    </h:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{linha.nome}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="entradaInput" value="#{linha.entrada}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="ifentradainput"value="#{linha.inicioFaixaEntrada}"/>

                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="ffentradainput" value="#{linha.fimFaixaEntrada}"/>
                                                </center>
                                            </h:column>                                            
                                            <h:column>
                                                <center>
                                                    <h:outputText id="saidainput" value="#{linha.saida}" >
                                                        <rich:jQuery selector="#saidainput" query="mask('99:99')" timing="onload"/>                                                       
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="ifsaidainput" value="#{linha.inicioFaixaSaida}" >
                                                        <rich:jQuery selector="#ifsaidainput" query="mask('99:99')" timing="onload"/>                                                       
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="ffsaidainput" value="#{linha.fimFaixaSaida}">
                                                        <rich:jQuery selector="#ffsaidainput" query="mask('99:99')" timing="onload"/>                                                      
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="toleranciaEntradaOutput" value="#{linha.toleranciaEntrada}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="toleranciaSaidaOutput" value="#{linha.toleranciaSaida}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>  
                                                    <h:outputLink  value="#" id="link">
                                                        <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                        <rich:componentControl for="editmodalPanel" attachTo="link" operation="show" event="onclick"/>
                                                        <a4j:support action="#{horarioBean.showEditarNovoHorario}"
                                                                     event="onclick"
                                                                     reRender="paneleditHorario,f_messagens">
                                                            <f:param name="horario_id_editar" value="#{linha.horario_id}"/>
                                                        </a4j:support>
                                                    </h:outputLink>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <a4j:commandButton id="btDeleteHorario"
                                                                       value="Deletar"
                                                                       image="../images/delete.png"                                                                      
                                                                       reRender="panelGridHorario,f_messagens"
                                                                       ajaxSingle="true"
                                                                       action="#{horarioBean.deletarLinha}">
                                                        <f:param name="horario_id_delete" value="#{linha.horario_id}"/>
                                                    </a4j:commandButton>
                                                </center>
                                            </h:column>
                                        </rich:dataTable>
                                        <rich:datascroller  id="datascrollers"
                                                            for="horarioList"
                                                            ajaxSingle="true"
                                                            renderIfSinglePage="false"
                                                            >
                                        </rich:datascroller>
                                        <br/>
                                        <h:panelGroup> 
                                            <rich:modalPanel id="addmodalPanel" width="320" height="280">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Novo Horário"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                        <rich:componentControl for="addmodalPanel" attachTo="hidelink" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id="paneladdHorario" columns="3" >
                                                        <h:outputText value="Nome do turno: " styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:inputText size="20" value="#{horarioBean.novoHorario.nome}" maxlength="20"/>
                                                        <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <rich:spacer width="5"/>
                                                        <h:inputText id="NHEntrada" size="2" value="#{horarioBean.novoHorario.entrada}" maxlength="20" >
                                                            <rich:jQuery selector="#NHEntrada" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Horário de Saída" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NHSaida" size="2" value="#{horarioBean.novoHorario.saida}">
                                                            <rich:jQuery selector="#NHSaida" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Início da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NIFEntrada" size="2" value="#{horarioBean.novoHorario.inicioFaixaEntrada}">
                                                            <rich:jQuery selector="#NIFEntrada" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Fim da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NFFEntrada" size="2" value="#{horarioBean.novoHorario.fimFaixaEntrada}">
                                                            <rich:jQuery selector="#NFFEntrada" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>

                                                        <h:outputText value="Início da Faixa de Saída" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NIFSaida" size="2" value="#{horarioBean.novoHorario.inicioFaixaSaida}">
                                                            <rich:jQuery selector="#NIFSaida" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Fim da Faixa de Saída" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NFFSaida" size="2" value="#{horarioBean.novoHorario.fimFaixaSaida}">
                                                            <rich:jQuery selector="#NFFSaida" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Tolerância da Entrada (min):" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="NTEntrada" size="1" value="#{horarioBean.novoHorario.toleranciaEntrada}">
                                                            <rich:jQuery selector="#NTEntrada" query="mask('99')" timing="onload"/>
                                                        </h:inputText>
                                                        <h:outputText value="Tolerância da Saída (min):" styleClass="labelRight" style="float:right"/>
                                                        <h:inputText id="tsaida" size="1" value="#{horarioBean.novoHorario.toleranciaSaida}">
                                                            <rich:jQuery selector="#tsaida" query="mask('99')" timing="onload"/>
                                                        </h:inputText>
                                                    </h:panelGrid>
                                                    <br/>
                                                    <center>
                                                        <h:commandButton value="Salvar" id="addID" action="#{horarioBean.addNovoHorario}">
                                                            <rich:componentControl for="addmodalPanel" attachTo="addID" operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </center>
                                            </rich:modalPanel>
                                            <center>
                                                <h:outputLink  value="#" id="link">
                                                    <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                    <rich:componentControl for="addmodalPanel" attachTo="link" operation="show" event="onclick"/>
                                                    <a4j:support action="#{horarioBean.showAdicionarNovoHorario}"
                                                                 event="onclick"
                                                                 reRender="paneladdHorario,f_messagens">
                                                    </a4j:support>
                                                </h:outputLink>
                                                <br/>
                                                Adicionar
                                            </center>
                                        </h:panelGroup>

                                        <rich:modalPanel id="editmodalPanel" width="320" height="280">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Horário"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="edithidelink"/>
                                                    <rich:componentControl for="editmodalPanel" attachTo="edithidelink" operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>   
                                            <center>                                                                                               
                                                <h:panelGrid  id="paneleditHorario" columns="3" >
                                                    <h:outputText value="Nome do turno: " styleClass="labelRight" />
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="nome_input"size="20" value="#{horarioBean.editHorario.nome}" maxlength="20"/>
                                                    <h:outputText value="Horário de Entrada:" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNHEntrada" size="2" value="#{horarioBean.editHorario.entrada}" maxlength="20">
                                                        <rich:jQuery selector="#editNHEntrada" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Horário de Saída" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNHSaida" size="2" value="#{horarioBean.editHorario.saida}">
                                                        <rich:jQuery selector="#editNHSaida" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Início da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNIFEntrada" size="2" value="#{horarioBean.editHorario.inicioFaixaEntrada}">
                                                        <rich:jQuery selector="edit#NIFEntrada" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Fim da Faixa de Entrada:" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNFFEntrada" size="2" value="#{horarioBean.editHorario.fimFaixaEntrada}">
                                                        <rich:jQuery selector="#editNFFEntrada" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Início da Faixa de Saída" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNIFSaida" size="2" value="#{horarioBean.editHorario.inicioFaixaSaida}">
                                                        <rich:jQuery selector="#editNIFSaida" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Fim da Faixa de Saída" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNFFSaida" size="2" value="#{horarioBean.editHorario.fimFaixaSaida}">
                                                        <rich:jQuery selector="#editNFFSaida" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Tolerância da Entrada (min):" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="editNTEntrada"  size="1" value="#{horarioBean.editHorario.toleranciaEntrada}">
                                                        <rich:jQuery selector="#editNTEntrada" query="mask('99')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Tolerância da Saída (min):" styleClass="labelRight" style="float:right"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="edittsaida" size="1" value="#{horarioBean.editHorario.toleranciaSaida}">
                                                        <rich:jQuery selector="#edittsaida" query="mask('99')" timing="onload"/>
                                                    </h:inputText>
                                                </h:panelGrid>
                                                <br/>
                                                <center> 
                                                    <h:commandButton value="Editar" id="editarID" action="#{horarioBean.editarHorario}">
                                                        <rich:componentControl for="editmodalPanel"
                                                                               attachTo="editarID"
                                                                               operation="hide"
                                                                               event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                    </h:panelGrid>
                                </center>
                            </rich:tab>
                            <rich:tab id="tab3" label="Jornadas" disabled="true">
                            </rich:tab>
                            <rich:tab id="tab4" label="Feriados">
                                <br/>
                                <center>
                                    <h:outputText value="Lista de Feriados" styleClass="labelMaior"/>
                                    <br/><br/>
                                    <h:panelGrid id="panelGridFeriado" columns="1">
                                        <rich:dataTable id="feriadoList"
                                                        value="#{feriadoBean.feriadoList}"
                                                        var="linha"
                                                        rows="10"
                                                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="NOME" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="DATA" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="CRÍTICO" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="EDITAR" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="DELETAR" />
                                                    </h:column>
                                                </rich:columnGroup>
                                            </f:facet>                                            
                                            <h:column>
                                                <center>
                                                    <h:outputText id="nomeFeriadoInput" value="#{linha.nome}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText id="dataFeriadoInput" value="#{linha.data}">
                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:selectBooleanCheckbox id="isOficialInput" value="#{linha.isOficial}" disabled="true"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputLink  value="#" id="linkFeriado">
                                                        <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                        <rich:componentControl for="editmodalPanelFeriado" attachTo="linkFeriado" operation="show" event="onclick"/>
                                                        <a4j:support action="#{feriadoBean.showEditarNovoFeriado}"
                                                                     event="onclick"
                                                                     reRender="paneleditFeriado,f_messagens">
                                                            <f:param name="feriado_id_editar" value="#{linha.id}"/>
                                                        </a4j:support>
                                                    </h:outputLink>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <a4j:commandButton id="btDeleteFeriado"
                                                                       value="Deletar"
                                                                       image="../images/delete.png"
                                                                       reRender="panelGridFeriado,f_messagens"
                                                                       ajaxSingle="true"
                                                                       action="#{feriadoBean.deletarFeriado}"
                                                                       onclick="javascript:if (!confirm('Deseja deletar o feriado?')) return false;">
                                                        <f:param name="feriado_id_delete" value="#{linha.id}"/>
                                                    </a4j:commandButton>
                                                </center>
                                            </h:column>
                                        </rich:dataTable>
                                        <rich:datascroller  id="datascrollersFeriado"
                                                            for="feriadoList"
                                                            ajaxSingle="true"
                                                            renderIfSinglePage="false"
                                                            reRender="f_messagens"
                                                            >
                                        </rich:datascroller>
                                        <br/>
                                        <h:panelGroup>
                                            <rich:modalPanel id="addmodalPanelFeriado" width="280" height="150" autosized="true" style="text-align:center;float:center;">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Novo Feriado"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkFeriado"/>
                                                        <rich:componentControl for="addmodalPanelFeriado" attachTo="hidelinkFeriado" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id="paneladdFeriado" columns="3" >
                                                        <h:outputText value="Nome do feriado: " styleClass="labelRight" style="float:right;"/>
                                                         <rich:spacer width="5"/>
                                                        <h:inputText size="20" value="#{feriadoBean.novoFeriado.nome}" maxlength="20"/>
                                                        <h:outputText value="Data: " styleClass="labelRight"  style="float:right;"/>
                                                        <rich:spacer width="5"/>
                                                        <rich:calendar inputSize="8" locale="#{feriadoBean.objLocale}" value="#{feriadoBean.novoFeriado.data}"/>                                                         
                                                        <h:outputText value="Oficial" styleClass="labelRight" style="float:right;"/>
                                                        <rich:spacer width="5"/>
                                                        <h:selectBooleanCheckbox  value="#{feriadoBean.novoFeriado.isOficial}"/>
                                                    </h:panelGrid>
                                                    <br/>
                                                    <center>
                                                        <h:commandButton value="Salvar" id="addFeriadoID" action="#{feriadoBean.addNovoFeriado}">
                                                            <rich:componentControl for="addmodalPanelFeriado" attachTo="addFeriadoID" operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </center>
                                            </rich:modalPanel>
                                            <center>
                                                <h:outputLink  value="#" id="linkFeriado">
                                                    <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                    <rich:componentControl for="addmodalPanelFeriado" attachTo="linkFeriado" operation="show" event="onclick"/>
                                                    <a4j:support action="#{feriadoBean.showAdicionarNovoFeriado}"
                                                                 event="onclick"
                                                                 reRender="paneladdFeriado,f_messagens">
                                                    </a4j:support>
                                                </h:outputLink>
                                                <br/>
                                                Adicionar
                                            </center>
                                        </h:panelGroup>

                                        <rich:modalPanel id="editmodalPanelFeriado" width="280" height="150" autosized="true" style="text-align:center;float:center;">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Horário"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="edithidelinkFeriado"/>
                                                    <rich:componentControl for="editmodalPanelFeriado" attachTo="edithidelinkFeriado" operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid id="paneleditFeriado" columns="3">
                                                    <h:outputText value="Nome do feriado: " styleClass="labelRight" style="float:right;"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="20" value="#{feriadoBean.editFeriado.nome}" maxlength="20"/>
                                                    <h:outputText value="Data: " styleClass="labelRight"  style="float:right;"/>
                                                    <rich:spacer width="5"/>
                                                    <rich:calendar  inputSize="8" locale="#{feriadoBean.objLocale}" value="#{feriadoBean.editFeriado.data}"/>
                                                    <h:outputText value="Oficial: " styleClass="labelRight" style="float:right;"/>
                                                    <rich:spacer width="5"/>
                                                    <h:selectBooleanCheckbox  value="#{feriadoBean.editFeriado.isOficial}"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Editar" id="editarFeriadoID" action="#{feriadoBean.editarFeriado}">
                                                        <rich:componentControl for="editmodalPanelFeriado"
                                                                               attachTo="editarFeriadoID"
                                                                               operation="hide"
                                                                               event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                    </h:panelGrid>
                                </center>
                            </rich:tab>
                                <rich:tab id="tab5" label="Justificativas" disabled="true">
                                <center>
                                    <br/>
                                    <h:panelGrid columns="5">
                                        <a4j:outputPanel id="justPanel">
                                            <rich:dataTable value="#{justificativaBean.justificativa}"
                                                            var="justificativa"                                                           
                                                            id="table"
                                                            rows="12"
                                                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                            onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'">>
                                                <f:facet name="header">
                                                    <rich:columnGroup>
                                                        <h:column>
                                                            <h:outputText value="Justificativa" />
                                                        </h:column>
                                                        <h:column>
                                                            <h:outputText value="Editar" />
                                                        </h:column>
                                                        <h:column>
                                                            <h:outputText value="Deletar" />
                                                        </h:column>
                                                    </rich:columnGroup>
                                                </f:facet>
                                                <h:column>
                                                    <center>
                                                        <h:outputText  value="#{justificativa.justificativaNome}"/>
                                                    </center>
                                                </h:column>
                                                <h:column>
                                                    <center>
                                                        <h:outputLink  value="#" id="linkJustificativa">
                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                            <rich:componentControl for="editJustificativaModalPanel"
                                                                                   attachTo="linkJustificativa"
                                                                                   operation="show"
                                                                                   event="onclick"/>
                                                            <a4j:support action="#{justificativaBean.showEditar}"
                                                                         event="onclick"
                                                                         reRender="paneleditJustificativa,f_messagens">
                                                                <f:param name="justificativa_id" value="#{justificativa.justificativaID}"/>
                                                            </a4j:support>
                                                        </h:outputLink>
                                                    </center>
                                                </h:column>
                                                <h:column>
                                                    <center>
                                                        <a4j:commandButton image="../images/delete.png"
                                                                           action="#{justificativaBean.deletar}"
                                                                           reRender="justPanel,f_messagens"
                                                                           ajaxSingle="true">
                                                            <f:param name="justificativa_id" value="#{justificativa.justificativaID}"/>
                                                        </a4j:commandButton>
                                                    </center>
                                                </h:column>
                                            </rich:dataTable>
                                            <rich:modalPanel id="editJustificativaModalPanel" width="260" height="110">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Editar Justificativa"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="editJustificativahidelink"/>
                                                        <rich:componentControl for="editJustificativaModalPanel"
                                                                               attachTo="editJustificativahidelink"
                                                                               operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid  id="paneleditJustificativa" columns="3" >
                                                        <h:outputText value="Nome do Abono:  " styleClass="labelRight"/>
                                                        <rich:spacer width="5"/>
                                                        <h:inputText size="20" value="#{justificativaBean.editJustificativa.justificativaNome}" maxlength="20"/>
                                                    </h:panelGrid>
                                                    <br/>
                                                    <center>
                                                        <h:commandButton value="Editar" id="editarJustificativaID" action="#{justificativaBean.editar}">
                                                            <rich:componentControl for="editJustificativaModalPanel" attachTo="editarJustificativaID" operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </center>
                                            </rich:modalPanel>
                                            <br/>
                                            <center>
                                                <h:outputLink  value="#" id="linkJustificativa">
                                                    <h:graphicImage  value="../images/add.png" style="border:0"/>
                                                    <rich:componentControl for="addJustificativaModalPanel" attachTo="linkJustificativa" operation="show" event="onclick"/>
                                                    <a4j:support action="#{justificativaBean.showInserir}"
                                                                 event="onclick"
                                                                 reRender="paneladdJustificativa,f_messagens">
                                                    </a4j:support>
                                                </h:outputLink>
                                                <br/>
                                                Adicionar
                                            </center>
                                        </a4j:outputPanel>
                                        <rich:modalPanel id="addJustificativaModalPanel" width="260" height="110">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Adicionar Justificativa"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addJustificativahidelink"/>
                                                    <rich:componentControl for="addJustificativaModalPanel"
                                                                           attachTo="addJustificativahidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneladdJustificativa" columns="3" >
                                                    <h:outputText value="Nome do Abono:  " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="20" value="#{justificativaBean.novaJustificativa.justificativaNome}" maxlength="20"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Salvar" id="addJustificativaID" action="#{justificativaBean.inserir}">
                                                        <rich:componentControl for="addJustificativaModalPanel" attachTo="addJustificativaID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                    </h:panelGrid>
                                </center>
                            </rich:tab>
                        </rich:tabPanel>
                    </h:panelGrid>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>

