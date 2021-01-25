package com.iuoip.controller.servlet;

import com.iuoip.domain.DormBuild;
import com.iuoip.domain.Record;
import com.iuoip.domain.User;
import com.iuoip.service.DormBuildService;
import com.iuoip.service.RecordService;
import com.iuoip.service.UserService;
import com.iuoip.service.impl.DormBuildServiceImpl;
import com.iuoip.service.impl.RecordServiceImpl;
import com.iuoip.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

@WebServlet("/record.action")
public class RecordServlet extends HttpServlet {
    public RecordServlet() {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        //获取url中的 action
        String action = req.getParameter("action");
        //获取url中的 id
        String id = req.getParameter("id");

        //获取缺勤信息
        RecordService recordService = new RecordServiceImpl();
        //获取用户基本信息
        UserService userService = new UserServiceImpl();
        //获取宿舍楼信息
        DormBuildService dormBuildService = new DormBuildServiceImpl();
        List<DormBuild> dormBuildList = dormBuildService.findDormBuild();
        req.setAttribute("builds", dormBuildList);
        if (action != null && action.equals("list")) {
            //获取前端传过来的搜索参数
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            String dormBuildId = req.getParameter("dormBuildId");
            String searchType = req.getParameter("searchType");
            String keyword = req.getParameter("keyword");

            //查询缺勤信息
            List<Record> records = recordService.findRecord(startDate, endDate, dormBuildId, searchType, keyword);

            req.setAttribute("records", records);
            req.setAttribute("startDate", startDate);
            req.setAttribute("endDate", endDate);
            req.setAttribute("dormBuildId", dormBuildId);
            req.setAttribute("searchType", searchType);
            req.setAttribute("keyword", keyword);

            req.setAttribute("mainRight", "recordList.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("preAdd")) {
            //缺勤信息添加页面
            req.setAttribute("mainRight", "recordAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("save")) {
            //添加 缺勤信息 页面
            String stuCode = req.getParameter("stuCode");
            User user = userService.findByStuCode(stuCode);

            String date = req.getParameter("date");
            //date字符串先转util.Date,再转sql.Date
            java.util.Date date1 = null;
            Date resDate = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
            try {
                date1 = simpleDateFormat.parse(date);
                resDate = new java.sql.Date(date1.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String remark = req.getParameter("remark");

            if (null != id && id.equals("")) {//新增

                Record record = new Record(user, resDate, remark);

                record.setDisabled(0);

                //新增缺勤信息
                recordService.saveRecord(record);

            } else { //修改
                Record record = recordService.findById(Integer.parseInt(id));
                User userByStuCode = userService.findByStuCode(stuCode);
                record.setDate(resDate);
                record.setRemark(remark);
                record.setUser(userByStuCode);

                //更新缺勤信息
                recordService.updateRecord(record);
            }
            //跳转到缺勤信息管理列表
            //重定向:请求链接断开时，不能再下一个servlet或jsp中获取保存在request中的参数
            resp.sendRedirect(getServletContext().getContextPath() + "/record.action?action=list");
        } else if (action != null && action.equals("preUpdate")) {
            //将要修改的缺勤信息回写到修改页面
            //根据缺勤记录的id，获取缺勤信息
            Record record = recordService.findById(Integer.parseInt(id));

            req.setAttribute("record", record);
            req.setAttribute("mainRight", "recordAddOrUpdate.jsp");
            req.getRequestDispatcher("/jsp/main.jsp").forward(req, resp);
        } else if (action != null && action.equals("deleteOrActive")) {
            //获取删除或激活标志
            String disabled = req.getParameter("disabled");

            //根据缺勤记录的id，获取缺勤信息
            Record record = recordService.findById(Integer.parseInt(id));
            record.setDisabled(Integer.parseInt(disabled));

            //更新缺勤记录
            recordService.updateRecord(record);

            //跳转到缺勤记录页面查看删除过后的所有缺勤记录
            resp.sendRedirect(getServletContext().getContextPath() + "/record.action?action=list");
        }
    }
}
