package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.entity.UserDYG;
import com.hmdp.mapper.UserDYGMapper;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserDYGService;
import com.hmdp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;
import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;

@Slf4j
@Service
public class UserDYGServiceImpl extends ServiceImpl<UserDYGMapper, UserDYG> implements IUserDYGService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login1(String email, String password, HttpSession session, HttpServletResponse response){
        String account=email;
        log.info("account is {}",account);
        log.info("password is {}",password);
        UserDYG user = query().eq("account", account).one();
        if (user == null){
            return Result.fail("用户名不存在");
        }
        StringBuilder newPassword = new StringBuilder(user.getPassword());
        newPassword.setCharAt(2, 'a');
        String hashCode = newPassword.toString();
        boolean passwordMatch = BCrypt.checkpw(password, hashCode);
        if (!passwordMatch){
            return Result.fail("用户名与密码不匹配");
        }
        String token = UUID.randomUUID().toString(true);
        Integer role=user.getRole();
        if(role>=3){
            return Result.fail("用户权限低");
        }
        stringRedisTemplate.opsForHash().putAll("token", new HashMap<String,String>(){{
            put(token,account);
        }});
        stringRedisTemplate.expire("token", LOGIN_USER_TTL, TimeUnit.MINUTES);

        Cookie sessionCookie = new Cookie("account", token);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", token);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addCookie(sessionCookie);
        //如果用户名密码正常，则存入redis，格式为account->token:level
        log.info("level is {}",user.getRole());
        return Result.ok("登陆成功");
    }

}
