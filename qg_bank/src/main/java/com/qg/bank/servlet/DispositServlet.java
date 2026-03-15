package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionServiceImpl;
import com.qg.bank.service.TransactionService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/dispositServlet")
public class DispositServlet extends HttpServlet {

    private TransactionService transactionService = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader br = request.getReader();
        String params = br.readLine();

        try {
            JSONObject jsonObject = JSON.parseObject(params);
            // 若输入不是数字，json解析会报错，捕获异常
            BigDecimal dispositAmount = jsonObject.getBigDecimal("dispositAmount");

            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");

            Integer user_id = user.getId();
            Result result = transactionService.disposit(user_id, dispositAmount, "存款");
            if (result.isSuccess()) {
                // 更新session中的用户余额
                user.setBalance(user.getBalance().add(dispositAmount));
            }
            response.getWriter().write(JSON.toJSONString(result));
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