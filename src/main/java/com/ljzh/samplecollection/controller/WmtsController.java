package com.ljzh.samplecollection.controller;

import cn.hutool.core.io.FileUtil;
import com.ljzh.samplecollection.domain.vo.LayerVO;
import com.ljzh.samplecollection.framwork.dto.WmtsRequest;
import com.ljzh.samplecollection.framwork.utils.ResponseUtils;
import com.ljzh.samplecollection.framwork.vo.BaseResponse;
import com.ljzh.samplecollection.service.LayerService;
import com.ljzh.samplecollection.utils.ImgSample;
import com.ljzh.samplecollection.utils.ImgSampleOpenCV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/application/services")
@RestController
@Log4j2
@Api(tags = "wmts服务")
public class WmtsController {

    @Value("${ljzh.img_store_path}")
    private String imgStorePath;

    @Value("${ljzh.img_wmts_path}")
    private String imgWmtsPath;


    //通过注解引入配置
    @Qualifier("defaultThreadPool")
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Qualifier("cutThreadPool")
    @Autowired
    ThreadPoolTaskExecutor cutThreadPool;


    @Autowired
    private LayerService imgService;

    @GetMapping("wmts")
    public BaseResponse<Void> wmts(WmtsRequest wmtsRequest, HttpServletResponse response) throws IOException {
        int tileMatrix = wmtsRequest.getTileMatrix();
        int tileCol = wmtsRequest.getTileCol();
        int tileRow = wmtsRequest.getTileRow();
        String layer = wmtsRequest.getLayer();

        String path = imgWmtsPath + File.separator+layer+ File.separator+tileMatrix+ File.separator+tileCol+ File.separator+tileRow+".png";
        log.info(path);
        File file = new File(path);
        if(file.exists()){
            byte[] decode = FileUtil.readBytes(file);
            try(OutputStream out = response.getOutputStream()){
                out.write(decode);
            }
        }
        return ResponseUtils.getSuccessResponse();
    }

    @GetMapping("cutImg")
    @ApiModelProperty("切片")
    public BaseResponse cutImg(String imgId){

        LayerVO img = imgService.getById(Long.valueOf(imgId));
        if(img == null){
            return  ResponseUtils.getErrorResponse(10000,"数据异常");
        }
        String path = imgWmtsPath + File.separator+imgId;
        String filePath = imgStorePath +img.getPath();
        File png0 = new File(path+"/0/0/0.png");
        if(png0.exists()){
            return ResponseUtils.getSuccessResponse();
        }
        log.info("cutImg:开始切图" );
        log.info(filePath);
        long l = System.currentTimeMillis();
        try {
            ImgSample imgSample = new ImgSampleOpenCV(filePath,threadPoolTaskExecutor,cutThreadPool);
            imgSample.scaleCutToFile(6,path);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtils.getErrorResponse(10000,"数据异常");
        }
        return ResponseUtils.getSuccessResponse(System.currentTimeMillis()-l);
    }


    @GetMapping("img")
    @ApiModelProperty("切片")
    public BaseResponse<Void> img(String imgId, HttpServletResponse response)throws IOException{
        LayerVO img = imgService.getById(Long.valueOf(imgId));
        if(img == null){
            return ResponseUtils.getErrorResponse();
        }
        String filePath = imgStorePath +img.getPath();
        File file = new File(filePath);
        BufferedImage read = ImageIO.read(file);
        if(file.exists()){
            try(OutputStream out = response.getOutputStream();){
                ImageIO.write(read,"PNG",out);
            }
        }
        return ResponseUtils.getSuccessResponse();
    }

}

