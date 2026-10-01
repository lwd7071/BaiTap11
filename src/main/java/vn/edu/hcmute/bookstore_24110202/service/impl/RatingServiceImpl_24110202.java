package vn.edu.hcmute.bookstore_24110202.service.impl;

import java.util.List;
import vn.edu.hcmute.bookstore_24110202.repository.RatingRepository_24110202;
import vn.edu.hcmute.bookstore_24110202.repository.impl.RatingRepositoryImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IRatingService_24110202;

public class RatingServiceImpl_24110202 implements IRatingService_24110202 {
    private final RatingRepository_24110202 repo = new RatingRepositoryImpl_24110202();
    public List<Object[]> reviews(int bookId) { return repo.findForBook(bookId); }
    public long count(int bookId) { return repo.countForBook(bookId); }
    public boolean exists(int userId, int bookId) { return repo.exists(userId, bookId); }
    public void save(int userId, int bookId, int rating, String text) {
        String review = text == null ? "" : text.trim();
        if (userId < 1 || bookId < 1 || rating < 1 || rating > 5 || review.isEmpty() || review.length() > 2000)
            throw new IllegalArgumentException("Dữ liệu đánh giá không hợp lệ");
        repo.save(userId, bookId, rating, review);
    }
}
