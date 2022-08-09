package uz.pdp.bookshop.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Author;
import uz.pdp.bookshop.model.Book;
import uz.pdp.bookshop.model.Category;
import uz.pdp.bookshop.model.User;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookService implements Base<Book>, DatabaseInit {
    @Override
    public boolean add(Book book) {
        return false;
    }

    @Override
    public List<Book> get() {
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
                    "group by b.id, c.id, c.name, b.title";


            PreparedStatement preparedStatement = connection.prepareStatement(sql);

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

    @Override
    public boolean delete(Long id) {
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("delete from books_authors where book_id=?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

            PreparedStatement preparedStatement1 =
                    connection.prepareStatement("delete from books where id=?");
            preparedStatement1.setLong(1, id);
            preparedStatement1.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    @Override
    public void edit(Book book) {
            try {

                Connection connection = DatabaseInit.getConnection();

                String updateBook = "update books set title=?,category_id=?, \"imgUrl\"=?,quantity=?,isbn=?,date=?  where id=? ";


                PreparedStatement preparedStatement = connection.prepareStatement(updateBook);

                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setInt(2, Math.toIntExact(book.getCategoryId()));
                preparedStatement.setString(3, book.getImgUrl());
                preparedStatement.setInt(4, book.getQuantity());
                preparedStatement.setString(5, book.getIsbn());
                preparedStatement.setDate(6, book.getDate());
                preparedStatement.setInt(7, Math.toIntExact(book.getId()));

                int executeUpdate = preparedStatement.executeUpdate();


                PreparedStatement preparedStatement1 = connection.prepareStatement("delete  from books_authors where  book_id=?");
                preparedStatement1.setLong(1,book.getId());
                int executeUpdate1 = preparedStatement1.executeUpdate();


                String insertBooksAuthors = "insert into books_authors VALUES (?, ?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(insertBooksAuthors);


                int executeUpdate2 = 0;
                for (Long authorId : book.getAuthorsIds()) {
                    preparedStatement2.setLong(1, book.getId());
                    preparedStatement2.setLong(2, authorId);
                    executeUpdate2 = preparedStatement2.executeUpdate();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public Book getBookById(long bookId) {
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select * from books where id=?");
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                Date date = resultSet.getDate("date");
                String isbn = resultSet.getString("isbn");
                int quantity = resultSet.getInt("quantity");
                String title = resultSet.getString("title");
                String imgUrl = resultSet.getString("imgUrl");
                long category_id = resultSet.getLong("category_id");
                Book book = Book.builder()
                        .id(id)
                        .date(date)
                        .isbn(isbn)
                        .quantity(quantity)
                        .title(title)
                        .imgUrl(imgUrl)
                        .categoryId(category_id)
                        .build();
                return book;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int getTotalSize() {
        try (Connection connection = DatabaseInit.getConnection();) {
            String sql = "select count(*) from books";
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
