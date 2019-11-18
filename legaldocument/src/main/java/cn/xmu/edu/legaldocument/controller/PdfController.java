package cn.xmu.edu.legaldocument.controller;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.entity.Page;
import cn.xmu.edu.legaldocument.service.PdfService;
import cn.xmu.edu.legaldocument.service.serviceImpl.PDFSplit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@RestController
public class PdfController {

    @Autowired
    PdfService pdfService;
    @Autowired
    public PDFSplit pdfSplit;


    //private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(value = "/upload/{userId}",method = RequestMethod.POST)
    public LegalDoc upload(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,@PathVariable("userId")BigInteger userId,
                           @RequestParam("file") MultipartFile file, @RequestParam("author")String author) throws Exception{
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Byte a=1;
        Byte b=0;
        LegalDoc legalDoc=new LegalDoc();
        String path=pdfService.upload(file);
//        System.out.println("文件路径："+path);

        int page=pdfService.getPages(path);
        int[] arg=new int[page+1];
        for(int i=0;i<page+1;i++)
        {
            arg[i]=i+1;
        }
        legalDoc.setAuthor(author);
        legalDoc.setName(file.getOriginalFilename());
        legalDoc.setPath(path);
        legalDoc.setIsEnrich(a);
        pdfService.insertLegalDoc(legalDoc);
        pdfSplit=new PDFSplit(arg,file);
//        System.out.println(legalDoc.getId());


        for(int i=0;i<arg.length-1;i++)
        {
            String   myPath="F:/";
            String fileName = myPath+arg[i]+".pdf";
            String txtfilePath = myPath+arg[i]+".txt";
            Page page1=new Page();
            page1=pdfService.readPdfToTxt(fileName,txtfilePath,legalDoc.getId(),i+1);
            pdfService.cut(txtfilePath,page1);
        }
        if(legalDoc!=null)
        {
            httpServletResponse.setStatus(200);
            //httpServletResponse.getWriter().write(toJSONString(ld));
        }else{
            httpServletResponse.setStatus(404);
        }
        return legalDoc;
    }

/*
    @PostMapping("/batch")
    public String handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            String filePath = "/Users/shaoziwei/Downloads/";
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(filePath + file.getOriginalFilename())));//设置文件路径及名字
                    stream.write(bytes);// 写入
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "第 " + i + " 个文件上传失败 ==> "
                            + e.getMessage();
                }
            } else {
                return "第 " + i
                        + " 个文件上传失败因为文件为空";
            }
        }
        return "上传成功";
    }



    @GetMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "dalaoyang.jpeg";// 文件名
        if (fileName != null) {
            //设置文件路径
            File file = new File("/Users/dalaoyang/Documents/dalaoyang.jpeg");
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }
    */
}
