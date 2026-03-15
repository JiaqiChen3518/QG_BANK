package com.qg.bank.dao;

import com.qg.bank.pojo.FinancialProduct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FinantialProductDao {

    void addRecord(Connection connection, FinancialProduct financialProduct) throws SQLException;

    List<FinancialProduct> findRecordsById(int id) throws SQLException;

    List<FinancialProduct> findAllRecords() throws SQLException;

    FinancialProduct RsToFinancialProduct(ResultSet resultSet) throws SQLException;


}
