package com.xuecheng.framework.domain.system.response;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysDictionaryResult extends ResponseResult {

    private SysDictionary sysDictionary;

    public SysDictionaryResult(SysDictionary sysDictionary){
        super();
        this.sysDictionary = sysDictionary;
    }
}
