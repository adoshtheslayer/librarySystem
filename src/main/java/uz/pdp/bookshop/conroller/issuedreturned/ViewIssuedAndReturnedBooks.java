package uz.pdp.bookshop.conroller.issuedreturned;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.Issued_Returned_Books;
import uz.pdp.bookshop.model.User;
import uz.pdp.bookshop.model.dao.BookDao;
import uz.pdp.bookshop.service.BookService;
import uz.pdp.bookshop.service.IssuedReturnedBookService;
import uz.pdp.bookshop.service.UserService;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uz.pdp.bookshop.utils.Util.PAGE_SIZE;

@WebServlet(value = "/issued-returned")

public class ViewIssuedAndReturnedBooks extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageStr = req.getParameter("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }


        List<Issued_Returned_Books> issued_returned_books = IssuedReturnedBookService.get(PAGE_SIZE, page);
        req.setAttribute("issued_returned_books", issued_returned_books);
        req.setAttribute("pageSize", PAGE_SIZE);
        int total = (int) Math.ceil((double) IssuedReturnedBookService.getTotalSize() / (double) PAGE_SIZE);
        req.setAttribute("total", total);

        BookService bookService = new BookService();
        List<Book> bookList = bookService.get();
        req.setAttribute("bookList", bookList);
        UserService userService = new UserService();
        List<User> userList = userService.get();
        req.setAttribute("userList", userList);
        req.getRequestDispatcher("issued_returned.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.parseLong(req.getParameter("studentId"));
        Date date = Date.valueOf(req.getParameter("date"));
        String[] bookIds = req.getParameterValues("booksIds");
        Set<Long> booksIds = getBooksIdsFromStrArr(bookIds);

        boolean res = req.getParameter("optradio1") != null;
        if (req.getParameter("optradio2")!= null){
            res = false;
        }

        IssuedReturnedBookService bookService = new IssuedReturnedBookService();
        bookService.add(new Issued_Returned_Books(date, userId, booksIds, res));

        resp.sendRedirect("/issued-returned");
    }

    private Set<Long> getBooksIdsFromStrArr(String[] booksIds) {
        Set<Long> bookIds = new HashSet<>();
        for (String authorId : booksIds) {
            bookIds.add(Long.parseLong(authorId));
        }
        return bookIds;
    }
}
