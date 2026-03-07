package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionService;
import com.qg.bank.service.TransactionServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/withdrawServlet")
public class WithdrawServlet extends HttpServlet {

    private TransactionServiceInterface transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        BufferedReader br = request.getReader();
        String params = br.readLine();

        JSONObject jsonObject = JSON.parseObject(params);
        BigDecimal withdrawAmount = jsonObject.getBigDecimal("withdrawAmount");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            Integer user_id = user.getId();
            Result result = transactionService.withdraw(user_id, withdrawAmount, "取款", null);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}