package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.VO.DiscussComment;
import cn.xmu.edu.legaldocument.VO.UserComment;
import cn.xmu.edu.legaldocument.VO.UserDiscuss;
import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.service.CommentService;
import cn.xmu.edu.legaldocument.service.DiscussService;
import cn.xmu.edu.legaldocument.service.UserService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    public static int ANONYMOUS_USERID = 1;

    @Autowired
    DiscussService discussService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

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

    @PostMapping(value = "/addDiscuss")
    public void addDiscuss(@RequestParam("title") String title, @RequestParam("content") String content,
                           HttpServletResponse httpServletResponse) {
        try {
            Discuss discuss = new Discuss();
            discuss.setContent(content);
            discuss.setCreatedDate(new Date());
            discuss.setTitle(title);
            int userId;
            if (hostHolder.getUser() == null) {
                userId=ANONYMOUS_USERID;
                // return WendaUtil.getJSONString(999);
            } else {
                userId=hostHolder.getUser().getId().intValue();
            }
            discuss.setUserId(userId);
            discussService.addDiscuss(discuss);
            //获得新添加的user discuss
            discuss=discussService.getAddDiscuss(userId);
            UserDiscuss userDiscuss=new UserDiscuss(discuss,userService.getUserById((long)userId));
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(userDiscuss));
//            if (discussService.addDiscuss(discuss) > 0) {
//                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
//                        .setActorId(question.getUserId()).setEntityId(question.getId())
//                        .setExt("title", question.getTitle()).setExt("content", question.getContent()));
//            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }
    }

    @GetMapping(value = "/discuss/{discussId}")
    public void discussDetail( HttpServletResponse httpServletResponse,@PathVariable("discussId") int discussId) throws IOException {
        Discuss discuss = discussService.getById(discussId);
        User user=userService.getUserById((long)discuss.getUserId());
        UserDiscuss userDiscuss=new UserDiscuss(discuss,user);

        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(userDiscuss));

    }

    private List<UserDiscuss> getDiscuss(int userId, int offset, int limit) {
        List<Discuss> discussList = discussService.getLatestDiscuss(userId, offset, limit);
        List<UserDiscuss> userDiscussList = new ArrayList<>();
        for (Discuss discuss : discussList) {
            User user=userService.getUserById((long) discuss.getUserId());
            UserDiscuss userDiscuss = new UserDiscuss(discuss,user);
            userDiscussList.add(userDiscuss);
        }
        return userDiscussList;
    }
}