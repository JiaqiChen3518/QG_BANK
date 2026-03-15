package com.qg.bank.service;

import com.qg.bank.pojo.Result;

import java.math.BigDecimal;

public interface FinancialService {

    Result buyCurrentProduct(int userId, BigDecimal amount);

    Result buyFixedProduct(int userId, BigDecimal amount);

    Result getRecordById(int userId);

    Result getAllRecords();

}
