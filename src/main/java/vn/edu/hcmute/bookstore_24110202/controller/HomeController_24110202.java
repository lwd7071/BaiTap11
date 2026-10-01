package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmute.bookstore_24110202.entity.Book_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.BookServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.RatingServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/home")
public class HomeController_24110202 extends HttpServlet {
    private final BookServiceImpl_24110202 books = new BookServiceImpl_24110202();
    private final RatingServiceImpl_24110202 ratings = new RatingServiceImpl_24110202();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        int page = ValidationUtil_24110202.integer(q.getParameter("page")) == null
                ? 1
                : Math.max(1, ValidationUtil_24110202.integer(q.getParameter("page")));
        int total = ValidationUtil_24110202.totalPages(books.count(), 6);
        page = Math.min(page, total);

        q.setAttribute("books", books.page(page, 6));

        Map<Integer, Long> counts = new HashMap<>();
        for (Book_24110202 b : books.page(page, 6)) {
            counts.put(b.getBookid(), ratings.count(b.getBookid()));
        }
        q.setAttribute("reviewCounts", counts);
        q.setAttribute("currentPage", page);
        q.setAttribute("totalPages", total);

        q.getRequestDispatcher("/WEB-INF/views/home.jsp").include(q, p);
    }
}
