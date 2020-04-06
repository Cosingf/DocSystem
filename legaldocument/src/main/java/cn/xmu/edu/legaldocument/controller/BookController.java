package cn.xmu.edu.legaldocument.controller;


import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.service.BookService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/publicbooks/search/{content}")
    public void searchPersonalBooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("content") String content) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.searchPublicBooksByContent(content);
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
            httpServletResponse.setStatus(404);
    }

    @PostMapping("/searchMybooks/{searchContent}/{userid}")
    public void searchMybooks(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("searchContent") String searchContent,@PathVariable("userid") Long userid) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");

        List<LegalDoc> legalDocList = bookService.searchMybooksByContentAndUserId(searchContent,userid);
        if (legalDocList!=null||legalDocList.size()!=0) {
            httpServletResponse.setStatus(200);
            httpServletResponse.getWriter().write(JSON.toJSONString(legalDocList));
        }
        else
            httpServletResponse.setStatus(404);
    }

    @DeleteMapping("/mybooks/delete/{bookId}/{userId}")
    public void deleteBook(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("bookId") Long bookId, @PathVariable("userId") Long userId) throws Exception
    {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        if (bookService.deleteBook(bookId,userId)) {
            httpServletResponse.setStatus(200);
        }
        else {
            httpServletResponse.setStatus(404);
        }
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