package com.ljzh.samplecollection;

import com.ljzh.samplecollection.domain.entity.Sample;
import com.ljzh.samplecollection.domain.entity.TaskLayer;
import com.ljzh.samplecollection.repository.SampleRepository;
import com.ljzh.samplecollection.repository.TaskLayerRepository;
import com.ljzh.samplecollection.repository.TaskRepository;
import com.ljzh.samplecollection.repository.UserRepository;
import com.ljzh.samplecollection.service.SampleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class SamplecollectionApplicationTests {
    @Autowired
    private SampleService sampleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testGeometry(){
        Optional<TaskLayer> byId = taskLayerRepository.findById(35L);
        List<Sample> byTaskLayerId = sampleRepository.findByTaskLayer(byId.get());
        for (Sample sample : byTaskLayerId) {
            System.out.println(sample.getTheGeom().toString());
        }
    }
}
