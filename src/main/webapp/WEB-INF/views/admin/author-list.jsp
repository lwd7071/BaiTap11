<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>Quản lý tác giả</title>
        <div class="hero">
            <div>
                <div class="eyebrow">Khu vực quản trị</div>
                <h1>Quản lý tác giả</h1>
            </div>
            <a class="button primary" href="${fn:escapeXml(pageContext.request.contextPath)}/admin/authors/create">＋ Thêm tác giả</a>
        </div>
        <c:if test="${not empty flashSuccess}">
            <div class="notice">${fn:escapeXml(flashSuccess)}</div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="notice errorbox">${fn:escapeXml(flashError)}</div>
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
                            <td>${fn:escapeXml(a.id)}</td>
                            <td>${fn:escapeXml(a.name)}</td>
                            <td>${fn:escapeXml(a.dateOfBirth)}</td>
                            <td>
                                <div class="actions">
                                    <a class="button" href="${fn:escapeXml(pageContext.request.contextPath)}/admin/authors/edit?id=${fn:escapeXml(a.id)}">Sửa</a>
                                    <form method="post" action="${fn:escapeXml(pageContext.request.contextPath)}/admin/authors"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${fn:escapeXml(a.id)}">
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
