package com.pgy.ginko.quartz.common.sms;

import lombok.Data;

/**
 * 
 * @author ginko
 * @description 普通短信发送响应实体类
 * @date 2018-8-23 22:37:39
 */
@Data
public class SmsSendResponse {
	/**
	 * 响应时间
	 */
	private String time;
	/**
	 * 消息id
	 */
	private String msgId;
	/**
	 * 状态码说明（成功返回空）
	 */
	private String errorMsg;
	/**
	 * 状态码（详细参考提交响应状态码）
	 */
	private String code;

}
