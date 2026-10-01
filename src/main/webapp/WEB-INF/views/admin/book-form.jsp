<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <title>${empty book ? 'Thêm sách' : 'Sửa sách'}</title>
        <p>
        <a href="${pageContext.request.contextPath}/admin/books">← Danh sách sách</a>
    </p>
    <form class="form-card" style="max-width:760px" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/books">
        <h1>${empty book ? 'Thêm sách' : 'Sửa sách'}</h1>
        <c:if test="${not empty errors}">
            <div class="notice errorbox">Vui lòng kiểm tra các trường được đánh dấu.</div>
        </c:if>
        <input type="hidden" name="action" value="${empty book ? 'create' : 'edit'}">
        <input type="hidden" name="id" value="${book.bookid}">
        <div class="field">
            <label>ISBN</label>
            <input name="isbn" value="${param.isbn ne null ? param.isbn : book.isbn}" required>
            <small class="error">${errors.isbn}</small>
        </div>
        <div class="field">
            <label>Tiêu đề</label>
            <input name="title" maxlength="200" value="${param.title ne null ? param.title : book.title}" required>
            <small class="error">${errors.title}</small>
        </div>
        <div class="field">
            <label>Nhà xuất bản</label>
            <input name="publisher" maxlength="100" value="${param.publisher ne null ? param.publisher : book.publisher}">
            <small class="error">${errors.publisher}</small>
        </div>
        <div class="field">
            <label>Giá</label>
            <input name="price" type="number" min="0" max="9999.99" step="0.01" value="${param.price ne null ? param.price : book.price}" required>
            <small class="error">${errors.price}</small>
        </div>
        <div class="field">
            <label>Số lượng</label>
            <input name="quantity" type="number" min="0" value="${param.quantity ne null ? param.quantity : book.quantity}" required>
            <small class="error">${errors.quantity}</small>
        </div>
        <div class="field">
            <label>Ngày xuất bản</label>
            <input name="publishDate" type="date" value="${param.publishDate ne null ? param.publishDate : book.publishDate}">
            <small class="error">${errors.publishDate}</small>
        </div>
        <div class="field">
            <label>Đường dẫn ảnh</label>
            <input name="coverImage" maxlength="255" value="${param.coverImage ne null ? param.coverImage : book.coverImage}">
            <small class="error">${errors.coverImage}</small>
        </div>
        <div class="field">
            <label>Hoặc tải ảnh bìa lên</label>
            <input type="file" name="coverFile" accept="image/jpeg,image/png,image/gif,image/webp">
            <small>JPEG, PNG, GIF hoặc WebP; tối đa 5 MB. Ảnh tải lên sẽ được ưu tiên hơn đường dẫn phía trên.</small>
        </div>
        <div class="field">
            <label>Mô tả</label>
            <textarea name="description">${param.description ne null ? param.description : book.description}</textarea>
        </div>
        <div class="field">
            <label>Tác giả (giữ Ctrl để chọn nhiều)</label>
            <select name="authorIds" multiple size="6">
                <c:forEach var="a" items="${authors}">
                    <c:set var="authorSelected" value="false"/>
                    <c:forEach var="selectedAuthor" items="${book.authors}">
                        <c:if test="${selectedAuthor.id eq a.id}">
                            <c:set var="authorSelected" value="true"/>
                        </c:if>
                    </c:forEach>
                    <option value="${a.id}"${authorSelected ? ' selected' : ''}>${a.name}</option>
                </c:forEach>
            </select>
            <small class="error">${errors.authorIds}</small>
        </div>
        <div class="form-grid two-columns">
            <div class="field">
                <label>Tên tác giả mới (nếu chưa có trong danh sách)</label>
                <input name="newAuthorName" maxlength="100" value="${param.newAuthorName}">
            </div>
            <div class="field">
                <label>Ngày sinh tác giả mới</label>
                <input type="date" name="newAuthorDateOfBirth" value="${param.newAuthorDateOfBirth}">
            </div>
        </div>
        <small>Có thể chọn tác giả có sẵn, nhập tác giả mới, hoặc dùng cả hai.</small>
        <button class="button primary">Lưu sách</button>
    </form>
