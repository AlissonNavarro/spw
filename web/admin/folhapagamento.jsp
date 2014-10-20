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
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <title>SGN - Soluções em Gestão De Negócios</title>
    </head>
    <body>
        <f:view>
            <jsp:include page="../www/_top.jsp"/>
            <h:form id="f_messagens" prependId="false">
                <center>
                    <a4j:outputPanel id="info" ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </center>
            </h:form>
            <h:form>
                <a4j:keepAlive beanName="FOLHABean" />
                <a4j:keepAlive beanName="FOLHATotalBean" />
                <center>
                    <h:panelGrid columns="1" >
                        <center>
                            <rich:tabPanel switchType="client" width="965">
                                <%--             <rich:tab id="tab1" label="Lista de Presença com Escala">
                                    <center>
                                        <h:panelGrid columns="1" >
                                            <rich:panel>
                                                <center>
                                                    <h:panelGrid columns="7" >
                                                        <h:outputText value="Departamento: " styleClass="label"/>
                                                        <rich:spacer width="20"/>
                                                        <h:outputText value="Data: " styleClass="label"/>
                                                        <rich:spacer width="20"/>
                                                        <h:outputText value="Hora" styleClass="label"/>
                                                        <rich:spacer width="20"/>
                                                        <rich:spacer width="20"/>

                                                        <h:selectOneMenu id="deparId" value="#{listaPresencaBean.departamentoSelecionado}"  >
                                                            <f:selectItems value="#{listaPresencaBean.departamentosSelecItem}"/>
                                                        </h:selectOneMenu>
                                                        <rich:spacer width="20"/>

                                                        <rich:calendar id="dataId" locale="#{listaPresencaBean.objLocale}" value="#{listaPresencaBean.data}">
                                                        </rich:calendar>
                                                        <rich:spacer width="20"/>

                                                        <h:inputText id="hora" size="5" value="#{listaPresencaBean.hora}">
                                                            <rich:jQuery selector="#hora" query="mask('99:99')" timing="onload"/>
                                                        </h:inputText>
                                                        <rich:spacer width="20"/>
                                                        <h:commandButton id="buttonId" value="Consultar" action="#{listaPresencaBean.consultar}"/>
                                                    </h:panelGrid>
                                                </center>
                                                <br/>
                                                <center>
                                                    <h:selectOneRadio
                                                        value="#{listaPresencaBean.filtroSelecionado}">
                                                        <f:selectItems
                                                            value="#{listaPresencaBean.filtroSelecItem}" />
                                                    </h:selectOneRadio>
                                                </center>
                                            </rich:panel>
                                        </h:panelGrid>
                                        <br/>
                                        <rich:dataTable id="presencaList" onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                        cellpadding="0" cellspacing="0"  width="560px"
                                                        border="0"
                                                        value="#{listaPresencaBean.listaPresencaList}" var="linha"
                                                        rendered="#{not empty listaPresencaBean.listaPresencaList}"
                                                        rows="25">
                                            <f:facet name="header">
                                                <rich:columnGroup>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="CPF" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Nome" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Último Registro" />
                                                    </h:column>
                                                    <h:column>
                                                        <h:outputText styleClass="headerText" value="Situação" />
                                                    </h:column>
                                                </rich:columnGroup>
                                            </f:facet>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{linha.cpf}" />
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{linha.nome}" />
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{linha.ultimoRegistro}" >
                                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                                    </h:outputText>
                                                </center>
                                            </h:column>
                                            <h:column>
                                                <center>
                                                    <h:outputText value="#{linha.situacao}" />
                                                </center>
                                            </h:column>
                                        </rich:dataTable>
                                        <rich:datascroller  id="datascrollers"
                                                            for="presencaList"
                                                            page="#{listaPresencaBean.page}"
                                                            renderIfSinglePage="false"
                                                            />
                                        <br/>
                                        <h:panelGroup>
                                            <h:commandButton value="Voltar" action="voltarAdmin"/>
                                            <rich:spacer width="20"/>
                                            <h:commandButton value="Imprimir" action="#{listaPresencaBean.imprimir}" rendered="#{not empty listaPresencaBean.listaPresencaList}"/>
                                        </h:panelGroup>
                                    </center>
                                </rich:tab>--%>
                                <rich:tab id="tab2" label="Folha de Pagamento">
                                    <center>
                                        <a4j:region id="region1">
                                            <h:panelGrid columns="1" >
                                                <rich:panel>
                                                    <center>
                                                        <h:panelGrid columns="2" >
                                                            <h:panelGrid columns="6" >
                                                                <h:outputText value="PAGAMENTO: " styleClass="label"/>
                                                                <rich:spacer width="12"/>
                                                                <h:outputText value="Data: " styleClass="label"/>
                                                                <rich:spacer width="12"/>
                                                                <h:outputText value="Hora:" styleClass="label"/>
                                                                <rich:spacer width="12"/>

                                                                <h:selectOneMenu id="deparIdT" value="#{listaPresencaTotalBean.departamentoSelecionado}"  >
                                                                    <f:selectItems value="#{listaPresencaTotalBean.departamentosSelecItem}"/>
                                                                    <a4j:support event="onchange" action="#{listaPresencaTotalBean.consultaFuncionario}"/>
                                                                </h:selectOneMenu>
                                                                <h:outputLink id="filtroFuncionarioListaPresenca" value="#">
                                                                    <center>
                                                                        <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                                    </center>
                                                                    <rich:componentControl for="filtroFuncionarioListaPresencaModalPanel"
                                                                                           attachTo="filtroFuncionarioListaPresenca" operation="show" event="onclick"/>
                                                                </h:outputLink>

                                                                <rich:calendar inputSize="8" id="dataIdT" locale="#{listaPresencaTotalBean.objLocale}" value="#{listaPresencaTotalBean.data}">
                                                                </rich:calendar>
                                                                <rich:spacer width="12"/>

                                                                <h:inputText id="horaT" size="3" value="#{listaPresencaTotalBean.hora}">
                                                                    <rich:jQuery selector="#horaT" query="mask('99:99')" timing="onload"/>
                                                                </h:inputText>

                                                                <h:panelGroup>
                                                                    <rich:spacer width="12"/>
                                                                    <a4j:commandButton action="#{listaPresencaTotalBean.consultar}" value="Pesquisar"
                                                                                       reRender="resumo,presencaListT,datascrollersT,panelPresenca">
                                                                    </a4j:commandButton>
                                                                </h:panelGroup>

                                                                <h:panelGroup>
                                                                    <h:selectBooleanCheckbox value="#{listaPresencaTotalBean.incluirSubSetores}">
                                                                        <a4j:support event="onchange" action="#{listaPresencaTotalBean.consultaFuncionario}"/>
                                                                    </h:selectBooleanCheckbox>
                                                                    <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                                </h:panelGroup>
                                                            </h:panelGrid>
                                                        </h:panelGrid>
                                                        <rich:modalPanel id="filtroFuncionarioListaPresencaModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                                            <f:facet name="header">
                                                                <h:panelGroup>
                                                                    <h:outputText value="Filtrar funcionários"></h:outputText>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <f:facet name="controls">
                                                                <h:panelGroup>
                                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkListaPresenca"/>
                                                                    <rich:componentControl for="filtroFuncionarioListaPresencaModalPanel" attachTo="hidelinkListaPresenca" operation="hide" event="onclick"/>
                                                                </h:panelGroup>
                                                            </f:facet>
                                                            <center>
                                                                <fieldset class="demo_fieldset" >
                                                                    <legend style="font-weight: bold;">Por Regime</legend>
                                                                    <h:panelGroup >
                                                                        <h:selectOneRadio
                                                                            value="#{listaPresencaTotalBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                                            <f:selectItems
                                                                                value="#{listaPresencaTotalBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                                        </h:selectOneRadio>
                                                                    </h:panelGroup>
                                                                </fieldset>
                                                                <br>
                                                                <fieldset class="demo_fieldset" >
                                                                    <legend style="font-weight: bold;">Por Gestor</legend>
                                                                    <h:panelGroup>
                                                                        <h:selectOneRadio
                                                                            value="#{listaPresencaTotalBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                                            <f:selectItems
                                                                                value="#{listaPresencaTotalBean.gestorFiltroFuncionarioList}"/>
                                                                        </h:selectOneRadio>
                                                                    </h:panelGroup>
                                                                </fieldset>
                                                                <br> <br>                                                                
                                                                <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeListaPresenca"
                                                                                 action="#{listaPresencaTotalBean.consultaFuncionario}">
                                                                    <rich:componentControl for="filtroFuncionarioListaPresencaModalPanel" attachTo="confirmarOpcaoFiltroRegimeListaPresenca"
                                                                                           operation="hide" event="onclick"/>
                                                                </h:commandButton>
                                                            </center>
                                                        </rich:modalPanel>
                                                    </center>
                                                    <br/>
                                                    <center>
                                                        <h:selectOneRadio
                                                            value="#{listaPresencaTotalBean.filtroSelecionado}">
                                                            <f:selectItems
                                                                value="#{listaPresencaTotalBean.filtroSelecItem}" />
                                                        </h:selectOneRadio>                                                       
                                                        <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
                                                        <rich:modalPanel id="panelStatus" autosized="true" >
                                                            <h:panelGrid columns="3">
                                                                <h:graphicImage url="../images/load.gif" />
                                                                <rich:spacer width="8"/>
                                                                <h:outputText value="  Carregando…" styleClass="label" />
                                                            </h:panelGrid>
                                                        </rich:modalPanel>
                                                    </center>
                                                </rich:panel>
                                            </h:panelGrid>                                           
                                            <br/>
                                            <center>
                                                <h:panelGroup id="pesquisa" >
                                                    <h:outputText id="filtro" value="Pesquisar: " styleClass="label"/>
                                                    <rich:spacer width="7"/>
                                                    <h:inputText value="#{listaPresencaTotalBean.filtroNomePesquisa}" id="input" size="40">
                                                        <a4j:support event="onkeyup" reRender="resumo,presencaListT,datascrollersT,panelPresenca"
                                                                     ignoreDupResponses="true" requestDelay="1100"
                                                                     action="#{listaPresencaTotalBean.consultar}"/>
                                                    </h:inputText>
                                                </h:panelGroup>
                                                <br/> 
                                                <h:panelGroup id="resumo" >
                                                    <center>
                                                        <h:panelGrid rendered="#{not empty listaPresencaTotalBean.listaPresencaList}" columns="2">
                                                            <h:panelGrid columns="18" styleClass="resumo" >
                                                                <h:outputText value="Total de Funcionários: " styleClass="label"/>
                                                                <h:outputText value="#{listaPresencaTotalBean.totalFuncionarios}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Funcionários Presentes: " styleClass="label"/>
                                                                <h:outputText value="#{listaPresencaTotalBean.totalPresentes}"/>
                                                                <rich:spacer width="15"/>
                                                                <h:outputText value="Funcionários Ausentes " styleClass="label"/>
                                                                <h:outputText value=" #{listaPresencaTotalBean.totalAusentes}"/>
                                                            </h:panelGrid>
                                                            <h:panelGroup>
                                                                <rich:spacer width="15"/>
                                                                <h:commandLink action="#{listaPresencaTotalBean.imprimir}" rendered="#{not empty listaPresencaTotalBean.listaPresencaList}">
                                                                    <h:graphicImage  value="../images/impressora.png" style="border:0"/>
                                                                </h:commandLink>
                                                            </h:panelGroup>
                                                        </h:panelGrid>                                                        
                                                    </center>
                                                </h:panelGroup>
                                            </center>
                                        </a4j:region>
                                        <h:panelGroup id="panelPresenca" >
                                            <rich:dataTable id="presencaListT"                                                            
                                                            cellpadding="0" cellspacing="0"  width="560px"
                                                            border="0"
                                                            rowClasses="zebra1,zebra2"
                                                            value="#{listaPresencaTotalBean.listaPresencaList}" var="linha"
                                                            rendered="#{not empty listaPresencaTotalBean.listaPresencaList}"
                                                            rows="25">
                                                <f:facet name="header">
                                                    <h:outputText value="LISTA DE PRESENÇA"/>
                                                </f:facet>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="CPF"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.cpf}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.nome}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.nome}"/>
                                                </rich:column>
                                                <rich:column sortBy="#{linha.ultimoRegistro}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="ÚLTIMO REGISTRO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.ultimoRegistro}" >
                                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm:ss" />
                                                    </h:outputText>
                                                </rich:column>
                                                <rich:column  style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="SITUAÇÃO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.situacao}" />
                                                </rich:column>
                                            </rich:dataTable>
                                            <rich:datascroller  id="datascrollersT"
                                                                for="presencaListT"
                                                                page="1"
                                                                renderIfSinglePage="false"                                                               
                                                                />                                            
                                            <br/>
                                        </h:panelGroup>                                        
                                    </center>
                                </rich:tab>
                            </rich:tabPanel>
                        </h:panelGrid> <br/>
                        <h:panelGroup>
                        </h:panelGroup>
                    </center>
                </h:form>
            </center>
            <jsp:include page="../www/_bot.jsp"/>
        </f:view>
    </body-->
</html>