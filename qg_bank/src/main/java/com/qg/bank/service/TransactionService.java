package com.qg.bank.service;

import com.qg.bank.dao.TransactionRecordDao;
import com.qg.bank.dao.TransactionRecordDaoInterface;
import com.qg.bank.dao.UserDao;
import com.qg.bank.dao.UserDaoInterface;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.TransactionRecord;
import com.qg.bank.pojo.User;
import com.qg.bank.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TransactionService implements TransactionServiceInterface {

    private UserDaoInterface userDao = new UserDao();
    private TransactionRecordDaoInterface transactionRecordDao = new TransactionRecordDao();

    /**
     * 存款操作（包括更新用户余额和增加交易记录）
     * @param user_id
     * @param amount
     * @param type
     * @param target_id
     * @return
     * @throws SQLException
     */
    public boolean disposit(int user_id, BigDecimal amount, String type, Integer target_id) throws SQLException {
        // 判断金额是否合法
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            // 金额不合法，返回false
            return false;
        }

        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            // 金额合法，更新用户余额
            userDao.updateBalance(connection, user_id, amount);

            // 增加交易记录
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setUserId(user_id);
            transactionRecord.setType(type);
            transactionRecord.setAmount(amount);
            transactionRecord.setTargetUserId(target_id);

            transactionRecordDao.insertRecord(connection, transactionRecord);

            connection.commit();
            // 存款成功
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBUtil.closeConnection(connection);
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
     * 取款操作（包括更新用户余额和增加交易记录）
     * @param user_id
     * @param amount
     * @param type
     * @param target_id
     * @return
     * @throws SQLException
     */
    public Result withdraw(int user_id, BigDecimal amount, String type, Integer target_id) throws SQLException {

        // 判断金额是否合法
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            // 金额不合法，返回false
            Result result = new Result(false, "金额不合法");
            return result;
        }

        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            User user = userDao.selectById(connection, user_id);
            BigDecimal balance = user.getBalance();
            // 判断用户余额是否足够
            if (balance.compareTo(amount) < 0) {
                // 余额不足，返回false
                Result result = new Result(false, "余额不足");
                return result;
            }

            // 金额合法，更新用户余额
            userDao.updateBalance(connection, user_id, amount.negate());

            // 增加交易记录
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setUserId(user_id);
            transactionRecord.setType(type);
            transactionRecord.setAmount(amount);
            transactionRecord.setTargetUserId(target_id);

            transactionRecordDao.insertRecord(connection, transactionRecord);

            connection.commit();
            // 取款成功
            return new Result(true, "取款成功");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBUtil.closeConnection(connection);
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    /**
     * 转账操作（包括更新用户余额和增加交易记录）
     * @param user_id
     * @param amount
     * @param target_id
     * @return
     * @throws SQLException
     */
    public boolean transfer(int user_id, BigDecimal amount, Integer target_id) throws SQLException {
        // 判断金额是否合法
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            // 金额不合法，返回false
            return false;
        }

        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            // 检查转出用户余额是否足够
            User user = userDao.selectById(connection, user_id);
            BigDecimal balance = user.getBalance();
            if (balance.compareTo(amount) < 0) {
                // 余额不足，回滚并返回false
                connection.rollback();
                return false;
            }

            // 金额合法，更新用户余额
            userDao.updateBalance(connection, user_id, amount.negate());
            userDao.updateBalance(connection, target_id, amount);

            // 增加交易记录
            TransactionRecord transactionRecord1 = new TransactionRecord();
            transactionRecord1.setUserId(user_id);
            transactionRecord1.setType("转出");
            transactionRecord1.setAmount(amount);
            transactionRecord1.setTargetUserId(target_id);

            TransactionRecord transactionRecord2 = new TransactionRecord();
            transactionRecord2.setUserId(target_id);
            transactionRecord2.setType("转入");
            transactionRecord2.setAmount(amount);
            transactionRecord2.setTargetUserId(user_id);

            transactionRecordDao.insertRecord(connection, transactionRecord1);
            transactionRecordDao.insertRecord(connection, transactionRecord2);

            connection.commit();
            // 转账成功
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBUtil.closeConnection(connection);
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public List<TransactionRecord> getTransactionRecords(int userId) throws SQLException {
        return transactionRecordDao.getTransactionRecords(userId);
    }

    public List<TransactionRecord> getAllTransactionRecords() throws SQLException {
        return transactionRecordDao.getAllTransactionRecords();
    }
}
