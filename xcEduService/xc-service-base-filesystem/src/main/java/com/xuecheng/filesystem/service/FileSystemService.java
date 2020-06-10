package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSONObject;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemService {
    // 初始化fastdfs资源文件的开关
    private static boolean initConfig = true;
    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;


    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param businesskey   业务键
     * @param filetag       文件标签
     * @param metadata      元数据(json)
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String businesskey, String filetag, String metadata) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        // 上传文件
        String fileId = "";
        try {
            fileId = upload_fastdfs(multipartFile,fileType);
        } catch (Exception e) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        // 构建数据
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(fileType);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        if(StringUtils.isNotEmpty(metadata)) {
            try {
                fileSystem.setMetadata(JSONObject.parseObject(metadata, Map.class));
            } catch (Exception e) {
                ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_METAERROR);
            }
        }
        fileSystem = fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }


    /**
     * 上传文件到fastdfs
     *
     * @param multipartFile
     * @return 文件id
     */
    public String upload_fastdfs(MultipartFile multipartFile,String fileType) throws IOException, MyException {
        // 初始化资源文件
        initFastDFSConfig();
        // 获取trackerClient
        TrackerClient trackerClient = new TrackerClient();
        // 获取连接
        TrackerServer trackerServer = trackerClient.getConnection();
        // 获取StorageServer
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        // 获取到最终上传的storageClient
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        return storageClient1.upload_appender_file1(multipartFile.getBytes(), fileType, null);
    }

    /**
     * 初始化资源文件
     *
     * @throws IOException
     * @throws MyException
     */
    public void initFastDFSConfig() throws IOException, MyException {
        if (initConfig) {
            synchronized (FileSystemService.class) {
                if (initConfig) {
                    ClientGlobal.initByTrackers(tracker_servers);
                    ClientGlobal.setG_charset(charset);
                    ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
                    ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
                    initConfig = false;
                }
            }
        }
    }
}
