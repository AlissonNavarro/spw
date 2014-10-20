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



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }
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
                    <a4j:keepAlive beanName="abonarDiaAbertoBean" />
                    <rich:tabPanel switchType="client"  width="965" >
                        <rich:tab id="tab1" label="Abonar" ignoreDupResponses="true">                           
                            <center>
                                <h:panelGrid columns="1">
                                    <h:panelGroup>
                                        <center>


                                            <h:panelGroup>
                                                <br/>
                                                <center>
                                                    <rich:dataTable id="tableFuncionario"
                                                                    cellpadding="0" cellspacing="0"  width="600px"
                                                                    border="0" var="tabelaFuncionario" value="#{abonarDiaAbertoBean.tabelaFuncionario}">
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
                                            <br/>

                                            <h:panelGroup>
                                                <center>
                                                    <rich:dataTable id="tableFaixas"
                                                                    cellpadding="0" cellspacing="0"
                                                                    width="600px" border="0" var="diaTurno" value="#{abonarDiaAbertoBean.tabelaHorarioList}">
                                                        <f:facet name="header">
                                                            <rich:columnGroup>
                                                                <rich:column colspan="4">
                                                                    <h:outputText value="FAIXAS - #{abonarDiaAbertoBean.dataStr}" />
                                                                </rich:column>
                                                                <rich:column breakBefore="true">
                                                                    <h:outputText value="TURNO 1 - ENTRADA"/>
                                                                </rich:column>
                                                                <rich:column>
                                                                    <h:outputText value="TURNO 1 - SAÍDA"/>
                                                                </rich:column>
                                                                <rich:column>
                                                                    <h:outputText value="TURNO 2 - ENTRADA"/>
                                                                </rich:column>
                                                                <rich:column>
                                                                    <h:outputText value="TURNO 2 - SAÍDA"/>
                                                                </rich:column>
                                                            </rich:columnGroup>
                                                        </f:facet>
                                                        <rich:column>
                                                            <center>
                                                                <h:outputText value="#{diaTurno.entrada1}"></h:outputText>
                                                                <br/>
                                                                <h:outputText value="#{diaTurno.faixaEntrada1}"></h:outputText>
                                                            </center>
                                                        </rich:column>
                                                        <rich:column>
                                                            <center>
                                                                <h:outputText value="#{diaTurno.saida1}"></h:outputText>
                                                                <br/>
                                                                <h:outputText value="#{diaTurno.faixaSaida1}"></h:outputText>
                                                            </center>
                                                        </rich:column>
                                                        <rich:column>
                                                            <center>
                                                                <h:outputText value="#{diaTurno.entrada2}"></h:outputText>
                                                                <br/>
                                                                <h:outputText value="#{diaTurno.faixaEntrada2}"></h:outputText>
                                                            </center>
                                                        </rich:column>
                                                        <rich:column>
                                                            <center>
                                                                <h:outputText value="#{diaTurno.saida2}"></h:outputText>
                                                                <br/>
                                                                <h:outputText value="#{diaTurno.faixaSaida2}"></h:outputText>
                                                            </center>
                                                        </rich:column>
                                                    </rich:dataTable>
                                                </center>

                                            </h:panelGroup>
                                            <br/>

                                            <h:panelGroup>
                                                <center>
                                                    <rich:dataTable id="tableSituacao"
                                                                    cellpadding="0" cellspacing="0"
                                                                    rowClasses="zebra1,zebra2"
                                                                    width="600px" border="0" var="situacao" value="#{abonarDiaAbertoBean.situacaoList}"
                                                                    rendered="#{not empty abonarDiaAbertoBean.situacaoList}">
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
                                            <br/>
                                            <a4j:outputPanel id="panelSolicitacao">
                                                <center>
                                                    <rich:dataTable id="tableSolicitacao"
                                                                    onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                    onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                    cellpadding="0" cellspacing="0"  width="600px"
                                                                    border="0" var="solicitacaoAbono" value="#{abonarDiaAbertoBean.solicitacaoAbono}"
                                                                    rendered="#{not empty abonarDiaAbertoBean.solicitacaoAbono}">
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
                                                    </rich:dataTable>
                                                </center>
                                            </a4j:outputPanel> 
                                            <br/>
                                            <br/>
                                            <h:outputLabel value="Categorize o abono" styleClass="label"/>
                                            <br/>
                                            <h:selectOneMenu value="#{abonarDiaAbertoBean.justificativa}">
                                                <f:selectItems value="#{abonarDiaAbertoBean.justificativaList}"/>
                                                <a4j:support event="onclick" reRender="info"/>
                                            </h:selectOneMenu>
                                            <br/>
                                            <br/>
                                            <h:outputLabel value="Selecione os pontos que deseja abonar" styleClass="label"/>
                                            <h:panelGrid columns="3">
                                                <h:selectManyCheckbox id ="manyMenuCheckBox" value="#{abonarDiaAbertoBean.horaAbonoList}" >
                                                    <f:selectItems value="#{abonarDiaAbertoBean.horaSelectAbonoList}"/>
                                                    <a4j:support event="onclick" reRender="info"/>
                                                </h:selectManyCheckbox >
                                                <%--
                                                <h:outputLink  value="#" id="link">
                                                    <center>
                                                        <h:graphicImage  value="../images/adicionar.png" style="border:0"/>
                                                    </center>
                                                    <rich:componentControl for="panel" attachTo="link" operation="show" event="onclick"/>
                                                </h:outputLink>
                                                <rich:modalPanel id="panel" width="330" height="100" style="text-align:center">
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
                                                        <h:inputText id="hora" size="2" value="#{abonarDiaAbertoBean.hora}">
                                                            <rich:jQuery selector="#hora" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                    </center>
                                                    <br/>
                                                     
                                                    
                                                    <h:commandButton value="Adicionar" id="adicionar" action="#{abonarDiaAbertoBean.addRegistro}">
                                                        <rich:componentControl for="panel" attachTo="adicionar" operation="hide" event="onclick"/>
                                                        <a4j:support event="onclick" reRender="info"/>
                                                    </h:commandButton>

                                                    
                                                </rich:modalPanel> ---%>
                                            </h:panelGrid>
                                            <h:panelGrid columns="1" style="text-align:center;">
                                                <h:outputLabel value="Digite uma justificativa" styleClass="label"/>
                                                <h:inputTextarea  onkeydown="limitText(this,200);" onkeyup="limitText(this,200);"
                                                                  cols="40" rows="3" value="#{abonarDiaAbertoBean.detalheJustificativa}" >
                                                </h:inputTextarea>
                                            </h:panelGrid>
                                            <br/>
                                            <h:panelGroup>
                                                <center>
                                                    <h:commandButton action="#{abonarDiaAbertoBean.enviar}" value="Confirmar"/>
                                                </center>
                                            </h:panelGroup>                                           
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
                            </h:commandLink>
                        </center>
                    </h:panelGroup>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>
