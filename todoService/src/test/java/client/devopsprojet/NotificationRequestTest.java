package client.devopsprojet;


import client.devopsprojet.model.NotificationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRequestTest {

    @Test
    void shouldUseNoArgsConstructorAndSettersGetters() {

        NotificationRequest request = new NotificationRequest();

        request.setEvent("EVENT_TEST");
        request.setTodoId(1L);
        request.setMessage("Test message");

        assertEquals("EVENT_TEST", request.getEvent());
        assertEquals(1L, request.getTodoId());
        assertEquals("Test message", request.getMessage());
    }

    @Test
    void shouldUseAllArgsConstructor() {

        NotificationRequest request =
                new NotificationRequest("EVENT_CREATE", 2L, "Created");

        assertEquals("EVENT_CREATE", request.getEvent());
        assertEquals(2L, request.getTodoId());
        assertEquals("Created", request.getMessage());
    }

    @Test
    void shouldAllowNullValues() {

        NotificationRequest request =
                new NotificationRequest(null, null, null);

        assertNull(request.getEvent());
        assertNull(request.getTodoId());
        assertNull(request.getMessage());
    }
}