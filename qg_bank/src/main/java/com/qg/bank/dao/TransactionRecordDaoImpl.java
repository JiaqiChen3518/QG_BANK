package com.qg.bank.dao;

import com.qg.bank.pojo.TransactionRecord;
import com.qg.bank.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRecordDaoImpl implements TransactionRecordDao {

    /**
     * 增加一条交易记录
     * @param connection
     * @param transactionRecord
     * @throws SQLException
     */
    public void insertRecord(Connection connection, TransactionRecord transactionRecord) throws SQLException {

        String sql = "insert into transaction_record(user_id, type, amount, target_user_id) values( ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, transactionRecord.getUserId());
        preparedStatement.setString(2, transactionRecord.getType());
        preparedStatement.setBigDecimal(3, transactionRecord.getAmount());
        if (transactionRecord.getTargetUserId() != null) {
            preparedStatement.setInt(4, transactionRecord.getTargetUserId());
        } else {
            preparedStatement.setNull(4, java.sql.Types.INTEGER);
        }
        preparedStatement.executeUpdate();

        preparedStatement.close();
    }

    /**
     * 获取用户的交易记录
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<TransactionRecord> getTransactionRecords(int userId) throws SQLException {
        String sql =" select tr.create_time as createTime, tr.type as type, tr.amount as amount, u2.username as targetUsername " +
                "from transaction_record tr left join user u " +
                "on tr.user_id = u.id " +
                "left join user u2 " +
                "on tr.target_user_id = u2.id " +
                "where u.id = ? " +
                "order by tr.create_time desc";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        List<TransactionRecord> records = new ArrayList<>();
        while(resultSet.next()){
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setCreateTime(resultSet.getDate("createTime"));
            transactionRecord.setType(resultSet.getString("type"));
            transactionRecord.setAmount(resultSet.getBigDecimal("amount"));
            transactionRecord.setTargetUsername(resultSet.getString("targetUsername"));

            records.add(transactionRecord);
        }

        preparedStatement.close();
        connection.close();

        return records;


    }

    /**
     * 获取所有交易记录
     * @return
     * @throws SQLException
     */
    public List<TransactionRecord> getAllTransactionRecords() throws SQLException {
        String sql =" select tr.create_time as createTime, tr.type as type," +
                " tr.amount as amount,u.username as username,u2.username as targetUsername " +
                "from transaction_record tr left join user u " +
                "on tr.user_id = u.id " +
                "left join user u2 " +
                "on tr.target_user_id = u2.id " +
                "order by tr.create_time desc";
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<TransactionRecord> records = new ArrayList<>();
        while(resultSet.next()){
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setCreateTime(resultSet.getDate("createTime"));
            transactionRecord.setType(resultSet.getString("type"));
            transactionRecord.setAmount(resultSet.getBigDecimal("amount"));
            transactionRecord.setUsername(resultSet.getString("username"));
            transactionRecord.setTargetUsername(resultSet.getString("targetUsername"));

            records.add(transactionRecord);
        }
        return records;
    }
}