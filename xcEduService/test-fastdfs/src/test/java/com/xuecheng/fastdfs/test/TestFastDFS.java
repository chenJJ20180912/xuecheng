package com.xuecheng.fastdfs.test;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;


/**
 * 测试fastdfs的上传和下载
 */
public class TestFastDFS {
    @Test
    public void testUpload() throws Exception {
        // 加载配置文件
        ClientGlobal.initByProperties("fastdfs-client.properties");
        // 创建 TrackerClient 对象
        TrackerClient trackerClient = new TrackerClient();
        // 连接到 TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        // 通过TrackerClient 和TrackerServer 获取到StorageServer
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //创建一个storage存储客户端
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        // 上传一个文件
        NameValuePair[] meta_list = null;        //new    NameValuePair[0];
        String item = "C:\\soft\\PDFelement\\Skin\\PDFDefault\\file.png";
        System.out.println(storageClient1.upload_file1(item, "png", meta_list));
    }

    //查询文件
    @Test
    public void testQueryFile() throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer,
                storageServer);
        FileInfo fileInfo = storageClient.query_file_info("group1", "M00/00/00/wKhjZF7WY1CAYHF0AAARU94pvkY160.png");
        System.out.println(fileInfo);
    }

    //下载文件
    @Test
    public void testDownloadFile() throws Exception {
        ClientGlobal.initByProperties("fastdfs-client.properties");
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
        byte[] result =  storageClient1.download_file1("group1/M00/00/00/wKhjZF7WY1CAYHF0AAARU94pvkY160.png");
        File file = new File("d:/1.png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(result);
        fileOutputStream.close();
    }

}
