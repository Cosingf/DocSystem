package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.mapper.LegalDocMapper;
import cn.xmu.edu.legaldocument.mapper.PageMapper;
import cn.xmu.edu.legaldocument.mapper.QAMapper;
import cn.xmu.edu.legaldocument.mapper.SectionMapper;
import cn.xmu.edu.legaldocument.vo.QA;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.exceptions.InvalidPasswordException;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdfwriter.COSWriter;
import org.pdfbox.util.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
        String filePath = sysPath+"\\file\\";

        assert fileName != null;
        File dest = new File(filePath, fileName);
            // 检测是否存在目录
        System.out.println("file directory:"+dest.getParentFile());
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
         *
         * @param writer
         * @param pdfPath
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
    public String enrichSection(List<Section> sectionList) throws IOException {
        //获取待增强文本内容
        String[] queryText=new String[sectionList.size()];
        for(int i=0;i<sectionList.size();i++){
            queryText[i]=sectionList.get(i).getSectionContent();
            logger.info("段落"+i+"内容："+queryText[i]);
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
            logger.info("QA"+i+"内容："+queryText[i]);
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
        String filePath = sysPath+"\\file\\";
        File dir = new File(filePath);
        builder.directory(dir);
        Process proc = builder.start();

        BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
        String result = in.readLine();
        in.close();
        logger.info("文本增强结果"+result);
        //TODO 文本增强结果处理，result格式未处理
        String[] results=result.split("], \\[");
        for(int i=0;i<results.length;i++){
            int num=0;
            for(char a:results[i].toCharArray()){
                if(Character.isDigit(a)){
                    num=num*10+ (int) a;
                }else if(){
                    num=0;
                }else{
                    continue;
                }
            }
        }
        List<List<Integer>> resultLists=new ArrayList<>();
        List<Integer> test=new ArrayList<>();
        test.add(1);
        test.add(4);
        resultLists.add(test);
        List<QASection> qaSectionList=new ArrayList<>();
        for(int i=0;i<resultLists.size();i++){
            List<Integer> list=resultLists.get(i);
            //获取当前section
            Section section=sectionList.get(i);
            //遍历rank后的QA
            for (int j=0;j<list.size();j++) {
                Integer num=list.get(j);//获取QA数组索引
                Long answerId=answerIdMap.get(num);//根据数组索引获得对应的answerId

                //根据answerId取当前rank所属QA
                QA currQA = new QA();
                for(QA qa:qaList){
                    if(qa.getAnswerId().equals(answerId)){
                        currQA=qa;
                    }
                }
                //更新数据到QASection
                QASection qaSection=new QASection();
                qaSection.setQuestionId(currQA.getQuestionId());
                qaSection.setAnswerId(currQA.getAnswerId());
                qaSection.setSectionId(section.getId());
                qaSection.setRank(j+1);//QA的权重
                qaSectionList.add(qaSection);
            }
        }
        insertQASection(qaSectionList);
        return result;
    }
    //将文本增强结果插入数据库
    private void insertQASection(List<QASection> qaSectionList) {
        qaMapper.insertQASection(qaSectionList);
    }

    private List<QA> getAllQA() {
        return qaMapper.getAllQA();
    }

    public String rank_idx(String[] query_text,String[] doc_text) throws IOException {
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<query_text.length;i++){
            command.add("\""+query_text[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<doc_text.length;i++){
            command.add("\""+doc_text[i]+"\"");
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+"\\file\\";
        File dir = new File(filePath);
        builder.directory(dir);
        Process proc = builder.start();

        BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
        String result = in.readLine();
        in.close();

        return result;
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
        Section section=new Section();
        while (iterator.hasNext()) {
            i++;
            String next = iterator.next();
            section.setPageId(page.getId());
            section.setSectionContent(next);
            section.setOrderNum(i);
            insertSection(section);
            sectionList.add(section);
//                System.out.println("段落开始：");
//                System.out.println(next);
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
                } catch (InvalidPasswordException e) {
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



    private void writeDocument(org.pdfbox.pdmodel.PDDocument doc, String fileName ) throws IOException, COSVisitorException
    {
        COSWriter writer = null;
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            writer = new COSWriter(output);
            writer.write(doc);
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
}
