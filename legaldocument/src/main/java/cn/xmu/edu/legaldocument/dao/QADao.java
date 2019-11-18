package cn.xmu.edu.legaldocument.dao;

import cn.xmu.edu.legaldocument.entity.QA;
import com.alibaba.fastjson.JSON;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class QADao {


    public List<QA> findIndexDB(String[] keywords,int n) throws Exception {

        /**
         * 参数一： IndexSearcher(Directory path)查询以xxx目录的索引库
         *
         * */
        Path path = Paths.get("D:/lucene/index01");
        Directory directory = FSDirectory.open(path);//索引创建在硬盘上。
        //创建IndexSearcher对象
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //创建QueryParser对象
        /**
         * 参数一： Version matchVersion 版本号【和上面是一样的】
         * 参数二：String f,【要查询的字段】
         * 参数三：Analyzer a【使用的拆词算法】
         * */
        Analyzer analyzer = new IKAnalyzer();
         //搜索域
        String[] fields={"question","answer"};
         //搜索条件
        BooleanClause.Occur[] occ ={BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};

        //创建Query对象来封装关键字
        Query query = MultiFieldQueryParser.parse(keywords,fields,occ,analyzer);

        //用IndexSearcher对象去索引库中查询符合条件的前n条记录
        TopDocs topDocs = indexSearcher.search(query, n);

        //获取符合条件的编号
        List<QA> qas = new ArrayList<>();
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {

            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int no = scoreDoc.doc;
            //用indexSearcher对象去索引库中查询编号对应的Document对象
            Document document = indexSearcher.doc(no);

            //将Document对象中的所有属性取出，再封装回JavaBean对象中去
            String question = document.get("question");
            String answer = document.get("answer");
            String link = document.get("link");

            QA qa = new QA(question, answer, link);
            qas.add(qa);
            System.out.println(JSON.toJSONString(qa));
        }
        return qas;
    }
}
