package vn.edu.hcmute.bookstore_24110202.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import vn.edu.hcmute.bookstore_24110202.dto.*;
import vn.edu.hcmute.bookstore_24110202.service.impl.*;
import vn.edu.hcmute.bookstore_24110202.util.*;

@WebServlet("/admin/books")
@MultipartConfig(maxFileSize=5*1024*1024, maxRequestSize=6*1024*1024)
public class AdminBookController_24110202 extends HttpServlet {
    private static final Set<String> IMAGE_TYPES=Set.of("image/jpeg","image/png","image/gif","image/webp");
    private final BookServiceImpl_24110202 books=new BookServiceImpl_24110202();
    private final AuthorServiceImpl_24110202 authors=new AuthorServiceImpl_24110202();

    protected void doGet(HttpServletRequest q,HttpServletResponse p)throws ServletException,IOException{
        String path=q.getPathInfo();
        if(path==null||path.equals("/")){
            int page=Math.max(1,Optional.ofNullable(ValidationUtil_24110202.integer(q.getParameter("page"))).orElse(1));
            int total=ValidationUtil_24110202.totalPages(books.count(),10);
            q.setAttribute("books",books.page(Math.min(page,total),10));
            q.setAttribute("currentPage",Math.min(page,total));q.setAttribute("totalPages",total);
            q.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").include(q,p);return;
        }
        q.setAttribute("authors",authors.all());
        Integer id=ValidationUtil_24110202.integer(q.getParameter("id"));
        if(id!=null)q.setAttribute("book",books.find(id).orElse(null));
        q.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").include(q,p);
    }

    protected void doPost(HttpServletRequest q,HttpServletResponse p)throws IOException,ServletException{
        String action=q.getParameter("action");
        Integer id=ValidationUtil_24110202.integer(q.getParameter("id"));
        try{
            if("delete".equals(action)){
                if(id!=null)books.delete(id);
                q.getSession().setAttribute("flashSuccess","Đã xóa sách");
            }else saveBook(q,id,action);
        }catch(IllegalArgumentException e){showError(q,p,e.getMessage());return;}
        catch(RuntimeException e){q.getSession().setAttribute("flashError","Không thể lưu sách: "+e.getMessage());}
        p.sendRedirect(q.getContextPath()+"/admin/books");
    }

    private void saveBook(HttpServletRequest q,Integer id,String action)throws IOException,ServletException{
        BookForm_24110202 f=new BookForm_24110202();
        f.setIsbn(q.getParameter("isbn"));f.setTitle(q.getParameter("title"));
        f.setPublisher(q.getParameter("publisher"));f.setPrice(q.getParameter("price"));
        f.setQuantity(q.getParameter("quantity"));f.setPublishDate(q.getParameter("publishDate"));
        f.setCoverImage(q.getParameter("coverImage"));f.setDescription(q.getParameter("description"));
        f.setAuthorIds(q.getParameterValues("authorIds"));
        String newName=Optional.ofNullable(q.getParameter("newAuthorName")).orElse("").trim();
        String newDate=q.getParameter("newAuthorDateOfBirth");
        boolean hasAuthors=f.getAuthorIds()!=null&&f.getAuthorIds().length>0;
        boolean validNew=newName.isEmpty()||(newName.length()<=100&&ValidationUtil_24110202.pastOrToday(newDate));
        if(f.getTitle()==null||f.getTitle().isBlank()||(!hasAuthors&&newName.isEmpty())||!validNew
                ||!ValidationUtil_24110202.positiveInteger(f.getIsbn())
                ||!ValidationUtil_24110202.price(f.getPrice())
                ||!ValidationUtil_24110202.nonNegativeInteger(f.getQuantity()))
            throw new IllegalArgumentException("Hãy kiểm tra dữ liệu và chọn hoặc nhập ít nhất một tác giả");

        Part cover=q.getPart("coverFile");
        if(cover!=null&&cover.getSize()>0)f.setCoverImage(storeCover(cover));
        if(!newName.isEmpty()){
            AuthorForm_24110202 af=new AuthorForm_24110202();af.setName(newName);af.setDateOfBirth(newDate);
            Integer newId=authors.create(af).getId();
            List<String> ids=new ArrayList<>();
            if(f.getAuthorIds()!=null)ids.addAll(Arrays.asList(f.getAuthorIds()));
            ids.add(String.valueOf(newId));f.setAuthorIds(ids.toArray(String[]::new));
        }
        books.save(f,"edit".equals(action)?id:null);
        q.getSession().setAttribute("flashSuccess","Đã lưu sách");
    }

    private String storeCover(Part file)throws IOException{
        String type=Optional.ofNullable(file.getContentType()).orElse("").toLowerCase();
        if(!IMAGE_TYPES.contains(type))throw new IllegalArgumentException("Ảnh phải là JPEG, PNG, GIF hoặc WebP");
        String ext=switch(type){case "image/jpeg"->".jpg";case "image/png"->".png";case "image/gif"->".gif";case "image/webp"->".webp";default->throw new IllegalArgumentException("Ảnh không hợp lệ");};
        Path dir=Path.of(getServletContext().getRealPath("/uploads"));Files.createDirectories(dir);
        String name=UUID.randomUUID()+ext;
        try(InputStream in=file.getInputStream()){Files.copy(in,dir.resolve(name));}
        return "uploads/"+name;
    }

    private void showError(HttpServletRequest q,HttpServletResponse p,String message)throws ServletException,IOException{
        q.setAttribute("message","Dữ liệu sách không hợp lệ");q.setAttribute("errors",Map.of("form",message));
        q.setAttribute("authors",authors.all());
        Integer id=ValidationUtil_24110202.integer(q.getParameter("id"));
        if(id!=null)q.setAttribute("book",books.find(id).orElse(null));
        q.getRequestDispatcher("/WEB-INF/views/admin/book-form.jsp").include(q,p);
    }
}
