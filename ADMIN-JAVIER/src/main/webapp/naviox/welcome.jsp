<%@page contentType="text/html; charset=UTF-8"%>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>ADMIN</title>

    <link rel="stylesheet" href="<%= ctx %>/naviox/welcome.css" />
</head>

<body class="wj-body">
<main class="wj-wrap">
    <img class="wj-logo" src="<%= ctx %>/images/header.jpeg" alt="Logo Javier" />

    <h1 class="wj-title">ADMIN</h1>

    <p class="wj-tip">
        Inicia sesión con el usuario: <b>admin</b>, contraseña: <b>admin</b>
    </p>

    <a class="wj-btn" href="<%= ctx %>/m/SignIn">INICIAR SESIÓN</a>
</main>
</body>
</html>
