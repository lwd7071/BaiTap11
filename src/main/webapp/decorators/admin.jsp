<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <!doctype html>
        <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width,initial-scale=1">
                <title>
                    ${fn:escapeXml(layoutTitle)}
                </title>
                <link rel="stylesheet" href="${fn:escapeXml(pageContext.request.contextPath)}/assets/css/app.css?v=20261001-1">
            </head>
            <body>
                <header class="top admin-top">
                    <a class="brand" href="${fn:escapeXml(pageContext.request.contextPath)}/admin/books">BOOK<span>STORE</span>
                    <small>ADMIN</small>
                </a>
                <nav>
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/admin/books">Quản lý sách</a>
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/admin/authors">Tác giả</a>
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/home">Trang người dùng</a>
                </nav>
                <a href="${fn:escapeXml(pageContext.request.contextPath)}/admin/orders">Quản lý đơn hàng</a>
                <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/logout"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
                    <button class="link-button">Đăng xuất</button>
                </form>
            </header>
            <main class="shell">
                <c:out value="${layoutBody}" escapeXml="false" />
            </main>
            <footer>Họ tên: <strong>Lương Viết Vĩ Đông</strong> · MSSV: 24110202 · Mã đề: 01</footer>
        </body>
    </html>
