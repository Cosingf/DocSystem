package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.VO.PageSectionVO;
import cn.xmu.edu.legaldocument.VO.QASectionVO;
import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.mapper.PageMapper;
import cn.xmu.edu.legaldocument.mapper.QAMapper;
import cn.xmu.edu.legaldocument.mapper.SectionMapper;
import cn.xmu.edu.legaldocument.mapper.SelectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadService {


    @Autowired
    PageMapper pageMapper;

    @Autowired
    SectionMapper sectionMapper;

    @Autowired
    SelectionMapper selectionMapper;
    @Autowired
    QAMapper qaMapper;


    public List<QA> getHighLightResult(String highLight,Long bookid,Integer pagenum) throws Exception {
        Page page =pageMapper.selectByBookIdAndPageNum(bookid,pagenum);
        List<QA> qas = new ArrayList<>();
        if (page!=null) {
            Selection selection = new Selection();
            selection.setSelection(highLight);
            selection.setPageId(page.getId());
            selection.setBookId(bookid);
            selectionMapper.insert(selection);
            List<QASection> results = new ArrayList<>();
            String[] words = highLight.split(" ");
            List<String> inputStr = new ArrayList<>();
            String strNum = "";
            String str1="";String str2="";
            if (words.length==1) {
                str1=words[0];
                strNum ="1";
            }//当高亮词小于十个，就把其划分成两个str，模糊查询
            else if (words.length<=10){
                int n = words.length/2;
                String str=words[0];
                for (int i=1;i<n;i++)
                     str=str+" "+words[i];
                str1=str;
                str=words[n];
                for (int i=n+1;i<words.length;i++)
                    str=str+" "+words[i];
                str2=str;
                strNum ="2";
            }
            //若高亮词大于十，取前是个划分成两个str，模糊查询，先定位出具体位置
            else {
                String str=words[0];
                for (int i=1;i<5;i++)
                    str=str+" "+words[i];
               str1 =str;
                str=words[5];
                for (int i=6;i<10;i++)
                    str=str+" "+words[i];
                 str2=str;
                strNum ="2";
            }
            List<Section> sections = sectionMapper.selectByPageIdAndHighLightAndNum(page.getId(),str1,str2,strNum);
            Section section = sections.get(1);
             qas = qaMapper.selectQASectionBySectionId(section.getId());
        }
        return qas;
    }


    public List<PageSectionVO> getBookEnrich(Long bookid) throws Exception {
        List<Page> pages =pageMapper.selectByBookId(bookid);
        List<PageSectionVO> pageSectionVOs = new ArrayList<>();
        if (pages!=null) {
            for (Page page : pages) {
                List<Section> sections = sectionMapper.selectByPageId(page.getId());
                List<QASectionVO> qaSectionVOs = new ArrayList<>();
                PageSectionVO pageSectionVO = new PageSectionVO();
                pageSectionVO.setPageId(page.getId());
                pageSectionVO.setPageNum(page.getOrderNum());
                for (Section section : sections) {
                    List<QA> qas = qaMapper.selectQASectionBySectionId(section.getId());
                    for (QA qa:qas)
                    {
                        QASectionVO qaSectionVO = new QASectionVO(qa,section.getId(),section.getOrderNum(),section.getSectionContent());
                        qaSectionVOs.add(qaSectionVO);
                    }
                }
                pageSectionVO.setQaSectionVOS(qaSectionVOs);
                pageSectionVOs.add(pageSectionVO);
            }
        }
        return  pageSectionVOs;
    }

}
