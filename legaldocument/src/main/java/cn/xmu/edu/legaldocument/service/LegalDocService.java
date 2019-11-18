package cn.xmu.edu.legaldocument.service;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface LegalDocService {
    LegalDoc insertLegalDoc(LegalDoc record);
    String upload(MultipartFile file);
    int getPages(String filePath);
}
