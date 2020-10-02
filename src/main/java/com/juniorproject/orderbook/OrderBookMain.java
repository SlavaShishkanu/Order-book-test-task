package com.juniorproject.orderbook;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.juniorproject.orderbook.repository.BookTreeSetImpl;
import com.juniorproject.orderbook.service.CommandHandler;
import com.juniorproject.orderbook.service.MyWriter;
import com.juniorproject.orderbook.service.MyWriterException;

/**
 * Simple implementation of limit order book.
 *  
 * Default input file is input.txt
 * default output file is output.txt
 * 
 * They can be overridden by command line arguments, first argument - input file name (or path),
 * second command line argument - output file name (or path)
 * 
 * @author Slava Shishkanu
 *
 */
public class OrderBookMain {
    private static Logger log = LoggerFactory.getLogger(OrderBookMain.class.getName());
    
    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        String outputFilePath = "output.txt";
        
        if (args.length != 0) {
            inputFilePath = args[0];
            if  (args.length >= 2) {
                outputFilePath = args[1];
            }
        }
        log.info("input file is {}", inputFilePath);
        log.info("output file is {}", outputFilePath);
        
        final CommandHandler commandHandler;
        try (Stream<String> stream = Files.lines(Paths.get(inputFilePath));
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath),
                        StandardCharsets.UTF_8)) {
            final MyWriter myWriter = new MyWriter(writer);
            commandHandler = new CommandHandler(BookTreeSetImpl.getInstance(), myWriter);
            stream.forEach(commandHandler::handle);
        } catch (IOException e) {
            log.error("error while reading, looks like input file does not exist", e);
            throw new MyWriterException("error while reading, looks like input file does not exist", e);
        }
    }
}
