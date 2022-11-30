package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ItemRepository {

    private final List<Item> items = new ArrayList<>();

    public Item createItem(Item item) {
        items.add(item);
        return item;
    }

    public List<Item> getAllItems() {
        return items;
    }
}
