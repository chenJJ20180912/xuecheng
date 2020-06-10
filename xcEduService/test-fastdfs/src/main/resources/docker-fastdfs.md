## 分布式文件管理系统FastDFS

### fastdfs基础知识

​	fastdfs 是一个分布式文件管理系统，它与传统的分布式文件管理系统不同，它不对文件进行分块存储(前面使用到的gridfs会对文件进行切割)，所以在小文件的存储和读取上，它的性能十分优越。

### 架构

#### tracker（追踪/调度服务器）

​	tracker的作用是负载均衡和调度，应用程序通过请求调度器来获取哪个storage为本次请求服务。



#### storage

​	storage的作用是存储。值得注意的是<font color='red'>storage并没有实现自己的文件管理系统</font>而是直接使用操作系统的文件管理系统管理文件。

​	storage集群采用了分组存储的方式，集群的总存储容量等与所有组的存储容量之和，但是<font color='red'>同一个组下面的文件是一样的，也就是主备</font>，组内的所有storage服务器是平等关系，不通组之间的storage并不会相互通讯。



<font color='green'>storage会定时向tracker 发送自己的状况，包括磁盘剩余空间、文件同步 状况、文件上传下载次数等统计信息。</font>





### 使用docker搭建fastdfs环境

    1. docker pull delron/fastdfs
        
    2. 使用docker镜像构建tracker容器（跟踪服务器，起到调度的作用）
       docker run -d --network=host --name tracker -v /home/tracker:/var/fdfs delron/fastdfs tracker
       
    3. 使用docker镜像构建storage容器（存储服务器，提供容量和备份服务）
       docker run -d --network=host --name storage -e TRACKER_SERVER=192.168.99.100:22122 -v /home/storage:/var/fdfs -e GROUP_NAME=group1 delron/fastdfs storage
       上面填写的是你的tracker服务ip地址，端口（端口默认是22122）
    
    4. 使用java客户端程序测试
    
    5. 测试Nginx 是否代理正常 通过浏览器访问文件，看是否能够显示正常
#### 注意：
    1. docker 镜像需要使用delron/fastdfs  很多教程内的镜像没有集成Nginx,导致后面需要手动集成
    2. 要先创建tracker容器 再创建storage容器，观察stroage是否启动成功执行docker logs storage,如果输出结果有很多mkdir 即启动成功
    3. storage 的配置文件 是 /etc/fdfs/storage.conf   nginx的配置文件是/usr/local/nginx/conf/nginx.conf
    4. 如果在创建的第3步中tracker指定的ip填写有误，可以通过修改storage容器中/etc/fdfs/storage.conf的tracker_server即可


​    

### java测试代码

maven 依赖

```xml
<dependency>
    <groupId>net.oschina.zcx7878</groupId>
    <artifactId>fastdfs-client-java</artifactId>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```



配置文件 fastdfs-client.properties

```properties
http连接超时时间
fastdfs.connect_timeout_in_seconds=50
#tracker与storage网络通信超时时间
fastdfs.network_timeout_in_seconds=300
#字符编码
fastdfs.charset=utf-8
#tracker服务器地址，多个地址中间用英文逗号分隔
fastdfs.tracker_servers=192.168.99.100:22122
```



测试代码

```java
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

```

#### 总结

使用客户端操作的步骤：

1. 初始化资源文件
2. new 一个trackerClient
3. 通过trackerClient获取连接对象trackerServer(这个为啥不叫connection)
4. 通过TrackerClient 和TrackerServer 获取到StorageServer
5. new一个StorageClient1(为啥要用1，可能是版本比较新，性能更好吧)
6. 通过StorageClient1中的api进行操作



​	本质就是在调用的最开始我们是不知道应该请求哪一个storage，所以我们需要先请求tracker,再通过tracker去获取到对应的storage。