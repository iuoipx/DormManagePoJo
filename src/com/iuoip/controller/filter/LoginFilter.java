package com.iuoip.controller.filter;

import com.iuoip.domain.User;
import com.iuoip.service.UserService;
import com.iuoip.service.impl.UserServiceImpl;
import com.iuoip.utils.CookieUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebFilter("/jsp/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setContentType("text/html;charset=utf-8");
        User sessionUser = (User) request.getSession().getAttribute("session_user");

        UserService userService = new UserServiceImpl();

        if (sessionUser != null)
            roleJudgment(sessionUser, request, response, chain);
        else {
            //判断cookie是否有用户信息
            Cookie cookie_name_pass = CookieUtil.getCookieByName(request, "cookie_name_pass");

            if (cookie_name_pass != null) {
                String cookieStr = URLDecoder.decode(cookie_name_pass.getValue(), "utf-8");

                String[] stuCodePassword = cookieStr.split("#");

                User user = userService.findUser(stuCodePassword[0], stuCodePassword[1]);

                if (user != null)
                    roleJudgment(sessionUser, request, response, chain);
                else {
                    request.setAttribute("error", "请先登录");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "请先登录");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }
    }

    private void roleJudgment(User user, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取用户角色
        int roleId = user.getRoleId();

        //获取请求地址
        String servletPath = request.getServletPath();

        //动态获取项目名
        String contextPath = request.getContextPath();

        //当用户发送的是宿舍楼管理模块或者宿舍管理员模块请求时，只有当前用户是超级管理员时才能通过
        if ((servletPath.startsWith(contextPath + "/dormBuild.action") ||
                servletPath.startsWith(contextPath + "/dormManage.action"))
                && roleId == 0) {
            request.getSession().setAttribute("session_user", user);
            chain.doFilter(request, response);
            //当用户发送的是学生模块请求时，只有当前不是学生是才能通过
        } else if (servletPath.startsWith(contextPath + "/student.action")
                && roleId != 2) {
            request.getSession().setAttribute("session_user", user);
            chain.doFilter(request, response);
            //当用户发送的是考勤、修改密码、退出模块请求时，所有角色都能通过
        } else if (servletPath.startsWith(contextPath + "/record.action") ||
                servletPath.startsWith(contextPath + "/password.action") ||
                servletPath.startsWith(contextPath + "/loginOut.action") ||
                servletPath.startsWith(contextPath + "/index.action")) {
            request.getSession().setAttribute("session_user", user);
            chain.doFilter(request, response);
        } else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}

