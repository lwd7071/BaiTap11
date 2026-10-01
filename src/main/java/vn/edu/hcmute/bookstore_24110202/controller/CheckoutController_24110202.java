package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.entity.User_24110202;
import vn.edu.hcmute.bookstore_24110202.service.IOrderService_24110202;
import vn.edu.hcmute.bookstore_24110202.service.impl.OrderServiceImpl_24110202;
import vn.edu.hcmute.bookstore_24110202.util.CartViewUtil_24110202;

@WebServlet("/checkout")
public class CheckoutController_24110202 extends HttpServlet {
    private final IOrderService_24110202 orders;
    public CheckoutController_24110202() { this(new OrderServiceImpl_24110202()); }
    public CheckoutController_24110202(IOrderService_24110202 orders) { this.orders = orders; }
    @Override protected void doGet(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        Cart_24110202 cart = cart(q);
        if (cart.isEmpty()) { q.getSession().setAttribute("flashError", "Giỏ hàng đang trống"); p.sendRedirect(q.getContextPath()+"/cart"); return; }
        CheckoutForm_24110202 form = new CheckoutForm_24110202();
        User_24110202 user = (User_24110202) q.getSession().getAttribute("currentUser");
        form.setRecipientName(user.getFullname());
        show(q, p, form, Map.of(), null);
    }
    @Override protected void doPost(HttpServletRequest q, HttpServletResponse p) throws ServletException, IOException {
        CheckoutForm_24110202 form = new CheckoutForm_24110202();
        form.setRecipientName(q.getParameter("recipientName")); form.setPhone(q.getParameter("phone"));
        form.setShippingAddress(q.getParameter("shippingAddress")); form.setNote(q.getParameter("note"));
        var validation = orders.validateCheckout(form);
        Cart_24110202 cart = cart(q);
        if (!validation.isValid()) { show(q,p,form,validation.getFieldErrors(),"Thông tin giao hàng chưa hợp lệ"); return; }
        if (cart.isEmpty()) { show(q,p,form,Map.of("form","Giỏ hàng đang trống"),"Không thể đặt hàng"); return; }
        if (!CartViewUtil_24110202.populate(q, cart)) { show(q,p,form,Map.of("form","Giỏ hàng không còn đủ số lượng"),"Vui lòng cập nhật giỏ hàng"); return; }
        try {
            User_24110202 user = (User_24110202) q.getSession().getAttribute("currentUser");
            int orderId = orders.placeOrder(user.getId(), cart, form);
            cart.clear();
            q.getSession().setAttribute("flashSuccess", "Đặt hàng thành công. Mã đơn: " + orderId);
            p.sendRedirect(q.getContextPath()+"/orders");
        } catch (IllegalArgumentException e) {
            show(q,p,form,Map.of("form",e.getMessage()),"Chưa thể đặt hàng");
        }
    }
    private Cart_24110202 cart(HttpServletRequest q) {
        Cart_24110202 cart = (Cart_24110202) q.getSession().getAttribute("cart");
        return cart == null ? new Cart_24110202() : cart;
    }
    private void show(HttpServletRequest q,HttpServletResponse p,CheckoutForm_24110202 form,Map<String,String> errors,String message)
            throws ServletException,IOException {
        CartViewUtil_24110202.populate(q, cart(q));
        q.setAttribute("form", form); q.setAttribute("errors", errors); q.setAttribute("message", message);
        q.getRequestDispatcher("/WEB-INF/views/checkout.jsp").include(q,p);
    }
}
