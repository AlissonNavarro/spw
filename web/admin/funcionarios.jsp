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
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" src="../resources/jquery.maskedinput-1.2.2.js">
        </script>
    </head>
    <body>
        <center>
            <f:view >
                <a4j:keepAlive beanName="funcionarioBean"/>

                <jsp:include page="../www/_top.jsp"/>

                <h:form id="f_messagens" prependId="false">
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:form id="form">                   
                    <h:panelGrid columns="1" styleClass="fundoPanel" >
                        <center>
                            <rich:tabPanel switchType="client" width="965">
                                <rich:tab id="tabAdicionar" label="Adicionar Funcionário" rendered="#{usuarioBean.perfil.funcionarios== true}">

                                </rich:tab>
                                <rich:tab id="tabListar" label="Listar Funcionários" rendered="#{usuarioBean.perfil.funcionarios== true}">

                                </rich:tab>  
                                <rich:tab id="tabConsulta" label="Consultar Funcionário" rendered="#{usuarioBean.perfil.funcionarios== true}" >
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