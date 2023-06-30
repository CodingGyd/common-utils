package com.codinggyd.entity;

import java.util.List;

/**
 * @ClassName Page
 * @Description TODO
 * @Author guoyading
 * @Date 2023/6/30 9:32
 * @Version 1.0
 */
public class Page<T> {
    int current;
    int size;
    int total;
    List<T> records;

    public int getCurrent() {
        return current;
    }

    public Page setCurrent(int current) {
        this.current = current;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Page setSize(int size) {
        this.size = size;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public Page setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<T> getRecords() {
        return records;
    }

    public Page setRecords(List<T> records) {
        this.records = records;
        return this;
    }
}
