package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Все бронирования пользователя (как арендатора)
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    // Текущие бронирования пользователя
    @Query("select b from Booking b " +
            "where b.booker.id = :bookerId " +
            "and b.start <= :now " +
            "and b.end >= :now")
    List<Booking> findCurrentByBookerId(@Param("bookerId") Long bookerId,
                                        @Param("now") LocalDateTime now,
                                        Sort sort);

    // Прошедшие бронирования пользователя
    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime end, Sort sort);

    // Будущие бронирования пользователя
    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime start, Sort sort);

    // Бронирования пользователя по статусу
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    // Все бронирования для вещей владельца
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId")
    List<Booking> findByItemOwnerId(@Param("ownerId") Long ownerId, Sort sort);

    // Текущие бронирования для вещей владельца
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.start <= :now " +
            "and b.end >= :now")
    List<Booking> findCurrentByItemOwnerId(@Param("ownerId") Long ownerId,
                                           @Param("now") LocalDateTime now,
                                           Sort sort);

    // Прошедшие бронирования для вещей владельца
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.end < :now")
    List<Booking> findPastByItemOwnerId(@Param("ownerId") Long ownerId,
                                        @Param("now") LocalDateTime now,
                                        Sort sort);

    // Будущие бронирования для вещей владельца
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.start > :now")
    List<Booking> findFutureByItemOwnerId(@Param("ownerId") Long ownerId,
                                          @Param("now") LocalDateTime now,
                                          Sort sort);

    // Бронирования для вещей владельца по статусу
    @Query("select b from Booking b " +
            "where b.item.owner.id = :ownerId " +
            "and b.status = :status")
    List<Booking> findByItemOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                             @Param("status") BookingStatus status,
                                             Sort sort);

    // Последнее и следующее бронирование для вещи
    @Query("select b from Booking b " +
            "where b.item.id = :itemId " +
            "and b.status = :status " +
            "and b.start <= :now " +
            "order by b.start desc " +
            "limit 1")
    Booking findFirstLastBooking(@Param("itemId") Long itemId,
                                  @Param("status") BookingStatus status,
                                  @Param("now") LocalDateTime now);

    @Query("select b from Booking b " +
            "where b.item.id = :itemId " +
            "and b.status = :status " +
            "and b.start > :now " +
            "order by b.start asc " +
            "limit 1")
    Booking findFirstNextBooking(@Param("itemId") Long itemId,
                                  @Param("status") BookingStatus status,
                                  @Param("now") LocalDateTime now);

    // Батч-загрузка последних бронирований для нескольких вещей
    @Query("select b from Booking b " +
            "where b.item.id in :itemIds " +
            "and b.status = :status " +
            "and b.start <= :now " +
            "order by b.item.id, b.start desc")
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Long> itemIds,
                                           @Param("status") BookingStatus status,
                                           @Param("now") LocalDateTime now);

    // Батч-загрузка следующих бронирований для нескольких вещей
    @Query("select b from Booking b " +
            "where b.item.id in :itemIds " +
            "and b.status = :status " +
            "and b.start > :now " +
            "order by b.item.id, b.start asc")
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Long> itemIds,
                                           @Param("status") BookingStatus status,
                                           @Param("now") LocalDateTime now);

    // Проверка возможности оставить комментарий
    @Query("select count(b) > 0 from Booking b " +
            "where b.booker.id = :userId " +
            "and b.item.id = :itemId " +
            "and b.status = :status " +
            "and b.end < :now")
    boolean existsCompletedBooking(@Param("userId") Long userId,
                                   @Param("itemId") Long itemId,
                                   @Param("status") BookingStatus status,
                                   @Param("now") LocalDateTime now);
}
