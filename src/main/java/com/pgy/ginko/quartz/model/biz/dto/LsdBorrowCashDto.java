package com.pgy.ginko.quartz.model.biz.dto;

import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 *
 * @author ginko
 * @date 2018-8-26 11:03:37
 */
@Data
@ToString
public class LsdBorrowCashDto extends LsdBorrowCashDo {

	private static final long serialVersionUID = -662078081699916696L;

	private String userName;//用户账户

	private String realName;//用户姓名

	private BigDecimal finalAmount;//到账额度

	private BigDecimal amountSum;//申请总金额

	private BigDecimal arrivalAmountSum;//到账总金额

	private String idcardAddress;//身份证地址

	private String mobile;//银行卡主卡预留手机号

	private BigDecimal unRepayAmount;//未还金额

	private String consigneeName;//对应商品填写的 收货人信息

	private String consigneeAddress;

	private String consigneeMobile;

	private String StatusName;

	private String reviewStatusName;

	private String type;

	private BigDecimal serverAmount;

	private String protocolUrl;

	public BigDecimal getServerAmount() {
		this.serverAmount = this.getAmount().subtract(this.getArrivalAmount().subtract(this.getActivityAmount()));
		return this.serverAmount;
	}

	public String getType() {
		if (borrowDays == 7) {
			type = "7天";
		} else {
			type = "14天";
		}
		return type;
	}

	public String getStatusName() {
		if(status == 0){
			this.StatusName = "申请/未审核";
		}else if(status == 1){
			this.StatusName = "已结清";
		}else if(status == 2){
			this.StatusName = "打款中";
		}else if(status == 3){
			this.StatusName = "打款失败";
		}else if(status == 4){
			this.StatusName = "关闭";
		}else if(status == 5){
			this.StatusName = "已经打款/待还款";
		}
		return StatusName;
	}

	public String getReviewStatusName() {
		if(reviewStatus == 0){
			this.reviewStatusName = "申请/待风控审核";
		}else if(reviewStatus == 1){
			this.reviewStatusName = "风控同意";
		}else if(reviewStatus == 2){
			this.reviewStatusName = "风控拒绝";
		}
		return reviewStatusName;
	}

}
