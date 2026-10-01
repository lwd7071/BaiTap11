<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>${book.title} | Book Store</title>
        <p>
        <a href="${pageContext.request.contextPath}/home">← Quay lại cửa hàng</a>
    </p>
    <c:if test="${not empty flashSuccess}">
        <div class="notice">${flashSuccess}</div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="notice errorbox">${flashError}</div>
    </c:if>
    <section class="detail">
        <img class="book-cover" src="${book.coverImage.startsWith('http://') or book.coverImage.startsWith('https://') ? book.coverImage : (book.coverImage.startsWith('/') ? pageContext.request.contextPath.concat(book.coverImage) : pageContext.request.contextPath.concat('/').concat(book.coverImage))}" alt="Bìa ${book.title}">
            <div>
                <div class="eyebrow">Chi tiết sách</div>
                <h1>${book.title}</h1>
                <p class="meta">ISBN: ${book.isbn}<br>Tác giả: <c:forEach var="a" items="${book.authors}" varStatus="s">${a.name}<c:if test="${!s.last}">, </c:if>
            </c:forEach>
            <br>Nhà xuất bản: ${book.publisher}<br>Ngày phát hành: ${book.publishDate}<br>Số lượng còn: ${book.quantity}</p>
            <form method="post" action="${pageContext.request.contextPath}/cart/add"><input type="hidden" name="bookId" value="${book.bookid}"><div class="field"><label for="quantity">Số lượng</label><input id="quantity" type="number" name="quantity" min="1" max="${book.quantity}" value="1" required></div><button class="button primary" ${book.quantity le 0 ? 'disabled' : ''}>Thêm vào giỏ</button></form>
            <p>
            <span class="tag">${reviewCount} đánh giá</span>
        </p>
        <p>${book.description}</p>
    </div>
</section>
<section class="panel" style="margin-top:28px">
    <h2>Đánh giá từ độc giả</h2>
    <c:forEach var="review" items="${reviews}">
        <p>
        <strong>${review[0]}</strong> · ${review[2]}/5<br>${review[1]}</p>
        <hr>
        </c:forEach>
        <c:if test="${empty reviews}">
            <p class="meta">Chưa có đánh giá.</p>
        </c:if>
        <c:if test="${not empty sessionScope.currentUser}">
            <h3>Gửi đánh giá</h3>
            <form method="post" action="${pageContext.request.contextPath}/rating/create">
                <input type="hidden" name="bookId" value="${book.bookid}">
                <div class="field">
                    <label>Điểm số</label>
                    <select name="rating" required>
                        <option value="">Chọn điểm</option>
                        <option>5</option>
                        <option>4</option>
                        <option>3</option>
                        <option>2</option>
                        <option>1</option>
                    </select>
                </div>
                <div class="field">
                    <label>Nội dung</label>
                    <textarea name="reviewText" maxlength="2000" required>
                    </textarea>
                </div>
                <button class="button primary">Gửi đánh giá</button>
            </form>
        </c:if>
    </section>
