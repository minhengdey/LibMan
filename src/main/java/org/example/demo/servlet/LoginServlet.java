package org.example.demo.servlet;

import org.example.demo.dao.LibrarianDAO;
import org.example.demo.model.Librarian;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private LibrarianDAO librarianDAO = new LibrarianDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Librarian librarian = librarianDAO.authenticate(username, password);

        if (librarian != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", librarian);
            session.setAttribute("role", librarian.getRole());
            session.setAttribute("userId", librarian.getId());

            if (librarian.getRole().equals("LIBRARIAN")) {
                response.sendRedirect(request.getContextPath() + "/librarian/borrow");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } else {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}