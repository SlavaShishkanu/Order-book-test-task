package com.juniorproject.orderbook.entity;

public class BookEntry {

    private final int price;

    private int size;

    private EntryType type;

    public BookEntry(int price) {
        this.price = price;
    }

    public BookEntry(int price, int size, EntryType type) {
        this.price = price;
        this.size = size;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Entry [price=" + price + ", size=" + size + ", type=" + type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + price;
        result = prime * result + size;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookEntry other = (BookEntry) obj;
        if (price != other.price)
            return false;
        if (size != other.size)
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
