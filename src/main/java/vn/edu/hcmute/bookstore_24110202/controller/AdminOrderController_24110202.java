package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import vn.edu.hcmute.bookstore_24110202.entity.OrderStatus_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.OrderServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.ValidationUtil_24110202;

@WebServlet("/admin/orders")
public class AdminOrderController_24110202 extends HttpServlet {
    private final OrderServiceImpl_24110202 orders = new OrderServiceImpl_24110202();
    @Override protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException {
        OrderStatus_24110202 status=OrderStatus_24110202.parse(q.getParameter("status"));
        int requested=Math.max(1,Optional.ofNullable(ValidationUtil_24110202.integer(q.getParameter("page"))).orElse(1));
        var result=orders.findAdminOrders(status,requested,10);
        int pages=ValidationUtil_24110202.totalPages(result.getTotal(),10), page=Math.min(requested,pages);
        if(page!=requested)result=orders.findAdminOrders(status,page,10);
        Map<Integer,List<OrderStatus_24110202>> transitions=new HashMap<>();
        for(var order:result.getOrders()) transitions.put(order.getOrderId(),Arrays.stream(OrderStatus_24110202.values()).filter(order.getStatus()::canTransitionTo).toList());
        q.setAttribute("orders",result.getOrders());q.setAttribute("currentPage",page);q.setAttribute("totalPages",pages);
        q.setAttribute("selectedStatus",status==null?"ALL":status.name());q.setAttribute("statuses",OrderStatus_24110202.values());q.setAttribute("transitions",transitions);
        q.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").include(q,p);
    }
    @Override protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException {
        Integer id=ValidationUtil_24110202.integer(q.getParameter("orderId"));
        OrderStatus_24110202 target=OrderStatus_24110202.parse(q.getParameter("targetStatus"));
        try {
            if(id==null||id<1||target==null)throw new IllegalArgumentException("Dữ liệu trạng thái đơn không hợp lệ");
            orders.changeStatus(id,target);
            q.getSession().setAttribute("flashSuccess","Đã cập nhật trạng thái đơn hàng");
        } catch(RuntimeException e) { q.getSession().setAttribute("flashError",e.getMessage()==null?"Không thể cập nhật đơn hàng":e.getMessage()); }
        String status=q.getParameter("status"),page=q.getParameter("page");
        p.sendRedirect(q.getContextPath()+"/admin/orders?status="+java.net.URLEncoder.encode(status==null?"ALL":status,java.nio.charset.StandardCharsets.UTF_8)+"&page="+java.net.URLEncoder.encode(page==null?"1":page,java.nio.charset.StandardCharsets.UTF_8));
    }
}
