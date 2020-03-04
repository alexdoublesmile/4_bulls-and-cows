package com.plohoy.bulls.servlet;

import com.plohoy.bulls.domain.User;
import com.plohoy.bulls.exception.DaoException;
import com.plohoy.bulls.service.UserService;
import com.plohoy.bulls.util.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);

    private UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/view/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = null;

        try {
            user = service.findUser(login, password);

        } catch (DaoException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException();
        }

        if (user == null) {
            req.setAttribute("errorString", "Unknown user or invalid password, please try again");
            getServletContext().getRequestDispatcher("/WEB-INF/view/login.jsp")
                    .forward(req, resp);

            return;
        }

        AppUtils.storeLoginedUser(req.getSession(), user);

        int redirectId = 0;
        try {
            redirectId = Integer.parseInt(req.getParameter("redirectId"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException();
        }
        String requestUri = AppUtils.getRedirectAfterLoginUrl(req.getSession(), redirectId);
        if(requestUri != null) {
            resp.sendRedirect(requestUri);
        } else {
            resp.sendRedirect(req.getContextPath() + "/userInfo");
        }
    }
}
