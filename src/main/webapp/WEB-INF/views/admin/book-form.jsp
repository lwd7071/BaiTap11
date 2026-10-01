<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <title>${fn:escapeXml(empty book ? 'Thêm sách' : 'Sửa sách')}</title>
        <p>
        <a href="${fn:escapeXml(pageContext.request.contextPath)}/admin/books">← Danh sách sách</a>
    </p>
    <form class="form-card wide" method="post" enctype="multipart/form-data" action="${fn:escapeXml(pageContext.request.contextPath)}/admin/books"><input type="hidden" name="csrfToken" value="${fn:escapeXml(sessionScope.csrfToken)}">
        <h1>${fn:escapeXml(empty book ? 'Thêm sách' : 'Sửa sách')}</h1>
        <c:if test="${not empty errors}">
            <div class="notice errorbox"><c:out value="${errors.form ne null ? errors.form : (errors.coverFile ne null ? errors.coverFile : message)}"/></div>
        </c:if>
        <input type="hidden" name="action" value="${fn:escapeXml(empty book ? 'create' : 'edit')}">
        <input type="hidden" name="id" value="${fn:escapeXml(book.bookid)}">
        <div class="field">
            <label for="isbn">ISBN</label>
            <input id="isbn" name="isbn" value="<c:out value='${param.isbn ne null ? param.isbn : book.isbn}'/>" required>
            <small class="error"><c:out value="${errors.isbn}"/></small>
        </div>
        <div class="field">
            <label for="title">Tiêu đề</label>
            <input id="title" name="title" maxlength="200" value="<c:out value='${param.title ne null ? param.title : book.title}'/>" required>
            <small class="error"><c:out value="${errors.title}"/></small>
        </div>
        <div class="field">
            <label for="publisher">Nhà xuất bản</label>
            <input id="publisher" name="publisher" maxlength="100" value="<c:out value='${param.publisher ne null ? param.publisher : book.publisher}'/>"/>
            <small class="error"><c:out value="${errors.publisher}"/></small>
        </div>
        <div class="field">
            <label for="price">Giá</label>
            <input id="price" name="price" type="number" min="0" max="9999.99" step="0.01" value="<c:out value='${param.price ne null ? param.price : book.price}'/>" required>
            <small class="error"><c:out value="${errors.price}"/></small>
        </div>
        <div class="field">
            <label for="quantity">Số lượng</label>
            <input id="quantity" name="quantity" type="number" min="0" value="<c:out value='${param.quantity ne null ? param.quantity : book.quantity}'/>" required>
            <small class="error"><c:out value="${errors.quantity}"/></small>
        </div>
        <div class="field">
            <label for="publishDate">Ngày xuất bản</label>
            <input id="publishDate" name="publishDate" type="date" value="<c:out value='${param.publishDate ne null ? param.publishDate : book.publishDate}'/>"/>
            <small class="error"><c:out value="${errors.publishDate}"/></small>
        </div>
        <div class="field">
            <label for="coverImage">Đường dẫn ảnh</label>
            <input id="coverImage" name="coverImage" maxlength="200" value="<c:out value='${param.coverImage ne null ? param.coverImage : book.coverImage}'/>"/>
            <small class="error"><c:out value="${errors.coverImage}"/></small>
        </div>
        <div class="field">
            <label for="coverFile">Hoặc tải ảnh bìa lên</label>
            <input id="coverFile" type="file" name="coverFile" accept="image/jpeg,image/png,image/gif,image/webp">
            <small>JPEG, PNG, GIF hoặc WebP; tối đa 5 MB. Ảnh tải lên sẽ được ưu tiên hơn đường dẫn phía trên.</small>
        </div>
        <div class="field">
            <label for="description">Mô tả</label>
            <textarea id="description" name="description"><c:out value="${param.description ne null ? param.description : book.description}"/></textarea>
        </div>
        <div class="field">
            <label for="authorIds">Tác giả (giữ Ctrl để chọn nhiều)</label>
            <select id="authorIds" name="authorIds" multiple size="6">
                <c:forEach var="a" items="${authors}">
                    <c:set var="authorSelected" value="false"/>
                <c:forEach var="selectedAuthorId" items="${paramValues.authorIds}">
                    <c:if test="${selectedAuthorId eq a.id}">
                        <c:set var="authorSelected" value="true"/>
                    </c:if>
                </c:forEach>
                    <c:forEach var="selectedAuthor" items="${book.authors}">
                    <c:if test="${empty paramValues.authorIds and selectedAuthor.id eq a.id}">
                            <c:set var="authorSelected" value="true"/>
                        </c:if>
                    </c:forEach>
                    <option value="${fn:escapeXml(a.id)}"${fn:escapeXml(authorSelected ? ' selected' : '')}>${fn:escapeXml(a.name)}</option>
                </c:forEach>
            </select>
            <small class="error"><c:out value="${errors.authorIds}"/></small>
        </div>
        <div class="form-grid two-columns">
            <div class="field">
                <label for="newAuthorName">Tên tác giả mới (nếu chưa có trong danh sách)</label>
                <input id="newAuthorName" name="newAuthorName" maxlength="100" value="<c:out value='${param.newAuthorName}'/>"/>
                <small class="error"><c:out value="${errors.newAuthorName}"/></small>
            </div>
            <div class="field">
                <label for="newAuthorDateOfBirth">Ngày sinh tác giả mới</label>
                <input id="newAuthorDateOfBirth" type="date" name="newAuthorDateOfBirth" value="<c:out value='${param.newAuthorDateOfBirth}'/>"/>
                <small class="error"><c:out value="${errors.newAuthorDateOfBirth}"/></small>
            </div>
        </div>
        <small>Có thể chọn tác giả có sẵn, nhập tác giả mới, hoặc dùng cả hai.</small>
        <button class="button primary">Lưu sách</button>
    </form>
