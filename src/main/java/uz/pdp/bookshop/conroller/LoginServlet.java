package uz.pdp.bookshop.conroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uz.pdp.bookshop.service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();


        Boolean error = Boolean.valueOf(req.getParameter("error"));
        Boolean logout = Boolean.valueOf(req.getParameter("logout"));
        if (error)
            writer.print("<h1 style=\"color: red\">Incorrect username or password</h1>");
        else if (logout)
            writer.print("<h1 style=\"color: green\">You have successfully logged out!!!</h1>");

        req.getRequestDispatcher("index.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        boolean checkUser = new UserService().checkUser(username, password);
        String role = new UserService().getRole(username);

        if (checkUser) {
            HttpSession session = req.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("isAuthenticated", true);

            if (role.equals("SUPER_ADMIN") || role.equals("ADMIN")) {
                resp.sendRedirect("/main");
            }else {
                resp.sendRedirect("/student");
            }

        } else {
            writer.print("Sorry username or password wrong!!!");
            req.getRequestDispatcher("index.jsp").include(req, resp);
        }
    }
}
