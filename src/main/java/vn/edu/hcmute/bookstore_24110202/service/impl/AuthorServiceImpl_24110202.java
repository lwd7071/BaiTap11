package vn.edu.hcmute.bookstore_24110202.service.impl;

import java.util.List;
import java.util.Optional;
import vn.edu.hcmute.bookstore_24110202.dto.AuthorForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.FormResult_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Author_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.AuthorRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.AuthorRepositoryImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IAuthorService_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

public class AuthorServiceImpl_24110202 implements IAuthorService_24110202 {
    private final AuthorRepository_24110202 repo = new AuthorRepositoryImpl_24110202();

    public FormResult_24110202<AuthorForm_24110202> validate(AuthorForm_24110202 form) {
        FormResult_24110202<AuthorForm_24110202> result = new FormResult_24110202<>(form);
        if (form == null || form.getName() == null || form.getName().isBlank() || form.getName().trim().length() > 100)
            result.addError("name", "Tên tác giả bắt buộc, tối đa 100 ký tự");
        if (form != null && (!ValidationUtil_24110202.optionalDate(form.getDateOfBirth())
                || !ValidationUtil_24110202.pastOrToday(form.getDateOfBirth())))
            result.addError("dateOfBirth", "Ngày sinh không hợp lệ");
        return result;
    }

    public List<Author_24110202> page(int page, int size) { return repo.findPage(page, size); }
    public List<Author_24110202> all() { return repo.findAll(); }
    public long count() { return repo.count(); }
    public Optional<Author_24110202> find(int id) { return repo.findById(id); }

    public Author_24110202 create(AuthorForm_24110202 form) {
        requireValid(form);
        Author_24110202 author = new Author_24110202();
        author.setName(form.getName().trim());
        author.setDateOfBirth(ValidationUtil_24110202.date(form.getDateOfBirth()));
        repo.save(author);
        return author;
    }

    public void save(AuthorForm_24110202 form, Integer id) {
        requireValid(form);
        if (id == null) { create(form); return; }
        Author_24110202 author = repo.findById(id).orElseThrow();
        author.setName(form.getName().trim());
        author.setDateOfBirth(ValidationUtil_24110202.date(form.getDateOfBirth()));
        repo.save(author);
    }

    public boolean delete(int id) {
        if (id < 1 || repo.findById(id).isEmpty() || repo.hasBooks(id)) return false;
        repo.delete(id);
        return true;
    }

    private void requireValid(AuthorForm_24110202 form) {
        if (!validate(form).isValid()) throw new IllegalArgumentException("Dữ liệu tác giả không hợp lệ");
    }
}
