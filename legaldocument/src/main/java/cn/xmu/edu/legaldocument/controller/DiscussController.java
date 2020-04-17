package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.VO.ViewObject;
import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.mapper.DiscussMapper;
import cn.xmu.edu.legaldocument.service.DiscussService;
import cn.xmu.edu.legaldocument.service.UserService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
        List<ViewObject> vos=getDiscuss(0,0,10);
        httpServletResponse.getWriter().write(JSON.toJSONString(vos));
    }

    @PostMapping("/discuss/home/{userId}")
    public void getDiscussHomeByUserId(HttpServletResponse httpServletResponse,@PathVariable("userId") int userId) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<ViewObject> vos=getDiscuss(userId,0,10);
        httpServletResponse.getWriter().write(JSON.toJSONString(vos));
    }

    private List<ViewObject> getDiscuss(int userId, int offset, int limit) {
        List<Discuss> discussList = discussService.getLatestDiscuss(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Discuss discuss : discussList) {
            ViewObject vo = new ViewObject();
            vo.set("discuss", discuss);
//            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, discuss.getId()));
            vo.set("user", userService.getUserById((long) discuss.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}