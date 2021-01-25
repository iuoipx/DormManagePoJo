package com.iuoip.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addCookie(String cookieName, int time,
                                 HttpServletRequest req, HttpServletResponse res,
                                 String stuCode, String password) {
        Cookie cookie = getCookieByName(req, cookieName);

        if (cookie != null)
            cookie.setValue(stuCode + "#" + password);
        else
            cookie = new Cookie(cookieName, stuCode + "#" + password);

        //设置cookie有效时间
        cookie.setMaxAge(time);
        //设置cookie作用域为，只在当前项目服务有效
        cookie.setPath(req.getContextPath());

        res.addCookie(cookie);

    }

    public static Cookie getCookieByName(HttpServletRequest req, String cookieName) {
        //从request域中获取点项目的cookie
        Cookie[] cookies = req.getCookies();
        //遍历所有cookie
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies)
                if (cookie.getName().equals(cookie))
                    return cookie;
        }
        return null;
    }
}
