package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.UserServiceImpl;
import com.qg.bank.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/selfInfoServlet")
public class SelfInfoServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("user");

        // 根据id重新查询，更新session中的user对象
        int id = ((User)userObj).getId();
        Result result = userService.selectById(id);
        if (result.isSuccess()) {
            User user = (User) result.getData();
            session.setAttribute("user", user);
            response.getWriter().write(JSON.toJSONString(user));
        } else {
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}