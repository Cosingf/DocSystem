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
        num = (num-1)*6;
        int num2= personalLegaldocStackMapper.getUserAllNumByUserId(userId);
        if ((num2-1-num)>=6) num2=num+5;
        else num2=num2-num;
        List<PersonalLegaldocStack> personalLegaldocStacks= personalLegaldocStackMapper.selectPageByUserId(userId,num,num2);
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
        int n = (num-1)*6;
        int n2 = legalDocMapper.getAllNum();
        if ((n2-1-n)>=6) n2=6;
        else n2=n2-n;
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicPage(n,n2);
        return legalDocs;
    }

    public List<LegalDoc> searchPublicBooksByName(String name){
        List<LegalDoc> legalDocs=legalDocMapper.selectPublicBooksByName(name);
        return legalDocs;
    }

    public List<LegalDoc> searchMybooksByNameAndUserId(String name,Long userId){

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
            if (legalDoc.getName().contains(name))
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
}
