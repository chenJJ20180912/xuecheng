package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TeachplanResult extends ResponseResult {

    private Teachplan teachplan;

    public TeachplanResult(Teachplan teachplan){
        super();
        this.teachplan = teachplan;
    }
}
