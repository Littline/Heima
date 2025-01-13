package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.UserDYG;
import com.hmdp.mapper.UserDYGMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;

public interface IUserDYGService  extends IService<UserDYG>{
    Result login1(String email, String password, HttpSession session, HttpServletResponse response);
    UserDYG getUserByUsername(String username) ;
}
