<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Đăng ký | Book Store</title>
        <form class="form-card" method="post" action="${pageContext.request.contextPath}/register">
            <div class="eyebrow">Tạo tài khoản</div>
            <h1>Đăng ký</h1>
            <c:if test="${not empty message}">
                <div class="notice errorbox">${message}</div>
            </c:if>
            <div class="field">
                <label>Email</label>
                <input type="email" name="email" maxlength="50" value="${param.email}" required>
                <small class="error">${errors.email}</small>
            </div>
            <div class="field">
                <label>Họ tên</label>
                <input name="fullname" maxlength="50" value="${param.fullname}" required>
                <small class="error">${errors.fullname}</small>
            </div>
            <div class="field">
                <label>Điện thoại</label>
                <input name="phone" inputmode="numeric" value="${param.phone}">
                <small class="error">${errors.phone}</small>
            </div>
            <div class="field">
                <label>Mật khẩu</label>
                <input type="password" name="password" minlength="6" maxlength="32" required>
                <small class="error">${errors.password}</small>
            </div>
            <div class="field">
                <label>Nhập lại mật khẩu</label>
                <input type="password" name="confirmPassword" required>
                <small class="error">${errors.confirmPassword}</small>
            </div>
            <button class="button primary">Gửi mã xác thực</button>
            <p class="auth-links">
                <a href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập</a>
            </p>
        </form>
