package vn.edu.hcmute.bookstore_24110202.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import vn.edu.hcmute.bookstore_24110202.dto.BookForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.BookRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.BookRepositoryImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IBookService_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

public class BookServiceImpl_24110202 implements IBookService_24110202 {
    private final BookRepository_24110202 repo = new BookRepositoryImpl_24110202();

    public FormResult_24110202<BookForm_24110202> validate(BookForm_24110202 form) {
        FormResult_24110202<BookForm_24110202> result = new FormResult_24110202<>(form);
        if (form == null) { result.addError("form", "Thông tin sách bắt buộc"); return result; }
        if (!ValidationUtil_24110202.positiveInteger(form.getIsbn())) result.addError("isbn", "ISBN phải là số nguyên dương");
        if (form.getTitle() == null || form.getTitle().isBlank() || form.getTitle().trim().length() > 200)
            result.addError("title", "Tiêu đề bắt buộc, tối đa 200 ký tự");
        if (form.getPublisher() != null && form.getPublisher().trim().length() > 100)
            result.addError("publisher", "Nhà xuất bản tối đa 100 ký tự");
        if (!ValidationUtil_24110202.price(form.getPrice())) result.addError("price", "Giá phải từ 0 đến 9999.99");
        if (!ValidationUtil_24110202.nonNegativeInteger(form.getQuantity())) result.addError("quantity", "Số lượng phải là số nguyên không âm");
        if (!ValidationUtil_24110202.optionalDate(form.getPublishDate())) result.addError("publishDate", "Ngày xuất bản không hợp lệ");
        if (form.getCoverImage() != null && form.getCoverImage().trim().length() > 200)
            result.addError("coverImage", "Đường dẫn ảnh tối đa 200 ký tự");
        else if (!ValidationUtil_24110202.safeImageUrl(form.getCoverImage()))
            result.addError("coverImage", "Đường dẫn ảnh phải thuộc ứng dụng hoặc dùng URL HTTP/HTTPS hợp lệ");
        String[] authorIds = form.getAuthorIds();
        if (authorIds != null) {
            Set<Integer> uniqueIds = new HashSet<>();
            if (authorIds.length > 100) result.addError("authorIds", "Chỉ được chọn tối đa 100 tác giả");
            for (String authorId : authorIds) {
                Integer parsed = ValidationUtil_24110202.integer(authorId);
                if (parsed == null || parsed < 1 || !uniqueIds.add(parsed)) {
                    result.addError("authorIds", "Danh sách tác giả không hợp lệ");
                    break;
                }
            }
        }
        return result;
    }

    public List<Book_24110202> page(int page, int size) { return repo.findPage(page, size); }
    public long count() { return repo.count(); }
    public Optional<Book_24110202> find(int id) { return repo.findById(id); }

    public void save(BookForm_24110202 form, Integer id) {
        if (!validate(form).isValid()) throw new IllegalArgumentException("Dữ liệu sách không hợp lệ");
        Book_24110202 book = id == null ? new Book_24110202() : repo.findById(id).orElseThrow();
        book.setIsbn(ValidationUtil_24110202.integer(form.getIsbn()));
        book.setTitle(form.getTitle().trim());
        book.setPublisher(trimToNull(form.getPublisher()));
        book.setPrice(new BigDecimal(form.getPrice().trim()));
        book.setQuantity(ValidationUtil_24110202.integer(form.getQuantity()));
        book.setPublishDate(ValidationUtil_24110202.date(form.getPublishDate()));
        book.setCoverImage(trimToNull(form.getCoverImage()));
        book.setDescription(form.getDescription());
        List<Integer> authorIds = new ArrayList<>();
        if (form.getAuthorIds() != null)
            for (String authorId : form.getAuthorIds()) authorIds.add(ValidationUtil_24110202.integer(authorId));
        repo.save(book, authorIds);
    }

    public void delete(int id) {
        if (id < 1 || find(id).isEmpty()) throw new IllegalArgumentException("Không tìm thấy sách");
        repo.delete(id);
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}
