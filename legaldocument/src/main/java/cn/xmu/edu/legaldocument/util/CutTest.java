package cn.xmu.edu.legaldocument.util;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


public class CutTest {
    private String pdfFile="F:\\DocSystem\\legaldocument\\file\\test.pdf";


    public String getPdfFile() {
        return pdfFile;
    }


    public class TestPDFRenderer {

//        public void Pdf_Png(int pageNumber) {
//            int pagen = pageNumber;
//            File file = new File("E:/maven.pdf");
//
//            PDFFile pdffile = null;
//            // set up the PDF reading
//            try {
//                RandomAccessFile raf = new RandomAccessFile(file, "r");
//                FileChannel channel = raf.getChannel();
//                ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
//                        channel.size());
//                pdffile = new PDFFile(buf);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //if (pagen < pdffile.getNumPages())
//            //return;
//            // print出该pdf文档的页数
//            System.out.println(pdffile.getNumPages());
//
//            // 设置将第pagen也生成png图片
//            PDFPage page = pdffile.getPage(pagen);
//
//            // create and configure a graphics object
//            int width = (int) page.getBBox().getWidth();
//            int height = (int) page.getBBox().getHeight();
//            BufferedImage img = new BufferedImage(width, height,
//                    BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2 = img.createGraphics();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                    RenderingHints.VALUE_ANTIALIAS_ON);
//
//            // do the actual drawing
//            PDFRenderer renderer = new PDFRenderer(page, g2, new Rectangle(0, 0,
//                    width, height), null, Color.WHITE);
//
//            try {
//                page.waitForFinish();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            renderer.run();
//
//            g2.dispose();
//
//            try {
//                ImageIO.write(img, "gif", new File("D:/Image.gif"));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
    }


    public static String execute() throws Exception {
        String filePath="F:\\\\DocSystem\\\\legaldocument/file/test.pdf";
        String outPath="F:\\\\DocSystem\\\\legaldocument/file/test.png";
        String outputPrefix = filePath.substring(filePath.lastIndexOf( '/' )+1,filePath.lastIndexOf('.'));
        System.out.println(outputPrefix);
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
        ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(outPath));
        writer.setOutput(imageout);
        writer.write(new IIOImage(img_temp, null, null));
        img_temp.flush();
        imageout.flush();
        imageout.close();
        //Warning: You did not close a PDF Document
        pdDocument.close();
        return null;
    }

    public static void main(String[]args) throws Exception {
        execute();
    }

}
