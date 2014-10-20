<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%--
    This file is an entry point for JavaServer Faces application.
--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link href="./css/default.css" rel="stylesheet" type="text/css" />
        <link href="./css/cssLayout.css" rel="stylesheet" type="text/css" />
        <link href="./css/cssTemplate.css" rel="stylesheet" type="text/css" />
        <title>JSP Page</title>
    </head>
    <body id="bg_body">
        <h:form> 
            <center>
                <h:panelGrid columns="1" rendered="#{not empty usuarioBean.usuario.nome}" >
                    <h:panelGrid columns="15" rendered="#{usuarioBean.ehAdministrador}" >
                        <h:panelGroup rendered="#{usuarioBean.perfil.consInd==true}">
                            <center>
                                <h:commandLink  action="navegarInicio" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/consulta_individual.png" style="border:0" />
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Frequência" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.consInd==true}"/>
                        <h:panelGroup rendered="#{usuarioBean.perfil.horCronoJorn==true}">
                            <center>
                                <h:commandLink action="navegarEscala" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/kontact_date_48.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Jornadas" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.horCronoJorn==true}"/>
                        <h:panelGroup rendered="#{usuarioBean.perfil.relatorios==true}">
                            <center><%-- navegarRelatorioMensal--%>
                                <h:commandLink action="navegarRelatorioMensal" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/mes.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Relatórios" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.relatorios==true}"/>
                        <%--      <h:commandLink value="Escalas" action="navegarEscala" rendered="#{usuarioBean.ehAdministrador}" styleClass="link"/>
                              <rich:spacer width="25"/>--%>
                        <h:panelGroup rendered="#{usuarioBean.perfil.presenca==true}">
                            <center>
                                <h:commandLink action="navegarPresenca" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/lista_de_presenca.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Presença" styleClass="labelRight"/>
                            </center>
                            <%--/h:panelGroup>
                              <rich:spacer width="60" rendered="#{usuarioBean.perfil.listaDePresenca==true}"/>
                            <h:panelGroup rendered="#{usuarioBean.perfil.listaDePresenca==true}">
                                <center>
                                    <h:commandLink action="navegarPagamento" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                        <h:graphicImage  value="../images/folha2.png" style="border:0"/>
                                    </h:commandLink>
                                    <br/>
                                    <h:outputLabel value="Folha" styleClass="labelRight"/>
                                </center--%>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.presenca==true}"/>
                        <h:panelGroup rendered="#{usuarioBean.perfil.abonos==true}">
                            <center>
                                <h:commandLink action="navegarAbono" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/abono.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Abonos" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.abonos==true}"/>
                        <h:panelGroup rendered="#{usuarioBean.perfil.cadastrosEConfiguracoes==true}">
                            <center>
                                <h:commandLink  action="navegarConfiguracoes" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/config_48.png" style="border:0" />
                                    <a4j:support action="#{consultaDiaEmAbertoBean.rotinasDeAtualização}"
                                                 event="onclick">
                                    </a4j:support>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Configurações" styleClass="labelRight"
                                               />
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="50" rendered="#{usuarioBean.perfil.cadastrosEConfiguracoes==true}"/>
                        <h:panelGroup rendered="#{usuarioBean.perfil.manutencao==true}">
                            <center>
                                <h:commandLink  action="navegarClockManager" rendered="#{usuarioBean.ehAdministrador}" styleClass="link">
                                    <h:graphicImage  value="../images/pontoClock.png" style="border:0" />
                                    
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Manutenção" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                        <rich:spacer width="40" rendered="#{usuarioBean.perfil.manutencao==true}"/>
                        <h:panelGroup>
                            <center>
                                <h:commandLink action="#{usuarioBean.sair}" styleClass="link">
                                    <h:graphicImage  value="../images/sair.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Sair" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>   
                    </h:panelGrid>

                    <h:panelGrid columns="4" rendered="#{not usuarioBean.ehAdministrador}" >
                        <rich:spacer width="850"/>
                        <h:panelGroup>
                            <center>
                                <h:commandLink action="#{usuarioBean.sair}" styleClass="link">
                                    <h:graphicImage  value="../images/sair.png" style="border:0"/>
                                </h:commandLink>
                                <br/>
                                <h:outputLabel value="Sair" styleClass="labelRight"/>
                            </center>
                        </h:panelGroup>
                    </h:panelGrid>
                    <br/>                    
                </h:panelGrid>                
            </center>
            <center>
                <br/> <b>
                    <h:panelGrid columns="1" width="965"  rendered="#{not empty usuarioBean.usuario.nome}" styleClass="italicoTop">
                        <h:outputText   value="Bem-vindo, #{usuarioBean.usuario.primeiroNome}!"/>
                        <br><h:outputText value="#{usuarioBean.versao}" /> 
                        <a4j:mediaOutput element="img" mimeType="#{file.mime}" rendered="#{fileUploadBean.logoExiste}"
                                         createContent="#{fileUploadBean.paint}" value="1"
                                         style="width:180px; height:100px;" cacheable="false">
                            <f:param value="#{fileUploadBean.timeStamp}" name="time"/>
                        </a4j:mediaOutput>
                    </h:panelGrid> </b>
            </center>     


            
        </h:form>
    </body>
</html>