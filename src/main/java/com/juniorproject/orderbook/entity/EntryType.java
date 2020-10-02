package com.juniorproject.orderbook.entity;

import java.util.HashMap;
import java.util.Map;

public enum EntryType {
    
    ASK("ask"),
    BID("bid"),
    SPREAD("spread");
    
    private String alias;
    
    private static final Map<String, EntryType> aliasMap = new HashMap<>();  
    
    static  {
        for ( EntryType entryType : EntryType.values()) {
            aliasMap.put(entryType.alias,  entryType);
        }
    }

    private EntryType(String alias) {
        this.alias = alias;
    }
    
    public static EntryType valueOfByAlias(String alias) {
        EntryType entryType = aliasMap.get(alias);
        if (entryType == null) {
            throw new IllegalArgumentException(
                    "No enum alias " + EntryType.class.getCanonicalName() + "." + alias);
        }
        return entryType;
    }
    
}
