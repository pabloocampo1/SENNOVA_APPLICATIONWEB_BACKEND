package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationsRepositoryJpa extends JpaRepository<Notifications, Long> {

    @Query("SELECT DISTINCT n FROM Notifications n JOIN n.tags t WHERE t IN :tags ORDER BY n.date DESC")
    List<Notifications> findByTagsIn(@Param("tags") List<String> tags);
    void deleteByDateBefore(LocalDateTime date);

}
