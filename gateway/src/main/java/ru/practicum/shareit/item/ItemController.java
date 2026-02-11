package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody ItemDto itemDto) {
        log.info("Gateway: POST /items - Creating item for user: {}", userId);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
            @RequestHeader(value = USER_ID_HEADER, required = false) Long userId) {
        log.info("Gateway: GET /items/{} - Getting item by id", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwner(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Gateway: GET /items - Getting all items for user: {}", userId);
        return itemClient.getItemsByOwner(userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto) {
        log.info("Gateway: PATCH /items/{} - Updating item for user: {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId) {
        log.info("Gateway: DELETE /items/{} - Deleting item for user: {}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(defaultValue = "") String text) {
        log.info("Gateway: GET /items/search?text={} - Searching items", text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentDto commentDto) {
        log.info("Gateway: POST /items/{}/comment - User {} adding comment", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
