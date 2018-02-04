package cn.panda.spider.downloadutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 视频文件下载工具，多线程
 */
//@Component
public class VideoDownloader implements Runnable{

    static  Logger logger = LoggerFactory.getLogger(VideoDownloader.class);

//    private String url;
//    private String name;
//
//    public VideoDownloader(String url, String name) {
//        super();
//        this.url = url;
//        this.name = name;
//    }


    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static Integer  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {

        Integer status = 0; //下载成功 1下载出错

        //文件保存位置
        File saveDir = new File(savePath);

        if(!saveDir.exists()){
            saveDir.mkdir();
        }

        String saveFileName = saveDir+File.separator+fileName;
        File file = new File(saveFileName);

        if(file.exists()){
            Thread.currentThread().interrupt();
            logger.info("文件已存在，保存线程中断");
        }

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = null;

        try {
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            logger.info("======>打开链接出错！");
            status = 1;
            e.printStackTrace();
        }

        //获取自己数组
        byte[] getData = new byte[0];
        try {
            getData = readInputStream(inputStream);
        } catch (IOException e) {
            logger.info("======>获取输入流数组出错！");
            status = 1;
            e.printStackTrace();
        }

        FileOutputStream fos = new FileOutputStream(file);

        try {
            fos.write(getData);
        } catch (IOException e) {
            logger.info("========>保存文件出错！");
            status = 1;
            e.printStackTrace();
        }

        if(fos!=null){
            fos.close();
        }

        if(inputStream!=null){
            inputStream.close();
        }

        logger.info("info:"+url+" download success");

        return status;

    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    @Override
    public void run() {

//        //TODO 修改保存目录
//        try {
//            downLoadFromUrl(url,name+".mp4","E:\\91porn");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println("===========>"+Thread.currentThread().getName());

    }
}
