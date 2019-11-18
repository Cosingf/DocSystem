package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.entity.Page;
import cn.xmu.edu.legaldocument.entity.Section;
import cn.xmu.edu.legaldocument.mapper.LegalDocMapper;
import cn.xmu.edu.legaldocument.mapper.PageMapper;
import cn.xmu.edu.legaldocument.mapper.SectionMapper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class PdfService {

    @Autowired
    public LegalDocMapper legalDocMapper;
    @Autowired
    public PageMapper pageMapper;
    @Autowired
    public SectionMapper sectionMapper;

    public String upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            //  log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            String filePath = "F:\\";
            String path = fileName;

            File dest = new File(filePath,path);
            // 检测是否存在目录
            System.out.println("文件目录："+dest.getParentFile());
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return filePath+path;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";

    }

    public int getPages(String filePath)
    {
        try{
            PDDocument doc = PDDocument.load(filePath);
            System.out.println(doc.getNumberOfPages());
            return doc.getNumberOfPages();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public LegalDoc insertLegalDoc(LegalDoc record)
    {
        legalDocMapper.insert(record);
        return record;
    }


    public Page insertPage(Page page)
    {
        pageMapper.insert(page);
        return page;
    }
    /**
         * 读取PDF文件内容到txt文件
         *
         * @param writer
         * @param pdfPath
         */
    public Page readPdfToTxt(String pdfPath,String txtfilePath,long id,int n) {
// 读取pdf所使用的输出流
        Page page=new Page();
        page.setBookId(id);
        PrintWriter writer = null;
        PdfReader reader = null;
        try {
            writer = new PrintWriter(new FileOutputStream(txtfilePath));
            reader = new PdfReader(pdfPath);
            int num = reader.getNumberOfPages();// 获得页数
            //System.out.println(num);
            String content = ""; // 存放读取出的文档内容
            for (int i = 1; i <= num; i++) {
                page.setOrderNum(n);
                content += PdfTextExtractor.getTextFromPage(reader, i);
                insertPage(page);
            }
            String[] arr = content.split("/n");
            for(int i=0;i<arr.length;i++){

            }
            writer.write(content);// 写入文件内容
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }
    /**
         * 读取pdf内容
         * @param pdfPath
         */
    public String readPdfToTxt(String pdfPath) {
        PdfReader reader = null;
        StringBuffer buff = new StringBuffer();
        try {
            reader = new PdfReader(pdfPath);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            int num = reader.getNumberOfPages();// 获得页数
            TextExtractionStrategy strategy;
            for (int i = 1; i <= num; i++) {
                strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                buff.append(strategy.getResultantText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buff.toString();
    }


    public Section insertSection(Section section)
    {
        sectionMapper.insert(section);
        return section;
    }
    Section section=new Section();
    public void cut(String filePath, Page page){
        String filepath=filePath;
        ArrayList<String> res = new ArrayList<>();// 段落切分结果
        StringBuilder sb = new StringBuilder();// 拼接读取的内容(while循环中不到断尾时,将字符一个个加入拼接)
        String temp = null;// 临时变量，存储sb去除空格的内容
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
            int ch = 0;
            int i=0;
            String all="";
            while ((ch = reader.read()) != -1) {//注意文本最后一段末尾不是'\r'。所以ch=-1时，最后一段sb已经拼接的temp还没有存入res.
                temp = sb.toString().trim().replaceAll("[\\r\\n]", "");//去除回车换行
                all+=(char)ch;
                //String s=String.valueOf(ch);
                if((char)ch =='.') {
                    i++;
                    if(i==5) {
                        res.add(temp);
                        sb.delete(0, sb.length());//清空sb
                        temp = null;
                        i=0;
                    }
                } else {
                    // 说明没到段落结尾，将结果暂存
                    sb.append((char) ch);
                }
            }
            if (reader.read() == -1) {//注意文本最后一段末尾没有'\r'。所以ch=-1时，最后一段temp还没有存入res.
            }
            // 最后一段如果非空， 将最后一段加入，否则不处理
            if (!"".equals(temp)) {
                res.add(temp);
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        Iterator<String> iterator = res.iterator();
        int i=0;
        while (iterator.hasNext()) {
            i++;
            String next = iterator.next();
            section.setPageId(page.getId());
            section.setSectionContent(next);
            section.setOrderNum(i);
            insertSection(section);
//                System.out.println("段落开始：");
//                System.out.println(next);
        }
        System.out.println("段落的个数是：" + res.size());
    }
}
