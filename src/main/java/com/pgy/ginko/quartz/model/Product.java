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
 * @description Product bean
 * @date 2018/8/23 10:44
 */
@Data
@ToString
@ApiModel
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -7960881983497310815L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("价格")
    private long price;

}