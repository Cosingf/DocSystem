package cn.xmu.edu.legaldocument.util;

import cn.xmu.edu.legaldocument.entity.QA;
import cn.xmu.edu.legaldocument.service.ReadService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/*
导入txt中的qa数据进数据库，并创建索引
 */
@RestController
public class ImportQAUtil {

    private static String filePath = "D:/lucene/pinfo-mz-test.txt";

    private static String lucenePath = "D:/lucene/QAindex";

    @Autowired
    private ReadService readService;

    @RequestMapping("/importQA")
    public void importTxt() throws IOException, ParseException {
        String encoding = "utf-8";
        List<QA> qaList = new ArrayList<>();
        File file = new File(filePath);
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);//放在缓存流中
        String lineTxt;
        String allTxt = "";
        int n = 0;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            allTxt += lineTxt;
        }
        bufferedReader.close();
        String[] qas = allTxt.split("question###");//切分数据

        //把数据填充到JavaBean对象中
        for (int i=1 ; i<qas.length-1;i++) {
            QA qa = new QA();
            String[] temp;
            temp = qas[i].split("answer###");
            qa.setQuestion(temp[0]);
            qa.setAnswer(temp[1]);
            qaList.add(qa);
        }
        createIndexes(qaList);
    }


    public static void createIndexes(List<QA> qaList) throws IOException {
        //创建IndexWriter对象
        Path path = Paths.get(lucenePath);
        Directory directory = FSDirectory.open(path);//索引创建在硬盘上。

        //使用英语分词算法对原始记录表进行拆分
        Analyzer analyzer = new EnglishAnalyzer();
        /**
         * IndexWriter将我们的document对象写到硬盘中
         *
         * 参数一：Directory d,写到硬盘中的目录路径是什么
         * 参数二：Analyzer a, 以何种算法来对document中的原始记录表数据进行拆分成词汇表
         * 参数三：MaxFieldLength mfl 最多将文本拆分出多少个词汇
         *
         * */
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, iwc);

        FieldType QAtype = new FieldType();

        QAtype.setStored(true); // 设置是否存储该字段
        QAtype.setTokenized(true); // 设置是否对该字段分词
        QAtype.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS); // 设置该字段的索引选项
        QAtype.freeze(); // 固定

        for (QA qa : qaList) {
            //创建Document对象【导入的是Lucene包下的Document对象】
            Document document = new Document();
            /**
             * 向Document对象加入一个字段
             * 参数一：字段的关键字
             * 参数二：字符的值
             * 参数三：存储类型
             * */
            document.add(new Field("question", qa.getQuestion(), QAtype));
            document.add(new Field("answer", qa.getAnswer(),QAtype));
            //将Document对象通过IndexWriter对象写入索引库中
            indexWriter.addDocument(document);
        }
        //提交
        indexWriter.commit();
        //关闭IndexWriter对象
        indexWriter.close();

    }
}
