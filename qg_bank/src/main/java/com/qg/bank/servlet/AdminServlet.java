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

@WebServlet("/adminServlet")
public class AdminServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (!"admin".equals(user.getRole())) {
            Result result = new Result(false, "您不是管理员，没有权限");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        Result result = userService.getAllUsers();
        response.getWriter().write(JSON.toJSONString(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}