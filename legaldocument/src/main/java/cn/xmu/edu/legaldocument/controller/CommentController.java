package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.VO.UserComment;
import cn.xmu.edu.legaldocument.entity.Comment;
import cn.xmu.edu.legaldocument.entity.EntityType;
import cn.xmu.edu.legaldocument.entity.HostHolder;
import cn.xmu.edu.legaldocument.service.CommentService;
import cn.xmu.edu.legaldocument.service.DiscussService;
import cn.xmu.edu.legaldocument.service.LikeService;
import cn.xmu.edu.legaldocument.service.UserService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    public static int ANONYMOUS_USERID = 1;
    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    DiscussService discussService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

//    @Autowired
//    EventProducer eventProducer;


    @PostMapping("/addComment")
    public void addComment(@RequestParam("discussId") int discussId,@RequestParam("content") String content,
                           HttpServletResponse httpServletResponse) throws IOException {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId().intValue());
            } else {
                comment.setUserId(ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_DISCUSS);
            comment.setEntityId(discussId);
            commentService.addComment(comment);
            //返回新建的user comment
            comment=commentService.getLatestComment(discussId);
            //总点赞数
            long likeCount=likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId());
            //当前用户对comment的赞踩状态 1:like -1:dislike 0:none
            long likeStatus=likeService.getLikeStatus(comment.getUserId(), EntityType.ENTITY_COMMENT, comment.getId());
            UserComment userComment=new UserComment(comment,userService.getUserById((long)comment.getUserId()),likeCount,likeStatus);

            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            discussService.updateCommentCount(comment.getEntityId(), count);
//
//            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
//                    .setEntityId(questionId));
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(userComment));
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }
    }

    @GetMapping("/comment/{discussId}")
    public void getCommentList(@PathVariable("discussId") int discussId, HttpServletResponse httpServletResponse) throws IOException {
        List<Comment> commentList = commentService.getCommentsByEntity(discussId, EntityType.ENTITY_DISCUSS);
        List<UserComment> comments = new ArrayList<>();
        int userId;
        if (hostHolder.getUser() == null) {
            userId=ANONYMOUS_USERID;
        }else{
            userId=hostHolder.getUser().getId().intValue();
        }
        for (Comment comment : commentList) {
            //总点赞数
            long likeCount=likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId());
            //当前用户对comment的赞踩状态 1:like -1:dislike 0:none
            long likeStatus=likeService.getLikeStatus(userId, EntityType.ENTITY_COMMENT, comment.getId());
            UserComment userComment=new UserComment(comment,userService.getUserById((long)comment.getUserId()),likeCount,likeStatus);
            comments.add(userComment);
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(comments));
    }
}
