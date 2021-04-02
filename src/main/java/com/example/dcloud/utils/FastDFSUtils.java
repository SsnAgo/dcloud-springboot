package com.example.dcloud.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FastDFSUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(FastDFSUtils.class);

    /**
     * 初始化客户端
     * ClientGlobal.init 读取配置文件，并初始化对应的属性
     */
    static {
        String filePath = null;
        try {
            //filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            //ClientGlobal.init(filePath);
            ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");
            //创建临时文件，将fdfs_client.conf的值赋值到临时文件中，创建这个临时文件的原因是springboot打成jar后无法获取classpath下文件
            String tempPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".conf";
            File f = new File(tempPath);
            IOUtils.copy(classPathResource.getInputStream(),new FileOutputStream(f));
            ClientGlobal.init(f.getAbsolutePath());
            LOGGER.info("初始化FastDFS成功");
        } catch (Exception e) {
            LOGGER.error("初始化FastDFS失败，");
            e.printStackTrace();
        }
    }

    public static String[] uploadFile(MultipartFile file){
        String name = file.getOriginalFilename();
        LOGGER.info("文件名：" + name);
        StorageClient storageClient = null;
        String[] uploadResult = null;
        try {
            // 获取storage客户端
            storageClient = getStorageClient();
            /**
             * 上传文件 ext_name就是扩展名，上传就是上传文件内容（字节）以及文件类型,因为保存到storage里，文件名将被重新hash
             * 参数列表 byte[] file_buff, String file_ext_name, NameValuePair[] meta_list
             */
            uploadResult = storageClient.upload_file(file.getBytes(), name.substring(name.indexOf(".")  + 1), null);
            LOGGER.info("上传文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("上传文件失败 ： ",e.getMessage());
        }
        if (null == uploadResult){
            LOGGER.error("上传失败",storageClient.getErrorCode());
        }

        return uploadResult;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFileInfo(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        FileInfo fileInfo = null;
        try {
            storageClient = getStorageClient();
            fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            LOGGER.info("获取文件信息成功");
        } catch (Exception e) {
            LOGGER.error("获取文件信息失败 ： ",e.getMessage());
        }
return fileInfo;
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downloadFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        InputStream inputStream = null;
        try {
            storageClient = getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            inputStream = new ByteArrayInputStream(bytes);
            LOGGER.info("下载文件成功");
        } catch (Exception e) {
            LOGGER.error("下载文件失败 ： ",e.getMessage());
        }
        return inputStream;
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient = null;
        try {
            storageClient = getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
            LOGGER.info("删除文件成功");
        } catch (Exception e) {
            LOGGER.error("删除文件失败 ： ",e.getMessage());
        }
    }





    /**
     * 生成 storage客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer);
        return storageClient;
    }


    /**
     * 生成tracker服务器
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient client = new TrackerClient();
        TrackerServer trackerServer = client.getTrackerServer();
        return trackerServer;
    }

    /**
     * 获取文件的存储路径
     * @return
     */
    public static String getTrackerUrl(){
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage = null;
        try{
            trackerServer = trackerClient.getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);

        }catch (Exception e){
            LOGGER.error("获取文件路径失败 ： ",e.getMessage());
        }
        return "http://" + storeStorage.getInetSocketAddress().getHostString() + ":7777/";
    }
}
