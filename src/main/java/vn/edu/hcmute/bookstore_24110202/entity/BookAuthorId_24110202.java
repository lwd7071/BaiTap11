package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BookAuthorId_24110202 implements Serializable {

    private Integer bookid;

    @Column(name = "author_id")
    private Integer authorId;

    public BookAuthorId_24110202() {
    }

    public BookAuthorId_24110202(Integer bookid, Integer authorId) {
        this.bookid = bookid;
        this.authorId = authorId;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookAuthorId_24110202 that)) return false;
        return Objects.equals(bookid, that.bookid) && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookid, authorId);
    }
}
