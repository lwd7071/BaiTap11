<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>Đăng nhập | Book Store</title>
        <form class="form-card" method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/login"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
            <div class="eyebrow">Chào mừng trở lại</div>
            <h1>Đăng nhập</h1>
            <c:if test="${not empty message}">
                <div class="notice errorbox">${fn:escapeXml(message)}</div>
            </c:if>
            <div class="field">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" maxlength="50" value="<c:out value='${email}'/>" required>
            </div>
            <div class="field">
                <label for="password">Mật khẩu</label>
                <input id="password" type="password" name="password" required>
            </div>
            <button class="button primary">Đăng nhập</button>
            <p class="auth-links">Chưa có tài khoản? <a href="${fn:escapeXml(pageContext.request.contextPath)}/register">Đăng ký</a>
        </p>
    </form>
