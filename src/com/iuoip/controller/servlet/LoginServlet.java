package com.iuoip.controller.servlet;

import com.iuoip.domain.User;
import com.iuoip.service.UserService;
import com.iuoip.service.impl.UserServiceImpl;
import com.iuoip.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    public LoginServlet() {
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        //post请求乱码
        req.setCharacterEncoding("utf-8");

        //获取前端发送的用户数据
        String stuCode = req.getParameter("stuCode");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        UserService userService = new UserServiceImpl();
        User user = userService.findUser(stuCode, password);

        if (user == null) {
            req.setAttribute("error", "您输入的学号或密码错误");
            req.getRequestDispatcher("index.jsp").forward(req, res);
        } else {
            //登录成功，将返回的用户信息存入session域
            req.getSession().setAttribute("session_user", user);

            //用户如果选择"记住我"后，通过cookie将用户信息保存一周
            if (remember != null && remember.equals("remember-me")) {
                CookieUtil.addCookie("cookie_name_pass",
                        7 * 24 * 60 * 60, req, res,
                        //加密
                        URLEncoder.encode(stuCode, "utf-8"),
                        URLEncoder.encode(password, "utf-8"));
            }
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, res);
        }
    }


}
