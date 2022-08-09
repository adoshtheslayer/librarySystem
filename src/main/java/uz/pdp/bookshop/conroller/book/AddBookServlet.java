package uz.pdp.bookshop.conroller.book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.dao.AuthorDao;
import uz.pdp.bookshop.model.dao.BookDao;
import uz.pdp.bookshop.model.dao.CategoryDao;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static uz.pdp.bookshop.utils.Util.UPLOAD_DIRECTORY;


@WebServlet("/add-book")
@MultipartConfig(maxFileSize = 10_000_000)
public class AddBookServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("authorList", AuthorDao.getAllAuthors());
        req.setAttribute("categoryList", CategoryDao.getAllCategories());

        req.getRequestDispatcher("/add-book-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        Integer quantity = Integer.parseInt(req.getParameter("quantity"));
        String[] authorsIdsStr = req.getParameterValues("authorsIds");

        Set<Long> authorsIds = getAuthorIdsFromStrArr(authorsIdsStr);
        Long categoryId = Long.parseLong(req.getParameter("categoryId"));
        String isbn = req.getParameter("isbn");
        Date date = Date.valueOf(req.getParameter("year"));
        Part imagePart = req.getPart("image");

        Book book = Book.builder()
                .title(title)
                .quantity(quantity)
                .date(date)
                .isbn(isbn)
                .authorsIds(authorsIds)
                .categoryId(categoryId)
                .imgUrl(uploadAndGetImageUrl(imagePart))
                .build();

        boolean result = BookDao.addNewBook(book);

        if (result) {
            resp.sendRedirect("/books?added=true");
        }
    }

    private Set<Long> getAuthorIdsFromStrArr(String[] authorsIdsStr) {
        Set<Long> authorIds = new HashSet<>();
        for (String authorId : authorsIdsStr) {
            authorIds.add(Long.parseLong(authorId));
        }
        return authorIds;
    }


    private String uploadAndGetImageUrl(Part imagePart) {
        try {

            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists())
                uploadDir.mkdir();
            int index = imagePart.getSubmittedFileName().lastIndexOf('.');
            String extension = imagePart.getSubmittedFileName().substring(index + 1);
            System.out.println("File extension is " + extension);

            String imgName = System.currentTimeMillis() + "." + extension;
            String imgPath = uploadDir.getPath() + "/" + imgName;
            imagePart.write(imgPath);
            return imgName;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
