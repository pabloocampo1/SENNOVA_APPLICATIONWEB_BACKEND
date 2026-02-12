package com.example.sennova.web.controllers;

import com.example.sennova.application.usecasesImpl.NotificationsService;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationsController {

    private final NotificationsService notificationsService;

    @Autowired
    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/getAll/{username}")
    public ResponseEntity<List<Notifications>> getNotifications(@PathVariable("username") String username){
        return new ResponseEntity<>(notificationsService.getAll(username),HttpStatus.OK);
    }
}
