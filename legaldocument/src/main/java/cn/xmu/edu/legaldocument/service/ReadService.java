package cn.xmu.edu.legaldocument.service;


import cn.xmu.edu.legaldocument.VO.KeywordWikiVO;
import cn.xmu.edu.legaldocument.VO.QASectionVO;
import cn.xmu.edu.legaldocument.algorithm.GetBookTopicKeywords;
import cn.xmu.edu.legaldocument.dao.QADao;
import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.mapper.*;
import cn.xmu.edu.legaldocument.util.WordUtil;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
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
    @Autowired
    LegalDocMapper legalDocMapper;
    @Autowired
    WordUtil wordUtil;
    @Autowired
    QADao qaDao;



    //获取基于深度学习算法实现的高亮搜索结果
    public List<QA> getHighLightResult(String highLight,Long bookid,Integer pagenum) throws Exception {
        //查询获取高亮内容所在页
        Page page =pageMapper.selectByBookIdAndPageNum(bookid,pagenum);
        List<QA> qas = new ArrayList<>();
        if (page!=null) {
            List<Section> sections;
            //获取该页下的所有段
            sections = sectionMapper.selectByPageId(page.getId());
            Map<Long,Float> sectionSim = new HashMap<>();
            //遍历所有段，调用编辑距离算法计算相似度
            for (Section section :sections)
            {
                float a=(float) calculateStringDistance(highLight,section.getSectionContent())/Math.max(highLight.length(),section.getSectionContent().length());
                sectionSim.put(section.getId(),1-a);
            }
            List<Map.Entry<Long ,Float>> sectionSimList = new ArrayList<Map.Entry<Long ,Float>>(sectionSim.entrySet());
            //对相似度排序
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
            //查询最相似的段关联的QA数据
            qas = qaMapper.selectQASectionBySectionId(sectionSimList.get(0).getKey());
        }
        return qas;
    }

    //获取基于Lucene技术实现的高亮结果
    public List<QA> getChromeHighLightResult(String highLight) throws Exception {
        String[] keyWords = wordUtil.getKeywords(highLight,2);
        return qaDao.findIndexDB(keyWords,5);
    }

    //获取基于Lucene技术实现的分段式全文增强
    public List<QASectionVO> getChromeEnrich(String content) throws Exception {
        List<Section> sections = new ArrayList<>();
        List<QASectionVO> results = new ArrayList<>();
        for (int i=0 ;i<content.length();i++)
        {
            Section section = new Section();
            section.setOrderNum(i);
            int start = i*50;
            int end = start + 99;
            section.setSectionContent(content.substring(start,end));
        }
            for (Section section:sections){
                QASectionVO result = new QASectionVO();
                String[] keyWords;
                keyWords = wordUtil.getKeywords(section.getSectionContent(), 2);

                List<QA> qas=qaDao.findIndexDB(keyWords,1);
                result.setQuestion(qas.get(0).getQuestion());
                result.setAnswer(qas.get(0).getAnswer());
                result.setSectionContent(section.getSectionContent());
                results.add(result);
            }
            return results;
    }

    //获取基于深度学习算法实现的分段式全文增强
    public List<QASectionVO> getPageEnrich(Long bookid,Integer pageNum) throws Exception {
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
    public List<QA> getChromeAllPageResultByAlgorithm(String allContent) throws Exception {
        GetBookTopicKeywords getBookTopicKeywords = new GetBookTopicKeywords();
        String[] keywords = getBookTopicKeywords.getBookTopicKeywords(null,allContent);
        return qaDao.findIndexDB(keywords,20);
    }

    //计算两个句子的相似度
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

    public String getDocById(Long bookId) throws IOException {
        String pdfPath=legalDocMapper.getPathByBookId(bookId);
        PdfReader reader = new PdfReader(pdfPath);
        int num = reader.getNumberOfPages();// 获得页数
        StringBuilder doc=new StringBuilder();
        for (int i = 1; i <= num; i++) {
            doc.append(PdfTextExtractor.getTextFromPage(reader, i)) ;
        }
        return doc.toString();
    }
}
