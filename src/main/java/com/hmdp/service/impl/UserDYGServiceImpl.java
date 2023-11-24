package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.entity.UserDYG;
import com.hmdp.mapper.UserDYGMapper;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserDYGService;
import com.hmdp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
@Slf4j
@Service
public class UserDYGServiceImpl extends ServiceImpl<UserDYGMapper, UserDYG> implements IUserDYGService {

    @Override
    public Result login1(String email, String password, HttpSession session){
        String account=email;
        UserDYG user = query().eq("account", account).one();
        if (user == null){
            return Result.fail("用户名不存在");
        }
        StringBuilder newPassword = new StringBuilder(user.getPassword());
        newPassword.setCharAt(2, 'a');
        String hashCode = newPassword.toString();
        boolean passwordMatch = BCrypt.checkpw(password, hashCode);
        if (!passwordMatch){
            return Result.fail("用户名或者密码错误");
        }
        //如果用户名密码正常，则存入redis，格式为account:role
        log.info("role is {}",user.getRole());
        return Result.fail("手机号格式错误");
    }

}
