package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.domain.constants.ReagentStateCons;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.restTemplate.ReagentEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchedulerReagent {

    private final ReagentUseCase reagentUseCase;
    private final UserUseCase userUseCase;
    private final ReagentEmail reagentEmail;
    private final NotificationsService notificationsService;

    @Autowired
    public SchedulerReagent(ReagentUseCase reagentUseCase, UserUseCase userUseCase, ReagentEmail reagentEmail, NotificationsService notificationsService) {
        this.reagentUseCase = reagentUseCase;
        this.userUseCase = userUseCase;
        this.reagentEmail = reagentEmail;
        this.notificationsService = notificationsService;
    }

    @Scheduled(cron = "0 40 11 * * ?", zone = "America/Bogota")
    public void checkAndChangeStateExpirationAndQuantity() {
        List<ReagentModel> reagentModels = this.reagentUseCase.getAllExpired();

        List<ReagentModel> listUpdate = reagentModels.stream().map(reagentModel -> {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_EXPIRED);
            this.reagentUseCase.saveDirect(reagentModel);
            return reagentModel;
        }).toList();

    }

    @Scheduled(cron = "0 29 13 * * ?", zone = "America/Bogota")
    public void sendNotificationExpiration() {
        List<ReagentModel> reagentExpiredList = this.reagentUseCase.getAllExpired();
        if (reagentExpiredList.isEmpty()) {
            return;
        }

        List<UserModel> users = this.userUseCase.findAllModels();

        users.stream()
                .filter(UserModel::getAvailable)
                .filter(UserModel::isNotifyReagents)
                .forEach(userModel -> {
                    String emailUser = userModel.getEmail();

                    try {
                        this.reagentEmail.sendEmailExpiration(emailUser, reagentExpiredList);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        reagentExpiredList.stream().forEach(reagent -> {
            Notifications notifications = new Notifications();
            notifications.setMessage("EL reactivo " + reagent.getReagentName() + " caduco hoy.");
            notifications.setType(TypeNotifications.EXPIRED);
            notifications.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN, RoleConstantsNotification.ROLE_ANALYSIS));
            notifications.setActorUser("Sistema");
            notifications.setImageUser(null);

            this.notificationsService.saveNotification(notifications);

        });


    }
}
