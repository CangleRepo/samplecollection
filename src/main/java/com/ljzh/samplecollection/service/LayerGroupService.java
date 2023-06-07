package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.Layer;
import com.ljzh.samplecollection.domain.entity.LayerGroup;
import com.ljzh.samplecollection.domain.dto.ImgGroupQueryPageDto;
import com.ljzh.samplecollection.domain.vo.LayerGroupPageVO;
import com.ljzh.samplecollection.domain.vo.LayerGroupVO;
import com.ljzh.samplecollection.domain.vo.LayerVO;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.repository.LayerGroupRepository;
import com.ljzh.samplecollection.repository.LayerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Log4j2
public class LayerGroupService {
    @Value("${ljzh.img_store_path}")
    private String imgStorePath;

    @Autowired
    private LayerGroupRepository layerGroupRepository;

    @Autowired
    private LayerRepository layerRepository;

    public LayerGroup getLayerGroupById(Long id) {
        return layerGroupRepository.findById(id).orElse(null);
    }

    public LayerGroup createLayerGroup(LayerGroup layerGroup) {
        return layerGroupRepository.save(layerGroup);
    }

    public boolean updateLayerGroup(LayerGroup layerGroup) {
        if (!layerGroupRepository.existsById(layerGroup.getId())) {
            return false;
        }
        layerGroupRepository.save(layerGroup);
        return true;
    }

    public boolean deleteLayerGroup(Long id) {
        LayerGroup layerGroup = layerGroupRepository.findById(id).orElse(null);
        if (layerGroup == null) {
            return false;
        }
        if (!layerGroup.getLayers().isEmpty()) {
            return false;
        }
        layerGroupRepository.delete(layerGroup);
        return true;
    }

    public List<LayerVO> getLayerVOsByLayerGroupId(Long layerGroupId) {
        LayerGroup layerGroup = layerGroupRepository.findById(layerGroupId).orElse(null);
        if (layerGroup == null) {
            return null;
        }
        List<LayerVO> layerVOList = new ArrayList<>();
        for (Layer layer : layerGroup.getLayers()) {
            layerVOList.add(new LayerVO(layer));
        }
        return layerVOList;
    }

    public LayerGroupVO getLayerGroupVO(Long id) {
        LayerGroup layerGroup = layerGroupRepository.findById(id).orElse(null);
        if (layerGroup == null) {
            return null;
        }
        return new LayerGroupVO(layerGroup);
    }


