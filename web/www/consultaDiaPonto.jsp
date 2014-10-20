<%--
    Document   : consultaDiaPonto
    Created on : 21/12/2009, 17:42:03
    Author     : amsgama
--%>
<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>


<%@ page import="ConsultaPonto.ConsultaFrequenciaComEscalaBean" %>
<%
            ConsultaFrequenciaComEscalaBean consultaFrequenciaComEscalaBean = (ConsultaFrequenciaComEscalaBean) request.getSession().getAttribute("consultaFrequenciaComEscalaBean");
            if (consultaFrequenciaComEscalaBean == null) {
                response.sendRedirect("login.jsp");
            }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<style>
    .cabecalho1{
        border-right: solid 1px #C0C0C0;
        border-bottom: solid 1px #C0C0C0;
        padding: 4px 4px 4px 4px;
        color: #000;
        text-align: center;
        font-weight: bold;
        font-size: 11px;
        font-family: Arial,Verdana,sans-serif;
        background-color: #BED6F8;
        background-image: url(/ponto/faces/a4j/g/3_3_2.SR1org.richfaces.renderkit.html.GradientA/DATB/eAH7!!3Tj2v7mAAZZAV3);
        background-position: top left;
        background-repeat: repeat-x;
    }
    .cabecalho2{
        border-right: solid 1px #C0C0C0;
        border-bottom: solid 1px #C0C0C0;
        padding: 4px 4px 4px 4px;
        color: #000;
        text-align: center;
        font-weight: bold;
        font-size: 11px;
        font-family: Arial,Verdana,sans-serif;
        background-color: #BED6F8;
        background-position: top left;
        background-repeat: repeat-x;
    }
    .cabecalho3{
        border-right: solid 1px #C0C0C0;
        border-bottom: solid 1px #C0C0C0;
        padding: 4px 4px 4px 4px;
        color: #000;
        text-align: center;
        font-weight: bold;
        font-size: 11px;
        font-family: Arial,Verdana,sans-serif;
        background-color: #ECF4FE;
    } 
