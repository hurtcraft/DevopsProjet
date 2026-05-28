package client.notificationservice;

import client.notificationservice.model.NotificationRequest;
import client.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationServiceTest {

    private final NotificationService notificationService =
            new NotificationService();

    @Test
    void shouldReturnNotificationMessage() {

        NotificationRequest request =
                new NotificationRequest(
                        "TODO_CREATED",
                        1L,
                        "Nouvelle tâche créée"
                );

        String result =
                notificationService.sendNotification(request);

        assertEquals(
                "Event : TODO_CREATED | Todo ID : 1 | Message : Nouvelle tâche créée",
                result
        );
    }
}