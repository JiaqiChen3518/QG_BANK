package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.dao.UserDao;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.UserService;
import com.qg.bank.service.UserServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/selfInfoServlet")
public class SelfInfoServlet extends HttpServlet {

    private UserServiceInterface userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if(user == null){
            Result result = new Result(false, "请先登录");
            response.getWriter().write(JSON.toJSONString(result));
        }

        // 根据id重新查询，更新session中的user对象
        int id = ((User)user).getId();
        try {
            user = userService.selectById(id);
            session.setAttribute("user", user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().write(JSON.toJSONString(user));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}