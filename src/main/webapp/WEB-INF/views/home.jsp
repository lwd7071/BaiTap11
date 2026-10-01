<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Book Store | Khám phá sách</title>
        <section class="hero">
            <div>
                <div class="eyebrow">Tủ sách dành cho bạn</div>
                <h1>Những câu chuyện<br>đáng để mở ra.</h1>
                <p>Khám phá những cuốn sách được yêu thích.</p>
            </div>
            <span class="tag">${books.size()} sách trên trang</span>
        </section>
        <c:if test="${not empty flashSuccess}">
            <div class="notice">${flashSuccess}</div>
        </c:if>
        <div class="grid">
            <c:forEach var="book" items="${books}">
                <article class="book-card">
                    <a href="${pageContext.request.contextPath}/book/detail?id=${book.bookid}">
                        <img class="book-cover" src="${book.coverImage.startsWith('http://') or book.coverImage.startsWith('https://') ? book.coverImage : (book.coverImage.startsWith('/') ? pageContext.request.contextPath.concat(book.coverImage) : pageContext.request.contextPath.concat('/').concat(book.coverImage))}" alt="Bìa ${book.title}">
                        </a>
                        <div class="book-info">
                            <h2>
                                <a href="${pageContext.request.contextPath}/book/detail?id=${book.bookid}">${book.title}</a>
                            </h2>
                            <div class="meta">ISBN: ${book.isbn}<br>Tác giả: <c:forEach var="a" items="${book.authors}" varStatus="s">${a.name}<c:if test="${!s.last}">, </c:if>
                        </c:forEach>
                        <br>NXB: ${book.publisher}<br>Ngày phát hành: ${book.publishDate}<br>Số lượng: ${book.quantity}<br>Đánh giá: ${reviewCounts[book.bookid]} review</div>
                        <form method="post" action="${pageContext.request.contextPath}/cart/add"><input type="hidden" name="bookId" value="${book.bookid}"><input type="hidden" name="quantity" value="1"><button class="button primary" ${book.quantity le 0 ? 'disabled' : ''}>Thêm vào giỏ</button></form>
                    </div>
                </article>
            </c:forEach>
        </div>
        <nav class="pager">
            <c:if test="${currentPage gt 1}">
                <a href="?page=${currentPage-1}">← Trước</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="p">
                <c:choose>
                    <c:when test="${p eq currentPage}">
                        <span class="active">${p}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${p}">${p}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage lt totalPages}">
                <a href="?page=${currentPage+1}">Sau →</a>
            </c:if>
        </nav>
