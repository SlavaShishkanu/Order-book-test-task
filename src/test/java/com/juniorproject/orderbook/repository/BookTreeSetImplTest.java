package com.juniorproject.orderbook.repository;

import com.juniorproject.orderbook.BaseTest;
import com.juniorproject.orderbook.entity.BookEntry;
import com.juniorproject.orderbook.entity.EntryType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookTreeSetImplTest extends BaseTest {

    private OrderBook bookTreeSet;

    @BeforeEach
    public void setUp() {
        super.setUp();
        bookTreeSet = BookTreeSetImpl.getInstance();
        bookTreeSet.addAll(entries);
    }
    
    @AfterEach
    public void tearDown() {
        bookTreeSet.clear();
    }
    
    @Test
    public void update_ShouldAddNewEntry_whenEntryNotExist() {
        final BookEntry bookEntry = new BookEntry(50, 20, EntryType.ASK);
        bookTreeSet.update(bookEntry);
        assertTrue(bookTreeSet.getEntries().stream().anyMatch(el -> el.equals(bookEntry)));
    }

    @Test
    public void update_ShouldReplaceEntry_whenEntryAlreadyExists() {
        final BookEntry bookEntry = new BookEntry(99, 20, EntryType.ASK);
        bookTreeSet.update(bookEntry);
        assertTrue(bookTreeSet.getEntries().stream().anyMatch(el -> el.equals(bookEntry)));
    }

    @Test
    public void update_ShouldNotCreateDuplicate_whenEntryAlreadyExists() {
        bookTreeSet.update(new BookEntry(99, 20, EntryType.ASK));
        assertEquals(1,
                bookTreeSet.getEntries().stream().filter(entry -> entry.getPrice() == 99)
                    .count());
    }

    @Test
    public void bestBid_shouldReturnValidBid() {
        assertEquals(new BookEntry(95, 40, EntryType.BID), bookTreeSet.bestBid());
    }

    @Test
    public void bestBid_shouldReturnSameBid_whenZeroSizeBidsInserted() {
        bookTreeSet.update(new BookEntry(96, 0, EntryType.BID));
        bookTreeSet.update(new BookEntry(94, 0, EntryType.BID));
        assertEquals(new BookEntry(95, 40, EntryType.BID), bookTreeSet.bestBid());
    }

    @Test
    public void bestBid_shouldReturnNewHighestBid_whenBiggerBidInserted() {
        bookTreeSet.update(new BookEntry(96, 1, EntryType.BID));
        assertEquals(new BookEntry(96, 1, EntryType.BID), bookTreeSet.bestBid());
    }

    @Test
    public void bestBid_shouldUpdateHighestBidEntry_whenEntryWasUpdated() {
        bookTreeSet.update(new BookEntry(95, 1, EntryType.BID));
        assertEquals(new BookEntry(95, 1, EntryType.BID), bookTreeSet.bestBid());
    }

    @Test
    public void bestAsk_shouldReturnValidBestAsk() {
        assertEquals(new BookEntry(98, 50, EntryType.ASK), bookTreeSet.bestAsk());
    }

    @Test
    public void bestAsk_shouldReturnSameAsk_whenZeroAskInserted() {
        bookTreeSet.update(new BookEntry(99, 0, EntryType.ASK));
        bookTreeSet.update(new BookEntry(97, 0, EntryType.ASK));
        assertEquals(new BookEntry(98, 50, EntryType.ASK), bookTreeSet.bestAsk());
    }
    
    @Test
    public void bestAsk_shouldNotUpdateAsk_whenNonZeroAskInserted() {
        bookTreeSet.update(new BookEntry(97, 1, EntryType.ASK));
        assertEquals(new BookEntry(97, 1, EntryType.ASK), bookTreeSet.bestAsk());
    }
    
    @Test
    public void getEntryByPrice_shouldReturnValidEntry() {
        assertEquals(new BookEntry(95, 40, EntryType.BID), bookTreeSet.getEntryByPrice(95));
    }

    @Test
    public void getEntryByPrice_shouldReturnNerZeroSizeEntry_whenEntryNotExist() {
        assertEquals(100500, bookTreeSet.getEntryByPrice(100500).getPrice());
        assertEquals(0, bookTreeSet.getEntryByPrice(100500).getSize());
    }

    @Test
    public void orderSell_noChangesInBids_whenZeroSize() {
        bookTreeSet.orderSell(0);
        assertEquals(147, bookTreeSet.getEntries()
            .stream()
            .filter(entry -> entry.getType() == EntryType.BID)
            .reduce(0, (res, entry2) -> res + entry2.getSize(), Integer::sum)
            .longValue()
        );
    }

    @Test
    public void orderSell_shouldRemoveFiveItemsFromBids() {
        bookTreeSet.orderSell(5);
        assertEquals(142, bookTreeSet.getEntries()
            .stream()
            .filter(entry -> entry.getType() == EntryType.BID)
            .reduce(0, (res, entry2) -> res + entry2.getSize(), Integer::sum)
            .longValue()
        );
    }

    @Test
    public void orderSell_shouldRemoveAllBids() {
        bookTreeSet.orderSell(147);
        assertEquals(0, bookTreeSet.getEntries()
            .stream()
            .filter(entry -> entry.getType() == EntryType.BID)
            .reduce(0, (res, entry2) -> res + entry2.getSize(), Integer::sum)
            .longValue()
        );
    }

    @Test
    public void orderSell_shouldThrowException_whenOrderIsTooBig() {
        assertThrows(BookException.class, () -> bookTreeSet.orderSell(149));
    }

    @Test
    public void orderSell_shouldStartFromBestBid() {
        bookTreeSet.orderSell(5);
        assertEquals(new BookEntry(95, 35, EntryType.BID), bookTreeSet.bestBid());
    }

    @Test
    public void orderBuy_ShouldNotChangeAsk_whenAmountIzZero() {
        bookTreeSet.orderBuy(0);
        assertEquals(50,
                bookTreeSet.getEntries().stream()
                    .filter(entry -> entry.getType() == EntryType.ASK)
                    .reduce(0, (res, entry2) -> res + entry2.getSize(), Integer::sum).longValue());
    }

    @Test
    public void orderBuy_AsksShouldChange_whenAmountIzNotZero() {
        bookTreeSet.orderBuy(5);
        assertEquals(45,
                bookTreeSet.getEntries().stream()
                    .filter(entry -> entry.getType() == EntryType.ASK)
                    .reduce(0, (res, entry2) -> res + entry2.getSize(), Integer::sum).longValue());
    }

    @Test
    public void orderBuy_BestAskShouldChange_whenAmountIzGreaterThanInBestAsk() {
        bookTreeSet.update(new BookEntry(99, 10, EntryType.ASK));
        bookTreeSet.orderBuy(55);
        final BookEntry bestAskAfterUpdate = new BookEntry(99, 5, EntryType.ASK);
        assertEquals(bestAskAfterUpdate, bookTreeSet.bestAsk());
    }

}
