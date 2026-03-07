package com.qg.bank.dao;

import com.qg.bank.pojo.User;
import com.qg.bank.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements UserDaoInterface {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public User login(String username, String password) throws SQLException {
        String sql = "select * from user where username=? and password=?";

        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst = connection.prepareStatement(sql);
        ppst.setString(1, username);
        ppst.setString(2, password);
        ResultSet resultSet = ppst.executeQuery();

        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String _username = resultSet.getString("username");
            String _password = resultSet.getString("password");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            String role = resultSet.getString("role");

            User user = new User(id, _username, _password, balance, role);

            return user;

        }

        return null;
    }

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     * @throws SQLException
     */
    public Integer selectByName(String username) throws SQLException {
        String sql = "select * from user where username=?";

        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst = connection.prepareStatement(sql);

        ppst.setString(1, username);
        ResultSet rs = ppst.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id");
            return id;
        }
        return null;
    }

    /**
     * 注册
     *
     * @param username
     * @param password
     * @throws SQLException
     */
    public void register(String username, String password) throws SQLException {
        String sql = "insert into user (username,password,balance,role) values (?,?,?,?)";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst = connection.prepareStatement(sql);
        ppst.setString(1, username);
        ppst.setString(2, password);
        ppst.setBigDecimal(3, new BigDecimal(0));
        ppst.setString(4, "user");
        ppst.executeUpdate();

    }


    /**
     * 更新余额
     *
     * @param connection
     * @param id
     * @param amount
     * @throws SQLException
     */
    public void updateBalance(Connection connection, int id, BigDecimal amount) throws SQLException {
        String sql = "update user set balance=balance+? where id=?";
        PreparedStatement ppst = connection.prepareStatement(sql);
        ppst.setBigDecimal(1, amount);
        ppst.setInt(2, id);
        ppst.executeUpdate();
    }

    /**
     * 获取所有用户（管理员查看）
     * @return
     * @throws SQLException
     */
    public List<User> getAllUsers() throws SQLException {

        String sql = "select * from user";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst = connection.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            //String password = resultSet.getString("password");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            String role = resultSet.getString("role");
            User user = new User(id, username, null, balance, role);
            users.add(user);
        }
        return users;
    }

    /**
     * 根据ID查询用户
     * @param id
     * @return
     * @throws SQLException
     */
    public User selectById(int id) throws SQLException {

        String sql = "select * from user where id=?";
        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst = connection.prepareStatement(sql);
        ppst.setInt(1, id);
        ResultSet resultSet = ppst.executeQuery();
        if (resultSet.next()) {
            String username = resultSet.getString("username");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            String role = resultSet.getString("role");
            User user = new User(id, username, null, balance, role);
            return user;
        }
        return null;
    }

    /**
     * 根据ID查询用户（带连接）
     * @param connection
     * @param id
     * @return
     * @throws SQLException
     */
    public User selectById(Connection connection, int id) throws SQLException {

        String sql = "select * from user where id=?";
        PreparedStatement ppst = connection.prepareStatement(sql);
        ppst.setInt(1, id);
        ResultSet resultSet = ppst.executeQuery();
        if (resultSet.next()) {
            String username = resultSet.getString("username");
            BigDecimal balance = resultSet.getBigDecimal("balance");
            String role = resultSet.getString("role");
            User user = new User(id, username, null, balance, role);
            return user;
        }
        return null;
    }

    /**
     * 更新用户信息
     * @param id
     * @param username
     * @param password
     * @throws SQLException
     */
    public void updateUser(int id, String username, String password) throws SQLException {
        String sql;
        Connection connection = DBUtil.getConnection();
        PreparedStatement ppst;

        if (password != null && !password.trim().isEmpty()) {
            // 更新用户名和密码
            sql = "update user set username=?, password=? where id=?";
            ppst = connection.prepareStatement(sql);
            ppst.setString(1, username);
            ppst.setString(2, password);
            ppst.setInt(3, id);
        } else {
            // 只更新用户名
            sql = "update user set username=? where id=?";
            ppst = connection.prepareStatement(sql);
            ppst.setString(1, username);
            ppst.setInt(2, id);
        }
        ppst.executeUpdate();
    }
}