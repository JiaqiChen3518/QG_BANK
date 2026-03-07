package com.qg.bank.service;

import com.qg.bank.dao.UserDao;
import com.qg.bank.dao.UserDaoInterface;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class UserService implements UserServiceInterface {

    private UserDaoInterface userDao = new UserDao();

    public User login(String username, String password) throws SQLException {
        // 调用UserDao的login方法进行登录验证
        User loginUser = userDao.login(username, password);
        return loginUser;
    }

    public Integer selectByName(String username) throws SQLException {
        return userDao.selectByName(username);
    }

    public void register(String username, String password) throws SQLException {
        userDao.register(username, password);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    public User selectById(int id) throws SQLException {
        return userDao.selectById(id);
    }

    public void updateUser(int id, String username, String password) throws SQLException {
        userDao.updateUser(id, username, password);
    }
}