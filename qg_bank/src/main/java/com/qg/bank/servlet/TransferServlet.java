package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionService;
import com.qg.bank.service.TransactionServiceInterface;
import com.qg.bank.service.UserService;
import com.qg.bank.service.UserServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/transferServlet")
public class TransferServlet extends HttpServlet {

    private UserServiceInterface userService = new UserService();
    private TransactionServiceInterface transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        BufferedReader br = request.getReader();
        String params = br.readLine();

        JSONObject jsonObject = JSON.parseObject(params);
        BigDecimal transferAmount = jsonObject.getBigDecimal("transferAmount");
        String transferTarget = jsonObject.getString("transferTarget");

        Integer target_id;
        try {
            target_id = userService.selectByName(transferTarget);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (target_id == null) {
            Result result = new Result(false, "目标用户不存在");
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }

        HttpSession session = request.getSession();
        User user =(User) session.getAttribute("user");

        try {
            transactionService.transfer(user.getId(), transferAmount,target_id );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Result result = new Result(true, "转账成功");
        response.getWriter().write(JSON.toJSONString(result));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}