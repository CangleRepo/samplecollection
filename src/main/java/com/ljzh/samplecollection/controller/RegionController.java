package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.LayerGroup;
import com.ljzh.samplecollection.domain.entity.Region;
import com.ljzh.samplecollection.domain.vo.LayerGroupVO;
import com.ljzh.samplecollection.domain.vo.RegionVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/regions")
@Api(tags = "区域相关接口")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping("/{id}")
    @ApiOperation("根据地区ID获取地区详情")
    public BaseResponse<RegionVO> getRegion(@PathVariable Long id) {
        Region region = regionService.getRegionById(id);
        if (region == null) {
            return ResponseUtils.getErrorResponse();
        }
        RegionVO result = new RegionVO(region);
        return ResponseUtils.getSuccessResponse(result);
    }

    @GetMapping("/tree/{id}")
    @ApiOperation("根据地区ID获取地区树")
    public BaseResponse<RegionVO> getRegionTreeById(@PathVariable Long id) {
        RegionVO regionVO = regionService.getRegionVOWithChildren(id);
        if (regionVO == null) {
          return ResponseUtils.getErrorResponse();
        }
        return ResponseUtils.getSuccessResponse(regionVO);
    }

    @PostMapping
    @ApiOperation("添加地区")
    public BaseResponse<Region> createRegion(@RequestParam String name,
                                               @RequestParam(required = false) Long parentId,
                                               @RequestParam(required = false) String code) {
        Region region = regionService.createRegion(name, parentId, code);
        return ResponseUtils.getSuccessResponse(region);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据地区ID更新地区信息")
    public BaseResponse<Void> updateRegion(@PathVariable Long id, @RequestBody RegionVO regionVO) {
        Region region = new Region();
        region.setId(id);
        region.setName(regionVO.getName());
        region.setParentId(regionVO.getParentId());
        region.setCode(regionVO.getCode());
        boolean isSuccess = regionService.updateRegion(region);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }

    @GetMapping("/{id}/groups")
    @ApiOperation("根据地区ID获取图层组")
    public BaseResponse<List<LayerGroupVO>> getLayerGroupsByRegionId(@PathVariable Long id) {
        List<LayerGroup> layerGroups = regionService.getLayerGroupsByRegionId(id);
        List<LayerGroupVO> result = new ArrayList<>();
        for (LayerGroup layerGroup : layerGroups) {
            LayerGroupVO layerGroupVO = new LayerGroupVO(layerGroup);
            result.add(layerGroupVO);
        }
        return ResponseUtils.getSuccessResponse(result);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据地区ID删除地区")
    public BaseResponse<Void> deleteRegion(@PathVariable Long id) {
        boolean isSuccess = regionService.deleteRegion(id);
        if (isSuccess) {
            return ResponseUtils.getSuccessResponse();
        }
        return ResponseUtils.getErrorResponse();
    }
}


