package cn.panda.spider.downloadutil;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * 视频文件下载工具，多线程
 */
//@Component
public class VideoDownloader implements Runnable {

    static Logger logger = LoggerFactory.getLogger(VideoDownloader.class);

    private String url;
    private String name;
    private Porn91Dao porn91Dao;

    public VideoDownloader(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public VideoDownloader(String url, String name, Porn91Dao porn91Dao) {
        this.url = url;
        this.name = name;
        this.porn91Dao = porn91Dao;
    }


    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public byte[] readInputStream(InputStream inputStream) throws IOException {

        byte[] buffer = new byte[1024];
        int len = 0;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        bos.close();

        return bos.toByteArray();
    }


    @Override
    public void run() {

        //TODO 修改保存目录

        Integer status = 0; //下载成功 1下载出错

        //文件保存位置
        File saveDir = new File("D:\\91porn");

        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        String saveFileName = saveDir + File.separator + name + ".mp4";
        File file = new File(saveFileName);

        if (file.exists()) {
            Thread.currentThread().interrupt();
            logger.info("文件已存在，保存线程中断");
        }

        URL videoSourceUrl = null;
        try {
            videoSourceUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) videoSourceUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置超时间为3秒
        conn.setConnectTimeout(10 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");

        //得到输入流
        InputStream inputStream = null;

        try {
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            logger.info("======>打开链接出错！");
            e.printStackTrace();
        }

        //获取自己数组
        byte[] getData = new byte[0];
        try {
            getData = readInputStream(inputStream);
        } catch (IOException e) {
            logger.info("======>获取输入流数组出错！");
            e.printStackTrace();
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fos.write(getData);
        } catch (IOException e) {
            logger.info("========>保存文件出错！");
            e.printStackTrace();
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.info("info:" + videoSourceUrl + "链接下载成功!");

        if (null != porn91Dao) {
            Porn91 porn91 = porn91Dao.getByVideoSourceIs(url);
            porn91.setDownloadTime(new Date());
            try {
                porn91Dao.save(porn91);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
