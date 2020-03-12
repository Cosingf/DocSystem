package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.VO.KeywordWikiVO;
import cn.xmu.edu.legaldocument.VO.QASectionVO;
import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.mapper.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;



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
    @Autowired
    WikiMapper wikiMapper;
    @Autowired
    KeywordMapper keywordMapper;


    public List<QA> getHighLightResult(String highLight,Long bookid,Integer pagenum) throws Exception {
        Page page =pageMapper.selectByBookIdAndPageNum(bookid,pagenum);
        List<QA> qas = new ArrayList<>();
        if (page!=null) {
            List<Section> sections;
            sections = sectionMapper.selectByPageId(page.getId());
            Map<Long,Float> sectionSim = new HashMap<>();
            for (Section section :sections)
            {
                float a=(float) calculateStringDistance(highLight,section.getSectionContent())/Math.max(highLight.length(),section.getSectionContent().length());
                sectionSim.put(section.getId(),1-a);
            }
            List<Map.Entry<Long ,Float>> sectionSimList = new ArrayList<Map.Entry<Long ,Float>>(sectionSim.entrySet());
            Collections.sort(sectionSimList,new Comparator<Map.Entry<Long ,Float>>(){
                @Override
                public int compare(Map.Entry<Long, Float> o1,Map.Entry<Long, Float> o2) {
                    Float p =(o2.getValue()-o1.getValue());
                    if(p>0){
                        return 1;
                    }
                    else if(p==0){
                        return 0;
                    }
                    else
                        return -1;
                }
            });
            qas = qaMapper.selectQASectionBySectionId(sectionSimList.get(0).getKey());
        }
        return qas;
    }


    public List<QASectionVO> getBookEnrich(Long bookid,Integer pageNum) throws Exception {
        Page page =pageMapper.selectByBookIdAndPageNum(bookid,pageNum);
        List<QASectionVO> qaSectionVOs = new ArrayList<>();
        if (page!=null) {
            List<Section> sections = sectionMapper.selectByPageId(page.getId());
            for (Section section : sections) {
                List<QA> qas = qaMapper.selectQASectionBySectionId(section.getId());
                for (QA qa:qas)
                {
                    QASectionVO qaSectionVO = new QASectionVO(qa,section.getId(),section.getOrderNum(),section.getSectionContent());
                    qaSectionVOs.add(qaSectionVO);
                }
            }
        }
        return  qaSectionVOs;
    }




    private static int calculateStringDistance(@NotNull String s1, String s2) {
        int Distance=0;
        int Length1 =s1.length();
        int Length2 =s2.length();
        if (Length1==0) {
            Distance=Length2;
        }
        if(Length2==0)
        {
            Distance=Length1;
        }
        if(Length1!=0&&Length2!=0) {
            int[][] Distance_Matrix = new int[Length1 + 1][Length2 + 1];
            //编号
            int Bianhao = 0;
            for (int i = 0; i <= Length1; i++) {
                Distance_Matrix[i][0] = Bianhao;
                Bianhao++;
            }
            Bianhao = 0;
            for (int i = 0; i <= Length2; i++) {
                Distance_Matrix[0][i] = Bianhao;
                Bianhao++;
            }

            char[] Str_1_CharArray = s1.toCharArray();
            char[] Str_2_CharArray = s2.toCharArray();


            for (int i = 1; i <= Length1; i++) {
                for (int j = 1; j <= Length2; j++) {
                    if (Str_1_CharArray[i - 1] == Str_2_CharArray[j - 1]) {
                        Distance = 0;
                    } else {
                        Distance = 1;
                    }

                    int Temp1 = Distance_Matrix[i - 1][j] + 1;
                    int Temp2 = Distance_Matrix[i][j - 1] + 1;
                    int Temp3 = Distance_Matrix[i - 1][j - 1] + Distance;

                    Distance_Matrix[i][j] = Temp1 > Temp2 ? Temp2 : Temp1;
                    Distance_Matrix[i][j] = Distance_Matrix[i][j] > Temp3 ? Temp3 : Distance_Matrix[i][j];

                }
            }
            Distance= Distance_Matrix[Length1][Length2];
        }
        return Distance;
    }

    public WikiAnnotation getWikiByMatchingKeywords(String keyword) {
        return wikiMapper.getWikiByMatchingKeywords(keyword);
    }

    public void insertWikiList(List<WikiAnnotation> wikiList) {
        wikiMapper.insertWikiList(wikiList);
    }

    public List<KeywordWikiVO> getMatchResult(Long bookId) {
        List<KeywordWikiVO> keywordWikiVOList=new ArrayList<>();
        //获取bookId对应的KeywordList
        List<Keyword> keywordList=keywordMapper.getKeywordByBookId(bookId);
        //way1:直接match wiki keyword和keyword
        // 返回keyword对应的pageNum和wiki
        for(Keyword keyword:keywordList){
            Long pageId=keyword.getPageId();
            Long wikiId=keyword.getWikiCorpusId();
            Integer pageNum=pageMapper.getPageNumByPageId(pageId);
            WikiAnnotation wiki=wikiMapper.getWikiByWikiId(wikiId);
            KeywordWikiVO wikiVO=new KeywordWikiVO();
            wikiVO.setKeyword(keyword.getKeyword());
            wikiVO.setPageNum(pageNum);
            wikiVO.setSummary(wiki.getSummary());
            wikiVO.setTitle(wiki.getTitle());
            wikiVO.setUrl(wiki.getUrl());
            keywordWikiVOList.add(wikiVO);
        }
        //way2:查询lucene索引库，返回最相关的文章
        return keywordWikiVOList;
    }
}
