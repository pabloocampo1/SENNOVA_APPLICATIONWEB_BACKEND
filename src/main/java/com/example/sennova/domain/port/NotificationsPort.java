package com.example.sennova.domain.port;

import com.example.sennova.infrastructure.persistence.entities.Notifications;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationsPort {

    Notifications save(Notifications notifications);
    List<Notifications> findAllByTargetRoleAndOrderByDateDesc(List<String> tags);
    void deleteByDateBefore(LocalDateTime limitDay);
    List<Notifications> getAll();
}
