package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ginko
 * @description ${todo}
 * @date 2018/8/23 21:21
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_Resource")
public class LsdResourceDo implements Serializable {

    private static final long serialVersionUID = -4454454666376128930L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    // 是否删除状态，1：删除，0：有效
    private Integer isDelete;
    // 创建时间
    private Date gmtCreate;
    // 最后修改时间
    private Date gmtModified;
    // 创建人
    private String creator;
    // 最后修改人
    private String modifier;
    // 配置类型，S:系统配置,B:业务配置
    private String dataType;
    // 配置类型，即配置的KEY，用于定位配置；所有字母大写，多个字母中间用下划线“_”分割；如：用户白名单类型USER_WHITE_LIST
    private String type;
    // 配置类型描述，如针对TYPE=USER_WHITE_LIST该值可描述为：用户白名单列表
    private String typeDesc;
    // 名称[在菠萝蜜中表示活动名称]
    private String name;
    // 值[在菠萝蜜中表示活动优惠劵URL地址]
    private String value;
    // 描述
    private String description;
    // 类型，可针对某一类型的配置做分类
    private String secType;
    // 扩展值1[在菠萝蜜中表示活动开始时间]
    private String value1;
    // 扩展值2[在菠萝蜜中表示活动结束时间]
    private String value2;
    // 扩展值3[在菠萝蜜中表示进行状态]
    private String value3;
    // 扩展值4[在菠萝蜜中表示活动启禁用状态O:表示启用, N表示禁用]
    private String value4;
    // 排序
    private Long sort;

}
