package com.juniorproject.orderbook.repository;

import com.juniorproject.orderbook.entity.BookEntry;
import com.juniorproject.orderbook.entity.EntryType;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple TreeSet implementation of limit order book.
 * 
 * @author Slava Shishkanu
 *
 */
public final class BookTreeSetImpl implements OrderBook {
    
    private static Logger log = LoggerFactory.getLogger(BookTreeSetImpl.class.getName());
    
    private NavigableSet<BookEntry> entries;
    
    private BookTreeSetImpl() {
        entries = new TreeSet<>(Comparator.comparing(BookEntry::getPrice).reversed());
    }

    public static BookTreeSetImpl getInstance() {
        return BookTreeSetHolder.BOOK_TREE_SET_INSTANCE;
    }

    @Override
    public List<BookEntry> getEntries() {
        return entries.stream().collect(Collectors.toList());
    }

    @Override
    public void addAll(final List<BookEntry> entriesReceived) {
        entriesReceived.forEach(this::update);
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public void update(final BookEntry bookEntry) {
        if (entries.contains(bookEntry)) {
            entries.remove(bookEntry);
        }
        entries.add(bookEntry);
    }

    @Override
    public BookEntry bestBid() {
        return entries.stream()
                .filter(entry -> (entry.getType() == EntryType.BID) && (entry.getSize() != 0))
                .findFirst().orElse(new BookEntry(0));
    }

    @Override
    public BookEntry bestAsk() {
        return entries.stream()
                .filter(entry -> (entry.getType() == EntryType.ASK) && (entry.getSize() != 0))
                .sorted(Comparator.comparing(BookEntry::getPrice)).findFirst()
                .orElse(new BookEntry(0));
    }

    @Override
    public BookEntry getEntryByPrice(final int price) {
        final BookEntry entry = new BookEntry(price);
        if (entries.contains(entry)) {
            return entries.tailSet(new BookEntry(price)).first();
        } else {
            return entry;
        }
    }

    @Override
    public void orderBuy(final int size) {
        log.info("buy {} shares", size);
        final List<BookEntry> selectedAsks = entries
                .descendingSet()
                .tailSet(bestAsk())
                .stream()
                .collect(Collectors.toList());

        removeFromFirstShares(selectedAsks, size);
    }

    @Override
    public void orderSell(final int size) {
        final List<BookEntry> selectedBids = entries.tailSet(bestBid()).stream()
                .collect(Collectors.toList());
        removeFromFirstShares(selectedBids, size);
    }

    private void removeFromFirstShares(final List<BookEntry> selectedShares, final int size) {
        log.info("remove from shares {}", size);
        int sizeLeft = size;
        for (final BookEntry entry : selectedShares) {
            if (sizeLeft == 0) {
                break;
            }
            log.info("updating entry={}, size left={}", entry, sizeLeft);
            if (sizeLeft < entry.getSize()) {
                entry.setSize(entry.getSize() - sizeLeft);
                sizeLeft = 0;
            } else {
                sizeLeft -= entry.getSize();
                entry.setSize(0);
            }
            log.info("updated entry={}, size left={}", entry, sizeLeft);
        }
        if (sizeLeft != 0) {
            if (selectedShares.get(0).getType() == EntryType.BID) {
                log.error("cannot sell {} shares, no more bids available, {} not sold left ", size, sizeLeft);
                throw new BookException(String.format("cannot sell %s shares, no more bids available", size));
            }
            if (selectedShares.get(0).getType() == EntryType.ASK) {
                log.error("cannot buy {} shares, no more asks available, {} not bought", size, sizeLeft);
                throw new BookException(String.format("cannot buy %s shares, no more asks available", size));
            }
        }
    }

    private static class BookTreeSetHolder {
        public static final BookTreeSetImpl BOOK_TREE_SET_INSTANCE = new BookTreeSetImpl();
    }

}
