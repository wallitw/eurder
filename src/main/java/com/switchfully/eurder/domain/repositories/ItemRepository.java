package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Item> getItemByName(String name) {
        return getAllItems().stream()
                .filter(item -> item.getName().equals(name))
                .findFirst();
    }

    public Optional<Item> getItemById(String id) {
        return getAllItems().stream()
                .filter(item -> item.getItemId().equals(id))
                .findFirst();
    }
}
