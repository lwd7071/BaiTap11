<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${errorTitle}</title>
</head>
<body>
    <main>
        <h1>${errorTitle}</h1>
        <p>${errorMessage}</p>
        <a href="${pageContext.request.contextPath}/home">Về trang chủ</a>
    </main>
</body>
</html>
