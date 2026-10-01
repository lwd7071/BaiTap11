package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RatingId_24110202 implements Serializable {

    private Integer userid;
    private Integer bookid;

    public RatingId_24110202() {
    }

    public RatingId_24110202(Integer userid, Integer bookid) {
        this.userid = userid;
        this.bookid = bookid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RatingId_24110202 that)) return false;
        return Objects.equals(userid, that.userid) && Objects.equals(bookid, that.bookid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, bookid);
    }
}
