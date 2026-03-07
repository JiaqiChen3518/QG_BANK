package com.qg.bank.dao;

import com.qg.bank.pojo.TransactionRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionRecordDaoInterface {

    void insertRecord(Connection connection, TransactionRecord transactionRecord) throws SQLException;

    List<TransactionRecord> getTransactionRecords(int userId) throws SQLException;

    List<TransactionRecord> getAllTransactionRecords() throws SQLException;
}
