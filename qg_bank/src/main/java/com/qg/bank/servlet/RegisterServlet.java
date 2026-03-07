package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.UserService;
import com.qg.bank.service.UserServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {

    private UserServiceInterface userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        String params = sb.toString();
        User user = JSON.parseObject(params, User.class);

        // 后端输入验证
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            Result result = new Result(false, "用户名不能为空");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if (user.getUsername().length() > 50) {
            Result result = new Result(false, "用户名长度不能超过50个字符");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if (!user.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            Result result = new Result(false, "用户名只能包含字母、数字和下划线");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            Result result = new Result(false, "密码不能为空");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if (user.getPassword().length() > 100) {
            Result result = new Result(false, "密码长度不能超过100个字符");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        if (!user.getPassword().matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$")) {
            Result result = new Result(false, "密码至少6位，包含字母和数字");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        // 检查用户名是否存在
        Integer id;
        try {
            id = userService.selectByName(user.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // 如果用户名存在，返回错误信息
        if (id != null) {
            Result result = new Result(false, "用户名已存在");
            response.getWriter().write(JSON.toJSONString(result));
        }else{
        // 如果用户名不存在，注册用户
            try {
                userService.register(user.getUsername(),user.getPassword());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // 注册成功，返回成功信息
            Result result = new Result(true, "注册成功");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}