package com.juniorproject.orderbook.service;

import java.io.IOException;

@SuppressWarnings("serial")
public class MyWriterException extends RuntimeException {

    public MyWriterException(String string, IOException e) {
        super(string, e);
    }

}
