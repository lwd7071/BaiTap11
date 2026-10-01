package vn.edu.hcmute.bookstore_24110202.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.dto.AuthorForm_24110202;
import vn.edu.hcmute.bookstore_24110202.dto.BookForm_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.AuthorServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.RatingServiceImpl_24110202;

class CatalogValidationTest_24110202 {
    private final AuthorServiceImpl_24110202 authors = new AuthorServiceImpl_24110202();
    private final BookServiceImpl_24110202 books = new BookServiceImpl_24110202();

    @Test void authorRequiresNameAndRejectsMalformedOrFutureBirthDate() {
        AuthorForm_24110202 form = new AuthorForm_24110202();
        form.setName("  ");
        form.setDateOfBirth("not-a-date");
        var result = authors.validate(form);
        assertTrue(result.getFieldErrors().containsKey("name"));
        assertTrue(result.getFieldErrors().containsKey("dateOfBirth"));

        form.setName("Valid name");
        form.setDateOfBirth(java.time.LocalDate.now().plusDays(1).toString());
        assertTrue(authors.validate(form).getFieldErrors().containsKey("dateOfBirth"));
    }

    @Test void bookValidatesStoredFieldLimitsAndAuthorIds() {
        BookForm_24110202 form = new BookForm_24110202();
        form.setIsbn("1");
        form.setTitle("x".repeat(201));
        form.setPublisher("x".repeat(101));
        form.setPrice("10.001");
        form.setQuantity("-1");
        form.setPublishDate("bad-date");
        form.setCoverImage("x".repeat(201));
        form.setAuthorIds(new String[] {"NaN"});
        var result = books.validate(form);
        assertTrue(result.getFieldErrors().keySet().containsAll(java.util.Set.of(
                "title", "publisher", "price", "quantity", "publishDate", "coverImage", "authorIds")));
    }

    @Test void ratingServiceRejectsInvalidScoreAndOversizedReview() {
        RatingServiceImpl_24110202 ratings = new RatingServiceImpl_24110202();
        assertThrows(IllegalArgumentException.class, () -> ratings.save(1, 1, 0, "review"));
        assertThrows(IllegalArgumentException.class, () -> ratings.save(1, 1, 5, "x".repeat(2001)));
    }
}
