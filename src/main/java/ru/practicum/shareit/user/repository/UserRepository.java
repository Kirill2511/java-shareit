package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = :email and u.id <> :excludeId")
    Optional<User> findByEmailExcluding(@Param("email") String email, @Param("excludeId") Long excludeId);

    Optional<User> findByEmail(String email);
}
