package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.service.BookService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class BookController {


    @Autowired
    BookService bookService;

    @PostMapping("/publicbooks/sixbooks/{num}")
    public void getPublicBooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,@PathVariable("num") String num) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.getSixPublicBooksByNum(Integer.valueOf(num));
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
        {httpServletResponse.setStatus(404);}
    }


    @PostMapping("/mybooks/sixbooks/{userid}/{num}")
    public void getPersonalBooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,@PathVariable("userid") Long userid,@PathVariable("num") String num) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.getSixMyBooksByNumAndUserId(Integer.valueOf(num),userid);
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
            httpServletResponse.setStatus(404);
    }

    @PostMapping("/publicbooks/search/{bookname}")
    public void searchPersonalBooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("bookname") String bookname) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.searchPublicBooksByName(bookname);
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
            httpServletResponse.setStatus(404);
    }

    @PostMapping("/mybooks/{bookname}/{userid}")
    public void searchMybooksByname(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("bookname") String bookname,@PathVariable("userid") Long userid) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.searchMybooksByNameAndUserId(bookname,userid);
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
            httpServletResponse.setStatus(404);
    }

    @PutMapping("/publicbooks/toMyBook/{userId}/{bookId}")
    public void toMyBooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("userId") Long userId,@PathVariable("bookId") Long bookId) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        if (bookService.addToMyBooks(bookId,userId)) {
            httpServletResponse.setStatus(200);
        }
        else {
            httpServletResponse.setStatus(404);
        }
    }

}
