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
        <title>Consulta Administrador</title>
        <script type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }

            function SomenteNumero(e) {
                var tecla = (window.event) ? event.keyCode : e.which;
                if ((tecla > 47 && tecla < 58))
                    return true;
                else {
                    if (tecla == 8 || tecla == 0)
                        return true;
                    else
                        return false;
                }
            }
        </script>
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js"></script>
        <script type="text/javascript" src="../resources/jquery.maskedinput-1.2.2.js"></script>
    </head>
    <body>
        <center>
            <f:view >
                <a4j:keepAlive beanName="funcionarioBean"/>
                <a4j:keepAlive beanName="permissaoBean" />
                <a4j:keepAlive beanName="perfilBean"/>
                <a4j:keepAlive beanName="feriadoBean"/>
                <a4j:keepAlive beanName="horaExtraBean"/>
                <a4j:keepAlive beanName="departamentoBean"/>
                <a4j:keepAlive beanName="configuracoesBean"/>
                <a4j:keepAlive beanName="justificativaBean"/>
                <a4j:keepAlive beanName="categoriaAfastamentoBean"/>
                <a4j:keepAlive beanName="cargoBean"/>
                <a4j:keepAlive beanName="verbaBean"/>
                <a4j:keepAlive beanName="empresaBean"/>


                <jsp:include page="../www/_top.jsp"/>

                <h:form id="f_messagens" prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>

                <h:form id="form">                   
                    <h:panelGrid columns="1" >
                        <center> 
                            <rich:tabPanel switchType="client" width="965">
                                <rich:tab id="tabUsuarios1" label="Permissões" rendered="#{usuarioBean.perfil.permissoes== true}" >
                                    <a4j:support event="ontabenter" reRender="perfilManu"
                                                 action="#{permissaoBean.reConstroi}"/>
                                    <br/>
                                    <a4j:region id="region1">
                                        <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');"
                                                    onstop="#{rich:component('panelStatus')}.hide()"/>
                                        <rich:modalPanel id="panelStatus" autosized="true" >
                                            <h:panelGrid columns="3">
                                                <h:graphicImage url="../images/load.gif" />
                                                <rich:spacer width="8"/>
                                                <h:outputText value="  Carregando…" styleClass="label" />
                                            </h:panelGrid>
                                        </rich:modalPanel>
                                        <center>
                                            <h:outputText id="filtro" value="Pesquisar: " styleClass="label"/>
                                            <rich:spacer width="7"/>
                                            <h:inputText value="#{permissaoBean.filterValue}" id="input" size="40">
                                                <a4j:support event="onkeyup" reRender="permissaoList,datascrollers"
                                                             ignoreDupResponses="true" requestDelay="1100"
                                                             action="#{permissaoBean.filtrar}"/>
                                            </h:inputText>
                                        </center>
                                        <br/>
                                        <br/>
                                        <center>
                                            <rich:dataTable id="permissaoList"
                                                            value="#{permissaoBean.permissaoList}" var="linha"
                                                            rowClasses="zebra1,zebra2"
                                                            rows="25">
                                                <f:facet name="header">
                                                    <h:outputText value="LISTA DE FUNCIONÁRIOS"/>
                                                </f:facet>
                                                <rich:column sortBy="#{linha.cpf}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="CPF"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.cpf}" />
                                                </rich:column>
                                                <rich:column sortBy="#{linha.nome}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="NOME"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.nome}" />
                                                </rich:column>
                                                <rich:column sortBy="#{linha.departamento}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="DEPARTAMENTO"/>
                                                    </f:facet>
                                                    <h:outputText value="#{linha.departamento}" />
                                                </rich:column>
                                                <rich:column sortBy="#{linha.cod_regime}" style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="REGIME"/>
                                                    </f:facet>
                                                    <center>
                                                        <h:selectOneMenu id="regimeComboBox" value="#{linha.cod_regime}">
                                                            <f:selectItems value="#{permissaoBean.regimelist}"/>
                                                            <a4j:support  event="onchange" action="#{linha.atualizarRegime}"
                                                                          ajaxSingle="true"/>
                                                            <f:param name="regimesList" value="#{permissaoBean.regimelist}"/>
                                                        </h:selectOneMenu>
                                                    </center>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="PERMISSÃO"/>
                                                    </f:facet>
                                                    <center>

                                                        <h:selectOneMenu id="departManu" value="#{linha.dept_permissao}">
                                                            <f:selectItems value="#{permissaoBean.departamentolist}"/>
                                                            <a4j:support  event="onchange" action="#{linha.atualizarPermissao}" reRender="permissaoList"
                                                                          ajaxSingle="true"/>
                                                        </h:selectOneMenu>

                                                    </center>
                                                </rich:column>
                                                <rich:column style="text-align:center">
                                                    <f:facet name="header">
                                                        <h:outputText value="PERFIL"/>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid rendered="#{linha.dept_permissao != 0}">
                                                            <h:selectOneMenu id="perfilManu" value="#{linha.perfil}" >
                                                                <f:selectItems value="#{permissaoBean.perfillist}"/>
                                                                <a4j:support  event="onchange" action="#{linha.atualizarPerfil}"
                                                                              ajaxSingle="true"/>
                                                            </h:selectOneMenu>
                                                        </h:panelGrid>
                                                        <h:panelGrid rendered="#{linha.dept_permissao == 0}">
                                                            <h:outputText value="Comum"/>
                                                        </h:panelGrid>
                                                    </center>
                                                </rich:column>
                                            </rich:dataTable>
                                            <rich:datascroller  id="datascrollers"
                                                                for="permissaoList"
                                                                page="#{permissaoBean.page}"
                                                                ajaxSingle="true"
                                                                renderIfSinglePage="false">
                                            </rich:datascroller>
                                        </center>
                                    </a4j:region>
                                </rich:tab>

                                <rich:tab id="tabUsuarios2" label="Funcionários" rendered="#{usuarioBean.perfil.funcionarios== true}" >
                                    <a4j:support event="ontabenter" action="#{funcionarioBean.reConstroi}"
                                                 reRender="funcionarioTAdmin,transferirGroup,transferirFuncionarioPanel"/>
                                    <br/>
                                    <center>
                                        <rich:panel id="FuncionarioPanel">
                                            <a4j:region id="FuncionariosRegion">
                                                <h:panelGrid columns="4" >
                                                    <h:outputText value="Departamento: " styleClass="label"/>
                                                    <rich:spacer width="12"/>
                                                    <h:outputText value="Funcionário: " styleClass="label"/>
                                                    <rich:spacer width="12"/>

                                                    <h:selectOneMenu id="departManuTAdmin" value="#{funcionarioBean.departamento}">
                                                        <f:selectItems value="#{funcionarioBean.departamentolist}"/>
                                                        <a4j:support event="onchange" action="#{funcionarioBean.consultaFuncionario}"
                                                                     reRender="funcionarioTAdmin,transferirGroup"/>
                                                    </h:selectOneMenu>
                                                    <rich:spacer width="10"/>

                                                    <h:selectOneMenu id="funcionarioTAdmin" value="#{funcionarioBean.cod_funcionario}">
                                                        <f:selectItems value="#{funcionarioBean.funcionarioList}"/>
                                                        <a4j:support event="onchange"  action="#{funcionarioBean.consultaDetalhesFuncionario}"
                                                                     reRender="transferirGroup,BotoesGrid"
                                                                     />
                                                    </h:selectOneMenu>

                                                    <h:outputLink id="filtroFuncionarioFuncionario" value="#">
                                                        <center>
                                                            <h:graphicImage  value="../images/filtro.gif" style="border:0"/>
                                                        </center>
                                                        <rich:componentControl for="filtroFuncionarioFuncionarioModalPanel"
                                                                               attachTo="filtroFuncionarioFuncionario" operation="show" event="onclick"/>
                                                    </h:outputLink>

                                                    <h:panelGroup>
                                                        <h:selectBooleanCheckbox value="#{funcionarioBean.incluirSubSetores}">
                                                            <a4j:support event="onchange" action="#{funcionarioBean.consultaFuncionario}"
                                                                         reRender="funcionarioTAdmin,transferirGroup"/>
                                                        </h:selectBooleanCheckbox>
                                                        <h:outputText value=" Incluir subsetores" styleClass="italico"/>
                                                    </h:panelGroup>

                                                    <a4j:status id="progressoEmAberto"  for="FuncionariosRegion" onstart="Richfaces.showModalPanel('FuncionariosRegionPanelStatus');"
                                                                onstop="#{rich:component('FuncionariosRegionPanelStatus')}.hide()"/>
                                                    <rich:modalPanel id="FuncionariosRegionPanelStatus" autosized="true" >
                                                        <h:panelGrid columns="3">
                                                            <h:graphicImage url="../images/load.gif" />
                                                            <rich:spacer width="8"/>
                                                            <h:outputText value="  Carregando…" styleClass="label" />
                                                        </h:panelGrid>
                                                    </rich:modalPanel>
                                                    <rich:modalPanel id="filtroFuncionarioFuncionarioModalPanel" width="750" height="250"  style="text-align:center;float:center;" >
                                                        <f:facet name="header">
                                                            <h:panelGroup>
                                                                <h:outputText value="Filtrar funcionários"></h:outputText>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <f:facet name="controls">
                                                            <h:panelGroup>
                                                                <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink"/>
                                                                <rich:componentControl for="filtroFuncionarioFuncionarioModalPanel" attachTo="hidelink" operation="hide" event="onclick"/>
                                                            </h:panelGroup>
                                                        </f:facet>
                                                        <center>
                                                            <fieldset class="demo_fieldset" >
                                                                <legend style="font-weight: bold;">Por Cargo</legend>
                                                                <h:panelGroup>
                                                                    <h:selectOneMenu value="#{funcionarioBean.cargoSelecionadoOpcaoFiltroFuncionario}">
                                                                        <f:selectItems value="#{funcionarioBean.cargoOpcaoFiltroFuncionarioList}"/>
                                                                    </h:selectOneMenu>
                                                                </h:panelGroup>
                                                            </fieldset>
                                                            <br>
                                                            <fieldset class="demo_fieldset" >
                                                                <legend style="font-weight: bold;">Por Regime</legend>
                                                                <h:panelGroup >
                                                                    <h:selectOneRadio
                                                                        value="#{funcionarioBean.regimeSelecionadoOpcaoFiltroFuncionario}" >
                                                                        <f:selectItems
                                                                            value="#{funcionarioBean.regimeOpcaoFiltroFuncionarioList}"/>
                                                                    </h:selectOneRadio>
                                                                </h:panelGroup>
                                                            </fieldset>
                                                            <br>
                                                            <fieldset class="demo_fieldset" >
                                                                <legend style="font-weight: bold;">Por Gestor</legend>
                                                                <h:panelGroup>
                                                                    <h:selectOneRadio
                                                                        value="#{funcionarioBean.tipoGestorSelecionadoOpcaoFiltroFuncionario}" >
                                                                        <f:selectItems
                                                                            value="#{funcionarioBean.gestorFiltroFuncionarioList}"/>
                                                                    </h:selectOneRadio>
                                                                </h:panelGroup>
                                                            </fieldset>
                                                            <br> <br>
                                                            <h:commandButton value="Confirmar" id="confirmarOpcaoFiltroRegimeFuncionario"
                                                                             action="#{funcionarioBean.consultaFuncionario}">
                                                                <rich:componentControl for="filtroFuncionarioFuncionarioModalPanel" attachTo="confirmarOpcaoFiltroRegimeFuncionario"
                                                                                       operation="hide" event="onclick"/>
                                                            </h:commandButton>
                                                        </center>
                                                    </rich:modalPanel>
                                                </h:panelGrid>

                                            </a4j:region>
                                            <h:panelGroup>                                                
                                                <rich:modalPanel id="AddFuncModalPanel" width="650" height="560" styleClass="center">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="Novo Funcionário"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkFunc"/>
                                                            <rich:componentControl for="AddFuncModalPanel" attachTo="hidelinkFunc" operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid id="panelAddFunc" columns="1" >
                                                            <h:panelGrid columns="3" style="text-align:center;float:center">
                                                                <h:panelGrid id="newFuncionarioNome" columns="3" style="text-align:center;float:center"  >
                                                                    <h:outputText value="Nome: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:inputText  value="#{funcionarioBean.newFuncionario.nome}" size="35" maxlength="70"/>
                                                                </h:panelGrid>
                                                                <rich:spacer width="25"/>
                                                                <h:panelGrid id="newFuncionarioCPF" columns="3" style="text-align:right;float:right" >
                                                                    <h:outputText value="CPF: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:inputText id="newCPF" size="11" value="#{funcionarioBean.newFuncionario.cpf}"   >
                                                                        <rich:jQuery selector="#newCPF" query="mask('999.999.999-99')" timing="onload"/>
                                                                    </h:inputText>
                                                                </h:panelGrid>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="5" style="text-align:center;float:center">
                                                                <h:panelGrid id="newFuncionarioPIS" columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="PIS: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:inputText id="newPis" size="11" value="#{funcionarioBean.newFuncionario.PIS}"   >
                                                                        <rich:jQuery selector="#newPis" query="mask('999.99999.99/9')" timing="onload"/>
                                                                    </h:inputText>
                                                                </h:panelGrid>
                                                                <rich:spacer width="25"/>
                                                                <h:panelGrid id="newFuncionarioCont" columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="Matrícula: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:inputText id="newMat_emcs" size="5" value="#{funcionarioBean.newFuncionario.mat_emcs}" maxlength="8"
                                                                                 onkeypress="return returnNumber(event);"/>
                                                                </h:panelGrid>
                                                                <rich:spacer width="25"/>    
                                                                <h:panelGrid id="newFuncionarioMatricula" columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="PIN: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:inputText id="newMatricula1" size="5" value="#{funcionarioBean.newFuncionario.matricula}" maxlength="8" onkeypress='return SomenteNumero(event)'/>
                                                                </h:panelGrid>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                                <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="Sexo: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:selectOneMenu id="newFuncionarioSexo" value="#{funcionarioBean.newFuncionario.sexo}" >
                                                                        <f:selectItems value="#{funcionarioBean.sexosList}"/>
                                                                        <a4j:support event="onchange"  />
                                                                    </h:selectOneMenu>
                                                                </h:panelGrid>
                                                                <rich:spacer width="25"/>
                                                                <h:panelGrid id="newFuncionarioDataNascimento" columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="Data de Nascimento: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <rich:calendar inputSize="8" datePattern="dd/MM/yyyy"
                                                                                   locale="#{funcionarioBean.objLocale}"
                                                                                   value="#{funcionarioBean.newFuncionario.dataNascimento}"
                                                                                   oninputkeypress="return ValidarNumero(this,event)"
                                                                                   oninputkeyup="mascara_data(this,event)"
                                                                                   enableManualInput="true"/>
                                                                </h:panelGrid>
                                                                <rich:spacer width="25"/>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                                <h:outputText value="Departamento: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:selectOneMenu id="newDepartamento" value="#{funcionarioBean.newFuncionario.cod_dept}">
                                                                    <f:selectItems value="#{funcionarioBean.departamentolist}"/>
                                                                </h:selectOneMenu>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                                <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                                    <h:outputText value="Cargo: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="3"/>
                                                                    <h:selectOneMenu id="newFuncionarioCargo" value="#{funcionarioBean.newFuncionario.cargo}" >
                                                                        <f:selectItems value="#{funcionarioBean.cargosList}"/>
                                                                        <a4j:support event="onchange"  action="#{funcionarioBean.nada}" />
                                                                    </h:selectOneMenu>
                                                                </h:panelGrid>
                                                            </h:panelGrid>
                                                            <h:panelGrid id="newFuncionarioDataContratação" columns="3" style="text-align:center;float:center" >
                                                                <h:outputText value="Data de Contratação: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <rich:calendar inputSize="8" datePattern="dd/MM/yyyy" locale="#{funcionarioBean.objLocale}" value="#{funcionarioBean.newFuncionario.dataContratação}"
                                                                               oninputkeypress="return ValidarNumero(this,event)"
                                                                               oninputkeyup="mascara_data(this,event)"
                                                                               enableManualInput="true"/>
                                                            </h:panelGrid>
                                                            <h:panelGrid id="newFuncionariodaRegime" columns="3" style="text-align:center;float:center" >
                                                                <h:outputText value="Regime: " styleClass="label" style="float:right"/>
                                                                <h:selectOneMenu id="newRegimeComboBox" value="#{funcionarioBean.newFuncionario.cod_regime}">
                                                                    <f:selectItems value="#{funcionarioBean.regimelist}"/>
                                                                </h:selectOneMenu>
                                                            </h:panelGrid>

                                                            <center>
                                                                <h:panelGrid columns="5" >
                                                                    <h:panelGrid  columns="3" >
                                                                        <h:outputText value="Ativo: " styleClass="label" style="float:right"/>
                                                                        <rich:spacer width="2"/>
                                                                        <h:selectBooleanCheckbox value="#{funcionarioBean.newFuncionario.isAtivo}"/>
                                                                    </h:panelGrid>
                                                                    <rich:spacer width="15"/>
                                                                    <h:panelGrid id="newSucetivelAFeriadoGrid" columns="3" style="text-align:center;float:center" >
                                                                        <h:outputText value="Folga em feriado: " styleClass="label" style="float:right"/>
                                                                        <rich:spacer width="2"/>
                                                                        <h:selectBooleanCheckbox value="#{funcionarioBean.newFuncionario.sucetivelAFeriado}"/>
                                                                    </h:panelGrid>
                                                                    <rich:spacer width="15"/>
                                                                    <h:panelGrid id="newAcessoLivreCatraca" columns="3" style="text-align:center;float:center" >
                                                                        <h:outputText value="Acesso Livre (Catracas): " styleClass="label" style="float:right"/>
                                                                        <rich:spacer width="2"/>
                                                                        <h:selectBooleanCheckbox value="#{funcionarioBean.newFuncionario.livreAcesso}"/>
                                                                    </h:panelGrid>
                                                                </h:panelGrid>
                                                            </center>
                                                        </h:panelGrid>
                                                        <br>
                                                        <br>
                                                        <center>
                                                            <h:commandButton value="Salvar" id="addFunc" action="#{funcionarioBean.addNewFunc}">
                                                                <rich:componentControl for="AddFuncModalPanel" attachTo="addFunc" operation="hide" event="onclick"/>
                                                            </h:commandButton>
                                                        </center>
                                                    </center>
                                                </rich:modalPanel>
                                                <center>
                                                    <h:outputLink  value="#" id="link">
                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                        <rich:componentControl for="AddFuncModalPanel" attachTo="link" operation="show" event="onclick"/>
                                                        <a4j:support action="#{funcionarioBean.createNewFunc}"
                                                                     event="onclick"
                                                                     reRender="panelAddFunc">
                                                        </a4j:support>
                                                    </h:outputLink>
                                                    <br/>
                                                    Adicionar
                                                </center>
                                            </h:panelGroup>

                                            <br><br>
                                            <h:panelGrid id="transferirGroup" width="53%" >

                                                <rich:panel id="funcionariosPanel" rendered="#{funcionarioBean.cod_funcionario != null&&
                                                                                               funcionarioBean.cod_funcionario != 0&&funcionarioBean.cod_funcionario != -1}"  >
                                                    <h:panelGrid id="transferirGrid" columns="1"  >
                                                        <h:panelGrid columns="3" style="text-align:center;float:center">
                                                            <h:panelGrid id="dadosFuncionarionome" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Nome: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText  value="#{funcionarioBean.funcionario.nome}" size="35" maxlength="70"/>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>
                                                            <h:panelGrid id="dadosFuncionariocpf" columns="3" style="text-align:right;float:right" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="CPF: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText id="cpf" size="11" value="#{funcionarioBean.funcionario.cpf}"   >
                                                                    <rich:jQuery selector="#cpf" query="mask('999.999.999-99')" timing="onload"/>
                                                                </h:inputText>
                                                            </h:panelGrid>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="5" style="text-align:center;float:center">
                                                            <h:panelGrid id="dadosFuncionarioPIS" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="PIS: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText id="pis" size="12" value="#{funcionarioBean.funcionario.PIS}"   >
                                                                    <rich:jQuery selector="#pis" query="mask('999.99999.99/9')" timing="onload"/>
                                                                </h:inputText>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>
                                                            <h:panelGrid id="dadosFuncionarioCont" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Matrícula: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText id="mat_emcs" size="8" value="#{funcionarioBean.funcionario.mat_emcs}" maxlength="10"
                                                                             onkeypress="return returnNumber(event);"/>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>    
                                                            <h:panelGrid id="dadosFuncionariomatricula" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="PIN: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText id="matricula1" size="8" value="#{funcionarioBean.funcionario.matricula}" maxlength="10" onkeypress='return SomenteNumero(event)' />
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>

                                                            <h:panelGrid styleClass="hide" id="dadosFuncionariocracha" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Crachá: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText id="cracha" size="5" value="#{funcionarioBean.funcionario.cracha}" maxlength="8"
                                                                             onkeypress="return returnNumber(event);"/>
                                                            </h:panelGrid>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                            <h:panelGrid columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Sexo: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:selectOneMenu id="dadosFuncionariosexo" value="#{funcionarioBean.funcionario.sexo}" >
                                                                    <f:selectItems value="#{funcionarioBean.sexosList}"/>
                                                                    <a4j:support event="onchange"  />
                                                                </h:selectOneMenu>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>
                                                            <h:panelGrid id="dadosFuncionariodataNascimento" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Data de Nascimento: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <rich:calendar inputSize="8" datePattern="dd/MM/yyyy"
                                                                               locale="#{funcionarioBean.objLocale}"
                                                                               value="#{funcionarioBean.funcionario.dataNascimento}"
                                                                               oninputkeypress="return ValidarNumero(this,event)"
                                                                               oninputkeyup="mascara_data(this,event)"
                                                                               enableManualInput="true"/>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>
                                                        </h:panelGrid>
                                                        <h:panelGrid columns="3" style="text-align:center;float:center" >
                                                            <h:panelGrid columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Cargo: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <h:selectOneMenu id="dadosFuncionariocargo" value="#{funcionarioBean.funcionario.cargo}" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                    <f:selectItems value="#{funcionarioBean.cargosList}"/>
                                                                    <a4j:support event="onchange"  action="#{funcionarioBean.nada}" />
                                                                </h:selectOneMenu>
                                                            </h:panelGrid>
                                                            <rich:spacer width="25"/>
                                                            <h:panelGrid id="dadosFuncionariodataContratação" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                <h:outputText value="Data de Contratação: " styleClass="label" style="float:right"/>
                                                                <rich:spacer width="3"/>
                                                                <rich:calendar inputSize="8" datePattern="dd/MM/yyyy" locale="#{funcionarioBean.objLocale}" value="#{funcionarioBean.funcionario.dataContratação}"
                                                                               oninputkeypress="return ValidarNumero(this,event)"
                                                                               oninputkeyup="mascara_data(this,event)"
                                                                               enableManualInput="true"/>
                                                            </h:panelGrid>
                                                        </h:panelGrid>
                                                        <h:panelGrid id="dadosFuncionariodaRegime" columns="5" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                            <h:outputText value="Regime: " styleClass="label" style="float:right"/>
                                                            <h:selectOneMenu id="regimeComboBox" value="#{funcionarioBean.funcionario.cod_regime}">
                                                                <f:selectItems value="#{funcionarioBean.regimelist}"/>
                                                                <a4j:support  event="onchange" action="#{funcionario.nada}"
                                                                              ajaxSingle="true"/>
                                                            </h:selectOneMenu>

                                                            <rich:spacer width="25"/>

                                                            <h:outputText value="Logon AD: " styleClass="label" style="float:right" rendered="#{funcionarioBean.funcionario.ADUsername != null}"/>
                                                            <h:outputText value="#{funcionarioBean.funcionario.ADUsername} " style="float:right" rendered="#{funcionarioBean.funcionario.ADUsername != null}"/>
                                                        </h:panelGrid>

                                                        <center>
                                                            <h:panelGrid columns="5" >
                                                                <h:panelGrid  columns="3" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                    <h:outputText value="Ativo: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="2"/>
                                                                    <h:selectBooleanCheckbox value="#{funcionarioBean.funcionario.isAtivo}"/>
                                                                </h:panelGrid>
                                                                <rich:spacer width="15"/>
                                                                <h:panelGrid id="sucetivelAFeriadoGrid" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                    <h:outputText value="Folga em feriado: " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="2"/>
                                                                    <h:selectBooleanCheckbox value="#{funcionarioBean.funcionario.sucetivelAFeriado}"/>
                                                                </h:panelGrid>
                                                                <rich:spacer width="15"/>
                                                                <h:panelGrid id="AcessoLivreCatraca" columns="3" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}"  >
                                                                    <h:outputText value="Acesso Livre (Catracas): " styleClass="label" style="float:right"/>
                                                                    <rich:spacer width="2"/>
                                                                    <h:selectBooleanCheckbox value="#{funcionarioBean.funcionario.livreAcesso}"/>
                                                                </h:panelGrid>
                                                            </h:panelGrid>
                                                        </center>
                                                        <br/>
                                                        <h:panelGrid width="100%" columns="4" style="text-align:center;float:center"
                                                                     rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}">
                                                            <h:panelGrid columns="1">    
                                                                <center>
                                                                    <a4j:commandButton id="btZerarSenha" value="Formatar Senha"
                                                                                       image="../images/senha.png"
                                                                                       action="#{funcionarioBean.zerarSenha}"
                                                                                       onclick="javascript:if (!confirm('Deseja formatar senha?')) return false;"
                                                                                       >
                                                                    </a4j:commandButton>
                                                                    <h:outputText value="Formatar Senha"/>
                                                                </center>
                                                            </h:panelGrid>

                                                            <h:panelGrid columns="1" rendered="#{not empty empresaBean.ipDigitCatcher}">    
                                                                <center>
                                                                    <h:outputLink id="fingerlink" value="#{empresaBean.fullNameIpCatcher}" 
                                                                                  target="_blank">
                                                                        <h:graphicImage value="../images/fingerprint_scan.png" />
                                                                        <a4j:support event="onclick" action="#{funcionarioBean.insertFingerPrint}"/>
                                                                    </h:outputLink>
                                                                    <h:outputText value="Cadastrar Digitais"/>
                                                                </center>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="1">    
                                                                <center>
                                                                    <a4j:commandButton id="btExcluirFuncionario" value="Excluir Funcionário"
                                                                                       image="../images/negado.png"
                                                                                       action="#{funcionarioBean.excluirFuncionario}"
                                                                                       onclick="javascript:if (!confirm('Deseja excluir o funcionário?')) return false;"
                                                                                       reRender="transferirGroup, FuncionarioPanel">
                                                                    </a4j:commandButton>
                                                                    <h:outputText value="Excluir Funcionário"/>
                                                                </center>
                                                            </h:panelGrid>
                                                            <h:panelGrid columns="1" rendered="#{funcionarioBean.funcionario.ADUsername != null}">    
                                                                <center>
                                                                    <a4j:commandButton id="btUnlinkAD" value="Desfazer ligação com o AD"
                                                                                       image="../images/off.png"
                                                                                       action="#{funcionarioBean.resetADLink}"
                                                                                       onclick="javascript:if (!confirm('Deseja desfazer a ligação com AD?')) return false;"
                                                                                       >
                                                                    </a4j:commandButton>
                                                                    <h:outputText value="Desfazer ligação do AD"/>
                                                                </center>
                                                            </h:panelGrid>

                                                        </h:panelGrid>
                                                    </h:panelGrid>
                                                </rich:panel>
                                                <br>
                                                <h:panelGroup>
                                                    <center>
                                                        <h:panelGrid columns="1" id="BotoesGrid" >
                                                            <h:panelGrid columns="5">
                                                                <h:panelGrid columns="1" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&
                                                                                                                                            funcionarioBean.cod_funcionario != 0&&funcionarioBean.cod_funcionario != -1}">
                                                                             <center>
                                                                                 <h:outputLink  value="#" id="transferirFuncionarioLink" style="float:center" >
                                                                                     <h:graphicImage value="../images/muda_depto_48.png" style="border:0"/>
                                                                                     <rich:componentControl for="transferirFuncionarioPanel" attachTo="transferirFuncionarioLink" operation="show" event="onclick"/>
                                                                                     <a4j:support event="onclick"  action="#{funcionarioBean.consultaDepartamentoDestino}"/>
                                                                                 </h:outputLink>
                                                                                 <h:outputText value="Transferir" styleClass="label"/>
                                                                             </center>
                                                                </h:panelGrid>


                                                                <rich:spacer width="10" rendered="#{not empty empresaBean.ipDigitSender}"/>
                                                                <h:panelGrid columns="1" style="text-align:center;float:center" rendered="#{not empty empresaBean.ipDigitSender}">    
                                                                    <center>
                                                                        <h:outputLink id="senderlink" value="#{empresaBean.fullNameIpSender}" 
                                                                                      target="_blank" style="float:center" >
                                                                            <h:graphicImage value="../images/pontoClock.png" style="border:0"/>
                                                                        </h:outputLink>
                                                                        <h:outputText value="Enviar aos Relógios" styleClass="label"/>
                                                                    </center>
                                                                </h:panelGrid>


                                                                <rich:spacer width="10"/>
                                                                <h:panelGrid columns="1" style="text-align:center;float:center" rendered="#{funcionarioBean.cod_funcionario != null&&
                                                                                                                                            funcionarioBean.cod_funcionario != 0&&funcionarioBean.cod_funcionario != -1}">
                                                                             <center>
                                                                                 <h:outputLink  value="#" id="salvarFuncionarioLink" style="float:center" rendered="#{funcionarioBean.cod_funcionario != null&&funcionarioBean.cod_funcionario != 0}">
                                                                                     <h:graphicImage value="../images/salvar_48.png" style="border:0"/>
                                                                                     <a4j:support event="onclick"  action="#{funcionarioBean.salvarFuncionarioAlteracoes}"/>
                                                                                     <br>
                                                                                 </h:outputLink>
                                                                                 <h:outputText value="Salvar" styleClass="label"/>
                                                                             </center>
                                                                </h:panelGrid>


                                                            </h:panelGrid>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGroup>

                                            </h:panelGrid>

                                            <rich:modalPanel id="transferirFuncionarioPanel" width="300" height="150" autosized="true" styleClass="center">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Transferir Usuário"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink3"/>
                                                        <rich:componentControl for="transferirFuncionarioPanel" attachTo="hidelink3" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <center>
                                                        <br>
                                                        <h:selectOneMenu id="departManuTAdminDestino" value="#{funcionarioBean.departamentoDestino}">
                                                            <f:selectItems value="#{funcionarioBean.departamentolistDestino}"/>
                                                        </h:selectOneMenu>
                                                        <br><br><br>
                                                        <h:commandButton  value="Salvar" id="funcionarioTransferenciaSalvar"
                                                                          action="#{funcionarioBean.salvarFuncionarioTransferencia}">
                                                            <rich:componentControl for="transferirFuncionarioPanel" attachTo="funcionarioTransferenciaSalvar"
                                                                                   operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </center>
                                            </rich:modalPanel>
                                            <br/>
                                        </rich:panel>
                                    </center>
                                </rich:tab> 
                                <rich:tab id="tabUsuarios3" label="Departamentos" rendered="#{usuarioBean.perfil.deptos== true}" >
                                    <br/>
                                    <a4j:region id="DepartamentosRegion">
                                        <center>
                                            <h:panelGrid columns="3" >
                                                <h:outputText value="Departamento: " styleClass="label"/>
                                                <rich:spacer width="10"/>
                                                <h:selectOneMenu id="editDepartamento" value="#{departamentoBean.departamentoSelecionado}">
                                                    <f:selectItems value="#{departamentoBean.departamentolist}"/>
                                                    <a4j:support event="onchange"
                                                                 reRender="BotaoAddDepartamento,BotaoRenomear,BotaoExcluir"/>
                                                </h:selectOneMenu>

                                                <a4j:status id="progressoEmAberto3"  for="DepartamentosRegion" onstart="Richfaces.showModalPanel('DepartamentosRegionPanelStatus');"
                                                            onstop="#{rich:component('DepartamentosRegionPanelStatus')}.hide()"/>
                                                <rich:modalPanel id="DepartamentosRegionPanelStatus" autosized="true" >
                                                    <h:panelGrid columns="3">
                                                        <h:graphicImage url="../images/load.gif" />
                                                        <rich:spacer width="8"/>
                                                        <h:outputText value="  Carregando…" styleClass="label" />
                                                    </h:panelGrid>
                                                </rich:modalPanel>
                                            </h:panelGrid>
                                        </center>

                                        <center>
                                            <h:panelGrid id="BotoesDepartamento" columns="5">
                                                <h:panelGrid id="BotaoAddDepartamento" style="text-align:center;float:center" width="50">
                                                    <center>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     >
                                                            <center>
                                                                <h:outputLink  value="#" id="linkAddDepartamento" style="float:center">
                                                                    <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                    <rich:componentControl for="addDepartamentoPanel" attachTo="linkAddDepartamento" operation="show" event="onclick"/>
                                                                    <a4j:support event="onclick" reRender="addDepartamentoGrid"
                                                                                 action="#{departamentoBean.showAdicionar}"/>
                                                                </h:outputLink>
                                                                <h:outputText value="Departamento" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGrid>
                                                <rich:spacer width="8"/>
                                                <h:panelGrid id="BotaoRenomear" style="text-align:center;float:center" width="50">
                                                    <center>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center">
                                                            <center>
                                                                <h:outputLink  value="#" id="linkRenomearDepartamento" style="float:center">
                                                                    <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                    <rich:componentControl for="renomearDepartamentoPanel" attachTo="linkRenomearDepartamento" operation="show" event="onclick"/>
                                                                    <a4j:support event="onclick" action = "#{departamentoBean.showEditar}" reRender="editDepartamentoGrid"/>
                                                                </h:outputLink>
                                                                <h:outputText value="Editar" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGrid>
                                                <rich:spacer width="8"/>
                                                <h:panelGrid id="BotaoExcluir" style="text-align:center;float:center" width="50">
                                                    <center>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"         >
                                                            <center>
                                                                <h:commandButton  value="Excluir" id="excluirDepartamento" image="../images/delete_24.png"
                                                                                  onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                  action="#{departamentoBean.excluirDepartamento}"/>
                                                                <h:outputText value="Excluir" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGrid>
                                            </h:panelGrid>
                                            <rich:modalPanel id="addDepartamentoPanel" width="300" height="150" autosized="true" styleClass="center">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Novo Departamento"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink7"/>
                                                        <rich:componentControl for="addDepartamentoPanel" attachTo="hidelink7" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id = "addDepartamentoGrid">
                                                        <center>
                                                            <br>
                                                            <h:outputText value="Nome do departamento: " styleClass="label"/>
                                                            <h:inputText id="novoDeptID" value="#{departamentoBean.deptoNovo.nomeDepartamento}" size="50" maxlength="30"/>
                                                            <br>
                                                            <h:panelGrid id = "addSuperDepartamentoGrid" rendered="#{not empty departamentoBean.departamentolist}">
                                                                <h:outputText value="Departamento a ser alocado: " styleClass="label"/>
                                                                <h:selectOneMenu id="departAlocPai" value="#{departamentoBean.deptoNovo.superDeptoId}">
                                                                    <f:selectItems value="#{departamentoBean.departamentolist}"/>
                                                                </h:selectOneMenu>
                                                                <br>
                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton value="Salvar" id="salvarNovoDepartamento"
                                                                     action="#{departamentoBean.salvarNovoDepartamento}"                                                                         >

                                                        <rich:componentControl for="addDepartamentoPanel"  attachTo="salvarNovoDepartamento"
                                                                               operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </rich:modalPanel>
                                            <rich:modalPanel id="renomearDepartamentoPanel" width="300" height="150" autosized="true" styleClass="center">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Editar Departamento"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelink8"/>
                                                        <rich:componentControl for="renomearDepartamentoPanel" attachTo="hidelink8" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id = "editDepartamentoGrid">
                                                        <center>
                                                            <br>
                                                            <h:outputText value="Nome do departamento: " styleClass="label"/>
                                                            <h:inputText id="editDeptID" value="#{departamentoBean.deptoEdit.nomeDepartamento}" size="50" maxlength="30"/>
                                                            <br>
                                                            <h:panelGrid id = "editSuperDepartamentoGrid" rendered="#{not empty departamentoBean.superDepartamentolist}">
                                                                <h:outputText value="Departamento a ser alocado: " styleClass="label"/>
                                                                <h:selectOneMenu id="departAlocPaiEdit" value="#{departamentoBean.deptoEdit.superDeptoId}">
                                                                    <f:selectItems value="#{departamentoBean.superDepartamentolist}"/>
                                                                </h:selectOneMenu>
                                                                <br>
                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton  value="Salvar" id="salvarEditDepartamento"
                                                                      action="#{departamentoBean.salvarEditDepartamento}"                                                                         >

                                                        <rich:componentControl for="renomearDepartamentoPanel"  attachTo="salvarEditDepartamento"
                                                                               operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </rich:modalPanel>
                                        </center>
                                    </a4j:region>
                                </rich:tab>
                                <rich:tab id="empresasTab" label="Empresas" rendered="#{usuarioBean.perfil.empresas == true}">
                                    <br/>
                                    <a4j:region id="empresasRegion">
                                        <rich:panel id="painelEmpresas">
                                            <center>

                                                <h:panelGrid columns="3" >
                                                    <h:outputText value="Empresa: " styleClass="label"/>
                                                    <rich:spacer width="10"/>
                                                    <h:selectOneMenu id="listEmpresas" value="#{empresaBean.empresaSelecionada}">
                                                        <f:selectItems value="#{empresaBean.empresaList}"/>
                                                        <a4j:support event="onchange" action="#{empresaBean.consultaDetalhesEmpresa}"
                                                                     reRender="showEmpresasPanel,editEmpresaOutputPanel"/>
                                                    </h:selectOneMenu>

                                                    <a4j:status id="progressoEmAberto4"  for="empresasRegion" onstart="Richfaces.showModalPanel('empresasRegionPanelStatus');"
                                                                onstop="#{rich:component('empresasRegionPanelStatus')}.hide()"/>
                                                    <rich:modalPanel id="empresasRegionPanelStatus" autosized="true" >
                                                        <h:panelGrid columns="3">
                                                            <h:graphicImage url="../images/load.gif" />
                                                            <rich:spacer width="8"/>
                                                            <h:outputText value="  Carregando…" styleClass="label" />
                                                        </h:panelGrid>
                                                    </rich:modalPanel>
                                                </h:panelGrid>
                                                <h:panelGrid id="BotoesEmpresa" columns="5">
                                                    <h:panelGrid id="BotaoAddEmpresa" style="text-align:center;float:center" width="50">
                                                        <center>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                         >
                                                                <center>
                                                                    <h:outputLink  value="#" id="linkAddEmpresa" style="float:center">
                                                                        <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                        <rich:componentControl for="addEmpresaPanel" attachTo="linkAddEmpresa" operation="show" event="onclick"/>
                                                                        <a4j:support event="onclick" reRender="addEmpresaGrid"
                                                                                     action="#{empresaBean.showAdicionar}"/>
                                                                    </h:outputLink>
                                                                    <h:outputText value="Empresa" styleClass="label"/>
                                                                </center>
                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGrid>
                                                    <rich:spacer width="8"/>
                                                    <h:panelGrid id="BotaoEditarEmpresa" style="text-align:center;float:center" width="50">
                                                        <center>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center">
                                                                <a4j:outputPanel id="editEmpresaOutputPanel">
                                                                    <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                 rendered="#{empresaBean.empresaSelecionada != -1 && empresaBean.empresaSelecionada != null}">
                                                                        <center>
                                                                            <h:outputLink  value="#" id="linkEditarEmpresa" style="float:center">
                                                                                <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                                <rich:componentControl for="editarEmpresaPanel" attachTo="linkEditarEmpresa" operation="show" event="onclick"/>
                                                                                <a4j:support event="onclick" action = "#{empresaBean.showEditar}" reRender="editEmpresaGrid"/>
                                                                            </h:outputLink>
                                                                            <h:outputText value="Editar" styleClass="label"/>
                                                                        </center>
                                                                    </h:panelGrid>
                                                                    <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                 rendered="#{empresaBean.empresaSelecionada == -1 || empresaBean.empresaSelecionada == null}">
                                                                        <center>
                                                                            <h:graphicImage value="../images/edit_dent_fosco.png" style="border:0"/>
                                                                            <h:outputText value="Editar" styleClass="label"/>
                                                                        </center>
                                                                    </h:panelGrid>
                                                                </a4j:outputPanel>

                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGrid>
                                                    <rich:spacer width="8"/>
                                                    <h:panelGrid id="BotaoExcluirEmpresa" style="text-align:center;float:center" width="50">
                                                        <a4j:outputPanel id="deleteEmpresaOutputPanel">
                                                            <center>
                                                                <h:panelGrid columns="1" style="text-align:center;float:center" 
                                                                             rendered="#{empresaBean.empresaSelecionada != -1 && empresaBean.empresaSelecionada != null}">
                                                                    <center>
                                                                        <h:commandButton  value="Excluir" id="excluirEmpresa" image="../images/delete_24.png"
                                                                                          onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                          action="#{empresaBean.excluirEmpresa}"/>
                                                                        <h:outputText value="Excluir" styleClass="label"/>
                                                                    </center>
                                                                </h:panelGrid>

                                                                <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                             rendered="#{empresaBean.empresaSelecionada == -1 || empresaBean.empresaSelecionada == null}">
                                                                    <center>
                                                                        <h:graphicImage value="../images/delete_transp_24.png" style="border:0"/>
                                                                        <h:outputText value="Excluir" styleClass="label"/>
                                                                    </center>
                                                                </h:panelGrid>
                                                            </center>
                                                        </a4j:outputPanel>
                                                    </h:panelGrid>
                                                </h:panelGrid>
                                                <rich:modalPanel id="addEmpresaPanel" width="400" height="150" autosized="true" styleClass="center">

                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="Nova Empresa"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkEmpresa"/>
                                                            <rich:componentControl for="addEmpresaPanel" attachTo="hidelinkEmpresa" operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid id = "addEmpresaGrid" columns="3" style="text-align:center;float:center">

                                                            <h:outputText value="CNPJ: " styleClass="label" style="float:left"/>
                                                            <rich:spacer width="3"/>
                                                            <h:inputText id="itEmpresaCnpj" size="40" value="#{empresaBean.empresa.cnpj}">
                                                                <rich:jQuery selector="#itEmpresaCnpj" query="mask('99.999.999/9999-99')" timing="onload"/>
                                                            </h:inputText>

                                                            <h:outputText value="Razão Social: " styleClass="label" style="float:left"/>
                                                            <rich:spacer width="3"/>
                                                            <h:inputText id="itEmpresaRazaoSocial" size="40" value="#{empresaBean.empresa.razaoSocial}"/>

                                                            <h:outputText value="Endereço: " styleClass="label" style="float:left"/>
                                                            <rich:spacer width="3"/>
                                                            <h:inputText id="itEmpresaAddress" size="40" value="#{empresaBean.empresa.address}"/>

                                                            <h:outputText value="Cei: " styleClass="label" style="float:left"/>
                                                            <rich:spacer width="3"/>
                                                            <h:inputText id="itEmpresCei" maxlength="14" value="#{empresaBean.empresa.cei}" style="float:left"/>

                                                        </h:panelGrid>
                                                        <br>
                                                        <h:commandButton  value="Salvar" id="salvarNovaEmpresa"
                                                                          action="#{empresaBean.salvar}"                                                                         >

                                                            <rich:componentControl for="addEmpresaPanel"  attachTo="salvarNovaEmpresa"
                                                                                   operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>

                                                </rich:modalPanel>
                                                <h:panelGrid id="showEmpresasPanel">
                                                    <rich:panel rendered="#{empresaBean.empresaSelecionada != '-1'}">
                                                        <h:panelGrid>
                                                            <h:panelGrid id="dadosEmpresaRazaoSocial" columns="3" style="text-align:center;float:center" >
                                                                <h:outputText value="Razão Social: " styleClass="label" style="float:left"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText size="40" value="#{empresaBean.empresa.razaoSocial}"/>


                                                                <h:outputText value="CNPJ: " styleClass="label" style="float:left"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText size="40" value="#{empresaBean.empresa.cnpj}"/>


                                                                <h:outputText value="Endereço: " styleClass="label" style="float:left"/>
                                                                <rich:spacer width="3"/>
                                                                <h:inputText size="40" value="#{empresaBean.empresa.address}"/>

                                                                <h:outputText value="Cei: " styleClass="label" style="float:left"/>
                                                                <rich:spacer width="3"/>
                                                                <h:outputText value="#{empresaBean.empresa.cei}" style="text-align:left;float:left"/>





                                                            </h:panelGrid>



                                                            <br/>
                                                            <h:panelGrid columns="1">
                                                                <a4j:commandButton value="Salvar" action="#{empresaBean.salvar}" reRender="painelEmpresas"/> 
                                                            </h:panelGrid>



                                                            <rich:spacer width="25"/>
                                                        </h:panelGrid>


                                                    </rich:panel>

                                                </h:panelGrid>
                                            </center>
                                        </rich:panel>
                                    </a4j:region>
                                </rich:tab>
                                <rich:tab id="cargosTab" label = "Cargos" rendered="#{usuarioBean.perfil.cargos== true}">
                                    <br/>
                                    <a4j:region id="cargosRegion">
                                        <center>
                                            <h:panelGrid columns="3" >
                                                <h:outputText value="Cargo: " styleClass="label"/>
                                                <rich:spacer width="10"/>

                                                <h:selectOneMenu id="editCargo" value="#{cargoBean.cargoSelecionado}">
                                                    <f:selectItems value="#{cargoBean.cargolist}"/>
                                                    <a4j:support event="onchange"
                                                                 reRender="BotaoAddcargo,editCargoOutputPanel,deleteCargoOutputPanel"/>
                                                </h:selectOneMenu>

                                                <a4j:status id="progressoEmAberto5"  for="cargosRegion" onstart="Richfaces.showModalPanel('cargosRegionPanelStatus');"
                                                            onstop="#{rich:component('cargosRegionPanelStatus')}.hide()"/>
                                                <rich:modalPanel id="cargosRegionPanelStatus" autosized="true" >
                                                    <h:panelGrid columns="3">
                                                        <h:graphicImage url="../images/load.gif" />
                                                        <rich:spacer width="8"/>
                                                        <h:outputText value="  Carregando…" styleClass="label" />
                                                    </h:panelGrid>
                                                </rich:modalPanel>
                                            </h:panelGrid>
                                        </center>

                                        <center>
                                            <h:panelGrid id="BotoesCargo" columns="5">
                                                <h:panelGrid id="BotaoAddcargo" style="text-align:center;float:center" width="50">
                                                    <center>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                     >
                                                            <center>
                                                                <h:outputLink  value="#" id="linkAddCargo" style="float:center">
                                                                    <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                                    <rich:componentControl for="addCargoPanel" attachTo="linkAddCargo" operation="show" event="onclick"/>
                                                                    <a4j:support event="onclick" reRender="addCargoGrid"
                                                                                 action="#{cargoBean.showAdicionar}"/>
                                                                </h:outputLink>
                                                                <h:outputText value="Cargo" styleClass="label"/>
                                                            </center>
                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGrid>
                                                <rich:spacer width="8"/>
                                                <h:panelGrid id="BotaoRenomearCargo" style="text-align:center;float:center" width="50">
                                                    <center>
                                                        <h:panelGrid columns="1" style="text-align:center;float:center">
                                                            <a4j:outputPanel id="editCargoOutputPanel">
                                                                <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                             rendered="#{cargoBean.cargoSelecionado != -1&&cargoBean.cargoSelecionado != null}">
                                                                    <center>
                                                                        <h:outputLink  value="#" id="linkRenomearCargo" style="float:center">
                                                                            <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                            <rich:componentControl for="renomearCargoPanel" attachTo="linkRenomearCargo" operation="show" event="onclick"/>
                                                                            <a4j:support event="onclick" action = "#{cargoBean.showEditar}" reRender="editCargoGrid"/>
                                                                        </h:outputLink>
                                                                        <h:outputText value="Editar" styleClass="label"/>
                                                                    </center>
                                                                </h:panelGrid>
                                                                <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                             rendered="#{cargoBean.cargoSelecionado == -1||cargoBean.cargoSelecionado == null}">
                                                                    <center>
                                                                        <h:graphicImage value="../images/edit_dent_fosco.png" style="border:0"/>
                                                                        <h:outputText value="Editar" styleClass="label"/>
                                                                    </center>
                                                                </h:panelGrid>
                                                            </a4j:outputPanel>

                                                        </h:panelGrid>
                                                    </center>
                                                </h:panelGrid>
                                                <rich:spacer width="8"/>
                                                <h:panelGrid id="BotaoExcluirCargo" style="text-align:center;float:center" width="50">
                                                    <a4j:outputPanel id="deleteCargoOutputPanel">
                                                        <center>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center"         rendered="#{cargoBean.cargoSelecionado != -1&&cargoBean.cargoSelecionado != null}">
                                                                <center>
                                                                    <h:commandButton  value="Excluir" id="excluirCargo" image="../images/delete_24.png"
                                                                                      onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                      action="#{cargoBean.excluirCargo}"/>
                                                                    <h:outputText value="Excluir" styleClass="label"/>
                                                                </center>
                                                            </h:panelGrid>

                                                            <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                         rendered="#{cargoBean.cargoSelecionado == -1||cargoBean.cargoSelecionado == null}">
                                                                <center>
                                                                    <h:graphicImage value="../images/delete_transp_24.png" style="border:0"/>
                                                                    <h:outputText value="Excluir" styleClass="label"/>
                                                                </center>
                                                            </h:panelGrid>
                                                        </center>
                                                    </a4j:outputPanel>
                                                </h:panelGrid>
                                            </h:panelGrid>
                                            <rich:modalPanel id="addCargoPanel" width="300" height="150" autosized="true" styleClass="center">

                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Novo Cargo"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkc9"/>
                                                        <rich:componentControl for="addCargoPanel" attachTo="hidelinkc9" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id = "addCargoGrid">
                                                        <center>
                                                            <br>
                                                            <h:outputText value="Nome do cargo: " styleClass="label"/>
                                                            <h:inputText id="novoCargoID" value="#{cargoBean.cargoNovo.nomeCargo}"/>
                                                            <br>
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton  value="Salvar" id="salvarNovoCargo"
                                                                      action="#{cargoBean.salvarNovoCargo}"                                                                         >

                                                        <rich:componentControl for="addCargoPanel"  attachTo="salvarNovoCargo"
                                                                               operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>

                                            </rich:modalPanel>
                                            <rich:modalPanel id="renomearCargoPanel" width="300" height="150" autosized="true" styleClass="center">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Editar Cargo"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkc8"/>
                                                        <rich:componentControl for="renomearCargoPanel" attachTo="hidelinkc8" operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid id = "editCargoGrid">
                                                        <center>
                                                            <br>
                                                            <h:outputText value="Nome do cargo: " styleClass="label"/>
                                                            <h:inputText id="editCargoID" value="#{cargoBean.cargoEdit.nomeCargo}"/>
                                                            <br>
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton  value="Salvar" id="salvarEditCargo"
                                                                      action="#{cargoBean.salvarEditCargo}">

                                                        <rich:componentControl for="renomearCargoPanel"  attachTo="salvarEditCargo"
                                                                               operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </rich:modalPanel>
                                        </center>
                                    </a4j:region>
                                </rich:tab>

                                <rich:tab id="subtab32" label="Feriados" rendered="#{usuarioBean.perfil.feriados== true}">
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
                                                                         reRender="paneleditFeriado">
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
                                                                           reRender="panelGridFeriado"
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
                                                                     reRender="paneladdFeriado">
                                                        </a4j:support>
                                                    </h:outputLink>
                                                    <br/>
                                                    Adicionar
                                                </center>
                                            </h:panelGroup>

                                            <rich:modalPanel id="editmodalPanelFeriado" width="280" height="150" autosized="true" style="text-align:center;float:center;">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Editar Horário" ></h:outputText>
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

                                <rich:tab id="subtab33" label="Justificativas de Abonos" rendered="#{usuarioBean.perfil.justificativas== true}">
                                    <center>
                                        <br/>
                                        <h:panelGrid columns="5">
                                            <a4j:outputPanel id="justPanel">
                                                <center>
                                                    <h:outputText value="Lista de Justificativas para Abono" styleClass="labelMaior"/>
                                                    <br> <br>
                                                    <rich:dataTable value="#{justificativaBean.justificativa}"
                                                                    var="justificativa"
                                                                    id="tableJustificativa"
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
                                                                                 reRender="paneleditJustificativa">
                                                                        <f:param name="justificativa_id" value="#{justificativa.justificativaID}"/>
                                                                    </a4j:support>
                                                                </h:outputLink>
                                                            </center>
                                                        </h:column>
                                                        <h:column>
                                                            <center>
                                                                <a4j:commandButton image="../images/delete.png"
                                                                                   action="#{justificativaBean.deletar}"
                                                                                   reRender="justPanel"
                                                                                   ajaxSingle="true"
                                                                                   onclick="javascript:if (!confirm('Tem certeza que deseja deletar a justificativa?')) return false;">
                                                                    <f:param name="justificativa_id" value="#{justificativa.justificativaID}"/>
                                                                </a4j:commandButton>
                                                            </center>
                                                        </h:column>
                                                    </rich:dataTable>
                                                    <rich:datascroller  id="datascrollerJustificativa"
                                                                        for="tableJustificativa"
                                                                        renderIfSinglePage="false">
                                                    </rich:datascroller>
                                                </center>
                                                <rich:modalPanel id="editJustificativaModalPanel" width="550" height="220">
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
                                                        <h:panelGrid  id="paneleditJustificativa" columns="1" style="text-align:left;float:left">
                                                            <h:panelGroup>
                                                                <h:outputText value="Nome do Abono:  " styleClass="labelRight"/>
                                                                <rich:spacer width="5"/>
                                                                <h:inputText size="20" value="#{justificativaBean.editJustificativa.justificativaNome}" maxlength="20"/>
                                                                <br/><br/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <h:selectBooleanCheckbox value="#{justificativaBean.editJustificativa.soAdministrador}"/>
                                                                <rich:spacer width="5"/>
                                                                <h:outputText value=" Abono exclusivo para administrador?" styleClass="labelRight"/>
                                                                <br/><br/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <h:selectBooleanCheckbox value="#{justificativaBean.editJustificativa.isTotal}"/>
                                                                <rich:spacer width="5"/>
                                                                <h:outputText value=" Abono Total (Funcionário terá todas as vantagens como se estivesse registrado o ponto)" styleClass="labelRight"/>
                                                                <br/><br/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <h:selectBooleanCheckbox value="#{justificativaBean.editJustificativa.isDescricaoObrigatoria}"/>
                                                                <rich:spacer width="5"/>
                                                                <h:outputText value=" Abono com descrição obrigatória?" styleClass="labelRight"/>
                                                                <br/><br/>
                                                            </h:panelGroup>
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
                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                        <rich:componentControl for="addJustificativaModalPanel" attachTo="linkJustificativa" operation="show" event="onclick"/>
                                                        <a4j:support action="#{justificativaBean.showInserir}"
                                                                     event="onclick"
                                                                     reRender="paneladdJustificativa">
                                                        </a4j:support>
                                                    </h:outputLink>
                                                    <br/>
                                                    Adicionar
                                                </center>
                                            </a4j:outputPanel>
                                            <rich:modalPanel id="addJustificativaModalPanel" width="550" height="220">
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
                                                    <h:panelGrid  id="paneladdJustificativa" columns="1" style="text-align:left;float:left">
                                                        <h:panelGroup>
                                                            <h:outputText value="Nome do Abono:  " styleClass="labelRight"/>
                                                            <rich:spacer width="5"/>
                                                            <h:inputText size="20" value="#{justificativaBean.novaJustificativa.justificativaNome}" maxlength="20"/>
                                                            <br/><br/>
                                                        </h:panelGroup>
                                                        <h:panelGroup>
                                                            <h:selectBooleanCheckbox value="#{justificativaBean.novaJustificativa.soAdministrador}"/>
                                                            <rich:spacer width="5"/>
                                                            <h:outputText value=" Abono exclusivo para administrador?" styleClass="labelRight"/>
                                                            <br/><br/>
                                                        </h:panelGroup>
                                                        <h:panelGroup>
                                                            <h:selectBooleanCheckbox value="#{justificativaBean.novaJustificativa.isTotal}"/>
                                                            <rich:spacer width="5"/>
                                                            <h:outputText value=" Abono Total (Funcionário terá todas as vantagens como se estivesse batido o ponto)" styleClass="labelRight"/>
                                                            <br/><br/>
                                                        </h:panelGroup>
                                                        <h:panelGroup>
                                                            <h:selectBooleanCheckbox value="#{justificativaBean.novaJustificativa.isDescricaoObrigatoria}"/>
                                                            <rich:spacer width="5"/>
                                                            <h:outputText value=" Abono com descrição obrigatória?" styleClass="labelRight"/>
                                                            <br/><br/>
                                                        </h:panelGroup>
                                                    </h:panelGrid>
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


                                <rich:tab id="subtab39" label="Verbas" rendered="#{usuarioBean.perfil.verbas == true}">
                                    <br/>
                                    <center>
                                        <h:outputText value="Lista de verbas" styleClass="labelMaior"/>
                                        <br/><br/>                                     
                                        <rich:panel id="verbaPanel" style="width:220px;">
                                            <f:facet name="header">
                                                <h:outputText value="Lista de Verbas"></h:outputText>
                                            </f:facet>
                                            <h:panelGrid columns="2">
                                                <h:outputText value="Código da empresa: " styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.empresa.cod_verba}"/>
                                                <h:outputText value="Adicional Noturno: " styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.adicionalNoturno.cod_verba}"/>
                                                <h:outputText value="Atrasos:" styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.atrasos.cod_verba}"/>
                                                <h:outputText value="Atrasos < 1:" styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.atrasos_menor_uma_hora.cod_verba}"/>
                                                <h:outputText value="Atrasos > 1:" styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.atrasos_maior_uma_hora.cod_verba}"/>
                                                <h:outputText value="Feriado Crítico: " styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.feriadoCritico.cod_verba}"/>
                                                <h:outputText value="DSR: " styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.dsr.cod_verba}"/>
                                                <h:outputText value="Faltas: " styleClass="labelRight"/>
                                                <h:inputText size="3" value="#{verbaBean.faltas.cod_verba}"/>                                             
                                            </h:panelGrid>
                                        </rich:panel>
                                        <br/>
                                        <h:panelGrid columns="1">
                                            <a4j:commandButton value="Salvar" action="#{verbaBean.salvar}" reRender="verbaPanel"/> 
                                        </h:panelGrid>
                                    </center>
                                </rich:tab>


                                <rich:tab id="subCategoriaAfastmento" label="Justificativas de Afastamentos"
                                          rendered="#{usuarioBean.perfil.categoriaAfastamento == true}">
                                    <center>
                                        <br/>
                                        <h:panelGrid columns="5">
                                            <a4j:outputPanel id="categPanel">
                                                <center>
                                                    <h:outputText value="Lista de Justificativas de Afastamento" styleClass="labelMaior"/>
                                                    <br> <br>
                                                    <rich:dataTable value="#{categoriaAfastamentoBean.categoriaAfastamentoList}"
                                                                    rendered="#{not empty categoriaAfastamentoBean.categoriaAfastamentoList}"
                                                                    var="categoriaAfastamento"
                                                                    id="tableCategoriaAfastamento"
                                                                    rows="12"
                                                                    onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                    onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'">>
                                                        <f:facet name="header">
                                                            <rich:columnGroup>
                                                                <h:column>
                                                                    <h:outputText value="Categoria" />
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
                                                                <h:outputText  value="#{categoriaAfastamento.descCategoriaAfastamento}"/>
                                                            </center>
                                                        </h:column>
                                                        <h:column>
                                                            <center>
                                                                <h:outputLink  value="#" id="linkCategoriaAfastamento">
                                                                    <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                    <rich:componentControl for="editCategoriaAfastamentoModalPanel"
                                                                                           attachTo="linkCategoriaAfastamento"
                                                                                           operation="show"
                                                                                           event="onclick"/>
                                                                    <a4j:support action="#{categoriaAfastamentoBean.showEditar}"
                                                                                 event="onclick"
                                                                                 reRender="paneleditCategoriaAfastamento">
                                                                        <f:param name="categoriaAfastamento_id" value="#{categoriaAfastamento.categoriaAfastamentoID}"/>
                                                                    </a4j:support>
                                                                </h:outputLink>
                                                            </center>
                                                        </h:column>
                                                        <h:column>
                                                            <center>
                                                                <a4j:commandButton image="../images/delete.png"
                                                                                   action="#{categoriaAfastamentoBean.deletar}"
                                                                                   reRender="categPanel"
                                                                                   ajaxSingle="true"
                                                                                   onclick="javascript:if (!confirm('Tem certeza que deseja deletar a categoria?')) return false;">
                                                                    <f:param name="categoriaAfastamento_id" value="#{categoriaAfastamento.categoriaAfastamentoID}"/>
                                                                </a4j:commandButton>
                                                            </center>
                                                        </h:column>
                                                    </rich:dataTable>
                                                    <rich:datascroller  id="datascrollerCategoriaAfastamento"
                                                                        for="tableCategoriaAfastamento"
                                                                        renderIfSinglePage="false">
                                                    </rich:datascroller>
                                                </center>


                                                <rich:modalPanel id="editCategoriaAfastamentoModalPanel"  width="290" height="140">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="Editar Justificativa"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="editCategoriaAfastamentohidelink"/>
                                                            <rich:componentControl for="editCategoriaAfastamentoModalPanel"
                                                                                   attachTo="editCategoriaAfastamentohidelink"
                                                                                   operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid  id="paneleditCategoriaAfastamento" columns="1" style="text-align:left;float:left">
                                                            <h:panelGrid columns="3" style="text-align:center;float:center;">
                                                                <h:outputText value="Nome do justificativa  " style="float:right;font-weight:bold"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText size="20"
                                                                             value="#{categoriaAfastamentoBean.editCategoriaAfastamento.descCategoriaAfastamento}"
                                                                             maxlength="20" style="float:left"/>

                                                                <h:outputText value="Legenda:  " style="float:right;font-weight:bold"/>
                                                                <rich:spacer width="15"/>
                                                                <h:inputText size="5"
                                                                             value="#{categoriaAfastamentoBean.editCategoriaAfastamento.legenda}"
                                                                             maxlength="3" style="float:left"/>

                                                            </h:panelGrid>
                                                            <h:panelGroup>
                                                                <br/>
                                                                <center>
                                                                    <h:commandButton value="Editar" id="editarCategoriaAfastamentoID" action="#{categoriaAfastamentoBean.editar}">
                                                                        <rich:componentControl for="editCategoriaAfastamentoModalPanel"
                                                                                               attachTo="editarCategoriaAfastamentoID" operation="hide" event="onclick"/>
                                                                    </h:commandButton>
                                                                </center>
                                                            </h:panelGroup>
                                                        </h:panelGrid>
                                                    </center>
                                                </rich:modalPanel>
                                                <br/>
                                                <center>
                                                    <h:outputLink  value="#" id="linkCategoriaAfastamento">
                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                        <rich:componentControl for="addCategoriaAfastamentoModalPanel"
                                                                               attachTo="linkCategoriaAfastamento" operation="show" event="onclick"/>
                                                        <a4j:support action="#{categoriaAfastamentoBean.showInserir}"
                                                                     event="onclick"
                                                                     reRender="paneladdCategoriaAfastamento">
                                                        </a4j:support>
                                                    </h:outputLink>
                                                    <br/>
                                                    Adicionar
                                                </center>
                                            </a4j:outputPanel>


                                            <rich:modalPanel id="addCategoriaAfastamentoModalPanel" width="290" height="140">
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Adicionar Justificativas"></h:outputText>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <f:facet name="controls">
                                                    <h:panelGroup>
                                                        <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addCategoriaAfastamentohidelink"/>
                                                        <rich:componentControl for="addCategoriaAfastamentoModalPanel"
                                                                               attachTo="addCategoriaAfastamentohidelink"
                                                                               operation="hide" event="onclick"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <center>
                                                    <h:panelGrid  id="paneladdCategoriaAfastamento" columns="1" style="text-align:left;float:left">
                                                        <h:panelGrid columns="3" style="text-align:center;float:center;">
                                                            <h:outputText value="Nome da justificativas  " style="float:right;font-weight:bold"/>
                                                            <rich:spacer width="15"/>
                                                            <h:inputText size="20" value="#{categoriaAfastamentoBean.novaCategoriaAfastamento.descCategoriaAfastamento}"
                                                                         maxlength="20" style="float:left"/>

                                                            <h:outputText value="Legenda:  " style="float:right;font-weight:bold"/>
                                                            <rich:spacer width="15"/>
                                                            <h:inputText size="5"
                                                                         value="#{categoriaAfastamentoBean.novaCategoriaAfastamento.legenda}"
                                                                         maxlength="3" style="float:left"/>
                                                        </h:panelGrid>                                                     
                                                        <h:panelGroup>
                                                            <br/>
                                                            <center>
                                                                <h:commandButton value="Salvar" id="addCategoriaAfastamentoID"
                                                                                 action="#{categoriaAfastamentoBean.inserir}">
                                                                    <rich:componentControl for="addCategoriaAfastamentoModalPanel"
                                                                                           attachTo="addCategoriaAfastamentoID" operation="hide" event="onclick"/>
                                                                </h:commandButton>
                                                            </center>
                                                        </h:panelGroup>
                                                    </h:panelGrid>
                                                </center>
                                            </rich:modalPanel>
                                        </h:panelGrid>
                                    </center>
                                </rich:tab>
                                <rich:tab id="subtab34" label="Título do Relatório" rendered="#{usuarioBean.perfil.tituloDoRelatorio== true}">
                                    <center>
                                        <h:panelGrid columns="3" >
                                            <h:outputText value="Título dos relatórios: " styleClass="label"/>
                                            <rich:spacer width="7"/>
                                            <h:inputText id="titulo"  value="#{configuracoesBean.titulo}" size="55" maxlength="30">                                              
                                            </h:inputText>
                                        </h:panelGrid>
                                        <br>
                                        <a4j:commandButton value="Salvar" action="#{configuracoesBean.editarTitulo}"/>
                                    </center>
                                </rich:tab>
                                <rich:tab id="tab5" label="Hora Extra e Gratificações" rendered="#{usuarioBean.isAtivo && usuarioBean.perfil.horaExtraEGratificacoes== true}">
                                    <center>
                                        <br/>
                                        <h:outputText id="migalha" value="#{horaExtraBean.migalhadePao}" styleClass="label"/>
                                        <br/><center>
                                            <h:panelGrid columns="1"><center>
                                                    <a4j:outputPanel id="regimePanel"><center>
                                                            <h:panelGrid columns="1">  <center>
                                                                    <rich:dataTable value="#{horaExtraBean.regimeHoraExtraList}"
                                                                                    var="regime"
                                                                                    id="table"
                                                                                    rows="12"
                                                                                    onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                                    onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                                    onRowContextMenu="this.style.backgroundColor='#F55555'"
                                                                                    rendered="#{not empty horaExtraBean.regimeHoraExtraList}">
                                                                        <f:facet name="header">
                                                                            <rich:columnGroup>
                                                                                <h:column>
                                                                                    <h:outputText value="REGIME" />
                                                                                </h:column>
                                                                                <h:column>
                                                                                    <h:outputText value="EDITAR" />
                                                                                </h:column>
                                                                                <h:column>
                                                                                    <h:outputText value="DELETAR" />
                                                                                </h:column>
                                                                            </rich:columnGroup>
                                                                        </f:facet>
                                                                        <h:column>
                                                                            <center>
                                                                                <a4j:commandLink id="nomeRegime"  value="#{regime.nome}"
                                                                                                 action="#{horaExtraBean.consultaDetalheRegime}"
                                                                                                 reRender="regimePanel,tipoPanelGrid,detalhePanelGrid,
                                                                                                 detalheGratifricacaoPanelGrid,detalheGratifricacaoPanelGrid,migalha"
                                                                                                 styleClass="#{regime.regime_css}">
                                                                                    <a4j:actionparam name="regime_param" value="#{regime.cod}"/>
                                                                                    <a4j:actionparam name="regime_nome_param" value="#{regime.nome}"/>
                                                                                    <a4j:actionparam name="regime_feriadoCritico_param" value="#{regime.feriadoCritico}"/>
                                                                                </a4j:commandLink>
                                                                            </center>
                                                                        </h:column>
                                                                        <h:column>
                                                                            <center>
                                                                                <h:outputLink  value="#" id="editRegime">
                                                                                    <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                                    <rich:componentControl for="editRegimeModalPanel"
                                                                                                           attachTo="editRegime"
                                                                                                           operation="show"
                                                                                                           event="onclick"/>
                                                                                    <a4j:support action="#{horaExtraBean.showEditar}"
                                                                                                 event="onclick"
                                                                                                 reRender="paneleditRegime"
                                                                                                 ajaxSingle="true">
                                                                                        <f:param name="cod_regime" value="#{regime.cod}"/>
                                                                                    </a4j:support>
                                                                                </h:outputLink>
                                                                            </center>
                                                                        </h:column>
                                                                        <h:column>
                                                                            <center>
                                                                                <a4j:commandButton image="../images/delete.png"
                                                                                                   action="#{horaExtraBean.deletar}"
                                                                                                   reRender="regimePanel,tipoPanelGrid,migalha"
                                                                                                   ajaxSingle="true">
                                                                                    <f:param name="cod_regime" value="#{regime.cod}"/>
                                                                                </a4j:commandButton>
                                                                            </center>
                                                                        </h:column>
                                                                    </rich:dataTable>
                                                                    <h:panelGroup rendered="#{empty horaExtraBean.regimeHoraExtraList}">
                                                                        <h:outputText value="Nenhum regime cadastrado" styleClass="label"/>
                                                                    </h:panelGroup>
                                                                    <h:panelGroup>
                                                                        <center>
                                                                            <h:outputLink  value="#" id="linkRegime">
                                                                                <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                                <rich:componentControl for="addRegimeModalPanel" attachTo="linkRegime" operation="show" event="onclick"/>
                                                                                <a4j:support action="#{horaExtraBean.showInserir}"
                                                                                             event="onclick"
                                                                                             reRender="paneladdRegime">
                                                                                </a4j:support>
                                                                            </h:outputLink>
                                                                            <br/>
                                                                            <h:outputText value="Adicionar" styleClass="label"/>
                                                                        </center>
                                                                    </h:panelGroup>  </center>
                                                            </h:panelGrid></center>
                                                    </a4j:outputPanel></center>
                                            </center>
                                            <a4j:outputPanel id="tipoPanelGrid" style="text-align:center;float:center;">                                               
                                                <center>
                                                    <h:panelGrid columns="5" rendered="#{horaExtraBean.regime_param != null}"
                                                                 style="text-align:center;float:center;">

                                                        <h:panelGroup>
                                                            <center>
                                                                <h:outputText value="Permitir feriados críticos:" styleClass="label"/>
                                                                <h:selectBooleanCheckbox
                                                                    value="#{horaExtraBean.regimeHoraExtra.feriadoCritico}" id="feriadoCriticoSelectOneID">
                                                                    <a4j:support event="onchange"  action="#{horaExtraBean.mudarFeriadoCritico()}"/>
                                                                </h:selectBooleanCheckbox>
                                                            </center>
                                                        </h:panelGroup>

                                                        <h:panelGroup id="tempoToleranciaID">
                                                            <h:panelGroup   rendered="#{horaExtraBean.regimeHoraExtra.modoTolerancia==1}">
                                                                <center>
                                                                    <h:outputText value="Tempo de Tolerância:" styleClass="label"/>
                                                                    <rich:inputNumberSpinner  value="#{horaExtraBean.regimeHoraExtra.tolerancia}"
                                                                                              inputSize="3" maxValue="1000" minValue="0"
                                                                                              id="tempoToleranciaInputNumberSpinnerID">
                                                                        <a4j:support event="onchange" requestDelay="120"  action="#{horaExtraBean.mudarTolerancia}"/>
                                                                    </rich:inputNumberSpinner>
                                                                </center>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <rich:spacer width="25"/>
                                                        <h:panelGroup>
                                                            <center>
                                                                <h:outputText value="Modo de Tolerância:" styleClass="label"/>
                                                                <h:selectOneRadio
                                                                    value="#{horaExtraBean.regimeHoraExtra.modoTolerancia}" id="modoToleranciaSelectOneID">
                                                                    <f:selectItems value="#{horaExtraBean.modoRegimeSelecItem}" id="modoRegimeSelectItem"/>
                                                                    <a4j:support event="onchange"  action="#{horaExtraBean.mudarModoTolerancia}" reRender="tempoToleranciaID"/>
                                                                </h:selectOneRadio>
                                                            </center>
                                                        </h:panelGroup>



                                                    </h:panelGrid>
                                                </center>
                                                <br>
                                                <center>
                                                    <h:panelGrid columns="5" rendered="#{horaExtraBean.regime_param != null}">
                                                        <h:panelGroup>
                                                            <rich:dataTable value="#{horaExtraBean.tipoHoraExtraList}"
                                                                            var="tipo"
                                                                            id="table2"
                                                                            rows="12"
                                                                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                            onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                            onRowClick="this.style.backgroundColor='#FFFFFF'"
                                                                            rendered="#{not empty horaExtraBean.tipoHoraExtraList}">
                                                                <f:facet name="header">
                                                                    <rich:columnGroup>
                                                                        <h:column>
                                                                            <h:outputText value="TIPO HORA EXTRA" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="EDITAR" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="DELETAR" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="PADRÃO" />
                                                                        </h:column>
                                                                    </rich:columnGroup>
                                                                </f:facet>
                                                                <h:column>
                                                                    <center>
                                                                        <a4j:commandLink id="nomeTipo"
                                                                                         value="#{tipo.nome}"
                                                                                         action="#{horaExtraBean.consultaDetalheHoraExtra}"
                                                                                         styleClass="#{tipo.tipo_css}"
                                                                                         reRender="detalhePanelGrid,detalheGratifricacaoPanelGrid,migalha">
                                                                            <a4j:actionparam name="cod_tipo" value="#{tipo.cod_tipo}"/>
                                                                            <a4j:actionparam name="tipo_nome_param" value="#{tipo.nome}"/>
                                                                        </a4j:commandLink>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <h:outputLink  value="#" id="editTipo">
                                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                            <rich:componentControl for="editTipoModalPanel"
                                                                                                   attachTo="editTipo"
                                                                                                   operation="show"
                                                                                                   event="onclick"/>
                                                                            <a4j:support action="#{horaExtraBean.showEditarTipo}"
                                                                                         event="onclick"
                                                                                         reRender="paneleditTipo">
                                                                                <f:param name="cod_tipo" value="#{tipo.cod_tipo}"/>
                                                                            </a4j:support>
                                                                        </h:outputLink>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <a4j:commandButton image="../images/delete.png"
                                                                                           onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                           action="#{horaExtraBean.deletarTipo}"
                                                                                           reRender="tipoPanelGrid">
                                                                            <f:param name="cod_tipo" value="#{tipo.cod_tipo}"/>
                                                                        </a4j:commandButton>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <h:selectBooleanCheckbox value="#{tipo.isPadrao}">
                                                                            <a4j:support action="#{horaExtraBean.setTipoPadrao}"
                                                                                         event="onchange"
                                                                                         reRender="tipoPanelGrid"
                                                                                         ajaxSingle="true"
                                                                                         >
                                                                                <f:param name="cod_tipo" value="#{tipo.cod_tipo}"/>
                                                                            </a4j:support>
                                                                        </h:selectBooleanCheckbox>
                                                                    </center>
                                                                </h:column>
                                                            </rich:dataTable>
                                                            <h:panelGroup rendered="#{empty horaExtraBean.tipoHoraExtraList}">
                                                                <h:outputText value="Nenhum tipo cadastrado" styleClass="label"/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <center>
                                                                    <h:outputLink  value="#" id="linkTipo">
                                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                        <rich:componentControl for="addTipoModalPanel" attachTo="linkTipo" operation="show" event="onclick"/>
                                                                        <a4j:support action="#{horaExtraBean.showInserirTipo}"
                                                                                     event="onclick"
                                                                                     reRender="paneladdTipo">
                                                                        </a4j:support>
                                                                    </h:outputLink>
                                                                    <br/>
                                                                    <h:outputText value="Adicionar" styleClass="label"/>
                                                                </center>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <rich:spacer width="20"/>
                                                        <h:panelGroup>
                                                            <rich:dataTable value="#{horaExtraBean.justificativaList}"
                                                                            var="justificativa"
                                                                            id="categoriaTable"
                                                                            rows="10"
                                                                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                            onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                            onRowClick="this.style.backgroundColor='#FFFFFF'"
                                                                            rendered="#{not empty horaExtraBean.justificativaList}">
                                                                <f:facet name="header">
                                                                    <rich:columnGroup>
                                                                        <h:column>
                                                                            <h:outputText value="JUSTIFICATIVA HORA EXTRA" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="EDITAR" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="DELETAR" />
                                                                        </h:column>
                                                                    </rich:columnGroup>
                                                                </f:facet>
                                                                <h:column>
                                                                    <center>
                                                                        <h:outputText id="nomeJustificativa"
                                                                                      value="#{justificativa.descricao}">

                                                                        </h:outputText>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <h:outputLink  value="#" id="editJustificativa">
                                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                            <rich:componentControl for="editJustificativaHEModalPanel"
                                                                                                   attachTo="editJustificativa"
                                                                                                   operation="show"
                                                                                                   event="onclick"/>
                                                                            <a4j:support
                                                                                action="#{horaExtraBean.showEditarJustificativa}"
                                                                                reRender="paneleditJustificativaHE"
                                                                                event="onclick">
                                                                                <f:param name="cod_justificativa_param" value="#{justificativa.cod_justificativa}"/>
                                                                                <f:param name="descricao_justificativa_param" value="#{justificativa.descricao}"/>
                                                                            </a4j:support>
                                                                        </h:outputLink>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <a4j:commandButton
                                                                            action="#{horaExtraBean.deletarJustificativa}"
                                                                            image="../images/delete.png"
                                                                            reRender="tipoPanelGrid"
                                                                            onclick="javascript:if (!confirm('Deseja excluir esse regime?')) return false;">
                                                                            <f:param name="cod_justificativa_param" value="#{justificativa.cod_justificativa}"/>
                                                                        </a4j:commandButton>
                                                                    </center>
                                                                </h:column>
                                                            </rich:dataTable>
                                                            <h:panelGroup rendered="#{empty horaExtraBean.justificativaList}">
                                                                <h:outputText value="Nenhum justificativa de hora extra cadastrada" styleClass="label"/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <center>
                                                                    <h:outputLink  value="#" id="linkJustificativaHE">
                                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                        <rich:componentControl for="addJustificativaHEModalPanel" attachTo="linkJustificativaHE" operation="show" event="onclick"/>
                                                                    </h:outputLink>
                                                                    <br/>
                                                                    <h:outputText value="Adicionar" styleClass="label"/>
                                                                </center>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                        <rich:spacer width="20"/>
                                                        <h:panelGroup>
                                                            <rich:dataTable value="#{horaExtraBean.gratificacaoList}"
                                                                            var="gratificacao"
                                                                            id="gratificacaoTable"
                                                                            rows="10"
                                                                            onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                            onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                            onRowClick="this.style.backgroundColor='#FFFFFF'"
                                                                            rendered="#{not empty horaExtraBean.gratificacaoList}">
                                                                <f:facet name="header">
                                                                    <rich:columnGroup>
                                                                        <h:column>
                                                                            <h:outputText value="GRATIFICAÇÃO" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="EDITAR" />
                                                                        </h:column>
                                                                        <h:column>
                                                                            <h:outputText value="DELETAR" />
                                                                        </h:column>
                                                                    </rich:columnGroup>
                                                                </f:facet>
                                                                <h:column>
                                                                    <center>
                                                                        <a4j:commandLink id="nomeGratificacao"
                                                                                         value="#{gratificacao.nome}"
                                                                                         action="#{horaExtraBean.consultaDetalheGratificacao}"
                                                                                         reRender="detalheGratifricacaoPanelGrid,detalhePanelGrid,migalha">
                                                                            <a4j:actionparam name="cod_gratificacao" value="#{gratificacao.cod_gratificacao}"/>
                                                                            <a4j:actionparam name="gratificacao_nome_param" value="#{gratificacao.nome}"/>
                                                                        </a4j:commandLink>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <h:outputLink  value="#" id="editGratificacao">
                                                                            <h:graphicImage value="../images/edit.gif" style="border:0"/>
                                                                            <rich:componentControl for="editGratificacaoModalPanel"
                                                                                                   attachTo="editGratificacao"
                                                                                                   operation="show"
                                                                                                   event="onclick"/>
                                                                            <a4j:support action="#{horaExtraBean.showEditarGratificacao}"
                                                                                         event="onclick"
                                                                                         reRender="paneleditGratificacao">
                                                                                <f:param name="cod_gratificacao" value="#{gratificacao.cod_gratificacao}"/>
                                                                            </a4j:support>
                                                                        </h:outputLink>
                                                                    </center>
                                                                </h:column>
                                                                <h:column>
                                                                    <center>
                                                                        <a4j:commandButton image="../images/delete.png"
                                                                                           onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                           action="#{horaExtraBean.deletarGratificacao}"
                                                                                           reRender="tipoPanelGrid">
                                                                            <f:param name="cod_gratificacao" value="#{gratificacao.cod_gratificacao}"/>
                                                                        </a4j:commandButton>
                                                                    </center>
                                                                </h:column>
                                                            </rich:dataTable>
                                                            <h:panelGroup rendered="#{empty horaExtraBean.gratificacaoList}">
                                                                <h:outputText value="Nenhuma Gratificacão Cadastrada" styleClass="label"/>
                                                            </h:panelGroup>
                                                            <h:panelGroup>
                                                                <center>
                                                                    <h:outputLink  value="#" id="linkGratificacao">
                                                                        <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                                        <rich:componentControl for="addGratificacaoModalPanel" attachTo="linkGratificacao" operation="show" event="onclick"/>
                                                                        <a4j:support action="#{horaExtraBean.showInserirGratificacao}"
                                                                                     event="onclick"
                                                                                     reRender="paneladdGratificacao"/>
                                                                    </h:outputLink>
                                                                    <br/>
                                                                    <h:outputText value="Adicionar" styleClass="label"/>
                                                                </center>
                                                            </h:panelGroup>
                                                        </h:panelGroup>
                                                    </h:panelGrid>
                                                </center>
                                            </a4j:outputPanel>
                                            <center>
                                                <a4j:outputPanel id="detalhePanelGrid">
                                                    <br/><center>
                                                        <rich:dataTable value="#{horaExtraBean.detalheHoraExtraList}"
                                                                        var="diaSemana"
                                                                        id="table3"
                                                                        rows="8"
                                                                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                        rendered="#{not empty horaExtraBean.detalheHoraExtraList and not horaExtraBean.isPadrao}">
                                                            <f:facet name="header">
                                                                <rich:columnGroup>
                                                                    <h:column>
                                                                        <h:outputText value="DIA DA SEMANA" />
                                                                    </h:column>
                                                                    <h:column>
                                                                        <h:outputText value="INÍCIO" />
                                                                    </h:column>
                                                                    <h:column>
                                                                        <h:outputText value="DIA INTEIRO" />
                                                                    </h:column>
                                                                </rich:columnGroup>
                                                            </f:facet>
                                                            <h:column>
                                                                <center>
                                                                    <h:outputText
                                                                        value="#{diaSemana.diaStr}" styleClass="label"/>
                                                                </center>
                                                            </h:column>
                                                            <h:column>
                                                                <center>
                                                                    <h:inputText id="horaInicio" size="1" value="#{diaSemana.inicio}" maxlength="3">
                                                                        <rich:jQuery selector="#horaInicio" query="mask('9.9')" timing="onload" />
                                                                        <a4j:support  event="onblur" action="#{horaExtraBean.editDetalhe}"
                                                                                      ajaxSingle="true">
                                                                            <f:param name="dia" value="#{diaSemana.dia}"/>
                                                                        </a4j:support>
                                                                    </h:inputText>
                                                                </center>
                                                            </h:column>
                                                            <h:column>
                                                                <center>
                                                                    <h:selectBooleanCheckbox
                                                                        value="#{diaSemana.isDiaInteiro}" styleClass="label">
                                                                        <a4j:support  event="onblur" action="#{horaExtraBean.editDetalheIsDiaInteiro}"
                                                                                      ajaxSingle="true">
                                                                            <f:param name="dia" value="#{diaSemana.dia}"/>
                                                                        </a4j:support>
                                                                    </h:selectBooleanCheckbox>
                                                                </center>
                                                            </h:column>
                                                        </rich:dataTable></center>
                                                    </a4j:outputPanel>

                                                <a4j:outputPanel id="detalheGratifricacaoPanelGrid">
                                                    <br/><center>
                                                        <rich:dataTable value="#{horaExtraBean.detalheGratificacaoList}"
                                                                        var="diaSemana"
                                                                        id="table4"
                                                                        rows="8"
                                                                        onRowMouseOver="this.style.backgroundColor='#F1F1F1'"
                                                                        onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}'"
                                                                        rendered="#{not empty horaExtraBean.detalheGratificacaoList}">
                                                            <f:facet name="header">
                                                                <rich:columnGroup>
                                                                    <h:column>
                                                                        <h:outputText value="DIA DA SEMANA" />
                                                                    </h:column>
                                                                    <h:column>
                                                                        <h:outputText value="INÍCIO" />
                                                                    </h:column>
                                                                    <h:column>
                                                                        <h:outputText value="FIM" />
                                                                    </h:column>
                                                                </rich:columnGroup>
                                                            </f:facet>
                                                            <h:column>
                                                                <center>
                                                                    <h:outputText
                                                                        value="#{diaSemana.diaStr}" styleClass="label"/>
                                                                </center>
                                                            </h:column>
                                                            <h:column>
                                                                <center>
                                                                    <h:inputText id="horaInicio" size="1" value="#{diaSemana.inicio}" maxlength="5">
                                                                        <rich:jQuery selector="#horaInicio" query="mask('99:99')" timing="onload" />
                                                                        <a4j:support  event="onblur" action="#{horaExtraBean.editDetalheInicio}"
                                                                                      ajaxSingle="true">
                                                                            <f:param name="dia" value="#{diaSemana.dia}"/>
                                                                        </a4j:support>
                                                                    </h:inputText>
                                                                </center>
                                                            </h:column>
                                                            <h:column>
                                                                <center>
                                                                    <h:inputText id="horaFim" size="1" value="#{diaSemana.fim}" maxlength="5">
                                                                        <rich:jQuery selector="#horaFim" query="mask('99:99')" timing="onload" />
                                                                        <a4j:support  event="onblur" action="#{horaExtraBean.editDetalheFim}"
                                                                                      ajaxSingle="true">
                                                                            <f:param name="dia" value="#{diaSemana.dia}"/>
                                                                        </a4j:support>
                                                                    </h:inputText>
                                                                </center>
                                                            </h:column>
                                                        </rich:dataTable></center>
                                                    </a4j:outputPanel>
                                            </center>
                                        </h:panelGrid>
                                        <rich:modalPanel id="editRegimeModalPanel" width="260" height="110">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Regime"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink"
                                                                    id="editRegimehidelink"/>
                                                    <rich:componentControl for="editRegimeModalPanel"
                                                                           attachTo="editRegimehidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneleditRegime" columns="3" >
                                                    <h:outputText value="Nome do regime: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="25" value="#{horaExtraBean.editRegime}" maxlength="25"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Editar" id="editarRegimeID" action="#{horaExtraBean.editar}">
                                                        <rich:componentControl for="editRegimeModalPanel" attachTo="editarRegimeID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="addRegimeModalPanel" width="280" height="100">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Adicionar Regime"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addRegimehidelink"/>
                                                    <rich:componentControl for="addRegimeModalPanel"
                                                                           attachTo="addRegimehidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneladdRegime" columns="3" >
                                                    <h:outputText value="Nome do regime: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="18" value="#{horaExtraBean.insereNomeRegime}" maxlength="50"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Salvar" id="addRegimeID" action="#{horaExtraBean.inserir}">
                                                        <rich:componentControl for="addRegimeModalPanel" attachTo="addRegimeID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="editTipoModalPanel" width="305" height="155">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Tipo"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink"
                                                                    id="editTipohidelink"/>
                                                    <rich:componentControl for="editTipoModalPanel"
                                                                           attachTo="editTipohidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneleditTipo" columns="3" >
                                                    <h:outputText value="Nome do tipo: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="10" value="#{horaExtraBean.editTipoHoraExtra.nome}" maxlength="12"/>
                                                    <h:outputText  value="Valor: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="valorHoraExtraEdit" value="#{horaExtraBean.editTipoHoraExtra.valor}" size="1"  maxlength="3">
                                                        <rich:jQuery selector="#valorHoraExtraEdit" query="mask('9.9')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Código da verba: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="3" value="#{horaExtraBean.editTipoHoraExtra.verba}"   maxlength="5">
                                                    </h:inputText>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Editar" id="editarTipoID" action="#{horaExtraBean.editarTipo}">
                                                        <rich:componentControl for="editTipoModalPanel" attachTo="editarTipoID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="editGratificacaoModalPanel" width="305" height="155">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Gratificacao"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink"
                                                                    id="editGratificacaohidelink"/>
                                                    <rich:componentControl for="editGratificacaoModalPanel"
                                                                           attachTo="editGratificacaohidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneleditGratificacao" columns="3" >
                                                    <h:outputText value="Nome da Gratificacão: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="10" value="#{horaExtraBean.editGratificacao.nome}" maxlength="12"/>
                                                    <h:outputText  value="Valor: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="valorGratificacaoEdit" value="#{horaExtraBean.editGratificacao.valor}" size="1"  maxlength="3">
                                                        <rich:jQuery selector="#valorGratificacaoEdit" query="mask('9.9')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Código da verba: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="3" value="#{horaExtraBean.editGratificacao.verba}"   maxlength="5">
                                                    </h:inputText>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Editar" id="editarGratificacaoID" action="#{horaExtraBean.editarGratificacao}">
                                                        <rich:componentControl for="editGratificacaoModalPanel" attachTo="editarGratificacaoID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="addTipoModalPanel" width="305" height="155">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Adicionar Tipo"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addTipohidelink"/>
                                                    <rich:componentControl for="addTipoModalPanel"
                                                                           attachTo="addTipohidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneladdTipo" columns="3" >
                                                    <h:outputText value="Nome do tipo: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="10" value="#{horaExtraBean.novoTipoHoraExtra.nome}" maxlength="12"/>
                                                    <h:outputText  value="Valor: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="valorHoraExtraAdd" value="#{horaExtraBean.novoTipoHoraExtra.valor}" size="1"  maxlength="3">
                                                        <rich:jQuery selector="#valorHoraExtraAdd" query="mask('9.9')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Código da verba: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText value="#{horaExtraBean.novoTipoHoraExtra.verba}" size="2"  maxlength="5">
                                                    </h:inputText>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Salvar" id="addTipoID" action="#{horaExtraBean.inserirTipo}">
                                                        <rich:componentControl for="addTipoModalPanel" attachTo="addTipoID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="addGratificacaoModalPanel" width="305" height="155">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Adicionar Gratificação"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addGratificacaohidelink"/>
                                                    <rich:componentControl for="addGratificacaoModalPanel"
                                                                           attachTo="addGratificacaohidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneladdGratificacao" columns="3" >
                                                    <h:outputText value="Nome da Gratificão: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="10" value="#{horaExtraBean.novaGratificacao.nome}" maxlength="12"/>
                                                    <h:outputText  value="Valor: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText id="valorGratificacaoAdd" value="#{horaExtraBean.novaGratificacao.valor}" size="1"  maxlength="3">
                                                        <rich:jQuery selector="#valorGratificacaoAdd" query="mask('9.9')" timing="onload"/>
                                                    </h:inputText>
                                                    <h:outputText value="Código da verba: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText value="#{horaExtraBean.novaGratificacao.verba}" size="2"  maxlength="5">
                                                    </h:inputText>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Salvar" id="addGratificacaoID" action="#{horaExtraBean.inserirGratificacao}">
                                                        <rich:componentControl for="addGratificacaoModalPanel" attachTo="addGratificacaoID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="addJustificativaHEModalPanel" autosized="true" >
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Adicionar Justificativa"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="addJustificativaHEhidelink"/>
                                                    <rich:componentControl for="addJustificativaHEModalPanel"
                                                                           attachTo="addJustificativaHEhidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid  id="paneladdJustificativaHE" columns="3" >
                                                    <h:outputText value="Justificativa " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="20" value="#{horaExtraBean.justificativa}" maxlength="20"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Salvar" id="addJustificativaHEID" action="#{horaExtraBean.inserirJustificativa}">
                                                        <rich:componentControl for="addJustificativaHEModalPanel" attachTo="addJustificativaHEID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                        <rich:modalPanel id="editJustificativaHEModalPanel" width="310" height="110">
                                            <f:facet name="header">
                                                <h:panelGroup>
                                                    <h:outputText value="Editar Justificativa"></h:outputText>
                                                </h:panelGroup>
                                            </f:facet>
                                            <f:facet name="controls">
                                                <h:panelGroup>
                                                    <h:graphicImage value="/images/close.gif" styleClass="hidelink"
                                                                    id="editJustificativaHEhidelink"/>
                                                    <rich:componentControl for="editJustificativaHEModalPanel"
                                                                           attachTo="editJustificativaHEhidelink"
                                                                           operation="hide" event="onclick"/>
                                                </h:panelGroup>
                                            </f:facet>
                                            <center>
                                                <h:panelGrid id="paneleditJustificativaHE" columns="3" >
                                                    <h:outputText value="Justificativa: " styleClass="labelRight"/>
                                                    <rich:spacer width="5"/>
                                                    <h:inputText size="25" value="#{horaExtraBean.editJustificativa.descricao}" maxlength="25"/>
                                                </h:panelGrid>
                                                <br/>
                                                <center>
                                                    <h:commandButton value="Editar" id="editarJustificativaHEID" action="#{horaExtraBean.editarJustificativa}">
                                                        <rich:componentControl for="editJustificativaHEModalPanel" attachTo="editarJustificativaHEID" operation="hide" event="onclick"/>
                                                    </h:commandButton>
                                                </center>
                                            </center>
                                        </rich:modalPanel>
                                    </center>
                                </rich:tab>
                            </rich:tabPanel>
                        </center>
                    </h:panelGrid>
                </h:form>
                <jsp:include page="../www/_bot.jsp"/>
            </f:view>
        </center>
    </body>
