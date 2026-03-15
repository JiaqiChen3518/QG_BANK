package com.qg.bank.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财产品类，用于表示用户购买的理财产品记录
 */
public class FinancialProduct {
    private int id;                    // 记录ID
    private int userId;                // 用户ID
    private String productName;        // 产品名称（活期理财/定期理财）
    private BigDecimal amount;         // 投资金额
    private BigDecimal rate;           // 年化收益率
    private int term;                  // 期限（月），活期理财为0
    private Date buyTime;              // 购买时间
    private String status;             // 状态（持有中/已赎回）

    // 构造方法
    public FinancialProduct() {}

    public FinancialProduct(int userId, String productName, BigDecimal amount,
                            BigDecimal rate, int term) {
        this.userId = userId;
        this.productName = productName;
        this.amount = amount;
        this.rate = rate;
        this.term = term;
        this.buyTime = new Date();
        this.status = "持有中";
    }

    public FinancialProduct(int id, int userId, String productName, BigDecimal amount, BigDecimal rate, int term, Date buyTime, String status) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.amount = amount;
        this.rate = rate;
        this.term = term;
        this.buyTime = buyTime;
        this.status = status;
    }

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
