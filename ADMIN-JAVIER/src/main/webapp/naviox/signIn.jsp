<%@page contentType="text/html; charset=UTF-8"%>
<%
    String ctx = request.getContextPath();
    String app = request.getParameter("application");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>ADMIN - SignIn</title>
    <link rel="stylesheet" href="<%= ctx %>/naviox/signin-custom.css" />
</head>
<body>

<!-- tarjeta de login centrada -->
<div class="signin-page">

    <div class="signin-top-img">
        <img src="<%= ctx %>/images/header.jpeg" alt="Header logo" class="app-header__img" />
    </div>

    <div class="signin-card">

        <!-- si quieres un título encima dentro de la card (opcional) -->
        <div class="card-title">Iniciar sesión</div>

        <!-- incluimos el módulo SignIn (mantiene funcionalidad) -->
        <div class="signin-module">
            <jsp:include page='<%="../xava/module.jsp?application=" + app + "&module=SignIn"%>'/>
        </div>

        <!-- pie pequeño (opcional) -->
        <div class="card-footer">¿No tienes cuenta? Contacta con tu administrador.</div>
    </div>
</div>

</body>
</html>
