package cn.xmu.edu.legaldocument.util;

import cn.xmu.edu.legaldocument.algorithm.WordListConverter;
import cn.xmu.edu.legaldocument.mapper.QAMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Component
public class WordUtil {

    private final  static  Integer NUM=4;

    @Autowired
    QAMapper qaMapper;

    public String[] getKeywords(String string,int n) throws IOException {

        //分词
         Analyzer ea =new EnglishAnalyzer();
         List<String> wordList= new ArrayList<>();

         string = string.replaceAll("[^a-zA-Z ]", "");
         WordListConverter wlc = new WordListConverter();

         Reader r = new StringReader(string);
         TokenStream tokenStream = ea.tokenStream("text",r);
         tokenStream.reset();
         while (tokenStream.incrementToken()){
             CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
             String word =charTermAttribute.toString();
             if (!word.equals("nbsp"))
                 wordList.add(word);
         }
         //将list转换为map
        Map<String,Integer> keyMap = new HashMap<>();
         for (String key:wordList){
             keyMap.put(key,keyMap.get(key)==null?1:keyMap.get(key)+1);
         }
          //排序
         ArrayList<Map.Entry<String,Integer>> keyList = new ArrayList<Map.Entry<String, Integer>>(keyMap.entrySet());
         Collections.sort(keyList, new Comparator<Map.Entry<String, Integer>>() {
             @Override
             public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                 return (o2.getValue()-o1.getValue());
             }
         });

         if(keyList.size()<n) n=keyList.size();
         String[] keyWords = new String[n];
         for (int i=0;i<keyList.size();i++){
             if (i<n)
                 keyWords[i]=keyList.get(i).getKey();
         }

         return  keyWords;
    }

}
