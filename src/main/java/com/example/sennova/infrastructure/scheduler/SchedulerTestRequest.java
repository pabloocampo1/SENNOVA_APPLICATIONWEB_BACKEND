package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.application.usecases.TestRequest.TestRequestUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TestRequestConstants;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.model.testRequest.TestRequestModel;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.restTemplate.TestRequestEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SchedulerTestRequest {

    private final TestRequestUseCase testRequestUseCase;
    private final TestRequestEmailService testRequestEmailService;
    private final NotificationsService notificationsService;
    private final UserUseCase userUseCase;

    @Autowired
    public SchedulerTestRequest(TestRequestUseCase testRequestUseCase, TestRequestEmailService testRequestEmailService, NotificationsService notificationsService, UserUseCase userUseCase) {
        this.testRequestUseCase = testRequestUseCase;
        this.testRequestEmailService = testRequestEmailService;
        this.notificationsService = notificationsService;
        this.userUseCase = userUseCase;
    }

    // This scheduler retrieves all test requests whose due date is for today
    // and sends the corresponding notifications.
    @Scheduled(cron = "0 10 0 * * *")
    @Transactional
    public void sendNotificationTestRequestDueDate(){

       try{
           List<TestRequestModel>  testRequest = this.testRequestUseCase.getTestRequestsDueToday();

           if(!testRequest.isEmpty()){

               Notifications notifications = new Notifications();
               notifications.setMessage("Hay "+testRequest.size() + " Ensayos para entregar hoy");
               notifications.setActorUser("Sistema");
               notifications.setType(TypeNotifications.DUE_DATE_TEST_REQUESTS);
               notifications.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN, RoleConstantsNotification.ROLE_ANALYST));

               this.notificationsService.saveNotification(notifications);


               List<UserModel> userAvailableAndWithEmailNotificationActive =
                       this.userUseCase.findAllModels()
                               .stream()
                               .filter(UserModel::getAvailable)
                               .filter(UserModel::isNotifyResults)
                               .toList();

               userAvailableAndWithEmailNotificationActive.forEach(user -> this.testRequestEmailService.sendEmailTestRequestDueDate(testRequest, user.getEmail(), user.getName()));
           }
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }


    // if one test request are expired, change the status delivery
   @Scheduled(cron = "0 20 0 * * *")
    @Transactional
    public void changeDeliveryStatus(){
        List<TestRequestModel> testRequestModels = this.testRequestUseCase.getTestRequestDueDateExpired();

        if(!testRequestModels.isEmpty()){
            testRequestModels.forEach(test -> {
                // if the test request was delivered not change the status delivery
               if(!test.getDeliveryStatus().equalsIgnoreCase(TestRequestConstants.DELIVERED_AND_FINISHED)){
                   test.setDeliveryStatus(TestRequestConstants.EXPIRED);
                   this.testRequestUseCase.updateStatus(test);
               }
            });
        }
    }
}
