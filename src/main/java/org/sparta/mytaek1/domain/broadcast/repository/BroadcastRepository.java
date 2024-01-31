package org.sparta.mytaek1.domain.broadcast.repository;

import org.sparta.mytaek1.domain.broadcast.entity.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {

    List<Broadcast> findAllByOnAirTrue();
}
