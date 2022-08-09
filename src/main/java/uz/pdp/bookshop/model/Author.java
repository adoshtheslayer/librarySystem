package uz.pdp.bookshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Author implements Base {
    private Long id;
    private String fullName;

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong(1);
            this.fullName = resultSet.getString(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
