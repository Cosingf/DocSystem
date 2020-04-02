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

    public List<LegalDoc> getSixMyBooksByNumAndUserId(int num,Long userId){
        List<LegalDoc> legalDocs=new ArrayList<>();
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

    public List<LegalDoc> getSixPublicBooksByNum(int num){
        int n = (num-1)*8;
        int n2 = legalDocMapper.getAllNum();
        if ((n2-1-n)>=8) n2=8;
        else n2=n2-n;
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicPage(n,n2);
        return legalDocs;
    }

    public List<LegalDoc> searchPublicBooksByContent(String content){
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicBooksByContent(content);
        return legalDocs;
    }

    public List<LegalDoc> searchMybooksByContentAndUserId(String content,Long userId){

        List<LegalDoc> legalDocs=new ArrayList<>();
        List<PersonalLegaldocStack> personalLegaldocStacks= personalLegaldocStackMapper.selectByUserId(userId);
        if (personalLegaldocStacks.size()!=0||personalLegaldocStacks!=null)
        {
            for (PersonalLegaldocStack personalLegaldocStack:personalLegaldocStacks)
            {
                legalDocs.add(legalDocMapper.selectByPrimaryKey(personalLegaldocStack.getBookId()));
            }
        }
        List<LegalDoc> books = new ArrayList<>();
        for (LegalDoc legalDoc:legalDocs)
        {
            if (legalDoc.getName().contains(content) || legalDoc.getAuthor().contains(content))
                books.add(legalDoc);
        }
        return books;
    }

    public boolean addToMyBooks(long bookId,long userId){
        if(legalDocMapper.selectByPrimaryKey(bookId)==null)
            return false;
        PersonalLegaldocStack personalLegaldocStack = new PersonalLegaldocStack();
        personalLegaldocStack.setBookId(bookId);
        personalLegaldocStack.setUserId(userId);
        if (personalLegaldocStackMapper.selectByBookIdAndUserId(bookId,userId)!=null) return true;
        personalLegaldocStackMapper.insert(personalLegaldocStack);
        return true;
    }

    public boolean deleteBook(long bookId,long userId){
        int result = personalLegaldocStackMapper.deleteByBookIdAndUserId(bookId,userId);
        if (result >0 )return true;
        return false;
    }
}
