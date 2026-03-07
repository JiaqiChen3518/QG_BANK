package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.TransactionRecord;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionService;
import com.qg.bank.service.TransactionServiceInterface;
import com.qg.bank.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/dispositServlet")
public class DispositServlet extends HttpServlet {

    private TransactionServiceInterface transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        BufferedReader br = request.getReader();
        String params = br.readLine();

        JSONObject jsonObject = JSON.parseObject(params);
        BigDecimal dispositAmount = jsonObject.getBigDecimal("dispositAmount");

        try {
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");
            Integer user_id = user.getId();
            boolean success = transactionService.disposit(user_id, dispositAmount, "存款", null);
            if (success) {
                Result result = new Result(true, "存款成功");
                response.getWriter().write(JSON.toJSONString(result));
            } else {
                Result result = new Result(false, "存款金额无效，存款失败");
                response.getWriter().write(JSON.toJSONString(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}