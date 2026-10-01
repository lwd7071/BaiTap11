<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${fn:escapeXml(errorTitle)}</title>
</head>
<body>
    <main>
        <h1>${fn:escapeXml(errorTitle)}</h1>
        <p>${fn:escapeXml(errorMessage)}</p>
        <a href="${fn:escapeXml(pageContext.request.contextPath)}/home">Về trang chủ</a>
    </main>
</body>
</html>
