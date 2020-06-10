package com.xuecheng.filesystem.controller;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    private FileSystemService fileSystemService;

    @PostMapping("/upload")
    @Override
    @ResponseBody
    public UploadFileResult upload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("businesskey") String businesskey,
            @RequestParam("filetag") String filetag,
            @RequestParam("metadata") String metadata) {
        return fileSystemService.upload(multipartFile, businesskey, filetag, metadata);
    }
}
