package com.example.sennova.application.event.handler;

import com.example.sennova.application.usecases.TestRequestUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.event.AnalysisResultSavedEvent;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.SampleAnalysisModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.domain.port.TestRequestPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.SampleAnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.analysisRequestsEntities.TestRequestEntity;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


// This handler is executed when an analysis result is recorded or issued
// with the purpose of taking the test request and checking whether all analyses of all samples are completed
// in order to update the test request's status and send notifications.

@Component
public class CheckAnalysisReadyHandler {

   private final TestRequestPersistencePort testRequestPersistencePort;
   private final NotificationsService notificationsService;
   private final TestRequestEmailService testRequestEmailService;
   private final UserUseCase userUseCase;

   @Autowired
    public CheckAnalysisReadyHandler(TestRequestPersistencePort testRequestPersistencePort, NotificationsService notificationsService, TestRequestEmailService testRequestEmailService, UserUseCase userUseCase) {
        this.testRequestPersistencePort = testRequestPersistencePort;
       this.notificationsService = notificationsService;
       this.testRequestEmailService = testRequestEmailService;
       this.userUseCase = userUseCase;
   }


    @Async
    @EventListener
    public void handle(AnalysisResultSavedEvent analysisResultSavedEvent){
        // get the test requests
        TestRequestEntity testRequest = this.testRequestPersistencePort.getWithSamplesAndAnalysis(analysisResultSavedEvent.getRequestCode())
                .orElseThrow();

        // recorremos la lista para el conteo
        boolean allAnalysisDone = testRequest.getSampleEntityList().stream()
                .allMatch(sample ->
                        sample.getAnalysisEntities().stream()
                                .allMatch(SampleAnalysisEntity::isStateResult)
                );

        if(allAnalysisDone){
            testRequest.setDeliveryStatus(TestRequestConstants.COMPLETED);
            this.testRequestPersistencePort.saveEntity(testRequest);

            Notifications notifications = new Notifications();
            notifications.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN));
            notifications.setType(TypeNotifications.TEST_REQUEST_COMPLETED);
            notifications.setActorUser("Sistema");
            notifications.setMessage("Se completaron todos los análisis del ensayo " + testRequest.getRequestCode());

            this.notificationsService.saveNotification(notifications);

             // get all user to send the email
            List<UserModel> users = this.userUseCase.findAllModels();
            users.stream()
                    .filter(UserModel::getAvailable)
                    .filter(UserModel::isNotifyResults)
                    .forEach(user -> this.testRequestEmailService.sendNotificationTestRequestCompleted(testRequest.getRequestCode(), user.getEmail(), user.getName()));
        }
        
    }
}
