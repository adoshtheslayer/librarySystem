package uz.pdp.bookshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {
    private Long id;
    private String title;
    private Integer quantity;
    private Set<Author> authors;

    private Set<Long> authorsIds;
    private Category category;

    private Long categoryId;
    private String isbn;
    private Date date;
    private String imgUrl;


    public Book(Long id, String title, Integer quantity, Set<Author> authors, Category category, String isbn,
                Date date, String imgUrl) {
        this.id = id;
        this.title = title;
        this.quantity= quantity;
        this.authors = authors;
        this.category = category;
        this.isbn = isbn;
        this.date = date;
        this.imgUrl = imgUrl;
    }
}
