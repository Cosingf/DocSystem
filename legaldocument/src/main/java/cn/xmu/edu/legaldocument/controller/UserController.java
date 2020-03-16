package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.User;
import cn.xmu.edu.legaldocument.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
//控制类，控制页面跳转，数据传输
public class UserController
{
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;


    //注册用户，使用POST，传输数据
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(HttpServletResponse httpServletResponse, @RequestParam("name")String name,@RequestParam("pass")String pass,@RequestParam("email")String email, HttpServletResponse response)
    {
        User user = new User();
        user.setAccount(name);
        user.setPassword(pass);
        user.setEmail(email);
        //使用userService处理业务
        int result = userService.register(user);
        httpServletResponse.setStatus(result);
        return  "";
    }



    @PostMapping("/login")
    public User loginPost(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("name")String name,@RequestParam("pass")String pass) throws Exception {
        User user=new User();
        user=userService.getUser(name);
        user.setAccount(name);
        user.setPassword(pass);
        System.out.println(user.toString());
        String result = userService.login(user);
        if (result.equals("登陆成功")) {

            return user;
        }
        else {
            throw new AuthenticationException("登录失败！");
        }

    }

    @PostMapping("/reset")
    public void resetPassword(HttpServletResponse httpServletResponse, @RequestParam("pass")String password,@RequestParam("userId")Long userId){
        User user=userService.getUserById(userId);
        user.setPassword(password);
        userService.updateUser(user);
    }

    @RequestMapping(value = "/getInfo/{userId}",method = RequestMethod.GET)
    public void getInfo(@PathVariable("userId")Long userId,HttpServletResponse httpServletResponse) throws IOException {
        User user=userService.getUserById(userId);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        if (user!=null) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(user));
        }
        else {
            httpServletResponse.setStatus(404);
        }
    }
}
