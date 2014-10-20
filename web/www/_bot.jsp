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
        <link href="../css/default.css" rel="stylesheet" type="text/css" >
        <!--<link href="../css/cssLayout.css" rel="stylesheet" type="text/css" >-->
        <link href="../css/cssTemplate.css" rel="stylesheet" type="text/css" >
        <title>JSP Page</title>
    </head>
    <body id="bg_body">
        <rich:spacer/>
        <center>
            <br/>
            <h:panelGrid columns="3">
                <h:panelGroup>
                    <h:outputLabel value="SGN - Soluções em Gestão de Negócios" styleClass="labelMenorNegrito" />
                    <rich:spacer width="42" />
                    <h:outputLabel value="Sergipe Parque Tecnológico - SergipeTec" styleClass="labelMenorNegrito" />
                    <br/>
                    <%--h:outputLabel value="Portal:" styleClass="label" />
                    <a href="http://www.sgnsolucoes.com.br" target="_blank">
                        www.sgnsolucoes.com.br
                    </a--%>
                    <h:outputLabel value="E-mail: " styleClass="labelMenorNegrito" />
                    <h:outputLabel value="atendimento@sgnsolucoes.com.br" styleClass="labelMenor"/>
                    <rich:spacer width="30" />
                    <h:outputLabel value="Fone: " styleClass="labelMenorNegrito" />
                    <h:outputLabel value="(79) 3179-7731 / 3042-0157" styleClass="labelMenor"/>
                    <br/>

                    <h:outputLabel value="Endereço: " styleClass="labelMenorNegrito" />
                    <h:outputLabel value="Av. Dr. Carlos Rodrigues da Cruz, 826 - Bairro Capucho - Aracaju/SE" styleClass="labelMenor"/>
                    <br/>
                </h:panelGroup>
                <rich:spacer width="30" />
                <h:panelGroup>
                    <a href="http://www.sgnsolucoes.com.br" target="_blank"  class="labelMenor">
                        <img src="../images/sgnlogo.PNG" />
                    </a>
                </h:panelGroup>
            </h:panelGrid>
            <br/>
        </center>
    </body>
</html>
