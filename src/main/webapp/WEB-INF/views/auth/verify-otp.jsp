<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Xác thực email | Book Store</title>
        <form class="form-card" method="post" action="${pageContext.request.contextPath}/verify-otp">
            <div class="eyebrow">Kiểm tra hộp thư</div>
            <h1>Xác thực email</h1>
            <p>Nhập mã 6 chữ số đã gửi tới địa chỉ email đăng ký. Mã có hiệu lực 5 phút.</p>
            <c:if test="${not empty message}">
                <div class="notice errorbox">${message}</div>
            </c:if>
            <div class="field">
                <label for="otp">Mã OTP</label>
                <input id="otp" name="otp" inputmode="numeric" pattern="[0-9]{6}" maxlength="6" required>
                <small class="error"><c:out value="${errors.otp}"/></small>
            </div>
            <button class="button primary">Xác nhận</button>
        </form>
