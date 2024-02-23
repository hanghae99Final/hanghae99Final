package org.sparta.mytaek1.domain.broadcast.repository;

import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
    @Query("SELECT b FROM Broadcast b LEFT JOIN FETCH b.product LEFT JOIN FETCH b.user WHERE b.broadcastId = :broadcastId")
    Optional<Broadcast> findByBroadcastId(Long broadcastId);

    List<Broadcast> findAllByOnAirTrue();

    Broadcast findByBroadcastTitle(String testTitle);

    @Query("SELECT b FROM Broadcast b LEFT JOIN FETCH b.product p WHERE b.user.userId = :userId")
    List<Broadcast> findAllByUserUserId(Long userId);
}
