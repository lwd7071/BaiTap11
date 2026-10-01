<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Đăng nhập | Book Store</title>
        <form class="form-card" method="post" action="${pageContext.request.contextPath}/login">
            <div class="eyebrow">Chào mừng trở lại</div>
            <h1>Đăng nhập</h1>
            <c:if test="${not empty message}">
                <div class="notice errorbox">${message}</div>
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
            <p class="auth-links">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </p>
    </form>
