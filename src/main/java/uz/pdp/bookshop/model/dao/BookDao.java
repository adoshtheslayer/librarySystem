package uz.pdp.bookshop.model.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Author;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.Category;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookDao  implements DatabaseInit{

    public static List<Book> getAllBooks(int size, int page) {
        try {
            ArrayList<Book> bookList = new ArrayList<>();
            Connection connection = DatabaseInit.getConnection();

            String sql = "select b.id, b.quantity, b.isbn, b.date,\n" +
                    "       b.title,\n" +
                    "       b.\"imgUrl\",\n" +
                    "       json_agg(\n" +
                    "               json_build_object(\n" +
                    "                       'id', a.id,\n" +
                    "                       'fullName', a.full_name)) as authors,\n" +
                    "    json_build_object('id', c.id, 'name', c.name) as category\n" +
                    "from books b\n" +
                    "         join books_authors ba on b.id = ba.book_id\n" +
                    "         join authors a on a.id = ba.authors_id\n" +
                    "         join category c on c.id = b.category_id\n" +
                    "group by b.id, c.id, c.name, b.title\n" +
                    "limit ? offset ? * ?";


            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, page);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                long bookId = resultSet.getLong("id");
                int quantity = resultSet.getInt("quantity");
                String isbn = resultSet.getString("isbn");
                Date date = resultSet.getDate("date");
                String title = resultSet.getString("title");
                Array array = resultSet.getArray("authors");
                Object categoryObj = resultSet.getObject("category");
                String imgUrl = resultSet.getString("imgUrl");
                Type listType = new TypeToken<Set<Author>>() {
                }.getType();
                Set<Author> list = new Gson().fromJson(array.toString(), listType);

                Category category = new Gson().fromJson(categoryObj.toString(), Category.class);

                Book book = Book.builder()
                        .id(bookId)
                        .title(title)
                        .quantity(quantity)
                        .isbn(isbn)
                        .date(date)
                        .authors(list)
                        .category(category)
                        .imgUrl(imgUrl)
                        .build();

                bookList.add(book);
            }
            return bookList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addNewBook(Book book) {

        try {

            Connection connection = DatabaseInit.getConnection();

            String insertBook = "insert into books (date, isbn, title, quantity, category_id, \"imgUrl\") VALUES " +
                    "(?, ?, ?, ?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertBook);

            preparedStatement.setDate(1, book.getDate());
            preparedStatement.setString(2, book.getIsbn());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.setLong(5, book.getCategoryId());
            preparedStatement.setString(6, book.getImgUrl());


            String insertBooksAuthors = "insert into books_authors VALUES ((select currval('books_id_seq')), ?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(insertBooksAuthors);

            int executeUpdate1 = preparedStatement.executeUpdate();

            int executeUpdate2 = 0;
            for (Long authorId : book.getAuthorsIds()) {
                preparedStatement2.setLong(1, authorId);
                executeUpdate2 = preparedStatement2.executeUpdate();
            }

            return executeUpdate1 == 1 && executeUpdate2 == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

//    public static boolean editBook(Book book) {
//
//        try {
//
//            Connection connection = DatabaseInit.getConnection();
//
////            String insertBook = "update books set date=?, isbn=?, title=?, quantity=?, category_id=?, imgUrl=? where id=?;
////
////            PreparedStatement preparedStatement = connection.prepareStatement(insertBook);
////
////            preparedStatement.setDate(1, book.getDate());
////            preparedStatement.setString(2, book.getIsbn());
////            preparedStatement.setString(3, book.getTitle());
////            preparedStatement.setInt(4, book.getQuantity());
////            preparedStatement.setLong(5, book.getCategoryId());
////            preparedStatement.setString(6, book.getImgUrl());
////            preparedStatement.setLong(7, book.getId());
////
//
//
//            String insertBooksAuthors = "insert into books_authors VALUES ((select currval('books_id_seq')), ?)";
//            PreparedStatement preparedStatement2 = connection.prepareStatement(insertBooksAuthors);
//
////            int executeUpdate1 = preparedStatement.executeUpdate();
//
//            int executeUpdate2 = 0;
//            for (Long authorId : book.getAuthorsIds()) {
//                preparedStatement2.setLong(1, authorId);
//                executeUpdate2 = preparedStatement2.executeUpdate();
//            }
//
//            return executeUpdate1 == 1 && executeUpdate2 == 1;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
