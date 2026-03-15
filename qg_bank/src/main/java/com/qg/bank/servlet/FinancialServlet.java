package com.qg.bank.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.service.FinancialServiceImpl;
import com.qg.bank.service.FinancialService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/financialServlet")
public class FinancialServlet extends HttpServlet {

    private FinancialService finantialService = new FinancialServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if("admin".equals(user.getRole())){
            Result allRecords = finantialService.getAllRecords();

            response.getWriter().write(JSON.toJSONString(allRecords));
        }
        else{
            Result result = finantialService.getRecordById(user.getId());

            response.getWriter().write(JSON.toJSONString(result));
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 读取请求体中的json数据
        BufferedReader br = new BufferedReader(request.getReader());
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        try {
            // 解析数据
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            String productType = jsonObject.getString("productType");
            String amountStr = jsonObject.getString("amount");
            BigDecimal amount = new BigDecimal(amountStr);

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            Result result = null;
            if ("current".equals(productType)) {
                result = finantialService.buyCurrentProduct(user.getId(), amount);
                if (result.isSuccess()) {
                    // 更新session中用户的余额
                    user.setBalance(user.getBalance().subtract(amount));
                }
            } else if ("fixed".equals(productType)) {
                result = finantialService.buyFixedProduct(user.getId(), amount);
                if (result.isSuccess()) {
                    // 更新session中用户的余额
                    user.setBalance(user.getBalance().subtract(amount));
                }
            } else {
                result = Result.fail("产品类型错误");
            }
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            Result result = new Result(false, "输入金额格式错误");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }
}