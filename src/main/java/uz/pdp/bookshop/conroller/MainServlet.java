package uz.pdp.bookshop.conroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.model.Author;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.Category;
import uz.pdp.bookshop.model.dao.AuthorDao;
import uz.pdp.bookshop.model.dao.BookDao;
import uz.pdp.bookshop.model.dao.CategoryDao;
import uz.pdp.bookshop.service.BookService;

import java.io.IOException;
import java.util.List;

import static uz.pdp.bookshop.utils.Util.MAIN_PAGE_SIZE;
import static uz.pdp.bookshop.utils.Util.PAGE_SIZE;

@WebServlet(value = "/main")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageStr = req.getParameter("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        List<Book> bookList = BookDao.getAllBooks(MAIN_PAGE_SIZE, page);
        req.setAttribute("bookList", bookList);

        List<Category> allCategories = CategoryDao.getAllCategories();
        req.setAttribute("categoryList", allCategories);

        List<Author> allAuthors = AuthorDao.getAllAuthors();
        req.setAttribute("authorList", allAuthors);

        int total = (int) Math.ceil((double) BookService.getTotalSize() / (double) MAIN_PAGE_SIZE);
        req.setAttribute("total", total);

        req.setAttribute("bookList", bookList);
        req.getRequestDispatcher("navbar.jsp").forward(req, resp);

    }
}
