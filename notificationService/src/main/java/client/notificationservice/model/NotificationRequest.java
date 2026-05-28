package client.notificationservice.model;


public class NotificationRequest {

    private String event;

    private Long todoId;

    private String message;

    public NotificationRequest() {
    }

    public NotificationRequest(String event, Long todoId, String message) {
        this.event = event;
        this.todoId = todoId;
        this.message = message;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}