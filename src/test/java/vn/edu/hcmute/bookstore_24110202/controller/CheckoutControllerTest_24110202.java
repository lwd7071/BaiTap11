package vn.edu.hcmute.bookstore_24110202.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import java.util.Map;
import org.junit.jupiter.api.Test;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.service.IOrderService_24110202;

class CheckoutControllerTest_24110202 {
    @Test void invalidCheckoutRetainsFormErrorsAndCart() throws Exception {
        IOrderService_24110202 orders=mock(IOrderService_24110202.class);
        CheckoutForm_24110202 form=new CheckoutForm_24110202();
        when(orders.validateCheckout(any())).thenAnswer(call -> {
            CheckoutForm_24110202 submitted=call.getArgument(0);
            FormResult_24110202<CheckoutForm_24110202> result=new FormResult_24110202<>(submitted);
            result.addError("phone","Số điện thoại không hợp lệ"); return result;
        });
        CheckoutController_24110202 controller=new CheckoutController_24110202(orders);
        HttpServletRequest request=mock(HttpServletRequest.class);HttpServletResponse response=mock(HttpServletResponse.class);
        HttpSession session=mock(HttpSession.class);Cart_24110202 cart=new Cart_24110202();
        RequestDispatcher dispatcher=mock(RequestDispatcher.class);
        when(request.getSession()).thenReturn(session);when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getParameter("recipientName")).thenReturn("Mai");when(request.getParameter("phone")).thenReturn("bad");
        when(request.getParameter("shippingAddress")).thenReturn("12 Main");when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);
        controller.doPost(request,response);
        verify(request).setAttribute(eq("form"),any(CheckoutForm_24110202.class));
        verify(request).setAttribute("errors",Map.of("phone","Số điện thoại không hợp lệ"));
        verify(dispatcher).include(request,response);
        assertTrue(cart.isEmpty());
        verify(orders,never()).placeOrder(anyInt(),any(),any());
    }
}
