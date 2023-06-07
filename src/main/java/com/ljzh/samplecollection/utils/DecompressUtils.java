package com.ljzh.samplecollection.utils;

import cn.hutool.core.io.FileUtil;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class DecompressUtils {
    /**
     * 解压缩RAR文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompressRAR(File file, String targetPath, boolean delete){
        Archive archive = null;
        OutputStream outputStream = null;
        try {
            archive = new Archive(file);
            FileHeader fileHeader;
            // 创建输出目录
            createDirectory(targetPath, null);
            while( (fileHeader = archive.nextFileHeader()) != null){
                if(fileHeader.isDirectory()){
                    createDirectory(targetPath, fileHeader.getFileNameString().trim()); // 创建子目录
                }else{
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + fileHeader.getFileNameString().trim()));
                    archive.extractFile(fileHeader, outputStream);
                }
            }
        } catch (RarException | IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(archive != null){
                    archive.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unzip(String zipFile, String outDir) throws Exception {
        if (!FileUtil.exist(zipFile)) {
            throw new FileNotFoundException();
        }
        final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("GBK"));
        ZipEntry entry = null;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String[] split = entry.getName().split("/");
                if (split.length<1){
                    throw new Exception("文件不存在");
                }
                final File file = new File(outDir, split[split.length-1]);
                if (!file.exists()) {
                    final boolean mkdirs = file.getParentFile().mkdirs();
                }

                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                int len = -1;
                final byte[] bytes = new byte[1024];
                while ((len = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedOutputStream.close();
                fileOutputStream.close();
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }

    /**
     *  构建目录
     * @param outputDir 输出目录
     * @param subDir 子目录
     */
    private static void createDirectory(String outputDir, String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }
}
