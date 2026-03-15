package com.qg.bank.dao;

import com.qg.bank.pojo.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    User login(String username, String password) throws SQLException;

    Integer selectByName(String username) throws SQLException;

    void register(String username, String password) throws SQLException;

    void updateBalance(Connection connection, int id, BigDecimal amount) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    User selectById(int id) throws SQLException;

    User selectById(Connection connection, int id) throws SQLException;

    void updateUser(int id, String username, String password) throws SQLException;
}
