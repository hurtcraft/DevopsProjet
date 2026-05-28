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
    private final SendNotificationService sendNotificationService;


    public TodoService(TodoRepository todoRepository, SendNotificationService sendNotificationService) {
        this.todoRepository = todoRepository;
        this.sendNotificationService = sendNotificationService;
    }

    public Todo createTodo(Todo todo) {

        Todo saved = todoRepository.save(todo);

        sendNotificationService.sendNotification(
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

        sendNotificationService.sendNotification(
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

        sendNotificationService.sendNotification(
                "TODO_DELETED",
                id,
                "Tâche supprimée"
        );
    }

    public Todo markAsCompleted(Long id) {

        Todo todo = getTodoById(id);

        todo.setCompleted(true);

        Todo updated = todoRepository.save(todo);

        sendNotificationService.sendNotification(
                "TODO_COMPLETED",
                updated.getId(),
                "Tâche terminée"
        );

        return updated;
    }


}