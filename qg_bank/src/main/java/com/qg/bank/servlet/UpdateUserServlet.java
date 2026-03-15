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

@WebServlet("/updateUserServlet")
public class UpdateUserServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String newUsername = updateUser.getUsername();
        String newPassword = updateUser.getPassword();

        Result updateResult = userService.updateUser(currentUser.getId(), newUsername, newPassword, currentUser.getUsername());
        if (updateResult.isSuccess()) {
            // 根据id重新查询，更新session中的user对象
            Result selectResult = userService.selectById(currentUser.getId());
            if (selectResult.isSuccess()) {
                User user = (User) selectResult.getData();
                session.setAttribute("user", user);
            }
        }
        response.getWriter().write(JSON.toJSONString(updateResult));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
