package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerNotifications {

    private final NotificationsService notificationsService;

    @Autowired
    public SchedulerNotifications(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @Scheduled(cron = "0 40 11 * * ?", zone = "America/Bogota")
    public void deleteNotificationExpired() {
        this.notificationsService.deleteByDateBefore();
    }


}
