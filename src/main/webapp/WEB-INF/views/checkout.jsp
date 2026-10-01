<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Thanh toán COD | Book Store</title>
<h1>Thông tin giao hàng</h1>
<c:if test="${not empty message}"><div class="notice errorbox"><c:out value="${message}"/></div></c:if>
<c:if test="${not empty errors.form}"><div class="notice errorbox"><c:out value="${errors.form}"/></div></c:if>
<section class="panel"><h2>Đơn hàng</h2><c:forEach var="row" items="${cartItems}"><p><c:choose><c:when test="${not empty row.book}">${row.book.title}</c:when><c:otherwise>Sách #${row.bookId} (không còn trong cửa hàng)</c:otherwise></c:choose> × ${row.quantity} <span>${row.lineTotal}</span></p></c:forEach><p><strong>Tổng cộng: ${cartTotal} · Thanh toán khi nhận hàng (COD)</strong></p></section>
<form class="form-card" method="post" action="${pageContext.request.contextPath}/checkout">
<div class="field"><label for="recipientName">Tên người nhận</label><input id="recipientName" name="recipientName" maxlength="100" value="<c:out value='${form.recipientName}'/>" required><small class="error"><c:out value="${errors.recipientName}"/></small></div>
<div class="field"><label for="phone">Số điện thoại</label><input id="phone" name="phone" type="tel" inputmode="numeric" pattern="0[0-9]{9}" maxlength="10" value="<c:out value='${form.phone}'/>" required><small class="error"><c:out value="${errors.phone}"/></small></div>
<div class="field"><label for="shippingAddress">Địa chỉ giao hàng</label><textarea id="shippingAddress" name="shippingAddress" maxlength="255" required><c:out value="${form.shippingAddress}"/></textarea><small class="error"><c:out value="${errors.shippingAddress}"/></small></div>
<div class="field"><label for="note">Ghi chú (không bắt buộc)</label><textarea id="note" name="note" maxlength="500"><c:out value="${form.note}"/></textarea><small class="error"><c:out value="${errors.note}"/></small></div>
<button class="button primary" ${cartValid ? '' : 'disabled'}>Đặt hàng COD</button> <a class="button" href="${pageContext.request.contextPath}/cart">Quay lại giỏ</a></form>
