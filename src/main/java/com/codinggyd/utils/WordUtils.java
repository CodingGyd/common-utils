package com.codinggyd.utils;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WordUtils
 * @Description Word文档内容读取工具（doc/docx）
 * @Author guoyading
 * @Date 2023/12/14 14:15
 * @Version 1.0
 */
public class WordUtils {
    //读取word文档中，doc后缀的文件
    public static List<String> searchWordDoc(String fileUrl){
        List<String> docList = new ArrayList<String>();
        String content=null;
        //读取字节流，读取文件路径
        InputStream input = null;
        try {
            input = new FileInputStream(new File(fileUrl));
            WordExtractor wex = new WordExtractor(input);
            content = wex.getText();
            docList.add(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return docList;
    }

    public static List<String> searchWordDocX(String fileUrl){
        //读取文件路径
        OPCPackage opcPackage = null;
        String content = null;
        List<String> docxList = new ArrayList<String>();
        try {
            opcPackage = POIXMLDocument.openPackage(fileUrl);
            XWPFDocument xwpf = new XWPFDocument(opcPackage);
            POIXMLTextExtractor poiText = new XWPFWordExtractor(xwpf);
            content = poiText.getText();
            docxList.add(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return docxList;
    }


}
