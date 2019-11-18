package cn.xmu.edu.legaldocument.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class CutTest {
    static File dir=new File("E:\\pic\\");//目录路径!
    public static void main(String[] args) {
        File file=new File(dir,"2.txt");//文件!
        long size=file.length();//文件大小!
        //创建随机流!
        RandomAccessFile raf1=null,raf2=null;
        byte[] bytes=new byte[1024];//缓冲区!
        try {
            raf1=new RandomAccessFile(file,"r");
            for(int i=0,len=0;i<=size/1024;i++) {
                len=raf1.read(bytes);//读入数据!
                raf2=new RandomAccessFile(new File(dir,"片段"+(i+1)+".txt"),"rw");
                raf2.write(bytes, 0, len);//写出数据!
                raf2.close();//关闭!
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }finally {//关流!
            if(raf1!=null) {
                try {
                    raf1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(raf2!=null) {
                try {
                    raf2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
