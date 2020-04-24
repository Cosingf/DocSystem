package cn.xmu.edu.legaldocument.controller;

import cn.xmu.edu.legaldocument.VO.KeywordWikiVO;
import cn.xmu.edu.legaldocument.VO.QASectionVO;
import cn.xmu.edu.legaldocument.entity.QA;
import cn.xmu.edu.legaldocument.service.ReadService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    @PostMapping("/read/chrome/highlight")
    public void getChromeHighLightResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("content") String content) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<QA>  qas =readService.getChromeHighLightResult(content);
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
    @PostMapping("/read/wiki/{bookid}")
    public void getWikiByMatchingKeywords(HttpServletResponse httpServletResponse, @PathVariable("bookid") Long bookId) throws IOException {
        //读取wiki匹配结果
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<KeywordWikiVO> matchResList=readService.getMatchResult(bookId);
        httpServletResponse.getWriter().write(JSON.toJSONString(matchResList));
    }

    @PostMapping("/read/doc/{bookid}")
    public void getDocById(HttpServletResponse httpServletResponse, @PathVariable("bookid") Long bookId) throws IOException {
        //读取doc txt
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String document=readService.getDocById(bookId);
        httpServletResponse.getWriter().write(JSON.toJSONString(document));
    }

    @PostMapping("/read/enhance")
    public void getAllPageResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("bookId") Long bookId,@RequestParam("pageNum") Integer pageNum ) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<QASectionVO> relusts =readService.getBookEnrich(bookId,pageNum);
        if (relusts==null)
            httpServletResponse.setStatus(404);
        else {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(relusts));
        }
    }
    @PostMapping("/read/chrome/enhance")
    public void getChromeAllPageResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("content") String content ) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<QASectionVO> relusts =readService.getChromeEnrich(content);
        if (relusts==null)
            httpServletResponse.setStatus(404);
        else {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(relusts));
        }
    }

    @PostMapping("/read/chrome/wiki")
    public void getChromeWiki(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,@RequestParam("bookId") Long bookId) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        List<KeywordWikiVO> matchResList=readService.getMatchResult(bookId);
        httpServletResponse.getWriter().write(JSON.toJSONString(matchResList));
    }
}
