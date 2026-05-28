package client.notificationservice.controller;

import client.notificationservice.model.NotificationRequest;
import client.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> notify(@RequestBody NotificationRequest request) {

        String response = notificationService.sendNotification(request);

        return ResponseEntity.ok(response);
    }
}