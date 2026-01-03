package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long ownerId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("POST /items - Creating item: {} for user: {}", itemDto.getName(), ownerId);
        return itemService.create(ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("GET /items/{} - Getting item by id", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getByOwner(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info("GET /items - Getting all items for owner: {}", ownerId);
        return itemService.getByOwnerId(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long ownerId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("PATCH /items/{} - Updating item for user: {}", itemId, ownerId);
        return itemService.update(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(USER_ID_HEADER) Long ownerId,
                       @PathVariable Long itemId) {
        log.info("DELETE /items/{} - Deleting item for user: {}", itemId, ownerId);
        itemService.delete(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(defaultValue = "") String text) {
        log.info("GET /items/search?text={} - Searching items", text);
        return itemService.search(text);
    }
}
