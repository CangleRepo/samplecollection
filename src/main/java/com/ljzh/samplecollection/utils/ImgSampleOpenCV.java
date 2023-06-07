package com.ljzh.samplecollection.utils;

import lombok.SneakyThrows;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ImgSampleOpenCV implements ImgSample {
    private int imgWight;
    private int imgHeight;
    private String path;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private ThreadPoolTaskExecutor cutThreadPool;

    public ImgSampleOpenCV(String path) {
        Mat mat = Imgcodecs.imread(path);
        this.path = path;
        this.imgWight = mat.width();
        this.imgHeight = mat.height();
    }

    public ImgSampleOpenCV(String path, ThreadPoolTaskExecutor threadPoolTaskExecutor, ThreadPoolTaskExecutor cutThreadPool) throws IOException {
        Mat mat = Imgcodecs.imread(path);
        this.path = path;
        this.imgWight = mat.width();
        this.imgHeight = mat.height();
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.cutThreadPool = cutThreadPool;
    }


    @Override
    @SneakyThrows
    public boolean scaleCutToFile(int num, String outDir){
        int w = 256;
        int h = 256;
        Mat s = Imgcodecs.imread(new String(path.getBytes(StandardCharsets.UTF_8),"GBK"));
        Imgproc.cvtColor(s,s,Imgproc.COLOR_BGR2BGRA);

        CountDownLatch d = new CountDownLatch(num);
        for (int i = 0; i < num ; i++) {
            int a = i;
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Mat mat = new Mat();
                    if(a != 0){
                        double pow = Math.pow(2.0, (double) a);
                        Imgproc.resize(s,mat, new Size(s.cols() / pow, s.rows() / pow));
                    }else{
                        mat = s;
                    }
                    int width = mat.width();
                    int height = mat.height();
                    int p_w =w -  width % w;
                    int p_h = h -  height % h;
                    Mat dst = new Mat();
                    Core.copyMakeBorder(mat,dst,0,p_h,0,p_w,Core.BORDER_CONSTANT,new Scalar(0,0,0,0));
                    int x = dst.width() / w;
                    int y = dst.height() / h;

                    CountDownLatch countDownLatch = new CountDownLatch(x * y);
                    for (int j = 0; j <x ; j++) {
                        for (int k = 0; k < y; k++) {
                            int ss = j;
                            int bb = k;
                            cutThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Mat s2 = new Mat(dst,new Rect(ss*w,bb*h,w,h));
                                    String path = outDir+ File.separator+(num - a -1)+ File.separator+ss+ File.separator+bb+".png";
                                    new File(path).getParentFile().mkdirs();
                                    Imgcodecs.imwrite(path,s2);
                                    countDownLatch.countDown();
                                }
                            });
                        }
                    }
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    d.countDown();
                }
            });

        }
        try {
            d.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
