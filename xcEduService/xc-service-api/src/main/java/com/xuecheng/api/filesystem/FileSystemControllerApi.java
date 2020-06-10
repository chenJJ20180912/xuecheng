package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件系统接口", description = "文件系统接口")
public interface FileSystemControllerApi {

    /**
     *
     * @param multipartFile 文件
     * @param businesskey    业务键
     * @param filetag        文件标签
     * @param metadata        元数据(json)
     * @return
     */
    @ApiOperation("文件上传")
    UploadFileResult upload(MultipartFile multipartFile,
                                   String businesskey,
                                   String filetag,
                                   String metadata);
}
