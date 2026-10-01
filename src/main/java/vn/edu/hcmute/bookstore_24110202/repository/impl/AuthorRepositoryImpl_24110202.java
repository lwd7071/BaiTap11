package vn.edu.hcmute.bookstore_24110202.repository.impl;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import vn.edu.hcmute.bookstore_24110202.config.JpaConfig_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.Author_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.AuthorRepository_24110202;

public class AuthorRepositoryImpl_24110202 implements AuthorRepository_24110202 {

    private <T> T read(Function<EntityManager, T> f) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            return f.apply(m);
        } finally {
            m.close();
        }
    }

    @Override
    public Optional<Author_24110202> findById(int id) {
        return read(m -> Optional.ofNullable(m.find(Author_24110202.class, id)));
    }

    @Override
    public List<Author_24110202> findPage(int p, int s) {
        return read(m -> m.createQuery("from Author_24110202 order by id", Author_24110202.class)
                .setFirstResult((p - 1) * s)
                .setMaxResults(s)
                .getResultList());
    }

    @Override
    public List<Author_24110202> findAll() {
        return read(m -> m.createQuery("from Author_24110202 order by name", Author_24110202.class)
                .getResultList());
    }

    @Override
    public long count() {
        return read(m -> m.createQuery("select count(a) from Author_24110202 a", Long.class)
                .getSingleResult());
    }

    @Override
    public boolean hasBooks(int id) {
        return read(m -> ((Number) m.createNativeQuery("select count(*) from book_author where author_id=?")
                .setParameter(1, id)
                .getSingleResult()).longValue() > 0);
    }

    @Override
    public void save(Author_24110202 a) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            m.getTransaction().begin();
            if (a.getId() != null) {
                m.merge(a);
            } else {
                m.persist(a);
            }
            m.getTransaction().commit();
        } finally {
            m.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager m = JpaConfig_24110202.createEntityManager();
        try {
            m.getTransaction().begin();
            Author_24110202 author = m.find(Author_24110202.class, id);
            if (author != null) {
                m.remove(author);
            }
            m.getTransaction().commit();
        } finally {
            m.close();
        }
    }
}
