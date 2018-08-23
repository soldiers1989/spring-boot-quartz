package com.pgy.ginko.quartz.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author ginko
 * @description User测试实体类
 * @date 2018/8/20 17:10
 */
@Data
@ToString
@Table(name = "user")
@ApiModel
public class User implements Serializable {

    private static final long serialVersionUID = 5603289846128272666L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

}