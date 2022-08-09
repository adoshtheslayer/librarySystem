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
import java.util.List;
import java.util.stream.Collectors;

import static uz.pdp.bookshop.utils.Util.PAGE_SIZE;

@WebServlet(value = "/users")

public class UserViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pageStr = req.getParameter("page");
        int page = 0;
        if (pageStr != null) {
            page = Integer.parseInt(pageStr) - 1;
        }

        List<User> userList = UserService.getAllStudents(PAGE_SIZE, page);
        req.setAttribute("userList", userList);
        req.setAttribute("pageSize", PAGE_SIZE);
        int total = (int) Math.ceil((double) UserService.getTotalSize() / (double) PAGE_SIZE);

        List<Role> allRoles = new RoleService().
                getAllRoles().stream().filter(role -> !role.getName()
                        .equals("SUPER_ADMIN")).collect(Collectors.toList());

        req.setAttribute("total", total);
        req.setAttribute("roleList", allRoles);

        req.getRequestDispatcher("users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullname = req.getParameter("fullname");
        String username = req.getParameter("username");
        Integer roleId = Integer.parseInt(req.getParameter("roleId"));
        String password = req.getParameter("password");

        UserService userService = new UserService();
        userService.add(new User(fullname, password, username, roleId));

        resp.sendRedirect("/users");
    }
}
