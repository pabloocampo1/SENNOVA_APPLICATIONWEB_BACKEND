package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.port.NotificationsPort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.persistence.repositoryJpa.NotificationsRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NotificationsAdapterImpl implements NotificationsPort {

    private final NotificationsRepositoryJpa notificationsRepositoryJpa;

    @Autowired
    public NotificationsAdapterImpl(NotificationsRepositoryJpa notificationsRepositoryJpa) {
        this.notificationsRepositoryJpa = notificationsRepositoryJpa;
    }

    @Override
    public Notifications save(Notifications notifications) {
        return this.notificationsRepositoryJpa.save(notifications);
    }

    @Override
    public List<Notifications> findAllByTargetRoleAndOrderByDateDesc(List<String> tags) {
        return this.notificationsRepositoryJpa.findByTagsIn(tags);
    }

    @Override
    public void deleteByDateBefore(LocalDateTime date) {
        this.notificationsRepositoryJpa.deleteByDateBefore(date);
    }


    @Override
    public List<Notifications> getAll() {
        return this.notificationsRepositoryJpa.findAll();
    }

}
