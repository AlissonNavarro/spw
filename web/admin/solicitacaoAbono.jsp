<%-- 
    Document   : solicitacaoAbono
    Created on : 05/01/2010, 17:51:18
    Author     : amsgama
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<script type="text/javascript" src="../javaScript/jquery.js"></script>
<script language="JavaScript" type="text/javascript">
    var $j = $.noConflict();

    function limitText(limitField, limitNum) {
        if (limitField.value.length > limitNum) {
            limitField.value = limitField.value.substring(0, limitNum);
        }
    }
</script>

<%@ page import="Abono.ConsultaDiaEmAbertoBean" %>
<%@ page import="Usuario.UsuarioBean" %>
<%@ page import="javax.faces.context.FacesContext" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <title>Solicitação de Abono</title>
    </head>
    <body>
        <f:view>
            <a4j:keepAlive beanName="historicoAbonoBean"/>
            <a4j:keepAlive beanName="abonoEmMassaBean"/>
            <a4j:keepAlive beanName="topAbonoBean"/>
            <jsp:include page="../www/_top.jsp"/>
            <center>
                <h:form id="f_messagens" prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
            </center>
            <center>
                <rich:tabPanel switchType="client"  width="965"  selectedTab="#{solicitacaoAbonoBean.abaCorrente}">
                    <rich:tab id="tab1" label="Solicitações" ignoreDupResponses="true" rendered="#{usuarioBean.perfil.solicitacao== true}" >
                        <a4j:support event="ontabenter" action="#{solicitacaoAbonoBean.setAba()}">
                            <a4j:actionparam name="tab" value="tab1"/>
                        </a4j:support>
                        <center>
                            <h:form id="solicitacoesForm">
                                <br/>
                                <h:panelGrid columns="7">
                                    <h:outputText value="Departamento: " styleClass="label"/>
                                    <rich:spacer width="12"/>
                                    <h:outputText value="Data Inicial: " styleClass="label"/>
                                    <rich:spacer width="12"/>
                                    <h:outputText value="Data Final:" styleClass="label"/>
                                    <rich:spacer width="12"/>
                                    <rich:spacer width="12"/>

                                    <h:selectOneMenu id="departManuTAdmin" value="#{solicitacaoAbonoBean.departamentoSelecionado}">
                                        <f:selectItems value="#{solicitacaoAbonoBean.departamentosSelecItem}"/>                                       
                                    </h:selectOneMenu>
                                    <rich:spacer width="12"/>

                                    <h:outputText value="#{consultaFrequenciaSemEscalaBean.dataInicio}" styleClass="label" rendered="#{usuarioBean.perfil.pesquisarData==false}"/>                                                                                   
                                    <rich:calendar inputSize="8" locale="#{solicitacaoAbonoBean.objLocale}" value="#{solicitacaoAbonoBean.dataInicio}" rendered="#{usuarioBean.perfil.pesquisarData==true}"/>

                                    <rich:spacer width="12"/>
                                    <h:outputText value="#{consultaFrequenciaSemEscalaBean.ultimoDiaDoMes()}" styleClass="label" rendered="#{usuarioBean.perfil.pesquisarData==false}"/>
                                    <rich:calendar inputSize="8" locale="#{solicitacaoAbonoBean.objLocale}" value="#{solicitacaoAbonoBean.dataFim}" rendered="#{usuarioBean.perfil.pesquisarData==true}"/>

                                    <rich:spacer width="12"/>
                                    <h:commandButton value="Pesquisar" action="#{solicitacaoAbonoBean.geraSolicitacaoAbonoList}"
                                                     />

                                    <h:panelGroup>
                                        <h:selectBooleanCheckbox value="#{solicitacaoAbonoBean.incluirSubSetores}"/>
                                        <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                    </h:panelGroup>
                                </h:panelGrid>

                                <br><br><br>
                                <a4j:outputPanel id="tableSolicitacoesAbonoGroup">
                                    <rich:dataTable
                                        id="tableSolicitacoesAbono" rows="20"
                                        rowClasses="zebra1,zebra2"
                                        value="#{solicitacaoAbonoBean.solicitacaoAbonoList}"
                                        var="solicitacaoAbono" rendered="#{not empty solicitacaoAbonoBean.solicitacaoAbonoList}">

                                        <f:facet name="header">
                                            <h:outputText value="SOLICITAÇÕES DE ABONO"/>
                                        </f:facet>
                                        <rich:column sortBy="#{solicitacaoAbono.inclusao}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="SOLICITAÇÃO ABONO"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.inclusao}">
                                                <f:convertDateTime pattern="dd/MM/yyyy"/>
                                            </h:outputText>
                                        </rich:column>
                                        <rich:column sortBy="#{solicitacaoAbono.departamento}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DEPARTAMENTO"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.departamento}"/>
                                        </rich:column>
                                        <rich:column sortBy="#{solicitacaoAbono.nome}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="NOME"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.nome}"/>
                                        </rich:column>
                                        <rich:column sortBy="#{solicitacaoAbono.data}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DATA"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.data}" >
                                                <f:convertDateTime pattern="dd/MM/yyyy" />
                                            </h:outputText>
                                        </rich:column>
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="PONTOS SOLICITADOS"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.pontosSolicitados}"/>
                                        </rich:column>
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DESCRIÇÃO"/>
                                            </f:facet>
                                            <h:outputText value="#{solicitacaoAbono.descricao}"/>
                                        </rich:column>

                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="ACEITAR"/>
                                            </f:facet>
                                            <h:commandLink id="myId"  action="abonar">
                                                <h:graphicImage  value="../images/aceito.png" style="border:0"/>
                                                <f:param name="cod_solicitacao" value="#{solicitacaoAbono.cod}"/>
                                            </h:commandLink>
                                        </rich:column>

                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="NEGAR"/>
                                            </f:facet>
                                            <a4j:commandButton action="#" id="link" image="../images/negado.png">
                                                <rich:componentControl for="negarPanel" attachTo="link" operation="show" event="onclick"/>
                                            </a4j:commandButton>

                                            <center>
                                                <rich:modalPanel id="negarPanel" width="330" height="190">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="NEGAR"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                            <rich:componentControl for="negarPanel" attachTo="hidelink" operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:outputText value="Digite uma justificativa: "/>
                                                        <h:inputTextarea onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                         rows="4" cols="45" value="#{solicitacaoAbono.respostaJustificativa}" />

                                                        <br/>
                                                        <br/>
                                                        <h:commandButton value="Enviar" type="submit" id="enviar" action="#{solicitacaoAbono.negar}">
                                                            <rich:componentControl for="negarPanel" attachTo="enviar" operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </rich:modalPanel>
                                            </center>

                                        </rich:column>
                                    </rich:dataTable>
                                    <br/>
                                    <rich:datascroller align="center" for="tableSolicitacoesAbono" maxPages="20"
                                                       page="1" id="sc2"
                                                       renderIfSinglePage="false"/>
                                </a4j:outputPanel>
                                <br/>
                                <br/>
                            </h:form>
                        </center>
                    </center>
                </rich:tab>
                <rich:tab id="tab2" label="Dias em Aberto" ignoreDupResponses="true" rendered="#{usuarioBean.perfil.diasEmAberto== true}">
                    <a4j:support event="ontabenter" action="#{solicitacaoAbonoBean.setAba()}">
                        <a4j:actionparam name="tab" value="tab2"/>
                    </a4j:support>
                    <br/>
                    <a4j:region id="region1">
                        <h:form id="diasForm">
                            <center>
                                <h:panelGrid columns="6">
                                    <h:outputText value="Departamento: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Inicial: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Final:" styleClass="label"/>
                                    <rich:spacer width="10"/>                                    

                                    <h:selectOneMenu id="departManuEmAberto" value="#{consultaDiaEmAbertoBean.departamento}">
                                        <f:selectItems value="#{consultaDiaEmAbertoBean.departamentolist}"/>
                                    </h:selectOneMenu>
                                    <rich:spacer width="10"/>

                                    <h:outputText value="#{consultaFrequenciaSemEscalaBean.dataInicio}" styleClass="label" rendered="#{usuarioBean.perfil.pesquisarData==false}"/>                                               
                                    <rich:calendar inputSize="8" locale="#{consultaDiaEmAbertoBean.objLocale}" value="#{consultaDiaEmAbertoBean.dataInicio}" rendered="#{usuarioBean.perfil.pesquisarData==true}"/>
                                    <rich:spacer width="8"/>

                                    <h:outputText value="#{consultaFrequenciaSemEscalaBean.ultimoDiaDoMes()}" styleClass="label" rendered="#{usuarioBean.perfil.pesquisarData==false}"/>
                                    <rich:calendar inputSize="8" locale="#{consultaDiaEmAbertoBean.objLocale}" value="#{consultaDiaEmAbertoBean.dataFim}" rendered="#{usuarioBean.perfil.pesquisarData==true}"/>
                                    <h:panelGroup>
                                        <rich:spacer width="8"/>
                                        <a4j:commandButton value="Pesquisar" action="#{consultaDiaEmAbertoBean.consultar}"
                                                           reRender="panelDiaEmAberto"/>
                                    </h:panelGroup>

                                    <h:panelGroup>
                                        <h:selectBooleanCheckbox value="#{consultaDiaEmAbertoBean.incluirSubSetores}"/>
                                        <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                    </h:panelGroup>
                                </h:panelGrid>
                                <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()">

                                </a4j:status>
                                <rich:modalPanel id="panelStatus" autosized="true" >
                                    <h:panelGrid columns="3">
                                        <h:graphicImage url="../images/load.gif" />
                                        <rich:spacer width="8"/>
                                        <h:outputText value="  Carregando…" styleClass="label" />
                                    </h:panelGrid>
                                </rich:modalPanel>
                            </center>
                            <br/>
                            <center>
                                <h:panelGroup id="panelDiaEmAberto">
                                    <h:panelGroup rendered="#{consultaDiaEmAbertoBean.isShowPesquisa}">
                                        <h:outputText id="filtro" value="Busca por nome: " styleClass="label"/>
                                        <rich:spacer width="7"/>
                                        <h:inputText id="inputPesquisa"  value="#{consultaDiaEmAbertoBean.filtro}" size="40">
                                            <a4j:support event="onkeyup" reRender="panelDiaEmAberto"
                                                         ignoreDupResponses="true" requestDelay="750"
                                                         action="#{consultaDiaEmAbertoBean.consultar}"/>
                                        </h:inputText>
                                    </h:panelGroup>
                                    <br/><br/>
                                    <rich:dataTable
                                        id="tableDiaEmAberto" rows="20"
                                        value="#{consultaDiaEmAbertoBean.diaEmAbertoList}"
                                        rendered="#{not empty consultaDiaEmAbertoBean.diaEmAbertoList}"
                                        rowClasses="zebra1,zebra2"                                       
                                        var="diaEmAberto">                                       
                                        <f:facet name="header">
                                            <h:outputText value="DIAS COM REGISTRO DE PONTO EM ABERTO"/>
                                        </f:facet>
                                        <%--rich:column style="text-align:center;display:none">
                                            <f:facet name="header">
                                                <h:outputText value="DETALHE"/>
                                            </f:facet>
                                            <h:commandLink action="#{consultaDiaEmAbertoBean.navegaAbono}">
                                                <h:graphicImage  value="../images/linha.png" style="border:0"/>
                                                <f:param  name="pontosAbertos" value="#{diaEmAberto.pontosAbertos}"/>
                                                <f:param  name="cod_funcionario" value="#{diaEmAberto.cod_funcionario}"/>
                                                <f:param  name="nomeFuncionario" value="#{diaEmAberto.nomeFuncionario}"/>
                                                <f:param  name="departamento" value="#{diaEmAberto.departamento}"/>
                                                <f:param  name="dataStr" value="#{diaEmAberto.dataStr}"/>
                                            </h:commandLink>
                                        </rich:column--%>
                                        <rich:column sortBy="#{diaEmAberto.data}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DATA"/>
                                            </f:facet>
                                            <h:outputText value="#{diaEmAberto.data}">
                                                <f:convertDateTime pattern="dd/MM/yyyy"/>
                                            </h:outputText>
                                        </rich:column>
                                        <rich:column sortBy="#{diaEmAberto.nomeFuncionario}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="NOME"/>
                                            </f:facet>
                                            <h:outputText value="#{diaEmAberto.nomeFuncionario}" />
                                        </rich:column>
                                        <rich:column sortBy="#{diaEmAberto.departamento}" style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DEPARTAMENTO"/>
                                            </f:facet>
                                            <h:outputText value="#{diaEmAberto.departamento}"/>
                                        </rich:column>
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="REGISTROS EM ABERTO"/>
                                            </f:facet>
                                            <h:outputText value="#{diaEmAberto.pontosAbertos}"/>
                                        </rich:column>
                                        <rich:column style="text-align:center" rendered="#{usuarioBean.perfil.abonosRapidos == true}">
                                            <f:facet name="header">
                                                <h:outputText value="MOTIVOS"/>
                                            </f:facet>
                                            <h:selectOneMenu id="departManuEmAberto" value="#{diaEmAberto.justificativa}">
                                                <f:selectItems value="#{consultaDiaEmAbertoBean.justificativaList}"/>
                                                <a4j:support event="onchange" action="#{diaEmAberto.isMotivoDeselecionado}" reRender="markedDiaEmAberto"/>

                                            </h:selectOneMenu>
                                        </rich:column>
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="DESCRIÇÃO"/>
                                            </f:facet>
                                            <h:outputText value="#{diaEmAberto.descricao}"/>
                                        </rich:column>
                                        <%--
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="ACEITAR"/>
                                            </f:facet>
                                            <a4j:commandButton styleClass="acceptButtons" reRender="" rendered="#{diaEmAberto.isDeletavel}" onclick="$j(this).closest('tr').hide()" action="#{diaEmAberto.aceitar}"/>
                                        </rich:column>
                                        --%>
                                        <rich:column style="text-align:center">
                                            <f:facet name="header">
                                                <h:outputText value="NEGAR"/>
                                            </f:facet>
                                            <a4j:commandButton styleClass="denyButtons" reRender="" rendered="#{diaEmAberto.isDeletavel}" onclick="$j(this).closest('tr').hide()" action="#{diaEmAberto.negar}" />
                                        </rich:column>
                                    </rich:dataTable>
                                    <rich:datascroller id="dcDiasEmAberto" align="center" for="tableDiaEmAberto"
                                                       page="#{consultaDiaEmAbertoBean.page}"
                                                       renderIfSinglePage="false"
                                                       />
                                    <br/><br/>
                                    <a4j:commandButton value="Abonar" action="#{consultaDiaEmAbertoBean.abonarEmMassa}"
                                                       reRender="panelDiaEmAberto" 
                                                       rendered="#{(usuarioBean.perfil.abonosRapidos == true)
                                                                   and consultaDiaEmAbertoBean.isShowPesquisa}"/>

                                </h:panelGroup>
                                <br/>
                                <br/>                                
                            </center>
                        </h:form>
                    </a4j:region>
                </rich:tab>   
                <rich:tab id="sub71" label="Abonos em Massa" style="text-align:center;float:center" rendered="#{usuarioBean.perfil.abonosEmMassa== true}">
                    <a4j:support event="ontabenter" action="#{solicitacaoAbonoBean.setAba()}">
                        <a4j:actionparam name="tab" value="sub71"/>
                    </a4j:support>
                    <h:form id="massaForm">
                        <center>
                            <br/>
                            <h:panelGrid columnClasses="gridContent" style="text-align:center;float:center">
                                <center>
                                    <a4j:region id="abonoEmMassaRegion">
                                        <center>
                                            <h:panelGrid id="Entradas" columns="8" style="text-align:center;float:center">
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Funcionário: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Inicial: " styleClass="label"/>
                                                <rich:spacer width="12"/>
                                                <h:outputText value="Data Final:" styleClass="label"/>
                                                <rich:spacer width="12"/>

                                                <h:selectOneMenu id="departManuAb" value="#{abonoEmMassaBean.departamento}">
                                                    <f:selectItems value="#{abonoEmMassaBean.departamentolist}"/>
                                                    <a4j:support event="onchange" action="#{abonoEmMassaBean.consultaFuncionario}"
                                                                 reRender="funcionario,abonoEmMassaGroup,butaoAbonoEmMassa,funcionarioAf"/>

                                                </h:selectOneMenu>
                                                <rich:spacer width="10"/>

                                                <h:selectOneMenu id="funcionarioAf" value="#{abonoEmMassaBean.cod_funcionario}">
                                                    <f:selectItems value="#{abonoEmMassaBean.funcionarioList}"/>
                                                    <a4j:support event="onchange"  action="#{abonoEmMassaBean.consultaAbonoEmMassa}"
                                                                 reRender="abonoEmMassaGroup,butaoAbonoEmMassa"/>
                                                </h:selectOneMenu>
                                                <rich:spacer  width="10"/>

                                                <rich:calendar inputSize="8" locale="#{abonoEmMassaBean.objLocale}" value="#{abonoEmMassaBean.dataInicio}" >
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{abonoEmMassaBean.consultaAbonoEmMassa}"
                                                                 reRender="abonoEmMassaGroup,butaoAbonoEmMassa"/>
                                                </rich:calendar>
                                                <rich:spacer width="10"/>

                                                <rich:calendar inputSize="8" locale="#{abonoEmMassaBean.objLocale}" value="#{abonoEmMassaBean.dataFim}">
                                                    <a4j:support event="onchanged" ajaxSingle="true"  action="#{abonoEmMassaBean.consultaAbonoEmMassa}"
                                                                 reRender="abonoEmMassaGroup,butaoAbonoEmMassa"/>
                                                </rich:calendar>
                                                <rich:spacer width="15"/>

                                                <h:panelGroup>
                                                    <h:selectBooleanCheckbox value="#{abonoEmMassaBean.incluirSubSetores}">
                                                        <a4j:support event="onchange" action="#{abonoEmMassaBean.consultaFuncionario}"
                                                                     reRender="funcionarioAf,abonoEmMassaGroup,butaoAbonoEmMassa"/>
                                                    </h:selectBooleanCheckbox>
                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                            <a4j:status id="progressoEmAberto"  for="abonoEmMassaRegion"
                                                        onstart="Richfaces.showModalPanel('panelAfastamentStatus');"
                                                        onstop="#{rich:component('panelAfastamentStatus')}.hide()"/>
                                            <rich:modalPanel id="panelAfastamentStatus" autosized="true" >
                                                <h:panelGrid columns="3">
                                                    <h:graphicImage url="../images/load.gif" />
                                                    <rich:spacer width="8"/>
                                                    <h:outputText value="  Carregando…" styleClass="label" />
                                                </h:panelGrid>
                                            </rich:modalPanel>
                                        </center>
                                        <h:panelGroup id="butaoAbonoEmMassa" style="text-align:center;float:center" >
                                            <center>
                                                <h:panelGrid columns="3" style="text-align:center;float:center"
                                                             rendered="#{abonoEmMassaBean.cod_funcionario != -1}">
                                                    <h:panelGrid>
                                                        <center>
                                                            <h:outputLink  value="#" id="linkAdiconarAbonoEmMassa">
                                                                <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                <rich:componentControl for="panelAdicionarAbonoEmMassa" attachTo="linkAdiconarAbonoEmMassa"
                                                                                       operation="show" event="onclick"/>
                                                                <a4j:support event="onclick" reRender="gridNovoAbonoEmMassaModalPanel"
                                                                             action="#{abonoEmMassaBean.showNovoAbonoEmMassa}" />
                                                            </h:outputLink>
                                                            Adicionar
                                                        </center>
                                                    </h:panelGrid>
                                                    <rich:spacer width="25"/>
                                                    <h:panelGrid  rendered="#{not empty abonoEmMassaBean.abonoEmMassaList}">
                                                        <center>
                                                            <h:outputLink  value="#" id="linkExcluirAbonoEmMassa">
                                                                <h:graphicImage  value="../images/fechar.gif" style="border:0"/>
                                                                <a4j:support event="onclick" reRender="abonoEmMassaGroup"
                                                                             action="#{abonoEmMassaBean.excluirTodosAbonoEmMassa}"
                                                                             onsubmit="javascript:if (!confirm('Deseja excluir todos esses abonos em massa?')) return false;"/>
                                                            </h:outputLink>
                                                            Excluir Todos
                                                        </center>
                                                    </h:panelGrid>
                                                </center>
                                            </h:panelGrid>
                                        </h:panelGroup>
                                    </a4j:region>

                                    <h:panelGroup id="abonoEmMassaGroup" style="text-align:center;float:center">
                                        <center>
                                            <rich:dataTable id="abonoEmMassaList"
                                                            value="#{abonoEmMassaBean.abonoEmMassaList}" var="abonoEmMassa"
                                                            rowClasses="zebra1,zebra2"
                                                            rows="25"
                                                            rendered="#{not empty abonoEmMassaBean.abonoEmMassaList}">
                                                <f:facet name="header">
                                                    <h:outputText value="LISTA DE FUNCIONÁRIOS"/>
                                                </f:facet>
                                                <rich:column sortBy="#{abonoEmMassa.nomeFuncionario}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.nomeFuncionario}" />
                                                </rich:column>
                                                <rich:column sortBy="#{abonoEmMassa.inicio}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="INÍCIO INTERVALO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.inicio}" >
                                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{abonoEmMassa.fim}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="FIM INTERVALO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.fim}">
                                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column sortBy="#{abonoEmMassa.categoria}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="CATEGORIA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.categoria}"/>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="DESCRIÇÃO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.descricaoCategoria}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{abonoEmMassa.dataAbonoEmMassa}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="DATA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{abonoEmMassa.dataAbonoEmMassa}">
                                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss"/>
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="ALTERAR"/>
                                                    </f:facet>
                                                    <center>
                                                        <h:outputLink  value="#" id="btAlterarAbonoEmMassa">
                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                            <rich:componentControl for="paneAlterarAbonoEmMassa" attachTo="btAlterarAbonoEmMassa" operation="show" event="onclick"/>
                                                            <a4j:support action="#{abonoEmMassaBean.showAlterarAbonoEmMassa}"
                                                                         event="onclick"
                                                                         reRender="gridAlterarAbonoEmMassaModalPanel">
                                                                <f:param name="posAbonoEmMassa" value="#{abonoEmMassa.posicao}"/>
                                                                <f:param name="cod_funcionario" value="#{abonoEmMassa.cod_funcionario}"/>
                                                                <f:param name="cod_justificativa" value="#{abonoEmMassa.cod_categoria}"/>
                                                            </a4j:support>
                                                        </h:outputLink>
                                                    </center>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="EXCLUIR"/>
                                                    </f:facet>
                                                    <a4j:commandButton id="btExcluirAbonoEmMassa"
                                                                       value="Deletar"
                                                                       image="../images/delete.png"
                                                                       reRender="abonoEmMassaGroup"
                                                                       ajaxSingle="true"
                                                                       action="#{abonoEmMassaBean.excluirAbonoEmMassa}"
                                                                       onclick="javascript:if (!confirm('Deseja excluir esse abono?')) return false;">
                                                        <f:param name="posAbonoEmMassa" value="#{abonoEmMassa.posicao}"/>
                                                    </a4j:commandButton>
                                                </rich:column>
                                            </rich:dataTable>
                                        </center>
                                        <rich:datascroller  id="datascrollerAbonoEmMassa"
                                                            for="abonoEmMassaList"
                                                            renderIfSinglePage="false">
                                        </rich:datascroller>

                                        <rich:modalPanel id="panelAdicionarAbonoEmMassa" width="400" height="270" autosized="true">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Abono em massa"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAbonoEmMassa"/>
                                                    <rich:componentControl for="panelAdicionarAbonoEmMassa" attachTo="hidelinkAbonoEmMassa" operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <h:panelGrid id="gridNovoAbonoEmMassaModalPanel" columns="1" style="text-align:center;float:center">
                                                <h:panelGroup>
                                                    <center>
                                                        <h:panelGrid  columns="8"  style="text-align:center;float:center">
                                                            <center>
                                                                <h:outputText value="Data Inicial " styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Hora " styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Data Final" styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Hora " styleClass="label"/>
                                                                <rich:spacer width="15"/>

                                                                <rich:calendar inputSize="8" locale="#{abonoEmMassaBean.objLocale}" value="#{abonoEmMassaBean.novoAbonoEmMassa.dataInicio}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText id="horaAbonoEmMassaInicio" size="2" value="#{abonoEmMassaBean.novoAbonoEmMassa.horaInicio}" maxlength="20"
                                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                                    <rich:jQuery selector="#horaAbonoEmMassaInicio" query="mask('99:99')" timing="onload"/>
                                                                </h:inputText>
                                                                <rich:spacer width="15"/>
                                                                <rich:calendar inputSize="8" locale="#{abonoEmMassaBean.objLocale}" value="#{abonoEmMassaBean.novoAbonoEmMassa.dataFim}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText id="horaAbonoEmMassaFim" size="2" value="#{abonoEmMassaBean.novoAbonoEmMassa.horaFim}" maxlength="20"
                                                                             onkeyup="mascara_hora(this.id)" style="float:left">
                                                                    <rich:jQuery selector="#horaAbonoEmMassaFim" query="mask('99:99')" timing="onload"/>
                                                                </h:inputText>
                                                            </center>
                                                        </h:panelGrid>

                                                        <h:panelGrid style="text-align:center;float:center">
                                                            <h:outputText value="Categoria do Abono" styleClass="label"/>
                                                            <h:selectOneMenu  value="#{abonoEmMassaBean.novoAbonoEmMassa.cod_justificativa}">
                                                                <f:selectItems value="#{abonoEmMassaBean.novoAbonoEmMassa.justificativasList}"/>
                                                            </h:selectOneMenu>
                                                        </h:panelGrid>

                                                        <h:panelGrid style="text-align:center;float:center">
                                                            <center>
                                                                <h:outputText value="Descrição do abono" styleClass="label"/>
                                                                <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                                  rows="4" cols="45" value="#{abonoEmMassaBean.novoAbonoEmMassa.descricaoJustificativa}"/>
                                                            </center>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="3">
                                                            <h:commandButton value="Confirmar" action="#{abonoEmMassaBean.addAbonoEmMassa}" id="confirmarNovoAbonoEmMassa" >
                                                                <rich:componentControl for="panelAdicionarAbonoEmMassa"
                                                                                       attachTo="confirmarNovoAbonoEmMassa" operation="hide" event="onclick"/>
                                                            </h:commandButton>
                                                            <rich:spacer width="15"/>
                                                            <h:commandButton value="Cancelar"/>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                        </rich:modalPanel>

                                        <rich:modalPanel id="paneAlterarAbonoEmMassa" width="400" height="270" autosized="true">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Alterar abono em massa"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAlterarAbonoEmMassa"/>
                                                    <rich:componentControl for="paneAlterarAbonoEmMassa" attachTo="hidelinkAlterarAbonoEmMassa" operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <h:panelGrid id="gridAlterarAbonoEmMassaModalPanel" columns="1" style="text-align:center;float:center">
                                                <h:panelGroup>
                                                    <center>
                                                        <h:panelGrid  columns="8"  style="text-align:center;float:center">
                                                            <center>
                                                                <h:outputText value="Data Inicial " styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Hora " styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Data Final" styleClass="label"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Hora " styleClass="label"/>
                                                                <rich:spacer width="15"/>

                                                                <rich:calendar inputSize="8" disabled="true" locale="#{abonoEmMassaBean.objLocale}" value="#{abonoEmMassaBean.alterarFastamento.dataInicio}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText id="horaAlterarAbonoEmMassaInicio" disabled="true" size="2"  value="#{abonoEmMassaBean.alterarFastamento.horaInicio}"
                                                                             maxlength="20"  style="float:left"/>

                                                                <rich:spacer width="15"/>
                                                                <rich:calendar inputSize="8" disabled="true" locale="#{abonoEmMassaBean.objLocale}"
                                                                               value="#{abonoEmMassaBean.alterarFastamento.dataFim}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText id="horaAlterarAbonoEmMassaFim" disabled="true" size="2" value="#{abonoEmMassaBean.alterarFastamento.horaFim}"
                                                                             maxlength="20" style="float:left"/>
                                                            </center>
                                                        </h:panelGrid>
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:panelGrid style="text-align:center;float:center">
                                                                    <h:outputText value="Categoria do Abono" styleClass="label"/>
                                                                    <h:selectOneMenu  value="#{abonoEmMassaBean.alterarFastamento.cod_justificativa}">
                                                                        <f:selectItems value="#{abonoEmMassaBean.alterarFastamento.justificativasList}"/>
                                                                    </h:selectOneMenu>
                                                                </h:panelGrid>

                                                                <h:panelGrid style="text-align:center;float:center">
                                                                    <center>
                                                                        <h:outputText value="Descrição do abono" styleClass="label"/>
                                                                        <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                                          rows="4" cols="45" value="#{abonoEmMassaBean.alterarFastamento.descricaoJustificativa}"/>
                                                                    </center>
                                                                </h:panelGrid>
                                                                <h:panelGrid columns="3">
                                                                    <h:commandButton value="Confirmar" action="#{abonoEmMassaBean.alterarAbonoEmMassa}"
                                                                                     id="confirmarAlterarAbonoEmMassa" >
                                                                        <rich:componentControl for="paneAlterarAbonoEmMassa" 
                                                                                               attachTo="confirmarAlterarAbonoEmMassa"
                                                                                               operation="hide" event="onclick"/>
                                                                    </h:commandButton>
                                                                    <rich:spacer width="15"/>
                                                                    <h:commandButton value="Cancelar"/>
                                                                </h:panelGrid>
                                                            </center>
                                                        </h:panelGroup>
                                                    </center>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                    </h:panelGroup>
                                </center>
                            </h:panelGrid>
                        </center>
                    </h:form>
                </rich:tab>

                <rich:tab id="tab3" label="Histórico" ignoreDupResponses="true" rendered="#{usuarioBean.perfil.historicoAbono== true}">
                    <a4j:support event="ontabenter" action="#{solicitacaoAbonoBean.setAba()}">
                        <a4j:actionparam name="tab" value="tab3"/>
                    </a4j:support>
                    <h:form id="historiaForm">
                        <center>
                            <br/>
                            <a4j:region id="region2">
                                <h:panelGrid columns="8">
                                    <h:outputText value="Departamento: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Responsável: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Inicial: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Final:" styleClass="label"/>
                                    <rich:spacer width="10"/>

                                    <h:selectOneMenu id="departManu" value="#{historicoAbonoBean.departamento}">
                                        <f:selectItems value="#{historicoAbonoBean.departamentolist}"/>
                                        <a4j:support event="onchange" action="#{historicoAbonoBean.consultaFuncionario}" reRender="funcionario,historicoList,historicoPorPeriodoList"/>
                                    </h:selectOneMenu>
                                    <rich:spacer width="10"/>

                                    <h:selectOneMenu id="funcionario" value="#{historicoAbonoBean.cod_funcionario}">
                                        <f:selectItems value="#{historicoAbonoBean.funcionarioList}"/>
                                        <a4j:support event="onchange" action="#{historicoAbonoBean.consultaHistorico}" reRender="historicoList,historicoPorPeriodoList,excluirBotao" />
                                    </h:selectOneMenu>
                                    <rich:spacer  width="10"/>

                                    <rich:calendar id="dataInicio" inputSize="8" locale="#{historicoAbonoBean.objLocale}" value="#{historicoAbonoBean.dataInicio}">
                                        <a4j:support event="onchanged" ajaxSingle="true" action="#{historicoAbonoBean.consultaHistorico}" reRender="historicoList,historicoPorPeriodoList,excluirBotao"/>
                                    </rich:calendar>
                                    <rich:spacer width="10"/>

                                    <rich:calendar  id="dataFim" inputSize="8" locale="#{historicoAbonoBean.objLocale}" value="#{historicoAbonoBean.dataFim}">
                                        <a4j:support event="onchanged" ajaxSingle="true" action="#{historicoAbonoBean.consultaHistorico}" reRender="historicoList,historicoPorPeriodoList,excluirBotao"/>
                                    </rich:calendar>
                                    <rich:spacer width="1"/>
                                    <h:panelGroup>
                                        <h:selectBooleanCheckbox id="incluirSubSetores" value="#{historicoAbonoBean.incluirSubSetores}">
                                            <a4j:support event="onchange" action="#{historicoAbonoBean.consultaFuncionario}"  reRender="funcionario"/>
                                        </h:selectBooleanCheckbox>
                                        <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                    </h:panelGroup>
                                </h:panelGrid>
                                <a4j:status id="progressoEmAberto"  for="region2" onstart="Richfaces.showModalPanel('panelStatus2');" onstop="#{rich:component('panelStatus2')}.hide()"/>
                                <rich:modalPanel id="panelStatus2" autosized="true" >
                                    <h:panelGrid columns="3">
                                        <h:graphicImage url="../images/load.gif" />
                                        <rich:spacer width="8"/>
                                        <h:outputText value="  Carregando…" styleClass="label" />
                                    </h:panelGrid>
                                </rich:modalPanel>
                            </a4j:region>
                            <br/>
                            <a4j:outputPanel id="excluirBotao" rendered="#{usuarioBean.perfil.exclusaoAbono==true}">
                                <a4j:commandButton  action="#{historicoAbonoBean.excluirAbonos}" value="Excluir"
                                                    rendered="#{not empty historicoAbonoBean.historicoAbonoList}" reRender="historicoList" />
                                <br/>
                            </a4j:outputPanel>
                            <br/>
                            <h:panelGroup id="historicoList" >
                                <rich:dataTable id="historico"
                                                value="#{historicoAbonoBean.historicoAbonoList}" var="linha"
                                                rows="25"  rowClasses="zebra1,zebra2"
                                                rendered="#{not empty historicoAbonoBean.historicoAbonoList and not historicoAbonoBean.isTodosFuncionarios}" >

                                    <f:facet name="header">
                                        <h:outputText value="HISTÓRICO DE ABONO"/>
                                    </f:facet>
                                    <rich:column sortBy="#{linha.diaAbono}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO EM"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.diaAbono}">
                                            <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                        </h:outputText>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.funcionario}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="FUNCIONÁRIO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.funcionario}"/>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.categoriaJustificativa}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="CATEGORIA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.categoriaJustificativa}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="SOLICITAÇÃO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.solicitacao}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="RESPOSTA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.justificativa}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.abono}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="PONTO ABONADO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.abono}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.status}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="STATUS"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.status}"/>
                                        <rich:spacer width="5"/>
                                        <a4j:commandButton rendered="#{linha.negado}"
                                                           id="btAlterarStatus"
                                                           value="AlterarStatus"
                                                           image="../images/desfazer_16x16.png"
                                                           action="#{historicoAbonoBean.alterarStatus}"
                                                           reRender="panelDiaEmAberto">
                                            <f:param name="posRegistro" value="#{linha.cod_solicitacao}"/>
                                        </a4j:commandButton>

                                    </rich:column>                                    
                                    <rich:column style="text-align:center" rendered="#{usuarioBean.perfil.exclusaoAbono==true}">
                                        <f:facet name="header">
                                            <h:outputText value="SELECIONE"/>
                                        </f:facet>
                                        <h:selectBooleanCheckbox  value="#{linha.marked}"/>
                                    </rich:column>
                                </rich:dataTable>
                                <rich:datascroller  id="datascrollers"
                                                    for="historico"
                                                    ajaxSingle="true"
                                                    renderIfSinglePage="false"
                                                    rendered="#{not historicoAbonoBean.isTodosFuncionarios}"
                                                    page="1"
                                                    >
                                </rich:datascroller>

                                <rich:extendedDataTable id="historicoT"
                                                        value="#{historicoAbonoBean.historicoAbonoList}" var="linha"
                                                        rows="25" rowClasses="zebra1,zebra2" enableContextMenu="false"
                                                        rendered="#{not empty historicoAbonoBean.historicoAbonoList and  historicoAbonoBean.isTodosFuncionarios}" >
                                    <f:facet name="header">
                                        <h:outputText value="HISTÓRICO DE ABONO"/>
                                    </f:facet>
                                    <rich:column sortBy="#{linha.diaAbono}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO EM"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.diaAbono}">
                                            <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                        </h:outputText>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.funcionario}" filterBy="#{linha.funcionario}" filterEvent="onkeyup" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="FUNCIONÁRIO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.funcionario}"/>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.categoriaJustificativa}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="CATEGORIA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.categoriaJustificativa}" />
                                    </rich:column>
                                    <rich:column style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="SOLICITAÇÃO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.solicitacao}" />
                                    </rich:column>
                                    <rich:column style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="RESPOSTA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.justificativa}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.abono}" filterBy="#{linha.abono}" filterEvent="onkeyup" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="PONTO ABONADO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.abono}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.responsavel}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO POR"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.responsavel}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.status}" filterBy="#{linha.status}" filterEvent="onkeyup" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="STATUS"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.status}" />
                                        <a4j:commandButton rendered="#{linha.negado}" id="btAlterarStatus" image="../images/desfazer_16x16.png"
                                                           action="#{historicoAbonoBean.alterarStatus}"
                                                           reRender="panelDiaEmAberto,historicoList,historico,historicoT">
                                            <f:param name="posRegistro" value="#{linha.cod_solicitacao}"/>
                                        </a4j:commandButton>
                                    </rich:column>
                                    <rich:column style="text-align:center" rendered="#{usuarioBean.perfil.exclusaoAbono==true}">
                                        <f:facet name="header">
                                            <h:outputText value="SELECIONE"  />
                                        </f:facet>
                                        <h:selectBooleanCheckbox value="#{linha.marked}"/>
                                    </rich:column>--%>
                                </rich:extendedDataTable>
                                <rich:datascroller  id="datascrollersT"
                                                    for="historicoT"
                                                    ajaxSingle="true"
                                                    renderIfSinglePage="false"
                                                    rendered="#{historicoAbonoBean.isTodosFuncionarios}"
                                                    page="1"
                                                    >
                                </rich:datascroller>
                            </h:panelGroup>
                            <br/>
                            <br/>
                            <h:panelGroup id="historicoPorPeriodoList" >
                                <rich:dataTable id="historicoPorPeriodo"
                                                rowClasses="zebra1,zebra2"
                                                value="#{historicoAbonoBean.historicoAbonoPorPeriodoList}" var="linha"
                                                rows="25"
                                                rendered="#{not empty historicoAbonoBean.historicoAbonoPorPeriodoList and not historicoAbonoBean.isTodosFuncionarios}" >
                                    <f:facet name="header">
                                        <h:outputText value="HISTÓRICO DE ABONO POR PERÍODO"/>
                                    </f:facet>
                                    <rich:column sortBy="#{linha.diaAbono}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO EM"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.diaAbono}">
                                            <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                        </h:outputText>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.funcionario}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="FUNCIONÁRIO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.funcionario}"/>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.categoriaJustificativa}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="CATEGORIA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.categoriaJustificativa}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="SOLICITAÇÃO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.solicitacao}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="RESPOSTA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.justificativa}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="PERÍODO ABONADO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.abono}" />
                                    </rich:column>
                                </rich:dataTable>
                                <rich:datascroller  id="datascrollersPP"
                                                    for="historicoPorPeriodo"
                                                    ajaxSingle="true"
                                                    renderIfSinglePage="false"
                                                    page="1">
                                </rich:datascroller>

                                <rich:dataTable id="historicoTPorPeriodo"
                                                rowClasses="zebra1,zebra2"
                                                value="#{historicoAbonoBean.historicoAbonoPorPeriodoList}" var="linha"
                                                rows="25"
                                                rendered="#{not empty historicoAbonoBean.historicoAbonoPorPeriodoList and  historicoAbonoBean.isTodosFuncionarios}" >
                                    <f:facet name="header">
                                        <h:outputText value="HISTÓRICO DE ABONO POR PERÍODO"/>
                                    </f:facet>
                                    <rich:column sortBy="#{linha.diaAbono}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO EM"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.diaAbono}">
                                            <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                        </h:outputText>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.funcionario}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="FUNCIONÁRIO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.funcionario}"/>
                                    </rich:column>
                                    <rich:column sortBy="#{linha.categoriaJustificativa}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="CATEGORIA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.categoriaJustificativa}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="SOLICITAÇÃO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.solicitacao}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="RESPOSTA"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.justificativa}" />
                                    </rich:column>
                                    <rich:column  style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="PERÍODO ABONADO"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.abono}" />
                                    </rich:column>
                                    <rich:column sortBy="#{linha.responsavel}" style="text-align:center">
                                        <f:facet name="header">
                                            <h:outputText value="ABONADO POR"/>
                                        </f:facet>
                                        <h:outputText value="#{linha.responsavel}" />
                                    </rich:column>
                                </rich:dataTable>
                                <rich:datascroller  id="datascrollersTPorPeriodo"
                                                    for="historicoTPorPeriodo"
                                                    ajaxSingle="true"
                                                    renderIfSinglePage="false"
                                                    page="1">
                                </rich:datascroller>
                            </h:panelGroup>
                            <br/>
                            <br/>
                        </center>
                    </h:form>
                </rich:tab>
                <rich:tab id="tabRelatorios" label="Ranking" rendered="#{usuarioBean.isAtivo && usuarioBean.perfil.rankingAbono==true}">
                    <a4j:support event="ontabenter" action="#{solicitacaoAbonoBean.setAba()}">
                        <a4j:actionparam name="tab" value="tabRelatorios"/>
                    </a4j:support>
                    <h:form id="rankForm">
                        <center>
                            <br/>
                            <a4j:region id="relatoriosRegion">
                                <h:panelGrid columns="8">
                                    <h:outputText value="Departamento: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Inicial: " styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Data Final:" styleClass="label"/>
                                    <rich:spacer width="10"/>
                                    <h:outputText value="Critério:" styleClass="label"/>
                                    <rich:spacer width="10"/>

                                    <h:selectOneMenu id="departManuR" value="#{topAbonoBean.departamento}">
                                        <f:selectItems value="#{topAbonoBean.departamentolist}"/>
                                        <a4j:support event="onchange" action="#{topAbonoBean.consultaRelatoriosList}" reRender="relatoriosTableGroup"/>
                                    </h:selectOneMenu>
                                    <rich:spacer width="10"/>



                                    <rich:calendar inputSize="8" locale="#{topAbonoBean.objLocale}" value="#{topAbonoBean.dataInicio}">
                                        <a4j:support event="onchanged" ajaxSingle="true" action="#{topAbonoBean.consultaRelatoriosList}" reRender="relatoriosTableGroup"/>
                                    </rich:calendar>
                                    <rich:spacer width="10"/>

                                    <rich:calendar inputSize="8" locale="#{topAbonoBean.objLocale}" value="#{topAbonoBean.dataFim}">
                                        <a4j:support event="onchanged" ajaxSingle="true" action="#{topAbonoBean.consultaRelatoriosList}" reRender="relatoriosTableGroup"/>
                                    </rich:calendar>
                                    <rich:spacer width="1"/>
                                    <h:selectOneMenu id="criterioRelatorio" value="#{topAbonoBean.criterio}">
                                        <f:selectItems value="#{topAbonoBean.criteriosList}"/>
                                        <a4j:support event="onchange" action="#{topAbonoBean.consultaRelatoriosList}" reRender="relatoriosTableGroup"/>
                                    </h:selectOneMenu>
                                    <rich:spacer width="10"/>

                                    <h:panelGroup>
                                        <h:selectBooleanCheckbox value="#{topAbonoBean.incluirSubSetores}">
                                            <a4j:support event="onchange" action="#{topAbonoBean.consultaRelatoriosList}"  reRender="relatoriosTableGroup"/>
                                        </h:selectBooleanCheckbox>
                                        <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                    </h:panelGroup>
                                </h:panelGrid>
                                <a4j:status for="relatoriosRegion" onstart="Richfaces.showModalPanel('panelStatusrelatorio');" onstop="#{rich:component('panelStatusrelatorio')}.hide()"/>
                                <rich:modalPanel id="panelStatusrelatorio" autosized="true" >
                                    <h:panelGrid columns="3">
                                        <h:graphicImage url="../images/load.gif" />
                                        <rich:spacer width="8"/>
                                        <h:outputText value="  Carregando…" styleClass="label" />
                                    </h:panelGrid>
                                </rich:modalPanel>

                                <br>
                                <h:panelGroup id = "relatoriosTableGroup">
                                    <rich:extendedDataTable enableContextMenu="false" noDataLabel="Sem abonos realizados no período" id="relatorioTablePorFunc" value="#{topAbonoBean.relatoriosList}"
                                                            var="relatorioAbono"
                                                            rendered="#{topAbonoBean.criterio==1&&(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}" width="90%" >
                                        <rich:column sortBy="#{relatorioAbono.funcionario}" id="colRelatAbonoNomeFunc" width="30%">
                                            <f:facet name="header">
                                                <h:outputText value="Nome do Funcionario" />
                                            </f:facet>
                                            <h:outputText value="#{relatorioAbono.funcionario}" />
                                        </rich:column>
                                        <rich:column sortable="false" id="colRelatAbonoDetalhesFunc" width="50%">
                                            <f:facet name="header">
                                                <h:outputText value="Abonos por categoria" />
                                            </f:facet>
                                            <rich:dataList var="qntPorCategoria" value="#{relatorioAbono.qntPorCategoriaList}" rows="0">
                                                <h:outputText value="#{qntPorCategoria.funcionario}: " styleClass="label"/>
                                                <h:outputText value="#{qntPorCategoria.detalhes} " />
                                            </rich:dataList>
                                        </rich:column>
                                        <rich:column sortBy="#{relatorioAbono.totalAbonos}" id="colRelatAbonoTotalFunc" width="20%">
                                            <f:facet name="header">
                                                <h:outputText value="Total de Abonos" />
                                            </f:facet>
                                            <h:outputText value="#{relatorioAbono.totalAbonos}" />
                                        </rich:column>
                                    </rich:extendedDataTable>

                                    <%--<rich:inputNumberSlider value="#{topAbonoBean.quantidadeLinhas}" showInput="true" maxValue="20" minValue="0"
                                                            enableManualInput="false" showBoundaryValues="true" inputPosition="bottom" label="Número de Funcionarios"
                                                            showToolTip="false" rendered="#{topAbonoBean.criterio==2&&(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}"/>
                                    --%>
                                    <h:outputText value="Número de funcionários" rendered="#{topAbonoBean.criterio==2&&(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}"/>
                                    <rich:inputNumberSpinner value="#{topAbonoBean.quantidadeLinhas}" inputSize="3" enableManualInput="true" label="Número de funcionários"  rendered="#{topAbonoBean.criterio==2&&(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}">
                                        <a4j:support event="onchange" action="#{topAbonoBean.consultaRelatoriosList}"
                                                     reRender="relatoriosTableGroup"/>
                                    </rich:inputNumberSpinner>
                                    <br>
                                    <rich:extendedDataTable enableContextMenu="false" noDataLabel="Sem abonos realizados no período" id="relatorioTablePorRespons"
                                                            value="#{topAbonoBean.relatoriosList}" var="relatorioAbono"
                                                            rendered="#{topAbonoBean.criterio==2&&(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}" width="90%" >

                                        <rich:column sortBy="#{relatorioAbono.funcionario}" id="colRelatAbonoNomeRespo"  width="30%">
                                            <f:facet name="header">
                                                <h:outputText value="Nome do Responsavel" />
                                            </f:facet>
                                            <h:outputText value="#{relatorioAbono.funcionario}" />
                                        </rich:column>
                                        <rich:column sortable="false" id="colRelatAbonoDetalhesRespons" width="50%">
                                            <f:facet name="header">
                                                <h:outputText value="Abonos por funcionário" />
                                            </f:facet>
                                            <rich:dataList var="qntPorCategoria" value="#{relatorioAbono.qntPorCategoriaList}" rows="#{topAbonoBean.quantidadeLinhas}">
                                                <h:outputText value="#{qntPorCategoria.funcionario}: " styleClass="label"/>
                                                <h:outputText value="#{qntPorCategoria.detalhes} " />
                                            </rich:dataList>
                                        </rich:column>
                                        <rich:column sortBy="#{relatorioAbono.totalAbonos}" sortable="false" id="colRelatAbonoTotalRespon" width="20%">
                                            <f:facet name="header">
                                                <h:outputText value="Total de Abonos" />
                                            </f:facet>
                                            <h:outputText value="#{relatorioAbono.totalAbonos}" />
                                        </rich:column>


                                    </rich:extendedDataTable>
                                    <br>
                                    <br>
                                    <h:commandButton  rendered="#{(topAbonoBean.departamento!= -1&&topAbonoBean.departamento!= null)}" value="Gerar Excel" action="#{topAbonoBean.gerar}"/>
                                </h:panelGroup>
                                <br>
                            </a4j:region>
                        </center>
                    </h:form>
                </rich:tab>
            </rich:tabPanel>
            <br/>
            <%-- <h:panelGroup>
                 <center>
                     <h:commandLink action="#{solicitacaoAbonoBean.voltar}">
                         <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                     </h:commandLink>
                 </h:panelGroup>
             </center>--%>

            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>