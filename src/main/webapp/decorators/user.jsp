<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!doctype html>
        <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width,initial-scale=1">
                <title>
                    ${layoutTitle}
                </title>
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=20261001-1">
            </head>
            <body>
                <header class="top">
                    <a class="brand" href="${pageContext.request.contextPath}/home">BOOK<span>STORE</span>
                </a>
                <nav>
                    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                    <a href="${pageContext.request.contextPath}/home">Sản phẩm</a>
                    <c:if test="${sessionScope.currentUser.admin}">
                        <a href="${pageContext.request.contextPath}/admin/books">Quản trị</a>
                    </c:if>
                </nav>
                <div class="account">
                    <a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
                    <c:if test="${not empty sessionScope.currentUser}"><a href="${pageContext.request.contextPath}/orders">Đơn hàng</a></c:if>
                    <c:choose>
                        <c:when test="${not empty sessionScope.currentUser}">
                            <span>${sessionScope.currentUser.fullname}</span>
                            <form method="post" action="${pageContext.request.contextPath}/logout">
                                <button class="link-button">Đăng xuất</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
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
