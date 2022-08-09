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
public class Category implements Base {
    private Long id;
    private String name;

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong(1);
            this.name = resultSet.getString(2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
