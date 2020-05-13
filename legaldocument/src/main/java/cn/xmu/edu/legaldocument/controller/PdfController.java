package cn.xmu.edu.legaldocument.controller;

import cn.xmu.edu.legaldocument.entity.*;
import cn.xmu.edu.legaldocument.service.PdfService;
import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@RestController
public class PdfController {

    @Autowired
    PdfService pdfService;
    private static final Logger logger= LoggerFactory.getLogger(PdfController.class);

    //private static final Logger log = LoggerFactory.getLogger(FileController.class);

    /**
     * 用户上传pdf文件，同时实现QAlink和wiki关键词匹配预处理
     * isEnrich：文章是否增强过
     * 1:已增强
     * 0:未增强(用户上传后的初始状态)
     * isPublic：文章是否放入公有书库
     * 1：是
     * 0：否
     **/
    @RequestMapping(value = "/upload/{userId}",method = RequestMethod.POST)
    public LegalDoc upload(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
                           @PathVariable("userId")Long userId, @RequestParam("file") final MultipartFile file,
                           @RequestParam("author")String author,@RequestParam("isPublic")Integer isPublic) throws Exception{
        final LegalDoc legalDoc=new LegalDoc();
        PersonalLegaldocStack personalLegaldocStack=new PersonalLegaldocStack();
        //存储文件到服务器文件夹
        String path=pdfService.upload(file);
        final int page=pdfService.getPages(path);
        //更新总书库
        legalDoc.setAuthor(author);
        legalDoc.setName(file.getOriginalFilename());
        legalDoc.setPath(path);
        legalDoc.setIsEnrich(0);//0代表未增强
        legalDoc.setIsPublic(isPublic);
<<<<<<< HEAD
        //TODO pdf首页生成封面
        String coverImg=pdfService.createCoverImg(path);
=======
>>>>>>> cc29c7e02b982718cd98d1530024001fedf90621
        legalDoc.setCoverImg(null);
        pdfService.insertLegalDoc(legalDoc);
        //更新关系书库
        personalLegaldocStack.setUserId(userId);
        Long bookId=pdfService.getLastBookId();
        personalLegaldocStack.setBookId(bookId);
        pdfService.insertPersonalLegalDoc(personalLegaldocStack);

        //另起一个线程处理文本增强
        final long t1 = System.currentTimeMillis();
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        ListenableFuture<Boolean> booleanTask = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String sysPath = System.getProperty("user.dir");
                String myPath = sysPath+"/file/";
                //将文献分页
                int[] pages=pdfService.split(myPath,page,file);
                logger.info(String.valueOf(pages.length));

                for(int i=0;i<pages.length-1;i++)
                {
                    String pdfFilePath = myPath+pages[i]+".pdf";
                    String txtFilePath = myPath+pages[i]+".txt";
                    //将pdf转化为txt，以便分段处理
                    Page page=pdfService.readPdfToTxt(pdfFilePath,txtFilePath,legalDoc.getId(),i+1);
                    //txt分词，将关键词存入存入数据库，并和wiki_corpus进行预匹配
//                    pdfService.mathchWikiCorpus(txtFilePath,legalDoc.getId(),page.getId());
                    //按照10 words切割成多个section
                    List<Section> sectionList=pdfService.cut(txtFilePath,page);
                    //调用python编写的文本匹配算法增强段落，并存储结果
                    pdfService.enrichSection(sectionList);
                }
//                pdfService.setLegalDocEnriched(bookId);
//                pdfService.matchRemainingKeywordsByTerm(legalDoc.getId());
//                pdfService.matchRemainingKeywordsByAlgorithm(legalDoc.getId());
                return true;
            }
        });

        Futures.addCallback(booleanTask, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                System.out.println("total time："+ (System.currentTimeMillis() - t1));
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
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
            } ele {
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

