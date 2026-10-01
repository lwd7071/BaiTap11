package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_author")
public class BookAuthor_24110202 {

    @EmbeddedId
    private BookAuthorId_24110202 id;

    public BookAuthor_24110202() {
    }

    public BookAuthor_24110202(BookAuthorId_24110202 id) {
        this.id = id;
    }

    public BookAuthorId_24110202 getId() {
        return id;
    }

    public void setId(BookAuthorId_24110202 id) {
        this.id = id;
    }
}
