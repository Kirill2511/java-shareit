package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findById(Long id);

    Collection<Item> findAll();

    Collection<Item> findByOwnerId(Long ownerId);

    Item update(Item item);

    void deleteById(Long id);

    boolean existsById(Long id);

    Collection<Item> search(String text);
}
