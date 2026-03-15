package com.qg.bank.service;

import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.TransactionRecord;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {

    Result disposit(int user_id, BigDecimal amount, String type);

    Result withdraw(int user_id, BigDecimal amount, String type);

    Result transfer(int user_id, BigDecimal amount, String targetUsername);

    Result getTransactionRecords(int userId);

    Result getAllTransactionRecords();
}