</style>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <title>Consulta Dia</title>
        <script type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
        </script>
    </head>
    <body>
        <f:view>
            <jsp:include page="_top.jsp"/>
            <center>
                <h:form prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:form id="form">
                    <h:panelGrid columns="1" columnClasses="gridContent">
                        <rich:tabPanel switchType="client" width="965">
                            <rich:tab label="Detalhe do dia">
                                <h:panelGroup>
                                    <br/>
                                    <center>
                                        <rich:dataTable id="tableFuncionario"
                                                        cellpadding="0" cellspacing="0"  width="600px"
                                                        border="0" var="tabelaFuncionario" value="#{detalheDiaBean.tabelaFuncionario}">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="2">
                                                        <h:outputText value="FUNCIONÁRIO"/>
                                                    </rich:column>
                                                    <rich:column breakBefore="true">
                                                        <h:outputText value="NOME" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="DEPARTAMENTO" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{tabelaFuncionario.nome}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{tabelaFuncionario.departamento}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                        </rich:dataTable>
                                    </center>
                                </h:panelGroup>
                                <%--<br/>
                                <h:panelGroup>
                                    <center>
                                        <rich:dataTable id="tableFaixas"
                                                        cellpadding="0" cellspacing="0"
                                                        width="600px" border="0" var="diaTurno" value="#{detalheDiaBean.tabelaHorarioList}">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="5">
                                                        <h:outputText value="FAIXAS - #{detalheDiaBean.pontoAcessoDataGridBean.dataSelecionada}" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:column>
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="TURNO 1 - ENTRADA"/>
                                                    </f:facet> 
                                                    <h:outputText value="#{diaTurno.entrada1}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaEntrada1}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="TURNO 1 - SAÍDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{diaTurno.saida1}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaSaida1}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="TURNO 2 - ENTRADA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{diaTurno.entrada2}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaEntrada2}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="TURNO 2 - SAÍDA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{diaTurno.saida2}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaSaida2}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                        </rich:dataTable>
                                    </center>
                                </h:panelGroup>--%>
                                <br/>
                                <h:panelGroup>
                                    <center>
                                        <rich:dataTable id="tableFaixas1"
                                                        cellpadding="0" cellspacing="0"
                                                        width="600px" border="0" var="diaTurno" value="#{detalheDiaBean.tabelaHorarioList}">
                                            <f:facet name="header">
                                                <rich:columnGroup>

                                                    <rich:column colspan="5">
                                                        <h:outputText value="FAIXAS - #{detalheDiaBean.pontoAcessoDataGridBean.dataSelecionada}" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:columnGroup>
                                                <rich:columnGroup>
                                                    <rich:column colspan="5" styleClass="cabecalho2">
                                                        <center>
                                                            <h:outputText value="TURNO - 1"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="0" breakBefore="true" styleClass="cabecalho3">
                                                        <center>
                                                            <h:outputText value="ENTRADA"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="0" styleClass="cabecalho3" rendered="#{diaTurno.faixa1Desc1Entrada != null}">
                                                        <center>
                                                            <h:outputText value="DESCANSO"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="0" styleClass="cabecalho3" rendered="#{diaTurno.interJornada1Entrada != null}">
                                                        <center>
                                                            <h:outputText value="INTERJORNADA"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="0" styleClass="cabecalho3" rendered="#{diaTurno.faixa1Desc2Entrada != null}">
                                                        <center>
                                                            <h:outputText value="DESCANSO"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="0" styleClass="cabecalho3">
                                                        <center>
                                                            <h:outputText value="SAIDA"/>
                                                        </center>
                                                    </rich:column>
                                                </rich:columnGroup>
                                                <rich:column colspan="0">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.entrada1}"></h:outputText>
                                                            <br/>
                                                        <h:outputText value="#{diaTurno.faixaEntrada1}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="0" rendered="#{diaTurno.faixa1Desc1Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa1Descanso1}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="0" rendered="#{diaTurno.interJornada1Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa1Interjornada}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="0" rendered="#{diaTurno.faixa1Desc2Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa1Descanso2}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="0">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.saida1}"></h:outputText>
                                                            <br/>
                                                        <h:outputText value="#{diaTurno.faixaSaida1}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                
                                                <rich:columnGroup rendered="#{diaTurno.entrada2 != null}">
                                                    <rich:column colspan="5" styleClass="cabecalho2">
                                                        <center>
                                                            <h:outputText value="TURNO - 2"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column breakBefore="true" colspan="1" styleClass="cabecalho3">
                                                        <center>
                                                            <h:outputText value="ENTRADA"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="1" styleClass="cabecalho3" rendered="#{diaTurno.faixa2Desc1Entrada != null}">
                                                        <center>
                                                            <h:outputText value="DESCANSO"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="1" styleClass="cabecalho3" rendered="#{diaTurno.interJornada2Entrada != null}">
                                                        <center>
                                                            <h:outputText value="INTERJORNADA"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="1" styleClass="cabecalho3" rendered="#{diaTurno.faixa2Desc2Entrada != null}">
                                                        <center>
                                                            <h:outputText value="DESCANSO"/>
                                                        </center>
                                                    </rich:column>
                                                    <rich:column colspan="1" styleClass="cabecalho3">
                                                        <center>
                                                            <h:outputText value="SAIDA"/>
                                                        </center>
                                                    </rich:column>
                                                </rich:columnGroup>
                                                <rich:column colspan="1" rendered="#{diaTurno.entrada2 != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.entrada2}"></h:outputText>
                                                            <br/>
                                                        <h:outputText value="#{diaTurno.faixaEntrada2}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="1" rendered="#{diaTurno.entrada2 != null && diaTurno.faixa2Desc1Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa2Descanso1}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="1" rendered="#{diaTurno.entrada2 != null && diaTurno.interJornada2Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa2Interjornada}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="1" rendered="#{diaTurno.entrada2 != null && diaTurno.faixa2Desc2Entrada != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.faixa2Descanso2}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                                <rich:column colspan="1" rendered="#{diaTurno.entrada2 != null}">
                                                    <center>
                                                        <h:outputText value="#{diaTurno.saida2}"></h:outputText>
                                                            <br/>
                                                        <h:outputText value="#{diaTurno.faixaSaida2}"></h:outputText>
                                                        </center>
                                                </rich:column>
                                            </rich:columnGroup>
                                        </rich:dataTable>
                                        <%--
                                    <rich:column colspan="1">
                                        <center>
                                            <f:facet name="header">  
                                                <h:outputText value="ENTRADA"/>
                                            </f:facet><h:outputText value="#{diaTurno.entrada1}"></h:outputText>
                                                <br/>
                                            <h:outputText value="#{diaTurno.faixaEntrada1}"></h:outputText>
                                            </center>
                                    </rich:column>
                                    <rich:column colspan="1">
                                        <center>
                                            <f:facet name="header">  
                                                <h:outputText value="SAIDA"/>
                                            </f:facet><h:outputText value="#{diaTurno.saida1}"></h:outputText>
                                                <br/>
                                            <h:outputText value="#{diaTurno.faixaSaida1}"></h:outputText>
                                            </center>
                                    </rich:column>
                                

                                        <rich:dataTable id="tableFaixas2"
                                                        cellpadding="0" cellspacing="0"
                                                        width="600px" border="0" var="diaTurno" value="#{detalheDiaBean.tabelaHorarioList}">

                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="5">
                                                        <h:outputText value="TURNO - 2"/>
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:column colspan="1">
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="ENTRADA"/>
                                                    </f:facet><h:outputText value="#{diaTurno.entrada2}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaEntrada2}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column colspan="1">
                                                <center>
                                                    <f:facet name="header">  
                                                        <h:outputText value="SAIDA"/>
                                                    </f:facet><h:outputText value="#{diaTurno.saida2}"></h:outputText>
                                                        <br/>
                                                    <h:outputText value="#{diaTurno.faixaSaida2}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                        </rich:dataTable>--%>
                                    </center>
                                </h:panelGroup>
                                <br/>
                                <h:panelGroup rendered="#{!detalheDiaBean.usuarioBean.ehAdministrador || detalheDiaBean.podeAbonar}">
                                    <center>
                                        <rich:dataTable id="tableSituacao"
                                                        cellpadding="0" cellspacing="0"
                                                        rowClasses="zebra1,zebra2"
                                                        width="600px" border="0" var="situacao" value="#{detalheDiaBean.situacaoList}"
                                                        rendered="#{not empty detalheDiaBean.situacaoList}">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="3">
                                                        <h:outputText value="REGISTROS DE PONTO" />
                                                    </rich:column>
                                                    <rich:column breakBefore="true">
                                                        <h:outputText value="HORÁRIO" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="SITUAÇÃO" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{situacao.horaPonto}"></h:outputText>
                                                        <br>
                                                    <h:outputText value="#{situacao.justificativaAbono}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{situacao.situacao}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                        </rich:dataTable>
                                    </center>
                                </h:panelGroup>
                                <h:panelGroup rendered="#{detalheDiaBean.usuarioBean.ehAdministrador && !detalheDiaBean.podeAbonar}">
                                    <center>
                                        <rich:dataTable id="tableSituacaoAdmin"
                                                        cellpadding="0" cellspacing="0"
                                                        rowClasses="zebra1,zebra2"
                                                        width="600px" border="0" var="situacao" value="#{detalheDiaBean.situacaoList}"
                                                        rendered="#{not empty detalheDiaBean.situacaoList}">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="5">
                                                        <h:outputText value="REGISTROS DE PONTO" />
                                                    </rich:column>
                                                    <rich:column breakBefore="true">
                                                        <h:outputText value="HORÁRIO" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="LOCAL" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="SITUAÇÃO" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="LIBERAÇÃO" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{situacao.horaPonto}"></h:outputText>
                                                        <br>
                                                    <h:outputText value="#{situacao.justificativaAbono}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{situacao.local}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <h:outputText value="#{situacao.situacao}"></h:outputText>
                                                    </center>
                                            </rich:column>
                                            <rich:column>
                                                <center>
                                                    <h:selectOneMenu value="#{situacao.horaPontoObj.tipoRegistro}">
                                                        <f:selectItems value="#{detalheDiaBean.tipoLiberacaoList}"/>
                                                        <a4j:support event="onchange" actionListener="#{situacao.liberarHorario}"
                                                                     action="#{detalheDiaBean.construtor}" reRender="tableSituacaoAdmin">
                                                            <f:param  name="cod_funcionario" value="#{detalheDiaBean.pontoAcessoDataGridBean.cod_funcionario}"/>
                                                            <f:param  name="nome" value="#{detalheDiaBean.pontoAcessoDataGridBean.nome}"/>
                                                            <f:param  name="tipoRegistro" value="#{situacao.horaPontoObj.tipoRegistro}"/>
                                                        </a4j:support>
                                                    </h:selectOneMenu>
                                                </center>
                                            </rich:column>
                                        </rich:dataTable>
                                    </center>
                                </h:panelGroup>
                                <br/>

                                <rich:modalPanel id="solicitaAbonoModal" autosized="true" style="text-align:center;float:center;" >
                                    <f:facet name="header">
                                        <h:panelGroup>
                                            <h:outputText value="#{detalheDiaBean.pontoAcessoDataGridBean.dataSelecionada}"></h:outputText>
                                        </h:panelGroup>
                                    </f:facet>
                                    <f:facet name="controls">
                                        <h:panelGroup>
                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                            <rich:componentControl for="solicitaAbonoModal" attachTo="hidelink" operation="hide" event="onclick"/>
                                        </h:panelGroup>
                                    </f:facet>
                                    <h:panelGroup>
                                        <center>
                                            <h:outputText value="Escolha o(os) ponto(os) que deseja solicitar o abono: "/>
                                            <h:selectManyCheckbox id ="manyMenuu" value="#{detalheDiaBean.horaSelecionadasAbonoList}" layout="pageDirection">
                                                <f:selectItems value="#{detalheDiaBean.horaAAbonarCheckBoxList}" />
                                            </h:selectManyCheckbox>
                                        </center>
                                        <br/>
                                        <center>
                                            <h:outputText value="Digite uma justificativa para a solicitação do abono: "/>
                                            <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                              rows="4" cols="45" value="#{detalheDiaBean.descricaoInputText}"/>
                                        </center>
                                        <br/>
                                        <br/>
                                        <h:commandButton value="Enviar" id="enviar" action="#{detalheDiaBean.solicitarAbono}">
                                            <rich:componentControl for="solicitaAbonoModal" attachTo="enviar" operation="hide" event="onclick"/>
                                        </h:commandButton>
                                    </h:panelGroup>
                                </rich:modalPanel>
                                <h:outputLink id="link" value="#" rendered="#{detalheDiaBean.podeAbonar}">
                                    <center>
                                        <h:graphicImage  value="../images/solicitar.png" style="border:0"/>
                                        <br/>
                                        Solicitar Abono
                                    </center>
                                    <rich:componentControl for="solicitaAbonoModal" attachTo="link" operation="show" event="onclick"/>
                                </h:outputLink>
                                <br/>
                                <a4j:outputPanel id="panelSolicitacao">
                                    <center>
                                        <rich:dataTable id="tableSolicitacao"
                                                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                        cellpadding="0" cellspacing="0"  width="600px"
                                                        border="0" var="solicitacaoAbono" value="#{detalheDiaBean.solicitacaoAbono}" rendered="#{not empty detalheDiaBean.solicitacaoAbono}">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <rich:column colspan="6">
                                                        <h:outputText value="SOLICITAÇÕES DE ABONO"/>
                                                    </rich:column>
                                                    <rich:column breakBefore="true">
                                                        <h:outputText value="SOLICITADO EM" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="PONTOS SOLICITADOS" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="DESCRIÇÃO" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="STATUS" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="RESPOSTA" />
                                                    </rich:column>
                                                    <rich:column>
                                                        <h:outputText value="EXCLUIR" />
                                                    </rich:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{solicitacaoAbono.inclusao}">
                                                        <f:convertDateTime pattern="dd/MM/yyyy"/>
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{solicitacaoAbono.pontosSolicitados}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{solicitacaoAbono.descricao}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{solicitacaoAbono.status}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{solicitacaoAbono.respostaJustificativa}"/>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:graphicImage  value="../images/negadoApagado.png" style="border:0" rendered="#{!solicitacaoAbono.isPendente or !detalheDiaBean.podeAbonar}"/>
                                                    <a4j:commandButton reRender="panelSolicitacao" image="../images/negado.png" id="deletarSolicitacao"
                                                                       action="#{detalheDiaBean.deletarSolicitacao}"  rendered="#{solicitacaoAbono.isPendente and detalheDiaBean.podeAbonar}"
                                                                       onclick="javascript:if (!confirm('Tem certeza que deseja deletar essa solicitação?')) return false;">
                                                        <f:param name="cod_solicitacao" value="#{solicitacaoAbono.cod}"/>
                                                    </a4j:commandButton>
                                                </center>
                                            </h:column>
                                        </rich:dataTable>
                                    </center>
                                </a4j:outputPanel>                                
                            </rich:tab>
                        </rich:tabPanel>
                        <br/>
                        <h:panelGroup>
                            <center>
                                <h:commandLink action="#{detalheDiaBean.navegar}">
                                    <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                </h:commandLink>
                            </center>
                        </h:panelGroup>
                    </h:panelGrid>                            
                </h:form>
                <jsp:include page="../www/_bot.jsp"/>
            </f:view>
        </center>
    </body>
</html>