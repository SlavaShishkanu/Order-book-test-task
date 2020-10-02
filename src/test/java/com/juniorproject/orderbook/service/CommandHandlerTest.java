package com.juniorproject.orderbook.service;

import com.juniorproject.orderbook.entity.BookEntry;
import com.juniorproject.orderbook.entity.EntryType;
import com.juniorproject.orderbook.repository.OrderBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerTest {
    
    private CommandHandler commandHandler;
    
    @Mock
    private OrderBook orderBook;
    
    @Mock
    private MyWriter myWriter;
    
    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler(orderBook, myWriter);
    }

    @Test
    public void handle_update_shouldUpdateOrderBookWithBID() {
        commandHandler.handle("u,9,1,bid");
        Mockito.verify(orderBook).update(new BookEntry(9, 1, EntryType.BID));
    }

    @Test
    public void handle_update_shouldUpdateOrderBookWithASK() {
        commandHandler.handle("u,11,5,ask");
        Mockito.verify(orderBook).update(new BookEntry(11, 5, EntryType.ASK));
    }

    @Test
    public void handle_update_shouldUpdateOrderBookWithSPREAD() {
        commandHandler.handle("u,11,5,spread");
        Mockito.verify(orderBook).update(new BookEntry(11, 5, EntryType.SPREAD));
    }

    @Test
    public void handle_update_shouldIgnoreTailSpaces() {
        commandHandler.handle("u,11,5,ask  \n");
        Mockito.verify(orderBook).update(new BookEntry(11, 5, EntryType.ASK));
    }

    @Test
    public void handle_query_shouldRequestAndWriteBestBid() {
        when(orderBook.bestBid()).thenReturn(new BookEntry(2, 3, EntryType.BID));
        commandHandler.handle("q,best_bid");
        Mockito.verify(orderBook).bestBid();
        Mockito.verify(myWriter).write(String.format("%d,%d%n", 2, 3));
    }

    @Test
    public void handle_query_shouldRequestAndWriteBestAsk() {
        when(orderBook.bestAsk()).thenReturn(new BookEntry(3, 4, EntryType.ASK));
        commandHandler.handle("q,best_ask");
        Mockito.verify(orderBook).bestAsk();
        Mockito.verify(myWriter).write(String.format("%d,%d%n", 3, 4));
    }

    @Test
    public void handle_querySize_shouldRequestAndWriteSize() {
        when(orderBook.getEntryByPrice(10)).thenReturn(new BookEntry(10, 2, EntryType.ASK));
        commandHandler.handle("q,size,10");
        Mockito.verify(orderBook).getEntryByPrice(10);
        Mockito.verify(myWriter).write(String.format("%d%n", 2));
    }

    @Test
    public void handle_orderBuy_shouldCallOrderSell() {
        commandHandler.handle("o,sell,1");
        Mockito.verify(orderBook).orderSell(1);
    }

    @Test
    public void handle_orderBuy_shouldCallOrderBuy() {
        commandHandler.handle("o,buy,100500");
        Mockito.verify(orderBook).orderBuy(100500);
    }

}
