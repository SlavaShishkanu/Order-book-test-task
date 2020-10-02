package com.juniorproject.orderbook.service;

import java.io.IOException;
import java.io.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.juniorproject.orderbook.OrderBookMain;

/**
 * Just a wrapper around Writer
 * 
 * @author Slava Shishkanu
 *
 */
public class MyWriter {
    private static Logger log = LoggerFactory.getLogger(MyWriter.class.getName());
    
    private Writer fileWriter;
    
    public MyWriter(Writer fileWriter) {
        this.fileWriter = fileWriter;
    }

    public void write(String output) {
        log.info("writing to file:{}", output);
        try {
            fileWriter.write(output);
        } catch (IOException e) {
            log.error("Error, writing to file", e);
            throw new MyWriterException("Error, writing to file", e);
        }
    }
    
}
