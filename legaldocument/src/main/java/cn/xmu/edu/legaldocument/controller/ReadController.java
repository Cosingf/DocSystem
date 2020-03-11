package cn.xmu.edu.legaldocument.controller;

import cn.xmu.edu.legaldocument.VO.KeywordWikiVO;
import cn.xmu.edu.legaldocument.VO.PageSectionVO;
import cn.xmu.edu.legaldocument.entity.QA;
import cn.xmu.edu.legaldocument.entity.QASection;
import cn.xmu.edu.legaldocument.entity.WikiAnnotation;
import cn.xmu.edu.legaldocument.service.ReadService;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ReadController {

    @Autowired
    ReadService readService;

    /**
     * @Description: getHighLightResult
     * @param content
     * @return qas
     * @throws Exception
     * @aurher pengshuyuan
     * @date 2019/7/5 9:26
     */

    @PostMapping("/read/highlight")
    public void getHighLightResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("content") String content, @RequestParam("bookId") String bookid,@RequestParam("pageNum") String pageNum) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<QA>  qas =readService.getHighLightResult(content,Long.valueOf(bookid),Integer.valueOf(pageNum));
        if (qas!=null||qas.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(qas));
        }
        else {
            httpServletResponse.setStatus(404);
        }
    }

    /**
     * 新增Wiki Annotation
     */
    @GetMapping("/read/wiki/{bookid}")
    public void getWikiByMatchingKeywords(HttpServletResponse httpServletResponse, @PathVariable("bookid") Long bookId) throws IOException {
        //读取wiki匹配结果
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<KeywordWikiVO> matchResList=readService.getMatchResult(bookId);
        httpServletResponse.getWriter().write(JSON.toJSONString(matchResList));
    }


    @PostMapping("/read/{bookid}")
    public void getAllPageResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("bookid") Long bookId ) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<PageSectionVO> relusts =readService.getBookEnrich(bookId);
        if (relusts==null)
            httpServletResponse.setStatus(404);
        else {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(relusts));
        }
    }


}
