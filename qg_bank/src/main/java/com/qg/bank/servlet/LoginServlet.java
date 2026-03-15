package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.UserServiceImpl;
import com.qg.bank.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置请求字符编码
        request.setCharacterEncoding("utf-8");
        // 设置响应内容类型和字符编码
        response.setContentType("application/json;charset=utf-8");

        BufferedReader reader = request.getReader();
        // 读取请求体中的JSON数据
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        String params = sb.toString();

        User user = JSON.parseObject(params, User.class);

        // 调用UserService的login方法进行登录验证
        Result result = userService.login(user.getUsername(), user.getPassword());

        // 如果登录成功，将用户信息存储到Session中
        if (result.isSuccess()) {
            User loginUser = (User) result.getData();
            HttpSession session = request.getSession();
            session.setAttribute("user", loginUser);

            // 返回响应，包含success字段和角色信息
            Result responseResult = new Result(true, loginUser.getRole());
            response.getWriter().write(JSON.toJSONString(responseResult));
        } else {
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}