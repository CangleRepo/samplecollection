package com.ljzh.samplecollection.config;

import org.opencv.core.Core;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCvConfig {

    static {
        String os = System.getProperty("os.name");
//        if(!(os.toLowerCase().startsWith("win"))){
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        }
    }
}
