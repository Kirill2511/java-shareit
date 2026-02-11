package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Long userId,
                                         @Valid @RequestBody BookingCreateDto bookingDto) {
        log.info("Gateway: POST /bookings - Creating booking for user: {}", userId);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        log.info("Gateway: PATCH /bookings/{} - User {} approving booking: {}", bookingId, userId, approved);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long bookingId) {
        log.info("Gateway: GET /bookings/{} - Getting booking for user: {}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBooker(@RequestHeader(USER_ID_HEADER) Long userId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        log.info("Gateway: GET /bookings?state={} - Getting bookings for booker: {}", state, userId);
        return bookingClient.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(@RequestHeader(USER_ID_HEADER) Long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        log.info("Gateway: GET /bookings/owner?state={} - Getting bookings for owner: {}", state, userId);
        return bookingClient.getBookingsByOwner(userId, state);
    }
}
