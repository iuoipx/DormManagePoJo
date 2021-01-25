package com.iuoip.controller.servlet;

import com.iuoip.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/loginOut.action")
public class LogoutServlet extends HttpServlet {

    public LogoutServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //清除保存在session会话中的用户信息
        req.getSession().removeAttribute("session_user");

        //清除保存在cookie中的用户信息
        Cookie cookie_name_pass = CookieUtil.getCookieByName(req, "cookie_name_pass");
        if (cookie_name_pass != null) {
            //将cookie的有效时间置为0
            cookie_name_pass.setMaxAge(0);

            //设置cookie的作用范围
            cookie_name_pass.setPath(req.getContextPath());

            //将cookie响应出去
            resp.addCookie(cookie_name_pass);
        }

        resp.sendRedirect("index.jsp");
    }
}
