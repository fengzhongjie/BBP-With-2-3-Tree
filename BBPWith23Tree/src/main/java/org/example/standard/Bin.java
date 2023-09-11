package org.example.standard;

import java.util.ArrayList;
import java.util.List;

public class Bin {
    private int capacity;
    private List<Integer> items;

    public Bin(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public boolean addItem(int itemSize) {
        if (getRemainingCapacity() >= itemSize) {
            items.add(itemSize);
            return true;
        }
        return false;
    }

    public int getRemainingCapacity() {
        int usedCapacity = items.stream().mapToInt(Integer::intValue).sum();
        return capacity - usedCapacity;
    }
}