package vn.edu.hcmute.bookstore_24110202.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "rating")
public class Rating_24110202 {

    @EmbeddedId
    private RatingId_24110202 id;

    private Integer rating;

    @Lob
    @Nationalized
    @Column(name = "review_text", columnDefinition = "nvarchar(max)")
    private String reviewText;

    public Rating_24110202() {
    }

    public Rating_24110202(RatingId_24110202 id, Integer rating, String reviewText) {
        this.id = id;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public RatingId_24110202 getId() {
        return id;
    }

    public void setId(RatingId_24110202 id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
