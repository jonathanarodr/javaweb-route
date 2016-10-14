<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>home</title>
    </head>
    <body>
        <h1>Home!</h1>
        <br>
        <input type="text" name="param" id="param" /><button onclick="location.href='param/' + document.querySelector('input').value">go param</button>
        <br><br>
        <a href="../">index</a>
    </body>
</html>