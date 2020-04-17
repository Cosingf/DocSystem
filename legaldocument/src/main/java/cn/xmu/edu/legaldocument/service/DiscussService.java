package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.mapper.DiscussMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussService
{
    @Autowired
    DiscussMapper discussMapper;


    public Discuss getById(int id) {
        return discussMapper.getById(id);
    }

//    public int addQuestion(Discuss question) {
//        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
//        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
//        // 敏感词过滤
//        question.setTitle(sensitiveService.filter(question.getTitle()));
//        question.setContent(sensitiveService.filter(question.getContent()));
//        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
//    }

    public List<Discuss> getLatestDiscuss(int userId, int offset, int limit) {
        return discussMapper.selectLatestDiscuss(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return discussMapper.updateCommentCount(id, count);
    }
}
