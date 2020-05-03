package cn.xmu.edu.legaldocument.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class VectorConvertor {
    //矢量转换器
    private String filePath;//文件路径
    private static int numberOfPart;//分段数
    private ArrayList<String> partList;
    private ArrayList<TermFrequencyVector> tfvList;//术语频率
    private ArrayList<HashMap<String, ArrayList<Integer>>> occurrenceLocalList;//产状局部列表，记录词语在段落的哪些位置出现过
    private HashMap<String, ArrayList<Integer>> occurrenceGlobalList;//产状总体列表，记录词语在哪些段落中出现过
    private ArrayList<String> nonDuplicateWordList;//不重复词语列表
    private int numOfWordPerPart;//每一部分的词语数量
    private ArrayList<Integer> record;//记录
    private String allContent;//文献内容

    public VectorConvertor(String filePath,String allContent, int numberOfPart) {
        this.filePath = filePath;
        this.numberOfPart = numberOfPart;
        this.allContent = allContent;
        partList = new ArrayList<String>();
        tfvList = new ArrayList<TermFrequencyVector>();
        occurrenceLocalList = new ArrayList<HashMap<String, ArrayList<Integer>>>();
        occurrenceGlobalList = new HashMap<String, ArrayList<Integer>>();
        nonDuplicateWordList = new ArrayList<String>();
        record = new ArrayList<Integer>();
    }

    //构造术语频率矢量
    public ArrayList<TermFrequencyVector> constructTermFrequencyVector()
            throws IOException {
        //初始化词语列表转化器
        WordListConverter wordListConverter = new WordListConverter(filePath,allContent);
        //获取有用的信息，对文献进去顶词消除、提取词干
        String bookContent = wordListConverter.extractUsefulInformation();
        String[] wordList = bookContent.split(" ");
        //构造分段词与列表
        constructPartList(wordList);
        //创建术语词频向量
        tfvList = createTermFrequencyVector(nonDuplicateWordList);
        //让每个术语词频向量存储无重复词语
        storeNonDuplicateWordList(nonDuplicateWordList);
        return tfvList;
    }

    private void storeNonDuplicateWordList(ArrayList<String> list) {
        for (int startIndex = 0; startIndex < tfvList.size(); startIndex++) {
            tfvList.get(startIndex).setNonDuplicateWordList(list);
        }
    }

    private ArrayList<TermFrequencyVector> createTermFrequencyVector(
            ArrayList<String> list) {
        ArrayList<TermFrequencyVector> newTfvList = new ArrayList<TermFrequencyVector>();

        for (int startOuterIndex = 0; startOuterIndex < partList.size(); startOuterIndex++) {
            int countZeroCompression = 0;//压缩统计零的数量
            ArrayList<PairNumber> currentTfv = new ArrayList<PairNumber>();
            ArrayList<Float> TF_IDF_Vector = new ArrayList<Float>();
            ArrayList<Float> TF_DF_Vector = new ArrayList<Float>();
            float zero = 0;

            //遍历所有不重复的词
            for (int startInnerIndex = 0; startInnerIndex < list.size(); startInnerIndex++) {
                String currentWord = list.get(startInnerIndex);
                //获取该段落所有当前词的位置
                ArrayList<Integer> localPositionList = occurrenceLocalList.get(
                        startOuterIndex).get(currentWord);
                //当有出现的时候
                if (localPositionList != null) {
                    //获取出现频率
                    int localFrequency = localPositionList.size();
                    int numberOfPartsContainCurrentWord = 0;
                    //获取全局出现的次数
                    if (occurrenceGlobalList.get(currentWord) != null) {
                        numberOfPartsContainCurrentWord = occurrenceGlobalList
                                .get(currentWord).size();
                    }
                    //精度为float，计算出   段落次数*段落数/总出现次数
                    float tf_idf = localFrequency
                            * (float) Math.log((double) numberOfPart
                            / (double) numberOfPartsContainCurrentWord);
                    float tf_df = localFrequency;
                    float tf = localFrequency;
                    //存储 每个不重复词的在该段落的 段落次数*段落数/总出现次数
                    TF_IDF_Vector.add(tf_idf);
                    //存储 在该段落词的次数
                    TF_DF_Vector.add(tf_df);
                    currentTfv.add(new PairNumber(tf, countZeroCompression));
                    countZeroCompression = 0;
                } else { //当没有出现的时候为零
                    TF_IDF_Vector.add(zero);
                    TF_DF_Vector.add(zero);
                    countZeroCompression++;
                }
            }

            // This step is to store the last followUp 0s.
            //最后统计
            if (countZeroCompression != 0) {
                currentTfv.add(new PairNumber(0, countZeroCompression));
            }

            TermFrequencyVector newTfv = new TermFrequencyVector(currentTfv,
                    list.size());
            newTfv.setTF_IDF_Vector(TF_IDF_Vector);
            newTfv.setTF_DF_Vector(TF_DF_Vector);
            //求向量的模
            newTfv.normalizeTFIDF();
            newTfvList.add(newTfv);
        }

        return newTfvList;
    }

    private void constructPartList(String[] wordList) throws IOException {
        partList = splitContent(wordList);
    }

    private ArrayList<String> constructOccurrenceMap(String[] wordList) {
        return findOccurence(wordList);
    }

    private ArrayList<String> findOccurence(String[] wordList) {
        ArrayList<String> list = new ArrayList<String>();
        int numberOfWord = wordList.length;
        int numberOfWordsPerPart = numberOfWord / numberOfPart;
        numOfWordPerPart = numberOfWordsPerPart;

        for (int startOuterIndex = 0; startOuterIndex < numberOfPart; startOuterIndex++) {
            occurrenceLocalList.add(new HashMap<String, ArrayList<Integer>>());
            int wordPosition = 0;

            int startInnerIndex = 0;
            for (startInnerIndex = startOuterIndex * numberOfWordsPerPart; startInnerIndex < (startOuterIndex + 1)
                    * numberOfWordsPerPart; startInnerIndex++) {
                String currentWord = wordList[startInnerIndex];

                if (!list.contains(currentWord)) {
                    list.add(currentWord);
                }

                ArrayList<Integer> localPosition = new ArrayList<Integer>();
                //判断当前这分段是否记录过当前的单词，有的话得到之前的位置记录
                if (occurrenceLocalList.get(startOuterIndex).containsKey(
                        currentWord)) {
                    localPosition = occurrenceLocalList.get(startOuterIndex)
                            .get(currentWord);
                } else {
                    ArrayList<Integer> globalPartList = new ArrayList<Integer>();
                    //没有则记录进去全局列表，该段落含有该单词
                    if (occurrenceGlobalList.containsKey(currentWord)) {
                        globalPartList = occurrenceGlobalList.get(currentWord);
                    }
                    globalPartList.add(startOuterIndex);
                    occurrenceGlobalList.put(currentWord, globalPartList);
                }
                //把当前词位置更新到局部列表中
                localPosition.add(wordPosition);
                occurrenceLocalList.get(startOuterIndex).put(currentWord,
                        localPosition);
                //更新下一个词的位置的位置，+1为空格
                wordPosition += currentWord.length() + 1;
            }
            //为最后一部分的时候
            if (startOuterIndex == numberOfPart - 1) {
                while (startInnerIndex < wordList.length) {
                    String currentWord = wordList[startInnerIndex];

                    if (!list.contains(currentWord)) {
                        list.add(currentWord);
                    }

                    ArrayList<Integer> localPosition = new ArrayList<Integer>();
                    if (occurrenceLocalList.get(startOuterIndex).containsKey(currentWord)) {
                        localPosition = occurrenceLocalList.get(startOuterIndex).get(currentWord);
                    } else {
                        ArrayList<Integer> globalPartList = new ArrayList<Integer>();
                        if (occurrenceGlobalList.containsKey(currentWord)) {
                            globalPartList = occurrenceGlobalList
                                    .get(currentWord);
                        }
                        globalPartList.add(startOuterIndex);
                        occurrenceGlobalList.put(currentWord, globalPartList);
                    }
                    localPosition.add(wordPosition);
                    occurrenceLocalList.get(startOuterIndex).put(currentWord,
                            localPosition);
                    wordPosition += currentWord.length() + 1;
                    startInnerIndex++;
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    //将所有有用信息分段
    private ArrayList<String> splitContent(String[] wordList) {
        ArrayList<String> newPartList = new ArrayList<String>();
        int numberOfWord = wordList.length;
        //每段有多少词
        int numberOfWordsPerPart = numberOfWord / numberOfPart;

        for (int startOuterIndex = 0; startOuterIndex < numberOfPart; startOuterIndex++) {
            StringBuffer strBuf = new StringBuffer();
            int startInnerIndex = 0;
            for (startInnerIndex = 0; startInnerIndex < numberOfWordsPerPart; startInnerIndex++) {
                strBuf.append(wordList[startOuterIndex * numberOfWordsPerPart
                        + startInnerIndex]
                        + " ");
            }

            if (startOuterIndex == numberOfPart - 1) {
                while (startOuterIndex * numberOfWordsPerPart + startInnerIndex != wordList.length) {
                    strBuf.append(wordList[startOuterIndex
                            * numberOfWordsPerPart + startInnerIndex]
                            + " ");
                    startInnerIndex++;
                }
            }
            newPartList.add(strBuf.toString());
        }
        return newPartList;
    }

    public ArrayList<Integer> getRecord() {
        return this.record;
    }

    public int getNumberOfWordsPerPart() {
        return this.numOfWordPerPart;
    }

    public ArrayList<String> getNonDuplicateWordList() {
        return nonDuplicateWordList;
    }
}

