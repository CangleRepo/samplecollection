package com.ljzh.samplecollection.service;

import com.ljzh.samplecollection.constant.SampleStatus;
import com.ljzh.samplecollection.constant.TaskLayerStatus;
import com.ljzh.samplecollection.domain.entity.*;
import com.ljzh.samplecollection.domain.dto.LayerGroupDTO;
import com.ljzh.samplecollection.domain.vo.TaskAssignVO;
import com.ljzh.samplecollection.domain.dto.TaskLayerQueryDTO;
import com.ljzh.samplecollection.framwork.constant.ResponseEnum;
import com.ljzh.samplecollection.framwork.exception.CustomException;
import com.ljzh.samplecollection.repository.*;
import com.ljzh.samplecollection.xmlbody.Object;
import com.ljzh.samplecollection.xmlbody.*;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
@Log4j2
public class TaskService {
    @Value("${ljzh.img_store_path}")
    private String imgStorePath;

    @Value("${ljzh.export_path}")
    private String exportPath;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskLayerRepository taskLayerRepository;

    @Autowired
    private TaskAssigneeRepository taskAssigneeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LayerGroupRepository layerGroupRepository;

    @Autowired
    private LayerRepository layerRepository;

    @Autowired
    private SampleTagRepository sampleTagRepository;

    @Autowired
    private SampleRepository sampleRepository;

