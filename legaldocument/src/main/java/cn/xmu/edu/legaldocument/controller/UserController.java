package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.User;
import cn.xmu.edu.legaldocument.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
//控制类，控制页面跳转，数据传输
public class UserController
{
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;


    //注册用户，使用POST，传输数据
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String registerPost(Model model,

                               @ModelAttribute(value = "user") User user, HttpServletResponse response)
    {
        System.out.println(user);
        //使用userService处理业务
        String result = userService.register(user);
        //将结果放入model中，在模板中可以取到model中的值
        model.addAttribute("result", result);
        return response.encodeRedirectURL("/index");
    }



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public User loginPost(@RequestBody Map<String, Object> map) throws AuthenticationException {
        User user=new User();
        user=userService.getUser(map.get("username").toString());
        user.setAccount(map.get("username").toString());
        user.setPassword(map.get("password").toString());
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
    @ResponseBody
    public User getInfo(@PathVariable("username")String username){
        User user=new User();
        user.setAccount(username);
        user=userService.getUser(user.getAccount());
        System.out.println(user.getEmail());
        return user;
    }
}
