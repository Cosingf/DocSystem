package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.entity.PersonalLegaldocStack;
import cn.xmu.edu.legaldocument.mapper.LegalDocMapper;
import cn.xmu.edu.legaldocument.mapper.PersonalLegaldocStackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService
{
    @Autowired
    LegalDocMapper legalDocMapper;
    @Autowired
    PersonalLegaldocStackMapper personalLegaldocStackMapper;
    //num为页码
    public List<LegalDoc> getSixMyBooksByNumAndUserId(int num,Long userId){
        List<LegalDoc> legalDocs=new ArrayList<>();
        //计算该页其实条数是第几条
        int n = (num-1)*8;
        int n2= personalLegaldocStackMapper.getUserAllNumByUserId(userId);
        if ((n2-1-n)>=8) n2=8;
        else n2=n2-n;
        List<PersonalLegaldocStack> personalLegaldocStacks= personalLegaldocStackMapper.selectPageByUserId(userId,n,n2);
        if (personalLegaldocStacks.size()!=0||personalLegaldocStacks!=null)
        {
            for (PersonalLegaldocStack personalLegaldocStack:personalLegaldocStacks)
            {
                legalDocs.add(legalDocMapper.selectByPrimaryKey(personalLegaldocStack.getBookId()));
            }
        }
        return legalDocs;
    }
    //num为页码
    public List<LegalDoc> getEightPublicBooksByNum(int num){
        //计算该页起始条数是第几条
        int n = (num-1)*8;
        //获取总共条数
        int n2 = legalDocMapper.getAllNum();
        //判断总共条数是否大于起始条数8条，是的话查询范围是[n，n+8）
        if ((n2-1-n)>=8) n2=8;
        //否则范围为[n,n2）
        else n2=n2-n;
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicPage(n,n2);
        return legalDocs;
    }

    public List<LegalDoc> searchPublicBooksByContent(String content){
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicBooksByContent(content);
        return legalDocs;
    }
    //content为搜索词
    public List<LegalDoc> searchMybooksByContentAndUserId(String content,Long userId){
        List<LegalDoc> legalDocs=new ArrayList<>();
        //获取该用户所有用户文献关系
        List<PersonalLegaldocStack> personalLegaldocStacks= personalLegaldocStackMapper.selectByUserId(userId);
        if (personalLegaldocStacks.size()!=0||personalLegaldocStacks!=null)
        {
            for (PersonalLegaldocStack personalLegaldocStack:personalLegaldocStacks)
            {
                //获取个人书库所有文献
                legalDocs.add(legalDocMapper.selectByPrimaryKey(personalLegaldocStack.getBookId()));
            }
        }
        List<LegalDoc> books = new ArrayList<>();
        //遍历每个文献
        for (LegalDoc legalDoc:legalDocs)
        {
            //判断文献名、作者名是否包含搜索词，是的话添加进去返回列表
            if (legalDoc.getName().contains(content) || legalDoc.getAuthor().contains(content))
                books.add(legalDoc);
        }
        return books;
    }

    public String addToMyBooks(long bookId,long userId){
        //判断文献是否存在
        if(legalDocMapper.selectByPrimaryKey(bookId)==null)
            return "Doesn't exist!";
        PersonalLegaldocStack personalLegaldocStack = new PersonalLegaldocStack();
        personalLegaldocStack.setBookId(bookId);
        personalLegaldocStack.setUserId(userId);
        //判断用户文献关系是否存在，存在则提示已存在
        if (personalLegaldocStackMapper.selectByBookIdAndUserId(bookId,userId)!=null)
            return "The book already exists";
        //否则插入用户文献关系，返回插入成功信息
        personalLegaldocStackMapper.insert(personalLegaldocStack);
        return "Successfully adding";
    }

    public boolean deleteBook(long bookId,long userId){
        int result = personalLegaldocStackMapper.deleteByBookIdAndUserId(bookId,userId);
        if (result >0 )return true;
        return false;
    }
}
