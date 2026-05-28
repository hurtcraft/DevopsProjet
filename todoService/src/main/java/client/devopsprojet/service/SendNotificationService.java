package client.devopsprojet.service;

import client.devopsprojet.model.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SendNotificationService {
    @Value("${notification.url}")
    private String NOTIFICATION_URL;
    private final WebClient webClient;

    public SendNotificationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void sendNotification(String event, Long todoId, String message) {
        try {
            NotificationRequest request = new NotificationRequest(event, todoId, message);

            webClient.post()
                    .uri(NOTIFICATION_URL)
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {

            System.err.println(
                    "Notification service unavailable : "
                            + e.getMessage()
            );
        }
    }
}
