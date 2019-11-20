package cn.xmu.edu.legaldocument.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.Reference;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class CutTest {
    public static void main(String[] args) {
        String result="[[1, 0], [1, 0], [1, 0], [1, 0], [1, 0], [1, 0], [1, 0]]";
        List<List<Integer>> resultLists=new ArrayList<>();

//        String[] results=result.split(", \\[");
//        for(int i=0;i<results.length;i++){
//            int num=0;
//            List<Integer> resultList=new ArrayList<>();
//            System.out.println(results[i]);
//            for(char a:results[i].toCharArray()){
//                if(Character.isDigit(a)){
//                    num=num*10+ (int) a-(int)'0';
//                    System.out.println(a+" "+num);
//                }else if(a==',') {
//                    resultList.add(num);
//                    num = 0;
//                }else if(a==']'){
//                    resultList.add(num);
//                    break;
//                }
//            }
//            resultLists.add(resultList);
//        }

        resultLists = JSON.parseObject(result,new TypeReference<List<List<Integer>>>(){});
        System.out.println(resultLists);
    }
}
