package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto create(Long ownerId, ItemDto itemDto);

    ItemDto getById(Long id);

    Collection<ItemDto> getByOwnerId(Long ownerId);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    void delete(Long ownerId, Long itemId);

    Collection<ItemDto> search(String text);
}