    public Task addTask(String name, String description) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        return taskRepository.save(task);
    }

    public void assignTaskLayerGroup(Long taskId, LayerGroupDTO layerGroupDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        List<Long> layerGroupIds = layerGroupDTO.getLayerGroupIds();

        List<Layer> layers = layerRepository.findAllByGroupIdIn(layerGroupIds);

        List<TaskLayer> taskLayers = layers.stream().map(layer -> {
            TaskLayer taskLayer = new TaskLayer();
            taskLayer.setTask(task);
            taskLayer.setLayer(layer);
            taskLayer.setStatus(0);
            return taskLayer;
        }).collect(Collectors.toList());

        taskLayerRepository.saveAll(taskLayers);
    }

    public void assignTaskAssignee(Long taskId, TaskAssignVO taskAssignVO) {
        User user = userRepository.findById(taskAssignVO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(taskAssignVO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

//        if (!user.getRoles().contains(role)) {
//            throw new RuntimeException("User does not have the role");
//        }

        TaskAssignee taskAssignee = new TaskAssignee();
        taskAssignee.setUser(user);
        taskAssignee.setRole(role);
        Optional<Task> task = taskRepository.findById(taskId);
        task.ifPresent(taskAssignee::setTask);
        taskAssigneeRepository.save(taskAssignee);

        if (taskAssignVO.getTaskLayerId() != null) {
            TaskLayer taskLayer = taskLayerRepository.findById(taskAssignVO.getTaskLayerId())
                    .orElseThrow(() -> new RuntimeException("Task layer not found"));

            taskAssignee.setTaskLayer(taskLayer);

            taskAssigneeRepository.save(taskAssignee);
        }
    }

    @EntityGraph(attributePaths = {"taskAssignees", "taskLayers"})
    public List<Task> getTasksByUserIdAndRoleId(Long userId, Long roleId) {
        List<TaskAssignee> taskAssignees = taskAssigneeRepository.findByUserIdAndRoleId(userId, roleId);
        // 获取任务 ID 列表并去重处理
        List<Long> taskIds = taskAssignees.stream()
                .map(taskAssignment -> taskAssignment.getTask().getId())
                .distinct()
                .collect(Collectors.toList());
        // 根据任务 ID 列表查询任务列表
        List<Task> tasks = new ArrayList<>();
        for (Long taskId : taskIds) {
            Optional<Task> task = taskRepository.findById(taskId);
            task.ifPresent(tasks::add);
        }
        return tasks;
    }

    public Page<TaskLayer> getTaskLayersByQueryDTO(TaskLayerQueryDTO queryDTO) {
        Pageable pageable = PageRequest.of(queryDTO.getPageNum(), queryDTO.getPageSize());

        List<TaskAssignee> taskAssignees = taskAssigneeRepository.findByUserIdAndRoleIdAndTaskId(
                queryDTO.getUserId(),
                queryDTO.getRoleId(),
                queryDTO.getTaskId()
        );

        for (TaskAssignee taskAssignee : taskAssignees) {
            TaskLayer taskLayer = taskAssignee.getTaskLayer();

        }

        List<TaskLayer> taskLayers = taskAssignees.stream()
                .map(TaskAssignee::getTaskLayer)
                .distinct()
                .filter(taskLayer -> queryDTO.getStatus() == null || queryDTO.getStatus().equals(taskLayer.getStatus()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), taskLayers.size());
        return new PageImpl<>(taskLayers.subList(start, end), pageable, taskLayers.size());
    }

    public void upload(MultipartFile uploadZipFile, Long taskId) {
        // 获取上传文件名、上传文件所在路径
        String originalFileName = uploadZipFile.getOriginalFilename();
        String uploadFolderPath = imgStorePath;
        File newFolder = new File(uploadFolderPath + File.separator + originalFileName.substring(0, originalFileName.lastIndexOf(".")));
        if (newFolder.exists()){
            throw new CustomException(ResponseEnum.FILE_EXIST);
        }

        // 新建文件夹并解压文件
        try (ZipInputStream zipInputStream = new ZipInputStream(
                new BufferedInputStream(uploadZipFile.getInputStream()), Charset.forName("GBK"))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                int index = entryName.lastIndexOf("/");
                if (index != -1) {
                    // 使用压缩包内部的文件夹名称作为文件夹名称
                    String newFolderName = entryName.substring(0, index);
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

        Map<String, Layer> layerMap = createLayerGroupWithLayers(newFolder);
        List<TaskLayer> taskLayers = new ArrayList<>();
        for (Layer layer : layerMap.values()){
            TaskLayer taskLayer = new TaskLayer();
            taskLayer.setLayer(layer);
            Optional<Task> task = taskRepository.findById(taskId);
            task.ifPresent(taskLayer::setTask);
            taskLayer.setCreateTime(LocalDateTime.now());
            taskLayer.setUpdateTime(LocalDateTime.now());
            // 默认导入的样本图层的状态为待审核状态
            taskLayer.setStatus(TaskLayerStatus.UNAUDITED.code());
            taskLayers.add(taskLayer);
        }
        taskLayerRepository.saveAll(taskLayers);
        for (String layerName : layerMap.keySet()) {
            String xmlPath = newFolder.getAbsolutePath() + File.separator + layerName + ".xml";
            Layer layer = layerMap.get(layerName);
            readXmlFile(xmlPath, layer, taskId);
        }
    }


    private void readXmlFile(String xmlPath, Layer layer, Long taskId) {
        File xmlFile = new File(xmlPath);
        // 如果对应的 XML 文件存在，则读取并解析其中的信息
        if (xmlFile.exists() && !xmlFile.isDirectory()) {
            // 读取该任务layer的taskLayerID
            TaskLayer taskLayer = taskLayerRepository.findByLayerIdAndTaskId(layer.getId(), taskId);
            List<Sample> samples = new ArrayList<>();
            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(xmlFile);
                Element rootElement = document.getRootElement();
                Iterator<Element> rootIt = rootElement.elementIterator();
                while (rootIt.hasNext()) {
                    Element rootNext = rootIt.next();
                    if ("object".equals(rootNext.getName())) {
                        Iterator<Element> rootNextIt = rootNext.elementIterator();
                        Sample sample = new Sample();
                        while (rootNextIt.hasNext()) {
                            Element rootNextNext = rootNextIt.next();
                            if ("name".equals(rootNextNext.getName())) {
                                SampleTag newTag = new SampleTag();
                                SampleTag tag = sampleTagRepository.findByName(rootNextNext.getText());
                                if (tag!=null){
                                    BeanUtils.copyProperties(tag,newTag);
                                }
                                newTag.setName(rootNextNext.getText());
                                if (newTag.getId() == null){
                                    newTag.setCreateTime(LocalDateTime.now());
                                }
                                newTag.setUpdateTime(LocalDateTime.now());
                                sampleTagRepository.save(newTag);
                                sample.setSampleTag(newTag);
                            }
                            if ("bndbox".equals(rootNextNext.getName())) {
                                Iterator<Element> rootNextNextIt = rootNextNext.elementIterator();
                                Double xmin = null, ymin = null, xmax = null, ymax = null;
                                while (rootNextNextIt.hasNext()) {
                                    Element rootNextNextNext = rootNextNextIt.next();
                                    switch (rootNextNextNext.getName()) {
                                        case "xmin":
                                            xmin = Double.valueOf(rootNextNextNext.getText());
                                            continue;
                                        case "ymin":
                                            ymin = Double.valueOf(rootNextNextNext.getText());
                                            continue;
                                        case "xmax":
                                            xmax = Double.valueOf(rootNextNextNext.getText());
                                            continue;
                                        case "ymax":
                                            ymax = Double.valueOf(rootNextNextNext.getText());
                                            continue;
                                        default:
                                            continue;
                                    }
                                }
                                if (xmin != null && ymin != null && xmax != null && ymax != null) {
                                    Coordinate[] coordinates = new Coordinate[5];
                                    coordinates[0] = new Coordinate(xmin, ymin);
                                    coordinates[1] = new Coordinate(xmin, ymax);
                                    coordinates[2] = new Coordinate(xmax, ymax);
                                    coordinates[3] = new Coordinate(xmax, ymin);
                                    coordinates[4] = new Coordinate(xmin, ymin);
                                    Polygon theGeom = new GeometryFactory().createPolygon(coordinates);
                                    sample.setTheGeom(theGeom);
                                }
                            }
                            if (sample.getSampleTag()!=null&&sample.getTheGeom()!=null){
                                sample.setTaskLayer(taskLayer);
                                sample.setStatus(SampleStatus.UNAUDITED.code());
                                sample.setCreateTime(LocalDateTime.now());
                                sample.setUpdateTime(LocalDateTime.now());
                                samples.add(sample);
                            }
                        }
                    }
                }
                sampleRepository.saveAll(samples);

                // 添加删除 XML 文件的代码
                if (xmlFile.delete()) {
                    log.info(xmlFile.getName() + " 已被成功删除！");
                } else {
                    log.info("删除 " + xmlFile.getName() + " 失败，请检查文件是否已被占用！");
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建新的图层组并保存到数据库，并关联指定目录下所有jpg和png文件作为其一部分图层。
     * @param newFolder 新建的文件夹对象
     * @return 由图层名和图层组成的Map
     */
    private Map<String, Layer> createLayerGroupWithLayers(File newFolder) {
        // 创建新的图层组对象
        LayerGroup layerGroup = new LayerGroup();
        layerGroup.setName(newFolder.getName());
        Date date = new Date();
        layerGroup.setDescription("新建图层组"+ date.toString());
        layerGroup.setRegionId(100L); // 假设regionId为1L
        layerGroup.setCreateTime(LocalDateTime.now());
        layerGroup.setUpdateTime(LocalDateTime.now());
        // 保存图层组及其关联的图层
        LayerGroup group = layerGroupRepository.save(layerGroup);
        // 遍历文件夹，解析出图层列表
        Map<String, Layer> layerMap = new HashMap<>();
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
                    layer.setGroupId(layerGroup.getId());
                    layerRepository.save(layer);
                    layerMap.put(layer.getName(), layer);
                }
            }
        }
        // 返回由图层名和图层组成的Map
        return layerMap;
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

    public boolean export(Long taskId,int width,int height, HttpServletResponse response) throws IOException {
        Optional<Task> task = taskRepository.findById(taskId);
        List<TaskLayer> taskLayers = taskLayerRepository.findByTaskId(taskId);
        for (TaskLayer taskLayer : taskLayers) {
            Layer layer = taskLayer.getLayer();
            BufferedImage image = ImageIO.read(new File(imgStorePath+"/"+layer.getPath()));
            int widthNum = (image.getWidth() / width)+1;
            int heightNum = (image.getHeight() / height)+1;
            List<Sample> samples = sampleRepository.findByTaskLayer(taskLayer);
            for (int i = 0; i < widthNum; i++) {
                for (int j = 0; j < heightNum; j++) {
                    // 创建 GeometryFactory 对象
                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                    Coordinate[] coordinates = new Coordinate[5];
                    if (i*width< image.getWidth()&&j*height< image.getHeight()){
                        coordinates[0] = new Coordinate(i*width, j*height);
                        if ((i*width+width)> image.getWidth()&&(j*height+height)< image.getHeight()){
                            // 创建 Coordinate 数组
                            coordinates[1] = new Coordinate(image.getWidth(), j*height);
                            coordinates[2] = new Coordinate(image.getWidth(), j*height+height);
                            coordinates[3] = new Coordinate(i*width, j*height+height);
                        }
                        else if((i*width+width)< image.getWidth()&&(j*height+height)> image.getHeight()){
                            coordinates[1] = new Coordinate(i*width+width, j*height);
                            coordinates[2] = new Coordinate(i*width+width, image.getHeight());
                            coordinates[3] = new Coordinate(i*width, image.getHeight());
                        }
                        else if((i*width+width)> image.getWidth()&&(j*height+height)>image.getHeight()){
                            coordinates[1] = new Coordinate(image.getWidth(), j*height);
                            coordinates[2] = new Coordinate(image.getWidth(), image.getHeight());
                            coordinates[3] = new Coordinate(i*width, image.getHeight());
                        }
                        else{
                            coordinates[1] = new Coordinate(i*width+width, j*height);
                            coordinates[2] = new Coordinate(i*width+width, j*height+height);
                            coordinates[3] = new Coordinate(i*width, j*height+height);
                        }
                        coordinates[4] = new Coordinate(i*width, j*height);
                    }
                    else{
                        break;
                    }

                    // 创建 Polygon 对象
                    Polygon polygon = geometryFactory.createPolygon(coordinates);
                    SampleAnnotation sampleAnnotation = new SampleAnnotation();
                    List<Object> objects = new ArrayList<>();
                    for (Sample sample : samples) {
                        Polygon theGeom = (Polygon) sample.getTheGeom();
                        Geometry result = polygon.intersection(theGeom);
                        if (!result.isEmpty()&&result instanceof Polygon){
                            Object object = new Object();
                            object.setName(sample.getSampleTag().getName());
                            object.setPose(0);
                            object.setTruncated(0);
                            BndBox bndBox = new BndBox();
                            Envelope envelopeInternal = result.getEnvelopeInternal();
                            if (((int) envelopeInternal.getMaxX() == (int) envelopeInternal.getMinX())
                                    ||((int) envelopeInternal.getMaxY() == (int) envelopeInternal.getMinY())){
                                break;
                            }
                            bndBox.setXmax((int) envelopeInternal.getMaxX()-i*width);
                            bndBox.setYmax((int) envelopeInternal.getMaxY()-j*height);
                            bndBox.setXmin((int) envelopeInternal.getMinX()-i*width);
                            bndBox.setYmin((int) envelopeInternal.getMinY()-j*height);
                            object.setBndBox(bndBox);
                            object.setDifficult(0);
                            objects.add(object);
                        }
                    }
                    if (!ObjectUtils.isEmpty(objects)){
                        LocalDate currentDate = LocalDate.now();
                        int year = currentDate.getYear();
                        int month = currentDate.getMonthValue();
                        int day = currentDate.getDayOfMonth();
                        // 切图 小图路径
                        String path = exportPath+"/"+task.get().getName()+"/"+layer.getName()+"-"+i+"_"+j +"-"+ year + "-" + month + "-" + day+".JPG";
                        int subWidth = (int)polygon.getEnvelopeInternal().getWidth();
                        int subHeight = (int) polygon.getEnvelopeInternal().getHeight();
                        BufferedImage subimage = image.getSubimage(i * width, j * height,subWidth , subHeight);
                        File file = new File(path);
                        boolean mkdirs = file.getParentFile().mkdirs();
                        ImageIO.write(subimage,"JPG",file);
                        sampleAnnotation.setObjectList(objects);
                        sampleAnnotation.setPath(path);
                        sampleAnnotation.setFolder(layer.getName());
                        sampleAnnotation.setFilename(layer.getName()+i+"_"+j + year + "-" + month + "-" + day+".JPG");
                        sampleAnnotation.setSource(new Source());
                        sampleAnnotation.setSegmented(0);
                        Size size = new Size();
                        size.setHeight(subimage.getHeight());
                        size.setWidth(subimage.getWidth());
                        sampleAnnotation.setSize(size);
                        try {
                            JAXBContext context = JAXBContext.newInstance(SampleAnnotation.class);
                            Marshaller marshaller = context.createMarshaller();

                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                            StringWriter sw = new StringWriter();
                            marshaller.marshal(sampleAnnotation, sw);

                            // 将序列化后的 XML 写入到文件中
                            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            StreamResult result = new StreamResult(new File(path.replace(".JPG",".xml")));
                            transformer.transform(new StreamSource(new StringReader(sw.toString())), result);
                        } catch (TransformerException e) {
                            e.printStackTrace();
                        } catch (JAXBException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }



        // 获取要下载的文件夹D:\sampleCollection\export\京山秋季采集
        File folder = new File(exportPath+"/"+task.get().getName());
        if (!folder.exists()) {
            // 如果文件夹不存在，返回错误信息
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Folder not found.");
            return false;
        }
        String zipFileName = folder.getName() + ".zip";
        setAttachmentResponseHeader(response,zipFileName);
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        compressFolder(folder, zos, "");
        zos.close();
        return true;
    }

    public MultiPolygon convert(List<Polygon> polygonList) {
        if (polygonList == null || polygonList.isEmpty()) {
            return null;
        }

        List<Polygon> validPolygons = new ArrayList<>();
        for (Polygon polygon : polygonList) {
            if (polygon != null && !polygon.isEmpty()) {
                validPolygons.add(polygon);
            }
        }

        // 获取 JTS 工厂
        GeometryFactory geometryFactory = new GeometryFactory();

        // 将多个 Polygon 转化为 MultiPolygon
        Polygon[] polygons = validPolygons.toArray(new Polygon[0]);
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygons);

        return multiPolygon;
    }

    public Polygon[] convert(MultiPolygon multiPolygon) {
        if (multiPolygon == null) {
            return null;
        }

        List<Polygon> polygonList = new ArrayList<>();
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            Geometry geometry = multiPolygon.getGeometryN(i);
            if (geometry instanceof Polygon) {
                polygonList.add((Polygon) geometry);
            }
        }

        return polygonList.toArray(new Polygon[0]);
    }

    private void compressFolder(File folder, ZipOutputStream zos, String path) throws IOException {
        byte[] buffer = new byte[1024];
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 如果当前文件是目录，递归调用 compressFolder() 方法处理子目录
                String subPath = path + encodeFilename(file.getName()) + "/";
                compressFolder(file, zos, subPath);
            } else {
                // 如果当前文件是普通文件，将其添加到压缩流中
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(path + encodeFilename(file.getName()));
                zos.putNextEntry(entry);
                int readBytes;
                while ((readBytes = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, readBytes);
                }
                fis.close();
                zos.closeEntry();
            }
        }
    }

    private String encodeFilename(String fileName) throws UnsupportedEncodingException {
        // 将文件名按GBK编码方式进行编码
//        return new String(fileName.getBytes("GBK"), "UTF-8");
        return fileName;
    }


    /**
     * 下载文件名重新编码
     *
     * @param response 响应对象
     * @param realFileName 真实文件名
     */
    public void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
//        System.out.println(getUTFStringByEncoding(s));
        return new String(s.getBytes(Charset.forName(getUTFStringByEncoding(s))), StandardCharsets.UTF_8.toString());
    }

    public static String getUTFStringByEncoding(String str) {
        String encode = "UTF-8";
        try {
            if(str!=null){
                if (str.equals(new String(str.getBytes("GB2312"), "GB2312"))) {
                    encode = "GB2312";
                }else if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
                    encode = "ISO-8859-1";
                }else if (str.equals(new String(str.getBytes("UTF-8"), "UTF-8"))) {
                    encode = "UTF-8";
                }else if (str.equals(new String(str.getBytes("GBK"), "GBK"))) {
                    encode = "GBK";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encode;
    }

    public List<TaskLayer> queryLayersByTaskId(Long taskId) {
        List<TaskLayer> taskLayers = taskLayerRepository.findByTaskId(taskId);
//        List<Layer> layers = taskLayers.stream().map(TaskLayer::getLayer).collect(Collectors.toList());
//        List<LayerVO> response = new ArrayList<>();
//        for (Layer layer : layers) {
//            LayerVO layerVO = new LayerVO(layer);
//            response.add(layerVO);
//        }
        return taskLayers;
    }

    public List<Task> taskList() {
        return taskRepository.findAll();
    }

    public boolean assignTaskLayer(Long taskId, Long layerId) {
        TaskLayer taskLayer = new TaskLayer();
        taskLayer.setStatus(TaskLayerStatus.UNCOLLECTED.code());
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()){
            taskLayer.setTask(task.get());
        }
        else {
            throw new CustomException(ResponseEnum.TASK_NOT_EXITS);
        }
        Optional<Layer> layer = layerRepository.findById(layerId);
        if (layer.isPresent()){
            taskLayer.setLayer(layer.get());
        }
        else {
            throw new CustomException(ResponseEnum.LAYER_NOT_EXITS);
        }
        taskLayer.setCreateTime(LocalDateTime.now());
        taskLayer.setUpdateTime(LocalDateTime.now());
        TaskLayer save = taskLayerRepository.save(taskLayer);
        return save.getId() != null;
    }
}


