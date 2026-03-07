package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.UserService;
import com.qg.bank.service.UserServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/adminServlet")
public class AdminServlet extends HttpServlet {

    private UserServiceInterface userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("user");
        if(user == null){
            Result result = new Result(false, "请先登录");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if(!"admin".equals(user.getRole())){
            Result result = new Result(false, "您不是管理员，没有权限");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        List<User> allUsers = null;
        try {
            allUsers = userService.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().write(JSON.toJSONString(allUsers));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}