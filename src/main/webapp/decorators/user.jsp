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
                <header class="top">
                    <a class="brand" href="${fn:escapeXml(pageContext.request.contextPath)}/home">BOOK<span>STORE</span>
                </a>
                <nav>
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/home#products">Sản phẩm</a>
                    <c:if test="${sessionScope.currentUser.admin}">
                        <a href="${fn:escapeXml(pageContext.request.contextPath)}/admin/books">Quản trị</a>
                    </c:if>
                </nav>
                <div class="account">
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/cart">Giỏ hàng</a>
                    <c:if test="${not empty sessionScope.currentUser}"><a href="${fn:escapeXml(pageContext.request.contextPath)}/orders">Đơn hàng</a></c:if>
                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser}">
                            <span>${fn:escapeXml(sessionScope.currentUser.fullname)}</span>
                            <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/logout"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
                                <button class="link-button">Đăng xuất</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a href="${fn:escapeXml(pageContext.request.contextPath)}/login">Đăng nhập</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </header>
            <main class="shell">
                <c:out value="${layoutBody}" escapeXml="false" />
            </main>
            <footer>Họ tên: <strong>Lương Viết Vĩ Đông</strong> · MSSV: 24110202 · Mã đề: 01</footer>
        </body>
    </html>
