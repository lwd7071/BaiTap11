<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>Book Store | Khám phá sách</title>
        <section class="hero">
            <div>
                <div class="eyebrow">Tủ sách dành cho bạn</div>
                <h1>Những câu chuyện<br>đáng để mở ra.</h1>
                <p>Khám phá những cuốn sách được yêu thích.</p>
            </div>
            <span class="tag">${fn:escapeXml(books.size())} sách trên trang</span>
        </section>
        <c:if test="${not empty flashSuccess}">
            <div class="notice">${fn:escapeXml(flashSuccess)}</div>
        </c:if>
        <div class="grid" id="products">
            <c:forEach var="book" items="${books}">
                <article class="book-card">
                    <a href="${fn:escapeXml(pageContext.request.contextPath)}/book/detail?id=${fn:escapeXml(book.bookid)}">
                        <img class="book-cover" src="${fn:escapeXml(book.coverImage.startsWith('http://') or book.coverImage.startsWith('https://') ? book.coverImage : (book.coverImage.startsWith('/') ? pageContext.request.contextPath.concat(book.coverImage) : pageContext.request.contextPath.concat('/').concat(book.coverImage)))}" alt="Bìa ${fn:escapeXml(book.title)}">
                        </a>
                        <div class="book-info">
                            <h2>
                                <a href="${fn:escapeXml(pageContext.request.contextPath)}/book/detail?id=${fn:escapeXml(book.bookid)}">${fn:escapeXml(book.title)}</a>
                            </h2>
                            <div class="meta">ISBN: ${fn:escapeXml(book.isbn)}<br>Tác giả: <c:forEach var="a" items="${book.authors}" varStatus="s">${fn:escapeXml(a.name)}<c:if test="${!s.last}">, </c:if>
                        </c:forEach>
                        <br>NXB: ${fn:escapeXml(book.publisher)}<br>Ngày phát hành: ${fn:escapeXml(book.publishDate)}<br>Số lượng: ${fn:escapeXml(book.quantity)}<br>Đánh giá: ${fn:escapeXml(reviewCounts[book.bookid])} review</div>
                        <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/cart/add"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}"><input type="hidden" name="bookId" value="${fn:escapeXml(book.bookid)}"><input type="hidden" name="quantity" value="1"><button class="button primary" ${fn:escapeXml(book.quantity le 0 ? 'disabled' : '')}>Thêm vào giỏ</button></form>
                    </div>
                </article>
            </c:forEach>
        </div>
        <nav class="pager">
            <c:if test="${currentPage gt 1}">
                <a href="?page=${fn:escapeXml(currentPage-1)}">← Trước</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="p">
                <c:choose>
                    <c:when test="${p eq currentPage}">
                        <span class="active">${fn:escapeXml(p)}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${fn:escapeXml(p)}">${fn:escapeXml(p)}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage lt totalPages}">
                <a href="?page=${fn:escapeXml(currentPage+1)}">Sau →</a>
            </c:if>
        </nav>
