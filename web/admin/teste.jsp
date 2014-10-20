<%-- 
    Document   : teste
    Created on : 19/06/2012, 08:41:55
    Author     : prccardoso
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../javaScript/jquery.js"></script>
        <script type="text/javascript">
            var $j = jQuery.noConflict();
            function showDuty24(){
                $j("#duty24").show();
                $j("#duty12").hide();
            }
     
            function showDuty12(){
                $j("#duty12").show();
                $j("#duty24").hide();
            }
            
            
        </script>
        <title>JSP Page</title>
    </head>
    <body>
        <h1 id="duty12" style="display:block" onclick="showDuty24()">Hello 12!</h1>
        <h1 id="duty24" style="display:block" onclick="showDuty12()">Hello 24!</h1>
    </body>
</html>
