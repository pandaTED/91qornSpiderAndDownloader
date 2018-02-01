package cn.panda.springspider.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Porn91 {

    @Id
    @GeneratedValue
    Long id;
    @Column(unique = true)
    String videoLink;
    String titleXpath;
    String imgUlr;
    String zuozheLink;
    String zuozheName;
    String shijian;
    String tianjiashijian;
    String chakan;
    String shoucang;
    String liuyan;
    String jifen;
}
