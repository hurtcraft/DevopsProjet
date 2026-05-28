package client.notificationservice.service;


import client.notificationservice.model.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public String sendNotification(NotificationRequest request) {

        String result =
                "Event : " + request.getEvent()
                        + " | Todo ID : " + request.getTodoId()
                        + " | Message : " + request.getMessage();

        System.out.println(result);

        return result;
    }
}