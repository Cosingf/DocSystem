package cn.xmu.edu.legaldocument.controller;

import cn.xmu.edu.legaldocument.entity.QA;
import cn.xmu.edu.legaldocument.entity.QASection;
import cn.xmu.edu.legaldocument.service.ReadService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void getHighLightResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,@RequestBody Map<String, Object> map) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<QA>  qas =readService.getHighLightResult(map.get("content").toString());
        if (qas!=null||qas.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(qas));
        }
        else {
            httpServletResponse.setStatus(404);
        }
    }


    @PostMapping("/read/{bookid}/{pagenum}")
    public void getAllPageResult(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("bookid") Long bookid,@PathVariable("pagenum") Integer pagenum  ) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

       List<QASection> relusts =readService.getPageAllResult(bookid,pagenum);
        if (relusts==null)
            httpServletResponse.setStatus(404);
        else {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(relusts));
        }
    }


}
