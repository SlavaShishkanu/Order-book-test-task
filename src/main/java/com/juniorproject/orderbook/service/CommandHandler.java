package com.juniorproject.orderbook.service;

import com.juniorproject.orderbook.entity.BookEntry;
import com.juniorproject.orderbook.entity.EntryType;
import com.juniorproject.orderbook.repository.OrderBook;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses strings and executes commands.
 * Calls OrderBook and  writes output to MyWriter.
 * 
 * @author Slava Shishkanu
 *
 */

public class CommandHandler {
    
    private static final String TWO_COMMA_SEPARATED_VALUES_PATTERN = "%d,%d%n";

    private static final String DELIMETER = ",";
    
    private static Logger log = LoggerFactory.getLogger(CommandHandler.class.getName());

    private OrderBook book;

    private MyWriter myWriter;

    private Pattern pattern = Pattern.compile(DELIMETER);

    /**
     * @param book - OrderBook implementation
     * @param myWriter - class that writes output to file
     */
    public CommandHandler(final OrderBook book, final MyWriter myWriter) {
        this.book = book;
        this.myWriter = myWriter;
    }

    /**
     * Parses strings and executes commands. <br>
     * Can recognize following commands:
     * <p>
     * <code> u,*price*,*size*,bid  </code> - set bid size at *price* to *size* <br>
     * <code> u,*price*,*size*,ask  </code> - set ask size at *price* to *size* <br>
     * <code> q,best_bid              </code> - print best bid price and size     <br>
     * <code> q,best_ask              </code> - print best ask price and size     <br>
     * <code> q,size,*price*          </code> - print size at specified price     <br>
     * <code> o,buy,*size*            </code> - remove *size* shares out of asks, most cheap ones
     * <br>
     * <code> o,sell,*size*          </code> -removes *size* shares out of bids, most expensive ones.
     * </p>
     * @param command
     */
    public void handle(final String command) {
        log.info("received string:{}", command);
        final String[] splittedCommand = pattern.split(command.strip());
        final String commandAlias = splittedCommand[0];
        if ("u".equals(commandAlias)) {
            update(splittedCommand);
            return;
        }

        if ("q".equals(commandAlias)) {
            query(splittedCommand);
            return;
        }

        if ("o".equals(commandAlias)) {
            order(splittedCommand);
        }

    }

    private void order(final String[] splittedCommand) {
        final int size = Integer.parseInt(splittedCommand[2]);
        if (splittedCommand[1].equals("buy")) {
            log.info("order, buy {} shares", size);
            book.orderBuy(size);
            return;
        }
        if (splittedCommand[1].equals("sell")) {
            log.info("order, sell {} shares", size);
            book.orderSell(size);
        }

    }

    private void query(final String[] splittedCommand) {
        BookEntry bookEntry;
        if (splittedCommand[1].equals("best_bid")) {
            log.info("query, get best bid");
            bookEntry = book.bestBid();
            bookEntry.getPrice();
            bookEntry.getSize();
            myWriter.write(String.format(TWO_COMMA_SEPARATED_VALUES_PATTERN,
                    bookEntry.getPrice(), bookEntry.getSize()));
            return;
        }
        if (splittedCommand[1].equals("best_ask")) {
            log.info("query, get best ask%");
            bookEntry = book.bestAsk();

            myWriter.write(String.format(TWO_COMMA_SEPARATED_VALUES_PATTERN,
                    bookEntry.getPrice(), bookEntry.getSize()));

            return;
        }
        if (splittedCommand[1].equals("size")) {
            final int price = Integer.parseInt(splittedCommand[2]);
            log.info("query, get size at price={}", price);
            bookEntry = book.getEntryByPrice(price);
            myWriter.write(String.format("%d%n", bookEntry.getSize()));
        }

    }

    private void update(final String[] splittedCommand) {
        final int price = Integer.parseInt(splittedCommand[1]);
        final int size = Integer.parseInt(splittedCommand[2]);
        final EntryType entryType = EntryType.valueOfByAlias(splittedCommand[3]);

        final BookEntry bookEntry = new BookEntry(price, size, entryType);
        log.info("update, new BookEntry={}", bookEntry);
        book.update(bookEntry);

    }
}
