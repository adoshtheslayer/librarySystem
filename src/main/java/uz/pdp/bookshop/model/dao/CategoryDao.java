package uz.pdp.bookshop.model.dao;

import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    public static List<Category> getAllCategories() {

        try (Connection connection = DatabaseInit.getConnection();) {
            List<Category> categoryList = new ArrayList<>();

            String sql = "select * from category";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                Category category = Category.builder()
                        .id(id)
                        .name(name)
                        .build();

                categoryList.add(category);
            }
            return categoryList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
