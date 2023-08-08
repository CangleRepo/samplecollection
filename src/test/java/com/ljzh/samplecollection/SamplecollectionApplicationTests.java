//package com.ljzh.samplecollection;
//
//import com.ljzh.samplecollection.constant.TaskAssignStatus;
//import com.ljzh.samplecollection.domain.entity.Layer;
//import com.ljzh.samplecollection.domain.entity.Sample;
//import com.ljzh.samplecollection.domain.entity.TaskLayer;
//import com.ljzh.samplecollection.domain.view.TaskLayerView;
//import com.ljzh.samplecollection.repository.*;
//import com.ljzh.samplecollection.service.SampleService;
//import com.ljzh.samplecollection.service.TaskLayerService;
//import org.junit.jupiter.api.Test;
//import org.locationtech.jts.util.Assert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//class SamplecollectionApplicationTests {
//    @Autowired
//    private TaskLayerViewRepository taskLayerViewRepository;
//
//    @Autowired
//    private SampleService sampleService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @Autowired
//    private SampleRepository sampleRepository;
//
//    @Autowired
//    private TaskLayerRepository taskLayerRepository;
//
//    @Autowired
//    private TaskLayerService taskLayerService;
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void testGeometry(){
//        Optional<TaskLayer> byId = taskLayerRepository.findById(35L);
//        List<Sample> byTaskLayerId = sampleRepository.findByTaskLayer(byId.get());
//        for (Sample sample : byTaskLayerId) {
//            System.out.println(sample.getTheGeom().toString());
//        }
//    }
//
//    @Test
//    void testTaskLayerView(){
//        int size = taskLayerViewRepository.findByAssignStatusAndTaskId(2,
//                4L).size();
//        for (TaskLayerView taskLayerView : taskLayerViewRepository.findByAssignStatusAndTaskId(1,
//                1L)) {
//            System.out.println("taskLayerView = " + taskLayerView.toString());
//        }
//        Assert.equals(size, 1);
//    }
//
//    @Test
//    void testGetLayerByTaskLayerView() {
//        Page<Layer> layersByTaskIdAndAssignStatus = taskLayerService.findLayersByTaskIdAndAssignStatusPage(0, 30, TaskAssignStatus.UNDISTRIBUTED.code(), 1L);
//        for (Layer layer : layersByTaskIdAndAssignStatus) {
//            System.out.println("layer = " + layer.toString());
//        }
//        System.out.println("count = "+layersByTaskIdAndAssignStatus.getTotalElements());
//    }
//}
