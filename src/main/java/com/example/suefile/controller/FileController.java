package com.example.suefile.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @GetMapping("/download")
    public void download(@RequestParam("downloadfile") String downloadFile) throws IOException {

        // basic auth 처리, 실제 파일 서버 접속 정보
        String username = "username";
        String password = "password";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity request = new HttpEntity(headers);

        String fileUrl = "http://49.247.36.30:8090/revit/remote.php/dav/files/sooyeon/testfolder/" + downloadFile;

        URI url = URI.create(fileUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, request, byte[].class);
        byte[] buffer = response.getBody();

        String fileName = UUID.randomUUID().toString(); // 파일명 (랜덤생성)

        String ext = "." + StringUtils.getFilenameExtension(fileUrl); // 확장자 추출

        Path target = Paths.get("/Users/imsooyeon/test", fileName + ext); // 파일 저장 경로

        try {
            FileCopyUtils.copy(buffer, target.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
