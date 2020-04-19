package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.entity.LoginTicket;
import cn.xmu.edu.legaldocument.entity.User;
import cn.xmu.edu.legaldocument.mapper.LoginTicketMapper;
import cn.xmu.edu.legaldocument.mapper.UserMapper;
import cn.xmu.edu.legaldocument.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserMapper userDao;
    @Autowired
    LoginTicketMapper loginTicketMapper;

    public int  register(String name,String pass,String email) {
        User user = new User();
        user.setAccount(name);
        user.setPassword(pass);
        user.setEmail(email);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(UserUtil.MD5(pass+user.getSalt()));
        //判断用户是否存在
        if (userDao.selectByAccount(user.getAccount()) == null) {
            userDao.insert(user);
            return 200;
        }
        else {
            return 2004;
        }
    }

    //用户登陆逻辑
    public String login(String name,String pass,User user) {
        String formatPass=UserUtil.MD5(pass+user.getSalt());
        assert formatPass != null;
        if (!formatPass.equals(user.getPassword())) {
            return null;
        }
        else {
            return addLoginTicket(user.getId().intValue());
        }
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketMapper.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUserByAccount(String account){
        return userDao.selectByAccount(account);
    }

    public User getUserById(Long userId){
        return userDao.selectByPrimaryKey(userId);
    }

    public void updateUser(User user){
        userDao.updateByPrimaryKey(user);
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }
}
