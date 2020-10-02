package com.juniorproject.orderbook.repository;

import com.juniorproject.orderbook.entity.BookEntry;
import java.util.List;

/**
 * Limit order book interface.
 *  
 * @author Slava Shishkanu
 *
 */
public interface OrderBook {
    
    /**
     * Put item to Book, existing item with same price will be overwritten.
     * 
     * @param  BookEntry
     */
    void update(BookEntry bookEntry);

    /**
     * gives biggest non-zero bid item.
     * returns empty BookEntry with zero price if no bids found
     * @return {@link BookEntry  BookEntry}
     */
    BookEntry bestBid();
    
    /**
     * Returns smallest non-zero ask item.
     * returns empty BookEntry with zero price if no asks found
     * @return {@link BookEntry  BookEntry}
     */
    BookEntry bestAsk();

    /**
     * @param price - integer greater than zero
     * @return {@link BookEntry  BookEntry}
     */
    BookEntry getEntryByPrice(int price);

    /**
     * remove shares out of asks, most cheap ones.
     * @param size - integer greater than zero
     * @throws BookException if no more asks available
     */
    void orderBuy(int size);

    /**
     * Removes *size* shares out of bids, most expensive ones.
     * @param size - integer greater than zero
     * @throws BookException if no more bids available
     */
    void orderSell(int size);

    /**
     * Clears all entries.
     * used only in tests
     */
    void clear();

    /**
     * Retrieves all entries, sorted by price in descending order.
     * used only in tests
     */
    List<BookEntry> getEntries();

    /**
     * Adds all entries in one batch.
     * used in tests
     * @param entries
     */
    void addAll(List<BookEntry> entries);
}
