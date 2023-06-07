package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.domain.entity.SampleTag;
import com.ljzh.samplecollection.domain.vo.SampleTagVO;
import com.ljzh.samplecollection.repository.SampleTagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SampleTagService {
    @Autowired
    private SampleTagRepository sampleTagRepository;

    // 新增样本标签
    public void addSampleTag(SampleTag sampleTag) {
        sampleTagRepository.save(sampleTag);
    }

    // 根据id删除样本标签
    public boolean deleteSampleTagById(Long id) {
        sampleTagRepository.deleteById(id);
        return true;
    }

    // 更新样本标签信息
    public boolean updateSampleTag(SampleTag sampleTag) {
        sampleTagRepository.save(sampleTag);
        return true;
    }

    // 根据id查询样本标签
    public SampleTag getSampleTagById(Long id) {
        return sampleTagRepository.findById(id).orElse(null);
    }

    // 查询所有样本标签
    public List<SampleTagVO> getAllSampleTags() {
        List<SampleTag> sampleTagList = sampleTagRepository.findAll();
        List<SampleTagVO> sampleTagVOList = new ArrayList<>();
        for (SampleTag sampleTag : sampleTagList) {
            SampleTagVO sampleTagVO = new SampleTagVO();
            // 将实体类的属性值拷贝到值对象中
            BeanUtils.copyProperties(sampleTag, sampleTagVO);
            sampleTagVOList.add(sampleTagVO);
        }
        return sampleTagVOList;
    }

    public Page<SampleTag> getAllSampleTagsByPage(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return sampleTagRepository.findAll(pageable);
    }
}

