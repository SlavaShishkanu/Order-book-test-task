package com.juniorproject.orderbook;

import com.juniorproject.orderbook.entity.BookEntry;
import com.juniorproject.orderbook.entity.EntryType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected List<BookEntry> entries;
    
    @BeforeEach
    public void setUp() {
        entries = new ArrayList<>();
        entries.add(new BookEntry(99, 0, EntryType.valueOfByAlias("ask")));
        entries.add(new BookEntry(98, 50, EntryType.valueOfByAlias("ask")));
        entries.add(new BookEntry(97, 0, EntryType.valueOfByAlias("spread")));
        entries.add(new BookEntry(96, 0, EntryType.valueOfByAlias("spread")));
        entries.add(new BookEntry(95, 40, EntryType.valueOfByAlias("bid")));
        entries.add(new BookEntry(94, 30, EntryType.valueOfByAlias("bid")));
        entries.add(new BookEntry(93, 0, EntryType.valueOfByAlias("bid")));
        entries.add(new BookEntry(92, 77, EntryType.valueOfByAlias("bid")));
    }
    
}
