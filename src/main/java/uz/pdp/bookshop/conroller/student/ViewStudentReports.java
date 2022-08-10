package uz.pdp.bookshop.conroller.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.model.Issued_Returned_Books;
import uz.pdp.bookshop.service.IssuedReturnedBookService;
import uz.pdp.bookshop.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static uz.pdp.bookshop.utils.Util.PAGE_SIZE;
import static uz.pdp.bookshop.utils.Util.STUDENT_BOOKS_SIZE;

@WebServlet(value = "/student")
public class ViewStudentReports extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageStr = req.getParameter("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        String username = String.valueOf(req.getSession().getAttribute("username"));
        Long studentId = new UserService().getUserId(username);

        List<Issued_Returned_Books> issued_returned_books
                    = IssuedReturnedBookService.get(PAGE_SIZE, page)
                            .stream().filter(books -> books.getUser_id()
                                    .equals(studentId)).collect(Collectors.toList());

        req.setAttribute("issued_returned_books", issued_returned_books);
        req.setAttribute("pageSize", STUDENT_BOOKS_SIZE);

        int total = (int) Math.ceil((double) IssuedReturnedBookService.getTotalSize() / (double) PAGE_SIZE);
        req.setAttribute("total", total);

        req.getRequestDispatcher("student-reports.jsp").forward(req, resp);
    }
}
