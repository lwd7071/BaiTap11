<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>Quản lý tác giả</title>
        <div class="hero">
            <div>
                <div class="eyebrow">Khu vực quản trị</div>
                <h1>Quản lý tác giả</h1>
            </div>
            <a class="button primary" href="${pageContext.request.contextPath}/admin/authors/create">＋ Thêm tác giả</a>
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
                        <th>Tên tác giả</th>
                        <th>Ngày sinh</th>
                        <th>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${authors}">
                        <tr>
                            <td>${a.id}</td>
                            <td>${a.name}</td>
                            <td>${a.dateOfBirth}</td>
                            <td>
                                <div class="actions">
                                    <a class="button" href="${pageContext.request.contextPath}/admin/authors/edit?id=${a.id}">Sửa</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/authors">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${a.id}">
                                        <button class="button" onclick="return confirm('Xóa tác giả này?')">Xóa</button>
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
