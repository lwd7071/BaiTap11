<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>${fn:escapeXml(book.title)} | Book Store</title>
        <p>
        <a href="${fn:escapeXml(pageContext.request.contextPath)}/home">← Quay lại cửa hàng</a>
    </p>
    <c:if test="${not empty flashSuccess}">
        <div class="notice">${fn:escapeXml(flashSuccess)}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="notice errorbox">${fn:escapeXml(flashError)}</div>
    </c:if>
    <section class="detail">
        <img class="book-cover" src="${fn:escapeXml(book.coverImage.startsWith('http://') or book.coverImage.startsWith('https://') ? book.coverImage : (book.coverImage.startsWith('/') ? pageContext.request.contextPath.concat(book.coverImage) : pageContext.request.contextPath.concat('/').concat(book.coverImage)))}" alt="Bìa ${fn:escapeXml(book.title)}">
            <div>
                <div class="eyebrow">Chi tiết sách</div>
                <h1>${fn:escapeXml(book.title)}</h1>
                <p class="meta">ISBN: ${fn:escapeXml(book.isbn)}<br>Tác giả: <c:forEach var="a" items="${book.authors}" varStatus="s">${fn:escapeXml(a.name)}<c:if test="${!s.last}">, </c:if>
            </c:forEach>
            <br>Nhà xuất bản: ${fn:escapeXml(book.publisher)}<br>Ngày phát hành: ${fn:escapeXml(book.publishDate)}<br>Số lượng còn: ${fn:escapeXml(book.quantity)}</p>
            <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/cart/add"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}"><input type="hidden" name="bookId" value="${fn:escapeXml(book.bookid)}"><div class="field"><label for="quantity">Số lượng</label><input id="quantity" type="number" name="quantity" min="1" max="${fn:escapeXml(book.quantity)}" value="1" required></div><button class="button primary" ${fn:escapeXml(book.quantity le 0 ? 'disabled' : '')}>Thêm vào giỏ</button></form>
            <p>
            <span class="tag">${fn:escapeXml(reviewCount)} đánh giá</span>
        </p>
        <p>${fn:escapeXml(book.description)}</p>
    </div>
</section>
<section class="panel section-gap">
    <h2>Đánh giá từ độc giả</h2>
    <c:forEach var="review" items="${reviews}">
        <p>
        <strong>${fn:escapeXml(review[0])}</strong> · ${fn:escapeXml(review[2])}/5<br>${fn:escapeXml(review[1])}</p>
        <hr>
        </c:forEach>
        <c:if test="${empty reviews}">
            <p class="meta">Chưa có đánh giá.</p>
        </c:if>
        <c:if test="${not empty sessionScope.currentUser}">
            <h3>Gửi đánh giá</h3>
            <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/rating/create"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
                <input type="hidden" name="bookId" value="${fn:escapeXml(book.bookid)}">
                <div class="field">
                    <label for="rating">Điểm số</label>
                    <select id="rating" name="rating" required>
                        <option value="">Chọn điểm</option>
                        <option>5</option>
                        <option>4</option>
                        <option>3</option>
                        <option>2</option>
                        <option>1</option>
                    </select>
                </div>
                <div class="field">
                    <label for="reviewText">Nội dung</label>
                    <textarea id="reviewText" name="reviewText" maxlength="2000" required>
                    </textarea>
                </div>
                <button class="button primary">Gửi đánh giá</button>
            </form>
        </c:if>
    </section>
