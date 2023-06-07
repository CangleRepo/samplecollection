package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.constant.SampleStatus;
import com.ljzh.samplecollection.domain.entity.Sample;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.domain.vo.SampleVO;
import com.ljzh.samplecollection.repository.SampleRepository;
import com.ljzh.samplecollection.repository.TaskLayerRepository;
import com.ljzh.samplecollection.repository.UserRepository;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sample not found"));
    }

    public List<Sample> getSamplesByTaskLayerId(TaskLayer taskLayer) {
        return sampleRepository.findByTaskLayer(taskLayer);
    }

    public Sample createSampleByTaskLayerId(Long taskLayerId, SampleVO sampleVO, Long userId) throws ParseException {
        // 根据任务图层 ID 获取任务图层
        TaskLayer taskLayer = taskLayerRepository.findById(taskLayerId)
                .orElseThrow(() -> new RuntimeException("Task layer not found"));

//        // 获取当前登录用户
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 确保用户拥有该任务图层的采集权限
//        if (!taskService.hasTaskCollectionRight(user, taskLayer.getTask())) {
//            throw new RuntimeException("User does not have collection right for this task layer");
//        }

        // 创建新的样本实体
        Sample sample = new Sample();
        sample.setName(sampleVO.getName());
        WKTReader wktReader = new WKTReader();
        sample.setTheGeom(wktReader.read(sampleVO.getTheGeom()));
        sample.setTaskLayer(taskLayer);
        sample.setSampleTag(sampleVO.getSampleTag());
        sample.setStatus(0);

        sampleRepository.save(sample);

        return sample;
    }

    public void updateSampleStatus(Long id, SampleStatus status) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sample not found"));

        sample.setStatus(status.code());

        sampleRepository.save(sample);
    }

    public void deleteSample(Long sampleId){
        sampleRepository.deleteById(sampleId);
    }

}



