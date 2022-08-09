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

@WebServlet(value = "/delete-user")
public class UserDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));
        UserService userService = new UserService();
        userService.delete(id);

        resp.sendRedirect("/users");
    }
}
