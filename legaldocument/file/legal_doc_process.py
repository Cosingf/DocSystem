# -*- coding: utf-8 -*-
import sys
import jieba
import logging
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords


def pre_process_en(corpora, low_freq_filter=False):
    """
    # 英文
    # 1、分词
    # 2、去掉停用词
    # 3、去掉标点符号
    # 4、处理为词干
    # 5、去除低频词
    a proprocess on courrses in English version
    :param corpora:
    :type [str,str,...]
    :param low_freq_filter: whether to filter low-frequence word
    :return: text
    :type [[],[],...]
    """
    # 分词
    texts_tokenized = word_tokenize(corpora)

    # 去除停用词
    texts_filtered_first = [word for word in texts_tokenized
                            if word not in stopwords.words('english')]
    #再次过滤自定义停用词
    stopwords_cn = []
    with open(r'F:\DocSystem\legaldocument\file\stopword.txt', 'r', encoding="UTF8") as reader:
        for line in reader.readlines():
            stopwords_cn.append(line.strip())

    texts_filtered_stopwords = [word for word in texts_tokenized
                                if word not in stopwords_cn]
    # 去除标点
    english_punctuations = \
        [',', '.', ':', ';', '?', '(', ')', '[', ']', '&', '!', '*', '@', '#', '$', '%','-','0','1','2','3','4','5','6','7','8','9']
    texts_filtered = [word for word in texts_filtered_stopwords if word not in english_punctuations]
    # 处理为词干
    # st = LancasterStemmer()
    # texts_stemmed = [st.stem(word) for word in texts_filtered]
    # for word in texts_stemmed:
    #     print(word)

    # 去除过低频词
    # if low_freq_filter:
    #     all_stems = sum(texts_stemmed)
    #     stems_once = set(stem for stem in set(all_stems) if all_stems.count(stem) == 1)
    #     texts = [[stem for stem in text if stem not in stems_once] for text in texts_stemmed]
    # else:
    #     texts = texts_stemmed
    return texts_filtered


# if __name__ == '__main__':
#     print(pre_process_en("I have a sister. She is younger than me. My sister has a special talent. She sings"))


if __name__ == '__main__':
    a = sys.argv[1]
    print(pre_process_en(a))
