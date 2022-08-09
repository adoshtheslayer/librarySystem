package uz.pdp.bookshop.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Author;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.Issued_Returned_Books;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IssuedReturnedBookService implements DatabaseInit {

    public void add( Issued_Returned_Books  issued_returned_books) {

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into issued_returned_books(date, is_issued, book_id, user_id)\n" +
                            "values (?,?,?,?)");
            for (Long book_id : issued_returned_books.getBooksIds()) {
                preparedStatement.setDate(1, issued_returned_books.getDate());
                preparedStatement.setBoolean(2, issued_returned_books.is_issued());
                preparedStatement.setLong(3, book_id);
                preparedStatement.setLong(4, issued_returned_books.getUser_id());
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Issued_Returned_Books> get(int size, int page) {

        List<Issued_Returned_Books> bookList = new ArrayList<>();

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select b.id, u.full_name, b.date, b.is_issued, json_agg(json_build_object(\n" +
                    "        'id', b2.id,'title', b2.title)) as title, b.user_id from issued_returned_books b\n" +
                    "join books b2 on b2.id = b.book_id join users u on u.id = b.user_id\n" +
                    "group by b.is_issued, b.date, u.full_name, b.id limit ? offset ? * ?");
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, page);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String full_name = resultSet.getString("full_name");
                Array array = resultSet.getArray("title");
                Date date = resultSet.getDate("date");
                boolean is_issued = resultSet.getBoolean("is_issued");
                long user_id = resultSet.getLong("user_id");

                Type listType = new TypeToken<Set<Book>>() {}.getType();
                Set<Book> list = new Gson().fromJson(array.toString(), listType);

                Issued_Returned_Books books = new Issued_Returned_Books(id,list, full_name, date, is_issued, user_id);
                bookList.add(books);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookList;
    }

    public void issued(Long id) {

        Issued_Returned_Books books = getSelectionById(id);

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("update issued_returned_books set user_id=?,  book_id=?, is_issued=true where id=?;");
            preparedStatement.setLong(1, books.getUser_id());
            preparedStatement.setLong(2, books.getBook_id());
            preparedStatement.setLong(3, books.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void returned(Long id) {

        Issued_Returned_Books books = getSelectionById(id);

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("update issued_returned_books set user_id=?,  book_id=?, is_issued=false where id=?;");
            preparedStatement.setLong(1, books.getUser_id());
            preparedStatement.setLong(2, books.getBook_id());
            preparedStatement.setLong(3, books.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Issued_Returned_Books getSelectionById(Long id) {
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select * from issued_returned_books where id=?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Issued_Returned_Books books = new Issued_Returned_Books();
                books.get(resultSet);
                return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int getTotalSize() {
        try (Connection connection = DatabaseInit.getConnection();) {
            String sql = "select count(*) from issued_returned_books";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
