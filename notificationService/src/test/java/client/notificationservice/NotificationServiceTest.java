package client.notificationservice;

import client.notificationservice.model.NotificationRequest;
import client.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;




class NotificationServiceTest {

    private final NotificationService notificationService =
            new NotificationService();

    @Test
    void shouldReturnFormattedNotificationMessage() {

        NotificationRequest request =
                new NotificationRequest(
                        "TODO_CREATED",
                        1L,
                        "Nouvelle tâche créée"
                );

        String result = notificationService.sendNotification(request);

        assertEquals(
                "Event : TODO_CREATED | Todo ID : 1 | Message : Nouvelle tâche créée",
                result
        );
    }

    @Test
    void shouldHandleNullValues() {

        NotificationRequest request =
                new NotificationRequest(null, null, null);

        String result = notificationService.sendNotification(request);

        assertEquals(
                "Event : null | Todo ID : null | Message : null",
                result
        );
    }

    @Test
    void shouldHandleEmptyStrings() {

        NotificationRequest request =
                new NotificationRequest("", 1L, "");

        String result = notificationService.sendNotification(request);

        assertEquals(
                "Event :  | Todo ID : 1 | Message : ",
                result
        );
    }

    @Test
    void shouldHandlePartialData() {

        NotificationRequest request =
                new NotificationRequest("TODO_UPDATED", null, "Updated");

        String result = notificationService.sendNotification(request);

        assertEquals(
                "Event : TODO_UPDATED | Todo ID : null | Message : Updated",
                result
        );
    }

    @Test
    void shouldBeDeterministic() {

        NotificationRequest request =
                new NotificationRequest("X", 99L, "Y");

        String r1 = notificationService.sendNotification(request);
        String r2 = notificationService.sendNotification(request);

        assertEquals(r1, r2);
    }
}