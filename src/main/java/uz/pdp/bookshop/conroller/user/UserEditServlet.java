package uz.pdp.bookshop.conroller.user;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.bookshop.model.Role;
import uz.pdp.bookshop.model.User;
import uz.pdp.bookshop.service.RoleService;
import uz.pdp.bookshop.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/edit-user")
public class UserEditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        long userId = 0;
        if (id != null){
            userId = Long.parseLong(id);
        }

        User user = new UserService().getUserById(userId);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        req.setAttribute("userList", userList);

        List<Role> allRoles = new RoleService().getAllRoles();
        req.setAttribute("roleList", allRoles);

        req.getRequestDispatcher("user-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        String fullname = req.getParameter("fullname");
        String username = req.getParameter("username");
        Integer roleId = Integer.parseInt(req.getParameter("roleId"));
        String password = req.getParameter("password");

        UserService userService = new UserService();
        userService.edit(new User(id,fullname, username, roleId, password));

        resp.sendRedirect("/users");
    }
}
