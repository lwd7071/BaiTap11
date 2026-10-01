package vn.edu.hcmute.bookstore_24110202.service;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.dto.AuthorForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Author_24110202;

public interface IAuthorService_24110202 {
    FormResult_24110202<AuthorForm_24110202> validate(AuthorForm_24110202 form);
    List<Author_24110202> page(int page, int size);
    List<Author_24110202> all();
    long count();
    Optional<Author_24110202> find(int id);
    Author_24110202 create(AuthorForm_24110202 form);
    void save(AuthorForm_24110202 form, Integer id);
    boolean delete(int id);
}
