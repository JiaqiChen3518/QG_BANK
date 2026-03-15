package com.qg.bank.service;

import com.qg.bank.dao.FinantialProductDaoImpl;
import com.qg.bank.dao.UserDaoImpl;
import com.qg.bank.pojo.FinancialProduct;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;
import com.qg.bank.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FinancialServiceImpl implements FinancialService {

    private FinantialProductDaoImpl finantialProductDao = new FinantialProductDaoImpl();
    private UserDaoImpl userDao = new UserDaoImpl();

    // 定义理财产品信息
    public static final String CURRENT_PRODUCT = "活期理财";      // 活期理财产品名称
    public static final String FIXED_PRODUCT = "定期理财";        // 定期理财产品名称
    public static final BigDecimal CURRENT_RATE = new BigDecimal("0.015");  // 活期年化收益率1.5%
    public static final BigDecimal FIXED_RATE = new BigDecimal("0.025");    // 定期年化收益率2.5%
    public static final int CURRENT_TERM = 0;   // 活期期限0个月
    public static final int FIXED_TERM = 3;     // 定期期限3个月
    public static final BigDecimal CURRENT_MIN_AMOUNT = new BigDecimal("1");     // 活期起投金额1元
    public static final BigDecimal FIXED_MIN_AMOUNT = new BigDecimal("1000");    // 定期起投金额1000元


    @Override
    public Result buyCurrentProduct(int userId, BigDecimal amount) {

        if (amount == null) {
            return Result.fail("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("金额必须大于0");
        }
        if (amount.compareTo(CURRENT_MIN_AMOUNT) < 0) {
            return Result.fail("活期起投金额不能小于" + CURRENT_MIN_AMOUNT + "元");
        }
        Connection connection = null;
        try {
            User user = userDao.selectById(userId);
            if (user.getBalance().compareTo(amount) < 0) {
                return Result.fail("余额不足");
            }
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            // 更新用户余额
            userDao.updateBalance(connection, userId, amount.negate());

            // 添加一条理财记录
            FinancialProduct financialProduct = new FinancialProduct(
                    userId, CURRENT_PRODUCT, amount, CURRENT_RATE, CURRENT_TERM
            );
            finantialProductDao.addRecord(connection, financialProduct);

            connection.commit();
            return Result.success("购买成功", financialProduct);

        } catch (SQLException e) {
            if(connection!=null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
            return Result.fail("活期理财购买失败", e.getMessage());
        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBUtil.closeConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result buyFixedProduct(int userId, BigDecimal amount) {
        if (amount == null) {
            return Result.fail("金额不能为空");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.fail("金额必须大于0");
        }
        if (amount.compareTo(FIXED_MIN_AMOUNT) < 0) {
            return Result.fail("定期起投金额不能小于" + FIXED_MIN_AMOUNT + "元");
        }
        Connection connection = null;
        try {
            User user = userDao.selectById(userId);
            if (user.getBalance().compareTo(amount) < 0) {
                return Result.fail("余额不足");
            }
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            // 更新用户余额
            userDao.updateBalance(connection, userId, amount.negate());

            // 添加一条理财记录
            FinancialProduct financialProduct = new FinancialProduct(
                    userId, FIXED_PRODUCT, amount, FIXED_RATE, FIXED_TERM
            );
            finantialProductDao.addRecord(connection, financialProduct);

            connection.commit();
            return Result.success("定期理财购买成功", financialProduct);

        } catch (SQLException e) {
            if(connection!=null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            e.printStackTrace();
            return Result.fail("购买失败", e.getMessage());
        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBUtil.closeConnection(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result getRecordById(int userId) {

        try {
            List<FinancialProduct> records = finantialProductDao.findRecordsById(userId);
            return Result.success("查询成功", records);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }

    @Override
    public Result getAllRecords() {
        try {
            List<FinancialProduct> records = finantialProductDao.findAllRecords();
            return Result.success("查询成功", records);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }
}