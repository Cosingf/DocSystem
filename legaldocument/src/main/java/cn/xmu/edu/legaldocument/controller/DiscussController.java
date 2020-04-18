package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.VO.UserDiscuss;
import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.entity.User;
import cn.xmu.edu.legaldocument.service.DiscussService;
import cn.xmu.edu.legaldocument.service.UserService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class DiscussController {
    private static final Logger logger= LoggerFactory.getLogger(DiscussController.class);

    @Autowired
    DiscussService discussService;
    @Autowired
    UserService userService;

    @PostMapping("/discuss/home")
    public void getDiscussHome(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<UserDiscuss> discussList=getDiscuss(1,0,10);
        httpServletResponse.getWriter().write(JSON.toJSONString(discussList));
    }

    @PostMapping("/discuss/home/{userId}")
    public void getDiscussHomeByUserId(HttpServletResponse httpServletResponse,@PathVariable("userId") int userId) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<UserDiscuss> discussList=getDiscuss(userId,0,10);
        httpServletResponse.getWriter().write(JSON.toJSONString(discussList));
    }

    private List<UserDiscuss> getDiscuss(int userId, int offset, int limit) {
        List<Discuss> discussList = discussService.getLatestDiscuss(userId, offset, limit);
        List<UserDiscuss> userDiscussList = new ArrayList<>();
        for (Discuss discuss : discussList) {
            UserDiscuss userDiscuss = new UserDiscuss();
            userDiscuss.setId(discuss.getId());
            userDiscuss.setTitle(discuss.getTitle());
            userDiscuss.setCommentCount(discuss.getCommentCount());
            userDiscuss.setContent(discuss.getContent());
            //date format
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date=discuss.getCreatedDate();
            String formatDate=df.format(date);
            userDiscuss.setCreatedDate(formatDate);

            User user=userService.getUserById((long) discuss.getUserId());
            userDiscuss.setAccount(user.getAccount());
            userDiscuss.setEmail(user.getEmail());
            userDiscussList.add(userDiscuss);
        }
        return userDiscussList;
    }
}