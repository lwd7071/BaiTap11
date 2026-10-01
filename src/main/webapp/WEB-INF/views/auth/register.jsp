<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>Đăng ký | Book Store</title>
        <form class="form-card" method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/register"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
            <div class="eyebrow">Tạo tài khoản</div>
            <h1>Đăng ký</h1>
            <c:if test="${not empty message}">
                <div class="notice errorbox">${fn:escapeXml(message)}</div>
            </c:if>
            <div class="field">
                <label for="email">Email</label>
                <input id="email" type="email" name="email" maxlength="50" value="<c:out value='${param.email}'/>" required>
                <small class="error"><c:out value="${errors.email}"/></small>
            </div>
            <div class="field">
                <label for="fullname">Họ tên</label>
                <input id="fullname" name="fullname" maxlength="50" value="<c:out value='${param.fullname}'/>" required>
                <small class="error"><c:out value="${errors.fullname}"/></small>
            </div>
            <div class="field">
                <label for="phone">Điện thoại</label>
                <input id="phone" name="phone" inputmode="numeric" value="<c:out value='${param.phone}'/>"/>
                <small class="error"><c:out value="${errors.phone}"/></small>
            </div>
            <div class="field">
                <label for="password">Mật khẩu</label>
                <input id="password" type="password" name="password" minlength="6" maxlength="32" required>
                <small class="error"><c:out value="${errors.password}"/></small>
            </div>
            <div class="field">
                <label for="confirmPassword">Nhập lại mật khẩu</label>
                <input id="confirmPassword" type="password" name="confirmPassword" required>
                <small class="error"><c:out value="${errors.confirmPassword}"/></small>
            </div>
            <button class="button primary">Gửi mã xác thực</button>
            <p class="auth-links">
                <a href="${fn:escapeXml(pageContext.request.contextPath)}/login">Đã có tài khoản? Đăng nhập</a>
            </p>
        </form>
