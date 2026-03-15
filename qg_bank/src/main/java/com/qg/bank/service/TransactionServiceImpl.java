package com.qg.bank.service;

import com.qg.bank.dao.TransactionRecordDaoImpl;
import com.qg.bank.dao.TransactionRecordDao;
import com.qg.bank.dao.UserDaoImpl;
import com.qg.bank.dao.UserDao;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.TransactionRecord;
import com.qg.bank.pojo.User;
import com.qg.bank.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private UserDao userDao = new UserDaoImpl();
    private TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();

    /**
     * 存款操作（包括更新用户余额和增加交易记录）
     * @param user_id
     * @param amount
     * @param type
     * @return
     */
    @Override
    public Result disposit(int user_id, BigDecimal amount, String type) {
        // 判断金额是否合法
        if (amount == null) {
            return Result.fail("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("金额必须大于0");
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

            transactionRecordDao.insertRecord(connection, transactionRecord);

            connection.commit();
            // 存款成功
            return Result.success("存款成功");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return Result.fail("存款失败", e.getMessage());
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
     * @return
     */
    @Override
    public Result withdraw(int user_id, BigDecimal amount, String type) {

        // 判断金额是否合法
        if (amount == null) {
            return Result.fail("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("金额必须大于0");
        }

        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            User user = userDao.selectById(connection, user_id);
            BigDecimal balance = user.getBalance();
            // 判断用户余额是否足够
            if (balance.compareTo(amount) < 0) {
                // 余额不足，返回失败
                return Result.fail("余额不足");
            }

            // 金额合法，更新用户余额
            userDao.updateBalance(connection, user_id, amount.negate());

            // 增加交易记录
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setUserId(user_id);
            transactionRecord.setType(type);
            transactionRecord.setAmount(amount);

            transactionRecordDao.insertRecord(connection, transactionRecord);

            connection.commit();
            // 取款成功
            return Result.success("取款成功");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return Result.fail("取款失败", e.getMessage());
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
     * @param targetUsername
     * @return
     */
    @Override
    public Result transfer(int user_id, BigDecimal amount, String targetUsername) {
        // 判断金额是否合法
        if (amount == null) {
            return Result.fail("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("金额必须大于0");
        }

        Connection connection = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);

            // 检查目标用户是否存在
            Integer target_id = userDao.selectByName(targetUsername);
            if (target_id == null) {
                connection.rollback();
                return Result.fail("目标用户不存在");
            }

            // 检查转出用户余额是否足够
            User user = userDao.selectById(connection, user_id);
            BigDecimal balance = user.getBalance();
            if (balance.compareTo(amount) < 0) {
                // 余额不足，回滚并返回失败
                connection.rollback();
                return Result.fail("余额不足");
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
            return Result.success("转账成功");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return Result.fail("转账失败", e.getMessage());
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
     * 查询用户交易记录
     * @param userId
     * @return
     */
    @Override
    public Result getTransactionRecords(int userId) {
        try {
            List<TransactionRecord> records = transactionRecordDao.getTransactionRecords(userId);
            return Result.success("查询成功", records);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }

    /**
     * 查询所有交易记录
     * @return
     */
    @Override
    public Result getAllTransactionRecords() {
        try {
            List<TransactionRecord> records = transactionRecordDao.getAllTransactionRecords();
            return Result.success("查询成功", records);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }
}
