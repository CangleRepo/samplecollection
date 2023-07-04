package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.Sample;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.domain.dto.SampleStatusUpdateDTO;
import com.ljzh.samplecollection.domain.vo.LayerWithTaskLayerIdVO;
import com.ljzh.samplecollection.domain.vo.SampleVO;
import com.ljzh.samplecollection.domain.dto.TaskLayerStatusUpdateDTO;
import com.ljzh.samplecollection.domain.vo.TaskLayerVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.SampleService;
import com.ljzh.samplecollection.service.TaskLayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taskLayer")
@Api(tags = "任务图片数据相关接口")
public class TaskLayerController {
    @Autowired
    private SampleService sampleService;

    @Autowired
    private TaskLayerService taskLayerService;


    @GetMapping("/page")
    @ApiOperation("根据任务Id和分配状态分页查询图层名")
    public BaseResponse<Page<LayerWithTaskLayerIdVO>> findLayersByTaskIdAndAssignStatusPage(@RequestParam(defaultValue = "1") int pageNum,
                                                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                                                            @RequestParam Integer assignStatus,
                                                                                            @RequestParam Long taskId) {
        return ResponseUtils.getSuccessResponse(taskLayerService.findLayersByTaskIdAndAssignStatusPage(pageNum, pageSize, assignStatus, taskId));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据任务图片ID获取任务图片")
    public TaskLayerVO getTaskLayerById(@PathVariable Long id) {
        TaskLayer taskLayer = taskLayerService.getById(id);

        return TaskLayerVO.from(taskLayer);
    }

    @GetMapping("/{id}/samples")
    @ApiOperation("根据任务图片ID获取任务样本详情")
    public BaseResponse<List<SampleVO>> getSamplesByTaskLayerId(@PathVariable Long id) {
        TaskLayer taskLayer = taskLayerService.getById(id);
        List<Sample> samples = sampleService.getSamplesByTaskLayerId(taskLayer);
        return ResponseUtils.getSuccessResponse(samples.stream().map(SampleVO::from).collect(Collectors.toList()));
    }

    @PostMapping("/{id}/samples")
    @ApiOperation("根据任务图片ID创建任务样本")
    public BaseResponse<SampleVO> createSampleByTaskLayerId(@PathVariable Long id,
                                                            @RequestBody @Validated SampleVO sampleVO) throws ParseException {
        Sample sample = sampleService.createSampleByTaskLayerId(id, sampleVO, null);
        return ResponseUtils.getSuccessResponse(SampleVO.from(sample));
    }

    @PutMapping("/samples/{sampleId}")
    @ApiOperation("根据样本ID更新样本状态{未审核： UNAUDITED,通过：ACCEPT,不通过：REJECT}")
    public BaseResponse<Void> updateSampleStatus(@PathVariable Long sampleId,
                                                 @RequestBody @Validated SampleStatusUpdateDTO sampleStatusUpdateDTO) {
        sampleService.updateSampleStatus(sampleId, sampleStatusUpdateDTO.getStatus());
        return ResponseUtils.getSuccessResponse();
    }

    @DeleteMapping("/samples/{sampleId}")
    @ApiOperation("根据样本ID删除样本数据")
    public BaseResponse<Void> deleteSample(@PathVariable Long sampleId) {
        sampleService.deleteSample(sampleId);
        return ResponseUtils.getSuccessResponse();
    }

    @GetMapping("/batchDeleteSamples")
    @ApiOperation("根据样本ID删除样本数据")
    public BaseResponse<Void> batchDeleteSamples(@RequestParam("ids") List<String> ids) {
        for (String id : ids) {
            sampleService.deleteSample(Long.valueOf(id));
        }
        return ResponseUtils.getSuccessResponse();
    }

    @DeleteMapping("/del/{taskLayerId}")
    @ApiOperation("删除任务下面的图片")
    public BaseResponse<Boolean> deleteTaskLayer(@PathVariable Long taskLayerId) {
        return ResponseUtils.getSuccessResponse(taskLayerService.delete(taskLayerId));
    }

    @PutMapping("/taskLayer/{taskLayerId}")
    @ApiOperation("根据任务图片ID更新状态{待采集：UNCOLLECTED, 已采集:COLLECTING, 待审核: UNAUDITED, 已审核: AUDITING, 已完成: DONE}")
    public BaseResponse<Void> updateSampleStatus(@PathVariable Long taskLayerId,
                                                 @RequestBody @Validated TaskLayerStatusUpdateDTO taskLayerStatusUpdateDTO) {
        taskLayerService.updateStatus(taskLayerId, taskLayerStatusUpdateDTO.getStatus());
        return ResponseUtils.getSuccessResponse();
    }


}
