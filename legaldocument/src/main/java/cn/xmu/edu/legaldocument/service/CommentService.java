package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.entity.Comment;
import cn.xmu.edu.legaldocument.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentMapper.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) {
        //过滤Html和敏感词
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentMapper.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentMapper.getCommentCount(entityId, entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentMapper.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId) {
        return commentMapper.updateStatus(commentId, 1) > 0;
    }

    public Comment getCommentById(int id) {
        return commentMapper.getCommentById(id);
    }

    public Comment getLatestComment(int discussId) {
        return commentMapper.getLatestComment(discussId,0,1);
    }
}
