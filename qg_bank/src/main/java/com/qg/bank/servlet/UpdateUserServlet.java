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

@WebServlet("/updateUserServlet")
public class UpdateUserServlet extends HttpServlet {

    private UserServiceInterface userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        String params = sb.toString();

        User updateUser = JSON.parseObject(params, User.class);

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            Result result = new Result(false, "用户未登录");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        String newUsername = updateUser.getUsername();
        String newPassword = updateUser.getPassword();

        // 验证用户名是否为空
        if (newUsername == null || newUsername.trim().isEmpty()) {
            Result result = new Result(false, "用户名不能为空");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        // 如果用户名改变了，检查新用户名是否已存在
        if (!newUsername.equals(currentUser.getUsername())) {
            try {
                Integer existingUserId = userService.selectByName(newUsername);
                if (existingUserId != null && !existingUserId.equals(currentUser.getId())) {
                    Result result = new Result(false, "用户名已存在");
                    response.getWriter().write(JSON.toJSONString(result));
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Result result = new Result(false, "系统错误");
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
        }

        // 更新用户信息
        try {
            // 调用Service层更新用户信息
            userService.updateUser(currentUser.getId(), newUsername, newPassword);

            // 更新session中的用户信息
            currentUser.setUsername(newUsername);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                currentUser.setPassword(newPassword);
            }
            session.setAttribute("user", currentUser);

            Result result = new Result(true, "修改成功");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (SQLException e) {
            e.printStackTrace();
            Result result = new Result(false, "修改失败：" + e.getMessage());
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
