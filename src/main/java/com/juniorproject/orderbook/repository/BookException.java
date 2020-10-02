package com.juniorproject.orderbook.repository;

@SuppressWarnings("serial")
public class BookException extends RuntimeException {

    public BookException(String format) {
        super(format);
    }

}
