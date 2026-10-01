package vn.edu.hcmute.bookstore_24110202.repository.impl;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import vn.edu.hcmute.bookstore_24110202.config.JpaConfig_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.BookRepository_24110202;

public class BookRepositoryImpl_24110202 implements BookRepository_24110202 {
    private <T> T read(Function<EntityManager, T> action) {
        EntityManager manager = JpaConfig_24110202.createEntityManager();
        try { return action.apply(manager); } finally { manager.close(); }
    }
    @Override public Optional<Book_24110202> findById(int id) {
        return read(manager -> {
            Book_24110202 book = manager.createQuery("select b from Book_24110202 b where b.bookid = :id", Book_24110202.class)
                    .setParameter("id", id).getResultStream().findFirst().orElse(null);
            if (book != null) book.getAuthors().size();
            return Optional.ofNullable(book);
        });
    }
    @Override public List<Book_24110202> findPage(int page, int size) {
        return read(manager -> {
            List<Book_24110202> books = manager.createQuery("select b from Book_24110202 b order by b.bookid", Book_24110202.class)
                    .setFirstResult((page - 1) * size).setMaxResults(size).getResultList();
            books.forEach(book -> book.getAuthors().size());
            return books;
        });
    }
    @Override public long count() { return read(manager -> manager.createQuery("select count(b) from Book_24110202 b", Long.class).getSingleResult()); }
    @Override public void save(Book_24110202 book, List<Integer> authorIds) {
        EntityManager manager = JpaConfig_24110202.createEntityManager();
        try {
            manager.getTransaction().begin();
            if (book.getBookid() == null) manager.persist(book); else book = manager.merge(book);
            manager.flush();
            manager.createNativeQuery("delete from book_author where bookid = ?").setParameter(1, book.getBookid()).executeUpdate();
            for (Integer authorId : authorIds) manager.createNativeQuery("insert into book_author(bookid, author_id) values(?, ?)")
                    .setParameter(1, book.getBookid()).setParameter(2, authorId).executeUpdate();
            manager.getTransaction().commit();
        } catch (RuntimeException exception) { if (manager.getTransaction().isActive()) manager.getTransaction().rollback(); throw exception; }
        finally { manager.close(); }
    }
    @Override public void delete(int id) {
        EntityManager manager = JpaConfig_24110202.createEntityManager();
        try {
            manager.getTransaction().begin();
            manager.createNativeQuery("delete from rating where bookid = ?").setParameter(1, id).executeUpdate();
            manager.createNativeQuery("delete from book_author where bookid = ?").setParameter(1, id).executeUpdate();
            Book_24110202 book = manager.find(Book_24110202.class, id); if (book != null) manager.remove(book);
            manager.getTransaction().commit();
        } catch (RuntimeException exception) { if (manager.getTransaction().isActive()) manager.getTransaction().rollback(); throw exception; }
        finally { manager.close(); }
    }
}
