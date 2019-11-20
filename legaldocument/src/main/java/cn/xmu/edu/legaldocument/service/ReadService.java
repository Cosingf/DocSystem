package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.entity.Page;
import cn.xmu.edu.legaldocument.vo.QA;
import cn.xmu.edu.legaldocument.dao.QADao;
import cn.xmu.edu.legaldocument.entity.QASection;
import cn.xmu.edu.legaldocument.entity.Section;
import cn.xmu.edu.legaldocument.mapper.PageMapper;
import cn.xmu.edu.legaldocument.mapper.SectionMapper;
import cn.xmu.edu.legaldocument.util.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadService {

    @Autowired
    WordUtil wordUtil;

    @Autowired
    PageMapper pageMapper;

    @Autowired
    SectionMapper sectionMapper;
    QADao qaDao = new QADao();


    public List<QA> getHighLightResult(String highLight) throws Exception {
        String[] keyWords = wordUtil.getKeywords(highLight,2);
        return qaDao.findIndexDB(keyWords,3);
    }


    public List<QASection> getPageAllResult(Long bookid, Integer pagenum) throws Exception {
        Page page =pageMapper.selectByBookIdAndPageNum(bookid,pagenum);
       List<QASection>results = new ArrayList<>();
        if (page!=null) {
            List<Section> sections = sectionMapper.selectByPageId(page.getId());
            for (Section section:sections){
                QASection result = new QASection();
                String[] keyWords;
                if (section.getKeywords()==null) {
                    keyWords = wordUtil.getKeywords(section.getSectionContent(), 2);
                }
                else {keyWords =section.getKeywords().split(" ");}
                List<QA> qas=qaDao.findIndexDB(keyWords,1);
                result.setQa(qas.get(0));
                result.setSection(section);
               results.add(result);
            }
            return results;
        }
        return  null;
    }

}
