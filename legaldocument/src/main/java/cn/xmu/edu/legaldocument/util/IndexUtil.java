package cn.xmu.edu.legaldocument.util;

import cn.xmu.edu.legaldocument.entity.WikiAnnotation;
import cn.xmu.edu.legaldocument.mapper.WikiMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;


import java.io.*;
import java.nio.file.FileSystems;
import java.util.List;

/**
 * 处理数据库现有的wiki文档，生成文档关键词的倒排索引
 */
@RestController
public class IndexUtil {
    public static Logger logger= LoggerFactory.getLogger(IndexUtil.class);

    Directory directory;

    @Autowired
    WikiMapper wikiMapper;

    public IndexUtil() throws IOException {
        this.directory= FSDirectory.open(FileSystems.getDefault().getPath("F:\\wikiIndex"));
    }

    public IndexWriter getIndexWriter() throws IOException {
        Analyzer analyzer =new EnglishAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(this.directory, config);
    }
    /*
    **读取数据库已有wiki，添加索引并存入数据库
     */
    @RequestMapping("/generateIndex")
    public void generateWikiIndex() throws Exception {
        List<WikiAnnotation> wikiList=wikiMapper.getAllWiki();
        for(WikiAnnotation wiki:wikiList){
            index(wiki.getPath(),wiki.getId());
        }
        directory.close();
//        WikiAnnotation wiki=wikiMapper.getWikiByWikiId((long)908);
//        index(wiki.getPath(),wiki.getId());

    }
    /**
     * 向索引库中添加索引
     * @param pathname 传入文件路径，可以是具体的某个文件，也可以是包含文件的文件夹路径
     * @throws IOException
     */
    public void index(String pathname,Long wikiId) throws Exception {
        IndexWriter indexWriter =getIndexWriter();

        File file = new File(pathname);
        Document document = new Document();
        // 文件名字
        String file_name = file.getName();
        if(!file.exists()){
            logger.info("文件不存在");
        }

        FieldType type = new FieldType();

        type.setStored(true); // 设置是否存储该字段
        type.setTokenized(true); // 设置是否对该字段分词
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS); // 设置该字段的索引选项
        type.freeze(); // 使不可更改

        Field fileNameField = new Field("fileName", file_name, type);
        // 文件内容
        String file_content = FileUtils.readFileToString(file, "utf-8");
        Field fileContentField = new Field("fileContent", file_content,type);

        FieldType wikiIdType = new FieldType();
        wikiIdType.setStored(true); // 设置是否存储该字段
        wikiIdType.setTokenized(false); // 设置是否对该字段分词
        wikiIdType.setIndexOptions(IndexOptions.DOCS);// 只存储了包含该词的 文档id,没有词频,位置
        Field wikiIdField=new Field("wikiId", String.valueOf(wikiId),wikiIdType);

        document.add(fileNameField);
        document.add(fileContentField);
        document.add(wikiIdField);

