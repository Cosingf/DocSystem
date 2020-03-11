package cn.xmu.edu.legaldocument.util;

import cn.xmu.edu.legaldocument.entity.WikiAnnotation;
import cn.xmu.edu.legaldocument.service.ReadService;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class WikiUtil {
    public static Logger logger = LoggerFactory.getLogger(WikiUtil.class);

    @Autowired
    ReadService readService;

    Directory directory;

    public WikiUtil() throws IOException {
        this.directory= FSDirectory.open(FileSystems.getDefault().getPath("F:\\wikiIndex"));
    }
    /*
    **导入维基数据集，并建立倒排索引
     */
    @RequestMapping("/importWiki")
    public void importTxt() throws IOException, ParseException {
        logger.info("开始导入数据");

        String encoding = "utf-8";
        List<WikiAnnotation> wikiList = new ArrayList<>();
        String dir = "F:\\data";
        File[] files = new File(dir).listFiles();
        assert files != null;
        for (File file : files){//循环文件夹中的文件
            if (file.isFile() && file.exists()) { //判断文件是否存在
                importFile(file, encoding, wikiList);  //将文件中的数据读取出来，并存放进集合中
            } else {
                logger.info("文件不存在");
            }
        }
        directory.close();
        readService.insertWikiList(wikiList);   //将集合中的wiki数据批量入库
    }


    //读取数据，存入集合中
    public void importFile(File file, String encoding, List wikiList) throws IOException, ParseException {
        InputStreamReader read = null;//考虑到编码格式
        try {
            read = new InputStreamReader(new FileInputStream(file), encoding);  //输入流
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        assert read != null;
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        //读取title
        lineTxt=bufferedReader.readLine();
        //empty file
        if(lineTxt==null){
            return;
        }
        String[] titleArr=lineTxt.split("[【】]");
        String title=titleArr[titleArr.length-1];
        System.out.println("title: "+title);
        //读取content
        StringBuilder content=new StringBuilder();
        while ((lineTxt = bufferedReader.readLine()) != null) {
            content.append(lineTxt+"\n");
        }
        //从content获取第一段作为summary
        int maxLen= Math.min(content.length(), 900);
        int summaryStop=content.indexOf("==")>0?content.indexOf("=="):maxLen;

        String summary=content.substring(1,summaryStop);
        summary=summary.replaceAll("^((\r\n)|\n)", "");
        //summary过短
        if(summary.length()<300){
            int space=content.indexOf("=",summary.length()+200);
            int period=content.indexOf(".",summary.length()+200);
            int stop=Math.min(space,period)>0?Math.min(space,period):maxLen;
            summary=content.substring(1,stop);

        }
        //summary过长java.lang.StringIndexOutOfBoundsException: start 1, end -1,
        else if(summary.length() > 900){
            int space=content.indexOf("=",800);
            int period=content.indexOf(".",800);
            int stop=Math.min(space,period)>0?Math.min(space,period):900;
            summary=content.substring(1,stop);
        }
//        log.info(String.valueOf(summary.length()));
//        log.info("summary:"+summary);
        //封装实体对象做入库准备
        WikiAnnotation wiki = new WikiAnnotation();
        wiki.setSummary(summary);
        wiki.setTitle(title);
        wiki.setPath(file.getPath());
        String urlTitle=title.replaceAll(" ","_");
        wiki.setUrl("https://en.wikipedia.org/wiki/"+urlTitle);
        wikiList.add(wiki);
        read.close();  //输入流关闭
    }

}
