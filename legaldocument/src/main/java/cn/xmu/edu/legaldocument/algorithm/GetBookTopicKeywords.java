package cn.xmu.edu.legaldocument.algorithm;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Null;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBookTopicKeywords {
    public static int numberOfParts = 30;//段落的数量
    public static int numberOfClusters = 5;//集群的数量

    public String[] getBookTopicKeywords(String filePath, String allContent) throws SQLException {
        //初始化向量转化器
        VectorConvertor vc = new VectorConvertor(filePath ,allContent, numberOfParts);
        ArrayList<TermFrequencyVector> tfvList = new ArrayList<TermFrequencyVector>();
        try {
            //构建段词频向量
            tfvList = vc.constructTermFrequencyVector();
        } catch (IOException e) {
            System.out.println("Unable to construct term frequency vector.");
        }
        //初始化最佳直方图
        OptimalHistogram optimalHistogram = new OptimalHistogram(numberOfClusters, tfvList);
        //发现最佳直方图
        optimalHistogram.findOptimalHistogram();
        //构建集群并获取关键词
        return optimalHistogram.getKeywordsByConstructCluster(8);
    }
}
