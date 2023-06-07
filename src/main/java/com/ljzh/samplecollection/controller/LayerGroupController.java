package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.dto.CreateLayerGroupDto;
import com.ljzh.samplecollection.domain.dto.ImgGroupQueryPageDto;
import com.ljzh.samplecollection.domain.entity.LayerGroup;
import com.ljzh.samplecollection.domain.vo.*;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.LayerGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/layer-groups")
@Api(tags = "图片组相关接口")
public class LayerGroupController {
    @Autowired
    private LayerGroupService layerGroupService;

    @GetMapping("/{id}")
    @ApiOperation("根据图片组ID查询图片组信息")
    public BaseResponse<LayerGroupVO> getLayerGroup(@PathVariable Long id) {
        LayerGroup layerGroup = layerGroupService.getLayerGroupById(id);
        if (layerGroup == null) {
            return ResponseUtils.getErrorResponse();
        }
        LayerGroupVO result = new LayerGroupVO(layerGroup);
        return ResponseUtils.getSuccessResponse(result);
    }

    @PostMapping("/queryImgGroupPage")
    @ApiOperation("按照图片组名称和创建时间分页查询图片组列表,支持无参数")
    public BaseResponse<LayerGroupPageVO> queryImgGroupPage(@RequestBody ImgGroupQueryPageDto dto) {
        return ResponseUtils.getSuccessResponse(layerGroupService.queryImgGroupPage(dto));
    }

    @PostMapping
    @ApiOperation("创建一个图片组")
    public BaseResponse<LayerGroupVO> createLayerGroup(@RequestBody CreateLayerGroupDto dto) {
        LayerGroup layerGroup = new LayerGroup();
        layerGroup.setName(dto.getName());
        layerGroup.setDescription(dto.getDescription());
        layerGroup.setRegionId(dto.getRegionId());
        layerGroup = layerGroupService.createLayerGroup(layerGroup);
        LayerGroupVO result = new LayerGroupVO(layerGroup);
        return ResponseUtils.getSuccessResponse(result);
    }

    @PostMapping("/upload")
    @ApiOperation("上传一组图层文件（不带样本）")
    public BaseResponse<Boolean> upload(@RequestParam("groupId") Long groupId, @RequestParam("file") MultipartFile file){
        return ResponseUtils.getSuccessResponse(layerGroupService.uploadLayers(file,groupId));
    }

    @PutMapping("/{id}")
    @ApiOperation("根据图片组ID更新图片组")
    public BaseResponse<Void> updateLayerGroup(@PathVariable Long id, @RequestBody LayerGroupVO layerGroupVO) {
        LayerGroup layerGroup = new LayerGroup();
        layerGroup.setId(id);
        layerGroup.setName(layerGroupVO.getName());
        layerGroup.setDescription(layerGroupVO.getDescription());
        layerGroup.setRegionId(layerGroupVO.getRegionId());
        boolean isSuccess = layerGroupService.updateLayerGroup(layerGroup);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }

    @GetMapping("/{id}/layers")
    @ApiOperation("根据图片组ID查询组中所有图片信息")
    public BaseResponse<List<LayerVO>> getLayersByLayerGroupId(@PathVariable Long id) {
        List<LayerVO> layers = layerGroupService.getLayerVOsByLayerGroupId(id);
        return ResponseUtils.getSuccessResponse(layers);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据图片组ID删除图片组及组中图片")
    public BaseResponse<Void> deleteLayerGroup(@PathVariable Long id) {
        boolean isSuccess = layerGroupService.deleteLayerGroup(id);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }
}

