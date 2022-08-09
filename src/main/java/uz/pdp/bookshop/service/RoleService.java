package uz.pdp.bookshop.service;

import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleService  {
    public List<Role> getAllRoles(){
        List<Role> roleList = new ArrayList<>();
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select * from roles");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Role role = new Role();
                role.get(resultSet);
                roleList.add(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roleList;
    }
}
