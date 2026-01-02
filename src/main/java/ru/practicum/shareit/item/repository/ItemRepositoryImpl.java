package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Long>> ownerIndex = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public Item save(Item item) {
        item.setId(currentId++);
        items.put(item.getId(), item);

        // Update owner index
        Long ownerId = item.getOwner() != null ? item.getOwner().getId() : null;
        if (ownerId != null) {
            ownerIndex.computeIfAbsent(ownerId, k -> new ArrayList<>())
                    .add(item.getId());
        }

        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Collection<Item> findByOwnerId(Long ownerId) {
        List<Long> itemIds = ownerIndex.getOrDefault(ownerId, new ArrayList<>());
        return itemIds.stream()
                .map(items::get)
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteById(Long id) {
        Item item = items.remove(id);
        if (item != null && item.getOwner() != null) {
            List<Long> ownerItems = ownerIndex.get(item.getOwner().getId());
            if (ownerItems != null) {
                ownerItems.remove(id);
            }
        }
    }

    @Override
    public boolean existsById(Long id) {
        return items.containsKey(id);
    }

    @Override
    public Collection<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> item.getName().toLowerCase().contains(searchText) ||
                        item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
}
