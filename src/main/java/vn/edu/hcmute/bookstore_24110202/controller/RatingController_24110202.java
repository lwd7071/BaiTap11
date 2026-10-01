package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.RatingServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/rating/create")
public class RatingController_24110202 extends HttpServlet {
    private final RatingServiceImpl_24110202 ratings = new RatingServiceImpl_24110202();
    private final BookServiceImpl_24110202 books = new BookServiceImpl_24110202();

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Integer bookId = ValidationUtil_24110202.integer(request.getParameter("bookId"));
        if (bookId == null || bookId < 1) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
        if (books.find(bookId).isEmpty()) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        Integer score = ValidationUtil_24110202.integer(request.getParameter("rating"));
        String text = request.getParameter("reviewText");
        String review = text == null ? "" : text.trim();
        if (score == null || score < 1 || score > 5 || review.isEmpty() || review.length() > 2000) {
            request.getSession().setAttribute("flashError", "Đánh giá cần có điểm từ 1 đến 5 và nội dung tối đa 2000 ký tự");
        } else {
            User_24110202 user = (User_24110202) request.getSession(false).getAttribute("currentUser");
            if (ratings.exists(user.getId(), bookId)) request.getSession().setAttribute("flashError", "Bạn đã đánh giá sách này");
            else {
                ratings.save(user.getId(), bookId, score, review);
                request.getSession().setAttribute("flashSuccess", "Đã gửi đánh giá");
            }
        }
        response.sendRedirect(request.getContextPath() + "/book/detail?id=" + bookId);
    }
}
