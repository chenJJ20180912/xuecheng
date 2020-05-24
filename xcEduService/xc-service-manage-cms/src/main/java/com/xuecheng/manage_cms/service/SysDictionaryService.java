package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import com.xuecheng.framework.domain.system.response.SysDictionaryResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysDictionaryService {

    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getByType(String type) {
        if (StringUtils.isEmpty(type)) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType(type);
        List<SysDictionaryValue> dValue = sysDictionary.getDValue();
        if(dValue != null){
            dValue = dValue.stream().filter(value -> "1".equals(value.getSdStatus())).collect(Collectors.toList());
            sysDictionary.setDValue(dValue);
        }
        return sysDictionary;
    }

    public QueryResponseResult list(int page, int size, SysDictionary sysDictionary) {
        if (sysDictionary == null) {
            sysDictionary = new SysDictionary();
        }
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("dName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("dType", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<SysDictionary> ex = Example.of(sysDictionary, matcher);
        // 根据dtype升序查询
        Pageable pageable = PageRequest.of(page, size,new Sort(Sort.Direction.ASC,"dType"));
        Page<SysDictionary> dictionaryPage = sysDictionaryRepository.findAll(ex, pageable);
        QueryResult<SysDictionary> result = new QueryResult<>();
        result.setList(dictionaryPage.getContent());
        result.setTotal(dictionaryPage.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, result);
    }

    public SysDictionaryResult add(SysDictionary sysDictionary) {
        if (sysDictionary == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary old = sysDictionaryRepository.findByDType(sysDictionary.getDType());
        if (old != null) {
            ExceptionCast.cast(CmsCode.SYS_DICTIONARY_EXIST);
        }
        sysDictionary.setId(null);
        SysDictionary save = sysDictionaryRepository.save(sysDictionary);
        return new SysDictionaryResult(save);
    }

    public SysDictionaryResult edit(String id, SysDictionary sysDictionary) {
        Optional<SysDictionary> optional = sysDictionaryRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary db = optional.get();
        db.setDName(sysDictionary.getDName());
        SysDictionary save = sysDictionaryRepository.save(db);
        return new SysDictionaryResult(save);
    }

    public ResponseResult del(String id) {
        sysDictionaryRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    public SysDictionaryResult addValue(String id, SysDictionaryValue sysDictionaryValue) {
        Optional<SysDictionary> optional = sysDictionaryRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary db = optional.get();
        List<SysDictionaryValue> dValue = db.getDValue();
        if(dValue == null){
            dValue = new ArrayList<>();
        }
        Object[] objects = dValue.stream().filter(value -> value.getSdId().equals(sysDictionaryValue.getSdId())).toArray();
        if(objects != null && objects.length > 0){
            ExceptionCast.cast(CmsCode.SYS_DICTIONARY_EXIST);
        }
        dValue.add(sysDictionaryValue);
        db.setDValue(dValue);
        SysDictionary save = sysDictionaryRepository.save(db);
        return new SysDictionaryResult(save);
    }

    public SysDictionaryResult editValue(String id, String sdId, SysDictionaryValue sysDictionaryValue) {
        Optional<SysDictionary> optional = sysDictionaryRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary db = optional.get();
        List<SysDictionaryValue> dValue = db.getDValue();
        dValue.stream().forEach(value -> {
            if(value.getSdId().equals(sdId)){
                value.setSdName(sysDictionaryValue.getSdName());
                value.setSdStatus(sysDictionaryValue.getSdStatus());
            }
        });
        SysDictionary save = sysDictionaryRepository.save(db);
        return new SysDictionaryResult(save);
    }

    public ResponseResult delValue(String id, String sdId) {
        Optional<SysDictionary> optional = sysDictionaryRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        SysDictionary db = optional.get();
        List<SysDictionaryValue> dValue = db.getDValue();
        if(dValue == null){
            dValue = new ArrayList<>();
        }
        dValue = dValue.stream().filter(value -> !value.getSdId().equals(sdId)).collect(Collectors.toList());
        db.setDValue(dValue);
        SysDictionary save = sysDictionaryRepository.save(db);
        return new SysDictionaryResult(save);
    }
}
