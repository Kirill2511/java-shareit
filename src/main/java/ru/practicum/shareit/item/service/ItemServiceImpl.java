package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        User owner = userService.getUserById(ownerId);

        Item item = ItemMapper.toItem(itemDto, owner);
        Item savedItem = itemRepository.save(item);

        log.info("Created item with id: {} for owner: {}", savedItem.getId(), ownerId);
        return ItemMapper.toDto(savedItem);
    }

    @Override
    public ItemDto getById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        return ItemMapper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getByOwnerId(Long ownerId) {
        userService.getUserById(ownerId); // validates owner exists

        return itemRepository.findByOwnerId(ownerId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        userService.getUserById(ownerId); // validates owner exists

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        // Check if the user is the owner
        if (existingItem.getOwner() == null || !existingItem.getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Only the owner can edit the item");
        }

        // Partial update - only update non-null fields
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.update(existingItem);
        log.info("Updated item with id: {}", itemId);
        return ItemMapper.toDto(updatedItem);
    }

    @Override
    public void delete(Long ownerId, Long itemId) {
        userService.getUserById(ownerId); // validates owner exists

        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        if (existingItem.getOwner() == null || !existingItem.getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Only the owner can delete the item");
        }

        itemRepository.deleteById(itemId);
        log.info("Deleted item with id: {}", itemId);
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return java.util.Collections.emptyList();
        }

        return itemRepository.search(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
