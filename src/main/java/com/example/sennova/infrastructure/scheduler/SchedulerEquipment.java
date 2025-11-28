package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.restTemplate.EquipmentEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulerEquipment {

    private final EquipmentUseCase equipmentUseCase;
    private final UserUseCase userUseCase;
    private final EquipmentEmail equipmentEmail;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationsService notificationsService;

    @Autowired
    public SchedulerEquipment(EquipmentUseCase equipmentUseCase, UserUseCase userUseCase, EquipmentEmail equipmentEmail, SimpMessagingTemplate messagingTemplate, NotificationsService notificationsService) {
        this.equipmentUseCase = equipmentUseCase;
        this.userUseCase = userUseCase;
        this.equipmentEmail = equipmentEmail;
        this.messagingTemplate = messagingTemplate;
        this.notificationsService = notificationsService;
    }

    @Scheduled(cron = "0 42 12 * * ?", zone = "America/Bogota")
    public void checkMaintenanceEquipment() {
        List<EquipmentModel> equipments = this.equipmentUseCase.getAllEquipmentToMaintenance();
        if (equipments.isEmpty()) {
            return;
        }

        List<UserModel> users = this.userUseCase.findAllModels();

        users.stream()
                .filter(UserModel::isNotifyEquipment)
                .forEach(user -> {
                    String email = user.getEmail();
                    try {
                        this.equipmentEmail.sendEmailMaintenance(email, equipments);

                    } catch (Exception e) {
                        System.err.println("Error al enviar correo a " + email + ": " + e.getMessage());
                    }
                });


        if(equipments.size() <= 5 ){
            equipments.stream().forEach(equipment -> {
                Notifications notifications = new Notifications();
                notifications.setMessage("EL equipo " + equipment.getEquipmentName() + " tiene mantenimiento hoy.");
                notifications.setType(TypeNotifications.MAINTENANCE);
                notifications.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN));
                notifications.setActorUser("Sistema");
                notifications.setImageUser(null);

                this.notificationsService.saveNotification(notifications);

            });
        }else {
                Notifications notifications = new Notifications();
                notifications.setMessage("Hay " + equipments.size() + " equipos con mantenimiento registrado para hoy.");
                notifications.setType(TypeNotifications.MAINTENANCE);
                notifications.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN));
                notifications.setActorUser("Sistema");
                notifications.setImageUser(null);

                this.notificationsService.saveNotification(notifications);


        }

    }


}
