package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.User;
import cn.xmu.edu.legaldocument.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/getInfo/{username}",method = RequestMethod.GET)
    public User getInfo(@PathVariable("username")String username){
        User user=new User();
        user.setAccount(username);
        user=userService.getUser(user.getAccount());
        System.out.println(user.getEmail());
        return user;
    }
}
