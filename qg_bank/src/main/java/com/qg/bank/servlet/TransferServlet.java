package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionServiceImpl;
import com.qg.bank.service.TransactionService;
import com.qg.bank.service.UserServiceImpl;
import com.qg.bank.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/transferServlet")
public class TransferServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private TransactionService transactionService = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        BufferedReader br = request.getReader();
        String params = br.readLine();

        try {
            JSONObject jsonObject = JSON.parseObject(params);
            BigDecimal transferAmount = jsonObject.getBigDecimal("transferAmount");
            String transferTarget = jsonObject.getString("transferTarget");

            Result transferResult = transactionService.transfer(user.getId(), transferAmount, transferTarget);
            if (transferResult.isSuccess()) {
                // 更新session中的用户余额
                user.setBalance(user.getBalance().subtract(transferAmount));
            }
            response.getWriter().write(JSON.toJSONString(transferResult));
        } catch (Exception e) {
            Result result = new Result(false, "输入金额格式错误");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}