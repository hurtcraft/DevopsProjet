package client.notificationservice;

import client.notificationservice.controller.NotificationController;
import client.notificationservice.model.NotificationRequest;
import client.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSendNotification() throws Exception {

        NotificationRequest request =
                new NotificationRequest(
                        "TODO_CREATED",
                        1L,
                        "Nouvelle tâche créée"
                );

        String expectedResponse =
                "Event : TODO_CREATED | Todo ID : 1 | Message : Nouvelle tâche créée";

        when(notificationService.sendNotification(
                any(NotificationRequest.class)
        )).thenReturn(expectedResponse);

        mockMvc.perform(post("/notify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        ))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}