package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ginko
 * @description 短语通道
 * @date 2018/4/17
 */
@Data
@ToString
@ApiModel
@Table(name = "sms_channel")
public class SmsChannelDo implements Serializable {

    private static final long serialVersionUID = 1378590418215718012L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 短信类型编码
     */
    private String smsUserfulCode;

    /**
     * 短信类型名称
     */
    private String smsUserfulName;

    /**
     * 通道编码
     */
    private String smsChannelCode;

    /**
     * 通道名称
     */
    private String smsChannelName;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 主键ID
     */
    private Integer isUserd;

    /**
     * 是否使用0:是;1:否
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除0:否;1:是
     */
    private Integer isDeleted;

}