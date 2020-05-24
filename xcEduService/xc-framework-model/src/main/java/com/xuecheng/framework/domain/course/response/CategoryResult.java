package com.xuecheng.framework.domain.course.response;


import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryResult extends ResponseResult {

    private Category category;

    public CategoryResult(Category category){
        super();
        this.category = category;
    }
}
