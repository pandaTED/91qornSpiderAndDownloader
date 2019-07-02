package cn.panda.spider.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 视频信息实体
 */
@Entity
@Data
@Table
public class Porn91 {

    @Id
    @GeneratedValue
    Long id;
    /**
     * 视频详情页
     */
    @Column(unique = true)
    String videoLink;
    /**
     * 标题
     */
    String titleXpath;
    /**
     * 封面图
     */
    String imgUlr;
    /**
     * 作者链接
     */
    String zuozheLink;
    /**
     * 作者名字
     */
    String zuozheName;
    /**
     * 视频时长
     */
    String shijian;
    /**
     * 添加时间
     */
    String tianjiashijian;
    /**
     * 查看次数
     */
    String chakan;
    /**
     * 收藏次数
     */
    String shoucang;
    /**
     * 留言次数
     */
    String liuyan;
    /**
     * 积分
     */
    String jifen;

    /**
     * 视频下载地址
     */
    @Column(unique = true)
    String videoSource;

    Date downloadTime;

}
