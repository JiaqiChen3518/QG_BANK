package com.qg.bank.service;

import com.qg.bank.dao.UserDaoImpl;
import com.qg.bank.dao.UserDao;
import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public Result login(String username, String password) {
        try {
            User loginUser = userDao.login(username, password);
            if (loginUser != null) {
                return Result.success("登录成功", loginUser);
            } else {
                return Result.fail("用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("登录失败", e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户是否存在
     * @param username
     * @return
     */
    @Override
    public Result selectByName(String username) {
        try {
            Integer count = userDao.selectByName(username);
            return Result.success("查询成功", count);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @Override
    public Result register(String username, String password) {
        // 后端输入验证
        if (username == null || username.trim().isEmpty()) {
            return Result.fail("用户名不能为空");
        }
        if (username.length() > 50) {
            return Result.fail("用户名长度不能超过50个字符");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return Result.fail("用户名只能包含字母、数字和下划线");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.fail("密码不能为空");
        }
        if (password.length() > 100) {
            return Result.fail("密码长度不能超过100个字符");
        }
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$")) {
            return Result.fail("密码至少6位，包含字母和数字");
        }

        // 检查用户名是否存在
        try {
            Integer count = userDao.selectByName(username);
            if (count != null && count > 0) {
                return Result.fail("用户名已存在");
            }

            // 如果用户名不存在，注册用户
            userDao.register(username, password);
            return Result.success("注册成功");
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("注册失败", e.getMessage());
        }
    }

    /**
     * 查询所有用户
     * @return
     */
    @Override
    public Result getAllUsers() {
        try {
            List<User> users = userDao.getAllUsers();
            return Result.success("查询成功", users);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }

    /**
     * 根据用户ID查询用户信息
     * @param id
     * @return
     */
    @Override
    public Result selectById(int id) {
        try {
            User user = userDao.selectById(id);
            return Result.success("查询成功", user);
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("查询失败", e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param id
     * @param username
     * @param password
     * @param currentUsername
     * @return
     */
    @Override
    public Result updateUser(int id, String username, String password, String currentUsername) {
        // 验证用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            return Result.fail("用户名不能为空");
        }

        // 如果用户名改变了，检查新用户名是否已存在
        if (!username.equals(currentUsername)) {
            try {
                Integer existingUserId = userDao.selectByName(username);
                if (existingUserId != null && !existingUserId.equals(id)) {
                    return Result.fail("用户名已存在");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return Result.fail("查询失败", e.getMessage());
            }
        }

        // 更新用户信息
        try {
            userDao.updateUser(id, username, password);
            return Result.success("更新成功");
        } catch (SQLException e) {
            e.printStackTrace();
            return Result.fail("更新失败", e.getMessage());
        }
    }
}