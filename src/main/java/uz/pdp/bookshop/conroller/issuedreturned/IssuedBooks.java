package uz.pdp.bookshop.conroller.issuedreturned;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.model.Issued_Returned_Books;
import uz.pdp.bookshop.model.User;
import uz.pdp.bookshop.model.dao.BookDao;
import uz.pdp.bookshop.service.IssuedReturnedBookService;
import uz.pdp.bookshop.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(value = "/issued")
public class IssuedBooks extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        long userId = 0;
        if (id != null){
            userId = Long.parseLong(id);
        }

        IssuedReturnedBookService service = new IssuedReturnedBookService();
        service.issued(userId);

        resp.sendRedirect("/issued-returned");
    }
}
