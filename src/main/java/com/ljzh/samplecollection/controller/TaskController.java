package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.entity.Task;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.domain.dto.LayerGroupDTO;
import com.ljzh.samplecollection.domain.vo.TaskAssignVO;
import com.ljzh.samplecollection.domain.dto.TaskLayerQueryDTO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Api(tags = "任务相关接口")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    @ApiOperation("新增任务")
    public BaseResponse<?> addTask(@RequestParam("name") String name,
                                   @RequestParam("description") String description) {
        Task task = taskService.addTask(name, description);
        return ResponseUtils.getSuccessResponse(task.getId());
    }

    @PostMapping("/{taskId}/task-layers")
    @ApiOperation("分配任务图片数据(多张图的图片组)")
    public BaseResponse<?> assignTaskLayerGroup(@PathVariable("taskId") Long taskId,
                                             @RequestBody LayerGroupDTO layerGroupDTO) {
        taskService.assignTaskLayerGroup(taskId, layerGroupDTO);
        return ResponseUtils.getSuccessResponse();
    }

    @PostMapping("/{taskId}/task-layer")
    @ApiOperation("分配任务图片数据(单张图)")
    public BaseResponse<Boolean> assignTaskLayer(@PathVariable("taskId") Long taskId,
                                           @RequestParam Long layerId) {
        return ResponseUtils.getSuccessResponse(taskService.assignTaskLayer(taskId, layerId));
    }

    @PostMapping("/task-assignees")
    @ApiOperation("分配任务图片给人员")
    public BaseResponse<?> assignTaskAssignee(@RequestBody TaskAssignVO taskAssignVO) {
        taskService.assignTaskAssignee(taskAssignVO);
        return ResponseUtils.getSuccessResponse();
    }

    @GetMapping("/taskList")
    @ApiOperation("查询所有任务列表")
    public BaseResponse<List<Task>> taskList() {
        return ResponseUtils.getSuccessResponse(taskService.taskList());
    }

    @GetMapping(params = {"userId", "roleId"})
    @ApiOperation("根据用户ID和角色ID查询任务列表")
    public BaseResponse<List<Task>> getTasksByUserIdAndRoleId(@RequestParam Long userId, @RequestParam Long roleId) {
        return ResponseUtils.getSuccessResponse(taskService.getTasksByUserIdAndRoleId(userId, roleId));
    }

    @GetMapping("/task-layers")
    @ApiOperation("根据用户ID、角色ID、任务ID、图片状态分页查询图片列表")
    public BaseResponse<Page<TaskLayer>> getTaskLayersByQueryDTO(TaskLayerQueryDTO queryDTO) {
        Page<TaskLayer> layers = taskService.getTaskLayersByQueryDTO(queryDTO);
        return ResponseUtils.getSuccessResponse(layers);
    }

    @GetMapping("/queryLayersByTaskId")
    @ApiOperation("根据任务ID查询任务下所有图片")
    public BaseResponse<List<TaskLayer>> queryLayersByTaskId(@RequestParam("taskId") Long taskId) {
        List<TaskLayer> layers = taskService.queryLayersByTaskId(taskId);
        return ResponseUtils.getSuccessResponse(layers);
    }

    @PostMapping("/upload")
    @ApiOperation("上传任务图片数据（带样本）")
    public BaseResponse<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("taskId") Long taskId) {
        // 调用 upload 方法处理文件上传和解析
        taskService.upload(file, taskId);
        return ResponseUtils.getSuccessResponse("上传成功！");
    }

    @GetMapping("/export")
    @ApiOperation("根据任务ID，导出尺寸width*height导出任务样本")
    public BaseResponse<Boolean> export(@RequestParam("taskId") Long taskId, @RequestParam("width") Integer width, @RequestParam("height") Integer height, HttpServletResponse response) throws IOException {
        return ResponseUtils.getSuccessResponse(taskService.export(taskId,width,height, response));
    }
}