</html>
<SCRIPT language="JavaScript" type="text/javascript">

    function returnNumber(e) {
        var tecla = (window.event) ? event.keyCode : e.which;
        if ((tecla > 47 && tecla < 58))
            return true;
        else {
            if ((tecla != 0) && (tecla != 8))
                return false;
            else
                return true;
        }
    }


    function mascaraInteiro() {
        if (event.keyCode < 48 || event.keyCode > 57) {
            event.returnValue = false;
            return false;
        }
        return true;
    }

    function mascara_data(obj, e)
    {
        var whichCode = (window.Event) ? e.which : e.keyCode;
        if (whichCode != 8)
        {
            var date = obj.value;
            if (date.length == 2)
            {
                obj.value += '/';
            }

            if (date.length == 5)
            {
                obj.value += '/';
            }
            if (date.length == 10)
            {
                verifica_data(obj);
            }
        }
    }
    function verifica_data(obj)
    {
        dia = (obj.value.substring(0, 2));
        mes = (obj.value.substring(3, 5));
        ano = (obj.value.substring(6, 10));

        //alert("t"+dia+" "+mes+" "+ano+"t");

        situacao = "";
        // verifica data e hora
        if ((mes > 12) || (mes < 1)) {
            alert("Data inválida!");
            obj.value = "";
        } else if ((mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12)) {
            if ((dia > 30) || (dia < 1)) {
                alert("Data inválida!");
                obj.value = "";
            }
        } else if ((mes == 2)) {
            if ((ano % 4) != 0) {
                if ((dia > 28) || (dia < 1)) {
                    alert("Data inválida!");
                    obj.value = "";
                } else if ((dia > 29) || (dia < 1)) {
                    alert("Data inválida!");
                    obj.value = "";
                }
            } else if (((ano % 100) == 0) && ((ano % 400) != 0)) {
                if ((dia > 28) || (dia < 1)) {
                    alert("Data inválida!");
                    obj.value = "";
                } else if ((dia > 29) || (dia < 1)) {
                    alert("Data inválida!");
                    obj.value = "";
                }
            }
        }
        else if ((mes == 4) || (mes == 6) || (mes == 9) || (mes == 11)) {
            if ((dia > 30) || (dia < 1)) {
                alert("Data inválida!");
                obj.value = "";
            }
        }
    }
    function ValidarNumero(objeto, e)
    {
        var arrayNumero = '0123456789';
        var date = objeto.value;
        var result = false;


        var whichCode = (window.Event) ? e.which : e.keyCode;
        // 13=enter, 8=backspace as demais retornam 0(zero)
        // whichCode==0 faz com que seja possivel usar todas as teclas como del, setas, etc
        //alert(whichCode);
        if ((whichCode == 13) || (whichCode == 0) || (whichCode == 8))
            return true;

        key = String.fromCharCode(whichCode); // Valor para o código da Chave

        if ((arrayNumero.indexOf(key) != -1) && (date.length < 10))
        {
            result = true; // Chave valida
        }

        return result;
    }
</SCRIPT>