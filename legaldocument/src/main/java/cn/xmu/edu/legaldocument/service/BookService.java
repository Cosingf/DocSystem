package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.entity.PersonalLegaldocStack;
import cn.xmu.edu.legaldocument.mapper.LegalDocMapper;
import cn.xmu.edu.legaldocument.mapper.PersonalLegaldocStackMapper;
import org.hibernate.validator.constraints.EAN;
import org.hibernate.validator.internal.util.logging.formatter.CollectionOfClassesObjectFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
          List<PersonalLegaldocStack> personalLegaldocStacks= personalLegaldocStackMapper.selectByUserId(userId);
          if (personalLegaldocStacks.size()!=0||personalLegaldocStacks!=null)
          {
              for (PersonalLegaldocStack personalLegaldocStack:personalLegaldocStacks)
              {
                  legalDocs.add(legalDocMapper.selectByPrimaryKey(personalLegaldocStack.getBookId()));
              }
          }
        Collections.sort(legalDocs, new Comparator<LegalDoc>() {
            @Override
            public int compare(LegalDoc o1, LegalDoc o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
          int booknum = num*6-1;
          return legalDocs;
    }

    public List<LegalDoc> getSixPublicBooksByNum(int num){
        List<LegalDoc> legalDocs=legalDocMapper.selectAll();
        int booknum = num*6-1;
        return legalDocs;
    }

    public List<LegalDoc> searchBooksByName(String name){
        List<LegalDoc> legalDocs=legalDocMapper.selectAll();
        List<LegalDoc> books = new ArrayList<>();
        for (LegalDoc legalDoc:legalDocs)
        {
            if (legalDoc.getName().contains(name))
                books.add(legalDoc);
        }
        return books;
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
