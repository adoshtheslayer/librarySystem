package uz.pdp.bookshop.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@Data
@Builder
public class User implements Base {
    private Long id;
    private String username;
    private String fullName;
    private String password;

    private Integer roleId;

    private String roleName;

    public User(Long id, String fullName, String username, String roleName, String password) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.roleName = roleName;
        this.password = password;
    }

    public User(String fullName, String password, String username, Integer role_id) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roleId = role_id;
    }

    public User(Long id, String fullName, String username, String password, Integer roleId, String roleName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public User(Long id,String fullName, String username,  Integer roleId, String password) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.roleId = roleId;
    }

    public User(Long id, String username, String fullName, String password) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    @Override
    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getLong("id");
            this.fullName = resultSet.getString("full_name");
            this.username = resultSet.getString("username");
            this.password = resultSet.getString("password");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
