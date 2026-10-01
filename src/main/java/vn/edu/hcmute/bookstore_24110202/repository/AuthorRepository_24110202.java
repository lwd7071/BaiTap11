package vn.edu.hcmute.bookstore_24110202.repository;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.entity.Author_24110202;

public interface AuthorRepository_24110202 {
    Optional<Author_24110202> findById(int id);
    List<Author_24110202> findPage(int page, int size);
    List<Author_24110202> findAll();
    long count();
    boolean hasBooks(int id);
    void save(Author_24110202 author);
    void delete(int id);
}
