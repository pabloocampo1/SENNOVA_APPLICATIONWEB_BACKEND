package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.NotificationsPort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationsService {

    private final UserUseCase userUseCase;
    private final NotificationsPort notificationsPort;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationsService(UserUseCase userUseCase, NotificationsPort notificationsPort, SimpMessagingTemplate messagingTemplate) {
        this.userUseCase = userUseCase;
        this.notificationsPort = notificationsPort;
        this.messagingTemplate = messagingTemplate;
    }

    public void saveNotification(Notifications notifications) {
        Notifications notification = this.notificationsPort.save(notifications);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    @Transactional
    public void deleteByDateBefore() {
        LocalDateTime limitDate = LocalDate.now().minusDays(30).atStartOfDay();
        this.notificationsPort.deleteByDateBefore(limitDate);
    }

    public List<Notifications> getAll(String userId) {

        try {
            UserModel user = this.userUseCase.findByUsername(userId);
            String role = user.getRole().getNameRole();

            List<String> tag = new ArrayList<>();
            tag.add(role);
            List<Notifications> notifications = this.notificationsPort.findAllByTargetRoleAndOrderByDateDesc(tag);

            return notifications;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
