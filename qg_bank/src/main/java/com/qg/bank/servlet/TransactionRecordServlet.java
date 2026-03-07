package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.TransactionRecord;
import com.qg.bank.pojo.User;
import com.qg.bank.service.TransactionService;
import com.qg.bank.service.TransactionServiceInterface;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/transactionRecordServlet")
public class TransactionRecordServlet extends HttpServlet {

    private TransactionServiceInterface transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        List<TransactionRecord> records;
        if ("admin".equals(role)) {
            try {
                records = transactionService.getAllTransactionRecords();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                records = transactionService.getTransactionRecords(user.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        response.getWriter().write(JSON.toJSONString(records));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}