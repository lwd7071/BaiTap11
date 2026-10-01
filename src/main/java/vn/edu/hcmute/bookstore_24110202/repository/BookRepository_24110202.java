package vn.edu.hcmute.bookstore_24110202.repository;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;

public interface BookRepository_24110202 {
    Optional<Book_24110202> findById(int id);
    List<Book_24110202> findPage(int page, int size);
    long count();
    void save(Book_24110202 b, List<Integer> authorIds);
    void delete(int id);
}
