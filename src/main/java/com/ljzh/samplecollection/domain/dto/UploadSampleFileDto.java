package com.ljzh.samplecollection.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author : RaoRui
 * @Date : 2023/4/4 9:10
 */
@Data
public class UploadSampleFileDto {
    /**
     * 任务ID
     */
    @NotBlank
    private String taskId;
    /**
     * 上传人id
     */
    @NotBlank
    private String userId;
    /**
     * 上传的文件
     */
    @NotNull
    private MultipartFile file;
}