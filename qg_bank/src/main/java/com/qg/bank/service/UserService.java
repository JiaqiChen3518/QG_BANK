package com.qg.bank.service;

import com.qg.bank.pojo.Result;
import com.qg.bank.pojo.User;

import java.util.List;

public interface UserService {

    Result login(String username, String password);

    Result selectByName(String username);

    Result register(String username, String password);

    Result getAllUsers();

    Result selectById(int id);

    Result updateUser(int id, String username, String password, String currentUsername);
}
