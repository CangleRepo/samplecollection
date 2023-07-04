package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.SampleTag;
import com.ljzh.samplecollection.domain.vo.SampleTagVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.SampleTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sample-tags")
@Api(tags = "样本标签相关接口")
public class SampleTagController {

    @Autowired
    private SampleTagService sampleTagService;

    @PostMapping
    @ApiOperation("新增样本标签")
    public BaseResponse<SampleTagVO> createSampleTag(@RequestBody SampleTagVO sampleTagVO) {
        SampleTag sampleTag = new SampleTag();
        sampleTag.setName(sampleTagVO.getName());
        sampleTag.setStyle(sampleTagVO.getStyle() != null ? sampleTagVO.getStyle().toString() : null);
        sampleTag.setDescription(sampleTagVO.getDescription());
        sampleTag.setIcon(sampleTagVO.getIcon());
        sampleTagService.addSampleTag(sampleTag);
        SampleTagVO result = new SampleTagVO(sampleTag);
        return ResponseUtils.getSuccessResponse(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据标签ID获取样本标签详情")
    public BaseResponse<SampleTagVO> getSampleTag(@PathVariable Long id) {
        SampleTag sampleTag = sampleTagService.getSampleTagById(id);
        if (sampleTag == null) {
            return ResponseUtils.getErrorResponse();
        }
        SampleTagVO result = new SampleTagVO(sampleTag);
        return ResponseUtils.getSuccessResponse(result);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据标签ID更新样本标签")
    public BaseResponse<Void> updateSampleTag(@PathVariable Long id, @RequestBody SampleTagVO sampleTagVO) {
        SampleTag sampleTag = new SampleTag();
        sampleTag.setId(id);
        sampleTag.setName(sampleTagVO.getName());
        sampleTag.setStyle(sampleTagVO.getStyle() != null ? sampleTagVO.getStyle().toString() : null);
        sampleTag.setDescription(sampleTagVO.getDescription());
        sampleTag.setIcon(sampleTagVO.getIcon());
        boolean isSuccess = sampleTagService.updateSampleTag(sampleTag);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据标签ID删除样本标签")
    public BaseResponse<Void> deleteSampleTag(@PathVariable Long id) {
        boolean isSuccess = sampleTagService.deleteSampleTagById(id);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }

    @GetMapping("/batchDeleteSampleTags")
    @ApiOperation("批量删除样本标签")
    public BaseResponse<Void> batchDeleteSampleTags(@RequestParam List<String> ids){
        for (String id : ids) {
            sampleTagService.deleteSampleTagById(Long.valueOf(id));
        }
        return  ResponseUtils.getSuccessResponse();
    }

    @GetMapping
    @ApiOperation("获取所有样本标签")
    public BaseResponse<List<SampleTagVO>> getAllSampleTags() {
        List<SampleTagVO> result = sampleTagService.getAllSampleTags();
        return ResponseUtils.getSuccessResponse(result);
    }
}

