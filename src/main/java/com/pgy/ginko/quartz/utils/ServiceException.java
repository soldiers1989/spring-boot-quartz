package com.pgy.ginko.quartz.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * For handler not expected status
 *
 * @author ginko
 * @date 2017-07-11 12:18
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceException extends Exception {

    private static final long serialVersionUID = 3084168374922475640L;

    public ServiceException(String msg, Exception e) {
        super(msg + "\n" + e);
    }

    public ServiceException(String msg) {
        super(msg);
    }
}
