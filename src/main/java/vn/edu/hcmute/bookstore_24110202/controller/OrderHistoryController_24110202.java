package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import vn.edu.hcmute.bookstore_24110202.dto.OrderPage_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.OrderServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/orders")
public class OrderHistoryController_24110202 extends HttpServlet {
    private final OrderServiceImpl_24110202 orders = new OrderServiceImpl_24110202();

    @Override
    protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        User_24110202 user = (User_24110202) q.getSession().getAttribute("currentUser");
        OrderStatus_24110202 status = OrderStatus_24110202.parse(q.getParameter("status"));

        int requestedPage = Math.max(1, Optional.ofNullable(ValidationUtil_24110202.integer(q.getParameter("page"))).orElse(1));
        OrderPage_24110202 result = orders.findUserOrders(user.getId(), status, requestedPage, 10);

        int pages = ValidationUtil_24110202.totalPages(result.getTotal(), 10);
        int page = Math.min(requestedPage, pages);

        if (page != requestedPage) {
            result = orders.findUserOrders(user.getId(), status, page, 10);
        }

        q.setAttribute("orders", result.getOrders());
        q.setAttribute("currentPage", page);
        q.setAttribute("totalPages", pages);
        q.setAttribute("selectedStatus", status == null ? "ALL" : status.name());
        q.setAttribute("statuses", OrderStatus_24110202.values());

        q.getRequestDispatcher("/WEB-INF/views/orders.jsp").include(q, p);
    }
}
