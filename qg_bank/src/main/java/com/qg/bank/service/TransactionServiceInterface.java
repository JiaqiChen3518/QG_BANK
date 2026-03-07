package com.qg.bank.service;

import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.TransactionRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


public interface TransactionServiceInterface {

    boolean disposit(int user_id, BigDecimal amount, String type, Integer target_id) throws SQLException;

    Result withdraw(int user_id, BigDecimal amount, String type, Integer target_id) throws SQLException;

    boolean transfer(int user_id, BigDecimal amount, Integer target_id) throws SQLException;

    List<TransactionRecord> getTransactionRecords(int userId) throws SQLException;

    List<TransactionRecord> getAllTransactionRecords() throws SQLException;
}
