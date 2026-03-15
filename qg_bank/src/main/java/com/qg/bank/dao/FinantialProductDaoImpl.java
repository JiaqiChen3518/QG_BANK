package com.qg.bank.dao;

import com.qg.bank.pojo.FinancialProduct;
import com.qg.bank.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinantialProductDaoImpl implements FinantialProductDao {

    /**
     * 添加一条理财记录
     * @param financialProduct
     * @throws SQLException
     */
    @Override
    public void addRecord(Connection connection, FinancialProduct financialProduct) throws SQLException {

        String sql = "insert into financial_products (user_id, product_name, amount, rate, term) " +
                "values (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, financialProduct.getUserId());
        preparedStatement.setString(2, financialProduct.getProductName());
        preparedStatement.setBigDecimal(3, financialProduct.getAmount());
        preparedStatement.setBigDecimal(4, financialProduct.getRate());
        preparedStatement.setInt(5, financialProduct.getTerm());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    /**
      * 根据用户ID查询所有理财记录
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public List<FinancialProduct> findRecordsById(int id) throws SQLException {

        String sql = "select * from financial_products where user_id=? order by buy_time desc";

        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<FinancialProduct> list = new ArrayList<>();
        while (resultSet.next()) {
            FinancialProduct financialProduct = RsToFinancialProduct(resultSet);
            list.add(financialProduct);
        }
        preparedStatement.close();
        connection.close();
        return list;
    }

    /**
     * 查询所有理财记录
     * @return
     * @throws SQLException
     */
    @Override
    public List<FinancialProduct> findAllRecords() throws SQLException {

        String sql = "select * from financial_products order by buy_time desc";

        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<FinancialProduct> list = new ArrayList<>();
        while (resultSet.next()) {
            FinancialProduct financialProduct = RsToFinancialProduct(resultSet);
            list.add(financialProduct);
        }
        preparedStatement.close();
        connection.close();
        return list;
    }

    /**
    * 将ResultSet转换为FinancialProduct对象
     * @param resultSet
     * @return
     * @throws SQLException
     */
    @Override
    public FinancialProduct RsToFinancialProduct(ResultSet resultSet) throws SQLException {

        int userId = resultSet.getInt("user_id");
        int id = resultSet.getInt("id");
        String productName = resultSet.getString("product_name");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        BigDecimal rate = resultSet.getBigDecimal("rate");
        int term = resultSet.getInt("term");
        Date buyTime = resultSet.getDate("buy_time");
        String status = resultSet.getString("status");

        FinancialProduct financialProduct = new FinancialProduct(id, userId, productName, amount, rate, term, buyTime, status);
        return financialProduct;
    }
}
