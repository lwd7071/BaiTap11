<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Quản lý sách</title>
        <div class="hero">
            <div>
                <div class="eyebrow">Khu vực quản trị</div>
                <h1>Quản lý sách</h1>
                <p>Danh mục và thông tin xuất bản.</p>
            </div>
            <a class="button primary" href="${pageContext.request.contextPath}/admin/books/create">＋ Thêm sách</a>
        </div>
        <c:if test="${not empty flashSuccess}">
            <div class="notice">${flashSuccess}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="notice errorbox">${flashError}</div>
        </c:if>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Bìa</th>
                        <th>Tên sách</th>
                        <th>ISBN</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Tác giả</th>
                        <th>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="b" items="${books}">
                        <tr>
                            <td>${b.bookid}</td>
                            <td>
                                <img class="small-cover" src="${b.coverImage.startsWith('http://') or b.coverImage.startsWith('https://') ? b.coverImage : (b.coverImage.startsWith('/') ? pageContext.request.contextPath.concat(b.coverImage) : pageContext.request.contextPath.concat('/').concat(b.coverImage))}" alt="">
                                </td>
                                <td>${b.title}</td>
                                <td>${b.isbn}</td>
                                <td>${b.price}</td>
                                <td>${b.quantity}</td>
                                <td>
                                    <c:forEach var="a" items="${b.authors}" varStatus="s">${a.name}<c:if test="${!s.last}">, </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <div class="actions">
                                    <a class="button" href="${pageContext.request.contextPath}/admin/books/edit?id=${b.bookid}">Sửa</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/books">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${b.bookid}">
                                        <button class="button" onclick="return confirm('Xóa sách này?')">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
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
