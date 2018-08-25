package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author ginko
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_commit_record")
public class LsdCommitRecordDo implements Serializable {

    private static final long serialVersionUID = 5578973806250085448L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String gmtModified;//最后修改时间
    private int isDelete;//是否删除状态
    private Integer type;// 提交类型，如verify风控认证
    private String relateId;// 关联id，如borrow_id
    private String content;// 提交的报文
    private String url;// 提交的url
    private String commitTime;// 提交的时间，每次累加，以逗号隔开
    private Integer commitNum;// 累计提交次数
    private Integer status;//处理状态

}
