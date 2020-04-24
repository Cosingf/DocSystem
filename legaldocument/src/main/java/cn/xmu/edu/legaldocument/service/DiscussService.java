package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.mapper.DiscussMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussService
{
    @Autowired
    DiscussMapper discussMapper;
    @Autowired
    SensitiveService sensitiveService;


    public Discuss getById(int id) {
        return discussMapper.getById(id);
    }

    public int addDiscuss(Discuss discuss) {
        //html过滤
        discuss.setTitle(HtmlUtils.htmlEscape(discuss.getTitle()));
        discuss.setContent(HtmlUtils.htmlEscape(discuss.getContent()));
        // 敏感词过滤
        discuss.setTitle(sensitiveService.filter(discuss.getTitle()));
        discuss.setContent(sensitiveService.filter(discuss.getContent()));
        return discussMapper.addDiscuss(discuss) > 0 ? discuss.getId() : 0;
    }

    public List<Discuss> getLatestDiscuss(int userId, int offset, int limit) {
        return discussMapper.selectLatestDiscuss(userId, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return discussMapper.updateCommentCount(id, count);
    }

    public Discuss getAddDiscuss(int userId) {
        return discussMapper.getAddDiscuss(userId, 0, 1);
    }
}
