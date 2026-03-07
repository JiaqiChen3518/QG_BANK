package com.qg.bank.service;

import com.qg.bank.pojo.User;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceInterface {

    User login(String username, String password) throws SQLException;

    Integer selectByName(String username) throws SQLException;

    void register(String username, String password) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    User selectById(int id) throws SQLException;

    void updateUser(int id, String username, String password) throws SQLException;
}
