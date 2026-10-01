<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<title>Giỏ hàng | Book Store</title>
<h1>Giỏ hàng</h1>
<c:if test="${not empty message}"><div class="notice errorbox"><c:out value="${message}"/></div></c:if>
<c:if test="${not empty flashSuccess}"><div class="notice"><c:out value="${flashSuccess}"/></div></c:if>
<c:if test="${not empty flashError}"><div class="notice errorbox"><c:out value="${flashError}"/></div></c:if>
<c:if test="${not empty errors.form or not empty errors.quantity}"><div class="error"><c:out value="${errors.form ne null ? errors.form : errors.quantity}"/></div></c:if>
<c:choose>
    <c:when test="${empty cartItems}">
        <section class="panel"><p>Giỏ hàng đang trống.</p><a class="button" href="${pageContext.request.contextPath}/home">Tiếp tục mua sắm</a></section>
    </c:when>
    <c:otherwise>
        <div class="table-wrap"><table>
            <thead><tr><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th></th></tr></thead>
            <tbody>
                <c:forEach var="row" items="${cartItems}">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${not empty row.book}"><a href="${pageContext.request.contextPath}/book/detail?id=${row.bookId}"><c:out value="${row.book.title}"/></a></c:when>
                                <c:otherwise>Sách #${row.bookId} (không còn trong cửa hàng)</c:otherwise>
                            </c:choose>
                            <c:if test="${not empty cartErrors[row.bookId]}"><div class="error"><c:out value="${cartErrors[row.bookId]}"/></div></c:if>
                        </td>
                        <td>${row.unitPrice}</td>
                        <td>
                            <c:if test="${not empty row.book}">
                                <form class="actions" method="post" action="${pageContext.request.contextPath}/cart/update">
                                    <input type="hidden" name="bookId" value="${row.bookId}">
                                    <input aria-label="Số lượng sản phẩm" type="number" name="quantity" min="1" max="${row.book.quantity}" value="<c:out value='${invalidBookId eq row.bookId ? invalidQuantity : row.quantity}'/>" required>
                                    <button class="button">Cập nhật</button>
                                </form>
                            </c:if>
                        </td>
                        <td>${row.lineTotal}</td>
                        <td><form method="post" action="${pageContext.request.contextPath}/cart/remove"><input type="hidden" name="bookId" value="${row.bookId}"><button class="button">Xóa</button></form></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table></div>
        <section class="panel" style="margin-top:20px"><p><strong>Tổng cộng: ${cartTotal}</strong></p><a class="button primary" href="${pageContext.request.contextPath}/checkout">Thanh toán COD</a></section>
    </c:otherwise>
</c:choose>
