package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.Comment;
import cn.xmu.edu.legaldocument.entity.EntityType;
import cn.xmu.edu.legaldocument.entity.HostHolder;
import cn.xmu.edu.legaldocument.service.CommentService;
import cn.xmu.edu.legaldocument.service.LikeService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class LikeController {
    public static Long ANONYMOUS_USERID = 1L;
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

//    @Autowired
//    EventProducer eventProducer;

    @PostMapping("/like")
    public void like(HttpServletResponse httpServletResponse,@RequestParam("commentId") int commentId) throws IOException {
        Long userId;
        if (hostHolder.getUser() == null) {
            userId=ANONYMOUS_USERID;
        }else{
            userId=hostHolder.getUser().getId();
        }

        Comment comment = commentService.getCommentById(commentId);
//
//        eventProducer.fireEvent(new EventModel(EventType.LIKE)
//                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
//                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
//                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(userId.intValue(), EntityType.ENTITY_COMMENT, commentId);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(likeCount));
    }

    @PostMapping("/dislike")
    public void dislike(HttpServletResponse httpServletResponse,@RequestParam("commentId") int commentId) throws IOException {
        Long userId;
        if (hostHolder.getUser() == null) {
            userId=ANONYMOUS_USERID;
        }else{
            userId=hostHolder.getUser().getId();
        }

        long likeCount = likeService.disLike(userId.intValue(), EntityType.ENTITY_COMMENT, commentId);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(likeCount));
    }
}
