package com.qg.bank.filter;

import com.alibaba.fastjson.JSON;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter",
        urlPatterns = {
                "/dispositServlet",
                "/withdrawServlet",
                "/transferServlet",
                "/transactionRecordServlet",
                "/selfInfoServlet",
                "/adminServlet",
                "/updateUserServlet",
                "/financialServlet",
                "/logoutServlet"
        }
)
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置编码
        httpRequest.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json;charset=utf-8");

        // 获取session中的用户信息
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");

        // 检查用户是否登录
        if (user == null) {
            Result result = new Result(false, "请先登录");
            httpResponse.getWriter().write(JSON.toJSONString(result));
            return;
        }

        // 继续执行后续的过滤器或servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁方法
    }
}
