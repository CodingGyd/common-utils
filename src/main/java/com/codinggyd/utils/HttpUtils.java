//package com.codinggyd.utils;
//
//import org.apache.commons.collections4.map.MultiValueMap;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import java.io.*;
//import java.net.URLEncoder;
//import java.net.http.HttpHeaders;
//
//public class HttpUtils {
//
//    /**
//     * 使用RestTemplate调用第三方http接口，同时传递文件参数和普通参数。
//     * @param file
//     * @return
//     * @throws FileNotFoundException
//     */
//    public String postForEntity(File file) throws FileNotFoundException {
//        InputStream inputStream = new FileInputStream(file);
////       文件 -->  字节数组 -->  字节数组资源
//        byte[] bytesFile = null;
//        try{
//
//            bytesFile = new byte[inputStream.available()];
//            inputStream.read(bytesFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayResource byteArrayResource = new ByteArrayResource(bytesFile) {
//            //必须重写该方法，否则服务器MultipartRequest.getFile获取文件为空，
//            //但是return的变量名 作SubmittedFileName（可自定义），并非做接收端按键取值时的文件的键名
//            //即上传的文件具有两个名字: 键名 和 提交的文件名SubmittedFileName
//            @Override
//            public String getFilename() {
//                return "tempFile";
//            }
//        };
//
////        httpRequest body
//        MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
//
//        paramsMap.add("file", byteArrayResource);//文件参数
////       paramsMap.add("img", true);//普通参数
//        //请求头
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Content-Type", "multipart/form-data");
//
//        HttpEntity<MultiValueMap<String, Object>> request =
//                new HttpEntity<>(paramsMap, httpHeaders);
//
//        String url = "http://host:port";
//        ResponseEntity<String> response = restTemplate.postForEntity(url, paramsMap, String.class);
//        return response.toString();
//    }
//}