    public Boolean uploadLayers(MultipartFile uploadZipFile, Long groupId) {
        // 获取上传文件名、上传文件所在路径
        String originalFileName = uploadZipFile.getOriginalFilename();
        String uploadFolderPath = imgStorePath;
        File newFolder = new File(uploadFolderPath + File.separator + originalFileName.substring(0, originalFileName.lastIndexOf(".")));
        if (newFolder.exists()){
            throw new CustomException(ResponseEnum.FILE_EXIST);
        }
        String newFolderName;
        // 新建文件夹并解压文件
        try (ZipInputStream zipInputStream = new ZipInputStream(
                new BufferedInputStream(uploadZipFile.getInputStream()), Charset.forName("GBK"))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                int index = entryName.lastIndexOf("/");
                if (index != -1) {
                    // 使用压缩包内部的文件夹名称作为文件夹名称
                    newFolderName = entryName.substring(0, index);
                    new File(uploadFolderPath + File.separator + newFolderName).mkdirs();
                }
                File newFile = new File(uploadFolderPath + File.separator + entryName);

                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = zipInputStream.read(buffer)) > 0) {
                        bos.write(buffer, 0, read);
                    }
                    bos.close();
                    fos.close();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 判断新的文件夹下是否有文件，如果没有文件则删除文件夹，删除上传的压缩包，返回空文件异常，如果文件夹有文件，则删除压缩包，返回成功
        File[] files = newFolder.listFiles();
        if (files == null || files.length == 0) {
            // 如果文件夹是空的，删除文件夹及其上级目录中的所有内容（递归删除）
            deleteFolder(newFolder);
            new File(uploadZipFile.getOriginalFilename()).delete();
            throw new RuntimeException("上传的压缩包中没有文件！");
        } else {
            new File(uploadZipFile.getOriginalFilename()).delete();
            log.info("上传成功！");
        }
        createLayerGroupWithLayers(newFolder, groupId);
        return true;
    }

    /**
     * 创建新的图层组并保存到数据库，并关联指定目录下所有jpg和png文件作为其一部分图层。
     * @param newFolder 新建的文件夹对象
     * @return 由图层名和图层组成的Map
     */
    private void createLayerGroupWithLayers(File newFolder, Long groupId) {
        // 遍历文件夹，解析出图层列表
        File[] fileList = newFolder.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (File file : fileList) {
                // 处理后缀大小写敏感，如果是jpg或png文件，则解析出图层信息
                if (file.isFile() && (file.getName().toLowerCase().endsWith(".jpg")
                        || file.getName().toLowerCase().endsWith(".png"))) {
                    Layer layer = new Layer();
                    layer.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
                    layer.setPath("/"+newFolder.getName() + "/" + file.getName());
                    try (InputStream in = new FileInputStream(file)) {
                        Image image = ImageIO.read(in);
                        layer.setWidth(image.getWidth(null));
                        layer.setHeight(image.getHeight(null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    layer.setCreateTime(LocalDateTime.now());
                    layer.setUpdateTime(LocalDateTime.now());
                    layer.setGroupId(groupId);
                    layerRepository.save(layer);
                }
            }
        }
    }

    /**
     * 删除指定文件夹及其上级目录中的所有内容（递归删除）
     * @param folder 待删除的文件夹
     */
    private void deleteFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            folder.delete();
        }
    }

    public LayerGroupPageVO queryImgGroupPage(ImgGroupQueryPageDto dto) {
        Pageable pageable = PageRequest.of(dto.getPageIndex()-1, dto.getPageSize());
        LayerGroupPageVO layerGroupPageVO = new LayerGroupPageVO();

        if (dto.getKeywords() != null) {
            String fuzzyKeywords = "%" + dto.getKeywords() + "%";

            if (dto.getStartDate() != null && dto.getEndDate() != null) {
                LocalDateTime startDateTime = dto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime endDateTime = dto.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);

                Page<LayerGroup> layerGroupPage = layerGroupRepository.findAllByKeywordsAndUpdateTimeBetween(fuzzyKeywords, startDateTime, endDateTime, pageable);
                List<LayerGroupVO> layerGroupVOList = layerGroupPage.map(LayerGroupVO::new).getContent();
                setPaginationFields(layerGroupPageVO, layerGroupPage);
                layerGroupPageVO.setLayerGroups(layerGroupVOList);
            } else {
                Page<LayerGroup> layerGroupPage = layerGroupRepository.findAllByKeywords(fuzzyKeywords, pageable);
                List<LayerGroupVO> layerGroupVOList = layerGroupPage.map(LayerGroupVO::new).getContent();
                setPaginationFields(layerGroupPageVO, layerGroupPage);
                layerGroupPageVO.setLayerGroups(layerGroupVOList);
            }
        } else if (dto.getStartDate() != null && dto.getEndDate() != null) {
            LocalDateTime startDateTime = dto.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endDateTime = dto.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);

            Page<LayerGroup> layerGroupPage = layerGroupRepository.findAllByUpdateTimeBetween(startDateTime, endDateTime, pageable);
            List<LayerGroupVO> layerGroupVOList = layerGroupPage.map(LayerGroupVO::new).getContent();
            setPaginationFields(layerGroupPageVO, layerGroupPage);
            layerGroupPageVO.setLayerGroups(layerGroupVOList);
        } else {
            Page<LayerGroup> layerGroupPage = layerGroupRepository.findAll(pageable);
            List<LayerGroupVO> layerGroupVOList = layerGroupPage.map(LayerGroupVO::new).getContent();
            setPaginationFields(layerGroupPageVO, layerGroupPage);
            layerGroupPageVO.setLayerGroups(layerGroupVOList);
        }

        return layerGroupPageVO;
    }
    private void setPaginationFields(LayerGroupPageVO layerGroupPageVO, Page<LayerGroup> layerGroupPage) {
        layerGroupPageVO.setTotalPages(layerGroupPage.getTotalPages());
        layerGroupPageVO.setTotalElements(layerGroupPage.getTotalElements());
        layerGroupPageVO.setPageNumber(layerGroupPage.getNumber()+1);
        layerGroupPageVO.setPageSize(layerGroupPage.getSize());
    }

}