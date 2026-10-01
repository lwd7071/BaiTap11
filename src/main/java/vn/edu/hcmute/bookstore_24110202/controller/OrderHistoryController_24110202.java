package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import vn.edu.hcmute.bookstore_24110202.entity.*;
import vn.edu.hcmute.bookstore_24110202.service.impl.OrderServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/orders")
public class OrderHistoryController_24110202 extends HttpServlet {
    private final OrderServiceImpl_24110202 orders = new OrderServiceImpl_24110202();
    @Override protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException {
        User_24110202 user=(User_24110202)q.getSession().getAttribute("currentUser");
        OrderStatus_24110202 status=OrderStatus_24110202.parse(q.getParameter("status"));
        int page=Math.max(1,java.util.Optional.ofNullable(ValidationUtil_24110202.integer(q.getParameter("page"))).orElse(1));
        var result=orders.findUserOrders(user.getId(),status,page,10);
        int pages=ValidationUtil_24110202.totalPages(result.getTotal(),10);page=Math.min(page,pages);
        if (page != Math.max(1,java.util.Optional.ofNullable(ValidationUtil_24110202.integer(q.getParameter("page"))).orElse(1)))
            result=orders.findUserOrders(user.getId(),status,page,10);
        q.setAttribute("orders",result.getOrders());q.setAttribute("currentPage",page);q.setAttribute("totalPages",pages);
        q.setAttribute("selectedStatus",status==null?"ALL":status.name());q.setAttribute("statuses",OrderStatus_24110202.values());
        q.getRequestDispatcher("/WEB-INF/views/orders.jsp").include(q,p);
    }
}
