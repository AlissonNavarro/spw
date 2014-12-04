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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../css/default.css" rel="stylesheet" type="text/css">
        <link href="../css/cssLayout.css" rel="stylesheet" type="text/css">
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="../../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <script type="text/javascript" src="../resources/jquery.maskedinput-1.2.2.js">
        </script>
        <title>SGN - Soluções em Gestão De Negócios</title>
        <script language="JavaScript" type="text/javascript">
            function limitText(limitField, limitNum) {
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                }
            }

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


            function mascara_hora(id) {
                var myhora = '';
                myhora = myhora + document.getElementById(id).value;
                if (myhora.length == 2) {
                    myhora = myhora + ':';
                    document.getElementById(id).value = myhora;
                }
                if (myhora.length == 5) {
                    verifica_hora(id);
                }
            }

            function verifica_hora(id) {
                hrs = (document.getElementById(id).value.substring(0, 2));
                min = (document.getElementById(id).value.substring(3, 5));
                situacao = "";
                // verifica data e hora
                if ((hrs < 00) || (hrs > 23) || (min < 00) || (min > 59)) {
                    situacao = "falsa";
                }

                if (document.getElementById(id).value == "") {
                    situacao = "falsa";
                }

                if (situacao == "falsa") {
                    alert("Hora inválida!");
                    document.getElementById(id).focus();
                    document.getElementById(id).value = "";

                }
            }

            function mascara_horaBlur(id) {
                var myhora = '';
                myhora = myhora + document.getElementById(id).value;
                if (myhora.length == 2) {
                    myhora = myhora + ':';
                    document.getElementById(id).value = myhora;
                }

                if (myhora.length < 5 && myhora.length > 0) {
                    alert("Hora inválida!");
                    document.getElementById(id).focus();
                    document.getElementById(id).value = "";
                }

            }
        </SCRIPT>
    </head>
    <body>
        <f:view>            
            <jsp:include page="../www/_top.jsp"/>
            <a4j:keepAlive beanName="permissaoBean" />
            <a4j:keepAlive beanName="perfilBean"/>
            <a4j:keepAlive beanName="conexaoBean" />
            <a4j:keepAlive beanName="machineBean"/>
            <a4j:keepAlive beanName="explorerBean"/>
            <a4j:keepAlive beanName="preparaBancoVazioBean"/>
            <center>
                <br>
                <br>
                <br>
                <h:form prependId="false" id="f_messagens">                  
                    <a4j:outputPanel ajaxRendered="true">
                        <rich:messages infoClass="info"/>
                    </a4j:outputPanel>
                </h:form>
                <h:form id="formVoltarTop">
                    <center>
                        <h:commandLink action="#{usuarioBean.sair}">
                            <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                        </h:commandLink>
                    </center>
                </h:form>
                <h:panelGrid columns="1" columnClasses="gridContent">
                    <rich:tabPanel id="tabPanelMain" switchType="client" width="965" selectedTab="#{explorerBean.abaCorrente}">
                        <br>
                        <rich:tab id="tabPadroes" label="Padrões" rendered="#{usuarioBean.isAtivo}" >
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabPadroes"/>
                            </a4j:support>
                            <br>
                            <rich:tabPanel id="tabPanelPermissao" switchType="client" width="965" selectedTab="#{explorerBean.subAbaCorrente}">
                                <rich:tab id="tabPermissoes1" label="Permissões" >
                                    <a4j:support event="ontabenter" action="#{explorerBean.setSubAba}" reRender="f_messagens">
                                        <a4j:actionparam name="subTab" value="tabPermissoes1"/>
                                    </a4j:support>
                                    <h:form id="formPermissoes">
                                        <a4j:support event="ontabenter" reRender="perfilManu"
                                                     action="#{permissaoBean.reConstroi}"/>
                                        <br>
                                        <a4j:region id="region1">
                                            <a4j:status id="progressoEmAberto"  for="region1" onstart="Richfaces.showModalPanel('panelStatus');" onstop="#{rich:component('panelStatus')}.hide()"/>
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
                                            <br>
                                            <br>
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
                                    </h:form>
                                </rich:tab>



                                <rich:tab id="tabUsuariosPerfis" label="Perfis">
                                    <a4j:support event="ontabenter" action="#{explorerBean.setSubAba}" reRender="f_messagens">
                                        <a4j:actionparam name="subTab" value="tabUsuariosPerfis"/>
                                    </a4j:support>                                        

                                    <h:form id="formUsuariosPerfis">
                                        <center>
                                            <br>
                                            <a4j:region id="PerfisRegion">

                                                <h:panelGrid columns="1" >
                                                    <h:outputText value="Selecione o Perfil: " styleClass="label"/>
                                                    <h:selectOneMenu id="perfisList" value="#{perfilBean.perfilSelecionado}">
                                                        <f:selectItems value="#{perfilBean.perfisList}"/>
                                                        <a4j:support event="onchange" action="#{perfilBean.consultaPermissoesPerfil}"
                                                                     reRender="perfisGroup,perfilBotoesGrid"/>
                                                    </h:selectOneMenu>
                                                </h:panelGrid>
                                                <br>

                                                <h:panelGrid columns="5" id="perfilBotoesGrid">
                                                    <h:panelGrid columns="1" style="text-align:center;float:center" >
                                                        <h:outputLink  value="#" id="linkAddPerfil" style="float:center">
                                                            <h:graphicImage value="../images/add_verde_24.png" style="border:0"/>
                                                            <rich:componentControl for="addPerfilPanel" attachTo="linkAddPerfil" operation="show" event="onclick"/>
                                                            <a4j:support event="onclick" reRender="addPerfilGrid"
                                                                         action="#{perfilBean.showPerfilNovo}"/>
                                                        </h:outputLink>
                                                        <h:outputText value="Adicionar"/>
                                                    </h:panelGrid>
                                                    <rich:spacer width="20"/>
                                                    <h:panelGrid id="BotaoRenomearPerfil" style="text-align:center;float:center" width="50">
                                                        <center>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center">
                                                                <a4j:outputPanel id="editPerfilOutputPanel">
                                                                    <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                 rendered="#{perfilBean.perfilSelecionado != -1&&perfilBean.perfilSelecionado != null&&perfilBean.perfilSelecionado != 1}">
                                                                        <center>
                                                                            <h:outputLink  value="#" id="linkRenomearPerfil" style="float:center">
                                                                                <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                                                <rich:componentControl for="renomearPerfilPanel" attachTo="linkRenomearPerfil" operation="show" event="onclick"/>
                                                                                <a4j:support event="onclick" action = "#{perfilBean.showEditar}" reRender="editPerfilGrid"/>
                                                                            </h:outputLink>
                                                                            <h:outputText value="Editar" />
                                                                        </center>
                                                                    </h:panelGrid>
                                                                    <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                                 rendered="#{perfilBean.perfilSelecionado == -1 || perfilBean.perfilSelecionado == null||perfilBean.perfilSelecionado == 1}">
                                                                        <center>
                                                                            <h:graphicImage value="../images/edit_dent_fosco.png" style="border:0"/>
                                                                            <h:outputText value="Editar" />
                                                                        </center>
                                                                    </h:panelGrid>
                                                                </a4j:outputPanel>

                                                            </h:panelGrid>
                                                        </center>
                                                    </h:panelGrid>
                                                    <rich:spacer width="20"/>
                                                    <a4j:outputPanel id="deletePerfilOutputPanel">
                                                        <center>
                                                            <h:panelGrid columns="1" style="text-align:center;float:center"         rendered="#{perfilBean.perfilSelecionado != -1&&perfilBean.perfilSelecionado != null && perfilBean.perfilSelecionado != 1}">
                                                                <center>
                                                                    <h:commandButton  value="Excluir" id="excluirPerfil" image="../images/delete_24.png"
                                                                                      onclick="javascript:if (!confirm('Realmente deseja excluir?')) return false;"
                                                                                      action="#{perfilBean.excluirPerfil}"/>
                                                                    <h:outputText value="Excluir" />
                                                                </center>
                                                            </h:panelGrid>

                                                            <h:panelGrid columns="1" style="text-align:center;float:center"
                                                                         rendered="#{perfilBean.perfilSelecionado == -1 || perfilBean.perfilSelecionado == null || perfilBean.perfilSelecionado == 1}">
                                                                <center>
                                                                    <h:graphicImage value="../images/delete_transp_24.png" style="border:0"/>
                                                                    <h:outputText value="Excluir" />
                                                                </center>
                                                            </h:panelGrid>
                                                        </center>
                                                    </a4j:outputPanel>

                                                </h:panelGrid>
                                                <rich:modalPanel id="addPerfilPanel" width="300" height="150" autosized="true" styleClass="center">

                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="Novo Perfil"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkc23"/>
                                                            <rich:componentControl for="addPerfilPanel" attachTo="hidelinkc23" operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid id = "addPerfilGrid">
                                                            <center>
                                                                <br>
                                                                <h:outputText value="Nome do perfil: " styleClass="label"/>
                                                                <h:inputText id="novoPerfilNome" value="#{perfilBean.novoPerfil}"/>
                                                                <br>
                                                            </center>
                                                        </h:panelGrid>
                                                        <br>
                                                        <h:commandButton  value="Salvar" id="salvarNovoPerfil"
                                                                          action="#{perfilBean.salvarNovoPerfil}"                                                                         >

                                                            <rich:componentControl for="addPerfilPanel"  attachTo="salvarNovoPerfil"
                                                                                   operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>

                                                </rich:modalPanel>
                                                <rich:modalPanel id="renomearPerfilPanel" width="300" height="150" autosized="true" styleClass="center">
                                                    <f:facet name="header">
                                                        <h:panelGroup>
                                                            <h:outputText value="Editar Perfil"></h:outputText>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <f:facet name="controls">
                                                        <h:panelGroup>
                                                            <h:graphicImage value="/images/close.gif" styleClass="hidelink" id="hidelinkcc8"/>
                                                            <rich:componentControl for="renomearPerfilPanel" attachTo="hidelinkcc8" operation="hide" event="onclick"/>
                                                        </h:panelGroup>
                                                    </f:facet>
                                                    <center>
                                                        <h:panelGrid id = "editPerfilGrid">
                                                            <center>
                                                                <br>
                                                                <h:outputText value="Nome do perfil: " styleClass="label"/>
                                                                <h:inputText id="editPerfilID" value="#{perfilBean.nomeEditPerfil}"/>
                                                                <br>
                                                            </center>
                                                        </h:panelGrid>
                                                        <br>
                                                        <h:commandButton  value="Salvar" id="salvarEditPerfil"
                                                                          action="#{perfilBean.salvarEditPerfil}">

                                                            <rich:componentControl for="renomearPerfilPanel"  attachTo="salvarEditPerfil"
                                                                                   operation="hide" event="onclick"/>
                                                        </h:commandButton>
                                                    </center>
                                                </rich:modalPanel>

                                                <h:panelGroup id="perfisGroup">
                                                    <br>

                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid1" columns="5"  columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">

                                                            <h:graphicImage value="../images/consulta_individual_24x24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Consulta Individual: " styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Frequência com Escala "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="freqnComEscala" value="#{perfilBean.perfilEdit.freqnComEscala}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Frequência sem Escala "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="freqnSemEscala" value="#{perfilBean.perfilEdit.freqnSemEscala}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Hora Extra "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="horaExtra" value="#{perfilBean.perfilEdit.horaExtra}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Mostrar resumo "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="showResumo" value="#{perfilBean.perfilEdit.showResumo}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid2" columns="5"  columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">

                                                            <h:graphicImage value="../images/jornadas_24x24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Jornadas: " styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Horários "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkHorarios" value="#{perfilBean.perfilEdit.horarios}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Jornadas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkJornadas" value="#{perfilBean.perfilEdit.jornadas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Cronogramas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="cronogramas" value="#{perfilBean.perfilEdit.cronogramas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Afastamento "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="afastamento" value="#{perfilBean.perfilEdit.afastamento}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid3" columns="5"  columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">

                                                            <h:graphicImage value="../images/relatorios_24x24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Relatorios:"  styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Mensal com escala "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioMensComEscala" value="#{perfilBean.perfilEdit.relatorioMensComEscala}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Mensal sem escala "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioMensSemEscala" value="#{perfilBean.perfilEdit.relatorioMensSemEscala}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Resumo de frequências "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioDeResumodeFrequencias" value="#{perfilBean.perfilEdit.relatorioDeResumodeFrequencias}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Resumo de escalas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioDeResumodeEscalas" value="#{perfilBean.perfilEdit.relatorioDeResumodeEscalas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Resumo de catracas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioDeCatracas" value="#{perfilBean.perfilEdit.relatorioCatracas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Log de Operações "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relatorioLogDeOperacoes" value="#{perfilBean.perfilEdit.relatorioLogDeOperacoes}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="AFDT "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkafdt" value="#{perfilBean.perfilEdit.afdt}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="ACJEF "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkacjef" value="#{perfilBean.perfilEdit.acjef}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Espelho de Ponto "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkEspelhoDePonto" value="#{perfilBean.perfilEdit.espelhoDePonto}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Configurações "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="configuracoes" value="#{perfilBean.perfilEdit.relatorioConfiguracao}"/>

                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid5" columns="5" columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                            <h:graphicImage value="../images/lista_de_presenca_24x24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Presença"   styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Lista de Presença:"   />
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="listaDePresenca" value="#{perfilBean.perfilEdit.listaDePresenca}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Consulta de Irregulares:"   />
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="consultaIrregulares" value="#{perfilBean.perfilEdit.consultaIrregulares}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Consulta Hora Extra:"   />
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="consultaHoraExtra" value="#{perfilBean.perfilEdit.consultaHoraExtra}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid4" columns="5"  columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">

                                                            <h:graphicImage value="../images/abono_24x24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Abonos"   styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Solicitação"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkSolicitacao" value="#{perfilBean.perfilEdit.solicitacao}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Dias em Aberto"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="diasEmAberto" value="#{perfilBean.perfilEdit.diasEmAberto}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Abono Rápido"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="abonoRapido" value="#{perfilBean.perfilEdit.abonosRapidos}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Histórico"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkHistoricoAbono" value="#{perfilBean.perfilEdit.historicoAbono}" />
                                                            <%--<a4j:support event="onchange" reRender="perfisGrid4"/>--%>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Exclusão de Abonos " />

                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="exclusaoAbonoCheck" value="#{perfilBean.perfilEdit.exclusaoAbono}" />
                                                            <%--
                                                            <h:selectBooleanCheckbox id="exclusaoAbonoCheck" value="#{perfilBean.perfilEdit.exclusaoAbono}" rendered="#{perfilBean.perfilEdit.historicoAbono==true}"/>
                                                            <h:selectBooleanCheckbox id="exclusaoAbonoCheckFalse" value="#{false}" disabled="true" rendered="#{perfilBean.perfilEdit.historicoAbono==false}"/>--%>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Abonos em Massa "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="abonosEmMassaCheck" value="#{perfilBean.perfilEdit.abonosEmMassa}"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Ranking de Abonos "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="rankingAbonoCheck" value="#{perfilBean.perfilEdit.rankingAbono}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Pesquisar Data Inicial Final "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="pesquisarDataInicialFinal" value="#{perfilBean.perfilEdit.pesquisarData}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>

                                                    <%--<br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid6" columns="5" columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                            <h:graphicImage value="../images/database_24.png" style="border:0"/>
                                                            <rich:spacer width="20"/>
                                                            <h:outputText value="Banco de Dados:"   styleClass="label"/>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="checkBancoDeDados" value="#{perfilBean.perfilEdit.bancoDeDados}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>--%>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid0" columns="5"  columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">

                                                            <h:graphicImage value="../images/config_24.png" style="border:0"/>
                                                            <rich:spacer width="1"/>
                                                            <h:outputText value="Cadastros e Configurações: " styleClass="label"/>
                                                            <rich:spacer width="5"/>
                                                            <rich:spacer width="1"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Permissões "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="permissoesCheck" value="#{perfilBean.perfilEdit.permissoes}"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Funcionários "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="funcionariosCheck" value="#{perfilBean.perfilEdit.funcionarios}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Empresas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="empresasCheck" value="#{perfilBean.perfilEdit.empresas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Departamentos "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="departamentosCheck" value="#{perfilBean.perfilEdit.deptos}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Cargos "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="cargosCheck" value="#{perfilBean.perfilEdit.cargos}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Feriados "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="feriadosCheck" value="#{perfilBean.perfilEdit.feriados}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Justificativas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="justificativasCheck" value="#{perfilBean.perfilEdit.justificativas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Verbas "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="verbasCheck" value="#{perfilBean.perfilEdit.verbas}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Categoria Afastamento"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="CategoriaAfastamento" value="#{perfilBean.perfilEdit.categoriaAfastamento}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Título do Relatório "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="tituloDoRelatorioCheck" value="#{perfilBean.perfilEdit.tituloDoRelatorio}"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Hora Extra e Gratificações: "/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="horaExtraEGratificacoesCheck" value="#{perfilBean.perfilEdit.horaExtraEGratificacoes}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid7" columns="5" columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                            <h:graphicImage value="../images/pontoClock30.png" style="border:0"/>
                                                            <rich:spacer width="20"/>
                                                            <h:outputText value="Manutenção:"   styleClass="label"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Lista de Relógios"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relogiosCheck" value="#{perfilBean.perfilEdit.listaRelogios}"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Coleta pelo AFD"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="afdCheck" value="#{perfilBean.perfilEdit.downloadAfd}"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Scan de Ip's"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="scanIpCheck" value="#{perfilBean.perfilEdit.scanIP}"/>
                                                            
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Relogios de Ponto"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="relogioPontoCheck" value="#{perfilBean.perfilEdit.relogioPonto}"/>

                                                        </h:panelGrid>
                                                    </rich:panel>
                                                    <br>
                                                    <rich:panel rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                        <h:panelGrid id="perfisGrid8" columns="5" columnClasses="top,top,perfil,top,top"
                                                                     rendered="#{perfilBean.perfilSelecionado!= -1 &&perfilBean.perfilSelecionado!=null}">
                                                            <h:graphicImage value="../images/edit_dent.png" style="border:0"/>
                                                            <rich:spacer width="20"/>
                                                            <h:outputText value="Outros:"   styleClass="label"/>
                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>

                                                            <rich:spacer width="20"/>
                                                            <rich:spacer width="20"/>
                                                            <h:panelGroup>
                                                                <rich:spacer width="10"/>
                                                                <h:outputText value="Autenticar Serial"/>
                                                            </h:panelGroup>
                                                            <rich:spacer width="20"/>
                                                            <h:selectBooleanCheckbox id="serialCheck" value="#{perfilBean.perfilEdit.autenticaSerial}"/>
                                                        </h:panelGrid>
                                                    </rich:panel>                                                    
                                                    <h:commandButton value="Salvar Alterações" id="salvarAlteracoesPerfil" action="#{perfilBean.salvarAlteracoesPerfil}"/>
                                                </h:panelGroup>
                                            </a4j:region>
                                        </center>
                                    </h:form>
                                </rich:tab>



                                <rich:tab id="tabUsuariosSenhas" label="Complexidade das Senhas">
                                    <a4j:support event="ontabenter" action="#{explorerBean.setSubAba}" reRender="f_messagens">
                                        <a4j:actionparam name="subTab" value="tabUsuariosSenhas"/>
                                    </a4j:support>
                                    <h:form id="formUsuariosSenhas">
                                        <center>
                                            <br>
                                            <a4j:region id="SenhasRegion">
                                                <h:panelGrid columns="2" >
                                                    <h:outputText value="Tamanho Mínimo: " styleClass="label"/>
                                                    <rich:comboBox defaultLabel="0" value="#{novoUsuarioBean.needSize}" style="">
                                                        <f:selectItem itemValue="0"/>
                                                        <f:selectItem itemValue="6"/>
                                                        <f:selectItem itemValue="7"/>
                                                        <f:selectItem itemValue="8"/>
                                                        <f:selectItem itemValue="9"/>
                                                        <f:selectItem itemValue="10"/>
                                                        <a4j:support event="onchange"/>
                                                    </rich:comboBox>

                                                    <h:outputText value="Nº mínimo de letras maiúsculas: " styleClass="label"/>
                                                    <rich:comboBox defaultLabel="0" value="#{novoUsuarioBean.needMaiusculas}" style="">
                                                        <f:selectItem itemValue="0"/>
                                                        <f:selectItem itemValue="1"/>
                                                        <f:selectItem itemValue="2"/>
                                                        <f:selectItem itemValue="3"/>
                                                        <a4j:support event="onchange"/>
                                                    </rich:comboBox>

                                                    <h:outputText value="Nº mínimo de números: " styleClass="label"/>
                                                    <rich:comboBox defaultLabel="0" value="#{novoUsuarioBean.needNumeros}" style="">
                                                        <f:selectItem itemValue="0"/>
                                                        <f:selectItem itemValue="1"/>
                                                        <f:selectItem itemValue="2"/>
                                                        <f:selectItem itemValue="3"/>
                                                        <a4j:support event="onchange"/>
                                                    </rich:comboBox>

                                                    <h:outputText value="Nº mínimo de símbols: " styleClass="label"/>
                                                    <rich:comboBox defaultLabel="0" value="#{novoUsuarioBean.needSimbolos}" style="">
                                                        <f:selectItem itemValue="0"/>
                                                        <f:selectItem itemValue="1"/>
                                                        <f:selectItem itemValue="2"/>
                                                        <f:selectItem itemValue="3"/>
                                                        <a4j:support event="onchange"/>
                                                    </rich:comboBox>
                                                </h:panelGrid>
                                                <h:outputText value="Para retirar a restrição, selecionar 'zero'" styleClass="label" style="color:red"/>
                                                <br>
                                                <br>
                                                <h:commandButton value="Salvar Alterações" id="salvarAlteracoesSenhas" action="#{novoUsuarioBean.salvarAlteracoesSenha}"/>
                                            </a4j:region>
                                        </center>
                                    </h:form>
                                </rich:tab>



                                <rich:tab id="tabComportamentoSistema" label="Opções de Sistema">
                                    <a4j:support event="ontabenter" action="#{explorerBean.setSubAba}" reRender="f_messagens">
                                        <a4j:actionparam name="subTab" value="tabUsuariosSenhas"/>
                                    </a4j:support>
                                    <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                        <a4j:actionparam name="tab" value="tabComportamentoSistema"/>
                                    </a4j:support>
                                    <h:form id="formComportamentoSistema">
                                        <center>
                                            <a4j:region id="opcoesRegion">

                                                <h:outputText value="Definições do sistema" styleClass="label"/>
                                                <h:panelGrid columns="3" >
                                                    <h:outputText value="Permitir batidas com matricula: " styleClass="label"/>
                                                    <h:selectBooleanCheckbox id="checkWithPIN" value="#{configuracoesBean.checkWithPIN}"/>
                                                    <h:commandLink value="Preparar Alteração" style="color:red" action="#{configuracoesBean.CorrigePIN()}"/>    
                                                </h:panelGrid>
                                                <h:commandButton value="Salvar Alterações" id="salvarConfig" action="#{configuracoesBean.SaveDefinitions}"/>
                                            </a4j:region>
                                        </center>
                                    </h:form>
                                </rich:tab>

                            </rich:tabPanel>
                        </rich:tab>

                        <rich:tab id="tabSenhaAdministrador" label="Mudar Senha de Administrador" rendered="#{usuarioBean.isAtivo}">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabSenhaAdministrador"/>
                            </a4j:support>                            
                            <h:form id="formMudarSenhaAdmin">
                                <br>
                                <center>
                                    <h:panelGrid columnClasses="gridContent">
                                        <rich:panel>
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <center>
                                                            <h:outputLabel value="MUDAR SENHA"/>
                                                        </center>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <h:panelGrid columns="2">
                                                    <h:outputText value="Senha Atual: " styleClass="label"/>
                                                    <h:inputSecret id="senhaAtual"  value="#{configuracoesBean.senhaAtual}" size="11" maxlength="11"/>
                                                    <h:outputText value="Nova Senha: " styleClass="label"/>
                                                    <h:inputSecret id="senhaNova"  value="#{configuracoesBean.senhaNova}" size="11" maxlength="11"/>
                                                    <h:outputText value="Confirmar Senha: " styleClass="label"/>
                                                    <h:inputSecret id="senhaNovaConfirmacao"  value="#{configuracoesBean.senhaNovaConfirmacao}" size="11" maxlength="11"/>
                                                </h:panelGrid>
                                                <br>
                                                <h:panelGroup>
                                                    <rich:spacer width="10"/>
                                                    <h:commandButton value="Confirmar" actionListener="#{configuracoesBean.alterarSenha}"/>
                                                </h:panelGroup>
                                            </center>
                                        </rich:panel>
                                    </h:panelGrid>
                                </center>
                            </h:form>
                        </rich:tab>



                        <rich:tab id="tabBancoDeDados" label="Banco de Dados">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabBancoDeDados"/>
                            </a4j:support>                            
                            <h:form id="formBancoDados">
                                <br>
                                <center>
                                    <h:panelGrid columnClasses="gridContent">
                                        <rich:panel style="text-align: center;">
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="CONEXÃO COM O BANCO DE DADOS"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <h:panelGrid columns="3" style="text-align:center;float:center;">
                                                    <h:outputText value="Servidor: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="servidor"  value="#{conexaoBean.conexao.server}" size="20"/>

                                                    <h:outputText value="Porta: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="porta"  value="#{conexaoBean.conexao.port}" size="20"/>

                                                    <h:outputText value="Banco de Dados: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="database"  value="#{conexaoBean.conexao.database}" size="20"/>


                                                    <h:outputText value="Usuário: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="usuario"  value="#{conexaoBean.conexao.usuario}" size="20"/>

                                                    <h:outputText value="Senha: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputSecret id="senha" redisplay="true" value="#{conexaoBean.conexao.senha}" size="20"/>
                                                </h:panelGrid>
                                            </center>
                                            <center>
                                                <h:panelGrid columns="2">
                                                    <h:panelGrid columns="1" style="text-align:center;float:center">
                                                        <h:commandLink type="submit" action="#{conexaoBean.salvar}" style="float:center">
                                                            <h:graphicImage  value="../images/conectar.png" style="border:0"/>
                                                        </h:commandLink>
                                                        <h:outputLabel value="Conectar" styleClass="label"/>
                                                    </h:panelGrid>

                                                    <h:panelGrid columns="1" style="text-align:center;float:center">
                                                        <center>
                                                            <h:commandLink type="submit" action="#{preparaBancoVazioBean.preparar}" style="float:center">
                                                                <h:graphicImage  value="../images/config_32.png" style="border:0"/>
                                                            </h:commandLink>
                                                            <h:outputLabel value="Preparar Banco" styleClass="label"/>
                                                        </center>
                                                    </h:panelGrid>
                                                </h:panelGrid>
                                            </center>
                                        </rich:panel>
                                    </h:panelGrid>
                                </center>
                            </h:form>
                        </rich:tab>  

                        <rich:tab id="tabBackUp" label="Backup">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabBackUp"/>
                            </a4j:support>                              
                            <h:form id="formBackup">

                                <br>
                                <center>
                                    <h:panelGrid columnClasses="gridContent">
                                        <rich:panel style="text-align: center;">
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="ROTINA DE BACKUP"/>
                                                    </h:panelGroup>
                                                </f:facet>                                             

                                                <h:panelGrid columns="3" style="text-align:center;float:center;">
                                                    <h:outputText value="Email do destinatário " styleClass="label" style="float:right" />
                                                    <rich:spacer width="10"/>
                                                    <h:inputText  value="#{backupBean.backupConfig.destinatario}" size="25" maxlength="40"/>

                                                    <h:outputText value="Email do remetente " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText  value="#{backupBean.backupConfig.remetente}" size="25" maxlength="40"/>

                                                    <h:outputText value="Senha do remetente " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputSecret  redisplay="true" value="#{backupBean.backupConfig.senha}" size="25" maxlength="20"/>

                                                    <h:outputText value="Servidor Smtp " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText  value="#{backupBean.backupConfig.smtp}" size="25" maxlength="20"/>

                                                    <h:outputText value="Porta: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText  value="#{backupBean.backupConfig.port}" size="25" maxlength="8"/>

                                                    <h:outputText value="Backup só das alterações: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:selectBooleanCheckbox value="#{backupBean.backupConfig.diferencial}" style="float:left;text-align:left"/>

                                                    <h:outputText value="SSL: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:selectBooleanCheckbox value="#{backupBean.backupConfig.ssl}" style="float:left;text-align:left"/>

                                                    <h:outputText value="Nome da empresa " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText  value="#{backupBean.backupConfig.empresa}" size="25"/>

                                                    <h:outputText value="Caminho do backup " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText value="#{backupBean.backupConfig.caminho}" size="25" maxlength="90" />

                                                    <h:outputText value="Hora do primeiro backup: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="horaBackup" size="2" value="#{backupBean.backupConfig.horaBackup}" maxlength="20"
                                                                 onkeyup="mascara_hora(this.id)" style="float:left" onblur="mascara_horaBlur(this.id)">
                                                        <rich:jQuery selector="#horaBackup" query="mask('99:99')" timing="onload"/>
                                                    </h:inputText>

                                                    <h:outputText value="Período de backup (em horas) " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <rich:inputNumberSpinner value="#{backupBean.backupConfig.intervaloBackup}"
                                                                             inputSize="3" maxValue="1000" minValue="1" />
                                                </h:panelGrid>
                                                <br>
                                                <h:graphicImage  value="../images/on.png" rendered="#{backupBean.isAtivo}" style="border:0"/>
                                                <h:graphicImage  value="../images/off.png" rendered="#{!backupBean.isAtivo}" style="border:0"/>
                                                <br><br>
                                                <h:panelGrid columns="5" style="text-align:center;float:center;">
                                                    <h:commandButton value="Testar" action="#{backupBean.teste}"/>
                                                    <rich:spacer width="15"/>
                                                    <h:commandButton value="Ativar" action="#{backupBean.salvar}"/>
                                                    <rich:spacer width="15"/>
                                                    <h:commandButton value="Cancelar" action="#{backupBean.cancelar}"/>                                                    
                                                </h:panelGrid>
                                            </center>                                           
                                        </rich:panel>
                                    </h:panelGrid>
                                </center>
                            </h:form>
                        </rich:tab>


                        <rich:tab id="tabRelogios" label="Relógios de Ponto">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabRelogios"/>
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

                        <rich:tab id="tabExplorer" label="Explorer">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabExplorer"/>
                            </a4j:support>                            
                            <h:form id="formLog">
                                <center>

                                    <a4j:outputPanel id="panelLog">
                                        <h:selectOneMenu id="lbUnidade" value="#{explorerBean.listBoxPasta}">
                                            <f:selectItem id="unidadeTomcat" itemLabel="tomcat" itemValue="T"/>
                                            <f:selectItem id="unidadeC" itemLabel="C:" itemValue="C" />
                                            <f:selectItem id="unidadeD" itemLabel="D:" itemValue="D" />
                                            <f:selectItem id="unidadeE" itemLabel="E:" itemValue="E" />
                                            <f:selectItem id="unidadeF" itemLabel="F:" itemValue="F" />  
                                            <a4j:support event="onchange" reRender="dLog"/>

                                        </h:selectOneMenu>
                                        <rich:extendedDataTable id="dLog" selection="#{explorerBean.selectLog}" value="#{explorerBean.logList}" var="logRow">
                                            <rich:column>
                                                <a4j:commandLink action="#{explorerBean.abrirPasta}" value="#{logRow}" reRender="panelLog">
                                                    <f:param name="alvo" value="#{logRow}"/>
                                                </a4j:commandLink>
                                            </rich:column>
                                            <rich:column>
                                                <h:commandLink action="#{explorerBean.abrirArquivo}" value="abrir">
                                                    <f:param name="alvo" value="#{logRow}"/>
                                                </h:commandLink>
                                            </rich:column>
                                            <rich:column>
                                                <h:commandLink action="#{explorerBean.deletarArquivo}" value="deletar">
                                                    <f:param name="alvo" value="#{logRow}"/>
                                                </h:commandLink>
                                            </rich:column>                                            
                                        </rich:extendedDataTable>
                                    </a4j:outputPanel>
                                </center>
                            </h:form>
                        </rich:tab>


                        <rich:tab id="tabAD" label="Active Directory">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabAD"/>
                            </a4j:support>                            
                            <h:form id="formAD">
                                <center>
                                    <h:panelGrid columnClasses="gridContent">
                                        <rich:panel style="text-align: center;">
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Configurar Active Directory"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <a4j:region id="insertADRegion">

                                                    <h:panelGrid columns="1" style="text-align:center;float:center;">
                                                        <center>
                                                            <h:outputText value="Insira o IP do AD por extenso: " styleClass="label" style="float:right" />
                                                            <h:outputText value="(ex. 172.010.000.001)" styleClass="label" />
                                                            <br>
                                                            <h:inputText id="ipAD" size="12" value="#{empresaBean.ipAD}">                                                
                                                                <rich:jQuery selector="#ipAD" query="mask('999.999.999.999')" timing="onload"/>
                                                            </h:inputText>
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton value="Ativar" action="#{empresaBean.salvarIpAD}"/>
                                                </a4j:region>
                                            </center>
                                        </rich:panel>

                                    </h:panelGrid>
                                </center>
                            </h:form>
                        </rich:tab>




                        <rich:tab id="tabDigit" label="Sistema de Captura">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabDigit"/>
                            </a4j:support>                            
                            <h:form id="formDigit">
                                <center>
                                    <h:panelGrid columnClasses="gridContent">
                                        <rich:panel style="text-align: center;">
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="Configurar Sistema de Captura"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <a4j:region id="insertCapturaRegion">

                                                    <h:panelGrid columns="1" style="text-align:center;float:center;">
                                                        <center>
                                                            <h:outputText value="Insira o IP e porta do terminal que captura digitais: " styleClass="label" style="float:right" />
                                                            <h:outputText value="(ex. 172.10.0.1:8050)" styleClass="label" />
                                                            <br>
                                                            <h:inputText id="ipCatcher" size="18" value="#{empresaBean.ipDigitCatcher}"/>                                                
                                                        </center>
                                                    </h:panelGrid>

                                                    <h:panelGrid columns="1" style="text-align:center;float:center;">
                                                        <center>
                                                            <h:outputText value="Insira o IP e porta do servidor que distribui as digitais: " styleClass="label" style="float:right" />
                                                            <h:outputText value="(ex. 172.10.0.1:8050)" styleClass="label" />
                                                            <br>
                                                            <h:inputText id="ipSender" size="18" value="#{empresaBean.ipDigitSender}"/>                                                
                                                        </center>
                                                    </h:panelGrid>
                                                    <br>
                                                    <h:commandButton value="Ativar" action="#{empresaBean.salvarIpDigitSenderCatcher}"/>
                                                </a4j:region>
                                            </center>
                                        </rich:panel>

                                    </h:panelGrid>
                                </center>
                            </h:form>
                        </rich:tab>


                        <rich:tab id="tabExtraConections" label="Conexões Externas">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabExtraConections"/>
                            </a4j:support>                            
                            <h:form id="formExtraConections">
                                <a4j:support event="ontabenter" reRender="extraConnList, extraConnGrid"
                                             action="#{conexaoBean.reConstroi}"/>
                                <center>
                                    <rich:panel id="panelExtraConn" style="text-align: center;">
                                        <f:facet name="header">
                                            <h:panelGroup>
                                                <h:outputText value="Conexão Externa"/>
                                            </h:panelGroup>
                                        </f:facet>

                                        <center>
                                            <h:panelGrid id="extraConnGrid" columns="3" style="text-align:center;float:center;">
                                                <h:outputText value="Servidor: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="extraServidor"  value="#{conexaoBean.extraConn.server}" size="30"/>

                                                <h:outputText value="Porta: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="extraPorta"  value="#{conexaoBean.extraConn.port}" size="30"/>

                                                <h:outputText value="Banco de Dados: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="ExtraDatabase"  value="#{conexaoBean.extraConn.database}" size="30"/>

                                                <h:outputText value="Usuário: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="ExtraUsuario"  value="#{conexaoBean.extraConn.usuario}" size="30"/>

                                                <h:outputText value="Senha: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="ExtraSenha" value="#{conexaoBean.extraConn.senha}" size="30"/>

                                                <h:outputText value="Alias: " styleClass="label" style="float:right"/>
                                                <rich:spacer width="10"/>
                                                <h:inputText id="ExtraAlias" value="#{conexaoBean.extraConn.desc}" size="30"/>
                                            </h:panelGrid>
                                        </center>
                                        <center>
                                            <h:panelGrid columns="5">

                                                <h:panelGrid columns="1">
                                                    <center>
                                                        <h:commandLink type="submit" action="#{conexaoBean.adicionar}">
                                                            <h:graphicImage  value="../images/add2.png" style="border:0"/>
                                                            <a4j:support event="onClick" reRender="panelExtraConn"/>
                                                        </h:commandLink>
                                                        <h:outputLabel value="Novo" styleClass="labelRight"/>
                                                    </center>
                                                </h:panelGrid>

                                                <rich:spacer width="10"/>
                                                <h:panelGrid columns="1">
                                                    <center>
                                                        <h:commandLink type="submit" action="#{conexaoBean.editar}">
                                                            <h:graphicImage  value="../images/edit.gif" style="border:0"/>
                                                            <a4j:support event="onClick" reRender="panelExtraConn"/>
                                                        </h:commandLink>
                                                        <h:outputLabel value="Editar" styleClass="labelRight"/>
                                                    </center>
                                                </h:panelGrid>

                                                <rich:spacer width="10"/>
                                                <h:panelGrid columns="1">
                                                    <center>
                                                        <h:commandLink type="submit" action="#{conexaoBean.excluir}">
                                                            <h:graphicImage  value="../images/delete_24.png" style="border:0"/>
                                                            <a4j:support event="onClick" reRender="panelExtraConn"/>
                                                        </h:commandLink>
                                                        <h:outputLabel value="Excluir" styleClass="labelRight"/>
                                                    </center>
                                                </h:panelGrid>

                                            </h:panelGrid>
                                        </center>
                                        <center>
                                            <h:panelGroup>
                                                <rich:extendedDataTable id="extraConnList"
                                                                        selection="#{conexaoBean.selectConn}" enableContextMenu="false"
                                                                        value="#{conexaoBean.connList}" var="connRow"
                                                                        rowClasses="zebra1,zebra2"
                                                                        rows="10" width="65%">
                                                    <f:facet name="header">
                                                        <h:outputText value="Lista de Conexões"/>
                                                    </f:facet>
                                                    <a4j:support event="onRowClick" action="#{conexaoBean.showEditarConexao}"
                                                                 reRender="panelExtraConn">
                                                        <f:param name="idConn" value="#{connRow.connid}"/>
                                                    </a4j:support>
                                                    <rich:column sortBy="#{connRow.server}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Servidor"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.server}" />
                                                    </rich:column>

                                                    <rich:column sortBy="#{connRow.port}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Porta"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.port}" />
                                                    </rich:column>
                                                    <rich:column sortBy="#{connRow.database}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Banco de Dados"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.database}" />
                                                    </rich:column>
                                                    <rich:column sortBy="#{connRow.usuario}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Usuário"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.usuario}" />
                                                    </rich:column>
                                                    <rich:column sortBy="#{connRow.senha}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Senha"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.senha}" />
                                                    </rich:column>
                                                    <rich:column sortBy="#{connRow.desc}" style="text-align:center">
                                                        <f:facet name="header">
                                                            <h:outputText value="Alias"/>
                                                        </f:facet>
                                                        <h:outputText value="#{connRow.desc}" />
                                                    </rich:column>

                                                </rich:extendedDataTable>
                                            </h:panelGroup>
                                        </center>
                                    </rich:panel>
                                </center>
                            </h:form>
                        </rich:tab>
                        <rich:tab id="tabValidar" label="Validar">
                            <a4j:support event="ontabenter" action="#{explorerBean.setAba}" reRender="f_messagens">
                                <a4j:actionparam name="tab" value="tabValidar"/>
                            </a4j:support>
                            <h:form id="formMain">
                                <br/>
                                <center>                        
                                    <h:panelGrid columnClasses="gridContent">
                                        <br><br><br>
                                        <rich:panel style="text-align: center; width: 400">
                                            <center>
                                                <f:facet name="header">
                                                    <h:panelGroup>
                                                        <h:outputText value="REGISTRAR PONTO WEB"/>
                                                    </h:panelGroup>
                                                </f:facet>
                                                <h:panelGrid columns="3" style="text-align:center;float:center;">
                                                    <h:outputText value="Serial: " styleClass="label" style="float:right"/>
                                                    <rich:spacer width="10"/>
                                                    <h:inputText id="itSerial" value="#{usuarioBean.serial}" size="50"/>
                                                </h:panelGrid>
                                            </center>
                                            <center>
                                                <h:panelGrid columns="1">
                                                    <center>
                                                        <h:commandLink type="submit" action="#{usuarioBean.verificarSerial()}">
                                                            <h:graphicImage  value="../images/ok.png" style="border:0"/>
                                                        </h:commandLink>
                                                        <h:outputLabel value="Validar" styleClass="labelRight"/>
                                                    </center>
                                                </h:panelGrid>
                                            </center>
                                            <center>
                                                <h:panelGrid columns="1">
                                                    <center>
                                                        <h:outputText value="Valido até: #{usuarioBean.validade}" styleClass="label" style="float:right"/>            
                                                    </center>    
                                                </h:panelGrid>
                                            </center>    
                                        </rich:panel>
                                    </h:panelGrid>
                                    <h:panelGroup>
                                        <center>
                                            <h:commandLink action="login">
                                                <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                            </h:commandLink>
                                        </center>
                                    </h:panelGroup>
                                </center>
                            </h:form>
                        </rich:tab>


                    </rich:tabPanel>
                    <br>
                    <h:panelGroup>
                        <h:form id="formVoltarBottom">
                            <center>
                                <h:commandLink action="#{usuarioBean.sair}">
                                    <h:graphicImage  value="../images/voltar.png" style="border:0"/>
                                </h:commandLink>
                            </center>
                        </h:form>
                    </h:panelGroup>
                </center>
            </h:panelGrid>
            <center>
                <jsp:include page="../www/_bot.jsp"/>
            </center>
        </f:view>
    </body>
</html>
