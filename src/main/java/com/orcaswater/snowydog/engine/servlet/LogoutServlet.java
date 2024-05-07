package com.orcaswater.snowydog.engine.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * @projectName: snowydog
 * @package: com.orcaswater.snowydog.engine.servlet
 * @className: LogoutServlet
 * @author: Orca121
 * @description: TODO
 * @createTime: 2024-05-07 17:47
 * @version: 1.0
 */

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        resp.sendRedirect("/");
    }
}
