package uz.pdp.bookshop.model.dao;

import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao {
    public static List<Author> getAllAuthors() {

        try (Connection connection = DatabaseInit.getConnection();) {
            List<Author> authorList = new ArrayList<>();

            String sql = "select * from authors";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String fullName = resultSet.getString("full_name");

                Author author = Author.builder()
                        .id(id)
                        .fullName(fullName)
                        .build();

                authorList.add(author);
            }
            return authorList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
