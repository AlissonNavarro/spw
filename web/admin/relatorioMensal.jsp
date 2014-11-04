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
                        <rich:messages infoClass="info" />
                    </center>
                </h:form>
                <h:form>
                    <a4j:keepAlive beanName="relatorioLotacaoBean"/>
                    <a4j:keepAlive beanName="relatorioResumoFrequenciaBean"/>
                    <a4j:keepAlive beanName="relatorioResumoEscalaBean"/>
                    <a4j:keepAlive beanName="afdtBean"/>
                    <a4j:keepAlive beanName="acjefBean"/>
                    <a4j:keepAlive beanName="relatorioPortaria1510Bean"/>
                    <a4j:keepAlive beanName="relatorioMensalSemEscalaBean"/>
                    <a4j:keepAlive beanName="fileUploadBean"/>
                    <a4j:keepAlive beanName="relatorioCatracasBean"/>
                    <h:panelGrid columns="1" >
                        <rich:tabPanel switchType="client" width="965" selectedTab="#{relatorioMensalBean.abaCorrente}">
                            <rich:tab id="tab1" label="Mensal com Escala" rendered="#{usuarioBean.perfil.relatorioMensComEscala== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab1"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid columns="2" >
                                        <h:panelGrid columns="6" >

                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Fim: " styleClass="label"/>
                                            <rich:spacer width="20"/>

                                            <h:selectOneMenu id="deparId" value="#{relatorioMensalBean.departamentoSelecionado}">
                                                <f:selectItems value="#{relatorioMensalBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{relatorioMensalBean.consultaFuncionario}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioMensalComEscala" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioMensalComEscalaModalPanel"
                                                                       attachTo="filtroFuncionarioMensalComEscala" operation="show" event="onclick"/>
                                            </h:outputLink>

                                            <rich:calendar inputSize="8" id="inicio" locale="#{relatorioMensalBean.objLocale}" value="#{relatorioMensalBean.dataInicio}">
                                            </rich:calendar>
                                            <rich:spacer width="20"/>

                                            <rich:calendar inputSize="8" id="fim" locale="#{relatorioMensalBean.objLocale}" value="#{relatorioMensalBean.dataFim}">
                                            </rich:calendar>
                                            <rich:spacer width="1"/>
                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{relatorioMensalBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{relatorioMensalBean.consultaFuncionario}"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>

                                            </h:panelGroup>
                                        </h:panelGrid>

                                        <%--   <h:panelGroup>
                                              <h:commandLink action="#{relatorioMensalBean.imprimirDepartamento}">
                                                  <h:graphicImage  value="../images/impressora.png" style="border:0"/>
                                              </h:commandLink>
                                          </h:panelGroup>--%>
                                        <h:panelGroup>
                                            <h:commandLink action="#{relatorioMensalBean.imprimirRelatorio}">
                                                <h:graphicImage  value="../images/impressora.png" style="border:0"/>
                                            </h:commandLink>
                                        </h:panelGroup>
                                    </h:panelGrid>
                                    <rich:modalPanel id="filtroFuncionarioMensalComEscalaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkMensalComEscala"/>
                                                <rich:componentControl for="filtroFuncionarioMensalComEscalaModalPanel" attachTo="hidelinkMensalComEscala" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{relatorioMensalBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{relatorioMensalBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{relatorioMensalBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioMensalBean.regimeOpcaoFiltroFuncionarioList}"/>

                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{relatorioMensalBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioMensalBean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeMensalComEscala"
                                                             action="#{relatorioMensalBean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioMensalComEscalaModalPanel" attachTo="confirmarOpcaoFiltroRegimeMensalComEscala"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>
                                </center>
                                <br/>
                                <br/>
                            </rich:tab>
                            <rich:tab id="tab2" label="Mensal sem Escala" rendered="#{usuarioBean.perfil.relatorioMensSemEscala== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab2"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid columns="2" >                                       
                                        <h:panelGrid columns="6" >
                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Fim:" styleClass="label"/>
                                            <rich:spacer width="20"/>

                                            <h:selectOneMenu id="deparId2" value="#{relatorioMensalSemEscalaBean.departamentoSelecionado}"  >
                                                <f:selectItems value="#{relatorioMensalSemEscalaBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{relatorioMensalSemEscalaBean.consultaFuncionario}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioMensalSemEscala" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioMensalSemEscalaModalPanel"
                                                                       attachTo="filtroFuncionarioMensalSemEscala" operation="show" event="onclick"/>
                                            </h:outputLink>

                                            <rich:calendar inputSize="8" id="inicio2" locale="#{relatorioMensalSemEscalaBean.objLocale}" value="#{relatorioMensalSemEscalaBean.dataInicio}">
                                            </rich:calendar>
                                            <rich:spacer width="20"/>

                                            <rich:calendar inputSize="8" id="fim2" locale="#{relatorioMensalSemEscalaBean.objLocale}" value="#{relatorioMensalSemEscalaBean.dataFim}">
                                            </rich:calendar>
                                            <rich:spacer width="1"/>

                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{relatorioMensalSemEscalaBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{relatorioMensalSemEscalaBean.consultaFuncionario}"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>

                                            </h:panelGroup>
                                        </h:panelGrid>

                                        <h:panelGroup>
                                            <h:commandLink action="#{relatorioMensalSemEscalaBean.imprimirDepartamento}">
                                                <h:graphicImage  value="../images/impressora.png" style="border:0"/>                                                
                                            </h:commandLink>
                                        </h:panelGroup>
                                    </h:panelGrid>
                                    <rich:modalPanel id="filtroFuncionarioMensalSemEscalaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkMensalSemEscala"/>
                                                <rich:componentControl for="filtroFuncionarioMensalSemEscalaModalPanel" attachTo="hidelinkMensalSemEscala" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{relatorioMensalSemEscalaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{relatorioMensalSemEscalaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{relatorioMensalSemEscalaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioMensalSemEscalaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{relatorioMensalSemEscalaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioMensalSemEscalaBean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeMensalSemEscala"
                                                             action="#{relatorioMensalSemEscalaBean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioMensalSemEscalaModalPanel" attachTo="confirmarOpcaoFiltroRegimeMensalSemEscala"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>
                                </center>
                                <br/>
                                <br/>
                            </rich:tab>
                            <rich:tab id="tab3" label="Resumo de Frequência" rendered="#{usuarioBean.perfil.relatorioDeResumodeFrequencias== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab3"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <a4j:region id="region1">
                                        <h:panelGrid columns="8" >
                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Funcionario " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Fim:" styleClass="label"/>
                                            <rich:spacer width="12"/>

                                            <h:selectOneMenu id="deparId3" value="#{relatorioResumoFrequenciaBean.departamentoSelecionado}"  >
                                                <f:selectItems value="#{relatorioResumoFrequenciaBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{relatorioResumoFrequenciaBean.consultaFuncionario}"
                                                             reRender="funcionarioH"/>
                                            </h:selectOneMenu>
                                            <rich:spacer width="10"/>

                                            <h:selectOneMenu id="funcionarioH" value="#{relatorioResumoFrequenciaBean.cod_funcionario}">
                                                <f:selectItems value="#{relatorioResumoFrequenciaBean.funcionarioList}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioFrequencia" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioFrequenciaModalPanel"
                                                                       attachTo="filtroFuncionarioFrequencia" operation="show" event="onclick"/>
                                            </h:outputLink>

                                            <rich:calendar inputSize="8" id="inicio3" locale="#{relatorioResumoFrequenciaBean.objLocale}"
                                                           value="#{relatorioResumoFrequenciaBean.dataInicio}">
                                            </rich:calendar>
                                            <rich:spacer width="10"/>

                                            <rich:calendar inputSize="8" id="fim3" locale="#{relatorioResumoFrequenciaBean.objLocale}"
                                                           value="#{relatorioResumoFrequenciaBean.dataFim}">
                                            </rich:calendar>
                                            <h:panelGroup>
                                                <rich:spacer width="15"/>
                                                <h:commandButton value="Consultar" action="#{relatorioResumoFrequenciaBean.consultar}"
                                                                 />
                                            </h:panelGroup>

                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{relatorioResumoFrequenciaBean.consultaFuncionario}" reRender="funcionarioH"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                            </h:panelGroup>
                                        </h:panelGrid>
                                    </a4j:region>
                                    <a4j:status id="progressoEmAberto" 
                                                for="region1" onstart="Richfaces.showModalPanel('panelStatus');"
                                                onstop="#{rich:component('panelStatus')}.hide()"/>
                                    <rich:modalPanel id="panelStatus" autosized="true" >
                                        <h:panelGrid columns="3">
                                            <h:graphicImage url="../images/load.gif" />
                                            <rich:spacer width="8"/>
                                            <h:outputText value="  Carregando…" styleClass="label" />
                                        </h:panelGrid>
                                    </rich:modalPanel>
                                    <rich:modalPanel id="filtroFuncionarioFrequenciaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                <rich:componentControl for="filtroFuncionarioFrequenciaModalPanel" attachTo="hidelink" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{relatorioResumoFrequenciaBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{relatorioResumoFrequenciaBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{relatorioResumoFrequenciaBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioResumoFrequenciaBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{relatorioResumoFrequenciaBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioResumoFrequenciaBean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeFrequencia"
                                                             action="#{relatorioResumoFrequenciaBean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioFrequenciaModalPanel" attachTo="confirmarOpcaoFiltroRegimeFrequencia"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>

                                    <h:panelGroup id="panelResumo" style="text-align:center;float:center">
                                        <br/>
                                        <center>
                                            <h:panelGrid columns="5" rendered="#{not empty relatorioResumoFrequenciaBean.relatorioResumoFrequencia}">
                                                <h:panelGroup>
                                                    <center>
                                                        <h:inputText style="width:200px" value="#{relatorioResumoFrequenciaBean.connWay}"></h:inputText>
                                                        </center>
                                                </h:panelGroup>
                                                <rich:spacer width="15"/>
                                                <h:panelGroup>
                                                    <center>
                                                        <h:commandButton value="Exportar dados para folha"
                                                                         actionListener="#{relatorioResumoFrequenciaBean.gerarFolha}"

                                                                         image="../images/database_24.png"/>
                                                        <br>
                                                        <h:outputText value="Exportar dados para folha"/>
                                                    </center>
                                                </h:panelGroup>
                                                <rich:spacer width="15"/>
                                                <h:panelGroup>
                                                    <center>
                                                        <h:commandButton value="Exportar frequência para folha"
                                                                         actionListener="#{relatorioResumoFrequenciaBean.gerarFrequenciaParaFolha}"

                                                                         image="../images/database_24.png"/>
                                                        <br>
                                                        <h:outputText value="Exportar frequência para folha"/>
                                                    </center>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                            <rich:spacer height="35"/>
                                            <h:panelGrid columns="3" rendered="#{not empty relatorioResumoFrequenciaBean.relatorioResumoFrequencia}">
                                                <h:panelGroup>
                                                    <center>
                                                        <h:commandButton value="Gerar Movimento"
                                                                         actionListener="#{relatorioResumoFrequenciaBean.gerarMovimento}"

                                                                         image="../images/movimento.png"/>
                                                        <br>
                                                        <h:outputText value="Gerar Movimento"/>
                                                    </center>
                                                </h:panelGroup>
                                                <rich:spacer width="35"/>
                                                <h:panelGroup id="panelEscolha">
                                                    <a4j:form>
                                                        <h:outputLink id="linkEscolha" value="#">
                                                            <center>
                                                                <h:graphicImage  value="../images/excel_icon.png" style="border:0"/>                                                                                                                            
                                                            </center>
                                                            <rich:componentControl for="exportarExcelModal"
                                                                                   attachTo="linkEscolha" operation="show" event="onclick"/>
                                                        </h:outputLink>                                                       
                                                        <h:outputText value="Exporta para Excel"/>
                                                        <rich:modalPanel id="exportarExcelModal" width="630" height="320"  style="text-align:center;float:center;" >
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <h:outputText value="Escolha as opções a serem exportadas"></h:outputText>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <f:facet name="controls">
                                                                <h:panelGroup>
                                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                                    <rich:componentControl for="exportarExcelModal" attachTo="hidelink" operation="hide" event="onclick"/>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <center>
                                                                <h:panelGrid columns="1" style="text-align:center;float:center">
                                                                    <h:panelGrid columns="3" style="text-align:left;float:left">
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.nome}"/>
                                                                            <h:outputText value="Nome " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.cpf}"/>
                                                                            <h:outputText value="CPF " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.matricula}"/>
                                                                            <h:outputText value="Matricula " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.departamento}"/>
                                                                            <h:outputText value="Departamento " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.cargo}"/>
                                                                            <h:outputText value="Cargo " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.regime}"/>
                                                                            <h:outputText value="Regime " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.dataAdmicao}"/>
                                                                            <h:outputText value="Data Admissão " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.saldo}"/>
                                                                            <h:outputText value="Saldo " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.atrasos}"/>
                                                                            <h:outputText value="Descontos " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.faltas}"/>
                                                                            <h:outputText value="Faltas " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.horaExtra}"/>
                                                                            <h:outputText value="Horas Extra " styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.totalQntAtraso16_59}"/>
                                                                            <h:outputText value="Atrasos < 1 hora" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.totalQntAtrasoMaiorUmaHora}"/>
                                                                            <h:outputText value="Atrasos > 1 hora" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.diasAtraso}"/>
                                                                            <h:outputText value="Lista de Dias Atraso" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.diasFalta}"/>
                                                                            <h:outputText value="Lista de Dias Falta" styleClass="label"/>
                                                                        </h:panelGroup>


                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.qntDiasSemEscalasTrabalhados}"/>
                                                                            <h:outputText value="Dias Trabalhados Sem Escala" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.diasSemEscalasTrabalhados}"/>
                                                                            <h:outputText value="Lista Dias Trabalhados Sem Escala" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.qntPontosForaDaFaixa}"/>
                                                                            <h:outputText value="Pontos fora da faixa" styleClass="label"/>
                                                                        </h:panelGroup>

                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.diasPontoForaDaFaixa}"/>
                                                                            <h:outputText value="Lista de Pontos fora da faixa" styleClass="label"/>
                                                                        </h:panelGroup>


                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.horaPrevista}"/>
                                                                            <h:outputText value="Horas Previstas " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.horaTrabalhada}"/>
                                                                            <h:outputText value="Horas Trabalhadas " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.gratificacao}"/>
                                                                            <h:outputText value="Gratificação " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.feriadosTrabalhados}"/>
                                                                            <h:outputText value="Feriados " styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.totalHorasReceberAddNoturno}"/>
                                                                            <h:outputText value="Adicional Noturno (Horas)" styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.qntDiasAddNoturno}"/>
                                                                            <h:outputText value="Adicional Noturno (Dias)" styleClass="label"/>
                                                                        </h:panelGroup>
                                                                        <h:panelGroup>
                                                                            <h:selectBooleanCheckbox value="#{relatorioResumoFrequenciaBean.escolhaExcel.qntDSR}"/>
                                                                            <h:outputText value="Descontos de dsr " styleClass="label"/>
                                                                        </h:panelGroup>        
                                                                    </h:panelGrid>
                                                                </center>
                                                                <br>
                                                                <h:panelGroup>
                                                                    <h:selectBooleanCheckbox onchange="selectAll(this);"/>
                                                                    <h:outputText value="Selecionar Todos" styleClass="label"/>
                                                                </h:panelGroup>
                                                                <script>
                                                                    function selectAll(checkAll) {
                                                                        var checked = checkAll.checked;
                                                                        var col = document.getElementsByTagName("input");
                                                                        for (var i = 0; i < col.length; i++) {
                                                                            col[i].checked = checked;
                                                                        }
                                                                    }
                                                                </script>  
                                                                <h:commandButton value="Enviar" id="enviar" actionListener="#{relatorioResumoFrequenciaBean.exportarExcel}" >
                                                                    <rich:componentControl for="exportarExcelModal" attachTo="enviar"
                                                                                           operation="hide" event="onclick"/>
                                                                </h:commandButton>
                                                            </h:panelGrid>
                                                        </rich:modalPanel>
                                                    </a4j:form>
                                                </h:panelGroup>
                                            </h:panelGrid>
                                            <br>
                                            <rich:dataTable id="resumoList"
                                                            cellpadding="0" cellspacing="0"
                                                            border="0"
                                                            rowClasses="zebra1,zebra2"
                                                            value="#{relatorioResumoFrequenciaBean.relatorioResumoFrequencia}" var="linha"
                                                            rendered="#{not empty relatorioResumoFrequenciaBean.relatorioResumoFrequencia}"
                                                            rows="25">
                                                <f:facet name="header">
                                                    <h:outputText value="RESUMO DA FREQUÊNCIA"/>
                                                </f:facet>
                                                <rich:column sortBy="#{linha.matricula}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="MATRÍCULA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.matricula}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.nome}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.nome}"/>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="SALDO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.saldo}"/>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="FALTAS"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.faltas}"/>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="HORA EXTRA"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.horaExtra}"/>
                                                </rich:column>                                                
                                                <rich:column  style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="ADICIONAL NOTURNO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.totalHorasReceberAddNoturno}" />
                                                </rich:column>                                                
                                            </rich:dataTable>
                                            <rich:datascroller  id="datascrollersT"
                                                                for="resumoList"                                                               
                                                                renderIfSinglePage="false"
                                                                page="#{relatorioResumoFrequenciaBean.page}" />
                                            <br/>
                                        </center>
                                    </h:panelGroup>                                    
                                </center>                                
                            </rich:tab>
                            <rich:tab id="tab4" label="Resumo de Escalas" rendered="#{usuarioBean.perfil.relatorioDeResumodeEscalas== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab4"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <a4j:support event="onclick" ajaxSingle="true" reRender="f_messagens"/>
                                    <h:panelGrid columns="6" >
                                        <h:outputText value="Departamento: " styleClass="label"/>
                                        <rich:spacer width="15"/>
                                        <h:outputText value="Mês: " styleClass="label"/>
                                        <rich:spacer width="15"/>
                                        <h:outputText value="Ano: " styleClass="label"/>
                                        <rich:spacer width="15"/>

                                        <h:selectOneMenu id="deparId4" value="#{relatorioResumoEscalaBean.departamentoSelecionado}"  >
                                            <f:selectItems value="#{relatorioResumoEscalaBean.departamentosSelecItem}"/>
                                        </h:selectOneMenu>
                                        <rich:spacer width="15"/>

                                        <h:selectOneMenu id="mesId4" value="#{relatorioResumoEscalaBean.mesSelecionado}"  >
                                            <f:selectItems value="#{relatorioResumoEscalaBean.mesesSelecItem}"/>
                                        </h:selectOneMenu>
                                        <rich:spacer width="15"/>


                                        <rich:inputNumberSpinner value="#{relatorioResumoEscalaBean.ano}" inputSize="3" maxValue="2020" minValue="2009"/>

                                        <h:panelGroup>
                                            <rich:spacer width="15"/>
                                            <h:commandButton  value="Consultar" action="#{relatorioResumoEscalaBean.consultar}" />
                                        </h:panelGroup>

                                        <h:panelGroup>
                                            <h:selectBooleanCheckbox value="#{relatorioResumoEscalaBean.incluirSubSetores}"/>
                                            <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                        </h:panelGroup>
                                    </h:panelGrid>
                                    <h:panelGroup id="panelResumoEscala" style="text-align:center;float:center">
                                        <br/>
                                        <center>
                                            <rich:dataTable id="resumoEscalaList"
                                                            cellpadding="0" cellspacing="0"
                                                            border="0"
                                                            rowClasses="zebra1,zebra2"
                                                            value="#{relatorioResumoEscalaBean.usuarioList}" var="linha"
                                                            rendered="#{not empty relatorioResumoEscalaBean.usuarioList}"
                                                            rows="25">
                                                <f:facet name="header">
                                                    <h:outputText value="FUNCIONÁRIOS SEM ESCALA"/>
                                                </f:facet>
                                                <rich:column sortBy="#{linha.ssn}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="CPF"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.ssn}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.nome}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.nome}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.deptname}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="DEPARTAMENTO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.deptname}"/>
                                                </rich:column>                                                
                                            </rich:dataTable>
                                            <rich:datascroller  id="datascrollersResumoEscala"
                                                                for="resumoEscalaList"
                                                                page="1"
                                                                renderIfSinglePage="false"/>
                                            <br/>
                                        </center>
                                    </h:panelGroup>                                    
                                </center>
                                <br/>
                                <br/>
                            </rich:tab>
                            <rich:tab id="tab5" label="Logs" rendered="#{usuarioBean.perfil.relatorioLogDeOperacoes== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab5"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid columns="5" >
                                        <h:outputText value="Data: " styleClass="label"/>
                                        <rich:spacer width="12"/>
                                        <rich:calendar inputSize="8" locale="#{logBean.objLocale}" value="#{logBean.dataLog}"/>
                                        <rich:spacer width="15"/>
                                        <h:commandButton value="Baixar" action="#{logBean.downloadLog}"/>
                                    </h:panelGrid>
                                </center>
                                <br/>
                                <br/>
                            </rich:tab>
                            <rich:tab id="sub61" label="AFDT" rendered="#{usuarioBean.perfil.afdt== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab61"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <a4j:region id="region3">
                                        <h:panelGrid columns="8" columnClasses="gridContent">

                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Funcionario " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Fim:" styleClass="label"/>
                                            <rich:spacer width="12"/>

                                            <h:selectOneMenu value="#{afdtBean.departamentoSelecionado}"  >
                                                <f:selectItems value="#{afdtBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{afdtBean.consultaFuncionario}"
                                                             reRender="funcionarioAfdt"/>
                                            </h:selectOneMenu>
                                            <rich:spacer width="10"/>

                                            <h:selectOneMenu id="funcionarioAfdt" value="#{afdtBean.cod_funcionario}">
                                                <f:selectItems value="#{afdtBean.funcionarioList}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioAfdt" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioAfdtModalPanel"
                                                                       attachTo="filtroFuncionarioAfdt" operation="show" event="onclick"/>
                                            </h:outputLink>
                                            <rich:calendar inputSize="8" locale="#{afdtBean.objLocale}" value="#{afdtBean.dataInicio}"/>
                                            <rich:spacer width="10"/>
                                            <rich:calendar inputSize="8" locale="#{afdtBean.objLocale}" value="#{afdtBean.dataFim}"/>
                                            <h:panelGroup>
                                                <rich:spacer width="15"/>
                                                <h:commandButton value="Gerar" action="#{afdtBean.gerar}"/>
                                            </h:panelGroup>

                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{afdtBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{afdtBean.consultaFuncionario}" reRender="funcionarioAfdt"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                            </h:panelGroup>
                                        </h:panelGrid>
                                    </a4j:region>
                                    <a4j:status  for="region3" onstart="Richfaces.showModalPanel('panelStatusAfdt');" onstop="#{rich:component('panelStatusAfdt')}.hide()"/>
                                    <rich:modalPanel id="panelStatusAfdt" autosized="true" >
                                        <h:panelGrid columns="3">
                                            <h:graphicImage url="../images/load.gif" />
                                            <rich:spacer width="8"/>
                                            <h:outputText value="  Carregando…" styleClass="label" />
                                        </h:panelGrid>
                                    </rich:modalPanel>
                                    <rich:modalPanel id="filtroFuncionarioAfdtModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAfdt"/>
                                                <rich:componentControl for="filtroFuncionarioAfdtModalPanel" attachTo="hidelinkAfdt" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{afdtBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{afdtBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{afdtBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{afdtBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{afdtBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{afdtBean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeAfdt"
                                                             action="#{afdtBean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioAfdtModalPanel" attachTo="confirmarOpcaoFiltroRegimeAfdt"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>
                                </center>
                            </rich:tab>
                            <rich:tab id="sub62" label="ACJEF" rendered="#{usuarioBean.perfil.acjef== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab62"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <a4j:region id="region2">
                                        <h:panelGrid columns="8" columnClasses="gridContent">

                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Funcionario " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Fim:" styleClass="label"/>
                                            <rich:spacer width="12"/>

                                            <h:selectOneMenu value="#{acjefBean.departamentoSelecionado}"  >
                                                <f:selectItems value="#{acjefBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{acjefBean.consultaFuncionario}"
                                                             reRender="funcionarioAcjef"/>
                                            </h:selectOneMenu>
                                            <rich:spacer width="10"/>

                                            <h:selectOneMenu id="funcionarioAcjef" value="#{acjefBean.cod_funcionario}">
                                                <f:selectItems value="#{acjefBean.funcionarioList}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioAcjef" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioAcjefModalPanel"
                                                                       attachTo="filtroFuncionarioAcjef" operation="show" event="onclick"/>
                                            </h:outputLink>
                                            <rich:calendar inputSize="8" locale="#{acjefBean.objLocale}" value="#{acjefBean.dataInicio}"/>
                                            <rich:spacer width="10"/>
                                            <rich:calendar inputSize="8" locale="#{acjefBean.objLocale}" value="#{acjefBean.dataFim}"/>
                                            <h:panelGroup>
                                                <rich:spacer width="15"/>
                                                <h:commandButton value="Gerar" action="#{acjefBean.gerar}"/>
                                            </h:panelGroup>

                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{acjefBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{acjefBean.consultaFuncionario}" reRender="funcionarioAcjef"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                            </h:panelGroup>
                                        </h:panelGrid>
                                    </a4j:region>
                                    <a4j:status  for="region2" onstart="Richfaces.showModalPanel('panelStatusAcjef');" onstop="#{rich:component('panelStatusAcjef')}.hide()"/>
                                    <rich:modalPanel id="panelStatusAcjef" autosized="true" >
                                        <h:panelGrid columns="3">
                                            <h:graphicImage url="../images/load.gif" />
                                            <rich:spacer width="8"/>
                                            <h:outputText value="  Carregando…" styleClass="label" />
                                        </h:panelGrid>
                                    </rich:modalPanel>
                                    <rich:modalPanel id="filtroFuncionarioAcjefModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkAcjef"/>
                                                <rich:componentControl for="filtroFuncionarioAcjefModalPanel" attachTo="hidelinkAcjef" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{acjefBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{acjefBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{acjefBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{acjefBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{acjefBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{acjefBean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeAcjef"
                                                             action="#{acjefBean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioAcjefModalPanel" attachTo="confirmarOpcaoFiltroRegimeAcjef"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>
                                </center>
                            </rich:tab>
                            <rich:tab id="sub63" label="Espelho de Ponto" rendered="#{usuarioBean.perfil.espelhoDePonto== true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tab63"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid  columns="3" style="text-align:center;float:center">
                                        <h:panelGrid id="espelhoPontoGrid" columns="10" style="text-align:center;float:center">
                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Funcionário: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Data Inicial: " styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Data Final:" styleClass="label"/>
                                            <rich:spacer width="12"/>
                                            <h:outputText value="Razão Social: " styleClass="label"/>
                                            <rich:spacer width="12"/>

                                            <h:selectOneMenu value="#{relatorioPortaria1510Bean.departamento}">
                                                <f:selectItems value="#{relatorioPortaria1510Bean.departamentolist}"/>
                                                <a4j:support event="onchange" action="#{relatorioPortaria1510Bean.consultaFuncionario}"
                                                             reRender="funcionarioEspelho"/>

                                            </h:selectOneMenu>
                                            <rich:spacer width="10"/>

                                            <h:selectOneMenu id="funcionarioEspelho" value="#{relatorioPortaria1510Bean.cod_funcionario}">
                                                <f:selectItems value="#{relatorioPortaria1510Bean.funcionarioList}"/>
                                                <a4j:support event="onchange"
                                                             />
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioEspelhoDePonto" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioEspelhoDePontoModalPanel"
                                                                       attachTo="filtroFuncionarioEspelhoDePonto" operation="show" event="onclick"/>
                                            </h:outputLink>

                                            <rich:calendar inputSize="8" locale="#{relatorioPortaria1510Bean.objLocale}" value="#{relatorioPortaria1510Bean.dataInicio}" >
                                                <a4j:support event="onchanged" ajaxSingle="true"
                                                             />
                                            </rich:calendar>
                                            <rich:spacer width="10"/>

                                            <rich:calendar inputSize="8" locale="#{relatorioPortaria1510Bean.objLocale}" value="#{relatorioPortaria1510Bean.dataFim}">
                                                <a4j:support event="onchanged" ajaxSingle="true"
                                                             />
                                            </rich:calendar>
                                            <rich:spacer width="10"/>
                                            <h:selectOneMenu id="listEmpresas" value="#{relatorioPortaria1510Bean.razaoSocial}">
                                                <f:selectItems value="#{empresaBean.empresaList}"/>
                                            </h:selectOneMenu>
                                            <rich:spacer width="10"/>
                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{relatorioPortaria1510Bean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{relatorioPortaria1510Bean.consultaFuncionario}"
                                                                 reRender="funcionarioEspelho"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                            </h:panelGroup>
                                        </h:panelGrid>
                                        <h:panelGrid columns="1">


                                        </h:panelGrid>
                                        <h:commandButton value="Gerar" action="#{relatorioPortaria1510Bean.geraRelatorio}"/>
                                    </h:panelGrid>
                                    <rich:modalPanel id="filtroFuncionarioEspelhoDePontoModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                            </h:panelGroup>
                                        </f:facet>
                                        <f:facet name="controls">
                                            <h:panelGroup>
                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkEspelhoDePonto"/>
                                                <rich:componentControl for="filtroFuncionarioEspelhoDePontoModalPanel" attachTo="hidelinkEspelhoDePonto" operation="hide" event="onclick"/>
                                            </h:panelGroup>
                                        </f:facet>
                                        <center>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                <h:panelGroup>
                                                    <h:selectOneMenu value="#{relatorioPortaria1510Bean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                        <f:selectItems value="#{relatorioPortaria1510Bean.cargoOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneMenu>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                <h:panelGroup >
                                                    <h:selectOneRadio
                                                        value="#{relatorioPortaria1510Bean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioPortaria1510Bean.regimeOpcaoFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br>
                                            <fieldset class="demo_fieldset" >
                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                <h:panelGroup>
                                                    <h:selectOneRadio
                                                        value="#{relatorioPortaria1510Bean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                        <f:selectItems
                                                            value="#{relatorioPortaria1510Bean.gestorFiltroFuncionarioList}"/>
                                                    </h:selectOneRadio>
                                                </h:panelGroup>
                                            </fieldset>
                                            <br> <br>










                                            <br>
                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeEspelhoDePonto"
                                                             action="#{relatorioPortaria1510Bean.consultaFuncionario}">
                                                <rich:componentControl for="filtroFuncionarioEspelhoDePontoModalPanel" attachTo="confirmarOpcaoFiltroRegimeEspelhoDePonto"
                                                                       operation="hide" event="onclick"/>
                                            </h:commandButton>
                                        </center>
                                    </rich:modalPanel>
                                </center>
                            </rich:tab>
                            <rich:tab id="tabCatracas" label="Relatório de Catracas"
                                      rendered="#{usuarioBean.perfil.relatorioCatracas == true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tabCatracas"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid columns="2" >
                                        <h:panelGrid columns="6" >

                                            <h:outputText value="Departamento: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Início: " styleClass="label"/>
                                            <rich:spacer width="20"/>
                                            <h:outputText value="Fim: " styleClass="label"/>
                                            <rich:spacer width="20"/>

                                            <h:selectOneMenu id="deparCatracaId" value="#{relatorioCatracasBean.departamentoSelecionado}">
                                                <f:selectItems value="#{relatorioCatracasBean.departamentosSelecItem}"/>
                                                <a4j:support event="onchange" action="#{relatorioCatracasBean.consultaFuncionario}"/>
                                            </h:selectOneMenu>
                                            <h:outputLink id="filtroFuncionarioCatracas" value="#">
                                                <center>
                                                    <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                </center>
                                                <rich:componentControl for="filtroFuncionarioMensalComEscalaModalPanel"
                                                                       attachTo="filtroFuncionarioMensalComEscala" operation="show" event="onclick"/>
                                            </h:outputLink>

                                            <rich:calendar inputSize="8" id="inicio4" locale="#{relatorioCatracasBean.objLocale}" value="#{relatorioCatracasBean.dataInicio}">
                                            </rich:calendar>
                                            <rich:spacer width="20"/>

                                            <rich:calendar inputSize="8" id="fim4" locale="#{relatorioCatracasBean.objLocale}" value="#{relatorioCatracasBean.dataFim}">
                                            </rich:calendar>
                                            <rich:spacer width="1"/>
                                            <h:panelGroup>
                                                <h:selectBooleanCheckbox value="#{relatorioCatracasBean.incluirSubSetores}">
                                                    <a4j:support event="onchange" action="#{relatorioCatracasBean.consultaFuncionario}"/>
                                                </h:selectBooleanCheckbox>
                                                <h:outputText value=" Incluir subsetores" styleClass="italico"/>

                                            </h:panelGroup>
                                        </h:panelGrid>

                                        <h:panelGroup>
                                            <h:commandLink action="#{relatorioCatracasBean.imprimirRelatorio}">
                                                <h:graphicImage  value="../images/impressora.png" style="border:0"/>
                                            </h:commandLink>
                                        </h:panelGroup>
                                    </h:panelGrid> 
                                </center>
                            </rich:tab>
                            <rich:tab id="tabLogomarca" label="Configurações / Logomarca"
                                      rendered="#{usuarioBean.perfil.relatorioConfiguracao == true}">
                                <a4j:support event="ontabenter" action="#{relatorioMensalBean.setAba}" reRender="f_messagens">
                                    <a4j:actionparam name="tab" value="tabLogomarca"/>
                                </a4j:support>
                                <br/>
                                <center>
                                    <h:panelGrid  columns="1" style="text-align:center;float:center">
                                        <center>
                                            <rich:panel style="text-align: center;">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Adicione uma logomarca"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <h:panelGrid columns="3" id="uploadGrid">
                                                    <rich:fileUpload fileUploadListener="#{fileUploadBean.listener}"
                                                                     maxFilesQuantity="1"
                                                                     immediateUpload="#{fileUploadBean.autoUpload}"
                                                                     acceptedTypes="jpg, gif, png, bmp"
                                                                     allowFlash="#{fileUploadBean.useFlash}"
                                                                     autoclear="true"
                                                                     id="upload"
                                                                     cancelEntryControlLabel="Cancelar"
                                                                     clearAllControlLabel="Limpar"
                                                                     listHeight="59" listWidth="250">
                                                        <a4j:support event="onuploadcomplete" reRender="uploadGrid"
                                                                     />
                                                    </rich:fileUpload>
                                                    <rich:spacer width="50"/>
                                                    <h:panelGroup  rendered="#{fileUploadBean.logoExiste}">
                                                        <rich:panel bodyClass="info_">
                                                            <f:facet name="header">
                                                                <h:outputText value="Logomarca" />
                                                            </f:facet>

                                                            <rich:panel bodyClass="rich-laguna-panel-no-header">
                                                                <h:panelGrid columns="2">
                                                                    <a4j:mediaOutput element="img" mimeType="#{file.mime}"
                                                                                     createContent="#{fileUploadBean.paint}" value="1"
                                                                                     style="width:180px; height:100px;" cacheable="false">
                                                                        <f:param value="#{fileUploadBean.timeStamp}" name="time"/>
                                                                    </a4j:mediaOutput>
                                                                </h:panelGrid>
                                                            </rich:panel>
                                                            <br />
                                                            <h:panelGroup id="cancelarButton">
                                                                <a4j:commandButton action="#{fileUploadBean.clearUploadData}"
                                                                                   reRender="uploadGrid" value="Deletar"/>
                                                            </h:panelGroup>
                                                        </rich:panel>
                                                        <rich:spacer height="3"/>
                                                    </h:panelGroup>
                                                </h:panelGrid>
                                            </rich:panel>
                                            <br/><br/>
                                            <rich:panel style="text-align: center;">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Escolha o tipo de relatório padrão do sistema"/>
                                                    </h:panelGroup>
                                                </f:facet>

                                                <h:selectOneRadio
                                                    value="#{tipoRelatorioBean.tipoRelatorio}" >
                                                    <f:selectItems
                                                        value="#{tipoRelatorioBean.tipoRelatorioList}"/>
                                                    <a4j:support event="onchange"  action="#{tipoRelatorioBean.alterarTipoRelatorio}"
                                                                 reRender="panel,dias,resumo,f_messagens"/>
                                                </h:selectOneRadio>
                                            </rich:panel>
                                        </center>
                                    </h:panelGrid>
                                </center>
                            </rich:tab>
                        </rich:tabPanel>
                        <br/>
                        <h:panelGroup>
                            <%--<center>
                                <h:commandLink action="voltarAdmin">
                                    <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                </h:commandLink>
                            </center>--%>
                        </h:panelGroup>
                    </h:panelGrid>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body>
</html>

