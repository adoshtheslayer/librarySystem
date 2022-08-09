package uz.pdp.bookshop.service;

import uz.pdp.bookshop.database.DatabaseInit;
import uz.pdp.bookshop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements DatabaseInit, Base<User> {

    public boolean checkUser(String username, String password){

        Connection connection = DatabaseInit.getConnection();
        boolean exist = false;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select * from check_user(?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                exist = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exist;
    }

    @Override
    public boolean add(User user) {

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from add_user(?,?,?)");
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.execute();

            PreparedStatement preparedStatement1
                    = connection.prepareStatement("insert into users_roles VALUES ((select currval('users_id_seq')), ?)");
            preparedStatement1.setInt(1, user.getRoleId());
            preparedStatement1.execute();

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> get() {

        List<User> userList = new ArrayList<>();

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select u.id, u.full_name, u.username, r.name as role, u.password  from users u join users_roles ur on u.id = ur.user_id\n" +
                            "join roles r on r.id = ur.roles_id");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long userId = resultSet.getLong("id");
                String full_name = resultSet.getString("full_name");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");
                User user = new User(userId, full_name, username, role, password);
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public boolean delete(Long id) {

        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from users_roles where user_id = ?");
            preparedStatement.setLong(1, id);
            int executeUpdate1 = preparedStatement.executeUpdate();


            PreparedStatement preparedStatement1
                    = connection.prepareStatement("delete from users where id = ?");
            preparedStatement1.setLong(1, id);

            int executeUpdate2 = preparedStatement1.executeUpdate();
            return executeUpdate1 == 1 && executeUpdate2 == 1;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void edit(User user) {
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("update users set full_name=?,  username=?, password=? where id=?;");
            preparedStatement.setString(1, user.getFullName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.execute();

            PreparedStatement preparedStatement1
                    = connection.prepareStatement("update users_roles set roles_id = ? where user_id = ?");
            preparedStatement1.setInt(1, user.getRoleId());
            preparedStatement1.setLong(2, user.getId());
            preparedStatement1.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(Long id){
        Connection connection = DatabaseInit.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select u.id, u.full_name, u.username, u.password, r.id as role_id, r.name as role_name from users u\n" +
                    "    join users_roles ur on u.id = ur.user_id\n" +
                    "    join roles r on r.id = ur.roles_id where u.id = ?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                long userId = resultSet.getLong("id");
                String full_name = resultSet.getString("full_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                String roleName = resultSet.getString("role_name");
                User user = new User(userId, full_name, username, password, roleId, roleName);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int getTotalSize() {
        try (Connection connection = DatabaseInit.getConnection();) {
            String sql = "select count(*) from users";
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

    public static List<User> getAllStudents(int size, int page) {

        try (Connection connection = DatabaseInit.getConnection();) {
            List<User> userList = new ArrayList<>();

            String sql = "select u.id, u.full_name, u.username, r.name as role, u.password  from users u join users_roles ur on u.id = ur.user_id\n" +
            "join roles r on r.id = ur.roles_id limit ? offset ? * ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, size);
            preparedStatement.setInt(3, page);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long userId = resultSet.getLong("id");
                String full_name = resultSet.getString("full_name");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");
                User user = new User(userId, full_name, username, role, password);
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRole(String username){

        Connection connection = DatabaseInit.getConnection();
        String role = "";
        Long userId = getUserId(username);
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select distinct r.name from users u\n" +
                    "join users_roles ur on u.id = ur.user_id\n" +
                    "join roles r on r.id = ur.roles_id where u.id = ?");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                role = resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return role;
    }

    public Long getUserId(String username){
        Connection connection = DatabaseInit.getConnection();
        Long userId = null;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select id from users where username=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                userId = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
