package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import vn.edu.hcmute.bookstore_24110202.service.impl.UserServiceImpl_24110202;

@WebServlet("/login")
public class LoginController_24110202 extends HttpServlet {
    private final UserServiceImpl_24110202 service = new UserServiceImpl_24110202();
    @Override protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException {
        q.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").include(q,p);
    }
    @Override protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException {
        String email=q.getParameter("email"), password=q.getParameter("password");
        var user=email==null||password==null?null:service.authenticate(email,password);
        if(user==null){q.setAttribute("message","Email hoặc mật khẩu không đúng");q.setAttribute("email",email);doGet(q,p);return;}
        HttpSession session=q.getSession(true);session.setAttribute("currentUser",user);
        String destination=(String)session.getAttribute("postLoginRedirect");session.removeAttribute("postLoginRedirect");
        if(destination==null||!destination.startsWith(q.getContextPath()+"/")||destination.startsWith(q.getContextPath()+"//"))
            destination=q.getContextPath()+(Boolean.TRUE.equals(user.getAdmin())?"/admin/books":"/home");
        p.sendRedirect(destination);
    }
}
