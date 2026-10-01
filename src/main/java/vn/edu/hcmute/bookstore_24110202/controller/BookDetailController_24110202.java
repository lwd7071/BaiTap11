package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.RatingServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/book/detail")
public class BookDetailController_24110202 extends HttpServlet {
    private final BookServiceImpl_24110202 books = new BookServiceImpl_24110202();
    private final RatingServiceImpl_24110202 ratings = new RatingServiceImpl_24110202();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        Integer id = ValidationUtil_24110202.integer(q.getParameter("id"));
        if (id == null || id < 1) {
            p.sendError(404);
            return;
        }

        Optional<Book_24110202> b = books.find(id);
        if (b.isEmpty()) {
            p.sendError(404);
            return;
        }

        q.setAttribute("book", b.get());
        q.setAttribute("reviews", ratings.reviews(id));
        q.setAttribute("reviewCount", ratings.count(id));

        q.getRequestDispatcher("/WEB-INF/views/book/detail.jsp").include(q, p);
    }
}
