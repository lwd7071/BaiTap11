<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>${empty author ? 'Thêm tác giả' : 'Sửa tác giả'}</title>
        <p>
        <a href="${pageContext.request.contextPath}/admin/authors">← Danh sách tác giả</a>
    </p>
    <form class="form-card" method="post" action="${pageContext.request.contextPath}/admin/authors">
        <h1>${empty author ? 'Thêm tác giả' : 'Sửa tác giả'}</h1>
        <c:if test="${not empty errors}">
            <div class="notice errorbox"><c:out value="${not empty message ? message : 'Vui lòng kiểm tra dữ liệu.'}"/></div>
        </c:if>
        <input type="hidden" name="action" value="${empty author ? 'create' : 'edit'}">
        <input type="hidden" name="id" value="${author.id}">
        <div class="field">
            <label for="name">Tên tác giả</label>
            <input id="name" name="name" maxlength="100" value="<c:out value='${param.name ne null ? param.name : author.name}'/>" required>
            <small class="error"><c:out value="${errors.name}"/></small>
        </div>
        <div class="field">
            <label for="dateOfBirth">Ngày sinh</label>
            <input id="dateOfBirth" name="dateOfBirth" type="date" value="<c:out value='${param.dateOfBirth ne null ? param.dateOfBirth : author.dateOfBirth}'/>"/>
            <small class="error"><c:out value="${errors.dateOfBirth}"/></small>
        </div>
        <button class="button primary">Lưu tác giả</button>
    </form>
