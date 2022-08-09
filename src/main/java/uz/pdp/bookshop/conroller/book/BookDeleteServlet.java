package uz.pdp.bookshop.conroller.book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.service.BookService;

import java.io.IOException;

@WebServlet(value = "/delete-book")
public class BookDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        BookService bookService = new BookService();
        bookService.delete(id);

        resp.sendRedirect("/books");
    }
}
