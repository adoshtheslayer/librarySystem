package uz.pdp.bookshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Issued_Returned_Books implements Base {
    private Long id;
    private Long user_id;
    private Set<Long> booksIds;
    private Set<Book> books;
    private String userName;
    private String bookName;
    private Long book_id;
    private Date date;
    private boolean is_issued;

    public Issued_Returned_Books(Date date, Long user_id, Set<Long> booksIds, boolean is_issued) {
        this.date = date;
        this.user_id = user_id;
        this.booksIds = booksIds;
        this.is_issued = is_issued;
    }

    public Issued_Returned_Books(Long id, Set<Book> books, String userName, Date date, boolean is_issued, Long user_id) {
        this.id = id;
        this.books = books;
        this.userName = userName;
        this.date = date;
        this.is_issued = is_issued;
        this.user_id = user_id;
    }

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong("id");
            this.user_id = resultSet.getLong("user_id");
            this.book_id = resultSet.getLong("book_id");
            this.date = resultSet.getDate("date");
            this.is_issued = resultSet.getBoolean("is_issued");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
