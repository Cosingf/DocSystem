package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.mapper.*;
import cn.xmu.edu.legaldocument.entity.QA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdfwriter.COSWriter;
import org.pdfbox.util.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.util.*;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);
    @Autowired
    LegalDocMapper legalDocMapper;
    @Autowired
    PageMapper pageMapper;
    @Autowired
    SectionMapper sectionMapper;
    @Autowired
    QAMapper qaMapper;
    @Autowired
    WikiMapper wikiMapper;
    @Autowired
    KeywordMapper keywordMapper;

    Directory directory;

    public PdfService() throws IOException {
        this.directory= FSDirectory.open(FileSystems.getDefault().getPath("F:\\wikiIndex"));
    }
    /**
     * 文件存储到file_path目录
     **/
    public String upload(MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new Exception("Uploading file failed");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
//      String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 设置文件存储路径

        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+"/file/";

        assert fileName != null;
        File dest = new File(filePath, fileName);
        // 检测是否存在目录
        logger.info("file directory:"+dest.getParentFile());
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
        file.transferTo(dest);// 文件写入
        return filePath+ fileName;
    }

    public int getPages(String filePath) throws IOException {
        PDDocument doc = PDDocument.load(new File(filePath));
        //PDDocument doc = PDDocument.load(filePath);
        logger.info(String.valueOf(doc.getNumberOfPages()));
        int pages=doc.getNumberOfPages();
        doc.close();
        return pages;
    }

    public void insertLegalDoc(LegalDoc record)
    {
        legalDocMapper.insert(record);
    }


    private void insertPage(Page page)
    {
        pageMapper.insert(page);
    }

    /**
     * 读取PDF文件内容到txt文件
     */
    public Page readPdfToTxt(String pdfPath,String txtfilePath,long id,int n) throws IOException {
        // 读取pdf所使用的输出流
        Page page=new Page();
        page.setBookId(id);
        PrintWriter writer = new PrintWriter(new FileOutputStream(txtfilePath));
        PdfReader reader = new PdfReader(pdfPath);
        int num = reader.getNumberOfPages();// 获得页数
        //System.out.println(num);
        String content = ""; // 存放读取出的文档内容
        for (int i = 1; i <= num; i++) {
            page.setOrderNum(n);
            content += PdfTextExtractor.getTextFromPage(reader, i);
//            page.setPageContent(content);
            insertPage(page);
        }
        writer.write(content);// 写入文件内容
        writer.flush();
        writer.close();
        reader.close();
        //删除中间pdf文件
        System.gc();
        File pdfTmp=new File(pdfPath);
        pdfTmp.delete();
        return page;
    }

    /**
     * 增强文本
     **/
    public void enrichSection(List<Section> sectionList) throws IOException {
        //获取待增强文本内容
        String[] queryText=new String[sectionList.size()];
        for(int i=0;i<sectionList.size();i++){
            queryText[i]=sectionList.get(i).getSectionContent();
//            logger.info("段落"+i+"内容："+queryText[i]);
        }
        //获取QA内容
        Map<Integer,Long> answerIdMap=new HashMap<>();//存数组索引和对应的answerId关系，因为算法只返回数组索引对应的rank
        List<QA> qaList= getAllQA();//存所有QA内容,主键为answerId(answer:question,多：1）
        String[] docText=new String[qaList.size()];
        for(int i=0;i<qaList.size();i++){
            QA currQA=qaList.get(i);
            answerIdMap.put(i,currQA.getAnswerId());
            String question=currQA.getQuestion();
            String answer=currQA.getAnswer();
            docText[i]=question+" "+answer;
//            logger.info("QA"+i+"内容："+docText[i]);
        }
        //调用增强算法
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<queryText.length;i++){
            command.add("\""+queryText[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<docText.length;i++){
            command.add("\""+docText[i]+"\"");
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        String sysPath = System.getProperty("user.dir");
        File dir = new File("/home/lf/桌面/qa_project/");
        builder.directory(dir);
        Process proc = builder.start();

        BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
        String result = in.readLine();
        in.close();
        if (result==null||result.length()==0){
            logger.info("no available result");
            return;
        }
        logger.info("文本增强结果"+result);
        //store enrich result

        List<List<Integer>> resultLists;
        resultLists = JSON.parseObject(result,new TypeReference<List<List<Integer>>>(){});
        System.out.println(resultLists);
        List<QASection> qaSectionList=new ArrayList<>();
//        Long qaSectionId=getLastQASectionId();
//        logger.info("last qa_section id is:"+qaSectionId);
        for(int i=0;i<resultLists.size();i++){
            List<Integer> list=resultLists.get(i);
            //获取当前section
            Section section=sectionList.get(i);
            //遍历rank后的QA
            for (int j=0;j<list.size();j++) {
                Integer num=list.get(j);//获取QA数组索引
                Long answerId=answerIdMap.get(num);//根据数组索引获得对应的answerId
//                logger.info("num:"+num+" answerId:"+answerId);
                //根据answerId取当前rank所属QA
                QA currQA = new QA();
                for(QA qa:qaList){
                    if(qa.getAnswerId().equals(answerId)){
                        currQA=qa;
//                        logger.info("answerId:"+qa.getAnswerId()+" questionId:"+ qa.getQuestionId());
                    }
                }
                //更新数据到QASection
                QASection qaSection=new QASection();
//                qaSection.setId(++qaSectionId);
                qaSection.setQuestionId(currQA.getQuestionId());
                qaSection.setAnswerId(currQA.getAnswerId());
                qaSection.setSectionId(section.getId());
                qaSection.setRankScore(j+1);//QA的权重
                qaSectionList.add(qaSection);
            }
        }
        insertQASection(qaSectionList);
    }

//    private Long getLastQASectionId() {
//        Long qaSectionId=qaMapper.getLastQASectionId();
//        if(qaSectionId==null){
//            qaSectionId=1L;
//        }
//        return qaSectionId;
//    }

    //将文本增强结果插入数据库
    private void insertQASection(List<QASection> qaSectionList) {
        qaMapper.insertQASection(qaSectionList);
    }

    private List<QA> getAllQA() {
        return qaMapper.getAllQA();
    }

    private void insertSection(Section section)
    {
        sectionMapper.insert(section);
    }

    //cut file
    public List<Section> cut(String filePath, Page page){
        ArrayList<String> res = new ArrayList<>();// 段落切分结果
        StringBuilder sb = new StringBuilder();// 拼接读取的内容(while循环中不到断尾时,将字符一个个加入拼接)
        String temp = null;// 临时变量，存储sb去除空格的内容
        File txtFile=new File(filePath);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            int ch = 0;
            int i=0;
            String all="";
            while ((ch = reader.read()) != -1) {//注意文本最后一段末尾不是'\r'。所以ch=-1时，最后一段sb已经拼接的temp还没有存入res.
                temp = sb.toString().trim().replaceAll("[\\r\\n]", "");//去除回车换行
                all+=(char)ch;
                //String s=String.valueOf(ch);
                if((char)ch ==' ') {
                    i++;
                    if(i==10) {
                        res.add(temp);
                        sb.delete(0, sb.length());//清空sb
                        temp = null;
                        i=0;
                    }
                    else{
                        sb.append((char) ch);
                    }
                } else {
                    // 说明没到段落结尾，将结果暂存
                    sb.append((char) ch);
                }
            }
// 最后一段如果非空， 将最后一段加入，否则不处理
            if (!"".equals(temp)) {
                res.add(temp);
            }
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Iterator<String> iterator = res.iterator();
        int i=0;
        List<Section> sectionList=new ArrayList<>(); //用于文本增强
        while (iterator.hasNext()) {
            Section section=new Section();
            i++;
            String next = iterator.next();
            section.setPageId(page.getId());
            section.setSectionContent(next);
            section.setOrderNum(i);
            insertSection(section);
            sectionList.add(section);
//            System.out.println("段落开始：");
//            System.out.println(next);
        }
        logger.info("The number of paragraphs is:" + res.size());
        //删除中间txt文件
        txtFile.delete();
        return sectionList;
    }

    public int[] split(String myPath, int page, MultipartFile file) throws Exception
    {
        int[] a=new int[page+1];
        for(int i=0;i<page+1;i++)
        {
            a[i]=i+1;
        }
        String password = "";
        String split = "1";
        Splitter splitter = new Splitter();
        String pdfFile = myPath+file.getOriginalFilename();

        org.pdfbox.pdmodel.PDDocument document = null;
        List documents = null;
        try (InputStream input = new FileInputStream(pdfFile)) {
            document = parseDocument(input);
            //文件解密
            if (document.isEncrypted()) {
                try {
                    document.decrypt(password);
                } catch (Exception e) {
                    //they didn't suppply a password and the default of "" was wrong.
                    System.err.println("Error: The document is encrypted.");
                    usage();
                }
            }

            splitter.setSplitAtPage(Integer.parseInt(split));
            documents = splitter.split(document);

            for (int i = 0; a[i] < documents.size() || a[i] == documents.size() && documents.size() < a.length; i++) {
                org.pdfbox.pdmodel.PDDocument doc = (org.pdfbox.pdmodel.PDDocument) documents.get(a[i] - 1);
                //String fileName = pdfFile.substring(0, pdfFile.length()-4 ) + "-" + a[i] + ".pdf";
                String pdfFilePath = myPath + a[i] + ".pdf";
                writeDocument(doc, pdfFilePath);
                doc.close();
            }
        } finally {
            for (int i = 0; documents != null && i < documents.size(); i++) {
                org.pdfbox.pdmodel.PDDocument doc = (org.pdfbox.pdmodel.PDDocument) documents.get(i);
                doc.close();
            }
        }
        document.close();
        return a;
    }



    private void writeDocument(org.pdfbox.pdmodel.PDDocument doc, String fileName ) throws IOException
    {
        COSWriter writer = null;
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            writer = new COSWriter(output);
            writer.write(doc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * This will parse a document.
     *
     * @param input The input stream for the document.
     *
     * @return The document.
     *
     * @throws IOException If there is an error parsing the document.
     */
    private org.pdfbox.pdmodel.PDDocument parseDocument(InputStream input )throws IOException
    {
        PDFParser parser = new PDFParser( input );
        parser.parse();
        return parser.getPDDocument();
    }

    /**
     * This will print the usage requirements and exit.
     */
    private void usage()
    {
        System.err.println( "Usage: java org.pdfbox.PDFSplit [OPTIONS] <PDF file>\n" +
                "  -password  <password>        Password to decrypt document\n" +
                "  -split     <integer>         split after this many pages\n" +
                "  <PDF file>                   The PDF document to use\n"
        );
        System.exit( 1 );
    }

    public Long getLastBookId() {
        Long bookId=legalDocMapper.getLastBookId();
        if(bookId==null){
            bookId=1L;
        }
        return bookId;
    }

    public void insertPersonalLegalDoc(PersonalLegaldocStack personalLegaldocStack) {
        legalDocMapper.insertPersonalLegalDoc(personalLegaldocStack);
    }

    public void setLegalDocEnriched(Long bookId) {
        legalDocMapper.setLegalDocEnriched(bookId);
    }
    
    //利用首页生成pdf封面
    public String createCoverImg(String filePath) throws IOException {
        String outputPrefix = filePath.substring( 0, filePath.lastIndexOf( '.' ));
        String imageFormat = "png";
        String outPath = outputPrefix  + "." + imageFormat;
        // 利用PdfBox生成图像
        PDDocument pdDocument = PDDocument.load(new File(filePath));
        System.out.println(pdDocument.getNumberOfPages());
        PDFRenderer renderer = new PDFRenderer(pdDocument);

        // 构造图片
        BufferedImage img_temp = renderer.renderImageWithDPI(0 , 300, ImageType.RGB);
        // 设置图片格式
        Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("png");
        // 将文件写出
        ImageWriter writer = (ImageWriter) it.next();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(new FileOutputStream(outPath));
        writer.setOutput(imageOut);
        writer.write(new IIOImage(img_temp, null, null));
        img_temp.flush();
        imageOut.flush();
        imageOut.close();
        //Warning: You did not close a PDF Document
        pdDocument.close();
        return "/uploadFile/"+filePath.substring(filePath.lastIndexOf( '/' )+1,filePath.lastIndexOf('.'))+".png";
    }

    //wiki keyword匹配预处理
    public void mathchWikiCorpus(String txtFilePath, Long bookId, Long pageId) throws IOException {
        //读取TXT内容
        InputStreamReader read = null;//考虑到编码格式
        String encoding = "utf-8";
        try {
            read = new InputStreamReader(new FileInputStream(txtFilePath), encoding);  //输入流
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        assert read != null;
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt;
        StringBuilder content=new StringBuilder();
        while ((lineTxt = bufferedReader.readLine()) != null) {
            content.append(lineTxt).append("\n");
        }
//        logger.info(content.toString());
        //调用python分词
        List<String> res=new ArrayList<>();
        String sysPath = System.getProperty("user.dir");
        String myPath = sysPath+"\\file\\legal_doc_process.py";
        String splitRes=null;
        try {
            String[] args = new String[] { "python", myPath, content.toString()};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            splitRes = in.readLine();
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        //处理分词结果
        List<String> splitList;
        splitList = JSON.parseObject(splitRes,new TypeReference<List<String>>(){});
        //取出重复单词
        assert splitList != null;
        HashSet<String> splitSet = new HashSet<>(splitList);
        List<Keyword> keywordList=new ArrayList<>();

        int matchCount=1;
        Long lastKeywordId=keywordMapper.getLastId();
        if(lastKeywordId==null){
            lastKeywordId=1L;
        }
        for(String keyword:splitSet){
            //与数据库wiki_corpus匹配关键词
            WikiAnnotation wiki=wikiMapper.matchWikiCorpusByKeyword(keyword);
            //匹配结果存数据库，方便再次读取
            Keyword keyword1=new Keyword();
            keyword1.setKeyword(keyword);
            keyword1.setBookId(bookId);
            keyword1.setPageId(pageId);
            Long keywordId=lastKeywordId+matchCount;
            matchCount++;
            keyword1.setId(keywordId);
            if(wiki!=null){
//                logger.info("match title："+wiki.getTitle()+"\n"+wiki.getSummary());
                keyword1.setWikiCorpusId(wiki.getId());
                keyword1.setIsMatched(1);

            }else{
                keyword1.setIsMatched(0);
            }
            keywordList.add(keyword1);
        }
        keywordMapper.insertKeywordList(keywordList);
    }

    //TODO 调用word embedding算法，处理未匹配的keyword
    public void matchRemainingKeywordsByAlgorithm(Long bookId) throws IOException {
        List<Keyword> keywordList=keywordMapper.getUnmatchedKeywordByBookId(bookId);
        //获取待匹配keyword
        String[] queryText=new String[keywordList.size()];
        for(int i=0;i<keywordList.size();i++){
            queryText[i]=keywordList.get(i).getKeyword();
        }
        //获取待匹配wiki title
        List<WikiAnnotation> wikiList=wikiMapper.getAllWiki();
        String[] docText=new String[wikiList.size()];
        for(int i=0;i<wikiList.size();i++){
            docText[i]=wikiList.get(i).getTitle();
        }
        //调用匹配算法
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<queryText.length;i++){
            command.add("\""+queryText[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<docText.length;i++){
            command.add("\""+docText[i]+"\"");
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        String sysPath = System.getProperty("user.dir");
        File dir = new File("/home/lf/桌面/qa_project/");
        builder.directory(dir);
        Process proc = builder.start();

        BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
        String result = in.readLine();
        in.close();
        if (result==null||result.length()==0){
            logger.info("no available result");
            return;
        }
        logger.info("Wiki match result:"+result);
        //store enrich result
        List<List<Integer>> resultLists;
        resultLists = JSON.parseObject(result,new TypeReference<List<List<Integer>>>(){});
        logger.info(resultLists.toString());
    }
    //读取未匹配的keywords,利用wiki 倒排索引进行匹配
    public void matchRemainingKeywordsByTerm(Long bookId) throws IOException, ParseException {
        List<Keyword> keywordList=keywordMapper.getUnmatchedKeywordByBookId(bookId);
        for(Keyword keyword:keywordList){
            matchKeywordsByTerm(keyword);
        }
    }

    //利用倒排索引匹配keyword
    private void matchKeywordsByTerm(Keyword keyword) throws IOException, ParseException {
        String key=keyword.getKeyword();

        IndexReader indexReader = DirectoryReader.open(this.directory);
        // 创建indexsearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //指定分词技术，这里采用的语言处理模块要和创建索引的时候一致，否则检索的结果很不理想
        Analyzer analyzer = new EnglishAnalyzer();
        //创建查询query，搜索词为“空间向量”
        QueryParser parse = new QueryParser("fileContent", analyzer);
        Query query = parse.parse(key);
        // 执行查询
        // 第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query,3);

        // 遍历查询结果
        // topDocs.scoreDocs存储了document对象的id
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            // scoreDoc.doc属性就是document对象的id
            // 根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            if(scoreDoc.score>0.7){
                keyword.setWikiCorpusId(Long.parseLong(document.get("wikiId")));
                keyword.setIsMatched(1);
                logger.info(scoreDoc.toString());
                logger.info("keyword:"+key);
                logger.info("查询结果的总条数：" + topDocs.totalHits);
                logger.info("filename:"+document.get("fileName"));
                logger.info("wikiId:"+document.get("wikiId"));
                logger.info("fileContent:\n"+document.get("fileContent"));
                logger.info("-----------------------------------");
                keywordMapper.updateKeywordById(keyword);
                break;
            }
        }
        indexReader.close();
    }
}
