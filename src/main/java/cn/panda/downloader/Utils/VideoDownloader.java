package cn.panda.downloader.Utils;

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

    private String url;
    private String name;

    public VideoDownloader(String url, String name) {
        super();
        this.url = url;
        this.name = name;
    }

    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);

        fos.write(getData);

        if(fos!=null){
            fos.close();
        }

        if(inputStream!=null){
            inputStream.close();
        }

       logger.info("info:"+url+" download success");

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

        logger.info("========>"+url);

        //TODO 修改保存目录
        try {
            downLoadFromUrl(url,name+".mp4","D:\\91porn");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
