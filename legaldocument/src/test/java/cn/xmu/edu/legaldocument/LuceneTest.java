package cn.xmu.edu.legaldocument;


import cn.xmu.edu.legaldocument.dao.QADao;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
public class LuceneTest {

    /*
        建立索引
     */

    @Test
    public void createIndexDB() throws Exception {

        //创建IndexWriter对象
        Path path = Paths.get("D:/lucene/index01");
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

        File file = new File("D:/lucene/pinfo-mz-test.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");//考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);//放在缓存流中
        String lineTxt;
        String allTxt = "";
        int n = 0;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            allTxt += lineTxt;
        }

        String[] qas = allTxt.split("question###");//切分数据

        String question = "";
        String answer = "";

        //把数据填充到JavaBean对象中
        for (int i=1 ; i<qas.length-1;i++) {

            String[] temp;
            temp = qas[i].split("answer###");
            question = temp[0];
            answer = temp[1];

            System.out.println(question + "\n" + answer + "\n" );
            //创建Document对象【导入的是Lucene包下的Document对象】
            Document document = new Document();
            /**
             * 向Document对象加入一个字段
             * 参数一：字段的关键字
             * 参数二：字符的值
             * 参数三：是否要存储到原始记录表中
             *      YES表示是
             *      NO表示否
             * 参数四：是否需要将存储的数据拆分到词汇表中
             *      ANALYZED表示拆分
             *      NOT_ANALYZED表示不拆分
             *
             * */
            document.add(new Field("question", question, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("answer", answer, Field.Store.YES, Field.Index.ANALYZED));
            //将Document对象通过IndexWriter对象写入索引库中
            indexWriter.addDocument(document);
        }

        //关闭IndexWriter对象
        indexWriter.close();

    }

    @Test
    public void findIndexDB() throws Exception {
      String[] keywords={"薛之谦","堕胎"};
      QADao qaDao = new QADao();
      qaDao.findIndexDB(keywords,2);

      System.in.read();
    }
}


