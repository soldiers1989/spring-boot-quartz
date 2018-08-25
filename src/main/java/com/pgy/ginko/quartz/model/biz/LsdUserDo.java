package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ginko
 * @description LsdUserDo
 * @date 2018/8/23 20:29
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_user")
public class LsdUserDo implements Serializable {

    private static final long serialVersionUID = 4222289610479830397L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private Date gmtCreate;

    private Date gmtModified;

    private String userName;

    private String password;

    private String salt;

    private String realName;

    private Long recommendId;

    private Integer failCount;

    private String recommendCode;

    private String idNumber;

    private Long channelId;

    private Long channelPointId;

    private Integer version;
}
