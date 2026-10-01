package vn.edu.hcmute.bookstore_24110202.service;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.dto.BookForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;

public interface IBookService_24110202 {
    FormResult_24110202<BookForm_24110202> validate(BookForm_24110202 form);
    List<Book_24110202> page(int page, int size);
    long count();
    Optional<Book_24110202> find(int id);
    void save(BookForm_24110202 form, Integer id);
    void delete(int id);
}
