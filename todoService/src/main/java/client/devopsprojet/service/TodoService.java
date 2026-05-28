package client.devopsprojet.service;

import client.devopsprojet.model.NotificationRequest;
import client.devopsprojet.model.Todo;

import java.util.List;
import client.devopsprojet.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final WebClient webClient;

    @Value("${notification.url}")
    private String NOTIFICATION_URL;
    public TodoService(
            TodoRepository todoRepository,
            WebClient webClient
    ) {
        this.todoRepository = todoRepository;
        this.webClient = webClient;
    }

    public Todo createTodo(Todo todo) {

        Todo saved = todoRepository.save(todo);

        sendNotification(
                "TODO_CREATED",
                saved.getId(),
                "Nouvelle tâche créée"
        );

        return saved;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(Long id) {

        return todoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Todo not found"));
    }

    public Todo updateTodo(Long id, Todo todo) {

        Todo existing = getTodoById(id);

        existing.setTitle(todo.getTitle());
        existing.setDescription(todo.getDescription());
        existing.setCompleted(todo.isCompleted());

        Todo updated = todoRepository.save(existing);

        sendNotification(
                "TODO_UPDATED",
                updated.getId(),
                "Tâche mise à jour"
        );

        return updated;
    }

    public void deleteTodo(Long id) {

        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found");
        }

        todoRepository.deleteById(id);

        sendNotification(
                "TODO_DELETED",
                id,
                "Tâche supprimée"
        );
    }

    public Todo markAsCompleted(Long id) {

        Todo todo = getTodoById(id);

        todo.setCompleted(true);

        Todo updated = todoRepository.save(todo);

        sendNotification(
                "TODO_COMPLETED",
                updated.getId(),
                "Tâche terminée"
        );

        return updated;
    }

    private void sendNotification(String event, Long todoId, String message) {
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