# 91qorn Spider And Downloader

## update -2019-07-12
    将视频下载，视频信息更新方法放入到子线程中。
    

## 系统要求
    1.JDK 1.8以上
    2.Mysql
    3.lombak
    4.IDEA (eclipse的话使用mvn转为eclipse项目)
    5.酸酸或者酸酸乳 （使用负载均衡来获取视频源地址最好）
    
## 相关框架
    1.spring boot
    2.webmagic （感谢作者 http://webmagic.io/） 
       
## 运行
    1.修改 application-dev.yml 中数据库配置，运行时数据库表会自动建立
    2.爬取视频网页链接信息时不需要修改爬虫的cooikes信息，下载时需要在cn.panda.spider.spider.SpiderSingle内修改cookies信息，请使用自己的账号登录91，然后将cookies信息填写
    3.运行入口：test内cn.panda.spider.SpiderPornTest.test1() 爬取视频网页链接到mysql
    4.修改cn.panda.downloader.Utils.VideoDownloader内的视频保存目录
    5.运行入口：test内cn.panda.spider.SpiderPornTest.test3() 根据筛选条件下载视频
    6.筛选条件请在cn.panda.spider.dao.Porn91Dao.getAllAndOrderByChakanDesc()内修改sql语句
    
## 目前架构很简单，分为两部分
    1.所有视频地址的爬取，采用爬列表页的方式，根据视频网页链接查重
    2.根据筛选条件下载视频，需要91的登录信息

## 请注意
    1.请不要传播下载的视频文件
    
## TODO
    1.下载视频工具修改为线程池的形式，避免启动过多下载线程；（已完成）
    2.保存视频源地址到mysql修改为批量方式，以减少浪费在操作db上的时间。（已完成）

## 一些数据
    1.目前爬取的91视频为 100704 个；
    2.上传视频最多的用户为：boismns6598，视频个数为：588个。
