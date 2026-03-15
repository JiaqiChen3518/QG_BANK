package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionServiceImpl;
import com.qg.bank.service.TransactionService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/transactionRecordServlet")
public class TransactionRecordServlet extends HttpServlet {

    private TransactionService transactionService = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String role = user.getRole();
        Result result;
        if ("admin".equals(role)) {
            result = transactionService.getAllTransactionRecords();
        } else {
            result = transactionService.getTransactionRecords(user.getId());
        }
        response.getWriter().write(JSON.toJSONString(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}