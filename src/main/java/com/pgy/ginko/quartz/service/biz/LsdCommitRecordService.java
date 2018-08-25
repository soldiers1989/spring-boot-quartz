package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.common.enums.YesNoStatus;
import com.pgy.ginko.quartz.model.biz.LsdCommitRecordDo;

/**
 * @author ginko
 */
public interface LsdCommitRecordService {

    LsdCommitRecordDo addRecord(Integer type, String relateId, String content, String url, YesNoStatus yesNoStatus);

}