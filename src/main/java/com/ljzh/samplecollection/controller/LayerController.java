package com.ljzh.samplecollection.controller;

import com.ljzh.samplecollection.domain.vo.LayerVO;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.LayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/layers")
@Api(tags = "图片相关接口")
@Log4j2
public class LayerController {
    @Autowired
    private LayerService layerService;

    @Value("${ljzh.img_store_path}")
    private String imgStorePath;

    @GetMapping("/{id}")
    @ApiOperation("根据图片ID查看图片详情")
    public BaseResponse<LayerVO> getById(@PathVariable("id") Long id) {
        LayerVO layerVO = layerService.getById(id);
        if (layerVO == null) {
            return ResponseUtils.getErrorResponse();
        }
        return ResponseUtils.getSuccessResponse(layerVO);
    }

    @GetMapping("/layer-group/{layerGroupId}")
    @ApiOperation("根据图片组ID查看图片组下的所有图片信息")
    public BaseResponse<List<LayerVO>> getByLayerGroup(@PathVariable("layerGroupId") Long layerGroupId) {
        List<LayerVO> layerVOs = layerService.getByLayerGroup(layerGroupId);
        if (layerVOs.isEmpty()) {
            return ResponseUtils.getErrorResponse();
        }
        return ResponseUtils.getSuccessResponse(layerVOs);
    }

    @GetMapping
    @ApiOperation("查询所有图片信息")
    public BaseResponse<List<LayerVO>> getAll() {
        List<LayerVO> layerVOs = layerService.getAll();
        if (layerVOs.isEmpty()) {
            return ResponseUtils.getErrorResponse();
        }
        return ResponseUtils.getSuccessResponse(layerVOs);
    }

    @PostMapping
    @ApiOperation("创建一个新的图片")
    public BaseResponse<Long> create(@RequestBody LayerVO layerVO) {
        Long id = layerService.create(layerVO);
        if (id == null) {
            return ResponseUtils.getErrorResponse();
        }
        return ResponseUtils.getSuccessResponse(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据ID更新图片信息")
    public BaseResponse<Void> update(@PathVariable("id") Long id, @RequestBody LayerVO layerVO) {
        boolean result = layerService.update(id, layerVO);
        if (result) {
            return ResponseUtils.getSuccessResponse();
        } else {
            return ResponseUtils.getErrorResponse();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据ID删除一张图片")
    public BaseResponse<Void> deleteById(@PathVariable("id") Long id) {
        boolean result = layerService.delete(id);
        if (result) {
            return ResponseUtils.getSuccessResponse();
        } else {
            return ResponseUtils.getErrorResponse();
        }
    }

    @GetMapping("/getSmallLayerById")
    @ApiOperation("根据图片Id获取缩略图")
    public void getSmallLayerById(String layerId, HttpServletResponse response)throws IOException {
        LayerVO layerVO = layerService.getById(Long.valueOf(layerId));
        if(layerVO == null){
            return;
        }
        String filePath = imgStorePath+"/"+"smallLayer" +"/"+layerVO.getLayerGroupId();
        String originImgPath = imgStorePath + layerVO.getPath();
        originImgPath.replace(" ","%20");
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        String smallLayerPath =filePath+"/"+ layerVO.getName() + "-small.png";
        File smallLayerFile = new File(smallLayerPath);
        if (!smallLayerFile.exists()){
            log.info(smallLayerPath);
            Thumbnails.of(originImgPath).scale(0.05f).outputQuality(0.25f).toFile(smallLayerPath);
        }
        try(OutputStream out = response.getOutputStream()){
            BufferedImage read = ImageIO.read(smallLayerFile);
            ImageIO.write(read,"PNG",out);
        }

    }
}


