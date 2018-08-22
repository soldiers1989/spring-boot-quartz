package com.pgy.ginko.quartz.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ginko
 * @description 定时任务实体
 */
@Data
@ToString
@ApiModel
public class ScheduleJob implements Serializable {

    private static final Long serialVersionUID = 1435515995276255188L;

    private Long id;

    @ApiModelProperty("定时任务对应类名")
    private String className;

    @ApiModelProperty("定时任务Cron表达式")
    private String cronExpression;

    @ApiModelProperty("定时任务名")
    private String jobName;

    @ApiModelProperty("定时任务群组")
    private String jobGroup;

    @ApiModelProperty("触发器名")
    private String triggerName;

    @ApiModelProperty("触发器群组")
    private String triggerGroup;

    @ApiModelProperty("暂停否")
    private Boolean pause;

    @ApiModelProperty("启用否")
    private Boolean enable;

    @ApiModelProperty("定时任务描述")
    private String description;

    @ApiModelProperty("创建时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
    private Date lastUpdateTime;

}