        indexWriter.addDocument(document);
        logger.info("current docs:"+indexWriter.numDocs());
        // 提交
        indexWriter.commit();
        indexWriter.close();
//        queryIndex(wikiId);
    }
    //

    @RequestMapping("/queryParse")
    public void queryParse() throws Exception{
        // 创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(this.directory);

        // 创建indexsearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //指定分词技术，这里采用的语言处理模块要和创建索引的时候一致，否则检索的结果很不理想
        Analyzer analyzer = new EnglishAnalyzer();
        //创建查询query，搜索词为“空间向量”
        QueryParser parse = new QueryParser("fileContent", analyzer);
        Query query = parse.parse("cappella music");
        // 执行查询
        // 第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query,2);

        // 查询结果的总条数
        System.out.println("查询结果的总条数：" + topDocs.totalHits);

        // 遍历查询结果
        // topDocs.scoreDocs存储了document对象的id
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            // scoreDoc.doc属性就是document对象的id
            // 根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(scoreDoc.toString());
            System.out.println("filename:"+document.get("fileName"));
            System.out.println("fileContent:\n"+document.get("fileContent"));
            System.out.println("-----------------------------------");
        }
        indexReader.close();
    }

    @RequestMapping("/queryTerm")
    public void queryIndex() throws Exception{
        // 创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(this.directory);
        // 创建indexsearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //指定分词技术，这里采用的语言处理模块要和创建索引的时候一致，否则检索的结果很不理想
        Analyzer analyzer = new EnglishAnalyzer();

        QueryParser parse = new QueryParser("fileContent", analyzer);
        Terms terms = MultiFields.getTerms(indexReader, "fileContent");
        TermsEnum termsEnums = terms.iterator(null);
        BytesRef byteRef = null;
        while((byteRef = termsEnums.next()) != null) {
            String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
            Term t=new Term("fileContent",term);
            if(termsEnums.totalTermFreq()>5){
                System.out.println("term is : " + term+" Freq is:"+termsEnums.totalTermFreq()+"doc Freq:"+termsEnums.docFreq());
                //查询每个term 最相关的doc
                Query queryT= parse.parse(term);
                TopDocs topDocsT = indexSearcher.search(queryT, 2);
                for (ScoreDoc scoreDoc : topDocsT.scoreDocs) {
                    // scoreDoc.doc属性就是document对象的id
                    // 根据document的id找到document对象
                    Document document = indexSearcher.doc(scoreDoc.doc);
                    System.out.println(scoreDoc.toString());
                    System.out.println("filename:"+document.get("fileName"));
                    System.out.println("WikiId:\n"+document.get("wikiId"));
                    System.out.println("-----------------------------------");
                }
            }

        }

        // 第七步：关闭IndexReader对象
        indexReader.close();
    }

    // 查询索引
    public void query(Long wikiId) throws Exception {

        // 创建一个indexReader对象，需要指定Directory对象。
        IndexReader indexReader = DirectoryReader.open(this.directory);

        // 创建indexsearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 创建查询
//        Query query = new TermQuery(new Term("wikiId", String.valueOf(wikiId)));

        //指定分词技术，这里采用的语言处理模块要和创建索引的时候一致，否则检索的结果很不理想
        Analyzer analyzer = new IKAnalyzer();
        //创建查询query，搜索词为“空间向量”
        QueryParser parse = new QueryParser("fileContent", analyzer);
        Query query = parse.parse("Aalen");


        // 执行查询
        // 第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query,2);

        // 查询结果的总条数
        System.out.println("查询结果的总条数：" + topDocs.totalHits);

        // 遍历查询结果
        // topDocs.scoreDocs存储了document对象的id
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            // scoreDoc.doc属性就是document对象的id
            // 根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(scoreDoc.toString());
            System.out.println("filename:"+document.get("fileName"));
            System.out.println("fileContent:\n"+document.get("fileContent"));
            System.out.println("-----------------------------------");
        }
        //遍历所有term，获取每个term最相关的3篇文章
        Terms terms = MultiFields.getTerms(indexReader, "fileContent");
        TermsEnum termsEnums = terms.iterator(null);
        BytesRef byteRef = null;
        while((byteRef = termsEnums.next()) != null) {
            String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
            System.out.println("term is : " + term+" Freq is:"+termsEnums.totalTermFreq());
            Query queryT= parse.parse(term);
            TopDocs topDocsT = indexSearcher.search(queryT, 3);
            for (ScoreDoc scoreDoc : topDocsT.scoreDocs) {
                // scoreDoc.doc属性就是document对象的id
                // 根据document的id找到document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println(scoreDoc.toString());
                System.out.println("filename:"+document.get("fileName"));
//                System.out.println("fileContent:\n"+document.get("fileContent"));
                System.out.println("-----------------------------------");
            }
        }

        // 第七步：关闭IndexReader对象
        indexReader.close();
    }

}
